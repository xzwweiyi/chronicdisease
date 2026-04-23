

ALTER TABLE patient_record ADD generated_manually int(3)  default 0 COMMENT '手动入组次数';
ALTER TABLE patient_info ADD generated_manually int(3)   default 0 COMMENT '手动入组次数';