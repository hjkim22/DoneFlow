package com.doneflow.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -399241236L;

    public static final QUser user = new QUser("user");

    public final com.doneflow.common.base.QBaseTimeEntity _super = new com.doneflow.common.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final EnumPath<com.doneflow.common.enums.Role> role = createEnum("role", com.doneflow.common.enums.Role.class);

    public final ListPath<com.doneflow.domain.todo.entity.Todo, com.doneflow.domain.todo.entity.QTodo> todos = this.<com.doneflow.domain.todo.entity.Todo, com.doneflow.domain.todo.entity.QTodo>createList("todos", com.doneflow.domain.todo.entity.Todo.class, com.doneflow.domain.todo.entity.QTodo.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

