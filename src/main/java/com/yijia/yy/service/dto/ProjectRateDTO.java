package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the ProjectRate entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectRateDTO implements Serializable {

    private Long id;

    private Long createTime;

    private Long finishTime;

    private Long ownerId;

    private Long projectId;

    private String projectName;

    private Integer management; //项目管理

    private Boolean shouldRateManagement;

    private String managementDesc;

    private Integer creation; //创意

    private Boolean shouldRateCreation;

    private String creationDesc;

    private Integer production; //制片

    private Boolean shouldRateProduction;

    private String productionDesc;

    private Integer direct; //导演

    private Boolean shouldRateDirect;

    private String directDesc;

    private Integer photography; //摄像

    private Boolean shouldRatePhotography;

    private String photographyDesc;

    private Integer postprocess; //后期

    private Boolean shouldRatePostprocess;

    private String postprocessDesc;

    private Integer cutting; //剪辑

    private String  cuttingDesc;

    private Integer effect; //特效

    private String effectDesc;

    private Integer graphic; //平面

    private String graphicDesc;

    private Integer music; //音乐

    private String musicDesc;

    private Integer threeDimension; //三维

    private String threeDimensionDesc;

    private Boolean finished;

    private Boolean isAvg;

    private Boolean isAudit;

    private Boolean shouldFinalRate;

    private Boolean closed;

    private EmployeeDTO closedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public Boolean getAvg() {
        return isAvg;
    }

    public void setAvg(Boolean avg) {
        isAvg = avg;
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

    public Boolean getAudit() {
        return isAudit;
    }

    public void setAudit(Boolean audit) {
        isAudit = audit;
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

    public EmployeeDTO getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(EmployeeDTO closedBy) {
        this.closedBy = closedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectRateDTO projectRateDTO = (ProjectRateDTO) o;

        if (!Objects.equals(id, projectRateDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectRateDTO{" +
            "id=" + id +
            ", createTime='" + createTime + "'" +
            '}';
    }
}
