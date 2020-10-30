<template>
    <div>
        <div id="printHtml">
            <div id='title'>销售出库单</div>
            <!-- <div id="nav_one">
                 <ul id="ul_one">
                     <li>公司名称:</li>
                     <li class="li_value">三杰建筑工业化有限公司</li>
                     <li>出库日期:</li>
                     <li class="li_value"><input type="text" style="border-style:none" name="invoice_date"
                                                 :value="this.props.invoiceDate | formatterDate"/></li>
                     <li>出库单号:</li>
                     <li class="li_value">{{ this.props.deliverGoodsNo }}</li>
                 </ul>
                 <ul>
                     <li>送达方:</li>
                     <li class="li_value">{{ this.props.project.customer }}</li>
                     <li>收货地址:</li>
                     <li class="li_value"><input style="border-style:none" type="text"/></li>
                     <li>收货联系人:</li>
                     <li class="li_value"><input style="border-style:none" type="text"/></li>
                 </ul>
                 <ul>
                     <li>项目名称:</li>
                     <li class="li_project_name">{{this.props.project.projectName}}</li>
                     <li class="li_bz">备注:<input type="text" style="border-style:none"></li>
                     <li>仓库:成品仓库</li>
                     <li>车号:</li>
                     <li class="li_value">{{this.props.licenseNo}}</li>
                 </ul>
             </div>-->

            <table id="table_center" cellpadding="4" cellspacing="0" width="100%"
                   style="text-align: left;width: 100%;table-layout:fixed">
                <tr>
                    <td>
                        公司名称:
                    </td>
                    <td class="td_value">
                        <span>三杰建筑工业化有限公司</span>
                    </td>
                    <td>出库日期:</td>
                    <td colspan="3" width="210px">
                        <span>{{this.props.invoiceDate | formatterDate}}</span><!--<el-input type="text" name="invoice_date"
                                      :value="this.props.invoiceDate | formatterDate"
                                      style="text-align: center;table-layout:fixed"/>-->
                    </td>
                    <td>出库单号:</td>
                    <td style="word-break:break-all;"><span>{{ this.props.deliverGoodsNo }}</span></td>
                </tr>
                <tr>
                    <td>送达方:</td>
                    <td class="td_value"><span>{{ this.props.project&&this.props.project.customer }}</span></td>
                    <td>收货地址:</td>
                    <td colspan="3">
                        <input class="input_show contenteditable" type="text" style="width:100%;height:100%;">
                    </td>
                    <td>收货联系人:</td>
                    <td>
                        <input class="input_show contenteditable" type="text" style="width:100%;height:100%;">
                    </td>
                </tr>
                <tr>
                    <td>项目名称:</td>
                    <td class="td_value">{{ this.props.project&&this.props.project.projectName}}</td>
                    <td>备注:</td>
                    <td class="td_value">
                        <input class="input_show" type="text" style="width:100%;height:100%;">
                    </td>
                    <td>仓库:</td>
                    <td><span>成品仓库</span></td>
                    <td>车号:</td>
                    <td><span>{{this.props.licenseNo}}</span></td>
                </tr>
            </table>
            <table border="1" cellpadding="4" cellspacing="0" width="100%"
                   style="text-align: center;width: 100%;table-layout:fixed">
                <tr>
                    <td>序号</td>
                    <td>存货名称</td>
                    <td>存货编码</td>
                    <td>规格型号</td>
                    <td>单位</td>
                    <td>数量</td>
                    <td>方量(m³)</td>
                    <td>备注</td>
                </tr>
                <tr v-for="item in data" :key="item.id">
                    <td>1</td>
                    <td>{{item.productDetail.product.productName}}</td>
                    <td>{{item.productDetail.product.productNo}}</td>
                    <td></td>
                    <td></td>
                    <td>1</td>
                    <td>{{item.productDetail.product.productVol}}</td>
                    <td></td>
                </tr>
                <tr>
                    <td>小计</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>{{this.productCount}}</td>
                    <td>{{ this.productVol }}</td>
                    <td></td>
                </tr>
                <tr>
                    <td>合计</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>{{this.zsprintObj.productCount}}</td>
                    <td>{{ this.zsprintObj.productVol  }}</td>
                    <td></td>
                </tr>
            </table>
            <el-form :inline="true" style="text-align: center;margin-left: -30px">

                <el-form-item label="审核人:">
                    <input class="input_show contenteditable" size="mini" type="text" style="width:80px">
                </el-form-item>


                <el-form-item label="验货人:">
                    <input class="input_show contenteditable" size="mini" type="text" style="width:80px">
                </el-form-item>

                <el-form-item label="送货人:">
                    <input class="input_show contenteditable" size="mini" type="text" style="width:80px">
                </el-form-item>


                <el-form-item label="收货人:">
                    <input class="input_show contenteditable" size="mini" style="width:80px" type="text">
                </el-form-item>


                <el-form-item label="制单人:">
                    <span>{{this.props.createdByName}}</span>
                </el-form-item>

            </el-form>
            <table id="table_color" style="margin-left: 50px;margin-top: -20px">
                <tr>
                    <td>白色: 存根联</td>
                    <td>绿色: 财务联</td>
                    <td>蓝色: 客户联</td>
                    <td>红色: 验收联</td>
                    <td>黄色: 运费结算</td>
                </tr>
            </table>
            <!--            <div style="display:flex;justify-content:space-between;padding-right: 0px 10px;">
                            <span>白色 存根联</span>
                            <span>绿色 财务联</span>
                            <span>蓝色 客户联</span>
                            <span>红色 验收联</span>
                            <span>黄色 运费结算</span>
                        </div>-->
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
    import model from '../../../../model/model'
    import planModel from '../../../../model/plan/plan'
    import moment from "moment";
    import storage from 'good-storage'
    import { pageStorage } from '@/util/pageStorage.js'

    export default {
        name: "InvoicePrintingSj",
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
                zsprintObj: {
                    products: '',
                    productCount: 0,
                    productConcrete: 0
                },
                productCount:0,
                productVol:0,
                data: [],
                pageStorage:pageStorage('obj')
            }
        },
        // mounted() {
        //     this.list()
        //     this.foobar('printHtml');
        // },
        activated() {
            this.init()
            this.foobar('printHtml');
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
            showData(obj) {
                let id = obj.id;
                // if (id === undefined || id == null) {
                //     this.$router.push({name: 'invoiceNewList'})
                //     return
                // }
                return model.findByIdForPrint(id).then(data => {
                    this.props = data.entity;
                    //根据invoiceId统计打印单需要的物件数、装车方量、构件信息
                    model.findByInvoiceIdForZSPrint(id).then(data => {
                        this.zsprintObj = data.entity;
                        // this.zsprintObj.productVol = this.numeralS(this.zsprintObj.productVol)
                    })
                    planModel.findByIdIn(obj.invoiceDetailIds).then((data)=>{
                        this.data = data.entity;
                        let num= 0;

                        for(let i =0;i<data.entity.length;i++){
                            num=parseFloat(num)+parseFloat(data.entity[i].productDetail.product.productVol==null?0:data.entity[i].productDetail.product.productVol);
                        }
                        this.productVol = num;
                        this.productCount = data.entity.length;

                        this.productVol = this.numeralS(this.productVol);
                    })
                    /* model.findDetailsByInvoiceId(id).then(data => {
                        this.data = data.entity;
                    })*/  
                    // 清除所有手动填写的格子
                    document.querySelectorAll('.contenteditable').forEach(option=>{
                        option.innerHTML = ""
                        option.value = ""
                    })
                })
                
            },
            numeralS(num) {
                return numeral(num).format('0.000')
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
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD");
            },
        },

    }
</script>

<style scoped>
    #ul_color li {
        display: inline;
        list-style-type: none;
        padding: 5px 5px;
    }

    #title {
        width: auto;
        font-size: 25px;
        font-weight: 500;
        text-align: center;
    }

    #table_color tr td {
        width: 100px;
    }

    .td_value {
        width: 180px;
    }

    .input_show {
        border-style:none;
    }


</style>