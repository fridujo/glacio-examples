Feature: Contact storage

    Scenario: Retrieve a contact by pattern with empty database
           Given database is empty
           When searching for this contact by pattern
               HTTP GET on '/api/contact/jo*'
           Then one contact is found
               last HTTP response status is 200
               last HTTP response body matches json-path '$.length()' with value '0'

    Scenario: Retrieve a contact by pattern
       Given database is empty
       And an existing contact
           HTTP POST on '/api/contact' with content
           """ hjson
           {
               name: John
               forename: Doe
               email: john.doe@contact.com 
           }
           """
           last HTTP response status is 201
       When searching for this contact by pattern
           HTTP GET on '/api/contact/jo.+'
       Then one contact is found
           last HTTP response status is 200
           last HTTP response body matches json-path '$.length()' with value '1'
