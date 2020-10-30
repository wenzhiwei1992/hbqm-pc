<template>
<div>
    <!-- 打印预览dom -->
    <!-- <div id="printHtmlView" style="display:none;">
        <div class="content">
            <h2 style="text-align: center">{{invoiceData.project.undertaking}}</h2>
            <h3 class="mid">
                发货单
            </h3>
            <div class="date-box">
                <span>发货日期:{{invoiceData.invoiceDate}}</span>
            </div>
        </div>
        <div class="table">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed">
                <tbody>
                    <tr>
                        <td>项目名称</td>
                        <td colspan="4">{{invoiceData.project.projectName}}</td>
                        <td>运输车牌号</td>
                        <td>{{invoiceData.licenseNo}}</td>
                        <td>发货单号</td>
                        <td colspan="3">{{invoiceData.deliverGoodsNo}}</td>
                    </tr>
                    <tr>
                        <td>收货单位</td>
                        <td colspan="4" contenteditable="true"></td>
                        <td>收货人电话</td>
                        <td contenteditable="true"></td>
                        <td>收货地址</td>
                        <td colspan="3" contenteditable="true"></td>
                    </tr>
                    <tr>
                        <td>序号</td>
                        <td colspan="2">产品名称</td>
                        <td colspan="1">类别</td>
                        <td>楼栋楼层</td>
                        <td>砼等级</td>
                        <td>数量(块)</td>
                        <td>体积(方)</td>
                        <td>重量(T)</td>
                        <td class="relative" colspan="2" :rowspan="dataList.length+1">
                            <div class="title">备注</div>
                            <div class="text" contenteditable="true">
                            </div>
                        </td>
                    </tr>
                    <tr v-for="(item,itemIndex) in dataList" :key="itemIndex">
                        <td>{{item.indexCode}}</td>
                        <td colspan="2">{{item.productName}}</td>
                        <td>{{item.productTypeName}}</td>
                        <td>{{item.buildAndFloor}}</td>
                        <td>{{item.productGrade}}</td>
                        <td>{{item.productNum}}</td>
                        <td>{{item.productVol}}</td>
                        <td>{{item.productWt}}</td>
                    </tr>
                    <tr>
                        <td colspan="6">合计</td>
                        <td>{{total}}</td>
                        <td>{{productVols}}</td>
                        <td>{{productWts}}</td>
                        <td colspan="2"></td>
                    </tr>
                </tbody>
            </table>
        </div>

        <ul class="foot">
            <li>
                <div class="text">质检：</div>
                <div class="input" contenteditable="true"></div>
            </li>
            <li>
                <div class="text">仓库：</div>
                <div class="input" contenteditable="true"></div>
            </li>
            <li>
                <div class="text">司机：</div>
                <div class="input" contenteditable="true"></div>
            </li>
            <li>
                <div class="text">收货人：</div>
                <div class="input" contenteditable="true"></div>
            </li>
        </ul>
    </div> -->
    <!-- 打印dom -->
    <div id="printHtml" >
        <div class="content">
            <h2 style="text-align: center">{{invoiceData.project.undertaking}}</h2>
            <h3 class="mid">
                发货单
            </h3>
            <div class="img-box">
                <img :src="invoiceData.logoPath" :onerror="defaultImg">
                <!--<img src="~@/assets/img/logo2.jpg">-->
            </div>
            <div class="date-box">
                <span>发货日期:{{invoiceData.invoiceDate}}</span>
            </div>
        </div>
        <div class="table">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed">
                <tbody style="border-bottom:none;">
                    <tr>
                        <td>项目名称</td>
                        <td colspan="4">{{invoiceData.project.projectName}}</td>
                        <td>运输车牌号</td>
                        <td>{{invoiceData.licenseNo}}</td>
                        <td>发货单号</td>
                        <td colspan="3">{{invoiceData.deliverGoodsNo}}</td>
                    </tr>
                    <tr>
                        <td>收货联系人</td>
                        <td colspan="4" class="contenteditable" contenteditable="true"></td>
                        <td>收货人电话</td>
                        <td class="contenteditable" contenteditable="true"></td>
                        <td>收货地址</td>
                        <td colspan="3" class="contenteditable" contenteditable="true"></td>
                    </tr>
                </tbody>
            </table>
            <table 
            v-for="(page,index) in componentData" 
            :key='index' 
            width="100%" 
            border="0" 
            cellpadding="0" 
            cellspacing="0" 
            style="table-layout:fixed;"
            :style="index?'page-break-before: always;':''"
            >
                <tbody :style="index?'border-bottom:none;':''">
                    <tr>
                        <td>序号</td>
                        <td>产品名称</td>
                        <td colspan="2">类别</td>
                        <td>楼栋楼层</td>
                        <td>砼等级</td>
                        <td>数量(块)</td>
                        <td>体积(方)</td>
                        <td>重量(T)</td>
                        <td class="relative" colspan="2" :rowspan="page.length+1">
                            <div class="title">备注</div>
                            <div class="text contenteditable" contenteditable="true"></div>
                        </td>
                    </tr>
                    <tr v-for="(item,itemIndex) in page" :key="itemIndex">
                        <td>{{item.indexCode}}</td>
                        <td>{{item.productName}}</td>
                        <td colspan="2">{{item.productTypeName}}</td>
                        <td>{{item.buildAndFloor}}</td>
                        <td>{{item.productGrade}}</td>
                        <td>{{item.productNum}}</td>
                        <td>{{item.productVol}}</td>
                        <td>{{item.productWt}}</td>
                    </tr>
                </tbody>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed">
                <tbody>
                    <tr>
                        <td colspan="6">合计</td>
                        <td>{{total}}</td>
                        <td>{{productVols}}</td>
                        <td>{{productWts}}</td>
                        <td colspan="2"></td>
                    </tr>
                </tbody>
            </table>
        </div>

        <ul class="foot">
            <li>
                <div class="text">质检：</div>
                <div class="input" contenteditable="true"></div>
            </li>
            <li>
                <div class="text">仓库：</div>
                <div class="input" contenteditable="true"></div>
            </li>
            <li>
                <div class="text">司机：</div>
                <div class="input" contenteditable="true"></div>
            </li>
            <li>
                <div class="text">收货人：</div>
                <div class="input" contenteditable="true"></div>
            </li>
        </ul>
    </div>
    <el-row :gutter="20">
        <el-col :span="2" :offset="22"><div class="grid-content bg-purple"><el-button size="mini" type="primary" @click="printPage">打印</el-button></div></el-col>
    </el-row>    
