package com.doneflow.domain.todo.repository;

import static com.doneflow.domain.todo.entity.QTodo.todo;

import com.doneflow.domain.todo.entity.Todo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Todo> searchTodos(
      String keyword, Boolean completed, Long categoryId, Sort sort, Pageable pageable) {

    BooleanBuilder predicate = new BooleanBuilder()
        .and(keyword != null && !keyword.isBlank() ?
            todo.title.containsIgnoreCase(keyword)
                .or(todo.content.containsIgnoreCase(keyword)) : null)
        .and(completed != null ? todo.completed.eq(completed) : null)
        .and(categoryId != null ? todo.category.id.eq(categoryId) : null);

    // 기본 정렬 적용 (최신 생성순)
    if (sort.isEmpty()) {
      sort = Sort.by(Sort.Direction.DESC, "createdAt");
    }

    List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(sort);

    List<Todo> results = queryFactory
        .selectFrom(todo)
        .where(predicate)
        .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
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

  // NULL 값을 마지막으로 정렬하는 QueryDSL OrderSpecifier 변환 메서드
  private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort) {
    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

    for (Sort.Order order : sort) {
      String sortField = order.getProperty().trim(); // 정렬 필드명 정리
      Sort.Direction direction = order.getDirection(); // 정렬 방향 처리

      // sort 필드가 "dueDate,asc"처럼 들어올 경우 강제 변환
      if (sortField.contains(",")) {
        String[] sortParts = sortField.split(",");
        sortField = sortParts[0].trim();  // 필드명 정리
        direction = Sort.Direction.fromString(sortParts[1].trim().toUpperCase());  // 방향 변환
      }

      switch (sortField) {
        case "dueDate" -> {

          // NULL 마지막으로 보내는 정렬
          NumberExpression<Integer> nullsLastOrder = Expressions.numberTemplate(Integer.class,
              "CASE WHEN {0} IS NULL THEN 1 ELSE 0 END", todo.dueDate);

          OrderSpecifier<?> dueDateOrder = (direction.isAscending())
              ? todo.dueDate.asc()
              : todo.dueDate.desc();

          orderSpecifiers.add(nullsLastOrder.asc()); // NULL 마지막으로 정렬
          orderSpecifiers.add(dueDateOrder);
          orderSpecifiers.add(todo.createdAt.desc()); // NULL 여러 개면 최근 생성일 순 정렬
        }
        case "createdAt" -> orderSpecifiers.add(
            (direction.isAscending()) ? todo.createdAt.desc() : todo.createdAt.asc());
        case "updatedAt" -> orderSpecifiers.add(
            (direction.isAscending()) ? todo.updatedAt.desc() : todo.updatedAt.asc());
        default -> throw new IllegalArgumentException("Invalid sort field: " + sortField);
      }
    }

    return orderSpecifiers;
  }
}