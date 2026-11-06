@ -0,0 +1,1190 @@
<template>
  <div class="app-container option-container">
    <!-- 患者基本信息 -->
    <el-descriptions :column="4" border class="patient_info" label-class-name="patient_label" content-class-name="patient_content">
      <el-descriptions-item label="患者姓名">
        {{ infoPatientInfo.patientName | nullFormat }}
      </el-descriptions-item>
      <el-descriptions-item label="性别">
        {{ infoPatientInfo.sex | convertAgentStr }}
      </el-descriptions-item>
      <el-descriptions-item label="患者ID">
        <!-- {{ infoPatientInfo.personId | nullFormat }} -->
        {{ usrePersonId || nullFormat }}
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">
        {{ infoPatientInfo.createTime | dateFormat }}
      </el-descriptions-item>
    </el-descriptions>
    <!-- 内容开始 -->
    <el-scrollbar class="option-detail-scrollbar" v-loading="loading">
      <el-form label-position="top">
        <div v-for="item in drawingList" :key="item.medicalFormId">
          <h4 class="com-details_title">
            <p style="font-size: 16px">{{ item.medicalName }}</p>
          </h4>
          <el-descriptions class="margin-top medical_form" :labelStyle="widthStyle" :column="1" border>
            <DetailsFollowUpNode
              v-for="(formItem, formIndex) in item.children"
              :key="getDrawingNodeKey(formItem, formIndex)"
              :node="formItem"
              :show-warn-tag="formItem.labelType !== 'form' && item.warnSource"
              @change="valueChange"
            />
          </el-descriptions>
        </div>
      </el-form>
    </el-scrollbar>
    <div class="user_detail_buttons user_detail_buttons_fixed">
      <!-- <el-button type="primary" @click="submitFormInfo(0)">保存</el-button> -->
      <!-- <el-button type="primary" @click="submitEdit">{{ isEditMode ? "取消编辑" : "编辑" }}</el-button> -->
      <el-button type="primary" :disabled="!isEditMode" @click="submitFormInfo(1)">提交</el-button>
      <el-button @click="cancleUserList(false)">返回</el-button>
    </div>
  </div>
</template>

