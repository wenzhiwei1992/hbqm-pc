// 自动加载 目录下的 .js 结尾的文件
const utilsContext = require.context('./', true, /\.js$/)
const utils = {}
utilsContext.keys().forEach(util => {
    if(util === './index.js'||util === './mixin.js')return
    let fileKey = util.replace(/\.\/|\.js/g,'')
    utils[fileKey] = utilsContext(util).default
})
export default utils