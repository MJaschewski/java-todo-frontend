package de.neuefische.backend.repository;


import de.neuefische.backend.model.ToDo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class ToDoRepository {

    private Map<String, ToDo> toDoMap = new HashMap<>();


    public ToDo postToDo(String description, String status, String uuid) {
        toDoMap.put(uuid,new ToDo(description,status,uuid));
        return toDoMap.get(uuid);
    }

    public List<ToDo> getToDos() {
        return new ArrayList<>(toDoMap.values());
    }

    public ToDo getToDo(String id) {
        return toDoMap.get(id);
    }

    public ToDo editTodo(String id, ToDo toDo) {
        toDoMap.replace(id,toDo);
        return toDoMap.get(id);
    }

    public ToDo removeToDo(String id) {
        ToDo removedToDo = getToDo(id);
        toDoMap.remove(id);
        return removedToDo;
    }
}
