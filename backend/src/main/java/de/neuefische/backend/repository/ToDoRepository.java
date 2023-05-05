package de.neuefische.backend.repository;


import de.neuefische.backend.model.ToDo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.server.ResponseStatusException;

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
        if(toDoMap.containsKey(id)){
            return toDoMap.get(id);
        }
        else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No such ToDo found."
            );
        }
    }

    public ToDo editTodo(String id, ToDo toDo) {
        toDo.setId(id);
        toDoMap.replace(id,toDo);
        return toDoMap.get(id);
    }

    public ToDo removeToDo(String id) {
        ToDo removedToDo = getToDo(id);
        toDoMap.remove(id);
        return removedToDo;
    }
}
