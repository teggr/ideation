Feature: Edit note

    Editing a note allows a User to edit the contents of a note. Once the note has been updated
    the user may save the note.

    Scenario: Making changes to the content
        Given a User has a note for Editing
        When the User makes change to the content of a note
        Then the note is marked as Dirty 
        And the note is available for saving

    Scenario: Saving changes to the content
        Given a User has made changes to the content of a note
        And the note has been marked as Dirty
        And the note is available for saving
        When the user saves the note
        Then the updated content is written back to the storage
        And the notes updated date is updated
        And the title is replaced with the first line of the note if available