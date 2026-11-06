<template>
  <div class="container" v-loading="isSaveLoading">
    <div class="left-board">
      <el-scrollbar class="left-scrollbar">
        <div class="components-list">
          <div class="components-title"><svg-icon icon-class="component" />常用组件</div>
          <draggable
            class="components-draggable"
            :list="inputComponents"
            :group="{ name: 'componentsGroup', pull: 'clone', put: false }"
            :clone="cloneComponent"
            draggable=".components-item"
            :sort="false"
            @end="onEnd"
          >
            <div
              v-for="(element, index) in inputComponents"
              :key="index"
              class="components-item"
              @click="addComponent(element)"
              :class="
                element.labelType == 'form' || element.tag == 'el-upload' ? 'components-base' : ''
              "
            >
              <div class="components-body">
                <svg-icon :icon-class="element.tagIcon" />
                <span>{{ element.propertyName || element.formName }}</span>
              </div>
            </div>
          </draggable>
        </div>
      </el-scrollbar>
    </div>

    <div class="center-board">
      <div class="action-bar">
        <el-button icon="el-icon-video-play" type="text" @click="execRun"> 预览 </el-button>
        <el-button type="text" @click="saveTemplate"> 保存 </el-button>
        <el-button class="delete-btn" icon="el-icon-delete" type="text" @click="empty">
          清空
        </el-button>
      </div>
      <p class="form_tips">注：此处填写表单只为试用效果不会在患者端生效</p>
      <el-scrollbar class="form_center_scrollbar" ref="centerWarningScroll">
        <el-row class="center-board-row" ref="centerBoardScroll">
          <el-form
            :size="formConf.size"
            :label-position="formConf.labelPosition"
            :label-width="formConf.labelWidth + 'px'"
            class="drawing-form"
          >
            <draggable
              class="drawing-board"
              :move="draggableMove"
              :list="drawingList"
              :animation="340"
              group="componentsGroup"
            >
              <draggable-item
                v-for="(element, index) in drawingList"
                :key="element.renderKey"
                :drawing-list="drawingList"
                :element="element"
                :index="index"
                :active-id="activeId"
                :form-conf="formConf"
                @activeItem="activeFormItem"
                @editItem="editCurrentItemOptions"
                @copyItem="drawingItemCopy"
                @deleteItem="drawingItemDelete"
                :form-type="formType"
                @editLogical="editlogincalCode"
                @setInstallmentRules="setInstallmentRulesList"
              />
            </draggable>
            <div v-show="!drawingList.length" class="empty-info">
              从左侧拖入或点选组件进行表单设计
            </div>
          </el-form>
        </el-row>
      </el-scrollbar>
    </div>
    <right-panel
      :active-data="activeData"
      :form-conf="formConf"
      :show-field="!!drawingList.length"
      :formula-current="formulaDialogVisible"
      @tag-formula="tagFormula"
      :form-type="formType"
    />
    <json-drawer
      size="60%"
      :visible.sync="jsonDrawerVisible"
      :json-str="JSON.stringify(formData)"
      @refresh="refreshJson"
    />
    <form-drawer
      size="60%"
      :visible.sync="drawerVisible"
      :form-data="previewFormData"
      :generate-conf="generateConf"
      @copy="execCopy"
    />
    <logical
      :visible.sync="logicalDialogVisible"
      :active-data="activeData"
      :drawing-list="drawingList"
      title="逻辑编辑"
      @logicalchange="drawerLogicalChange"
    ></logical>

    <edit-item-option
      :visible.sync="editDialogVisible"
      :title="activeData.propertyName + '选项修改'"
      :current-item="activeData"
      @setcomponentoptions="setActiveDataOptions"
    >
    </edit-item-option>
    <set-installment-info
      v-if="activeData"
      :visible.sync="setInstallmentVisible"
      title="分期规则设置"
      :current-item="activeData"
      :setInstallmentVisible="setInstallmentVisible"
      :drawing-list="drawingList"
    >
    </set-installment-info>
    <code-type-dialog
      :visible.sync="dialogVisible"
      title="选择生成类型"
      :operation-type="operationType"
      :hide-file-type="hideFileType"
      :show-file-name="showFileName"
      :template-name="templateName"
      @confirm="generate"
    />
    <input id="copyNode" type="hidden" />
    <!-- 公式设置 -->
    <formula-dialog
      :visible.sync="formulaDialogVisible"
      :current-item="activeData"
      title="公式设置"
      :drawing-list="drawingList"
    >
    </formula-dialog>
  </div>
