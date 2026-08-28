package io.github.cursodsousa.icompras.faturamento.bucket;

import io.github.cursodsousa.icompras.faturamento.config.props.MinioProps;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BucketService {
    private final MinioClient client;
    private final MinioProps props;

    public void upload(BucketFile file){

    }
    public String getUrl(String fileName){

    }
}
