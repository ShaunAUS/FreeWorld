package com.example.testSecurity.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberArticleBookmark is a Querydsl query type for MemberArticleBookmark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberArticleBookmark extends EntityPathBase<MemberArticleBookmark> {

    private static final long serialVersionUID = -408971726L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberArticleBookmark memberArticleBookmark = new QMemberArticleBookmark("memberArticleBookmark");

    public final QArticle article;

    public final QMember member;

    public final NumberPath<Long> no = createNumber("no", Long.class);

    public QMemberArticleBookmark(String variable) {
        this(MemberArticleBookmark.class, forVariable(variable), INITS);
    }

    public QMemberArticleBookmark(Path<? extends MemberArticleBookmark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberArticleBookmark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberArticleBookmark(PathMetadata metadata, PathInits inits) {
        this(MemberArticleBookmark.class, metadata, inits);
    }

    public QMemberArticleBookmark(Class<? extends MemberArticleBookmark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.article = inits.isInitialized("article") ? new QArticle(forProperty("article"), inits.get("article")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

