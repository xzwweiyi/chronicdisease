<template>
  <div class="crf_container" v-loading="isSaveLoading">
    <div class="left-board">
      <el-tabs v-model="currentTab" class="left_tabs" stretch @tab-click="tabClick">
        <el-tab-pane label="标准录入项" name="entry" v-if="formType != 3" />
        <el-tab-pane label="标准拍照项" name="photograph" v-if="formType == 0" />
        <el-tab-pane label="标准量表" name="liang" v-if="formType == 3" />
        <el-tab-pane label="自定义项" name="custom" />
      </el-tabs>
      <div
        class="left-scrollbar entry_list_scrollbar"
        v-show="currentTab == 'entry'"
        v-loading="isTabListLoading"
      >
        <div class="search_content">
          <el-input
            v-model="formNameSearch"
            clearable
            placeholder="请输入录入项查询"
            @keyup.enter.native="searchEntryList"
          ></el-input>
          <el-button type="primary" plain @click="searchEntryList"> 搜索 </el-button>
        </div>
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
                  :group="{
                    name: 'componentsGroup',
                    pull: 'clone',
                    put: false,
                  }"
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
          <el-scrollbar class="entry_second_list" v-loading="isTabChildLoading">
            <draggable
              class="components-draggable"
              v-if="entrySecondList && entrySecondList.length"
              :list="entrySecondList"
              :group="{ name: 'componentsGroup', pull: 'clone', put: false }"
              :clone="cloneAttributeComponent"
              draggable=".components-item"
              :sort="false"
              @end="onEnd"
              :move="draggableItemMove"
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
      <div class="left-scrollbar" v-show="currentTab == 'photograph'" v-loading="isTabListLoading">
        <div class="entry_list photo_list" v-if="photographyList.length">
          <el-scrollbar class="entry_first_list">
            <draggable
              class="components-draggable"
              :list="photographyList"
              :group="{ name: 'componentsGroup', pull: 'clone', put: false }"
              :clone="cloneFormComponent"
              draggable=".components-item"
              :sort="false"
              @end="onEnd"
              :move="draggeablePhotoMove"
            >
              <div
                v-for="(element, index) in photographyList"
                :key="index"
                :class="activePhotographyFromId == element.libraryId ? 'active_formItem' : ''"
                class="components-item attribute_item components-base"
                @click="entryFirstItemClick(element.libraryId, '2')"
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
          <el-scrollbar class="entry_second_list" v-loading="isTabChildLoading">
            <draggable
              class="components-draggable"
              v-if="photographySecondList && photographySecondList.length"
              :list="photographySecondList"
              :group="{ name: 'componentsGroup', pull: 'clone', put: false }"
              :clone="cloneFormComponent"
              draggable=".components-item"
              :sort="false"
              @end="onEnd"
              :move="draggeablePhotoMove"
            >
              <div
                v-for="(element, index) in photographySecondList"
                :key="index"
                @click="addAttributeComponent(element)"
                class="components-item attribute_item components-base"
              >
                <div class="properties_item">
                  <el-tooltip
                    effect="dark"
                    :content="element.formName"
                    placement="top-start"
                    :disabled="element.formName && element.formName.length < 16"
                  >
                    <p class="ellipsis">{{ element.formName }}</p>
                  </el-tooltip>
                </div>
              </div>
            </draggable>
            <el-empty v-else description="暂无属性内容"></el-empty>
          </el-scrollbar>
        </div>
        <el-empty v-else description="暂无标准拍照项"></el-empty>
      </div>
      <div
        class="left-scrollbar custom_scrollbar"
        v-show="currentTab == 'liang'"
        v-loading="isTabListLoading"
      >
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
                class="components-item attribute_item entry_first_list_item"
                @click="entryFirstItemClick(element.libraryId, '3')"
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
          <el-scrollbar class="entry_second_list" v-loading="isTabChildLoading">
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
      <el-scrollbar class="left-scrollbar custom_scrollbar" v-show="currentTab == 'custom'">
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
                @draggedMove="draggableItmeMove"
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
      @warnEarlyChange="warnEarlyValueChange"
      :isShowPropertyCode="false"
      :formula-current="formulaDialogVisible"
      @tag-formula="tagFormula"
      @set-calculation-formula="setCalculationFormula"
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
      v-if="logicalDialogVisible"
    ></logical>
    <form-drawer
      size="60%"
      :visible.sync="drawerVisible"
      :form-data="previewFormData"
      :generate-conf="generateConf"
      @copy="execCopy"
    />
    <!-- 公式设置 -->
    <formula-dialog
      v-if="formulaDialogVisible"
      :visible.sync="formulaDialogVisible"
      :current-item="activeData"
      title="公式设置"
      :drawing-list="drawingList"
      :key="activeData.propertyCode"
    >
    </formula-dialog>
  </div>
</template>

