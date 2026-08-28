package io.github.cursodsousa.icompras.faturamento.model;

import java.math.BigDecimal;

public record ItemPedido(Long codigo, String descricao, BigDecimal valorunitario, Integer quantidade, BigDecimal total) {

}
