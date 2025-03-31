<template>
    <div class="app-container">
        <el-row class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd"
                    v-hasPermi="['system:config:add']">添加新的访视点</el-button>
            </el-col>
        </el-row>
        <div id="classifyScroll" :class="showLeftIcon ? 'active' : ''">
            <div class="classifys">
                <i class="el-icon-arrow-right rightIcon" v-show="showRightIcon" @click="rightClick"></i>
                <i class="el-icon-arrow-left leftIcon" v-show="showLeftIcon" @click="leftClick"></i>
                <div class="node-desc classifys-item" v-for="(item, index) in classify" :key="index"
                    :ref="`item${index}`">
                    <div class="circle">{{index+1}}</div>
                    <div class="load-time" v-if="item.time">
                        <label>
                            <span>{{item.time}}</span>
                        </label>
                    </div>
                    <div class="line"></div>
                    <div class="content">{{item.name}}</div>
                    <div class="delBtn">
                        <el-button type="danger">删除</el-button>
                    </div>
                </div>
            </div>
            <div slot="footer" class="dialog-footer" style="margin:0 auto;text-align: center;">
                <el-button type="primary" @click="submitForm">编 辑</el-button>
                <el-button type="primary" @click="handCrf">下一步</el-button>
            </div>
        </div>
        <!-- 添加或修改缓存配置对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-position="top">
                <el-form-item label="与上一次随访间隔" prop="name">
                    <el-input v-model="form.name" placeholder="请输入" class="input-with-select">
                        <el-select v-model="value" slot="append" placeholder="请选择" style="width:130px">
                            <el-option v-for="item in nameOptions" :key="item.value" :label="item.name"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-input>
                </el-form-item>
                <el-form-item label="是否随访重复次数" prop="resource">
                    <el-radio-group v-model="form.resource">
                        <el-radio label="1">限制次数</el-radio>
                        <el-radio label="2">不限次数</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="随访重复次数" prop="value" v-if="form.resource == 1">
                    <el-input v-model="form.value" placeholder="请输入">
                        <template slot="append">次</template>
                    </el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {
    listConfig, getConfig, delConfig, addConfig, updateConfig, refreshCache
} from "@/api/system/config";
import { get } from "http";
export default {
    name: "queueSettingsIndex",
    dicts: ['sys_yes_no'],
    data() {
        return {
            // 遮罩层
            loading: true,
            showRightIcon: true, // 是否显示右箭头
            maxClickNum: 0, // 最大点击次数
            lastLeft: 0, // 上次滑动距离
            clickNum: 0, // 点击次数
            classify: [
                { name: "基线", time: "1个月", },
                { name: "随访01", time: "每1个月一次", },
                { name: "随访05", time: "", },
                { name: "结项", },
                { name: "运营", },
                { name: "进行", },
                { name: "完成", },
            ],
            // 是否显示
            open: false,
            // 弹出层标题
            title: "",
            // 随访
            nameOptions: [
                { name: '天', value: '0' },
                { name: '周', value: '1' },
                { name: '月', value: '2' },
            ],
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                configName: [{ required: true, message: "参数名称不能为空", trigger: "blur" }],
                configKey: [{ required: true, message: "参数键名不能为空", trigger: "blur" }],
                configValue: [{ required: true, message: "参数键值不能为空", trigger: "blur" }]
            },
            firstName: '张',
            lastName: '三'
        };
    },
    computed: {
        showLeftIcon() {
            return this.clickNum > 0
        }
    },
    created() {

    },
    methods: {
        // 点击右箭头（左侧滚动）
        rightClick() {
            // 如果点击次数小于数组长度-1时，执行左滑动效果。
            if (this.clickNum < this.classify.length - 1) {
                // 获取当前元素宽度
                let width = document.querySelectorAll('.classifys-item')[this.clickNum].offsetWidth
                // 获取最后一个元素距离左侧的距离
                let lastItemOffsetLeft = document.getElementsByClassName('classifys-item')[this.classify.length - 1]
                    .offsetLeft
                // 获取可视区域宽度
                const lookWidth = document.getElementById('classifyScroll').clientWidth
                // 如果最后一个元素距离左侧的距离大于可视区域的宽度，表示最后一个元素没有出现，执行滚动效果
                if (lastItemOffsetLeft > lookWidth) {
                    // 公示：滚动距离（元素的magin-left值） = 负的它自己的长度 + 上一次滑动的距离
                    document.getElementsByClassName('classifys')[0].style.marginLeft = `${-width + this.lastLeft}px`
                    this.lastLeft = -width + this.lastLeft
                    // 点击次数+1
                    this.clickNum += 1
                    // 记录到最后一个元素出现在可视区域时，点击次数的最大值。用于后面点击左侧箭头时判断右侧箭头的显示
                    this.maxClickNum = this.clickNum
                }
                /* 
                  如果最后一个元素距离左侧的距离大于可视距离+自身的宽度，说明没有到最后一个, 显示右侧箭头，否则隐藏右侧箭头
                  注意：这里要加上其自身的宽度，否则要多点击一下才会隐藏。因为点击的时候实际上最后一个元素还没有滚动，
                  它距离左侧的距离是大于可视区域的（也就是说获取的是上一次点击之后，距离左侧的距离）
                  所以这里要加上其自身的宽度
                  看不懂的话不用管，反正加上就对了
                */
                this.showRightIcon = lastItemOffsetLeft > lookWidth + width
            }
        },
        // 点击左箭头（右侧滚动）
        leftClick() {
            // console.log(this.lastLeft, 'lastLeft')
            // console.log(document.querySelectorAll('.classifys-item')[this.clickNum - 1].offsetWidth, '上一个的宽度')
            // 当点击次数大于0时才触发，这样当点击次数clickNum等于1的到时候，clickNum--等于0.根据计算属性的判断会隐藏掉左箭头
            if (this.clickNum > 0) {
                // 获取当前元素宽度
                let width = document.querySelectorAll('.classifys-item')[this.clickNum - 1].offsetWidth
                // 公示：滚动距离（元素的magin-left值） = 它自己的长度 + 上一次滑动的距离
                document.getElementsByClassName('classifys')[0].style.marginLeft = `${this.lastLeft + width}px`
                this.lastLeft = width + this.lastLeft
                // 点击次数-1
                this.clickNum--
                // 如果点击次数小于最大点击次数，说明最后一个元素已经不在可是区域内了，显示右箭头
                if (this.clickNum < this.maxClickNum) {
                    this.showRightIcon = true
                }
            }
        },
        // 点击下一步进入CRF
        handCrf() {
            this.$router.push({
                path: '/queueSettings/cycle/crf'
            })
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                name: undefined,
                host: undefined,
                port: undefined,
                resource: '1',
                password: undefined
            };
            this.resetForm("form");
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "设置周期";
        },
        submitForm: function () {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != undefined) {
                        if (this.form.password == this.form.newPassword) {
                            this.$modal.msgWarning("密码与新密码不能一致！");
                        }
                        updateRedis(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addRedis(this.form).then(response => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
                        });
                    }
                }
            });
        },
    }
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
        // 注意一下，这里没有用position: relative，，而图标用了position:absolute。因为这个东西是准备做组件的，我把相对定位写在父级的css标签上了。
        transition: all 0.3s;
        display: block;
        width: 100%;
        margin: 200px 0;
        white-space: nowrap;

        // display: flex;
        // align-items: center;
        .rightIcon {
            position: absolute;
            right: 40px;
            bottom: 65%;
            font-size: 60px;
            transform: translate(0, 50%);
            z-index: 9;
            color: #999;
        }

        .leftIcon {
            position: absolute;
            left: 40px;
            bottom: 65%;
            font-size: 60px;
            transform: translate(0, 50%);
            z-index: 9;
            color: #999;
        }

        &-item {
            font-size: 14px;
            font-weight: 400;
            width: 300px;
            //   margin-left: -4px;
            display: inline-block;
            text-align: center;
            white-space: nowrap;
            color: rgb(64 158 255);
            position: relative;

            &.active {
                .circle {
                    background: rgb(64 158 255);
                }

                .line {
                    background: rgb(64 158 255);
                }

                .content {
                    color: rgb(64 158 255);
                }
            }

            &:last-child {
                .line {
                    background: rgb(188, 188, 188, 0);
                }

                .content {
                    width: 100%;
                    // margin-left: -42px;
                }
            }

            .circle {
                position: absolute;
                top: -20px;
                width: 60px;
                height: 60px;
                line-height: 60px;
                border-radius: 50%;
                border: 1px solid rgb(64 158 255);
                background-color: #fff;
                cursor: pointer;
                font-size: 18px;
                font-weight: bold;
            }

            .load-time {
                position: absolute;
                top: -12px;
                width: 100%;
                text-align: center;

                label {
                    font-size: 12px;
                    color: #333;
                    // background: rgba(0,0,0, 0.2);
                    padding: 0 5px;
                    border-radius: 15px;
                }

                span {
                    color: #333;
                }
            }

            .line {
                width: 100%;
                height: 2px;
                display: inline-block;
                background: rgb(64 158 255);
            }

            .content {
                position: absolute;
                margin-top: 40px;
                text-align: center;
                width: 100%;
                left: -40%;
                color: #333;
            }

            .delBtn {
                position: absolute;
                margin-top: 70px;
                text-align: center;
                width: 100%;
                left: -40%;
            }
        }
    }
}
</style>