<script>
import {
  workDetails, //详情接口
  selectResearch, //病种及队列下拉框
  saveOrUpdatePatientRecord, // 医生代填随访数据插入接口
} from "@/api/follow/work";
import { previewFllowupCRFData } from "@/api/crf/fllowupCRF";
import cityData from "@/utils/city.json";
import { formConf } from "@/utils/generator/config";
import DraggableItem from "@/views/tool/build/DraggableItem";
import ComponentEcho from "../components/assembly";
import EditForm from "../components/editForm";
import EditItem from "../components/editItem";
import DetailsFollowUpNode from "./DetailsFollowUpNode";
import ElImageViewer from "element-ui/packages/image/src/image-viewer";
import MFormInfo from "@/views/tool/build/previewForm/followUpDetailItem";
import { convertFieldsDataLoop } from "@/utils/index";
import { evaluateFormulaList } from "@/utils/formula";
import Tesseract from "tesseract.js";
import { inputComponents } from "@/utils/generator/config.js";
import { getToken } from "@/utils/auth";
import { deepClone, isEmpty } from "@/utils";
import { data7 } from './data.js'
export default {
  name: "userDetails",
  props: {
    drawerMode: {
      type: Boolean,
      default: false,
    },
    queryParams: {
      type: Object,
      default: () => ({}),
    },
  },
  components: {
    DraggableItem,
    ComponentEcho,
    EditForm,
    DetailsFollowUpNode,
    MFormInfo,
    EditItem,
    ElImageViewer,
  },
  data() {
    return {
      widthStyle: { width: "300px" },
      uploadFileUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 上传的图片服务器地址
      uploadHeaders: {
        Authorization: "Bearer " + getToken(),
      },
      formConf,
      open: false,
      // 遮罩层
      isLoading: true,
      treeOptions: [],
      defaultProps: {
        children: "childrenList",
        label: "nodeName",
      },
      infoData: {},
      size: "medium",
      infoPatientInfo: {},
      infoMedicalInfo: {},
      infoMedicalInfoArray: [],
      appMedicalInfo: {},
      appMedicalInfoArray: [],
      openDialog: false,
      title: null,
      fromContent: "",
      rules: {
        nickName: [{ required: true, message: "用户昵称不能为空", trigger: "blur" }],
      },
      isOpenHistory: false,
      historyList: [],
      researchId: "",
      currentNodeKey: "",
      currentNodeInfo: undefined,
      edcStatus: "",
      patientId: "",
      followId: "",
      recordId: "",
      medicalReplyContent: "",
      medicalFormId: "", //表单id
      listQuestionRecord: [], //EDC 质疑记录
      FllowQuestionRecord: [], //随访质疑记录
      customTagList: [
        "com-BMI",
        "com-TimeNumeral",
        "com-Maternity",
        "com-popQ",
        "com-DValue",
        "com-complication",
        "com-yearMonthDay",
        "com-questionnaire",
      ],
      arrayValTagList: ["el-checkbox-group", "el-cascader"],
      isLoading: false,
      isShowViewer: false,
      drawingList: [], //表单列表
      isLoadingText: false,
      preImageUrlLst: [],
      usrePersonId: "",
      isEditMode: true,
    };
  },
  created() {
    this.initData();
  },
  watch: {
    "$route.query.patientId"(newVal) {
      if (newVal && this.$route.query.edcStatus) {
        this.initData();
      }
    },
  },
  computed: {
    previewImgClass() {
      let sidebarStatus = this.$store.getters.sidebar;
      if (sidebarStatus.opened) {
        return "preview_sideOpen_image";
      } else if (!sidebarStatus.opened) {
        return "preview_sideClose_image";
      }
    },
  },
  methods: {
    getDrawingNodeKey(node, index) {
      return node.renderKey || node.propertyCode || node.formCode || `${node.medicalFormId || "node"}-${index}`;
    },
    walkFieldNodes(list, callback, parentNode = null) {
      if (!Array.isArray(list) || !list.length) {
        return;
      }
      list.forEach((node, index) => {
        callback(node, parentNode, index, list);
        if (node.children && node.children.length) {
          this.walkFieldNodes(node.children, callback, node);
        }
      });
    },
    getVisibleFieldNodes(list) {
      const nodeList = [];
      this.walkFieldNodes(list, (node) => {
        if (node && node.isShowDom) {
          nodeList.push(node);
        }
      });
      return nodeList;
    },
    addMultipleForm(form, event) {
      let fields = form;
      console.log(fields);
      let currentIndex = fields.findIndex((f) => f.formCode == event);
      if (currentIndex > -1) {
        let addFormInfo = deepClone(fields.find((f) => f.formCode == event));
        if (addFormInfo.children && addFormInfo.children.length) {
          addFormInfo.children.forEach((item) => {
            item.propertyCode = item.propertyCode + Math.random().toString(36).substring(2, 5);
          });
        }
        addFormInfo.openMultiple = "0";
        addFormInfo.isDeleteForm = true;
        addFormInfo.formCode = addFormInfo.formCode + Math.random().toString(36).substring(2, 6);
        if (currentIndex > -1) {
          fields.splice(currentIndex + 1, 0, addFormInfo);
        }
        // console.log('addFormInfo', addFormInfo.formCode)
      } else {
        fields.forEach((item, index) => {
          if (item.labelType == "form" && item.children && item.children.length) {
            let childIndex = item.children.findIndex((f) => f.formCode == event);
            if (childIndex > -1) {
              item.children = this.addChildrenMultiple(event, item.children);
            }
          }
        });
      }
      form = fields;
    },
    addChildrenMultiple(event, children) {
      let childIndex = children.findIndex((f) => f.formCode == event);
      let addFormInfo = deepClone(children.find((f) => f.formCode == event));
      if (addFormInfo.children && addFormInfo.children.length) {
        addFormInfo.children.forEach((item) => {
          item.propertyCode = item.propertyCode + Math.random().toString(36).substring(2, 5);
        });
      }
      addFormInfo.openMultiple = "0";
      addFormInfo.isDeleteForm = true;
      addFormInfo.formCode = addFormInfo.formCode + Math.random().toString(36).substring(2, 6);
      children.splice(childIndex + 1, 0, addFormInfo);
      return children;
    },
    deleteFormMultiple(form, event) {
      let fields = form;
      let currentIndex = fields.findIndex((f) => f.formCode == event);
      if (currentIndex > -1) {
        fields.splice(currentIndex, 1);
      } else {
        fields.forEach((item, index) => {
          if (item.children && item.children.length) {
            let childIndex = item.children.findIndex((f) => f.formCode == event);
            if (childIndex > -1) {
              item.children.splice(childIndex, 1);
            }
          }
        });
      }
      form = fields;
    },
    /** 关闭按钮 */
    cancleUserList() {
      if (this.drawerMode) {
        this.$emit("close");
        return;
      }
      const obj = {
        path: this.$route.query.oldPath,
      };
      this.$tab.closeOpenPage(obj);
    },
    initData() {
      const queryData = this.drawerMode ? this.queryParams : this.$route.query;
      this.nodeTypeStatus = queryData.nodeTypeStatus;
      this.patientId = queryData.patientId;
      this.detailsType = queryData.type;
      this.recordId = queryData.recordId;
      this.usrePersonId = queryData.personId || this.patientId;
      this.getDetails();
    },
    getDetails() {
      this.loading = true;
      this.drawingList = [];
      let dataObject = {
        patientId: this.patientId,
        recordId: this.recordId,
        detailType: this.detailsType,
      };
      workDetails(dataObject).then((response) => {
        let objData = response.data;
        // let objData = data7;

        const { medicalFormVOS, medicalFormExamVOS, ...otherObjData } = objData;
        //用户基本信息
        this.infoPatientInfo = {
          ...otherObjData,
        };
        //封装表单
        let medicalFormVOSList =
          medicalFormVOS && medicalFormVOS.length
            ? medicalFormVOS.map((m) => {
                return {
                  ...m,
                  checked: false,
                };
              })
            : [];
        if (medicalFormExamVOS && medicalFormExamVOS.length) {
          let examVos = medicalFormExamVOS.map((m) => {
            return {
              ...m,
              propertyVOS: m.propertyExamVOS,
              checked: false,
            };
          });
          medicalFormVOSList = medicalFormVOSList.concat(examVos);
        }
        //表单容器
        if (medicalFormVOSList && medicalFormVOSList.length > 0) {
          let fields = [];
          fields = medicalFormVOSList.map((m, index) => {
            const { attribute, ...otherInfo } = m;
            let attributeJson = attribute
              ? {
                  ...JSON.parse(attribute),
                }
              : null;
            let info = {
              ...attributeJson,
              ...otherInfo,
              medicalFormId: m.medicalFormId,
              medicalFormType: m.medicalFormType,
              formName: m.medicalName,
              medicalName: m.medicalName,
              formCode: m.medicalCode,
              dynamic: m.dynamic,
              renderKey: new Date().getTime() + "" + index,
              children: this.convartMedicalList(m),
            };
            if (m.id) info.id = m.id.toString();
            return info;
          });
          fields.sort((a, b) => {
            return a.medicalFormId - b.medicalFormId;
          });
          this.drawingList = fields;
          //设置初始化显示与隐藏
          if (this.drawingList?.length > 0) {
            this.drawingList.forEach((item) => {
              if (item.children?.length > 0) {
                this.convertPreviewFormModel(item.children);
              }
            });
          }
          this.setFormEditState(true);
          console.log(this.drawingList);
          this.loading = false;
        } else {
          this.loading = false;
        }
      });
    },
    //设置表单是否显示隐藏
    convertPreviewFormModel(formFiledList) {
      let formObjArr = {};
      if (formFiledList && formFiledList.length) {
        let formList = convertFieldsDataLoop(formFiledList);
        this.walkFieldNodes(formFiledList, (item) => {
          item.isShowDom = this.convertIsShowDom(item, formList);
          if (!item.isShowDom) {
            if (Array.isArray(item.defaultValue)) {
              item.defaultValue = [];
              if (item.options) {
                item.options.forEach((optItem) => {
                  optItem.disabled = false;
                });
              }
            } else if (item.labelType !== "form") {
              item.defaultValue = undefined;
            }
          }
          if (item.labelType == "CaseSpecification") {
            item.description = this.convertItemDescription(item, formFiledList);
          }
          if (item.labelType !== "form") {
            item.formScores = this.convertItemFormScores(item) || 0;
            if (item.labelType == "Statistic" && item.formulaList && item.formulaList.length) {
              item.defaultValue = this.convertPropertyScoreVal(item, formFiledList);
            }
            formObjArr[item.propertyCode] = item.defaultValue;
          }
        });
        // 计算多个分数统计，且当前分数值里有上面的分数统计参与运算
        let hasFormulaListArr = this.getVisibleFieldNodes(formFiledList).filter((f) => f.formulaList && f.formulaList.length);
        if (hasFormulaListArr && hasFormulaListArr.length) {
          hasFormulaListArr.forEach((hasItem, index) => {
            this.walkFieldNodes(formFiledList, (forItem) => {
              if (forItem.labelType == "form") {
                return;
              }
              forItem.formScores = this.convertItemFormScores(forItem) || 0;
              if (forItem.formulaList && forItem.formulaList.length) {
                forItem.defaultValue = this.convertPropertyScoreVal(forItem, formFiledList);
              }
            });
          });
        }
      }
      // console.log('formObjArr', formObjArr)
      return formObjArr;
    },
    // 计算分数
    convertItemFormScores(item) {
      let formScores = undefined;
      if (item.labelType == "Checkbox" && item.defaultValue.length) {
        let scoresNum = [];
        item.defaultValue.forEach((dealItem) => {
          scoresNum.push(item.options.find((f) => f.value == dealItem).itemScores);
        });
        formScores = this.mathSum(scoresNum);
      } else if (item.labelType == "Radio" || item.labelType == "Select") {
        formScores =
          item.defaultValue && item.options && item.options.length && item.options.find((f) => f.value == item.defaultValue)
            ? item.options.find((f) => f.value == item.defaultValue).itemScores
            : 0;
      } else if (item.labelType == "Checkbox" && !item.defaultValue.length) {
        formScores = 0;
      } else if (item.labelType == "Input" && item.regValue && item.regValue.value == "number") {
        formScores = Number(item.defaultValue) != NaN ? Number(item.defaultValue) : 0;
      } else if (item.labelType == "Statistic") {
        formScores = item.defaultValue || 0;
      } else if (item.labelType == "CaseSpecification") {
        formScores = item.description;
      }
      return formScores;
    },
    mathSum(arr) {
      return arr.reduce(function (total, value) {
        return total + value;
      }, 0);
    },
    roundNumber(number, decimals) {
      const factor = 10 ** decimals;
      return Math.round(number * factor) / factor;
    },
    convertPropertyScoreVal(item, formFiledList) {
      const formulaList = Array.isArray(item.formulaList) ? item.formulaList : [];
      if (!formulaList.length) {
        return 0;
      }

      const formList = convertFieldsDataLoop(formFiledList);

      try {
        const scoreVal = evaluateFormulaList(formulaList, formList, {
          getFieldScore: (field) => this.convertItemFormScores(field),
        });
        const precision = item.attribute?.precision || item.decimalPlaces || 0;
        return precision && !isNaN(Number(precision)) ? this.roundNumber(scoreVal, Number(precision)) : scoreVal;
      } catch (e) {
        console.error("Error in convertPropertyScoreVal:", e);
        return 0;
      }
    },
    normalizePreviewProperty(info, source = {}) {
      const imageUrls = Array.isArray(source.imageUrls)
        ? source.imageUrls.filter((item) => !!item)
        : [];
      if (info.tag === "el-upload" && imageUrls.length) {
        info.imageUrls = imageUrls;
        info.fileList = imageUrls.map((url, index) => ({
          uid: `init-${source.id || source.propertyCode || index}`,
          name: `${index}`,
          url,
        }));
        if (!info.defaultValue || !info.defaultValue.length) {
          info.defaultValue = imageUrls;
        }
      }
      return info;
    },
    // 处理基础表单
    convartMedicalList(dataInfo) {
      let formAttribute = inputComponents.find((f) => f.labelType == "form");
      let medicalFormData = dataInfo.childMedicalFormVOS;
      let medicalPropertiesData = dataInfo.propertyVOS || dataInfo.propertyExamVOS;
      let childFormList = [];
      let propertyList = [];
      if (medicalFormData && medicalFormData.length) {
        childFormList = medicalFormData.map((m, index) => {
          const { attribute, ...otherInfo } = m;
          let attributeJson = {};
          try {
            attributeJson = attribute ? JSON.parse(attribute) : formAttribute;
          } catch (e) {
            console.error("Failed to parse attribute:", e, "for:", m);
            attributeJson = formAttribute;
          }
          let propertySource = m.propertyVOS || m.propertyExamVOS || [];
          let info = {};
          if (m.formType == 2 && m.openImage == "1") {
            let propertyAttribute = {};
            try {
              if (propertySource.length && propertySource[0].attribute) {
                propertyAttribute = JSON.parse(propertySource[0].attribute);
              }
            } catch (e) {
              console.error("Failed to parse propertyVOS attribute:", e, "for:", m);
            }
            info = {
              ...propertyAttribute,
              ...(propertySource.length ? propertySource[0] : {}),
              ...otherInfo,
              medicalFormId: m.medicalFormId,
              formName: m.medicalName,
              propertyName: m.medicalName,
              medicalName: m.medicalName,
              id: propertySource.length ? propertySource[0].id : "",
              formCode: m.medicalCode,
              renderKey: new Date().getTime() + "" + index,
              children: null,
            };
            info = this.normalizePreviewProperty(
              info,
              propertySource.length ? propertySource[0] : {}
            );
          } else {
            info = {
              ...attributeJson,
              ...otherInfo,
              medicalFormId: m.medicalFormId,
              formName: m.medicalName,
              propertyName: m.medicalName,
              medicalName: m.medicalName,
              id: m.id,
              formCode: m.medicalCode,
              renderKey: new Date().getTime() + "" + index,
              children: m.formType == "2" ? null : this.convartMedicalList(m),
            };
          }
          if (m.id) info.id = m.id.toString();
          return info;
        });
      }
      if (medicalPropertiesData && medicalPropertiesData.length) {
        propertyList = medicalPropertiesData.map((m, index) => {
          if (m.id) m.id = m.id.toString();
          const { attribute, propertyVOS, ...mInfo } = m;
          let attributeObj = {};
          try {
            attributeObj = attribute ? JSON.parse(attribute) : {};
          } catch (e) {
            console.error("Failed to parse attribute:", e, "for:", m);
          }
          return this.normalizePreviewProperty({
            ...attributeObj,
            renderKey: new Date().getTime() + "" + index,
            ...mInfo,
          }, m);
        });
      }
      return childFormList.concat(propertyList);
    },
    // 是否显示
    convertIsShowDom(item, fields) {
      let fieldsData = convertFieldsDataLoop(fields);
      let isShowDom = false;
      if (item.relationInfo && item.relationInfo.relationQuestion.length) {
        let relationQuestoinList = item.relationInfo.relationQuestion;
        relationQuestoinList.forEach((itemR, indexR) => {
          let fieldsItem = fieldsData.find((f) => f.propertyCode == itemR.questionId);
          let aggregate = [];
          let valueList = [];
          if (fieldsItem) {
            if (Array.isArray(fieldsItem.defaultValue) && fieldsItem.defaultValue.length) {
              valueList = fieldsItem.defaultValue;
            } else if (
              fieldsItem.defaultValue !== undefined &&
              fieldsItem.defaultValue !== null &&
              fieldsItem.defaultValue !== ""
            ) {
              valueList = fieldsItem.defaultValue;
            } else {
              valueList = fieldsItem.propertyValue || [];
            }
          }
          if (
            fieldsItem &&
            (fieldsItem.tag == "el-radio-group" || fieldsItem.tag == "el-checkbox-group" || fieldsItem.tag == "el-select")
          ) {
            if (!Array.isArray(valueList)) {
              valueList = [valueList];
            }

            if (itemR.checkedOptions && itemR.checkedOptions.length) {
              itemR.checkedOptions.forEach((checkItem, checkIndex) => {
                let checkItemString = checkItem.toString();
                if (valueList.indexOf(checkItemString) > -1 || valueList.findIndex((f) => f == checkItem) > -1) {
                  aggregate.push(checkIndex);
                }
              });
            }
          } else if (fieldsItem && fieldsItem.tag == "el-switch") {
            if (itemR.checkedOptions && itemR.checkedOptions.length) {
              itemR.checkedOptions.forEach((checkItem, checkIndex) => {
                if (valueList.indexOf(checkItem) > -1) {
                  aggregate.push(checkIndex);
                }
              });
            }
          } else if (
            fieldsItem &&
            ((fieldsItem.defaultValue !== undefined && fieldsItem.defaultValue !== null && fieldsItem.defaultValue !== "") ||
              fieldsItem.propertyValue)
          ) {
            aggregate.push(true);
          }
          if (aggregate.length) {
            isShowDom = true;
          } else if (
            fieldsItem &&
            fieldsItem.tag == "el-upload" &&
            ((fieldsItem.defaultValue && fieldsItem.defaultValue.length) ||
              (fieldsItem.propertyValue && fieldsItem.propertyValue.length))
          ) {
            isShowDom = true;
          } else {
            isShowDom = false;
          }
        });
      } else {
        isShowDom = true;
      }
      return isShowDom;
    },
    /**
     * 提交
     */
    async submitFormInfo(tip) {
      // if (!this.isEditMode) {
      //   this.$message.warning("请先点击编辑后再提交");
      //   return;
      // }
      let isSaveValue = true;
      let valueVos = [];
      let isSaveItem = undefined;
      console.log(this.drawingList);

      if (this.drawingList.length > 0) {
        this.drawingList.forEach((formItem) => {
          this.walkFieldNodes(formItem.children, (item) => {
            if (
              !isSaveItem &&
              item.labelType !== "form" &&
              item.labelType !== "Caption" &&
              item.isShowDom &&
              item.required &&
              isEmpty(item.defaultValue)
            ) {
              isSaveValue = false;
              isSaveItem = item;
            }
          });
        });
        if (tip) {
          if (!isSaveValue) {
            this.$message.warning(`${isSaveItem.propertyName}未填写内容`);
            return;
          }
          try {
            await this.$confirm("确认提交当前随访表单吗？", "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
            });
          } catch (error) {
            return;
          }
        }
        let data = this.convertTemplateProperty(tip);

        // 收集分数统计结果
        let statisticResults = this.collectStatisticResults();

        data.selfAssessmentForms = statisticResults;

        console.log(data);
        const response = await saveOrUpdatePatientRecord(data);
        let res = response.data;
        if (res.code == 200 && res.msg == "操作成功") {
          if (tip == 0) {
            this.$message.success("保存成功");
          }
          if (tip == 1) {
            this.$message.success("提交成功");
            if (this.drawerMode) {
              this.$emit("submitted");
              return;
            }
            // 返回随访工作站
            this.cancleUserList(true);
          }
        }
        //封装数据
      } else {
        console.log("暂无配置表单");
      }

      //校验必填项
      //封装数据
    },
    // 提交数据封装
    convertTemplateProperty(tip) {
      let data = {
        recordId: this.recordId,
        isSubmit: tip, //0保存 1提交
      };
      let formArray = []; //表单数据
      let imageUrlsArray = []; //图片数据
      for (const section of this.drawingList) {
        section.children.forEach((row, index) => {
          this.walkFieldNodes([row], (node, parentNode) => {
            if (node.labelType == "form") {
              return;
            }
            node.dynamic = section.dynamic;
            if (parentNode && parentNode.labelType == "form") {
              node.number = index + 1;
            }
            if (node.labelType == "Upload") {
              const imageDict = this.getUpdateData(node);
              imageDict && imageUrlsArray.push(imageDict);
              return;
            }
            const formDict = this.getDefaultData(node);
            if (formDict) {
              formArray.push(formDict);
            }
          });
        });
      }
      //   data["propertyFormsJson"] = formArray
      //   data["imageUrlsJson"] = imageUrlsArray
      data["propertyFormsJson"] = JSON.stringify(formArray);
      data["imageUrlsJson"] = JSON.stringify(imageUrlsArray);
      return data;
    },
    getDefaultData(item) {
      let propertyValue = item.defaultValue || item.propertyValue;
      if (Array.isArray(propertyValue)) {
        propertyValue = propertyValue.toString();
      } else if (!propertyValue) {
        propertyValue = "";
      }
      if (!item.isShowDom) {
        return;
      }
      let formDict = {
        propertyType: item.propertyType,
        propertyId: item.id,
        propertyValue: propertyValue,
        medicalFormId: item.medicalFormId,
        medicalFormType: item.medicalFormType,
        propertyCode: item.propertyCode,
        tagType: item.tagType,
        dynamic: item.dynamic,
        number: item.number,
        other: item.other,
      };
      switch (item.labelType) {
        case "Radio":
          item.options.forEach((itm) => {
            if (itm.isShowInputValue && itm.inputAttr && propertyValue == itm.value) {
              formDict["other"] = itm.inputVal;
            }
          });
          break;
        case "Checkbox":
          let arrObj = item.defaultValue != null ? item.defaultValue : [];
          let otherArr = [];
          arrObj.forEach((itm) => {
            let checkOptions = item.options.find((f) => f.value == itm && f.isShowInputValue);
            // 添加 checkOptions 判断否则报错 isShowInputValue 属性不存在！！！
            if (checkOptions) {
              otherArr.push(checkOptions);
            }
          });
          if (otherArr.length) {
            formDict["other"] = otherArr
              .map((m) => {
                if (m.isShowInputValue && m.inputAttr) {
                  return m.inputVal;
                }
              })
              .join(",");
          }
          break;
        case "BMI":
        case "WHR":
          item.propertyValue = item.propertyValue || [];
          break;
        case "":
        default:
          break;
      }
      if (item.tag == "com-yearMonthDay") {
        item["specialComponentType"] = item.specialComponentType;
        item["propertyValue"] = item.propertyValue;
      }
      return formDict;
    },
    getUpdateData(item) {
      if (!item.isShowDom) {
        return;
      }
      return {
        medicalFormId: item.medicalFormId,
        propertyId: item.id,
        imageUrl: this.getUpdateImageUrls(item),
      };
    },
    getUpdateImageUrls(item) {
      let imageUrls = [];
      for (const image of item.fileList) {
        if (image.thumbUrl) {
          imageUrls.push(image.thumbUrl);
        } else if (image.url) {
          imageUrls.push(image.url);
        }
      }
      return imageUrls;
    },

    // 收集分数统计结果
    collectStatisticResults() {
      let statisticResults = [];
      this.drawingList.forEach((formItem) => {
        this.walkFieldNodes(formItem.children, (item) => {
          if (item.labelType === "Statistic" && !isEmpty(item.defaultValue)) {
            statisticResults.push({
              medicalFormId: item.medicalFormId,
              propertyId: item.id,
              code: item.propertyCode,
              name: item.propertyName,
              scores: item.defaultValue,
            });
          }
        });
      });
      return statisticResults;
    },

    valueChange(event) {
      console.log(event);
      let defaultVal = event.value;
      let propertyVal = event.value;
      let info = event.item;
      if (this.customTagList.findIndex((f) => f == event.item.tag) > -1 && defaultVal) {
        defaultVal = JSON.parse(event.value);
      }
      if ((event.item.tag == "el-input" || event.item.tag == "el-input-number") && event.item.unitVal) {
        let jsonStr = JSON.parse(event.value);
        defaultVal = Number(jsonStr.content);
      }
      if (this.arrayValTagList.findIndex((f) => f == event.item.tag) > -1 && defaultVal) {
        propertyVal = event.value.toString();
      }
      event.item.defaultValue = defaultVal;
      event.item.propertyValue = defaultVal;
      this.drawingList.forEach((parentItem) => {
        if (parentItem.children?.length) {
          let medicalInfoArray = parentItem.children;
          parentItem.children = medicalInfoArray;
          this.convertPreviewFormModel(parentItem.children);
        }
      });
      console.log(this.drawingList);
    },

    submitEdit() {
      if (!this.isEditMode) {
        this.isEditMode = true;
        this.setFormEditState(true);
        return;
      }
      this.$confirm("确认取消编辑吗？当前已填写内容会保留。", "提示", {
        type: "warning",
      })
        .then(() => {
          this.isEditMode = false;
          this.setFormEditState(false);
        })
        .catch(() => {});
    },
    setFormEditState(isEditMode) {
      if (!this.drawingList || !this.drawingList.length) {
        return;
      }
      this.drawingList.forEach((item) => {
        if (item.children && item.children.length) {
          this.updateFormItemDisabled(item.children, !isEditMode);
        }
      });
    },
    updateFormItemDisabled(list, disabled) {
      if (!list || !list.length) {
        return;
      }
      list.forEach((item) => {
        this.$set(item, "disabled", disabled);
        if (item.children && item.children.length) {
          this.updateFormItemDisabled(item.children, disabled);
        }
      });
    }
  },
};
</script>
<style>
.custom_tip_popover .el-popover__title {
  color: #fff !important;
}
</style>
<style lang="scss" scoped>
.preview_sideClose_image {
  position: absolute;
  right: 43%;
}

