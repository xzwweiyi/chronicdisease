<template>
    <el-menu :default-active="activeMenu" mode="horizontal" @select="handleSelect" :style="menuStyle">
        <template v-for="(item, index) in topMenus">
            <el-menu-item :style="{'--theme': theme}" :index="item.path" :key="index" v-if="index < visibleNumber">
                <svg-icon :icon-class="item.meta.icon" />
                <span class="topmenu-title">{{ item.meta.title }}</span>
            </el-menu-item>
        </template>

        <!-- 顶部菜单超出数量折叠 -->
        <el-submenu :style="{'--theme': theme}" index="more" v-if="topMenus.length > visibleNumber">
            <template slot="title">更多</template>
            <template v-for="(item, index) in topMenus">
                <el-menu-item :index="item.path" :key="index" v-if="index >= visibleNumber">
                    <svg-icon :icon-class="item.meta.icon" />
                    <span class="topmenu-title">{{ item.meta.title }}</span>
                </el-menu-item>
            </template>
        </el-submenu>
    </el-menu>
</template>

<script>
    import {
        constantRoutes
    } from "@/router";

    export default {
        data() {
            return {
                // 顶部栏展示的菜单数（自适应计算，超出部分折叠进“更多菜单”）
                visibleNumber: 5,
                // 是否为首次加载
                isFrist: false,
                // 当前激活菜单的 index
                currentIndex: undefined,
                // 顶部菜单水平间距（px），根据内容多少自适应计算
                menuGap: 8
            };
        },
        computed: {
            theme() {
                return this.$store.state.settings.theme;
            },
            // 顶部菜单根节点样式（通过 CSS 变量控制菜单间距）
            menuStyle() {
                return {
                    '--topmenu-gap': this.menuGap + 'px'
                };
            },
            // 顶部显示菜单
            topMenus() {
                let topMenus = [];
                this.routers.map((menu) => {
                    if (menu.hidden !== true) {
                        // 首页菜单直接展示，点击后导航至其第一个子菜单（工作台/dashboard）
                        topMenus.push(menu);
                    }
                });
                return topMenus;
            },
            // 所有的路由信息
            routers() {
                return this.$store.state.permission.topbarRouters;
            },
            // 设置子路由
            childrenMenus() {
                var childrenMenus = [];
                this.routers.map((router) => {
                    for (var item in router.children) {
                        if (router.children[item].parentPath === undefined) {
                            if (router.path === "/") {
                                router.children[item].path = "/" + router.children[item].path;
                            } else {
                                if (!this.ishttp(router.children[item].path)) {
                                    router.children[item].path = router.path + "/" + router.children[item].path;
                                }
                            }
                            router.children[item].parentPath = router.path;
                        }
                        childrenMenus.push(router.children[item]);
                    }
                });
                return constantRoutes.concat(childrenMenus);
            },
            // 默认激活的菜单
            activeMenu() {
                const path = this.$route.path;
                let activePath = path;
                if (path.lastIndexOf("/") > 0 && path !== "/") {
                    const tmpPath = path.substring(1, path.length);
                    const firstSlash = tmpPath.indexOf("/");
                    if (firstSlash > 0) {
                        activePath = "/" + tmpPath.substring(0, firstSlash);
                    }
                    this.$store.dispatch('app/toggleSideBarHide', false);
                } else if (path === "/dashboard") {
                    // Dashboard 是首页的子路由，高亮"首页"顶部菜单
                    activePath = "/";
                    this.$store.dispatch('app/toggleSideBarHide', false);
                } else if (path === "/" || path === "") {
                    // On root path, activate the first top menu
                    if (this.topMenus.length > 0) {
                        activePath = this.topMenus[0].path || "path";
                        this.$store.dispatch('app/toggleSideBarHide', false);
                    }
                } else if (!this.$route.children || this.$route.children.length === 0) {
                    activePath = path;
                    this.$store.dispatch('app/toggleSideBarHide', true);
                }
                this.activeRoutes(activePath);
                return activePath;
            },
        },
        watch: {
            // 路由/菜单加载完成后重新计算间距
            topMenus() {
                this.setVisibleNumber();
            }
        },
        beforeMount() {
            window.addEventListener('resize', this.setVisibleNumber)
        },
        beforeDestroy() {
            window.removeEventListener('resize', this.setVisibleNumber)
        },
        mounted() {
            this.setVisibleNumber();
        },
        methods: {
            // 根据菜单数量与容器宽度自适应：放得下就均匀分布间距，放不下则折叠进“更多菜单”
            setVisibleNumber() {
                const total = this.topMenus.length || 0;
                if (!total) {
                    this.visibleNumber = 0;
                    return;
                }
                // 先全部展示，以便测量每个菜单的真实宽度
                this.visibleNumber = total;
                this.$nextTick(() => {
                    const menuEl = this.$el;
                    if (!menuEl) return;
                    const containerWidth = menuEl.getBoundingClientRect().width;
                    if (!containerWidth) return;

                    // 只取顶层菜单项（不含“更多菜单”弹层里的子项）
                    const items = Array.prototype.filter.call(
                        menuEl.children,
                        el => el.classList && el.classList.contains('el-menu-item')
                    );
                    if (!items.length) return;

                    const minGap = 4;
                    const maxGap = 16;
                    const widths = items.map(el => el.getBoundingClientRect().width);
                    const sumWidth = widths.reduce((a, b) => a + b, 0);
                    const count = widths.length;

                    // 最小间距下能全部放下：按剩余空间均匀分布间距
                    if (sumWidth + minGap * 2 * count <= containerWidth) {
                        let gap = (containerWidth - sumWidth) / (2 * count);
                        gap = Math.max(minGap, Math.min(maxGap, gap));
                        this.menuGap = Math.floor(gap * 10) / 10;
                        this.visibleNumber = total;
                        return;
                    }

                    // 放不下：使用最小间距，超出部分折叠进“更多菜单”
                    this.menuGap = minGap;
                    const moreWidth = 100 + minGap * 2; // 预留“更多菜单”按钮宽度
                    let used = 0;
                    let visible = 0;
                    for (let i = 0; i < widths.length; i++) {
                        const w = widths[i] + minGap * 2;
                        const reserve = (i < widths.length - 1) ? moreWidth : 0;
                        if (used + w + reserve > containerWidth) break;
                        used += w;
                        visible++;
                    }
                    if (visible < 1) visible = 1;
                    this.visibleNumber = Math.min(visible, total);
                });
            },
            // 菜单选择事件
            handleSelect(key, keyPath) {
                this.currentIndex = key;
                const route = this.routers.find(item => item.path === key);
                if (this.ishttp(key)) {
                    // http(s):// 路径新窗口打开
                    window.open(key, "_blank");
                } else if (!route || !route.children) {
                    // 没有子路由路径内部打开（使用绝对路径）
                    const absKey = (key && key.startsWith('/')) ? key : '/' + (key || '');
                    this.$router.push({ path: absKey || '/dashboard' });
                    this.$store.dispatch('app/toggleSideBarHide', true);
                } else {
                    // 显示左侧联动菜单
                    this.activeRoutes(key);
                    this.$store.dispatch('app/toggleSideBarHide', false);
                    // 跳转：确保使用绝对路径
                    if (route.children && route.children.length > 0) {
                        const firstChild = route.children.find(c => c.component) || route.children[0];
                        const childPath = firstChild.path || firstChild;
                        if (childPath && !this.ishttp(String(childPath))) {
                            const absPath = String(childPath).startsWith('/') ? String(childPath) : '/' + String(childPath);
                            this.$router.push({ path: absPath });
                        }
                    } else if (key && !this.ishttp(key)) {
                        const absKey = key.startsWith('/') ? key : '/' + key;
                        this.$router.push({ path: absKey });
                    }
                }
            },
            // 当前激活的路由
            activeRoutes(key) {
                var routes = [];
                if (this.childrenMenus && this.childrenMenus.length > 0) {
                    this.childrenMenus.map((item) => {
                        if (key == item.parentPath || (key == "index" && "" == item.path)) {
                            routes.push(item);
                        }
                    });
                }
                if (routes.length > 0) {
                    this.$store.commit("SET_SIDEBAR_ROUTERS", routes);
                }
            },
            ishttp(url) {
                return url.indexOf('http://') !== -1 || url.indexOf('https://') !== -1
            }
        },
    };
