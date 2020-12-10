<h1 align="center">GOBARBER JAVA</h1>

![GitHub Workflow Status](https://img.shields.io/github/workflow/status/jamadeu/GoBarber_backend_java/Gobarber%20CI)

## Sobre
Projeto em desenvolvimento baseado no projeto [GOBARBER](https://github.com/jamadeu/GoBarber_backend) desenvolvido em typescript.

## Executando o projeto com Docker

* Requisito: [Docker](https://docs.docker.com/get-docker/)

Execute o docker, abra o terminal em '.../GoBarber_backend_java' e execute o comando:

```sh
docker-compose up
```

## Executando o projeto localmente
* Requisito: [Maven](https://maven.apache.org/download.cgi)

Abra o terminal em '.../GoBarber_backend_java' e execute:

```sh
mvn clean install
```
Após terminal a instalação, execute:

```sh
mvn spring-boot:run
```
## Documentação
Para documentação deste projeto foi utilizado o framework Swagger.

Com os serviços em execução, a documentação das API estará disponível em:

http://localhost:8080/swagger-ui.html

|   Username   |   Password|
|:------------:|:----------|
| admin        | admin     |