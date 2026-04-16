FROM maven:3.9.6-eclipse-temurin-21

# Define o diretório de trabalho
WORKDIR /usr/src/app

# Copia os arquivos do projeto para o container
COPY . .

# Expõe a porta do Spark Java
EXPOSE 4567

# Executa a limpeza, compilação e inicia a aplicação
ENTRYPOINT ["mvn", "clean", "compile", "exec:java", "-Dexec.mainClass=com.gabriel.backend.Main"]