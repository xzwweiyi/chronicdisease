<template>
    <el-drawer v-bind="$attrs" v-on="$listeners" @opened="onOpen" @close="onClose">
        <div style="height:100%">
            <div class="action-bar" :style="{'text-align': 'left'}">
                <el-radio-group v-model="isAppPreview">
                    <el-radio-button :label="false">电脑预览</el-radio-button>
                    <el-radio-button :label="true">手机预览</el-radio-button>
                </el-radio-group>
                <span class="bar-btn delete-btn" @click="$emit('update:visible', false)">
                    <i class="el-icon-circle-close" />
                    关闭
                </span>
            </div>
            <div :class="isAppPreview?'app_preview':'result-wrapper'" :style="'width:'+screenWidth+';height:'+screenHeight" class="preview_style">
                <div v-loading="isIframeLoaded" class="preview_container_form">
                    <preview-dom :preview-form-data="previewFormFields" :key="renderKey" id="containerForm" />
                </div>
            </div>
            <el-select v-if="isAppPreview" v-model="currentScreen" class="screen_option">
                <el-option v-for="item in screenList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
            </el-select>
        </div>
    </el-drawer>
</template>
<script>
import previewDom from './previewForm/index'

export default {
    components: {
        previewDom
    },
    props: ['formData', 'generateConf'],
    data() {
        return {
            htmlCode: '',
            jsCode: '',
            cssCode: '',
            codeFrame: '',
            isIframeLoaded: false,
            isInitcode: false, // 保证open后两个异步只执行一次runcode
            isRefreshCode: false, // 每次打开都需要重新刷新代码
            scripts: [],
            links: [],
            previewPageData: {},
            isAppPreview: false,
            screenList: [
                {
                    value: '320*568',
                    width: 320,
                    height: 568,
                    label: 'iPhone 5/SE'
                },
                {
                    value: '360*640',
                    width: 360,
                    height: 640,
                    label: 'Galaxy S5'
                },
                {
                    value: '375*667',
                    width: 375,
                    height: 667,
                    label: 'iPhone 6/7/8'
                },
                {
                    value: '414*736',
                    width: 414,
                    height: 736,
                    label: 'iPhone 6/7/8 Plus'
                },
                {
                    value: '375*812',
                    width: 375,
                    height: 812,
                    label: 'iPhone X'
                },
                {
                    value: '414*896',
                    width: 414,
                    height: 896,
                    label: 'iPhone XR'
                },
                {
                    value: '390*844',
                    width: 390,
                    height: 844,
                    label: 'iPhone 12 Pro'
                },
            ],
            currentScreen: '375*667',
            screenWidth: '90%',
            screenHeight: 'calc(100vh - 50px)',
            formRenderList: [],
            formModel: {},
            previewFormFields: undefined,
            preViewFormRules: undefined,
            previewFormModel: undefined,
            renderKey: new Date() + ''
        }
    },
    computed: {},
    watch: {
        isAppPreview(newVal) {
            if (newVal) {
                let current = this.screenList.find(f => f.value == '375*667')
                this.screenWidth = current.width + 'px';
                this.screenHeight = current.height + 'px';
            } else {
                this.screenWidth = '90%';
                this.screenHeight = 'calc(100vh - 50px)';
            }
        },
        currentScreen(newval) {
            let current = this.screenList.find(f => f.value == newval)
            this.screenWidth = current.width + 'px';
            this.screenHeight = current.height + 'px';
        },
        formData: {
            immediate: true,
            deep: true,
            handler(newVal, oldVal) {
                const { fields, ...otherData } = newVal
                if (fields) {
                    this.previewFormFields = JSON.parse(JSON.stringify(fields));
                }
                this.renderKey = new Date() + ''
            }
        }
    },
    created() { },
    mounted() {

    },
    beforeDestroy() { },
    methods: {
        onOpen() {
            this.renderKey = new Date() + ''
        },
        onClose() {

        },
    }
}
</script>

<style lang="scss" scoped>
@import "@/utils/from-generator/mixin.scss";

.result-wrapper {
    height: calc(100vh - 33px);
    width: 100%;
    overflow: auto;
    padding: 12px;
    box-sizing: border-box;
    margin: 0 auto;
}

.app_preview {
    margin: 10px 20px 0 50px;
    overflow: auto;
    padding: 20px 20px;
    float: left;
    border: 1px solid #dcdfe6;
    border-radius: 5px;
    background: #f5f5f5;
}

.screen_option {
    margin-top: 12px;
}

@include action-bar;

::v-deep .el-drawer__header {
    display: none;
}

/*滚动条样式*/
.preview_style::-webkit-scrollbar {
    width: 4px;
}

.preview_style::-webkit-scrollbar-thumb {
    border-radius: 10px;
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
    background: rgba(0, 0, 0, 0.2);
}

.preview_style::-webkit-scrollbar-track {
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
    border-radius: 0;
    background: rgba(0, 0, 0, 0.1);
}
</style>
