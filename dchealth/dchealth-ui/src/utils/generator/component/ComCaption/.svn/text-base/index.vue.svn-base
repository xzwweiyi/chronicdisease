<template>
    <div class="caption_content">
        <el-alert :closable="closable" :type="type" :effect="effect">
            <template slot="title" >
                <div v-html="description"></div>
            </template>
        </el-alert>
    </div>
</template>

<script>
    export default {
        name: 'Pagination',
        props: ['type', 'closable', 'effect','description'],
        data() {
            return {
                value:'',
            };
        },
        computed: {
        },
        created(){
            
        },
        methods: {
            
        }
    }
</script>

<style lang="scss" scoped>
    .caption_content {
        ::v-deep .caption_description{
            .el-input__inner{
                border:0;
                background: transparent;
                &:focus{
                    border:0;
                    background: transparent;
                }
            }
        }
    }

</style>