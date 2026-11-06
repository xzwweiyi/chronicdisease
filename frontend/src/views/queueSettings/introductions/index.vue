<template>
    <div class="app-container">
        <el-breadcrumb separator-class="el-icon-arrow-right"
            style="font-size:16px; margin-bottom: 12px; font-weight: 800;color: #1890ff !important;">
            <el-breadcrumb-item class="myColor">队列管理</el-breadcrumb-item>
            <el-breadcrumb-item class="myColor">{{addform&&addform.deptName}}</el-breadcrumb-item>
            <el-breadcrumb-item class="myColor">{{addform&&addform.diseasesName}}</el-breadcrumb-item>
            <el-breadcrumb-item class="myColor">{{addform&&addform.projectName}}</el-breadcrumb-item>
        </el-breadcrumb>

        <el-tabs type="border-card" v-model="activeName" @tab-click="handleTab">
            <el-tab-pane label="编辑队列简介" name="first">
                <el-scrollbar class="create_queue_dialog" ref="createQueueScroll">
                    <step-one :researchId="researchId" :addform="addform" @nextSubmitTwo="nextSubmitTwo"
                        v-if="tabRefresh.first"></step-one>
                </el-scrollbar>
            </el-tab-pane>
            <el-tab-pane label="设置随访周期" name="two">
                <step-two :researchId="researchId" :nameOptions="nameOptions" @nextSubmitThree="nextSubmitThree"
                    @nextSubmitThreeSave="nextSubmitThreeSave" v-if="tabRefresh.two"></step-two>
            </el-tab-pane>
            <el-tab-pane label="设置随访CRF" name="three">
                <step-three :researchId="researchId" @nextSubmitFour="nextSubmitFour"
                    @nextSubmitFourSave="nextSubmitFourSave" v-if="tabRefresh.three"></step-three>
            </el-tab-pane>
            <el-tab-pane label="设置队列患教" name="four">
                <step-four :researchId="researchId" @nextSubmitFive="nextSubmitFive"
                    @nextSubmitFiveSave="nextSubmitFiveSave" @nextSubmitFiveSaveClick="nextSubmitFiveSaveClick"
                    v-if="tabRefresh.four"></step-four>
            </el-tab-pane>
            <el-tab-pane label="设置智能外呼" name="five">
                <step-five :researchId="researchId" @nextSubmitSix="nextSubmitSix"
                    @nextSubmitSixSave="nextSubmitSixSave" v-if="tabRefresh.five"></step-five>
            </el-tab-pane>
            <el-tab-pane label="导入随访患者" name="six">
                <el-scrollbar class="create_queue_dialog">
                    <step-six :researchId="researchId" @nextSubmitSeven="nextSubmitSeven"
                        @nextSubmitSevenSave="nextSubmitSevenSave" :deptId="deptId" v-if="tabRefresh.six"></step-six>
                </el-scrollbar>
            </el-tab-pane>
            <el-tab-pane label="分配随访人员" name="seven">
                <step-seven :researchId="researchId" @nextSubmitEightSaveClick="nextSubmitEightSaveClick"
                    v-if="tabRefresh.seven"></step-seven>
            </el-tab-pane>
            <el-tab-pane label="设置队列状态" name="eight">
                <step-eight :researchId="researchId" v-if="tabRefresh.eight"></step-eight>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
