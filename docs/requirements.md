# Entitäten

* Werk (locations)
    * Produkt (product)

* Support (support)
    * Ticket (ticket)

* Lieferant
    * Einzelteile (parts)

* Bestellungen/Aufträge (order)

## Zentrale

* Verwaltung der Produkte
    
* Verwaltung der Einzelteile

## Fabrik

## Auftrag-Message von der Zentrale
``` json

        auftrag: {
            kundennummer: UUID
            produkte:[
                {    
                    produktID: UUID
                    anzahl: Number (1-n)
                    produktionsZeit: Number (1-n seconds)
                    einzelteile: [
                        einzelteilID: UUID
                        anzahl: Number (1-n)
                    ]
                }
            ]
        }
```

## Auftrag in der DB
``` json
{
    auftraeage:[
        {    
            auftragsID: UUID,
            kundennummer: UUID,
            eingangsTimestamp: Number (timestamp in milliseconds),
            status: enum{open, done},
            produkte:[
                {
                    produktID: UUID,
                    anzahl: Number (1-n),
                    status: enum{open, done},
                    produktionsZeit: Number (1-n seconds),
                    einzelteile: [
                        {
                            einzelteilID: UUID,
                            anzahl: Number (1-n)
                        }
                    ]
                }
            ],
            bestellungen: [
                {
                    orderID: UUID,
                    status: enum{open, done},
                    lieferantenID: UUID,
                    einzelteile: [
                        {
                            einzelteilFremdID: UUID (Die ID die beim Lieferanten hinterlegt ist),
                            anzahl: Number (1-n),
                            konditionsID: UUID
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
    einzelteilID: UUID
    konditionen:[
        {
            konditionsID: UUID,
            lieferantenID: UUID,
            preis: {Number, currency: enum{euro}},
            einzelteilFremdID: UUID,
            negotiationTimestamp: Number (timestamp in milliseconds)
        }
    ]

}
```

## Bestellungs-Request an den Lieferanten
### Request
``` json
{	
    einzelteile:[ 
        {
    		einzelteilFremdID: UUID,
    		negotiationTimestamp: Number (timestamp in milliseconds),
    		einzelteilFremdID: UUID,
    		anzahl: Number (1-n)
    	}
    ]
}
```
### Response 
``` json
{
    orderID: UUID,
}
```

## KPI-Message an die Zentrale
``` json
{	
    auftragsAnzahl seit Mitternacht: number (0-n)
    Anzahl der fertig produzierten Waren: number(0-n)
    
    
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