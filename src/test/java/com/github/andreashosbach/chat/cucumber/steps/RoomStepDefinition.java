package com.github.andreashosbach.chat.cucumber.steps;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.github.andreashosbach.chat.cucumber.StepDefinition;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RoomStepDefinition extends StepDefinition {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	private static class RoomList extends ArrayList<String> {
		private static final long serialVersionUID = 1L;
	};

	private RoomList roomList = null;
	private int lastStatusCode = 0;

	private String baseUrl() {
		return "http://localhost:" + port + "/room";
	}

	@Given("^the room \"([^\"]*)\" does not exist$")
	public void given_room_does_not_exist(String name) throws Throwable {
		when_client_delete_room(name);
		when_client_get_room_list();
		then_room_is_not_in_the_response(name);
	}

	@Given("^the room \"([^\"]*)\" does exist$")
	public void given_room_does_exist(String name) throws Throwable {
		when_client_add_room(name);
		when_client_get_room_list();
		then_room_is_in_the_response(name);
	}

	@When("^the client deletes the room \"([^\"]*)\"$")
	public void when_client_delete_room(String name) throws Throwable {
		try {
			testRestTemplate.delete(baseUrl() + "/" + name);
			lastStatusCode = 200;
		} catch (HttpClientErrorException ex) {
			lastStatusCode = ex.getRawStatusCode();
		}
	}

	@When("^the client adds the room \"([^\"]*)\"$")
	public void when_client_add_room(String name) throws Throwable {
		try {
			testRestTemplate.put(baseUrl() + "/" + name, null);
			lastStatusCode = 200;
		} catch (HttpClientErrorException ex) {
			lastStatusCode = ex.getRawStatusCode();
		}
	}

	@When("^the client gets the room list$")
	public void when_client_get_room_list() throws Throwable {
		ResponseEntity<RoomList> entity = testRestTemplate.getForEntity(baseUrl(), RoomList.class);
		roomList = entity.getBody();
		lastStatusCode = entity.getStatusCodeValue();
	}

	@Then("^the room \"([^\"]*)\" should be in the response$")
	public void then_room_is_in_the_response(String name) throws Throwable {
		then(roomList).contains(name);
	}

	@Then("^the room \"([^\"]*)\" should not be in the response$")
	public void then_room_is_not_in_the_response(String name) throws Throwable {
		then(roomList).doesNotContain(name);
	}

	@Then("^the room service response code should be (\\d+)$")
	public void then_response_code_is(Integer code) throws Throwable {
		then(lastStatusCode).isEqualTo(code);
	}
}
