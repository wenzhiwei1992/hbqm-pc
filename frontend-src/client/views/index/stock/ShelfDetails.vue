<template>
  <div class="mod-demo-echarts">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
            <div v-if="datas.length == 0" align="center">
                暂无数据显示
            </div>
            <div id="J_chartLineBox" class="chart-box-stock" v-for="element in datas" :key="element.id" :class="divClass" >
                    <div class="pic-d pic2">
                        <img src="~@/assets/img/icon-details.png" alt="" style="width: 60px;" >
                    </div>
                <!-- <div class="box-right"  >

                    <p class="box-name">{{element.shelfName}}</p>
                    <p class="box-info">{{element.shelfDesc}}</p>
                </div> -->
            </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
    var echarts = require('echarts');
    import clientModel from '../../../model/client-model'
    export default {
        data () {
            return {
                datas:[],
                divClass:'chart-box-center',
            }
        },
        mounted(){
            this.statistic1Change()
        },
        activated () {
            this.statistic1Change()
        },
        methods: {
            statistic1Change(){
                let id = this.$route.query.id;
                clientModel.ax.get('/statistic/statistic6?id='+id)
                    .then((data) => {
                        console.log("this.datas",this.datas)
                        this.datas=data.entity;
                    })
            }
        }
    }
</script>

<style lang="scss">
  .mod-home {
    line-height: 1.5;
  }
  .mod-demo-echarts {
    > .el-alert {
        margin-bottom: 10px;
    }
    > .el-row {
        margin-top: -10px;
        margin-bottom: -10px;
        .el-col {
            padding-top: 10px;
            padding-bottom: 10px;
        }
    }
    .chart-box-stock {
                width: 260px;
                height: 142px;
                float: left;
                margin: 4%;
                cursor: pointer;
                //box-shadow: 0 5px 10px 0 rgba(0,0,0,.1);
                border: 1px solid #eaeaea
    }
    .chart-box-center:hover{
        border-color: #00c1de
        //background: rgb(16, 149, 226)
    }
    .box-right{
        float: right;
        margin-top: 20px;
        width: 145px;
        height: 103px;
    }
    .box-name{
        height: 18px;
        line-height: 18px;
        font-size: 18px;
        color: #333;
        margin-bottom: 8px;
    }
    .box-info{
        line-height: 20px;
        font-size: 12px;
        color: #666;
    }
    .pic-d{
        width: 46px;
        height: 46px;
        margin-left: 80px;
        margin-top: 35px;
        float: left;
    }
  }
</style>

