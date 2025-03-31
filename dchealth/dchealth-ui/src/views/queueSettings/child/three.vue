<template>
    <div class="tabPane">
        <div id="classifyScroll2" :class="showLeftIcon ? 'active' : ''">
            <div class="classifys2">
                <i class="el-icon-arrow-right rightIcon" v-show="showRightIcon" @click="rightClick2"></i>
                <i class="el-icon-arrow-left leftIcon" v-show="showLeftIcon" @click="leftClick2"></i>
                <div class="queue_stage_content">
                    <div class="queue_stage_item classifys-item" v-for="(item, index) in cycleList" :key="index"
                        :ref="`item${index}`" :class="{'stage_item_repeat':(index>0&&!(item.followFrequency == '2'
                            ||item.followFrequency == '1' && (item.followTotal == 0||item.followTotal == 1))),
                            pre2_self1:item.frequency=='2_1',pre1_self1_last2:item.frequency=='1_1_2',
                            pre2_self2_last1:item.frequency=='2_2_1',pre1_self2_last2:item.frequency=='1_2_2',
                            pre2_self2_last2:item.frequency=='2_2_2',pre2_self1_last2:item.frequency=='2_1_2',
                            pre2_self1_last1:item.frequency=='2_1_1',pre1_self2_last1:item.frequency=='1_2_1',}">
                        <div class="stage_item_previewFrequency" v-if="item.followInterval">
                            {{item.followFrequency=="1"?item.followInterval:item.followInterval}}
                            {{item.followIntervalUnit == 1?"天": item.followIntervalUnit == 2? "周": "月"}}
                            后
                        </div>
                        <div class="stage_item_dom">
                            <template v-if="index>0&&!(item.followFrequency == '2'
                                ||item.followFrequency == '1' && (item.followTotal == 0||item.followTotal == 1))">
                                <div class="stage_item_info stage_item_info_end">
                                    <div class="stage_item_digit">{{ item.num-item.followTotal+1 }}</div>
                                    <div class="stage_item_tit">
                                        随访{{(item.num-item.followTotal+1)<10?"0"+(item.num-item.followTotal+1)
                                        :item.num-item.followTotal+1}}
                                    </div>
                                </div>
                                <div class="stage_item_infoTips">
                                    <div class="stage_item_frequency" v-if="item.followInterval">
                                        {{item.followFrequency=="1"?item.followInterval:item.followInterval}}
                                        {{item.followIntervalUnit == 1?"天": item.followIntervalUnit == 2? "周": "月"}}
                                        一次
                                    </div>
                                    <div class="line_dotted"></div>
                                </div>
                            </template>
                            <div class="stage_item_info">
                                <div class="stage_item_digit">{{ item.num }}</div>
                                <div class="stage_item_tit">
                                    {{index==0?'基线'
                                    :'随访'+(item.num < "10" ? "0" + item.num : item.num)}}
                                </div>
                            </div>
                        </div>
                        <div class="stage_item_nextInfo" v-if="index<cycleList.length-1">
                            <div class="line_orange_slider"></div>
                        </div>
                    </div>
                </div>
                <div class="node-desc classifys2-item" v-for="(item, index) in cycleList" :key="index"
                    :ref="`item${index}`">
                    <!-- 基线开始 -->
                    <template v-if="index == 0">
                        <el-card class="box" :body-style="{ padding: '0px' }">
                            <el-tag type="info" effect="dark" class="box_title">{{ item.followName }}</el-tag>
                            <follow-form :follow-item="item" @creat-medical="handCreat" @import-medical="importCRF"
                                @follow-preview="followPreview" :researchStatus="researchStatus"></follow-form>
                        </el-card>
                    </template>
                    <!-- 基线结束 -->
                    <!-- 随访CRF开始 -->
                    <template v-else-if="(index > 0 && cycleList.length - index > 0)">
                        <el-card class="box" :body-style="{ padding: '0px' }">
                            <el-tag type="info" effect="dark" class="box_title"
                                v-if="item.followFrequency == '2' && item.followTotal != '0'">
                                {{"随访"+(10>item.num  ? "0" + item.num : item.num)}}CRF
                            </el-tag>
                            <el-tag type="info" effect="dark" class="box_title"
                                v-else-if="item.followFrequency == '1' && (item.followTotal == '0'||item.followTotal == '1')">
                                {{"随访"+(10>item.num  ? "0" + item.num : item.num)}}CRF
                            </el-tag>
                            <el-tag type="info" effect="dark" class="box_title" v-else>
                                随访{{10>(item.num - item.followTotal + 1)? "0" + (item.num - item.followTotal+1)
                                : item.num - item.followTotal+1}}- {{ 10>item.num  ? "0" + item.num : item.num }}CRF
                            </el-tag>
                            <follow-form :follow-item="item" @creat-medical="handCreat" @import-medical="importCRF"
                                @follow-preview="followPreview" :researchStatus="researchStatus"></follow-form>
                        </el-card>
                    </template>
                </div>
            </div>
            <div slot="footer" class="footer_button">
                <el-button type="primary" @click="submitFormSave">锁定该配置</el-button>
                <el-button type="primary" @click="submitForm">锁定配置并进入下一步</el-button>
            </div>
        </div>
        <!-- 分类 -->
        <el-dialog :visible.sync="isShowcategory" width="85%" center top="8vh" destroy-on-close>
            <el-scrollbar class="templateList-scrollbar">
                <category :follow-id="followId" dialog-type="import" class="category_content" v-if="isShowcategory"
                    @setcategoryInfo="setCategoryInfo"></category>
            </el-scrollbar>
        </el-dialog>
        <!-- 模板 -->
        <el-dialog :visible.sync="isShowTemplate" width="78%" center top="8vh" destroy-on-close
            v-loading="isLoadingTemplate">
            <templat-form :follow-id="followId" dialog-type="import" class="category_content"
                :category-info="categoryInfo" @toformdetail="toFormDetails" v-if="isShowTemplate"
                @templateInfo="setTemplateInfo" @previewInfo="previewTemplateInfo">
            </templat-form>
        </el-dialog>
        <el-dialog :visible.sync="isShowPreView" :title="previewTitle" top="8vh" destroy-on-close @close="closeDrawer"
            append-to-body>
            <el-scrollbar class="preview-scrollbar" v-loading="isLoadingPreviewTemplateForm">
                <Preview-template v-if="drawingList.length&&isShowPreView" :drawing-list="drawingList" :key="renderKey">
                </Preview-template>
                <el-empty v-else></el-empty>
            </el-scrollbar>
        </el-dialog>
        <el-dialog :visible.sync="isShowTemplateForm" width="78%" center top="8vh" destroy-on-close
            v-loading="isLoadingTemplateForm">
            <el-scrollbar class="preview-scrollbar">
                <TemplateList :follow-id="followId" dialog-type="import" :template-form-id="templateId"
                    :key="templateId" @applicationInfo="applicationTemplateInfo"></TemplateList>
            </el-scrollbar>
        </el-dialog>
    </div>
