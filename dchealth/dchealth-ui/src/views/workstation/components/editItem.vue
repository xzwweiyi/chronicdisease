<template>
    <div class="components_value">
        <template v-if="parent">
            <el-form-item :label="info.propertyName" :required="info.required">
                <div v-if="info.isShowSubTitle">{{ info.subTitle }}</div>
                <template v-if="info.tag=='el-upload'">
                    <el-upload :action='info.action' :headers="info.headers" :list-type="info['list-type']"
                        :accept="info.accept" :name="info.name" :limit="info.limit" :multiple="info.multiple"
                        :on-success="file=>{return uploadSuccess(file,info,parent)}" :file-list="info.fileList"
                        :before-upload="file=>{return beforeUploadFile(file,info,parent)}">
                        <i slot="default" class="el-icon-plus"></i>
                    </el-upload>
                </template>
                <template v-else-if="info.labelType=='Radio'">
                    <el-radio-group v-model="info.defaultValue" class="components_groups">
                        <div v-for="(item,index) in info.options" :key="index" class="components_groups_item">
                            <el-radio :label="item.value" :class="item.isShowInputValue?'components_groups_radio':''">
                                {{ item.label }}
                                <el-input @input="event=>{return radioGroupChange(event,info,parent)}"
                                    v-if="item.isShowInputValue&&info.defaultValue==item.value"
                                    v-model="item.inputVal" />
                            </el-radio>
                        </div>
                    </el-radio-group>
                </template>
            </el-form-item>
        </template>
        <template v-else>
            <template v-if="info.tag=='el-upload'">
                <div v-if="info.isShowSubTitle">{{ info.subTitle }}</div>
                <el-upload :action='info.action' :headers="info.headers" :list-type="info['list-type']"
                    :accept="info.accept" :name="info.name" :limit="info.limit" :multiple="info.multiple"
                    :on-success="file=>{return uploadSuccess(file,info)}" :file-list="info.fileList"
                    :before-upload="file=>{return beforeUploadFile(file,info)}">
                    <i slot="default" class="el-icon-plus"></i>
                </el-upload>
            </template>
            <template v-else-if="info.labelType=='Radio'">
                <el-radio-group v-model="info.defaultValue" @change="radioChange" class="components_groups">
                    <div v-for="(item,index) in info.options" :key="index" class="components_groups_item">
                        <el-radio :label="item.value" :class="item.isShowInputValue?'components_groups_radio':''">
                            {{ item.label }}
                            <el-input @input="event=>{return radioGroupChange(event,info)}"
                                v-if="item.isShowInputValue&&info.defaultValue==item.value" v-model="item.inputVal" />
                        </el-radio>
                    </div>
                </el-radio-group>
            </template>

        </template>
    </div>
</template>

<script>
    export default {
        name: "workstation-item_value",
        props: ['info', 'parent'],
        components: {},
        data() {
            return {
                timer:null
            };
        },
        watch: {},
        computed: {},
        created() {
            this.initData()
        },
        methods: {
            initData() {},
            radioChange(event){
                console.log('radioChange',event)
            },
            // 上传成功
            uploadSuccess(params, info, parent) {
                this.$emit('img-upload-success', {
                    params,
                    info,
                    parent
                })
            },
            // 上传之前
            beforeUploadFile(file, info) {
                // console.log('beforeUploadFile', file, info)
                const units = {
                    KB: '1024',
                    MB: '1024/ 1024',
                    GB: '1024/ 1024/1024'
                }
                const unitNum = units[info.sizeUnit];
                if (info.fileSize) {
                    let isRightSize = (eval(file.size + '/' + unitNum) < info.fileSize)
                    if (!isRightSize) {
                        this.$message.error(`文件大小不能超过 ${info.fileSize}${info.sizeUnit}`)
                        return false
                    }

                }
                if (info.accept) {
                    let isAccept = new RegExp(info.accept).test(file.type)
                    if (!isAccept) {
                        this.$message.error(`应该选择${info.accept}类型的文件`)
                        return false
                    }
                }
            },
            // 单选 change和选项inputchange
            radioGroupChange(value, info, parent) {
                // console.log('radioGroupChange',value,info)
                if(this.timer!==null)clearTimeout(this.timer)
                this.timer = setTimeout(() => {
                    this.$emit('option-value-change', {
                        value,
                        info,
                        parent
                    })
                }, 800)
            }
        },
    };
</script>

<style lang="scss" scoped>
    .el-image {
        width: 150px;
        height: 100px;
        background: #f5f5f5;
        border: 1px solid #eaeaea;
        margin: 5px;

        .image-slot {
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
        }
    }

    ::v-deep .components_groups {
        display: flex;
        flex-wrap: wrap;

        .components_groups_item {
            margin-right: 15px;
            padding: 8px 0;

            .el-radio {
                display: flex;
                align-items: center;

                .el-radio__label {
                    display: flex;
                    align-items: center;
                    padding-left: 8px;

                    .el-input {
                        flex: 1;
                        margin-left: 10px;
                    }
                }
            }
        }
    }
</style>