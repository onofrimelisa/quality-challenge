
# Quality Challenge



## Indices

* [Ungrouped](#ungrouped)

  * [Book fligth](#1-book-fligth)
  * [Book hotel](#2-book-hotel)
  * [Get all flights](#3-get-all-flights)
  * [Get all flights with filters](#4-get-all-flights-with-filters)
  * [Get all hotels](#5-get-all-hotels)
  * [Get all hotels with filters](#6-get-all-hotels-with-filters)


--------


## Ungrouped



### 1. Book fligth



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:8080/api/v1/flight-reservation
```



***Body:***

```js        
{
    "username": "arjonamiguel@gmail.com",
    "flightReservation": {
        "dateFrom": "01/03/2021",
        "dateTo": "14/03/2021",
        "destination": "buenos aires",
        "origin": "bogotá",
        "flightNumber": "boba-6567",
        "seats": 2, 
        "seatType": "economy",
        "people": [
            {
                "dni": "4568215",
                "name": "bu",
                "lastname": "bu",
                "birthDate": "10/12/2021",
                "mail": "arjonamiguel@gmail.com"
            },
            {
                "dni": "4568215",
                "name": "bu",
                "lastname": "bu",
                "birthDate": "10/12/2021",
                "mail": "arjonamiguel@gmail.com"
            }
        ],
        "paymentMethod": {
            "type": "credit",
            "number": "1234",
            "dues": 1
        }
    }
}
```



### 2. Book hotel



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: localhost:8080/api/v1/booking
```



***Body:***

```js        
{
    "username": "arjonamiguel@gmail.com",
    "booking": {
        "dateFrom": "25/02/2021",
        "dateTo": "01/03/2021",
        "destination": "puerto iguazú",
        "hotelCode": "CH-0003",
        "peopleAmount": 3, 
        "roomType": "triple",
        "people": [
            {
                "dni": "4568215",
                "name": "bu",
                "lastname": "bu",
                "birthDate": "10/25/8759",
                "mail": "arjonamiguel@gmail.com"
            },
            {
                "dni": "4568215",
                "name": "bu",
                "lastname": "bu",
                "birthDate": "10/25/8759",
                "mail": "arjonamiguel@gmail.com"
            },
            {
                "dni": "4568215",
                "name": "bu",
                "lastname": "bu",
                "birthDate": "10/25/8759",
                "mail": "arjonamiguel@gmail.com"
            }
        ],
        "paymentMethod": {
            "type": "credit",
            "number": "1234",
            "dues": 1
        }
    }
}
```



### 3. Get all flights



***Endpoint:***

```bash
Method: GET
Type: 
URL: 
```



### 4. Get all flights with filters



***Endpoint:***

```bash
Method: GET
Type: 
URL: localhost:8080/api/v1/flights
```



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| dateFrom | 11/02/2021 |  |
| dateTo | 25/03/2021 |  |
| destination | buenos aires |  |
| origin | bogotá |  |



### 5. Get all hotels



***Endpoint:***

```bash
Method: GET
Type: 
URL: localhost:8080/api/v1/hotels
```



### 6. Get all hotels with filters



***Endpoint:***

```bash
Method: GET
Type: 
URL: localhost:8080/api/v1/hotels
```



***Query params:***

| Key | Value | Description |
| --- | ------|-------------|
| dateFrom | 11/02/2021 |  |
| dateTo | 02/03/2021 |  |
| destination | puerto iguazú |  |



---
[Back to top](#quality-challenge)
> Made with &#9829; by [thedevsaddam](https://github.com/thedevsaddam) | Generated at: 2021-03-18 12:03:58 by [docgen](https://github.com/thedevsaddam/docgen)
