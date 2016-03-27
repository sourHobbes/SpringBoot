package com.vmware.sdugar.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 2031319997L;

    public static final QProduct product = new QProduct("product");

    public final ListPath<Part, QPart> parts = this.<Part, QPart>createList("parts", Part.class, QPart.class, PathInits.DIRECT2);

    public final NumberPath<Integer> serialNumber = createNumber("serialNumber", Integer.class);

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata<?> metadata) {
        super(Product.class, metadata);
    }

}

