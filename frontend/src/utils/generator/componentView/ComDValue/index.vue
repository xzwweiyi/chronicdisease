<template>
    <el-form class="default_value_component">
        <el-form-item v-for="(item,index) in options" :key="index" :label="item.propertyName+':'">
            <p class="difference_val">{{ info&&info[item.propertyCode] }}</p>
            <p v-if="item.unit">{{ item.unit }}</p>
        </el-form-item>
    </el-form>
</template>

<script>
export default {
    props: ['options', 'info'],
    data() {
        return {
            comNumber: undefined,
            precision: 2,
            differenceVal: 0,
            componentVal: [undefined, undefined]
        }
    },
    computed: {},
    watch: {
        componentVal: {
            handler(newVal) {
                if (newVal[0] && newVal[1]) {
                    this.differenceVal = newVal[1] - newVal[0]
                }
            }
        }
    },
    created() { },
    methods: {

    }
}
</script>

<style lang="scss" scoped>
.default_value_component {
    display: inline-flex;
    flex-direction: column;
    margin-left: 10px;
    ::v-deep .el-form-item {
        display: inline-flex;
        align-items: center;
        margin-bottom: 0px !important;

        .el-form-item__content {
            display: inline-flex;
        }

        .is-without-controls {
            width: 120px;
        }
        .difference_val {
            margin: 0 3px;
            border: 1px solid #dcdfe6;
            min-width: 50px;
            border-radius: 3px;
            text-align: center;
        }
    }
}
</style>