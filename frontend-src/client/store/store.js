import Vue from 'vue'
import Vuex from 'vuex'
import common from './common'
import menu from './modules/menu'
import header from './modules/header'
import tab from './modules/tab'
import permission from './modules/permission'



const isDev=process.env.NODE_ENV==='development'

Vue.use(Vuex)
export default new Vuex.Store({
    strict:isDev,
    state:common.state,
    mutations:common.mutations,
    getters:common.getters,
    actions:common.actions,
    modules:{
        menu,
        header,
        tab,
        permission
    }
})
