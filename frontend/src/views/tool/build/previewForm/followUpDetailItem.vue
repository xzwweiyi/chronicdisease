<template>
  <div class="preview_item">
    <template v-if="info.tag == 'el-input'">
      <el-input
        v-model="inputValue"
        :type="info.type || ''"
        :maxlength="info.maxlength || ''"
        @input="inputBlur"
        @change="valueChange($event, info)"
        :autosize="{
          minRows: info.autosize && info.autosize.minRows,
          maxRows: info.autosize && info.autosize.maxRows,
        }"
        :show-word-limit="info['show-word-limit'] || ''"
        :readonly="info.readonly"
        :prefix-icon="info['prefix-icon']"
        :suffix-icon="info['suffix-icon']"
        :min="info.min"
        :max="info.max"
        :placeholder="info.placeholder"
        :clearable="info.clearable"
        :disabled="info.disabled"
      >
        <template slot="prepend" v-if="info.prepend">{{ info.prepend }}</template>
        <template slot="append" v-if="info.append">{{ info.append }}</template>
      </el-input>
      <span class="unit_info" v-if="info.unit">{{ info.unit }}</span>
      <el-select class="multiple_unit" v-else-if="info.isShowUnit && info.isSetMultipleUnit" v-model="info.unitVal" :disabled="info.disabled">
        <el-option v-for="(item, index) in info.unitList" :key="index" :label="item.label" :value="item.value"></el-option>
      </el-select>
    </template>
    <template v-else-if="info.tag == 'el-button'">
      <el-button :type="info.type" :icon="info.icon" :disabled="info.disabled"></el-button>
    </template>
    <template v-else-if="info.tag == 'el-input-number'">
      <el-input-number
        v-model="info.defaultValue"
        :type="info.type"
        :icon="info.icon"
        :disabled="info.disabled"
        :placeholder="info.placeholder"
        :controls="info.controls"
        :step="info.step"
        @change="valueChange($event, info)"
        :step-strictly="info['step-strictly']"
        :precision="info.precision"
        :controls-position="info['controls-position']"
        :min="info.min"
        :max="info.max"
      ></el-input-number>
      <span class="unit_info" v-if="info.unit">{{ info.unit }}</span>
      <el-select class="multiple_unit" v-else-if="info.isShowUnit && info.isSetMultipleUnit" v-model="info.unitVal" :disabled="info.disabled">
        <el-option v-for="(item, index) in info.unitList" :key="index" :label="item.label" :value="item.value"></el-option>
      </el-select>
    </template>
    <template v-else-if="info.tag == 'el-select'">
      <el-select
        v-model="info.defaultValue"
        :disabled="info.disabled"
        :clearable="info.clearable"
        @change="valueChange($event, info)"
        :placeholder="info.placeholder"
      >
        <el-option
          v-for="(item, index) in info.options"
          :key="index"
          :value="item.value"
          :label="item.itemScores ? item.label + '（' + item.itemScores + '分）' : item.label"
          :disabled="item.disabled"
        ></el-option>
        <!-- <el-option v-for="(item, index) in info.options" :key="index" :value="item.value" :label="item.label"
                  :disabled="item.disabled">{{ item.label }}{{item.itemScores?'（' + item.itemScores + '分）':''}}</el-option> -->
      </el-select>
    </template>
    <template v-else-if="info.tag == 'el-radio-group'">
      <el-radio-group v-model="info.defaultValue" :disabled="info.disabled" @input="radioGroupChange($event, info)">
        <div class="group_custom_item" v-for="(item, index) in info.options" :key="index">
          <el-radio :label="normalizeRadioValue(item.value)" :disabled="item.disabled" :border="info.border"
            >{{ item.label }}{{ item.itemScores ? "（" + item.itemScores + "分）" : "" }}</el-radio
          >
          <div class="group_input" v-if="item.inputAttr && item.isShowInputValue && normalizeRadioValue(info.defaultValue) == normalizeRadioValue(item.value)">
            <p class="radio_text" v-if="item.inputAttr.inputPrefixVal">
              {{ item.inputAttr.inputPrefixVal }}
            </p>
            <el-input
              class="option_input"
              v-if="item.inputAttr.inputType != 'number'"
              v-model="item.inputVal"
              :disabled="info.disabled"
              :maxlength="(item.inputAttr && item.inputAttr.maxlength) || undefined"
              :minlength="(item.inputAttr && item.inputAttr.minlength) || undefined"
              :placeholder="item.inputAttr && item.inputAttr.placeholder"
            />
            <el-input-number
              v-if="item.inputAttr.inputType == 'number'"
              :controls="false"
              v-model="item.inputVal"
              :disabled="info.disabled"
              :max="(item.inputAttr && item.inputAttr.max) || undefined"
              :min="(item.inputAttr && item.inputAttr.min) || undefined"
              :placeholder="item.inputAttr && item.inputAttr.placeholder"
              :step="item.inputAttr.step"
              :precision="item.inputAttr.precision"
            />
            <p class="radio_text" v-if="item.inputAttr.inputSuffixVal">
              {{ item.inputAttr.inputSuffixVal }}
            </p>
          </div>
          <p class="dispose_explain" v-if="item.disposeExplain" v-html="item.disposeExplain"></p>
        </div>
      </el-radio-group>
    </template>
    <template v-else-if="info.tag == 'el-checkbox-group'">
      <el-checkbox-group v-model="info.defaultValue" :disabled="info.disabled" @input="checkboxChange">
        <div class="group_custom_item" v-for="(item, index) in info.options" :key="index + renderKey">
          <el-checkbox :label="item.value" :disabled="item.disabled" :border="info.border"
            >{{ item.label }}{{ item.itemScores ? "（" + item.itemScores + "分）" : "" }}</el-checkbox
          >
          <div class="group_input" v-if="item.isShowInputValue && info.defaultValue.findIndex((f) => f == item.value) > -1">
            <p class="radio_text" v-if="item.inputAttr.inputPrefixVal">
              {{ item.inputAttr.inputPrefixVal }}
            </p>
            <el-input
              class="option_input"
              v-if="item.inputAttr.inputType != 'number'"
              v-model="item.inputVal"
              :disabled="info.disabled"
              :maxlength="(item.inputAttr && item.inputAttr.maxlength) || undefined"
              :minlength="(item.inputAttr && item.inputAttr.minlength) || undefined"
              :placeholder="item.inputAttr && item.inputAttr.placeholder"
            />
            <el-input-number
              v-if="item.inputAttr.inputType == 'number'"
              :controls="false"
              v-model="item.inputVal"
              :disabled="info.disabled"
              :max="(item.inputAttr && item.inputAttr.max) || undefined"
              :min="(item.inputAttr && item.inputAttr.min) || undefined"
              :placeholder="item.inputAttr && item.inputAttr.placeholder"
              :step="item.inputAttr.step"
              :precision="item.inputAttr.precision"
            />
            <p class="radio_text" v-if="item.inputAttr.inputSuffixVal">
              {{ item.inputAttr.inputSuffixVal }}
            </p>
          </div>
          <p class="dispose_explain" v-if="item.disposeExplain" v-html="item.disposeExplain"></p>
        </div>
      </el-checkbox-group>
    </template>
    <template v-else-if="info.tag == 'el-cascader'">
      <el-cascader
        v-model="info.defaultValue"
        :disabled="info.disabled"
        :options="info.options"
        :placeholder="info.placeholder"
        :props="info.props.props"
        :show-all-levels="info['show-all-levels']"
        :filterable="info.filterable"
        :separator="info.separator || '/'"
        :clearable="info.clearable"
        @change="valueChange($event, info)"
      ></el-cascader>
    </template>
    <template v-else-if="info.tag == 'el-switch'">
      <el-switch
        v-model="info.defaultValue"
        :disabled="info.disabled"
        :active-text="info['active-text']"
        :inactive-text="info['inactive-text']"
        :active-color="info['active-color']"
        :inactive-color="info['inactive-color']"
        :active-value="info['active-value']"
        :inactive-value="info['inactive-value']"
        @change="valueChange($event, info)"
      ></el-switch>
    </template>
    <template v-else-if="info.tag == 'el-slider'">
      <el-slider
        v-model="info.defaultValue"
        :disabled="info.disabled"
        :min="info.min"
        :max="info.max"
        :step="info.step"
        :range="info.range"
        :show-stops="info['show-stops']"
        :marks="marks"
        @change="valueChange($event, info)"
      ></el-slider>
    </template>
    <template v-else-if="info.tag == 'el-time-picker'">
      <el-time-picker
        v-model="info.defaultValue"
        :disabled="info.disabled"
        :clearable="info.clearable"
        :start-placeholder="info['start-placeholder']"
        :end-placeholder="info['end-placeholder']"
        :range-separator="info['range-separator']"
        :is-range="info['is-range']"
        :format="info.format"
        :value-format="info['value-format']"
        :picker-options="JSON.stringify(info['picker-options'])"
        :placeholder="info.placeholder"
        @change="valueChange($event, info)"
      ></el-time-picker>
    </template>
    <template v-else-if="info.tag == 'el-date-picker'">
      <el-date-picker
        v-model="info.defaultValue"
        :disabled="info.disabled"
        :type="info.type"
        :placeholder="info.placeholder"
        :start-placeholder="info['start-placeholder']"
        :end-placeholder="info['end-placeholder']"
        :range-separator="info['range-separator']"
        :is-range="info['is-range']"
        :format="info.format"
        :value-format="info['value-format']"
        :picker-options="JSON.stringify(info['picker-options'])"
        :readonly="info.readonly"
        :clearable="info.clearable"
        @change="valueChange($event, info)"
      ></el-date-picker>
    </template>
    <template v-else-if="info.tag == 'el-rate'">
      <el-rate
        v-model="info.defaultValue"
        :disabled="info.disabled"
        :max="info.max"
        :allow-half="info['allow-half']"
        :show-text="info['show-text']"
        :show-score="info['show-score']"
        :readonly="info.readonly"
        :clearable="info.clearable"
        @change="valueChange($event, info)"
      ></el-rate>
    </template>
    <template v-else-if="info.tag == 'el-color-picker'">
      <el-color-picker
        v-model="info.defaultValue"
        :disabled="info.disabled"
        :show-alpha="info['show-alpha']"
        :color-format="info['color-format']"
        @change="valueChange($event, info)"
      ></el-color-picker>
    </template>
    <template v-else-if="info.tag == 'el-upload'">
      <el-upload
        :ref="info.propertyCode"
        :disabled="info.disabled"
        :file-list="info.fileList"
        :list-type="info['list-type']"
        :accept="info.accept || '*'"
        action=""
        :http-request="uploadImgUrlFun"
        :name="info.name"
        :on-success="uploadSuccess"
        :on-remove="uploadRemove"
        :on-preview="handleUploadPreview"
        :auto-upload="info['auto-upload']"
        :on-error="handleUploadError"
        :multiple="info.multiple"
        :on-exceed="handleExceed"
        :beforeUpload="beforeUpload"
        :headers="uploadHeaders"
      >
        <template v-if="info['list-type'] === 'picture-card'">
          <i class="el-icon-plus"></i>
        </template>
        <template v-else>
          <el-button type="primary" icon="el-icon-upload" :disabled="info.disabled">{{ info.buttonText }}</el-button>
        </template>
        <template v-if="info.showTip">
          <div slot="tip" class="el-upload__tip">只能上传不超过 {{ getUploadFileSizeLimit(info) }}{{ getUploadSizeUnit(info) }} 的{{ info.accept }}文件</div>
        </template>
      </el-upload>
    </template>
    <template v-else-if="info.tag == 'el-statistic'">
      <el-statistic
        :value="info.defaultValue"
        :precision="info.precision"
        :group-separator="info['group-separator']"
        :description="info.description"
      >
        <template slot="suffix">{{ info.suffix }}</template>
      </el-statistic>
    </template>
    <template v-else-if="info.tag === 'com-BMI'">
      <com-BMI
        @value-change="valueChange($event, info)"
        :isPreview="true"
        :options="info.options"
        :defaultValue="info.defaultValue"
        :disabled="info.disabled"
      ></com-BMI>
    </template>
    <template v-else-if="info.tag === 'com-TimeNumeral'">
      <com-TimeNumeral
        @value-change="valueChange($event, info)"
        :placeholder="info.placeholder"
        :unit="info.unit"
        :type="info.type"
        :isSetEndTime="info.isSetEndTime"
        :startDateLabel="info.startDateLabel"
        :endDateLabel="info.endDateLabel"
        :differenceLabel="info.differenceLabel"
        :differenceUnit="info.differenceUnit"
        :valueFormat="info.format"
        :defaultValue="info.defaultValue"
        :calculationType="info.calculationType"
        :labelType="info.labelType"
        :disabled="info.disabled"
        :required="info.required"
      ></com-TimeNumeral>
    </template>
    <template v-else-if="info.tag === 'com-complication'">
      <com-complication
        @value-change="valueChange($event, info)"
        :options="info.options"
        :lablType="info.labelType"
        :propertyName="info.propertyName"
        :children="info.children"
        :defaultValue="info.defaultValue"
        :renderList="previewFormData"
        :disabled="info.disabled"
        :required="info.required"
        :associationList="associationList"
      ></com-complication>
    </template>
    <template v-else-if="info.tag === 'com-caption'">
      <com-caption
        @value-change="valueChange($event, info)"
        :description="info.description"
        :effect="info.effect"
        :closable="info.closable"
        :type="info.type"
        :disabled="info.disabled"
      ></com-caption>
    </template>
    <template v-else-if="info.tag == 'com-specification'">
      <el-alert class="com-specification_alert" :closable="false" :title="info.description" type="info"> </el-alert>
    </template>
    <template v-else-if="info.tag == 'com-yearMonthDay'">
      <com-yearMonthDay
        :info="info"
        :defaultValue="info.defaultValue"
        @value-change="valueChange($event, info)"
        :disabled="info.disabled"
        :format="info.format"
        :value-format="info['value-format']"
        :isShowYear="info.isShowYear"
        :isShowMonth="info.isShowMonth"
        :isShowDay="info.isShowDay"
        :separator="info.separator"
        :yearUnit="info.yearUnit"
        :monthUnit="info.monthUnit"
        :dayUnit="info.dayUnit"
      ></com-yearMonthDay>
    </template>
    <template v-else-if="info.tag == 'com-DValue'">
      <com-DValue
        :info="info"
        :defaultValue="info.defaultValue"
        :options="info.options"
        :disabled="info.disabled"
        :placeholder="info.placeholder"
        :unit="info.unit"
        @value-change="valueChange($event, info)"
      ></com-DValue>
    </template>
    <template v-else-if="info.tag == 'com-WHR'">
      <com-WHR
        :info="info"
        :defaultValue="info.defaultValue"
        :options="info.options"
        :disabled="info.disabled"
        :placeholder="info.placeholder"
        :unit="info.unit"
        :isFlex="info.isFlex"
        @value-change="valueChange($event, info)"
        :required="info.required"
        :isPreview="true"
      ></com-WHR>
    </template>

    <template v-else-if="info.tag == 'com-table'">
      <com-table
        :options="info.options"
        :headers="info.headers"
        :tableAlignType="info.tableAlignType"
        :disabled="info.disabled"
        @change="valueChange($event, info)"
      ></com-table>
    </template>
    <template v-else-if="info.tag == 'el-table'">
      <el-table :data="info.options">
        <el-table-column v-for="(item, index) in info.headers" :key="index" :label="item" :prop="item" align="center" min-width="220">
          <template slot-scope="scope">
            <el-input
              clearable
              v-if="scope.row[scope.column.property].type == 'text'"
              type="textarea"
              v-model="scope.row[scope.column.property].value"
              :disabled="info.disabled"
              autosize
            ></el-input>
            <el-date-picker
              v-if="scope.row[scope.column.property].type != 'text'"
              v-model="scope.row[scope.column.property].value"
              :disabled="info.disabled"
              :type="scope.row[scope.column.property].type"
              :format="
                scope.row[scope.column.property].type == 'week'
                  ? 'yyyy 第 WW 周'
                  : scope.row[scope.column.property].type == 'month'
                  ? 'yyyy-MM'
                  : scope.row[scope.column.property].type == 'year'
                  ? 'yyyy'
                  : scope.row[scope.column.property].type == 'datetime'
                  ? 'yyyy-MM-DD HH:mm'
                  : 'yyyy-MM-DD'
              "
              placeholder="选择日期"
            >
            </el-date-picker>
          </template>
        </el-table-column>
      </el-table>
    </template>
    <template v-else-if="info.tag == 'el-divider'">
      <el-divider :direction="info.direction"></el-divider>
    </template>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth";
