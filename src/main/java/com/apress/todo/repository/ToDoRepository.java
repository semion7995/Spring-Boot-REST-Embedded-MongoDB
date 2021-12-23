package com.apress.todo.repository;

import com.apress.todo.domain.ToDo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ToDoRepository extends MongoRepository<ToDo, String > {

}
