<template>
  <div class="mod-demo-echarts">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
            <div v-if="datas.length == 0" align="center">
                暂无数据显示
            </div>
            <div id="J_chartLineBox" class="chart-box-stock" v-for="element in datas" :key="element.id" :class="divClass" @click="toSubInfo(element.id)">
                    <div class="pic pic2">
                        <img src="~@/assets/img/icon-shelves.png" alt="" style="width: 50px;" >
                    </div>
                <div class="box-right"  >
                    <table class="box-info">
                        <thead class="box-name">
                            <tr>
                                <td>货架名</td>
                                <td></td>
                                <td>{{element.shelfName}}</td>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>货架状态</td>
                                <td></td>
                                <td :style="element.fullStatus == '未满' ? 'color:blue':'color:red'">{{element.fullStatus}}</td>
                            </tr>
                            <tr>
                                <td>已堆放数</td>
                                <td></td>
                                <td :style="element.inCount == element.shelfNum ? 'color:red':'color:blue'">{{element.inCount}}</td>
                            </tr>
                            <tr>
                                <td>货架容量</td>
                                <td></td>
                                <td>{{element.shelfNum}}</td>
                            </tr>
                        </tbody>

                    </table>
                </div>
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
                let stockId = this.$route.query.id;
                clientModel.ax.get('/statistic/statistic5?stockId='+stockId)
                    .then((data) => {
                        console.log("client data:",data.entity);//TO DELETE
                        this.datas=data.entity;
                    })
            },
            toSubInfo(id){
                this.$router.push({path: '/stock/ShelfDetails',query: {"id":id} });
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
        height: 16px;
        line-height: 18px;
        font-size: 14px;
        color: #333;
        margin-bottom: 8px;
    }
    .box-info{
        line-height: 20px;
        font-size: 12px;
        color: #666;
    }
    .pic{
        width: 46px;
        height: 46px;
        margin-left: 30px;
        margin-top: 39px;
        float: left;
    }
  }
</style>

