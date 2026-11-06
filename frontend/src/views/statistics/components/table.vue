<template>
    <div class="city_list">
        <div class="display_flex city_list_item" v-for="(item,index) in tableList" :key="index">
            <div class="info">{{index==0?'排序':index}}</div>
            <div class="info">{{item.cityName}}</div>
            <template v-if="index==0">
                <div class="info">{{item.newPatientCount}}</div>
                <div class="info">{{item.patientCount}}</div>
            </template>
            <template v-else>
                <animated :number="item.newPatientCount||0" class="info"></animated>
                <animated :number="item.patientCount" class="info"></animated>
            </template>
        </div>
    </div>
</template>

<script>
import animated from './animated.vue'
export default ({
    // eslint-disable-next-line vue/multi-word-component-names
    name: "mytable",
    components: {
        animated
    },
    props: ['tableList'],
    data() {
        return {

        }
    },
    created() {

    },
    methods: {

    },
});
</script>

<style scoped>
.city_list {
    padding-bottom: 1vh;
    min-height: 200px;
}

.info {
    flex: 1;
    font-size: 1.4vh;
    line-height: 4vh;
}

.city_list_item {
    display: flex;
    text-align: center;
}

.city_list .city_list_item:nth-child(1) {
    background: rgba(10, 95, 173, 0.6);
}

.city_list .city_list_item {
    border-bottom: 1px #0a5fad99 solid;
}

.city_list .city_list_item:last-child {
    border: none;
}
</style>