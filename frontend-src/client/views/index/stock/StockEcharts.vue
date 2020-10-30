<template>
    <div class="list-wrap">
        <el-row class="list">
            <div v-if="datas.length == 0" align="center">
                暂无数据显示
            </div>
            <el-col class="stock-wrap">
                <el-card class="stock" v-for="item of datas" :key="item.id">
                    <div slot="header" class="stock-name">{{item.stockName}}</div>
                    <el-card  @click.native="seeDetailed(item.id,page.id)" shadow="hover" class="shelf-wrap" v-for="page of item.shelfList" :key="page.id">
                        <div>{{page.shelfName}}:{{page.inCount}}</div>
                    </el-card>
                </el-card>
            </el-col>
        </el-row>
    </div>
    <!-- <div class="frm_con">
        <div class="frm_birg clearfix" id="houseType">
            <div class='frm_birg_o clearfix' >
                <ul class='frm_birg_o_list'>
                    <li class='frm_birg_o_cen' v-for="item of datas" :key="item.id">
                        <p class='frm_birg_o_cen_stairs'>{{item.stockName}}</p>
                        <ul class="frm_birg_o_firs">
                            <li v-for="page of item.shelfList" :key="page.id">
                                <p class='frm_birg_o_firs_txt' style='margin-right: 5px;'>{{page.shelfName}}</p>
                                <div class='frm_birg_o_firs_zpt'>
                                    <div class='frm_birg_o_firs_box' :style="`width:${page.shelfProportion};`">{{page.shelfCount}}</div>
                                    <div class='frm_birg_o_firs_boxs' :style="`width:${page.surplusProportion};`">{{page.surplusCount}}</div>
                                </div>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </div> -->
</template>

<script>
    import clientModel from '../../../model/client-model'
    import $ from 'jquery'
    export default {
        data () {
            return {
                datas:[],
                arrBuilding: [],
                // arrProject: [],
                total: '',
                buildCode: '',
                listData: [],
                checkList: [],
                productNameOptions: [],
                buildingId: '',
                projectId: '',
                checkAll: false,
                buttonData: [
                    {
                        name: '新建',
                    },
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: 'click'
                    },
                ],
                project:{}
            }
        },
        mounted(){
            // localStorage.statistic4?this.datas=JSON.parse(localStorage.statistic4):this.statistic1Change()
            this.statistic1Change()
        },
        methods: {
            statistic1Change(){
                clientModel.ax.get('/statistic/statistic4').then((data) => {
                    localStorage.statistic4 = JSON.stringify(data.entity)
                    this.datas=JSON.parse(localStorage.statistic4)

                })
            },
            seeDetailed(stockId,shelfId){
                this.$router.push({
                    name:'productDetailInRecordList',
                    params: {
                        stockId,
                        shelfId
                    }
                })
            }
        }
    }
</script>
<style lang="scss" scoped>
    ul,li{margin:0;padding:0;list-style-type:none;}
    .list-wrap{
        .list{
            
            .stock-wrap{
                display:flex;
                justify-content:space-between;
                .stock-name{
                    font-weight: bold;
                    text-align: center;
                    line-height: 1.5;
                }
                .stock{
                    //  /deep/ css穿透
                    /deep/ .el-card__body{
                        padding:10px;
                        text-align: center;
                    }
                    flex: 1;
                    margin-left: 10px;
                    cursor:pointer;
                    &:first-child{
                        margin-left:0;
                    }
                    .shelf-wrap{
                        margin-top: 5px;
                    }
                }
            }
        }
    }
