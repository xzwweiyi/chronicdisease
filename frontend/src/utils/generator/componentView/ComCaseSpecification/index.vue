<template>
    <div class="case_specification">
        <div v-if="result">{{ result }}</div>
        <div v-else class="placeholder">{{ placeholder }}</div>
    </div>
</template>

<script>
    export default {
        props: [ 'placeholder', 'result'],
        data() {
            return {
               
            }
        },
        watch: {
        },
        created() {

        },
        methods: {

        }
    }
</script>

<style lang="scss" scoped>
    .case_specification {
        .placeholder{
            color:#999;
        }
        

    }
</style>