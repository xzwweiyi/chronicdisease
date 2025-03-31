<template>
    <div v-if="!item.hidden">
        <template v-if="hasOneShowingChild(item.children,item) && (!onlyOneChild.children||onlyOneChild.noShowingChildren)&&!item.alwaysShow">
            <div v-if="onlyOneChild.path == 'medical'"
                @click="addShow">
                <!-- <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path, onlyOneChild.query)"> -->
                <el-menu-item :index="resolvePath(onlyOneChild.path)"
                    class="medical-title"
                    :class="{'submenu-title-noDropdown':!isNest}">
                    <item :icon="onlyOneChild.meta.icon||(item.meta&&item.meta.icon)"
                        :title="onlyOneChild.meta.title" />
                    <i class="medicalMenusBtn"
                        :class="!flag?'el-icon-arrow-down':'el-icon-arrow-up'"></i>
                </el-menu-item>
                <!-- </app-link> -->
                <el-tree v-show="flag"
                    :data="asyncTree"
                    :props="defaultProps"
                    :filter-node-method="filterNode"
                    highlight-current
                    check-on-click-node
                    ref="tree"
                    default-expand-all>
                    <div slot-scope="{ node, data }"
                        @click="()=>handleNodeClick(data,node)">
                        <div class="custom-tree-node"
                            :class="(data.nodetype=='3'&&thirdMenus&&(thirdMenus.name==data.name)
                            &&secondMenus&&secondMenus.id==node.parent.data.id)?'highligth-current-tree':''">
                            <span class="custom-label">{{ node.label }}</span>
                                <i class="el-icon-delete custom-delete"
                                    v-if="data.nodetype>1&&(!data.children||!data.children.length)"
                                    @click.stop="()=>remove(node,data)"></i>
                        </div>
                    </div>
                </el-tree>
            </div>
            <div v-else>
                <app-link v-if="onlyOneChild.meta"
                    :to="resolvePath(onlyOneChild.path, onlyOneChild.query)">
                    <el-menu-item :index="resolvePath(onlyOneChild.path)"
                        :class="{'submenu-title-noDropdown':!isNest}">
                        <item :icon="onlyOneChild.meta.icon||(item.meta&&item.meta.icon)"
                            :title="onlyOneChild.meta.title" />
                    </el-menu-item>
                </app-link>
            </div>


        </template>

        <el-submenu v-else
            ref="subMenu"
            :index="resolvePath(item.path)"
            popper-append-to-body>
            <template slot="title">
                <item v-if="item.meta"
                    :icon="item.meta && item.meta.icon"
                    :title="item.meta.title" />
            </template>
            <sidebar-item v-for="child in item.children"
                :key="child.path"
                :is-nest="true"
                :item="child"
                :base-path="resolvePath(child.path)"
                class="nest-menu" />
        </el-submenu>
    </div>
</template>

