// 获取筛选数据
export function filterData(data,key){
  let arr = []
  data.forEach(val=>{
    let str = JSON.stringify(arr)
    if(str.indexOf(val[key])===-1){
      arr.push({
        text:val[key],
        value:val[key]
      })
    }
  })
  return arr
}
// 自动获取筛选的表头
export function filterHandler(value, row, column) {
  const property = column['property'];
  return row[property] === value;
}

export default{
  filterData,
  filterHandler
}