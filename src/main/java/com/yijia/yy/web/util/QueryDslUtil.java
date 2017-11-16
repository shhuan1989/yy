package com.yijia.yy.web.util;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.yijia.yy.domain.*;
import com.yijia.yy.domain.converter.*;
import com.yijia.yy.domain.enumeration.ProjectBonusStatus;
import com.yijia.yy.domain.enumeration.ProjectStatus;
import com.yijia.yy.domain.enumeration.TaskStatus;
import com.yijia.yy.service.dto.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * utils for converting dto to query dsl predicate
 */
public class QueryDslUtil {

    public static Predicate EmployeeSearchDto2Predicate(EmployeeSearchDTO dto) {
        if (dto == null) {
            return null;
        }

        QEmployee emp = QEmployee.employee;
        List<BooleanExpression> predicates = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getName())) {
            predicates.add(emp.name.like("%"+dto.getName()+"%"));
        }
        if (dto.getGenderId() != null) {
            predicates.add(emp.gender.eq(dto.getGender()));
        }
        if (dto.getAgeFrom() != null) {
            predicates.add(emp.age.goe(dto.getAgeFrom()));
        }
        if (dto.getAgeTo() != null) {
            predicates.add(emp.age.loe(dto.getAgeTo()));
        }
        if (dto.getNativePlaceId() != null) {
            predicates.add(emp.nativePlace.id.eq(dto.getNativePlaceId()));
        }
        if (dto.getEducationId() != null) {
            predicates.add(emp.education.eq(dto.getEducation()));
        }
        if (dto.getJobPositionStatus() != null) {
            predicates.add(emp.jobPositionStatus.eq(dto.getJobPositionStatus()));
        }
        if (dto.getDeptId() != null) {
            predicates.add(emp.dept.id.eq(dto.getDeptId()));
        }
        if (dto.getJobTitleId() != null) {
            predicates.add(emp.jobTitles.contains(new JobTitle().withId(dto.getJobTitleId())));
        }
        if (dto.getHireDateFrom() != null) {
            predicates.add(emp.hireDate.goe(dto.getHireDateFrom()));
        }
        if (dto.getHireDateTo() != null) {
            predicates.add(emp.hireDate.loe(dto.getHireDateTo()));
        }
        if (dto.getDeleted() != null) {
            predicates.add(emp.deleted.eq(dto.getDeleted()));
        }
        if (dto.getHasDeduct() != null) {
            if (dto.getHasDeduct() == 1) {
                predicates.add(emp.probationDeduction.isNotNull().or(emp.employeeDeduction.isNotNull()));
            } else {
                predicates.add(emp.probationDeduction.isNull().and(emp.employeeDeduction.isNull()));
            }
        }
