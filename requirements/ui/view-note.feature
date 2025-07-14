Feature: View note

    In order to read a note the User must be able to see the note

    Scenario: Select a note in the list
        Given the list of notes is not empty
        When a user select a note from the list
        Then the note is shown to the user
    
    Scenario: Select multiple notes in the list
        Given the list of notes is not empty
        When a user selects more than one note from the list
        Then the notes are shown to the user
        And one note is shown and the others are in the background

    Scenario: Select a notes when one already being viewed
        Given the list of notes is not empty
        And a notes is already being viewed
        When a user selects one or more notes from the list
        Then the notes are shown to the user in the background