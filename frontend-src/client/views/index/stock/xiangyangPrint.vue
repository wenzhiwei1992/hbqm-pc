<template>
<div>
    <!-- 打印预览dom -->
    <!-- <div id="printHtmlView" v-loading="loading">
        <table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed">
            <tbody>
                <tr>
                    <td colspan="11" style="height:40px;font-size:16px;">襄阳市宏泰宝业绿色建筑科技有限公司</td>
                </tr>
                <tr>
                    <td colspan="1">
                      发货单号
                    </td>
                    <td colspan="4">
                      {{invoiceData.invoiceNo}}
                    </td>
                    <td colspan="1">
                      发货时间:
                    </td>
                    <td colspan="5">
                      {{invoiceData.invoiceDate}}
                    </td>
                </tr>
                <tr>
                    <td colspan="1">收货单位</td>
                    <td colspan="2" contenteditable="true">{{invoiceData.project.customer}}</td>
                    <td colspan="1">项目名称</td>
                    <td colspan="3" contenteditable="true">{{invoiceData.project.projectName}}</td>
                    <td colspan="1">物流公司</td>
                    <td colspan="3" contenteditable="true">{{invoiceData.logCompany.logCompany}}</td>
                </tr>
                <tr>
                    <td colspan="1">收货地址</td>
                    <td colspan="4" contenteditable="true">{{invoiceData.project.workPlace}}</td>
                    <td colspan="1">联系人</td>
                    <td colspan="1" contenteditable="true"></td>
                    <td colspan="1">联系人电话</td>
                    <td colspan="3" contenteditable="true"></td>
                </tr>
                <tr>
                    <td colspan="1">供方</td>
                    <td colspan="4" contenteditable="true">{{this.$store.state.user.org.orgName }}</td>
                    <td colspan="1">方木</td>
                    <td colspan="1" contenteditable="true">{{invoiceData.squareWood}}</td>
                    <td colspan="1">工字钢</td>
                    <td colspan="3" contenteditable="true">{{invoiceData.ibeam}}</td>
                </tr>
                <tr>
                    <td colspan="1">技术要求</td>
                    <td colspan="3" contenteditable="true">装车完好,施工现场做好保护</td>
                    <td colspan="1">备注</td>
                    <td colspan="6" contenteditable="true"></td>
                </tr>
                <tr>
                    <td colspan="11">垫木、货物等装车辅助物要及时带回，如不带回将按原价计费。</td>
                </tr>
                <tr>
                    <td colspan="11">装车构件信息</td>
                </tr>
                <tr>
                    <td colspan="1">编号</td>
                    <td colspan="2">构件编码</td>
                    <td colspan="1">构件类型</td>
                    <td colspan="1">构件名称</td>
                    <td colspan="1">楼栋楼层</td>
                    <td colspan="2">构件尺寸</td>
                    <td colspan="1">构件方量</td>
                    <td colspan="1">构件重量</td>
                    <td colspan="1">货架号</td>
                </tr>
                <tr v-for="(item,index) in dataList" :key="index">
                    <td colspan="1">{{item.indexCode}}</td>
                    <td colspan="2">{{item.productNo}}</td>
                    <td colspan="1">{{item.productTypeName}}</td>
                    <td colspan="1">{{item.productName}}</td>
                    <td colspan="1">{{item.buildCode}}{{item.floorNum}}F</td>
                    <td colspan="2">{{item.productLen}}*{{item.productThick}}*{{item.productHeight}}</td>
                    <td colspan="1">{{item.productVol}}</td>
                    <td colspan="1">{{item.productWt}}</td>
                    <td colspan="1">{{item.shelfName}}</td>
                </tr>
                <tr>
                    <td colspan="1">合计</td>
                    <td colspan="2"></td>
                    <td colspan="2"></td>
                    <td colspan="1"></td>
                    <td colspan="2"></td>
                    <td colspan="1">{{invoiceData.productVolsNum}}</td>
                    <td colspan="1">{{invoiceData.productWtsNum}}</td>
                    <td colspan="1"></td>
                </tr>
                <tr>
                    <td colspan="1">*发货人</td>
                    <td colspan="2" contenteditable="true"></td>
                    <td colspan="1">车牌号</td>
                    <td colspan="2" contenteditable="true">{{invoiceData.licenseNo}}</td>
                    <td colspan="1">装车时间</td>
                    <td colspan="4" contenteditable="true"></td>
                </tr>
                <tr>
                    <td colspan="1">*到货时间</td>
                    <td colspan="2" contenteditable="true"></td>
                    <td colspan="1">*离场时间</td>
                    <td colspan="2" contenteditable="true"></td>
                    <td colspan="1">司机签字</td>
                    <td colspan="1" contenteditable="true"></td>
                    <td colspan="1">*收货人签字</td>
                    <td colspan="2" contenteditable="true"></td>
                </tr>
                <tr>
                    <td colspan="11">第一联白色 存根联 ；第二联红色 财务联 ；第三联绿色 客户联 ；第四联蓝色 签收回单联 ；第五联黄色 安保联</td>
                </tr>
            </tbody>
        </table>
    </div> -->
    <!-- 打印dom -->
    <div id="printHtml" v-loading="loading">
        <table width="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed">
            <tbody style="border-bottom:none;">
                <tr>
                    <td colspan="11" style="height:40px;font-size:16px;">襄阳市宏泰宝业绿色建筑科技有限公司</td>
                </tr>
                <tr>
                    <td colspan="1">
                      发货单号
                    </td>
                    <td colspan="4">
                      {{invoiceData.invoiceNo}}
                    </td>
                    <td colspan="1">
                      发货时间:
                    </td>
                    <td colspan="5">
                      {{invoiceData.invoiceDate}}
                    </td>
                </tr>
                <tr>
                    <td colspan="1">收货单位</td>
                    <td colspan="2" contenteditable="true">{{invoiceData.project.customer}}</td>
                    <td colspan="1">项目名称</td>
                    <td colspan="3" contenteditable="true">{{invoiceData.project.projectName}}</td>
                    <td colspan="1">物流公司</td>
                    <td colspan="3" contenteditable="true">{{invoiceData.logCompany.logCompany}}</td>
                </tr>
                <tr>
                    <td colspan="1">收货地址</td>
                    <td colspan="4" contenteditable="true">{{invoiceData.project.workPlace}}</td>
                    <td colspan="1">联系人</td>
                    <td colspan="1" class="contenteditable" contenteditable="true"></td>
                    <td colspan="1">联系人电话</td>
                    <td colspan="3" class="contenteditable" contenteditable="true"></td>
                </tr>
                <tr>
                    <td colspan="1">供方</td>
                    <td colspan="4" contenteditable="true">{{this.$store.state.user.org.orgName }}</td>
                    <td colspan="1">方木</td>
                    <td colspan="1" contenteditable="true">{{invoiceData.squareWood}}</td>
                    <td colspan="1">工字钢</td>
                    <td colspan="3" contenteditable="true">{{invoiceData.ibeam}}</td>
                </tr>
                <tr>
                    <td colspan="1">技术要求</td>
                    <td colspan="3" contenteditable="true">装车完好,施工现场做好保护</td>
                    <td colspan="1">备注</td>
                    <td colspan="6" class="contenteditable" contenteditable="true"></td>
                </tr>
                <tr>
                    <td colspan="11">垫木、货物等装车辅助物要及时带回，如不带回将按原价计费。</td>
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
        style="table-layout:fixed"
        :style="index?'page-break-before: always;':''">
            <tbody :style="index?'border-top:none;':''">
                <tr>
                    <td colspan="11">装车构件信息</td>
                </tr>
                <tr>
                    <td colspan="1">编号</td>
                    <td colspan="2">构件编码</td>
                    <td colspan="1">构件类型</td>
                    <td colspan="1">构件名称</td>
                    <td colspan="1">楼栋楼层</td>
                    <td colspan="2">构件尺寸</td>
                    <td colspan="1">构件方量</td>
                    <td colspan="1">构件重量</td>
                    <td colspan="1">货架号</td>
                </tr>
                <tr v-for="(item,itemIndex) in page" :key="itemIndex">
                    <td colspan="1">{{item.indexCode}}</td>
                    <td colspan="2">{{item.productNo}}</td>
                    <td colspan="1">{{item.productTypeName}}</td>
                    <td colspan="1">{{item.productName}}</td>
                    <td colspan="1">{{item.buildCode}}{{item.floorNum}}F</td>
                    <td colspan="2">{{item.productLen}}*{{item.productThick}}*{{item.productHeight}}</td>
                    <td colspan="1">{{item.productVol}}</td>
                    <td colspan="1">{{item.productWt}}</td>
                    <td colspan="1">{{item.shelfName}}</td>
                </tr>
                <tr>
                    <td colspan="1">合计</td>
                    <td colspan="2" class="contenteditable"></td>
                    <td colspan="2" class="contenteditable"></td>
                    <td colspan="1" class="contenteditable"></td>
                    <td colspan="2" class="contenteditable"></td>
                    <td colspan="1">{{invoiceData.productVolsNum}}</td>
                    <td colspan="1">{{invoiceData.productWtsNum}}</td>
                    <td colspan="1" class="contenteditable"></td>
                </tr>
                <tr>
                    <td colspan="1">*发货人</td>
                    <td colspan="2" class="contenteditable" contenteditable="true"></td>
                    <td colspan="1">车牌号</td>
                    <td colspan="2" contenteditable="true">{{invoiceData.licenseNo}}</td>
                    <td colspan="1">装车时间</td>
                    <td colspan="4" class="contenteditable" contenteditable="true"></td>
                </tr>
                <tr>
                    <td colspan="1">*到货时间</td>
                    <td colspan="2" class="contenteditable" contenteditable="true"></td>
                    <td colspan="1">*离场时间</td>
                    <td colspan="2" class="contenteditable" contenteditable="true"></td>
                    <td colspan="1">司机签字</td>
                    <td colspan="1" class="contenteditable" contenteditable="true"></td>
                    <td colspan="1">*收货人签字</td>
                    <td colspan="2" class="contenteditable" contenteditable="true"></td>
                </tr>
                <tr>
                    <td colspan="11">第一联白色 存根联 ；第二联红色 财务联 ；第三联绿色 客户联 ；第四联蓝色 签收回单联 ；第五联黄色 安保联</td>
                </tr>
            </tbody>
        </table>
    </div>
    <el-row :gutter="20">
        <el-col :span="2" :offset="22"><div class="grid-content bg-purple"><el-button size="mini" type="primary" @click="printPage">打印</el-button></div></el-col>
    </el-row>    
