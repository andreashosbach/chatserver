Feature: Message handling in rooms

Scenario: The client sends messages to a room
	Given the room "myRoom" does exist
    
    When the client sends the message "Hello World" as user user "Anyone" to the room "myRoom"
    Then the message service response code should be 200
    
    When the client gets the messages from room "myRoom"
    Then the message service response code should be 200
    Then the message "Hello World" from user "Anyone" should be in the response
    