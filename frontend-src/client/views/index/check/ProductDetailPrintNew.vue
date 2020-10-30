<template>
    <div>
        <div id="allTitle">
            <div id="title">
                宝业建筑工业化有限公司
            </div>
            <div id="invoice">
                <span style="font-size: 20px;border-bottom: 2px solid black">蒙城宝业建筑工业化有限公司</span>
            </div>
            <div style="width: 80%">
                <div>
                    <span></span>
                </div>
            </div>
        </div>
        <div style="text-align: center">
            <table border="1" cellpadding="4" cellspacing="0" width="60%"
                   style="text-align: center;position: absolute;right: 20%"><!--table-layout:fixed-->
                <tbody id="tbody_1">
                <tr>
                    <td colspan="2"><img width="150px" height="80px" :src="this.logoUrl" style="max-width:70%"></td>
                    <td rowspan="12">
                        <canvas id="canvas2"></canvas>
                    </td>
                </tr>
                <tr>
                    <td>产品编号:</td>
                    <td><span>{{ this.productDetailInfoNew.id }}</span></td>

                </tr>
                <tr>
                    <td>产品净重:</td>
                    <td><span>{{ this.productDetailInfoNew.productWt }}</span></td>

                </tr>
                <tr>
                    <td>生产日期:</td>
                    <td><span>{{ this.productDetailInfoNew.overTime }}</span></td>

                </tr>
                <tr>
                    <td rowspan="8" style="line-height: 100px">生产质检合格章:</td>
                    <td rowspan="8"></td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="foot" style="position:absolute;right: 21%;top: 50%;">
            <div><span>{{ this.productDetailInfoNew.idCode }}</span></div>
        </div>

    </div>
</template>

<script>
    var QRCode = require('qrcode')
    import avatar from '../../../assets/img/logo.png'
    import productDetailModel from '../../../model/project/productDetail'
    import serverConfig from '../../../../server.config'

    export default {
        name: "ProductDetailPrintNew",
        // props:['productDetailInfoNew'],
        data() {
            return {
                productDetailInfoNew: '',
                logoUrl: avatar,
            }
        },
        mounted() {
            this.show()

        },
        methods: {
            showQR() {
                QRCode.toDataURL(document.getElementById('canvas2'),
                    "项目名称：[" +
                    this.productDetailInfoNew.projectName + "]"+"\n"+
                    "楼号：[" +
                    this.productDetailInfoNew.buildCode + "]" +"\n"+
                    "层次：[" +
                    this.productDetailInfoNew.floorNum + "]" +"\n"+
                    "产品编码：[" +
                    this.productDetailInfoNew.idCode + "]"+"\n"+
                    "产品名称：[" +
                    this.productDetailInfoNew.productName +"]"+"\n"+
                    "尺寸：[" +
                    this.productDetailInfoNew.productLen + "*"+
                    this.productDetailInfoNew.productHeight+"*"+
                    this.productDetailInfoNew.productThick+"]"+"\n"+
                    "重量["+
                    this.productDetailInfoNew.productWt+"t ]"+"", {
                    errorCorrectionLevel: 'H',
                    width: '200'
                }, function (error, url) {
                    if (error) console.error(error)
                    console.log('success!');
                })

                // QRCode.toDataURL(document.getElementById('canvas2'),'http://192.168.0.10:8000/#/product?'+this.productDetailInfoNew.id+" ", {
                //     errorCorrectionLevel: 'H',
                //     width: '200'
                // }, function (error,url) {
                //     if (error) console.error(error)
                //     console.log('success!');
                // })
                // this.productDetailId = this.productDetailInfo.id
            },
            show() {
                let id = this.$route.params.id;
                if (id == undefined || id == null) {
                    this.$router.push({name: 'platePlanProductDetails'})
                    return
                }
                const loading = this.$loading({
                    text: '加载中',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                })
                productDetailModel.getProductDetailInfoByIdNew(id).then((data) => {
                    this.productDetailInfoNew = data.entity
                    this.productDetailInfoNew.logoUrl = serverConfig.baseURL + this.productDetailInfoNew.logoUrl
                    this.showQR()
                    loading.close()
                })
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
</style>