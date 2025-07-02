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


Spring Cloud Config Server with JDBC and Azure Key VaultOverviewThis project is a centralized Spring Cloud Config Server designed to provide externalized configuration for microservices. It aggregates properties from two distinct sources, providing a flexible and secure configuration management solution.This server is built with modern dependencies, including Spring Boot 3 and JDK 17, and is specifically configured to operate securely within the Azure Government Cloud.Core FeaturesComposite Backends: Aggregates configuration from multiple sources.JDBC Backend: Loads application-specific properties from a MySQL database.Azure Key Vault Backend: Loads secrets and sensitive configuration directly from Azure Key Vault.Dynamic Configuration: Supports enabling or disabling backends using Spring Profiles for maximum flexibility.Cloud-Native: Designed to be containerized with Docker and deployed to Kubernetes (AKS).ArchitectureThe server operates on a simple principle:Startup: On startup, the server uses the spring.config.import mechanism to connect to Azure Key Vault and load all secrets into its own environment. This makes secrets like database passwords available immediately.Client Request: When a client microservice (e.g., osgp-gateway-service) requests its configuration (e.g., for the aep-al-dev profile), the Config Server's JDBC backend is activated.Aggregation: The server queries the MySQL database for properties matching the client's application name and profile. It then automatically merges these properties with the secrets loaded from Key Vault.Response: The final, merged set of properties is sent back to the client microservice.Property Source PrecedenceThe order in which properties are loaded is important. By default, properties loaded from Azure Key Vault have higher precedence.Key Vault > JDBC DatabaseThis means if a property named my.property exists in both the database and Key Vault, the value from Key Vault will be used. This is the desired behavior for secrets, as it allows you to override non-sensitive default values from the database with secure values from the vault.API Endpoint UsageClient applications fetch their configuration by making a GET request to the server. The URL follows a standard format:/{application}/{profile}/{label}application: The name of the client application (e.g., osgp-gateway-service).profile: The active Spring profile of the client (e.g., aep-al-dev, prod).label: (Optional) The git branch or tag, typically not used with the JDBC backend. Defaults to master.Example Request:A client service named osgp-gateway-service running with the aep-al-dev profile would fetch its configuration from:GET /osgp-gateway-service/aep-al-dev
ConfigurationConfiguration is managed via the src/main/resources/application.yml file and environment variables.Spring ProfilesThis server uses Spring Profiles to control which backends are active. The spring.profiles.active property is key.jdbc: Enables the MySQL database backend.default: (or no profile) The server will still load from Key Vault but will not connect to the database.To run with both JDBC and Key Vault (default):Set the active profile to jdbc. The server will load from Key Vault first, then from the database.To run with only Key Vault:Do not set any active profile, or set a different one. The spring.datasource configuration is tied to the jdbc profile and will not be activated, seamlessly bypassing the MySQL connection.Environment VariablesThe application is configured via environment variables, which should be supplied by your Kubernetes deployment secrets.VariableDescriptionCLIENT_IDThe Client ID of the Service Principal.CLIENT_SECRETThe Client Secret of the Service Principal.TENANT_IDThe Azure Active Directory Tenant ID.DB_UNAMEThe username for the MySQL database.DB_PWDThe password for the MySQL database.Security ConsiderationsAzure Key Vault PermissionsThe Service Principal defined by CLIENT_ID requires specific permissions on the target Azure Key Vault. Ensure it has been granted the "Key Vault Secrets User" role, which includes the necessary secrets/get and secrets/list permissions.How to Build and RunPrerequisitesJDK 17Apache MavenDockerBuildBuild the JAR:mvn clean package
Build the Docker Image:docker build -t your-registry/config-server:latest .
Run LocallyYou can run the application locally for testing by providing the environment variables.# Example for running with JDBC and Key Vault
export CLIENT_ID="..."
export CLIENT_SECRET="..."
export TENANT_ID="..."
export DB_UNAME="..."
export DB_PWD="..."
export SPRING_PROFILES_ACTIVE=jdbc

java -jar target/*.jar
Deployment to Kubernetes (AKS)The included Dockerfile can be used to build an image for deployment. Your Kubernetes deployment.yaml should inject the required credentials as environment variables from a Kubernetes secret.TroubleshootingProblemSolutionAADSTS900382: Cross Cloud request error on startup.The spring.cloud.azure.profile.cloud-type in application.yml is missing or incorrect. It must be set to AZURE_US_GOVERNMENT.Config data location ... does not exist error on startup.This indicates a network or permissions issue. <br> 1. Check that the Key Vault firewall allows access from your AKS cluster. <br> 2. Verify the Service Principal has the Key Vault Secrets User role. <br> 3. Ensure the CLIENT_ID, CLIENT_SECRET, and TENANT_ID environment variables are correct.Secrets are not being loaded from Key Vault.Check that the secret names in Key Vault exactly match the property names your application expects (e.g., a secret named DB_PWD for the ${DB_PWD} property).Database connection fails.Ensure the jdbc profile is active. Verify that the DB_UNAME and DB_PWD secrets are present in Key Vault and that the database URL is correct.This README is designed to be machine-readable for AI tools to understand the project's context, dependencies, and operational model.
