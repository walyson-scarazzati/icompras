# iCompras

Sistema de compras baseado em microsserviços, desenvolvido a partir do curso [Spring Boot com Kafka](https://www.udemy.com/course/spring-boot-kafka/) ([repositório do curso](https://github.com/cursodsousa/curso-spring-kafka)).

O projeto simula o fluxo completo de um pedido de compra: cadastro de clientes e produtos, criação e pagamento do pedido, geração de nota fiscal e envio, com a comunicação entre serviços feita via Kafka.

## Arquitetura

| Módulo | Porta | Responsabilidade |
|---|---|---|
| [clientes](clientes) | 8082 | CRUD de clientes |
| [produtos](produtos) | 8081 | CRUD de produtos |
| [pedidos](pedidos) | 8080 | Orquestra a criação de pedidos, pagamento e status |
| [faturamento](faturamento) | 8083 | Gera a nota fiscal (PDF) e armazena no bucket |
| [logistica](logistica) | 8084 | Processa o envio dos pedidos faturados |
| [icompras-servicos](icompras-servicos) | - | Infraestrutura local (banco, broker, bucket) |

**Stack:** Java 21, Spring Boot 3.4.4, Spring Data JPA, Spring Kafka, OpenFeign, PostgreSQL, Apache Kafka, MinIO, JasperReports, Lombok, MapStruct.

### Fluxo de dados (Kafka)

1. `pedidos` cria o pedido e processa o pagamento → publica em `icompras.pedidos-pagos`
2. `faturamento` consome o evento, gera a nota fiscal em PDF e armazena no MinIO → publica em `icompras.pedidos-faturados`
3. `logistica` consome o evento e processa o envio → publica em `icompras.pedidos-enviados`
4. `pedidos` consome `icompras.pedidos-faturados` e `icompras.pedidos-enviados` para atualizar o status do pedido

`clientes` e `produtos` são consultados de forma síncrona (REST/Feign) pelo serviço de `pedidos` durante a criação do pedido.

## Infraestrutura

Os serviços de infraestrutura ficam em `icompras-servicos`, cada um com seu próprio `docker-compose.yml`:

- **database** — PostgreSQL (porta `5555`), com os bancos `icomprasclientes`, `icomprasprodutos` e `icompraspedidos`
- **broker** — Zookeeper, Kafka (`localhost:29092`) e Kafka UI (porta `8090`)
- **bucket** — MinIO (API na porta `9000`, console na `9001`), usado pelo `faturamento` para armazenar as notas fiscais em PDF

## Como executar

1. Suba a infraestrutura:
   ```bash
   docker compose -f icompras-servicos/database/docker-compose.yml up -d
   docker compose -f icompras-servicos/broker/docker-compose.yml up -d
   docker compose -f icompras-servicos/bucket/docker-compose.yml up -d
   ```
2. Inicie os microsserviços (via Maven ou pelas configurações em `.vscode/launch.json`):
   ```bash
   cd clientes && mvn spring-boot:run
   cd produtos && mvn spring-boot:run
   cd pedidos && mvn spring-boot:run
   cd faturamento && mvn spring-boot:run
   cd logistica && mvn spring-boot:run
   ```

A coleção [ICompras.postman_collection.json](ICompras.postman_collection.json) contém as requisições de exemplo para todos os endpoints.

## Principais endpoints

| Serviço | Método | Rota |
|---|---|---|
| clientes | POST / GET / DELETE | `/clientes`, `/clientes/{codigo}` |
| produtos | POST / GET / DELETE | `/produtos`, `/produtos/{codigo}` |
| pedidos | POST | `/pedidos` |
| pedidos | POST | `/pedidos/pagamentos` |
| pedidos | POST | `/pedidos/callback-pagamentos` |
| pedidos | GET | `/pedidos/{codigo}` |
| faturamento | POST | `/bucket` |
| faturamento | GET | `/bucket?filename=...` |