</template>

<script>
import draggable from "vuedraggable";
import beautifier from "js-beautify";
import JsonDrawer from "./JsonDrawer";
import render from "@/utils/generator/render";
import RightPanel from "./RightPanel";
import EditItemOption from "./editItemOption";
import formulaDialog from "./formulaDialog";
import SetInstallmentInfo from "../build/setInstallmentInfo";
import { selectDiseases } from "@/api/info/config.js";
import { listDiseasesGroups } from "@/api/basics/config.js";
import { inputComponents, formConf } from "@/utils/generator/config";
import { beautifierConf, titleCase, deepClone } from "@/utils/index";
import { makeUpHtml, vueTemplate, vueScript, cssStyle } from "@/utils/generator/html";
import { makeUpJs } from "@/utils/generator/js";
import { makeUpCss } from "@/utils/generator/css";
import CodeTypeDialog from "./CodeTypeDialog";
import DraggableItem from "./DraggableItem";
import FormDrawer from "./FormDrawer";
import logical from "./logical.vue";
import { editLibraryProperty, getChildFormAndProperty } from "@/api/tool/form";
let oldActiveId;
let tempActiveData;
import nationData from "@/utils/nation.json";
import cityData from "@/utils/city.json";

export default {
  components: {
    draggable,
    render,
    RightPanel,
    CodeTypeDialog,
    DraggableItem,
    JsonDrawer,
    FormDrawer,
    EditItemOption,
    logical,
    formulaDialog,
    SetInstallmentInfo,
  },
  data() {
    return {
      idGlobal: Math.random().toString(36).substring(2, 5),
      formName: "",
      formConf,
      inputComponents,
      labelWidth: 100,
      drawingList: [],
      activeId: "",
      drawerVisible: false,
      formData: {},
      previewFormData: {},
      dialogVisible: false,
      generateConf: null,
      showFileName: false,
      activeData: {},
      isSaveLoading: false,
      isTreeDataLoading: false,
      operationType: "",
      templateName: "",
      jsonDrawerVisible: false,
      hideFileType: false,
      editDialogVisible: false, //编辑选项的弹窗是否显示
      logicalDialogVisible: false,
      libraryId: "",
      formulaDialogVisible: false, //公式设置弹窗
      setInstallmentVisible: false, // 分期规则弹窗
      formType: "1",
      setTimeoutTimes: -1,
    };
  },
  created() {
    // 防止 firefox 下 拖拽 会新打卡一个选项卡
    document.body.ondrop = (event) => {
      event.preventDefault();
      event.stopPropagation();
    };
    let query = this.$route.query;
    this.formType = query.formType;
    let componentDom = JSON.parse(JSON.stringify(inputComponents));
    if (query.formType == "2") {
      componentDom = componentDom.filter((f) => f.labelType != "Rate" && f.tag != "el-upload");
    } else if (query.formType == "3") {
      componentDom = componentDom.filter((f) => f.tag != "el-upload");
    } else {
      componentDom = componentDom.filter((f) => f.tag != "el-upload");
    }
    this.inputComponents = componentDom;
    this.getDepartmentData();
    this.getDiseasesGroups();

    this.inputComponents.find((f) => f.tagType == "province").options = cityData;
    this.inputComponents.find((f) => f.tagType == "nation").options = nationData;
    this.libraryId = query.libraryId;

    this.getAttributeList(query.libraryId, query.formType);
  },
  watch: {
    // eslint-disable-next-line func-names
    "activeData.propertyName": function (val, oldVal) {
      if (val) {
        if (
          this.activeData.placeholder === undefined ||
          !this.activeData.tag ||
          oldActiveId !== this.activeId
        ) {
          return;
        }
      }
    },
    activeId: {
      handler(val) {
        oldActiveId = val;
      },
      immediate: true,
    },
    drawingList: {
      deep: true,
      handler(newVal) {
        this.setTimeoutTimes++;
        if (this.setTimeoutTimes == 1) {
          setTimeout(() => {
            // this.saveTemplate('realSave')
          }, 300000);
        }
      },
    },
  },
  mounted() {},
  methods: {
    /**
     *  =================  左侧固定数据组件数据处理 start
     **/
    // 科室选择组件
    getDepartmentData() {
      selectDiseases().then((res) => {
        let resData = res.data;
        resData.forEach((item) => {
          item["value"] = item.id;
        });
        this.inputComponents.find((f) => f.tagType == "departments").options = res.data;
      });
    },
    // 病种选择组件
    getDiseasesGroups() {
      let params = {
        diseasesGroupsName: "",
      };
      listDiseasesGroups(params).then((res) => {
        let resData = res.data;
        resData.forEach((item) => {
          item["value"] = item.diseasesGroupsId;
          item["label"] = item.diseasesGroupsName;
        });
        this.inputComponents.find((f) => f.tagType == "diseases").options = res.data;
      });
    },
    /**
     * ============== 左侧固定数据组件数据处理 end
     */
    // ========= 获取可以拖拽的基础表单信息 start =========
    // 编辑时基础表单详情
    getAttributeList(libraryId, formType) {
      let params = {
        libraryId,
        formType,
      };
      this.isSaveLoading = true;
      getChildFormAndProperty(params).then((res) => {
        let fields = [];
        if (res.code == 200) {
          let resData = res.data;
          this.libraryId = resData.libraryId;
          fields =
            resData.libraryProperties && resData.libraryProperties.length
              ? resData.libraryProperties.map((m, index) => {
                  if (m.id) m.id = m.id.toString();
                  const { attribute, libraryProperties, ...mInfo } = m;
                  let attributeJSON = JSON.parse(attribute);
                  if (attributeJSON.tagType == "province") {
                    attributeJSON.options = cityData;
                  }
                  return {
                    ...attributeJSON,
                    ...mInfo,
                    renderKey: new Date().getTime() + "" + index,
                  };
                })
              : [];
          let libraryFormList = this.recoverLibraryForms(resData.libraryForms);
          if (libraryFormList.length) {
            fields.push(...libraryFormList);
          }
          fields.sort((a, b) => {
            return a.sortNum - b.sortNum;
          });
          if (fields.length && fields.findIndex((f) => f.propertyCode == "openImage") > -1) {
            fields.splice(0, 1);
          }
        }
        this.formData["fields"] = fields;
        this.drawingList = fields;
        this.isSaveLoading = false;
      });
    },
    // 处理基础表单
    recoverLibraryForms(libraryForms) {
      let list = [];
      if (libraryForms && libraryForms.length) {
        libraryForms.forEach((item, index) => {
          if (item.id) item.id = item.id.toString();
          const { attribute, libraryProperties, libraryForms, ...itemInfo } = item;
          let attributeJSON = JSON.parse(attribute);
          if (attributeJSON.tagType == "province") {
            attributeJSON.options = cityData;
          }
          let info = {
            ...attributeJSON,
            ...itemInfo,
            children: [],
            renderKey: index + "" + new Date().getTime(),
          };
          if (item.libraryProperties && item.libraryProperties.length) {
            info.children = item.libraryProperties.map((m) => {
              if (m.id) m.id = m.id.toString();
              const { attribute, libraryProperties, ...mInfo } = m;
              return {
                ...JSON.parse(m.attribute),
                ...mInfo,
              };
            });
          }
          let libraryFormList = this.recoverLibraryForms(item.libraryForms);
          if (libraryFormList.length) {
            info.children.push(...libraryFormList);
          }
          if (info.children && info.children.length) {
            info.children.sort((a, b) => {
              return a.sortNum - b.sortNum;
            });
          }
          list.push(info);
        });
      }
      return list;
    },
    // ========= 获取可以拖拽的基础表单信息 end =========
    activeFormItem(element) {
      this.activeData = element;
      this.activeId = element.propertyCode || element.formCode;
    },
    editCurrentItemOptions(element) {
      this.activeData = element;
      this.activeId = element.propertyCode || element.formCode;
      this.editDialogVisible = true;
    },
    setInstallmentRulesList(element) {
      this.setInstallmentVisible = true;
      this.activeData = element;
      this.activeId = element.propertyCode || element.formCode;
    },
    onEnd(obj) {
      // console.log('obj',obj)
      if (
        obj.item.className.indexOf("components-base") != -1 &&
        obj.to.className == "drag-wrapper"
      ) {
        this.$message.warning("问题组内不可以嵌套问题组和上传组件");
        this.drawingList.forEach((item) => {
          if (item.children && item.children.length) {
            let deleteIndex = item.children.findIndex(
              (f) => f.labelType == "form" || f.tag == "el-upload",
            );
            item.children.splice(deleteIndex, 1);
          }
        });
      }

      if (obj.from !== obj.to) {
        this.activeData = tempActiveData;
        this.activeId = this.idGlobal;
      }
    },
    addComponent(item) {
      const clone = this.cloneComponent(item);
      this.drawingList.push(clone);
      this.activeFormItem(clone);
      this.$nextTick(() => {
        this.$refs.centerWarningScroll.wrap.scrollTop = (this.drawingList.length + 1) * 130;
      });
    },
    cloneComponent(origin) {
      const codeStr = Math.random().toString(36).substring(2, 5);
      const clone = JSON.parse(JSON.stringify(origin));
      // clone.formId = Math.random().toString(36).substring(2, 5)
      clone.renderKey = +new Date(); // 改变renderKey后可以实现强制更新组件
      if (!clone.span) clone.span = 24;
      if (!clone.layout) clone.layout = "colFormItem";
      clone.tagType = "hideForm";
      if (clone.layout === "colFormItem") {
        clone.vModel = `field${origin.tagIcon.replace("-", "")}${codeStr}`;
        clone.propertyCode = "field" + clone.labelType + codeStr;
        // clone.placeholder !== undefined && (clone.placeholder += clone.propertyName)
        tempActiveData = clone;
      } else if (clone.layout === "rowFormItem") {
        delete clone.propertyName;
        clone.formCode = `row${codeStr}`;
        clone.formName = `问题组`;
        clone.gutter = this.formConf.gutter;
        clone.formType = this.formType;
        tempActiveData = clone;
      }
      return tempActiveData;
    },
    AssembleFormData() {
      this.formData = {
        fields: JSON.parse(JSON.stringify(this.drawingList)),
        ...this.formConf,
      };
    },
    generate(data) {
      const func = this[`exec${titleCase(this.operationType)}`];
      this.generateConf = data;
      func && func(data);
    },
    execRun(data) {
      let formData = {
        fileName: undefined,
        type: "file",
      };
      this.generate(formData);
      this.AssembleFormData();
      this.drawerVisible = true;
      let previewData = this.formData;
      let filedsData = previewData.fields.map((m) => {
        return {
          ...m,
          children: m.children,
        };
      });
      previewData.fields = filedsData;
      this.previewFormData = previewData;
    },
    // 保存成模板
    saveTemplate(event) {
      let query = this.$route.query;
      this.editLibraryForm(query.formType, event);
    },
    // 处理保存数据格式
    recoverFormPrograms(data, parentId, formType) {
      let libraryFormsList = [];
      let libraryPropertiesList = [];
      data.forEach((item, index) => {
        item.sortNum = index;
        const { children, attribute, libraryForms, libraryProperties, ...otherItem } = item;
        if (Array.isArray(otherItem.defaultValue)) {
          otherItem.defaultValue = [];
        } else {
          otherItem.defaultValue = null;
        }
        const { options, ...otherAttribute } = otherItem;

        let info = {
          propertyType: item.labelType,
          dictType: "",
          attribute:
            item.tagType == "province" ? JSON.stringify(otherAttribute) : JSON.stringify(otherItem),
          openImage: item.openImage,
          sortNum: index,
          formType,
        };
        if (item.labelType == "form") {
          info["parentId"] = parentId;
          info["formCode"] = item.formCode;
          info["formName"] = item.formName;
          info["libraryId"] = item.libraryId || parentId;
          info["isDoctorShow"] = item.isDoctorShow == "undefined" ? true : item.isDoctorShow;
          info["isPatientShow"] = item.isPatientShow == "undefined" ? true : item.isPatientShow;
          if (item.children && item.children.length) {
            let itemInfo = this.recoverFormPrograms(item.children, item.libraryId, formType);
            info = {
              ...itemInfo,
              ...info,
            };
          }
          libraryFormsList.push(info);
        } else {
          info["propertyCode"] = item.propertyCode;
          info["propertyName"] = item.propertyName;
          info["libraryId"] = item.libraryId || parentId;
          info["id"] = item.id || "";
          info["isDoctorShow"] = item.isDoctorShow == "undefined" ? true : item.isDoctorShow;
          info["isPatientShow"] = item.isPatientShow == "undefined" ? true : item.isPatientShow;
          if (item.options && item.options.length && this.$route.query.formType == "3") {
            info["itemList"] = item.options.map((m) => {
              return {
                ...m,
                itemScores: m.itemScores,
              };
            });
          }
          libraryPropertiesList.push(info);
        }
      });
      let returnData = {
        libraryProperties: libraryPropertiesList,
        libraryForms: libraryFormsList,
        libraryId: this.libraryId || "",
      };
      return returnData;
    },
    convertScoresVal(properties) {
      const propertyList = properties.map((item) => {
        const { attribute, ...otherItem } = item;
        let returnInfo = {
          ...otherItem,
        };
        if (attribute) {
          returnInfo = {
            ...JSON.parse(attribute),
            ...returnInfo,
          };
        }
        return returnInfo;
      });
      let scoresVal = 0;
      let scoresData = propertyList.filter(
        (f) => f.tag == "el-radio-group" || f.tag == "el-checkbox-group" || f.tag == "el-Select",
      );
      scoresData.forEach((item) => {
        if (item.itemList && item.itemList.length) {
          if (item.tag == "el-checkbox-group") {
            item.itemList.forEach((f) => {
              if (f.itemScores) {
                scoresVal += f.itemScores;
              }
            });
          } else {
            let itemScores = item.itemList.map((m) => m.itemScores);
            itemScores.sort((a, b) => {
              return b - a;
            });
            if (itemScores[0]) scoresVal += itemScores[0];
          }
        }
      });
      return scoresVal;
    },
    // 修改保存基础表单属性
    editLibraryForm(formType, event) {
      this.AssembleFormData();
      let templateData = this.recoverFormPrograms(this.formData.fields, this.libraryId, formType);
      console.log(templateData);
      templateData["formType"] = formType;
      // templateData['libraryProperties'].splice(0,1)
      if (
        formType == "2" &&
        ((templateData["libraryProperties"] &&
          templateData["libraryProperties"].length &&
          templateData["libraryProperties"].findIndex((f) => f.propertyCode == "openImage") ==
            -1) ||
          (templateData["libraryForms"] && templateData["libraryForms"].length))
      ) {
        let photographImg = {
          ...inputComponents.find((f) => f.labelType == "Upload"),
        };
        photographImg["tagType"] = "openImage";
        photographImg["fileSize"] = 5;
        photographImg["required"] = true;
        templateData["libraryProperties"].unshift({
          attribute: JSON.stringify(photographImg),
          formType,
          openImage: "1",
          libraryId: templateData.libraryId,
          propertyCode: "openImage",
          propertyName: this.$route.query.propertyName || "拍照项",
          propertyType: photographImg.labelType,
        });
      }
      if (formType == "3") {
        templateData["scoresTotalVal"] = this.convertScoresVal(templateData.libraryProperties);
      }
      this.isSaveLoading = true;
      editLibraryProperty(templateData)
        .then((res) => {
          this.isSaveLoading = false;
          this.$message.success("保存成功");
          if (event != "realSave") {
            this.$tab.closePage(this.$route);
            let query = this.$route.query;
            this.$router.push({
              path: `${decodeURIComponent(query.oldPath)}`,
            });
          } else {
            this.setTimeoutTimes = 0;
          }
        })
        .catch((eror) => {
          this.isSaveLoading = false;
        });
    },
    execCopy(data) {
      let formData = {
        fileName: undefined,
        type: "file",
      };
      this.generate(formData);
    },
    empty() {
      this.$confirm("确定要清空所有组件吗？", "提示", {
        type: "warning",
      }).then(() => {
        this.drawingList = [];
      });
    },
    drawingItemCopy(item, parent) {
      let clone = JSON.parse(JSON.stringify(item));
      clone = this.createIdAndKey(clone);
      parent.push(clone);
      this.activeFormItem(clone);
      let scrolltop = this.$refs.centerBoardScroll.$el.clientHeight;
      this.$nextTick(() => {
        this.$refs.centerWarningScroll.wrap.scrollTop = scrolltop;
      });
    },
    createIdAndKey(event) {
      const { id, ...item } = event;
      const codeStr = Math.random().toString(36).substring(2, 5);
      item.renderKey = +new Date();
      let itemCode = "";
      if (item.layout === "colFormItem") {
        item.vModel = `field${item.labelType}${codeStr}`;
        itemCode = item.propertyCode;
        if (itemCode) {
          item.propertyCode = `${itemCode.slice(0, 15)}${codeStr}`;
        } else {
          item.propertyCode = `field${item.labelType}${codeStr}`;
        }
      } else if (item.layout === "rowFormItem") {
        itemCode = item.formCode;
        if (itemCode) {
          item.formCode = `${itemCode.slice(0, 15)}${codeStr}`;
        } else {
          item.formCode = `row${codeStr}`;
        }
        item.formName = item.formName ? item.formName : `问题组`;
      }
      if (Array.isArray(item.children)) {
        item.children = item.children.map((childItem) => this.createIdAndKey(childItem));
      }
      return item;
    },
    drawingItemDelete(index, parent) {
      parent.splice(index, 1);
      this.$nextTick(() => {
        const len = this.drawingList.length;
        if (len) {
          let pagingList = this.drawingList.filter((f) => f.tag == "paging");
          this.drawingList.forEach((item, itemIndex) => {
            if (item.tag == "paging") {
              item.total--;
              let pagingIndex = pagingList.findIndex((f) => f.propertyCode == item.propertyCode);
              item.page = pagingIndex + 1;
            }
            let relationInfo = item.relationInfo;
            if (
              relationInfo &&
              relationInfo.relationQuestion &&
              relationInfo.relationQuestion.length
            ) {
              relationInfo.relationQuestion.forEach((relationItem, relationIndex) => {
                let hasIndex = this.drawingList.findIndex(
                  (f) => f.propertyCode == relationItem.propertyCode,
                );
                if (hasIndex == -1) {
                  relationInfo.relationQuestion.splice(relationIndex, 1);
                }
              });
            }
          });
          this.activeFormItem(this.drawingList[len - 1]);
        }
      });
    },
    generateCode() {
      const { type } = this.generateConf;
      this.AssembleFormData();
      const script = vueScript(makeUpJs(this.formData, type));
      const html = vueTemplate(makeUpHtml(this.formData, type));
      const css = cssStyle(makeUpCss(this.formData));
      return beautifier.html(html + script + css, beautifierConf.html);
    },
    updateDrawingList(newTag, list) {
      const index = list.findIndex((item) => item.propertyCode === this.activeId);
      if (index > -1) {
        list.splice(index, 1, newTag);
      } else {
        list.forEach((item) => {
          if (Array.isArray(item.children)) this.updateDrawingList(newTag, item.children);
        });
      }
    },
    showJson() {
      this.AssembleFormData();
      this.jsonDrawerVisible = true;
    },
    refreshJson(data) {
      this.drawingList = deepClone(data.fields);
      delete data.fields;
      this.formConf = data;
    },
    // 当前组件的options修改
    setActiveDataOptions(element) {
      this.activeData = element;
      this.activeId = element.propertyCode || element.formCode;
    },
    editlogincalCode(element) {
      this.activeData = element;
      this.activeId = element.propertyCode || element.formCode;
      this.logicalDialogVisible = true;
    },
    draggableMove(event) {
      if (
        event.draggedContext.element &&
        (event.draggedContext.element.labelType == "form" ||
          event.draggedContext.element.tag == "el-upload") &&
        event.to.className.indexOf("drag-wrapper") != -1
      ) {
        return false;
      }
    },
    tagFormula(flag) {
      if (flag === true) {
        this.formulaDialogVisible = true;
      } else {
        this.formulaDialogVisible = false;
      }
    },
    drawerLogicalChange(event) {
      this.activeData["relationInfo"] = event;
    },
  },
};
</script>

