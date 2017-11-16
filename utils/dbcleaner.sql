set sql_safe_updates=0;


delete from shoot_agenda;
delete from shoot_config;

delete from expense_item;


delete from approval_request_bonuses;
delete from approval_item;


delete from approval_request;


update approval set next_approval_id = null;
delete from approval;

delete from asset;

delete from comment;

delete from meeting_members;
delete from meeting;

delete from task_picture_infos;
delete from task_owner;
delete from task_members;
delete from task_attachments;
delete from task;


delete from notice_projects;
delete from notice_employees;
delete from notice_depts;
delete from notice_chat;
delete from notice;



delete from director_needs_item;
delete from director_needs_comment;
delete from director_needs;

delete from performance_bonus;


delete from project_aes;
delete from project_cost;
delete from project_members;
delete from project_payment;
delete from project_progress_item;
delete from project_progress_table;
delete from project_rate;
delete from project_viewers;
delete from project_timeline;
delete from project;


delete from client;
delete from contract_installment;
delete from contract_attachments;
delete from contract_invoice;
delete from contract;

delete from deduct;


delete from jhi_user_authority where user_id in (select id from jhi_user where email like '%yy%');
delete FROM jhi_user_authority where user_id in (select id from jhi_user where login ='anonymoususer' or login='system');
delete from jhi_user where email like '%yy%';
delete from jhi_user where login = 'anonymoususer';

delete from jhi_user where login = 'system';
delete from employee_job_titles where employee_id < 100;
delete from employee where id < 100 and id != 1;


update file_info set thumbnail_id = null;
delete from file_info where id not in (select photo_id from employee);



delete from room;

delete from file_info where id not in (select photo_id from employee where photo_id is not null)
and id not in (select photo_id from actor_photos where photo_id is not null);
