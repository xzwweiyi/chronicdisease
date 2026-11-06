<template>
  <div v-loading="isLoading">
    <el-scrollbar class="addEvaluation_content">
      <div class="addEvaluation_style">
        <el-table :data="standardList" border highlight-current-row>
          <el-table-column
            label="分值区间"
            align="center"
            prop="scoreVal"
            width="200"
          >
            <template slot-scope="scope">
              {{ scope.row.minValue }}~{{ scope.row.maxValue }}
            </template>
          </el-table-column>
          <el-table-column
            label="评价结果"
            align="center"
            prop="evaluationResult"
          >
            <template slot-scope="scope">
              <div
                class="reveal-container"
                v-html="scope.row.evaluationResult"
              ></div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="180">
            <template slot-scope="scope">
              <el-button
                type="text"
                icon="el-icon-edit"
                @click="editEvaluationInfo(scope.row, scope.$index)"
              >
                编辑</el-button
              >
              <!-- v-hasPermi="[permisEdit]"  2025/6/5，权限放开 -->
              <el-button
                type="text"
                icon="el-icon-delete"
                class="delete_btn"
                @click="deleteEvaluationInfo(scope.row)"
                >删除
              </el-button>
              <!-- v-hasPermi="[permisDelete]" 2025/6/5，权限放开 -->
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-scrollbar>
    <el-form class="add_evaluation" label-width="100px" :model="evaluationForm">
      <el-form-item label="分值区间">
        <el-input
          type="text"
          placeholder="区间最小值"
          class="evaluation"
          v-model="evaluationForm.minValue"
        />
        ~
        <el-input
          type="text"
          placeholder="区间最大值"
          class="evaluation"
          v-model="evaluationForm.maxValue"
        />
        <span class="evaluation_text">问卷总分： {{ evaluationTotal }}</span>
      </el-form-item>
      <el-form-item label="评价结果">
        <el-input
          type="textarea"
          class="result_val"
          :rows="4"
          v-model="evaluationForm.evaluationResult"
        >
        </el-input>
      </el-form-item>
      <el-form-item class="form_content">
        <el-button
          type="primary"
          @click="addEvaluation"
          :disabled="
            (!evaluationForm.minValue && evaluationForm.minValue != 0) ||
            evaluationForm.minValue === '' ||
            (!evaluationForm.maxValue && evaluationForm.maxValue != 0) ||
            evaluationForm.maxValue === '' ||
            !evaluationForm.evaluationResult ||
            evaluationForm.evaluationResult === ''
          "
        >
          添加该设置</el-button
        >
      </el-form-item>
    </el-form>
    <div class="footer_btn">
      <el-button type="primary" @click="closeSetEvaluation">确认</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "standardCom",
  props: [
    "isShowSetEvaluation",
    "medicalFormId",
    "searchList",
    "evaluationTotal",
    "permisAdd",
    "permisEdit",
    "permisDelete",
  ],
  components: {},
  data() {
    return {
      evaluationForm: {},
      fraction: 100,
      isLoading: false,
    };
  },
  created() {},
  watch: {},
  computed: {
    standardList() {
      return this.searchList.map((m) => {
        return {
          ...m,
          evaluationResult: m.evaluationResult.replace(/\n/g, "<br>"),
        };
      });
    },
  },
  mounted() {},
  methods: {
    // 添加该设置
    addEvaluation() {
      this.evaluationForm["minValue"] = this.evaluationForm["minValue"]
        .toString()
        .replace(/\s*/g, "");
      this.evaluationForm["maxValue"] = this.evaluationForm["maxValue"]
        .toString()
        .replace(/\s*/g, "");
      this.evaluationForm["minValue"] = this.evaluationForm["minValue"].replace(
        /\s*/g,
        ""
      );
      this.evaluationForm["maxValue"] = this.evaluationForm["maxValue"].replace(
        /\s*/g,
        ""
      );
      console.log(
        this.evaluationForm["minValue"],
        this.evaluationForm["maxValue"],
        this.evaluationForm.evaluationResult
      );
      if (
        !this.evaluationForm["minValue"] ||
        this.evaluationForm["minValue"] == "" ||
        !this.evaluationForm["maxValue"] ||
        this.evaluationForm["maxValue"] == "" ||
        !this.evaluationForm["evaluationResult"] ||
        this.evaluationForm["evaluationResult"] == ""
      ) {
        this.$message.error("分值区间和评价结果不能为空");
        return;
      } else if (
        isNaN(this.evaluationForm["minValue"]) ||
        isNaN(this.evaluationForm["maxValue"])
      ) {
        this.$message.error("分值区间最大值和最小值只能为数字");
        return;
      } else if (
        Number(this.evaluationForm["minValue"]) >
        Number(this.evaluationForm["maxValue"])
      ) {
        this.$message.error("分值区间最小值应该小于分值区间最大值");
        return;
      } else {
        if (this.evaluationForm["id"] && this.searchList.length) {
          this.$emit("edit-evaluate-item", this.evaluationForm);
        } else {
          this.$emit("add-evaluate-item", this.evaluationForm);
        }
        this.evaluationForm = {
          minValue: "",
          maxValue: "",
          evaluationResult: "",
        };
      }
    },
    // 关闭测评结果
    closeSetEvaluation() {
      this.$emit("close-dialog", false);
    },
    // 删除评分数据
    deleteEvaluationInfo(event) {
      this.$confirm(`确定要删除“${event.evaluationResult}”吗?`, "提示", {
        type: "warning",
      })
        .then(() => {
          this.$emit("del-evaluate-item", event.id);
        })
        .catch((error) => {
          return false;
        });
    },
    // 编辑测评数据
    editEvaluationInfo(event) {
      this.evaluationForm = {
        ...event,
      };
    },
  },
};
</script>

<style lang="scss" scoped>
.display-flex {
  display: flex;
}

.list_container {
  background: #fff;
  padding: 20px;
  margin: 15px;

  .table-buttons {
    align-items: center;
  }

  .templateList {
    padding-top: 10px;
  }
}

.add_question_form {
  margin-top: 0;
}

.delete_btn {
  color: #ff9292;
}

.addEvaluation_content {
  height: calc(90vh - 500px);

  .addEvaluation_style {
    padding: 0 10px;
  }
}

.add_evaluation {
  border: 1px #ededed solid;
  margin-top: 20px;
  padding: 20px 20px;
  border-radius: 5px;

  .el-form-item {
    .evaluation {
      width: 30%;
    }

    .evaluation_text {
      margin-left: 10px;
    }
  }

  .form_content {
    margin-bottom: 0;
    text-align: center;

    .el-button {
      margin-left: -100px;
    }
  }
}

.footer_btn {
  margin-top: 15px;
  text-align: center;
}
</style>
