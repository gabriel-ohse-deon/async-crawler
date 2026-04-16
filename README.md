# Async Crawler Java

Um crawler assíncrono que procura por palavras em um site e armazena as urls onde foram encontradas.  
Desenvolvido em Java 21, focado em eficiência, escalabilidade e design patterns modernos. 
O projeto utiliza o ecossistema Maven e é totalmente conteinerizado com Docker para facilitar o deploy e a execução.

## Tecnologias Utilizadas

* **Java 21**: Utilizando as funcionalidades mais recentes da linguagem (como melhorias em concorrência e sintaxe).
* **Maven**: Gerenciamento de dependências e automação de build.
* **Spark Java**: Framework web leve para a interface do crawler.
* **Gson**: Processamento de JSON com alta performance e suporte a Pretty Printing.
* **Docker**: Ambiente isolado e reprodutível para execução.
* **JUnit 5 & Mockito**: Garantia de qualidade através de testes unitários e de integração.

## ️ Arquitetura de Configuração

O projeto utiliza um sistema flexível de **Service Provider Interface (SPI)** para carregar provedores de configuração de forma dinâmica. A hierarquia de busca de parâmetros segue esta ordem de prioridade:

1.  **Variáveis de Ambiente**: Verificadas em primeiro lugar (via `EnvConfigProvider`), facilitando configurações em ambientes Cloud e containers Docker.
2.  **Arquivos JSON**: Utilizados como fallback (via `FileConfigProvider`) para configurações estruturadas no sistema de arquivos.
3.  **Valores Padrão (Hardcoded)**: Para casos específicos de infraestrutura onde um arquivo JSON seria redundante (como o caminho de armazenamento de dados), o sistema utiliza um valor padrão seguro definido no código.

### Exemplo: Gerenciamento de Armazenamento
O sistema utiliza a classe `CrawlStoragePathProvider` para determinar o destino dos resultados:
* Ele tenta ler a variável de ambiente `CRAWL_DATA_DIR`.
* Caso não esteja configurada, utiliza por padrão a pasta `data/crawls` na raiz do projeto.
* O `JsonCrawlRepository` garante a criação automática dessa estrutura de pastas no primeiro uso (através de `mkdirs()`).

## Como Executar

### Pré-requisitos
* Docker instalado.
* (Opcional) Maven 3.9+ e Java 21 se desejar rodar localmente sem container.

### Via Docker (Recomendado)
Para compilar e rodar o projeto em um container isolado:

```bash
# Build da imagem (forçando clean build)
docker build --no-cache -t async-crawler .

# Execução do container
docker run -p 4567:4567 async-crawler