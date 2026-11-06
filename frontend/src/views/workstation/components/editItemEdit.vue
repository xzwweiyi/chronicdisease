<template>
    <div class="components_value">
        <p class="subTitle_style" v-if="componentsInfo.isShowSubTitle">{{componentsInfo.subTitle}}</p>
        <template v-if="showContent">
            <!-- 上传文件/图片 -->
            <!-- imageUrls -->
            <template v-if="componentsInfo.tag == 'el-upload'">
                <!-- 这里上传接口注意 -->
                <el-upload
                    :action="uploadImgUrl"
                    :file-list="componentsInfo.convertValueStr"
                    :headers="headers"
                    :name="componentsInfo.name"
                    :disabled="isDisabled"
                    :multiple="componentsInfo.multiple"
                    list-type="picture"
                    :accept="componentsInfo.accept"
                    :auto-upload="componentsInfo.autoUpload"
                    :limit="componentsInfo.limit"
                    :on-exceed="handleExceed"
                    :before-upload="beforeUpload"
                    :on-remove="handlerRemove"
                    :on-success="handleAvatarSuccess">
                    <el-button slot="trigger" size="small" type="primary">{{componentsInfo.buttonText}}</el-button>
                    <div slot="tip" class="el-upload__tip">只能上传{{componentsInfo.accept}}文件，且不超过{{componentsInfo.fileSize}}MB</div>
                </el-upload>
            </template>
            <!-- 滑块 -->
            <template v-else-if="componentsInfo.tag == 'el-slider'">
                <el-slider 
                    v-model="componentsInfo.convertValueStr" 
                    :min="componentsInfo.min" 
                    :max="componentsInfo.max" 
                    :step="componentsInfo.step"
                    :disabled="isDisabled"
                    :range="componentsInfo.range"
                    :marks="marks"></el-slider>
            </template>
            <!-- 下拉框 数据回显数据类型value有时是number-->
            <template v-else-if="componentsInfo.tag == 'el-select'">
                <el-select 
                    v-model="componentsInfo.convertValueStr" 
                    :disabled="isDisabled"
                    @change="changeValueFn(componentsInfo.vModel,componentsInfo.convertValueStr)"
                    :placeholder="componentsInfo.placeholder"
                    :filterable="componentsInfo.filterable ? componentsInfo.filterable : false"
                    :multiple="componentsInfo.multiple ? componentsInfo.multiple : false"
                >
                    <el-option
                    v-for="item in componentsInfo.options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                    </el-option>
                </el-select>
            </template>
            <!-- 多选组 -->
            <template v-else-if="componentsInfo.tag == 'el-checkbox-group'">
                <el-checkbox-group 
                    v-model="checkboxGroup" 
                    :disabled="isDisabled"
                    class="inline_group"
                    @change="changeValueFn(componentsInfo.vModel,checkboxGroup)"
                >
                    <el-checkbox v-for="item,index in componentsInfo.options" :key="index" :label="item.value">{{item.label}}</el-checkbox>
                </el-checkbox-group>
            </template>
            <!-- 单选组 -->
            <template v-else-if="componentsInfo.tag == 'el-radio-group'">
                <el-radio-group 
                    class="group_custom_item" 
                    :disabled="isDisabled"
                    v-model="componentsInfo.convertValueStr"
                    @change="changeValueFn(componentsInfo.vModel,componentsInfo.convertValueStr)"
                >
                    <el-radio v-for="(item,index) in componentsInfo.options" :key="index" :label="item.value">{{item.label}}</el-radio>
                </el-radio-group>
            </template>
            <!-- 评分 -->
            <el-rate 
                v-else-if="componentsInfo.tag=='el-rate'" 
                v-model="componentsInfo.convertValueStr" 
                show-score 
                :disabled="isDisabled"
                text-color="#ff9900" 
                score-template="{value}" 
                :max="componentsInfo.max"
            >
            </el-rate>
            <!-- 输入框 -->
            <template v-if="componentsInfo.tag == 'el-input'">
                <el-input 
                    v-model="componentsInfo.convertValueStr" 
                    :placeholder="componentsInfo.placeholder"
                    :disabled="isDisabled"
                    :type="componentsInfo.type"
                    :show-word-limit="componentsInfo.showWordLimit"
                    :maxlength="componentsInfo.maxlength"
                ></el-input>
            </template>
            <template v-else-if="componentsInfo.tag == 'com-BMI'" inline>
                <el-input 
                    v-model="componentsInfo.convertValueStr" 
                    :placeholder="componentsInfo.placeholder" 
                    :disabled="isDisabled"
                    :show-word-limit="componentsInfo.showWordLimit"
                    :maxlength="componentsInfo.maxlength"
                    :type="componentsInfo.type"></el-input>
            </template>
            <template v-else-if="componentsInfo.tag == 'com-DValue'" inline>
                <el-input 
                    v-model="componentsInfo.convertValueStr" 
                    :placeholder="componentsInfo.placeholder" 
                    :disabled="isDisabled"
                    :show-word-limit="componentsInfo.showWordLimit"
                    :maxlength="componentsInfo.maxlength"
                    :type="componentsInfo.type"
                ></el-input>
            </template>
            <template  v-else-if="componentsInfo.tag == 'el-switch'">
                <el-switch
                    v-model="componentsInfo.convertValueStr"
                    active-color="#13ce66"
                    :disabled="isDisabled"
                    inactive-color="#ff4949">
                </el-switch>
            </template>
            <!-- 日期时间选择 -->
            <template  v-else-if="componentsInfo.tag == 'el-date-picker'">
                <el-date-picker
                    v-model="componentsInfo.convertValueStr"
                    :type="componentsInfo.type"
                    :placeholder="componentsInfo.placeholder"
                    :disabled="isDisabled"
                    :format="componentsInfo.format"
                    :picker-options="componentsInfo.pickerOptions"
                    :value-format="componentsInfo.valueFormat"
                    >
                </el-date-picker>
            </template>
            <!-- 时间选择-->
            <template  v-else-if="componentsInfo.tag == 'el-time-picker'">
                <el-time-picker
                    v-model="componentsInfo.convertValueStr"
                    :picker-options="componentsInfo.pickerOptions"
                    :format="componentsInfo.format"
                    :value-format="componentsInfo.valueFormat"
                    :disabled="isDisabled"
                    :placeholder="componentsInfo.placeholder"
                >
                </el-time-picker>
            </template>
            <!-- 级联选择 -->
            <template  v-else-if="componentsInfo.tag == 'el-cascader'">
                <el-cascader
                    v-model="componentsInfo.convertValueStr"
                    :options="componentsInfo.options"
                    :disabled="isDisabled"
                    :props="componentsInfo.props"
                    :separator="componentsInfo.separator ? componentsInfo.separator : '/'"
                    :clearable="componentsInfo.clearable"
                    >
                </el-cascader>
            </template>
            <!-- 计数器 -->
            <template  v-else-if="componentsInfo.tag == 'el-input-number'">
                <el-input-number 
                    v-model="componentsInfo.convertValueStr" 
                    :label="componentsInfo.label"
                    :disabled="isDisabled"
                    :min="componentsInfo.min ? componentsInfo.min : -Infinity"
                    :max="componentsInfo.max ? componentsInfo.max : Infinity"
                    :step="componentsInfo.step ? componentsInfo.step : 1"
                    :step-strictly="componentsInfo.stepStrictly ? componentsInfo.stepStrictly : false"
                    :precision="componentsInfo.precision ? componentsInfo.precision : null"
                    :controls="componentsInfo.controls ? componentsInfo.controls : true"
                    :placeholder="componentsInfo.placeholder ? componentsInfo.placeholder : ''"
                >
                </el-input-number>
            </template>
            <!-- 时间计算器 -->
            <template v-else-if="componentsInfo.tag==='com-TimeNumeral'">
                <!-- <view-TimeNumeral :unit="componentsInfo.unit" :isSetEndTime="componentsInfo.isSetEndTime" :startDateLabel="componentsInfo.startDateLabel" :endDateLabel="componentsInfo.endDateLabel" :differenceLabel="componentsInfo.differenceLabel" :differenceUnit="componentsInfo.differenceUnit" :info="componentsInfo.convertValueStr"></view-TimeNumeral> -->
                <span style="padding-right: 15px;">{{componentsInfo.startDateLabel}}</span>
                <el-date-picker
                    v-model="startDate"
                    :type="componentsInfo.type"
                    :disabled="isDisabled"
                    :placeholder="componentsInfo.placeholder"
                    :value-format="componentsInfo.format"
                    :picker-options="startPickerOptions"
                    @change="handleStartChange"
                    >
                </el-date-picker>
                <span style="padding: 0 15px;">{{componentsInfo.endDateLabel}}</span>
                <el-date-picker
                    v-model="endDate"
                    :type="componentsInfo.type"
                    :disabled="isDisabled"
                    :placeholder="componentsInfo.placeholder"
                    :value-format="componentsInfo.format"
                    :picker-options="endPickerOptions"
                    @change="handleEndChange"
                    >
                </el-date-picker>
                <span style="padding: 0 15px;">{{componentsInfo.differenceLabel}}</span>
                <el-input disabled v-model="daysDiff" style="display: inline-block;width: 50px;"></el-input>
                <span style="padding: 0 15px;">{{componentsInfo.differenceUnit}}</span>
            </template>
            <!-- 未知 -->
            <!-- 说明文字? -->
            <template v-else-if="componentsInfo.tag == 'com-caption'">
                <com-caption :type="componentsInfo.type" :closable="componentsInfo.closable"
                    :effect="componentsInfo.effect" :description="componentsInfo.description"></com-caption>
            </template>
        </template>
        <!-- 验证规则提示信息 -->
        <!-- <div style="color: #F56C6C;">{{verifyInfo}}</div> -->
    </div>
