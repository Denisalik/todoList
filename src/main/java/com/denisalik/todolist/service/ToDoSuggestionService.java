package com.denisalik.todolist.service;

import com.denisalik.todolist.entity.ToDoItem;
import com.denisalik.todolist.entity.ToDoSuggestion;
import com.denisalik.todolist.exception.ToDoItemNotFoundException;
import com.denisalik.todolist.exception.ToDoSuggestionNotFoundException;
import com.denisalik.todolist.repository.ToDoItemRepository;
import com.denisalik.todolist.repository.ToDoSuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ToDoSuggestionService {
    private final ToDoItemRepository toDoItemRepository;
    private final ToDoSuggestionRepository toDoSuggestionRepository;
    private final ChatClient chatClient;

    @Value("${spring.ai.openai.chat.model}")
    private String model;

    private static final String SYSTEM_PROMPT = """
        You are a productivity assistant. The user gives you a todo item
        (title + description). Respond with a concise, actionable,
        step-by-step plan on how to accomplish it. Use plain text,
        no markdown headers, max 6 steps.
        """;

    public ToDoSuggestion generateSuggestion(Long toDoItemId) {
        ToDoItem item = toDoItemRepository.findById(toDoItemId)
                .orElseThrow(() -> new ToDoItemNotFoundException(toDoItemId));

        String userPrompt = """
                Title: %s
                Description: %s
                """.formatted(item.getTitle(), item.getDescription());

        String aiResponse = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(userPrompt)
                .call()
                .content();

        ToDoSuggestion answer = toDoSuggestionRepository.findByToDoItemId(toDoItemId)
                .map(existing -> {
                    existing.setContent(aiResponse);
                    return existing;
                })
                .orElseGet(() -> ToDoSuggestion.builder()
                        .toDoItem(item)
                        .content(aiResponse)
                        .model(model)
                        .build());

        return toDoSuggestionRepository.save(answer);
    }

    @Transactional(readOnly = true)
    public ToDoSuggestion getSuggestion(Long toDoItemId) {
        return toDoSuggestionRepository.findByToDoItemId(toDoItemId)
                .orElseThrow(() -> new ToDoSuggestionNotFoundException(toDoItemId));
    }
}
