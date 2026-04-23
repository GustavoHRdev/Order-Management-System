# Order Management System

Sistema de gerenciamento de pedidos em Java, evoluído de uma aplicação de console para uma base com camadas mais bem definidas, persistência em H2 via JDBC e API REST.

O foco do projeto é consolidar fundamentos de backend:
- modelagem de domínio
- regras de negócio
- separação de responsabilidades
- testes
- evolução incremental da arquitetura

## Estado atual

O projeto atualmente oferece:
- cadastro e listagem de clientes
- cadastro e listagem de produtos
- criação de pedidos por ID real
- adição de itens ao pedido
- atualização de status com regras de transição
- persistência em H2 usando JDBC
- API REST sobre os mesmos services usados pelo console
- testes unitários e testes de integração de repositório JDBC
- Maven Wrapper (`mvnw`, `mvnw.cmd`)

## Arquitetura

A estrutura atual está organizada em camadas:

- `model`
  Classes de domínio como `Cliente`, `Produto`, `Pedido`, `ItemPedido` e `StatusPedido`.

- `service`
  Regras de negócio da aplicação. A camada REST e a camada CLI usam esses mesmos services.

- `repository`
  Contratos de persistência e implementações concretas.
  Hoje existem implementações em memória e implementações JDBC.

- `database`
  Infraestrutura de conexão e inicialização do banco H2.

- `cli`
  Interface de console e comandos.

- `api`
  Endpoints HTTP simples usando `HttpServer`, sem framework web pesado.

- `app`
  Bootstrap da aplicação, contexto de dependências e pontos de entrada.

## Principais decisões já aplicadas

- troca de index por ID real nos fluxos de negócio
- criação de `InputReader` para centralizar entrada no console
- fortalecimento das regras de negócio do pedido
- expansão dos testes
- criação de interfaces para repositórios
- separação entre contrato e implementação (`InMemory*` e `Jdbc*`)
- retirada de responsabilidade do `Main`
- criação de `ApplicationContext` para centralizar a composição
- adição de H2 com JDBC
- exposição de API REST mantendo os `services` como centro da regra de negócio

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
- JUnit 5
- H2 Database
- JDBC
- Jackson
- `com.sun.net.httpserver.HttpServer`

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

Hoje o caminho mais simples é abrir o projeto na IDE e executar:

- `app.Main`

Essa entrada usa o `ApplicationContext`, inicializa o banco H2 e sobe o fluxo CLI.

### Rodar a API REST

Execute na IDE:

- `app.ApiMain`

A API sobe em:

```text
http://localhost:8080
```

## Endpoints REST

### Clientes

- `GET /clientes`
- `GET /clientes/{id}`
- `POST /clientes`

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
    │       ├── database/
    │       ├── model/
    │       ├── repository/
    │       └── service/
    └── test/
        └── java/
            ├── repository/
            └── service/
```

## Persistência

O projeto agora usa H2 com JDBC como persistência principal da aplicação.

Pontos importantes:
- o banco é inicializado pelo `DatabaseInitializer`
- o contexto padrão usa H2 em arquivo local
- os repositórios JDBC implementam os mesmos contratos usados anteriormente pelas implementações em memória
- os contratos agora usam `Optional` em buscas por ID

Isso permite trocar a infraestrutura sem reescrever a regra de negócio.

## Testes

Hoje o projeto possui:
- testes de service
- testes cobrindo regras de status e IDs inválidos
- testes de integração para repositórios JDBC/H2

Execução:

```bash
mvnw.cmd test
```

## Melhorias que ainda serão aplicadas

As próximas melhorias que continuam fazendo sentido para a evolução do projeto são:

- adicionar DTOs específicos para a API, separados das respostas internas atuais
- melhorar tratamento de erros HTTP com estrutura padronizada
- extrair casos de uso mais explícitos se os services crescerem
- adicionar testes de integração para a camada REST
- criar comandos Maven para facilitar execução do console e da API sem depender da IDE
- revisar persistência para suportar consultas mais ricas e paginação
- melhorar UX do console
- adicionar validações mais consistentes de entrada e mensagens mais claras
- documentar a API com exemplos mais completos
- evoluir a infraestrutura HTTP para um framework web quando fizer sentido
- considerar autenticação/autorização se o projeto passar a ter uso multiusuário

## Próximos passos naturais

A sequência mais coerente de evolução, a partir do estado atual, é:

1. consolidar a API REST com testes e DTOs
2. melhorar a experiência de execução e observabilidade
3. refinar a modelagem e o tratamento de erros
4. só depois avaliar uma migração para framework web mais robusto

## Observação

O projeto foi evoluído de forma incremental para preservar clareza arquitetural. A intenção não é apenas “ter funcionalidades”, mas construir uma base que deixe explícito como cada mudança foi introduzida e por quê.
