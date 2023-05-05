package de.neuefische.backend.service;

import de.neuefische.backend.model.ToDo;
import de.neuefische.backend.repository.ToDoRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KanbanService {


    private final ToDoRepository toDoRepository;
    private final GenerateUUIDService generateUUIDService;

    public ToDo postToDo(String description, String status) {
        return toDoRepository.postToDo(description,status, generateUUIDService.generateUUID());
    }

    public List<ToDo> getToDos() {
        return toDoRepository.getToDos();
    }

    public ToDo getToDo(String id) {
        return toDoRepository.getToDo(id);
    }

    public ToDo editTodo(String id, ToDo toDo) {
        return toDoRepository.editTodo(id,toDo);
    }

    public ToDo removeToDo(String id) {
        return toDoRepository.removeToDo(id);
    }

}
