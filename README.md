# Worker de mensagens HTTP

Aplicação Java com Spring Boot que recebe lotes de mensagens por uma API REST,
publica cada mensagem no RabbitMQ e as processa de forma assíncrona. Para cada
mensagem, o worker envia o payload como JSON em uma requisição HTTP `POST` para
a URL informada.

O projeto também oferece autenticação JWT e persiste no PostgreSQL os lotes e
os status dos itens.

## Tecnologias

- Java 25 e Spring Boot
- Spring Web MVC, Spring Security e JWT
- RabbitMQ para processamento assíncrono
- PostgreSQL e Flyway para persistência e migrações
- Maven Wrapper para build e execução

## Pré-requisitos

- JDK 25
- Docker e Docker Compose
- PostgreSQL disponível localmente

O `docker-compose.yml` inicia somente o RabbitMQ. O PostgreSQL precisa ser
iniciado separadamente. A configuração de desenvolvimento da aplicação está em
`src/main/resources/application-dev.yaml`; configure o banco para corresponder
a essa configuração ou ajuste o arquivo para o seu ambiente.

## Executar localmente

1. Inicie o RabbitMQ:

   ```bash
   docker compose up -d rabbitmq
   ```

   O broker fica disponível na porta `5672`; a interface de gerenciamento fica
   na porta `15672`. O projeto carrega as definições de exchanges, filas e
   bindings de `rabbitmq/definitions.json`.

2. Inicie o PostgreSQL e configure a conexão em
   `src/main/resources/application-dev.yaml`.

3. Na raiz do projeto, compile e execute a aplicação:

   ```bash
   ./mvnw test
   ./mvnw spring-boot:run
   ```

   O perfil padrão é `dev`. Na inicialização, o Flyway aplica as migrações do
   banco. A API fica disponível em `http://localhost:8080`.

## Autenticação

As rotas de registro e autenticação são públicas. Registre um usuário:

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H 'Content-Type: application/json' \
  -d '{
    "username": "meu-usuario",
    "password": "uma-senha-local"
  }'
```

Obtenha um token JWT:

```bash
curl -X POST http://localhost:8080/api/v1/auth/authenticate \
  -H 'Content-Type: application/json' \
  -d '{
    "username": "meu-usuario",
    "password": "uma-senha-local"
  }'
```

Use o token retornado no cabeçalho `Authorization` das requisições protegidas,
com o esquema Bearer seguido pelo JWT.

## Enviar um lote

No comando abaixo, defina `JWT_HEADER` com o valor de autorização descrito acima.

Envie um array `messages`. Cada item deve conter `url` (destino HTTP) e
`payload` (conteúdo JSON a ser enviado ao destino):

```bash
curl -X POST http://localhost:8080/api/v1/message/add-message \
  -H "Authorization: ${JWT_HEADER}" \
  -H 'Content-Type: application/json' \
  -d '{
    "messages": [
      {
        "url": "https://seu-servico.example/api/notificacoes",
        "payload": {
          "tipo": "notificacao",
          "conteudo": "Olá"
        }
      },
      {
        "url": "https://seu-servico.example/api/eventos",
        "payload": {
          "tipo": "evento",
          "id": 123
        }
      }
    ]
  }'
```

A API responde com HTTP `201 Created` e os dados do lote, incluindo o
identificador do lote e os identificadores e status iniciais dos itens. O
worker consome as mensagens pela fila configurada e envia cada payload ao
respectivo destino como JSON.

## Configuração

Os principais parâmetros de conexão e do RabbitMQ estão em
`src/main/resources/application-dev.yaml`. As definições do broker, incluindo
fila, exchange e roteamento, ficam em `rabbitmq/definitions.json` e
`rabbitmq/rabbitmq.conf`.

Antes de usar fora do ambiente local, configure credenciais próprias para o
banco e o RabbitMQ e um segredo JWT seguro. Não reutilize os valores de
desenvolvimento em produção.

## Observações

- As rotas de mensagens exigem autenticação; as rotas sob
  `/api/v1/auth/**` são públicas.
- O destino de cada mensagem recebe uma requisição HTTP `POST` com o payload
  no corpo.
- Os exemplos de requisição também estão em `http_tests/message.http`.
- Atualmente, a API expõe o envio de lotes; não há uma rota HTTP para consultar
  posteriormente o status de um lote ou de seus itens.