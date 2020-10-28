# Reactive Client

This is a new scalable application that allows its existing users to \
place some payload.
Implemented using reactive style programming, from endpoint handling to database queries.

System consist of two microservices: Client and Server. \
They are communicating directly via REST to each other. 

This service is **Client**.
The **Server** example is here [https://github.com/sergey-rubtsov/reactive-server](https://github.com/sergey-rubtsov/reactive-server)

It sends a new request to the Server microservice every 2 seconds to url \
[http://localhost:8086/add](http://localhost:8086/add)
Server makes some calculations and turning back request Id and some value as a response.

## Client

Run it with gradle task bootRun

Client db, server url, possible random request string and port can be configured in application.yml
port is random by default

---

Client payload request has format: 

  - requestId

  - data (string, for example: "AAAA", "BBBB", "DDDD", "EE" or any other)

  - quantity

Example:

`
{  
    "requestId":  "5389302e-0095-422b-8b23-87c16ca66153",  
    "data":   "AAAA",  
    "quantity": 42  
}
`

- Client is printing new request data and response data into the standard output and log

Server is calculating payload response value based on this formula:

  - responseNumber = quantity * random number from 1.1 to 2.0
  
Example:

`
{
    "requestId":      "5389302e-0095-422b-8b23-87c16ca66153";
    "responseNumber":   1.2
}
`
With default configuration metrics are available by url:

[http://localhost:8086/metrics](http://localhost:8086/metrics)

Server metrics endpoint returns 2 properties:

  - total count of processed requests

  - average responseNumber
  
Example:

`
{
    "total":    72,
    "average":  68.59713168127218613333333333333333333333333
}
`