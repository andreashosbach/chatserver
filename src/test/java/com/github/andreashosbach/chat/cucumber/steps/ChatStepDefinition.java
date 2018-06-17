package com.github.andreashosbach.chat.cucumber.steps;

import static org.assertj.core.api.BDDAssertions.then;

import java.net.URI;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import com.github.andreashosbach.chat.ChatMessage;
import com.github.andreashosbach.chat.cucumber.StepDefinition;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ChatStepDefinition extends StepDefinition {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	private static class MessageList extends ArrayList<ChatMessage> {
		private static final long serialVersionUID = 1L;
	};

	private MessageList messageList = null;
	private int lastStatusCode = 0;

	private String baseUrl() {
		return "http://localhost:" + port + "/room";
	}

	@When("^the client sends the message \"([^\"]*)\" as user user \"([^\"]*)\" to the room \"([^\"]*)\"$")
	public void when_client_sends_message(String message, String user, String room) throws Throwable {
		ChatMessage chatMessage = new ChatMessage(user, message);
		ResponseEntity<String> entity = testRestTemplate.postForEntity(new URI(baseUrl() + "/" + room), chatMessage,
				String.class);
		lastStatusCode = entity.getStatusCodeValue();
	}

	@When("^the client gets the messages from room \"([^\"]*)\"$")
	public void when_client_gets_messages_from_room(String room) throws Throwable {
		ResponseEntity<MessageList> entity = testRestTemplate.getForEntity(baseUrl() + "/" + room, MessageList.class);
		messageList = entity.getBody();
		lastStatusCode = entity.getStatusCodeValue();
	}

	@Then("^the message \"([^\"]*)\" from user \"([^\"]*)\" should be in the response$")
	public void then_room_is_in_the_response(String message, String user) throws Throwable {
		then(messageList).contains(new ChatMessage(user, message));
	}

	@Then("^the message service response code should be (\\d+)$")
	public void then_response_code_is(Integer code) throws Throwable {
		then(lastStatusCode).isEqualTo(code);
	}
}
