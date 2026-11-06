<template>
  <div class="crf_container" v-loading="isSaveLoading">
    <div class="left-board">
      <el-tabs v-model="currentTab" class="left_tabs" stretch @tab-click="tabClick">
        <el-tab-pane label="标准量表" name="liang" />
        <el-tab-pane label="自定义量表项" name="custom" />
      </el-tabs>
      <div class="left-scrollbar" v-show="currentTab == 'liang'" v-loading="isTabListLoading">
        <div class="entry_list" v-if="liangList.length">
          <el-scrollbar class="entry_first_list">
            <draggable
              class="components-draggable"
              :list="liangList"
              :group="{ name: 'componentsGroup', pull: 'clone', put: false }"
              :clone="cloneAttributeComponent"
              draggable=".components-item"
              :sort="false"
              @end="onEnd"
              :move="draggeablePhotoMove"
            >
              <div
                v-for="(element, index) in liangList"
                :key="index"
                :class="activeLiangFromId == element.libraryId ? 'active_formItem' : ''"
                class="entry_first_list_item components-item"
                @click="entryFirstItemClick(element)"
              >
                <el-tooltip
                  effect="dark"
                  :content="element.formName"
                  placement="top-start"
                  :disabled="element.formName && element.formName.length < 16"
                >
                  <p class="ellipsis">{{ element.formName }}</p>
                </el-tooltip>
              </div>
            </draggable>
          </el-scrollbar>
          <el-scrollbar class="entry_second_list">
            <draggable
              class="components-draggable"
              v-if="liangSecondList && liangSecondList.length"
              :list="liangSecondList"
              :group="{ name: 'componentsGroup', pull: 'clone', put: false }"
              :clone="cloneAttributeComponent"
              draggable=".components-item"
              :sort="false"
              @end="onEnd"
            >
              <div
                v-for="(element, index) in liangSecondList"
                :key="index"
                @click="addAttributeComponent(element)"
                class="components-item attribute_item"
              >
                <div class="properties_item">
                  <el-tooltip
                    effect="dark"
                    :content="element.propertyName || element.formName"
                    placement="top-start"
                    :disabled="
                      (element.propertyName && element.propertyName.length < 16) ||
                      (element.formName && element.formName.length < 16)
                    "
                  >
                    <p class="ellipsis properties_name">
                      {{ element.propertyName || element.formName }}
                    </p>
                  </el-tooltip>
                  <span class="properties_tip">{{ element.formName ? "[多题]" : "" }}</span>
                </div>
              </div>
            </draggable>
            <el-empty v-else description="暂无属性内容"></el-empty>
          </el-scrollbar>
        </div>
        <el-empty v-else description="暂无标准量表"></el-empty>
      </div>
      <el-scrollbar class="left-scrollbar" v-show="currentTab == 'custom'">
        <draggable
          class="components-draggable custom_list"
          :list="customList"
          :group="{ name: 'componentsGroup', pull: 'clone', put: false }"
          :clone="cloneComponent"
          draggable=".components-item"
          :sort="false"
          @end="onEnd"
        >
          <div
            v-for="(element, index) in customList"
            :key="index"
            class="components-item"
            @click="addComponent(element)"
            :class="
              element.labelType == 'form' || element.tag == 'el-upload' ? 'components-base' : ''
            "
          >
            <div class="custom_item">
              <svg-icon :icon-class="element.tagIcon" />
              <span>{{ element.propertyName || element.formName }}</span>
            </div>
          </div>
        </draggable>
      </el-scrollbar>
    </div>

    <div class="center-board">
      <div class="action-bar">
        <el-button icon="el-icon-video-play" type="text" @click="previewForm"> 预览 </el-button>
        <el-button type="text" @click="saveCRFTemplate"> 保存 </el-button>
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
              @update="datadragEnd"
              :list="drawingList"
              :animation="340"
              group="componentsGroup"
              :move="draggableMove"
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
      :form-type="formType"
    />
    <edit-item-option
      v-if="activeData"
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
    <logical
      :visible.sync="logicalDialogVisible"
      :active-data="activeData"
      :drawing-list="drawingList"
      title="逻辑编辑"
      @logicalchange="drawerLogicalChange"
    ></logical>
    <form-drawer
      size="60%"
      :visible.sync="drawerVisible"
      :form-data="previewFormData"
      :generate-conf="generateConf"
      @copy="execCopy"
    />
  </div>
</template>

