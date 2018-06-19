package com.github.andreashosbach.chat;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

public class ChatMessageTest {

	@Test
	public void testEquals_true() {
		// Given
		ChatMessage a = new ChatMessage("User", "Text");
		ChatMessage b = new ChatMessage("User", "Text");

		// Then
		then(a.equals(a)).isEqualTo(true);
		then(a.equals(b)).isEqualTo(true);
	}

	@Test
	public void testEquals_false() {
		// Given
		ChatMessage a = new ChatMessage("User", "Text");
		ChatMessage b = new ChatMessage("User2", "Text");
		ChatMessage c = new ChatMessage("User", "Text2");
		ChatMessage d = new ChatMessage("User2", "Text2");

		// Then
		then(a.equals(b)).isEqualTo(false);
		then(a.equals(c)).isEqualTo(false);
		then(a.equals(d)).isEqualTo(false);
	}

	@Test
	public void testEquals_false_special_cases() {
		// Given
		ChatMessage a = new ChatMessage("User", "Text");

		// Then
		then(a.equals(null)).isEqualTo(false);
		then(a.equals("Hello")).isEqualTo(false);
	}

}
