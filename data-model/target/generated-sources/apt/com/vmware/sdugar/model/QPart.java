package com.vmware.sdugar.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QPart is a Querydsl query type for Part
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPart extends EntityPathBase<Part> {

    private static final long serialVersionUID = -2096900123L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPart part = new QPart("part");

    public final NumberPath<Integer> ID = createNumber("ID", Integer.class);

    public final QProduct product;

    public QPart(String variable) {
        this(Part.class, forVariable(variable), INITS);
    }

    public QPart(Path<? extends Part> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPart(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPart(PathMetadata<?> metadata, PathInits inits) {
        this(Part.class, metadata, inits);
    }

    public QPart(Class<? extends Part> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

