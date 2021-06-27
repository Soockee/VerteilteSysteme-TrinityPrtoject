# Headquarter Service

## API DOC

- All Swagger Resources(groups) `http://localhost:8080/documentation/swagger-resources`
- Swagger UI endpoint: `http://localhost:8080/documentation/swagger-ui/`
- Swagger docs endpoint: `http://localhost:8080/v3/api-docs`


## Messaging

Headquarter defines 3 queues:
* conditions
* kip
* order

All queues are reachable over a exchange via routing key
* headquarter.exchange

The queues are bound to the exchange via corresponding routing keys
* headquarter.routing.condition
* headquarter.routing.kip
* headquarter.routing.order

!["messaging architecture"](./img/messaging.png)

## AMPQ Visualizer
https://jmcle.github.io/rabbitmq-visualizer/