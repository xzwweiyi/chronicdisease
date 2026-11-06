<template>
    <div class="com_title" v-if="type == 'dept'">{{number}}</div>
    <div class="com_title" v-else>{{tweeningValue}}</div>
</template>

<script>
import TWEEN from '@/views/components/options/gsap.min.js'
export default ({
    name: "HelloWorld",
    components: {
        // VChart
    },
    props: ['number', 'type'],
    data() {
        return {
            tweeningValue: 0
        }
    },
    computed: {},
    watch: {
        number: function (newValue, oldValue) {
            this.tween(oldValue, newValue)
        }
    },
    mounted: function () {
        this.tween(0, this.number)
    },
    created() { },
    methods: {
        tween(startValue, endValue) {
            var vm = this

            function animate() {
                if (TWEEN.update()) {
                    requestAnimationFrame(animate)
                }
            }

            new TWEEN.Tween({
                tweeningValue: startValue
            })
                .to({
                    tweeningValue: endValue
                }, 500)
                .onUpdate(function () {
                    vm.tweeningValue = this.tweeningValue.toFixed(0)
                })
                .start()

            animate()
            // setTimeout(()=>{
            //     this.tween(0, this.number)
            // },4000)
        }
    },
});
</script>

<style scoped>
</style>