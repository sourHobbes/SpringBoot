package com.vmware.sdugar.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QPart is a Querydsl query type for Part
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPart extends EntityPathBase<Part> {

    private static final long serialVersionUID = -2096900123L;

    public static final QPart part = new QPart("part");

    public final NumberPath<Integer> ID = createNumber("ID", Integer.class);

    public QPart(String variable) {
        super(Part.class, forVariable(variable));
    }

    public QPart(Path<? extends Part> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPart(PathMetadata<?> metadata) {
        super(Part.class, metadata);
    }

}

