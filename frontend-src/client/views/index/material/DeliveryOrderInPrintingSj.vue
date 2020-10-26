<template>
    <div>
        <div id="printHtml" style="color: #000 !important;font-family: Tahoma !important;">
            <div id='title'>{{ this.title }}</div>

            <table id="table_center" cellpadding="0" cellspacing="0" width="100%"
                   style="text-align: left;width: 100%;table-layout:fixed;font-size: 6px;margin-top: 20px;color: #000 !important;font-family: Tahoma !important;">
                <tr>
                    <td>公司名称 : {{ this.company }}</td>
                    <td>入库日期 : {{this.props&&this.props.deliveryOrderTime | formatterDate}}</td>
                    <td>入库单号 : {{ this.props&&this.props.deliveryOrderNo }}</td>
                </tr>
                <tr>
                    <td colspan="2">供货单位 : {{this.props&&this.props.supplierName}}</td>
                    <td>仓 库 : {{this.props&&this.props.stockName}}</td>
                </tr>
                <tr>
                    <td>部门 : {{this.props&&this.props.deptName}}</td>
                    <td>业务类型 : 普通采购</td>
                    <td>
                        <div class="ipt-box" style="display:flex;">
                            <div>备 注 : </div>
                            <div class="contenteditable" style="flex:1;outline:none;" contenteditable="true"></div>
                        </div>
                    </td>
                </tr>
            </table>
            <table border="1" cellpadding="4" cellspacing="0" width="100%"  bordercolor="#000000"
                   style="text-align: center;margin-top: 10px;width: 90%;table-layout:fixed;font-size: 8px;color: #000 !important;font-family: Tahoma !important;">
                <tr>
                    <td style="width: 45px">序号</td>
                    <td>存货编码</td>
                    <td>存货名称</td>
                    <td>规格型号</td>
                    <td class="td_font_size">单位</td>
                    <td class="td_font_size_70">数量</td>
                    <td class="td_font_size_100">原币含税单价</td>
                    <td class="td_font_size_70">税率</td>
                    <td class="td_font_size_100">原币价税合计</td>
                    <td class="td_font_size_100">不含税价合计</td>
                    <td class="td_font_size_90">备注</td>
                </tr>
                <tr v-for="(item,index) in data" :key="item.index">
                    <td style="width: 70px">{{index+1}}</td>
                    <td>{{item.materialStorageId}}</td>
                    <td>{{item.materialName}}</td>
                    <td>{{item.materialSpecification}}</td>
                    <td class="td_font_size_90">{{item.materialUnit}}</td>
                    <td class="td_font_size">{{item.amount}}</td>
                    <td class="td_font_size_70">{{item.univalence}}</td>
                    <td class="td_font_size_90">{{item.taxRate}}</td>
                    <td class="td_font_size_70">{{Number(ToFixed(item.totalUnivalence,3))}}</td>
                    <td class="td_font_size_70">{{Number(ToFixed(item.totalUnivalenceNo,3))}}</td>
                    <td class="td_font_size_90"></td>
                </tr>
                <tr>
                    <td>小计</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>{{this.pageTotalQuantity}}</td>
                    <td></td>
                    <td></td>
                    <td>{{this.pageTotalUnivalence}}</td>
                    <td>{{this.pageTotalUnivalenceNo}}</td>
                    <td></td>
                </tr>
                <tr>
                    <td>合计</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>{{this.totalQuantity}}</td>
                    <td></td>
                    <td></td>
                    <td>{{this.allTotalUnivalence}}</td>
                    <td>{{this.allTotalUnivalenceNo}}</td>
                    <td></td>
                </tr>
            </table>

            <el-form :inline="true" v-if="this.$store.state.user.org.orgName!='杭州宝业建筑工业化制造有限公司'" style="font-size: 12px;position: fixed;bottom: 10px;color: #000 !important;font-family: Tahoma !important;">


                <el-form-item style="width:180px;">
                    <span>审核人: {{this.props&&this.props.ccheck}}</span>
                </el-form-item>

                <el-form-item style="width:180px">
                    <span>制单人:  {{this.props&&this.props.createdByName}}</span>
                </el-form-item>

                <el-form-item style="width:180px">
                    <span>经办人:  </span>
                    <!--<span>经办人:  {{this.props.createdByName}}</span>-->
                </el-form-item>

            </el-form>
            <table class="table_color" v-if="this.$store.state.user.org.orgName!='杭州宝业建筑工业化制造有限公司'" style="margin-left: 50px;margin-top: -20px;position: fixed;bottom: 10px;color: #000 !important;font-family: Tahoma !important;">
                <tr>
                    <td>注：</td>
                    <td>白联: 仓库</td>
                    <td>绿联: 财务</td>
                    <td>蓝联: 采购部</td>
                </tr>
            </table>


            <el-form :inline="true" v-if="this.$store.state.user.org.orgName=='杭州宝业建筑工业化制造有限公司'" style="font-size: 12px;position: fixed;bottom: 20px;color: #000 !important;font-family: Tahoma !important;">
                <el-form-item style="width:40px">
                </el-form-item>

                <el-form-item style="width:170px">
                    <span>制单人:  {{this.props&&this.props.createdByName}}</span>
                </el-form-item>

                <el-form-item style="width:170px;">
                    <span>收货人:  </span>
                </el-form-item>

                <el-form-item style="width:170px">
                    <span>质检人:  </span>
                    <!--<span>经办人:  {{this.props.createdByName}}</span>-->
                </el-form-item>

            </el-form>

            <table class="table_color" v-if="this.$store.state.user.org.orgName=='杭州宝业建筑工业化制造有限公司'" style="margin-left: 50px;margin-top: -20px;position: fixed;bottom: 10px;color: #000 !important;font-family: Tahoma !important;">
                <tr>
                    <td style="width:180px">注：白联为仓库联 </td>
                    <td style="width:180px">红联为采购联</td>
                    <td style="width:180px">黄联为财务联</td>
                </tr>
            </table>
        </div>
        <el-row :gutter="20" style="margin-top:50px;">
            <el-col :span="2" :offset="22">
                <div class="grid-content bg-purple">
                    <el-button size="mini" type="primary" @click="printPage">打印</el-button>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    var numeral = require('numeral')
    import moment from "moment";
    import storage from 'good-storage'
    import { pageStorage } from '@/util/pageStorage.js'

    export default {
        name: "DeliveryOrderPrintPreviewSJ",
        data() {
            return {
                props: {
                    inspector: {
                        inspector: ''
                    },
                    logCompany: {
                        logCompany: ''
                    },
                    project: {
                        projectName: '',
                        workPlace: '',
                        customer: '',
                    }
                },
                pageTotalQuantity:0,
                totalQuantity:0,
                pageTotalUnivalence:0,
                allTotalUnivalence:0,
                pageTotalUnivalenceNo:0,
                allTotalUnivalenceNo:0,
                data: [],
                company:this.$store.state.user.org.orgName,
                title:'材料（配件）入库单',
                pageStorage:pageStorage('info')
            }
        },
        // mounted() {
        //     this.list()
        //     this.foobar('printHtml');
        //     if(this.$store.state.user.org.orgName=='杭州宝业建筑工业化制造有限公司'){
        //         this.title= '杭州宝业建筑工业化制造有限公司（PC工厂）原材料入库单';
        //     }
        // },
        activated() {
            this.init()
            this.foobar('printHtml');
            if(this.$store.state.user.org.orgName=='杭州宝业建筑工业化制造有限公司'){
                this.title= '杭州宝业建筑工业化制造有限公司（PC工厂）原材料入库单';
            }
        },
        methods: {
            init(){
                this.pageStorage().then(data=>{
                  this.showData(data).then(_=>{
                    if(location.href.split('?')[1]==='printHtml'){
                      this.printHtml(data)
                    }
                  })
                })
            },
            showData(info) {
                return new Promise((resolve,reject)=>{
                    this.props = info;
                    this.data = info.listDoDetail;
                    
                    let num = 0;
                    let num1 = 0;
                    let num2 = 0;
                    let size = 0;
                    let size2 = 0;
                    for(let i =0;i<info.listDoDetail.length;i++){
                        if(info.listDoDetail[i].totalUnivalence && info.listDoDetail[i].totalUnivalence.indexOf('.')!=-1){
                            let size1 = info.listDoDetail[i].totalUnivalence.split('.')[1].length;
                            if(size1>size){
                                size = size1;
                            }
                        }

                        if(info.listDoDetail[i].amount && info.listDoDetail[i].amount.indexOf('.')!=-1){
                            let size3 = info.listDoDetail[i].amount.split('.')[1].length;
                            if(size3>size2){
                                size2 = size3;
                            }
                        }

                        num=parseFloat(num)+parseFloat(info.listDoDetail[i].amount==null?0:info.listDoDetail[i].amount);
                        num1=parseFloat(num1)+parseFloat((info.listDoDetail[i].totalUnivalence==null || info.listDoDetail[i].totalUnivalence=='')?'':info.listDoDetail[i].totalUnivalence);
                        num2=parseFloat(num2)+parseFloat((info.listDoDetail[i].totalUnivalenceNo==null || info.listDoDetail[i].totalUnivalenceNo=='')?'':info.listDoDetail[i].totalUnivalenceNo);

                        this.totalQuantity = info.listDoDetail[i].totalQuantity;
                        this.allTotalUnivalence = info.listDoDetail[i].allTotalUnivalence;
                        this.allTotalUnivalenceNo = info.listDoDetail[i].allTotalUnivalenceNo;
                    }
                    this.pageTotalQuantity = num.toFixed(size2);
                    this.pageTotalUnivalence = num1.toFixed(size);
                    this.pageTotalUnivalenceNo = num2.toFixed(size);

                    this.totalQuantity = parseFloat(this.totalQuantity).toFixed(size2);
                    this.allTotalUnivalence = parseFloat(this.allTotalUnivalence).toFixed(size);
                    this.allTotalUnivalenceNo = parseFloat(this.allTotalUnivalenceNo).toFixed(size);
                    

                    this.totalQuantity = this.totalQuantity == 0?"":this.totalQuantity;
                    this.allTotalUnivalence = this.allTotalUnivalence == 0 ||  Number.isNaN(this.allTotalUnivalence)?"":this.allTotalUnivalence;
                    this.pageTotalQuantity = this.pageTotalQuantity == 0?"":this.pageTotalQuantity;


                    this.pageTotalUnivalence = this.pageTotalUnivalence==0?"":Number(this.ToFixed(this.pageTotalUnivalence,3))
                    this.allTotalUnivalence = this.allTotalUnivalence==0?"":Number(this.ToFixed(this.allTotalUnivalence,3))
                    this.allTotalUnivalence = this.allTotalUnivalence == 'NaN'?"":this.allTotalUnivalence;


                    this.totalQuantityNo = this.totalQuantityNo == 0?"":this.totalQuantityNo;
                    this.allTotalUnivalenceNo = this.allTotalUnivalenceNo == 0 ||  Number.isNaN(this.allTotalUnivalenceNo)?"":this.allTotalUnivalenceNo;
                    this.pageTotalQuantityNo = this.pageTotalQuantityNo == 0?"":this.pageTotalQuantityNo;


                    this.pageTotalUnivalenceNo = this.pageTotalUnivalenceNo==0?"":Number(this.ToFixed(this.pageTotalUnivalenceNo,3))
                    this.allTotalUnivalenceNo = this.allTotalUnivalenceNo==0?"":Number(this.ToFixed(this.allTotalUnivalenceNo,3))
                    this.allTotalUnivalenceNo = this.allTotalUnivalenceNo == 'NaN'?"":this.allTotalUnivalenceNo;
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
            },
            /**
             * 防止打印时，输入框的值没有带入到打印页面
             */
            foobar(wrapperId) {
                var elms = document.getElementById(wrapperId).getElementsByTagName('input')
                let L = elms.length
                let F = function () {
                    this.setAttribute('value', this.value);
                }
                while (L--) {
                    elms[L].onchange = F;
                }
                //textarea's use innerHTML instead of value for their default in-source 'value'
                //select and radiobuttons etc might need some work to, take it from here.
            }
        },
        filters: {
            formatterDate(row) {
                var date = row;
                if (date == undefined || !date) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD");
            },
        },

    }
</script>
<style>
    /*#printHtml .el-form .el-form-item__label{*/
        /*font-family: Tahoma !important;*/
        /*color: #000 !important;}*/
</style>
<style scoped>
    #ul_color li {
        display: inline;
        list-style-type: none;
        padding: 5px 5px;
    }

    #title {
        width: auto;
        font-size: 15px;
        font-weight: 500;
        text-align: center;
        font-family: Tahoma !important;
        color: #000 !important;
    }
    #table_center tr td{
        padding-top:5px;
        padding-bottom:5px;
    }
    .table_color tr td {
        width: 100px;
    }

    .td_value {
        width: 180px;
    }

    .input_show {
        border-style: none;
    }

    table {
        font-size: 6px;
    }

    .td_font_size {
        width: 50px;
    }

    .td_font_size_90 {
        width: 90px;
    }

    .td_font_size_70 {
        width: 70px;
    }

</style>