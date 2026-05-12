# Order Management System

Sistema de gerenciamento de pedidos em Java, evoluído de uma aplicação de console para uma API REST com Spring Boot, persistência em H2 via JDBC e testes automatizados.

O foco do projeto é consolidar fundamentos de backend:
- modelagem de domínio
- regras de negócio
- separação de responsabilidades
- testes
- evolução incremental da arquitetura

## Resumo técnico

- Spring Boot 3.5
- API REST com Spring MVC
- H2 como banco principal de desenvolvimento
- JDBC manual via datasource do Spring Boot
- Bean Validation para entrada da API
- tratamento padronizado de erros em JSON
- testes de integração com MockMvc
- CI com GitHub Actions

## Estado atual

O projeto atualmente oferece:
- cadastro e listagem de clientes
- cadastro e listagem de produtos
- criação de pedidos por ID real
- adição de itens ao pedido
- atualização de status com regras de transição
- persistência em H2 usando JDBC
- API REST com Spring MVC sobre os mesmos services usados pelo console
- validação de entrada com Bean Validation
- tratamento padronizado de erros HTTP em JSON
- testes unitários, testes de integração de repositório JDBC e testes da camada REST
- workflow GitHub Actions rodando `mvn test`
- Maven Wrapper (`mvnw`, `mvnw.cmd`)

Hoje o projeto já está numa base compatível com backend júnior/pleno inicial: Spring Boot, API HTTP, persistência real, validação, tratamento de erro e testes automatizados.

## Arquitetura

A estrutura atual está organizada em camadas:

- `model`
  Classes de domínio como `Cliente`, `Produto`, `Pedido`, `ItemPedido` e `StatusPedido`.

- `service`
  Regras de negócio da aplicação. A camada REST e a camada CLI usam esses mesmos services.

- `repository`
  Contratos de persistência e implementações concretas.
  Hoje existem implementações em memória e implementações JDBC.

- `cli`
  Interface de console e comandos.

- `api`
  Controllers REST com Spring MVC, DTOs, Bean Validation e tratamento padronizado de erros.

- `app`
  Bootstrap com Spring Boot e pontos de entrada da aplicação.

- `exception`
  Exceções de domínio e validação usadas pela regra de negócio e mapeadas na camada HTTP.

## Principais decisões já aplicadas

- troca de index por ID real nos fluxos de negócio
- criação de `InputReader` para centralizar entrada no console
- fortalecimento das regras de negócio do pedido
- expansão dos testes
- criação de interfaces para repositórios
- separação entre contrato e implementação (`InMemory*` e `Jdbc*`)
- retirada de responsabilidade do `Main`
- adição de H2 com JDBC
- migração da API para Spring Boot, controllers e injeção de dependência nativa
- validação de entrada com Bean Validation
- padronização de erros da API com handler centralizado

## Regras de negócio implementadas

- um pedido precisa estar associado a um cliente válido
- a quantidade do item deve ser maior que zero
- não é permitido avançar o pedido sem itens, exceto para cancelamento
- não é permitido alterar itens de um pedido que já saiu de `PENDENTE`
- o fluxo válido de status é:
  `PENDENTE -> PROCESSANDO -> ENVIADO -> ENTREGUE`
- cancelamento é permitido antes da finalização
- não é permitido alterar um pedido já `ENTREGUE` ou `CANCELADO`

## Tecnologias

- Java 17
- Maven
- Maven Wrapper
- Spring Boot 3.5.14
- Spring Web
- Spring Validation
- Spring JDBC
- H2 Database
- JDBC
- MockMvc
- JUnit 5
- GitHub Actions

## Como executar

### Pré-requisitos

- Java 17+

### Clonar o projeto

```bash
git clone https://github.com/GustavoHRdev/Order-Management-System.git
cd Order-Management-System
```

### Rodar os testes

```bash
mvnw.cmd test
```

No Linux/macOS:

```bash
./mvnw test
```

### Rodar a aplicação de console

Windows:

```bash
mvnw.cmd compile exec:java@run-cli
```

Linux/macOS:

```bash
./mvnw compile exec:java@run-cli
```

Opcionalmente, ainda e possivel executar `app.Main` pela IDE.

Essa entrada sobe um contexto Spring sem servidor web, inicializa o banco H2 e executa o fluxo CLI.

### Rodar a API REST

Windows:

```bash
mvnw.cmd compile exec:java@run-api
```