.preview_sideOpen_image {
  position: absolute;
  right: 37%;
  overflow: hidden;
}
.display_flex {
  display: flex;
}

.deleteBtn {
  color: #ff9292;
}
::v-deep .patient_info {
  padding: 15px;
  background: rgba(24, 144, 255, 0.05);
  .patient_label {
    color: #333;
    font-weight: bold;
    background: rgba(24, 144, 255, 0.05);
    width: 100px;
  }
  .patient_content {
    background: rgba(24, 144, 255, 0.05);
  }
}
::v-deep .medical_form {
  .required {
    content: "*";
    color: #f56c6c;
  }
  .patient_label {
    color: #333;
    color: #333;
    font-weight: bold;
    background: rgba(24, 144, 255, 0.05);
  }
}
.option-container {
  height: calc(100vh - 66px);
  color: #333;
  position: relative;
  padding-bottom: 88px;

  .com-details_title {
    .el-checkbox {
      display: flex;
      align-items: center;
    }
    ::v-deep.el-checkbox__label {
      color: #6379bb;
      font-weight: bold;
      padding-left: 0;
    }
  }
}

.option-detail-scrollbar {
  height: calc(100% - 65px);
}

/** 表单布局 **/
.user_follow_opinion {
  min-height: 100px;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 5px 10px;
}

