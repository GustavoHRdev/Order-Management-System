# API REST

Documentacao executavel da API com exemplos de `curl` e uma collection Postman importavel.

A API agora roda sobre Spring Boot e Spring MVC, com validacao de entrada via Bean Validation, tratamento padronizado de erros em JSON e testes de integracao com MockMvc.

## Pre-requisitos

- Java 17+
- API em execucao em `http://localhost:8080`

Windows:

```bash
mvnw.cmd compile exec:java@run-api
```

Linux/macOS:

```bash
./mvnw compile exec:java@run-api
```

Se a porta `8080` estiver ocupada:

```bash
mvnw.cmd "-Dapi.port=8081" compile exec:java@run-api
```

## Base URL

```text
http://localhost:8080
```

## Collection Postman

Importe o arquivo abaixo no Postman:

- [docs/postman/Order-Management-System.postman_collection.json](postman/Order-Management-System.postman_collection.json)

A collection usa a variavel `baseUrl`, com valor padrao `http://localhost:8080`.

## Comportamentos da API

- criacao retorna `201 Created` com header `Location`
- validacoes de entrada retornam `400 Bad Request`
- erros de negocio retornam JSON estruturado
- metodos nao suportados retornam `405 Method Not Allowed`

## Fluxo completo via curl

### 1. Criar cliente

```bash
curl -i -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"Ana\",\"email\":\"ana@example.com\"}"
```

Resposta esperada:

```http
HTTP/1.1 201 Created
Content-Type: application/json; charset=UTF-8
Location: http://localhost:8080/clientes/1
```

```json
{
  "id": 1,
  "nome": "Ana",
  "email": "ana@example.com"
}
```

### 2. Criar produto

```bash
curl -i -X POST http://localhost:8080/produtos \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"Mouse\",\"preco\":50.0}"
```

Resposta esperada:

```json
{
  "id": 1,
  "nome": "Mouse",
  "preco": 50.0
}
```

Header relevante:

```http
Location: http://localhost:8080/produtos/1
```

### 3. Criar pedido

```bash
curl -i -X POST http://localhost:8080/pedidos \
  -H "Content-Type: application/json" \
  -d "{\"clienteId\":1}"
```

Resposta esperada:

```json
{
  "id": 1,
  "clienteId": 1,
  "clienteNome": "Ana",
  "status": "PENDENTE",
  "itens": [],
  "total": 0.0
}
```

Header relevante:

```http
Location: http://localhost:8080/pedidos/1
```

### 4. Adicionar item ao pedido

```bash
curl -i -X POST http://localhost:8080/pedidos/1/itens \
  -H "Content-Type: application/json" \
  -d "{\"produtoId\":1,\"quantidade\":2}"
```

Resposta esperada:

```json
{
  "id": 1,
  "clienteId": 1,
  "clienteNome": "Ana",
  "status": "PENDENTE",
  "itens": [
    {
      "produtoId": 1,
      "produtoNome": "Mouse",
      "precoUnitario": 50.0,
      "quantidade": 2,
      "subtotal": 100.0
    }
  ],
  "total": 100.0
}
```

### 5. Avancar status do pedido

```bash
curl -i -X PATCH http://localhost:8080/pedidos/1/status \
  -H "Content-Type: application/json" \
  -d "{\"status\":\"PROCESSANDO\"}"
```

Resposta esperada:

```json
{
  "id": 1,
  "clienteId": 1,
  "clienteNome": "Ana",
  "status": "PROCESSANDO",
  "itens": [
    {
      "produtoId": 1,
      "produtoNome": "Mouse",
      "precoUnitario": 50.0,
      "quantidade": 2,
      "subtotal": 100.0
    }
  ],
  "total": 100.0
}
```

## Consultas

### Listar clientes

```bash
curl -i http://localhost:8080/clientes
```

```json
[
  {
    "id": 1,
    "nome": "Ana",
    "email": "ana@example.com"
  }
]
```

### Buscar cliente por id

```bash
curl -i http://localhost:8080/clientes/1
```

### Listar produtos

```bash
curl -i http://localhost:8080/produtos
```

### Buscar produto por id

```bash
curl -i http://localhost:8080/produtos/1
```

### Listar pedidos

```bash
curl -i http://localhost:8080/pedidos
```

### Buscar pedido por id

```bash
curl -i http://localhost:8080/pedidos/1
```

## Erros comuns

### JSON invalido

```bash
curl -i -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"Ana\","
```

Resposta esperada:

```json
{
  "timestamp": "2026-05-04T12:34:56.789Z",
  "status": 400,
  "error": "Bad Request",
  "message": "JSON inválido.",
  "path": "/clientes"
}
```

### Validacao de entrada

```bash
curl -i -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"\",\"email\":\"invalido\"}"
```

Resposta esperada:

```json
{
  "timestamp": "2026-05-05T12:34:56.789Z",
  "status": 400,
  "error": "Bad Request",
  "message": "nome: não deve estar em branco.",
  "path": "/clientes"
}
```

### Cliente inexistente ao criar pedido

```bash
curl -i -X POST http://localhost:8080/pedidos \
  -H "Content-Type: application/json" \
  -d "{\"clienteId\":999}"
```

Resposta esperada:

```json
{
  "timestamp": "2026-05-04T12:34:56.789Z",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente inválido!",
  "path": "/pedidos"
}
```

### Rota inexistente

```bash
curl -i http://localhost:8080/invalido
```

Resposta esperada:

```json
{
  "timestamp": "2026-05-04T12:34:56.789Z",
  "status": 404,
  "error": "Not Found",
  "message": "Rota não encontrada.",
  "path": "/invalido"
}
```

### Metodo nao permitido

```bash
curl -i -X DELETE http://localhost:8080/clientes
```

Resposta esperada:

```http
HTTP/1.1 405 Method Not Allowed
Allow: GET, POST
Content-Type: application/json; charset=UTF-8
```

```json
{
  "timestamp": "2026-05-04T12:34:56.789Z",
  "status": 405,
  "error": "Method Not Allowed",
  "message": "Método não permitido.",
  "path": "/clientes"
}
```

### Status invalido

```bash
curl -i -X PATCH http://localhost:8080/pedidos/1/status \
  -H "Content-Type: application/json" \
  -d "{\"status\":\"QUALQUER_COISA\"}"
```

Resposta esperada:

```json
{
  "timestamp": "2026-05-04T12:34:56.789Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Status inválido.",
  "path": "/pedidos/1/status"
}
```
