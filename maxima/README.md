## API de Gerenciamento de Usuários e Transações Financeiras

>Este projeto é uma API simples desenvolvida com Spring Boot que utiliza um banco de dados em memória (H2). A API oferece funcionalidades para:

-Cadastro de usuários

-Consulta de informações de usuários

-Realização de transferências bancárias


-Instruções para Executar o Projeto

### Instruções para Executar o Projeto


### 1. Configuração do Ambiente

-Java: Certifique-se de ter o JDK 11 ou superior instalado.

-Maven: Garanta que o Maven esteja configurado em sua máquina.

**-Java**: Certifique-se de ter o JDK 11 ou superior instalado.

**-Maven**: Garanta que o Maven esteja configurado em sua máquina.


### 2. Executando o Projeto

-Clone o repositório:

-git clone -----url do repositorio
-cd -----pastado projeto

-Compile e execute o projeto:

-mvn spring-boot:run

-Acesse a aplicação no navegador ou via ferramentas como Postman:

-API Base: http://localhost:8080

-Console do banco de dados H2: http://localhost:8080/h2-console

-JDBC URL: jdbc:h2:mem:testdb

-Username: sa

-Password: (deixe em branco)

-Decisões Técnicas e Justificativas

-Banco de Dados em Memória (H2)

-Motivo: Facilitar o desenvolvimento e testes rápidos, eliminando a necessidade de configuração de um banco de dados externo.

-Arquitetura do Projeto

###  1.Clone o repositório:

>git clone url-do-repositorio
cd pasta-do-projeto

###  2.Compile e execute o projeto:

>mvn spring-boot:run

###  3.Acesse a aplicação no navegador ou via ferramentas como Postman:

**API Base**: http://localhost:8080

**Console do banco de dados H2**: http://localhost:8080/h2-console

**>JDBC URL**: jdbc:h2:mem:testdb

**>Username**: sa

**>Password**: (deixe em branco)

### Decisões Técnicas e Justificativas

**Banco de Dados em Memória (H2)**

**Motivo**: Facilitar o desenvolvimento e testes rápidos, eliminando a necessidade de configuração de um banco de dados externo.

### Arquitetura do Projeto


>O projeto segue uma organização baseada em pacotes para separação de responsabilidades:

**Config**: Configurações gerais do projeto.

**Controller**: Camada que expõe as APIs REST.

**DAO**: Camada de acesso ao banco de dados.

**DTO**: Objetos de Transferência de Dados para requisições e respostas.

**Model**: Representação das entidades do sistema.

**Service**: Contém a lógica de negócios.

### Validações de Regras de Negócio

**Cadastro de usuário**: Idade mínima de 18 anos, CPF único.

**Transferências**: Saldo suficiente e verificação de contas existentes.

### Status HTTP

>A API utiliza códigos HTTP apropriados:

-200 OK: Requisição bem-sucedida.

-404 Not Found: Recurso não encontrado.

-400 Bad Request: Dados inválidos ou erros de validação.

### Endpoints Disponíveis

>1. Cadastro de Usuário

>URL: POST /api/users

Corpo da Requisição:

{
  "nome": "João Silva",
  "idade": 25,
  "cpf": "12345678901"
}

-Resposta de Sucesso:

{
  "mensagem": "Usuário cadastrado com sucesso!",
  "numeroConta": "123456"
}

-Validações:

Idade mínima de 18 anos.

CPF deve ser único no sistema.

### 2. Consulta de Usuários

>Buscar por ID

URL: GET /api/users/{id}

Exemplo de Resposta:

{
  "nome": "João Silva",
  "idade": 25,
  "cpf": "12345678901",
  "numeroConta": "123456",
  "saldo": 1000.0
}

>Listar Todos

URL: GET /api/users

Exemplo de Resposta:

[
  {
    "nome": "João Silva",
    "idade": 25,
    "cpf": "12345678901",
    "numeroConta": "123456",
    "saldo": 1000.0
  }
]

### 3. Transferência entre Contas

URL: POST /transacoes/realizar

Corpo da Requisição:

{
  "contaOrigem": "123456",
  "contaDestino": "654321",
  "valor": 200.0
}

Resposta de Sucesso:

{
  "mensagem": "Transferência realizada com sucesso!"
}

 > Validações:

-Ambas as contas devem existir.

-Saldo suficiente na conta de origem.

### Regras de Negócio

**Cadastro de Usuários**

-Nome, idade, CPF e número da conta são obrigatórios.

-Idade mínima de 18 anos.

-CPF deve ser único no sistema.

**Consulta de Usuários**

-Buscar por ID ou listar todos os usuários cadastrados.

-A resposta inclui: nome, idade, CPF, número da conta e saldo.

**Transferências**

-Somente entre contas existentes.

-Saldo do remetente deve ser maior ou igual ao valor da transferência.

-Contas inexistentes resultam em erro 404 Not Found.

### Considerações Finais

Este projeto foi desenvolvido para demonstrar:

Boas práticas no desenvolvimento de APIs REST com Spring Boot e JAVA.

Uso de um banco de dados em memória (H2) para simplificar o processo de desenvolvimento e testes.
