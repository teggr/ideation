Feature: Navigation

    In order to help the user navigate to the various features and start
    tasks and workflows the application should provide some navigation help

    Scenario: Create a note
        Given the user has the application open
        And the user has access to the navigation
        When the user chooses the option to create a note
        Then a new note will be created
        And the note will be selected and shown for immediate editing
    
    Scenario: Save open
        Given the user has a note open for editing
        And the note is in a dirty state
        When the users chooses the option to save
        Then the note currently in focus for editing will be saved
    
    Scenario: Save all
        Given the user has a number of notes open for editing
        And some of those notes are in a dirty state
        When the user chooses the option to save all
        Then the notes that are currently in a dirty state will be saved
    
    Scenario: Close all 
        Given a User has some notes open for editing
        And none are in a dirty state
        When the user chooses the close all option
        Then any open notes should be closed

    Scenario: Close all with dirty states
        Given a User has some notes open for editing
        And some are in a dirty state
        When the user chooses the close all option
        Then the user will be given a choice to save all and close or cancel the close