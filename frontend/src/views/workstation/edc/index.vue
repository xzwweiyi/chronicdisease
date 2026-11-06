<template>
  <div class="page_content">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="6" :xs="24">
        <div class="edc_tree_title">任务列表</div>
        <div class="tree_search_box">
          <el-input
            v-model="treeQuery.diseasesName"
            placeholder="请输入病种名称"
            clearable
            @keyup.enter.native="handleTreeSearch"
            @clear="resetTreeSearch"
          >
            <el-button slot="append" icon="el-icon-search" @click="handleTreeSearch"></el-button>
          </el-input>
        </div>
        <el-scrollbar class="scrollbar_tree">
          <div class="head-container">
            <el-tree
              :data="deptOptions"
              class="user_tree_line"
              :props="defaultProps"
              :filter-node-method="filterNode"
              ref="tree"
              :indent="0"
              accordion
              @node-click="treeNodeClick"
              highlight-current
              node-key="nodeKey"
              :default-expanded-keys="defaultExpandedKeys"
              :current-node-key="currentNodeKey"
            >
              <span class="custom-tree-node" slot-scope="{ data }">
                <span>{{ data.nodeName ? data.nodeName : "无" }}</span>
                <span v-if="data.childrenList">（待处理：{{ data.handleNum ? data.handleNum : "0" }}）</span>
              </span>
            </el-tree>
          </div>
        </el-scrollbar>
      </el-col>
      <!--用户数据-->
      <el-col :span="18" :xs="24">
        <div class="fatherBox">
          <el-scrollbar style="height: 100%">
            <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" class="search_form">
              <el-form-item label="患者" prop="patientName">
                <el-input
                  v-model="queryParams.patientName"
                  placeholder="请输入患者"
                  clearable
                  style="width: 240px"
                  @keyup.enter.native="handleQuery"
                  id="patientName"
                />
              </el-form-item>
              <el-form-item label="患者ID" prop="patientId">
                <el-input
                  v-model="queryParams.patientId"
                  placeholder="请输入患者ID"
                  clearable
                  style="width: 240px"
                  @keyup.enter.native="handleQuery"
                  id="patientId"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索 </el-button>
                <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
              </el-form-item>
            </el-form>
            <!-- <el-row class="mb8">
                            <el-button type="warning" plain icon="el-icon-download" @click="handleExport"
                                v-hasPermi="['system:user:export']">导出</el-button>
                        </el-row> -->

            <el-table v-loading="loading" :data="userList">
              <el-table-column label="患者ID" align="center" width="130" key="patientId" prop="patientId" />
              <el-table-column label="患者编码" align="center" width="130" key="patientCode" prop="patientCode" v-if="showFlag" />
              <el-table-column label="患者名称" align="center" min-width="140" key="patientName" prop="patientName" />
              <el-table-column label="访视点" align="center" key="name" prop="name">
                <template slot-scope="scope">
                  {{ scope.row.name | nullFormat }}
                </template>
              </el-table-column>
              <el-table-column label="创建时间" align="center" prop="createTime" width="190">
                <template slot-scope="scope">
                  <span>{{ scope.row.createTime | dateFormat }}</span>
                </template>
              </el-table-column>
              <el-table-column label="锁库时间" align="center" prop="lockVisitTime" width="190" v-if="nodeTypeStatus == 4">
                <template slot-scope="scope">
                  <span>{{ scope.row.updateTime | dateFormat }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" fixed="right" width="180">
                <template slot-scope="scope">
                  <el-button type="text" @click="toUserDetail(scope.row)" v-hasPermi="['edc:details:check']">查看详情 </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-scrollbar>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  edcTreeList,
  getEDCPatientList, //入组列表
} from "@/api/follow/work";
import { obesityShow } from "@/api/info/config";
export default {
  name: "WorkstationEdc",
  data() {
    return {
      showFlag: false,
      // 遮罩层
      loading: false,
      researchId: "",
      nodeTypeStatus: "",
      showSearch: true,
      userList: [],
      deptOptions: [],
      nodeName: undefined,
      queryParams: {},
      treeQuery: {
        diseasesName: "",
      },
      defaultProps: {
        children: "childrenList",
        label: "nodeName",
        key: "nodeKey",
      },
      id: "",
      expandedList: [],
      defaultExpandedKeys: [], //默认展开的节点
      currentNodeKey: "", //当前选中的节点
      nodeType: "",
    };
  },
  watch: {},
  created() {
    const queryDaya = this.$route.query;
    this.treeQuery.diseasesName = queryDaya.diseasesName || "";
    this.getTreeselect();
    if (queryDaya.defaultExpandedKeys) {
      this.defaultExpandedKeys = queryDaya.defaultExpandedKeys ? queryDaya.defaultExpandedKeys.split(",") : [];
      this.currentNodeKey = this.defaultExpandedKeys[this.defaultExpandedKeys.length - 1];
      this.researchId = queryDaya.researchId;
      this.nodeTypeStatus = queryDaya.nodeTypeStatus;
      this.handleQuery();
    } else {
      this.getEDCWorkList();
    }
  },
  methods: {
    /** 查询列表 */
    getEDCWorkList() {
      this.loading = true;
      const queryData = {
        researchId: this.researchId,
        edcStatus: this.nodeTypeStatus,
        ...this.queryParams,
      };
      getEDCPatientList(queryData).then((response) => {
        this.userList = response.data;
        this.loading = false;
      });
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      edcTreeList(this.getTreeQueryParams()).then((response) => {
        this.deptOptions =
          response.data.childrenList && response.data.childrenList.length
            ? response.data.childrenList
                .map((m) => {
                  return {
                    ...m,
                    nodeKey: `${m.id}_${m.nodeId}`,
                    childrenList: this.convertTreesData(m.childrenList, m.id),
                  };
                })
                .filter((f) => f.childrenList && f.childrenList.length)
            : [];
      });
    },
    getTreeQueryParams() {
      return {
        diseasesName: this.treeQuery.diseasesName || undefined,
      };
    },
    handleTreeSearch() {
      this.defaultExpandedKeys = [];
      this.currentNodeKey = "";
      this.updateRouteQuery({
        diseasesName: this.treeQuery.diseasesName || undefined,
        defaultExpandedKeys: undefined,
      });
      this.getTreeselect();
    },
    resetTreeSearch() {
      if (!this.treeQuery.diseasesName) {
        this.handleTreeSearch();
        return;
      }
      this.treeQuery.diseasesName = "";
      this.handleTreeSearch();
    },
    updateRouteQuery(extraQuery = {}) {
      const nextQuery = {
        ...this.$route.query,
        ...extraQuery,
      };
      Object.keys(nextQuery).forEach((key) => {
        if (nextQuery[key] === undefined || nextQuery[key] === null || nextQuery[key] === "") {
          delete nextQuery[key];
        }
      });
      this.$router.replace({
        path: this.$route.path,
        query: nextQuery,
      });
    },
    buildOldPath(item) {
      const query = {
        defaultExpandedKeys: this.defaultExpandedKeys.toString(),
        nodeTypeStatus: this.nodeTypeStatus,
        nodeType: this.nodeType,
        researchId: item && item.researchId ? item.researchId : this.researchId,
        diseasesName: this.treeQuery.diseasesName || undefined,
      };
      const queryString = Object.keys(query)
        .filter((key) => query[key] !== undefined && query[key] !== null && query[key] !== "")
        .map((key) => `${key}=${encodeURIComponent(query[key])}`)
        .join("&");
      return queryString ? `${this.$route.path}?${queryString}` : this.$route.path;
    },
    convertTreesData(childrenList, id) {
      if (childrenList && childrenList.length) {
        let child = childrenList.map((m) => {
          return {
            ...m,
            nodeKey: `${id}_${m.id}_${m.nodeId}`,
            childrenList: this.convertTreesData(m.childrenList, id),
          };
        });
        return child;
      } else {
        return null;
      }
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    treeNodeClick(data, node) {
      // console.log('treeNodeClick', node, data)
      if (data.nodeTypeStatus == 0) return;
      this.defaultExpandedKeys = [];
      this.currentNodeKey = node.data.nodeKey;
      this.nodeType = data.nodeType;
      this.researchId = data.researchId;
      this.nodeTypeStatus = data.nodeTypeStatus;
      this.convertDefaultKeys(node);
      this.getEDCWorkList();
      this.$router.push({
        path: this.$route.path,
        query: {
          defaultExpandedKeys: this.defaultExpandedKeys.toString(),
          nodeTypeStatus: this.nodeTypeStatus,
          nodeType: this.nodeType,
          researchId: this.researchId,
          diseasesName: this.treeQuery.diseasesName || undefined,
        },
      });
      console.log("node", node);
      if (node.level == 4) {
        this.obesityShow(data.researchId);
      }
    },

    obesityShow(researchId) {
      obesityShow(researchId).then((response) => {
        this.showFlag = response.data;
        console.log("showFlag", this.showFlag);
      });
    },

    convertDefaultKeys(node) {
      if (node.level > 0) {
        this.defaultExpandedKeys.unshift(node.data.nodeKey);
        this.convertDefaultKeys(node.parent);
      }
    },
    // 修改入组
    handleUpdate() {},
    // 随访意见
    toUserDetail(item) {
      this.$router.push({
        path: "/workstation/edc/userDetail",
        query: {
          patientId: item.patientId,
          followId: item.followId,
          recordId: item.recordId,
          edcStatus: item.edcStatus,
          researchId: item.researchId,
          pageType: "2",
          oldPath: encodeURIComponent(this.buildOldPath(item)),
        },
      });
    },
    exportLockInfo(item) {},
    /** 搜索按钮操作 */
    handleQuery() {
      this.getEDCWorkList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "/edc/work/export",
        {
          researchId: this.researchId,
          edcStatus: this.nodeTypeStatus,
          ...this.queryParams,
        },
        `user_${new Date().getTime()}.xlsx`
      );
    },
    /**
     * 节点展开
     */
    // handleNodeExpand(data, node) {
    //     console.log(data)
    //     // 保存当前展开的节点
    //     let flag = false
    //     this.expandedList.some(ele => {
    //         if(ele === data[this.id]) {
    //             // 判断当前节点是否存在，存在则不做处理
    //             flag = true
    //             return true
    //         }
    //     })

    // if (!flag) {
    //         // 不存在则存到数组中
    //         this.expandedList.push(data[this.id])
    //     }
    //     console.log('this.expandedList', this.expandedList)
    // },
    // handleNodeCollapse(data, node) {

    // },
  },
};
</script>

<style lang="scss" scoped>
.custom-tree-node {
  font-size: 14px;
}

.page_content {
  padding: 10px 20px;
  margin: 15px;
  background-color: #fff;
  border-radius: 5px;
  min-height: calc(100vh - 114px);
  overflow: hidden;
  box-shadow: 0px 0px 10px 10px rgb(0, 0, 0, 0.01);

  .search_form {
    margin-top: 10px;
  }
}

.fatherBox {
  height: calc(100vh - 140px);
}

.tree_title {
  line-height: 40px;
  color: #333;
  font-size: 14px;
  text-indent: 15px;
}

/* element滚动条组件 隐藏水平滚动条 */

.is-horizontal {
  display: none;
}

.scrollbar_tree {
  height: calc(100vh - 194px);
}

.edc_tree_title {
  height: 40px;
  font-size: 16px;
  font-weight: bold;
}

.tree_search_box {
  margin-bottom: 12px;
}
</style>
