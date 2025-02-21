package com.doneflow.domain.todo.repository;

import com.doneflow.domain.todo.entity.Todo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

  @Query("SELECT t FROM Todo t WHERE (:completed IS NULL OR t.completed = :completed) " +
      "ORDER BY " +
      "CASE WHEN :sortBy = 'dueDate' THEN " +
      "CASE WHEN t.dueDate IS NULL THEN 1 ELSE 0 END END, " +
      "CASE WHEN :sortBy = 'dueDate' THEN t.dueDate END ASC, " +
      "t.createdAt DESC") // 동일한 마감기한이면 최근 할 일 먼저 정렬
  List<Todo> findAllByCompleted(@Param("completed") Boolean completed,
      @Param("sortBy") String sortBy);

  List<Todo> findAllByCategory(String category);
}
