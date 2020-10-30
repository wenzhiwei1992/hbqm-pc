<template>
    <div id="printDiv" >
        <div id="tablePrintDiv">

        </div>
        <div id="tableDiv">

        </div>
        <div id="testDiv">

        </div>
        <el-button type="primary" id="print1" @click="printPage">打印</el-button>
        <el-button id="print2" @click="close">关闭</el-button>
    </div>
</template>

<script>
    var QRCode = require('qrcode')
    import productDetailModel from '../../../model/project/productDetail'
    import avatar from '../../../assets/img/logo2.jpg'
    import serverConfig from '../../../../server.config'
    export default {
        name: "ProductDetailPrint",
        props: ['productDetailInfo',],
        data() {
            return {
                avatar:avatar,
                newStr: '',
                productDetailId: '',
                productDetailInfoNew:'',
            }
        },
        mounted() {
            // this.init()
           // this.showPrintTable()
            this.show()

        },
        methods: {
            close() {
                this.$emit('print-cancel')
            },
            init: function () {
                let qrcodeStr =
                    '{ "ID":"' + this.productDetailInfoNew.id + '", \n' +
                    '"项目名称": "' +
                    this.productDetailInfoNew.projectName + '", \n' +
                    '"楼栋": "' +
                    this.productDetailInfoNew.buildCode + '", \n' +
                    '"层次": "' +
                    this.productDetailInfoNew.floorNum + '", \n' +
                    '"产品编码": "' +
                    this.productDetailInfoNew.idCode + '", \n' +
                    '"产品名称": "' +
                    this.productDetailInfoNew.productName + '", \n' +
                    '"尺寸": "' +
                    this.productDetailInfoNew.productLen + '*' +
                    this.productDetailInfoNew.productHeight + '*' +
                    this.productDetailInfoNew.productThick + '", \n' +
                    '"重量": "' +
                    this.productDetailInfoNew.productWt + 't" }' + '\n';

                console.log("this.productDetailInfoNew", this.productDetailInfoNew);
                // console.log(JSON.parse(qrcodeStr).ID)
                //获取当前链接
                let url = location.href.split("#");
                let finalUrl = url[0] + "#/product";
                //'http://192.168.1.162:8000/#/product?'

/*
                QRCode.toCanvas(document.getElementById('canvas'), this.productDetailInfoNew.id + " ", {
                    errorCorrectionLevel: 'H',
                    width: '100'
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
*/
                /*     QRCode.toDataURL(document.getElementById('canvas2'),this.productDetailInfoNew.id+" ", {
                         errorCorrectionLevel: 'H',
                         width: '200'
                     }, function (error,url) {
                         if (error) console.error(error)
                         console.log('success!');
                         +'?'+this.$store.state.user.tenantId
                     })*/

                //打印页面二维码
                QRCode.toDataURL(document.getElementById('canvas'),finalUrl+'?'+this.productDetailInfoNew.id+"-"+this.$store.state.user.tenantId+"-", {
                    errorCorrectionLevel: 'H',
                    width: '100'
                }, function (error, url) {
                    if (error) console.error(error)
                    console.log('success!');
                })


                QRCode.toDataURL(document.getElementById('canvas1'),finalUrl+'?'+this.productDetailInfoNew.id+"-"+this.$store.state.user.tenantId+"-", {
                    errorCorrectionLevel: 'H',
                    width: '100'
                }, function (error, url) {
                    if (error) console.error(error)
                    console.log('success!');
                })

                //pc端弹出框显示的二维码
                QRCode.toDataURL(document.getElementById('canvas2'),finalUrl+'?'+this.productDetailInfoNew.id+"-"+this.$store.state.user.tenantId+"-", {
                    errorCorrectionLevel: 'H',
                    width: '200'
                }, function (error, url) {
                    if (error) console.error(error)
                    console.log('success!');
                })
            },
            show() {
                let id = this.productDetailInfo.id;
                if (id == undefined || id == null) {
                    //this.$router.push({name: 'platePlanProductDetails'})
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
                    this.test()
                   // this.showPrintTable()
                    loading.close()
                })
            },
            test(){
                let str2 ='<table align="center" width="80%" height="350px" cellspacing="0" cellpadding="0" style="text-align: center;">';
                str2 +='<tr><td colspan="2" style="width:100px;border: 1px #D3DCE6 solid"><canvas id="canvas2"></canvas></td><td colspan="4" style="width:100px;border: 1px #D3DCE6 solid;"><img width="150px" height="150px" src =" '+this.avatar+ '  "/></td></tr>';
                str2 +='<tr>';
                str2 +='<td rowspan="2" style="border: 1px #D3DCE6 solid;font-weight: bold;" id="test">项目名称</td>';
                str2 +='<td rowspan="2" style="border: 1px #D3DCE6 solid;font-weight: bold;">构件编码</td>';
                str2 +='<td rowspan="2" style="border: 1px #D3DCE6 solid;font-weight: bold;">构件类型</td>';
                str2 +='<td rowspan="2" style="border: 1px #D3DCE6 solid;font-weight: bold;">构件名称</td>';
                str2 +='<td style="border: 1px #D3DCE6 solid;font-weight: bold;">楼栋</td>';
                str2 +='<td style="border: 1px #D3DCE6 solid;font-weight: bold;">方量(m³)</td>';
                str2 +='</tr>';
                str2 +='<tr>';
                str2 +='<td style="border: 1px #D3DCE6 solid;">' + this.productDetailInfo.buildCode + '</td>';
                str2 +='<td style="border: 1px #D3DCE6 solid;">' + this.productDetailInfo.productConcrete + '</td></tr>';
                str2 +='<tr>';
                str2 +='<td rowspan="2" style="border: 1px #D3DCE6 solid;">' + this.productDetailInfo.projectName + '</td>';
                str2 +='<td rowspan="2" style="border: 1px #D3DCE6 solid;">' + this.productDetailInfo.id + '</td>';
                str2 +='<td rowspan="2" style="border: 1px #D3DCE6 solid;">' + this.productDetailInfo.productTypeName + '</td>';
                str2 +='<td rowspan="2" style="border: 1px #D3DCE6 solid;">' + this.productDetailInfo.productName + '</td>';
                str2 +='<td style="border: 1px #D3DCE6 solid;font-weight: bold;">楼层</td>';
                str2 +='<td style="border: 1px #D3DCE6 solid;font-weight: bold;">重量(T)</td></tr>';
                str2 +='<tr><td style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);">' + this.productDetailInfo.floorNum + '</td>';
                str2 +='<td style="border: 1px #D3DCE6 solid;">' + this.productDetailInfo.productWt + '</td></tr>';
                str2 +='</table>';
                document.getElementById('testDiv').innerHTML = str2
                this.init()
            },
            showTable1() {
               /* let str = '<table cellspacing="0" cellpadding="0" class="AAA"  ><tr><td rowspan="5" style="width:100px;border: 1px #D3DCE6 solid"><canvas id="canvas"></canvas></td><td style="border: 1px #D3DCE6 solid;font-weight: bold">项目名称</td><td colspan="3" style="border: 1px #D3DCE6 solid">' + this.productDetailInfo.projectName + '</td></tr>';
                str += '<tr style="border: 1px #D3DCE6 solid"><td style="border: 1px #D3DCE6 solid;font-weight: bold">构件编码</td><td style="border: 1px #D3DCE6 solid" colspan="3">' + this.productDetailInfo.detailNo + '</td></tr>';
                str += '<tr><td style="border: 1px #D3DCE6 solid;font-weight: bold">构件类型</td><td style="border: 1px #D3DCE6 solid" colspan="3">' + this.productDetailInfo.productTypeName + '</td></tr>';
                str += '<tr><td style="border: 1px #D3DCE6 solid;font-weight: bold">楼栋</td><td style="border: 1px #D3DCE6 solid">' + this.productDetailInfo.buildCode + '</td><td style="border: 1px #D3DCE6 solid;font-weight: bold">楼层</td><td style="border: 1px #D3DCE6 solid">' + this.productDetailInfo.floorNum + '</td></tr>';
                str += '<tr style="border: 1px #D3DCE6 solid"><td style="border: 1px #D3DCE6 solid;font-weight: bold">方量(m³)</td><td style="border: 1px #D3DCE6 solid">' + this.productDetailInfo.productConcrete + '</td><td style="border: 1px #D3DCE6 solid;font-weight: bold">重量(T)</td><td style="border: 1px #D3DCE6 solid">' + this.productDetailInfo.productWt + '</td></tr></table>';
                */
                let str ='<table align="center" width="330px" height="230px" cellspacing="0" cellpadding="0" style="text-align: center;-moz-transform:scaleY(-1);-webkit-transform:scaleY(-1);-o-transform:scaleY(-1);transform:scaleY(-1);">';
                str +='<tr><td colspan="3" style="width:60px;border: 1px #D3DCE6 solid"><canvas id="canvas"></canvas></td><td colspan="4" style="width:100px;border: 1px #D3DCE6 solid;"><img width="70px" height="70px" src =" '+this.avatar+ '  "/></td></tr>';
                str +='<tr>';
                str +='<td rowspan="2" style="font-size: 3px ;border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">项目名称</td>';
                str +='<td rowspan="2" style="border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">构件编码</td>';
                str +='<td rowspan="2" style="border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">构件类型</td>';
                str +='<td rowspan="2" style="border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">构件名称</td>';
                str +='<td style="border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">楼栋</td>';
                str +='<td style="border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">方量(m³)</td>';
                str +='</tr>';
                str +='<tr>';
                str +='<td style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.buildCode + '</td>';
                str +='<td style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.productConcrete + '</td></tr>';
                str +='<tr>';
                str +='<td rowspan="2" style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.projectName + '</td>';
                str +='<td rowspan="2" style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.id + '</td>';
                str +='<td rowspan="2" style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.productTypeName + '</td>';
                str +='<td rowspan="2" style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.productName + '</td>';
                str +='<td style="border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">楼层</td>';
                str +='<td style="border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">重量(T)</td></tr>';
                str +='<tr><td style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.floorNum + '</td>';
                str +='<td style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.productWt + '</td></tr>';
                str +='</table>';
                document.getElementById('tableDiv').innerHTML = str
                this.init()

            },
            // 未执行
            showTable() {
                /* let str = '<table cellspacing="0" cellpadding="0" class="AAA"  ><tr><td rowspan="5" style="width:100px;border: 1px #D3DCE6 solid"><canvas id="canvas"></canvas></td><td style="border: 1px #D3DCE6 solid;font-weight: bold">项目名称</td><td colspan="3" style="border: 1px #D3DCE6 solid">' + this.productDetailInfo.projectName + '</td></tr>';
                 str += '<tr style="border: 1px #D3DCE6 solid"><td style="border: 1px #D3DCE6 solid;font-weight: bold">构件编码</td><td style="border: 1px #D3DCE6 solid" colspan="3">' + this.productDetailInfo.detailNo + '</td></tr>';
                 str += '<tr><td style="border: 1px #D3DCE6 solid;font-weight: bold">构件类型</td><td style="border: 1px #D3DCE6 solid" colspan="3">' + this.productDetailInfo.productTypeName + '</td></tr>';
                 str += '<tr><td style="border: 1px #D3DCE6 solid;font-weight: bold">楼栋</td><td style="border: 1px #D3DCE6 solid">' + this.productDetailInfo.buildCode + '</td><td style="border: 1px #D3DCE6 solid;font-weight: bold">楼层</td><td style="border: 1px #D3DCE6 solid">' + this.productDetailInfo.floorNum + '</td></tr>';
                 str += '<tr style="border: 1px #D3DCE6 solid"><td style="border: 1px #D3DCE6 solid;font-weight: bold">方量(m³)</td><td style="border: 1px #D3DCE6 solid">' + this.productDetailInfo.productConcrete + '</td><td style="border: 1px #D3DCE6 solid;font-weight: bold">重量(T)</td><td style="border: 1px #D3DCE6 solid">' + this.productDetailInfo.productWt + '</td></tr></table>';
                 */
                let str ='<table align="center" width="330px" height="230px" cellspacing="0" cellpadding="0" style="text-align: center;-moz-transform:scaleY(-1);-webkit-transform:scaleY(-1);-o-transform:scaleY(-1);transform:scaleY(-1);">';
                // str +='<tr><td colspan="6" style="width:100px;border: 1px #D3DCE6 solid;"><img width="100%" height="100%" src =" '+this.avatar+ '  "/></td></tr>';
                str +='<tr>';
                str +='<td rowspan="2" style="font-size: 3px ;border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">项目名称</td>';
                str +='<td rowspan="2" style="border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">构件编码</td>';
                str +='<td rowspan="2" style="border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">构件类型</td>';
                str +='<td rowspan="2" style="border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">构件名称</td>';
                str +='<td style="border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">楼栋</td>';
                str +='<td style="border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">方量(m³)</td>';
                str +='</tr>';
                str +='<tr>';
                str +='<td style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.buildCode + '</td>';
                str +='<td style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.productConcrete + '</td></tr>';
                str +='<tr>';
                str +='<td rowspan="2" style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.projectName + '</td>';
                str +='<td rowspan="2" style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.id + '</td>';
                str +='<td rowspan="2" style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.productTypeName + '</td>';
                str +='<td rowspan="2" style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.productName + '</td>';
                str +='<td style="border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">楼层</td>';
                str +='<td style="border: 1px #D3DCE6 solid;font-weight: bold;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">重量(T)</td></tr>';
                str +='<tr><td style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.floorNum + '</td>';
                str +='<td style="border: 1px #D3DCE6 solid;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);">' + this.productDetailInfo.productWt + '</td></tr>';
                str +='</table>';
                document.getElementById('tableDiv').innerHTML = str
                this.init()
            },
            showPrintTable1() {


/*
* ;-moz-transform:scaleX(-1);-webkit-transform:scaleX(-1);-o-transform:scaleX(-1);transform:scaleX(-1);
* */

                /*let str1 = '<div style=" -moz-transform:scaleY(-1);-webkit-transform:scaleY(-1);-o-transform:scaleY(-1);transform:scaleY(-1);"><table cellspacing="0" cellpadding="0" style="width: 100%; height: 100%;text-align: center;border:2px #D3DCE6 solid ;" ><tr><td rowspan="5" style="width:100px;border: 1px #D3DCE6 solid"><canvas id="canvas1"></canvas></td><td style="border: 1px #D3DCE6 solid;font-weight: bold">项目名称</td><td colspan="3" style="border: 1px #D3DCE6 solid">' + this.productDetailInfo.projectName + '</td></tr>';
                str1 += '<tr style="border: 1px #D3DCE6 solid"><td style="border: 1px #D3DCE6 solid;font-weight: bold">构件编码</td><td style="border: 1px #D3DCE6 solid" colspan="3">' + this.productDetailInfo.detailNo + '</td></tr>';
                str1 += '<tr><td style="border: 1px #D3DCE6 solid;font-weight: bold">构件类型</td><td style="border: 1px #D3DCE6 solid" colspan="3">' + this.productDetailInfo.productTypeName + '</td></tr>';
                str1 += '<tr><td style="border: 1px #D3DCE6 solid;font-weight: bold">楼栋</td><td style="border: 1px #D3DCE6 solid">' + this.productDetailInfo.buildCode + '</td><td style="border: 1px #D3DCE6 solid;font-weight: bold">楼层</td><td style="border: 1px #D3DCE6 solid">' + this.productDetailInfo.floorNum + '</td></tr>';
                str1 += '<tr style="border: 1px #D3DCE6 solid"><td style="border: 1px #D3DCE6 solid;font-weight: bold">方量(m³)</td><td style="border: 1px #D3DCE6 solid">' + this.productDetailInfo.productConcrete + '</td><td style="border: 1px #D3DCE6 solid;font-weight: bold">重量(T)</td><td style="border: 1px #D3DCE6 solid">' + this.productDetailInfo.productWt + '</td></tr></table></div>';
                document.getElementById('tablePrintDiv').innerHTML = str1
                this.init()*/

                let str1 ='<table width="330px" height="230px" style="text-align: center;" cellspacing="0" cellpadding="0">';
                str1 +='<tr><td colspan="3" style="width:100px;border: 1px #D3DCE6 solid"><canvas id="canvas1"></canvas></td><td colspan="4" style="width:100px;border: 1px #D3DCE6 solid;"><img width="70px" height="70px" src =" '+this.avatar+ '  "/></td></tr>';
                str1 +='<tr>';
                str1 +='<td  rowspan="2" style="border: 1px #D3DCE6 solid;font-weight: bold;">项目名称</td>';
                str1 +='<td  rowspan="2"  style="border: 1px #D3DCE6 solid;font-weight: bold;">构件编码</td>';
                str1 +='<td  rowspan="2" style="border: 1px #D3DCE6 solid;font-weight: bold;">构件类型</td>';
                str1 +='<td rowspan="2" style="border: 1px #D3DCE6 solid;font-weight: bold;">构件名称</td>';
                str1 +='<td  style="border:1px #D3DCE6 solid;font-weight: bold;">楼栋</td>';
                str1 +='<td  style="border: 1px #D3DCE6 solid;font-weight: bold;">方量(m³)</td>';
                str1 +='</tr>';
                str1 +='<tr>';
                str1 +='<td style="border: 1px #D3DCE6 solid;">' + this.productDetailInfo.buildCode + '</td>';
                str1 +='<td style="border: 1px #D3DCE6 solid;">' + this.productDetailInfo.productConcrete + '</td></tr>';
                str1 +='<tr>';
                str1 +='<td rowspan="2" style="border: 1px #D3DCE6 solid;">' + this.productDetailInfo.projectName + '</td>';
                str1 +='<td rowspan="2" style="border: 1px #D3DCE6 solid;">' + this.productDetailInfo.id + '</td>';
                str1 +='<td rowspan="2" style="border: 1px #D3DCE6 solid;">' + this.productDetailInfo.productTypeName + '</td>';
                str1 +='<td rowspan="2" style="border: 1px #D3DCE6 solid;">' + this.productDetailInfo.productName + '</td>';
                str1 +='<td style="border: 1px #D3DCE6 solid;font-weight: bold;">楼层</td>';
                str1 +='<td style="border: 1px #D3DCE6 solid;font-weight: bold;">重量(T)</td></tr>';
                str1 +='<tr><td style="border: 1px #D3DCE6 solid;">' + this.productDetailInfo.floorNum + '</td>';
                str1 +='<td style="border: 1px #D3DCE6 solid;">' + this.productDetailInfo.productWt + '</td></tr>';
                str1 +='</table>';
                document.getElementById('tablePrintDiv').innerHTML = str1
                this.init()
            },
            showPrintTable() {
                console.log("this.avatarPrint2",this.avatar)
                let str1 = '<table width="344px" height="230px" style="text-align: center;" cellspacing="0" cellpadding="0">';
                str1 += '<tr><td colspan="3" style="width:200px;border: 1px #D3DCE6 solid" ><canvas id="canvas1"></canvas></td>'
                str1+='<td colspan="3" style="border: 1px #D3DCE6 solid" ><img width="120px" height="120px" src =" '+this.avatar+ ' "/></td>'
                str1 += '</tr></table>'
                document.getElementById('tablePrintDiv').innerHTML = str1
                this.init()
            },
            printPage() {
                //根据ID累计打印次数
                let id = this.productDetailInfo.id;
                productDetailModel.printCountIncrease(id).then(data=>{
                    console.log("data.status",data.entity)
                }).catch(err=>{
                    console.error(err)
                })
                //隐藏打印按钮
                document.getElementById("print1").style.display = "none"
                document.getElementById("print2").style.display = "none"
                document.getElementById("testDiv").style.display = "none"
                this.showTable1()
                this.showPrintTable1()
                document.getElementById("test").style.border = "1px #D3DCE6 solid;"
                document.getElementById("tableDiv").style.display = "block"
                document.getElementById("tableDiv").style.paddingTop = "200px"
                document.getElementById("tablePrintDiv").style.paddingBottom = "200px"
                

                let newStr = document.getElementById("printDiv").innerHTML;//需要打印内容
                let oldstr = document.body.innerHTML;  //当前页面内容
                document.body.innerHTML = newStr;  //替换（隐藏不需要打印的内容）
                this.init()
                window.print();//打印
                //恢复
                document.body.innerHTML = oldstr;

                this.init()
                document.getElementById("tablePrintDiv").style.display = "none"
                location.reload()//刷新页面
                document.getElementById("print2").style.display = "block"
                document.getElementById("tablePrintDiv").style.display = "none"

                return false;
            },
            doPrint3() {
                //判断iframe是否存在，不存在则创建iframe
                var iframe = document.getElementById("print-iframe");
                if (!iframe) {
                    document.getElementById("print1").style.display = "none"
                    document.getElementById("print2").style.display = "none"
                    let el = document.getElementById('printDiv')
                    this.init()
                    let ids = this.productDetailId
                    iframe = document.createElement('IFRAME');
                    let doc = null;
                    iframe.setAttribute("id", "print-iframe");
                    iframe.setAttribute('style', 'position:absolute;width:0px;height:0px;left:-500px;top:-500px;');
                    document.body.appendChild(iframe);
                    doc = iframe.contentWindow.document;
                    //这里可以自定义样式
                    //  doc.write(" <LINK rel=' stylesheet' type='text/css' href='css/print.css'>");
                    doc.write('<div>' + el.innerHTML + '</div>')
                    doc.close();
                    iframe.contentWindow.focus();
                }
                iframe.contentWindow.print();
                if (navigator.userAgent.indexOf("MSIE") > 0) {
                    document.body.removeChild(iframe);
                }
            },
        },
        watch: {
            id: function () {
                this.init()
            }
        }
    }
</script>

<style scoped>
    #tableDiv, #tablePrintDiv{
        position:absolute;
        left: 50%;
        margin-left: -165px; 
    }
    #tablePrintDiv {
        bottom:0;
    }
    
    *{
        font-size: 10px;
    }
    
    
</style>