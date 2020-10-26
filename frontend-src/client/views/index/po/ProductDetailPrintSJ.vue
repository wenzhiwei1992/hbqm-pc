<template>
    <div>
        <div>
            <el-row :gutter="20">
                <el-col :span="2" :offset="22">
                    <div class="grid-content bg-purple">
                        <el-button size="mini" type="primary" @click="printPage">打印</el-button>
                    </div>
                </el-col>
            </el-row>
        </div>
        <div id="printHtml">
            <div class="div_print_sj" v-for="productDetail in productDetailInfoSJ" :key="productDetail.id">
                <table class="class_table" :id="'table_'+productDetail.id" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td colspan="8"><span class="title center size-mini">{{productDetail.projectName}}</span></td>
                    </tr>
                    <tr>
                      <td width="40%" colspan="4" rowspan="7" style="padding:0;">
                          <canvas style="width: 100%;height: 100%;display: inline-table " :id="'canvas'+productDetail.id"></canvas>
                      </td>
                      <td colspan="4"><span class="size-mini">编号:{{productDetail.productName}}</span></td>
                    </tr>
                    <tr>
                        <td colspan="4"><span class="size-mini">PID:{{productDetail.detailNo}}</span></td>
                    </tr>
                    <tr>
                        <td colspan="4">
                          <span class="size-mini">楼号:{{productDetail.buildCode}} &nbsp;&nbsp; 楼层:{{productDetail.floorNum}}F</span>
                          <span class="size-mini"></span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4"><span class="size-mini">类型:{{productDetail.productTypeName}}</span></td>
                    </tr>
                    <tr>
                        <td colspan="4"><span class="size-mini">体积:{{productDetail.productVol}}m³</span></td>
                    </tr>
                    <tr>
                      <td colspan="4"><span class="size-mini">砼强度:{{productDetail.productGrade}}</span></td>
                    </tr>
                    <tr>
                        <td colspan="4"><span class="size-mini">构件尺寸:{{productDetail.productLen}}*{{productDetail.productHeight}}*{{productDetail.productThick}} m</span></td>
                    </tr>
                    <tr>
                      <td colspan="8"><span class="undertaking center size-mini">{{productDetail.undertaking}}</span></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</template>

<script>
    var QRCode = require('qrcode')
    import productDetailModel from '../../../model/project/productDetail'
    import { pageStorage } from '@/util/pageStorage.js'

    export default {
        name: "ProductDetailPrintSJ",
        data() {
            return {
                productDetailInfoSJ: [],
                pageStorage:pageStorage('ids'),
            }
        },
        // mounted() {
        //     this.pageStorage().then(data=>{
        //       this.list(data)
        //     })
        // },
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
            list(ids) {
                return productDetailModel.findProductDetailByIdUsedForPrint(ids).then((data) => {
                    let entityTemp = data.entity;
                    let rootData = [];
                    let childData = [];
                    this.productDetailInfoSJ = entityTemp;
                })

            },
            info() {
                //获取当前链接
                let url = location.href.split("#");
                let finalUrl = url[0] + "#/product";
                console.log('test1------>',this.productDetailInfoSJ)
                let tempData = this.productDetailInfoSJ;
                for (let i =0;i<tempData.length;i++){
                    //pc端弹出框显示的二维码
                    QRCode.toDataURL(document.getElementById('canvas'+tempData[i].id), finalUrl + '?' + tempData[i].id + "-" + this.$store.state.user.tenantId + "-", {
                        errorCorrectionLevel: 'L',
                        width: '110'
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
    // @page{
    //     size:80mm 60mm;
    //     margin:2mm 0 0 0;
    // }
    .div_print_sj{
        page-break-before:always;
        .class_table{
          width :80mm - 4mm;
          height:60mm - 4mm;
          margin:0 auto;
          // table-layout: fixed;
          // border-right:1px solid #000;
          // border-bottom:1px solid #000;
          tr{
            td{
              // border-top:1px solid #000;
              // border-left:1px solid #000;
              color:#000;
              text-align: left;
              padding:3px 0;
              .size-mini{
                font-size:13px;
                // transform:scale(0.9);
                display:block;
              }
              .center{
                text-align:center;
                font-weight:bold;
              }
              .title{
                font-size:22px;
              }
              .undertaking{
                font-size:16px;
              }
            }
          }
        }
    }
</style>