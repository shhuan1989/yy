package com.yijia.yy.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Dept.
 */
@Entity
@Table(name = "dept")
public class Dept implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Dept name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dept dept = (Dept) o;
        if(dept.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dept.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Dept{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }

    public Dept withId(Long deptId) {
        this.setId(deptId);
        return this;
    }
}
