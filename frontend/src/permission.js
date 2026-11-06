import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import { isRelogin } from '@/utils/request'
import Vue from 'vue'

import Viewer from 'v-viewer'


NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/auth-redirect', '/bind', '/register']

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    Vue.use(Viewer)
    Viewer.setDefaults({
        className: 'halfBg',
    })
    to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      if (store.getters.roles.length === 0) {
        isRelogin.show = true
        // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetInfo').then((responedInfo) => {
          isRelogin.show = false
          store.dispatch('GenerateRoutes').then(accessRoutes => {
            // 根据roles权限生成可访问的路由表
            router.addRoutes(accessRoutes) // 动态添加可访问路由表
            // 登录成功后直接跳转工作台
            if (to.path === '/login' || to.path === '/' || to.path === '/dashboard') {
              next({ path: '/dashboard', replace: true })
            } else {
              // 使用干净的location对象，避免展开Route对象时带入matched等内部属性导致路由匹配失败→403
              next({ path: to.path, query: to.query, params: to.params, replace: true })
            }
          })
        }).catch(err => {
            store.dispatch('LogOut').then(() => {
              Message.error(err)
              next({ path: '/' })
            })
          })
      } else {
        next()
      }
    }
    // if( to.fullPath != '/quality/evaluateDetail') {
    //     Vue.use(Viewer)
    //     Viewer.setDefaults({
    //         className: '',
    //     })
    // } else {
        
    // }
    // next ()
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
    NProgress.done()
})