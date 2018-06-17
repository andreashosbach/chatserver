Feature: CRUD operations on rooms

Scenario: client adds and deletes a room
	Given the room "myRoom" does not exist
    
    When the client adds the room "myRoom"
    Then the room service response code should be 200
    
    When the client gets the room list
    Then the room service response code should be 200
    Then the room "myRoom" should be in the response
    
    When the client deletes the room "myRoom"
   Then the room service response code should be 200
     
    When the client gets the room list
    Then the room service response code should be 200
    Then the room "myRoom" should not be in the response

 
Scenario: client adds several rooms
  	Given the room "myRoom" does not exist
   	Given the room "myRoom1" does not exist
    
    When the client adds the room "myRoom"
    Then the room service response code should be 200
    
    When the client adds the room "myRoom1"
    Then the room service response code should be 200
    
    When the client gets the room list
    Then the room service response code should be 200
    Then the room "myRoom" should be in the response
    Then the room "myRoom1" should be in the response
    