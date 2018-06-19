package com.github.andreashosbach.chat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class ChatControllerTest {

	@Mock
	ChatSpace chatSpaceMock;

	@InjectMocks
	ChatController controller;

	@Test
	public void roomList() {
		// Given
		ArrayList<ChatRoom> testList = new ArrayList<>();
		testList.add(new ChatRoom("myRoom"));
		testList.add(new ChatRoom("otherRoom"));

		Mockito.when(chatSpaceMock.getRooms()).thenReturn(testList);

		// When
		ResponseEntity<List<String>> response = controller.roomList();

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody(), contains("myRoom", "otherRoom"));
	}

	@Test
	public void roomContent_notExisting() {
		// Given
		Mockito.when(chatSpaceMock.getRoom(ArgumentMatchers.anyString())).thenReturn(null);

		// When
		ResponseEntity<List<ChatMessage>> response = controller.roomContent("myRoom");

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
	}

	@Test
	public void roomContent_existing() {
		// Given
		ChatRoom room = new ChatRoom("myRoom");
		ChatMessage givenMessage = new ChatMessage("User1", "Text1");
		room.add(givenMessage);
		Mockito.when(chatSpaceMock.getRoom(ArgumentMatchers.anyString())).thenReturn(room);

		// When
		ResponseEntity<List<ChatMessage>> response = controller.roomContent("myRoom");

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		assertThat(response.getBody(), contains(givenMessage));
	}

	@Test
	public void createRoom() {
		// Given

		// When
		ResponseEntity<?> response = controller.createRoom("myRoom");

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		Mockito.verify(chatSpaceMock, Mockito.times(1)).addRoom("myRoom");
	}

	@Test
	public void deleteRoom() {
		// Given

		// When
		ResponseEntity<?> response = controller.deleteRoom("myRoom");

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		Mockito.verify(chatSpaceMock, Mockito.times(1)).deleteRoom("myRoom");
	}

	@Test
	public void say() {
		// Given
		ChatMessage message = new ChatMessage("User1", "Text1");
		Mockito.when(chatSpaceMock.addMessage("myRoom", message)).thenReturn(true);

		// When
		ResponseEntity<?> response = controller.say("myRoom", message);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		Mockito.verify(chatSpaceMock, Mockito.times(1)).addMessage("myRoom", message);
	}

	@Test
	public void say_roomDoesNotExist() {
		// Given
		ChatMessage message = new ChatMessage("User1", "Text1");

		// When
		ResponseEntity<?> response = controller.say("myRoom", message);

		// Then
		assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
	}

}