<style lang="scss" scoped>
// home
.action-bar {
  position: relative;
  text-align: right;
  padding: 5px 15px;
  box-sizing: border-box;
  border: 1px solid #f1e8e8;
  border-top: none;
  border-left: none;

  .delete-btn {
    color: #f56c6c;
  }

  & .el-button + .el-button {
    margin-left: 0;
  }

  & i {
    font-size: 20px;
    vertical-align: middle;
    position: relative;
    top: -1px;
  }

  .el-button {
    padding: 10px;
  }
}

.form_tips {
  color: #1890ff;
  padding: 6px 8px;
  text-align: center;
}

.el-upload__tip {
  line-height: 1.2;
}

$selectedColor: #f6f7ff;
$lighterBlue: #409eff;

.container {
  background: #fff;
  display: flex;
  flex-wrap: nowrap;
  width: 100%;
  height: 100%;
  overflow: auto;
}

.components-list {
  padding: 8px;
  box-sizing: border-box;
  height: 100%;

  .components-item {
    display: inline-block;
    width: 48%;
    margin: 1%;
    transition: transform 0ms !important;
  }
}

.form_center_scrollbar {
  min-width: 300px;
  overflow-x: hidden;
  height: calc(100vh - 160px);
  border-left: 1px solid #f1e8e8;
  border-right: 1px solid #f1e8e8;

  .el-scrollbar__view {
    height: 100%;
    color: #000;
  }
}