//        if (dto.getHasLogin() != null) {
//            if (dto.getHasLogin()) {
//                predicates.add(emp.user.isNotNull());
//            } else {
//                predicates.add(emp.user.isNull());
//            }
//        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate ClientSearchDto2Predicate(ClientSearchDTO dto) {
        if (dto == null) {
            return null;
        }

        QClient client = QClient.client;
        List<BooleanExpression> predicates = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getName())) {
            predicates.add(client.name.like("%"+dto.getName()+"%"));
        }
        if (StringUtils.isNotBlank(dto.getContactName())) {
            predicates.add(client.contact.like("%"+dto.getContactName()+"%"));
        }
        if (StringUtils.isNotBlank(dto.getClientOwner())) {
            predicates.add(client.owner.like("%"+dto.getClientOwner()+"%"));
        }
        if (StringUtils.isNotBlank(dto.getContactTel())) {
            predicates.add(client.contactTel.like("%"+dto.getContactTel()+"%"));
        }
        if (dto.getClientSourceId() != null) {
            predicates.add(client.source.id.eq(dto.getClientSourceId()));
        }
        if (dto.getIndustryId() != null) {
            predicates.add(client.industry.id.eq(dto.getIndustryId()));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate ActorSearchDTO2Predicate(ActorSearchDTO dto) {
        if (dto == null) {
            return null;
        }

        QActor actor = QActor.actor;
        List<BooleanExpression> predicates = new ArrayList<>();
        if (dto.getGenderId() != null) {
            predicates.add(actor.gender.eq(dto.getGender()));
        }
        if (dto.getAgeFrom() != null) {
            predicates.add(actor.age.goe(dto.getAgeFrom()));
        }
        if (dto.getAgeTo() != null) {
            predicates.add(actor.age.loe(dto.getAgeTo()));
        }
        if (dto.getCountryId() != null) {
            predicates.add(actor.country.id.eq(dto.getCountryId()));
        }
        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate StaffSearchDto2Predicate(StaffSearchDTO dto) {
        if (dto == null) {
            return null;
        }

        QStaff staff = QStaff.staff;
        List<BooleanExpression> predicates = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getName())) {
            predicates.add(staff.name.like("%"+dto.getName()+"%"));
        }
        if (dto.getTypeId() != null) {
            predicates.add(staff.type.id.eq(dto.getTypeId()));
        }
        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate ProjectSearchDTO2Predicate(ProjectSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QProject project = QProject.project;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getName() != null) {
            predicates.add(project.name.like("%"+searchDTO.getName()+"%"));
        }
        if (searchDTO.getAeId() != null) {
            predicates.add(project.aes.contains(new Employee().id(searchDTO.getAeId())));
        }
        if (searchDTO.getClientName() != null) {
            predicates.add(project.client.name.like("%"+searchDTO.getClientName()+"%"));
        }
        if (searchDTO.getContractNumber() != null) {
            predicates.add(project.contract.idNumber.like("%"+searchDTO.getContractNumber()+"%"));
        } else if (searchDTO.getHasContact() != null) {
            predicates.add(project.hasContract.eq(searchDTO.getHasContact()));
        }
        if (searchDTO.getDirectorId() != null) {
            predicates.add(project.director.id.eq(searchDTO.getDirectorId()));
        }
        if (searchDTO.getBonusStatusId() != null) {
            predicates.add(project.bonusStatus.eq(new ProjectBonusStatusConverter().convertToEntityAttribute(searchDTO.getBonusStatusId())));
        }

        int[] statusIds = searchDTO.getStatusIds();
        if (statusIds != null && statusIds.length > 0) {
            BooleanExpression p = project.status.eq(new ProjectStatusConverter().convertToEntityAttribute(statusIds[0]));
            for (int i = 1; i < statusIds.length; i++) {
                if (statusIds[i] >= 0 && statusIds[i] < ProjectStatus.values().length) {
                    p = p.or(project.status.eq(new ProjectStatusConverter().convertToEntityAttribute(statusIds[i])));
                }
            }
            predicates.add(p);
        }

        int[] bonusStatusIds = searchDTO.getBonusStatusIds();
        if (bonusStatusIds != null && bonusStatusIds.length > 0) {
            BooleanExpression p = project.bonusStatus.eq(new ProjectBonusStatusConverter().convertToEntityAttribute(bonusStatusIds[0]));
            for (int i = 1; i < bonusStatusIds .length; i++) {
                if (bonusStatusIds[i] >= 0 && bonusStatusIds[i] < ProjectBonusStatus.values().length) {
                    p = p.or(project.bonusStatus.eq(new ProjectBonusStatusConverter().convertToEntityAttribute(bonusStatusIds[i])));
                }
            }
            predicates.add(p);
        }

        if (searchDTO.getStatusId() != null) {
            predicates.add(project.status.eq(new ProjectStatusConverter().convertToEntityAttribute(searchDTO.getStatusId())));
        }
        if (searchDTO.getIdNumber() != null) {
            predicates.add(project.idNumber.like("%"+searchDTO.getIdNumber()+"%"));
        }
        if (searchDTO.getHasShootConfig() != null) {
            if (BooleanUtils.toBoolean(searchDTO.getHasShootConfig())) {
                predicates.add(project.shootConfigs.isNotEmpty());
            } else {
                predicates.add(project.shootConfigs.isEmpty());
            }
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }


    public static Predicate TaskSearchDTO2Predicate(TaskSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QTask task = QTask.task;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getOwnerId() != null) {
            predicates.add(task.owner.id.eq(searchDTO.getOwnerId()));
        }
        if (searchDTO.getCreatorId() != null) {
            predicates.add(task.creator.id.eq(searchDTO.getCreatorId()));
        }
        if (searchDTO.getProjectId() != null) {
            predicates.add(task.project.id.eq(searchDTO.getProjectId()));
        }
        if (searchDTO.getStatus() != null) {
            predicates.add(task.status.eq(new TaskStatusConverter().convertToEntityAttribute(searchDTO.getStatus())));
        }

        int[] statusIds = searchDTO.getStatusIds();
        if (statusIds != null && statusIds.length > 0) {
            BooleanExpression p = task.status.eq(new TaskStatusConverter().convertToEntityAttribute(statusIds[0]));
            for (int i = 1; i < statusIds.length; i++) {
                p = p.or(task.status.eq(new TaskStatusConverter().convertToEntityAttribute(statusIds[i])));
            }
            predicates.add(p);
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate CommentSearchDTO2Predicate(CommentSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QComment comment = QComment.comment;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getTaskId() != null) {
            predicates.add(comment.task.id.eq(searchDTO.getTaskId()));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate ProjectCostSearchDTO2Predicate(ProjectCostSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QProjectCost cost = QProjectCost.projectCost;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getProjectId() != null) {
            predicates.add(cost.project.id.eq(searchDTO.getProjectId()));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate ProjectTimelineSearchDTO2Predicate(ProjectTimelineSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QProjectTimeline timeline = QProjectTimeline.projectTimeline;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getProjectId() != null) {
            predicates.add(timeline.project.id.eq(searchDTO.getProjectId()));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate ProjectRateSearchDTO2Predicate(ProjectRateSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QProjectRate rate = QProjectRate.projectRate;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getProjectId() != null) {
            predicates.add(rate.project.id.eq(searchDTO.getProjectId()));
        }
        if (searchDTO.getFinished() != null) {
            predicates.add(rate.finished.eq(searchDTO.getFinished()));
        }
        if (searchDTO.getOwnerId() != null) {
            predicates.add(rate.owner.id.eq(searchDTO.getOwnerId()));
        }
        if (searchDTO.getAvg() != null) {
            predicates.add(rate.isAvg.eq(searchDTO.getAvg()));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate ContractSearchDTO2Predicate(ContractSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QContract contract = QContract.contract;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(searchDTO.getClientName())) {
            predicates.add(contract.project.client.name.like("%"+searchDTO.getClientName()+"%"));
        }
        if (StringUtils.isNotBlank(searchDTO.getIdNumber())) {
            predicates.add(contract.idNumber.like("%"+searchDTO.getIdNumber()+"%"));
        }
        if (StringUtils.isNotBlank(searchDTO.getProjectIdNumber())) {
            predicates.add(contract.project.idNumber.like("%"+searchDTO.getProjectIdNumber()+"%"));
        }
        if (StringUtils.isNotBlank(searchDTO.getProjectName())) {
            predicates.add(contract.project.name.like("%"+searchDTO.getProjectName()+"%"));
        }
        if (searchDTO.getSignTimeFrom() != null) {
            predicates.add(contract.signTime.goe(searchDTO.getSignTimeFrom()));
        }
        if (searchDTO.getSignTimeTo() != null) {
            predicates.add(contract.signTime.loe(searchDTO.getSignTimeTo()));
        }
        if (searchDTO.getPayTimeFrom() != null) {
            predicates.add(contract.nextInstallmentETA.goe(searchDTO.getPayTimeFrom()));
        }
        if (searchDTO.getPayTimeTo() != null) {
            predicates.add(contract.nextInstallmentETA.loe(searchDTO.getPayTimeTo()));
        }
        if (searchDTO.getPaymentStatusId() != null) {
            predicates.add(contract.paymentStatus.eq(
                new ContractPaymentStatusConverter().convertToEntityAttribute(searchDTO.getPaymentStatusId())
            ));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate PerformanceApprovalRequestSearchDTO2Predicate(PerformanceApprovalRequestSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QPerformanceApprovalRequest request = QPerformanceApprovalRequest.performanceApprovalRequest;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getStatusId() != null) {
            predicates.add(request.status.eq(new ApprovalStatusConverter().convertToEntityAttribute(searchDTO.getStatusId())));
        }
        if (searchDTO.getProjectId() != null) {
            predicates.add(request.project.id.eq(searchDTO.getProjectId()));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate MeetingSearchDTO2Predicate(MeetingSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QMeeting meeting = QMeeting.meeting;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getDateFrom() != null) {
            predicates.add(meeting.endTime.goe(searchDTO.getDateFrom()));
        }
        if (searchDTO.getDateTo() != null) {
            predicates.add(meeting.startTime.loe(searchDTO.getDateTo()));
        }
        if (searchDTO.getStatusId() != null) {
            predicates.add(meeting.status.eq(
                new MeetingStatusConverter().convertToEntityAttribute(searchDTO.getStatusId())
            ));
        }
        if (searchDTO.getProjectId() != null) {
            predicates.add(meeting.project.id.eq(searchDTO.getProjectId()));
        }
        if (searchDTO.getMemberId() != null) {
            predicates.add(meeting.members.contains(new Employee().id(searchDTO.getMemberId())));
        }

        int[] statusIds = searchDTO.getStatusIds();
        if (statusIds != null && statusIds.length > 0) {
            BooleanExpression p = meeting.status.eq(new MeetingStatusConverter().convertToEntityAttribute(statusIds[0]));
            for (int i = 1; i < statusIds.length; i++) {
                p = p.or(meeting.status.eq(new MeetingStatusConverter().convertToEntityAttribute(statusIds[i])));
            }
            predicates.add(p);
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate VacationSearchDTO2Predicate(VacationSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QVacation vacation = QVacation.vacation;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getOwnerId() != null) {
            predicates.add(vacation.owner.id.eq(searchDTO.getOwnerId()));
        }
        if (searchDTO.getTimeFrom() != null) {
            predicates.add(vacation.endTime.goe(searchDTO.getTimeFrom()));
        }
        if (searchDTO.getTimeTo() != null) {
            predicates.add(vacation.startTime.loe(searchDTO.getTimeTo()));
        }
        if (searchDTO.getApprovalStatusId() != null) {
            predicates.add(vacation.approvalRequest.status.eq(
                new ApprovalStatusConverter().convertToEntityAttribute(searchDTO.getApprovalStatusId())
            ));
        }
        if (searchDTO.getTypeId() != null) {
            predicates.add(vacation.type.id.eq(searchDTO.getTypeId()));
        }
        if (searchDTO.getDeptId() != null) {
            predicates.add(vacation.owner.dept.id.eq(searchDTO.getDeptId()));
        }
        if (searchDTO.getJobTitleId() != null) {
            predicates.add(vacation.owner.jobTitles.contains(new JobTitle().withId(searchDTO.getJobTitleId())));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate OvertimeWorkSearchDTO2Predicate(OvertimeWorkSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QOvertimeWork overtimeWork = QOvertimeWork.overtimeWork;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getOwnerId() != null) {
            predicates.add(overtimeWork.owner.id.eq(searchDTO.getOwnerId()));
        }
        if (searchDTO.getTimeFrom() != null) {
            predicates.add(overtimeWork.endTime.goe(searchDTO.getTimeFrom()));
        }
        if (searchDTO.getTimeTo() != null) {
            predicates.add(overtimeWork.startTime.loe(searchDTO.getTimeTo()));
        }
        if (searchDTO.getApprovalStatusId() != null) {
            predicates.add(overtimeWork.approvalRequest.status.eq(
                new ApprovalStatusConverter().convertToEntityAttribute(searchDTO.getApprovalStatusId())
            ));
        }
        if (searchDTO.getDeptId() != null) {
            predicates.add(overtimeWork.owner.dept.id.eq(searchDTO.getDeptId()));
        }
        if (searchDTO.getJobTitleId() != null) {
            predicates.add(overtimeWork.owner.jobTitles.contains(new JobTitle().withId(searchDTO.getJobTitleId())));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate AssetSearchDTO2Predicate(AssetSearchDTO searchDTO) {
        if (searchDTO == null) {
            return null;
        }

        QAsset asset = QAsset.asset;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(searchDTO.getMemo())) {
            predicates.add(asset.memo.like("%" + searchDTO.getMemo() + "%"));
        }
        if (StringUtils.isNotBlank(searchDTO.getName())) {
            predicates.add(asset.name.like("%"+searchDTO.getName()+"%"));
        }
        if (searchDTO.getOwnerId() != null) {
            predicates.add(asset.owner.id.eq(searchDTO.getOwnerId()));
        }
        if (searchDTO.getNeedReturn() != null) {
            if (BooleanUtils.toBoolean(searchDTO.getNeedReturn())) {
                predicates.add(asset.needReturn.eq(searchDTO.getNeedReturn()));
            } else {
                predicates.add(asset.needReturn.eq(searchDTO.getNeedReturn()).or(asset.needReturn.isNull()));
            }
        }
        if (BooleanUtils.toBoolean(searchDTO.getOnlyPublicAsset())) {
            predicates.add(asset.owner.id.isNull());
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate AssetBorrowSearchSearchDTO2Predicate(AssetBorrowRecordSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QAssetBorrowRecord borrowRecord = QAssetBorrowRecord.assetBorrowRecord;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getOwnerId() != null) {
            predicates.add(borrowRecord.owner.id.eq(searchDTO.getOwnerId()));
        }
        if (searchDTO.getTimeFrom() != null) {
            predicates.add(borrowRecord.startTime.goe(searchDTO.getTimeFrom()));
        }
        if (searchDTO.getTimeTo() != null) {
            predicates.add(borrowRecord.startTime.loe(searchDTO.getTimeTo()));
        }
        if (searchDTO.getApprovalStatusId() != null) {
            predicates.add(borrowRecord.approvalRequest.status.eq(
                new ApprovalStatusConverter().convertToEntityAttribute(searchDTO.getApprovalStatusId())
            ));
        }
        if (searchDTO.getDeptId() != null) {
            predicates.add(borrowRecord.owner.dept.id.eq(searchDTO.getDeptId()));
        }
        if (searchDTO.getJobTitleId() != null) {
            predicates.add(borrowRecord.owner.jobTitles.contains(new JobTitle().withId(searchDTO.getJobTitleId())));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate PerformanceBonusSearchDTO2Predicate(PerformanceBonusSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QPerformanceBonus bonus = QPerformanceBonus.performanceBonus;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getProjectId() != null) {
            predicates.add(bonus.project.id.eq(searchDTO.getProjectId()));
        }

        if (searchDTO.getOwnerId() != null) {
            predicates.add(bonus.owner.id.eq(searchDTO.getOwnerId()));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate ShootConfigSearchDTO2Predicate(ShootConfigSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QShootConfig shootConfig = QShootConfig.shootConfig;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getProjectIdNumber() != null) {
            predicates.add(shootConfig.project.idNumber.like("%"+searchDTO.getProjectIdNumber()+"%"));
        }
        if (searchDTO.getProjectId() != null) {
            predicates.add(shootConfig.project.id.eq(searchDTO.getProjectId()));
        }
        if (searchDTO.getContractIdNumber() != null) {
            predicates.add(shootConfig.project.contract.idNumber.like("%"+searchDTO.getContractIdNumber()+"%"));
        }
        if (searchDTO.getProjectName() != null) {
            predicates.add(shootConfig.project.name.like("%"+searchDTO.getProjectName()+"%"));
        }
        if (searchDTO.getApprovalStatusId() != null) {
            predicates.add(shootConfig.approvalRequest.status.eq(new ApprovalStatusConverter().convertToEntityAttribute(searchDTO.getApprovalStatusId())));
        }
        if (searchDTO.getType() != null) {
            predicates.add(shootConfig.type.eq(searchDTO.getType()));
        }
        if (searchDTO.getLastModifiedTimeFrom() != null) {
            predicates.add(shootConfig.lastModifiedTime.goe(searchDTO.getLastModifiedTimeFrom()));
        }
        if (searchDTO.getLastModifiedTimeTo() != null) {
            predicates.add(shootConfig.lastModifiedTime.loe(searchDTO.getLastModifiedTimeTo()));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate DirectorNeedsSearchDTO2Predicate(DirectorNeedsSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QDirectorNeeds directorNeeds = QDirectorNeeds.directorNeeds;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getProjectIdNumber() != null) {
            predicates.add(directorNeeds.project.idNumber.like("%"+searchDTO.getProjectIdNumber()+"%"));
        }
        if (searchDTO.getContractIdNumber() != null) {
            predicates.add(directorNeeds.project.contract.idNumber.like("%"+searchDTO.getContractIdNumber()+"%"));
        }
        if (searchDTO.getProjectName() != null) {
            predicates.add(directorNeeds.project.name.like("%"+searchDTO.getProjectName()+"%"));
        }
        if (searchDTO.getCreatorId() != null) {
            predicates.add(directorNeeds.creator.id.eq(searchDTO.getCreatorId()));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate ExpenseSearchDTO2Predicate(ExpenseSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QExpense expense = QExpense.expense;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getProjectIdNumber() != null) {
            predicates.add(expense.project.idNumber.like("%"+searchDTO.getProjectIdNumber()+"%"));
        }
        if (searchDTO.getProjectId() != null) {
            predicates.add(expense.project.id.eq(searchDTO.getProjectId()));
        }
        if (searchDTO.getContractIdNumber() != null) {
            predicates.add(expense.project.contract.idNumber.like("%"+searchDTO.getContractIdNumber()+"%"));
        }
        if (searchDTO.getProjectName() != null) {
            predicates.add(expense.project.name.like("%"+searchDTO.getProjectName()+"%"));
        }
        if (searchDTO.getApprovalStatusId() != null) {
            predicates.add(expense.approvalRequest.status.eq(new ApprovalStatusConverter().convertToEntityAttribute(searchDTO.getApprovalStatusId())));
        }
        if (searchDTO.getPayMethodId() != null) {
            predicates.add(expense.payMethod.id.eq(searchDTO.getPayMethodId()));
        }
        if (searchDTO.getPaymentStatusId() != null) {
            predicates.add(expense.approvalRequest.status.eq(new ApprovalStatusConverter().convertToEntityAttribute(Math.toIntExact(searchDTO.getPaymentStatusId()))));
        }
        if (searchDTO.getPayTimeFrom() != null) {
            predicates.add(expense.payTime.goe(searchDTO.getPayTimeFrom()));
        }
        if (searchDTO.getPayTimeTo() != null) {
            predicates.add(expense.payTime.loe(searchDTO.getPayTimeTo()));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate ProjectProgressTableSearchDTO2Predicate(ProjectProgressTableSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QProjectProgressTable pt = QProjectProgressTable.projectProgressTable;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getProjectId() != null) {
            predicates.add(pt.project.id.eq(searchDTO.getProjectId()));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate NoticeSearchDTO2Predicate(NoticeSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QNotice notice = QNotice.notice;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getStartTime() != null) {
            predicates.add(notice.startTime.goe(searchDTO.getStartTime()));
        }
        if (searchDTO.getExpireTime() != null) {
            predicates.add(notice.expireTime.loe(searchDTO.getExpireTime()));
        }
        if (searchDTO.getValidate() != null) {
            Long ct = System.currentTimeMillis();
            if (searchDTO.getValidate()) {
                predicates.add(notice.startTime.loe(ct));
                predicates.add(notice.expireTime.goe(ct));
            } else {
                predicates.add(notice.startTime.goe(ct));
                predicates.add(notice.expireTime.loe(ct));
            }
        }
        if (StringUtils.isNotBlank(searchDTO.getSubject())) {
            predicates.add(notice.subject.like("%"+searchDTO.getSubject()+"%"));
        }
        if (searchDTO.getProjectId() != null) {
            predicates.add(notice.project.id.eq(searchDTO.getProjectId()));
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;
    }

    public static Predicate IncomeSearchDTO2Predicate(IncomeSearchDTO searchDTO) {
        if (searchDTO == null) {
            return  null;
        }

        QIncome income = QIncome.income;
        List<BooleanExpression> predicates = new ArrayList<>();

        if (searchDTO.getIncomeTimeFrom() != null) {
            predicates.add(income.incomeTime.goe(searchDTO.getIncomeTimeFrom()));
        }
        if (searchDTO.getIncomeTimeTo() != null) {
            predicates.add(income.incomeTime.loe(searchDTO.getIncomeTimeTo()));
        }
        if (searchDTO.getIncomeMethodId() != null) {
            predicates.add(income.incomeMethod.id.eq(searchDTO.getIncomeMethodId()));
        }
        if (searchDTO.getIncomeStatusId() != null) {
            predicates.add(
                income.approvalRequest.status.eq(
                    new ApprovalStatusConverter()
                        .convertToEntityAttribute(
                            Math.toIntExact(searchDTO.getIncomeStatusId())
                        )
                )
            );
        }

        Optional<BooleanExpression> p = predicates.stream().reduce(BooleanExpression::and);
        return p.isPresent() ? p.get() : null;    }
}
