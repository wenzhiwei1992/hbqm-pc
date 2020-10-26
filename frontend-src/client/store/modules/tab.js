import Vue from 'vue'
export default {
    namespaced:true,
    state:{
        activeIndex:'',
        openTabs: []
    },
    getters:{

    },
    mutations:{
        setActiveIndex(state,data){
            state.activeIndex=data
        },
        addOpenTab (state,data) {
            state.openTabs.push(data)
        },
        removeOpenTab(state,index){
            let tabs=state.openTabs
            for (let i = 0; i <tabs.length ; i++) {
                if(tabs[i].index==index){
                    tabs.splice(i,1)
                    return
                }
            }
        }
    },
    actions:{
    }
}