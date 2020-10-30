<template>
    <div class="print-box">
        <div style="text-align: center" id="printHtml">
            <table border="1" cellpadding="4" cellspacing="0" width="80%" height="350px"
                   style="text-align: center;margin-left: 50px">
                <tbody id="tbody_1">
                <tr>
                    <td colspan="2">
                        <canvas id="canvas"></canvas>
                    </td>
                    <td colspan="3"><img width="150px" height="150px" :src="this.logoUrl" style="max-width:70%"></td>
                </tr>
                <tr>
                    <td rowspan="2">项目名称</td>
                    <td rowspan="2">构件名称</td>
                    <td rowspan="2">构件类型</td>
                    <td>楼栋</td>
                    <td>方量</td>
                </tr>
                <tr>
                    <td>{{productDetailInfoNew.buildCode}}</td>
                    <td>{{productDetailInfoNew.productConcrete}}</td>
                </tr>
                <tr>
                    <td rowspan="2">{{productDetailInfoNew.projectName}}</td>
                    <td rowspan="2">{{productDetailInfoNew.detailNo}}</td>
                    <td rowspan="2">{{productDetailInfoNew.productTypeName}}</td>
                    <td>楼层</td>
                    <td>重量</td>
                </tr>
                <tr>
                    <td>{{productDetailInfoNew.floorNum}}</td>
                    <td>{{productDetailInfoNew.productWt}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div id="print" style="overflow: hidden;display: none">
            <div id="printHtml1">
                <table border="1" cellpadding="0" cellspacing="0" width="100%" height="100%" class="y_class" align="center">
                    <tbody>
                    <tr>
                        <td colspan="2">
                            <canvas id="canvas1"></canvas>
                        </td>
                        <td colspan="3"><img width="70px" height="70px" :src="this.logoUrl" style="max-width:70%"></td>
                    </tr>
                    <tr>
                        <td class="x_class"
                            rowspan="2">项目名称
                        </td>
                        <td class="x_class" rowspan="2">构件名称</td>
                        <td class="x_class" rowspan="2">构件类型</td>
                        <td class="x_class">楼栋</td>
                        <td class="x_class">方量</td>
                    </tr>
                    <tr>
                        <td class="x_class_info">{{productDetailInfoNew.buildCode}}</td>
                        <td class="x_class_info"> {{productDetailInfoNew.productConcrete}}</td>

                    </tr>
                    <tr>
                        <td class="x_class_info" rowspan="2">{{productDetailInfoNew.projectName}}</td>
                        <td class="x_class_info" rowspan="2">{{productDetailInfoNew.detailNo}}</td>
                        <td class="x_class_info" rowspan="2">{{productDetailInfoNew.productTypeName}}</td>
                        <td class="x_class">楼层</td>
                        <td class="x_class">重量</td>
                    </tr>
                    <tr>
                        <td class="x_class_info">{{productDetailInfoNew.floorNum}}</td>
                        <td class="x_class_info">{{productDetailInfoNew.productWt}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div id="printHtml2">
                <table border="1" cellpadding="0" cellspacing="0" width="500px" height="300px">
                    <tbody>
                    <tr>
                        <td style="font-size: 70px;font-weight: bold;">{{productDetailInfoNew.productName}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>


        <div>
            <el-row :gutter="20">
                <el-col :span="2" :offset="22">
                    <div class="grid-content bg-purple">
                        <el-button size="mini" type="primary" @click="printHtml">打印</el-button>
                    </div>
                </el-col>
            </el-row>
        </div>
    </div>

</template>

<script>
    var QRCode = require('qrcode')
    import serverConfig from '../../../../../server.config'
    import productDetailModel from '../../../../model/project/productDetail'
    import avatar from '../../../../assets/img/logo2.jpg'
    import pcmaster_xy from '@/assets/img/pcmaster_xy.jpg'

    export default {
        props: ['productDetailInfoNew'],
        name: "ProductDetailPrintNews",
        data() {
            return {
                //productDetailInfoNew: '',
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
                productDetailModel.getProductDetailInfoById(id).then((data) => {
                    this.productDetailInfoNew = data.entity
                    // this.productDetailInfoNew.logoUrl = serverConfig.baseURL + this.productDetailInfoNew.logoUrl
                    this.info()
                    loading.close()
                })
            },
            info() {
                //获取当前链接
                let url = location.href.split("#");
                let finalUrl = url[0] + "#/product";
                //pc端弹出框显示的二维码
                QRCode.toDataURL(document.getElementById('canvas'),finalUrl+'?'+this.productDetailInfoNew.id+"-"+this.$store.state.user.tenantId+"-", {
                    errorCorrectionLevel: 'H',
                    width: '200'
                }, function (error, url) {
                    if (error) console.error(error)
                    console.log('success!');
                })
                //打印页面二维码
                QRCode.toDataURL(document.getElementById('canvas1'),finalUrl+'?'+this.productDetailInfoNew.id+"-"+this.$store.state.user.tenantId+"-", {
                    errorCorrectionLevel: 'H',
                    width: '100'
                }, function (error, url) {
                    if (error) console.error(error)
                    console.log('success!');
                })
                QRCode.toDataURL(document.getElementById('canvas2'),finalUrl+'?'+this.productDetailInfoNew.id+"-"+this.$store.state.user.tenantId+"-", {
                    errorCorrectionLevel: 'H',
                    width: '100'
                }, function (error, url) {
                    if (error) console.error(error)
                    console.log('success!');
                })


             /*   QRCode.toCanvas(document.getElementById('canvas'), this.productDetailInfoNew.id + " ", {
                    errorCorrectionLevel: 'H',
                    width: '200'
                }, function (error) {
                    if (error) console.error(error)
                    console.log('success!');
                })
                QRCode.toCanvas(document.getElementById('canvas1'), this.productDetailInfoNew.id + " ", {
                    errorCorrectionLevel: 'H',
                    width: '100'
                }, function (error) {
                    if (error) console.error(error)
                    console.log('success!');
                })
                QRCode.toCanvas(document.getElementById('canvas2'), this.productDetailInfoNew.id + " ", {
                    errorCorrectionLevel: 'H',
                    width: '100'
                }, function (error) {
                    if (error) console.error(error)
                    console.log('success!');
                })*/

            },
            printHtml() {
                //需要打印内容
                let newStr = document.getElementById("print").innerHTML;
                // newStr += document.getElementById("printHtml2").innerHTML;
                //当前页面内容
                let oldstr = document.body.innerHTML;
                //替换（隐藏不需要打印的内容）
                document.body.innerHTML = newStr;
                this.info()
                window.print();//打印
                //恢复
                document.body.innerHTML = oldstr;
                // this.$router.push({name: 'platePlanProductDetails'})
                window.location.reload();
            }
        }
    }
</script>

<style scoped>
    /* @page{
      size:500px 600px;
    } */
    /**
      x轴翻转
     */
    .x_class {
        font-size: 3px;
        -moz-transform: scaleX(-1);
        -webkit-transform: scaleX(-1);
        -o-transform: scaleX(-1);
        transform: scaleX(-1);
    }

    /**
     y轴翻转
     */
    .y_class {
        text-align: center;
        -moz-transform: scaleY(-1);
        -webkit-transform: scaleY(-1);
        -o-transform: scaleY(-1);
        transform: scaleY(-1);
    }

    .x_class_info {
        -moz-transform: scaleX(-1);
        -webkit-transform: scaleX(-1);
        -o-transform: scaleX(-1);
        transform: scaleX(-1);
    }
    /* #printHtml1{
      top:150px;
    }
    #printHtml2{
      bottom:150px;
    } */
    #printHtml1, #printHtml2{
      width: 100mm;
      height:60mm;
      position:absolute;
      left:50%;
      margin-left:-50mm;
      text-align:center;
    }

</style>