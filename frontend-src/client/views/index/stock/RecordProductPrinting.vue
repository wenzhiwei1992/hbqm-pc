<template>
    <div>
        <div id="printHtml" style="color: #000 !important;font-family: Tahoma !important;">
            <div class="print-title">{{this.$store.state.user.org.orgName}}</div>
            <table border="1" cellpadding="4" cellspacing="0" width="100%"  bordercolor="#000000"
                   style="text-align: center;margin-top: 10px;width: 100%;table-layout:fixed;font-size: 8px;color: #000 !important;font-family: Tahoma !important;">
                <tr>
                    <td colspan="22">成品入库单</td>
                </tr>
                <tr>
                    <td colspan="2">项目</td>
                    <td colspan="14">{{this.projectName}}</td>
                    <td colspan="2">日期</td>
                    <td colspan="4">{{this.recordDate | formatterDate}}</td>
                </tr>
                <tr>
                    <td colspan="2">序号</td>
                    <td colspan="3">构件编号</td>
                    <td colspan="2">构件名称</td>
                    <td colspan="2">楼栋楼层</td>
                    <td colspan="2">构件类型</td>
                    <td colspan="1">数量</td>
                    <td colspan="2">库位号</td>
                    <td colspan="2">重量</td>
                    <td colspan="2">面积/体积</td>
                    <td colspan="2">混凝土等级</td>
                    <td colspan="2">备注</td>
                </tr>
                <tr v-for="(item,index) in data" :key="index">
                    <td colspan="2">{{index+1}}</td>
                    <td colspan="3">{{item.productNo}}</td>
                    <td colspan="2">{{item.productName}}</td>
                    <td colspan="2">{{`${item.buildCode}${item.floorNum}F`}}</td>
                    <td colspan="2">{{item.name}}</td>
                    <!-- <td colspan="4">{{item.inBoundOrder?item.inBoundOrder:""}}</td> -->
                    <td colspan="1">1</td>
                    <td colspan="2">{{item.shelfNo?item.shelfNo:""}}</td>
                    <td colspan="2">{{item.productWt}}</td>
                    <td colspan="2">{{item.productVol}}m³</td>
                    <td colspan="2">{{item.productGrade}}</td>
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td colspan="2">合计</td>
                    <td colspan="3"></td>
                    <td colspan="2"></td>
                    <td colspan="2"></td>
                    <td colspan="2"></td>
                    <td colspan="1">{{data.length}}</td>
                    <td colspan="2"></td>
                    <td colspan="2">{{productWts}}</td>
                    <td colspan="2">{{productVols}}m³</td>
                    <td colspan="2"></td>
                    <td colspan="2"></td>
                </tr>
                <tr>
                    <td colspan="2">生产班组:</td>
                    <td colspan="3"></td>
                    <td colspan="2">质检员:</td>
                    <td colspan="4"></td>
                    <td colspan="1"></td>
                    <td colspan="2">入库人员:</td>
                    <td colspan="2"></td>
                    <td colspan="2">审核人:</td>
                    <td colspan="2"></td>
                    <td colspan="2"></td>
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
        data() {
            return {
                projectName:"",
                recordDate:"",
                data: [],
                company:this.$store.state.user.org.orgName,
                title:'成品入库单',
                nums:'合计数量',
                productWts:'合计重量',
                productVols:'合计方量'
            }
        },
        mounted() {
            this.list()
            this.foobar('printHtml');
        },
        methods: {
            list() {
                let info = this.$route.params.info;
                let projectName = this.$route.params.projectName;
                let recordDate = this.$route.params.recordDate;
                log(this.$route.params)
                if (info === undefined || info == null) {
                    this.$router.push({name: 'RecordProductPrinting'})
                    return
                }
                this.projectName = projectName;
                this.recordDate = recordDate;
                this.data = info;
                this.productWts = this.FixedSummation(info.map(val=>val.productWt))
                this.productVols = this.FixedSummation(info.map(val=>val.productVol))
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
    .print-title{
      text-align: center;
      font-size:20px;
    }
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