<template>
    <div class="tabPane">
        <div v-if="isShow" v-loading="loading">
            <el-scrollbar class="tree-box">
                <el-tree :props="treeProps" :data="treeData" simple-data @node-click="handleClickNode"
                    highlight-current>
                    <span class="custom-tree-node" slot-scope="{ data }">
                        <i class="el-icon-folder-opened"></i>
                        {{ data.name }}
                    </span>
                </el-tree>
            </el-scrollbar>
            <div class="content_box">
                <div class="table-box">
                    <div class="left-box">
                        <h3>待添加({{ leftData.length }}/{{ leftData.length }})</h3>
                        <el-table :data="leftData" force-scroll @row-click="clickRowLeft" ref="selectTableLeft"
                            @selection-change="handleLeftSelect" :height="tableHeight">
                            <el-table-column type="selection"></el-table-column>
                            <el-table-column prop="title" :show-overflow-tooltip="true" label="标题">
                                <template slot-scope="scope">
                                    <el-button type="text"
                                        @click.stop="handleArticleDetail(scope.row.id, scope.row.type)">
                                        {{scope.row.title}}</el-button>
                                </template>
                            </el-table-column>
                            <el-table-column prop="positionType" label="类型" width="80">
                                <template slot-scope="scope">
                                    {{scope.row.type | convertArticleType }}
                                </template>
                            </el-table-column>
                            <!-- <el-table-column prop="createTime" label="日期" width="100">
                                <template slot-scope="scope">
                                    {{ scope.row.createTime | dateFormat }}
                                </template>
                            </el-table-column> -->
                        </el-table>
                    </div>
                    <div class="btn-box">
                        <el-button @click="moveRight" :disabled="!leftSelect.length!=0">
                            <i class="h-icon-angle_right_sm el-icon-arrow-right"></i>
                        </el-button>
                        <el-button @click="moveLeft" :disabled="!rightSelect.length!=0">
                            <i class="h-icon-angle_left_sm el-icon-arrow-left"></i>
                        </el-button>
                    </div>
                    <div class="right-box">
                        <h3>已添加({{ rightData.length }}/{{ rightData.length }})</h3>
                        <el-table :data="rightData" force-scroll @row-click="clickRowRight" ref="selectTableRight"
                            @selection-change="handleRightSelect" :height="tableHeight">
                            <el-table-column type="selection"></el-table-column>
                            <el-table-column prop="title" :show-overflow-tooltip="true" label="标题">
                                <template slot-scope="scope">
                                    <el-button type="text"
                                        @click.stop="handleArticleDetail(scope.row.id, scope.row.type)">
                                        {{scope.row.title}}</el-button>
                                </template>
                            </el-table-column>
                            <el-table-column prop="positionType" label="类型" width="80">
                                <template slot-scope="scope">
                                    {{scope.row.type | convertArticleType }}
                                </template>
                            </el-table-column>
                            <!-- <el-table-column prop="createTime" label="日期" width="120">
                                <template slot-scope="scope">
                                    {{ scope.row.createTime | dateFormat}}
                                </template>
                            </el-table-column> -->
                        </el-table>
                    </div>
                </div>
                <div class="dialog-footer">
                    <el-button type="primary" :disabled="rightData.length == 0" @click="handleSubmit('save')">保 存
                    </el-button>
                    <el-button type="primary" :disabled="rightData.length == 0" @click="handleSubmit('next')">保存并进入下一步
                    </el-button>
                    <el-button @click="handleCancel">取 消</el-button>
                </div>
            </div>
        </div>
        <div v-else>
            <el-form :model="articleForm" ref="articleForm" :inline="true" label-width="90px">
                <el-form-item label="标题名称" prop="title">
                    <el-input v-model="articleForm.title" placeholder="请输入标题名称" clearable style="width: 240px"
                        @keyup.enter.native="handleQuery" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
                    <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
                </el-form-item>
            </el-form>

            <el-table v-loading="loading" :data="articleData">
                <el-table-column label="标题" align="left" key="title" prop="title" v-if="columns[0].visible"
                    :show-overflow-tooltip="true" min-width="300">
                    <template slot-scope="scope">
                        <el-button type="text" @click="handleArticleDetail(scope.row.id, scope.row.type)">
                            {{scope.row.title}}</el-button>
                    </template>
                </el-table-column>
                <el-table-column label="类型" align="center" key="positionType" prop="positionType" width="80">
                    <template slot-scope="scope">
                        {{scope.row.type | convertArticleType }}
                    </template>
                </el-table-column>
                <el-table-column label="状态" align="center" key="positionType1" prop="positionType1" width="80">
                    <template slot-scope="scope">
                        <el-tag :type="scope.row.delFlag=='N'?'error':''">
                            {{scope.row.delFlag | convertArticleDelFlag }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="首页位状态" align="center" key="positionType2" prop="positionType2" width="100">
                    <template slot-scope="scope">
                        {{scope.row.indexType | convertArticleIndexType }}
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" fixed="right" width="220">
                    <template slot-scope="scope" v-if="scope.row.userId !== 1">
                        <el-button type="text" @click="showIndexSetDialog(scope.row)" v-if="scope.row.type=='1'||scope.row.type=='3'"
                            v-hasPermi="['research:article:indexPosition']">首页位设置</el-button>
                        <el-button type="text" @click="handleArticleDetail(scope.row.id, scope.row.type)"
                            v-hasPermi="['system:article:query']">查看</el-button>
                        <el-button type="text" class="el-button-text_delete"
                            @click="handleRemoveArticle(scope.row)">移除队列</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div slot="footer" class="dialog-footer" style="margin:20px auto;text-align: center;">
                <el-button type="primary" @click="handleArticle" :disabled="researchStatus === '2'">添加患教内容</el-button>
                <el-button type="primary" @click="handleNext" :disabled="researchStatus === '2'">进入下一步</el-button>
            </div>

        </div>
        <!-- 患教文章详情详情 -->
        <el-dialog :title="articleContent.title" :visible.sync="articleContentOpen" width="70%" top="8vh">
            <div class="sketch_content" v-loading="isLoadingDetails">
                <div class="reveal-container" v-html="articleContent.content" v-if="articleContent.content"></div>
                <el-empty v-else></el-empty>
            </div>
        </el-dialog>
        <!-- 患教问卷详情详情 -->
        <el-dialog :visible.sync="isShowDetails" width="70%" center v-loading="isLoadingDetails" top="8vh"
            :title="formList.medicalName">
            <el-scrollbar class="details-scrollbar">
                <DetailInfo v-if="formList.subjectList&&formList.subjectList.length" :form-list="formList.subjectList"
                    :error-message="errorMessage"></DetailInfo>
                <el-empty v-else></el-empty>
            </el-scrollbar>
        </el-dialog>
        <el-dialog :visible.sync="isShowSetIndexPage" center title="首页位设置" width="400px" top="28vh">
            <el-form label-position="top" class="set_article_info">
                <el-form-item label="请选择该文章的位置">
                    <el-radio-group v-model="articleIndexType">
                        <el-radio label="0">无</el-radio>
                        <el-radio label="1">轮播图</el-radio>
                        <el-radio label="2">首页列表</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <div class="dialog_btns">
                <el-button type="primary" @click="submitIndexSetInfo">确认</el-button>
                <el-button type="primary" plain @click="cancleSetIndex">取消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import cache from '@/plugins/cache'
import {
    listEduData, articleAdd, listArticleData, delArticle, setindexPosition
} from '@/api/info/config'
import { getArticleDetail } from '@/api/patient/config'
import DetailInfo from './details'
export default {
    props: {
        researchId: { type: String, default: null, },
    },
    components: {
        DetailInfo
    },
    data() {
        return {
            loading: true,
            tableHeight: 'calc(100vh - 350px)',
            //队列患教
            articleData: [],
            // 查询参数
            articleForm: {
                researchId: '',
                articleId: '',
                diseaseId: '',
                title: '',
            },
            isShow: false, // 当前显示模块
            // 列信息
            columns: [
                { key: 0, label: `标题`, visible: true },
                { key: 1, label: `类型`, visible: true },
                { key: 2, label: `状态`, visible: true },
            ],
            list: [], // 接口获取到的右侧数据列表
            treeProps: { label: 'name', value: 'id', },
            treeData: [],
            expandNode: [],
            currentNode: "",
            treeSelect: {},
            leftData: [], // 待添加数据
            rightData: [], // 已添加数据
            leftSelect: [], // 勾选的待添加数据
            rightSelect: [], // 勾选的已添加数据
            copyRightData: [], // 拷贝已添加数据
            researchStatus: '',
            articleContentOpen: false,
            articleContent: { title: '' },
            isShowDetails: false,
            isLoadingDetails: false,
            formList: [],
            errorMessage: '',
            currentArticleInfo: {},
            isShowSetIndexPage: undefined,
            articleIndexType: '0',
        }
    },
    watch: {
        researchId: {
            immediate: true, // 监听到后，立即执行 handler方法
            handler(newVal, oldValue) {
                if (newVal && newVal != null) {
                    this.getListArticle()
                    this.researchStatus = cache.session.getJSON('researchStatus')
                }
            }
        }
    },
    methods: {
        // 获取保存的患教列表
        getListArticle() {
            this.loading = true
            this.articleForm.researchId = this.researchId
            listArticleData(this.articleForm).then(response => {
                if (response)
                    this.articleData = response.data
                this.loading = false
            }).catch(error => {
                console.log(error)
            })
        },
        // 患教列表
        getArticle() {
            this.loading = true
            listEduData().then(response => {
                this.treeData = response.data
                this.loading = false;
            })
        },
        // 患教时间格式
        getPartTime(val) {
            // let ti = val.split(' ');
            var timearr = val.replace(" ", ":").replace(/:/g, "-").split("-");
            var timestr = timearr[0] + "/" + Number(timearr[1]) + "/" + timearr[2];
            // console.log(4444,val)
            return timestr;
        },
        // 添加患教内容
        handleArticle() {
            this.isShow = true;
            this.rightData = this.articleData
            this.$nextTick(() => {
                this.getArticle()
            })
        },
        // 保存患教
        handleSubmit(valData) {
            let list = []
            // 更新接口获取到的右侧数据列表，以进行保存操作
            this.rightData.forEach(item => {
                item = {
                    researchId: this.researchId,
                    articleId: item.id,
                    categoryId: item.categoryId,
                }
                list.push(item)
            })
            articleAdd(list).then(response => {
                // this.$message.success('保存成功111')
                this.isShow = false;
                this.getListArticle()
                this.$nextTick(() => {
                    if (valData == 'next') {
                        this.$emit('nextSubmitFive', true)
                    } else if (valData == 'save') {
                        this.$emit('nextSubmitFiveSave', true)
                    }
                })
            })
        },
        // 患教详情
        handleArticleDetail(ids, type) {
            if (type == '1') {
                this.articleContentOpen = true;
                this.isLoadingDetails = true;
                getArticleDetail(ids).then(response => {
                    this.articleContent = response.data
                    this.isLoadingDetails = false;
                }).catch(error => {
                    this.isLoadingDetails = false;
                })
            } else {
                this.isShowDetails = true;
                this.isLoadingDetails = true;
                getArticleDetail(ids).then(response => {
                    this.isLoadingDetails = false;
                    let resData = response.data.fmedicalExamForm || response.data.fMedicalExamForm;
                    if (resData) {
                        if (resData.subjectList && resData.subjectList.length) {
                            resData.subjectList = resData.subjectList.map(item => {
                                const {
                                    attribute,
                                    ...otherInfo
                                } = item
                                return {
                                    ...otherInfo,
                                    ...JSON.parse(attribute),
                                    disabled: true,
                                }
                            })
                        } else {
                            this.errorMessage = '暂无问卷信息'
                        }
                        this.formList = resData;
                    } else {
                        this.errorMessage = response.errorMessage
                    }
                }).catch(error => {
                    this.isLoadingDetails = false;
                })
            }

        },
        // 进入下一步
        handleNext() {
            this.$emit('nextSubmitFiveSaveClick', true)
        },
        // 删除患教
        handleRemoveArticle(item) {
            let itemData = {
                researchId: this.researchId,
                articleId: item.id,
                categoryId: item.categoryId,
            }

            this.$confirm(`确定要删除吗?`, '提示', {
                type: 'warning'
            }).then(() => {
                delArticle(itemData).then(response => {
                    this.getListArticle()
                    this.$modal.msgSuccess(`删除成功`)
                })
            }).catch(error => { })
        },
        clickRowLeft(row) {
            this.$refs.selectTableLeft.toggleRowSelection(row);
        },
        // 待添加数据选中
        handleLeftSelect(data) {
            this.leftSelect = data;
        },

        clickRowRight(row) {
            this.$refs.selectTableRight.toggleRowSelection(row);
        },
        // 已添加数据选中
        handleRightSelect(data) {
            this.rightSelect = data;
        },

        // 数据右移
        moveRight() {
            // 右侧数据添加左侧勾选的数据
            this.rightData = this.rightData.concat(this.leftSelect);
            // 左侧数据删除勾选的数据
            this.leftData = this.leftData.filter(x => this.leftSelect.every(y => x.id != y.id));
        },

        // 数据左移
        moveLeft() {
            // 左侧数据添加左移列表中跟当前选择树节点deptIndexCode相同的数据
            this.rightSelect.forEach(item => {
                // this.treeData.forEach(itemObj => {
                // if (itemObj.children.find(f => f.id === item.id)) {
                this.leftData.push(item);
                // }
                // })
            });
            // 右侧数据删除勾选的数据
            this.rightData = this.rightData.filter(x => this.rightSelect.every(y => x.id != y.id));
        },
        // 取消
        handleCancel() {
            // 点击取消按钮还原右侧数据
            this.rightData = JSON.parse(JSON.stringify(this.copyRightData));
            this.isShow = false;
        },
        // 点击树节点
        handleClickNode(data) {
            this.treeSelect = data;
            this.getPersonList();
        },

        // 获取
        getPersonList() {
            if (this.treeSelect.cmsArticleList && this.treeSelect.cmsArticleList.length > 0) {
                this.leftData = this.treeSelect.cmsArticleList.map(item => {
                    // 这里将左侧的数据格式进行过滤，跟右侧接口读取到的数据格式一致
                    return {
                        ...item
                    }
                })
                // 将每次点击异步树节点后获取到的用户数据过滤掉跟右侧数据重复的数据
                this.leftData = this.leftData.filter(x =>
                    !this.rightData.some(y => x.id === y.id)
                )
            }
        },
        handleQuery() {
            this.getListArticle()
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("articleForm");
            this.getListArticle();
        },
        showIndexSetDialog(event) {
            this.currentArticleInfo = event;
            this.articleIndexType = event.indexType
            this.isShowSetIndexPage = true;
        },
        submitIndexSetInfo() {
            let paramsData = {
                researchId: this.$route.query.id,
                articleId: this.currentArticleInfo.id,
                categoryId: this.currentArticleInfo.categoryId,
                type: this.articleIndexType,
            }
            setindexPosition(paramsData).then(res => {
                this.$message.success('设置成功')
                this.cancleSetIndex()
                this.getListArticle()
            })
        },
        cancleSetIndex() {
            this.isShowSetIndexPage = false;
            this.currentArticleInfo = {}
        }
    }
}
</script>

<style lang="scss" scoped>
.details-scrollbar {
    height: 70vh;
}

.tree-box {
    height: calc(100vh - 250px);
    border: 1px solid rgb(229, 229, 229);
    border-right: 0px;
    width: 20%;
    display: inline-block;
    vertical-align: top;
    padding: 12px 0;
}

.content_box {
    width: 80%;
    display: inline-block;
    vertical-align: top;
}

.table-box {
    height: calc(100vh - 300px);
    display: flex;

    .left-box,
    .right-box {
        width: 45%;
        height: 100%;
        padding: 0 16px;
        border: 1px solid #e5e5e5;
        display: inline-block;
        vertical-align: top;

        h3 {
            height: 48px;
            padding: 12px 0;
            margin: 0;
            color: #4c4c4c;
            font-size: 14px;
            font-weight: normal;
            line-height: 20px;
        }

        .el-table {
            border: 0px;
        }

        .el-table:after {
            background-color: transparent;
        }

        .el-table__empty-block {
            min-height: 389px;
        }
    }

    .btn-box {
        width: 10%;
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;

        .el-button {
            width: 76px;
            padding: 0;
            height: 40px;
            margin-left: 0;
            background-color: #1890ff;
            color: #fff;

            & + .el-button {
                margin-top: 10px;
            }

            &.is-disabled {
                background-color: transparent;
                color: #333;
            }
        }
    }
}

.dialog-footer {
    text-align: right;
    margin-top: 10px;
}

.sketch_content {
    overflow: auto;
    height: calc(92vh - 200px);

    ::v-deep p {
        max-width: 100vw;
    }

    ::v-deep img {
        max-width: 100%;
    }

    &::-webkit-scrollbar {
        width: 3px;
    }

    // 滚动条里面默认的小方块,自定义样式
    &::-webkit-scrollbar-thumb {
        background: #8798af;
        border-radius: 2px;
    }

    // 滚动条里面的轨道
    &::-webkit-scrollbar-track {
        background: transparent;
    }
}
.set_article_info {
    width: 300px;
    margin: 0 auto;
}
.dialog_btns {
    text-align: center;
    .el-button {
        margin-right: 20px;
    }
}
</style>