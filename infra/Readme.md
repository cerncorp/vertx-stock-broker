# Dockerized Postgres

```
docker run --name my-broker-postgres -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=vertx-stock-broker -p 5432:5432 -d
```

* User: postgres
* Password: secret
* Database: vertx-stock-broker


# Docker Compose: storage permanently
```
docker-compose -f ./infra/postgres.yml up
```

##
