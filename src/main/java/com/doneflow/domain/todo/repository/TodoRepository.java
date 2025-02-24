package com.doneflow.domain.todo.repository;

import com.doneflow.domain.category.entity.Category;
import com.doneflow.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {

  @Query("SELECT t FROM Todo t WHERE (:completed IS NULL OR t.completed = :completed) " +
      "ORDER BY " +
      "CASE WHEN :sortBy = 'dueDate' THEN " +
      "CASE WHEN t.dueDate IS NULL THEN 1 ELSE 0 END END, " +
      "CASE WHEN :sortBy = 'dueDate' AND :order = 'asc' THEN t.dueDate END ASC, " +
      "CASE WHEN :sortBy = 'dueDate' AND :order = 'desc' THEN t.dueDate END DESC, " +
      "CASE WHEN :sortBy = 'createdAt' AND :order = 'asc' THEN t.createdAt END ASC, " +
      "CASE WHEN :sortBy = 'createdAt' AND :order = 'desc' THEN t.createdAt END DESC")
  Page<Todo> findPagedTodosByCompleted(
      @Param("completed") Boolean completed,
      @Param("sortBy") String sortBy,
      @Param("order") String order,
      Pageable pageable);

  @Query("SELECT t FROM Todo t WHERE t.category = :category " +
      "ORDER BY " +
      "CASE WHEN :sortBy = 'dueDate' THEN " +
      "CASE WHEN t.dueDate IS NULL THEN 1 ELSE 0 END END, " +
      "CASE WHEN :sortBy = 'dueDate' AND :order = 'asc' THEN t.dueDate END ASC, " +
      "CASE WHEN :sortBy = 'dueDate' AND :order = 'desc' THEN t.dueDate END DESC, " +
      "CASE WHEN :sortBy = 'createdAt' AND :order = 'asc' THEN t.createdAt END ASC, " +
      "CASE WHEN :sortBy = 'createdAt' AND :order = 'desc' THEN t.createdAt END DESC")
  Page<Todo> findPagedTodosByCategory(
      @Param("category") Category category,
      @Param("sortBy") String sortBy,
      @Param("order") String order,
      Pageable pageable);
}