</template>

<script>
import cache from "@/plugins/cache";
import {
    recordTemplateList
} from "@/api/info/config";
import Category from "@/views/crf/category/index";
import TemplatForm from "@/views/crf/template/index";
import {
    importOrUpdateTemplate,
    importOrUpdateTemplateForm,
    previewFllowupCRFData
} from "@/api/crf/fllowupCRF";
import {
    getTemplateDetail
} from "@/api/tool/form";
import PreviewTemplate from '@/views/tool/build/previewTemplate'
import TemplateList from '@/views/crf/templateEdit/index'
import {
    inputComponents
} from '@/utils/generator/config.js'
import FollowForm from '@/views/queueSettings/components/followForm'
export default {
    props: {
        researchId: { type: String, default: null, },
    },
    components: { Category, TemplatForm, PreviewTemplate, TemplateList, FollowForm },
    data() {
        return {
            researchStatus: "",
            showRightIcon: false, // 是否显示右箭头
            maxClickNum: 0, // 最大点击次数
            lastLeft: 0, // 上次滑动距离
            clickNum: 0, // 点击次数
            cycleList: [],
            followId: "",
            isShowcategory: false, //是否显示保存到模板的弹窗
            isShowTemplate: false, //显示模板弹窗
            categoryInfo: {}, //分类信息
            isLoadingTemplate: false,
            templateFormInfo: null,
            drawingList: [],
            isShowPreView: false,
            renderKey: new Date() + '',
            previewTitle: '预览',
            isLoadingTemplateForm: false,
            isShowTemplateForm: false,
            templateId: '', //模板id
            isLoadingPreviewTemplateForm: false,
        };
    },
    computed: {
        showLeftIcon() {
            return this.clickNum > 0;
        },
    },
    watch: {
        researchId: {
            immediate: true, // 监听到后，立即执行 handler方法
            handler(newVal, oldValue) {
                if (newVal) {
                    this.getRecordTemplateList();
                }
            },
        },
    },
    activated() {
        this.initShowBtn()
    },
    methods: {
        initShowBtn() {
            let classifys2Dom = document.getElementsByClassName("classifys2-item")[this.cycleList.length - 1]
            if (classifys2Dom) {
                let lastItemOffsetLeft = classifys2Dom.offsetLeft;
                // 获取可视区域宽度
                const lookWidth = document.getElementById("classifyScroll2").clientWidth;
                if (lastItemOffsetLeft > lookWidth) {
                    this.showRightIcon = true
                } else {
                    this.showRightIcon = false
                }
            } else {
                this.showRightIcon = false
            }
        },
        // 周期流程
        getRecordTemplateList() {
            this.researchStatus = cache.session.getJSON("researchStatus");
            const queryParams = {
                researchId: this.researchId,
            };
            recordTemplateList(queryParams).then((response) => {
                let arr = response.rows;
                arr.forEach((item, index) => {
                    item["num"] = index == 0 ?
                        Number(item.followTotal == null ? 0 : item.followTotal + 1) :
                        Number((!item.followTotal ?
                            1 : item.followTotal) + arr[index - 1]["num"]);
                    if (item.fmedicalForms && item.fmedicalForms.length > 0) {
                        let newVal = item.fmedicalForms.filter((f) => f.parentId == 0);
                        newVal.sort((a, b) => {
                            return a.sortNum - b.sortNum
                        })
                        item["fmedicalForms"] = newVal;
                    }

                    // <!-- 随访不限制次数开始  followFrequency 1-限制次数 2-不限次数-->
                    // <!-- 随访限制次数但次数是0开始 -->
                    // <!-- 随访不限制次数结束 -->
                    // <!-- 随访限制次数但次数但是>0开始 -->
                    // <!-- 随访不限制次数开始  followFrequency 1-限制次数 2-不限次数-->
                    if (item.followFrequency == '2' && item.followTotal != '0' || item
                        .followFrequency == '1' && (item.followTotal == '0' || !item.followTotal ||
                            item.followTotal == '1')) {
                        item.frequencyType = '1'
                    } else {
                        item.frequencyType = '2'
                    }

                });
                arr.forEach((item, index) => {
                    let lastItem = index < arr.length - 1 ? arr[index + 1] : item
                    let preItem = index > 0 ? arr[index - 1] : item
                    if (preItem.frequencyType == '1' && item.frequencyType == '1' && lastItem
                        .frequencyType == '2') {
                        item.frequency = '1_1_2'
                    } else if (preItem.frequencyType == '1' && item.frequencyType == '2' && lastItem
                        .frequencyType == '1') {
                        item.frequency = '1_2_1'
                    } else if (preItem.frequencyType == '1' && item.frequencyType == '2' && lastItem
                        .frequencyType == '2') {
                        item.frequency = '1_2_2'
                    } else if (preItem.frequencyType == '2' && item.frequencyType == '2' && lastItem
                        .frequencyType == '1') {
                        item.frequency = '2_2_1'
                    } else if (preItem.frequencyType == '2' && item.frequencyType == '2' && lastItem
                        .frequencyType == '2') {
                        item.frequency = '2_2_2'
                    } else if (preItem.frequencyType == '2' && item.frequencyType == '1' && lastItem
                        .frequencyType == '2') {
                        item.frequency = '2_1_2'
                    } else if (preItem.frequencyType == '2' && item.frequencyType == '1' && lastItem
                        .frequencyType == '1') {
                        item.frequency = '2_1_1'
                    } else if (preItem.frequencyType == '2' && item.frequencyType == '1') {
                        item.frequency = '2_1'
                    }
                })
                this.cycleList = arr;
                this.$nextTick(() => {
                    this.initShowBtn();
                })
            });
        },
        // 点击右箭头（左侧滚动）
        rightClick2() {
            // 如果点击次数小于数组长度-1时，执行左滑动效果。
            if (this.clickNum < this.cycleList.length - 1) {
                // 获取当前元素宽度
                let width =
                    document.querySelectorAll(".classifys2-item")[this.clickNum]
                        .offsetWidth;
                // 获取最后一个元素距离左侧的距离
                let lastItemOffsetLeft =
                    document.getElementsByClassName("classifys2-item")[
                        this.cycleList.length - 1
                    ].offsetLeft;
                // 获取可视区域宽度
                const lookWidth =
                    document.getElementById("classifyScroll2").clientWidth;
                // 如果最后一个元素距离左侧的距离大于可视区域的宽度，表示最后一个元素没有出现，执行滚动效果
                if (lastItemOffsetLeft > lookWidth) {
                    // 公示：滚动距离（元素的magin-left值） = 负的它自己的长度 + 上一次滑动的距离
                    document.getElementsByClassName(
                        "classifys2"
                    )[0].style.marginLeft = `${-width + this.lastLeft}px`;
                    this.lastLeft = -width + this.lastLeft;
                    // 点击次数+1
                    this.clickNum += 1;
                    // 记录到最后一个元素出现在可视区域时，点击次数的最大值。用于后面点击左侧箭头时判断右侧箭头的显示
                    this.maxClickNum = this.clickNum;
                }
                /* 
                    如果最后一个元素距离左侧的距离大于可视距离+自身的宽度，说明没有到最后一个, 显示右侧箭头，否则隐藏右侧箭头
                    注意：这里要加上其自身的宽度，否则要多点击一下才会隐藏。因为点击的时候实际上最后一个元素还没有滚动，
                    它距离左侧的距离是大于可视区域的（也就是说获取的是上一次点击之后，距离左侧的距离）
                    所以这里要加上其自身的宽度
                    看不懂的话不用管，反正加上就对了
                */
                this.showRightIcon = lastItemOffsetLeft > lookWidth + width;
            } else {
                this.showRightIcon = false
            }
        },
        // 点击左箭头（右侧滚动）
        leftClick2() {
            // console.log(this.lastLeft, 'lastLeft')
            // console.log(document.querySelectorAll('.classifys-item')[this.clickNum - 1].offsetWidth, '上一个的宽度')
            // 当点击次数大于0时才触发，这样当点击次数clickNum等于1的到时候，clickNum--等于0.根据计算属性的判断会隐藏掉左箭头
            if (this.clickNum > 0) {
                // 获取当前元素宽度
                let width =
                    document.querySelectorAll(".classifys2-item")[this.clickNum - 1]
                        .offsetWidth;
                // 公示：滚动距离（元素的magin-left值） = 它自己的长度 + 上一次滑动的距离
                document.getElementsByClassName("classifys2")[0].style.marginLeft =
                    `${this.lastLeft + width}px`;
                this.lastLeft = width + this.lastLeft;
                // 点击次数-1
                this.clickNum--;
                // 如果点击次数小于最大点击次数，说明最后一个元素已经不在可是区域内了，显示右箭头
                if (this.clickNum < this.maxClickNum) {
                    this.showRightIcon = true;
                }
            }
        },
        // 创建CRF
        handCreat(item) {
            // console.log(item, "item", this.$route);
            let query = this.$route.query
            let oldPath = `${this.$route.path}?id=${query.id}&stage=2`

            this.$router.push({
                path: "/queueSettings/cycle/crfSettings",
                query: {
                    followId: item.followId,
                    researchId: query.id,
                    stage: query.stage,
                    oldPath
                },
            });
        },
        // CRF库导入
        importCRF(event) {
            this.followId = event.followId;
            this.isShowcategory = true;
        },
        // 显示选择分类
        addFollowupToTemplate() {
            this.isShowcategory = true;
        },
        // 选择分类
        setCategoryInfo(event) {
            if (event.templateNum > 0) {
                this.categoryInfo = event;
                this.isShowTemplate = true;
            } else {
                this.$message.warning('该分类下没有模板内容')
            }
        },
        // 选择模板
        setTemplateInfo(event) {
            let params = {
                templateId: event.templateId,
                followId: this.followId,
            };
            this.isLoadingTemplate = true;
            importOrUpdateTemplate(params).then((res) => {
                this.isLoadingTemplate = false;
                this.isShowTemplate = false;
                this.isShowcategory = false;
                this.isShowPreView = false;
                this.templateFormInfo = null;
                this.$message.success("模板导入成功");
                this.getRecordTemplateList()
            });
        },
        // 导入模板
        applicationTemplateInfo(event) {
            let params = {
                templateId: event.templateId,
                followId: this.followId,
                templateFromIds: event.templateFormsIds
            }
            this.isLoadingTemplateForm = true
            importOrUpdateTemplateForm(params).then((res) => {
                this.isLoadingTemplate = false;
                this.isShowTemplate = false;
                this.isShowcategory = false;
                this.isShowPreView = false;
                this.templateFormInfo = null;
                this.isShowTemplateForm = false;
                this.$message.success("模块导入成功");
                this.getRecordTemplateList()
                this.isLoadingTemplateForm = false
            }).catch(error => {
                this.isLoadingTemplateForm = false
            });
        },
        closeDrawer() {
            this.isShowDrawer = false;
        },
        // 查看模块数据列表
        toFormDetails(event) {
            this.templateId = event.templateId;
            this.isShowTemplateForm = true;
        },
        // 预览
        previewTemplateInfo(event) {
            this.templateFormInfo = event;
            this.isLoadingPreviewTemplateForm = true
            this.previewTitle = event.templateName + "预览";
            this.isShowPreView = true;
            getTemplateDetail(event.templateId).then((res) => {
                let fields = [];
                if (res.code == 200) {
                    let resData = res.data;
                    fields = resData.templateProperties && resData.templateProperties.length ?
                        resData.templateProperties.map((m, index) => {
                            if (m.id) m.id = m.id.toString();
                            const {
                                attribute,
                                templateProperties,
                                ...mInfo
                            } = m;
                            return {
                                ...JSON.parse(m.attribute),
                                renderKey: new Date().getTime() + "" + index,
                                ...mInfo,
                            };
                        }) : [];
                    let libraryFormList = this.recoverTemplateForms(
                        resData.templateForms
                    );
                    if (libraryFormList.length) {
                        fields.push(...libraryFormList);
                    }
                    fields.sort((a, b) => {
                        return a.sortNum - b.sortNum;
                    });
                }
                this.drawingList = fields;
                this.renderKey = new Date() + ''
                this.isLoadingPreviewTemplateForm = false
            });
        },
        // 处理基础表单
        recoverTemplateForms(templateForms) {
            let list = [];
            if (templateForms && templateForms.length) {
                templateForms.forEach((item, index) => {
                    if (item.id) item.id = item.id.toString()
                    let formAttribute = inputComponents.find(f => f.labelType == 'form')
                    const {
                        attribute,
                        templateProperties,
                        ...itemInfo
                    } = item
                    let attributeJson = attribute ? {
                        ...JSON.parse(attribute)
                    } : formAttribute
                    let info = {
                        ...attributeJson,
                        ...itemInfo,
                        children: [],
                        renderKey: index + '' + new Date().getTime(),
                    }
                    if (item.templateProperties && item.templateProperties.length) {
                        info.children = item.templateProperties.map(m => {
                            if (m.id) m.id = m.id.toString()
                            const {
                                attribute,
                                templateProperties,
                                ...mInfo
                            } = m
                            return {
                                ...JSON.parse(m.attribute),
                                ...mInfo
                            }
                        });
                    }
                    let libraryFormList = this.recoverTemplateForms(item.templateForms);
                    if (libraryFormList.length) {
                        info.children.push(...libraryFormList)
                    }
                    if (info.children && info.children.length) {
                        info.children.sort((a, b) => {
                            return a.sortNum - b.sortNum
                        })
                    }
                    list.push(info)
                })
            }
            return list
        },
        submitForm: function () {
            this.$emit("nextSubmitFour", true);
        },
        submitFormSave: function () {
            this.$emit("nextSubmitFourSave", true);
        },
        cancel: function () { },
        // 
        followPreview(event) {
            this.previewTitle = event.followName + "预览";
            this.isLoadingPreviewTemplateForm = true;
            this.drawingList = []
            this.isShowPreView = true;
            previewFllowupCRFData(event.followId).then(res => {
                let fields = [];
                let formAttribute = inputComponents.find(f => f.labelType == 'form')
                if (res.code == 200) {
                    let resData = res.data;
                    if (resData && resData.length) {
                        fields = resData.map((m, index) => {
                            const {
                                attribute,
                                otherInfo
                            } = m
                            let attributeJson = attribute ? {
                                ...JSON.parse(attribute)
                            } : formAttribute
                            let info = {
                                ...attributeJson,
                                ...otherInfo,
                                formName: m.medicalName,
                                medicalName: m.medicalName,
                                formCode: m.medicalCode,
                                renderKey: new Date().getTime() + '' + index,
                                children: this.convartMedicalList(m)
                            }
                            if (m.id) info.id = m.id.toString()
                            return info
                        })
                    }
                    fields.sort((a, b) => {
                        return a.sortNum - b.sortNum
                    })
                }
                this.drawingList = fields;
                this.isLoadingPreviewTemplateForm = false
            })
        },

        // 处理基础表单
        convartMedicalList(dataInfo) {
            let formAttribute = inputComponents.find(f => f.labelType == 'form')
            let medicalFormData = dataInfo.medicalFormList
            let medicalPropertiesData = dataInfo.medicalProperties
            let list = [];
            if (medicalPropertiesData && medicalPropertiesData.length) {
                list = medicalPropertiesData.map((m,
                    index) => {
                    if (m.id) m.id = m.id.toString()
                    const {
                        attribute,
                        medicalProperties,
                        ...mInfo
                    } = m
                    return {
                        ...JSON.parse(m.attribute),
                        renderKey: new Date().getTime() + '' + index,
                        ...mInfo,
                    }
                })
            }
            if (medicalFormData && medicalFormData.length) {
                let arrayStemp = medicalFormData.map((m, index) => {
                    const {
                        attribute,
                        otherInfo
                    } = m
                    let attributeJson = attribute ? {
                        ...JSON.parse(attribute)
                    } : formAttribute
                    let info = {
                        ...attributeJson,
                        ...otherInfo,
                        formName: m.medicalName,
                        medicalName: m.medicalName,
                        formCode: m.medicalCode,
                        renderKey: new Date().getTime() + '' + index,
                        children: m.formType == '2' ? null : this.convartMedicalList(m)
                    }
                    if (m.id) info.id = m.id.toString()
                    return info
                })
                list = list.concat(arrayStemp)
            }
            list.sort((a, b) => {
                return a.sortNum - b.sortNum
            })
            return list
        },
    },
};
</script>

