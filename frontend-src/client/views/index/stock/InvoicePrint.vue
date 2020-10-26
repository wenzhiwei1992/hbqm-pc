<template>
    <div >
        <div id="printHtml">
            <div >
      <!--          <div id="img" style="position: absolute">
                   &lt;!&ndash; <img src="../img/logo/baoye.jpg" alt="图片" width="70" height="70">&ndash;&gt;
                </div>-->
                <div id="allTitle">
                    <div id="title">
                        {{this.$store.state.user.org.orgName }}
                    </div>
                    <div id="invoice">
                        <span style="font-size: 20px;border-bottom: 2px solid black">发货单</span>
                    </div>
                    <div style="width: 100%">
                        <div style="float: right">
                    <span>
                        发货日期：
                    </span>
                            <span>{{ this.props.dateCreated | formatterDate}}</span>
                        </div>
                    </div>
                </div>
                <table border="1" cellpadding="4" cellspacing="0" width="100%" style="text-align: center;width: 100%;table-layout:fixed" >
                    <tbody id="tbody_1">
                    <tr>
                        <td>发货单号</td>
                        <td colspan="4"> <span>{{ this.props.deliverGoodsNo }}</span></td>

                        <td style="width: 100px">运输车牌号</td>
                        <td style="width: 100px"> <span>{{ this.props.licenseNo }}</span></td>
                        <td style="width: 100px">物流公司</td>
                        <td>  <span>{{ this.props.logCompany.logCompany }}</span></td>
                    </tr>
                    <tr>
                        <td>质检人</td>
                        <td > <span>{{this.props.inspector.inspector}}</span></td>
                        <td>批次号</td>
                        <td > <span>{{this.props.invoiceNo}}</span></td>
                        <td>方木</td>
                        <td><span>{{ this.props.squareWood }}</span></td>
                        <td>工字钢</td>
                        <td>{{ this.props.ibeam }}</td>
                    </tr>
                    <tr>
                        <td>楼栋</td>
                        <td>楼层</td>
                        <td>构件编码</td>
                        <td>构件名称</td>
                        <td>构件重量(T)</td>
                        <td>库区号</td>
                        <td>货架号</td>
                        <td colspan="2">备注</td>
                    </tr>
                    <tr v-for="item in data" :key="item.id" >
                        <td>{{item.floorRelevance.building.buildCode}}</td>
                        <td>{{item.floorRelevance.floor.floorNum}}</td>
                        <td>{{item.productDetail.product.productNo}}</td>
                        <td>{{item.productDetail.product.productName}}</td>
                        <td>{{item.productDetail.product.productWt}}</td>
                        <td>{{item.productDetail.stock.stockNo}}</td>
                        <td>{{item.productDetail.shelf.shelfNo}}</td>
                    </tr>
                    </tbody>
                </table>

                <el-form>
                    <el-row :gutter="24">
                        <el-col :span="8" :offset="6">
                            <div class="grid-content bg-purple">
                                <el-form-item label="司机">
                                    <span id="sj"><el-input size="mini" v-model="sj" style="width:30%" > </el-input></span>
                                </el-form-item>
                            </div>
                        </el-col>
                        <el-col :span="8" >
                            <div class="grid-content bg-purple-light">
                                <el-form-item label="收货人">
                                    <span id="reciver"><el-input size="mini" style="width:30%" v-model="reciver"></el-input></span>
                                </el-form-item>
                            </div>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
        </div>
        <el-row :gutter="20">
            <el-col :span="2" :offset="22"><div class="grid-content bg-purple"><el-button size="mini" type="primary" @click="printPage">打印</el-button></div></el-col>
        </el-row>

    </div>

</template>
<script>
    import model from '../../../model/model'
    import moment from "moment";
    import { pageStorage } from '@/util/pageStorage.js'
    import storage from 'good-storage'

    export default {
        data(){
            return{
                invoiceId:null,
                props:{
                    inspector:{
                        inspector:''
                    },
                    logCompany:{
                        logCompany:''
                    }
                },
                data:[],
                sj:null,
                reciver:null,
                pageStorage:pageStorage('id'),
            }
        },
        filters:{
            formatterDate(row) {
                var date = row;
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
        },
        mounted(){
            // this.showData();
            //displate:none other nav
        },
        activated() {
            this.init()
        },
        methods:{
            init(){
              this.pageStorage().then(data=>{
                this.showData(data).then(_=>{
                  if(location.href.split('?')[1]==='printHtml'){
                    this.sj = storage.session.get(this.$route.name+'sj')
                    this.reciver = storage.session.get(this.$route.name+'reciver')
                    this.printHtml(data)
                  }else{
                    this.sj = ""
                    this.reciver = ""
                  }
                })
              })
            },
            showData(id){
                return model.findByIdForPrint(id).then(data=>{
                    this.props = data.entity;
                    console.log(this.props)
                }).then(_=>{
                  return model.findDetailsByInvoiceId(id).then(data=>{
                      this.data = data.entity;
                      console.log(this.data)
                  })
                })
            },
            printPage() {
                storage.session.set(this.$route.name+'sj',this.sj)
                storage.session.set(this.$route.name+'reciver',this.reciver)
                window.open(location.href+'?printHtml')
            },
            printHtml(){
                let sj = document.getElementById("sj");
                sj.innerHTML = this.sj;
                let reciverF = document.getElementById("reciver");
                reciverF.innerHTML = this.reciver;
                //需要打印内容
                let newStr = document.getElementById("printHtml").innerHTML;
                // //当前页面内容
                let oldStr = document.body.innerHTML;
                //替换（隐藏不需要打印的内容）
                document.body.innerHTML = newStr;

                window.print();//打印
                //恢复
                // document.body.innerHTML = oldStr;
                // window.location.reload();

                window.opener=null;
                window.open('','_self');
                window.close();
            }
        }
    }
</script>
<style scoped>
    #title {
        width: auto;
        font-size: 25px;
        font-weight: 500;
        text-align: center;
    }
    #invoice {
        text-align: center;
    }
    #bg {
        width: 93%;
        margin-left: 10px;
        float: left;
    }
    .foot {
        float: left;
        width: 21%;
    }


</style>