# Gerenciador de Tarefas com ZIO

## Grupo 3: Projeto com o Framework ZIO

Este projeto foi desenvolvido pelo Grupo 3 para explorar as funcionalidades do framework **ZIO**, utilizando-o para criar uma API capaz de gerenciar uma lista de tarefas.

### Funcionalidades da API
- Adicionar tarefas.
- Atualizar tarefas.
- Listar todas as tarefas.
- Excluir tarefas.

---

## Objetivo do Projeto

Este trabalho tem como objetivo aprofundar o conhecimento prático e teórico sobre os principais frameworks da linguagem **Scala**, promovendo a compreensão de como cada ferramenta pode auxiliar no desenvolvimento de aplicações funcionais.

Cada grupo recebeu a missão de estudar um framework específico, implementando um projeto demonstrativo que evidencie suas capacidades e preparando uma apresentação detalhada sobre as principais características e benefícios do framework estudado.

O Grupo 3 recebeu o **ZIO**, destacando sua robustez para a construção de aplicações funcionais reativas.  

---

Aqui está a seção sobre **Dependências** e **Como Rodar o Projeto** no README.md:


## Dependências  

O projeto utiliza as seguintes dependências no arquivo `build.sbt`:  

- **ZIO Core**: Biblioteca principal para programação funcional e reativa.  
  - `"dev.zio" %% "zio" % "2.1.1"`  
- **ZIO JSON**: Para serialização e desserialização de objetos JSON.  
  - `"dev.zio" %% "zio-json" % "0.6.2"`  
- **ZIO HTTP**: Framework para construir servidores HTTP.  
  - `"dev.zio" %% "zio-http" % "3.0.0-RC8"`  
- **Quill com ZIO**: Integração com Quill para manipulação de banco de dados usando ZIO.  
  - `"io.getquill" %% "quill-zio" % "4.7.0"`  
  - `"io.getquill" %% "quill-jdbc-zio" % "4.7.0"`  
- **H2 Database**: Banco de dados relacional para testes e desenvolvimento.  
  - `"com.h2database" % "h2" % "2.2.224"`  

### Requisitos para Executar o Projeto  
- **Scala**: Versão 3.3.4 ou superior.  
- **SBT (Scala Build Tool)**: Ferramenta de build para Scala e Java.  
- **Java**: JDK 17 ou superior.  

---

## Como Rodar o Projeto  

1. **Clone o repositório**:  
   ```bash
   git clone https://github.com/franckallyson/lista-de-tarefas-zio.git
   cd lista-de-tarefas-zio
   ```

2. **Compile as dependências**:  
   No terminal, execute o comando:
   ```bash
   sbt update
   sbt compile
   ```

3. **Inicie a aplicação**:  
   Para rodar a API localmente, use o comando:
   ```bash
   sbt run
   ```

4. **Acesse a API**:  
   Por padrão, a API estará disponível em:
   ```
   http://localhost:8080
   ```

Pronto! A aplicação estará rodando, pronta para gerenciar sua lista de tarefas.

---

## Estrutura do Projeto

### Modelo de Dados: [Task.scala](src/main/scala/tasks/Task.scala)

A API utiliza uma estrutura clara para representar as tarefas gerenciadas. Abaixo estão os detalhes do modelo utilizado:

#### Classe `Task`

A classe `Task` define a estrutura básica de uma tarefa antes de ser armazenada no sistema.

- **`title: String`**  
  O título ou nome da tarefa.
- **`description: String`**  
  Uma descrição que detalha a tarefa.
- **`isCompleted: Boolean`**  
  Um indicador que informa se a tarefa foi concluída ou não.

#### Classe `TaskReturn`

A classe `TaskReturn` representa uma tarefa após ser criada ou consultada, incluindo informações adicionais como o identificador único.

- **`id: UUID`**  
  Um identificador único para a tarefa.
- **`title: String`**  
  O título ou nome da tarefa.
- **`description: String`**  
  A descrição da tarefa.
- **`isCompleted: Boolean`**  
  O status indicando se a tarefa foi concluída.

Ambas as classes são serializáveis, utilizando as seguintes ferramentas:

- **`zio.json`**: Para serialização e desserialização de JSON.
- **`zio.schema`**: Para derivar schemas automaticamente.

### Observações

- Comentários no código indicam que as propriedades `createdAt` e `updatedAt` podem ser adicionadas futuramente para rastrear quando a tarefa foi criada ou atualizada pela última vez.
- A integração com `zio.schema` permite a validação e manipulação avançada de dados no formato especificado.

Essa estrutura permite gerenciar tarefas com simplicidade e eficiência, garantindo que as informações básicas e identificadores estejam sempre disponíveis.  

---

## Repositório de Tarefas: [TaskRepo.scala](src/main/scala/tasks/TaskRepo.scala)

A interface e o objeto `TaskRepo` são responsáveis por encapsular as operações que manipulam as tarefas no sistema. Eles seguem o estilo funcional e reativo proporcionado pelo framework **ZIO**, garantindo que as operações sejam seguras e eficientes.

