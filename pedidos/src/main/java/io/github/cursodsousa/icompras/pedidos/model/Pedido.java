package io.github.cursodsousa.icompras.pedidos.model;

import io.github.cursodsousa.icompras.pedidos.model.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="pedido")
@Getter
@Setter
@NoArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    @Column(nullable = false)
    private Long codigoCliente;
    @Column(nullable = false)
    private LocalDateTime dataPedido;
    @Column(precision = 16, scale = 2)
    private BigDecimal total;
    private String chavePagamento;
    private String observacoes;
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    private String codigoRastreio;
    @Column(name = "url_nf")
    private String urlNotaFiscal;
}
