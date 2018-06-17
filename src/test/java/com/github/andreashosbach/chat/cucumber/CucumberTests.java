package com.github.andreashosbach.chat.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/feature", glue = "com.github.andreashosbach.chat.cucumber.steps", strict = true)
public class CucumberTests {
}
