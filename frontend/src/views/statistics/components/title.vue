<template>
    <div class="com_title">
        <p class="title_text">{{title}}</p>
        <div class="title_empty"></div>
    </div>
</template>

<script>
export default ({
    name: "HelloWorld",
    components: {
        // VChart
    },
    props: ['title'],
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
.com_title {
    display: flex;
}

.title_empty,
.title_text {
    height: 4vh;
    line-height: 4vh;
}

.title_text {
    padding: 0 1vh;
    font-size: 1.6vh;
    letter-spacing: 2px;
    background: url("../../../assets/statistics/title_1_1.png") no-repeat;
    background-size: auto 1.8vh;
    background-position: right bottom;
}

.title_empty {
    flex: 1;
    background: url("../../../assets/statistics/title_1.png") no-repeat;
    background-size: 100% 1.8vh;
    background-position: left bottom;
}
</style>