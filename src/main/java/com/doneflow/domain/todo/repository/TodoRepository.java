package com.doneflow.domain.todo.repository;

import com.doneflow.domain.todo.entity.Todo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

  List<Todo> findAllByCompleted(Boolean completed);

  List<Todo> findAllByCategory(String category);
}
