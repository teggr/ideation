Feature: First time user

    In order to help a User get started with the applicaton they should be prompted
    with tasks to start using the application such as choosing the application data 
    directory

    Scenario: Setting up the application's data directory
        Given the user has not used the application before
        And the App environment has initialised
        When the user opens the application
        Then the User is prompted to choose a folder to store application data
        And the folder is defaulted to the "{user home}/ideation" location
        And that folder is initialised with an extensible set of folders such a "notes"
        And that location is stored in the App home directory properties so that it may be used when the application is loaded next time

