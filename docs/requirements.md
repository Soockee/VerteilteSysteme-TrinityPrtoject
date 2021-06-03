# Entitäten

* Factory
    * Product

* Support
    * Ticket

* Supplier
    * parts

* Order 

## headquarter

* Verwaltung der Products
    
* Verwaltung der parts

## Factory

## Auftrag-Message von der Headerquarter
``` json
        order: {
            customerId: UUID
            products:[
                {    
                    productId: UUID
                    count: Number (1-n)
                    productionTime: Number (1-n seconds)
                    parts: [
                        partId: UUID
                        count: Number (1-n)
                    ]
                }
            ]
        }
```

## Auftrag in der DB
``` json
{
    productOrders:[
        {    
            productOrderId: UUID,
            customerId: UUID,
            receptionTime: Number (timestamp in milliseconds),
            status: enum{open, done},
            products:[
                {
                    productId: UUID,
                    count: Number (1-n),
                    status: enum{open, done},
                    productionTime: Number (1-n seconds),
                    parts: [
                        {
                            partId: UUID,
                            count: Number (1-n)
                        }
                    ]
                }
            ],
            partOrders: [
                {
                    partOrderId: UUID,
                    status: enum{open, done},
                    supplierId: UUID,
                    parts: [
                        {
                            partSupplierId: UUID (Die ID die beim Lieferanten hinterlegt ist),
                            count: Number (1-n),
                            conditionId: UUID
                        }
                    ]
                }
            ]
        }
    ]
}
```

## Konditionen(gecached)
``` json
{	
    partId: UUID
    conditions:[
        {
            conditionsId: UUID,
            supplierId: UUID,
            price: {Number, currency: enum{euro}},
            partSupplierId: UUID,
            negotiationTimestamp: Number (timestamp in milliseconds)
        }
    ]
}
```

## ProductOrder-Request an den Lieferanten
### Request
``` json
{	
    productOrder:[ 
        {
    		partSupplierId: UUID,
    		negotiationTimestamp: Number (timestamp in milliseconds),
    		count: Number (1-n)
    	}
    ]
}
```
### ProductOrder-Response 
``` json
{
    productOrderId: UUID,
}
```

## KPI-Message an die Zentrale
``` json
{	
    productsCount: number (0-n) //auftragsAnzahl seit Mitternacht: number (0-n)
    finishedProductsCount: number(0-n)
}
```
# Requirements



# Produktzyklus in der Fabrik

* Auftrag kommt rein

* (Fabrik prueft, ob die Einzelteile fuer die Produkte des Auftrags vorhanden sind)

* Bestellung der Einzelteile fuer den Auftrag

* (Werk Fragt zyklisch bei den Lieferanten nach dem Status seiner Bestellungen)

* Werk wartet pro Produkt des Auftrags eine **produktabhängige** und **werkabhängige** Zeit ab

* Wenn fuer alle Produkte Zeit `x` gewertet worden ist, wird der Status des Auftrags auf 'fertig' gesetzt 

# 