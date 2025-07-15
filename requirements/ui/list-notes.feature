Feature: List notes

    In order to browse the notes that a User has made, they should browse
    able to view a simple list of the notes that they have made so that
    they may perform futher actions on those notes such as viewing and editing

    Scenario: Empty list
        Given a user has created no notes
        When the user goes to manage their notes
        Then they get an empty list of notes

    Scenario: One item
        Given a user has created a single note
        When the user goes to manage their notes
        Then they get a list with a single note
        And each note should be identifiable by the note title and date created or updated

    Scenario: Multiple items
        Given a user has created more than one note
        When the user goes to manage their notes
        Then they get a list with all their notes
        And each note should be identifiable by the note title and date created or updated

    Scenario: Too many items to view
        Given a user has created more than one note and it's not reasonable to see them all
        When the user goes to manage their notes
        Then they get a list with a page of notes
        And each note should be identifiable by the note title and date created or updated
        And notes that are not shown immediately should be available via navigation

    Scenario: Delete a note
        Given the user has a note in the list
        When the user chooses to deletes the not
        Then the note content and metadata should be removed
        And the list updated
    