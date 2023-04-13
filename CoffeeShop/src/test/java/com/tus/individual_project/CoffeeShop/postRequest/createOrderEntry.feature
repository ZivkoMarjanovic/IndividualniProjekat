Feature: Testing create the Order entry in the application
         Use POST /order to create a job entry in the application
         
Background: Create and initialise the base url
	Given url 'http://localhost:8082'
	
	Scenario:To create an Order entry using JSON format
	 Given path '/order'
   And request {"customerName":"Aleksandra","customerEmail":"alex@email.com","productsWithQuantities":[{"productName":"DOPPIO","quantity":"1"},{"productName":"AMERICANO","quantity":"1"}]}
   And headers {Accept : 'application/json', Content-Type: 'application/json'}
   When method post
   Then status 201

  Scenario:To create an Order entry using JSON format
    Given path '/order'
    And request {"customerName":"Aleksandra","customerEmail":"alex@email.com","productsWithQuantities":[{"productName":"DOPPIO","quantity":"-1"},{"productName":"AMERICANO","quantity":"1"}]}
    And headers {Accept : 'application/json', Content-Type: 'application/json'}
    When method post
    Then status 400
    And match response == "Quantity can not have negative value."