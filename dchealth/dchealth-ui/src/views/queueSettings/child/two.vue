<template>
    <div class="tabPane">
        <div id="classifyScroll" :class="showLeftIcon ? 'active' : ''">
            <div class="classifys">
                <i class="el-icon-arrow-right rightIcon" v-show="showRightIcon" @click="rightClick"></i>
                <i class="el-icon-arrow-left leftIcon" v-show="showLeftIcon" @click="leftClick"></i>
                <div class="queue_stage_content">
                    <div class="queue_stage_item classifys-item" v-for="(item, index) in cycleList" :key="index"
                        :ref="`item${index}`" :class="{'stage_item_repeat':(index>0&&!(item.followFrequency == '2'
                            ||item.followFrequency == '1' && (item.followTotal == 0||item.followTotal == 1)))}">
                        <div class="stage_item_previewFrequency" v-if="item.followInterval">
                            {{item.followFrequency=="1"?item.followInterval:item.followInterval}}
                            {{item.followIntervalUnit == 1?"天": item.followIntervalUnit == 2? "周": 
                            item.followIntervalUnit == 3?"月":"年"}}
                            后
                        </div>
                        <div class="stage_item_dom">
                            <template v-if="index>0&&!(item.followFrequency == '2'
                                ||item.followFrequency == '1' && (item.followTotal == 0||item.followTotal == 1))">
                                <div class="stage_item_info stage_item_info_end">
                                    <div class="stage_item_digit">{{ item.num-item.followTotal+1 }}</div>
                                    <div class="stage_item_tit">
                                        随访{{(item.num-item.followTotal+1)<10?"0"+(item.num-item.followTotal+1):item.num-item.followTotal+1}}
                                    </div>
                                </div>
                                <div class="stage_item_infoTips">
                                    <div class="stage_item_frequency" v-if="item.followInterval">
                                        {{item.followFrequency=="1"?item.followInterval:item.followInterval}}
                                        {{item.followIntervalUnit == 1?"天": item.followIntervalUnit == 2? "周": 
                                        item.followIntervalUnit == 3?"月":"年"}}
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
                                <el-button type="danger" icon="el-icon-delete" circle @click="delCycle(item)"
                                    v-if="index!=0"></el-button>
                            </div>
                        </div>
                        <div class="stage_item_nextInfo"
                            v-if="isShowAddStage||(!isShowAddStage&&index!=cycleList.length-1)">
                            <div class="line_orange_slider"></div>
                        </div>
                    </div>
                    <div class="queue_stage_item classifys-item">
                        <div class="stage_item_info" style="cursor: pointer; width: 60px" v-if="isShowAddStage">
                            <div class="stage_item_digit" @click="handleAdd">+</div>
                            <div class="stage_item_tit">添加</div>
                        </div>
                    </div>
                </div>
            </div>
            <div slot="footer" class="dialog-footer" style="margin: 0 auto; text-align: center">
                <el-button type="primary" @click="submitFormSave">保 存</el-button>
                <el-button type="primary" @click="submitForm">保存并进入下一步</el-button>
            </div>
        </div>
        <!-- 添加或修改缓存配置对话框 -->
        <el-dialog :title="title" :visible.sync="openCycle" width="500px">
            <el-form ref="formCycle" :model="formCycle" :rules="rulesCycle" label-position="top">
                <el-form-item label="与上一次随访间隔" prop="followInterval">
                    <el-input v-model="formCycle.followInterval" placeholder="请输入与上一次随访间隔（只能输入纯数字）"
                        @input="(v) => (formCycle.followInterval = v.replace(/[^\d]/g, ''))" class="input-with-select">
                        <el-select v-model="formCycle.followIntervalUnit" slot="append" placeholder="请选择"
                            style="width:130px;background-color: rgb(44 120 238);color: #fff;">
                            <el-option v-for="item in nameOptions" :key="item.value" :label="item.name"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-input>
                </el-form-item>
                <el-form-item label="是否限制随访次数" prop="followFrequency">
                    <el-radio-group v-model="formCycle.followFrequency" @change="changRadio">
                        <el-radio label="1">限制次数</el-radio>
                        <el-radio label="2">不限次数</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="随访次数" prop="followTotal" v-if="formCycle.followFrequency == 1">
                    <el-input v-model="formCycle.followTotal"
                        @input="(v) => (formCycle.followTotal = v.replace(/[^\d]/g, ''))"
                        placeholder="请输入随访次数（只能输入纯数字）">
                        <template slot="append">次</template>
                    </el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer" style="margin: 0 auto; text-align: center">
                <el-button type="primary" @click="submitFormCycle">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import cache from "@/plugins/cache";
import { recordTemplateList, recordTemplateAdd, recordTemplateDel, } from "@/api/info/config";

