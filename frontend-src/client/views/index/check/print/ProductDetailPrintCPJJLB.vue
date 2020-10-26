<template>
    <div>
        <el-row :gutter="20">
            <el-col
                    :span="2"
                    :offset="22"
            >
                <div class="btn-print grid-content bg-purple">
                    <el-button
                            size="mini"
                            type="primary"
                            @click="printPage"
                    >打印
                    </el-button>
                </div>
            </el-col>
        </el-row>
        <div id="printHtml-wrap" v-loading="loading">
            <div class="printHtml" v-for="(item,index) in tableData" :key="index">
                <div class="head">
                    <div class="title">PC构件成品检验记录表</div>
                    <div class="date">检查日期：_____年__月__日</div>
                </div>
                <div
                        class="table"
                >
                    <table
                            width="100%"
                            border="0"
                            cellpadding="0"
                            cellspacing="0"
                            class="left"
                            style="table-layout:fixed;"
                    >
                        <tbody>
                        <tr>
                            <td colspan="2">
                                <div class="td-box">项目名称</div>
                            </td>
                            <td colspan="8">
                                <div class="td-box">{{item.project.projectName}}</div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <div class="td-box">构件编号</div>
                            </td>
                            <td colspan="4">
                                <div class="td-box">{{item.product.productNo}}</div>
                            </td>
                            <td colspan="1">
                                <div class="td-box">订单编号</div>
                            </td>
                            <td colspan="3">
                                <div class="td-box">{{item.order.orderNo}}</div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <div class="td-box">检测依据</div>
                            </td>
                            <td colspan="3">
                                <div class="td-box">GB/T 51231-2016</div>
                            </td>
                            <td colspan="1">
                                <div class="td-box">图号/版次</div>
                            </td>
                            <td colspan="1">
                                <div class="td-box"></div>
                            </td>
                            <td colspan="1">
                                <div class="td-box">生产线</div>
                            </td>
                            <td colspan="2">
                                <div class="td-box">
                                    <ul class="list">
                                        <li>国产</li>
                                        <li>固定</li>
                                        <li>进口</li>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" rowspan="2">
                                <div class="td-box">构件信息</div>
                            </td>
                            <td colspan="1">
                                <div class="td-box">混凝土强度</div>
                            </td>
                            <td colspan="2">
                                <div class="td-box">长（m）</div>
                            </td>
                            <td colspan="1">
                                <div class="td-box">宽（m）</div>
                            </td>
                            <td colspan="1">
                                <div class="td-box">厚（m）</div>
                            </td>
                            <td colspan="1">
                                <div class="td-box">体积(m3)</div>
                            </td>
                            <td colspan="2">
                                <div class="td-box">重量（t）</div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="1">
                                <div class="td-box">{{item.product.productGrade}}</div>
                            </td>
                            <td colspan="2">
                                <div class="td-box">{{item.product.productLen}}</div>
                            </td>
                            <td colspan="1">
                                <div class="td-box">{{item.product.productHeight}}</div>
                            </td>
                            <td colspan="1">
                                <div class="td-box">{{item.product.productThick}}</div>
                            </td>
                            <td colspan="1">
                                <div class="td-box">{{item.product.productVol}}</div>
                            </td>
                            <td colspan="2">
                                <div class="td-box">{{wtFormat(item.product.productWt)}}</div>
                            </td>
                        </tr>
                        <tr v-if="item.checkForPrintVos.length">
                            <td colspan="1" :rowspan="item.checkForPrintVos.length+1">
                                <div class="td-box">成<br>品<br>检<br>验</div>
                            </td>
                            <td colspan="3">
                                <div class="td-box">名称</div>
                            </td>
                            <td colspan="2">
                                <div class="td-box">允许偏差(mm)</div>
                            </td>
                            <td colspan="1">
                                <div class="td-box">实测数据</div>
                            </td>
                            <td colspan="1">
                                <div class="td-box">是否合格</div>
                            </td>
                            <td colspan="1" rowspan="1">
                                <div class="td-box">检验人</div>
                            </td>
                            <td colspan="1" rowspan="1">
                                <div class="td-box">评定意见</div>
                            </td>
                        </tr>
                        <tr v-for="(page,pageIndex) in item.checkForPrintVos" :key="pageIndex">
                            <td colspan="3" rowspan="1">
                                <div class="td-box">{{page.name}}</div>
                            </td>
                            <td colspan="2" rowspan="1">
                                <div class="td-box">{{page.deviation}}</div>
                            </td>
                            <td colspan="1" rowspan="1">
                                <div class="td-box"></div>
                            </td>
                            <td colspan="1" rowspan="1">
                                <div class="td-box" v-if="page.result==='合格'">√</div>
                                <div class="td-box" v-if="page.result==='不合格'">×</div>
                            </td>
                            <td v-if="!pageIndex" colspan="1" :rowspan="item.checkForPrintVos.length">
                                <div class="td-box">{{item.checkPerson}}</div>
                            </td>
                            <td v-if="!pageIndex" colspan="1" :rowspan="item.checkForPrintVos.length">
                                <div class="td-box"></div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="foot">
                    <div>注：合格（√），不合格（×），不检项用“/”注明，不合格项应进行整改，并备注是否整改完成。</div>
                    <!-- <ul>
                      <li>1、检验合格可打“√”表示，有检测数据直接填写。</li>
                      <li>2、表中所述要求可能不尽全面，实际还应包含具体的相关标准、工艺要求。</li>
                      <li>3、本记录表由质检部保存。</li>
                    </ul> -->
                </div>
            </div>
        </div>
    </div>
