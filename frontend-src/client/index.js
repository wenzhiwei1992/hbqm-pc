
import Vue from 'vue'
import VueRouter from 'vue-router'
import Vuex from 'vuex'
import '@/element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './app.vue'
import router from '@/router'
import store from './store/store'
import '@/assets/scss/index.scss'
import '@/assets/styles/common.css'
import global_ from './components/global.vue'
import mixin_ from './util/mixin.js'
import '@/assets/icons/index.js'
import '@/assets/iconfont/iconfont.css'
import '@/assets/iconfont/iconfont.js'
import '@/components';
import 'remixicon/fonts/remixicon.css'


import BaiduMap from 'vue-baidu-map'
import moment from 'moment'

window.log = console.log
// 时间格式化
moment.locale('zh-cn');
Vue.filter('dateformat', function(dataStr, pattern = 'YYYY-MM-DD HH:mm:ss') {
    return moment(dataStr).format(pattern)
})

Vue.use(VueRouter)
Vue.use(Vuex)
Vue.use(mixin_)
// 百度地图
Vue.use(BaiduMap, {
    ak: 'qZ7tm4GXFOttwV60jDYp1ZV5YIsaNzVd'
})

Vue.prototype.$ELEMENT = { size: 'mini' }
Vue.prototype.GLOBAL = global_

import { registerLayoutComponents } from './layout/register';
registerLayoutComponents()



new Vue({
    router,
    store,
    render:(h)=>h(App)
}).$mount('#root')
