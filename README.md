# API SoundList

API SoundList e uma API REST desenvolvida em Java com Spring Boot para gerenciar playlists musicais e as musicas vinculadas a elas. O projeto permite cadastrar, listar, buscar, atualizar e remover playlists e musicas, mantendo o relacionamento entre uma playlist e suas respectivas musicas.

## Objetivo do projeto

O objetivo da aplicacao e fornecer uma estrutura backend simples e organizada para um sistema de catalogo musical. A API trabalha com duas entidades principais:

- `Playlist`: representa uma lista de musicas criada pelo usuario.
- `Music`: representa uma musica cadastrada no sistema e associada a uma playlist.

Com isso, a aplicacao permite organizar musicas por playlists, consultar os dados cadastrados e manter a persistencia dessas informacoes em um banco de dados PostgreSQL.

## Tecnologias utilizadas

| Tecnologia | Finalidade |
| --- | --- |
| Java 21 | Linguagem principal da aplicacao |
| Spring Boot 4.0.6 | Framework usado para inicializacao e configuracao do projeto |
| Spring Web MVC | Criacao dos endpoints REST |
| Spring Data JPA | Persistencia e acesso ao banco de dados |
| Hibernate | Implementacao JPA usada para mapear entidades para tabelas |
| PostgreSQL | Banco de dados relacional utilizado pela aplicacao |
| Bean Validation | Validacao dos dados recebidos nas requisicoes |
| Lombok | Reducao de codigo repetitivo nas entidades |
| Maven | Gerenciamento de dependencias e build do projeto |

## Estrutura de pastas

A organizacao principal do codigo esta em `src/main/java/com/projeto/apisoundlist`.

| Pasta | Finalidade |
| --- | --- |
| `controller` | Contem os controladores REST da aplicacao. Essas classes recebem as requisicoes HTTP, definem as rotas da API e retornam as respostas para o cliente. No projeto, `MusicController` gerencia os endpoints de musicas e `PlaylistController` gerencia os endpoints de playlists. |
| `dto` | Contem os DTOs, que sao objetos usados para transportar dados entre cliente e API. Eles evitam expor diretamente as entidades do banco de dados e tambem definem quais campos entram e saem em cada operacao. Exemplos: `MusicRequestDto`, `MusicResponseDto`, `PlaylistRequestDto` e `PlaylistResponseDto`. |
| `exception` | Contem o tratamento global de erros da aplicacao. A classe `GlobalHandleException` captura excecoes como entidade nao encontrada, erro de validacao e erro interno, retornando respostas padronizadas ao cliente. |
| `mapper` | Contem classes responsaveis por converter entidades em DTOs e DTOs em entidades. Isso separa a logica de conversao da logica de negocio. `MusicMapper` converte dados de musicas e `PlaylistMapper` converte dados de playlists. |
| `model` | Contem as entidades JPA que representam as tabelas do banco de dados. Neste projeto, as entidades sao `Music` e `Playlist`. Elas definem os campos, nomes das tabelas e relacionamentos. |
| `repository` | Contem as interfaces de acesso ao banco de dados. Elas estendem `JpaRepository`, fornecendo operacoes prontas como salvar, buscar por id, listar com paginacao e excluir. |
| `service` | Contem a regra de negocio da aplicacao. As classes de service validam a existencia de registros, chamam repositories, aplicam as conversoes dos mappers e centralizam o fluxo das operacoes de CRUD. |

## Entidades e dados

### Entidade `Playlist`

Representa uma playlist cadastrada no sistema. Uma playlist pode possuir varias musicas.

Tabela no banco: `tb_playlists`

| Campo na entidade | Coluna no banco | Tipo Java | Obrigatorio | Descricao |
| --- | --- | --- | --- | --- |
| `id` | `id` | `Long` | Sim | Identificador unico da playlist. Gerado automaticamente pelo banco. |
| `name` | `name` | `String` | Sim | Nome da playlist. Possui limite de 255 caracteres. |
| `description` | `description` | `String` | Nao | Descricao opcional da playlist. Possui limite de 255 caracteres. |
| `musics` | Nao e coluna direta | `List<Music>` | Nao | Lista de musicas relacionadas a playlist. Relacionamento `OneToMany`. |

### Entidade `Music`

Representa uma musica cadastrada no sistema. Cada musica pertence a uma playlist.

