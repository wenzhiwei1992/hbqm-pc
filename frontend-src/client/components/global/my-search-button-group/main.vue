<template>
  <div>
    <el-tooltip
      v-for="(item,index) of buttonData"
      :key="index"
      class="item"
      effect="light"
      :content="item.name"
      placement="top"
    >
      <el-button
        :type="item.type"
        @click="handleClick(item.event)"
        :icon="item.icon"
      >{{item.name}}</el-button>
    </el-tooltip>
  </div>
</template>

<script>
export default {
  name: 'my-search-button-group',
  props: {
    model: Array
  },
  mounted() {
    this.receiveButtonData()
  },
  methods: {
    // 获取父组件传过来的值
    receiveButtonData() {
      if (this.model) {
        this.model.forEach((val, index) => {
          this.buttonConstant.forEach((item, index) => {
            if (val.name === item.name) {
              val.icon = item.icon || ''
              val.type = item.type || ''
              this.buttonData.push(val)
            }
          })
        })
      }
    },
    // 按钮点击事件
    handleClick(event) {
      this.seekParent(this.$parent, event)
    },
    // 寻找父组件
    seekParent(parent, event) {
      if (!parent.$refs.mainContent) {
        this.seekParent(parent.$parent, event)
      } else {
        if (event) {
          // 判断event是否有参数
          if(event.lastIndexOf('(')!==-1){
            let parameter
            let method = event.slice(0,event.lastIndexOf('('))
            // 判断event参数使用的是''还是""
            if(event.indexOf("'")!==-1){
              parameter = event.slice(event.indexOf("'")+1,event.lastIndexOf("'"))
            }else if(event.indexOf('"')!==-1){
              parameter = event.slice(event.indexOf('"')+1,event.lastIndexOf('"'))
            }
            parent[method](parameter)
          }else{
            parent[event]()
          }
        }
      }
    },
  },
  data() {
    let self = this
    return {
      buttonData: [],
      buttonConstant:self.GLOBAL.BUTTON_CONSTANT,
      /*buttonConstant: [
        {
          icon: 'el-icon-search',
          name: '查询',
          type:'primary'
        },
        {
          icon: 'el-icon-refresh',
          name: '重置'
        }
      ]*/
    }
  }
}
</script>

<style lang="scss" scoped>
</style>