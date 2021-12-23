package com.apress.todo;

import com.apress.todo.domain.ToDo;
import com.apress.todo.repository.ToDoRepository;
import com.mongodb.MongoClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;

@SpringBootApplication
public class TodoMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoMongoApplication.class, args);
	}

	@Bean
	CommandLineRunner runner (ToDoRepository toDoRepository){
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				toDoRepository.deleteAll();

				final ToDo toDo = new ToDo("new Root");

				toDoRepository.save(toDo);
			}
		};
	}


}