<style lang="scss" scoped>
#classifyScroll2 {
    width: 1200px;
    overflow: hidden;
    margin: 0px auto;
    transition: all 0.3s;

    &.active {
        transition: all 0.3s;
        margin: 0 auto;
    }

    .footer_button {
        margin: 30px auto 20px;
        text-align: center;
    }

    .classifys2 {
        // 注意一下，这里没有用position: relative，，而图标用了position:absolute。因为这个东西是准备做组件的，我把相对定位写在父级的css标签上了。
        transition: all 0.3s;
        display: block;
        width: 100%;
        margin: 0px 0;
        white-space: nowrap;

        .rightIcon,
        .leftIcon {
            color: #999;
            cursor: pointer;
            position: absolute;
            bottom: 50%;
            font-size: 60px;
            transform: translate(0, 50%);
            z-index: 9;
            background: rgba(255, 255, 255, 0.8);
            border-radius: 40%;
            padding: 10px;
        }

        .rightIcon {
            right: 0px;
        }

        .leftIcon {
            left: 0;
        }

        &-item {
            font-size: 14px;
            font-weight: 400;
            width: 300px;
            display: inline-block;
            text-align: center;
            white-space: nowrap;
            color: rgb(64 158 255);
            padding: 0px 15px;

            .box {
                display: flex;
                flex-direction: column;
                height: 480px;

                .box_title {
                    font-weight: bold;
                    width: 100%;
                    border-radius: 0;
                    height: 40px;
                    line-height: 40px;
                }

                .system_type {
                    ::v-deep .el-tabs__item {
                        padding: 0;
                    }
                }
            }
        }

        .queue_stage_content {
            display: inline-flex;
            display: flex;
            padding: 25px 0 10px 130px;

            .queue_stage_item {
                width: 300px;
                font-weight: 400;
                white-space: nowrap;
                color: #333;
                display: flex;
                text-align: center;
                position: relative;
                flex-shrink: 0;

                .stage_item_previewFrequency {
                    position: absolute;
                    left: -190px;
                    width: 120px;
                    color: #409eff;
                    font-weight: bold;
                }

                .stage_item_dom {
                    display: flex;
                    justify-content: space-between;
                    align-items: flex-start;
                }

                &.stage_item_repeat {
                    .stage_item_dom {
                        width: 170px;
                    }
                }

                .stage_item_info {
                    display: flex;
                    flex-direction: column;
                    align-items: center;

                    .stage_item_digit {
                        width: 40px;
                        height: 40px;
                        line-height: 35px;
                        border-radius: 50%;
                        color: #409eff;
                        border: 2px solid #409eff;
                        font-size: 20px;
                        font-weight: bold;
                    }

                    .stage_item_tit {
                        margin-top: 10px;
                        margin-bottom: 10px;
                    }
                }

                .stage_item_infoTips {
                    flex: 1;

                    .stage_item_frequency {
                        width: 60%;
                        margin: 0 auto 0;
                        font-weight: bold;
                        color: #5c96ee;
                    }

                    .line_dotted {
                        width: 96%;
                        margin: 3px 2% 0;
                        border-bottom: 2px dashed #409eff;
                    }
                }

                .stage_item_nextInfo {
                    flex: 1;
                }

                .line_orange_slider {
                    position: relative;
                    width: 90%;
                    height: 2px;
                    display: inline-block;
                    border-bottom: 2px solid rgb(255 195 90);
                    margin: 20px 6% 0 4%;

                    &::before {
                        content: "";
                        width: 10px;
                        height: 10px;
                        background: rgb(255 195 90);
                        border-radius: 50%;
                        position: absolute;
                        left: 0;
                        top: -4px;
                    }

                    &::after {
                        content: "";
                        width: 0px;
                        height: 0px;
                        border: 8px solid #000;
                        border-left-color: rgb(255 195 90);
                        border-bottom-color: transparent;
                        border-top-color: transparent;
                        border-right-color: transparent;
                        position: absolute;
                        right: -12px;
                        top: -7px;
                    }
                }

                &.pre1_self1_last2 {
                    width: 237px;
                }

                &.pre2_self2_last1 {
                    width: 362px;

                    .stage_item_previewFrequency {
                        left: -125px;
                    }
                }

                &.pre1_self2_last2,
                &.pre2_self1 {
                    width: 298px;

                    .stage_item_previewFrequency {
                        left: -155px;
                    }
                }

                &.pre2_self2_last2 {
                    .stage_item_previewFrequency {
                        left: -125px;
                    }
                }

                &.pre2_self1_last2 {
                    width: 240px;

                    .stage_item_previewFrequency {
                        left: -155px;
                    }
                }

                &.pre2_self1_last1 {
                    width: 286px;

                    .stage_item_previewFrequency {
                        left: -155px;
                    }
                }

                &.pre1_self2_last1 {
                    width: 360px;

                    .stage_item_previewFrequency {
                        left: -155px;
                    }
                }
            }
        }
    }
}
.templateList-scrollbar {
    min-height: 70vh;
}

.preview-scrollbar {
    overflow-x: hidden;
    height: calc(82vh - 100px);

    .el-checkbox {
        padding: 3px 0;
    }
}

::v-deep .el-dialog__body {
    padding-top: 5px;
}

::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
}

.container_form {
    overflow: hidden;
}
</style>