<template>
    <div class="user-detail-container" v-loading="isLoading">
        <el-row :gutter="20">
            <!--部门数据-->
            <el-col :span="4" :xs="24">
                <el-scrollbar class="head-container">
                    <el-tree :data="deptOptions" :props="defaultProps" highlight-current
                        :filter-node-method="filterNode" ref="leftTree" accordion node-key="id"
                        :current-node-key="currentNodeKey" default-expand-all @node-click="handleNodeClick" />
                </el-scrollbar>
            </el-col>
            <!--用户数据-->
            <el-col :span="20" :xs="24">
                <el-form ref="infoPatientInfo" :model="infoPatientInfo" class="el-form-top" label-width="100px">
                    <el-row>
                        <el-col :span="6">
                            <el-form-item label="患者姓名：" prop="patientName">
                                {{infoPatientInfo.patientName}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="性别：" prop="sex">
                                {{(infoPatientInfo.sex == 1 ? '男' : '女')}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="患者手机：" prop="patientMobile">
                                {{infoPatientInfo.patientMobile}}
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="创建时间：" prop="createTime">
                                {{infoPatientInfo.createTime | dateFormat}}
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
                <!-- 内容开始 -->
                <!-- 内容结束 -->
                <el-scrollbar class="descriptions_form_info">
                    <el-descriptions :column="1" :size="size" border>
                        <el-descriptions-item v-for="(item, index) in appMedicalInfoArray" :key="index"
                            label-class-name="descriptions_label">
                            <template slot="label">
                                {{item.propertyName ||item.formName}}
                            </template>
                            <template>
                                <div class="description_info">
                                    <div class="properties_list" v-if="item.children&&item.children.length">
                                        <div v-for="(ittem,ittemIndex) in item.children" :key="ittemIndex"
                                            class="properties_item">
                                            <div class="properties_item_compont">
                                                <div class="property_name">{{ittem.propertyName}} ：</div>
                                                <component-echo class="property_value" :info="ittem">
                                                </component-echo>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="patient_record_files"
                                        v-else-if="item.patientRecordFiles&&item.patientRecordFiles.length">
                                        <div class="property_name">拍照项 ：</div>
                                        <!-- <el-image class="patient_record_img" :src="ittem.imageUrl"
                                            v-for="(ittem,ittemIndex) in item.patientRecordFiles" fit="contain"
                                            :key="ittemIndex" :preview-src-list="item.imageUrlLst">
                                        </el-image> -->
                                        <el-image class="patient_record_img"
                                            v-for="(ittem,ittemIndex) in item.patientRecordFiles" :key="ittemIndex"
                                            :src="ittem.imageUrl" :preview-src-list="item.imageUrlLst" fit="contain">
                                            <div slot="error" class="image-slot">
                                                <i class="el-icon-picture-outline"></i>
                                            </div>
                                            <div slot="placeholder" class="image-slot">
                                                加载中<span class="dot">...</span>
                                            </div>
                                        </el-image>
                                    </div>
                                    <component-echo class="info_val" :info="item" v-else></component-echo>
                                </div>
                            </template>
                        </el-descriptions-item>
                    </el-descriptions>
                </el-scrollbar>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import {
    listLefTree
} from "@/api/info/config";
import {
    workEdcDetail,
    subEdcInsert,
    getListQuestionRecord,
    quseiontList, //质疑列表
} from "@/api/follow/work";
import {
    getToken
} from "@/utils/auth";
import cityData from '@/utils/city.json'
import ComponentEcho from '../../workstation/components/assembly'
import EditForm from '../../workstation/components/editForm'
import EditItem from '../../workstation/components/editItem.vue'
export default {
    name: "patintDetail",
    components: {
        ComponentEcho,
        EditForm,
        EditItem
    },
    data() {
        return {
            // 遮罩层
            isLoading: true,
            defaultProps: {
                children: "childrenList",
                label: "nodeName"
            },
            deptOptions: [],
            size: 'medium',
            infoPatientInfo: {},
            infoMedicalInfo: {},
            infoMedicalInfoArray: [],
            appMedicalInfo: {},
            appMedicalInfoArray: [],
            title: null,
            formData: {},
            listQuestionRecord: [], //EDC 质疑记录
            FllowQuestionRecord: [], //随访质疑记录
            fromContent: '',
            currentNodeKey: '',
            patientId: '',
            followId: '',
            recordId: '',
            medicalFormId: '', //表单id
        };
    },
    created() {
        this.initData()
    },
    watch: {
        '$route.query.patientId'(newVal) {
            if (newVal) {
                this.initData()
            }
        }
    },
    methods: {
        initData() {
            let queryData = this.$route.query;
            this.researchId = queryData.researchId;
            this.patientId = queryData.patientId;
            this.followId = queryData.followId;
            this.recordId = queryData.recordId;
            this.getListLefTree()
        },
        getListLefTree() {
            this.isLoading = true
            let dataObj = {
                patientId: Number(this.$route.query.patientId),
                researchId: Number(this.$route.query.researchId)
            }
            listLefTree(dataObj).then(response => {
                let responseData = response.data
                let newArr = []
                responseData.forEach((item,index)=>{
                    if(index==0||newArr.findIndex(f=>f.id==item.id)==-1){
                        newArr.push(item)
                    }
                })
                this.deptOptions = newArr.map(item => {
                    return {
                        ...item,
                        id: item.id && item.id.toString(),
                        childrenList: item.childrenList&&item.childrenList.length?item.childrenList.map(m => {
                            return {
                                ...m,
                                id: m.id && m.id.toString()
                            }
                        }):undefined
                    }
                })
                if (this.deptOptions !== undefined && this.deptOptions != null && this.deptOptions.length >
                    0) {
                    let currentTreeInfo = this.deptOptions[0].childrenList[0];
                    this.currentNodeKey = currentTreeInfo.id
                    this.researchId = currentTreeInfo.researchId
                    // this.recordId = currentTreeInfo.recordId;
                    this.$nextTick(function () {
                        this.$refs.leftTree.setCurrentKey(this.currentNodeKey)
                    })
                    this.getEDCWorkList(currentTreeInfo)
                }
                this.isLoading = false
            })
        },
        handleNodeClick(data) {
            this.researchId = data.researchId
            this.currentNodeKey = data.id
            // this.recordId = data.recordId;
            if (data.childrenList == null) {
                this.getEDCWorkList(data)
            }
        },
        // 筛选节点
        filterNode(value, data) {
            if (!value) return true;
            return data.label.indexOf(value) !== -1;
        },
        //模块详情
        getEDCWorkList(item) {
            let dataObj = {
                medicalFormId: item.id,
                researchId: item.researchId,
                patientId: item.patientId,
                followId: item.followId,
                recordId:this.recordId
            }
            this.isLoading = true;
            workEdcDetail(dataObj).then(response => {
                this.info = response.data
                this.infoPatientInfo = this.info.patientInfo
                let appMedicalInfo = this.info.appMedicalInfo
                this.appMedicalInfoArray = this.convertLeftMedicalInfoArray(appMedicalInfo)
                this.appMedicalInfoArray.sort((a, b) => {
                    return a.sortNum - b.sortNum
                })
                this.isLoading = false;
            }).catch(error => {
                this.infoPatientInfo = {};
                this.appMedicalInfoArray = [];
            })
        },
        // 重置左侧值显示
        convertLeftMedicalInfoArray(valueListInfo) {
            let medicalInfoList = []
            if (valueListInfo && valueListInfo.medicalProperties &&
                valueListInfo.medicalProperties.length) {
                valueListInfo.medicalProperties.forEach(element => {
                    const {
                        attribute,
                        ...itemInfo
                    } = element
                    let infoData = {
                        ...JSON.parse(element.attribute),
                        ...itemInfo,
                        id: element.id.toString()
                    }
                    if (infoData.defaultValue) {
                        if (infoData.labelType == 'Radio' || infoData.labelType == 'Select') {
                            let a = infoData.options.find(f => f.value === infoData
                                .defaultValue)
                            infoData['defaultValue'] = a.label
                        } else if (infoData.labelType == 'Checkbox') {
                            let arrCheckbox = []
                            infoData.defaultValue.map(item => {
                                let a = infoData.options.find(f => f.value === item)
                                arrCheckbox.push(a.label)
                            })
                            infoData['defaultValue'] = arrCheckbox.toString()
                        }
                    }
                    medicalInfoList.push(infoData)
                });
            }
            if (valueListInfo && valueListInfo.medicalFormList && valueListInfo.medicalFormList
                .length) {
                valueListInfo.medicalFormList.forEach(element => {
                    const {
                        attribute,
                        ...itemInfo
                    } = element
                    let infoData = {
                        ...JSON.parse(element.attribute),
                        ...itemInfo,
                        children: this.convertLeftMedicalInfoArray(element, true)
                    }
                    if (element.patientRecordFiles && element.patientRecordFiles.length) {
                        infoData['imageUrlLst'] = element.patientRecordFiles.map(m => m.imageUrl)
                    }
                    medicalInfoList.push(infoData)
                })
            }
            return medicalInfoList
        },
    }
};
</script>

<style lang="scss">
.user-detail-container {
    background: #fff;
    margin: 10px;
    padding: 20px 10px 20px 0;

    .head-container {
        height: calc(100vh - 160px);

        .el-tree {
            padding: 10px 0;
            background: rgba(24, 144, 255, 0.05);
            border-radius: 5px;
        }

        .custom-tree-node {
            width: 80%;
        }

        .el-tree-node {
            border-top: 1px #f8f8f8 solid;
        }

        .search_form {
            border-radius: 5px;
        }
    }

    .description_info {
        display: flex;
        flex: 1;

        .info_val {
            flex: 1;
        }

        .properties_list {
            width: 100%;
        }
    }

    .button_style {
        display: block;
        margin: 20px auto 0;
        text-align: center;
    }

    .descriptions_form_info {
        height: calc(100vh - 290px);

        .descriptions_item {
            padding: 5px;
        }

        .descriptions_item_component {
            width: 100%;

            .el-cascader {
                width: 80%;
            }
        }

        .el-descriptions-item__cell {
            padding: 15px 10px;

            .replay_content {
                display: flex;
                align-items: center;
                margin-top: 5px;

                .el-input {
                    flex: 1;
                }
            }
        }
    }

    .el-descriptions {
        .el-descriptions__header {
            margin: 15px 0 10px;
            padding: 8px 0;
            font-weight: bold;
            text-indent: 12px;
            border-left: 2px #409eff solid;
            background: rgba(24, 144, 255, 0.08);
        }
    }

    .properties_item {
        color: #333;
        margin: 5px 0;
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        flex-wrap: wrap;

        .property_value {
            color: #666;
        }

        .properties_item_compont {
            display: flex;
        }
    }

    .descriptions_label {
        background: rgba(24, 144, 255, 0.05);
        color: #666;
        width: 200px;
    }

    .question_record_label {
        padding: 0 !important;
    }

    .upload_recordFiles {
        width: 200px;
        height: 100px;
    }

    .el-radio-group {
        width: 100%;
    }

    .patient_record_files {
        .patient_record_img {
            width: 200px;
            height: 100px;
        }
    }
}
</style>
