<template>
  <el-row class="details_content">
    <el-row class="btn-back">
      <el-button @click="goBack">返回</el-button>
    </el-row>
    <el-row style="flex: 1; overflow: auto">
      <el-col :span="6" v-loading="isLoadingTreeList">
        <el-scrollbar class="center-scrollbar">
          <el-tree
            :data="treeList"
            node-key="nodeKey"
            class="user_tree_line"
            :props="defaultProps"
            :indent="0"
            @node-click="nodeClick"
            ref="leftTree"
            highlight-current
            accordion
            :current-node-key="currentNodeKey"
            :default-expanded-keys="defaultExpandedKeys"
          ></el-tree>
        </el-scrollbar>
      </el-col>
      <el-col :span="18" v-loading="isLoadingPatientInfo">
        <div class="patient_details">
          <p class="patient_title">
            <span>用药信息 </span>
            <el-button type="primary" @click="openDrugDialog" v-if="showDrugButton">添加用药信息</el-button>
          </p>
          <!-- 用药信息显示 -->
          <el-scrollbar class="patient_info-scrollbar">
            <el-table
              v-if="currentDrugData && currentDrugData.length > 0"
              :data="currentDrugData"
              stripe
              border
              style="width: 100%"
              class="drug-table"
            >
              <el-table-column prop="drugExposureStartDate" label="用药时间" width="300" align="center">
                <template slot-scope="scope">
                  {{ scope.row.drugExposureStartDate || "-" }}
                </template>
              </el-table-column>

              <el-table-column prop="departmentName" label="开单科室" width="150" align="center">
                <template slot-scope="scope">
                  {{ scope.row.departmentName || "-" }}
                </template>
              </el-table-column>
              <el-table-column prop="drugName" label="药品名称" width="300" align="center">
                <template slot-scope="scope">
                  {{ scope.row.drugName || "-" }}
                </template>
              </el-table-column>
              <el-table-column prop="drugType" label="药品类型" width="120" align="center">
                <template slot-scope="scope">
                  {{ scope.row.drugType || "-" }}
                </template>
              </el-table-column>

              <el-table-column prop="dosageForm" label="剂型名称" width="120" align="center">
                <template slot-scope="scope">
                  {{ scope.row.dosageForm || "-" }}
                </template>
              </el-table-column>

              <el-table-column prop="drugSpec" label="药品规格" width="120" align="center">
                <template slot-scope="scope">
                  {{ scope.row.drugSpec || "-" }}
                </template>
              </el-table-column>
              <el-table-column prop="drugUnit" label="药品单位" width="100" align="center">
                <template slot-scope="scope">
                  {{ scope.row.drugUnit || "-" }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150" align="center" v-if="showDrugButton">
                <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="editDrug(scope.row, scope.$index)">编辑</el-button>
                  <el-button size="mini" type="danger" @click="deleteDrug(scope.row, scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-empty v-else :image-size="200"></el-empty>
          </el-scrollbar>
        </div>
      </el-col>
    </el-row>
    <!-- 用药信息弹窗 -->
    <el-dialog title="用药信息" :visible.sync="isShowDrugDialog" width="500px">
      <el-form :model="dialogDrugForm" label-width="120px" class="drug-form" ref="drugFormRef" :rules="drugFormRules">
        <el-form-item label="药品名称" required prop="drugName">
          <el-select
            v-model="dialogDrugForm.drugName"
            placeholder="请选择药品名称"
            filterable
            clearable
            remote
            :remote-method="remoteDrugSearch"
            :loading="drugSearchLoading"
            style="width: 100%"
            @change="handleDrugChange"
            @clear="handleClear"
            @focus="handleDrugFocus"
          >
            <el-option v-for="item in drugDictList" :key="item.id" :label="item.drugName" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="药品类型">
          <el-input v-model="dialogDrugForm.drugType" placeholder="请输入药品类型"></el-input>
        </el-form-item>
        <el-form-item label="剂型名称">
          <el-input v-model="dialogDrugForm.dosageForm" placeholder="请输入剂型名称"></el-input>
        </el-form-item>
        <el-form-item label="用药时间">
          <el-date-picker
            v-model="dialogDrugForm.medicationTime"
            type="date"
            placeholder="请选择用药时间"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="用药天数">
          <el-input v-model="dialogDrugForm.medicationDays" label="请输入用药天数">
            <template slot="append">天</template>
          </el-input>
        </el-form-item>
        <el-form-item label="药品规格">
          <el-input v-model="dialogDrugForm.drugSpec" placeholder="请输入药品规格"></el-input>
        </el-form-item>
        <el-form-item label="药品单位">
          <el-input v-model="dialogDrugForm.drugUnit" placeholder="请输入药品单位"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="isShowDrugDialog = false">取 消</el-button>
        <el-button type="primary" @click="confirmDrugInfo" :loading="isSubmitting">确 定</el-button>
      </span>
    </el-dialog>
  </el-row>
</template>

<script>
import {
  getDrugInfoMpList,
  getDrugInfoList,
  addDrugInfo,
  editDrugInfo,
  deleteDrugInfo,
  getDrugDictList,
} from "../../../api/patient/index.js";

export default {
  name: "drugInfoDetail",
  components: {},
  data() {
    return {
      treeList: [],
      isLoadingTreeList: false,
      isLoadingPatientInfo: false,
      defaultProps: {
        children: "childrenList",
        label: "nodeName",
        key: "nodeKey",
      },
      currentNodeKey: "",
      defaultExpandedKeys: [],
      currentSelectedNodeKey: "",
      drugForm: {
        drugType: "",
        drugName: "",
        dosageForm: "",
        medicationTime: "",
        medicationDays: 1,
        departmentName: "",
      },
      isShowDrugDialog: false,
      dialogDrugForm: {
        drugType: "",
        drugName: "",
        dosageForm: "",
        medicationTime: "",
        medicationDays: 1,
        departmentName: "",
        drugCode: "",
        drugSpec: "",
        drugUnit: "",
      },
      personId: "",
      drugData: {},
      currentDrugData: [],
      showDrugButton: false,
      editingIndex: -1, // 当前编辑的索引
      drugDictList: [],
      drugSearchLoading: false,
      drugFormRules: {
        drugName: [
          {
            required: true,
            message: "请选择药品名称",
            trigger: "change",
          },
        ],
      },
      isSubmitting: false,
    };
  },
  created() {
    let queryInfo = this.$route.query;
    if (queryInfo.personId) {
      this.personId = queryInfo.personId;
      sessionStorage.setItem("currentPatientPersonId", this.personId);
    } else {
      this.personId = sessionStorage.getItem("currentPatientPersonId") || "";
    }
    this.initData();
  },
  methods: {
    goBack() {
      let oldPath = decodeURIComponent(this.$route.query.oldPath);
      const obj = {
        path: oldPath,
      };
      this.$tab.closeOpenPage(obj);
    },
    initData() {
      // 获取药品字典
      this.queryDrugDictList();
      // 构建固定的树结构
      this.getTreeList();
      // 获取原始用药信息
      this.queryMpList();
      // 获取新加开药信息列表
      this.queryDrugInfoList();
    },
    // 获取药品字典
    queryDrugDictList() {
      let params = {
        pageNum: 1,
        pageSize: 50,
      };
      getDrugDictList(params)
        .then((res) => {
          this.drugDictList = res.rows;
        })
        .catch(() => {
          this.$message.error("获取药品字典失败");
        });
    },
    getTreeList() {
      // 构建固定的树结构
      const hospitalDrugInfo = {
        id: 1,
        nodeId: "hospital_drug_info",
        nodeName: "院内用药信息",
        nodeKey: "hospital_drug_info",
        treeType: 1,
        childrenList: [],
      };
      const doctorRecordDrugInfo = {
        id: 2,
        nodeId: "doctor_record_drug_info",
        nodeName: "用药信息（医生记录）",
        nodeKey: "doctor_record_drug_info",
        treeType: 2,
        childrenList: [],
      };

      this.treeList = [hospitalDrugInfo, doctorRecordDrugInfo];

      // 设置默认展开第一个节点
      this.setDefaultExpanded(this.treeList);
    },
    setDefaultExpanded(data) {
      if (data && data.length > 0) {
        // 默认展开第一个节点
        this.defaultExpandedKeys = [data[0].nodeKey];
        this.currentNodeKey = data[0].nodeKey;
        this.$nextTick(() => {
          if (this.$refs.leftTree) {
            this.$refs.leftTree.setCurrentKey(this.currentNodeKey);
          }
        });
      }
    },
    queryMpList() {
      let params = {
        personId: this.personId,
        // personId: "10954341",
      };
      getDrugInfoMpList(params)
        .then((res) => {
          this.updateTreeWithDrugData(res.data, "hospital_drug_info", 1, 3);
        })
        .catch(() => {
          this.$message.error("获取用药信息失败");
        });
    },
    queryDrugInfoList() {
      let params = {
        personId: this.personId,
      };
      getDrugInfoList(params)
        .then((res) => {
          this.updateTreeWithDrugData(res.data, "doctor_record_drug_info", 2, 100);
        })
        .catch(() => {
          this.$message.error("获取用药信息失败");
        });
    },
    updateTreeWithDrugData(drugData, nodeKey, treeType, idStart = 3) {
      const drugNode = this.treeList.find((node) => node.nodeKey === nodeKey);
      if (drugNode && drugData) {
        // 清空原有子节点
        drugNode.childrenList = [];
        Object.keys(drugData).forEach((visitOccurrenceId, index) => {
          const drugRecords = drugData[visitOccurrenceId];
          drugNode.childrenList.push({
            id: index + idStart,
            nodeId: `${nodeKey}_${visitOccurrenceId}`,
            nodeName: `${visitOccurrenceId}`,
            nodeKey: `${nodeKey}_${visitOccurrenceId}`,
            treeType: treeType,
            childrenList: [],
            rawData: drugRecords,
          });
        });
        // 强制更新视图
        this.$forceUpdate();
      }
    },
    // 节点单击事件
    nodeClick(node, event) {
      console.log("node", node, event);
      this.showDrugButton = node.nodeKey === "doctor_record_drug_info" || node.nodeKey.startsWith("doctor_record_drug_info_");
      this.currentSelectedNodeKey = node.nodeKey;
      if (node.rawData && Array.isArray(node.rawData)) {
        // 如果是包含用药数据的子节点
        this.currentDrugData = node.rawData;
      } else {
        // 如果是父节点或其他节点，清空显示
        this.currentDrugData = [];
      }
    },
    setCurrentNode() {
      if (this.currentSelectedNodeKey && this.$refs.leftTree) {
        this.$nextTick(() => {
          this.$refs.leftTree.setCurrentKey(this.currentSelectedNodeKey);
        });
      }
    },
    // 打开新增/编辑弹窗
    openDrugDialog(drug = null, index = -1) {
      if (drug) {
        // 编辑
        this.dialogDrugForm = {
          drugType: drug.drugType || "",
          drugName: drug.drugName || "",
          dosageForm: drug.dosageForm || "",
          medicationTime: drug.drugExposureStartDate || "",
          medicationDays: drug.daysSupply || 0,
          departmentName: drug.departmentName || "",
          drugSpec: drug.drugSpec || "",
          drugUnit: drug.drugUnit || "",
        };
        this.editingIndex = index;
      } else {
        // 添加
        this.dialogDrugForm = {
          drugType: "",
          drugName: "",
          dosageForm: "",
          medicationTime: "",
          medicationDays: 1,
          departmentName: "",
        };
        this.editingIndex = -1;
      }
      this.$nextTick(() => {
        if (this.$refs.drugFormRef) {
          this.$refs.drugFormRef.clearValidate();
        }
      });
      this.isShowDrugDialog = true;
    },
    confirmDrugInfo() {
      if (this.isSubmitting) {
        return;
      }
      this.$refs.drugFormRef.validate((valid) => {
        if (valid) {
          this.isSubmitting = true;
          this.isShowDrugDialog = false;
          // 保存当前选中的节点key和节点类型信息
          const savedNodeKey = this.currentSelectedNodeKey;
          const savedNodeType = this.getNodeType(savedNodeKey);
          const params = {
            personId: this.personId,
            drugType: this.dialogDrugForm.drugType || "",
            drugValue: this.dialogDrugForm.drugName || "",
            dosageForm: this.dialogDrugForm.dosageForm || "",
            drugExposureStartDate: this.dialogDrugForm.medicationTime || "",
            daysSupply: Number(this.dialogDrugForm.medicationDays) || 0,
            departmentName: this.dialogDrugForm.departmentName || "",
            drugCode: this.dialogDrugForm.drugCode || "",
            drugSpec: this.dialogDrugForm.drugSpec || "",
            drugUnit: this.dialogDrugForm.drugUnit || "",
          };
          if (this.editingIndex === -1) {
            // 新增
            addDrugInfo(params)
              .then(() => {
                this.$message.success("用药信息添加成功");
                return Promise.all([this.queryDrugInfoList(), this.queryMpList()]);
              })
              .then(() => {
                // 恢复之前选中的节点状态
                this.restoreSelectedNode(savedNodeKey, savedNodeType);
              })
              .catch(() => {
                this.$message.error("用药信息操作失败");
              })
              .finally(() => {
                this.isSubmitting = false;
              });
          } else {
            // 编辑操作
            const editParams = {
              ...params,
              id: this.currentDrugData[this.editingIndex].id,
            };
            editDrugInfo(editParams)
              .then(() => {
                this.$message.success("用药信息编辑成功");
                return Promise.all([this.queryDrugInfoList(), this.queryMpList()]);
              })
              .then(() => {
                // 恢复之前选中的节点状态
                this.restoreSelectedNode(savedNodeKey, savedNodeType);
              })
              .catch(() => {
                this.$message.error("用药信息编辑失败");
              })
              .finally(() => {
                this.isSubmitting = false;
              });
          }
        }
      });
    },
    // 编辑
    editDrug(drug, index) {
      this.openDrugDialog(drug, index);
    },
    // 删除
    deleteDrug(drug, index) {
      this.$confirm("确定要删除这条用药信息吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // 保存当前选中的节点key和节点类型信息
          const savedNodeKey = this.currentSelectedNodeKey;
          const savedNodeType = this.getNodeType(savedNodeKey);
          deleteDrugInfo(drug.id)
            .then(() => {
              this.$message.success("用药信息删除成功");
              return Promise.all([this.queryDrugInfoList(), this.queryMpList()]);
            })
            .then(() => {
              // 恢复之前选中的节点状态
              this.restoreSelectedNode(savedNodeKey, savedNodeType);
            })
            .catch(() => {
              this.$message.error("用药信息操作失败");
            });
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },
    // 获取节点类型
    getNodeType(nodeKey) {
      if (!nodeKey) return "";
      if (nodeKey === "hospital_drug_info" || nodeKey === "doctor_record_drug_info") {
        return "parent";
      } else if (nodeKey.startsWith("hospital_drug_info_")) {
        return "hospital_child";
      } else if (nodeKey.startsWith("doctor_record_drug_info_")) {
        return "doctor_child";
      }
      return "";
    },
    restoreSelectedNode(savedNodeKey, savedNodeType) {
      if (!savedNodeKey) return;
      setTimeout(() => {
        this.$nextTick(() => {
          if (this.$refs.leftTree) {
            let targetNode = null;
            targetNode = this.findNodeByKey(this.treeList, savedNodeKey);
            if (!targetNode && savedNodeType) {
              targetNode = this.findNodeByType(this.treeList, savedNodeType);
            }
            if (targetNode) {
              // 设置当前选中的节点
              this.currentSelectedNodeKey = targetNode.nodeKey;
              this.$refs.leftTree.setCurrentKey(targetNode.nodeKey);
              // 重新触发节点点击事件以刷新右侧数据
              this.nodeClick(targetNode);
            } else {
              // 如果找不到任何匹配的节点，默认选中第一个节点
              if (this.treeList && this.treeList.length > 0) {
                const firstNode = this.treeList[0];
                this.currentSelectedNodeKey = firstNode.nodeKey;
                this.$refs.leftTree.setCurrentKey(firstNode.nodeKey);
                this.nodeClick(firstNode);
              }
            }
          }
        });
      }, 100);
    },
    findNodeByKey(nodes, key) {
      for (let node of nodes) {
        if (node.nodeKey === key) {
          return node;
        }
        if (node.childrenList && node.childrenList.length > 0) {
          const found = this.findNodeByKey(node.childrenList, key);
          if (found) {
            return found;
          }
        }
      }
      return null;
    },
    // 根据节点类型查找节点
    findNodeByType(nodes, nodeType) {
      for (let node of nodes) {
        if (nodeType === "parent" && (node.nodeKey === "hospital_drug_info" || node.nodeKey === "doctor_record_drug_info")) {
          return node;
        }
        if (nodeType === "hospital_child" && node.nodeKey.startsWith("hospital_drug_info_")) {
          return node;
        }
        if (nodeType === "doctor_child" && node.nodeKey.startsWith("doctor_record_drug_info_")) {
          return node;
        }
        if (node.childrenList && node.childrenList.length > 0) {
          const found = this.findNodeByType(node.childrenList, nodeType);
          if (found) {
            return found;
          }
        }
      }
      return null;
    },
    findNodeByKey(nodes, key) {
      for (let node of nodes) {
        if (node.nodeKey === key) {
          return node;
        }
        if (node.childrenList && node.childrenList.length > 0) {
          const found = this.findNodeByKey(node.childrenList, key);
          if (found) {
            return found;
          }
        }
      }
      return null;
    },
    // 药品选择change事件
    handleDrugChange(selectedDrugId) {
      if (!selectedDrugId) {
        this.dialogDrugForm.drugType = "";
        this.dialogDrugForm.dosageForm = "";
        this.dialogDrugForm.drugCode = "";
        this.dialogDrugForm.drugName = "";
        this.dialogDrugForm.drugSpec = "";
        this.dialogDrugForm.drugUnit = "";

        return;
      }
      const selectedDrug = this.drugDictList.find((item) => item.id === selectedDrugId);
      if (selectedDrug) {
        this.dialogDrugForm.drugType = selectedDrug.drugMaterialType || "";
        this.dialogDrugForm.dosageForm = selectedDrug.drugForm || "";
        this.dialogDrugForm.drugCode = selectedDrug.drugCode || "";
        this.dialogDrugForm.drugName = selectedDrug.drugName || "";
        this.dialogDrugForm.drugSpec = selectedDrug.drugSpec || "";
        this.dialogDrugForm.drugUnit = selectedDrug.drugUnit || "";
      }
    },
    // 搜索药品
    remoteDrugSearch(query) {
      if (query !== "") {
        this.drugSearchLoading = true;
        let params = {
          pageNum: 1,
          pageSize: 50,
          drugName: query,
        };
        getDrugDictList(params)
          .then((res) => {
            this.drugDictList = res.rows;
            this.drugSearchLoading = false;
          })
          .catch(() => {
            this.drugSearchLoading = false;
            this.$message.error("搜索药品失败");
          });
      } else {
        // 清空时恢复默认查询
        this.queryDrugDictList();
      }
    },
    // 清空
    handleClear() {
      this.queryDrugDictList();
    },
    // 药品输入框获取焦点时，重新加载默认药品列表
    handleDrugFocus() {
      this.queryDrugDictList();
    },
  },
};
</script>
<style lang="scss" scoped>
.details_content {
  height: calc(100vh - 115px);
  color: #333;
  padding: 20px 20px;
  margin: 15px;
  background-color: #fff;
  border-radius: 5px;
  overflow: hidden;
  box-shadow: 0px 0px 10px 10px rgb(0, 0, 0, 0.01);
  display: flex;
  flex-direction: column;
  .btn-back {
    padding-bottom: 10px;
  }
  .el-col {
    height: 100%;
    border: 1px #ededed solid;
    overflow: hidden;

    ::v-deep.center-scrollbar {
      height: 100%;
      overflow-x: hidden;

      .el-scrollbar__view {
        height: 100%;

        .el-tree {
          padding: 10px 0;
        }
      }
    }

    .patient_details {
      margin: 0 10px;
      height: 100%;

      .patient_title {
        text-indent: 5px;
        margin: 20px 0 10px;
        font-size: 1.2em;
        font-weight: bold;
        color: #409eff;
        display: flex;
        justify-content: space-between;
      }

      .patient_info-scrollbar {
        height: calc(100% - 50px);
        border-bottom: 1px #ededed solid;

        ::v-deep .el-scrollbar__wrap {
          overflow-x: hidden;
        }
        .drug-cards-container {
          display: flex;
          flex-direction: column;
          gap: 12px;
          padding: 16px;
        }
        .drug-card {
          border-radius: 8px;
          border: 1px solid #e8e8e8;
          background: #ffffff;
          box-shadow: 0 2px 6px rgba(0, 0, 0, 0.04);
          transition: all 0.2s ease;
          &:hover {
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            border-color: #409eff;
          }
          ::v-deep .el-card__body {
            padding: 16px;
          }
          .drug-info {
            p {
              margin: 6px 0;
              font-size: 13px;
              line-height: 1.4;
              color: #666;
              strong {
                color: #409eff;
                font-weight: 500;
                min-width: 70px;
                display: inline-block;
              }
            }
          }
        }
        .no-data {
          display: flex;
          justify-content: center;
          align-items: center;
          height: 120px;
          color: #999;
          font-size: 14px;
        }

        ::v-deep .patient_info {
          margin: 5px 0;
          .patient_label {
            color: #333;
          }
          .patient_content {
            width: 30%;
          }
        }
      }
    }
  }
}
</style>
