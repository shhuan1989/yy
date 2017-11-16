package com.yijia.yy.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by palad on 2017/1/8.
 */
public class ShootConfigs {
    @ManyToOne
    private Project project;

    @OneToMany(mappedBy = "shootConfigs", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ShootConfig> shootConfigs;

    public List<ShootConfig> getShootConfigs() {
        return shootConfigs;
    }

    public void setShootConfigs(List<ShootConfig> shootConfigs) {
        this.shootConfigs = shootConfigs;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
