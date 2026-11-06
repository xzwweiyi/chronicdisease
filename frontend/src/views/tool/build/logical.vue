<template>
    <el-dialog v-bind="$attrs" width="60%" :modal-append-to-body="false" v-on="$listeners" @open="onOpen"
        :close-on-click-modal="false" :close-on-press-escape="false" @close="throughParent" class="dialog">
        <el-form class="relation_content" inline label-width="100px">
            <el-form-item label="当前题目：">{{(activeData.propertyName||activeData.formName)}}</el-form-item>
            <el-form-item :label="item.label+''+(index+1)+'：'" :key="index" v-for="(item,index) in aboutQuestionList">
                <!-- <el-cascader :options="aboutTemplateList" placeholder="请选择关联题目" :props="props" :show-all-levels="false"
                    clearable class="relation_custom_cascader">
                    <template slot-scope="{ node, data }">
                        <span class="cascader_name">{{ data.treeLabel }}{{ node.children.length }}</span>
                    </template>
                </el-cascader> -->
                <!-- <el-select v-model="item.belongingFormId" placeholder="请选择所属模块" clearable no-match-text="没有匹配数据"
                    filterable class="about_question">
                    <el-option v-for="item in aboutTemplateList" :key="item.treeCode" :value="item.treeCode"
                        :label="item.treeLabel">
                    </el-option>
                </el-select>
                <el-select v-model="item.questionId" placeholder="请选择关联题目" @change="changeAboutQuestion($event,index)"
                    class="about_question" no-match-text="没有匹配数据" filterable>
                    <el-option v-for="item in questionList" :key="item.propertyCode" :value="item.propertyCode"
                        :label="item.propertyName">
                    </el-option>
                </el-select> -->
                <el-select v-model="item.questionId" placeholder="请选择关联题目" @change="changeAboutQuestion($event,index)"
                    class="about_question" no-match-text="没有匹配数据">
                    <el-option v-for="listItem in questionList" :key="listItem.propertyCode"
                        :value="listItem.propertyCode" :label="listItem.propertyName||listItem.propertyCode">
                        <!-- <span v-if="listItem.formName">({{listItem.formName}})</span> -->
                    </el-option>
                </el-select>
                <!-- <el-button type="text" icon="el-icon-plus" @click="addAboutQuestion"
                v-if="index==0&&aboutQuestionList.length==1&&questionList.length>1">更多</el-button> -->
                <el-button type="text" @click="delAboutQuestion(index)">取消</el-button>
                <div class="checked_set margin_top10" v-if="item.questionId">
                    <span>当 <el-tag>{{item.questionLabel}}</el-tag> 的值</span>
                    <template v-if="item.tag=='el-radio-group'||item.tag=='el-checkbox-group'||item.tag=='el-select'">
                        <div>
                            选择
                            <!-- 后期需要用到 -->
                            <!-- <el-select v-model="item.isCheckedOption" class="checked_option">
                            <el-option label="选择" :value="true"></el-option>
                            <el-option label="没有选择" :value="false"></el-option>
                        </el-select> -->
                            下面的选项：
                        </div>
                        <el-checkbox-group v-model="item.checkedOptions" @change="checkValueChange"
                            v-if="questionList.find(f=>f.propertyCode==item.questionId)">
                            <el-checkbox v-for="option in questionList.find(f=>f.propertyCode==item.questionId).options"
                                :label="option.value" :key="option.value">{{option.label}}</el-checkbox>
                        </el-checkbox-group>

                        <!-- <el-select v-model="item.isAllOption" class="checked_option margin_top10">
                        <el-option label="全部选项" :value="true"></el-option>
                        <el-option label="其中一个" :value="false"></el-option>
                         </el-select> -->
                    </template>
                    <template v-else-if="item.tag=='el-switch'">
                        选择为
                        <el-radio-group v-model="item.checkedOptions[0]" @change="checkValueChange"
                            v-if="questionList.find(f=>f.propertyCode==item.questionId)">
                            <el-radio label="1">是</el-radio>
                            <el-radio label="0">否</el-radio>
                        </el-radio-group>
                    </template>
                    <template
                        v-else-if="item.tag=='el-input-number'||item.tag=='el-slider'||item.tag=='el-rate'||item.tag=='el-input'">
                        <el-select v-model="item.operationInfo" class="operation_option">
                            <el-option v-for="info in operationList" :key="info.value" :value="info.value"
                                :label="info.label" />
                        </el-select>
                        <div class="operation_info">
                            <el-input-number :controls="false" class="operation_number" :min="0"
                                v-if="item.operationInfo=='6'" v-model="item.operationStartVal" />
                            <span v-if="item.operationInfo=='6'">至</span>
                            <el-input-number :controls="false" class="operation_number" :min="0"
                                v-if="item.operationInfo=='6'" v-model="item.operationEndVal" />
                            <el-input-number :controls="false" class="operation_number" :min="0"
                                v-if="item.operationInfo!='6'" v-model="item.operationValue" />
                        </div>
                    </template>
                    <div v-else>
                        <span>有值</span>
                    </div>
                    时，<el-tag>
                        {{activeData.propertyName||activeData.formName}}
                        <span v-if="!activeData.propertyName&&!activeData.formName">{{ activeData.propertyCode }}</span>
                    </el-tag> 才出现

                </div>
            </el-form-item>
            <el-form-item label="关联多题时："
                v-if="aboutQuestionList.length&&aboutQuestionList[0].questionId&&aboutQuestionList.length>1">
                <div class="relation_ship">
                    <span>多题（每个矩阵行标题视为一题）间</span>
                    <el-radio-group v-model="relationship">
                        <el-radio label="and">为“且”的关系( ? )</el-radio>
                        <el-radio label="or">为“或”的关系( ? )</el-radio>
                    </el-radio-group>
                </div>
            </el-form-item>
            <el-form-item class="submitBtn">
                <el-button type="primary" @click="onClose">确定</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</template>
