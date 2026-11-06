<template>
    <div class="app-container">
        <el-row :gutter="20">
            <el-col :span="6" :xs="24">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span>个人信息</span>
                    </div>
                    <div>
                        <div class="text-center">
                            <userAvatar :user="user" />
                        </div>
                        <ul class="list-group list-group-striped">
                            <li class="list-group-item">
                                <span class="list_group_icon">
                                    <svg-icon class="mr10" icon-class="user" />用户名称
                                </span>
                                <div class="pull-right">{{ user.userName }}</div>
                            </li>
                            <li class="list-group-item">
                                <span class="list_group_icon">
                                    <svg-icon class="mr10" icon-class="phone" />手机号码

                                </span>
                                <div class="pull-right">{{ user.phonenumber }}</div>
                            </li>
                            <li class="list-group-item">
                                <span class="list_group_icon">
                                    <svg-icon class="mr10" icon-class="email" />用户邮箱

                                </span>
                                <div class="pull-right">{{ user.email }}</div>
                            </li>
                            <li class="list-group-item">
                                <span class="list_group_icon">
                                    <svg-icon class="mr10" icon-class="tree" />所属部门

                                </span>
                                <div class="pull-right" v-if="user.dept">{{ user.dept.deptName }} / {{ postGroup }}
                                </div>
                            </li>
                            <li class="list-group-item">
                                <span class="list_group_icon">
                                    <svg-icon class="mr10" icon-class="peoples" />所属角色

                                </span>
                                <div class="pull-right">{{ roleGroup }}</div>
                            </li>
                            <li class="list-group-item">
                                <span class="list_group_icon">
                                    <svg-icon class="mr10" icon-class="date" />创建日期

                                </span>
                                <div class="pull-right">{{ user.createTime }}</div>
                            </li>
                        </ul>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="18" :xs="24">
                <el-card>
                    <div slot="header" class="clearfix">
                        <span>基本资料</span>
                    </div>
                    <el-tabs v-model="activeTab">
                        <el-tab-pane label="基本资料" name="userinfo">
                            <userInfo class="base_tab_info" :user="user" />
                        </el-tab-pane>
                        <el-tab-pane label="修改密码" name="resetPwd">
                            <resetPwd class="base_tabInfo" :user="user" />
                        </el-tab-pane>
                    </el-tabs>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import userAvatar from "./userAvatar";
import userInfo from "./userInfo";
import resetPwd from "./resetPwd";
import { getUserProfile } from "@/api/system/user";

export default {
    name: "Profile",
    components: { userAvatar, userInfo, resetPwd },
    data() {
        return {
            user: {},
            roleGroup: {},
            postGroup: {},
            activeTab: "userinfo"
        };
    },
    created() {
        this.getUser();
    },
    methods: {
        getUser() {
            getUserProfile().then(response => {
                this.user = response.data;
                this.roleGroup = response.roleGroup;
                this.postGroup = response.postGroup;
            });
        }
    }
};
</script>
<style lang="scss" scoped>
.list-group-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .list_group_icon {
        display: flex;
        align-items: center;
    }
}
.el-tabs {
    .el-tab-pane {
        padding-top: 20px;
    }
}
</style>
