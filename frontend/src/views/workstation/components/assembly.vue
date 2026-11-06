<template>
    <div class="components_value">
        <p class="subTitle_style" v-if="componentsInfo.isShowSubTitle">{{componentsInfo.subTitle}}</p>
        <template v-if="componentsInfo.tag == 'el-upload'">
            <div v-if="componentsInfo.imageUrls&&componentsInfo.imageUrls.length">
                <el-image v-for="(item,index) in componentsInfo.imageUrls" :key="index" :src="item" fit="contain" @click="openImageViewer" :preview-src-list="previewImageUrls">
                    <div slot="error" class="image-slot">
                        <i class="el-icon-picture-outline"></i>
                    </div>
                    <div slot="placeholder" class="image-slot">
                        加载中<span class="dot">...</span>
                    </div>
                </el-image>
            </div>
            <el-image v-else>
                <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                </div>
            </el-image>
        </template>
        <template v-else-if="componentsInfo.tag == 'el-slider'">
            <el-slider v-model="componentsInfo.convertValueStr" disabled :min="componentsInfo.min" :max="componentsInfo.max" :marks="marks"></el-slider>
        </template>
        <template v-else-if="componentsInfo.tag == 'el-checkbox-group'">
            <el-checkbox-group v-model="componentsInfo.convertValueStr" class="inline_group">
                <div class="group_custom_item" v-for="(item,index) in componentsInfo.options" :key="index">
                    <el-checkbox :label="typeof item.value=='number'?item.value.toString():item.value" disabled>
                        {{ item.label }}{{item.itemScores?'（' + item.itemScores + '分）':''}}</el-checkbox>
                    <div v-if="item.isShowInputValue&&item.inputAttr&&componentsInfo.convertValueStr&&componentsInfo.convertValueStr.length
                        &&componentsInfo.convertValueStr.findIndex(f=>f==item.value)>-1" class="group_input">
                        <p class="radio_text" v-if="item.inputAttr.inputPrefixVal">{{item.inputAttr.inputPrefixVal}}
                        </p>
                        <p class="radio_text">
                            {{ item.inputVal | nullFormat}}</p>
                        <p class="radio_text">{{item.inputAttr.inputSuffixVal}}
                        </p>
                    </div>
                    <p class="dispose_explain" v-if="item.disposeExplain" v-html="item.disposeExplain"></p>
                </div>
            </el-checkbox-group>
        </template>
        <template v-else-if="componentsInfo.tag == 'el-radio-group'">
            <el-radio-group v-model="componentsInfo.convertValueStr">
                <div class="group_custom_item" v-for="(item,index) in componentsInfo.options" :key="index">
                    <el-radio :label="typeof item.value=='number'?item.value.toString():item.value" disabled>
                        {{ item.label }}{{item.itemScores?'（' + item.itemScores + '分）':''}}</el-radio>
                    <div v-if="item.inputAttr&&item.isShowInputValue&&componentsInfo.convertValueStr==item.value" class="group_input">
                        <p class="radio_text" v-if="item.inputAttr.inputPrefixVal">{{item.inputAttr.inputPrefixVal}}
                        </p>
                        <p class="radio_text">
                            {{ item.inputVal | nullFormat}}</p>
                        <p class="radio_text" v-if="item.inputAttr.inputSuffixVal">{{item.inputAttr.inputSuffixVal}}
                        </p>
                    </div>
                    <p class="dispose_explain" v-if="item.disposeExplain" v-html="item.disposeExplain"></p>
                </div>
            </el-radio-group>
        </template>
        <template v-else-if="componentsInfo.tag == 'el-select'">
            <el-select
                :value="componentsInfo.propertyValue"
                disabled
                :clearable="componentsInfo.clearable"
                :placeholder="componentsInfo.placeholder || '请选择'"
            >
                <el-option
                    v-for="(item,index) in componentsInfo.options || []"
                    :key="index"
                    :value="item.value"
                    :label="item.itemScores ? item.label + '（' + item.itemScores + '分）' : item.label"
                    :disabled="item.disabled"
                ></el-option>
            </el-select>
        </template>
        <el-rate v-else-if="componentsInfo.tag=='el-rate'" v-model="componentsInfo.convertValueStr" disabled show-score text-color="#ff9900" score-template="{value}" :max="componentsInfo.max">
        </el-rate>
        <template v-else-if="componentsInfo.tag == 'com-caption'">
            <com-caption :type="componentsInfo.type" :closable="componentsInfo.closable"
                :effect="componentsInfo.effect" :description="componentsInfo.description"></com-caption>
        </template>
        <template v-else-if="componentsInfo.propertyValue">
            <template v-if="componentsInfo.tag == 'el-input'||componentsInfo.tag=='el-input-number'">
                {{ componentsInfo.convertValueStr!='null'? componentsInfo.convertValueStr :'--'}}
                <span class="unit_info" v-if="componentsInfo.unit">{{ componentsInfo.unit }}</span>
            </template>
            <template v-else-if="componentsInfo.tag == 'com-BMI'">
                <view-BMI :info="componentsInfo.convertValueStr"></view-BMI>
            </template>
            <template v-else-if="componentsInfo.tag == 'com-DValue'">
                <view-DValue :info="componentsInfo.convertValueStr" :options="componentsInfo.options"></view-DValue>
            </template>
            <template  v-else-if="componentsInfo.tag == 'el-switch'">
                <el-switch :value="componentsInfo.propertyValue=='0'?false:true" disabled></el-switch>
            </template>
            <template v-else-if="componentsInfo.tag==='com-TimeNumeral'">
                <view-TimeNumeral :unit="componentsInfo.unit" :isSetEndTime="componentsInfo.isSetEndTime" :startDateLabel="componentsInfo.startDateLabel" :endDateLabel="componentsInfo.endDateLabel" :differenceLabel="componentsInfo.differenceLabel" :differenceUnit="componentsInfo.differenceUnit" :info="componentsInfo.convertValueStr"></view-TimeNumeral>
            </template>
            <div v-else-if="componentsInfo.convertValueStr">{{ componentsInfo.convertValueStr }}</div>
            <div v-else>{{ componentsInfo.propertyValue }}</div>
        </template>
        <div v-else class="noContent">--</div>
    </div>
