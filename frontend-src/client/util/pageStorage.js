import router from '@/router/index.js'
import storage from 'good-storage'

export function pageStorage(key){
    let isPageOpen = false
    return function(){
      return new Promise((resolve, reject)=>{
        // 当前打开的页面
        let pageName = router.history.current.name
        // 页面传递的参数 params 集合
        let params = router.history.current.params
        // 缓存的key
        let storageKey = `${pageName}_${key}`
        // 缓存数据
        let storageData = storage.get(storageKey)
        // 上一个页面传递的参数
        let value = params[key]
        // 错误进入(无缓存无参数)
        let isError = !storageData && !value
        // 第一次进该页面 或者清除了缓存(无缓存有参数)
        let isFirst = !storageData && value
        // 顶部tabar进入或者刷新页面(有缓存无参数)
        let onlyStorage = storageData && !value
        // 非第一次进入,也是频率最多的进入(有缓存有参数)
        let normal = storageData && value
        if (isError) {
          router.go(-1)
          return
        }
        if (isFirst) {
            // 第一次进入页面 给参数存到缓存
            storage.set(storageKey, value)
        }
        if (onlyStorage) {
            if (isPageOpen) {
                // tabbar切换 不做任何操作
                return
            }
        }
        log(isPageOpen)
        if (normal) {
            if (isPageOpen) {
                if (JSON.stringify(value) === JSON.stringify(storageData)) {
                    // 带了重复的参数
                    return
                }
            }
            // 缓存和参数不相等时取参数的值赋给缓存
            storage.set(storageKey, value)
        }
        isPageOpen = true
        // 因为中途会有存缓存步骤  这里不能拿变量storageData  只能重新获取storage.get(storageKey)
        resolve(storage.get(storageKey))
      })
    }
}

export default {
  pageStorage
}