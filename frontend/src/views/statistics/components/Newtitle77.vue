<template>
    <div class="com_title">
        <p class="title_text">{{title}}</p>
    </div>
</template>

<script>
export default ({
    name: "",
    components: {
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
    justify-content: center;
    align-items: center;
    height: 35px;
}
.title_text {
  height: 19px;
  font-size: 1.8vh;
  font-family: Source Han Sans CN, Source Han Sans CN-Regular;
  font-weight: 400;
  text-align: left;
  color: #dae9ff;
  line-height: 35px;
}

</style>