<template>
    <div class="BMI_component">
        <div v-for="(item,index) in itemList" :key="index" class="BMI_component_item">
            <el-input clearable v-model="item.label" class="BMI_lable"></el-input>
            <el-input-number clearable v-model="item.value" class="BMI_value" :controls="false" @change="BMIValueChange"
                v-if="item.code"></el-input-number>
            <p v-else class="BMI_value" :key="item.value">{{ item.value }}</p>
            <p class="BMI_unit">{{ item.unit }}</p>
        </div>
    </div>
</template>

<script>
    export default {
        props: ['options', 'formula'],
        data() {
            return {
                itemList: this.options,
                componentValue:''
            }
        },
        watch: {
            'options': {
                immediate: true,
                handler(newVal) {
                    // console.log('options',newVal)
                    // this.itemList = [...newVal]
                },
                deep: true,
            }
        },
        created() {

        },
        methods: {
            BMIValueChange(event) {
                let list = this.itemList
                if (list.findIndex(f => f.code && !f.value) == -1) {
                    let weight = this.getNumberValue(list, 'weight');
                    let height = this.getNumberValue(list, 'height')
                    let BMIvalue = weight / (height * height)
                    let info = {...this.itemList[2],value:BMIvalue.toFixed(2)}
                    this.itemList[2] = info
                    // this.componentValue = BMIvalue
                    // console.log('info',info)
                }
            },
            getNumberValue(list, key) {
                return Number(list.find(f => f.code == key).value)
            }
        }
    }
</script>

<style lang="scss" scoped>
    .BMI_component {
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        font-size: 15px;
        .BMI_component_item {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            margin-top: 5px;

            .BMI_lable {
                width: 150px;
            }

            .BMI_value {
                width: 100px !important;
                text-align: center;
            }

            .BMI_unit {
                margin: 0 5px;
            }
        }
    }
</style>