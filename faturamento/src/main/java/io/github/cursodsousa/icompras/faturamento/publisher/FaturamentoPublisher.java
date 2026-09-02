package io.github.cursodsousa.icompras.faturamento.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cursodsousa.icompras.faturamento.model.Pedido;
import io.github.cursodsousa.icompras.faturamento.publisher.representation.AtualizacaoStatusPedido;
import io.github.cursodsousa.icompras.faturamento.publisher.representation.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FaturamentoPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;
    @Value("${icompras.config.kafka.topics.pedidos-faturados}")
    private String topico;

    public void publisher(Pedido pedido, String urlNotaFiscal){
        try {
            var representation = new AtualizacaoStatusPedido(pedido.codigo(), StatusPedido.FATURADO, urlNotaFiscal);
            String json =mapper.writeValueAsString(representation);
            kafkaTemplate.send(topico,"dados", json);
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }

    }

}
