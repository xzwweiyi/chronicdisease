<template>
    <div class="follow_preview">
        <div>
            <div v-for="(element,index) in previewMedicalList" :key="index" class="follow_preview_form"
                v-if="element.isShowDom">
                <p class="preview_form_name">{{ element.medicalName ||element.templateName}}</p>
                <preview-dom :preview-form-data="element.children" :key="renderKey" class="preview_container_form"
                    @formchange="preViewFormChange($event,element)" />
            </div>
        </div>
        <el-select v-if="isAppPreview" v-model="currentScreen" class="screen_option">
            <el-option v-for="item in screenList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
        </el-select>
    </div>
</template>
<script>

import previewDom from './previewForm/index'
import { convertFieldsDataLoop } from '@/utils/index'

export default {
    components: {
        previewDom
    },
    props: ['drawingList'],
    data() {
        return {
            previewPageData: {},
            isAppPreview: false,
            screenList: [{
                value: '320*568',
                width: 320,
                height: 568,
                label: 'iPhone 5/SE'
            },
            {
                value: '360*640',
                width: 360,
                height: 640,
                label: 'Galaxy S5'
            },
            {
                value: '375*667',
                width: 375,
                height: 667,
                label: 'iPhone 6/7/8'
            },
            {
                value: '414*736',
                width: 414,
                height: 736,
                label: 'iPhone 6/7/8 Plus'
            },
            {
                value: '375*812',
                width: 375,
                height: 812,
                label: 'iPhone X'
            },
            {
                value: '414*896',
                width: 414,
                height: 896,
                label: 'iPhone XR'
            },
            {
                value: '390*844',
                width: 390,
                height: 844,
                label: 'iPhone 12 Pro'
            },
            ],
            currentScreen: '375*667',
            screenWidth: '90%',
            screenHeight: 'calc(100vh - 33px)',
            generateConf: {
                type: 'file'
            },
            renderKey: new Date() + '',
            previewMedicalList: []
        }
    },
    computed: {},
    watch: {
        isAppPreview(newVal) {
            if (newVal) {
                let current = this.screenList.find(f => f.value == '375*667')
                this.screenWidth = current.width + 'px';
                this.screenHeight = current.height + 'px';
            } else {
                this.screenWidth = '90%';
                this.screenHeight = 'calc(100vh - 33px)';
            }
        },
        currentScreen(newval) {
            let current = this.screenList.find(f => f.value == newval)
            this.screenWidth = current.width + 'px';
            this.screenHeight = current.height + 'px';
        },
        drawingList: {
            immediate: true,
            handler(newVal) {
                if (newVal && newVal.length) {
                    this.previewMedicalList = newVal.map(m => {
                        let info = { ...m }
                        if (m.relationInfo && m.relationInfo.relationQuestion && m.relationInfo.relationQuestion.length) {
                            info.isShowDom = false
                        }else{
                            info.isShowDom = true
                        }
                        return info
                    })
                }
            }
        }
    },
    created() {
    },
    mounted() {
    },
    beforeDestroy() { },
    methods: {
        preViewFormChange(event, medicalInfo) {
            // console.log('change', event, medicalInfo)
            this.previewMedicalList.forEach((item, index) => {
                item.isShowDom = this.convertPreviewMedicalIsShowDom(item, medicalInfo,event.formData)
            })
        },

        // 是否显示
        convertPreviewMedicalIsShowDom(item, medicalInfo,childrenInfo) {
            let isShowDom = false;
            let fieldsData = convertFieldsDataLoop(childrenInfo)
            if (item.relationInfo && item.relationInfo.relationQuestion.length &&
                medicalInfo.medicalFormId == item.relationInfo.relationQuestion[0].belongingFormId) {
                let relationQuestoinList = item.relationInfo.relationQuestion;
                relationQuestoinList.forEach((itemR, indexR) => {
                    let aggregate = [];
                    let fieldsItem = fieldsData.find(f => f.propertyCode == itemR.questionId);
                    if (medicalInfo.medicalFormId == itemR.belongingFormId && itemR.medicalCode == medicalInfo.medicalCode) {
                        let valueList = fieldsItem && fieldsItem.defaultValue || []
                        if (fieldsItem && (fieldsItem.tag == "el-radio-group" || fieldsItem.tag ==
                            'el-checkbox-group' || fieldsItem.tag == 'el-select')) {
                            if (!Array.isArray(valueList)) {
                                valueList = [valueList]
                            }
                            if (itemR.checkedOptions && itemR.checkedOptions.length) {
                                itemR.checkedOptions.forEach((checkItem, checkIndex) => {
                                    let checkItemString = checkItem.toString()
                                    if (valueList.indexOf(checkItemString) > -1 ||
                                        valueList.findIndex(f => f == checkItem) > -1) {
                                        aggregate.push(checkIndex)
                                    }
                                })
                            }
                        }else if (fieldsItem && fieldsItem.defaultValue) {
                            aggregate.push(true)
                        }
                    }
                    if (aggregate.length) {
                        isShowDom = true;
                    } else {
                        isShowDom = false;
                    }
                })
            } else {
                isShowDom = true;
            }
            return isShowDom
        },
    }
}
</script>

<style lang="scss" scoped>
.follow_preview {
    .follow_preview_form {
        border: 1px #dcdfe6 dotted;
        padding: 10px;
        margin-bottom: 10px;
        margin-top: 5px;
        border-radius: 6px;
        .preview_form_name {
            margin-bottom: 10px;
        }
    }

    ::v-deep .el-form {
        overflow: hidden;
        padding-left: 10px;

        .el-row {
            border: 1px #dcdfe6 dotted;
            padding: 10px;
            margin-bottom: 10px;
            margin-top: 5px;
            border-radius: 6px;

            .el-form-item {
                padding-left: 10px;
            }
        }
    }
}
</style>
