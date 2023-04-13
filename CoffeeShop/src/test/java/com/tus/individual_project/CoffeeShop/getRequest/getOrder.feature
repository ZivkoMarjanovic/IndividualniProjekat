Feature: To test the get endpoint to get Order by ID
  To test different cases

  Background: Get Order by Id
    Given url 'http://localhost:8082'

  Scenario: Create an Order, Get the Order and then delete it
    Given path '/order'
    And request {"customerName":"Aleksandra","customerEmail":"alex@email.com","productsWithQuantities":[{"productName":"DOPPIO","quantity":"1"},{"productName":"AMERICANO","quantity":"1"}]}
    And headers {Accept : 'application/json', Content-Type: 'application/json'}
    When method post
    Then status 201

    Given path '/order/' + response.orderId
    And header Accept = 'application/json'
    When method get # Send the get request
    Then status 200 # Send the get request

    Given path '/order/' + response.orderId
    And headers {Accept : 'application/json', Content-Type: 'application/json'}
    When method delete
    Then status 204