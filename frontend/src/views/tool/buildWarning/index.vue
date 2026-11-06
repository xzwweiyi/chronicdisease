<template>
  <div class="crf_container" v-loading="isSaveLoading">
    <div class="left-board">
      <el-tabs v-model="currentTab" class="left_tabs" stretch @tab-click="tabClick">
        <el-tab-pane label="标准录入项" name="entry" />
        <el-tab-pane label="自定义项" name="custom" />
      </el-tabs>
      <div class="left-scrollbar" v-show="currentTab == 'entry'" v-loading="isTabListLoading">
        <div class="entry_list" v-if="entryList.length">
          <el-scrollbar class="entry_first_list">
            <el-collapse
              v-model="activeEntryFromId"
              @change="entryFirstItemClick($event, '1')"
              accordion
            >
              <el-collapse-item
                v-for="(item, index) in entryList"
                :key="index"
                :title="item.formName"
                :name="item.libraryId"
                class="entry_first_list_item"
                :class="activeEntryFromId == item.libraryId ? 'active_formItem' : ''"
              >
                <template slot="title">
                  <el-tooltip
                    effect="dark"
                    :content="item.formName"
                    placement="top-start"
                    :disabled="item.formName.length < 16"
                  >
                    <p class="ellipsis">{{ item.formName }}</p>
                  </el-tooltip>
                </template>
                <draggable
                  class="components-draggable children_list"
                  v-show="activeEntryFromId == item.libraryId"
                  v-if="item.children && item.children.length"
                  :list="item.children"
                  :group="{ name: 'componentsGroup', pull: 'clone', put: false }"
                  :clone="cloneAttributeComponent"
                  draggable=".components-item"
                  :sort="false"
                  @end="onEnd"
                  :key="item.children.length"
                  :move="draggableItemMove"
                  :disabled="entrySecondList.find((f) => f.disabled)"
                >
                  <div
                    v-for="(element, index) in item.children"
                    :key="index"
                    :class="activeChildrenId == element.libraryId ? 'activeChildrenItem' : ''"
                    @click="childrenItemClick(element, '1')"
                    class="components-item children_item"
                  >
                    <el-tooltip
                      effect="dark"
                      :content="element.formName"
                      placement="top-start"
                      :disabled="element.formName.length < 16"
                    >
                      <p class="ellipsis">{{ element.formName }}</p>
                    </el-tooltip>
                  </div>
                </draggable>
              </el-collapse-item>
            </el-collapse>
          </el-scrollbar>
          <el-scrollbar class="enter_second_list">
            <draggable
              class="components-draggable"
              v-if="entrySecondList && entrySecondList.length"
              :list="entrySecondList"
              :group="{ name: 'componentsGroup', pull: 'clone', put: false }"
              :clone="cloneAttributeComponent"
              draggable=".components-item"
              :sort="false"
              :move="draggableItemMove"
              @end="onEnd"
            >
              <div
                v-for="(element, index) in entrySecondList"
                :key="index"
                @click="addAttributeComponent(element)"
                class="components-item attribute_item"
                :class="
                  element.labelType == 'form' || element.tag == 'el-upload' ? 'components-base' : ''
                "
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
        <el-empty v-else description="暂无标准录入项"></el-empty>
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
            ref="formCont"
            :label-width="formConf.labelWidth + 'px'"
            class="drawing-form"
          >
            <draggable
              class="drawing-board"
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
                @draggedMove="draggableItmeMove"
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
import draggable from "vuedraggable";
import RightPanel from "../build/RightPanel";
import EditItemOption from "../build/editItemOption.vue";
import {
  getLibraryFormListAll,
  getChildFormAndProperty,
  addTemplateFromAttribute,
  getTemplateFormDetail,
} from "@/api/tool/form";
import { getFllowupFormDetail, addFllowupFormAttribute } from "@/api/crf/fllowupCRF";
import { inputComponents, formConf } from "@/utils/generator/config";
import DraggableItem from "../build/DraggableItem";
import logical from "../build/logical.vue";
import FormDrawer from "../build/FormDrawer";
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
  },
  data() {
    return {
      formConf,
      currentTab: "entry",
      tabIndex: 0,
      isSaveLoading: false,
      entryList: [],
      entrySecondList: [],
      photographySecondList: [],
      currentLibraryFormChildInfo: undefined, //标准录入项当前二级信息
      isTabListLoading: false,
      activeEntryFromId: "",
      activeChildrenId: "",
      customList: [],
      childrenFormProperty: {},
      generateConf: null,
      // 中间部分内容
      drawingList: [],
      photoDrawingList: [],
      activeId: "",
      activeData: {},
      editDialogVisible: false, //编辑选项的弹窗是否显示
      formData: {},
      previewFormData: {},
      templateFormId: "",
      templateFormInfo: {},
      logicalDialogVisible: false,
      drawerVisible: false,
      formType: "4",
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
    this.formType = this.$route.query.formType;
    this.customList = inputComponents.filter(
      (f) =>
        f.labelType == "Input" ||
        f.labelType == "Radio" ||
        f.labelType == "Checkbox" ||
        f.labelType == "Select",
    );
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
    if (params.formSource == "CRF") {
      this.getTabList("1");
      if (params.templateFormId) {
        this.templateFormId = this.$route.params.templateFormId;
        this.getTemplateInfo();
      } else {
        this.getFloowInfo();
      }
    } else if (params.formSource == "CMS") {
    }
  },
  methods: {
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
        this.getTabList("1");
      }
    },
    /**
     * 子表单切换
     */
    childrenItemClick(event, formType) {
      if (formType == "1") {
        let list = [];
        if (event.libraryProperties && event.libraryProperties.length) {
          list = [...event.libraryProperties];
        }
        if (event.libraryForms && event.libraryForms.length) {
          list = [...list, ...event.libraryForms];
        }
        if (list.length) {
          list = list.map((item) => {
            item = {
              ...JSON.parse(item.attribute),
              ...item,
            };
            if (item.libraryProperties && item.libraryProperties.length) {
              item.children = list.map((m) => {
                return {
                  ...JSON.parse(m.attribute),
                  ...m,
                };
              });
            }
            return item;
          });
        }
        list.sort((a, b) => {
          return a.sortNum - b.sortNum;
        });
        // console.log('list', list)
        this.entrySecondList = list;
        this.currentLibraryFormChildInfo = event;
      }
      this.activeChildrenId = event.libraryId;
    },
    revertList(list) {
      let templateList = [];
      templateList = list.map((m) => {
        const { libraryProperties, ...itemInfo } = m;
        return {
          ...JSON.parse(m.attribute),
          ...itemInfo,
        };
      });
      return templateList;
    },
    /**
     * 获取标准录入项  获取标准拍照项  获取列表接口
     */
    getTabList(formType) {
      this.isTabListLoading = true;
      let params = {
        formType,
        parentId: 0,
      };
      getLibraryFormListAll(params).then((res) => {
        let resData = res.data;
        if (res.code == 200) {
          if (formType == "1") {
            resData.sort((a, b) => {
              return a.sortNum - b.sortNum;
            });
            this.entryList = resData;
            if (resData.length) {
              this.activeEntryFromId = resData[0].libraryId;
              this.getChildFormAndPropertyList(formType);
            }
          } else {
            this.isTabListLoading = false;
          }
        } else {
          this.isTabListLoading = false;
        }
      });
    },
    // 一级标准录入项tab切换
    entryFirstItemClick(event, formType) {
      if (event) {
        if (formType == "1") {
          this.activeEntryFromId = event;
        }
        this.getChildFormAndPropertyList(formType);
      }
    },
    // 获取子表单和属性列表
    getChildFormAndPropertyList(formType) {
      let params = {
        formType,
      };
      if (formType == "1") {
        params["libraryId"] = this.activeEntryFromId;
      }
      this.isTabListLoading = true;
      getChildFormAndProperty(params).then((res) => {
        let resData = res.data;
        if (resData) {
          let formList = resData.libraryForms;
          if (formList && formList.length) {
            formList = formList.map((item) => {
              return {
                ...item,
                tagType: "classifyForm",
              };
            });
            formList.sort((a, b) => {
              return a.sortNum - b.sortNum;
            });
          }
          if (formType == "1") {
            this.entryList.find((f) => f.libraryId == resData.libraryId).children = formList;
          } else if (formType == "2") {
            let photoList = resData.libraryForms;
            photoList.sort((a, b) => {
              return a.sortNum - b.sortNum;
            });

            this.photographySecondList = photoList;
          }
          this.childrenFormProperty = resData;
          this.$forceUpdate();
        }
        this.isTabListLoading = false;
      });
    },

    /**
     * ======================= 处理拖拽元素 ============================
     */
    addAttributeComponent(item) {
      let event = {
        ...JSON.parse(item.attribute),
        ...item,
        id: item.id.toString(),
      };
      this.addComponent(event);
    },
    // 检查是否已经在表单中
    checkHasItem(list, info) {
      // console.log('info', list, info)
      if (list.find((f) => f.propertyCode == info.propertyCode)) {
        return true;
      } else {
        return false;
      }
    },
    getScrollTopVal(drawingIndex) {
      let drawingListHeight = 0;
      this.drawingList.forEach((forItem, index) => {
        let stempArray = [
          {
            type: "Radio",
            height: 120,
          },
          {
            type: "Select",
            height: 120,
          },
          {
            type: "Checkbox",
            height: 120,
          },
          {
            type: "Textarea",
            height: 160,
          },
        ];
        if (index <= drawingIndex) {
          if (
            forItem.labelType == "form" &&
            forItem.formType == "1" &&
            forItem.children &&
            forItem.children.length
          ) {
            forItem.children.forEach((childItem) => {
              let stempArrayIndex = stempArray.findIndex((f) => f.type == childItem.labelType);
              if (stempArrayIndex > -1) {
                drawingListHeight = drawingListHeight + stempArray[stempArrayIndex].height;
              } else {
                drawingListHeight = drawingListHeight + 92;
              }
            });
          } else {
            let stempArrayIndex = stempArray.findIndex((f) => f.type == forItem.labelType);
            if (stempArrayIndex > -1) {
              drawingListHeight = drawingListHeight + stempArray[stempArrayIndex].height;
            } else {
              drawingListHeight = drawingListHeight + 92;
            }
          }
        }
      });
      return drawingListHeight;
    },
    addComponent(item) {
      const clone = this.cloneComponent(item);
      let scrollTopVal = this.$refs.formCont.$el.scrollHeight;
      if (this.currentTab == "entry") {
        let drawingIndex = this.drawingList.findIndex(
          (f) => f.sourceLibraryId == clone.libraryId && f.labelType == "form",
        );
        if (drawingIndex > -1) {
          scrollTopVal = this.getScrollTopVal(drawingIndex);
          clone.sourceLibraryId = clone.libraryId;
          this.drawingList[drawingIndex].children.push(clone);
        } else {
          let formInfo = JSON.parse(JSON.stringify(this.currentLibraryFormChildInfo));
          const { libraryProperties, ...otherItem } = formInfo;
          this.drawingList.push({
            ...JSON.parse(otherItem.attribute),
            ...otherItem,
            sourceLibraryId: clone.libraryId,
            children: [
              {
                ...clone,
                sourceLibraryId: clone.libraryId,
              },
            ],
          });
        }
      } else {
        this.drawingList.push(clone);
      }
      this.activeFormItem(clone);
      this.$nextTick(() => {
        this.$refs.centerWarningScroll.wrap.scrollTop = scrollTopVal;
      });
    },
    // 拖拽结束
    onEnd(obj) {
      if (
        obj.item.className.indexOf("components-base") != -1 &&
        obj.to.className == "drag-wrapper"
      ) {
        this.$message.warning("行容器内不可以嵌套行容器和上传组件");
        this.drawingList.forEach((item) => {
          if (item.children && item.children.length) {
            let deleteIndex = item.children.findIndex(
              (f) => f.labelType == "form" || f.tag == "el-upload",
            );
            item.children.splice(deleteIndex, 1);
          }
        });
      }
      let itemList = JSON.parse(JSON.stringify(this.drawingList));
      itemList.forEach((item, index) => {
        if (item.formType == "2" && item.parentId == 0) {
          let children = item.children;
          if (children.length) {
            children.forEach((childItem, childIndex) => {
              childItem.sortNum = index + childIndex;
            });
          }
          itemList.splice(index, 1, ...children);
        } else if (item.formType == "1" && item.labelType == "form" && item.children) {
          item.children = this.revertOnEndList(item.children);
        }
      });
      this.drawingList = itemList;
      // console.log('onEnd', obj, obj.from, obj.to)
      if (obj.from !== obj.to) {
        this.activeData = tempActiveData || {};
        this.activeId = this.idGlobal;
      }
    },
    revertOnEndList(list) {
      let templteArray = [];
      if (list.length) {
        list.forEach((item, index) => {
          if (
            (item.formType == "1" && item.parentId == 0) ||
            (item.formType == "1" && item.tagType == "classifyForm")
          ) {
            let children = item.children;
            if (children.length) {
              children.forEach((childItem, childIndex) => {
                childItem.sortNum = index + childIndex;
              });
            }
            list.splice(index, 1, ...children);
          }
          list = list.filter((f) => f.labelType != "form");
        });
        templteArray = list;
      }
      return templteArray;
    },
    cloneAttributeComponent(event) {
      let { attribute, libraryProperties, libraryForms, ...eventOtherInfo } = event;
      eventOtherInfo.id = eventOtherInfo.id && eventOtherInfo.id.toString();
      let eventChildren = [];
      if (libraryProperties && libraryProperties.length) {
        libraryProperties.forEach((item, index) => {
          eventChildren.push({
            ...item,
            ...this.recoverAttribute(item.attribute),
            id: item.id.toString(),
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
        ...eventOtherInfo,
        sourceLibraryId: event.libraryId,
        renderKey: new Date() + "",
      };
      let returnCloneItem = undefined;
      if (this.currentTab == "entry" && cloneItemInfo.labelType != "form") {
        if (this.drawingList.findIndex((f) => f.sourceLibraryId == event.libraryId) == -1) {
          let currentLibraryInfo = JSON.parse(JSON.stringify(this.currentLibraryFormChildInfo));
          let itemInfo = {
            ...currentLibraryInfo,
            ...JSON.parse(currentLibraryInfo.attribute),
            sourceLibraryId: event.libraryId,
            children: [
              {
                ...cloneItemInfo,
              },
            ],
          };
          returnCloneItem = itemInfo;
        } else {
          returnCloneItem = cloneItemInfo;
        }
      } else if (this.currentTab == "entry" && cloneItemInfo.labelType == "form") {
        if (cloneItemInfo.labelType == "form") {
          cloneItemInfo.children = eventChildren && eventChildren.length ? eventChildren : [];
        }
        returnCloneItem = cloneItemInfo;
      } else {
        if (cloneItemInfo.labelType == "form") {
          cloneItemInfo.children = eventChildren && eventChildren.length ? eventChildren : [];
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
    recoverAttribute(event) {
      return JSON.parse(event);
    },
    cloneComponent(origin) {
      const clone = JSON.parse(JSON.stringify(origin));
      // ********** formid = crf的id+formId  ***************
      // clone.formId = Math.random().toString(36).substring(2, 6)
      clone.span = formConf.span;
      clone.renderKey = +new Date(); // 改变renderKey后可以实现强制更新组件
      if (!clone.layout) clone.layout = "colFormItem";

      const codeStr = Math.random().toString(36).substring(2, 6);
      if (clone.layout === "colFormItem") {
        clone.vModel = `field${origin.tagIcon.replace("-", "")}${codeStr}`;
        if (clone.propertyCode) {
          clone.propertyCode = `${clone.propertyCode}${codeStr}`;
        } else {
          clone.propertyCode = `field${clone.propertyCode}${codeStr}`;
        }
        tempActiveData = clone;
      } else if (clone.layout === "rowFormItem") {
        delete clone.propertyName;
        if (clone.formCode) {
          clone.formCode = `${clone.formCode}${codeStr}`;
        } else {
          clone.formCode = `row${codeStr}`;
        }
        clone.formName = clone.formName ? clone.formName : `问题组`;
        clone.gutter = this.formConf.gutter;
        tempActiveData = clone;
      }
      return tempActiveData;
    },
    // 复制组件
    drawingItemCopy(item, parent, parentIndex) {
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
    // 标准录入项问题组内的组件移动
    draggableItmeMove(event) {
      const draggedContext = event.draggedContext.element;
      const relatedContext = event.relatedContext.element;
      if (
        (event.from.className !== event.to.className && draggedContext.sourceLibraryId) ||
        (draggedContext && draggedContext.sourceLibraryId != relatedContext.sourceLibraryId)
      ) {
        return false;
      }
    },
    // 标准录入项的move
    draggableItemMove(event) {
      // console.log('标准录入项的move', event)
      let draggedElement = event.draggedContext && event.draggedContext.element;
      let relatedElement = event.relatedContext && event.relatedContext.element;
      if (draggedElement) {
        if (
          (event.to.className == "drawing-board" &&
            (!this.drawingList.length ||
              this.drawingList.findIndex(
                (f) => f.sourceLibraryId == draggedElement.libraryId && f.labelType == "form",
              ) == -1)) ||
          (event.to.className == "drag-wrapper" &&
            relatedElement.sourceLibraryId &&
            relatedElement.sourceLibraryId == draggedElement.libraryId)
        ) {
          return true;
        } else {
          return false;
        }
      } else {
        return true;
      }
    },
    // 标准拍照项的拖拽
    draggeablePhotoMove(event) {
      const draggedContext = event.draggedContext.element;
      if (draggedContext.formType == "2" && event.to.className == "drag-wrapper") {
        return false;
      }
    },
    empty() {
      this.$confirm("确定要清空所有组件吗？", "提示", {
        type: "warning",
      }).then(() => {
        this.drawingList = [];
      });
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
    // 保存成CRF
    saveTemplate() {
      let params = this.$route.params;
      if (params.templateFormId) {
        //crf
        this.saveCRFTemplate();
      } else {
        //随访
        this.saveFollowupTemplate();
      }
    },
    //
    // recover params
    recoverTemplatePrograms(data, templateId, formListName, propertiesListName) {
      let templateFormsList = [];
      let templatePropertiesList = [];
      let params = this.$route.params;
      data.forEach((item, index) => {
        item.sortNum = index;
        const {
          children,
          attribute,
          medicalProperties,
          medicalFormList,
          templateForms,
          templateProperties,
          libraryProperties, //随访CRF 拍照项
          libraryForms,
          ...otherItem
        } = item;

        if (Array.isArray(otherItem.defaultValue)) {
          otherItem.defaultValue = [];
        } else {
          otherItem.defaultValue = null;
        }
        const { options, ...otherAttribute } = otherItem;
        let info = {
          attribute:
            item.tagType == "province" ? JSON.stringify(otherAttribute) : JSON.stringify(otherItem),
          sortNum: index,
        };
        if (item.labelType == "form") {
          info["templateId"] = this.templateFormInfo.templateId;
          info["openImage"] = item.openImage;
          info["openMultiple"] = item.openMultiple;
          info["parentId"] = templateId;
          info["formType"] = otherItem.formType;
          info["isDoctorShow"] = item.isDoctorShow == "undefined" ? true : item.isDoctorShow;
          info["isPatientShow"] = item.isPatientShow == "undefined" ? true : item.isPatientShow;
          if (item.children && item.children.length) {
            let itemInfo = this.recoverTemplatePrograms(
              item.children,
              item.templateId,
              formListName,
              propertiesListName,
            );
            info = {
              ...itemInfo,
              ...info,
            };
          }
          if (item.formType == "2" && item.libraryId) {
            info["baseFormId"] = item.libraryId;
          }
          if (params.templateFormId) {
            info["templateCode"] = item.formCode;
            info["templateName"] = item.formName;
          } else {
            info["medicalCode"] = item.formCode;
            info["medicalName"] = item.formName;
          }
          templateFormsList.push(info);
        } else {
          if (item.id) info["id"] = item.id.toString();
          info["parentId"] = ""; //逻辑需要
          info["propertyType"] = item.labelType;
          info["dictType"] = item.dictType;
          info["propertyCode"] = item.propertyCode;
          info["propertyName"] = item.propertyName;
          info["isDoctorShow"] = item.isDoctorShow == "undefined" ? true : item.isDoctorShow;
          info["isPatientShow"] = item.isPatientShow == "undefined" ? true : item.isPatientShow;
          // let warnMedicalEarlyList = item.warnMedicalEarlyForms
          // if (warnMedicalEarlyList && warnMedicalEarlyList.length) {
          //     info['warnMedicalEarlyForms'] = warnMedicalEarlyList.map(mItem => {
          //         mItem.warnMedicalEarlyConditionForms.forEach(forItem => {
          //             forItem.followId = this.templateFormInfo.followId;
          //             forItem.propertyId = item.id
          //         })
          //         return {
          //             ...mItem,
          //             formId: item.medicalFormId,
          //             followId: this.templateFormInfo.followId,
          //             propertyId: item.id
          //         }
          //     })
          // }
          if (params.templateFormId) {
            info["templateFormId"] = item.templateFormId;
          } else {
            info["medicalFormId"] = item.medicalFormId;
            item["followId"] = this.templateFormInfo.followId;
          }
          templatePropertiesList.push(info);
        }
      });
      let returnData = {
        [propertiesListName]: templatePropertiesList,
        [formListName]: templateFormsList,
        templateFormId: templateId || "",
      };
      return returnData;
    },
    AssembleFormData() {
      this.formData = {
        fields: JSON.parse(JSON.stringify(this.drawingList)),
        ...this.formConf,
      };
    },
    // 处理基础表单
    recoverTemplateForms(libraryForms, formName, propertiesName) {
      let list = [];
      if (libraryForms && libraryForms.length) {
        libraryForms.forEach((item, index) => {
          if (item.id) item.id = item.id.toString();
          const { attribute, libraryProperties, ...itemInfo } = item;
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
          if (item[propertiesName] && item[propertiesName].length) {
            info.children = item[propertiesName].map((m) => {
              if (m.id) m.id = m.id.toString();
              const { attribute, libraryProperties, ...mInfo } = m;
              return {
                ...JSON.parse(m.attribute),
                ...mInfo,
              };
            });
          }
          let libraryFormList = this.recoverTemplateForms(item[formName], formName, propertiesName);
          if (libraryFormList.length) {
            if (!info.children) {
              info.children = libraryFormList;
            } else {
              info.children.push(...libraryFormList);
            }
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
      getTemplateFormDetail(this.templateFormId).then((res) => {
        let fields = [];
        if (res.code == 200) {
          let resData = res.data;
          fields =
            resData.templateProperties && resData.templateProperties.length
              ? resData.templateProperties.map((m, index) => {
                  if (m.id) m.id = m.id.toString();
                  const { attribute, templateProperties, ...mInfo } = m;
                  let attributeJSON = JSON.parse(attribute);
                  if (attributeJSON.tagType == "province") {
                    attributeJSON.options = cityData;
                  }
                  return {
                    ...attributeJSON,
                    renderKey: new Date().getTime() + "" + index,
                    ...mInfo,
                  };
                })
              : [];
          let libraryFormList = this.recoverTemplateForms(
            resData.templateForms,
            "templateForms",
            "templateProperties",
          );
          if (libraryFormList.length) {
            fields.push(...libraryFormList);
          }
          fields.sort((a, b) => {
            return a.sortNum - b.sortNum;
          });
        }
        this.formData["fields"] = fields;
        this.drawingList = fields;
        this.templateFormInfo = res.data;
        this.isSaveLoading = false;
      });
    },
    //  保存
    saveCRFTemplate(event) {
      this.AssembleFormData();
      let submitData = [...this.formData.fields];
      let templateData = this.recoverTemplatePrograms(
        submitData,
        this.$route.query.medicalFormId,
        "templateForms",
        "templateProperties",
      );
      let paramsData = {
        ...this.templateFormInfo,
        templateProperties: templateData.templateProperties,
        templateForms: templateData.templateForms,
      };
      // return
      this.isSaveLoading = true;
      addTemplateFromAttribute(paramsData, this.$route.query.commonFlag)
        .then((res) => {
          this.isSaveLoading = false;
          this.$message.success("保存成功");
          if (event != "realSave") {
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
    /**
     * =====================================  CRF 编辑所用 end ==========================================
     */
    /**
     * =====================================  随访 编辑所用 start ==========================================
     */
    //获取详情
    getFloowInfo() {
      let query = this.$route.params;
      // return
      this.isSaveLoading = true;

      getFllowupFormDetail(query.medicalFormId).then((res) => {
        let fields = [];
        if (res.code == 200) {
          let resData = res.data;
          fields =
            resData.medicalProperties && resData.medicalProperties.length
              ? resData.medicalProperties.map((m, index) => {
                  if (m.id) m.id = m.id.toString();
                  const { attribute, medicalProperties, ...mInfo } = m;
                  let attributeJSON = JSON.parse(attribute);
                  if (attributeJSON.tagType == "province") {
                    attributeJSON.options = cityData;
                  }
                  return {
                    ...attributeJSON,
                    renderKey: new Date().getTime() + "" + index,
                    ...mInfo,
                  };
                })
              : [];
          let libraryFormList = this.recoverTemplateForms(
            resData.medicalFormList,
            "medicalFormList",
            "medicalProperties",
          );
          if (libraryFormList.length) {
            fields.push(...libraryFormList);
          }
          fields.sort((a, b) => {
            return a.sortNum - b.sortNum;
          });
        }
        this.formData["fields"] = fields;
        this.drawingList = fields;
        this.templateFormInfo = res.data;
        this.isSaveLoading = false;
      });
    },
    // 保存
    saveFollowupTemplate() {
      this.AssembleFormData();
      let submitData = [...this.formData.fields];
      let templateData = this.recoverTemplatePrograms(
        submitData,
        this.$route.query.templateId,
        "medicalFormList",
        "medicalProperties",
      );
      let paramsData = {
        ...this.templateFormInfo,
        formType: "4",
        medicalProperties: templateData.medicalProperties,
        medicalFormList: templateData.medicalFormList,
      };
      // return
      this.isSaveLoading = true;
      addFllowupFormAttribute(paramsData)
        .then((res) => {
          this.isSaveLoading = false;
          this.$message.success("保存成功");
          this.$tab.closePage(this.$route);
          let query = this.$route.query;
          this.$router.push({
            path: `${decodeURIComponent(query.oldPath)}`,
          });
        })
        .catch((eror) => {
          this.isSaveLoading = false;
        });
    },
    drawerLogicalChange(event) {
      this.$nextTick(() => {
        this.activeData["relationInfo"] = event;
      });
    },
  },
};
</script>
<style lang="scss">
.el-collapse-item__header {
  font-size: 14px;
}

.crf_container .el-tabs {
  .el-tabs__item {
    width: 100%;
    text-align: center;
    padding: 0;
  }

  .el-tabs__nav {
    width: 100%;
  }
}

.active_formItem {
  .el-collapse-item__header {
    color: #fff;
    background: rgba(24, 144, 255, 0.8);
    font-size: 14px;
  }
}

.right-scrollbar .el-form {
  padding-top: 20px;
}
</style>

<style lang="scss" scoped>
$selectedColor: #f6f7ff;
$lighterBlue: #409eff;

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
        .enter_second_list {
          width: 50%;
          font-size: 14px;
          border-right: 1px #f1e8e8 solid;
        }

        .entry_first_list_item,
        .attribute_item {
          line-height: 38px;
          text-indent: 10px;
          cursor: pointer;
        }

        .attribute_item {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding-right: 5px;
          cursor: move;
          border: 1px #fff solid;
          border-bottom-color: #f1e8e8;
          border-radius: 3px;

          &:hover {
            border: 1px dashed #1890ff;
            color: #1890ff;
          }
        }

        .children_list {
          background: #f8f8f8;

          .children_item {
            color: #333;
            line-height: 38px;
            text-indent: 30px;
            border-top: 1px solid #ededed;

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

      .photo_list .active_formItem {
        color: #fff;
        background: rgba(24, 144, 255, 0.8);

        &:hover {
          color: #fff;
        }
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

      .custom_list {
        .components-item {
          display: inline-block;
          width: 48%;
          margin: 1%;

          .custom_item {
            padding: 10px;
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
      min-width: 320px;
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
          margin: 0 0 15px !important;

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
