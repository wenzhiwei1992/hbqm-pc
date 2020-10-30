<template>
    <div>
        <div id="printHtml"  style="color: #000 !important;font-family: Tahoma !important;">
            <div id='title'>{{this.title}}</div>

            <table id="table_center" cellpadding="0" cellspacing="0" width="100%"
                   style="text-align: left;width: 100%;table-layout:fixed;font-size: 6px;margin-top: 20px;color: #000 !important;font-family: Tahoma !important;">
                <tr>
                    <td>
                        公司名称:
                    </td>
                    <td class="td_value">
                        <span>{{ this.company }}</span>
                    </td>
                    <td>出库日期:</td>
                    <td colspan="2">
                        <span>{{this.props.deliveryOrderTime | formatterDate}}</span><!--<el-input type="text" name="invoice_date"
                                      :value="this.props.invoiceDate | formatterDate"
                                      style="text-align: center;table-layout:fixed"/>-->
                    </td>
                    <td width="80px">出库单号:</td>
                    <td style="word-break:normal;">{{ this.props.deliveryOrderNo }}</td>
                </tr>
                <tr>
                    <td>项目名称:</td>
                    <td class="td_value"><span>{{this.props.projectName}}</span></td>
                    <td></td>
                    <td colspan="2">
                        <input class="input_show" type="text" style="width:100%;height:100%;">
                    </td>
                    <td>仓 库:</td>
                    <td class="td_value"><span>{{this.props.stockName}}</span></td>
                    <td>
                        <input class="input_show" type="text" style="width:100%;height:100%;">
                    </td>
                </tr>
                <tr>
                    <td>部门:</td>
                    <td class="td_value">{{this.props.deptName}}</td>
                    <td>业务类型:</td>
                    <td colspan="2" class="td_value">领料</td>
                    <td>备 注:</td>
                    <td class="td_value"><span></span></td>
                </tr>
            </table>
            <table border="1" cellpadding="4" cellspacing="0" width="100%" bordercolor="#000000"
                   style="text-align: center;margin-top: 10px;width: 90%;table-layout:fixed;font-size: 8px;color: #000 !important;font-family: Tahoma !important;">
                <tr>
                    <td style="width: 45px">序号</td>
                    <td class="td_font_size_120">材料编码</td>
                    <td class="td_font_size_120">材料名称</td>
                    <td class="td_font_size_120">规格型号</td>
                    <td class="td_font_size_70">数量</td>
                    <td class="td_font_size">单位</td>
                    <td class="td_font_size_70">单价</td>
                    <td class="td_font_size_70">金额</td>
                    <td class="td_font_size_90">备注</td>
                </tr>
                <tr v-for="(item,index) in data" :key="item.index">
                    <td style="width: 45px">{{index+1}}</td>
                    <td>{{item.materialStorageId}}</td>
                    <td>{{item.materialName}}</td>
                    <td class="td_font_size_120">{{item.materialSpecification}}</td>
                    <td class="td_font_size">{{item.quantity}}</td>
                    <td class="td_font_size">{{item.materialUnit}}</td>
                    <td class="td_font_size_70"></td>
                    <td class="td_font_size_70"></td>
                    <td class="td_fond_size_90"></td>
                </tr>
                <tr>
                    <td>小计</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>{{this.pageTotalQuantity}}</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>合计</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>{{this.totalQuantity}}</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
            <el-form :inline="true" v-if="this.$store.state.user.org.orgName!='杭州宝业建筑工业化制造有限公司'"  style="font-size: 12px;position: fixed;bottom: 10px;color: #000 !important;font-family: Tahoma !important;">

                <el-form-item style="width:180px;">
                    <span>审核人: {{this.props.ccheck}}</span>
                </el-form-item>

                <el-form-item style="width:180px">
                    <span>制单人:  {{this.props.createdByName}}</span>
                </el-form-item>

                <el-form-item style="width:180px">
                    <span>领料人:  </span>
                    <!--<span>领料人:  {{this.props.proposer}}</span>-->
                </el-form-item>

            </el-form>
            <table class="table_color" v-if="this.$store.state.user.org.orgName!='杭州宝业建筑工业化制造有限公司'"  style="margin-left: 50px;margin-top: -20px;position: fixed;bottom: 10px;color: #000 !important;font-family: Tahoma !important;">
                <tr>
                    <td>注：</td>
                    <td>白联: 仓库</td>
                    <td>绿联: 财务</td>
                    <td>蓝联: 领料部门</td>
                </tr>
            </table>


            <el-form :inline="true" v-if="this.$store.state.user.org.orgName=='杭州宝业建筑工业化制造有限公司'" style="font-size: 12px;position: fixed;bottom: 20px;color: #000 !important;font-family: Tahoma !important;">
                <el-form-item style="width:40px">
                </el-form-item>

                <el-form-item style="width:170px">
                    <span>制单人:  {{this.props.createdByName}}</span>
                </el-form-item>

                <el-form-item style="width:170px;">
                    <span>收货人:  </span>
                </el-form-item>

                <el-form-item style="width:170px">
                    <span>仓管:  </span>
                    <!--<span>经办人:  {{this.props.createdByName}}</span>-->
                </el-form-item>

            </el-form>

            <table class="table_color" v-if="this.$store.state.user.org.orgName=='杭州宝业建筑工业化制造有限公司'" style="margin-left: 50px;margin-top: -20px;position: fixed;bottom: 10px;color: #000 !important;font-family: Tahoma !important;">
                <tr>
                    <td style="width:180px">注：白联为仓库联 </td>
                    <td style="width:180px">红联为领用部门</td>
                    <td style="width:180px">黄联为财务联</td>
                </tr>
            </table>
        </div>
        <el-row :gutter="20" style="margin-top:50px;">
            <el-col :span="2" :offset="22">
                <div class="grid-content bg-purple">
                    <el-button size="mini" type="primary" @click="printHtml">打印</el-button>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    var numeral = require('numeral')
    import moment from "moment";

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
                data: [],
                company:this.$store.state.user.org.orgName,
                title:'材料（配件）出库单'
            }
        },
        mounted() {
            this.list()
            this.foobar('printHtml');
            if(this.$store.state.user.org.orgName=='杭州宝业建筑工业化制造有限公司'){
                this.title= '杭州宝业建筑工业化制造有限公司（PC工厂）原材料·出库单';
            }
        },
        methods: {
            list() {
                let info = this.$route.params.info;
                if (info === undefined || info == null) {
                    this.$router.push({name: 'DeliveryOrderPrintPreviewSJ'})
                    return
                }

                this.props = info;
                this.data = info.listDoDetail;
                log(this.data)
                let num = 0;
                let size = 0;
                for(let i =0;i<info.listDoDetail.length;i++){
                    if(info.listDoDetail[i].quantity.indexOf('.')!=-1){
                        let size1 = info.listDoDetail[i].quantity.split('.')[1].length;
                        if(size1>size){
                            size = size1;
                        }
                    }

                    num=parseFloat(num)+parseFloat(info.listDoDetail[i].quantity==null?0:info.listDoDetail[i].quantity);
                    this.totalQuantity = info.listDoDetail[i].totalQuantity;
                }

                this.pageTotalQuantity = num.toFixed(size);
                this.totalQuantity = parseFloat(this.totalQuantity).toFixed(size);
            },
            numeralS(num) {
                return numeral(num).format('0.0000')
            },
            printHtml() {
                //  this.changeInput()
                let codestr = window.document.getElementById("printHtml").innerHTML;
                window.document.body.innerHTML = codestr;
                window.print();   //打印当前窗口

                window.location.reload();

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
        font-size: 15px;
        font-weight: 500;
        text-align: center;
        font-family: Tahoma !important;
        color: #000 !important;
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

    .td_font_size_120 {
        width: 120px;
    }


</style>