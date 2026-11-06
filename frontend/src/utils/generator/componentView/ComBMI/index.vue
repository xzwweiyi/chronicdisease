<template>
    <el-form inline class="view_componentBMI">
        <el-form-item v-for="item in options" :key="item.code + renderKey" :label="item.label + '：'">
            <span class="item_value">{{ info[item.code] }} {{ item.unit }}</span>
        </el-form-item>
    </el-form>
</template>

<script>
import moment from 'moment';
export default {
    props: ['info', 'options'],
    data() {
        return {
            itemList: this.options,
            componentValue: '',
            renderKey: moment().valueOf()
        }
    },
    watch: {
    },
    created() {

    },
    methods: {}
}
</script>

<style lang="scss" scoped>
.view_componentBMI {
    ::v-deep .item_value {
        line-height: 40px;
    }
}
</style>