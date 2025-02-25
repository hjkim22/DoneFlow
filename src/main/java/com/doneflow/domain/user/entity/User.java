package com.doneflow.domain.user.entity;

import com.doneflow.common.base.BaseTimeEntity;
import com.doneflow.common.enums.Role;
import com.doneflow.domain.todo.entity.Todo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Setter
  @Column(nullable = false)
  private String nickname;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  // 유저가 생성한 Todo 목록 (1:N 관계 설정)
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Todo> todos = new ArrayList<>();

  // 신규 유저 생성 시 기본 ROLE 설정
  @PrePersist
  private void prePersist() {
    if (this.role == null) {
      this.role = Role.USER;
    }
  }

  // Todo 추가 메서드 (양방향 연관관계 편의 메서드)
  public void addTodo(Todo todo) {
    this.todos.add(todo);
    todo.setUser(this);
  }
}