export default {
    props: {
        researchId: { type: String, default: null, },
        nameOptions: { type: Array, default: null, },
    },
    data() {
        return {
            researchStatus: "",
            cycleList: [],
            openCycle: false,
            title: "", // 弹出层标题
            formCycle: {}, //设置周期表单参数
            showRightIcon: false, // 是否显示右箭头
            maxClickNum: 0, // 最大点击次数
            lastLeft: 0, // 上次滑动距离
            clickNum: 0, // 点击次数
            // 表单校验
            rulesCycle: {
                followInterval: [{ required: true, message: "随访间隔不能为空", trigger: "blur" }],
                followTotal: [{ required: true, message: "随访重复次数不能为空", trigger: "blur" }],
            },
            isShowAddStage: true,
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
                if (newVal && newVal != null) {
                    this.getRecordTemplateList();
                }
            },
        },
    },
    methods: {
        // 周期流程
        getRecordTemplateList() {
            const queryParams = {
                researchId: this.researchId,
            };
            this.researchStatus = cache.session.getJSON("researchStatus");
            recordTemplateList(queryParams).then((response) => {
                this.cycleList = response.rows;
                this.cycleList.forEach((item, index) => {
                    item["num"] =
                        index == 0 ?
                            Number(item.followTotal == null ? 0 : item.followTotal + 1) :
                            Number(
                                (!item.followTotal ?
                                    1 : item.followTotal) + this.cycleList[index - 1]["num"]
                            );
                    if (index == this.cycleList.length - 1) {
                        this.isShowAddStage = item.followFrequency != '2'
                    }
                });
                this.$nextTick(() => {
                    this.initShowBtn();
                })
            });
        },
        initShowBtn() {
            let listDomLength = this.isShowAddStage ? this.cycleList.length : this.cycleList.length - 1
            let classifys2Dom = document.getElementsByClassName("classifys-item")[listDomLength]
            if (classifys2Dom) {
                let lastItemOffsetLeft = classifys2Dom.offsetLeft;
                // 获取可视区域宽度
                const lookWidth = document.getElementById("classifyScroll").clientWidth;
                if (lastItemOffsetLeft > lookWidth) {
                    this.showRightIcon = true
                } else {
                    this.showRightIcon = false
                }
            } else {
                this.showRightIcon = false
            }
        },
        // 设置周期
        submitFormCycle: function () {
            if (!Number(this.formCycle.followInterval)) {
                this.$message.warning('与上一次随访间隔时间不能为小于1')
                return
            }
            this.$refs["formCycle"].validate((valid) => {
                if (valid) {
                    this.formCycle.followName = "随访" + this.cycleList.length;
                    recordTemplateAdd(this.formCycle).then((response) => {
                        this.$modal.msgSuccess("新增成功");
                        this.openCycle = false;
                        this.getRecordTemplateList();
                    });
                }
            });
        },
        // 点击右箭头（左侧滚动）
        rightClick() {
            let listDomLength = this.isShowAddStage ? this.cycleList.length : this.cycleList.length - 1
            // 如果点击次数小于数组长度-1时，执行左滑动效果。
            if (this.clickNum < listDomLength) {
                // 获取当前元素宽度
                let width =
                    document.querySelectorAll(".classifys-item")[this.clickNum]
                        .offsetWidth;
                // 获取最后一个元素距离左侧的距离
                let lastItemOffsetLeft =
                    document.getElementsByClassName("classifys-item")[listDomLength].offsetLeft;
                // 获取可视区域宽度
                const lookWidth = document.getElementById("classifyScroll").clientWidth;
                // 如果最后一个元素距离左侧的距离大于可视区域的宽度，表示最后一个元素没有出现，执行滚动效果
                if (lastItemOffsetLeft > lookWidth) {
                    // 公示：滚动距离（元素的magin-left值） = 负的它自己的长度 + 上一次滑动的距离
                    document.getElementsByClassName("classifys")[0].style.marginLeft =
                        `${-width + this.lastLeft}px`;
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
        leftClick() {
            // console.log(this.lastLeft, 'lastLeft')
            // console.log(document.querySelectorAll('.classifys-item')[this.clickNum - 1].offsetWidth, '上一个的宽度')
            // 当点击次数大于0时才触发，这样当点击次数clickNum等于1的到时候，clickNum--等于0.根据计算属性的判断会隐藏掉左箭头
            if (this.clickNum > 0) {
                // 获取当前元素宽度
                let width =
                    document.querySelectorAll(".classifys-item")[this.clickNum - 1]
                        .offsetWidth;
                // 公示：滚动距离（元素的magin-left值） = 它自己的长度 + 上一次滑动的距离
                document.getElementsByClassName("classifys")[0].style.marginLeft =
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

        // 删除
        delCycle(item) {
            const followIds = item.followId;
            this.$confirm(`周期已配置CRF，删除周期将同时删除对应的CRF，请确认是否删除周期？
                        </br></br>注：可前往CRF设置页面保存CRF为模板`, "提示", {
                dangerouslyUseHTMLString: true,
            })
                .then(() => {
                    recordTemplateDel(followIds).then((response) => {
                        this.getRecordTemplateList();
                        this.$modal.msgSuccess(`删除成功`);
                    });
                })
                .catch((error) => { });
        },
        // 选择是否重复次数
        changRadio(val) {
            if (val == "2") {
                this.formCycle.followTotal = "";
            }
        },
        // 表单重置
        reset() {
            this.formCycle = {
                researchId: this.researchId,
                followName: undefined,
                followInterval: undefined,
                followIntervalUnit: "1",
                followFrequency: "1",
                followTotal: undefined,
            };
            this.resetForm("formCycle");
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.openCycle = true;
            this.title = "设置周期";
        },
        submitForm: function () {
            this.$emit("nextSubmitThree", true);
        },
        submitFormSave() {
            this.$emit("nextSubmitThreeSave", true);
        },
        handCrf: function () { },
        cancel() {
            this.reset();
            this.openCycle = false;
        },
    },
};
</script>

<style lang="scss" scoped>
#classifyScroll {
    width: 80%;
    overflow: hidden;
    margin: 0 10%;
    transition: all 0.3s;

    &.active {
        transition: all 0.3s;
        margin: 0 auto;
    }

    .classifys {
        // 注意一下，这里没有用position: relative，，而图标用了position:absolute。
        // 因为这个东西是准备做组件的，我把相对定位写在父级的css标签上了。
        transition: all 0.3s;
        display: block;
        width: 100%;
        margin: 150px 0;
        white-space: nowrap;
        margin-left: 0px;

        .rightIcon {
            position: absolute;
            right: 40px;
            bottom: 65%;
            font-size: 60px;
            transform: translate(0, 50%);
            z-index: 9;
            color: #999;
            cursor: pointer;
        }

        .leftIcon {
            position: absolute;
            left: 40px;
            bottom: 65%;
            font-size: 60px;
            transform: translate(0, 50%);
            z-index: 9;
            color: #999;
            cursor: pointer;
        }

        .queue_stage_content {
            display: flex;

            .queue_stage_item {
                width: 300px;
                font-weight: 400;
                white-space: nowrap;
                color: #2c78ee;
                display: flex;
                text-align: center;
                position: relative;
                flex-shrink:0;

                .stage_item_previewFrequency {
                    position: absolute;
                    left: -180px;
                    top: -7px;
                    width: 120px;
                    border: 1px solid #2c78ee;
                    border-radius: 5px;
                    color: #5c96ee;
                    background: rgba(92, 150, 238, 0.1);
                    padding: 5px 0px;
                }

                .stage_item_dom {
                    display: flex;
                    justify-content: space-between;
                    align-items: flex-start;
                }

                &.stage_item_repeat {
                    width: 560px;

                    .stage_item_dom {
                        width: 330px;
                    }
                }

                .stage_item_info {
                    height: 120px;
                    display: flex;
                    flex-direction: column;
                    align-items: center;

                    .stage_item_digit {
                        width: 60px;
                        height: 60px;
                        line-height: 60px;
                        border-radius: 50%;
                        border: 1px solid rgb(44 120 238);
                        font-size: 24px;
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
                        margin: -8px auto 0;
                        border: 1px solid #2c78ee;
                        border-radius: 5px;
                        color: #5c96ee;
                        background: rgba(92, 150, 238, 0.1);
                        padding: 5px 0px;
                    }

                    .line_dotted {
                        width: 100%;
                        margin-top: 13px;
                        border-bottom: 2px dashed #2c78ee;
                    }
                }

                .stage_item_nextInfo {
                    flex: 1;
                }

                .line_orange_slider {
                    position: relative;
                    width: 86%;
                    height: 2px;
                    display: inline-block;
                    border-bottom: 2px solid rgb(255 195 90);
                    margin-top: 33px;

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
            }
        }

        .box {
            display: block;

            .classifys-item {
                font-size: 14px;
                font-weight: 400;
                //   margin-left: -4px;
                display: inline-block;
                text-align: center;
                white-space: nowrap;
                color: #2c78ee;
                position: relative;

                &.active {
                    .circle {
                        background: rgb(44 120 238);
                    }

                    .line {
                        background: rgb(44 120 238);
                    }

                    .content {
                        color: rgb(44 120 238);
                    }
                }

                .circle {
                    position: absolute;
                    top: -20px;
                    right: 0;
                    width: 60px;
                    height: 60px;
                    line-height: 60px;
                    border-radius: 50%;
                    border: 1px solid rgb(44 120 238);
                    background-color: #fff;
                    font-size: 24px;
                    font-weight: bold;
                }

                .circleLeft {
                    position: absolute;
                    top: -20px;
                    right: 240px;
                    width: 60px;
                    height: 60px;
                    line-height: 60px;
                    border-radius: 50%;
                    border: 1px solid rgb(44 120 238);
                    background-color: #fff;
                    font-size: 24px;
                    font-weight: bold;
                }

                .load-time {
                    position: absolute;
                    top: -30px;
                    width: 100%;
                    left: 0%;
                    padding-left: 60px;
                    text-align: center;

                    label {
                        font-size: 12px;
                        color: rgb(92 150 238);
                        background: rgba(92, 150, 238, 0.1);
                        padding: 5px 0px;
                        border-radius: 5px;
                        display: block;
                        width: 80%;
                        margin: 0 auto;
                        border: 1px solid rgb(44 120 238);
                    }

                    span {
                        color: rgb(44 120 238);
                    }
                }

                .load-timeBom {
                    position: absolute;
                    top: -30px;
                    width: 100%;
                    left: -100%;
                    padding-left: 60px;
                    text-align: center;

                    label {
                        font-size: 12px;
                        color: rgb(44 120 238);
                        background: rgba(92, 150, 238, 0.1);
                        // background: rgba(255, 195, 90, 0.1);
                        padding: 5px 0px;
                        border-radius: 5px;
                        display: block;
                        width: 80%;
                        margin: 0 auto;
                        // border: 1px solid rgb(255 195 90);
                        border: 1px solid rgb(44 120 238);

                        span {
                            color: rgb(44 120 238);
                        }
                    }
                }

                .line {
                    position: relative;
                    width: 70%;
                    height: 2px;
                    display: inline-block;
                    margin-left: 5%;
                    margin-right: 25%;
                    border-bottom: 2px solid rgb(255 195 90);
                    // background: rgb(44 120 238);
                }

                .line::before {
                    content: "";
                    width: 10px;
                    height: 10px;
                    background: rgb(255 195 90);
                    border-radius: 50%;
                    position: absolute;
                    left: 0;
                    top: -4px;
                }

                .line::after {
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

                .lineDashed {
                    width: 100%;
                    height: 2px;
                    display: inline-block;
                    border-bottom: 2px dashed rgb(44 120 238);
                    // background: rgb(44 120 238);
                }

                .content {
                    position: absolute;
                    margin-top: 28px;
                    text-align: center;
                    width: 60px;
                    right: 0px;
                    color: rgb(44 120 238);
                }

                .delBtn {
                    position: absolute;
                    margin-top: 60px;
                    text-align: center;
                    width: 60px;
                    left: 0px;
                }
            }

            .fristClassifys {
                .circle {
                    left: 0;
                }

                .content {
                    left: 0;
                }

                .line {
                    margin-right: 0;
                    margin-left: 20%;
                }

                .classifysBox {
                    width: 300px;
                }

                .classifysBoxPlus {
                    width: 600px;

                    .delBtn {
                        left: 150px;
                    }

                    .load-time {
                        position: absolute;
                        top: -30px;
                        width: 300px;
                        left: 0%;
                        padding-left: 60px;
                        text-align: center;

                        label {
                            font-size: 12px;
                            color: rgb(92 150 238);
                            background: rgba(92, 150, 238, 0.1);
                            padding: 5px 0px;
                            border-radius: 5px;
                        }

                        label {
                            display: block;
                            width: 80%;
                            margin: 0 auto;
                            border: 1px solid rgb(44 120 238);
                        }

                        span {
                            color: rgb(44 120 238);
                        }
                    }

                    .load-timeBom {
                        width: 160px;
                        left: -230px;
                    }

                    .lineDashedPlus {
                        display: inline-block;
                        height: 4px;

                        .lineLeft {
                            width: 300px;
                            float: left;
                            border-bottom: 2px dashed rgb(44 120 238);
                        }

                        .lineRight {
                            width: 200px;
                            float: left;
                            background: #fff;
                            position: relative;
                            height: 2px;
                            display: inline-block;
                            margin-left: 60px;
                            border-bottom: 2px solid rgb(255 195 90);
                        }

                        .lineRight::before {
                            content: "";
                            width: 10px;
                            height: 10px;
                            background: rgb(255 195 90);
                            border-radius: 50%;
                            position: absolute;
                            left: 0;
                            top: -4px;
                        }

                        .lineRight::after {
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

                    .contentLeft {
                        position: absolute;
                        margin-top: 28px;
                        text-align: center;
                        width: 40px;
                        right: 250px;
                        color: #2c78ee;
                    }
                }
            }
        }
    }
}
</style>