
# Spring Boot JWT Authentication Example

Este projeto demonstra a implementação de autenticação e autorização utilizando JSON Web Tokens (JWT) em uma aplicação Spring Boot com **Kotlin** como linguagem principal. A aplicação permite o registro de novos usuários, autenticação e acesso a recursos protegidos com base nas permissões atribuídas.

## Funcionalidades

- **Registro de Usuário:** Permite que novos usuários se cadastrem na aplicação.
- **Autenticação:** Usuários podem autenticar-se utilizando email e senha para obter um token JWT.
- **Autorização:** Acesso a endpoints protegidos é controlado com base nas permissões do usuário (por exemplo, ADMIN).

## Arquitetura

A aplicação segue uma arquitetura típica de Spring Boot com os seguintes componentes principais:

- **Modelos:** Representam as entidades do domínio, como `User` e `RefreshToken`.
- **Repositórios:** Interfaces que estendem `JpaRepository` para interagir com o banco de dados.
- **Serviços:** Contêm a lógica de negócio, como gerenciamento de usuários e tokens.
- **Controladores:** Definem os endpoints da API e lidam com as requisições HTTP.
- **Configurações:** Configuram a segurança da aplicação, gerenciamento de tokens e detalhes de autenticação.

## Tecnologias Utilizadas

- **Kotlin**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **JWT**
- **Hibernate**
- **PostgreSQL** 

## Endpoints Principais

- `POST /api/auth`: Autentica um usuário e retorna tokens de acesso e refresh.
- `POST /api/auth/refresh`: Gera um novo token de acesso utilizando um token de refresh válido.
- `POST /api/user/create`: Registra um novo usuário.
- `GET /api/user`: Lista todos os usuários (requer permissão de ADMIN).
- `GET /api/user/{uuid}`: Obtém detalhes de um usuário específico por UUID (requer permissão de ADMIN).
- `DELETE /api/user/{uuid}`: Deleta um usuário específico por UUID (requer permissão de ADMIN).

## Configuração de Segurança

A segurança da aplicação é configurada para:

- **Desabilitar CSRF:** Não é necessário para APIs RESTful que não utilizam cookies para autenticação.
- **Gerenciamento de Sessão Stateless:** A aplicação não mantém estado de sessão no servidor; cada requisição deve incluir um token válido.
- **Filtros de Autenticação:** Um filtro personalizado (`JwtAuthenticationFilter`) é utilizado para validar tokens JWT em cada requisição.
- **Provedores de Autenticação:** `DaoAuthenticationProvider` é configurado com um serviço de detalhes do usuário e um codificador de senha.

## Como Executar

1. **Pré-requisitos:**
   - JDK 17 ou superior instalado.
   - Banco de dados PostgreSQL 17.1 configurado e acessível.
   - Variáveis de ambiente ou arquivo de propriedades configurado com as informações do banco de dados e propriedades JWT.

2. **Clonar o Repositório:**
   ```bash
   git clone https://github.com/viniciusdsandrade/spring-security-with-kotlin.git
   cd jwt
   ```

3. **Configurar o Banco de Dados:**
   Atualize o arquivo `application.properties` ou `application.yml` com as configurações do PostgreSQL.

4. **Compilar e Executar:**
   ```bash
   ./mvnw spring-boot:run
   ```

## Utilizando a API com Postman

Para testar e utilizar a API no Postman siga estes passos:

### 1. Criar novo usuário

- **Método:** POST
- **URL:** `http://localhost:8080/api/user/create`
- **Body (JSON):**
```json
{
  "email": "usuario@example.com",
  "password": "senhaSegura"
}
```

### 2. Autenticar usuário

- **Método:** POST
- **URL:** `http://localhost:8080/api/auth`
- **Body (JSON):**
```json
{
  "email": "usuario@example.com",
  "password": "senhaSegura"
}
```

Resposta esperada:
```json
{
  "accessToken": "tokenJWT",
  "refreshToken": "tokenRefresh"
}
```

### 3. Acessar rotas protegidas

- **Método:** GET
- **URL:** `http://localhost:8080/api/user`
- **Headers:**
  - `Authorization: Bearer <accessToken>`

Lembre-se de substituir `<accessToken>` pelo token retornado na autenticação.

### 4. Atualizar Token de Acesso

- **Método:** POST
- **URL:** `http://localhost:8080/api/auth/refresh`
- **Body (JSON):**
```json
{
  "token": "<refreshToken>"
}
```

Resposta esperada:
```json
{
  "token": "novoAccessToken"
}
```

Utilize este novo token nas requisições futuras.

## Variáveis de Ambiente

Para executar corretamente a aplicação, você precisa configurar as variáveis de ambiente abaixo:

```env
JWT_KEY=q6mIYl2xzP3yVtkUfxgBq34Wz4VdD2NdZpR2f1LfJpM
POSTGRES_DB=db_auth
POSTGRES_USER=postgres
POSTGRES_PASSWORD=GhostSthong567890@
```

### Configurando com arquivo `.env`

Crie um arquivo chamado `.env` na raiz do seu projeto com as variáveis acima. Exemplo:

```bash
touch .env
```

Em seguida, copie o conteúdo das variáveis acima para dentro do arquivo `.env`:

```env
JWT_KEY=q6mIYl2xzP3yVtkUfxgBq34Wz4VdD2NdZpR2f1LfJpM
POSTGRES_DB=db_auth
POSTGRES_USER=postgres
POSTGRES_PASSWORD=GhostSthong567890@
```

Este arquivo será carregado automaticamente se você estiver usando ferramentas como Docker Compose, IntelliJ IDEA ou se você utilizar uma biblioteca como `dotenv` em conjunto com sua aplicação Spring.

### Usando no IntelliJ IDEA

1. Abra as configurações da sua execução (Run/Debug Configurations).
2. Vá até a seção "Environment Variables" e clique em "Load variables from file".
3. Selecione seu arquivo `.env`.
## Considerações Finais

Este projeto serve como uma base para implementar autenticação e autorização utilizando JWT em aplicações Spring Boot com Kotlin. Sinta-se à vontade para expandir e adaptar conforme as necessidades específicas do seu projeto.

Para mais informações sobre JWT e Spring Security, consulte a [documentação oficial do Spring Security](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html).
