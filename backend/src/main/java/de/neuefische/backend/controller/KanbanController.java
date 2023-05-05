package de.neuefische.backend.controller;


import de.neuefische.backend.model.ToDo;
import de.neuefische.backend.service.KanbanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

@RequestMapping("/api")

public class KanbanController {

    private final KanbanService kanbanService;

    @PostMapping("/todo")
    public ToDo postToDo(@RequestBody ToDo toDo){
        return kanbanService.postToDo(toDo.getDescription(),toDo.getStatus());
    }

    @GetMapping("/todo")
    public List<ToDo> getToDos(){
        return kanbanService.getToDos();
    }

    @GetMapping("/todo/{id}")
    public ToDo getToDo(@PathVariable String id){
        return kanbanService.getToDo(id);
    }

    @PutMapping("/todo/{id}")
    public ToDo editToDo(@PathVariable String id,@RequestBody ToDo toDo){
        return kanbanService.editTodo(id,toDo);
    }

    @DeleteMapping("/todo/{id}")
    public ToDo removeToDo(@PathVariable String id){
        return kanbanService.removeToDo(id);
    }

}