</div>
</template>
<style lang="scss" scoped>
    @page{
        size:263mm 241mm;
        margin:8mm 0;
    }
    @media print{
        #printHtml{
            display:block;
        }
    }
    ul,li{
        margin:0;
        padding:0;
        list-style-type:none;
    }
    *[contenteditable]{
        outline:none;
    }
    .mid{
        text-align:center;
    }
    table {
        border-collapse: collapse;
        border-spacing: 1px;
        text-align: center;
    }
    tbody{
        border-bottom:1px solid #000;
        border-right:1px solid #000;
    }
    td{
        border-left:1px solid #000;
        border-top:1px solid #000;
        padding:0;
        text-align:center;
        font-size:12px;
        // transform:scale(0.9);
    }
    #printHtml{
        color:#000 !important;
        position:relative;
    }
    .table tr {
        height: 20px;
    }

    .content, .table, .foot {
        padding: 0 50px;
        margin-top: 10px;
    }

    .content > div {
        margin-top: 5px;
    }
    .foot{
        display: flex;
        li{
            width: 25%;
            display: flex;
            .text{
                width: 4em;
            }
            .input{
                flex: 1;
            }
        }
    }
    .relative{
        position:relative;
    }
    td .title{
        position:absolute;
        width:100%;
        height:20px;
        line-height:20px;
        top:0;
        left:0;
        border-bottom:1px solid #000;
    }
    td .text{
        position:absolute;
        top:20px;
        width: 100%;
        height: calc(100% - 20px);
        overflow:hidden;
        display: flex;
        flex-direction: column;
        justify-content: center;    
        align-items: center;
        flex-wrap: wrap;
    }
    .content{
        position:relative;
        .img-box{
            position:absolute;
            left:50px;
            bottom:-10px;
            width:60px;
            height: 60px;
            img{
                width: 100%;
                height:100%;
            }
        }
        .date-box{
            position:absolute;
            right:50px;
            bottom:0;
        }
    }
    

    
</style>

<script>
    import model from '../../../model/model'
    import avatar from '../../../assets/img/logo2.jpg'
    import moment from "moment";
    import storage from 'good-storage'
    import { pageStorage } from '@/util/pageStorage.js'

export default {
    data(){
        return{
            invoiceData:{
                logCompany:{},
                project:{}
            },
            componentData:[],
            productWts:'',
            productVols:'',
            total:'',
            dataList:[],
            pageStorage:pageStorage('obj'),
            defaultImg: 'this.src="' + avatar + '"' //默认图地址
        }
    },
    // mounted(){
    //     this.showData();
    // },
    activated() {
        this.init()
    },
    methods:{
        init(){
          this.pageStorage().then(data=>{
            this.showData(data).then(_=>{
              if(location.href.split('?')[1]==='printHtml'){
                this.printHtml(data)
              }
            })
          })
        },
        formatterDate(date0) {
            var date = date0;
            if (date == undefined) {
                return "";
            }
            return moment(date).format("YYYY-MM-DD");
        },
        showData(obj){
            // let id = this.$route.params.invoiceIdHT;
            // if(id == undefined || id == null){
            //     this.$router.push({name:'invoiceNewList'})
            //     return
            // }
            return new Promise((resolve,reject)=>{
              this.invoiceData = obj.invoiceIdHT
              console.log(this.invoiceData)
              this.productWts = obj.productWts
              this.productVols = obj.productVols
              this.dataList = obj.data
              this.total = obj.data.length
              this.invoiceData.invoiceDate = this.FormatDateTime(this.invoiceData.invoiceDate)
              let num = 30
              let data = obj.data
              let page = Math.ceil(data.length/num)
              for(let i=0;i<page;i++){
                  this.componentData[i] = []
                  for(let j=i*num;this.componentData[i].length<num&&j<data.length;j++){
                      data[j].indexCode = j+1
                      this.componentData[i].push(data[j])
                  }
              }
              console.log(this.componentData)
              // this.loading = false
              // 清除所有手动填写的格子
              document.querySelectorAll('.contenteditable').forEach(option=>{
                option.innerHTML = ""
              })
              resolve()
            })
            
        },
        printPage() {
            let codestr = window.document.getElementById("printHtml").innerHTML;
            storage.session.set(this.$route.name+'codestr',codestr)
            window.open(location.href+'?printHtml')
        },
        printHtml(){
            // 打印缓存里的页面
            window.document.body.innerHTML = storage.session.get(this.$route.name+'codestr');
            window.print();   //打印当前窗口
            // 延时关闭 解决图片加载异常
            setTimeout(_=>{
              window.opener=null;
              window.open('','_self');
              window.close();
            },0)
        }
    }
}
</script>


