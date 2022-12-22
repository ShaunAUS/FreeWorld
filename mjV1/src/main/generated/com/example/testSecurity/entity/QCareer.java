package com.example.testSecurity.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCareer is a Querydsl query type for Career
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCareer extends EntityPathBase<Career> {

    private static final long serialVersionUID = -158690402L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCareer career = new QCareer("career");

    public final StringPath assignedTask = createString("assignedTask");

    public final NumberPath<Integer> category = createNumber("category", Integer.class);

    public final NumberPath<Integer> categoryDetail = createNumber("categoryDetail", Integer.class);

    public final StringPath companyName = createString("companyName");

    public final StringPath description = createString("description");

    public final NumberPath<Long> no = createNumber("no", Long.class);

    public final QProfile profile;

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QCareer(String variable) {
        this(Career.class, forVariable(variable), INITS);
    }

    public QCareer(Path<? extends Career> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCareer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCareer(PathMetadata metadata, PathInits inits) {
        this(Career.class, metadata, inits);
    }

    public QCareer(Class<? extends Career> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.profile = inits.isInitialized("profile") ? new QProfile(forProperty("profile"), inits.get("profile")) : null;
    }

}

