package com.yijia.yy.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A JobTitle.
 * Don't link job title to dept because employee may has multiple job titles
 */
@Entity
@Table(name = "job_title")
public class JobTitle implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int LEVEL_PRESIDENT = 1;
    public static final int LEVEL_DIRECTOR = 2;
    public static final int LEVEL_MANAGER = 3;
    public static final int LEVEL_STAFF = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column
    private Integer level = LEVEL_STAFF;

    @Column
    private Boolean immutable = true;

    @OneToMany(mappedBy = "leader", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<JobTitle> subordinates;

    @ManyToOne
    @JsonBackReference
    private JobTitle leader;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public JobTitle name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<JobTitle> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(Set<JobTitle> subordinates) {
        this.subordinates = subordinates;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public JobTitle getLeader() {
        return leader;
    }

    public void setLeader(JobTitle leader) {
        this.leader = leader;
    }

    public Boolean getImmutable() {
        return immutable;
    }

    public void setImmutable(Boolean immutable) {
        this.immutable = immutable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobTitle jobTitle = (JobTitle) o;
        if(jobTitle.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, jobTitle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JobTitle{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }

    public JobTitle withId(Long jobTitleId) {
        this.id = jobTitleId;
        return this;
    }
}