<script>
// 随访、CRF、患教库添加条目页面
import draggable from "vuedraggable";
import RightPanel from "../build/RightPanel";
import EditItemOption from "../build/editItemOption.vue";
import SetInstallmentInfo from "../build/setInstallmentInfo";
import { getChildFormAndProperty } from "@/api/tool/form";
import {
  getQuestionLeftPanel,
  getQuestionFormDetail,
  saveQuestionForm,
} from "@/api/patient/config";
import { selectDiseases } from "@/api/info/config.js";
import { listDiseasesGroups } from "@/api/basics/config.js";

import { inputComponents, formConf } from "@/utils/generator/config";
import DraggableItem from "../build/DraggableItem";
import logical from "../build/logical.vue";
import FormDrawer from "../build/FormDrawer";
import nationData from "@/utils/nation.json";
import cityData from "@/utils/city.json";

let tempActiveData;
export default {
  name: "crf-template",
  components: {
    RightPanel,
    draggable,
    DraggableItem,
    EditItemOption,
    logical,
    FormDrawer,
    SetInstallmentInfo,
  },
  data() {
    return {
      formConf,
      currentTab: "liang",
      tabIndex: 0,
      isSaveLoading: false,
      liangList: [],
      liangSecondList: [],
      isTabListLoading: false,
      activeLiangFromId: "",
      activeChildrenId: "",
      customList: [],
      generateConf: null,
      formType: "3",
      // 中间部分内容
      drawingList: [],
      photoDrawingList: [],
      activeId: "",
      activeData: {},
      editDialogVisible: false, //编辑选项的弹窗是否显示
      formData: {},
      previewFormData: {},
      articleId: "",
      templateFormInfo: {},
      logicalDialogVisible: false,
      drawerVisible: false,
      pageType: "",
      setInstallmentVisible: false, // 分期规则弹窗
      // 右侧部分
      setTimeoutTimes: -1,
    };
  },
  created() {
    // 防止 firefox 下 拖拽 会新打卡一个选项卡
    document.body.ondrop = (event) => {
      event.preventDefault();
      event.stopPropagation();
    };
    this.customList = inputComponents.filter(
      (f) => f.labelType != "pager" && f.labelType != "upload",
    );
    this.getDepartmentData();
    this.getDiseasesGroups();

    this.customList.find((f) => f.tagType == "province").options = cityData;
    this.customList.find((f) => f.tagType == "nation").options = nationData;
  },
  watch: {
    drawingList: {
      deep: true,
      handler(newVal) {
        this.setTimeoutTimes++;
        if (this.setTimeoutTimes == 1) {
          setTimeout(() => {
            // this.saveCRFTemplate('realSave')
          }, 300000);
        }
      },
    },
  },
  mounted() {
    let params = this.$route.params;
    let query = this.$route.query;
    this.pageType = query.pageType;
    this.getTabList();
    this.articleId = params.articleId;
    this.getTemplateInfo();
  },
  methods: {
    // 左侧组件数据处理
    // 科室选择组件
    getDepartmentData() {
      selectDiseases().then((res) => {
        let resData = res.data;
        resData.forEach((item) => {
          item["value"] = item.id;
        });
        this.customList.find((f) => f.tagType == "departments").options = res.data;
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
        this.customList.find((f) => f.tagType == "diseases").options = res.data;
      });
    },
    /**
     * ============== 左侧固定数据组件数据处理 end
     */
    /**
     * 左侧tab切换
     */
    tabClick(event) {
      if (event.index == this.tabIndex) return;
      this.tabIndex = event.index;
      this.activeChildrenId = "";
      if (event.index == "0") {
        this.getTabList();
      }
    },
    /**
     * 子表单切换
     */
    childrenItemClick(event) {
      this.liangSecondList = event.libraryProperties;
      this.activeChildrenId = event.libraryId;
    },
    /**
     * 获取标准录入项  获取标准拍照项  获取列表接口
     */
    getTabList() {
      this.isTabListLoading = true;
      let params = {
        formType: this.formType,
        parentId: 0,
      };
      getQuestionLeftPanel(params).then((res) => {
        let resData = res.rows;
        if (res.code == 200) {
          resData.sort((a, b) => {
            return a.sortNum - b.sortNum;
          });
          this.liangList = resData;
          if (resData.length) {
            this.activeLiangFromId = resData[0].libraryId;
            this.getChildFormAndPropertyList();
          }
        }
        this.isTabListLoading = false;
      });
    },
    // 一级标准录入项tab切换
    entryFirstItemClick(event) {
      if (event) {
        this.activeLiangFromId = event.libraryId;
        this.getChildFormAndPropertyList();
      }
    },
    // 获取子表单和属性列表
    getChildFormAndPropertyList() {
      let params = {
        formType: this.formType,
        libraryId: this.activeLiangFromId,
      };
      this.isTabListLoading = true;
      getChildFormAndProperty(params).then((res) => {
        let resData = res.data;
        if (resData) {
          let properties = resData.libraryProperties;
          if (properties.length) {
            properties.sort((a, b) => {
              return a.sortNum - b.sortNum;
            });
          }
          this.liangSecondList = properties;
          this.liangList.forEach((item) => {
            if (item.libraryId == resData.libraryId) {
              item["libraryProperties"] = properties;
            }
          });
          this.$forceUpdate();
        }
        this.isTabListLoading = false;
      });
    },

    /**
     * ======================= 处理拖拽元素 ============================
     */
    moveAttributeComponentItem(event) {
      const element = event.draggedContext.element;
      if (this.checkHasItem(this.drawingList, element)) {
        return false;
      } else {
        return true;
      }
    },
    addAttributeComponent(eventItem) {
      let { attribute, libraryProperties, libraryForms, ...eventOtherInfo } = eventItem;
      let eventChildren = [];
      if (libraryProperties && libraryProperties.length) {
        libraryProperties.forEach((item, index) => {
          eventChildren.push({
            ...item,
            ...this.recoverAttribute(item.attribute),
            id: item.id && item.id.toString(),
            renderKey: new Date() + "" + index,
            sourceLibraryId: eventItem.libraryId,
          });
        });
      }
      let event = {
        ...JSON.parse(eventItem.attribute),
        ...eventOtherInfo,
        children: eventChildren,
      };
      this.addComponent(event);
    },
    // 检查是否已经在表单中
    checkHasItem(list, info) {
      if (list.find((f) => f.propertyCode == info.propertyCode)) {
        return true;
      } else {
        return false;
      }
    },
    addComponent(item) {
      const clone = this.cloneComponent(item);
      clone.id = item.id && item.id.toString();
      this.drawingList.push(clone);
      this.activeFormItem(clone);
      this.$nextTick(() => {
        this.$refs.centerWarningScroll.wrap.scrollTop = (this.drawingList.length + 1) * 130;
      });
    },
    // 拖拽结束
    onEnd(obj) {
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
      // 拖拽结束拆分
      let itemList = JSON.parse(JSON.stringify(this.drawingList));
      itemList.forEach((item, index) => {
        if (item.formType == "3" && item.labelType == "form") {
          if (item.children.length && item.formContentType == "formDemo") {
            let children = item.children;
            if (children.length) {
              children.forEach((childItem, childIndex) => {
                childItem.sortNum = index + childIndex;
              });
            }
            itemList.splice(index, 1, ...children);
          }
        }
      });
      this.drawingList = itemList;
      if (obj.from !== obj.to) {
        this.activeData = tempActiveData;
        this.activeId = this.idGlobal;
      }
    },
    cloneAttributeComponent(event) {
      let { attribute, libraryProperties, libraryForms } = event;
      let eventChildren = [];
      if (libraryProperties && libraryProperties.length) {
        libraryProperties.forEach((item, index) => {
          eventChildren.push({
            ...item,
            ...this.recoverAttribute(item.attribute),
            id: item.id && item.id.toString(),
            renderKey: new Date() + "" + index,
            sourceLibraryId: event.libraryId,
          });
        });
      }
      let libraryFormList = this.recoverLibraryForms(libraryForms);
      if (libraryFormList.length) {
        eventChildren.push(...libraryFormList);
      }
      if (eventChildren.length) {
        eventChildren.sort((a, b) => {
          return a.sortNum - b.sortNum;
        });
      }
      let cloneItemInfo = {
        ...JSON.parse(attribute),
        renderKey: new Date() + "",
        formContentType:
          event.libraryProperties && event.libraryProperties.length ? "formDemo" : "",
      };
      let returnCloneItem = undefined;
      if (this.currentTab == "liang" && cloneItemInfo.labelType == "form") {
        cloneItemInfo["children"] = eventChildren && eventChildren.length ? eventChildren : [];
        returnCloneItem = cloneItemInfo;
      } else {
        if (cloneItemInfo.labelType == "form") {
          cloneItemInfo["children"] = eventChildren && eventChildren.length ? eventChildren : [];
        }
        returnCloneItem = cloneItemInfo;
      }
      returnCloneItem = this.cloneComponent(returnCloneItem);
      this.activeFormItem(returnCloneItem);
      return returnCloneItem;
    },
    // 处理基础表单
    recoverLibraryForms(libraryForms) {
      let list = [];
      if (libraryForms && libraryForms.length) {
        libraryForms.forEach((item, index) => {
          if (item.id) item.id = item.id.toString();
          const { attribute, libraryProperties, ...itemInfo } = item;
          let info = {
            ...itemInfo,
            children: [],
            renderKey: index + "" + new Date().getTime(),
          };
          if (item.libraryProperties && item.libraryProperties.length) {
            info.children = item.libraryProperties.map((m) => {
              if (m.id) m.id = m.id.toString();
              const { attribute, libraryProperties, ...mInfo } = m;
              return {
                ...JSON.parse(attribute),
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
    recoverAttribute(event) {
      return JSON.parse(event);
    },
    cloneComponent(origin) {
      const clone = JSON.parse(JSON.stringify(origin));
      const codeStr = Math.random().toString(36).substring(2, 6);
      // ********** formid = crf的id+formId  ***************
      // clone.formId = Math.random().toString(36).substring(2, 6)
      clone.span = formConf.span;
      clone.renderKey = +new Date(); // 改变renderKey后可以实现强制更新组件
      if (!clone.layout) clone.layout = "colFormItem";
      if (!clone.span) clone.span = 24;
      if (clone.tag == "paging") {
        let pagingList = this.drawingList.filter((f) => f.tag == "paging");
        let totalNum = pagingList.length + 1;
        pagingList.forEach((item) => {
          if (item.tag == "paging") {
            item.total = totalNum;
          }
        });
        clone.page = totalNum;
        clone.total = totalNum;
      }
      if (clone.options && clone.options.length && clone.tag != "el-cascader" && !clone.tagType) {
        clone.options.forEach((item) => {
          item.subjectScores = 0;
        });
      }
      let tagData = ["Radio", "Select", "Checkbox"];
      if (tagData.indexOf(clone.labelType) == -1) {
        clone.subjectScores = 0;
      }
      if (clone.layout === "colFormItem") {
        clone.vModel = `field${origin.tagIcon.replace("-", "")}${codeStr}`;
        if (clone.propertyCode) {
          clone.propertyCode = `${clone.propertyCode}`;
        } else {
          clone.propertyCode = "field" + clone.labelType + codeStr;
        }
        // clone.propertyCode = 'field' + clone.labelType + codeStr
        clone.propertyType = clone.labelType;
        // clone.placeholder !== undefined && (clone.placeholder += clone.propertyName)
        tempActiveData = clone;
      } else if (clone.layout === "rowFormItem") {
        delete clone.propertyName;
        if (clone.formCode) {
          clone.formCode = `${clone.formCode}`;
        } else {
          clone.formCode = clone.formCode ? clone.formCode + `${codeStr}` : `row${codeStr}`;
        }
        // clone.formCode = clone.formCode ? clone.formCode + `${codeStr}` : `row${codeStr}`
        clone.formName = clone.formName ? clone.formName : `问题组`;
        clone.gutter = this.formConf.gutter;
        tempActiveData = clone;
      }
      return tempActiveData;
    },
    // 复制组件
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
      const codeStr = Math.random().toString(36).substring(2, 6);
      item.renderKey = +new Date();
      if (item.options && item.options.length && item.tag != "el-cascader" && !item.tagType) {
        item.options.forEach((eachItem) => {
          eachItem.subjectScores = 0;
        });
      }
      let tagData = ["Radio", "Select", "Checkbox"];
      if (tagData.indexOf(item.labelType) == -1) {
        item.subjectScores = 0;
      }
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
    // 删除组件
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
    // 编辑属性内容
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
    activeFormItem(element) {
      this.activeData = element;
      this.activeId = element.propertyCode || element.formCode;
    },
    datadragEnd(event) {},
    empty() {
      this.$confirm("确定要清空所有组件吗？", "提示", {
        type: "warning",
      }).then(() => {
        this.drawingList = [];
      });
    },
    // 中间部分的内容
    draggableMove(event) {
      const draggedContext = event.draggedContext.element;
      if (
        draggedContext &&
        (draggedContext.labelType == "form" || draggedContext.tag == "el-upload") &&
        event.to.className.indexOf("drag-wrapper") != -1
      ) {
        return false;
      }
    },
    draggeablePhotoMove(event) {
      const draggedContext = event.draggedContext.element;
      if (
        (draggedContext.formType == "2" || draggedContext.formType == "3") &&
        event.to.className == "drag-wrapper"
      ) {
        return false;
      }
    },
    previewForm() {
      let formData = {
        fileName: undefined,
        type: "file",
      };
      this.drawerVisible = true;
      this.generateConf = formData;
      this.AssembleFormData();
      let previewData = this.formData;
      let filedsData = previewData.fields.map((m) => {
        return {
          ...m,
          children: m.formType == "2" ? null : m.children,
        };
      });
      previewData.fields = filedsData;
      this.previewFormData = previewData;
    },

    execCopy(data) {
      let formData = {
        fileName: undefined,
        type: "file",
      };
      this.generateConf = formData;
    },
    AssembleFormData() {
      this.formData = {
        fields: JSON.parse(JSON.stringify(this.drawingList)),
        ...this.formConf,
      };
    },

    /**
     *  =============================== 编辑选项 ================================
     */
    // 当前组件的options修改
    setActiveDataOptions(element) {
      this.activeData = element;
      this.activeId = element.propertyCode || element.formCode;
    },
    //
    editlogincalCode(element) {
      this.activeData = element;
      this.activeId = element.propertyCode || element.formCode;
      this.logicalDialogVisible = true;
    },
    /**
     * ======================================  CRF编辑所用 start =========================================
     */
    //获取详情
    getTemplateInfo() {
      this.isSaveLoading = true;
      getQuestionFormDetail(this.articleId).then((res) => {
        let fields = [];
        if (res.code == 200 && res.data) {
          let resData = res.data;
          fields =
            resData.subjectList && resData.subjectList.length
              ? resData.subjectList.map((m, index) => {
                  if (m.id) m.id = m.id.toString();
                  const { attribute, subjectList, ...mInfo } = m;
                  let attributeJSON = attribute ? JSON.parse(attribute) : null;
                  if (attributeJSON && attributeJSON.tagType == "province") {
                    attributeJSON.options = cityData;
                  }
                  return {
                    ...attributeJSON,
                    renderKey: new Date().getTime() + "" + index,
                    ...mInfo,
                  };
                })
              : [];
        }
        this.formData["fields"] = fields;
        this.drawingList = fields;
        this.templateFormInfo = res.data;
        this.isSaveLoading = false;
      });
    },
    // recover save params
    recoverTemplatePrograms(data) {
      let subjectList = [];
      data.forEach((item) => {
        const { attribute, itemList, ...itemInfo } = item;
        if (Array.isArray(itemInfo.defaultValue)) {
          itemInfo.defaultValue = [];
        } else {
          itemInfo.defaultValue = null;
        }
        const { options, ...otherAttribute } = itemInfo;
        subjectList.push({
          attribute:
            item.tagType == "province" ? JSON.stringify(otherAttribute) : JSON.stringify(itemInfo),
          propertyCode: itemInfo.propertyCode,
          propertyName: itemInfo.propertyName,
          showProperty: itemInfo.showProperty,
          propertyType: itemInfo.labelType,
          sortNum: itemInfo.sortNum,
          updateTime: itemInfo.updateTime,
          updateBy: itemInfo.updateBy,
          isDoctorShow: item.isDoctorShow == "undefined" ? true : item.isDoctorShow,
          isPatientShow: item.isPatientShow == "undefined" ? true : item.isPatientShow,
          itemList:
            item.options && item.options.length
              ? item.options.map((m) => {
                  return {
                    ...m,
                    itemValue: m.value,
                    itemLabel: m.label,
                    itemScores: m.itemScores,
                  };
                })
              : [],
          articleId: this.articleId,
        });
      });

      return subjectList;
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
    //  保存
    saveCRFTemplate(event) {
      this.AssembleFormData();
      let submitData = [...this.formData.fields];
      let examSubjectList = this.recoverTemplatePrograms(submitData);
      // return
      let paramsData = {
        articleId: this.articleId,
        examSubjectList,
      };
      if (this.formType == "3") {
        paramsData.scoresTotalVal = this.convertScoresVal(examSubjectList);
      }
      this.isSaveLoading = true;
      saveQuestionForm(paramsData, this.$route.params.commonFlag)
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
    //

    /**
     * =====================================  CRF 编辑所用 end ==========================================
     */
    drawerLogicalChange(event) {
      this.activeData["relationInfo"] = event;
    },
  },
};
</script>
<style lang="scss">
.el-collapse-item__header {
  font-size: 14px;
}

.right-scrollbar .el-form {
  padding-top: 20px;
}
</style>

<style lang="scss" scoped>
$selectedColor: #f6f7ff;
$lighterBlue: #409eff;

::v-deep .el-tabs {
  .el-tabs__item {
    width: 100%;
    text-align: center;
    padding: 0;
  }

  .el-tabs__nav {
    width: 100%;
  }

  .el-tabs__header {
    margin-bottom: 0 !important;
  }
}

.crf_container {
  display: flex;
  background: #fff;

  & > div {
    flex-shrink: 0;
    height: calc(100vh - 85px);
  }

  .left-scrollbar {
    height: calc(100vh - 126px);
  }

  .left-board {
    width: 500px;

    .left-scrollbar {
      height: calc(100vh - 130px);

      .entry_list {
        display: flex;
        height: 100%;

        .entry_first_list,
        .entry_second_list {
          flex: 1;
          flex-shrink: 0;
          font-size: 14px;
          border-right: 1px #f5f5f5 solid;
        }

        .entry_first_list_item,
        .attribute_item {
          line-height: 38px;
          text-indent: 10px;
          cursor: pointer;
          border: 1px #fff solid;
          border-bottom-color: #f5f5f5;
        }

        .active_formItem {
          color: #fff;
          background: rgba(24, 144, 255, 0.8);
          cursor: all-scroll;

          &:hover {
            color: #fff;
          }
        }

        .attribute_item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding-right: 5px;
          cursor: move;
          border: 1px #fff solid;
          border-bottom-color: #f5f5f5;
          border-radius: 3px;

          &:hover {
            border: 1px dashed #1890ff;
            color: #1890ff;
          }
          .properties_item {
            display: flex;
            width: 100%;

            .properties_name {
              flex: 1;
            }

            .properties_tip {
              flex-shrink: 1;
              color: #409eff;
            }
          }
        }

        .children_list {
          background: #f8f8f8;

          .children_item {
            color: #333;
            line-height: 38px;
            text-indent: 30px;
            border: 1px solid #f5f5f5;

            &.active_children {
              background: #f5f5f5;
            }
          }

          .activeChildrenItem {
            color: #fff;
            background: rgba(24, 144, 255, 0.8);
          }
        }
      }

      .custom_list {
        .components-item {
          display: inline-block;
          width: 48%;
          margin: 1%;

          .custom_item {
            padding: 8px 10px;
            background: #f6f7ff;
            cursor: move;
            border: 1px dashed #f6f7ff;
            border-radius: 3px;

            .svg-icon {
              color: #777;
              margin-right: 3px;
            }

            &:hover {
              border-color: #1890ff;
            }
          }
        }
      }
    }
  }

  .center-board {
    flex: 1;
    border-left: 1px solid #f1e8e8;
    border-right: 1px solid #f1e8e8;

    .action-bar {
      position: relative;
      text-align: right;
      padding: 5px 15px;
      box-sizing: border-box;
      border: 1px solid #f1e8e8;
      border-top: none;
      border-left: none;

      .el-button {
        padding: 12px 10px;
      }

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
    }

    .form_tips {
      color: #1890ff;
      padding: 6px 8px;
      text-align: center;
    }

    .form_center_scrollbar {
      min-width: 300px;
      overflow-x: hidden;
      height: calc(100vh - 160px);
    }

    .center-board-row {
      padding: 0 15px 30px;
      min-height: calc(100% - 42px);
      box-sizing: border-box;

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

        .drawing-row-item {
          position: relative;
          cursor: move;
          box-sizing: border-box;
          border: 1px dashed #ccc;
          border-radius: 3px;
          padding: 10px 10px;
          margin-bottom: 15px;

          .drawing-row-item {
            margin-bottom: 2px;
          }

          .el-form-item {
            margin-bottom: 0;
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
            font-size: 12px;
            color: #bbb;
            display: inline-block;
            padding: 0 6px;
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
          .el-form-item {
            padding: 0 10px;
            width: 98%;
            margin: 0;
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
            position: absolute;
            top: 0px;
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
            right: 56px;
            border-color: $lighterBlue;
            color: $lighterBlue;
            background: #fff;

            &:hover {
              background: $lighterBlue;
              color: #fff;
            }
          }

          .drawing-item-logic {
            right: 88px;
            border-color: $lighterBlue;
            color: $lighterBlue;
            background: #fff;

            &:hover {
              background: $lighterBlue;
              color: #fff;
            }
          }

          .drawing-item-delete {
            right: 24px;
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
  }
}
</style>
