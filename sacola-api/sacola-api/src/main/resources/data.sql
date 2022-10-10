INSERT INTO restaurante (id, cep, complemento, nome) VALUES
               (1L, '0000001', 'Complemento Endereço Restaurante 1', 'Restaurante 1'),
               (2L, '0000002', 'Complemento Endereço Restaurante 2', 'Restaurante 2');

INSERT INTO cliente (id, cep, complemento, nome) VALUES
    (1L, '0000001', 'Complemento Endereço Cliente 1', 'Cliente 1'),
    (2L, '55200-000', 'Av. carlos de brito, 361 - prado - Pesqueira - pe', 'Pietra Panda');

INSERT INTO sacola (id, forma_pagamento, fechada, valor_total, cliente_id) VALUES
    (1L, 0, false, 0.0, 1L);

INSERT INTO produto (id, disponivel, nome, valor_unitario, restaurante_id) VALUES
               (1L, true, 'Hamburguer', 15.0, 1L),
               (2L, true, 'Coca-cola 350ml', 6.0, 1L),
               (3L, true, 'Produto 3', 7.0, 2L);
