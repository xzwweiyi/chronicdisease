CREATE TABLE `f_medical_form_eval_result`
(
    `id`                bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `medical_form_id`   bigint       DEFAULT NULL,
    `record_id`         bigint       DEFAULT NULL,
    `evaluation_result` varchar(255) DEFAULT NULL,
    `evaluation_scores` varchar(20)  DEFAULT NULL,
    `create_by`         bigint       DEFAULT NULL,
    `create_time`       datetime     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



-- 留言表personid数据类型需要修改为String