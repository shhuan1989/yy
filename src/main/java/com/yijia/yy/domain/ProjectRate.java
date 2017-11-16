package com.yijia.yy.domain;


import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProjectRate.
 */
@Entity
@Table(
    name = "project_rate",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"owner_id", "project_id"})
    }
)
public class ProjectRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Long createTime;

    @Column(name = "finish_time", nullable = false)
    private Long finishTime;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Employee owner;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "project_id")
    private Project project;

    @Column
    private Boolean isAudit;

    @Column
    @Max(100)
    private Integer management ; //项目管理

    @Column(length = 2)
    private String managementDesc;

    @Column
    private Boolean shouldRateManagement = false;

    @Column
    @Max(100)
    private Integer creation; //创意

    @Column(length = 2)
    private String creationDesc;

    @Column
    private Boolean shouldRateCreation = false;

    @Column
    @Max(100)
    private Integer production; //制片

    @Column
    private Boolean shouldRateProduction = false;

    @Column(length = 2)
    private String productionDesc;

    @Column
    @Max(100)
    private Integer direct; //导演

    @Column
    private Boolean shouldRateDirect = false;

    @Column(length = 2)
    private String directDesc;

    @Column
    @Max(100)
    private Integer photography; //摄像

    @Column(length = 2)
    private String photographyDesc;

    @Column
    private Boolean shouldRatePhotography = false;

    @Column
    @Max(100)
    private Integer postprocess; //后期

    @Column
    private Boolean shouldRatePostprocess = false;

    @Column(length = 2)
    private String postprocessDesc;

    @Column
    @Max(100)
    private Integer cutting; //剪辑

    @Column(length = 2)
    private String cuttingDesc;

    @Column
    @Max(100)
    private Integer effect; //特效

    @Column(length = 2)
    private String effectDesc;

    @Column
    @Max(100)
    private Integer graphic; //平面

    @Column(length = 2)
    private String graphicDesc;

    @Column
    @Max(100)
    private Integer music; //音乐

    @Column(length = 2)
    private String musicDesc;

    @Column
    @Max(100)
    private Integer threeDimension; //三维

    @Column(length = 2)
    private String threeDimensionDesc;

    @Column
    private Boolean finished;

    @Column
    private Boolean isAvg;

    @Column
    private Boolean shouldFinalRate;

    @Column
    private Boolean closed;

    @ManyToOne
    private Employee closedBy;

    public Boolean getAudit() {
        return isAudit;
    }

    public void setAudit(Boolean audit) {
        isAudit = audit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ProjectRate createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Employee getOwner() {
        return owner;
    }

    public void setOwner(Employee owner) {
        this.owner = owner;
    }

    public ProjectRate owner(Employee owner) {
        this.owner = owner;
        return this;
    }

    public Project getProject() {
        return project;
    }

    public ProjectRate project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Boolean getAvg() {
        return isAvg;
    }

    public void setAvg(Boolean avg) {
        isAvg = avg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectRate projectRate = (ProjectRate) o;
        if(projectRate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projectRate.id);
    }

    public Integer getManagement() {
        return management;
    }

    public void setManagement(Integer management) {
        this.management = management;
    }

    public Integer getCreation() {
        return creation;
    }

    public void setCreation(Integer creation) {
        this.creation = creation;
    }

    public Integer getProduction() {
        return production;
    }

    public void setProduction(Integer production) {
        this.production = production;
    }

    public Integer getDirect() {
        return direct;
    }

    public void setDirect(Integer direct) {
        this.direct = direct;
    }

    public Integer getPhotography() {
        return photography;
    }

    public void setPhotography(Integer photography) {
        this.photography = photography;
    }

    public Integer getPostprocess() {
        return postprocess;
    }

    public void setPostprocess(Integer postprocess) {
        this.postprocess = postprocess;
    }

    public Integer getCutting() {
        return cutting;
    }

    public void setCutting(Integer cutting) {
        this.cutting = cutting;
    }

    public Integer getEffect() {
        return effect;
    }

    public void setEffect(Integer effect) {
        this.effect = effect;
    }

    public Integer getGraphic() {
        return graphic;
    }

    public void setGraphic(Integer graphic) {
        this.graphic = graphic;
    }

    public Integer getMusic() {
        return music;
    }

    public void setMusic(Integer music) {
        this.music = music;
    }

    public Integer getThreeDimension() {
        return threeDimension;
    }

    public void setThreeDimension(Integer threeDimension) {
        this.threeDimension = threeDimension;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public String getManagementDesc() {
        return managementDesc;
    }

    public void setManagementDesc(String managementDesc) {
        this.managementDesc = managementDesc;
    }

    public String getCreationDesc() {
        return creationDesc;
    }

    public void setCreationDesc(String creationDesc) {
        this.creationDesc = creationDesc;
    }

    public String getProductionDesc() {
        return productionDesc;
    }

    public void setProductionDesc(String productionDesc) {
        this.productionDesc = productionDesc;
    }

    public String getDirectDesc() {
        return directDesc;
    }

    public void setDirectDesc(String directDesc) {
        this.directDesc = directDesc;
    }

    public String getPhotographyDesc() {
        return photographyDesc;
    }

    public void setPhotographyDesc(String photographyDesc) {
        this.photographyDesc = photographyDesc;
    }

    public String getPostprocessDesc() {
        return postprocessDesc;
    }

    public void setPostprocessDesc(String postprocessDesc) {
        this.postprocessDesc = postprocessDesc;
    }

    public String getCuttingDesc() {
        return cuttingDesc;
    }

    public void setCuttingDesc(String cuttingDesc) {
        this.cuttingDesc = cuttingDesc;
    }

    public String getEffectDesc() {
        return effectDesc;
    }

    public void setEffectDesc(String effectDesc) {
        this.effectDesc = effectDesc;
    }

    public String getGraphicDesc() {
        return graphicDesc;
    }

    public void setGraphicDesc(String graphicDesc) {
        this.graphicDesc = graphicDesc;
    }

    public String getMusicDesc() {
        return musicDesc;
    }

    public void setMusicDesc(String musicDesc) {
        this.musicDesc = musicDesc;
    }

    public String getThreeDimensionDesc() {
        return threeDimensionDesc;
    }

    public void setThreeDimensionDesc(String threeDimensionDesc) {
        this.threeDimensionDesc = threeDimensionDesc;
    }

    public Boolean getShouldRateManagement() {
        return shouldRateManagement;
    }

    public void setShouldRateManagement(Boolean shouldRateManagement) {
        this.shouldRateManagement = shouldRateManagement;
    }

    public Boolean getShouldRateCreation() {
        return shouldRateCreation;
    }

    public void setShouldRateCreation(Boolean shouldRateCreation) {
        this.shouldRateCreation = shouldRateCreation;
    }

    public Boolean getShouldRateProduction() {
        return shouldRateProduction;
    }

    public void setShouldRateProduction(Boolean shouldRateProduction) {
        this.shouldRateProduction = shouldRateProduction;
    }

    public Boolean getShouldRateDirect() {
        return shouldRateDirect;
    }

    public void setShouldRateDirect(Boolean shouldRateDirect) {
        this.shouldRateDirect = shouldRateDirect;
    }

    public Boolean getShouldRatePhotography() {
        return shouldRatePhotography;
    }

    public void setShouldRatePhotography(Boolean shouldRatePhotography) {
        this.shouldRatePhotography = shouldRatePhotography;
    }

    public Boolean getShouldRatePostprocess() {
        return shouldRatePostprocess;
    }

    public void setShouldRatePostprocess(Boolean shouldRatePostprocess) {
        this.shouldRatePostprocess = shouldRatePostprocess;
    }

    public Boolean getShouldFinalRate() {
        return shouldFinalRate;
    }

    public void setShouldFinalRate(Boolean shouldFinalRate) {
        this.shouldFinalRate = shouldFinalRate;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Employee getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(Employee closedBy) {
        this.closedBy = closedBy;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectRate{" +
            "id=" + id +
            ", createTime='" + createTime + "'" +
            '}';
    }
}
