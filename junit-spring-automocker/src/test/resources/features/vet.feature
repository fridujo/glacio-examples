Feature: Vet administration

    Scenario: A newly added vet can be found in the database
        Given a new Vet is successfully added
            HTTP POST on '/api/vets' with content
            """ hjson
            {
              firstName: John
              lastName: Doe
              specialties:
              [
                {
                  name: dentistry
                }
              ]
            }
            """
            last HTTP response status is '201'
        When all Vets are listed
            HTTP GET on '/api/vets'
        Then the response contains the newly added vet
            last HTTP response status is '200'
            last HTTP response body matches json-path '$.[?(@.lastName == 'Doe')].firstName' with value 'John'
