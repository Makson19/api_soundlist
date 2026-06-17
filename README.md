# API SoundList

API SoundList é uma API REST desenvolvida em Java com Spring Boot para gerenciar playlists musicais e as músicas vinculadas a elas. O projeto permite cadastrar, listar, buscar, atualizar e remover playlists e músicas, mantendo o relacionamento entre uma playlist e suas respectivas músicas.

## Objetivo do projeto

O objetivo da aplicação e fornecer uma estrutura backend simples e organizada para um sistema de catálogo musical. A API trabalha com duas entidades principais:

- `Playlist`: representa uma lista de músicas criada pelo usuário.
- `Music`: representa uma música cadastrada no sistema e associada a uma playlist.

Com isso, a aplicação permite organizar músicas por playlists, consultar os dados cadastrados e manter a persistência dessas informações em um banco de dados PostgreSQL.

## Tecnologias utilizadas

| Tecnologia | Finalidade |
| --- | --- |
| Java 21 | Linguagem principal da aplicação |
| Spring Boot 4.0.6 | Framework usado para inicialização e configuração do projeto |
| Spring Web MVC | Criação dos endpoints REST |
| Spring Data JPA | Persistência e acesso ao banco de dados |
| Hibernate | Implementação JPA usada para mapear entidades para tabelas |
| PostgreSQL | Banco de dados relacional utilizado pela aplicação |
| Bean Validation | Validação dos dados recebidos nas requisições |
| Lombok | Redução de codigo repetitivo nas entidades |
| Maven | Gerenciamento de dependências e build do projeto |

## Estrutura de pastas

A organização principal do código esta em `src/main/java/com/projeto/apisoundlist`.

| Pasta | Finalidade |
| --- | --- |
| `controller` | Contém os controladores REST da aplicação. Essas classes recebem as requisições HTTP, definem as rotas da API e retornam as respostas para o cliente. No projeto, `MusicController` gerencia os endpoints de músicas e `PlaylistController` gerencia os endpoints de playlists. |
| `dto` | Contém os DTOs, que são objetos usados para transportar dados entre cliente e API. Eles evitam expor diretamente as entidades do banco de dados e também definem quais campos entram e saem em cada operação. Exemplos: `MusicRequestDto`, `MusicResponseDto`, `PlaylistRequestDto` e `PlaylistResponseDto`. |
| `exception` | Contém o tratamento global de erros da aplicação. A classe `GlobalHandleException` captura exceções como entidade não encontrada, erro de validação e erro interno, retornando respostas padronizadas ao cliente. |
| `mapper` | Contém classes responsáveis por converter entidades em DTOs e DTOs em entidades. Isso separa a lógica de conversão da lógica de negócio. `MusicMapper` converte dados de musicas e `PlaylistMapper` converte dados de playlists. |
| `model` | Contém as entidades JPA que representam as tabelas do banco de dados. Neste projeto, as entidades são `Music` e `Playlist`. Elas definem os campos, nomes das tabelas e relacionamentos. |
| `repository` | Contém as interfaces de acesso ao banco de dados. Elas estendem `JpaRepository`, fornecendo operações prontas como salvar, buscar por id, listar com paginação e excluir. |
| `service` | Contém a regra de negócio da aplicação. As classes de service validam a existência de registros, chamam repositories, aplicam as conversões dos mappers e centralizam o fluxo das operações de CRUD. |

## Entidades e dados

### Entidade `Playlist`

Representa uma playlist cadastrada no sistema. Uma playlist pode possuir várias músicas.

Tabela no banco: `tb_playlists`

| Campo na entidade | Coluna no banco | Tipo Java | Obrigatorio | Descricao |
| --- | --- | --- | --- | --- |
| `id` | `id` | `Long` | Sim | Identificador único da playlist. Gerado automaticamente pelo banco. |
| `name` | `name` | `String` | Sim | Nome da playlist. Possui limite de 255 caracteres. |
| `description` | `description` | `String` | Não | Descrição opcional da playlist. Possui limite de 255 caracteres. |
| `musics` | Não e coluna direta | `List<Music>` | Não | Lista de músicas relacionadas a playlist. Relacionamento `OneToMany`. |

### Entidade `Music`

Representa uma música cadastrada no sistema. Cada música pertence a uma playlist.

Tabela no banco: `tb_musics`