</template>


<script>
    export default {
        data() {
            return {
                loading: false,
                tableData: [
                    {
                        project: {},
                        product: {},
                        order: {},
                        checkForPrintVos: []
                    }
                ]
            }
        },
        activated() {
            if (this.$route.params.array) {
                window.localStorage.ProductDetailPrintCPJJLB_array = JSON.stringify(this.$route.params.array)
            }
            if (window.localStorage.ProductDetailPrintCPJJLB_array) {
                this.tableData = JSON.parse(window.localStorage.ProductDetailPrintCPJJLB_array)
            } else {
                this.$router.push({name: 'productDetailList'})
                return
            }
            // 新窗口执行打印
            if(location.href.split('?')[1]==='printHtml'){
                // 因为直接打印内容有漏,所以让dom加载完后执行打印
                this.$nextTick(_=>{
                    this.printHtml()
                })
            }
        },
        methods: {
            printPage() {
                window.open(location.href+'?printHtml')
            },
            printHtml(){
                let codestr = window.document.getElementById("printHtml-wrap").innerHTML;
                window.document.body.innerHTML = codestr;
                window.print();//打印

                window.opener=null;
                window.open('','_self');
                window.close();
            },
            wtFormat(cellValue) {
                if (cellValue) {
                    return require('numeral')(cellValue).format('0.000')
                } else {
                    return cellValue
                }
            },
        }
    }
</script>
<style lang="scss" scoped>
    $head-height: 12mm;
    $foot-height: 12mm;
    $a4-height: 297mm;
    $a4-width: 210mm;
    $black-border: 1px solid #000;
    @page {
        size: $a4-width $a4-height;
    }

    .btn-print {
        position: fixed;
        top: 100px;
        right: 30px;
    }

    .printHtml {
        color: #000 !important;
        width: $a4-width;
        height: $a4-height;
        box-sizing: border-box;
        position: relative;
        overflow: hidden;
        page-break-before: always;
    }

    .head {
        height: $head-height;
        font-size: 12px;

        .title {
            text-align: center;
            font-size: 25px;
        }

        .date {
        }
    }

    .table {
        width: $a4-width;
        height: $a4-height - $head-height - $foot-height -5px;

        table {
            width: 100%;
            height: 100%;
            border-collapse: collapse;
            border-spacing: 1px;
            text-align: center;

            tbody {
                border-left: $black-border;
                border-top: $black-border;

                tr {
                    height: 25px;

                    td {
                        position: relative;
                        border-right: $black-border;
                        border-bottom: $black-border;

                        &.no-wrap {
                            overflow: hidden;
                            white-space: nowrap;
                        }

                        .td-box {
                            position: absolute;
                            top: -50%;
                            left: -50%;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            width: 200%;
                            height: 200%;
                            font-size: 24px;
                            transform: scale(0.5);

                            .list {
                                display: flex;

                                li {
                                    text-indent: 1em;
                                    margin-left: 0.5em;
                                    position: relative;

                                    &::before {
                                        content: "";
                                        position: absolute;
                                        top: 0;
                                        left: -2px;
                                        width: 1em;
                                        height: 1em;
                                        border: $black-border;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    .foot {
        height: $foot-height;
        margin-top: 5px;
        display: flex;
        font-size: 12px;
        text-indent: 2em;
    }
</style>

