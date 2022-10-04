package me.dio.sacola.service;

import me.dio.sacola.model.Item;
import me.dio.sacola.model.Sacola;
import me.dio.sacola.resources.dto.ItemDto;

public interface SacolaService {
    Sacola verSacola(Long id);
    Sacola fecharSacola(Long id, int formaPagamento);
    Item incluirItemNaSacola(ItemDto itemDto);
    Sacola removerItemDaSacola(Long sacolaId, Long itemId);
}
