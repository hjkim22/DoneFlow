package com.doneflow.domain.todo.repository;

import com.doneflow.domain.todo.entity.Todo;
import com.doneflow.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {

  List<Todo> findByUser(User user);
}
