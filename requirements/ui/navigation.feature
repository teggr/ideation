Feature: Navigation

    In order to help the user navigate to the various features and start
    tasks and workflows the application should provide some navigation help

    Scenario: Create a note
        Given the user has the application open
        And the user has access to the navigation
        When the user chooses the option to create a note
        Then a new note will be created
        And the note will be selected and shown for immediate editing