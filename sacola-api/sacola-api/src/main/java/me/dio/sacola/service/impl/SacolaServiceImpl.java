package me.dio.sacola.service.impl;

import lombok.RequiredArgsConstructor;
import me.dio.sacola.enumeration.FormaPagamento;
import me.dio.sacola.model.Item;
import me.dio.sacola.model.Produto;
import me.dio.sacola.model.Restaurante;
import me.dio.sacola.model.Sacola;
import me.dio.sacola.repository.ItemRepository;
import me.dio.sacola.repository.ProdutoRepository;
import me.dio.sacola.repository.SacolaRepository;
import me.dio.sacola.resources.dto.ItemDto;
import me.dio.sacola.service.SacolaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SacolaServiceImpl implements SacolaService {
    private final SacolaRepository sacolaRepository;
    private final ProdutoRepository produtoRepository;

    private final ItemRepository itemRepository;
    @Override
    public Sacola verSacola(Long id) {
        return sacolaRepository.findById(id).orElseThrow(
                ()-> {
                    throw new RuntimeException("Essa sacola não existe");
                }
        );
    }
    @Override
    public Sacola fecharSacola(Long id, int numeroFormaPagamento) {
        Sacola sacola = verSacola(id);
        if(sacola.getItens().isEmpty()) {
            throw new RuntimeException("Inclua ítens na sacola");
        }
        FormaPagamento formaPagamento =
                numeroFormaPagamento == 0 ? FormaPagamento.DINHEIRO : FormaPagamento.MAQUINETA;
        sacola.setFormaPagamento(formaPagamento);
        sacola.setFechada(true);
        return sacolaRepository.save(sacola);
    }
    @Override
    public Item incluirItemNaSacola(ItemDto itemDto) {
        Sacola sacola = verSacola(itemDto.getSacolaId());
        Produto produto = produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(
                () -> {
                    throw new RuntimeException("Produto não encontrado");
                }
        );
        if (sacola.isFechada()) {
            throw new RuntimeException("Essa sacola está fechada.");
        }
        Item novoItem = Item.builder()
                .produto(produto)
                .quantidade(itemDto.getQuantidade())
                .sacola(sacola)
                .build();

        List<Item> itensDaSacola = sacola.getItens();

        if (itensDaSacola.isEmpty()) {
            itensDaSacola.add(novoItem);
        } else {
            Restaurante restauranteNaSacola
                    = itensDaSacola.get(0).getProduto().getRestaurante();
            Restaurante restauranteNovoItem = produto.getRestaurante();
            if (restauranteNovoItem.equals(restauranteNaSacola)) {
                itensDaSacola.add(novoItem);
            } else {
                throw new RuntimeException(
                        "Não é possivel adicionar produtos de restaurantes diferentes. " +
                                "Feche a sacola ou esvazie."
                );
            }
        }
        List<Double> valorDosItens = new ArrayList<>();

        for(Item itenmDaSacola : itensDaSacola){
            double somaValorItem =
                    itenmDaSacola.getProduto().getValorUnitario() * itenmDaSacola.getQuantidade();
            valorDosItens.add(somaValorItem);
        }
        double valorTotalSacola = valorDosItens.stream()
                .mapToDouble(valorTotalCadaItem -> valorTotalCadaItem)
                        .sum();
      /*  for(Double valorItens : valorDosItens){
            valorTotalSacola += valorItens;
        }*/


        sacola.setValorTotal(valorTotalSacola);
        sacolaRepository.save(sacola);
        return novoItem;
    }

    public Sacola removerItemDaSacola(Long sacolaId, Long itemId) {
        Sacola sacola = verSacola(sacolaId);
        if (sacola.isFechada()) {
            throw new RuntimeException("Esta sacola está fechada");
        }
        List<Item> itensDaSacola = sacola.getItens();
        for(Item item : itensDaSacola) {
            if (item.getId().equals(itemId)) {
                System.out.println("entyoru");

                itensDaSacola.remove(item);

//                sacola.setItens(itensDaSacola);
                itemRepository.delete(item);
            }
        }
        sacolaRepository.save(sacola);
        return sacola;
    }
}