### Interface `TaskRepo`

A interface `TaskRepo` define os métodos principais para gerenciar as tarefas:

- **`register(task: Task): zio.Task[String]`**  
  Registra uma nova tarefa e retorna um identificador único.

- **`update(id: String, updatedTask: Task): zio.Task[Boolean]`**  
  Atualiza uma tarefa existente com base no identificador fornecido. Retorna `true` se a atualização foi bem-sucedida.

- **`lookup(id: String): zio.Task[Option[TaskReturn]]`**  
  Busca uma tarefa pelo identificador. Retorna a tarefa encontrada ou `None` se não existir.

- **`tasks: zio.Task[List[TaskReturn]]`**  
  Retorna uma lista de todas as tarefas registradas.

- **`delete(id: String): zio.Task[Boolean]`**  
  Remove uma tarefa com base no identificador. Retorna `true` se a exclusão foi bem-sucedida.

### Objeto Companion `TaskRepo`

O objeto companion `TaskRepo` fornece implementações utilitárias para as operações definidas na interface, usando a funcionalidade de injeção de dependência do ZIO com `ZIO.serviceWithZIO`.

#### Funções disponíveis

- **`register(task: Task): ZIO[TaskRepo, Throwable, String]`**  
  Chama o método `register` de um serviço `TaskRepo` disponível no ambiente ZIO.

- **`update(id: String, updatedTask: Task): ZIO[TaskRepo, Throwable, Boolean]`**  
  Chama o método `update` para atualizar uma tarefa no repositório.

- **`lookup(id: String): ZIO[TaskRepo, Throwable, Option[TaskReturn]]`**  
  Facilita a busca de uma tarefa no repositório.

- **`tasks: ZIO[TaskRepo, Throwable, List[TaskReturn]]`**  
  Obtém todas as tarefas registradas no repositório.

- **`delete(id: String): ZIO[TaskRepo, Throwable, Boolean]`**  
  Remove uma tarefa usando o serviço `TaskRepo`.

### Benefícios

- **Segurança e Resiliência**: Usando tipos como `zio.Task`, as operações são seguras e tratam erros de maneira funcional.
- **Modularidade**: A interface permite substituir ou estender o repositório facilmente.
- **Integração com ZIO**: Aproveita a injeção de dependência do ZIO, simplificando o código e promovendo boas práticas.

---

## Rotas da API: [TaskRoutes.scala](src/main/scala/tasks/TaskRoutes.scala)

O arquivo `TaskRoutes.scala` define as rotas da API para gerenciar a lista de tarefas. Ele utiliza o framework **ZIO HTTP** para lidar com requisições HTTP e interagir com o repositório de tarefas (`TaskRepo`).

### Descrição das Rotas

1. **`POST /tasks`**
    - **Descrição**: Registra uma nova tarefa.
    - **Entrada**: JSON contendo os dados da tarefa no formato `{ "title": "...", "description": "...", "isCompleted": false }`.
    - **Saída**:
        - Código 200: Identificador da tarefa criada.
        - Código 400: Erro de validação do corpo da requisição.
        - Código 500: Erro interno ao registrar a tarefa.

2. **`GET /tasks/:id`**
    - **Descrição**: Recupera uma tarefa pelo identificador.
    - **Entrada**: Identificador da tarefa como parâmetro de URL.
    - **Saída**:
        - Código 200: JSON com os dados da tarefa.
        - Código 404: Tarefa não encontrada.
        - Código 500: Erro interno ao buscar a tarefa.

3. **`GET /tasks`**
    - **Descrição**: Lista todas as tarefas registradas.
    - **Entrada**: Nenhum parâmetro.
    - **Saída**:
        - Código 200: JSON com a lista de tarefas.
        - Código 500: Erro interno ao listar as tarefas.

4. **`PUT /tasks/:id`**
    - **Descrição**: Atualiza uma tarefa pelo identificador.
    - **Entrada**:
        - Identificador da tarefa como parâmetro de URL.
        - JSON com os dados atualizados no formato `{ "title": "...", "description": "...", "isCompleted": false }`.
    - **Saída**:
        - Código 200: Mensagem de sucesso na atualização.
        - Código 404: Tarefa não encontrada.
        - Código 400: Erro de validação do corpo da requisição.
        - Código 500: Erro interno ao atualizar a tarefa.

5. **`DELETE /tasks/:id`**
    - **Descrição**: Exclui uma tarefa pelo identificador.
    - **Entrada**: Identificador da tarefa como parâmetro de URL.
    - **Saída**:
        - Código 200: Mensagem de sucesso na exclusão.
        - Código 404: Tarefa não encontrada.
        - Código 500: Erro interno ao excluir a tarefa.

### Benefícios

- **Clareza**: Cada rota possui uma responsabilidade bem definida, facilitando a manutenção e expansão.
- **Tratamento de Erros**: Mensagens de erro claras e diferenciadas para problemas de validação, recurso não encontrado ou erros internos.
- **Integração com ZIO**: Utiliza as funções do repositório (`TaskRepo`) para realizar as operações de forma reativa e funcional.

