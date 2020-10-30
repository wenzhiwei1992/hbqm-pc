<template>
    <div>
        <div clsss="button">
            <el-button type="primary" id="print1" @click="printPage">打印</el-button>
        </div>
        <div id="printHtml">
            <div class="div_print_ht" v-for="(productDetail,index) in productDetailPrintHTInfo" :key="index">
                <div  class="print_box">
                    <div class="product-title">
                        <span>{{productDetail.projectName}}</span>
                    </div>
                    <div class="list-wrap">
                        <ul>
                            <li>
                                <div><span>型号:{{productDetail.productName}}</span></div>
                            </li>
                            <li>
                                <div><span>楼号:{{productDetail.buildCode}}</span></div>
                                <div style="margin-left:2em;"><span>楼层:{{productDetail.floorNum}}F</span></div>
                            </li>
                            <li>
                                <div><span>编号:{{productDetail.productNo}}</span></div>
                            </li>
                            <li>
                                <div><span>子型号:{{productDetail.detailNo}}</span></div>
                            </li>
                            <li>
                                <div><span>类型:{{productDetail.productTypeName}}</span></div>
                            </li>
                            <li>
                                <div><span>方量:{{productDetail.productVol}}m³</span></div>
                            </li>
                            <li>
                                <div><span>重量:{{productDetail.productWt}}吨</span></div>
                                <div style="margin-left:2em;"><span>砼强度:{{productDetail.productGrade}}</span></div>
                            </li>
                        </ul>
                    </div>
                    <div class="qrcode-wrap">
                        <div class="img-box canvas-box">
                            <canvas :id="'canvas'+productDetail.id"></canvas>
                        </div>
                    </div>
                    <div class="undertaking">
                        <span>{{productDetail.undertaking}}</span>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
    var QRCode = require('qrcode')
    import avatar from '../../../assets/img/logo2.jpg'
    import pcmaster_xy from '@/assets/img/pcmaster_xy.jpg'
    import productDetailModel from '../../../model/project/productDetail'
    import { pageStorage } from '@/util/pageStorage.js'

    export default {
        data() {
            return {
                logoUrl: avatar,
                productDetailPrintHTInfo: '',
                pageStorage:pageStorage('ids'),
                defaultImg: 'this.src="' + avatar + '"' //默认图地址
            }
        },
        created(){

        },
        mounted() {

        },
        activated() {
          this.init()
        },
        updated() {
          this.info()
        },
        methods: {
            init(){
                this.pageStorage().then(data=>{
                  this.list(data).then(_=>{
                    if(location.href.split('?')[1]==='printHtml'){
                      this.printHtml(data)
                    }
                  })
                })
            },
            // 保留两位小数
            twoToFixed(num){
              if(!isNaN(num*1)){
                var x = String(num).indexOf('.') + 1;
                var y = String(num).length - x;
                if(y > 2){
                    return num = (num*1).toFixed(2)
                }
              }
              return num

            },
            list(ids) {
                return productDetailModel.findProductDetailByIdUsedForPrint(ids).then((data) => {
                    if(data.entity){
                      data.entity.forEach(val=>{
                        val.productHeight = this.twoToFixed(val.productHeight)
                        val.productLen = this.twoToFixed(val.productLen)
                        val.productThick = this.twoToFixed(val.productThick)
                      })
                    }
                    data.entity.forEach(val=>{
                      val.productVol = this.ToFixed(val.productVol,3)
                      val.productWt = this.ToFixed(val.productWt,3)
                    })
                    this.productDetailPrintHTInfo = data.entity;
                    log(this.productDetailPrintHTInfo)
                })
            },
            info() {
                let url = location.href.split("#");
                let that = this
                let finalUrl = url[0] + "#/product";
                //console.log(this.productDetailPrintHTInfo)
                for (let i = 0; i < this.productDetailPrintHTInfo.length; i++) {
                        QRCode.toDataURL(document.getElementById('canvas' + that.productDetailPrintHTInfo[i].id), finalUrl + '?' + that.productDetailPrintHTInfo[i].id + "-" + this.$store.state.user.tenantId + "-", {
                            errorCorrectionLevel: 'L',
                            width: '200',
                            margin:1
                        }, function (error, url) {
                            if (error) console.error(error)
                            console.log('success!');
                        })
                }

            },
            printPage() {
                window.open(location.href+'?printHtml')
                
            },
            printHtml(ids){
                //根据ids 累计次数
                productDetailModel.printCountIncrease(ids);
                //需要打印内容
                let newStr = document.getElementById("printHtml").innerHTML;
                // //当前页面内容
                let oldStr = document.body.innerHTML;
                //替换（隐藏不需要打印的内容）
                document.body.innerHTML = newStr;

                this.info()
                window.print();//打印
                //恢复
                // document.body.innerHTML = oldStr;
                // window.location.reload();
                window.opener=null;
                window.open('','_self');
                window.close();
            }
        }
    }
</script>

<style lang="scss" scoped>
    ul,li{ padding:0;margin:0;list-style:none}
    .div_print_ht{
        width: 100mm;
        height:150mm;
        page-break-after:always;
        .print_box{
            width: 100%;
            height:100%;
            overflow: hidden;
            display: flex;
            flex-direction: column;
            .product-title{
              height: 12%;
              font-size: 24px;
              font-weight:bold;
              display: flex;
              align-items:center;
              justify-content:center;
            }
            .qrcode-wrap{
                height: 36%;
                margin: 0 auto;
                .canvas-box{
                    canvas{
                        height: 100% !important;
                    }
                }
            }
            .list-wrap{
                height: 40%;
                font-size: 20px;
                margin:0 auto;
                ul{
                    width: 100%;
                    display: flex;
                    justify-content: center;
                    flex-direction:column;
                    align-items:center;
                    li{
                        display: flex;
                        width: 100%;
                        height: 32px;
                        line-height: 32px;
                        white-space:nowrap;
                        div{
                            width: 70%;
                            &:first-child{
                              width:50%;
                                flex: 1;
                            }
                        }
                        .title{
                            width: 100%;
                            white-space: nowrap;
                        }
                    }
                }
            }
            .undertaking{
              font-size:24px;
              font-weight: bold;
              height:12%;
              display: flex;
              // align-items:center;
              justify-content:center;
            }
        }
    }
</style>
