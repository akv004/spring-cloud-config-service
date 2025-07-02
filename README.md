# Spring Cloud Config Service

This project provides a Spring Cloud Config Server that aggregates configuration from a MySQL database and Azure Key Vault. It is configured to run on **JDK 17** and uses only Spring Boot's auto-configuration.

## Building

```bash
mvn package
```

## Running

Set the following environment variables:

- `AZURE_TENANT_ID`, `AZURE_CLIENT_ID`, `AZURE_CLIENT_SECRET` – credentials for Azure Government Cloud
- `AZURE_KEY_VAULT_ENDPOINT` – Key Vault endpoint
- `MYSQL_HOST`, `MYSQL_PORT`, `MYSQL_DB`, `MYSQL_USER`, `MYSQL_PASSWORD` – MySQL database connection information

Then run:

```bash
java -jar target/spring-cloud-config-service-0.0.1-SNAPSHOT.jar
```
