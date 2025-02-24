package com.doneflow.domain.todo.repository;

import static com.doneflow.domain.todo.entity.QTodo.todo;

import com.doneflow.domain.todo.entity.Todo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom{

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Todo> searchTodos(String keyword, Pageable pageable) {
    BooleanBuilder predicate = new BooleanBuilder();

    if (keyword != null && !keyword.isBlank()) {
      predicate.and(todo.title.containsIgnoreCase(keyword)
          .or(todo.content.containsIgnoreCase(keyword)));
    }

    List<Todo> results = queryFactory
        .selectFrom(todo)
        .where(predicate)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(todo.count())
        .from(todo)
        .where(predicate)
        .fetchOne();
    total = (total != null) ? total : 0L;

    return new PageImpl<>(results, pageable, total);
  }
}
