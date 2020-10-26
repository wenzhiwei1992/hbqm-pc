<template>
  <div>
    <el-row>
      <el-col :span="4" style="line-height:28px;">推送日设置:</el-col>
      <el-col :span="20">
        <el-checkbox-group v-model="week">
          <el-checkbox v-for="(item,index) of weekArr" :key="index" :label="item.value" border  style="margin-right:0;">{{item.name}}</el-checkbox>
        </el-checkbox-group>
      </el-col>
    </el-row>
    <el-row style="margin-top:20px;">
      <el-col :span="4" style="line-height:28px;">推送时间设置:</el-col>
      <el-col :span="20">
        <el-time-picker
          v-model="dateTime"
          :picker-options="{
            selectableRange: '00:00:00 - 23:59:59',
          }"
          value-format="HH:mm"
          placeholder="任意时间点">
        </el-time-picker>
      </el-col>
    </el-row>
    
    <div slot="footer" class="dialog-footer" style="margin-top:50px;display: flex;
    justify-content: flex-end;">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="dateSubmmit()">确 定</el-button>
    </div>
  </div>
</template>

<script>
import newsPush from '@/model/system/newsPush.js'
import {Notification} from 'element-ui'
export default {
    props:['news'],
    data(){
      return{
        dateTime:'',
        week:[],
        weekArr:[
          {
            name:'周一',
            value:"1"
          },
          {
            name:'周二',
            value:"2"
          },
          {
            name:'周三',
            value:"3"
          },
          {
            name:'周四',
            value:"4"
          },
          {
            name:'周五',
            value:"5"
          },
          {
            name:'周六',
            value:"6"
          },
          {
            name:'周日',
            value:"7"
          }
        ],
        jobName:'',
        cron:''
      }
    },
    mounted(){
      this.init()
    },
    methods:{
      init(){
        this.jobName = this.news.typeCode
        this.cron = this.news.cron
        this.dateTime = this.cron.split(" ")[2]+':'+this.cron.split(" ")[1]

        log(this.cron.split(" ")[5].split(","))
        this.week = this.cron.split(" ")[5].split(",")
      },
      dateSubmmit(){
        let _weekAndDate = ""
        let s = 0
        let m = this.dateTime.split(':')[1] || 0
        let h = this.dateTime.split(':')[0] || 0
        let d = '?'
        let M = '*'
        let w = this.week.sort((x,y)=>{
          return x-y
        }).join(',')
        _weekAndDate = `${s} ${m} ${h} ${d} ${M} ${w}`
        log(_weekAndDate)
        let params = {
          cron:_weekAndDate,
          jobName:this.jobName
        }
        newsPush.updateJobTime(params).then(data=>{
          if(data.status == 'success'){
            Notification({
              message: '设置成功',
              type: 'success'
            })
            this.finish()
          }else{
            Notification({
              message: data.msg,
              type: 'warning'
            })
          }
        })
      },
      finish(){
        this.$emit('dialog-finish')
      },
      cancel(){
        this.$emit('dialog-cancel')
      }
    }
}
</script>

<style>

</style>