</script>

<style lang="scss">
    .topmenu-container.el-menu--horizontal {
        background: transparent !important;
        border-bottom: none !important;
        display: flex;
        align-items: center;
        flex-wrap: nowrap;
        height: 64px;
        overflow: hidden;
    }

    .topmenu-container.el-menu--horizontal>.el-menu-item {
        float: none !important;
        flex: 0 0 auto;
        height: 64px !important;
        line-height: 64px !important;
        color: rgba(255, 255, 255, 0.75) !important;
        padding: 0 5px !important;
        margin: 0 var(--topmenu-gap, 8px) !important;
        font-size: 16px;
        white-space: nowrap;
        font-family: '微软雅黑', 'Microsoft YaHei', sans-serif;
        border-bottom: 2px solid transparent !important;

        &:hover {
            background-color: rgba(255, 255, 255, 0.1) !important;
            color: #fff !important;
        }
    }

    .topmenu-container.el-menu--horizontal .topmenu-title {
        margin-left: 6px;
        white-space: nowrap;
    }

    .topmenu-container.el-menu--horizontal>.el-menu-item.is-active {
        border-bottom: 2px solid #FFFFFF !important;
        color: #FFFFFF !important;
        background-color: rgba(255, 255, 255, 0.15) !important;
    }

    .el-menu--horizontal>.el-submenu.is-active .el-submenu__title {
        border-bottom: 2px solid #FFFFFF !important;
        color: #FFFFFF !important;
    }

    /* submenu item */
    .topmenu-container.el-menu--horizontal>.el-submenu .el-submenu__title {
        float: none !important;
        flex: 0 0 auto;
        height: 64px !important;
        line-height: 64px !important;
        color: rgba(255, 255, 255, 0.75) !important;
        padding: 0 5px !important;
        margin: 0 var(--topmenu-gap, 8px) !important;
        font-size: 16px;
        white-space: nowrap;
        font-family: '微软雅黑', 'Microsoft YaHei', sans-serif;
        border-bottom: 2px solid transparent !important;

        &:hover {
            background-color: rgba(255, 255, 255, 0.1) !important;
            color: #fff !important;
        }
    }

    /* submenu popover items */
    .el-menu--horizontal .el-menu--popup-bottom-start {
        .el-menu-item {
            color: #333 !important;

            &:hover {
                background-color: rgba(37, 97, 239, 0.08) !important;
                color: #2561EF !important;
            }

            &.is-active {
                color: #2561EF !important;
            }
        }
    }
</style>