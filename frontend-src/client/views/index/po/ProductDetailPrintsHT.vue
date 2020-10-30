<template>
    <div>
        <!-- 打印之后回来本页面,图片会消失,图片先存起来就可以解决该问题 -->
        <div style="display:none;" >
          <img width="100%" height="100%" src="~@/assets/img/logo2.jpg">
          <img width="100%" height="100%" src="~@/assets/img/pcmaster_xy.jpg">
        </div>
        <div clsss="button">
            <el-button type="primary" id="print1" @click="printPage">打印</el-button>
        </div>
        <div id="printHtml">
            <div class="div_print_ht" v-for="(productDetail,index) in productDetailPrintHTInfo" :key="index">
                    <div  class="print_box">
                        <div class="top-wrap">
                            <div class="img-box">
                                <img width="100%" height="100%" :src="logoUrl">
                            </div>
                            <div class="img-box canvas-box">
                                <canvas :id="'canvas'+productDetail.id"></canvas>
                            </div>
                        </div>
                        <div class="bottom-wrap">
                            <ul>
                                <li>
                                    <div class="title"><span>{{productDetail.undertaking}}</span></div>
                                </li>
                                <li>
                                    <div><span>项目名称:</span></div>
                                    <div><span>{{productDetail.projectName}}</span></div>
                                </li>
                                <li>
                                    <div><span>构件名称:</span></div>
                                    <div><span>{{productDetail.productName}}</span></div>
                                </li>
                                <!--<li>-->
                                    <!--<div><span>构件编码:</span></div>-->
                                    <!--<div><span>{{productDetail.productNo}}</span></div>-->
                                <!--</li>-->
                                <li>
                                    <div><span>明细编码:</span></div>
                                    <div><span>{{productDetail.detailNo}}</span></div>
                                </li>
                                <li>
                                    <div><span>楼栋楼层:</span></div>
                                    <div><span v-if="productDetail.floorNum">{{productDetail.buildCode}} {{productDetail.floorNum}}F</span></div>
                                </li>
                                <li>
                                    <div><span>构件重量:</span></div>
                                    <div><span>{{productDetail.productWt}} T</span></div>
                                </li>
                                <li>
                                    <div><span>砼等级:</span></div>
                                    <div><span>{{productDetail.productGrade}}</span></div>
                                </li>
                                <li>
                                    <div><span>项目地点:</span></div>
                                    <div><span>{{productDetail.workPlace}}</span></div>
                                </li>
                                <li>
                                    <div><span>尺&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;寸:</span></div>
                                    <div><span>{{productDetail.productLen}}*{{productDetail.productThick}}*{{productDetail.productHeight}}m</span></div>
                                </li>
                                <!--<li>-->
                                    <!--<div><span>生产日期:</span></div>-->
                                    <!--<div><span>{{productDetail.checkTime}}</span></div>-->
                                <!--</li>-->
                            </ul>
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
        name: "ProductDetailPrintsHT",
        data() {
            let self = this
            return {
                logoUrl: avatar,
                pcmaster_xy: pcmaster_xy,
                productDetailPrintHTInfo: '',
                // 函数结果赋值形成闭包保存变量
                pageStorage:pageStorage('ids'),
                defaultImg: 'this.src="' + avatar + '"' //默认图地址
            }
        },
        mounted() {},
        activated() {
            this.init()
            if(this.$store.state.user.tenantId === 'pcmaster_xy'){
              this.logoUrl = pcmaster_xy
            }
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
                    this.productDetailPrintHTInfo = data.entity;
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
                            width: '200'
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
                setTimeout(()=>{
                  window.opener=null;
                  window.open('','_self');
                  window.close();
                },0)
                
            }
        }
    }
</script>

<style lang="scss" scoped>
    // @page{
    //   size:80mm 150mm;
    //   margin:2mm 4mm;
    // }
    ul,li{ padding:0;margin:0;list-style:none}
    .button{
        .el-button{}
    }

    .div_print_ht{
        // width:420px;
        width: 80mm;
        height:150mm;
        page-break-after:always;
        .print_box{
            width: 80mm;
            height:150mm;
            .top-wrap{
                // padding-bottom: 10px;
                width: 80mm;
                height: 45%;
                display: flex;
                align-items:center;
                .img-box{
                    width:100%;
                    height: 59%;
                    img{
                        padding:10px;
                        width: 100%;
                        height: 100%;
                    }
                }
                .canvas-box{
                    canvas{
                        width: 100% !important;
                        height: 100% !important;
                    }
                }
            }
            .bottom-wrap{
                width: 100%;
                // padding-top:30px;
                height: 55%;
                font-size: 20px;
                // border-top:1px solid #000000;
                position:relative;
                ul{
                    width: 100%;
                    display: flex;
                    position:absolute;
                    bottom:0;
                    justify-content: center;
                    flex-direction:column;
                    align-items:center;
                    li{
                        display: flex;
                        width: 100%;
                        height: 30px;
                        line-height: 30px;
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
        }
    }



</style>
