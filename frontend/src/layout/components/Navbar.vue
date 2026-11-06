<template>
    <div class="navbar">
        <div class="navbar-logo-container" id="hamburger-container">
            <img src="@/assets/logo/sxwl-logo.png" class="navbar-logo" />
            <span class="navbar-logo-title">慢病综合管理平台</span>
        </div>

        <top-nav id="topmenu-container" class="topmenu-container" v-if="topNav" />

        <div class="right-menu">
            <template v-if="device!=='mobile'">
                <!-- <search id="header-search" class="right-menu-item" /> -->

                <!-- <el-tooltip content="源码地址" effect="dark" placement="bottom">
          <ruo-yi-git id="ruoyi-git" class="right-menu-item hover-effect" />
        </el-tooltip>

        <el-tooltip content="文档地址" effect="dark" placement="bottom">
          <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />
        </el-tooltip> -->
             <!-- <el-button type="text" @click="toTangPangAi"><i class="el-icon-service"></i> 唐胖智健大模型入口</el-button> -->

                <el-button type="text" @click="toHandbook">查看帮助手册</el-button>

                <screenfull id="screenfull" class="right-menu-item hover-effect" />

                <el-tooltip content="布局大小" effect="dark" placement="bottom">
                    <size-select id="size-select" class="right-menu-item hover-effect" />
                </el-tooltip>

            </template>

            <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
                <div class="avatar-wrapper">
                    <img :src="avatar" class="user-avatar">
                    <i class="el-icon-caret-bottom" />
                </div>
                <el-dropdown-menu slot="dropdown">
                    <router-link to="/user/profile">
                        <el-dropdown-item>个人中心</el-dropdown-item>
                    </router-link>
                    <el-dropdown-item @click.native="setting = true">
                        <span>布局设置</span>
                    </el-dropdown-item>
                    <el-dropdown-item divided @click.native="logout">
                        <span>退出登录</span>
                    </el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'

export default {
    components: {
        Breadcrumb,
        TopNav,
        Hamburger,
        Screenfull,
        SizeSelect,
        Search,
        RuoYiGit,
        RuoYiDoc
    },
    computed: {
        ...mapGetters([
            'sidebar',
            'avatar',
            'device'
        ]),
        setting: {
            get() {
                return this.$store.state.settings.showSettings
            },
            set(val) {
                this.$store.dispatch('settings/changeSetting', {
                    key: 'showSettings',
                    value: val
                })
            }
        },
        topNav: {
            get() {
                return this.$store.state.settings.topNav
            }
        }
    },
    methods: {
        toggleSideBar() {
            this.$store.dispatch('app/toggleSideBar')
        },
        async logout() {
            this.$confirm('确定注销并退出系统吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$store.dispatch('LogOut').then(() => {
                    location.href = '/tdadmin/login';
                })
            }).catch(() => { });
        },
        toHandbook() {
            let url = 'https://td.dchealthclinical.com/files/使用手册.pdf'
            if (window.origin == 'http://172.100.45.33') {
                url = 'http://172.100.45.33/files/使用手册.pdf'
            }
            window.open(url)
        },

      toTangPangAi() {
        let url = 'https://10.131.101.161:3042/#/login'
        if (window.origin == 'http://172.100.45.33') {
          url = 'http://172.100.45.33/files/使用手册.pdf'
        }
        window.open(url)
      }
    }
}
</script>

<style lang="scss" scoped>
.navbar {
    height: 64px;
    overflow: hidden;
    position: relative;
    background: #2561EF;
    box-shadow: 0 2px 8px rgba(0, 21, 41, 0.15);

    .navbar-logo-container {
        float: left;
        height: 64px;
        line-height: 64px;
        padding: 0 12px;
        display: flex;
        align-items: center;
        background: #2561EF;
        min-width: 250px;
        border-right: 1px solid rgba(255, 255, 255, 0.15);
      justify-content: center;

        .navbar-logo {
            max-width: 90px;
            max-height: 30px;
            width: auto;
            height: auto;
            vertical-align: middle;
            margin-right: 8px;
        }

        .navbar-logo-title {
            color: #fff;
            font-weight: 600;
            font-size: 15px;
            white-space: nowrap;
        }
    }

    .breadcrumb-container {
        float: left;
    }

    .topmenu-container {
        position: absolute;
        left: 250px;
        right: 300px;
    }

    .errLog-container {
        display: inline-block;
        vertical-align: top;
    }

    .right-menu {
        float: right;
        height: 100%;
        line-height: 64px;
        display: flex;
        align-items: center;

        &:focus {
            outline: none;
        }

        .right-menu-item {
            display: inline-block;
            padding: 0 8px;
            height: 100%;
            font-size: 18px;
            color: rgba(255, 255, 255, 0.85);
            vertical-align: text-bottom;

            &.hover-effect {
                cursor: pointer;
                transition: background 0.3s;

                &:hover {
                    background: rgba(255, 255, 255, 0.1);
                }
            }
        }

        .el-button--text {
            color: rgba(255, 255, 255, 0.85);

            &:hover {
                color: #fff;
            }
        }

        .avatar-container {
            margin-right: 30px;

            .avatar-wrapper {
                margin-top: 12px;
                position: relative;

                .user-avatar {
                    cursor: pointer;
                    width: 40px;
                    height: 40px;
                    border-radius: 10px;
                }

                .el-icon-caret-bottom {
                    cursor: pointer;
                    position: absolute;
                    right: -20px;
                    top: 25px;
                    font-size: 12px;
                    color: rgba(255, 255, 255, 0.85);
                }
            }
        }
    }
}
</style>
