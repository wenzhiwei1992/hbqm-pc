import { Loading } from 'element-ui'
import { debounce } from '@/util/util.js'

let needLoadingRequestCount = 0
let loading;

function startLoading() {
  loading = Loading.service({
    lock: true,
    text: '加载中……',
    background: 'rgba(0, 0, 0, 0.7)'
  })
}
export function endLoading() {
  loading.close()
}

export function showFullScreenLoading() {
  if (needLoadingRequestCount === 0) {
    startLoading()
  }
  needLoadingRequestCount++
}
export function tryHideFullScreenLoading() {
  if (needLoadingRequestCount <= 0) return
    needLoadingRequestCount--
  if (needLoadingRequestCount === 0) {
    debounce(endLoading(),300)
  }
}