| Campo na entidade | Coluna no banco | Tipo Java | Obrigatorio | Descricao |
| --- | --- | --- | --- | --- |
| `id` | `id` | `Long` | Sim | Identificador único da música. Gerado automaticamente pelo banco. |
| `title` | `title` | `String` | Sim | Título da música. Possui limite de 255 caracteres. |
| `artist` | `artist` | `String` | Sim | Nome do artista. Possui limite de 255 caracteres. |
| `genre` | `genre` | `String` | Não | Genero musical. Possui limite de 255 caracteres. |
| `duration` | `duration` | `Integer` | Sim | Duração da música. Deve ser um valor positivo. |
| `playlist` | `playlist_id` | `Playlist` | Sim na API | Playlist a qual a música pertence. A API exige `playlistId` no cadastro e atualização. |

## Relacionamento entre entidades

| Relacionamento | Origem | Destino | Como esta mapeado | Descricao |
| --- | --- | --- | --- | --- |
| Uma playlist possui várias músicas | `Playlist` | `Music` | `@OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)` | A entidade `Playlist` possui uma lista de músicas no atributo `musics`. O `mappedBy` indica que o relacionamento e controlado pelo campo `playlist` da entidade `Music`. |
| Uma música pertence a uma playlist | `Music` | `Playlist` | `@ManyToOne` com `@JoinColumn(name = "playlist_id")` | A entidade `Music` possui uma referência para `Playlist`. No banco, essa relação e armazenada pela coluna `playlist_id` na tabela `tb_musics`. |

Na prática, o relacionamento e de um para muitos: uma `Playlist` pode ter várias `Music`, mas cada `Music` pertence a uma única `Playlist`.

Ao excluir uma playlist, as músicas associadas podem ser removidas em cascata por causa de `cascade = CascadeType.ALL` e `orphanRemoval = true` no relacionamento da entidade `Playlist`.

## DTOs da aplicação

| DTO | Uso | Campos |
| --- | --- | --- |
| `PlaylistRequestDto` | Entrada para criar ou atualizar playlist | `name`, `description` |
| `PlaylistResponseDto` | Resposta ao consultar playlist | `id`, `name`, `description`, `musics` |
| `MusicPlaylistDto` | Representação resumida de músicas dentro de uma playlist | `id`, `title`, `artist`, `genre`, `duration` |
| `MusicRequestDto` | Entrada para criar ou atualizar música | `title`, `artist`, `genre`, `duration`, `playlistId` |
| `MusicResponseDto` | Resposta ao consultar música | `id`, `title`, `artist`, `genre`, `duration`, `playlistId` |
| `ExceptionResponseDto` | Resposta padronizada para erros | `status`, `errors`, `localDateTime` |

## Validações

| Campo | Regra |
| --- | --- |
| `PlaylistRequestDto.name` | Obrigatório. Não pode ser vazio. |
| `MusicRequestDto.title` | Obrigatório. Não pode ser vazio. |
| `MusicRequestDto.artist` | Obrigatório. Não pode ser vazio. |
| `MusicRequestDto.duration` | Obrigatório e deve ser positivo. |
| `MusicRequestDto.playlistId` | Obrigatório. Deve referenciar uma playlist existente. |

## Endpoints disponíveis

URL base local: `http://localhost:8080`

### Endpoints de playlists

| Método | Endpoint | Descrição | Corpo da requisição | Resposta de sucesso |
| --- | --- | --- | --- | --- |
| `GET` | `/api/playlists` | Lista playlists com paginação. O tamanho padrão da página e 10. | Não possui | `200 OK` com `Page<PlaylistResponseDto>` |
| `GET` | `/api/playlists/{id}` | Busca uma playlist pelo id. | Não possui | `200 OK` com `PlaylistResponseDto` |
| `POST` | `/api/playlists` | Cria uma nova playlist. | `PlaylistRequestDto` | `201 Created` com `PlaylistResponseDto` |
| `PUT` | `/api/playlists/{id}` | Atualiza uma playlist existente. | `PlaylistRequestDto` | `200 OK` com `PlaylistResponseDto` |
| `DELETE` | `/api/playlists/{id}` | Remove uma playlist pelo id. | Não possui | `204 No Content` |

### Endpoints de músicas

