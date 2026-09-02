package io.github.cursodsousa.icompras.pedidos.service;

import io.github.cursodsousa.icompras.pedidos.model.enums.StatusPedido;
import io.github.cursodsousa.icompras.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizacaoStatusPedidoService {

    private final PedidoRepository repository;

    public void atualizarStatus(Long codigo, StatusPedido status, String urlNotaFiscal, String rastreio){

    }
}
