package com.example.testSecurity.auth.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberAccess is a Querydsl query type for MemberAccess
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberAccess extends EntityPathBase<MemberAccess> {

    private static final long serialVersionUID = -161906450L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberAccess memberAccess = new QMemberAccess("memberAccess");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.testSecurity.entity.QMember member;

    public final DateTimePath<java.time.LocalDateTime> refreshDate = createDateTime("refreshDate", java.time.LocalDateTime.class);

    public final StringPath refreshToken = createString("refreshToken");

    public final DateTimePath<java.time.LocalDateTime> tokenCreateDate = createDateTime("tokenCreateDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> tokenExpireDate = createDateTime("tokenExpireDate", java.time.LocalDateTime.class);

    public QMemberAccess(String variable) {
        this(MemberAccess.class, forVariable(variable), INITS);
    }

    public QMemberAccess(Path<? extends MemberAccess> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberAccess(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberAccess(PathMetadata metadata, PathInits inits) {
        this(MemberAccess.class, metadata, inits);
    }

    public QMemberAccess(Class<? extends MemberAccess> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.testSecurity.entity.QMember(forProperty("member")) : null;
    }

}

