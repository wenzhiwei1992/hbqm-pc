export default {
    namespaced: true,
    state: {
        menu: {
            menuAuthorized: false,
            defaultActive: '',
            defaultOpeneds: [],
            submenus: [],
            allMenus: [],
            currentSystem: '管理系统'
        }
    },
    mutations: {
        setDefaultActive(state, data) {
            state.menu.defaultActive = data
        }
        ,
        setMenuAuthorized(state, data) {
            state.menu.menuAuthorized = data
        }
        ,
        setSubmenus(state, data) {
            log(data,'setSubmenus')
            state.menu.submenus = data
        }
        ,
        setCurrentSystem(state, data) {
            state.menu.currentSystem = data
        }
        ,

    }
    ,
    getters: {}
    ,
    actions: {}
}
