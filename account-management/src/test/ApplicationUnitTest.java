package dtu.usermanagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import messaging.MessageQueue;
import messaging.implementations.MessageQueueAsync;
import messaging.implementations.MessageQueueSync;
import messaging.implementations.RabbitMqQueue;
import usermanagement.domain.aggregate.Address;
import usermanagement.domain.aggregate.Contact;
import usermanagement.domain.aggregate.UserId;
import usermanagement.domain.repositories.ReadModelRepository;
import usermanagement.domain.repositories.UserRepository;
import usermanagement.domain.service.UserService;

public class ApplicationUnitTest {

	private UserService service;
	UserRepository repository;

	public void setUp_async_queues() {
		MessageQueue eventQueue = new MessageQueueAsync();
		repository = new UserRepository(eventQueue);
		ReadModelRepository readRepository = new ReadModelRepository(eventQueue);
		service = new UserService(repository, readRepository);
	}

	private void setup_sync_queues() throws InterruptedException, ExecutionException, Exception {
		MessageQueue eventQueue = new MessageQueueSync();
		repository = new UserRepository(eventQueue);
		ReadModelRepository readRepository = new ReadModelRepository(eventQueue);
		service = new UserService(repository, readRepository);
	}

	private void setup_rabbitmq() throws InterruptedException, ExecutionException, Exception {
		MessageQueue eventQueue = new RabbitMqQueue("event");
		repository = new UserRepository(eventQueue);
		ReadModelRepository readRepository = new ReadModelRepository(eventQueue);
		service = new UserService(repository, readRepository);
	}

	public void create_a_new_user() throws InterruptedException, ExecutionException {
		var userId = service.createUser("Kumar", "Chandrakant");
		var user = repository.getById(userId);
		assertEquals("Kumar", user.getFirstname());
		assertEquals("Chandrakant", user.getLastname());
		assertEquals(userId, user.getUserid());
		assertTrue(user.getAddresses().isEmpty());
		assertTrue(user.getContacts().isEmpty());
	}
	
	public void create_ten_new_users_concurrently() throws Exception {
		var uids = new HashSet<UserId>();
		for (int i = 0; i < 10; i++) {
			final int k=i;
			new Thread(() -> {try {
				uids.add(service.createUser("Name"+k, "Name"+k));
			} catch (InterruptedException | ExecutionException e) {
				throw new Error(e);
			}}).start();
		}
		Thread.sleep(3000);
		assertEquals(10,uids.size());
	}

	public void create_a_new_user_with_one_update() throws InterruptedException, ExecutionException {
		var userId = service.createUser("Kumar", "Chandrakant");
		service.updateUser(userId,
				Stream.of(new Address("New York", "NY", "10001"))
						.collect(Collectors.toSet()),
				Stream.of(new Contact("EMAIL", "tom.sawyer@gmail.com"))
						.collect(Collectors.toSet()));
		Thread.sleep(100); // Give the repository time to update its data (-> eventual consistency)
		var user = repository.getById(userId);
		assertEquals("Kumar", user.getFirstname());
		assertEquals("Chandrakant", user.getLastname());
		assertEquals(userId, user.getUserid());
		assertEquals(1,user.getAddresses().size());
		assertEquals(1,user.getContacts().size());
	}

	public void queries_return_correct_results() throws Exception {
		var userId = service.createUser("Kumar", "Chandrakant");
		service.updateUser(userId,
				Stream.of(new Address("New York", "NY", "10001"), new Address("Los Angeles", "CA", "90001"))
						.collect(Collectors.toSet()),
				Stream.of(new Contact("EMAIL", "tom.sawyer@gmail.com"), new Contact("EMAIL", "tom.sawyer@rediff.com"))
						.collect(Collectors.toSet()));

		service.updateUser(userId,
				Stream.of(new Address("New York", "NY", "10001"), new Address("Housten", "TX", "77001"))
						.collect(Collectors.toSet()),
				Stream.of(new Contact("EMAIL", "tom.sawyer@gmail.com"), new Contact("PHONE", "700-000-0001"))
						.collect(Collectors.toSet()));
		Thread.sleep(1000);
		assertEquals(Stream.of(new Contact("EMAIL", "tom.sawyer@gmail.com")).collect(Collectors.toSet()),
				service.contactByType(userId, "EMAIL"));

		assertEquals(Stream.of(new Address("New York", "NY", "10001")).collect(Collectors.toSet()),
				service.addressByRegion(userId, "NY"));

	}
	
	@Test
	public void should_run_using_rabbitmq( ) throws Exception {
		setup_rabbitmq();
		create_a_new_user();
		create_a_new_user_with_one_update();
		create_ten_new_users_concurrently();
		queries_return_correct_results();
	}

	@Test
	public void should_run_using_sync_queue( ) throws Exception {
		setup_sync_queues();
		create_a_new_user();
		create_a_new_user_with_one_update();
		create_ten_new_users_concurrently();
		queries_return_correct_results();
	}
	
	@Test
	public void should_run_using_async_queue( ) throws Exception {
		setUp_async_queues();
		create_a_new_user();
		create_a_new_user_with_one_update();
		create_ten_new_users_concurrently();
		queries_return_correct_results();
	}

}