Linux/macOS:

```bash
./mvnw compile exec:java@run-api
```

Opcionalmente, ainda e possivel executar `app.ApiMain` ou `app.OrderManagementApplication` pela IDE.

Se a porta `8080` ja estiver em uso, rode com outra porta:

```bash
mvnw.cmd "-Dapi.port=8081" compile exec:java@run-api
```

A API sobe em:

```text
http://localhost:8080
```

## Documentacao da API

A documentacao executavel da API fica em:

- [docs/API-REST.md](docs/API-REST.md)
- [docs/postman/Order-Management-System.postman_collection.json](docs/postman/Order-Management-System.postman_collection.json)

Ela inclui:
- fluxo completo com `curl`
- exemplos de payload e resposta
- exemplos de validacao e erros HTTP
- cenarios de erro
- collection importavel no Postman

## Endpoints REST

### Clientes

- `GET /clientes`
- `GET /clientes/{id}`
- `POST /clientes`

Respostas:
- `201 Created` em criacao, com header `Location`
- `400 Bad Request` para payload invalido ou falha de validacao
- `405 Method Not Allowed` para metodo nao suportado na rota

Exemplo de payload:

```json
{
  "nome": "Ana",
  "email": "ana@example.com"
}
```

### Produtos

- `GET /produtos`
- `GET /produtos/{id}`
- `POST /produtos`

Respostas:
- `201 Created` em criacao, com header `Location`
- `400 Bad Request` para payload invalido ou falha de validacao
- `405 Method Not Allowed` para metodo nao suportado na rota

Exemplo de payload:

```json
{
  "nome": "Mouse",
  "preco": 50.0
}
```

### Pedidos

- `GET /pedidos`
- `GET /pedidos/{id}`
- `POST /pedidos`
- `POST /pedidos/{id}/itens`
- `PATCH /pedidos/{id}/status`

Respostas:
- `201 Created` em criacao do pedido, com header `Location`
- `200 OK` ao adicionar item e atualizar status
- `400 Bad Request` para JSON, validacao, ID ou status invalidos
- `404 Not Found` para entidade inexistente ou rota inexistente
- `405 Method Not Allowed` para metodo nao suportado na rota

Criar pedido:

```json
{
  "clienteId": 1
}
```

Adicionar item:

```json
{
  "produtoId": 1,
  "quantidade": 2
}
```

Atualizar status:

```json
{
  "status": "PROCESSANDO"
}
```

## Estrutura do projeto

```text
Order-Management-System/
├── .github/
│   └── workflows/
├── .mvn/
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   └── java/
    │       ├── api/
    │       ├── app/
    │       ├── cli/
    │       ├── model/
    │       ├── repository/
    │       └── service/
    │   └── resources/
    └── test/
        └── java/
            ├── api/
            ├── repository/
            └── service/
```

## Persistência

O projeto agora usa H2 com JDBC como persistência principal da aplicação.

Pontos importantes:
- o banco é inicializado via `schema.sql`
- o contexto padrão usa H2 em arquivo local por meio do datasource do Spring Boot
- os repositórios JDBC implementam os mesmos contratos usados anteriormente pelas implementações em memória
- os contratos agora usam `Optional` em buscas por ID

Isso permite trocar a infraestrutura sem reescrever a regra de negócio.

Observação: o próximo passo natural de infraestrutura é adicionar PostgreSQL e migrar a camada JDBC manual para Spring Data JPA.

## Testes

Hoje o projeto possui:
- testes de service
- testes cobrindo regras de status e IDs inválidos
- testes de integração para repositórios JDBC/H2
- testes de integração da camada REST com `MockMvc`
- workflow CI no GitHub Actions executando `mvn test`

Execução:

```bash
mvnw.cmd test
```

## Melhorias futuras

As próximas melhorias que continuam fazendo sentido para a evolução do projeto são:

- extrair casos de uso mais explícitos se os services crescerem
- migrar a persistência para Spring Data JPA
- adicionar perfil PostgreSQL para ambiente mais próximo de produção
- revisar persistência para suportar consultas mais ricas e paginação
- melhorar UX do console
- evoluir a camada de autenticacao/autorizacao quando fizer sentido

## Observação

O projeto foi evoluído de forma incremental para preservar clareza arquitetural. A intenção não é apenas “ter funcionalidades”, mas construir uma base que deixe explícito como cada mudança foi introduzida e por quê.
