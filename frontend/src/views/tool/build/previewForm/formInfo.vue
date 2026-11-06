<template>
  <el-row>
    <el-col
      :span="element.span || 24"
      v-for="(element, elIndex) in previewFormData"
      :key="elIndex"
      class="drawing-item"
    >
      <el-row
        v-if="element.labelType == 'form' && element.isShowDom"
        class="form_row_style"
      >
        <template>
          <el-col :span="24">
            <div class="open_multiple">
              <el-button
                type="text"
                v-if="element.openMultiple == '1'"
                @click="addMultipleForm(element.formCode)"
                >新增</el-button
              >
              <el-button
                type="text"
                class="el-button-text_delete"
                v-if="element.isDeleteForm"
                @click="deleteFormMultiple(element.formCode)"
                >删除</el-button
              >
            </div>
            <el-popover
              placement="left"
              title="注释"
              trigger="hover"
              v-if="element.isExplanatoryNote"
              effect="light"
              popper-class="custom_tip_popover"
              :visible-arrow="false"
            >
              <div v-html="element.explanatoryNote"></div>
              <el-button
                plain
                type="text"
                slot="reference"
                class="form_explanatory_note"
                icon="el-icon-info"
              ></el-button>
            </el-popover>
            <span class="subTitle_style" v-if="!element.isHideName">{{
              element.formName
            }}</span>
            <span class="subTitle_style" v-if="element.isShowSubTitle">{{
              element.subTitle
            }}</span>
          </el-col>
          <template
            v-if="
              element.openImage != '1' &&
              element.children &&
              element.children.length
            "
          >
            <template v-for="(item, index) in element.children">
              <!-- 处理嵌套问题组 -->
              <el-row
                v-if="item.labelType == 'form' && item.isShowDom"
                :key="'nested' + index"
                class="form_row_style"
              >
                <el-col :span="24">
                  <span class="subTitle_style">{{ item.formName }}</span>
                </el-col>
                <!-- 嵌套问题组的子项 -->
                <template v-if="item.children && item.children.length">
                  <el-col
                    :span="subItem.span || 24"
                    v-for="(subItem, subIndex) in item.children"
                    :key="'subItem' + subIndex"
                  >
                    <el-form-item
                      v-if="subItem.isShowDom"
                      :prop="subItem.propertyCode"
                      :class="
                        subItem.isFlex
                          ? 'form_item_column el-form--label-top'
                          : 'form_item_row el-form--label-left'
                      "
                    >
                      <template
                        slot="label"
                        v-if="
                          !(
                            subItem.assemblyType == 'tool' ||
                            subItem.labelType == 'Caption' ||
                            subItem.propertyName == '' ||
                            subItem.isHideName
                          )
                        "
                      >
                        <span
                          class="subTitle_style"
                          v-if="!subItem.isHideName"
                          >{{ subItem.propertyName }}</span
                        >
                      </template>
                      <view-info
                        :info="subItem"
                        @change="formItemChange"
                        :key="subItem.renderKey + elIndex + subIndex"
                        :previewFormData="previewFormData"
                      ></view-info>
                    </el-form-item>
                  </el-col>
                </template>
              </el-row>

              <!-- 处理普通属性 -->
              <el-col
                v-else-if="item.labelType !== 'form' && item.isShowDom"
                :span="item.span || 24"
                :key="'item' + index"
              >
                <el-form-item
                  :prop="item.propertyCode"
                  :class="
                    item.isFlex
                      ? 'form_item_column el-form--label-top'
                      : 'form_item_row el-form--label-left'
                  "
                >
                  <template
                    slot="label"
                    v-if="
                      !(
                        item.assemblyType == 'tool' ||
                        item.labelType == 'Caption' ||
                        item.propertyName == '' ||
                        item.isHideName
                      )
                    "
                  >
                    <span class="subTitle_style" v-if="!item.isHideName">{{
                      item.propertyName
                    }}</span>
                  </template>
                  <view-info
                    :info="item"
                    @change="formItemChange"
                    :key="item.renderKey + elIndex + index"
                    :previewFormData="previewFormData"
                  ></view-info>
                </el-form-item>
              </el-col>
            </template>
          </template>
          <template v-else-if="element.openImage == '1'">
            <el-col :span="element.span || 24">
              <el-upload
                style="margin-bottom: 10px"
                list-type="picture-card"
                :headers="uploadHeaders"
                :action="uploadFileUrl"
                accept="image/*"
                :auto-upload="element['auto-upload']"
              >
                <i slot="default" class="el-icon-plus"></i>
              </el-upload>
            </el-col>
          </template>
        </template>
      </el-row>
      <el-form-item
        v-if="element.labelType !== 'form' && element.isShowDom"
        :prop="element.propertyCode"
        :class="
          element.isFlex
            ? 'form_item_column el-form--label-top'
            : 'form_item_row el-form--label-left'
        "
      >
        <template
          slot="label"
          v-if="
            !(
              element.assemblyType == 'tool' ||
              element.labelType == 'Caption' ||
              element.propertyName == ' ' ||
              element.isHideName
            )
          "
        >
          <span class="subTitle_style" v-if="!element.isHideName">{{
            element.propertyName
          }}</span>
        </template>
        <p class="subTitle_style" v-if="element.isShowSubTitle">
          {{ element.subTitle }}
        </p>
        <el-popover
          placement="left"
          title="注释"
          trigger="hover"
          v-if="element.isExplanatoryNote"
          effect="light"
          popper-class="custom_tip_popover"
          :visible-arrow="false"
        >
          <div v-html="element.explanatoryNote"></div>
          <el-button
            plain
            type="text"
            slot="reference"
            class="form_explanatory_note"
            icon="el-icon-info"
          ></el-button>
        </el-popover>
        <view-info
          :info="element"
          :previewFormData="previewFormData"
          @change="formItemChange"
          :key="element.renderKey + elIndex"
        ></view-info>
      </el-form-item>
    </el-col>
  </el-row>
