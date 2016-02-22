package com.vmware.sdugar.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QRPlan is a Querydsl query type for RPlan
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRPlan extends EntityPathBase<RPlan> {

    private static final long serialVersionUID = -578060023L;

    public static final QRPlan rPlan = new QRPlan("rPlan");

    public final ComparablePath<java.util.UUID> planId = createComparable("planId", java.util.UUID.class);

    public final StringPath planName = createString("planName");

    public final ListPath<RpVm, QRpVm> vms = this.<RpVm, QRpVm>createList("vms", RpVm.class, QRpVm.class, PathInits.DIRECT2);

    public QRPlan(String variable) {
        super(RPlan.class, forVariable(variable));
    }

    public QRPlan(Path<? extends RPlan> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRPlan(PathMetadata<?> metadata) {
        super(RPlan.class, metadata);
    }

}

