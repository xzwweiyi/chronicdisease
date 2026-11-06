package com.dchealth.web.controller.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Reorder top-level menus to match prototype design sequence.
 * Hide extra menus not in the primary navigation.
 * Runs once, tracked in sys_migration.
 *
 * Prototype order (from 平台架构.html top nav CSS left positions):
 *   首页(99), 慢病筛查(260), 慢病路径管理(200), 居民管理(270),
 *   分级诊疗(280), 远程会诊(290), 随访与健康干预(210),
 *   AI评估分析(300), 签约管理(310), 绩效考核(250),
 *   数据可视化(220), 知识库管理(230), 系统配置(240)
 *
 * Extra menus to hide (in prototype top nav but not in primary nav):
 *   慢病门诊(330), 设备采集(320), 预警管理(340), 宣教管理(350),
 *   基础管理(if exists), 系统配置-duplicate(360)
 */
@Component
@Order(5) // Run after DashboardMenuRunner and other menu runners
public class MenuOrderRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MenuOrderRunner.class);
    private static final String VERSION = "V5__menu_order";

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            ensureMigrationTable(conn);

            if (isApplied(conn)) {
                log.info("=== Menu order migration V5 already applied, skipping ===");
                return;
            }

            log.info("=== Starting menu order migration V5 ===");

            // Step 1: Reorder top-level menus
            reorderTopLevelMenus(conn);

            // Step 2: Hide extra menus
            hideExtraMenus(conn);

            // Step 3: Fix any remaining "首页" menu issues
            fixDashboardMenu(conn);

            recordApplied(conn);
            log.info("=== Menu order migration V5 completed ===");
        } catch (Exception e) {
            log.error("Menu order migration failed: {}", e.getMessage(), e);
        }
    }

    private void reorderTopLevelMenus(Connection conn) throws Exception {
        int[][] orders = {
            // {menu_id, order_num}
            // Order from 平台架构.html prototype CSS left positions
            {99,  0},   // 首页             (0px)
            {260, 1},   // 慢病筛查         (48px)
            {200, 2},   // 慢病路径管理     (184px)
            {270, 3},   // 居民管理         (298px)
            {280, 4},   // 分级诊疗         (396px)
            {290, 5},   // 远程会诊         (471px)
            {210, 6},   // 随访与健康干预   (552px)
            {300, 7},   // AI评估分析       (674px)
            {310, 8},   // 签约管理         (771px)
            {250, 9},   // 绩效考核         (1116px)
            {220, 10},  // 数据可视化       (1207px)
            {230, 11},  // 知识库管理       (under 基础管理 at 1299px)
            {240, 12},  // 系统配置         (1402px)
        };

        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE sys_menu SET order_num = ? WHERE menu_id = ? AND parent_id = 0")) {
            for (int[] order : orders) {
                ps.setInt(1, order[1]);
                ps.setInt(2, order[0]);
                int updated = ps.executeUpdate();
                if (updated > 0) {
                    log.info("  Updated menu_id={} order_num={}", order[0], order[1]);
                }
            }
        }
    }

    private void hideExtraMenus(Connection conn) throws Exception {
        int[] hideMenuIds = {320, 330, 340, 350, 360};

        // 使用PreparedStatement，避免SQL注入风险
        try (PreparedStatement selectPs = conn.prepareStatement(
                "SELECT menu_name FROM sys_menu WHERE menu_id = ? AND parent_id = 0");
             PreparedStatement updatePs = conn.prepareStatement(
                     "UPDATE sys_menu SET visible = '1', status = '1' WHERE menu_id = ?")) {

            for (int menuId : hideMenuIds) {
                // 先查询
                selectPs.setInt(1, menuId);
                try (ResultSet rs = selectPs.executeQuery()) {
                    if (rs.next()) {
                        String menuName = rs.getString("menu_name");
                        // 关闭ResultSet后，再执行更新
                        rs.close();

                        // 执行更新
                        updatePs.setInt(1, menuId);
                        int updated = updatePs.executeUpdate();
                        if (updated > 0) {
                            log.info("  Hidden menu_id={} ({}): visible=1, status=1", menuId, menuName);
                        }
                    }
                }
            }
        }
    }

    /**
     * Ensure 首页 menu has correct properties for the TopNav to handle it properly.
     */
    private void fixDashboardMenu(Connection conn) throws Exception {
        try (Statement s = conn.createStatement()) {
            // Ensure 首页 has order_num=0, M-type, path="/", visible/status OK
            int updated = s.executeUpdate(
                "UPDATE sys_menu SET order_num = 0, menu_type = 'M', path = '', " +
                "component = NULL, visible = '0', status = '0', perms = '' " +
                "WHERE menu_name = '首页' AND parent_id = 0");
            if (updated > 0) log.info("  Fixed '首页' top-level menu properties");

            // Ensure 工作台 child menu has correct properties
            updated = s.executeUpdate(
                "UPDATE sys_menu SET order_num = 1, path = 'dashboard', component = 'index', " +
                "visible = '0', status = '0' " +
                "WHERE menu_name = '工作台' AND menu_type = 'C'");
            if (updated > 0) log.info("  Fixed '工作台' child menu properties");
        }
    }

    private void ensureMigrationTable(Connection conn) throws Exception {
        try (Statement s = conn.createStatement()) {
            s.execute(
                "CREATE TABLE IF NOT EXISTS sys_migration (" +
                "    version VARCHAR(64) PRIMARY KEY," +
                "    applied_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    description VARCHAR(255)" +
                ")"
            );
        }
    }

    private boolean isApplied(Connection conn) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(1) FROM sys_migration WHERE version = ?")) {
            ps.setString(1, VERSION);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private void recordApplied(Connection conn) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO sys_migration (version, description) VALUES (?, ?)")) {
            ps.setString(1, VERSION);
            ps.setString(2, "Menu reorder: prototype sequence, hide extra menus");
            ps.executeUpdate();
        }
    }
}
