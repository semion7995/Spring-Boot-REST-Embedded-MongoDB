package com.apress.todo.controller;

import com.apress.todo.domain.ToDo;
import com.apress.todo.domain.ToDoBuilder;
import com.apress.todo.domain.request.ToDoRequest;
import com.apress.todo.repository.ToDoRepository;
import com.apress.todo.service.ToDoService;
import com.apress.todo.validation.ToDoValidationError;
import com.apress.todo.validation.ToDoValidationErrorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ToDoController {

    private ToDoService toDoService;

    @Autowired
    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }



    @GetMapping(value = "/todo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getToDos(){
        return ResponseEntity.ok(toDoService.readAllToDo());
    }


    @GetMapping("/todo/{id}")
    public ResponseEntity<ToDo> getToDoById(@PathVariable String id){
       return toDoService.readForId(id);
    }

    @PatchMapping("/todo/{id}")
    public ResponseEntity<ToDo> setCompleted(@PathVariable String id){
        return toDoService.setCompleted(id);
    }


    @PostMapping(path = "/todo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ToDo createBook (@RequestBody ToDoRequest request) {
        return toDoService.postModel(request);
    }

    @PutMapping(path = "/todo/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<ToDo> updateToDo(@RequestBody ToDoRequest request, @PathVariable String id){
        return toDoService.put(request, id);
    }
    @RequestMapping(value = "/todo", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> createToDo(@RequestBody @Valid ToDo toDo, Errors errors){
       return toDoService.postPutMethodObject(toDo, errors);
    }
    @RequestMapping(value = "/todo/{id}", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> createToDoId(@Valid @RequestBody ToDo toDo, Errors errors, @PathVariable String id){
        return toDoService.postPutMethod(toDo, errors, id);
    }
    @DeleteMapping("/todo/{id}")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable String id){
        return toDoService.deleteForId(id);
    }

    @DeleteMapping("/todo")
    public ResponseEntity<ToDo> deleteToDo(@RequestBody ToDo toDo){
        return toDoService.delete(toDo);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ToDoValidationError handlerException(Exception exception){
        return new ToDoValidationError(exception.getMessage());
    }
}
