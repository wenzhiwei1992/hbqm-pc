import Router from 'vue-router'
import router from './routes'
import store from '@/store/store.js'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'
import Watermark from '@/common/js/watermark';
import clientModel from '@/model/client-model'
import menuTabService from '@/service/menuTabService'
import { filterAsyncRouter } from '@/store/modules/permission.js'
import storage from 'good-storage'

const whiteList = ['/login','/menu','/product','/chartDemo','/mapIndex','/register'] //免登录白名单
let _currentSystem
// 解决两次跳转同一路由报错的问题
const VueRouterPush = Router.prototype.push
Router.prototype.push = function push (to) {
    return VueRouterPush.call(this, to).catch(err => err)
}

router.beforeEach((to, from, next) => {
  NProgress.start()
  console.log('上一个页面',from)
  console.log('要去的页面',to)
  _currentSystem = storage.get('currentSystem') || store.state.menu.menu.currentSystem
  if(store.state.user&&Object.keys(store.state.user)){
    // 城投加水印
    setWatermark()
    if(to.path === '/login'){
      if(_currentSystem==='管理系统'){
        next({ path: '/home' })
      }else if(_currentSystem==='权限系统'){
        next({ path: '/system/orgManage' })
      }
    }else{
      if(!store.state.menu.menu.submenus.length){
        loadMenu().then(_=>{
          next()
          loadRouter(to,next)
        })
      }else{
        next()
      }
    }
  }else{
    store.dispatch('loginRefresh').then(data=>{
      console.log('登录状态',data.status)
      // 城投加水印
      setWatermark()
      if (data.status === 'success') {
          if(to.path === '/login'){
            if(_currentSystem==='管理系统'){
              next({ path: '/home' })
            }else if(_currentSystem==='权限系统'){
              next({ path: '/system/orgManage' })
            }
          }else{
            if(!store.state.menu.menu.submenus.length){
              loadMenu().then(_=>{
                next()
                loadRouter(to,next)
              })
            }else{
              next()
            }
          }
      }else{
        if (whiteList.indexOf(to.path) !== -1) { // 在免登录白名单，直接进入
          next()
        }else{
          next({ path: '/login' })
        }
      }
    })
  }
  NProgress.done()
})

function setWatermark(){
  if(store.state.user&&store.state.user.org&&store.state.user.org.orgName === '福建省城投科技有限公司'){
  // if(store.state.user&&store.state.user.org&&store.state.user.org.orgName !== '福建省城投科技有限公司'){
    store.commit('updateBaseName', 'ct')
    let deptName = store.state.user.deptName || '暂无'
    let username = store.state.user.username || '暂无'
    Watermark.set(`${deptName} ${username}`)
  }
}

function loadMenu() {
  var menus = [];
  //加载数据库的菜单
  return clientModel.ax.post('/sys/menu/getMenuListByUser', store.state.user, {showLoading: false}).then((data) => {
      data.entity.content.forEach(option=>{
        if(option.title === _currentSystem){
          menus = option.menuItems
        } 
      })
      menuTabService.setCurrentMenus(store, menus)
  }).catch(err => {
      console.error('err-------->', err)
  });
}

function loadRouter(to,next) {
  var asyncRouter = [];
  //加载数据库的路由
  clientModel.ax.post('/sys/menu/findRoute', store.state.user, {showLoading: false}).then((data) => {
      log(data.entity)
      data.entity.forEach(option=>{
        if(option.title === _currentSystem){
          asyncRouter = filterAsyncRouter(option.menuItems)
          return
        } 
      })
      // console.log('asyncRouter----------',asyncRouter)
      store.dispatch('GenerateRoutes', asyncRouter).then(() => { // 存储路由
        router.options.routes = store.state.permission.routers  // 动态添加可访问路由表
        router.addRoutes(asyncRouter)
        next({ ...to, replace: true })
      })
  }).catch(err => {
      console.error('err-------->', err)
  });
}

export default router
