<template>
    <div class="orgin_list">
        <div class="display_flex list_item" v-for="(item,index) in list" :key="index">
            <div class="item_icon" :class="index>2?'blueBg':'Top3Bg'">{{index+1}}</div>
            <div class="item_label">{{item.provinceName}}</div>
            <div class="info_data">
                <div class="press" :style="'width:'+item.percent+'%'"></div>
            </div>
            <div class="value">{{item.patientCount | numberFormat}}</div>
        </div>
    </div>
</template>

<script>
export default ({
    name: "HelloWorld",
    components: {
        // VChart
    },
    props: ['list'],
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
.orgin_list {
    display: flex;
    flex-direction: column;
    padding: 1vh 0;
    width: 100%;
}

.list_item {
    display: flex;
    align-items: center;
    color: #fff;
    flex: 1;
    width: 100%;
    max-height: 45px;
}

.item_icon {
    width: 2.6vh;
    height: 2.6vh;
    font-size: 1.1vh;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 0.5vh;
}

.blueBg {
    background: #00affa;
}

.Top3Bg {
    background: #f09d2c;
}

.item_label {
    margin-left: 1vh;
    font-size: 1.4vh;
    width: 7vh;
    text-align: left;
}

.value {
    font-size: 1.6vh;
    width: 5.5vh;
    text-align: right;
}

.info_data {
    flex: 1;
    height: 1vh;
    background: rgba(48, 166, 255, 0.4);
    margin-right: 0.5vh;
}

.press {
    background: rgba(48, 166, 255, 1);
    height: 100%;
}
</style>