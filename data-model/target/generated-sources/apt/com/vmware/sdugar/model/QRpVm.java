package com.vmware.sdugar.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QRpVm is a Querydsl query type for RpVm
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRpVm extends EntityPathBase<RpVm> {

    private static final long serialVersionUID = -2096827001L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRpVm rpVm = new QRpVm("rpVm");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QRPlan rp;

    public QRpVm(String variable) {
        this(RpVm.class, forVariable(variable), INITS);
    }

    public QRpVm(Path<? extends RpVm> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QRpVm(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QRpVm(PathMetadata<?> metadata, PathInits inits) {
        this(RpVm.class, metadata, inits);
    }

    public QRpVm(Class<? extends RpVm> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.rp = inits.isInitialized("rp") ? new QRPlan(forProperty("rp")) : null;
    }

}

