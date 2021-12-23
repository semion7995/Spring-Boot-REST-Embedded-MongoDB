package com.apress.todo.service;

import com.apress.todo.domain.ToDo;
import com.apress.todo.domain.ToDoBuilder;
import com.apress.todo.domain.request.ToDoRequest;
import com.apress.todo.repository.ToDoRepository;
import com.apress.todo.validation.ToDoValidationErrorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoService {
    private ToDoRepository toDoRepository;

    @Autowired
    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }


    public ResponseEntity<ToDo> readForId(String id) {
        Optional<ToDo> toDo = toDoRepository.findById(id);
        if (toDo.isPresent())
            return ResponseEntity.ok(toDo.get());
        return ResponseEntity.notFound().build();
    }

    public List<ToDo> readAllToDo() {
        return toDoRepository.findAll();
    }

    public ResponseEntity<ToDo> setCompleted(String id) {
        Optional<ToDo> toDo = toDoRepository.findById(id);
        if (!toDo.isPresent())
            return ResponseEntity.notFound().build();
        ToDo result = toDo.get();
        result.setCompleted(true);
        toDoRepository.save(result);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(result.getId()).toUri();
        return ResponseEntity.ok().header("Location", location.toString()).build();
    }

    public ToDo postModel(ToDoRequest request) {
        ToDo toDo1 = new ToDo();
        toDo1.setDescription(request.getDescription());
        ToDo result = toDoRepository.save(toDo1);
        return result;
    }

    public ResponseEntity<ToDo> deleteForId(String id) {
        toDoRepository.delete(ToDoBuilder.created().withId(id).build());
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<ToDo> delete(ToDo toDo) {
        toDoRepository.delete(toDo);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> postPutMethod(ToDo toDo, Errors errors, String id) {
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
        }
        ToDo byId = toDoRepository.findById(id).get();
        if (byId != null){
            byId.setDescription(toDo.getDescription());
            toDoRepository.save(byId);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(byId.getId()).toUri();
            return ResponseEntity.created(location).build();
        }
        ToDo result = toDoRepository.save(toDo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    public ResponseEntity<ToDo> put(ToDoRequest request, String id) {
        Optional<ToDo> byId = toDoRepository.findById(id);
        if (!byId.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        ToDo toDo1 = byId.get();
        toDo1.setDescription(request.getDescription());
        ToDo result = toDoRepository.save(toDo1);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<?> postPutMethodObject(ToDo toDo, Errors errors) {
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
        }
        Optional<ToDo> optional = toDoRepository.findById(toDo.getId());
        if (optional.isEmpty()){
//            POST
            ToDo toDo1 = new ToDo();
            toDo1.setDescription(toDo.getDescription());
            ToDo result = toDoRepository.save(toDo1);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
            return ResponseEntity.created(location).build();
        }
        else {
//            PUT
            ToDo result = toDoRepository.save(toDo);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
            return ResponseEntity.created(location).build();
        }
    }
}
