

ALTER TABLE patient_record ADD generated_manually int(3)  default 0 COMMENT '手动入组次数';
ALTER TABLE patient_info ADD generated_manually int(3)   default 0 COMMENT '手动入组次数';
ALTER TABLE patient_record MODIFY COLUMN follow_status varchar(255) DEFAULT '0' COMMENT '随访状态 -1:随访任务不可见 0:无状态 1:待处理 2:进行中 3:已完成';




ALTER TABLE research_info ADD is_follow_rate int(1)   default 1 COMMENT '是否开启随访率：1-开启，0-关闭';
ALTER TABLE research_info ADD public_time datetime   default null COMMENT '发布时间';
update research_info set public_time = create_time where research_status =1;