// 类型判断
export function type(target) {
  var ret = typeof(target);
  var template = {
      "[object Array]": "Array",
      "[object Object]":"Object",
      "[object Number]":"Number",
      "[object Boolean]":"Boolean",
      "[object String]":'String',
      "[object Date]":'Date',
      "[object FormData]":'FormData'
  }
  if(target === null) {
      return 'null';
  }else if(ret == "object"){
      var str = Object.prototype.toString.call(target);
      return template[str];
  }else{
      return ret;
  }
}
export function typeError(typeString,value){
  let valueType = type(value)
  if(valueType!==typeString){
    console.error(`参数类型不是${typeString}类型, 而是${valueType}类型`)
    return false
  }
  return true
}

// 防抖
export function debounce(fn, wait) {
  let timeout = null
  return function() {
    if(timeout !== null) clearTimeout(timeout)     
    timeout = setTimeout(fn, wait);
  }
}
// 节流
export function throttle(fn, delay) { 
  var prev = Date.now()         
  return function() {               
    var now = Date.now()               
    if (now - prev > delay) {                   
      fn()                
      prev = Date.now()             
    }         
  }       
}   

export default{
  type,
  typeError,
  debounce,
  throttle
}