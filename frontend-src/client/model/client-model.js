import axios from 'axios'
import bus from '../util/bus'
import serverConfig from '../../server.config'
import { Notification } from 'element-ui';
import { showFullScreenLoading,tryHideFullScreenLoading,endLoading } from '@/common/js/loading.js'

let ax = axios.create({
    baseURL: serverConfig.baseURL,
    withCredentials: true,
})
const notification = ({message,type='warning'})=>Notification({message,type})

ax.interceptors.request.use(config => {
    if (config.showLoading) {
      showFullScreenLoading()
      // log(config.method)
      // log(config)
    }
    return config;
  },error => Promise.reject(error)
);
const errorContent = {
  '400':{
    message:'错误请求'
  },
  '401':{
    message:'未授权，请重新登录'
  },
  '403':{
    message:'您已掉线或未授权，请重新登录',
    callback(){return bus.$emit('not-auth')}
  },
  '404':{
    message:'请求错误,未找到该资源'
  },
  '405':{
    message:'请求方法未允许'
  },
  '408':{
    message:'请求超时'
  },
  '500':{
    message:'服务器端出错'
  },
  '501':{
    message:'网络未实现'
  },
  '502':{
    message:'网络错误'
  },
  '503':{
    message:'服务不可用'
  },
  '504':{
    message:'网络超时'
  },
  '505':{
    message:'http版本不支持该请求'
  },

}
ax.interceptors.response.use(response=> {
    if (response.config.showLoading) {
      tryHideFullScreenLoading()
      // log(response.config.method)
      // log(response.config)
    }
    if (!response.data) {
        console.log("no response data")
        bus.$emit('system-error')
        return Promise.reject(response)
    }
    if (!response.data.status && !response.status) {
        console.log("no status");
        bus.$emit('system-error')
        return Promise.reject(response)
    }
    if (response.data.status == 'fail' && !response.data.msg && !response.status) {
        console.log("no fail msg")
        bus.$emit('system-error')
        return Promise.reject(response)
    }
    return response.data;
},error=> {
    if (error && error.response) {
      endLoading()
      if (error && error.response) {
        if(!errorContent[error.response.status]){
          notification({message:`连接错误${error.response.status}`})
        }else{
          notification({message: errorContent[error.response.status].message})
          !errorContent[error.response.status].callback||(errorContent[error.response.status].callback)()
        }
      } else {
        notification({message: "连接到服务器失败"})
      }
    } else {
      notification({message: "连接到服务器失败"})
    }
    return Promise.reject(error);
});
const axMethod = {
  post: (url, data = null, config = { showLoading: true }) => ax.post(url, data, config),
  get: (url, data = null, config = { showLoading: true }) => ax.get(url, data, config),
  update: (url, data = null, config = { showLoading: true }) => ax.update(url, data, config),
  delete: (url, data = null, config = { showLoading: true }) => ax.delete(url, data, config),
  put: (url, data = null, config = { showLoading: true }) => ax.put(url, data, config),
}
export default {
    login(username, password, tenantId) {
        axios.defaults.headers.common['X-TenantID'] = tenantId;
        return axMethod.post('/user/login', {username, password, tenantId})
    },
    loginRefresh() {
        return axMethod.post('/user/login/refreshLoginUser','',{showLoading:false})
    },
    logout() {
        return axMethod.post('/user/logout')
    },
    setTenantId(tenantId) {
        axios.defaults.headers.common['X-TenantID'] = tenantId;
    },
    ax:axMethod
}
