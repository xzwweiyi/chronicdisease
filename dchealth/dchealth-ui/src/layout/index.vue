<template>
    <div :class="[classObj ,'fontSize_'+fontSize]" class="app-wrapper" :style="{'--current-color': theme}">
        <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside" />
        <sidebar v-if="!sidebar.hide" class="sidebar-container" />
        <div :class="{hasTagsView:needTagsView,sidebarHide:sidebar.hide}" class="main-container">
            <div :class="{'fixed-header':fixedHeader}">
                <navbar />
                <tags-view v-if="needTagsView" />
            </div>
            <app-main />
            <right-panel>
                <settings />
            </right-panel>
        </div>
        <el-popover placement="left" width="660" trigger="click" class="gloable_QRcode" popper-class="qRCode_global_popover">
            <el-row class="qrCde_tips_content">
                <el-col>
                    <p class="qrCode_tips"> <strong>唐都随访患者管理</strong> </p>
                    <el-image :src="wechartQRcodeUrl" class="customer_img"></el-image>
                    <p class="qrCode_tips">请使用微信扫码</p>
                </el-col>
                <el-col>
                    <p class="qrCode_tips"> <strong>慢病管理患者端</strong></p>
                    <el-image :src="patientQRcodeUrl" class="customer_img"></el-image>
                    <p class="qrCode_tips">请使用浏览器扫码</p>
                    <p class="qrCode_tips">（安卓用户）</p>
                </el-col>
                <el-col>
                    <p class="qrCode_tips"> <strong>慢病管理医生端</strong></p>
                    <el-image :src="doctorQRcodeUrl" class="customer_img"></el-image>
                    <p class="qrCode_tips">请使用浏览器扫码</p>
                    <p class="qrCode_tips">（安卓用户）</p>
                </el-col>
            </el-row>
            <el-button type="warning" slot="reference" class="gloable_button">移<br>动<br>端<br>下<br>载</el-button>
        </el-popover>
    </div>
</template>

<script>
import RightPanel from '@/components/RightPanel'
import {
    AppMain,
    Navbar,
    Settings,
    Sidebar,
    TagsView
} from './components'
import ResizeMixin from './mixin/ResizeHandler'
import {
    mapState
} from 'vuex'
import variables from '@/assets/styles/variables.scss'

export default {
    name: 'Layout',
    components: {
        AppMain,
        Navbar,
        RightPanel,
        Settings,
        Sidebar,
        TagsView
    },
    mixins: [ResizeMixin],
    computed: {
        ...mapState({
            theme: state => state.settings.theme,
            sideTheme: state => state.settings.sideTheme,
            sidebar: state => state.app.sidebar,
            device: state => state.app.device,
            needTagsView: state => state.settings.tagsView,
            fixedHeader: state => state.settings.fixedHeader,
            fontSize: state => state.app.size,
        }),
        classObj() {
            return {
                hideSidebar: !this.sidebar.opened,
                openSidebar: this.sidebar.opened,
                withoutAnimation: this.sidebar.withoutAnimation,
                mobile: this.device === 'mobile'
            }
        },
        variables() {
            return variables;
        }
    },
    data() {
        return {
            doctorQRcodeUrl: require('@/assets/images/doctorQRCode.png'),
            wechartQRcodeUrl: require('@/assets/images/wechartQRcode.jpg'),
            patientQRcodeUrl: require('@/assets/images/patientAndoridQRCode.png'),
        }
    },
    methods: {
        handleClickOutside() {
            this.$store.dispatch('app/closeSideBar', {
                withoutAnimation: false
            })
        }
    }
}
</script>

<style lang="scss" scoped>
    @import "~@/assets/styles/mixin.scss";
    @import "~@/assets/styles/variables.scss";

    .app-wrapper {
        @include clearfix;
        position: relative;
        height: 100%;
        width: 100%;

        &.mobile.openSidebar {
            position: fixed;
            top: 0;
        }
    }

    .drawer-bg {
        background: #000;
        opacity: 0.3;
        width: 100%;
        top: 0;
        height: 100%;
        position: absolute;
        z-index: 999;
    }

    .fixed-header {
        position: fixed;
        top: 0;
        right: 0;
        z-index: 9;
        width: calc(100% - #{$base-sidebar-width});
        transition: width 0.28s;
    }

    .hideSidebar .fixed-header {
        width: calc(100% - 54px);
    }

    .mobile .fixed-header {
        width: 100%;
    }
    .gloable_QRcode{
        position: fixed;
        bottom: 70px;
        right: 20px;
        z-index: 9999999999999;
        .gloable_button{
            padding:10px 8px;
            line-height:1.2;
        }
       
    }
    .qRCode_global_popover{
        .qrCde_tips_content{
            display: flex;
            justify-content: center;
            .el-col{
                display: flex;
                flex-direction: column;
                align-items: center;

            }
        }
        .qrCode_tips{
            margin:5px 0;
            padding:0 5px;
        }
        .customer_img{
            width:120px;
            height:120px;
        }
    }
</style>