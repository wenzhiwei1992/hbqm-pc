import Vue from 'vue'
import TopHeader from './top-header.vue'
import AsideMenu from './aside-menu.vue'
import ContentTab from './content-tab.vue'

function registerLayoutComponents(){
    Vue.component('TopHeader', TopHeader)
    Vue.component('AsideMenu', AsideMenu)
    Vue.component('ContentTab', ContentTab)
}

export {
    registerLayoutComponents
}