# Trinity-Project

- A project in the course 'Distributed Systems'
- Members of the Trinity-Project
  - Björn Franke
  - Fabian Drees
  - Lars Hieronymi
  - Simon Stockhause

## Requirements

[Requirements Document](./docs/Requirements%20für%20eFridge.com.pdf)

## Starting the project

- Requirements: `docker & docker-compose`

- Run start script `./run.sh`

## Architecture

![architecture](./img/architecture.svg)

## Sample Requests

### Order

- [order 1](./docs/requests/orderRequest.json)
- [order 2](./docs/requests/orderRequest2.json)
- [order 3](./docs/requests/orderRequest3.json)

### Ticket

- Submit Ticket: [Submit Ticket](./docs/requests/supportTicketRequest.json)
- Edit Ticket: [edit ticket](./doc/requests/supportTicketText.json)

## Example Requests

### Order

```bash
curl -XPOST -H "Content-type: application/json" -d '{
  "customer_id": "b9effa6e-d93a-11eb-b8bc-0242ac130003",
  "products": [
    {
      "product_id": "10b69f3e-d66a-11eb-b8bc-0242ac130003",
      "count": 1
    },
    {
      "product_id": "9e9383ca-d855-11eb-b8bc-0242ac130003",
      "count": 2
    }
  ]
}' 'localhost:8080/order/'
```


### Ticket

create ticket in HQ:

```bash
curl -XPOST -H "Content-type: application/json" -d '{
  "customerId" : "10b6a0b0-d66a-11eb-b8bc-0242ac130003",
  "text" : "Mein Kühlschrank geht nicht mehr zuuuu."
}' 'localhost:8080/ticket'
```


update ticket in Support Service:
```bash
curl -XPATCH -H "Content-type: application/json" -d '{
	"supportTicketId": "0f0b42ce-d855-11eb-b8bc-0242ac130003",
	"status": "OPEN",
	"text": "KLAPPE ZU AFFE TOD"
}' 'localhost:8081/ticket'
```
### Administration: restart headquarter

```bash
curl -XGET 'localhost:8080/restart'
```

## Our range of products

- [Supplier](./docs/supplier.md)
- [Products](./docs/products.md)
- [Parts](./docs/parts.md)
- [Conditions](./docs/conditions.md)

## Greatest learning successes

![reactive programming](./img/reactiveprogramming.jpg)
![estimated solution](./img/estimated-solution.jpg)
![database dev](./img/database-dev.jpg)
![null problemo](./img/null-problem-löser.png)