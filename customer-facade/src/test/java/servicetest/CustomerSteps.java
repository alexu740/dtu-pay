package servicetest;
import static org.junit.Assert.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CustomerSteps {

    @Given("a step")
    public void sampleTest() {
        assertTrue(true);
    }

    @Then("it is a success")
    public void sampleAssert() {
        assertTrue(true);
    }
}
