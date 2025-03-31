import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: 路由配置项
 *
 * hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                  // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                  // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                  // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * query: '{"id": 1, "name": "ry"}' // 访问路由的默认传递参数
 * roles: ['admin', 'common']       // 访问路由的角色权限
 * permissions: ['a:a:a', 'b:b:b']  // 访问路由的菜单权限
 * meta : {
    noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'                // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false               // 如果设置为false，则不会在breadcrumb面包屑中显示
    activeMenu: '/system/user'      // 当路由设置了该属性，则会高亮相对应的侧边栏。
  }
 */

// 公共路由
export const constantRoutes = [{
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [{
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
    }]
},
{
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
},
{
    path: '/register',
    component: () => import('@/views/register'),
    hidden: true
},
{
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
},
{
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
},
{
    path: '/403',
    component: () => import('@/views/error/403'),
    hidden: true
},
// {
//     path: '',
//     component: Layout,
//     redirect: 'index',
//     children: [{
//         path: 'index',
//         component: () => import('@/views/index'),
//         name: 'Index',
//         meta: {
//             title: '首页',
//             icon: 'dashboard',
//             affix: true
//         }
//     }]
// },
{
    path: '',
    component: Layout,
    redirect: 'queue',
    hidden: true,
    children: [{
        path: 'queue',
        component: () => import('@/views/queueSettings/queue/index'),
        name: 'queueIndex',
        meta: {
            title: '队列管理',
            affix: true
        }
    }]
},
{
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [{
        path: 'profile',
        component: () => import('@/views/system/user/profile/index'),
        name: 'Profile',
        meta: {
            title: '个人中心',
            icon: 'user'
        }
    }]
}
]

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = [
    {
        path: '/queueSettings/introductions/index',
        component: Layout,
        hidden: true,
        permissions: ['research:info:query'],
        children: [{
            path: '/queueSettings/introductions/index',
            component: () => import('@/views/queueSettings/introductions/index'),
            name: 'introductions',
            meta: {
                title: '队列简介',
                activeMenu: '/queue',
            }
        }, {
            path: '/queueSettings/patient/import',
            component: () => import('@/views/queueSettings/patient/exclusionCondition'),
            name: 'patient_import',
            meta: {
                title: '纳排入组',
                activeMenu: '/queue',
            }
        }]
    },
    {
        path: '/queueSettings/patient',
        component: Layout,
        hidden: true,
        permissions: ['research:patient:query'],
        children: [{
            path: '/queueSettings/patient/list/:researchId',
            component: () => import('@/views/queueSettings/patient/list'),
            name: 'queue_patintList',
            meta: {
                title: '患者列表',
                activeMenu: '/queue'
            }
        },{
            path: '/queueSettings/allpatient/details',
            component: () => import('@/views/patient/administer/detail'),
            name: 'patintDetail',
            meta: {
                title: '查看详情',
                activeMenu: '/queue'
            }
        }]
    },
    {
        path: '/queueSettings/role/',
        component: Layout,
        hidden: true,
        permissions: ['researh:role:update'],
        children: [{
            path: 'authUser/:roleId(\\d+)',
            component: () => import('@/views/queueSettings/role/authUser'),
            name: 'AuthUser-fllow',
            meta: {
                title: '分配用户',
                activeMenu: '/queueSettings/role/authUser'
            }
        }]
    },
    {
        path: '/patient/list',
        component: Layout,
        hidden: true,
        permissions: ['system:info:list'],
        children: [{
            path: '/patient/list/:followName',
            component: () => import('@/views/patient/administer/index'),
            name: 'patintList',
            meta: {
                title: params => {
                    return params.followName
                },
                activeMenu: '/patients/patient/framework'
            }
        }]
    },
    {
        path: '/patient/detail',
        component: Layout,
        hidden: true,
        permissions: ['system:info:list'],
        children: [{
            path: '/patient/detail',
            component: () => import('@/views/patient/administer/detail'),
            name: 'patint_detail',
            meta: {
                title: '患者详情',
                activeMenu: '/patients/list'
            }
        }]
    },
    {
        path: '/queueSettings/cycle/crfSettings',
        component: Layout,
        hidden: true,
        permissions: ['base:medical:list'],
        children: [{
            path: '/queueSettings/cycle/crfSettings',
            component: () => import('@/views/queueSettings/cycle/crfSettings'),
            name: 'crfSettings',
            meta: {
                title: '设置随访CRF',
                activeMenu: '/queue',
                noCache: true
            }
        }, {
            path: '/queueSettings/buildCRF/:medicalFormId/:followId/:medicalName/:formSource',
            component: () => import('@/views/tool/buildCRF/index'),
            name: 'queueSettings-template',
            meta: {
                title: params => {
                    return params.medicalName
                },
                activeMenu: '/queue'
            }
        }]
    },
    {
        path: '/queueSettings/cycle/crf',
        component: Layout,
        hidden: true,
        permissions: ['researh:role:list'],
        children: [{
            path: '/queueSettings/cycle/crf',
            component: () => import('@/views/queueSettings/cycle/crf'),
            name: 'crf',
            meta: {
                title: '随访CRF',
                activeMenu: '/queueSettings/cycle/crf'
            }
        }]
    },
    {
        path: '/researh/role/roleList',
        component: Layout,
        hidden: true,
        permissions: ['researh:role:list'],
        children: [{
            path: '/researh/role/roleList',
            component: () => import('@/views/researh/role/roleList'),
            name: 'roleList',
            meta: {
                title: '角色管理',
                activeMenu: '/researh/role/roleList'
            }
        }]
    },
    {
        path: '/researh/role-auth',
        component: Layout,
        hidden: true,
        permissions: ['system:role:edit'],
        children: [{
            path: 'user/:roleId(\\d+)',
            component: () => import('@/views/researh/role/authUser'),
            name: 'AuthUser',
            meta: {
                title: '分配用户',
                activeMenu: '/researh/role/authUser'
            }
        }]
    },

    {
        path: '/system/user-auth',
        component: Layout,
        hidden: true,
        permissions: ['system:user:edit'],
        children: [{
            path: 'role/:userId(\\d+)',
            component: () => import('@/views/system/user/authRole'),
            name: 'AuthRole',
            meta: {
                title: '分配角色',
                activeMenu: '/system/user'
            }
        }]
    },
    {
        path: '/system/role-auth',
        component: Layout,
        hidden: true,
        permissions: ['system:role:edit'],
        children: [{
            path: 'user/:roleId(\\d+)',
            component: () => import('@/views/system/role/authUser'),
            name: 'AuthUser-role',
            meta: {
                title: '分配用户',
                activeMenu: '/system/role'
            }
        }]
    },
    {
        path: '/system/dict-data',
        component: Layout,
        hidden: true,
        permissions: ['system:dict:list'],
        children: [{
            path: 'index/:dictId(\\d+)',
            component: () => import('@/views/system/dict/data'),
            name: 'Data',
            meta: {
                title: '字典数据',
                activeMenu: '/system/dict'
            }
        }]
    },
    {
        path: '/monitor/job-log',
        component: Layout,
        hidden: true,
        permissions: ['monitor:job:list'],
        children: [{
            path: 'index',
            component: () => import('@/views/monitor/job/log'),
            name: 'JobLog',
            meta: {
                title: '调度日志',
                activeMenu: '/monitor/job'
            }
        }]
    },
    {
        path: '/tool/gen-edit',
        component: Layout,
        hidden: true,
        permissions: ['tool:gen:edit'],
        children: [{
            path: 'index/:tableId(\\d+)',
            component: () => import('@/views/tool/gen/editTable'),
            name: 'GenEdit',
            meta: {
                title: '修改生成配置',
                activeMenu: '/tool/gen'
            }
        }]
    },
    {
        path: '/medical/detail',
        component: Layout,
        hidden: true,
        permissions: ['medical:user:detail'],
        children: [{
            path: 'userinfo/:id',
            component: () => import('@/views/medical/components/userinfo'),
            name: 'userinfo',
            meta: {
                title: '患者记录',
                activeMenu: '/medical'
            }
        }]
    },
    {
        path: '/medical/detail',
        component: Layout,
        hidden: true,
        permissions: ['medical:user:cycle'],
        children: [{
            path: 'cycle/:id',
            component: () => import('@/views/medical/components/cycle'),
            name: 'medical-detail',
            meta: {
                title: '患者记录',
                activeMenu: '/medical'
            }
        }]
    },
    {
        path: '/CRF',
        component: Layout,
        hidden: true,
        permissions: ['template:category:list'],
        children: [{
            path: '/public/CRF/categoryTemplate/:commonFlag/:id/:templateCategoryName',
            component: () => import('@/views/crf/template/index'),
            name: 'publicCRF-form',
            meta: {
                title: params => {
                    return decodeURIComponent(params.templateCategoryName)
                },
                activeMenu: '/public/categoryCRF'
            }
        },{
            path: '/privately/CRF/categoryTemplate/:commonFlag/:id/:templateCategoryName',
            component: () => import('@/views/crf/template/index'),
            name: 'privatelyCRF-form',
            meta: {
                title: params => {
                    return decodeURIComponent(params.templateCategoryName)
                },
                activeMenu: '/privately/categoryCRF'
            }
        }, {
            path: '/public/CRF/categoryTemplate/detail/:commonFlag/:templateId/:templateName',
            component: () => import('@/views/crf/templateEdit/index'),
            name: 'publicCRFCategory_detail',
            meta: {
                title: params => {
                    return params.templateName
                },
                activeMenu: '/public/categoryCRF'
            }
        }, {
            path: '/privately/CRF/categoryTemplate/detail/:commonFlag/:templateId/:templateName',
            component: () => import('@/views/crf/templateEdit/index'),
            name: 'privatelyCRFCategory_detail',
            meta: {
                title: params => {
                    return params.templateName
                },
                activeMenu: '/privately/categoryCRF'
            }
        }, {
            path: '/public/CRF/categoryTemplate/buildCRF/:templateFormId/:templateName/:formSource',
            component: () => import('@/views/tool/buildCRF/index'),
            name: 'publicCRF-template',
            meta: {
                title: params => {
                    return '添加' + decodeURIComponent(params.templateName) + '条目'
                },
                activeMenu: '/public/categoryCRF'
            }
        }, {
            path: '/privately/CRF/categoryTemplate/buildCRF/:templateFormId/:templateName/:formSource',
            component: () => import('@/views/tool/buildCRF/index'),
            name: 'privatelyCRF-template',
            meta: {
                title: params => {
                    return '添加' + decodeURIComponent(params.templateName) + '条目'
                },
                activeMenu: '/privately/categoryCRF'
            }
        }]
    },
    {
        // 标准录入项
        path: '/entry',
        component: Layout,
        hidden: true,
        permissions: ['base:library:list'],
        children: [{
            path: '/entryItem/:libraryId',
            component: () => import('@/views/tool/questionbank/index'),
            name: 'entryItem_libraryId',
            meta: {
                title: '二级列表',
                activeMenu: '/entryItem',
            }
        }, {
            path: '/entry/tool/build',
            component: () => import('@/views/tool/build'),
            name: 'build_entry',
            meta: {
                title: '表单构建',
                activeMenu: '/entryItem'
            }
        }]
    },
    {
        // 标准拍照项
        path: '/photograph',
        component: Layout,
        hidden: true,
        permissions: ['base:library:list'],
        children: [{
            path: '/photography/:libraryId',
            component: () => import('@/views/tool/photography/index'),
            name: 'photography_library',
            meta: {
                title: '二级列表',
                activeMenu: '/photography',
            }
        }, {
            path: '/photograph/tool/build',
            component: () => import('@/views/tool/build'),
            name: 'build_photograph',
            meta: {
                title: '表单构建',
                activeMenu: '/photography'
            }
        }]
    },
    {
        // 标准量表
        path: '',
        component: Layout,
        hidden: true,
        permissions: ['base:library:list'],
        children: [{
            path: '/standardgauge/:libraryId',
            component: () => import('@/views/tool/standardgauge/list'),
            name: 'standardgauge_libraryId',
            meta: {
                title: '二级列表',
                activeMenu: '/standardgauge',
            }
        }, {
            path: '/standardgauge/tool/build',
            component: () => import('@/views/tool/build'),
            name: 'build_standardgauge',
            meta: {
                title: '表单构建',
                activeMenu: '/standardgauge'
            }
        }]
    },
    {
        path: '/public',
        component: Layout,
        hidden: true,
        permissions: ['system:category:list'],
        children: [{
            path: '/public/patient/category/list/:commonFlag/:parentId/:name',
            component: () => import('@/views/patient/category/list'),
            name: 'public-categoryList',
            meta: {
                // title: '患教列表',
                title: params => {
                    return decodeURIComponent(params.name) + '-患教列表'
                },
                activeMenu: '/public/patientlibrary'
            }
        }, {
            path: '/patient/category/createForm/:commonFlag/:articleId/:formSource',
            component: () => import('@/views/tool/buildliang/index'),
            name: 'public-createForm',
            meta: {
                title: '配置问卷',
                activeMenu: '/public/patientlibrary'
            }
        }, {
            path: '/patient/category/statistics/:commonFlag',
            component: () => import('@/views/patient/category/statistics'),
            name: 'public-statisticsList',
            meta: {
                title: '问卷统计',
                activeMenu: '/public/patientlibrary'
            }
        }]
    },
    {
        path: '/privately',
        component: Layout,
        hidden: true,
        permissions: ['system:category:list'],
        children: [{
            path: '/privately/patient/category/list/:commonFlag/:parentId/:name',
            component: () => import('@/views/patient/category/list'),
            name: 'privately-categoryList',
            meta: {
                // title: '患教列表',
                title: params => {
                    return decodeURIComponent(params.name) + '-患教列表'
                },
                activeMenu: '/privately/patientlibrary'
            }
        }, {
            path: '/privately/patient/category/createForm/:commonFlag/:articleId/:formSource',
            component: () => import('@/views/tool/buildliang/index'),
            name: 'privately-createForm',
            meta: {
                title: '配置问卷',
                activeMenu: '/privately/patientlibrary'
            }
        }, {
            path: '/privately/patient/category/statistics/:commonFlag',
            component: () => import('@/views/patient/category/statistics'),
            name: 'privately-statisticsList',
            meta: {
                title: '问卷统计',
                activeMenu: '/privately/patientlibrary'
            }
        }]
    },
    {
        path: '/patientquestionnaire',
        component: Layout,
        hidden: true,
        permissions: ['system:article:record'],
        children: [{
            path: '/patient/questionnaire/details',
            component: () => import('@/views/patient/questionnaire/detail'),
            name: 'questionDetail',
            meta: {
                title: '问卷详情',
                activeMenu: '/patient/questionnaire'
            }
        }, ]
    },
    {
        path: '/workstation/user/opinionDetail',
        component: Layout,
        hidden: true,
        permissions: ['follow:details:view'],
        children: [{
            path: '/workstation/user/opinionDetail',
            component: () => import('@/views/workstation/user/opinionDetail'),
            name: 'opinion',
            meta: {
                title: '随访工作站-查看随访',
                activeMenu: '/workstation/user'
            }
        },{
            path: '/workstation/patients/opinionDetail',
            component: () => import('@/views/workstation/user/healthRecordDetails'),
            name: 'health_details',
            meta: {
                title: '随访工作站-查看随访',
                activeMenu: '/workstation/user'
            }
        },]
    },
    {
        path: '/workstation/edc/userDetail',
        component: Layout,
        hidden: true,
        permissions: ['edc:details:check'],
        children: [{
            path: '/workstation/edc/userDetail',
            component: () => import('@/views/workstation/edc/userDetail'),
            name: 'userDetails',
            meta: {
                title: 'EDC工作站-查看随访',
                activeMenu: '/workstation/edc'
            }
        }]
    },
    {
        path: '/evaluate/qualityevaluation/details',
        component: Layout,
        permissions: ['evaluate:quality:details'],
        hidden: true,
        children: [{
            path: '/evaluate/qualityevaluation/details',
            component: () => import('@/views/evaluate/qualityevaluation/details'),
            name: 'qualityevaluationDetail',
            meta: {
                title: '质效评价详情',
                activeMenu: '/evaluate/qualityevaluation'
            }
        }]
    },
    {
        path: '/evaluate/patientevaluation/details',
        component: Layout,
        permissions: ['evaluate:patient:details'],
        hidden: true,
        children: [{
            path: '/evaluate/patientevaluation/details',
            component: () => import('@/views/evaluate/patientevaluation/details'),
            name: 'patientevaluationDetail',
            meta: {
                title: '患者评价详情',
                activeMenu: '/evaluate/patientevaluation'
            }
        }]
    },
    {
        path: '/outbound/details',
        component: Layout,
        permissions: ['medical:outbound:outboundTree'],
        hidden: true,
        children: [{
            path: '/outbound/details',
            component: () => import('@/views/outbound/details'),
            name: 'outbound_detail',
            meta: {
                title: '患者评价详情',
                activeMenu: '/outbound'
            }
        }]
    },
]

// 防止连续点击多次路由报错
let routerPush = Router.prototype.push;
Router.prototype.push = function push(location) {
    // console.log('location',location)
    return routerPush.call(this, location).catch(err => err)
}

export default new Router({
    mode: 'history', // 去掉url中的#
    base: '/tdadmin',
    scrollBehavior: () => ({
        y: 0
    }),
    routes: constantRoutes
})