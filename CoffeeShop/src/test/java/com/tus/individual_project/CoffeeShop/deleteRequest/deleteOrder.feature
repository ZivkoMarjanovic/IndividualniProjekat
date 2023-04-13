Feature: Testing delete the Order entry in the application
  Use DELETE /order/{orderId} to delete an entry in the application

  Background: Create Order and then delete the same Order
    Given url 'http://localhost:8082'

  Scenario:Create Order entry and delete the same Order entry
    Given path '/order'
    And request {"customerName":"Aleksandra","customerEmail":"alex@email.com","productsWithQuantities":[{"productName":"DOPPIO","quantity":"1"},{"productName":"AMERICANO","quantity":"1"}]}
    And headers {Accept : 'application/json', Content-Type: 'application/json'}
    When method post
    Then status 201

    Given path '/order/' + response.orderId
    And headers {Accept : 'application/json', Content-Type: 'application/json'}
    When method delete
    Then status 204