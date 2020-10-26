import { constantRouterMap } from '@/router/routes'
import Main from '@/views/main.vue'
import Empty from '@/views/main-empty.vue'

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers)
    }
  },
  actions: {
    GenerateRoutes({ commit }, asyncRouter) {
      commit('SET_ROUTERS', asyncRouter)
    }
  }
}

export const filterAsyncRouter = (routers) => { // 遍历后台传来的路由字符串，转换为组件对象
  return routers.filter(router => {
    if (router.component) {
      if (router.component === 'Main') { // Layout组件特殊处理
        router.component = Main
        router.redirect=="noredirect"
      } else if(router.component === 'Empty'){
        router.component = Empty
        router.redirect=="noredirect"
      } else {
        const component = router.component
        router.component = loadView(component)
      }
    }

    router.meta = {
      title:router.title,
      isTab:router.isTab
    }
    if (router.menuItems && router.menuItems.length) {
      router.children = filterAsyncRouter(router.menuItems)
    }
    return true 
  })
}

export const loadView = (view) => {
  return () => import(`@/views/${view}`)
}

export default permission
