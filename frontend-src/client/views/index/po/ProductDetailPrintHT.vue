<template>
    <div>
        <div id="printHtml">
            <div>
                <table class="table_class" cellspacing="0" cellpadding="0"
                       style="margin:0 auto;width: 200px;height: 375px">
                    <tr>
                        <td>
                            <img width="100px" height="80px" :src="this.logoUrl" style="max-width:70%">
                        </td>
                        <td>
                            <canvas id="canvas"></canvas>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <span>湖北宝业建筑工业化有限公司</span>
                        </td>
                    </tr>
                    <tr>
                        <td>项目名称:</td>
                        <td>{{productDetailPrintHTInfo.projectName}}</td>
                    </tr>
                    <tr>
                        <td>构件名称:</td>
                        <td>{{productDetailPrintHTInfo.productName}}</td>
                    </tr>
                    <tr>
                        <td>楼栋楼层:</td>
                        <td><span>{{productDetailPrintHTInfo.buildCode}}</span><span>{{productDetailPrintHTInfo.floorNum}}</span>
                        </td>
                    </tr>
                    <tr>
                        <td>构件序号</td>
                        <td>{{productDetailPrintHTInfo.detailNo}}</td>
                    </tr>

                    <tr>
                        <td>构件重量</td>
                        <td>{{productDetailPrintHTInfo.productWt}}</td>
                    </tr>
                    <tr>
                        <td>项目地点</td>
                        <td>{{productDetailPrintHTInfo.workPlace}}</td>
                    </tr>
                    <tr>
                        <td>尺寸(长*宽*高)</td>
                        <td>
                            {{productDetailPrintHTInfo.productLen}}*{{productDetailPrintHTInfo.productThick}}*{{productDetailPrintHTInfo.productHeight}}
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div>
            <el-button type="primary" id="print1" @click="printPage">打印</el-button>
        </div>
    </div>
</template>

<script>
    var QRCode = require('qrcode')
    import avatar from '../../../assets/img/logo2.jpg'
    import pcmaster_xy from '@/assets/img/pcmaster_xy.jpg'
    import productDetailModel from '../../../model/project/productDetail'


    export default {
        name: "ProductDetailPrintHT",
        props: ['productDetailPrintHTInfo'],
        data() {
            return {
                logoUrl: avatar,
            }
        },
        mounted() {
            this.info()
            if(this.$store.state.user.tenantId === 'pcmaster_xy'){
              this.logoUrl = pcmaster_xy
            }
        },
        methods: {
            info() {
                //获取当前链接
                let url = location.href.split("#");
                let finalUrl = url[0] + "#/product";
                QRCode.toDataURL(document.getElementById('canvas'), finalUrl + '?' + this.productDetailPrintHTInfo.id + "-" + this.$store.state.user.tenantId + "-", {
                    errorCorrectionLevel: 'H',
                    width: '100'
                }, function (error, url) {
                    if (error) console.error(error)
                    console.log('success!');
                })
            },
            printPage() {
                //根据ID累计打印次数
                let id = this.productDetailPrintHTInfo.id;
                productDetailModel.printCountIncrease(id).then(data => {
                    console.log("data.status", data.entity)
                }).catch(err => {
                    console.error(err)
                })
                //需要打印内容
                let newStr = document.getElementById("printHtml").innerHTML;
                //当前页面内容
                let oldstr = document.body.innerHTML;
                //替换（隐藏不需要打印的内容）
                document.body.innerHTML = newStr;
                this.info()
                window.print();//打印
                //恢复
                document.body.innerHTML = oldstr;
                window.location.reload();
                return false;
            },

        }
    }
</script>

<style scoped>
    .table_class td {
        font-size: 12px;
    }

    .table_class td:first-child {
        font-weight: bold;
        width: 90px;
    }
    .table_class td:last-child {
         width: 110px;
    }


</style>