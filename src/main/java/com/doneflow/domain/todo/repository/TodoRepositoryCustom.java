package com.doneflow.domain.todo.repository;

import com.doneflow.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface TodoRepositoryCustom {

  Page<Todo> searchTodos(String keyword, Boolean completed, Long categoryId, Sort sort, Pageable pageable);
}
