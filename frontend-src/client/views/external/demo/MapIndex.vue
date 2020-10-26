<template>
    <div>
      <el-card v-if="isOpen" class="box-card">
        <i class="btn el-icon-close" @click="toggleOpen"></i>
        <div v-for="(item,index) of pointData" :key="index" class="list-row clearfix">
          <div class="name-box" style="text-indent: 0px;" @click="toChart(item.orgIds,item.regionName)()">
            <span style="font-weight:500;">{{item.regionName}}</span>
          </div>
          <div class="name-box" v-for="org of item.children" :key="org.orgIds[0]" @click="setCenter(org.orgPoint)">
            <span>{{org.orgName}}</span>
            <div class="icon el-icon-arrow-right"></div>
          </div>
        </div>
      </el-card>
      <el-button v-else class="open-btn" @click="toggleOpen" type="primary" icon="el-icon-caret-bottom" circle></el-button>
      <baidu-map :center="center" :zoom="zoom" @ready="handler" style="height:100vh" @click="getClickInfo" :scroll-wheel-zoom='true'></baidu-map>
    </div>
    
</template>
<script>
  import clientModel from '@/model/client-model.js'
  import bdImg from '@/assets/img/bd.png'
  import htImg from '@/assets/img/ht.png'
  import hzImg from '@/assets/img/hz.png'
  import laImg from '@/assets/img/la.png'
  import jsImg from '@/assets/img/js.png'
  import fyImg from '@/assets/img/fy.png'
  import bzImg from '@/assets/img/bz.png'
  import hqImg from '@/assets/img/hq.png'
  import mcImg from '@/assets/img/mc.png'
  import zcImg from '@/assets/img/zc.png'
  import xwdImg from '@/assets/img/xwd.png'
  export default {
    name: 'TestBaiDu',
    data () {
      return {
        center: {lng: 117.339063, lat: 31.763451},
        zoom: 13,
        pointData:[],
        isOpen:true
      }
    },
    created(){
      this.getPointData()
    },
    methods: {
      toggleOpen(){
        this.isOpen = !this.isOpen
      },
      getPointData(){
        return clientModel.ax.get('/indexMap/getIndexMapOrg').then(data=>{
          this.pointData = data.entity
        })
      },
      handler({BMap, map}) {
        this.getPointData().then(data=>{
          this.pointData.forEach(val=>{
            val.children.forEach(org=>{
              let point = new BMap.Point(org.orgPoint.x,org.orgPoint.y)
              org.img = org.img.replace('@/','')
              import(`@/${org.img}`).then(data=>{
                var myIcon = new BMap.Icon(data.default, new BMap.Size(343,98));
                // log('myIcon',myIcon)
                let marker = new BMap.Marker(point,{icon:myIcon})// 创建标注
                marker.addEventListener('click',this.toChart(org.orgIds,org.orgName))// 给标注添加事件
                map.addOverlay(marker)// 将标注添加到地图中
              })
            })
          })
        })
        
      },
      getClickInfo (e) {
        console.log(e.point.lng)
        console.log(e.point.lat)
        // this.center.lng = e.point.lng
        // this.center.lat = e.point.lat
      },
      setCenter(point){
        this.center.lng = point.x
        this.center.lat = point.y
        log(this.center,point)
      },
      toChart(chartId,chartTitle){
        let self = this
        log(chartId)
        return function (){
          self.$router.push({
            name:'chartDemo',
            params:{chartId,chartTitle}
          })
        }
      }
    }
  }
  // pointData:[
        //   {
        //     point:{
        //       x:117.227285,
        //       y:31.725321
        //     },
        //     pointNum:13,
        //     markerEventType:'click',
        //     markerEvent:this.toChart(4),
        //     img:xwdImg,
        //     name:'宝业西伟德混凝土预制件（合肥）有限公司'
        //   },
        //   {
        //     point:{
        //       x:117.490377,
        //       y:31.808397
        //     },
        //     pointNum:13,
        //     markerEventType:'click',
        //     markerEvent:this.toChart(0),
        //     img:bdImg,
        //     name:'合肥宝德建筑化工业有限公司'
        //   },
        //   {
        //     point:{
        //       x:115.969894,
        //       y:32.876975
        //     },
        //     pointNum:13,
        //     markerEventType:'click',
        //     markerEvent:this.toChart(3),
        //     img:laImg,
        //     name:'六安宝业建筑化工业有限公司'
        //   },
        //   {
        //     point:{
        //       x:115.963175,
        //       y:32.314995
        //     },
        //     pointNum:13,
        //     markerEventType:'click',
        //     markerEvent:this.toChart(5),
        //     img:hqImg,
        //     name:'霍邱宝业建筑工业化有限公司'
        //   },
        //   {
        //     point:{
        //       x:116.572287,
        //       y:33.19509
        //     },
        //     pointNum:13,
        //     markerEventType:'click',
        //     markerEvent:this.toChart(6),
        //     img:mcImg,
        //     name:'蒙城宝业建筑工业化有限公司'
        //   },
        //   {
        //     point:{
        //       x:115.804447,
        //       y:33.905323
        //     },
        //     pointNum:13,
        //     markerEventType:'click',
        //     markerEvent:this.toChart(7),
        //     img:bzImg,
        //     name:'亳州城建宝业建筑工业化有限公司'
        //   },
        //   {
        //     point:{
        //       x:115.570727,
        //       y:33.233191
        //     },
        //     pointNum:13,
        //     markerEventType:'click',
        //     markerEvent:this.toChart(8),
        //     img:fyImg,
        //     name:'阜阳宝业瑞祥建筑工业化有限公司'
        //   },
        //   {
        //     point:{
        //       x:115.358221,
        //       y:33.292412
        //     },
        //     pointNum:13,
        //     markerEventType:'click',
        //     markerEvent:this.toChart(9),
        //     img:jsImg,
        //     name:'界首宝业瑞祥建筑工业化有限公司'
        //   },
        //   {
        //     point:{
        //       x:117.750936,
        //       y:31.53347
        //     },
        //     pointNum:13,
        //     markerEventType:'click',
        //     markerEvent:this.toChart(10),
        //     img:zcImg,
        //     name:'安徽中材宝业建筑化工业有限公司'
        //   },
        //   {
        //     point:{
        //       x:114.380381,
        //       y:30.346557
        //     },
        //     pointNum:13,
        //     markerEventType:'click',
        //     markerEvent:this.toChart(1),
        //     img:htImg,
        //     name:'湖北宏泰宝业建筑工业化有限公司'
        //   },
        //   {
        //     point:{
        //       x:119.714604,
        //       y:30.248122
        //     },
        //     pointNum:13,
        //     markerEventType:'click',
        //     markerEvent:this.toChart(2),
        //     img:hzImg,
        //     name:'杭州宝业建筑工业化制造有限公司'
        //   }
        // ],
</script>

<style lang="scss" scoped>
  .open-btn{
    position:absolute;
    top:20px;
    left:20px;
    z-index: 1;
    font-size:30px;
  }
  .box-card{
    position:fixed;
    top:20px;
    left:20px;
    z-index:999;
    width: 350px;
    .btn{
      position:absolute;
      left:5px;
      top:5px;
      font-size:20px;
    }
    .list-row{
      border-bottom:1px solid #ebeef5;
      cursor: pointer;
      &:last-child{
        border:none;
      }
      .name-box{
        position:relative;
        padding:10px 0;
        .icon{
          position:absolute;
          right:0;
          top:50%;
          transform: translateY(-50%);
          border-radius:100%;
          background:#496de8;
          color:#fff;
          padding:5px;
        }
      }
    }
    
    
  }
</style>