export default {
  install: function (Vue, opt) {
    Vue.mixin({
      methods: {
        /*根据 my-row-button-group 数组的长度来设置 my-row-button-group 组件所在表格列的宽度
         * 参数1 Array  按钮集合
         */
        rowButtonGroupWidth(model){
          if(model&&Array.isArray(model)){
            let width = model.length * 45
            width = width > 80 ? width : 80
            return width
          }
        },
        /*element表格 点击整行任意位置都会勾选中
        * 参数1 2 3 方法自带
        */
        handleClickRowSelection(row, column, event) {
          this.$refs.handleClickRowSelection.toggleRowSelection(row);
        },
        // 深拷贝
        Clone(obj){
          return JSON.parse(JSON.stringify(obj))
        },
        /*保留小数点num位 并四舍五入
         * 参数1 Number  需要四舍五入的值
         * 参数2 Number  需要保留的小数位数
         */
        ToFixed(value, num) {
          if (!isNaN(value * 1)) {
            let x = String(value).indexOf('.') + 1;
            let y = String(value).length - x;
            if (y > num) {
              return value = (value * 1).toFixed(num)
            }
          }
          return value
        },
        /*格式化日期时间
        * 参数1 日期时间
        * 不保留秒数
        */
        FormatDateTime(date){
            if(date && typeof(date)=='string'){
                date = date.replace('T',' ')
                date = date.replace(':00.000+0000','')
            }
            return date
        },
        /*格式化日期时间
        * 参数1 日期时间
        * 保留秒数
        */ 
        FormatDateTimeSecond(date){
          if(date && typeof(date)=='string'){
              date = date.replace('T',' ')
              date = date.replace('.000+0000','')
          }
          return date
        },
        GetNowFormatDate(){
            let date = new Date();
            let seperator1 = "-";
            let seperator2 = ":";
            //外国的月份都是从0开始的，所以+1
            let month = date.getMonth() + 1;
            let strDate = date.getDate();
            //1-9月用0补位
            if(month >=1 && month <=9){
                month = "0" + month;
            }
            //1-9日用0补位
            if(strDate >=0 && strDate <= 9){
                strDate = "0" + strDate;
            }
            //获取当前时间 yyyy-MM-dd HH:mm:ss
            let currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate + " " +date.getHours() + seperator2 + date.getMinutes() + seperator2 + date.getSeconds();
            return currentdate;
        },
        /**
         * 
         * @param {求和数组(支持小数)} arr 
         */
        FixedSummation(arr){
            let size = 0;
            let num
            if(arr.length<=1){
              return arr[0]
            }
            if (!arr.every(value => isNaN(value))) {
              arr.forEach(val=>{
                if(val.toString().indexOf('.')!=-1){
                  let newSize = val.toString().split('.')[1].length;
                  if(newSize>size){
                      size = newSize;
                  }
                }
              })
              num = arr.reduce((prev, curr, index) => {
                if (!isNaN(Number(curr))&&!isNaN(Number(prev))) {
                  if(index===1){
                    prev = Number(prev+'e+'+size)
                  }
                  curr = (Number(curr+'e+'+size))
                  return prev + curr;
                } else {
                  return prev;
                } 
              })
              return Number(num+'e-'+size)
            }else{
              console.error('你完了')
            }
        },
      }
    })
  }
}