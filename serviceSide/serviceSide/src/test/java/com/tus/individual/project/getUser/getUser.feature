Feature: To test crud operations on User
  To test different cases

  Background: Manager can access User operations
    Given url 'http://localhost:8083'

  Scenario: Login as manager and Get All User
    Given path '/signin'
    And request {"username":"vidak","password":"vidak1"}
    And headers {Accept : 'application/json', Content-Type: 'application/json'}
    When method post
    Then status 200

    Given path '/users'
    And headers {Accept : 'application/json', Content-Type: 'application/json', Authorization: 'Bearer ' + response.token}
    When method get
    Then status 200

  Scenario: Login as manager and Create User
    Given path '/signin'
    And request {"username":"vidak","password":"vidak1"}
    And headers {Accept : 'application/json', Content-Type: 'application/json'}
    When method post
    Then status 200

    Given path '/signup'
    And request {"username":"vidak","password":"vidak1","email":"vidak@gmail.com","role":["MANAGER"]}
    And headers {Accept : 'application/json', Content-Type: 'application/json', Authorization: 'Bearer ' + response.token}
    When method put
    Then status 201











#
#
#    Given path '/order'
#    And request {"customerName":"Aleksandra","customerEmail":"alex@email.com","productsWithQuantities":[{"productName":"DOPPIO","quantity":"1"},{"productName":"AMERICANO","quantity":"1"}]}
#    And headers {Accept : 'application/json', Content-Type: 'application/json'}
#    When method post
#    Then status 201
#
#    Given path '/order/' + response.orderId
#    And header Accept = 'application/json'
#    When method get # Send the get request
#    Then status 200 # Send the get request
#
#    Given path '/order/' + response.orderId
#    And headers {Accept : 'application/json', Content-Type: 'application/json'}
#    When method delete
#    Then status 204