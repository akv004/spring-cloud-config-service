# ReamMe

This short guide summarizes how to build and debug the Spring Cloud Config Service.

## Build
```bash
mvn package
```

## Run
Set the environment variables described in `README.md` and launch the jar:
```bash
java -jar target/spring-cloud-config-service-0.0.1-SNAPSHOT.jar
```

### Enable Debug Logging
```bash
java -Dlogging.level.com.example.configserver=DEBUG \
     -jar target/spring-cloud-config-service-0.0.1-SNAPSHOT.jar
```

## Docker
```bash
docker build -t my-config-server .
docker run -p 8888:8888 my-config-server
```
