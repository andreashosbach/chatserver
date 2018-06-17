package com.github.andreashosbach.chat;

import static org.assertj.core.api.BDDAssertions.then;

import java.net.URI;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatApplicationTests {

	private static class MessageList extends ArrayList<ChatMessage> {
		private static final long serialVersionUID = 1L;
	}

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	private String baseUrl() {
		return "http://localhost:" + port + "/room";
	}

	@Test
	public void testRoomList() throws Exception {

		final String ROOM = "testRoomListRoom";
		testRestTemplate.delete(baseUrl() + "/" + ROOM);

		@SuppressWarnings("rawtypes")
		ResponseEntity<ArrayList> entity = testRestTemplate.getForEntity(baseUrl(), ArrayList.class);

		then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(entity.getBody()).doesNotContain(ROOM);

		testRestTemplate.put(baseUrl() + "/" + ROOM, null);

		@SuppressWarnings("rawtypes")
		ResponseEntity<ArrayList> entity2 = testRestTemplate.getForEntity(baseUrl(), ArrayList.class);

		then(entity2.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(entity2.getBody()).contains(ROOM);
	}

	@Test
	public void testAddDeleteRoom() throws Exception {

		final String ROOM = "testAddDeleteRoomRoom";
		testRestTemplate.delete(baseUrl() + "/" + ROOM);

		@SuppressWarnings("rawtypes")
		ResponseEntity<ArrayList> entity1 = testRestTemplate.getForEntity(baseUrl() + "/" + ROOM, ArrayList.class);

		then(entity1.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		testRestTemplate.put(baseUrl() + "/" + ROOM, null);

		@SuppressWarnings("rawtypes")
		ResponseEntity<ArrayList> entity2 = testRestTemplate.getForEntity(baseUrl() + "/" + ROOM, ArrayList.class);

		then(entity2.getStatusCode()).isEqualTo(HttpStatus.OK);

		testRestTemplate.delete(baseUrl() + "/" + ROOM);

		@SuppressWarnings("rawtypes")
		ResponseEntity<ArrayList> entity3 = testRestTemplate.getForEntity(baseUrl() + "/" + ROOM, ArrayList.class);

		then(entity3.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void testChat() throws Exception {

		final String ROOM = "testChatRoom";
		testRestTemplate.delete(baseUrl() + "/" + ROOM);
		testRestTemplate.put(baseUrl() + "/" + ROOM, null);

		ChatMessage message = new ChatMessage("Anonymous", "Hello");
		testRestTemplate.postForLocation(new URI(baseUrl() + "/" + ROOM), message);

		ChatMessage message2 = new ChatMessage("Anyone", "Bye");
		testRestTemplate.postForLocation(new URI(baseUrl() + "/" + ROOM), message2);

		ResponseEntity<MessageList> entity = testRestTemplate.getForEntity(baseUrl() + "/" + ROOM, MessageList.class);

		then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		then(entity.getBody().size()).isEqualTo(2);
		then(entity.getBody().get(0).getText()).isEqualTo(message.getText());
		then(entity.getBody().get(0).getUser()).isEqualTo(message.getUser());
		then(entity.getBody().get(1).getText()).isEqualTo(message2.getText());
		then(entity.getBody().get(1).getUser()).isEqualTo(message2.getUser());
	}
}