.center-board {
  flex: 1;
  box-sizing: border-box;
}

.center-board-row {
  padding: 0 15px 30px;
  box-sizing: border-box;
  min-height: calc(100% - 42px);

  .empty-info {
    position: absolute;
    top: 46%;
    left: 0;
    right: 0;
    text-align: center;
    font-size: 18px;
    color: #ccb1ea;
    letter-spacing: 4px;
  }

  ::v-deep .drawing-board {
    // height: 100%;
    min-height: calc(100vh - 260px);
    padding-bottom: 60px;
    position: relative;
    display: flex;
    flex-wrap: wrap;
    align-content: baseline;

    .drawing-item {
      position: relative;
      cursor: move;
      margin-bottom: 8px;

      &.unfocus-bordered:not(.activeFromItem) > div:first-child {
        border: 1px dashed #ccc;
      }

      .el-form-item {
        padding: 0 10px;
        width: 98%;
        margin: 0;

        .el-form-item__label {
          display: flex;

          .el-input__inner:hover {
            border: 1px #c0c4cc solid;
          }
        }

        .el-button--text {
          display: block;
        }
      }
    }

    .components-body {
      padding: 0;
      margin: 0;
      font-size: 0;
    }

    .sortable-ghost {
      position: relative;
      display: block;
      overflow: hidden;

      &::before {
        content: " ";
        position: absolute;
        left: 0;
        right: 0;
        top: 0;
        height: 3px;
        background: rgb(89, 89, 223);
        z-index: 2;
      }
    }

    .components-item.sortable-ghost {
      width: 100%;
      height: 60px;
      background-color: $selectedColor;
    }

    .active-from-item {
      background: $selectedColor;
      border-radius: 6px;

      .drawing-item-copy,
      .drawing-item-delete,
      .drawing-item-logic {
        display: initial;
      }

      .component-name {
        color: $lighterBlue;
      }
    }

    .drawing-item,
    .drawing-row-item {
      &:hover {
        background: $selectedColor;
        border-radius: 6px;

        .drawing-item-copy,
        .drawing-item-delete,
        .drawing-item-logic {
          display: initial;
        }
      }

      .row_form_itemBtns {
        display: flex;
        position: absolute;
        right: 0;
        top: 5px;
        z-index: 100;
        cursor: pointer;
      }

      .drawing-item-copy,
      .drawing-item-delete,
      .drawing-item-logic {
        display: none;
        margin-right: 8px;
        width: 22px;
        height: 22px;
        line-height: 22px;
        text-align: center;
        border-radius: 50%;
        border: 1px solid;
        cursor: pointer;
        z-index: 1;
      }

      .drawing-item-copy {
        border-color: $lighterBlue;
        color: $lighterBlue;
        background: #fff;

        &:hover {
          background: $lighterBlue;
          color: #fff;
        }
      }

      .drawing-item-logic {
        border-color: $lighterBlue;
        color: $lighterBlue;
        background: #fff;

        &:hover {
          background: $lighterBlue;
          color: #fff;
        }
      }

      .drawing-item-delete {
        border-color: #f56c6c;
        color: #f56c6c;
        background: #fff;

        &:hover {
          background: #f56c6c;
          color: #fff;
        }
      }
    }
  }
}

