<template>
  <div class="follow_form">
    <div class="boxBtn" v-if="followItem.fmedicalForms.length == 0">
      <el-button
        type="primary"
        v-hasPermi="['base:medical:list']"
        @click="handCreat(followItem)"
        :disabled="researchStatus == '2'"
      >
        创建新的CRF</el-button
      >
      <el-button
        type="success"
        @click="importCRF(followItem)"
        :disabled="researchStatus == '2'"
        >CRF库导入</el-button
      >
    </div>
    <div class="boxBtn" v-else>
      <el-scrollbar style="height: 100%">
        <div class="boxBtnList">
          <el-tooltip
            class="item"
            effect="dark"
            v-for="(items, index) in followItem.fmedicalForms"
            :key="index"
            :disabled="items.medicalName.length < 11"
            :content="items.medicalName"
            placement="bottom"
          >
            <el-tag
              :type="
                items.formType == '0'
                  ? 'primary'
                  : items.formType == '1' ||
                    items.formType == '2' ||
                    items.formType == '3'
                  ? 'warning'
                  : items.formType == '5'
                  ? 'success'
                  : 'danger'
              "
              plain
            >
              {{ items.medicalName }}
            </el-tag>
          </el-tooltip>
        </div>
      </el-scrollbar>
      <div class="form_btn">
        <!-- :disabled="researchStatus != '0'" -->
        <el-button
          type="text"
          @click="importCRF(followItem)"
          :disabled="researchStatus == '2'"
          >CRF库导入</el-button
        >
        <!-- <el-button
          type="text"
          plain
          @click="handCreat(followItem)"
          v-hasPermi="['base:medical:list']"
          :disabled="researchStatus != '0'"
          >编辑</el-button
        > -->
        <el-button
          type="text"
          plain
          @click="handCreat(followItem)"
          v-hasPermi="['base:medical:list']"
          >编辑</el-button
        >
        <el-button
          type="text"
          plain
          @click="followPreview(followItem)"
          v-hasPermi="['base:medical:list']"
          >预览</el-button
        >
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["followItem", "researchStatus"],
  data() {
    return {};
  },
  computed: {},
  watch: {},
  created() {},
  methods: {
    importCRF() {
      this.$emit("import-medical", this.followItem);
    },
    handCreat() {
      this.$emit("creat-medical", this.followItem);
    },
    followPreview() {
      this.$emit("follow-preview", this.followItem);
    },
  },
};
</script>

<style lang="scss" scoped>
.follow_form {
  .boxBtn {
    display: flex;
    flex-direction: column;
    justify-content: center;
    height: 440px;
    padding: 15px 20px 10px;

    .el-scrollbar__wrap {
      overflow-x: hidden !important;
    }

    .el-scrollbar__bar.is-horizontal {
      display: none;
    }

    .el-button,
    .el-tag {
      margin-left: 0px;
      margin-bottom: 10px;
    }

    .el-tag {
      height: 36px;
      line-height: 36px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  .boxBtnList {
    display: flex;
    justify-content: center;
    flex-direction: column;
  }

  .form_btn {
    padding: 10px 0;

    .el-button {
      margin-bottom: 0;
    }

    .el-button + .el-button {
      margin-left: 20px;
    }
  }
}
</style>
