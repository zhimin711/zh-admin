import Vue from 'vue'
import Router from 'vue-router'
import routes, { assembleMenus } from './routers'
import store from '@/store'
import iView from 'iview'
import { setToken, getToken, setTitle } from '@/libs/util'
import config from '@/config'
const { homeName } = config

Vue.use(Router)

const createRouter = () => new Router({
  mode: 'history', // require service support
  // scrollBehavior: () => ({ y: 0 }),
  routes
})

const router = createRouter()

export function resetRouter () {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

const LOGIN_PAGE_NAME = 'login'

router.beforeEach((to, from, next) => {
  iView.LoadingBar.start()
  const token = getToken()
  if (!token && to.name !== LOGIN_PAGE_NAME) {
    // 未登录且要跳转的页面不是登录页
    next({
      name: LOGIN_PAGE_NAME // 跳转到登录页
    })
  } else if (!token && to.name === LOGIN_PAGE_NAME) {
    // 未登陆且要跳转的页面是登录页
    next() // 跳转
  } else if (token && to.name === LOGIN_PAGE_NAME) {
    // 已登录且要跳转的页面是登录页
    next({
      name: homeName // 跳转到homeName页
    })
  } else {
    if (store.state.user.hasGetInfo) {
      next()
      // turnTo(to, store.state.user.access, next)
    } else {
      // get user info
      store.dispatch('getUserInfo').then(async (user) => {
        // 拉取用户信息，通过用户权限和跳转的页面的name来判断是否有权限访问;access必须是一个数组，如：['super_admin'] ['super_admin', 'admin']

        const menus = await store.dispatch('getUserMenus', user.role.id)
        // dynamically add accessible routes
        const accessRoutes = assembleMenus(menus)
        router.addRoutes(accessRoutes)
        await store.dispatch('setMenus', accessRoutes)

        // await store.dispatch('getUserPermissions', user.role.id)

        // turnTo(to, user.access, next)
        router.push(to.path)
      }).catch((err) => {
        console.log(err)
        iView.Modal.warning({
          title: '登录过期',
          content: '登录已失效,请重新登录',
          okText: '重新登录',
          onOk: () => {
            setToken('')
            next({
              name: 'login'
            })
          }
        })
      })
    }
  }
})

router.afterEach(to => {
  setTitle(to, router.app)
  iView.LoadingBar.finish()
  window.scrollTo(0, 0)
})

export default router
