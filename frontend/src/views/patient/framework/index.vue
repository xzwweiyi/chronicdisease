<!-- 患者管理列表 -->
<template>
    <el-scrollbar class="page_container" ref="queueScrollbar">
        <div class="scrollbar_content">
            <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
                <el-form-item label="队列名称" prop="projectName">
                    <el-input v-model="queryParams.projectName" placeholder="请输入队列名称" clearable @keyup.enter.native="searchPatientList" />
                </el-form-item>
                <el-form-item label="队列ID" prop="id">
                    <el-input v-model="queryParams.id" placeholder="请输入队列ID" clearable @keyup.enter.native="searchPatientList" />
                </el-form-item>
                <el-form-item label="状态" prop="researchStatus">
                    <el-select v-model="queryParams.researchStatus" clearable>
                        <el-option v-for="(option,index) in patientStatusData" :key="index" :label="option.label" :value="option.value"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="searchPatientList">搜索</el-button>
                    <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
                </el-form-item>
            </el-form>
            <el-row class="mb8">
                <!-- <el-button type="warning" plain icon="el-icon-download" @click="exportFile"
                    v-hasPermi="['system:config:export']">导出</el-button> -->
                <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
            </el-row>
            <!-- 表格风格 -->
            <el-table v-loading="loading" :data="patientList" row-key="rowKey" :default-expand-all="false" :key="refreshTable" ref="queueTable" :tree-props="{children: 'children', hasChildren: 'hasChildren'}" @row-click="toggleExpandRow" :expand-row-keys="expandRowKeys" class="queue_table">
                <el-table-column label="名称" prop="label" min-width="380">
                    <template slot-scope="scope">
                        {{ scope.row.label }} ( {{scope.row.count}}
                        {{ scope.row.type=='2'?'病种': scope.row.type=='3'?'队列':'患者' }}）
                    </template>
                </el-table-column>
                <el-table-column label="队列状态" align="center" width="120" prop="researchStatus">
                    <template slot-scope="scope" v-if="scope.row.type == '4'">
                        <el-tag :type="scope.row.researchStatus == '0' ? 'warning' : 
                            scope.row.researchStatus =='1' ? 'success' : 'info'">
                            {{scope.row.researchStatus == '0' ? '待发布' : 
                            scope.row.researchStatus == '1' ? '进行中' : '已关闭'}}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="患者总数" align="center" min-width="110" prop="totalNum" />
                <el-table-column label="入组患者" align="center" min-width="110" prop="enrollmentNum" />
                <el-table-column label="基线患者" align="center" min-width="110" prop="baseNum" />
                <el-table-column label="随访患者" align="center" min-width="110" prop="followNum" />
                <el-table-column label="填写完成患者" align="center" min-width="110" prop="completeNum" />
                <el-table-column label="失访患者" align="center" min-width="110" prop="lostFollowNum" />
                <el-table-column label="退出患者" align="center" min-width="110" prop="exitNum" />
                <el-table-column label="操作" align="center" fixed="right" width="150">
                    <template slot-scope="scope">
                        <el-button v-if="!scope.row.children||!scope.row.children.length" type="text" @click.stop="checkFollowUp(scope.row)">患者列表
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
        </div>
    </el-scrollbar>
</template>

<script>
import {
    getDiseasesTreeList
} from "@/api/patient/index";

export default {
    name: "patient_list",
    dicts: ['sys_yes_no'],
    data() {
        return {
            // 默认为卡片风格，为false时切换成列表风格
            cardType: true,
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 非单个禁用
            single: true,
            // 非多个禁用
            multiple: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 参数表格数据
            patientList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 日期范围
            dateRange: [],
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                projectName: undefined,
                id: undefined,
                researchStatus: '',
                patientName: '',
            },
            // 表单参数
            form: {},
            patientStatusData: [
                { label: '待发布', value: 0 },
                { label: '进行中', value: 1 },
                { label: '已关闭', value: 2 }
            ],
            expandRowKeys: [],
            refreshTable: '123',
        };
    },
    created() {
        this.getList();
    },
    methods: {
        // 患者状态
        reversePatientStatus(info) {
            let statusList = ['待发布', '进行中', '已关闭']
            return statusList[info]
        },
        /** 查询参数列表 */
        getList() {
            this.loading = true;
            getDiseasesTreeList(this.queryParams).then(response => {
                let resData = response.data;
                this.patientList = resData.map(m => {
                    return {
                        ...m,
                        rowKey: `${m.id}_${m.nodeId}`,
                        children: this.convertChildren(m.children, m.id, m.label)
                    }
                });
                this.loading = false;
            });
        },
        // 
        convertChildren(children, id, label, parentIds) {
            if (children && children.length) {
                let child = children.map(m => {
                    let parentName = Array.isArray(label) ? [...label] : [label];
                    let nextParentName = [...parentName, m.label]
                    let parent_ids = parentIds ? parentIds : [id]
                    let nextParentIds = [...parent_ids, m.id]
                    return {
                        ...m,
                        rowKey: `${id}_${m.id}_${m.nodeId}`,
                        parentName: parentName,
                        parentIds: parent_ids,
                        children: this.convertChildren(m.children, id, nextParentName, nextParentIds)
                    }
                })
                return child
            } else {
                return null
            }
        },
        /** 搜索按钮操作 */
        searchPatientList() {
            this.queryParams.pageNum = 1;
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.dateRange = [];
            this.resetForm("queryForm");
            this.searchPatientList();
        },
        /** 查看随访按钮操作 */
        checkFollowUp(row) {
            let str = row.parentLabel ? row.parentLabel + '-' + row.label : row.label;
            // 将str里的/和\变为- 解决跳转403问题
            let followName = str.replace(/[\\/]/g, '-')
            this.$router.push({
                path: `/patient/list/${followName}?researchId=${row.id}`,

            })
        },
        /** 展开/折叠操作 */
        toggleExpandRow(row, column, cell) {
            if (row.children && column.label != '操作') {
                if (cell.currentTarget.querySelector(".el-table__expand-icon")) {
                    cell.currentTarget.querySelector(".el-table__expand-icon").click()
                }
            }
        },
        // 
        reversePatientName(list, element) {
            let followName = ''
            list.forEach(item => {
                if ("children" in item) {
                    let info = item.children.find(f => f.id == element.id);
                    if (info) {
                        followName = info.parentLabel
                        return followName
                    }
                }
            })
            console.log('followName', followName)
            return followName
        },
        /** 导出按钮操作 */
        exportFile() {
            this.download('system/config/export', {
                ...this.queryParams
            }, `config_${new Date().getTime()}.xlsx`)
        },
        /** 刷新缓存按钮操作 */
        handleRefreshCache() {
            this.getList();
        },
    }
};
</script>
<style lang="scss" scoped>
.el-select {
    width: 100px;
}

::v-deep .queue_table {
    .el-table__row--level-0,
    .el-table__row--level-1 {
        cursor: pointer;
    }
}

.el-input {
    width: 150px;
}

.page_container {
    background: #fff;
    margin: 15px;
    border-radius: 5px;
    height: calc(100vh - 114px);

    .scrollbar_content {
        padding: 20px 20px 30px;
    }
}
</style>