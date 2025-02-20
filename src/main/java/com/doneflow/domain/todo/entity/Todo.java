package com.doneflow.domain.todo.entity;

import com.doneflow.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

  private String category;
}