</template>

<script>
import {
    formConf
} from '@/utils/generator/config'
import DraggableItem from '@/views/tool/build/DraggableItem'
import cityData from '@/utils/city.json'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
export default {
    name: "workstation-components_value",
    props: ['info'],
    components: {
        DraggableItem,
        ElImageViewer
    },
    data() {
        return {
            componentsInfo: {},
            formConf,
            isShowViewer: false,
            marks: undefined
        };
    },
    watch: {
        componentsInfo: {
            immediate: true,
            deep: true,
            handler(newVal) {
                // if (newVal.tag == 'el-slider') {
                //     let marks = { 0: '0', 1: '', 2: '', 3: '', 4: '', 5: '', 6: '', 7: '', 8: '', 9: '', 10: '10' }
                //     let min = newVal.min
                //     let max = newVal.max
                //     let stempArray = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
                //     let step = Math.floor(newVal.max / 10)
                //     stempArray.forEach((item, index) => {
                //         if (index < stempArray.length - 1) {
                //             marks[item] = min.toString()
                //             min = min + step
                //         } else {
                //             marks[item] = max.toString()
                //         }
                //     })
                //     this.marks = marks
                // }
            }
        }
    },
    computed: {
        previewImageUrls() {
            return (this.componentsInfo.imageUrls || []).map(item => this.appendBigToImageUrl(item))
        },
        previewImgClass() {
            let sidebarStatus = this.$store.getters.sidebar
            if (sidebarStatus.opened) {
                return 'preview_sideOpen_image'
            } else if (!sidebarStatus.opened) {
                return 'preview_sideClose_image'
            }
        }
    },
    created() {
        this.initData()
    },
    methods: {
        appendBigToImageUrl(url) {
            if (!url) {
                return url
            }
            const urlText = String(url)
            const [path, query = ''] = urlText.split('?')
            const extensionIndex = path.lastIndexOf('.')
            const slashIndex = path.lastIndexOf('/')

            if (extensionIndex > slashIndex) {
                const filePath = path.endsWith('_big' + path.slice(extensionIndex))
                    ? path
                    : `${path.slice(0, extensionIndex)}_big${path.slice(extensionIndex)}`
                return query ? `${filePath}?${query}` : filePath
            }

            return path.endsWith('_big') ? urlText : `${urlText}_big`
        },
        normalizeNullableValue(value) {
            return value === 'null' || value === 'undefined' || value === '' ? null : value
        },
        initData() {
            const {
                attribute,
                ...otherEvent
            } = this.info;
            let componentsInfo = {}
            if (attribute) {
                componentsInfo = {
                    ...JSON.parse(attribute),
                    ...otherEvent,
                }
            } else {
                componentsInfo = {
                    ...otherEvent
                }
            }
            componentsInfo.propertyValue = this.normalizeNullableValue(componentsInfo.propertyValue)
            componentsInfo.other = this.normalizeNullableValue(componentsInfo.other)
            otherEvent.propertyValue = componentsInfo.propertyValue
            if (otherEvent.propertyValue) {
                if (componentsInfo.tag == 'el-input'||componentsInfo.tag == 'el-input-number') {
                    if (componentsInfo.isSetMultipleUnit) {
                        let propertyValObj = JSON.parse(otherEvent.propertyValue)
                        componentsInfo.convertValueStr = propertyValObj.content || ''
                        let unitIndex = componentsInfo.unitList.findIndex(f => f.value == propertyValObj.unit)
                        componentsInfo.unit = componentsInfo.unitList[unitIndex].label
                    } else {
                        componentsInfo.convertValueStr = otherEvent.propertyValue
                    }
                } else if (componentsInfo.tag == 'el-radio-group') {
                    componentsInfo.convertValueStr = componentsInfo.propertyValue
                    if (componentsInfo.options.find(f => f.value == componentsInfo.propertyValue)) {
                        componentsInfo.options.find(f => f.value == componentsInfo.propertyValue).isShowInputValue = true;
                        componentsInfo.options.find(f => f.value == componentsInfo.propertyValue)['inputVal'] = componentsInfo.other
                    }
                } else if (componentsInfo.tag == 'el-select') {
                    let selectInfo = componentsInfo.options.find(f => f.value == componentsInfo.propertyValue)
                    componentsInfo.convertValueStr = selectInfo ? selectInfo.label : '--'
                } else if (otherEvent.propertyType == "slider" || otherEvent.propertyType == "Rate") {
                    componentsInfo.convertValueStr = Number(componentsInfo.propertyValue)
                } else if (otherEvent.propertyType == 'province') {
                    let provinceValues = []
                    if (this.isJSONParseData(componentsInfo.propertyValue) &&
                        Array.isArray(JSON.parse(componentsInfo.propertyValue))) {
                        provinceValues = JSON.parse(componentsInfo.propertyValue)
                    } else if (this.isJSONSplitData(componentsInfo.propertyValue) &&
                        Array.isArray(componentsInfo.propertyValue.split(','))) {
                        provinceValues = componentsInfo.propertyValue.split(',')
                    } else {
                        provinceValues = componentsInfo.propertyValue
                    }
                    componentsInfo.convertValueStr = this.convertPropertyValueStr(provinceValues, cityData, 'code',
                        'name')
                } else if (componentsInfo.tag == 'el-checkbox-group') {
                    let checkboxValues = !Array.isArray(componentsInfo.propertyValue) ? componentsInfo
                        .propertyValue.split(',') : componentsInfo.propertyValue
                    componentsInfo.convertValueStr = checkboxValues
                    if (componentsInfo.options.find(f => f.isShowInputValue) && componentsInfo.other) {
                        let checkOther = componentsInfo.other.split(',')
                        componentsInfo.options.forEach(item => {
                            let valIndex = checkboxValues.findIndex(f => f == item.value)
                            if (valIndex > -1) {
                                item['inputVal'] = checkOther[valIndex]
                                item.isShowInputValue = true;
                            }
                        })
                    }
                } else if (componentsInfo.tag == 'el-cascader') {
                    let valueList = []
                    if (this.isJSONParseData(componentsInfo.propertyValue) &&
                        Array.isArray(JSON.parse(componentsInfo.propertyValue))) {
                        valueList = JSON.parse(componentsInfo.propertyValue)
                    } else if (this.isJSONSplitData(componentsInfo.propertyValue) &&
                        Array.isArray(componentsInfo.propertyValue.split(','))) {
                        valueList = componentsInfo.propertyValue.split(',')
                    } else {
                        valueList = componentsInfo.propertyValue
                    }
                    componentsInfo.convertValueStr = this.convertPropertyValueStr(valueList, componentsInfo.options,
                        'value', 'label')
                } else if (otherEvent.propertyType == 'BMI' || otherEvent.propertyType == 'HEMOGLOBIN'
                    || otherEvent.propertyType == 'TIMENUMERAL') {
                    componentsInfo.convertValueStr = JSON.parse(componentsInfo.propertyValue)
                }
            } else {
                otherEvent.propertyValue = null
            }
            // console.log(componentsInfo.tag, componentsInfo.propertyValue, componentsInfo.convertValueStr)
            this.componentsInfo = componentsInfo

           
        },
        // 是否是json格式
        isJSONParseData(str) {
            if (typeof str == 'string') {
                try {
                    var obj = JSON.parse(str);
                    if (typeof obj == 'object' && obj) {
                        return true;
                    } else {
                        return false;
                    }

                } catch (e) {
                    return false;
                }
            }
        },
        // 是否是json格式
        isJSONSplitData(str) {
            if (typeof str == 'string') {
                try {
                    var obj = str.split(',');
                    if (typeof obj == 'object' && obj) {
                        return true;
                    } else {
                        return false;
                    }

                } catch (e) {
                    return false;
                }
            }
        },
        convertPropertyValueStr(value, options, code, name) {
            let currentInfo = options.find(f => f[code] == value[0]),
                valueStr = currentInfo[name]
            if (value.length > 1) {
                valueStr =
                    `${valueStr}/${this.convertPropertyValueStr(value.splice(1, value.length - 1), currentInfo.children, code, name)}`
            }
            return valueStr
        },
        closeViewer() {
            this.isShowViewer = false
        },
        openImageViewer() {
            this.isShowViewer = true
        }
    },
};
</script>

<style lang="scss" scoped>
.components_value {
    flex: 1;
}
.el-slider {
    width: 80%;
    margin-left: 10px;

    .el-slider__runway.disabled .el-slider__bar {
        background-color: #1890ff;
    }
}

::v-deep .el-image {
    width: 150px;
    height: 100px;
    background: #f5f5f5;
    border: 1px solid #eaeaea;
    margin: 0 3px;

    .image-slot {
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: 40px;
    }
}
.preview_sideClose_image {
    position: absolute;
    right: 43%;
}

.preview_sideOpen_image {
    position: absolute;
    right: 38%;
}
::v-deep .el-slider {
    .el-slider__runway.disabled {
        .el-slider__bar {
            background-color: #1890ff;
        }
    }
}
.radio_text {
    display: inline-block;
}

::v-deep .el-radio-group,
::v-deep .el-checkbox-group {
    padding: 5px 0;
    .group_custom_item {
        padding: 3px 0;
    }

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
    .radio_text {
        margin: 0 3px;
    }
}
</style>