Tabela no banco: `tb_musics`

| Campo na entidade | Coluna no banco | Tipo Java | Obrigatorio | Descricao |
| --- | --- | --- | --- | --- |
| `id` | `id` | `Long` | Sim | Identificador unico da musica. Gerado automaticamente pelo banco. |
| `title` | `title` | `String` | Sim | Titulo da musica. Possui limite de 255 caracteres. |
| `artist` | `artist` | `String` | Sim | Nome do artista. Possui limite de 255 caracteres. |
| `genre` | `genre` | `String` | Nao | Genero musical. Possui limite de 255 caracteres. |
| `duration` | `duration` | `Integer` | Sim | Duracao da musica. Deve ser um valor positivo. |
| `playlist` | `playlist_id` | `Playlist` | Sim na API | Playlist a qual a musica pertence. A API exige `playlistId` no cadastro e atualizacao. |

## Relacionamento entre entidades

| Relacionamento | Origem | Destino | Como esta mapeado | Descricao |
| --- | --- | --- | --- | --- |
| Uma playlist possui varias musicas | `Playlist` | `Music` | `@OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)` | A entidade `Playlist` possui uma lista de musicas no atributo `musics`. O `mappedBy` indica que o relacionamento e controlado pelo campo `playlist` da entidade `Music`. |
| Uma musica pertence a uma playlist | `Music` | `Playlist` | `@ManyToOne` com `@JoinColumn(name = "playlist_id")` | A entidade `Music` possui uma referencia para `Playlist`. No banco, essa relacao e armazenada pela coluna `playlist_id` na tabela `tb_musics`. |

Na pratica, o relacionamento e de um para muitos: uma `Playlist` pode ter varias `Music`, mas cada `Music` pertence a uma unica `Playlist`.

Ao excluir uma playlist, as musicas associadas podem ser removidas em cascata por causa de `cascade = CascadeType.ALL` e `orphanRemoval = true` no relacionamento da entidade `Playlist`.

## DTOs da aplicacao

| DTO | Uso | Campos |
| --- | --- | --- |
| `PlaylistRequestDto` | Entrada para criar ou atualizar playlist | `name`, `description` |
| `PlaylistResponseDto` | Resposta ao consultar playlist | `id`, `name`, `description`, `musics` |
| `MusicPlaylistDto` | Representacao resumida de musicas dentro de uma playlist | `id`, `title`, `artist`, `genre`, `duration` |
| `MusicRequestDto` | Entrada para criar ou atualizar musica | `title`, `artist`, `genre`, `duration`, `playlistId` |
| `MusicResponseDto` | Resposta ao consultar musica | `id`, `title`, `artist`, `genre`, `duration`, `playlistId` |
| `ExceptionResponseDto` | Resposta padronizada para erros | `status`, `errors`, `localDateTime` |

## Validacoes

| Campo | Regra |
| --- | --- |
| `PlaylistRequestDto.name` | Obrigatorio. Nao pode ser vazio. |
| `MusicRequestDto.title` | Obrigatorio. Nao pode ser vazio. |
| `MusicRequestDto.artist` | Obrigatorio. Nao pode ser vazio. |
| `MusicRequestDto.duration` | Obrigatorio e deve ser positivo. |
| `MusicRequestDto.playlistId` | Obrigatorio. Deve referenciar uma playlist existente. |

## Endpoints disponiveis

URL base local: `http://localhost:8080`

### Endpoints de playlists

| Metodo | Endpoint | Descricao | Corpo da requisicao | Resposta de sucesso |
| --- | --- | --- | --- | --- |
| `GET` | `/api/playlists` | Lista playlists com paginacao. O tamanho padrao da pagina e 10. | Nao possui | `200 OK` com `Page<PlaylistResponseDto>` |
| `GET` | `/api/playlists/{id}` | Busca uma playlist pelo id. | Nao possui | `200 OK` com `PlaylistResponseDto` |
| `POST` | `/api/playlists` | Cria uma nova playlist. | `PlaylistRequestDto` | `201 Created` com `PlaylistResponseDto` |
| `PUT` | `/api/playlists/{id}` | Atualiza uma playlist existente. | `PlaylistRequestDto` | `200 OK` com `PlaylistResponseDto` |
| `DELETE` | `/api/playlists/{id}` | Remove uma playlist pelo id. | Nao possui | `204 No Content` |

