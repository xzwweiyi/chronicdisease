<template>
    <div>
        <el-dialog v-bind="$attrs" width="700px" :close-on-click-modal="false" v-on="$listeners" @close="closeDialog"
            center top="7vh" class="installment_list">
            <div v-for="(item,index) in installmentList" :key="index" class="installment_items">
                <div class="installment_tit">当规则条件为：</div>
                <div class="installment_condition_list">
                    <div v-for="(a,i) in item.conditions" :key="i">
                        <span v-if="i>0">且</span>
                        <el-tag>{{ a.propertyName }}</el-tag>
                        选择值为
                        <el-tag>{{ a.selectValStr }}</el-tag>
                    </div>
                </div>
                <div class="installment_results display-flex">
                    <div class="installment_tips">显示结果为：</div>
                    <el-tag type="info">{{ item.ruleResult }}</el-tag>
                    <el-button class="clearable_radioVal" @click="deleteInstallmentInfo(index)" icon="el-icon-close"
                        circle type="danger" plain />
                </div>
            </div>
            <div class="form_btns">
                <el-button type="primary" @click="addInstallment">增加分期规则</el-button>
                <el-button type="primary" @click="confirmInstallment">确认</el-button>
                <el-button type="primary" plain @click="cancelInstallment">取消</el-button>
            </div>
        </el-dialog>
        <el-dialog :close-on-click-modal="false" :visible.sync="addInfoDialogVisible" @close="closeAddDialog" center
            title="新增分期规则" top="7vh" class="setinstallment_info">
            <el-form label-position="top">
                <el-form-item label="规则条件"></el-form-item>
                <el-form-item v-for="(item,index) in checkedDrawingList" :key="index">
                    <!-- <el-radio-group v-model="checkedDrawingList[index].selectVal">
                        <el-radio v-for="(listItem,listIndex) in item.options" :key="listIndex"
                            :label="''+listItem.value">
                            {{ listItem.label }}</el-radio>
                    </el-radio-group> -->
                    <!-- <el-button class="clearable_radioVal" @click="clearableVal(index)" icon="el-icon-close" circle type="danger" plain /> -->
                    <span v-if="index>0">且</span>
                    题目
                    <el-select v-model="checkedDrawingList[index].propertyCode"
                        @change="propertyInfoChange($event,index)" clearable>
                        <el-option v-for="(b,i) in defaultDraingList" :key="i" :value="b.propertyCode"
                            :label="b.propertyName" />
                    </el-select>
                    选项
                    <el-select v-model="checkedDrawingList[index].selectVal" @change="optionsValChange($event,index)"
                        clearable>
                        <el-option v-for="(b,i) in checkedDrawingList[index].options" :key="i" :value="b.value"
                            :label="b.label" />
                    </el-select>
                    <el-button class="clearable_radioVal" v-if="index==checkedDrawingList.length-1"
                        @click="addCheckedInfo" icon="el-icon-plus" circle plain />
                </el-form-item>
                <el-form-item label="规则结果">
                    <el-input type="textarea" v-model="ruleResult" :rows="3" clearable />
                </el-form-item>
                <div class="form_buttons">
                    <el-button type="primary" @click="confirmAddInfo">确定</el-button>
                    <el-button type="primary" plain @click="closeAddDialog">取消</el-button>
                </div>
            </el-form>
        </el-dialog>
    </div>
