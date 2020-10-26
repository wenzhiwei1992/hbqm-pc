import Vue from 'vue'
import router from '@/router'
import { Notification } from 'element-ui';
let bus=new Vue()
bus.$on('not-auth', (err) => {
    //跳转到未经授权页面
    router.push({ name: 'unauthorized' })
    //router.push({ name: 'login' })

})

bus.$on('system-error', (err) => {
    console.log(err);//TO DELETE
    Notification({
        message: '系统异常,请重试',
        type: 'warning'

    })
})
export default bus
