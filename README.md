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