<script>
export default {
    inheritAttrs: false,
    props: ['activeData', 'drawingList', 'templateList','parent'],
    data() {
        return {
            aboutQuestionList: this.resetRelationQuestion(),
            relationship: 'and',
            operationList: [{
                label: '等于',
                value: '1',
                code: '='
            }, {
                label: '大于',
                value: '2',
                code: '>'
            }, {
                label: '大于等于',
                value: '3',
                code: '>='
            }, {
                label: '小于',
                value: '4',
                code: '<'
            }, {
                label: '小于等于',
                value: '5',
                code: '<='
            }, {
                label: '区间',
                value: '6'
            }],
            props: {
                value: 'treeValue',
                label: 'treeLabel',
            }
        }
    },
    watch: {
        activeData: {
            immediate: true,
            deep: true,
            handler(newVal, oldVal) {
                // console.log('activeData',newVal.labelType,JSON.stringify(newVal))
                if (newVal && (newVal.propertyCode || newVal.formCode)) {
                    if (newVal.relationInfo && newVal.relationInfo.relationQuestion && newVal.relationInfo
                        .relationQuestion.length) {
                        this.aboutQuestionList = newVal.relationInfo.relationQuestion;
                        this.relationship = newVal.relationInfo.relationship;
                        // console.log(newVal.relationInfo.relationQuestion)
                    } else {
                        this.aboutQuestionList = this.resetRelationQuestion();
                        this.relationship = '';
                    }
                }
            }
        },
        templateList: {
            immediate: true,
            deep: true,
            handler(newVal) { }
        }
    },
    computed: {
        aboutTemplateList() {
            let templateList = this.templateList;
            let params = this.$route.params;
            if (params.medicalFormId) {
                templateList.find(f => f.medicalFormId == params.medicalFormId).children = this.questionList
            } else if (params.templateFormId) {
                templateList.find(f => f.templateFormId == params.templateFormId).children = this.questionList
            } else {
                templateList = this.questionList
            }
            return templateList
        },
        questionList() {
            // const tagList = ['el-radio-group', 'el-checkbox-group', 'el-select', 'el-switch',
            //     'el-input-number', 'el-rate', 'el-slider', 'el-input']
            const tagList = ['el-radio-group', 'el-checkbox-group', 'el-select', ]
            let filterList = JSON.parse(JSON.stringify(this.drawingList))
            let list = [];
            if (filterList.length) {
                let index = -1;
                if (this.activeData.labelType != 'form') {
                    index = filterList.findIndex(f => f.propertyCode == this.activeData.propertyCode);
                } else {
                    index = filterList.findIndex(f => f.formCode == this.activeData.formCode);
                }
                if (index > -1) {
                    list = filterList.slice(0, index)
                } else {
                    filterList.forEach((element,elIndex)=>{
                        if(element.labelType=='form'&&element.children){
                            index = element.children.findIndex(f => f.propertyCode == this.activeData.propertyCode)
                            if(index>-1){
                                list = element.children.slice(0, index)
                            }
                        }
                    })
                }
            }
            list.sort((a, b) => {
                return a.sortNum - b.sortNum
            })
            list = list.filter(f => tagList.findIndex(l => l == f.tag) > -1)
            list = list.filter(f => (f.tag == 'el-input' && f.regValue && f.regValue.value == 'number' || f.tag != 'el-input'))
            this.aboutQuestionList.forEach(item => {
                let itemQuestion = list.find(f => f.propertyCode == item.questionVModel)
                if (item.questionVModel && itemQuestion) {
                    item.questionLabel = itemQuestion.propertyName || item.questionVModel
                }
            })
            return list
        },
    },
    methods: {
        loopArray(array, parentIndex) {
            return array.reduce((prev, curr, index) => {
                if (parentIndex != undefined) {
                    curr.sortNum = parentIndex + index
                }
                curr.treeLabel = curr.propertyName
                curr.treeValue = curr.propertyCode
                return prev.concat(Array.isArray(curr.children) ? this.loopArray(curr.children, index) :
                    curr)
            }, [])
        },
        convertForm(formList) {
            let list = [];
            let currentIndex = -1
            formList.forEach((item, index) => {
                if (item.labelType == 'form') {
                    let itemIndex = item.children.findIndex(f => f.propertyCode == this.activeData
                        .propertyCode);
                    let children = item.children.map((m, mIndex) => {
                        return {
                            ...m,
                            sortNum: mIndex + index,
                            propertyName: `${m.propertyName}`,
                            formName: item.formName,
                            formCode: item.formCode,
                        }
                    })
                    item.children = children
                    if (itemIndex > -1) {
                        currentIndex = index
                        list = list.concat(children.slice(0, itemIndex))
                    }
                }
                if (currentIndex == -1) {
                    list.push(item)
                }
            })
            return list
        },
        resetRelationQuestion() {
            let info = [{
                label: '关联题目',
                belongingFormId: '',
                questionId: '',
                questionVModel: '',
                isCheckedOption: true,
                checkedOptions: [],
                isAllOption: false,
                questionLabel: '',
            }]
            return info
        },
        addAboutQuestion() {
            let info = this.resetRelationQuestion()
            // console.log('info', info)
            this.aboutQuestionList.push(info[0])
            // console.log('this.aboutQuestionList', this.aboutQuestionList)
        },
        delAboutQuestion(index) {
            if (index > 0) {
                this.aboutQuestionList.splice(0, 1)
            } else {
                this.aboutQuestionList = this.resetRelationQuestion()
            }
        },
        changeAboutQuestion(event, itemIndex) {
            // console.log('changeAboutQuestion', event)
            let aboutIndex = this.aboutQuestionList.findIndex(f => f.questionId == event)
            if (aboutIndex == itemIndex) {
                let currentQuestion = this.questionList.find(f => f.propertyCode == event)
                let changeQuestionInfo = this.aboutQuestionList.find(f => f.questionId == event)
                changeQuestionInfo.questionLabel = currentQuestion.propertyName;
                changeQuestionInfo.questionVModel = currentQuestion.propertyCode;
                changeQuestionInfo.checkedOptions = [];
                changeQuestionInfo.belongingFormId = currentQuestion.templateFormId ||
                    currentQuestion.medicalFormId;
                this.aboutQuestionList[itemIndex].labelType = currentQuestion.labelType
                this.aboutQuestionList[itemIndex].tag = currentQuestion.tag
            } else {
                this.$alert('关联题目不能重复', '', {
                    confirmButtonText: '确定',
                });
                this.aboutQuestionList[itemIndex] = this.resetRelationQuestion()[0]
            }
            this.$forceUpdate()
        },
        checkValueChange(event) {
            // console.log('event', event)
        },
        onOpen() {

        },
        onClose() {
            const findIndex = this.aboutQuestionList.findIndex(f => f.checkedOptions.length == 0);
            const findItem = this.aboutQuestionList[findIndex]
            if (findIndex > -1 && (findItem.tag == 'el-radio-group' || findItem.tag == 'el-checkbox-group' ||
                findItem.tag == 'el-select')) {
                this.$alert('您还没设置完关联逻辑，确定要关闭吗？', '', {
                    confirmButtonText: '确定',
                }).then(() => {
                    this.throughParent()
                }).catch(() => { });
            } else {
                this.throughParent()
            }
        },
        throughParent() {
            if (this.aboutQuestionList.findIndex(f => f.questionId) > -1) {
                this.activeData.relationInfo = {
                    relationQuestion: this.aboutQuestionList,
                    relationship: 'and',
                }
            } else {
                this.activeData.relationInfo = undefined
            }
            // console.log('this.activeData', this.activeData.relationInfo)
            this.$emit('logicalchange', this.activeData.relationInfo)
            this.$emit('update:visible', false)

        },
    }
}
</script>

<style lang="scss" scoped>
.el-dialog {
    height: 500px;
}

.relation_content {
    ::v-deep .el-form-item {
        width: 100%;
        .el-form-item__content {
            width: calc(100% - 150px);
        }
        .about_question {
            width: calc(100% - 80px);
            margin-right: 10px;
        }
    }
}

.checked_option {
    width: 110px;
}

.margin_top10 {
    margin-top: 10px;
}

.submitBtn {
    display: flex;
    justify-content: flex-end;
}

.relation_ship {
    display: flex;
    flex-direction: column;
}

::v-deep .relation_custom_cascader {
    .cascader_name {
        width: 300px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }
}
.operation_option {
    width: 100px;
    margin-left: 5px;
}
.operation_info {
    margin-top: 5px;
    .operation_number {
        width: 100px;
    }
}
</style>
