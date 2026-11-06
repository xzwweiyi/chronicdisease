package com.dchealth.web.controller.system;

import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * One-time menu rename: 基础管理 -> 系统配置
 */
@Component
@Order(1)
public class MenuRenameRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MenuRenameRunner.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            int updated = stmt.executeUpdate(
                "UPDATE sys_menu SET menu_name='系统配置' WHERE menu_id=240 AND menu_name='基础管理'"
            );
            if (updated > 0) {
                log.info("Menu renamed: 基础管理 -> 系统配置 ({} rows)", updated);
            }
        } catch (Exception e) {
            log.debug("Menu rename skipped: {}", e.getMessage());
        }
    }
}