<script>
    import Cookies from 'js-cookie'
    import path from 'path'
    import { mapGetters } from "vuex";
    import { isExternal } from '@/utils/validate'
    import Item from './Item'
    import AppLink from './Link'
    import FixiOSBug from './FixiOSBug'


    export default {
        name: 'SidebarItem',
        components: { Item, AppLink },
        mixins: [FixiOSBug],
        computed: {
            ...mapGetters(["medicalTreeInfo"]),
            firstMenus() {
                return this.medicalTreeInfo.firstMenus && JSON.parse(Cookies.get('medicalTreeInfo.firstMenus'))
            },
            secondMenus() {
                return this.medicalTreeInfo.secondMenus && JSON.parse(Cookies.get('medicalTreeInfo.secondMenus'))
            },
            thirdMenus() {
                return this.medicalTreeInfo.thirdMenus && JSON.parse(Cookies.get('medicalTreeInfo.thirdMenus'))
            }
        },
        props: {
            // route object
            item: {
                type: Object,
                required: true
            },
            isNest: {
                type: Boolean,
                default: false
            },
            basePath: {
                type: String,
                default: ''
            },
            asyncTree: {
                type: Array,
                default: () => [],
            }
        },
        data() {
            this.onlyOneChild = null
            return {
                flag: false,
                defaultProps: {
                    children: "children",
                    label: "name"
                },
            }
        },
        watch: {
            asyncTree: {
                deep: true,
                handler(newVal) {}
            }
        },
        methods: {
            remove(Node, data) {
                const parent = Node.parent;
                const children = parent.data.children || parent.data;
                const index = children.findIndex(d => d.id === data.id);
                children.splice(index, 1);
            },
            handleNodeClick(item, domNode) {
                if (item.nodetype == '3') {
                    this.$store.dispatch('app/setMedicalThird', { medicalThirdItem: item })
                    this.$store.dispatch('app/setMedicalFirst', { medicalFirstItem: domNode.parent.parent.data })
                    this.$store.dispatch('app/setMedicalSeconds', { medicalSecondItem: domNode.parent.data })
                    let obj = {}
                    if (item.name == '基础信息') {
                        obj = {
                            path: `/medical/detail/userinfo/${item.id}`,
                            query: { businessid: item.businessid, patientId: domNode.parent.data.patientId },
                            meta: { title: `基础信息(${domNode.parent.data.name})` }
                        }
                    } else {
                        obj = {
                            path: `/medical/detail/cycle/${item.id}`,
                            query: { businessid: item.businessid, patientId: domNode.parent.data.patientId },
                            meta: { title: `${item.name}(${domNode.parent.data.name})` }
                        }
                    }
                    this.$store.dispatch('tagsView/addView', obj);
                    this.$router.push(obj)
                } else if (item.nodetype == '1') {
                    this.$router.push({ path: `/medical` });
                    this.$store.dispatch('app/setMedicalFirst', { medicalFirstItem: item })
                } else {
                    return
                }
            },
            filterNode(value, data) {
                if (!value) return true;
                return data.label.indexOf(value) !== -1;
            },
            addShow() {
                // this.getTreeselect()
                this.flag = !this.flag
                if (this.flag) {
                    this.$emit('changeasynctree', false)
                }
            },
            hasOneShowingChild(children = [], parent) {
                if (!children) {
                    children = [];
                }
                const showingChildren = children.filter(item => {
                    if (item.hidden) {
                        return false
                    } else {
                        // Temp set(will be used if only has one showing child)
                        this.onlyOneChild = item
                        return true
                    }
                })

                // When there is only one child router, the child router is displayed by default
                if (showingChildren.length === 1) {
                    return true
                }

                // Show parent if there are no child router to display
                if (showingChildren.length === 0) {
                    this.onlyOneChild = { ...parent, path: '', noShowingChildren: true }
                    return true
                }

                return false
            },
            resolvePath(routePath, routeQuery) {
                if (isExternal(routePath)) {
                    return routePath
                }
                if (isExternal(this.basePath)) {
                    return this.basePath
                }
                if (routeQuery) {
                    let query = JSON.parse(routeQuery);
                    return { path: path.resolve(this.basePath, routePath), query: query }
                }
                return path.resolve(this.basePath, routePath)
            }
        }
    }
</script>
<style lang="scss"
    scoped>
    ::v-deep .el-tree {
        background: #1f2d3d;
        color: rgb(191, 203, 217);

        .el-tree-node__content {
            height: 50px;
        }

        .el-tree-node:focus>.el-tree-node__content {
            color: rgb(64, 158, 255);
            background-color: transparent;
        }

        .el-tree-node:hover>.el-tree-node__content {
            background-color: #001528;
        }

        &.el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content,
        .highligth-current-tree {
            color: rgb(64, 158, 255);
            background-color: transparent;
        }
    }

    .custom-tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 14px;
        padding-right: 8px;

        .custom-delete {
            padding-left: 5px;
        }

        .custom-label {
            flex: 1;
            white-space: nowrap;
            text-overflow: ellipsis;
            word-break: keep-all;
            overflow: hidden;
        }
    }

    .medical-title {
        .medicalMenusBtn {
            float: right;
            line-height: 50px;
        }

    }
</style>