<template>
    <div class="echarts-container">
        <e-charts class="echarts" v-if="optionsData" :option="optionsData" autoresize theme="light"></e-charts>
        <div class="noCount" v-else>
            <p>暂无数据</p>
        </div>
    </div>
</template>

<script>
import * as echarts from 'echarts';
import ECharts from "vue-echarts";
export default {
    name: 'chart',
    components: {
        ECharts
    },
    data() {
        return {}
    },
    props: ['optionsData'],
    watch: {
        optionsData(newVal) {
            // console.log(newVal, '12313213213')
        }
    },
    methods: {

    }
}

</script>

<style lang="scss" scoped>
.noCount,
.echarts,
.echarts-container {
    width: 100%;
    height: 100%;
}

.noCount {
    background: #f8f8f8;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #666;
}
</style>
