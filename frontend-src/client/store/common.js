import model from '../model/client-model'
import { Notification } from 'element-ui';
import storage from 'good-storage'

export default {
    state:{
        user:null,
        loading:false,
        setup:null,
        // 页面文档可视高度(随窗口改变大小)
        documentClientHeight: 0,
        // 导航条, 布局风格, defalut(默认) / inverse(反向)
        navbarLayoutType: 'default',
        // 侧边栏, 布局皮肤, light(浅色) / dark(黑色) / wram(暖色)
        sidebarLayoutSkin: 'light',
        // 侧边栏, 折叠状态
        sidebarFold: false,
        // 侧边栏, 菜单
        menuList: [],
        menuActiveName: '',
        // 主入口标签页
        mainTabs: [],
        mainTabsActiveName: '',
        // 内容页面header和footer栏的高度
        headerOperationBarHeight:0,
        footerOperationBarHeight:0,
        mainContentHeight:0,
        baseName:null
    },
    getters:{
    },
    mutations:{
        updateMainContentHeight(state, height) {
            state.mainContentHeight = height
        },
        updateHeaderOperationBarHeight (state, height) {
            state.headerOperationBarHeight = height
        },
        updateFooterOperationBarHeight (state, height) {
            state.footerOperationBarHeight = height
        },
        doLogin (state, userInfo) {
            state.user = userInfo
        },
        doLogout (state) {
            state.user = null
        },
        startLoading (state) {
            state.loading = true
        },
        endLoading (state) {
            state.loading = false
        },
        setSetup(state, setup) {
            state.setup = setup
        },
        updateDocumentClientHeight (state, height) {
            state.documentClientHeight = height
        },
        updateNavbarLayoutType (state, type) {
            state.navbarLayoutType = localStorage.navbarLayoutType = type
        },
        updateSidebarLayoutSkin (state, skin) {
            state.sidebarLayoutSkin = localStorage.sidebarLayoutSkin = skin
        },
        updateSidebarFold (state, fold) {
            state.sidebarFold = fold
        },
        updateMenuList (state, list) {
            state.menuList = list
        },
        updateMenuActiveName (state, name) {
            state.menuActiveName = name
        },
        updateMainTabs (state, tabs) {
            // console.log('tabs',tabs)
            // if(tabs.length>0){
            //   storage.session.set('mainTabs',tabs)
            // }
            // state.mainTabs = storage.session.get('mainTabs')
            state.mainTabs = tabs
        },
        updateMainTabsActiveName (state, name) {
            console.log('name',name)
            storage.session.set('mainTabsActiveName',name)
            state.mainTabsActiveName = storage.session.get('mainTabsActiveName')
        },
        updateBaseName (state, name) {
            console.log('name',name)
            state.baseName = name
            // storage.session.set('baseName',name)
            // state.mainTabsActiveName = storage.session.get('mainTabsActiveName')
        }
    },
    actions:{
        login ({ commit }, { username, password,tenantId }) {
            commit('startLoading')
            return new Promise((resolve, reject) => {
                model.login(username, password,tenantId)
                    .then(data => {
                        if(data.status=='fail'){
                            Notification({
                                message: data.msg,
                                type: 'warning'

                            })
                            commit('doLogin',null)
                            commit('endLoading')
                            return reject(data)
                        }
                        commit('doLogin', data.entity)
                        commit('updateMainTabs',[])//将打开的tabs都关闭
                        model.setTenantId(data.entity.tenantId)
                        resolve(data)
                        commit('endLoading')
                    })
                    .catch(err=>{
                        commit('endLoading')
                    })
            })
        },
        loginRefresh({commit}){
            commit('startLoading')
            return new Promise((resolve,reject)=>{
                model.loginRefresh().then(data=>{
                    if(data.status=='fail'){
                        commit('doLogin',null)
                    }else{
                        model.setTenantId(data.entity.tenantId)
                        commit('doLogin', data.entity)
                    }
                    resolve(data)
                    commit('endLoading')
                }).catch(err=>{
                    commit('endLoading')
                })
            });
        },
        logout({commit}){
            commit('startLoading')
            return new Promise((resolve,reject)=>{
                model.logout().then(data=>{
                    commit('doLogout')
                    resolve()
                    commit('endLoading')
                }).catch(err=>{
                    commit('endLoading')
                })
            });
        },
        setupRefresh({commit}){
            return new Promise((resolve,reject)=>{
                model.ax.get('setup').then(data=>{
                    if(data.status=='fail'){
                        commit('setSetup',null)
                    }else{
                        commit('setSetup', data.entity)
                    }
                    resolve(data)
                }).catch(err=>{
                })
            });
        }
    }
}