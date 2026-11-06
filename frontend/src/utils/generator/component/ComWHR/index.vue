<template>
    <div class="WHR_component">
        <div v-for="(item,index) in itemList" :key="index" class="WHR_component_item">
            <el-input clearable v-model="item.label" class="WHR_lable" v-if="!isPreview"></el-input>
            <p v-else class="WHR_unit">{{ item.label }}</p>
            <el-input-number clearable v-model="item.value" class="WHR_value" :controls="false" @change="WHRValueChange" v-if="item.code !='WHR'" :precision="1"></el-input-number>
            <p v-else class="WHR_value_border" :key="item.value">{{ item.value }}</p>
            <p class="WHR_unit">{{ item.unit }}</p>
        </div>
    </div>
</template>

<script>
export default {
    props: ['options', 'isPreview', 'formula'],
    data() {
        return {
            itemList: this.options,
            componentValue: ''
        }
    },
    watch: {
        'options': {
            immediate: true,
            handler(newVal) {
                //console.log('options',newVal)
                this.itemList = [...newVal]
                //console.log(this.itemList)
            },
            deep: true,
        }
    },
    created() {

    },
    methods: {
        WHRValueChange(event) {
            let list = this.itemList
            let waist = this.getNumberValue(list, 'waist');
            let hip = this.getNumberValue(list, 'hip')
            let returnVal = undefined
            if (waist && hip) {
                let WHRvalue = waist / hip
                let info = { ...this.itemList[2], value: WHRvalue.toFixed(2) }
                this.itemList[2] = info
                this.options[2] = info
                this.itemList.forEach(item => {
                    returnVal[item.code] = item.value
                })
                returnVal = JSON.stringify(returnVal)
            }
            console.log('JSON.stringify(returnVal)',returnVal)
            this.$emit('value-change', returnVal)
        },
        getNumberValue(list, key) {
            return Number(list.find(f => f.code == key).value)
        }
    }
}
</script>

<style lang="scss" scoped>
    .WHR_component {
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        font-size: 15px;
        .WHR_component_item {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            margin-top: 3px;

            .WHR_lable {
                width: 150px;
            }

            .WHR_value {
                width: 100px !important;
                text-align: center;
            }
			.WHR_value_border{
				width: 90px !important;
				border: 1px solid #ccc;
                margin: 0px 5px;
                border-radius: 4px;
                padding: 0 15px;
                height: 36px;
			}
            .WHR_unit {
                margin: 0 5px;
            }
        }
    }
</style>