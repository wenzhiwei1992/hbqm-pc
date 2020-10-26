import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/views/main.vue'
import Empty from '@/views/main-empty.vue'

Vue.use(Router)

export const constantRouterMap = [
    {
        path: '/',
        redirect: '/home'
    },
    {
        path: '/Main',
        component: Main,
        name: 'main',
        redirect: {name: 'home'},
        meta: {title: '主入口整体布局'},
        children: [
          {
              path: '/home',
              component: () => import('@/views/common/home.vue'),
              name: 'home',
              meta: {title: '首页', isTab: true}
          },
          {
              path: '/theme', 
              component: () => import('@/views/common/theme.vue'), 
              name: 'theme', 
              meta: {title: '主题', isTab: true}
          }
        ]
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('@/views/login/login.vue')
    },
    {
        path: '/unauthorized',
        name: 'unauthorized',
        component: () => import('@/views/login/unauthorized.vue')
    },
    {
        path: '/menu',
        name: 'menu',
        component: () => import('@/views/login/menu.vue')
    },
    {
        path: '/product',
        component: () => import('@/views/index/check/ProductDetailPrintQRCodeNew.vue'),
        name: 'productDetailPrintQRCode'
    },
    {
        path: '/mapIndex',
        component: () => import('@/views/external/demo/MapIndex.vue'),
        name: 'mapIndex'
    },
    {
        path: '/chartDemo',
        component: () => import('@/views/external/demo/ChartDemo.vue'),
        name: 'chartDemo'
    },
    {
        path: '/receipt',
        component: () => import('@/views/index/ship/receipt/index.vue'),
        name: 'receipt'
    }
]

export default new Router({
  mode: 'hash',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})
