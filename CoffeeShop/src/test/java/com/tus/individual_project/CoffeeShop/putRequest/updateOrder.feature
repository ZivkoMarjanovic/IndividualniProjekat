Feature: Testing update the Order entry in the application
  Use PUT /order/{orderId} to create a job entry in the application

  Background: Create and initialise the base url
    Given url 'http://localhost:8082'

  Scenario:Create Order entry and update customerName on the same Order entry
    Given path '/order'
    And request {"customerName":"Aleksandra","customerEmail":"alex@email.com","productsWithQuantities":[{"productName":"DOPPIO","quantity":"1"},{"productName":"AMERICANO","quantity":"1"}]}
    And headers {Accept : 'application/json', Content-Type: 'application/json'}
    When method post
    Then status 201

    Given path '/order/' + response.orderId
    And request {"customerName":"Vidak","customerEmail":"alex@email.com","productsWithQuantities":[{"productName":"DOPPIO","quantity":"1"},{"productName":"AMERICANO","quantity":"1"}]}
    And headers {Accept : 'application/json', Content-Type: 'application/json'}
    When method put
    Then status 200
    And response.customerName == "Vidak"