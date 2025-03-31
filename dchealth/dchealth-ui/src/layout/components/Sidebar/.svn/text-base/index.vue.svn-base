<template>
    <div :class="{'has-logo':showLogo}"
        :style="{ backgroundColor: settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }">
        <logo v-if="showLogo"
            :collapse="isCollapse" />
        <el-scrollbar :class="settings.sideTheme"
            wrap-class="scrollbar-wrapper">
            <el-menu :default-active="activeMenu"
                :collapse="isCollapse"
                :background-color="settings.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground"
                :text-color="settings.sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor"
                :unique-opened="true"
                :active-text-color="settings.theme"
                :collapse-transition="false"
                mode="vertical">
                <sidebar-item v-for="(route, index) in sidebarRouters"
                    :key="route.path  + index"
                    :item="route"
                    :async-tree="asyncTree"
                    @changeasynctree="changeAsyncTree"
                    :base-path="route.path" />
            </el-menu>
        </el-scrollbar>
    </div>
</template>

<script>
    import Cookies from 'js-cookie'
    import { mapGetters, mapState } from "vuex";
    import Logo from "./Logo";
    import SidebarItem from "./SidebarItem";
    import variables from "@/assets/styles/variables.scss";
    import { postAsync } from "@/api/research/info";

    export default {
        components: { SidebarItem, Logo },
        computed: {
            ...mapState(["settings"]),
            ...mapGetters(["sidebarRouters", "sidebar", "medicalTreeInfo"]),
            activeMenu() {
                const route = this.$route;
                const { meta, path } = route;
                // if set path, the sidebar will highlight the path you set
                if (meta.activeMenu) {
                    return meta.activeMenu;
                }
                return path;
            },
            showLogo() {
                return this.$store.state.settings.sidebarLogo;
            },
            variables() {
                return variables;
            },
            isCollapse() {
                return !this.sidebar.opened;
            },
        },
        data() {
            return {
                asyncTree: [],
                currentAsync: undefined,
                isTreeLoading: false,
            }
        },
        watch: {
            "medicalTreeInfo.thirdMenus"(newVal) {
                let medicalTreeInfo = this.medicalTreeInfo.firstMenus?this.medicalTreeInfo.firstMenus:JSON.parse(Cookies.get('medicalTreeInfo.firstMenus'))
                if (newVal.nodetype && newVal.nodetype == '3') return
                this.isTreeLoading = true;
                this.currentAsync = {
                    businessid: newVal.patientId,
                    nodetype: medicalTreeInfo.nodetype,
                    id: medicalTreeInfo.id
                };
                // this.getTreeselect();
            }
        },
        created(){
            // this.getTreeselect();
        },
        methods: {
            changeAsyncTree(event) {
                console.log('changeAsyncTree', event)
                // this.getTreeselect()
            },
            /** 查询下拉树结构 */
            getTreeselect(flag) {
                var asyncData = {
                    id: this.currentAsync ? this.currentAsync.id : 'A0',
                    businessid: this.currentAsync ? this.currentAsync.businessid : 0,
                    nodetype: this.currentAsync ? Number(this.currentAsync.nodetype) + 1 : 0,
                    patientId: this.currentAsync ? this.currentAsync.patientId : 0,
                    templateType: 0,
                }
                postAsync(asyncData).then(response => {
                    if (this.asyncTree.length) {
                        if (asyncData.nodetype == 2) {
                            this.$store.dispatch('app/setMedicalSeconds', { medicalSecondItem: response[0] })
                            let firstTreeInfo = this.asyncTree.find(f => f.id == this.currentAsync.id)

                            if (!firstTreeInfo.children || !firstTreeInfo.children.length) {
                                firstTreeInfo.children = response;
                            } else if (firstTreeInfo.children && firstTreeInfo.children.length && firstTreeInfo.children.findIndex(f => f.id == response[0].id) == -1) {

                                firstTreeInfo.children.push(...response);
                            }
                            this.asyncTree = JSON.parse(JSON.stringify(this.asyncTree))
                            this.currentAsync = response[0]
                            setTimeout(() => {
                                this.getTreeselect(true)
                            }, 1000)
                        } else if (asyncData.nodetype == 3) {
                            let nameStr = this.medicalTreeInfo.thirdMenus.nameStr
                            this.$store.dispatch('app/setMedicalThird', { medicalThirdItem: response.find(f => f.name == nameStr) })
                            let seniorInfo = this.asyncTree.find(f => f.id == this.currentAsync.pId);
                            seniorInfo.children.find(f => f.id == this.currentAsync.id).children = response;
                            this.asyncTree = JSON.parse(JSON.stringify(this.asyncTree))
                            this.isTreeLoading = false;
                        }
                    } else {
                        this.asyncTree = response;
                        this.isTreeLoading = false;
                    }

                });
            },
        }
    };
</script>