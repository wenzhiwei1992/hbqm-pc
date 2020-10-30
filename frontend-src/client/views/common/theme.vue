<template>
  <el-form>
    <h2>布局设置</h2>
    <el-form-item label="导航条类型">
      <el-radio-group v-model="navbarLayoutType">
        <el-radio v-for="(item,index) in navBarMenuList" :key="index" :label="item.label" border>{{item.label}}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="侧边栏皮肤">
      <el-radio-group v-model="sidebarLayoutSkin">
        <el-radio v-for="(item,index) in sideBarMenuList" :key="index" :label="item.label" border>{{item.label}}</el-radio>
      </el-radio-group>
    </el-form-item>
  </el-form>
</template>

<script>
  export default {
    data(){
      return{
        navBarMenuList:[
          {
            label:'default'
          },
          {
            label:'inverse'
          },
          {
            label:'dark'
          },
          {
            label:'vitality'
          },
          {
            label:'mysterious'
          },

        ],
        sideBarMenuList:[
          {
            label:'default'
          },
          {
            label:'dark'
          },
          {
            label:'light'
          }
        ]
      }
    },
    computed: {
      navbarLayoutType: {
        get () { return this.$store.state.navbarLayoutType },
        set (val) { this.$store.commit('updateNavbarLayoutType', val) }
      },
      sidebarLayoutSkin: {
        get () { return this.$store.state.sidebarLayoutSkin },
        set (val) { this.$store.commit('updateSidebarLayoutSkin', val) }
      }
    }
  }
</script>

