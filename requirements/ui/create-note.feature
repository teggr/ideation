Feature: Create note

    In order to create new notes that may be viewed and further edited a User
    may create a note

    Scenario: First note
        Given that the User has no notes created
        When the User opens the application
        Then the User is prompted to create a new note

    Scenario: Create note
        Given the User has chosed to create a note
        When the User creates a note
        Then a new Note is created
        And it has a created date of now
        And it has empty content
        And it has a plain text content type
        And 