::v-deep .drawing-row-item {
  position: relative;
  cursor: move;
  box-sizing: border-box;
  border: 1px dashed #ccc;
  border-radius: 3px;
  padding: 10px 2px;
  margin-bottom: 15px;

  .drawing-row-item {
    margin-bottom: 2px;
  }

  .el-form-item {
    margin-bottom: 0;
    padding: 0 10px;
    width: 98%;
    margin: 0;
  }

  .drag-wrapper {
    min-height: 130px;
    width: 100%;
    overflow: hidden;
    display: flex;
    flex-wrap: wrap;
  }

  &.active-from-item {
    border: 1px dashed $lighterBlue;
  }

  .component-name {
    // position: absolute;
    // top: 5px;
    // left: 0;
    font-size: 12px;
    color: #bbb;
    display: inline-block;
    padding: 0 6px;
  }
}

.left-board,
.center-board,
.right-board {
  height: calc(100vh - 84px);
}

.left-scrollbar {
  height: calc(100vh - 126px);
  overflow: hidden;
}

.el-divider {
  background: transparent;
  border-width: 1px;
  border-color: #dcdfe6;
  border-top: 1px solid #dcdfe6;

  &.solid {
    border-style: solid;
  }

  &.dotted {
    border-style: dotted;
  }

  &.dashed {
    border-style: dashed;
  }
}