| Metodo | Endpoint | Descrição | Corpo da requisição | Resposta de sucesso |
| --- | --- | --- | --- | --- |
| `GET` | `/api/musics` | Lista músicas com paginação. O tamanho padrão da página e 10. | Não possui | `200 OK` com `Page<MusicResponseDto>` |
| `GET` | `/api/musics/{id}` | Busca uma música pelo id. | Não possui | `200 OK` com `MusicResponseDto` |
| `POST` | `/api/musics` | Cria uma nova música e a associa a uma playlist existente. | `MusicRequestDto` | `201 Created` com `MusicResponseDto` |
| `PUT` | `/api/musics/{id}` | Atualiza uma música existente e sua playlist associada. | `MusicRequestDto` | `200 OK` com `MusicResponseDto` |
| `DELETE` | `/api/musics/{id}` | Remove uma música pelo id. | Não possui | `204 No Content` |

## Exemplos de requisição

### Criar playlist

`POST /api/playlists`

```json
{
  "name": "Rock clássico",
  "description": "Playlist com músicas clássicas de rock"
}
```

### Criar música

`POST /api/musics`

```json
{
  "title": "Bohemian Rhapsody",
  "artist": "Queen",
  "genre": "Rock",
  "duration": 354,
  "playlistId": 1
}
```

## Paginação

Os endpoints de listagem aceitam os parâmetros padrão de paginação do Spring Data, como:

| Parâmetro | Exemplo | Descrição |
| --- | --- | --- |
| `page` | `/api/musics?page=0` | Número da página. A primeira página e `0`. |
| `size` | `/api/musics?size=5` | Quantidade de registros por página. O padrão configurado nos controllers e 10. |
| `sort` | `/api/musics?sort=title,asc` | Ordenação dos dados por campo e direção. |

Exemplo completo:

```text
GET /api/playlists?page=0&size=10&sort=name,asc
```

## Tratamento de erros

A aplicação possui tratamento global de exceções em `GlobalHandleException`. As respostas de erro seguem o formato abaixo:

```json
{
  "status": "400",
  "errors": {
    "campo": "Mensagem de erro"
  },
  "localDateTime": "2026-06-12T10:00:00"
}
```

| Situação | Status HTTP | Descrição |
| --- | --- | --- |
| Entidade não encontrada | `404 Not Found` | Ocorre ao buscar, atualizar ou excluir uma playlist ou música inexistente. |
| Dados inválidos | `400 Bad Request` | Ocorre quando algum campo obrigatório está ausente ou inválido. |
| Erro inesperado | `500 Internal Server Error` | Ocorre quando uma exceção não tratada e lançada pela aplicação. |

## Configuração do banco de dados

A configuração do banco esté em `src/main/resources/application.yaml`. O projeto está configurado para utilizar PostgreSQL.

Principais configurações:

| Propriedade | Finalidade |
| --- | --- |
| `spring.datasource.url` | URL de conexão com o banco PostgreSQL. |
| `spring.datasource.username` | Usuário do banco de dados. |
| `spring.datasource.password` | Senha do banco de dados. |
| `spring.jpa.hibernate.ddl-auto=update` | Atualiza o schema do banco com base nas entidades JPA. |
| `spring.jpa.show-sql=true` | Exibe no console os comandos SQL executados. |

Antes de executar o projeto, verifique se o PostgreSQL está ativo e se existe um banco de dados compatível com a URL configurada.

## Como executar o projeto

1. Verifique se o Java 21 está instalado.
2. Verifique se o PostgreSQL está em execução.
3. Clone o repositório:

```bash
git clone https://github.com/Makson19/api_soundlist.git
```

4. Entre na pasta do projeto:

```bash
cd api_soundlist
```

5. Configure usuário, senha e URL do banco em `src/main/resources/application.yaml`.
6. Execute a aplicação com Maven:

```bash
./mvnw spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

## Fluxo resumido da aplicação

1. O cliente envia uma requisição HTTP para um endpoint em `controller`.
2. O controller recebe os dados em um DTO e encaminha para o `service`.
3. O service aplica a regra de negócio e usa o `repository` para acessar o banco.
4. O `mapper` converte entidades em DTOs de resposta.
5. O controller retorna a resposta HTTP para o cliente.
6. Caso ocorra algum erro, o pacote `exception` monta uma resposta padronizada.