</style>
<style lang="scss" scoped>
    ul, li {
        list-style: none;
        padding: 0;
        margin:0;
    }
    i {
        font-style: normal;
    }
    .cont {
        width: 100%;
        height: 100%;
    }
    .cont .frm {
        width: 100%;
        float: left;
        /* background-color: #fffeda; */
        min-height: calc(100% - 80px);
        padding-bottom: 30px;
    }
    .clearfix:after {
        content: "";
        display: block;
        clear: both;
    }
    .frm .frm_con {
        width: 100%;
        min-width: 1000px;
        margin: 0 auto;
    }
    .frm_msg .frm_msg_tit {
        font-size: 24px;
    }

    .frm_msg .quantity {
        width: 100%;
    }
    .frm_marg .frm_msg {
        float: left;
        width: 320px;
    }
    .frm_marg .echar {
        width: 100px;
        height: 100px;
        float: left;
    }
    .frm_con .frm_birg {
        margin-top: 40px;
    }
    .quantity ul {
        float: left;
        width: 100%;
    }

    .quantity li {
        float: left;
        margin-top: 5px;
    }
    .frm_birg .frm_birg_o {
        width: 100%;
    }

    .frm_birg_o .frm_birg_o_tit {
        width: 100%;
        float: left;
        margin-top:10px;
    }

    .frm_birg_o_tit p {
        float: left;
    }

    .frm_birg_o_tit i {
        font-size: 20px;
        margin-left: 15px;
    }

    .frm_birg_o .frm_birg_o_list {
        width: 100%;
        display: flex;
        overflow:auto;
        margin-top: 15px;
        margin-left: 10px;
    }

    .frm_birg_o_list .frm_birg_o_cen {
        float: left;
        width: 20%;
        /* margin-left: 20px; */
        min-width: 250px;
        position: relative;
    }

    .frm_birg_o_list .frm_birg_o_cen:nth-child(1) {
        margin-left: 0;
    }

    .frm_birg_o_cen .frm_birg_o_cen_stairs {
        float: left;
        width: 100%;
        text-align: center;
    }

    .frm_birg_o_list .frm_birg_o_firs {
        float: left;
        width: 100%;
        font-size: 18px;
    }

    .frm_birg_o_firs li {
        float: left;
        width: 60%;
        position: relative;
        margin-left: 88px;
        border: 1px solid black;
        border-top: 0;
        border-bottom: 0;

    }

    .frm_birg_o_firs li:nth-child(1) {
        border-top: 1px solid black;
    }

    .frm_birg_o_firs .frm_birg_o_firs_txt {
        float: left;
        position: absolute;
        top: -17px;
        left: -65px;
    }

    .frm_birg_o_firs .frm_birg_o_firs_zpt {
        float: left;
        width: 100%;
    }

    .frm_birg_o_firs_zpt .frm_birg_o_firs_box {
        float: left;
        width: 50%;
        text-align: center;
        background-color: #3E8EF7;
        color:#fff;
        border-bottom: 1px solid black;
    }

    .frm_birg_o_firs_zpt .frm_birg_o_firs_boxs {
        float: left;
        width: 50%;
        text-align: center;
        background-color: #fff;
        border-bottom: 1px solid black;
    }
    .frm_birg_o_list .frm_birg_o_cen {
        position: relative;
        /*height:600px;*/
    }

    /* .frm_birg_o_list .frm_birg_o_firs {
        margin-top: 0px;
    } */

    .frm_birg_o_cen .frm_birg_o_cen_stairs {
        // position: absolute;
        font-size:18px;
        // bottom: -40px;
        // left: -30px;
    }

    .frm_birg_o .frm_birg_o_list {
        margin-top: 0 !important;
    }
    .fl {
        margin-left: 25px;
        font-size: 14px;
    }

    .blu {
        color: #0000ff;
    }

    .gren {
        color: #008000;
    }

    .rea {
        color: #ff0000;
    }
    
    .quantity_one {
        padding-left: 10px;
        border-left: 4px solid #3E8EF7;
    }

    .quantity_tow1 {
        padding-left: 10px;
        border-left: 4px solid #990000;
    }

    .quantity_tow {
        padding-left: 10px;
        border-left: 4px solid #008000;
    }

    .cont .frm table tr {
        float: left;
        margin-top: 5px;
    }

    .quantity_thre {
        padding-left: 10px;
        border-left: 4px solid #ff0000;
    }
</style>

