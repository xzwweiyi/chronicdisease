<template>
    <div class="orgin_list" ref="scrollContainer">
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
            scrollTimer: null
        }
    },
    mounted() {
        document.addEventListener('visibilitychange', this.handlePageVisibilityChange)
        this.startAutoScroll()
    },
    activated() {
        this.startAutoScroll()
    },
    deactivated() {
        this.stopAutoScroll()
    },
    beforeDestroy() {
        document.removeEventListener('visibilitychange', this.handlePageVisibilityChange)
        this.stopAutoScroll()
    },
    watch: {
        list: {
            handler() {
                this.$nextTick(() => {
                    this.startAutoScroll()
                })
            },
            deep: true
        }
    },
    methods: {
        handlePageVisibilityChange() {
            if (document.hidden) {
                this.stopAutoScroll()
                return
            }
            this.startAutoScroll()
        },
        isContainerVisible(container) {
            if (!container) {
                return false
            }
            if (document.hidden) {
                return false
            }
            return container.offsetParent !== null
        },
        stopAutoScroll() {
            if (this.scrollTimer) {
                clearInterval(this.scrollTimer)
                this.scrollTimer = null
            }
        },
        startAutoScroll() {
            this.stopAutoScroll()
            const container = this.$refs.scrollContainer
            if (!this.isContainerVisible(container)) {
                return
            }
            container.scrollTop = 0
            this.scrollTimer = setInterval(() => {
                if (this._isDestroyed || this._isBeingDestroyed) {
                    this.stopAutoScroll()
                    return
                }
                if (!this.isContainerVisible(container)) {
                    this.stopAutoScroll()
                    return
                }
                const maxScrollTop = container.scrollHeight - container.clientHeight
                if (maxScrollTop <= 0) {
                    return
                }
                // 避免因为小数精度或浏览器取整导致到底后卡住
                if (container.scrollTop >= (maxScrollTop - 1)) {
                    container.scrollTop = 0
                    return
                }
                const prevTop = container.scrollTop
                container.scrollTop = prevTop + 1
                // 兜底：如果已经无法继续下滚，直接回到顶部继续循环
                if (container.scrollTop === prevTop) {
                    container.scrollTop = 0
                }
            }, 60)
        }
    },
});
</script>

<style scoped>
.orgin_list {
    display: flex;
    flex-direction: column;
    padding: 1vh 0;
    width: 100%;
    height: 100%;
    overflow-y: auto;
    overflow-x: hidden;
    -ms-overflow-style: none;
    scrollbar-width: none;
}

.orgin_list::-webkit-scrollbar {
    width: 0;
    height: 0;
    display: none;
}

.list_item {
    display: flex;
    align-items: center;
    color: #fff;
    flex: 0 0 4.5vh;
    width: 100%;
    height: 4.5vh;
    min-height: 4.5vh;
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