### Endpoints de musicas

| Metodo | Endpoint | Descricao | Corpo da requisicao | Resposta de sucesso |
| --- | --- | --- | --- | --- |
| `GET` | `/api/musics` | Lista musicas com paginacao. O tamanho padrao da pagina e 10. | Nao possui | `200 OK` com `Page<MusicResponseDto>` |
| `GET` | `/api/musics/{id}` | Busca uma musica pelo id. | Nao possui | `200 OK` com `MusicResponseDto` |
| `POST` | `/api/musics` | Cria uma nova musica e a associa a uma playlist existente. | `MusicRequestDto` | `201 Created` com `MusicResponseDto` |
| `PUT` | `/api/musics/{id}` | Atualiza uma musica existente e sua playlist associada. | `MusicRequestDto` | `200 OK` com `MusicResponseDto` |
| `DELETE` | `/api/musics/{id}` | Remove uma musica pelo id. | Nao possui | `204 No Content` |

## Exemplos de requisicao

### Criar playlist

`POST /api/playlists`

```json
{
  "name": "Rock classico",
  "description": "Playlist com musicas classicas de rock"
}
```

### Criar musica

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

## Paginacao

Os endpoints de listagem aceitam os parametros padrao de paginacao do Spring Data, como:

| Parametro | Exemplo | Descricao |
| --- | --- | --- |
| `page` | `/api/musics?page=0` | Numero da pagina. A primeira pagina e `0`. |
| `size` | `/api/musics?size=5` | Quantidade de registros por pagina. O padrao configurado nos controllers e 10. |
| `sort` | `/api/musics?sort=title,asc` | Ordenacao dos dados por campo e direcao. |

Exemplo completo:

```text
GET /api/playlists?page=0&size=10&sort=name,asc
```

## Tratamento de erros

A aplicacao possui tratamento global de excecoes em `GlobalHandleException`. As respostas de erro seguem o formato abaixo:

```json
{
  "status": "400",
  "errors": {
    "campo": "Mensagem de erro"
  },
  "localDateTime": "2026-06-12T10:00:00"
}
```

| Situacao | Status HTTP | Descricao |
| --- | --- | --- |
| Entidade nao encontrada | `404 Not Found` | Ocorre ao buscar, atualizar ou excluir uma playlist ou musica inexistente. |
| Dados invalidos | `400 Bad Request` | Ocorre quando algum campo obrigatorio esta ausente ou invalido. |
| Erro inesperado | `500 Internal Server Error` | Ocorre quando uma excecao nao tratada e lancada pela aplicacao. |

## Configuracao do banco de dados

A configuracao do banco esta em `src/main/resources/application.yaml`. O projeto esta configurado para utilizar PostgreSQL.

Principais configuracoes:

| Propriedade | Finalidade |
| --- | --- |
| `spring.datasource.url` | URL de conexao com o banco PostgreSQL. |
| `spring.datasource.username` | Usuario do banco de dados. |
| `spring.datasource.password` | Senha do banco de dados. |
| `spring.jpa.hibernate.ddl-auto=update` | Atualiza o schema do banco com base nas entidades JPA. |
| `spring.jpa.show-sql=true` | Exibe no console os comandos SQL executados. |

Antes de executar o projeto, verifique se o PostgreSQL esta ativo e se existe um banco de dados compativel com a URL configurada.

## Como executar o projeto

1. Verifique se o Java 21 esta instalado.
2. Verifique se o PostgreSQL esta em execucao.
3. Clone o repositório:

```bash
git clone https://github.com/Makson19/api_soundlist.git
```

4. Entre na pasta do projeto:

```bash
cd api_soundlist
```

5. Configure usuario, senha e URL do banco em `src/main/resources/application.yaml`.
6. Execute a aplicacao com Maven:

```bash
./mvnw spring-boot:run
```

A API ficara disponivel em:

```text
http://localhost:8080
```

## Fluxo resumido da aplicacao

1. O cliente envia uma requisicao HTTP para um endpoint em `controller`.
2. O controller recebe os dados em um DTO e encaminha para o `service`.
3. O service aplica a regra de negocio e usa o `repository` para acessar o banco.
4. O `mapper` converte entidades em DTOs de resposta.
5. O controller retorna a resposta HTTP para o cliente.
6. Caso ocorra algum erro, o pacote `exception` monta uma resposta padronizada.
