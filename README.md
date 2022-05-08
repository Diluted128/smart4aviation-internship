## smart4viation-internship ✈️

## Run
To run the app use the command below in the root directory of pulled repository:
```
docker-compose up
```
After putting some changes to the project just use the same command. The .jar file will be generated automatically.

## API
```
/flight               POST
```
Saves generated flights placed in request body.
```
/cargo               POST
```
Assigns generated cargos to provided flights.
```
/flight/{flightNumber}/weight?date={dateUTC}               GET
```
Return weight summary about the specified flight.
```
/flight/airport/{IATA}?date={date[YYYY-MM-DD]}               GET 
```
Returns airport summary about the specified airport.
## Input data
To generate input data concerning fligts and their cargo use code below. JSON generator is available [here](https://json-generator.com/)<br/>

**Flight**
```
[
 '{{repeat(1000)}}',
 {
 flightId: '{{index() + 1}}',
 flightNumber: '{{integer(1000, 9999)}}',
 departureAirportIATACode: '{{random("SEA","YYZ","YYT","ANC","LAX", "MIT","LEW","GDN","KRK","PPX")}}',
 arrivalAirportIATACode: '{{random("SEA","YYZ","YYT","ANC","LAX","MIT","LEW","GDN","KRK","PPX")}}',
 departureDate: '{{date(new Date(2022, 4, 0), new Date(2022, 4, 20), "YYYY-MM-ddThh:mm:ssZ")}}'
 }
]
```

**Cargo**
```
[
 '{{repeat(10000)}}',
 {
 flightId: '{{index() + 1}}',
 baggage: [
 '{{repeat(3,8)}}',
 {
 id: '{{index()}}',
 weight: '{{integer(1, 999)}}',
 weightUnit: '{{random("kg","lb")}}',
 pieces: '{{integer(1, 999)}}'
 }
 ],
 cargo: [
 '{{repeat(3,5)}}',
 {
 id: '{{index()}}',
 weight: '{{integer(1, 999)}}',
 weightUnit: '{{random("kg","lb")}}',
 pieces: '{{integer(1, 999)}}'
 }
 ]
 }
]
```
Amount of generated entities has been increased. Departure date was limit to 0-20 day of May to check statistic generation about all flights and baggages in specyfic airport. After generation there would be often flights with the same arrival and departure airports codes. Before saving flights to db they should be appropriately filltred.  