</template>

<script>
import cityData from '@/utils/city.json'
import {
    getToken
} from "@/utils/auth";
export default {
    name: "workstation-components_editvalue",
    props: ['info','parent'],
    data() {
        return {
            isDisabled: false, // 是否禁用此表单组件
            verifyInfo: '',
            flag: false,
            uploadImgUrl: '', // 上传的图片服务器地址
            headers: {
                Authorization: "Bearer " + getToken(),
            },
            imageObj: [],
            componentsInfo: {},
            showContent: false,
            marks: undefined,
            startDate: null,
            endDate: null,
            daysDiff: null,
            checkboxGroup: []
        };
    },
    created() {
        this.initData()
    },
    computed: {
        startPickerOptions() {
        return {
            disabledDate: time => {
            if (this.endDate) {
                return time > new Date(this.endDate)
            }
            return false
            }
        }
        },
        endPickerOptions() {
        return {
            disabledDate: time => {
            if (this.startDate) {
                return time < new Date(this.startDate)
            }
            return false
            }
        }
        }
    },
    methods: {
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
            // console.log(componentsInfo);
            componentsInfo.convertValueStr = null
            if(componentsInfo.tag == 'el-checkbox-group') {
                if(componentsInfo.options) {
                    componentsInfo.options.forEach(item => {
                        item.value = String(item.value)
                    })
                }
                componentsInfo.convertValueStr = null
                this.checkboxGroup = componentsInfo.propertyValue ? componentsInfo.propertyValue.split(',') : []
            } else if(componentsInfo.tag == 'el-radio-group') {
                if(componentsInfo.options) {
                    componentsInfo.options.forEach(item => {
                        item.value = String(item.value)
                    })
                }
                componentsInfo.convertValueStr = componentsInfo.propertyValue ? componentsInfo.propertyValue : null
            } else if(componentsInfo.tag == 'el-select'){
                if(componentsInfo.options) {
                    componentsInfo.options.forEach(item => {
                        item.value = String(item.value)
                    })
                }
                if(componentsInfo.multiple) {
                    componentsInfo.convertValueStr = componentsInfo.propertyValue ? componentsInfo.propertyValue.split(',') : []
                }  else {
                    componentsInfo.convertValueStr = componentsInfo.propertyValue ? componentsInfo.propertyValue : null
                }
            } else if( componentsInfo.tag == 'el-upload') {
                let list = componentsInfo.imageUrls ? componentsInfo.imageUrls : []
                if(componentsInfo.multiple) {
                    this.uploadImgUrl = process.env.VUE_APP_BASE_API + "/common/uploads"
                } else {
                    this.uploadImgUrl = process.env.VUE_APP_BASE_API + "/common/upload"
                }
                if(list.length > 0) {
                    let fileList = []
                    let imageObj = []
                    list.forEach(item => {
                        let obj = {
                            url: item
                        }
                        fileList.push(obj)
                        let obj1 = {
                            response: obj
                        }
                        imageObj.push(obj1)
                    })
                    componentsInfo.convertValueStr = fileList
                    this.imageObj = imageObj
                } else {
                    componentsInfo.convertValueStr = []
                }
            } else if(componentsInfo.tag == 'el-slider' || componentsInfo.tag == 'el-rate' ) {
                componentsInfo.convertValueStr = componentsInfo.propertyValue ? Number(componentsInfo.propertyValue) : null
            } else if(componentsInfo.tag == 'com-TimeNumeral' && componentsInfo.propertyValue) {
                // 时间计算器
                let obj = JSON.parse(componentsInfo.propertyValue)
                this.startDate = obj.start
                this.endDate = obj.end
                this.daysDiff = obj.difference
            } else if(componentsInfo.tag == 'el-switch') {
                if(componentsInfo.propertyValue) {
                    if(componentsInfo.propertyValue == 'true') {
                        componentsInfo.convertValueStr = true
                    } else {
                        componentsInfo.convertValueStr = false
                    }
                }
            } else if(componentsInfo.tag == 'el-cascader'){
                componentsInfo.convertValueStr = componentsInfo.propertyValue ? JSON.parse(componentsInfo.propertyValue) : []
                if(componentsInfo.propertyName == '省市县'){
                    // 需要处理级联选择数据
                    if(componentsInfo.options) {
                        componentsInfo.options.forEach(item1 => {
                            item1.level = 1
                            item1.label = item1.name
                            item1.value = item1.code
                            if(item1.children) {
                                item1.children.forEach(item2 => {
                                    item2.level = 2
                                    item2.label = item2.name
                                    item2.value = item2.code
                                    if(item2.children) {
                                        item2.children.forEach(item3 => {
                                            item3.level = 3
                                            item3.label = item3.name
                                            item3.value = item3.code
                                            if(item3.children) {
                                                item3.children.forEach(item4 => {
                                                    item4.level = 4
                                                    item4.label = item3.name
                                                    item4.value = item3.code
                                                })
                                            }
                                        })
                                    }
                                })
                            }
                        });
                    }
                }
            } else {
                componentsInfo.convertValueStr = componentsInfo.propertyValue ? componentsInfo.propertyValue : null
            }
            if(componentsInfo.relationInfo) {
                this.isDisabled = true
            }
            this.componentsInfo = componentsInfo
            this.showContent = true
        },
        changeValueFn(vModel,value) {
            this.$emit('searchSonVmodel',vModel,value)
            // 反馈给父，让父去调用子，找对应需要显示与否的子
        },
        // 父循环找需要修改显示的子
        searchVmodel(vModel,value){
            // 判断是否达成禁用条件 
            if(this.componentsInfo.relationInfo) {
                if(this.componentsInfo.relationInfo.relationQuestion[0].questionVModel == vModel) {
                    // 如果是多选框组 需要看数据 或者/包含都需要显示
                    if(this.componentsInfo.relationInfo.relationQuestion[0].tag == 'el-checkbox-group'){
                        let arr2 =  this.componentsInfo.relationInfo.relationQuestion[0].checkedOptions
                        let arr1 = value
                        let flag = true;  // 默认值为true
                        // 检查arr1中是否包含arr2中的任何一个元素
                        for (let i = 0; i < arr2.length; i++) {
                            if (arr1.includes(arr2[i])) {
                                flag = false;
                                break;  // 一旦找到匹配项，立即跳出循环
                            }
                        }
                        this.isDisabled = flag
                        if(this.isDisabled) {
                            this.initData()
                        }
                    } else {
                        let obj = this.componentsInfo.relationInfo.relationQuestion[0]
                        this.isDisabled = !obj.checkedOptions.some(item1 => item1 == value)
                        if(this.isDisabled) {
                            this.initData()
                        }
                    }
                    // console.log(123456,this.componentsInfo);
                }
            }
        },
        handleStartChange(val) {
            if (val && this.endDate && new Date(val) > new Date(this.endDate)) {
                this.endDate = ''
            }
            this.calculateDays()
        },
        handleEndChange(val) {
            if (val && this.startDate && new Date(val) < new Date(this.startDate)) {
                this.startDate = ''
            }
            this.calculateDays()
        },
        calculateDays() {
            if (this.startDate && this.endDate) {
                const diff = Math.abs(new Date(this.endDate) - new Date(this.startDate))
                this.daysDiff = Math.round(diff / (1000 * 3600 * 24))
            } else {
                this.daysDiff = null
            }
        },
        handleExceed(files, fileList) {
            this.$message.warning(`当前限制选择 ${this.componentsInfo.limit} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
        },
        beforeUpload(file) {
            // 上传文件大小限制
            let isSize
            if (this.isFileSizeWithinLimit(file.size, this.componentsInfo.fileSize, this.componentsInfo.sizeUnit)) {
                isSize = true
            } else {
                this.$message.error(`上传文件大小不能超过${this.componentsInfo.fileSize}${this.componentsInfo.sizeUnit}!`);
                isSize = false
            }
            return isSize
        },
        isFileSizeWithinLimit(uploadFileSizeBytes, fileSize, sizeUnit) {
            // 将 fileSize 转换为字节
            let limitInBytes;
            switch (sizeUnit.toUpperCase()) {
                case "KB":
                    limitInBytes = fileSize * 1024;
                    break;
                case "MB":
                    limitInBytes = fileSize * 1024 * 1024;
                    break;
                case "GB":
                    limitInBytes = fileSize * 1024 * 1024 * 1024;
                    break;
                default:
                    throw new Error("Invalid size unit. Use KB, MB, or GB.");
            }
            // 比较上传文件的大小和限制大小
            return uploadFileSizeBytes <= limitInBytes;
        },
        handleAvatarSuccess(response, file, fileList) {
            if(response.code==200&&response.msg=='操作成功'){
                this.imageObj = fileList
            }
        },
        handlerRemove(file, fileList) {
            this.imageObj = fileList
            if(this.componentsInfo.required) {
                if( fileList.length < 1) {
                    this.verifyInfo = `${this.componentsInfo.propertyName}为必填项`
                    // this.$message.error(`${this.componentsInfo.propertyName}为必填项`)
                    this.flag = true
                    return false
                }
            }
        },
        // 填写验证 - start
        // required // 必填
        // regValue // 验证规则
        verifyFn() {
            this.verifyInfo = ''
            let one = false
            let two = false
            if(this.componentsInfo.required) {
                if( this.componentsInfo.convertValueStr && this.componentsInfo.convertValueStr.length > 0) {
                    one = true
                } else {
                    one = false
                    this.verifyInfo = `${this.componentsInfo.propertyName}为必填项`
                    // this.$message.error(`${this.componentsInfo.propertyName}为必填项`)
                    this.flag = true
                    return false
                }
            } else {
                one = true
            }
            if(this.componentsInfo.regValue) {
                let reg = this.componentsInfo.regValue
                if(Array.isArray(reg) && reg.length > 0) {
                    reg.forEach(item => {
                        // 去掉开头和结尾的斜杠，提取出正则表达式的模式部分
                        const regexPattern = item.pattern.slice(1, -1);
                        let regExp = new RegExp(regexPattern)
                        if(!regExp.test(this.componentsInfo.convertValueStr)){
                            this.verifyInfo = item.message
                            // this.$message.error(item.message)
                            two = false
                            this.flag = false
                            return false
                        }
                    })
                } else if(reg.pattern) {
                    // 是对象
                    const regexPattern = reg.pattern.slice(1, -1);
                    let regExp = new RegExp(regexPattern)
                    if(!regExp.test(this.componentsInfo.convertValueStr)){
                        this.verifyInfo = reg.message
                        // this.$message.error(reg.message)
                        two = false
                        this.flag = false
                        return false
                    }else {
                        two = true
                    }
                } else {
                    two = true
                }
                if(!two) {
                    this.flag = false
                    return  false
                }
            } else{
                two = true
            }
            this.flag = true
        },  
        // 给父传校验结果
        flagToParent() {
            return this.flag
        },
        dataToParent() {
            let imageUrl = []
            this.imageObj.forEach(item => {
                imageUrl.push(item.response ? item.response.url : item.url)
            })
            let imageUrls = {
                medicalFormId: this.componentsInfo.medicalFormId,
                propertyId:  this.componentsInfo.id,
                imageUrl: imageUrl
            }
            if(this.componentsInfo.tag == 'com-TimeNumeral') {
                let obj = {
                    start: this.startDate,
                    end: this.endDate,
                    difference: this.daysDiff,
                }
                this.componentsInfo.convertValueStr = JSON.stringify(obj)
            }
            if(this.componentsInfo.tag == 'el-checkbox-group') {
                this.componentsInfo.convertValueStr = this.checkboxGroup.length > 0 ? this.checkboxGroup.join(',') : null
            }
            return {
                data:this.componentsInfo,
                imageUrls:imageUrls,
                parent: this.parent
            }
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
