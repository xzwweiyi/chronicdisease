<template>
    <div class="caption_content">
        <el-alert :closable="closable" :type="type" :effect="effect">
            <template slot="title">
                <div v-html="description" class="descript_html"></div>
            </template>
        </el-alert>
    </div>
</template>

<script>
export default {
    name: 'Pagination',
    props: ['type', 'closable', 'effect', 'description'],
    data() {
        return {
            value: '',
        };
    },
    computed: {
    },
    created() {

    },
    methods: {

    }
}
</script>

<style lang="scss" scoped>
.caption_content {
    .descript_html{
        min-height:20px;
    }
}
</style>