</div>
</template>
<style lang="scss" scoped>
    @page{
        size:241mm 279mm;
        margin:8mm 8mm 8mm 10mm;
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
    }
    tr,td{
        text-align:center;
        font-size:12px;
        // transform:scale(0.9);
    }
    #printHtml{
        color:#000 !important;
        page-break-before: always;
    }
    table tr {
        height: 20px;
    }
    
</style>

<script>
    import model from '../../../model/model'
    import moment from "moment";
    import storage from 'good-storage'
    import { pageStorage } from '@/util/pageStorage.js'

export default {
    data(){
        return{
            invoiceData:{
                logCompany:{},
                project:{},
                inspector:''
            },
            componentData:[],
            productWts:'',
            productVols:'',
            total:'',
            dataList:[],
            loading: true,
            pageStorage:pageStorage('obj')
        }
    },
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
            return new Promise((resolve,reject)=>{
                this.invoiceData = obj.invoiceIdXY
                this.productWts = obj.productWts
                this.productVols = obj.productVols
                for(let value of obj.data){
                  // log(value)
                  value.productVol = this.ToFixed(value.productVol,3)
                  // value.productWt = this.ToFixed(value.productWt,3)
                }
                this.dataList = obj.data

                this.total = obj.data.length
                this.invoiceData.invoiceDate = this.FormatDateTime(this.invoiceData.invoiceDate)
                let num = 34
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
                this.loading = false
                this.invoiceData.productVolsNum = this.ToFixed(this.invoiceData.productVolsNum,3);
                this.invoiceData.productWtsNum = this.ToFixed(this.invoiceData.productWtsNum,3);
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


