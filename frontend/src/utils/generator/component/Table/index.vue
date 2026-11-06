<template>
    <el-table border :data="options" :key="tableHeader.length&&options.length">
        <el-table-column v-for="(item,index) in tableHeader" :key="index" :prop="item" :label="item"
            :align="tableAlignType" show-overflow-tooltip>
        </el-table-column>
    </el-table>


</template>

<script>
    export default {
        data() {
            return {}
        },
        props: ['options', 'headers', 'preview', 'tableAlignType'],
        computed: {
            tableHeader() {
                let tempArray = Object.keys(this.options[0])
                console.log('tempArray',tempArray)
                return tempArray || []
            }
        },
        watch: {},
        created() {
            console.log('newVal', this.options, this.headers)
        },

        methods: {}
    }
</script>

<style lang="scss" scoped>
    ::v-deep .el-input .el-input__inner {
        background: transparent;
        border: none;
    }
</style>