.drawing-item {
  .solid {
    .el-divider {
      border-style: solid;
      border-width: 1px;
      border-color: #dcdfe6;
    }
  }

  .dotted {
    .el-divider {
      border-style: dotted;
      border-width: 1px;
      border-color: #dcdfe6;
    }
  }

  .dashed {
    .el-divider {
      border-style: dashed;
      border-width: 1px;
      border-color: #dcdfe6;
    }
  }
}

.custom_form_upload {
  margin-left: 10px;
}

.left-board {
  width: 360px;
  flex-shrink: 0;

  .el-tabs__item {
    padding: 0 !important;
  }

  .components-draggable {
    padding-bottom: 20px;
  }

  .components-title {
    font-size: 14px;
    color: #222;
    margin: 6px 2px;

    .svg-icon {
      color: #666;
      font-size: 18px;
    }
  }

  .components-body,
  .formList-body {
    padding: 8px 10px;
    background: $selectedColor;
    font-size: 12px;
    cursor: move;
    border: 1px dashed $selectedColor;
    border-radius: 3px;

    .svg-icon {
      color: #777;
      font-size: 15px;
      margin-right: 3px;
    }

    &:hover {
      border: 1px dashed #787be8;
      color: #787be8;

      .svg-icon {
        color: #787be8;
      }
    }
  }

  .formList_draggable {
    display: flex;
    height: 100%;

    .formList_content {
      width: 50%;
      border-right: 0.5px solid #ededed;
    }
  }

  .formList-body {
    background: transparent;
    line-height: 20px;
    cursor: auto;
  }

  .attribute-body {
    cursor: move;
    width: 80%;
  }

  .formList_content,
  .attribute_content {
    flex: 1;

    .attribute-item {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .el-checkbox {
        margin-right: 10px;
      }

      .attribute-body {
        padding: 8px 0px 8px 5px;
      }
    }
  }

  .ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 100%;
  }

  .active_formItem {
    .formList-body {
      background: $selectedColor;
      cursor: move;
    }
  }

  .noAttribute_cont {
    font-size: 12px;
    line-height: 50px;
    text-align: center;
    color: #999;
  }
}

.el-date-editor.el-input {
  max-width: 180px;

  .el-input__inner {
    padding-right: 10px;
  }
}
</style>
