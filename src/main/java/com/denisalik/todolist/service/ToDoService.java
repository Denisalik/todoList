package com.denisalik.todolist.service;

import com.denisalik.todolist.dto.req.ToDoItemRequest;
import com.denisalik.todolist.entity.ToDoItem;
import com.denisalik.todolist.exception.ToDoItemNotFoundException;
import com.denisalik.todolist.repository.ToDoItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ToDoService {
    private ToDoItemRepository toDoRepository;

    @Transactional(readOnly = true)
    public List<ToDoItem> getToDoItems() {
        log.info("Getting all ToDo items");
        return toDoRepository.findAll();
    }
    @Transactional(readOnly = true)
    public List<ToDoItem> getToDoItemsByUser(String username) {
        log.info("Getting all ToDo items created by {}", username);
        return toDoRepository.findAllByCreatedBy(username);
    }

    @Transactional(readOnly = true)
    public Optional<ToDoItem> getToDoItemById(Long id) {
        log.info("Getting ToDo item by id {}", id);
        return toDoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public ToDoItem getToDoItemByIdOrException(Long id) {
        log.info("Getting ToDo item by id {} or throw ToDoItemNotFoundException", id);
        return toDoRepository
                .findById(id)
                .orElseThrow(() -> new ToDoItemNotFoundException(id));
    }

    public ToDoItem createToDoItem(ToDoItemRequest item) {
        log.info("Creating ToDo item {}", item.title());
        ToDoItem createdItem = ToDoItem
                .builder()
                .description(item.description())
                .title(item.title())
                .build();
        ToDoItem saved = toDoRepository.save(createdItem);
        log.info("Successfully created ToDo item with id {}", saved.getId());
        return saved;
    }

    public ToDoItem updateToDoItem(Long id, ToDoItemRequest updated) {
        log.info("Updating ToDo item by id {}", id);

        ToDoItem existing = getToDoItemByIdOrException(id);

        Optional.ofNullable(updated.title()).ifPresent(existing::setTitle);
        Optional.ofNullable(updated.description()).ifPresent(existing::setDescription);

        ToDoItem saved = toDoRepository.save(existing);
        log.info("Successfully updated ToDo item with id {}", saved.getId());
        return saved;
    }

    public void deleteToDoItem(Long id) {
        log.info("Deleting ToDo item by id {}", id);

        ToDoItem item = getToDoItemByIdOrException(id);

        toDoRepository.delete(item);
        log.info("Successfully deleted ToDo item {}", item.getId());
    }
}