</template>

<script>
import viewInfo from "./item";
import { deepClone } from "@/utils";
import { getToken } from "@/utils/auth";
export default {
  name: "form-info",
  props: ["previewFormData", "formModel"],
  components: { viewInfo },
  data() {
    return {
      uploadFileUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 上传的图片服务器地址
      uploadHeaders: {
        Authorization: "Bearer " + getToken(),
      },
      renderKey: "date" + new Date().getTime(),
    };
  },
  watch: {
    formModel: {
      immediate: true,
      deep: true,
      handler(newVal) {
        this.renderKey = "date" + new Date().getTime();
      },
    },
    previewFormData: {
      immediate: true,
      deep: true,
      handler(newVal) {
        if (newVal && newVal.length) {
        }
      },
    },
  },
  computed: {},
  mounted() {},
  destroyed() {},
  methods: {
    formItemChange(event) {
      let value = event.value;
      this.$emit("form-data-change", event);
    },

    addMultipleForm(event) {
      let fields = this.previewFormData;
      debugger;
      console.log(fields);
      let currentIndex = fields.findIndex((f) => f.formCode == event);
      if (currentIndex > -1) {
        let addFormInfo = deepClone(fields.find((f) => f.formCode == event));
        if (addFormInfo.children && addFormInfo.children.length) {
          addFormInfo.children.forEach((item) => {
            item.propertyCode =
              item.propertyCode + Math.random().toString(36).substring(2, 5);
          });
        }
        addFormInfo.openMultiple = "0";
        addFormInfo.isDeleteForm = true;
        addFormInfo.formCode =
          addFormInfo.formCode + Math.random().toString(36).substring(2, 6);
        if (currentIndex > -1) {
          fields.splice(currentIndex + 1, 0, addFormInfo);
        }
        // console.log('addFormInfo', addFormInfo.formCode)
      } else {
        fields.forEach((item, index) => {
          if (
            item.labelType == "form" &&
            item.children &&
            item.children.length
          ) {
            let childIndex = item.children.findIndex(
              (f) => f.formCode == event,
            );
            if (childIndex > -1) {
              item.children = this.addChildrenMultiple(event, item.children);
            }
          }
        });
      }
      this.previewFormData = fields;
    },
    addChildrenMultiple(event, children) {
      let childIndex = children.findIndex((f) => f.formCode == event);
      let addFormInfo = deepClone(children.find((f) => f.formCode == event));
      if (addFormInfo.children && addFormInfo.children.length) {
        addFormInfo.children.forEach((item) => {
          item.propertyCode =
            item.propertyCode + Math.random().toString(36).substring(2, 5);
          item.propertyValue = this.reSetPropertyValue(item);
          item.defaultValue = this.reSetPropertyValue(item);
        });
      }
      addFormInfo.openMultiple = "0";
      addFormInfo.isDeleteForm = true;
      addFormInfo.formCode =
        addFormInfo.formCode + Math.random().toString(36).substring(2, 6);
      addFormInfo.number = Number(fields[childIndex].number) + 1 + "";
      children.splice(childIndex + 1, 0, addFormInfo);
      return children;
    },
    reSetPropertyValue(event) {
      let defaultValue = "";
      if (
        event.labelType == "Checkbox" ||
        event.labelType == "Cascader" ||
        event.labelType == "province"
      ) {
        defaultValue = [];
      } else if (event.labelType == "Radio") {
        defaultValue = "";
      } else if (event.tag == "com-Maternity") {
        // debugger
      } else if (event.tag == "com-TimeNumeral") {
        defaultValue = ["", "", ""];
      } else if (
        event.labelType == "Statistic" ||
        event.labelType == "Rate" ||
        event.labelType == "slider"
      ) {
        defaultValue = 0;
      } else if (event.labelType == "slider") {
        defaultValue = true;
      }
      return defaultValue;
    },
    deleteFormMultiple(event) {
      let fields = this.previewFormData;
      let currentIndex = fields.findIndex((f) => f.formCode == event);
      if (currentIndex > -1) {
        fields.splice(currentIndex, 1);
      } else {
        fields.forEach((item, index) => {
          if (item.children && item.children.length) {
            let childIndex = item.children.findIndex(
              (f) => f.formCode == event,
            );
            if (childIndex > -1) {
              item.children.splice(childIndex, 1);
            }
          }
        });
      }
      this.previewFormData = fields;
    },
  },
};
</script>
<style lang="scss" scoped>
@import "@/utils/generator/com-mixin.scss";
.preview_form {
  width: 100%;
  ::v-deep .el-form-item {
    .el-form-item__label {
      display: flex;
      align-items: center;
      text-align: left;
      margin-top: 5px;

      &::before {
        line-height: 2;
      }
    }
    &.form_item_row {
      .el-form-item__label {
        width: 30%;
      }
      .el-form-item__content {
        width: 100%;
        .el-slider {
          width: 96%;
        }
        .caption_content {
          min-height: 30px;
        }
      }
    }
  }
}
</style>
