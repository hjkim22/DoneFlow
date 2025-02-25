package com.doneflow.domain.todo.entity;

import com.doneflow.common.enums.RepeatType;
import com.doneflow.common.base.BaseTimeEntity;
import com.doneflow.domain.category.entity.Category;
import com.doneflow.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title; // 타이틀

  @Column(columnDefinition = "TEXT")
  private String content; // 내용

  @Column(nullable = false)
  @Builder.Default
  private boolean completed = false; // 완료 여부

  private LocalDateTime dueDate; // 마감 기한

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @Enumerated(EnumType.STRING)
  private RepeatType repeatType = RepeatType.NONE; // 반복 유형

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false) // 유저와 연결 (N:1 관계)
  private User user;

  public void setCategoryWithDefault(Category category) {
    this.category = (category != null) ? category : new Category(0L, "미분류");
  }
}
