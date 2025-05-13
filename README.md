# AntiFraude Backend

API de prevenção a fraudes combinando biometria e análise de documentos, com eventos Kafka.

---

## Pré-requisitos

* Docker & Docker Compose
* Java 17
* Maven

## Como rodar

1. Clone o repositório e entre na pasta:

   ```bash
   git clone <repo-url> && cd antifraude-backend
   ```
2. Ajuste variáveis no `docker-compose.yml` se necessário.
3. Levante os serviços:

   ```bash
   docker-compose up --build
   ```
4. A API estará em `http://localhost:8080`, Mongo em `mongodb://localhost:27017`, Kafka em `kafka:9092`.

---

## Variáveis de Ambiente (Postman Environment)

Crie um Environment `Antifraude-API` com estas chaves:

| Chave       | Valor                   |
| ----------- | ----------------------- |
| `baseUrl`   | `http://localhost:8080` |
| `userId`    |                         |
| `authToken` |                         |

---

## Endpoints & Exemplos (Postman)

### 1. Registrar Usuário

**POST** `{{baseUrl}}/api/auth/register`

* Body (JSON):

  ```json
  { "name":"Alice Silva", "email":"alice@example.com", "pass":"senha123" }
  ```
* **Tests** captura `userId` e `authToken`:

  ```js
  pm.environment.set("userId", pm.response.json().id);
  pm.environment.set("authToken", pm.response.json().token);
  ```

### 2. Login

**POST** `{{baseUrl}}/api/auth/login`

* Body (JSON): `{ "email":"alice@example.com", "pass":"senha123" }`
* **Tests** atualiza `authToken`:

  ```js
  pm.environment.set("authToken", pm.response.json().token);
  ```

### 3. Listar Usuários

**GET** `{{baseUrl}}/api/usuarios`

* Header: `Authorization: Bearer {{authToken}}`

### 4. Biometria Facial

**POST** `{{baseUrl}}/api/usuarios/{{userId}}/biometria/facial`

* Header: `Authorization: Bearer {{authToken}}`
* Body (form-data): key `imagem` → file JPEG <5 MB

### 5. Biometria Digital

**POST** `{{baseUrl}}/api/usuarios/{{userId}}/biometria/digital`

* key `arquivo` → binário <5 MB

### 6. Documentoscopia

**POST** `{{baseUrl}}/api/usuarios/{{userId}}/documentos`

* key `documento` (PDF <10 MB), key `face` (JPEG), key `tipoDocumento` (`RG` ou `CNH`)

---

## Postman Collection (v2.1)

Importe como JSON para obter todas as requests acima:

```json
{
  "info": {
    "name": "Antifraude API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Register User",
      "request": {
        "method": "POST",
        "header": [{ "key": "Content-Type", "value": "application/json" }],
        "url": { "raw": "{{baseUrl}}/api/auth/register", "host": ["{{baseUrl}}"], "path": ["api","auth","register"] },
        "body": { "mode": "raw", "raw": "{ \"name\": \"Alice Silva\", \"email\": \"alice@example.com\", \"pass\": \"senha123\" }" }
      }
    },
    {
      "name": "Login",
      "request": {
        "method": "POST",
        "header": [{ "key": "Content-Type", "value": "application/json" }],
        "url": { "raw": "{{baseUrl}}/api/auth/login", "host": ["{{baseUrl}}"], "path": ["api","auth","login"] },
        "body": { "mode": "raw", "raw": "{ \"email\": \"alice@example.com\", \"pass\": \"senha123\" }" }
      }
    },
    {
      "name": "List Users",
      "request": {
        "method": "GET",
        "header": [{ "key": "Authorization", "value": "Bearer {{authToken}}" }],
        "url": { "raw": "{{baseUrl}}/api/usuarios", "host": ["{{baseUrl}}"], "path": ["api","usuarios"] }
      }
    },
    {
      "name": "Biometria Facial",
      "request": {
        "method": "POST",
        "header": [{ "key": "Authorization", "value": "Bearer {{authToken}}" }],
        "url": { "raw": "{{baseUrl}}/api/usuarios/{{userId}}/biometria/facial", "host": ["{{baseUrl}}"], "path": ["api","usuarios","{{userId}}","biometria","facial"] },
        "body": { "mode": "formdata", "formdata": [ { "key":"imagem","type":"file" } ] }
      }
    },
    {
      "name": "Biometria Digital",
      "request": {
        "method": "POST",
        "header": [{ "key": "Authorization", "value": "Bearer {{authToken}}" }],
        "url": { "raw": "{{baseUrl}}/api/usuarios/{{userId}}/biometria/digital", "host": ["{{baseUrl}}"], "path": ["api","usuarios","{{userId}}","biometria","digital"] },
        "body": { "mode": "formdata", "formdata": [ { "key":"arquivo","type":"file" } ] }
      }
    },
    {
      "name": "Documentoscopia",
      "request": {
        "method": "POST",
        "header": [{ "key": "Authorization", "value": "Bearer {{authToken}}" }],
        "url": { "raw": "{{baseUrl}}/api/usuarios/{{userId}}/documentos", "host": ["{{baseUrl}}"], "path": ["api","usuarios","{{userId}}","documentos"] },
        "body": { "mode": "formdata", "formdata": [
          { "key":"documento","type":"file" },
          { "key":"face","type":"file" },
          { "key":"tipoDocumento","type":"text","value":"RG" }
        ] }
      }
    }
  ]
}
```
