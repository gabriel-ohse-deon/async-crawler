# Async Crawler Java

An asynchronous crawler that searches for keywords on websites and stores the URLs where they were found.
Developed in Java 21, focusing on efficiency, scalability, and modern design patterns.
The project utilizes the Maven ecosystem and is fully containerized with Docker to facilitate deployment and execution.

## Technologies Used

* **Java 21**: Utilizing the latest language features, such as improvements in concurrency and syntax.
* **Maven**: Dependency management and build automation.
* **Spark Java**: A lightweight web framework for the crawler interface.
* **Gson**: High-performance JSON processing with Pretty Printing support.
* **Docker**: Isolated and reproducible environment for execution.
* **JUnit 5 & Mockito**: Quality assurance through unit and integration tests.

## Configuration Architecture

The project uses a flexible **Service Provider Interface (SPI)** system to load configuration providers dynamically. The parameter search hierarchy follows this order of priority:

1.  **Environment Variables**: Checked first (via `EnvConfigProvider`), facilitating configurations in Cloud environments and Docker containers.
2.  **JSON Files**: Used as a fallback (via `FileConfigProvider`) for structured configurations in the file system.
3.  **Default Values (Hardcoded)**: For specific infrastructure cases where a JSON file would be redundant (such as the data storage path), the system uses a secure default value defined in the code.

### Example: Storage Management
The system uses the `CrawlStoragePathProvider` class to determine the destination of the results:
* It attempts to read the `CRAWL_DATA_DIR` environment variable.
* If not configured, it defaults to the `data/crawls` folder at the project root.
* The `JsonCrawlRepository` ensures the automatic creation of this folder structure on first use (using `mkdirs()`).

## How to Run

### Prerequisites
* Docker installed.
* (Optional) Maven 3.9+ and Java 21 if you wish to run locally without a container.

### Via Docker (Recommended)
To compile and run the project in an isolated container:

```bash
# Image build (forcing a clean build)
docker build --no-cache -t async-crawler .

# Container execution
docker run -p 4567:4567 async-crawler