package com.vmware.sdugar.model;

import javax.persistence.*;

/**
 * sourabhdugar
 * 2/20/16.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "ID"))
public class Part {

    public Integer ID;

    public Product product;

    @Transient
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
}
