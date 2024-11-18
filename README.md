# PayFlow Manager

Um sistema de gerenciamento de contas a pagar construído com Spring Boot e práticas modernas de Java. Esta aplicação fornece uma API REST completa para gerenciar contas, incluindo recursos como criação de contas, processamento de pagamentos, rastreamento de status e importações via CSV.

## 🌟 Funcionalidades

- Gerenciamento completo do ciclo de vida das contas (Criar, Atualizar, Pagar, Cancelar)
- Rastreamento flexível de status (Pendente, Pago, Vencido, Cancelado)
- Autenticação segura com JWT
- Importação em lote de contas via CSV
- Busca e filtragem abrangente de contas
- Análise e relatórios de períodos de pagamento
- Controle de acesso baseado em funções (Usuário, Administrador)
- Documentação da API com OpenAPI/Swagger
- Migrações de banco de dados com Flyway
- Containerizado com Docker

## 🛠️ Stack Tecnológica

- Java 17
- Spring Boot 3.3.5
- Spring Security com JWT
- PostgreSQL
- Flyway
- Docker & Docker Compose
- OpenCSV
- OpenAPI/Swagger

## 🚀 Começando

### Pré-requisitos

- Docker e Docker Compose
- Java 17 (para desenvolvimento local)
- Maven (opcional, wrapper incluído)
- Postman (para testar a API)

### Executando a Aplicação

1. Clone o repositório:
```bash
git clone https://github.com/Djoarns/payflow-manager.git
cd payflow-manager
```

2. Copie o arquivo de ambiente:
```bash
cp .env.example .env
```

3. (Opcional) Modifique o arquivo `.env` se desejar personalizar:
   - Credenciais do banco de dados
   - Configurações JWT
   - Portas da aplicação

4. Inicie a aplicação usando Docker Compose:
```bash
docker-compose up -d
```

A aplicação estará disponível em:
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- PgAdmin: http://localhost:5050 (credenciais no arquivo .env)

## 📝 Documentação da API

A documentação da API está disponível através do Swagger UI em `/swagger-ui/index.html` quando a aplicação estiver em execução.

### Endpoints Principais:

- Autenticação:
  - `POST /api/v1/auth/register`: Registrar novo usuário
  - `POST /api/v1/auth/login`: Login e obtenção do token JWT

- Contas:
  - `POST /api/v1/bills`: Criar nova conta
  - `PUT /api/v1/bills/{id}`: Atualizar conta
  - `PATCH /api/v1/bills/{id}/pay`: Pagar conta
  - `GET /api/v1/bills`: Listar contas com filtros
  - `POST /api/v1/bills/import`: Importar contas do CSV

## 🔧 Configuração do Postman

1. Importe a coleção do Postman:
   - Abra o Postman
   - Clique em "Import"
   - Selecione o arquivo `postman-collection.json` da raiz do projeto

2. Importe o ambiente:
   - Clique em "Import" novamente
   - Selecione o arquivo `postman-environment.json`
   - Selecione o ambiente "PayFlow Local" no Postman

3. Fluxo de Autenticação:
   - Use a requisição "Register" para criar um novo usuário
   - Use a requisição "Login" para obter um token JWT
   - Adicione o token gerado ao campo "token" dentro do environment para ser utilizado por todas as requisições

## 🧪 Testes

### Executando Testes

```bash
# Executar todos os testes
./mvnw test

### Dados de Teste

Um arquivo CSV de exemplo (`test-bills.csv`) é fornecido na raiz do projeto para testar a funcionalidade de importação.

## 📁 Estrutura do Projeto

O projeto segue os princípios do Domain-Driven Design:

```
src/main/java/
├── application/        # Camada de Aplicação (Controllers, DTOs)
├── domain/            # Camada de Domínio (Entidades, Objetos de Valor)
├── infrastructure/    # Camada de Infraestrutura (Repositórios, Segurança)
└── PayflowManagerApplication.java
```

## 🔐 Segurança

- Todos os endpoints exceto `/api/v1/auth/**` requerem autenticação
- Tokens JWT expiram após 24 horas (configurável)
- Senhas são criptografadas usando BCrypt
- Controle de acesso baseado em funções implementado

## 📊 Banco de Dados

- Migrações são gerenciadas pelo Flyway
- Schema inicial é criado automaticamente
- PgAdmin está incluído para gerenciamento do banco de dados
- Detalhes de conexão:
  - Host: localhost
  - Porta: 5432
  - Banco de dados: payflow_db
  - Usuário e Senha: Conforme definido no arquivo .env
