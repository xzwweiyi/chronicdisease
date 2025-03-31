<!-- 已答问卷列表 -->
<template>
    <div>
        <el-form v-if="formList&&formList.length">
            <render-item v-for="(element, index) in formList" :key="element.renderKey" :element="element"
                :index="index" />
        </el-form>
        <el-empty v-else :description="errorMessage"></el-empty>
    </div>
</template>

<script>
    import renderItem from './renderItem.vue'
    export default {
        name: "list-details",
        components: {
            renderItem
        },
        data() {
            return {};
        },
        props: ['id', 'formList','errorMessage'],
        computed: {},
        watch: {

        },
        created() {
            //    this.fileds = this.formList.subjectList
        },
        methods: {

        }
    };
</script>
<style lang='scss' scoped>
</style>