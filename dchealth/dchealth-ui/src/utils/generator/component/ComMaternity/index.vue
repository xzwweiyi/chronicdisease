<template>
    <div class="maternity_component">
        <el-form>
            <el-form-item>
                <div class="maternity_baseInfo">
                    <span>孕</span>
                    <el-input-number :controls="false" v-model="defaultValue[0]" :min="defaultValue[1]" />
                    <span>次，</span>
                    <span>产</span>
                    <el-input-number :controls="false" v-model="defaultValue[1]" :max="defaultValue[0]" :min="0" />
                    <span>次</span>
                </div>
            </el-form-item>
            <el-form-item v-if="defaultValue[1]">
                <el-form inline label-width="80px" v-for="(item,index) in maternityList"
                    :key="index+maternityList.length">
                    <h4 class="maternity_number">生产第{{ index+1 }}次</h4>
                    <el-form-item :label="item.childbirth.label" class="maternity_item">
                        <el-radio-group v-model="item.childbirth.value">
                            <el-radio v-for="(optionItem,optionIndex) in item.childbirth.option" :key="optionIndex"
                                :label="optionItem.value">{{ optionItem.label }}</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item :label="item.birthHistory.label" class="maternity_item">
                        <el-checkbox-group v-model="item.birthHistory.value">
                            <el-checkbox v-for="(optionItem,optionIndex) in item.birthHistory.option" :key="optionIndex"
                                :label="optionItem.value">{{ optionItem.label }}</el-checkbox>
                        </el-checkbox-group>
                    </el-form-item>
                    <el-form-item :label="item.embryo.label" class="maternity_item"
                        label-width="100px">
                        <el-input-number v-model="item.embryo.value"
                            :min="item.embryo.min" :max="item.embryo.max" :controls="false" />
                        <span v-if="item.embryo.unit">{{ item.embryo.unit }}</span>
                    </el-form-item>
                </el-form>

            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    export default {
        props: ['value', 'placeholder', 'options'],
        data() {
            return {
                defaultValue: [],
                maternityList: this.options,
                maternityInfo: JSON.parse(JSON.stringify(this.options[0]))
            }
        },
        watch: {
            defaultValue(newVal) {
                if (newVal[1] >= 2) {
                    if (this.maternityList.length < newVal[1]) {
                        let stempArr = new Array(newVal[1] - this.maternityList.length).toString().split(',')
                            .map((item, index) => {
                                return index
                            });
                        let newArray = []
                        stempArr.forEach(item => {
                            newArray.push(JSON.parse(JSON.stringify(this.maternityInfo)))
                        })
                        this.maternityList = this.maternityList.concat(newArray)
                    } else {
                        this.maternityList = this.maternityList.slice(0, newVal[1])
                    }
                } else if (newVal[1] == 1) {
                    this.maternityList = [JSON.parse(JSON.stringify(this.maternityInfo))]
                } else {
                    this.maternityList = []
                }
            },
            options: {
                immediate: true,
                deep: true,
                handler(newVal) {
                    // console.log('options',newVal)
                    this.maternityList = newVal
                }
            }
        },
        created() {},
        methods: {
        }
    }
</script>

<style lang="scss" scoped>
    .maternity_component {
        color: #333;

        .maternity_baseInfo {
            display: flex;
            color: #333;

            ::v-deep .el-input-number.is-without-controls {
                width: 80px !important;
            }
        }

        .maternity_number {
            margin-top: 15px;
        }

        .maternity_item {
            display: flex;
            padding: 0 !important;
            margin-top: 5px !important;

            ::v-deep .el-form-item__label {
                flex-shrink: 0;
                color: #606266;
            }

            ::v-deep .el-radio-group,
            .el-checkbox-group {
                padding: 5px 0;

                .el-radio,
                .el-checkbox {
                    line-height: 1.6;
                }
            }
        }
    }
</style>