<template>
    <div class="display_flex ">
        <div class="info_icon" :class="hospitalInfo.class"></div>
        <div class="info_data">
            <p class="info_title">{{hospitalInfo.name}}</p>
            <animated class="num" :number="hospitalInfo.num"></animated>
        </div>
    </div>
</template>

<script>
import animated from './animated'
export default ({
    name: "HelloWorld",
    components: {
        animated
    },
    props: ['hospitalInfo'],
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

<style lang="scss" scoped>
.display_flex {
    display: flex;
    .info_data {
        flex: 1;
    }
}

.info_icon {
    width: 2.2vw;
    height: 2.2vw;
    border-radius: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0 0.5vw 1.5vh 0.3vw;
    font-size: 0.6vh;
}
.info_title {
    /* line-height: 2.6vh; */
    font-size: 1.4vh;
}

.num {
    font-size: 2.5vh;
    color: #44efff;
    text-align: center;
    width: 60%;
}

.icon_1 {
    background: url("../../../assets/statistics/icon_1.png");
    background-size: 100%;
}

.icon_2 {
    background: url("../../../assets/statistics/icon_2.png");
    background-size: 100%;
}

.icon_3 {
    background: url("../../../assets/statistics/icon_3.png");
    background-size: 100%;
}

.icon_4 {
    background: url("../../../assets/statistics/icon_4.png");
    background-size: 100%;
}

.icon_5 {
    background: url("../../../assets/statistics/icon_5.png");
    background-size: 100%;
}

.icon_6 {
    background: url("../../../assets/statistics/icon_6.png");
    background-size: 100%;
}
.icon_7 {
    background: url("../../../assets/statistics/icon_7.png");
    background-size: 100%;
}
.icon_8 {
    background: url("../../../assets/statistics/icon_8.png");
    background-size: 100%;
}
</style>