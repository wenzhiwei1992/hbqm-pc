export default {
    namespaced:true,
    state:{
        title:'持云智能工厂'
    },
    getters:{
        fullTitle(state,getters,rootState){
            // return state.title+'系统'
            return state.title
        }
    },
    mutations:{
        updateTitle(state,data){
            state.title=data
        }
    },
    actions:{
    }
}