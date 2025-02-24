package com.doneflow.domain.todo.repository;

import com.doneflow.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoRepositoryCustom {

  Page<Todo> searchTodos(String keyword, Pageable pageable);
}