<script>
import draggable from "vuedraggable";
import RightPanel from "../build/RightPanel";
import EditItemOption from "../build/editItemOption";
import SetInstallmentInfo from "../build/setInstallmentInfo";
import {
  getLibraryFormListAll,
  getChildFormAndProperty,
  addTemplateFromAttribute,
  getTemplateFormDetail,
} from "@/api/tool/form";
import {
  getFllowupFormDetail,
  addFllowupFormAttribute,
  deleteFllowupCRFProperty,
} from "@/api/crf/fllowupCRF";
import { selectDiseases } from "@/api/info/config.js";
import { listDiseasesGroups } from "@/api/basics/config.js";
import { inputComponents, formConf } from "@/utils/generator/config";
import DraggableItem from "../build/DraggableItem";
import formulaDialog from "../build/formulaDialog";
import logical from "../build/logical";
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
    formulaDialog,
    SetInstallmentInfo,
  },
  data() {
    return {
      formConf,
      currentTab: "entry",
      tabIndex: 0,
      isSaveLoading: false,
      entryList: [], //标准录入项
      entrySecondList: [], //标准录入项
      photographyList: [], //标准拍照项
      photographySecondList: [], //标准拍照项
      currentLibraryFormChildInfo: undefined, //标准录入项当前二级信息
      isTabListLoading: false,
      isTabChildLoading: false,
      activePhotographyFromId: "", //标准拍照项
      activeEntryFromId: "", //标准录入项
      activeChildrenId: "", //标准录入项
      liangList: [], //标准量表
      liangSecondList: [], //标准量表
      activeLiangFromId: "", //标准量表
      customList: [], //自定义
      childrenFormProperty: {},
      generateConf: null,
      // 中间部分内容
      drawingList: [],
      photoDrawingList: [],
      activeId: "",
      activeData: {},
      editDialogVisible: false, //编辑选项的弹窗是否显示
      formData: {},
      templateFormId: "",
      templateFormInfo: {},
      logicalDialogVisible: false,
      drawerVisible: false,
      setInstallmentVisible: false, // 分期规则弹窗
      // end
      formType: 0, // 0模块1标准项2拍照项3自评量表4预警填报
      warnEarlySameIndex: -1, //有相同与预警项的下标
      warnEarlyEmptyIndex: -1, //有未设置完的预警项下标
      previewFormData: {},
      formNameSearch: "",
      formulaDialogVisible: false, //公式设置弹窗
      setTimeoutTimes: -1,
      saveSetTimeout: null,
    };
  },
  created() {
    // 防止 firefox 下 拖拽 会新打卡一个选项卡
    document.body.ondrop = (event) => {
      event.preventDefault();
      event.stopPropagation();
    };
    this.formType = this.$route.query.formType;
    this.getDepartmentData();
    this.getDiseasesGroups();
    this.customList =
      this.formType == "3"
        ? inputComponents.filter((f) => f.labelType != "pager" && f.labelType != "upload")
        : inputComponents.filter(
            (f) =>
              f.labelType != "pager" &&
              f.labelType != "upload" &&
              f.labelType != "Statistic" &&
              f.labelType != "Rate",
          );

    this.customList.find((f) => f.tagType == "province").options = cityData;
  },
  watch: {
    drawingList: {
      deep: true,
      handler(newVal) {
        this.setTimeoutTimes++;
        if (this.setTimeoutTimes == 1) {
          this.saveSetTimeout = setTimeout(() => {
            // this.saveTemplate('realSave')
          }, 300000);
        }
      },
    },
  },
  mounted() {
    let params = this.$route.params;
    if (this.formType == "3") {
      this.currentTab = "liang";
      this.getTabList("3");
    } else {
      this.getTabList("1");
    }
    if (params.templateFormId) {
      this.templateFormId = this.$route.params.templateFormId;
      this.getTemplateInfo();
    } else {
      this.getFloowInfo();
    }
  },
  beforeDestroy() {
    clearTimeout(this.saveSetTimeout);
  },
  methods: {
    // 左侧组件数据处理
    // 科室选择组件
    getDepartmentData() {
      selectDiseases().then((res) => {
        let resData = res.data;
        resData.forEach((item) => {
          item["value"] = item.id && item.id.toString();
          item.children = [];
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
          item["value"] = item.diseasesGroupsId && item.diseasesGroupsId.toString();
          item["label"] = item.diseasesGroupsName;
          item.children = [];
        });
        this.customList.find((f) => f.tagType == "diseases").options = res.data;
      });
    },
    /**
     * ============== 左侧固定数据组件数据处理 end
     */
    // 搜索标准录入项
    searchEntryList() {
      this.getTabList(1);
    },
    /**
     * 左侧tab切换
     */
    tabClick(event) {
      if (event.index == this.tabIndex) return;
      this.tabIndex = event.index;
      this.activeChildrenId = "";
      let getFormType = 1;
      if (this.formType == "0") {
        getFormType = event.index == "0" ? "1" : event.index == "1" ? "2" : "";
      } else if (this.formType == "3") {
        getFormType = event.index == "0" ? "3" : "";
      } else if (this.formType == "4") {
        getFormType = event.index == "0" ? "1" : "";
      }
      if (getFormType != "") {
        this.getTabList(getFormType);
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
              id: item.id && item.id.toString(),
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
        this.entrySecondList = list;
        this.currentLibraryFormChildInfo = event;
      } else if (this.formType == "3") {
        this.liangSecondList = event.libraryProperties;
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
      if (this.formNameSearch) {
        params["formNameSearch"] = this.formNameSearch;
      }
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
              this.getChildFormAndPropertyList(formType, this.activeEntryFromId);
            }
          } else if (formType == "2") {
            this.photographyList = resData;
            if (resData.length) {
              this.activePhotographyFromId = resData[0].libraryId;
              this.getChildFormAndPropertyList(formType, this.activePhotographyFromId);
            }
          } else if (formType == "3") {
            // this.liangList.forEach(item => {
            //     if (item.libraryId == resData.libraryId) {
            //         item['children'] = resData.libraryForms;
            //     }
            // })
            this.liangList = resData;
            if (resData.length) {
              this.activeLiangFromId = resData[0].libraryId;
              this.getChildFormAndPropertyList(formType, this.activeLiangFromId);
            }
          }
          this.isTabListLoading = false;
        } else {
          this.isTabListLoading = false;
        }
      });
    },
    // 一级标准录入项tab切换
    entryFirstItemClick(event, formType) {
      if (event) {
        let libraryId = "";
        if (formType == "1") {
          this.activeEntryFromId = event;
          libraryId = event;
        } else if (formType == "2") {
          this.activePhotographyFromId = event;
          libraryId = event;
        } else if (formType == "3") {
          this.activeLiangFromId = event;
          libraryId = event;
        }
        this.getChildFormAndPropertyList(formType, libraryId);
      }
    },
    // 获取子表单和属性列表
    getChildFormAndPropertyList(formType, libraryId) {
      let params = {
        formType,
        libraryId,
        formName: this.formNameSearch,
      };
      this.isTabChildLoading = true;
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
            if (formList && formList.length) {
              this.childrenItemClick(formList[0], "1");
            } else {
              this.entrySecondList = [];
            }
          } else if (formType == "2") {
            let photoList = resData.libraryForms;
            photoList.sort((a, b) => {
              return a.sortNum - b.sortNum;
            });

            this.photographySecondList = photoList;
          } else if (formType == "3") {
            let libraryData = [];
            if (resData.libraryForms && resData.libraryForms.length) {
              libraryData = libraryData.concat(
                resData.libraryForms.map((item) => {
                  return {
                    ...JSON.parse(item.attribute),
                    ...item,
                    id: item.libraryId && item.libraryId.toString(),
                    children: (item.libraryProperties || []).map((prop) => {
                      return {
                        ...JSON.parse(prop.attribute),
                        ...prop,
                        id: prop.id && prop.id.toString(),
                        renderKey: prop.renderKey || new Date().getTime(),
                      };
                    }),
                  };
                }),
              );
            }
            if (resData.libraryProperties && resData.libraryProperties.length) {
              libraryData = libraryData.concat(
                resData.libraryProperties.map((item) => {
                  return {
                    ...JSON.parse(item.attribute),
                    ...item,
                    id: item.id && item.id.toString(),
                    renderKey: item.renderKey || new Date().getTime(),
                  };
                }),
              );
            }
            this.liangSecondList = libraryData;

            this.liangList.find((f) => f.libraryId == this.activeLiangFromId).children =
              libraryData;
          } else if (formType == "4") {
          }
          this.childrenFormProperty = resData;
          this.$forceUpdate();
        }
        this.isTabChildLoading = false;
      });
    },

    /**
     * ======================= 处理拖拽元素 ============================
     */
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
      let currentItem = JSON.parse(info.attribute);
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
            isPatientShow: formInfo.isPatientShow != undefined ? formInfo.isPatientShow : true,
            isDoctorShow: formInfo.isDoctorShow != undefined ? formInfo.isDoctorShow : true,
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
          //标准拍照项一级拖拽
          let children = item.children;
          if (children.length) {
            children.forEach((childItem, childIndex) => {
              childItem.sortNum = index + childIndex;
            });
          }
          itemList.splice(index, 1, ...children);
        } else if (item.formType == "1" && item.labelType == "form" && item.children) {
          //标准录入项一级拖拽
          item.children = this.revertOnEndList(item.children);
        } else if (item.formType == "3" && item.labelType == "form") {
          let children = item.children;
          if (children.length) {
            children.forEach((childItem, childIndex) => {
              childItem.sortNum = index + childIndex;
            });
          }
          itemList.splice(index, 1, ...children);
        }
      });
      this.drawingList = itemList;
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
          // 不再过滤 labelType == "form" 的项，保留问题组结构  2026/3/19
          // list = list.filter((f) => f.labelType != "form");
        });
        templteArray = list;
      }
      return templteArray;
    },
    // 替换 cloneAttributeComponent 方法中的相关逻辑（第 450-520 行）
    cloneAttributeComponent(event) {
      let { attribute, libraryProperties, libraryForms, ...eventOtherInfo } = event;
      eventOtherInfo.id = eventOtherInfo.id && eventOtherInfo.id.toString();
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
        ...eventOtherInfo,
        sourceLibraryId: event.libraryId,
        renderKey: new Date().getTime() + "",
      };

      let returnCloneItem = undefined;

      if (this.currentTab == "entry" && cloneItemInfo.labelType != "form") {
        if (this.drawingList.findIndex((f) => f.sourceLibraryId == event.libraryId) == -1) {
          let currentLibraryInfo = JSON.parse(JSON.stringify(this.currentLibraryFormChildInfo));
          let itemInfo = {
            ...currentLibraryInfo,
            ...JSON.parse(currentLibraryInfo.attribute),
            sourceLibraryId: event.libraryId,
            isPatientShow:
              currentLibraryInfo.isPatientShow != undefined
                ? currentLibraryInfo.isPatientShow
                : true,
            isDoctorShow:
              currentLibraryInfo.isDoctorShow != undefined ? currentLibraryInfo.isDoctorShow : true,
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
        // 无论是标准项还是自定义项，都正确初始化 children
        if (eventChildren && eventChildren.length) {
          // 标准项有 children（来自 libraryProperties 或 libraryForms）
          cloneItemInfo.children = eventChildren;
        } else {
          // 自定义项没有 children，初始化为空数组以支持后续拖拽
          cloneItemInfo.children = [];
        }
        returnCloneItem = cloneItemInfo;
      } else if (this.currentTab == "liang" && cloneItemInfo.labelType == "form") {
        cloneItemInfo.children = cloneItemInfo.children.map((m, index) => {
          return {
            ...JSON.parse(m.attribute),
            ...m,
            id: m.id && m.id.toString(),
            renderKey: new Date().getTime() + "" + index,
          };
        });
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
    // 第 650-680 行，确保 children 初始化
    cloneComponent(origin) {
      const clone = JSON.parse(JSON.stringify(origin));
      const codeStr = Math.random().toString(36).substring(2, 6);
      clone.renderKey = new Date().getTime() + codeStr;

      if (!clone.layout) clone.layout = "colFormItem";
      if (!clone.span) clone.span = 24;

      if (clone.labelType === "form" && !Array.isArray(clone.children)) {
        clone.children = [];
      }

      if (clone.layout === "colFormItem") {
        clone.vModel = `field${origin.labelType}${codeStr}`;
        if (clone.propertyCode) {
          clone.propertyCode = `${clone.propertyCode}`;
        } else {
          clone.propertyCode = `field${clone.labelType}${codeStr}`;
        }
      } else if (clone.layout === "rowFormItem") {
        delete clone.propertyName;
        if (clone.formCode) {
          clone.formCode = `${clone.formCode}`;
        } else {
          clone.formCode = `row${codeStr}`;
        }
        clone.formName = clone.formName ? clone.formName : `问题组`;
        clone.gutter = this.formConf.gutter;
      }

      if (clone.tagType == "province") {
        clone.options = cityData;
      } else if (clone.tagType == "nation") {
        clone.options = nationData;
      }

      if (Array.isArray(clone.children) && clone.children.length > 0) {
        clone.children = clone.children.map((childItem) => this.cloneComponent(childItem));
      }

      tempActiveData = { required: true, ...clone };
      return tempActiveData;
    },
    // 复制组件
    drawingItemCopy(item, parent, parentIndex) {
      let clone = JSON.parse(JSON.stringify(item));
      clone = this.createIdAndKey(clone);
      parent.push(clone);
      let scrolltop = this.$refs.centerBoardScroll.$el.clientHeight;
      this.$nextTick(() => {
        this.$refs.centerWarningScroll.wrap.scrollTop = scrolltop;
      });
      this.activeFormItem(clone);
    },
    createIdAndKey(event) {
      const { id, ...item } = event;
      const codeStr = Math.random().toString(36).substring(2, 6);
      item.renderKey = +new Date().getTime();
      if (!item.span) item.span = 24;

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
      if (item.tagType == "province") {
        item.options = cityData;
      } else if (item.tagType == "nation") {
        item.options = nationData;
      }
      if (Array.isArray(item.children)) {
        item.children = item.children.map((childItem) => this.createIdAndKey(childItem));
      }
      return item;
    },
    // 删除组件
    drawingItemDelete(index, parent) {
      const deletedItem = parent[index];
      console.log(deletedItem);

      // 添加删除确认弹窗
      this.$confirm("确定要删除该项吗？删除后将无法恢复。", "删除", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let params = {
            id: deletedItem.id,
            medicalFormId: deletedItem.medicalFormId,
            formType: this.formType,
          };
          deleteFllowupCRFProperty(params)
            .then(() => {
              parent.splice(index, 1);
              this.$nextTick(() => {
                const len = this.drawingList.length;
                if (len) {
                  let drawingData = this.convertFieldsDataLoop(this.drawingList);
                  this.drawingList.forEach((item, itemIndex) => {
                    let relationInfo = item.relationInfo;
                    if (
                      relationInfo &&
                      relationInfo.relationQuestion &&
                      relationInfo.relationQuestion.length
                    ) {
                      relationInfo.relationQuestion.forEach((relationItem, relationIndex) => {
                        let hasIndex = drawingData.findIndex(
                          (f) => f.propertyCode == relationItem.questionId,
                        );
                        if (hasIndex == -1) {
                          relationInfo.relationQuestion.splice(relationIndex, 1);
                        }
                      });
                    }
                    if (
                      !relationInfo ||
                      !relationInfo.relationQuestion ||
                      !relationInfo.relationQuestion.length
                    ) {
                      relationInfo = undefined;
                    }
                    item.relationInfo = relationInfo;
                  });
                }
              });
              // 删除成功提示
              this.$message.success("删除成功");
            })
            .catch((error) => {
              console.error("删除失败:", error);
              this.$message.error("删除失败，请重试");
            });
        })
        .catch(() => {
          // 用户取消删除
          this.$message.info("已取消删除");
        });
    },
    // 编辑属性内容
    editCurrentItemOptions(element) {
      this.activeData = element;
      this.activeId = element.propertyCode || element.formCode;
      this.editDialogVisible = true;
    },
    setInstallmentRulesList(element) {
      this.activeData = element;
      this.activeId = element.propertyCode || element.formCode;
      this.setInstallmentVisible = true;
    },
    activeFormItem(element) {
      this.activeData = element;
      this.activeId = element.propertyCode || element.formCode;
    },
    datadragEnd(event) {},
    // 标准录入项的拖拽
    draggableItemMove(event) {
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
            relatedElement &&
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
    // 标准录入项问题组内的组件移动
    draggableItmeMove(event) {
      const draggedContext = event.draggedContext.element;
      const relatedContext = event.relatedContext.element;
      if (
        (event.from.className !== event.to.className &&
          relatedContext &&
          draggedContext.sourceLibraryId) ||
        (draggedContext &&
          relatedContext &&
          draggedContext.sourceLibraryId != relatedContext.sourceLibraryId) ||
        (draggedContext && !relatedContext && draggedContext.sourceLibraryId)
      ) {
        return false;
      }
    },
    // 标准拍照项的拖拽
    draggeablePhotoMove(event) {
      const draggedContext = event.draggedContext.element;
      if (
        (draggedContext.formType == "2" || draggedContext.formType == "3") &&
        event.to.className == "drag-wrapper"
      ) {
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
      let filedsData = this.drawingList.map((m) => {
        return {
          ...m,
          children: m.formType == "2" ? null : m.children,
        };
      });
      previewData.fields = filedsData;
      this.$nextTick(() => {
        this.previewFormData = previewData;
      });
    },

    execCopy(data) {
      let formData = {
        fileName: undefined,
        type: "file",
      };
      this.generateConf = formData;
    },
    // 保存成CRF
    saveTemplate(event) {
      this.$confirm("修改后已填历史数据可能丢失,确定要修改吗？", "提示", {
        type: "warning",
      }).then(() => {
        let params = this.$route.params;
        if (params.templateFormId) {
          //crf模板库
          this.saveCRFTemplate(event);
        } else {
          //随访
          this.saveFollowupTemplate(event);
        }
      });
    },
    //
    // recover params
    convertSaveTemplatePrograms(data, templateId, formListName, propertiesListName) {
      let templateFormsList = [];
      let templatePropertiesList = [];
      let params = this.$route.params;
      data.forEach((item, index) => {
        item.sortNum = index;
        let newItem = item.attribute
          ? {
              ...JSON.parse(item.attribute),
              ...item,
            }
          : item;
        if (Array.isArray(newItem.defaultValue)) {
          newItem.defaultValue = [];
        } else {
          newItem.defaultValue = null;
        }
        const {
          children,
          attribute,
          medicalProperties,
          medicalFormList,
          templateForms,
          templateProperties,
          libraryProperties,
          warnMedicalEarlyForms,
          warnTemplateEarlyForms,
          warnTemplateEarlyVOS,
          ...otherItem
        } = newItem;

        const { options, ...otherAttribute } = otherItem;
        let info = {
          attribute:
            newItem.tagType == "province"
              ? JSON.stringify(otherAttribute)
              : JSON.stringify(otherItem),
          sortNum: index,
        };
        if (newItem.labelType == "form") {
          info["templateId"] = this.templateFormInfo.templateId;
          info["openImage"] = item.openImage;
          info["openMultiple"] = item.openMultiple;
          info["parentId"] = templateId;
          info["formType"] = !item.formType ? this.formType : item.formType;
          (info["baseFormId"] = item.libraryId ? item.libraryId : null),
            (info["isDoctorShow"] = item.isDoctorShow == "undefined" ? true : item.isDoctorShow);
          info["isPatientShow"] = item.isPatientShow == "undefined" ? true : item.isPatientShow;
          if (item.medicalFormId) {
            info["medicalFormId"] = item.medicalFormId;
          }
          item.children =
            item.children && item.children.length
              ? item.children
              : medicalProperties && medicalProperties.length
              ? medicalProperties
              : libraryProperties && libraryProperties.length
              ? libraryProperties
              : templateProperties && templateProperties.length
              ? templateProperties
              : [];

          if (item.children && item.children.length) {
            let itemInfo = this.convertSaveTemplatePrograms(
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
          if (this.formType == "2" && item.libraryId) {
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
          info["propertyType"] = newItem.labelType;
          info["dictType"] = item.dictType;
          info["propertyCode"] = item.propertyCode;
          info["propertyName"] = item.propertyName;
          info["isDoctorShow"] = item.isDoctorShow == "undefined" ? true : item.isDoctorShow;
          info["isPatientShow"] = item.isPatientShow == "undefined" ? true : item.isPatientShow;
          if (item.options && item.options.length && this.formType == "3") {
            info["itemList"] = item.options.map((m, index) => {
              const id = item.itemList && item.itemList[index] ? item.itemList[index].id : null;
              return {
                ...m,
                itemLabel: m.label,
                itemValue: m.value,
                id: id,
              };
            });
          }
          if (params.templateFormId) {
            // info['templateFormId'] = item.templateFormId
          } else {
            info["medicalFormId"] = item.medicalFormId;
            info["followId"] = this.templateFormInfo.followId;
          }
          if (this.formType == "4") {
            let earlyFormsName = params.templateFormId
              ? "warnTemplateEarlyForms"
              : "warnMedicalEarlyForms";
            let earlyVosName = params.templateFormId
              ? "warnTemplateEarlyVOS"
              : "warnMedicalEarlyVOS";
            let earlyConditionFormsName = params.templateFormId
              ? "warnTemplateEarlyConditionForms"
              : "warnMedicalEarlyConditionForms";
            let earlyConditionVOSName = params.templateFormId
              ? "warnTemplateEarlyConditionVOS"
              : "warnMedicalEarlyConditionVOS";
            let warnMedicalEarlyList = item[earlyFormsName];
            let warnEarlyConditionForms = [];
            if (warnMedicalEarlyList && warnMedicalEarlyList.length) {
              warnMedicalEarlyList = warnMedicalEarlyList;
              warnEarlyConditionForms = warnMedicalEarlyList[0][earlyConditionFormsName];
            } else if (item[earlyVosName] && item[earlyVosName].length) {
              warnMedicalEarlyList = item[earlyVosName];
              warnEarlyConditionForms = warnMedicalEarlyList[0][earlyConditionVOSName];
            }
            if (warnMedicalEarlyList && warnMedicalEarlyList.length) {
              info[earlyFormsName] = warnMedicalEarlyList.map((mItem) => {
                let warnEarlyCheckInfo = this.isSameWarnEarlyInfo(
                  mItem.isCondition,
                  warnEarlyConditionForms,
                );
                if (warnEarlyCheckInfo.sameIndex > -1) {
                  this.warnEarlySameIndex = warnEarlyCheckInfo.sameIndex;
                  this.$message.warning(`${info["propertyName"]}的预警规则有相同条件设置`);
                  return;
                }
                if (warnEarlyCheckInfo.emptyIndex > -1) {
                  this.warnEarlyEmptyIndex = warnEarlyCheckInfo.emptyIndex;
                  this.$message.warning(`${info["propertyName"]}组件的预警规则不能为空`);
                  return;
                }
                mItem[earlyConditionFormsName] = warnEarlyConditionForms.map((forItem) => {
                  return {
                    ...forItem,
                    id: item.id ? forItem.id : null,
                    followId: this.templateFormInfo.followId,
                    propertyId: item.id,
                    earlyWarnValue: Array.isArray(forItem.earlyWarnValue)
                      ? forItem.earlyWarnValue.toString()
                      : forItem.earlyWarnValue,
                  };
                });
                return {
                  ...mItem,
                  formId: item.medicalFormId,
                  followId: this.templateFormInfo.followId,
                  propertyId: item.id,
                };
              });
            }
          }
          templatePropertiesList.push(info);
        }
      });
      let returnData = {
        [propertiesListName]: templatePropertiesList,
        [formListName]: templateFormsList,
        // templateFormId: templateId || ''
      };
      return returnData;
    },
    // 判断是否有相同的预警规则
    isSameWarnEarlyInfo(isCondition, warnEarlyList) {
      // console.log('warnEarlyList',warnEarlyList)
      let sameIndex = -1,
        emptyIndex = -1;
      if (isCondition == "1") {
        warnEarlyList.forEach((item, index) => {
          let otherWarnEarly = warnEarlyList.map((m) => m);
          otherWarnEarly.splice(index, 1);
          sameIndex = otherWarnEarly.findIndex(
            (f) =>
              f.earlyWarnLevel == item.earlyWarnLevel &&
              f.sexCondition == item.sexCondition &&
              !f.ageConditionType == item.ageConditionType &&
              f.ageEarlyWarnValue == item.ageEarlyWarnValue &&
              f.ageEarlyWarnEnd == item.ageEarlyWarnEnd &&
              f.ageEarlyWarnStart == item.ageEarlyWarnStart &&
              f.conditionType == item.conditionType &&
              f.earlyWarnValue == item.earlyWarnValue &&
              f.earlyWarnStart == item.earlyWarnStart &&
              f.earlyWarnEnd == item.earlyWarnEnd,
          );
          if (sameIndex > -1) this.warnEarlySameIndex = sameIndex;
        });
        emptyIndex = warnEarlyList.findIndex(
          (f) =>
            !f.earlyWarnLevel ||
            !f.sexCondition ||
            !f.ageConditionType ||
            (f.ageConditionType < 6 && !f.ageEarlyWarnValue) ||
            (f.ageConditionType == 6 && (!f.ageEarlyWarnEnd || !f.ageEarlyWarnStart)) ||
            !f.conditionType ||
            (f.conditionType < 6 && !f.earlyWarnValue) ||
            (f.conditionType == 6 && (!f.earlyWarnEnd || !f.earlyWarnStart)),
        );
      } else if (isCondition == "2") {
        emptyIndex = warnEarlyList.findIndex(
          (f) =>
            !f.earlyWarnLevel ||
            !f.conditionType ||
            (f.conditionType < 6 && !f.earlyWarnValue) ||
            (f.conditionType == 6 && (!f.earlyWarnEnd || !f.earlyWarnStart)),
        );

        warnEarlyList.forEach((item, index) => {
          let otherWarnEarly = warnEarlyList.map((m) => m);
          otherWarnEarly.splice(index, 1);
          sameIndex = otherWarnEarly.findIndex(
            (f) =>
              f.earlyWarnLevel == item.earlyWarnLevel &&
              f.earlyWarnValue == item.earlyWarnValue &&
              f.earlyWarnStart == item.earlyWarnStart &&
              f.earlyWarnEnd == item.earlyWarnEnd,
          );
          if (sameIndex > -1) this.warnEarlySameIndex = sameIndex;
        });
      } else {
        warnEarlyList.forEach((item, index) => {
          let otherWarnEarly = warnEarlyList.map((m) => {
            let earlyWarnValue = m.earlyWarnValue;
            if (Array.isArray(earlyWarnValue)) {
              earlyWarnValue.sort((a, b) => {
                return a - b;
              });
              earlyWarnValue = earlyWarnValue.toString();
            }
            return {
              ...m,
              earlyWarnValue: earlyWarnValue,
            };
          });
          item.earlyWarnValue = item.earlyWarnValue.toString();
          otherWarnEarly.splice(index, 1);
          sameIndex = otherWarnEarly.findIndex(
            (f) =>
              f.earlyWarnLevel == item.earlyWarnLevel && f.earlyWarnValue == item.earlyWarnValue,
          );
          if (sameIndex > -1) this.warnEarlySameIndex = sameIndex;
        });
        emptyIndex = warnEarlyList.findIndex(
          (f) => !f.earlyWarnLevel || !f.earlyWarnValue || !f.earlyWarnValue.length,
        );
      }
      return {
        sameIndex,
        emptyIndex,
      };
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
          let attributeJSON = attribute ? JSON.parse(attribute) : attribute;
          if (attributeJSON && attributeJSON.tagType == "province") {
            attributeJSON.options = cityData;
          }
          let info = {
            ...attributeJSON,
            ...itemInfo,
            children: [],
            renderKey: index + "" + new Date().getTime(),
          };
          if (info.span == undefined) info.span = 24;
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

    // ================== 标准拍照项  ==================
    cloneFormComponent(event) {
      let formList = {};
      if (event.libraryId == this.childrenFormProperty.libraryId) {
        formList = this.childrenFormProperty;
      } else {
        formList = event;
      }
      let { attribute, libraryForms, parentId } = formList;
      let allChildren = [];
      let domInfo = {
        ...this.recoverAttribute(attribute),
        libraryForms,
        libraryId: event.libraryId,
        libraryProperties: event.libraryProperties,
        children: [],
      };
      if (event.parentId == 0) {
        let libraryFormList = this.recoverNochildrenForms(libraryForms);
        if (libraryFormList.length) {
          allChildren = libraryFormList;
        }
        allChildren.sort((a, b) => {
          return a.sortNum - b.sortNum;
        });
        domInfo.children = allChildren;
        if (parentId == 0) {
          domInfo.tagType = "hideForm";
        }
      }
      const cloneInfo = this.cloneComponent(domInfo);

      this.activeFormItem(cloneInfo);
      return cloneInfo;
    },
    // 处理基础表单
    recoverNochildrenForms(libraryForms) {
      let list = [];
      if (libraryForms && libraryForms.length) {
        libraryForms.forEach((item, index) => {
          if (item.id) item.id = item.id.toString();
          const { attribute, libraryProperties, ...itemInfo } = item;
          let info = {
            ...JSON.parse(item.attribute),
            ...itemInfo,
            libraryProperties,
            children: [],
            renderKey: index + "" + new Date().getTime(),
          };
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
      let submitData = [...this.drawingList];
      let templateData = this.convertSaveTemplatePrograms(
        JSON.parse(JSON.stringify(submitData)),
        this.$route.query.medicalFormId,
        "templateForms",
        "templateProperties",
      );
      let paramsData = {
        ...this.templateFormInfo,
        templateProperties: templateData.templateProperties,
        templateForms: templateData.templateForms,
      };
      if (this.formType == "3") {
        paramsData.scoresTotalVal = this.convertScoresVal(templateData.templateProperties);
      }
      // return
      if (this.warnEarlySameIndex > -1) return;
      if (this.warnEarlyEmptyIndex > -1) return;
      this.isSaveLoading = true;
      addTemplateFromAttribute(paramsData, this.$route.query.commonFlag)
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
            clearTimeout(this.saveSetTimeout);
          }
        })
        .catch((eror) => {
          this.isSaveLoading = false;
        });
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
    /**
     * =====================================  CRF 编辑所用 end ==========================================
     */
    /**
     * =====================================  随访 编辑所用 start ==========================================
     */
    //
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
    saveFollowupTemplate(event) {
      this.warnEarlySameIndex = -1;
      this.warnEarlyEmptyIndex = -1;
      this.AssembleFormData();
      let submitData = [...this.drawingList];
      let templateData = this.convertSaveTemplatePrograms(
        JSON.parse(JSON.stringify(submitData)),
        this.$route.query.templateId,
        "medicalFormList",
        "medicalProperties",
      );
      let paramsData = {
        ...this.templateFormInfo,
        medicalProperties: templateData.medicalProperties,
        medicalFormList: templateData.medicalFormList,
      };
      //设置拍照必填项为required
      templateData.medicalFormList.forEach((item) => {
        let attribute = JSON.parse(item.attribute);
        item.medicalProperties &&
          item.medicalProperties.forEach((subItem) => {
            // subItem.id = null;
            let subAttribute = JSON.parse(subItem.attribute);
            if (subItem.propertyType == "Upload") {
              subAttribute.required = attribute.required;
              subItem.attribute = JSON.stringify(subAttribute);
            }
          });
      });
      if (this.formType == "3") {
        paramsData.scoresTotalVal = this.convertScoresVal(templateData.medicalProperties);
      }
      if (this.warnEarlySameIndex > -1) return;
      if (this.warnEarlyEmptyIndex > -1) return;
      // return
      this.isSaveLoading = true;
      addFllowupFormAttribute(paramsData)
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
    drawerLogicalChange(event) {
      this.$nextTick(() => {
        this.activeData["relationInfo"] = event;
      });
    },
    // 预警条件规则设置
    warnEarlyValueChange(event) {
      let params = this.$route.params;
      let earlyFormsName = params.templateFormId
        ? "warnTemplateEarlyForms"
        : "warnMedicalEarlyForms";
      this.activeData[earlyFormsName] = [
        {
          ...event,
        },
      ];
      this.drawingList.forEach((item) => {
        if (item.propertyCode == this.activeData.propertyCode) {
          item[earlyFormsName] = [{ ...event }];
        } else if (item.children && item.children.length) {
          item.children = this.convertWarnChildren(item.children, this.activeData, event);
        }
      });
    },
    convertWarnChildren(children, activeData, event) {
      let params = this.$route.params;
      let earlyFormsName = params.templateFormId
        ? "warnTemplateEarlyForms"
        : "warnMedicalEarlyForms";
      children.forEach((item) => {
        if (item.propertyCode == activeData.propertyCode) {
          item[earlyFormsName] = [{ ...event }];
        } else if (item.children && item.children.length) {
          item.children = this.convertWarnChildren(item.children);
        }
      });
      return children;
    },
    tagFormula(flag) {
      if (flag === true) {
        this.formulaDialogVisible = true;
      } else {
        this.formulaDialogVisible = false;
      }
    },
    setCalculationFormula(flag) {
      if (flag === true) {
        this.formulaDialogVisible = true;
      } else {
        this.formulaDialogVisible = false;
      }
    },
    calculationFormulaChange(event) {
      this.formulaDialogVisible = false;
    },
  },
};
</script>
<style lang="scss">
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
  &.entry_first_list_item {
    color: #fff;
    background: rgba(24, 144, 255, 0.8);
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

  .left-board {
    width: 500px;

    .left-scrollbar {
      .el-scrollbar {
        height: calc(100vh - 190px);
        overflow: hidden;
      }

      &.custom_scrollbar {
        height: calc(100vh - 130px);
      }

      .search_content {
        display: flex;
        margin: 10px 10px;

        .el-button {
          padding: 7px 15px;
          margin-left: 5px;
        }
      }

      .entry_list {
        display: flex;
        height: 100%;

        .entry_first_list,
        .entry_second_list {
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

        ::v-deep .hidden_collapse_arrow {
          .el-collapse-item__arrow {
            display: none;
          }
        }
      }

      .photo_list .active_formItem,
      .entry_first_list .active_formItem {
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
    // box-sizing: border-box;
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

      .el-button {
        padding: 12px;
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
      height: calc(100vh - 170px);

      ::v-deep .el-scrollbar__view {
        height: 100%;
      }
    }

    .center-board-row {
      padding: 0 15px 30px;
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

            .is-without-controls {
              width: 120px;
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

            & .drawing-item-copy,
            & .drawing-item-delete,
            & .drawing-item-logic {
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
            // right: 56px;
            border-color: $lighterBlue;
            color: $lighterBlue;
            background: #fff;

            &:hover {
              background: $lighterBlue;
              color: #fff;
            }
          }

          .drawing-item-logic {
            // right: 88px;
            border-color: $lighterBlue;
            color: $lighterBlue;
            background: #fff;

            &:hover {
              background: $lighterBlue;
              color: #fff;
            }
          }

          .drawing-item-delete {
            // right: 24px;
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
