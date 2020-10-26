<template>
  <div class="home">
    <div class="tab-bar">
      <ul class="tab-option-box">
        <li class="tab-option" :class="item.homeTabActive?'tab-active':''" v-for="(item,index) of tabBar" :key="index" @click="selectTab(index)">
          <i :class="item.homeTabIcon"></i>
          <span>{{item.codeName}}</span>
        </li>
      </ul>
    </div>
    <div class="content">
      <component
          v-for="(item,index) in tabBar" 
          :key="index"
          :is="item.homeTabTemplateName"
          :ref="item.homeTabRef"
          v-show="item.homeTabActive"
      ></component>
    </div>
  </div>
</template>

<script>
import DailyIndicators from './DailyIndicators.vue'
import YardInventory from './YardInventory.vue'
import util from '@/util/index.js'
import codeExplainContent from '@/model/basicdata/codeExlpainContent'

export default {
  components:{
    DailyIndicators,
    YardInventory
  },
  data() {
    return {
      currentView:'DailyIndicators',
      tabBar:[
        
      ]
    }
  },
  activated() {
    this.init()
    
  },
  methods: {
    init(){
      this.getTabBar().then(data=>{
        let tabBar = data.entity
        tabBar.forEach((option,index) => {
          if(option.homeTabHtml){
            let opt = {
              templateName:option.homeTabTemplateName,
              html:option.homeTabHtml
            }
            util.template.doComponents(opt)
          }
          option.homeTabActive = false
        })
        tabBar[0].homeTabActive = true
        this.tabBar = tabBar
      })
    },
    getTabBar(){
      return codeExplainContent.getAllByCodeNo('HOME_CHART').then(data=>{
        return data
      })
    },
    // 总选项卡
    selectTab(index){
      this.tabBar.forEach(option => {
        option.homeTabActive = false
      })
      this.tabBar[index].homeTabActive = true
      let ref = this.$refs[this.tabBar[index].homeTabRef][0]
      if(ref.lineChart){
        this.$nextTick(_=>{
          ref.lineChart.resize()
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .home{
    font-family: "PingFangSC","helvetica neue","hiragino sans gb","arial","microsoft yahei ui","microsoft yahei","simsun","sans-serif"!important;
    width: 100%;
    height: 100%;
    padding-bottom:50px;
    .tab-bar{
      background:#fff;
      border-bottom:1px solid rgb(232, 232, 232);;
      .tab-option-box{
        display:flex;
        justify-content: center;
        .tab-option{
          padding:15px;
          font-size:16px;
          margin-right:32px;
          line-height:40px;
          border-bottom:2px solid rgba(0,0,0,0);
        }
        .tab-active{
          color:#1890ff;
          font-weight:500;
          border-bottom-color:#1890ff;
        }
      }
    }
    .content{
      margin-top:24px;
      padding: 0 40px;
    }
  }
</style>

