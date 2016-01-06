package com.vmware.sdugar.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QRPlan is a Querydsl query type for RPlan
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRPlan extends EntityPathBase<RPlan> {

    private static final long serialVersionUID = -578060023;

    public static final QRPlan rPlan = new QRPlan("rPlan");

    public final ComparablePath<java.util.UUID> planId = createComparable("planId", java.util.UUID.class);

    public final StringPath planName = createString("planName");

    public QRPlan(String variable) {
        super(RPlan.class, forVariable(variable));
    }

    public QRPlan(BeanPath<? extends RPlan> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QRPlan(PathMetadata<?> metadata) {
        super(RPlan.class, metadata);
    }

}