import cache from '@/plugins/cache'
import {
    classify,
    nameOptions
} from '@/utils/generator/queue'
import {
    infoAdd,
    infoEdit,
    infoQuery,
    selectDiseases,
    updateStageData,
} from '@/api/info/config'
import stepOne from '@/views/queueSettings/child/one'
import stepTwo from '@/views/queueSettings/child/two'
import stepThree from '@/views/queueSettings/child/three'
import stepFour from '@/views/queueSettings/child/four'
import stepFive from '@/views/queueSettings/child/five'
import stepSix from '@/views/queueSettings/child/six'
import stepSeven from '@/views/queueSettings/child/seven'
import stepEight from '@/views/queueSettings/child/eight'
export default {
    components: {
        stepOne,
        stepTwo,
        stepThree,
        stepFour,
        stepSix,
        stepSeven,
        stepEight,
        stepFive
    },
    name: "introductions",
    data() {
        return {
            nameOptions,
            // 表单参数
            addform: { queueType: '0' },
            // 表单参数
            activeName: 'first', // el-tabs active
            cycleList: [], // 周期列表
            deptId: undefined,
            tabRefresh: {
                first: true, two: false, three: false, four: false,
                five: false, six: false, seven: false, eight: false,
            },
            // 表单校验
            rules: {
                projectName: [{ required: true, message: "队列名称不能为空", trigger: "blur" }],
                researchSynopsis: [{ required: true, message: "队列介绍不能为空", trigger: "blur" }],
                imageUrl: [{ required: true, message: "队列展示图不能为空", trigger: "change" }],
                doctorId: [{ required: true, message: "队列负责人不能为空", trigger: "change" }],
                researchScheme: [{ required: true, message: "队列负责人不能为空", trigger: "change" }],
                researchStandard: undefined,
            },
        };
    },
    watch: {
        "$route.query.id": {
            immediate: true, //第一次的数据，也要当做是一种变化
            // deep: true,
            handler(newVal, oldVal) {
                if (newVal != undefined && newVal != 0) {
                    this.getInfoQuery();
                }
            }
        },
        "$route.query.stage": {
            immediate: true, //第一次的数据，也要当做是一种变化
            // deep: true,
            handler(newVal, oldVal) {
                if (newVal != undefined) {
                    switch (newVal) {
                        case '0':
                            this.$route.meta.title = "队列简介"
                            this.activeName = "first"
                            this.switchTab('first')
                            break
                        case '1':
                            this.$route.meta.title = "设置随访周期"
                            this.activeName = "two"
                            this.switchTab('two')
                            break
                        case '2':
                            this.$route.meta.title = "设置随访CRF"
                            this.activeName = "three"
                            this.switchTab('three')
                            break
                        case '3':
                            this.$route.meta.title = "设置队列患教"
                            this.activeName = "four"
                            this.switchTab('four')
                            break
                        case '4':
                            this.$route.meta.title = "设置智能外呼"
                            this.activeName = "five"
                            this.switchTab('five')
                            break
                        case '5':
                            this.$route.meta.title = "导入随访患者"
                            this.activeName = "six"
                            this.switchTab('six')
                            break
                        case '6':
                            this.$route.meta.title = "分配随访人员"
                            this.activeName = "seven"
                            this.switchTab('seven')
                            break
                        case '7':
                            this.$route.meta.title = "设置队列状态"
                            this.activeName = "eight"
                            this.switchTab('eight')
                            break
                    }
                }
            }
        }

    },
    computed: {
        showLeftIcon() {
            return this.clickNum > 0
        },
        researchId: {
            get() {
                return this.$route.query.id
            },
            set(val) {
                this.$route.query.id = val
            }
        },
    },
    created() {
        this.researchId = this.$route.query.id;
        //
        // if(this.researchId != undefined && this.researchId != 0) {
        //     this.getInfoQuery();
        // }
    },
    methods: {
        // tab切换触发
        handleTab(tab) {
            if (this.researchId != 0 && this.researchId != null) {
                this.activeName = tab.name
                switch (this.activeName) {
                    case 'first':
                        this.$route.meta.title = "队列简介"
                        this.getInfoQuery()
                        this.switchTab('first')
                        break
                    case 'two':
                        this.$route.meta.title = "设置随访周期"
                        this.switchTab('two')
                        break
                    case 'three':
                        this.$route.meta.title = "设置随访CRF"
                        this.switchTab('three')
                        break
                    case 'four':
                        this.$route.meta.title = "设置队列患教"
                        this.switchTab('four')
                        break
                    case 'five':
                        this.$route.meta.title = "设置智能外呼"
                        this.switchTab('five')
                        break
                    case 'six':
                        this.$route.meta.title = "导入随访患者"
                        this.switchTab('six')
                        break
                    case 'seven':
                        this.$route.meta.title = "分配随访人员"
                        this.switchTab('seven')
                        break
                    case 'eight':
                        this.$route.meta.title = "设置队列状态"
                        this.switchTab('eight')
                        break
                }
            }

        },
        switchTab(tab) {
            for (const key in this.tabRefresh) {
                if (key == tab) {
                    this.tabRefresh[key] = true
                } else {
                    this.tabRefresh[key] = false
                }
            }
        },
        /**
         * 获取队列详情
         */
        getInfoQuery() {
            const id = this.researchId;
            infoQuery(id).then(response => {
                this.addform = response.data
                this.deptId = this.addform && this.addform.deptId
                if (this.addform && this.addform.researchStatus != undefined) {
                    cache.session.setJSON('researchStatus', this.addform.researchStatus)
                }
                this.$nextTick(() => {
                    if (this.$refs['createQueueScroll']) {
                        this.$refs['createQueueScroll'].wrap.scrollTop = 0
                    }
                })
            })
        },
        // 根据阶段完成情况，跳转设置CRF 
        nextSubmitTwo(ids) {
            this.activeName = 'two'
            this.switchTab('two')
        },
        // 根据阶段完成情况，跳转设置CRF 
        nextSubmitThree() {
            let id = this.researchId
            let stage = '2'
            updateStageData(id, stage).then(res => {
                this.activeName = 'three'
                this.switchTab('three')
            })
        },
        nextSubmitThreeSave() {
            let id = this.researchId
            let stage = '2'
            updateStageData(id, stage).then(res => {
                this.$message.success('保存成功')
            })
        },
        // 根据阶段完成情况，跳转患教配置页面 
        nextSubmitFour() {
            let id = this.researchId
            let stage = '3'
            updateStageData(id, stage).then(res => {
                this.activeName = 'four'
                this.switchTab('four')
            })
        },
        nextSubmitFourSave() {
            let id = this.researchId
            let stage = '3'
            updateStageData(id, stage).then(res => {
                this.$message.success('保存成功')
            })
        },
        // 根据阶段完成情况，跳转智能外呼页面
        nextSubmitFive() {
            let id = this.researchId
            let stage = '4'
            updateStageData(id, stage).then(res => {
                this.activeName = 'five'
                this.switchTab('five')
            })
        },
        nextSubmitFiveSave() {
            let id = this.researchId
            let stage = '4'
            updateStageData(id, stage).then(res => {
                this.$message.success('保存成功')
            })
        },
        nextSubmitFiveSaveClick() {
            let id = this.researchId
            let stage = '4'
            updateStageData(id, stage).then(res => {
                this.activeName = 'five'
                this.switchTab('five')
            })
        },
        // 根据阶段完成情况，跳转智能外呼页面
        nextSubmitSix() {
            let id = this.researchId
            let stage = '5'
            updateStageData(id, stage).then(res => {
                this.activeName = 'six'
                this.switchTab('six')
            })
        },
        nextSubmitSixSave() {
            let id = this.researchId
            let stage = '5'
            updateStageData(id, stage).then(res => {
                this.$message.success('保存成功')
            })
        },

        // 根据阶段完成情况，跳转随访患者
        nextSubmitSeven() {
            let id = this.researchId
            let stage = '6'
            updateStageData(id, stage).then(res => {
                this.activeName = 'seven'
                this.switchTab('seven')
            })
        },
        nextSubmitSevenSave() {
            let id = this.researchId
            let stage = '6'
            updateStageData(id, stage).then(res => {
                this.$message.success('保存成功')
            })
        },

        nextSubmitEightSaveClick() {
            let id = this.researchId
            let stage = '7'
            updateStageData(id, stage).then(res => {
                this.activeName = 'eight'
                this.switchTab('eight')
            })
        },
    },
};
</script>

<style lang="scss" scoped>
.app-container {
    height: calc(100vh - 114px);
}

.tabPane {
    display: block;
    position: relative;
    height: calc(100vh - 250px);
    overflow: auto;
}

.boxLine {
    display: block;
    width: 100%;
    padding: 20px 20px;
    margin: 10px 0;
    border: 1px dashed #dcdddd;
}

.el-breadcrumb ::v-deep .el-breadcrumb__inner {
    color: #1890ff !important;
    font-weight: bold !important; //你想要设置的字体颜色
}

.create_queue_dialog {
    height: calc(100vh - 250px);
}
</style>