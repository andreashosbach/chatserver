package com.github.andreashosbach.chat;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChatController {

	@Autowired
	ChatSpace chatSpace;

	@GetMapping("/room")
	@ResponseBody
	public ResponseEntity<List<String>> roomList() {
		Collection<ChatRoom> room = chatSpace.getRooms();
		return new ResponseEntity<>(room.stream().map(x -> x.getName()).collect(Collectors.toList()), HttpStatus.OK);
	}

	@GetMapping("/room/{room}")
	@ResponseBody
	public ResponseEntity<List<ChatMessage>> roomContent(@PathVariable String room) {
		return Optional.ofNullable(chatSpace.getRoom(room))
				.map(r -> new ResponseEntity<>(r.getMessages(), HttpStatus.OK))
				.orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
	}

	@PutMapping("/room/{room}")
	@ResponseBody
	public ResponseEntity<?> createRoom(@PathVariable String room) {
		chatSpace.addRoom(room);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@DeleteMapping("/room/{room}")
	@ResponseBody
	public ResponseEntity<?> deleteRoom(@PathVariable String room) {
		chatSpace.deleteRoom(room);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@PostMapping("/room/{room}")
	@ResponseBody
	public ResponseEntity<?> say(@PathVariable String room, @RequestBody ChatMessage message) {
		boolean success = chatSpace.addMessage(room, message);
		return new ResponseEntity<>(null, success ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}
}