</template>
<script>
import {deepClone} from "@/utils/index"
    export default {
        components: {},
        inheritAttrs: false,
        props: ['currentItem', 'drawingList', 'setInstallmentVisible'],
        data() {
            return {
                installmentList: [],
                checkedDrawingList: [],
                defaultDraingList: [],
                addInfoDialogVisible: false,
                formData: {},
                ruleResult: '',
                defaultOptions: [],
            }
        },
        computed: {},
        watch: {
            currentItem: {
                handler(newVal, oldVal) {
                    if (newVal.propertyCode != oldVal.propertyCode) {
                        if (this.drawingList && this.drawingList.length) {
                            let newDrwingList = this.convertFieldsDataLoop(this.drawingList)
                            let currentIndex = newDrwingList.findIndex(f => f.propertyCode == this.currentItem
                                .propertyCode)
                            this.defaultDraingList = newDrwingList.filter((f, i) => i < currentIndex).filter((f, i) =>
                                f.labelType == 'Radio' || f.labelType == 'Select' )
                        }
                    }
                },
                deep: true
            },
            drawingList(newVal, oldVal) {},
            setInstallmentVisible(newVal) {
                if(this.currentItem.installmentList){
                    let newDrwingList = this.convertFieldsDataLoop(this.drawingList)
                    let installmentList = deepClone(this.currentItem.installmentList);
                    installmentList.forEach((item, index) => {
                        item.conditions.forEach((a, i) => {
                            let findIndex = newDrwingList.findIndex(f => f.propertyCode == a.propertyCode)
                            if (findIndex == -1) {
                                item.conditions.splice(i, 1)
                            } else {
                                if (newDrwingList[findIndex].propertyName != a.propertyName) {
                                    a.propertyName = newDrwingList[findIndex].propertyName
                                }
                                let optionIndex = newDrwingList[findIndex].options.findIndex(f => f.value ==
                                    a.selectVal)
                                if (optionIndex == -1) {
                                    a.selectVal = ''
                                    a.selectValStr = ''
                                } else if (a.selectValStr != newDrwingList[findIndex].options[optionIndex]
                                    .label) {
                                    a.selectValStr = newDrwingList[findIndex].options[optionIndex].label
                                }
                            }
                        })
                    })
                    this.installmentList = installmentList
                }else {
                    this.installmentList = []
                }

            }
        },
        created() {},
        mounted() {
        },
        methods: {
            convertFieldsDataLoop(array, parentIndex) {
                return array.reduce((prev, curr, index) => {
                    return prev.concat(Array.isArray(curr.children) ? this.convertFieldsDataLoop(curr.children,
                            index) :
                        curr)
                }, [])
            },
            closeDialog() {
                this.$emit('update:visible', false)
            },
            addInstallment() {
                this.addInfoDialogVisible = true;
                this.checkedDrawingList = [{
                    propertyCode: '',
                    selectVal: '',
                }]
            },
            confirmInstallment() {
                this.currentItem['installmentList'] = this.installmentList
                this.$emit('update:visible', false)
            },
            cancelInstallment() {
                this.$emit('update:visible', false)
            },
            deleteInstallmentInfo(index) {
                this.installmentList.splice(index, 1)
            },

            closeAddDialog() {
                this.addInfoDialogVisible = false;
                this.checkedDrawingList = [{
                    propertyCode: '',
                    selectVal: '',
                }]
                this.ruleResult = ''
            },
            addCheckedInfo() {
                if (this.checkedDrawingList.findIndex(f => !f.propertyCode) > -1 ||
                    this.checkedDrawingList.findIndex(f => !f.selectVal) > -1) {
                    this.$message.warning('还有已添加的条件没有设置')
                    return
                }
                this.checkedDrawingList.push({
                    propertyCode: '',
                    selectVal: '',
                })
            },
            propertyInfoChange(event, index) {
                let stempArray = []
                if (event) {
                    let currentInfo = this.defaultDraingList.find(f => f.propertyCode == event)
                    stempArray = [...currentInfo.options]
                    this.checkedDrawingList[index].propertyName = currentInfo.propertyName
                }
                if (this.checkedDrawingList.length && this.checkedDrawingList.filter(f => f.propertyCode == event)
                    .length > 1) {
                    this.$message.warning('已经有相同的题目')
                    this.checkedDrawingList[index].propertyCode = ''
                    return
                }
                this.checkedDrawingList[index].selectVal = ''
                this.checkedDrawingList[index].options = stempArray
            },
            optionsValChange(event, index) {
                this.checkedDrawingList[index].selectValStr = this.checkedDrawingList[index].options.find(f => f
                    .value == event).label
            },
            // 确定添加的数据
            confirmAddInfo() {
                let selectValList = this.checkedDrawingList.filter(f => f.selectVal).map(m => {
                    return {
                        propertyCode: m.propertyCode,
                        propertyName: m.propertyName,
                        selectVal: m.selectVal,
                        selectValStr: m.options.find(f => f.value == m.selectVal).label
                    }
                })
                let addObj = {
                    ruleResult: this.ruleResult,
                    conditions: selectValList
                }
                if (!addObj.ruleResult) {
                    this.$message.warning('请填写对应的规则结果！')
                    return
                }

                console.log('selectValList', addObj)
                this.installmentList.push(addObj)
                this.addInfoDialogVisible = false;
                this.checkedDrawingList = [{
                    propertyCode: '',
                    selectVal: '',
                }]
                this.ruleResult = ''
            }

        }
    }
</script>
<style lang="scss" scoped>
    ::v-deep .el-dialog__body {
        height: 70vh;
        overflow-y: auto;
    }

    .display-flex {
        display: flex;
        align-items: center;
    }

    .clearable_radioVal {
        padding: 3px;
        margin-left: 10px;
    }

    .installment_list {
        .el-tag {
            margin: 5px 5px;
        }

        .installment_items {
            width: 100%;

            .installment_condition_list {
                display: block;
                margin-left: 20px;
            }

            .installment_tit {
                width: 100%;
                font-weight: bold;
            }

            .installment_results {

                .installment_tips {
                    font-weight: bold;
                }
            }

        }

        .form_btns {
            display: flex;
            justify-content: center;
            margin: 15px auto;

            .el-button {
                &:first-child {
                    margin-right: 15px;
                }
            }
        }
    }

    .setinstallment_info {
        ::v-deep .el-form-item__label {
            padding-bottom: 0;
        }

        ::v-deep .el-form-item__content {
            margin-left: 30px;
        }

        .el-radio {
            line-height: 30px;
        }

        .form_buttons {
            display: flex;
            justify-content: center;

            .el-button {
                &:first-child {
                    margin-right: 15px;
                }
            }
        }
    }
</style>