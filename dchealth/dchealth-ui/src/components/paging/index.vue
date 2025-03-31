<template>
    <div class="paging_content" :class="flexType">
        <span>[第{{page}}页] </span>
        <span>[共{{total}}页]</span>
    </div>
</template>

<script>
    export default {
        name: 'Pagination',
        props: ['total', 'page', 'flexType'],
        data() {
            return {};
        },
        computed: {
        },
        created(){
            
        },
        methods: {
            handleSizeChange(val) {

            },
            handleCurrentChange(val) {

            }
        }
    }
</script>

<style scoped>
    .paging_content {
        display: flex;
        align-items: center;
        justify-content: center;
        color: #606266;
    }

    .flex_start {
        justify-content: flex-start;
    }

    .flex_center{
        justify-content: center;
    }
    .flex_end{
        justify-content: flex-end;
    }
</style>