import { uploadImage } from "@/api/cms/article";
import cityData from "@/utils/city.json";
import nationData from "@/utils/nation.json";
export default {
  name: "view-item",
  props: ["info", "modelValue", "previewFormData", "associationList"],
  model: {
    prop: "modelValue",
    event: "change",
  },
  components: {},
  data() {
    return {
      uploadFileUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 上传的图片服务器地址
      uploadHeaders: {
        Authorization: "Bearer " + getToken(),
      },
      renderKey: new Date().getTime() + "",
      inputValue: "",
      marks: undefined,
      lastInitValue: undefined,
    };
  },
  watch: {
    info: {
      immediate: true,
      deep: true,
      handler(newVal) {
        this.initData(newVal);
      },
    },
  },
  computed: {},
  mounted() {},
  destroyed() {},
  methods: {
    setReactiveFieldValue(target, field, value) {
      this.$set(target, field, value);
    },
    initData(newVal) {
      if (!newVal) return;
      if (!Object.prototype.hasOwnProperty.call(newVal, "defaultValue")) {
        this.setReactiveFieldValue(newVal, "defaultValue", undefined);
      }
      this.normalizeUploadLimit(newVal);
      this.initOptions(newVal);

      if (newVal.tag == "el-input") {
        this.inputValue = this.hasValue(newVal.defaultValue) ? newVal.defaultValue : "";
      }

      const propertyValue = newVal.propertyValue;
      this.normalizeOptionValueTypes(newVal, propertyValue);
      if (!this.hasValue(propertyValue) || propertyValue === this.lastInitValue) {
        return;
      }

      switch (newVal.tag) {
        case "el-input":
        case "el-input-number":
          this.initInputValue(newVal, propertyValue);
          break;
        case "el-radio-group":
          this.initRadioValue(newVal, propertyValue);
          break;
        case "el-checkbox-group":
          this.initCheckboxValue(newVal, propertyValue);
          break;
        case "el-select":
          this.initSelectValue(newVal, propertyValue);
          break;
        case "el-cascader":
          this.initCascaderValue(newVal, propertyValue);
          break;
        case "el-slider":
        case "el-rate":
        case "el-color-picker":
          this.initNumericLikeValue(newVal, propertyValue);
          break;
        case "el-switch":
          this.initSwitchValue(newVal, propertyValue);
          break;
        case "el-date-picker":
        case "el-time-picker":
          this.initDateTimeValue(newVal, propertyValue);
          break;
        case "el-upload":
          this.initUploadValue(newVal, propertyValue);
          break;
        default:
          this.initGeneralValue(newVal, propertyValue);
          break;
      }

      this.lastInitValue = propertyValue;
    },
    initOptions(newVal) {
      if (newVal.tagType == "province") {
        newVal.options = cityData;
      }
      if (newVal.tagType == "nation") {
        newVal.options = nationData;
      }
    },
    normalizeUploadLimit(info) {
      if (!info || info.tag !== "el-upload") {
        return;
      }
      const currentFileSize = Number(info.fileSize);
      if (!currentFileSize || currentFileSize === 2) {
        this.setReactiveFieldValue(info, "fileSize", 100);
      }
      if (!info.sizeUnit) {
        this.setReactiveFieldValue(info, "sizeUnit", "MB");
      }
    },
    getUploadFileSizeLimit(info) {
      const fileSize = Number(info && info.fileSize);
      return fileSize || 100;
    },
    getUploadSizeUnit(info) {
      return (info && info.sizeUnit) || "MB";
    },
    initInputValue(info, propertyValue) {
      if (this.hasValue(info.defaultValue) && !this.isUnitJsonString(propertyValue)) {
        if (info.tag == "el-input") {
          this.inputValue = info.defaultValue;
        }
        return;
      }
      if (info.isSetMultipleUnit && this.isUnitJsonString(propertyValue)) {
        const propertyValObj = this.safeJsonParse(propertyValue, {});
        if (!this.hasValue(info.defaultValue)) {
          this.setReactiveFieldValue(info, "defaultValue", info.tag == "el-input-number" ? this.toNumberValue(propertyValObj.content) : propertyValObj.content || "");
        }
        if (!this.hasValue(info.unitVal) && propertyValObj.unit) {
          info.unitVal = propertyValObj.unit;
        }
      } else if (!this.hasValue(info.defaultValue)) {
        this.setReactiveFieldValue(info, "defaultValue", info.tag == "el-input-number" ? this.toNumberValue(propertyValue) : propertyValue);
      }
      if (info.tag == "el-input") {
        this.inputValue = this.hasValue(info.defaultValue) ? info.defaultValue : "";
      }
    },
    initRadioValue(info, propertyValue) {
      const matchedValue = this.matchOptionValue(info.options, propertyValue);
      if (!this.hasValue(info.defaultValue)) {
        this.setReactiveFieldValue(info, "defaultValue", this.normalizeRadioValue(matchedValue));
      } else {
        this.setReactiveFieldValue(info, "defaultValue", this.normalizeRadioValue(info.defaultValue));
      }
      if (info.options && info.options.length) {
        const selectedOption = info.options.find((item) => this.normalizeRadioValue(item.value) == this.normalizeRadioValue(matchedValue));
        if (selectedOption && selectedOption.isShowInputValue && info.other && !this.hasValue(selectedOption.inputVal)) {
          selectedOption.inputVal = info.other;
        }
      }
    },
    initCheckboxValue(info, propertyValue) {
      const checkboxValues = this.normalizeArrayOptionValues(info.options, this.parseArrayValue(propertyValue));
      if (!this.hasValue(info.defaultValue)) {
        this.setReactiveFieldValue(info, "defaultValue", checkboxValues);
      }
      if (info.options && info.options.length && info.other) {
        const checkOther = info.other.split(",");
        info.options.forEach((item) => {
          const valueIndex = checkboxValues.findIndex((value) => value == item.value);
          if (valueIndex > -1 && item.isShowInputValue && !this.hasValue(item.inputVal)) {
            item.inputVal = checkOther[valueIndex] || "";
          }
        });
      }
      this.checkboxChange(info.defaultValue || []);
    },
    initSelectValue(info, propertyValue) {
      if (this.hasValue(info.defaultValue)) return;
      if (info.multiple) {
        this.setReactiveFieldValue(info, "defaultValue", this.normalizeArrayOptionValues(info.options, this.parseArrayValue(propertyValue)));
      } else {
        this.setReactiveFieldValue(info, "defaultValue", this.matchOptionValue(info.options, propertyValue));
      }
    },
    initCascaderValue(info, propertyValue) {
      if (this.hasValue(info.defaultValue)) return;
      this.setReactiveFieldValue(info, "defaultValue", this.parseArrayValue(propertyValue));
    },
    initNumericLikeValue(info, propertyValue) {
      if (this.hasValue(info.defaultValue)) return;
      if (info.tag == "el-color-picker") {
        this.setReactiveFieldValue(info, "defaultValue", propertyValue);
        return;
      }
      this.setReactiveFieldValue(info, "defaultValue", this.toNumberValue(propertyValue));
    },
    initSwitchValue(info, propertyValue) {
      if (this.hasValue(info.defaultValue)) return;
      if (info["active-value"] !== undefined || info["inactive-value"] !== undefined) {
        this.setReactiveFieldValue(info, "defaultValue", propertyValue);
      } else if (propertyValue === "true" || propertyValue === true) {
        this.setReactiveFieldValue(info, "defaultValue", true);
      } else if (propertyValue === "false" || propertyValue === false) {
        this.setReactiveFieldValue(info, "defaultValue", false);
      } else {
        this.setReactiveFieldValue(info, "defaultValue", propertyValue == "1" ? true : propertyValue == "0" ? false : propertyValue);
      }
    },
    initDateTimeValue(info, propertyValue) {
      if (this.hasValue(info.defaultValue)) return;
      if (info["is-range"]) {
        this.setReactiveFieldValue(info, "defaultValue", this.parseArrayValue(propertyValue));
      } else {
        this.setReactiveFieldValue(info, "defaultValue", propertyValue);
      }
    },
    initUploadValue(info, propertyValue) {
      if (info.fileList && info.fileList.length) return;
      let fileUrls = [];
      if (this.hasValue(propertyValue)) {
        fileUrls = this.parseArrayValue(propertyValue);
      } else if (info.imageUrls && info.imageUrls.length) {
        fileUrls = info.imageUrls;
      }
      info.fileList = fileUrls.filter((item) => this.hasValue(item)).map((url, index) => ({ uid: `init-${index}`, url }));
    },
    initGeneralValue(info, propertyValue) {
      if (this.hasValue(info.defaultValue)) return;
      if (info.tag == "com-BMI" || info.tag == "com-TimeNumeral" || info.tag == "com-DValue" || info.tag == "com-WHR") {
        this.setReactiveFieldValue(info, "defaultValue", this.safeJsonParse(propertyValue, info.defaultValue));
        return;
      }
      this.setReactiveFieldValue(info, "defaultValue", propertyValue);
    },
    hasValue(value) {
      if (Array.isArray(value)) {
        return value.length > 0;
      }
      if (value === 0 || value === false) {
        return true;
      }
      if (value === null || value === undefined || value === "" || value === "null") {
        return false;
      }
      if (typeof value === "object") {
        return Object.keys(value).length > 0;
      }
      return true;
    },
    safeJsonParse(value, fallback = null) {
      if (typeof value !== "string") return fallback;
      try {
        return JSON.parse(value);
      } catch (error) {
        return fallback;
      }
    },
    parseArrayValue(value) {
      if (Array.isArray(value)) {
        return value;
      }
      if (!this.hasValue(value)) {
        return [];
      }
      const jsonValue = this.safeJsonParse(value, null);
      if (Array.isArray(jsonValue)) {
        return jsonValue;
      }
      if (typeof value === "string" && value.indexOf(",") > -1) {
        return value.split(",");
      }
      return [value];
    },
    isUnitJsonString(value) {
      const jsonValue = this.safeJsonParse(value, null);
      return !!(jsonValue && typeof jsonValue === "object" && !Array.isArray(jsonValue) && (jsonValue.content !== undefined || jsonValue.unit !== undefined));
    },
    toNumberValue(value) {
      const numberValue = Number(value);
      return Number.isNaN(numberValue) ? value : numberValue;
    },
    normalizeRadioValue(value) {
      if (!this.hasValue(value)) {
        return value;
      }
      return String(value);
    },
    castOptionValueByType(value, targetType) {
      if (targetType === "string") {
        return value === null || value === undefined ? value : String(value);
      }
      if (targetType === "number") {
        const numberValue = Number(value);
        return Number.isNaN(numberValue) ? value : numberValue;
      }
      if (targetType === "boolean") {
        if (value === "true" || value === true || value === 1 || value === "1") {
          return true;
        }
        if (value === "false" || value === false || value === 0 || value === "0") {
          return false;
        }
      }
      return value;
    },
    normalizeOptionValueTypes(info, propertyValue) {
      if (!info || !["el-radio-group", "el-select", "el-checkbox-group"].includes(info.tag)) {
        return;
      }
      if (!Array.isArray(info.options) || !info.options.length) {
        return;
      }

      const optionTypeList = info.options
        .map((item) => typeof item.value)
        .filter((type) => type !== "undefined");

      if (!optionTypeList.length || new Set(optionTypeList).size <= 1) {
        return;
      }

      const compareValue = Array.isArray(propertyValue) ? propertyValue[0] : propertyValue;
      if (!this.hasValue(compareValue)) {
        return;
      }

      const matchedOption = info.options.find((item) => item.value == compareValue);
      if (!matchedOption) {
        return;
      }

      const targetType = typeof matchedOption.value;
      info.options = info.options.map((item) => ({
        ...item,
        value: this.castOptionValueByType(item.value, targetType),
      }));
    },
    matchOptionValue(options, value) {
      if (!options || !options.length) {
        return value;
      }
      const matchedOption = options.find((item) => item.value == value);
      return matchedOption ? matchedOption.value : value;
    },
    normalizeArrayOptionValues(options, values) {
      if (!Array.isArray(values)) {
        return [];
      }
      return values.map((value) => this.matchOptionValue(options, value));
    },
    inputBlur(event) {
      this.setReactiveFieldValue(this.info, "defaultValue", event);
    },
    radioGroupChange(event, info) {
      const normalizedValue = this.normalizeRadioValue(event);
      this.setReactiveFieldValue(info, "defaultValue", normalizedValue);
      this.valueChange(normalizedValue, info);
    },
    valueChange(event, info) {
      // console.log(event, info)
      let returnVal = event;
      if (this.info.tag == "el-input" || this.info.tag == "el-input-number") {
        if (this.info.unitVal) {
          returnVal = JSON.stringify({
            content: event,
            unit: this.info.unitVal,
          });
        } else {
          returnVal = event;
        }
      }
      if (this.info.tag == "el-checkbox-group" || this.info.tag == "el-radio-group") {
        let index = this.info.options.findIndex((f) => f.isShowInputValue);
        if (this.info.tag == "el-radio-group" && index > -1) {
          const selectedOption = this.info.options.find((item) => this.normalizeRadioValue(item.value) == this.normalizeRadioValue(returnVal));
          this.info["inputVal"] = selectedOption ? selectedOption.inputVal : "";
        } else if (this.info.tag == "el-checkbox-group" && index > -1) {
          let inputValArr = [];
          this.info.options.forEach((fItem) => {
            if (fItem.isShowInputValue && returnVal.findIndex((f) => f == fItem.value) > -1) {
              inputValArr.push(fItem.inputVal);
            }
          });
          this.info["inputVal"] = inputValArr.toString();
        }
      } else if (this.info.tag == "el-upload") {
        console.log(event);
        if (event.findIndex((f) => f.response || f.raw) > -1) return;
        returnVal = JSON.stringify(event.map((m) => m.url));
      }
      this.$emit("change", { value: returnVal, item: this.info });
    },
    /**
     * ===================上传start ====================
     */
    // 上传成功回调
    uploadSuccess(res, file, fileList) {
      // if (!this.info.fileList || !this.info.fileList.length) {
      //     this.info.fileList = [];
      // }
      // console.log(res)
      this.$modal.closeLoading();
    },
    uploadImgUrlFun(resData) {
      let params = new FormData();
      params.append("file", resData.file);
      uploadImage(params)
        .then((res) => {
          this.info.fileList.push({
            uid: resData.uid,
            url: res.url,
          });
          this.info.imageUrls = this.info.fileList.map((item) => item.url || item.thumbUrl).filter((item) => this.hasValue(item));
          this.valueChange(this.info.fileList);
          this.$modal.closeLoading();
        })
        .catch((error) => {
          // this.handleUploadError()
        });
    },
    uploadRemove(event, fileList) {
      console.log("event", event);
      let index = fileList.findIndex((f) => f.uid == event.uid);
      if (index > -1) {
        fileList.splice(index, 1);
      }
      let returnInfo = fileList.map((item) => {
        if (item.response) {
          return {
            uid: item.uid,
            ...item.response,
          };
        } else {
          return item;
        }
      });
      this.info.fileList = returnInfo;
      this.info.imageUrls = returnInfo.map((item) => item.url || item.thumbUrl).filter((item) => this.hasValue(item));
      this.valueChange(returnInfo);
    },
    beforeUpload(file) {
      let conf = this.info;
      const units = {
        KB: "1024",
        MB: "1024 / 1024",
        GB: "1024 / 1024 / 1024",
      };
      const uploadSizeUnit = this.getUploadSizeUnit(conf);
      const unitNum = units[uploadSizeUnit] || units.MB;
      const uploadFileSizeLimit = this.getUploadFileSizeLimit(conf);
      if (uploadFileSizeLimit) {
        let isRightSize = `${file.size} / ${unitNum} < ${uploadFileSizeLimit}`;
        isRightSize = eval(isRightSize);
        if (!isRightSize) {
          this.$message.error(`文件大小超过 ${uploadFileSizeLimit} ${uploadSizeUnit}`);
          return false;
        }
      }
      if (conf.accept) {
        let isAccept = new RegExp(conf.accept).test(file.type);
        if (!isAccept) {
          this.$message.error(`应该选择${conf.accept}类型的文件`);
          return false;
        }
      }
      this.$modal.loading("正在上传文件，请稍候...");
    },
    uploadChange(file, fileList) {
      if (fileList.length > this.info.limit) {
        //这里只获取最后一次选择的文件
        fileList.splice(0, 1);
      }
      this.info.fileList = fileList;
      if (fileList.findIndex((f) => !f.response) == -1) {
        let returnData = fileList.map((m) => {
          return m.response.url;
        });
        this.valueChange(returnData);
      }
      // console.log(file)
    },
    // 文件个数超出
    handleExceed() {
      this.$modal.msgError(`上传文件数量不能超过 ${this.info.limit} 个!`);
    },
    handleUploadPreview(file) {
      const previewUrl = this.appendBigToImageUrl(file.originalUrl || file.url || file.thumbUrl);
      if (!previewUrl) {
        return;
      }
      window.open(previewUrl, "_blank");
    },
    appendBigToImageUrl(url) {
      if (!this.hasValue(url)) {
        return url;
      }
      const urlText = String(url);
      const [path, query = ""] = urlText.split("?");
      const extensionIndex = path.lastIndexOf(".");
      const slashIndex = path.lastIndexOf("/");

      if (extensionIndex > slashIndex) {
        const filePath = `${path.slice(0, extensionIndex)}_big${path.slice(extensionIndex)}`;
        return query ? `${filePath}?${query}` : filePath;
      }

      return path.endsWith("_big") ? urlText : `${urlText}_big`;
    },
    // 上传失败
    handleUploadError(err) {
      this.$modal.msgError("上传图片失败，请重试");
      this.$modal.closeLoading();
    },

    /**
     *  ===================  上传end =======================
     */
    checkboxChange(event) {
      // console.log('checkboxChange', event)
      let groupOption = this.info.options;
      let newArray = event.length ? groupOption.filter((f) => event.findIndex((e) => f.value == e) > -1) : groupOption;
      let disabledArr = newArray.length && newArray[0].mutualExclusion;
      if (disabledArr && disabledArr.length) {
        disabledArr.forEach((item) => {
          groupOption.find((f) => f.value == item)["disabled"] = true;
        });
      } else {
        groupOption.forEach((item) => {
          if (item.mutualExclusion) {
            let itemDisabled = false;
            event.forEach((info) => {
              if (item.mutualExclusion.findIndex((f) => f == info) > -1) {
                itemDisabled = true;
              }
            });
            item.disabled = itemDisabled;
          } else {
            item.disabled = false;
          }
        });
      }
      this.info.options = groupOption;
      this.renderKey = new Date().getTime() + "";
      this.valueChange(event, this.info);
    },
  },
};
</script>
<style lang="scss" scoped>
.preview_item {
  display: flex;
  align-items: center;
  padding-left: 15px;
  min-height: 34px;
  .el-input,
  .el-textarea,
  .el-slider {
    width: 70%;
  }
}
.unit_info {
  margin-left: 8px;
}
.el-radio-group,
.el-checkbox-group {
  .group_custom_item {
    margin-right: 10px;
    margin-bottom: 5px;
  }
}
.el-slider {
  margin-left: 20px;
}

::v-deep .el-slider {
  .el-slider__runway.disabled {
    .el-slider__bar {
      background-color: #1890ff;
    }
  }
}

::v-deep .el-radio-group,
::v-deep .el-checkbox-group {
  .el-radio,
  .el-checkbox {
    display: flex;
    align-items: center;
    .is-disabled.is-checked {
      .el-radio__inner,
      .el-checkbox__inner {
        border-color: #1890ff;
        background: #1890ff;
      }
      .el-radio__inner::after {
        background-color: #fff;
      }
      .el-checkbox__inner::after {
        border-color: #fff;
      }
    }
    .is-disabled + span {
      &.el-radio__label,
      &.el-checkbox__label {
        color: #606266;
      }
    }
  }
}
.group_input {
  display: flex;
  align-items: center;
  width: 100%;
  margin-top: 5px;
  margin-left: 10px;
  .option_input {
    margin: 0 3px;
  }
  .radio_text {
    margin: 0 3px;
    font-size: 14px;
  }
}
</style>
