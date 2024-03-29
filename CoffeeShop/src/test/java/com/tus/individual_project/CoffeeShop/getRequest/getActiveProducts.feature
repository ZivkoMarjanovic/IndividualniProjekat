Feature: To test the get endpoint to get all Products with status active
  To test different cases

  Background: Setup the Base path
    Given url 'http://localhost:8082'

  Scenario: To get all the data from application in JSON format
    Given path '/products/active'
    And header Accept = 'application/json'
    When method get # Send the get request
    Then status 200 # Send the get request