.adjustType_radio_group {
  height: 45px;
  display: flex;
  align-items: center;
}

.el-form-top {
  .el-form-item {
    margin-top: 22px;
  }
}

.el-descriptions__header {
  justify-content: start;
}

.box-card {
  .item {
    line-height: 1.5;
    font-size: 14px;
  }
}

.childMedicalFormVOS_property {
  display: flex;
}

.el-rate {
  height: 36px;
  line-height: 36px;

  .el-rate__icon {
    font-size: 24px;
    margin-top: 6px;
  }
}

/* 强制居中弹窗 */
.el-dialog {
  display: flex;
  display: -ms-flex;
  /* 兼容IE */
  flex-direction: column;
  -ms-flex-direction: column;
  /* 兼容IE */
  margin: 0 !important;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  max-height: calc(100% - 30px);
  max-width: calc(100% - 30px);
}

.el-dialog .el-dialog__body {
  max-height: 100%;
  flex: 1;
  -ms-flex: 1 1 auto;
  /* 兼容IE */
  overflow-y: auto;
  overflow-x: hidden;
}

.el-dialog__wrapper {
  /*隐藏ie和edge中遮罩的滚动条*/
  overflow: hidden;
}

.earlyBox {
  display: block;
  overflow: hidden;
  padding: 20px 20px;
  border: 1px solid #f5f5f5;

  .warn_earlyList {
    .warn_early_item {
      display: flex;
      flex-direction: row;
      padding: 10px 0;

      .head_img {
        width: 50px;
        height: 50px;
        border-radius: 50%;
      }

      .warn_guide_info {
        margin-left: 15px;
        margin-right: 0;

        .span {
          font-size: 16px;
          font-weight: bold;
        }

        .em {
          font-size: 12px;
          color: #666;
          padding: 0 10px;
        }

        .i {
          font-size: 12px;
          padding: 5px 10px;
          color: #fff;
          background: #409eff;
          border-radius: 5px;
        }

        .center {
          display: block;
          padding: 8px 10px;
          font-size: 14px;
          margin-top: 10px;
          border: 1px solid #efefef;
          border-radius: 5px;

          .el-image {
            width: 200px;
          }
        }
      }

      .flex_end {
        display: flex;
        flex-direction: row-reverse;
      }

      .flex_start {
        flex-direction: row-reverse;
      }
    }

    .early_item_doctor {
      flex-direction: row-reverse;

      .warn_guide_info {
        margin-right: 15px;
        margin-left: 0;
      }
    }
  }
}

.user_detail_buttons {
  margin: 20px 0;
  display: flex;
  justify-content: center;
}

.user_detail_buttons_fixed {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 10;
  margin: 0;
  padding: 16px 24px;
  background: #fff;
  border-top: 1px solid #e5e7eb;
  box-shadow: 0 -2px 12px rgba(0, 0, 0, 0.08);
}

.dialog_footer {
  margin-bottom: 10px;
}

.dialog_cascader {
  width: 100%;
}

.el-input .el-button {
  background: #1890ff;
  border-color: #1890ff;
  color: #fff;
}

.ignore_content {
  font-size: 13px;
  width: 300px;
  margin: 0 auto;

  .ignore_tips {
    text-align: center;
    margin-bottom: 5px;
  }

  .ignore_tit {
    line-height: 40px;
    font-size: 13px;
  }
}
</style>
