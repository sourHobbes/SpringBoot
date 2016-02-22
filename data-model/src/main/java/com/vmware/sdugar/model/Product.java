package com.vmware.sdugar.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * sourabhdugar
 * 2/20/16.
 */
@Entity
public class Product {

    private Integer serialNumber;
    private List<Part> parts = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getSerialNumber() { return serialNumber; }
    void setSerialNumber(Integer sn) { serialNumber = sn; }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        inverseJoinColumns = @JoinColumn(name = "parts_key"))
    @OrderBy(value = "ID DESC")
    // @OrderColumn
    public List<Part> getParts() { return parts; }

    public void addPart(Part part) { this.parts.add(part); }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
}
