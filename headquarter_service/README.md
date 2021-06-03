# Project Service

## prerequisite
* docker

## Building

build docker image, which builds a local docker image named `projectservice:0.0.1-SNAPSHOT`

```
mvn spring-boot:build-image
```

or 

docker build -t 

or let docker-compose handle the build
## Running

run via docker compose:

`docker-compose up`

## deployment on docker swarm

run via bash:

`./run.sh`

## HTTPIE Example
Create Project 
```
http POST localhost:8080/central/ name="sample central"
```

Get Project with example id
```
http GET localhost:8080/central/409ba8d0-a757-11eb-9c6b-0242ac190002
```

Update Project 
```
http PUT localhost:8080/central/409ba8d0-a757-11eb-9c6b-0242ac190002 name="kekw" description="hello world" id=409ba8d0-a757-11eb-9c6b-0242ac190002
```

delete Project 
```
http DELETE localhost:8080/central/409ba8d0-a757-11eb-9c6b-0242ac190002 name="kekw" description="hello world" id="409ba8d0-a757-11eb-9c6b-0242ac190002"
```

## Database Adminer

![adminer 1](/img/adminer_1.png)

![adminer 2](/img/adminer_2.png)