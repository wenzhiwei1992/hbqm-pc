<template>
<div>
    <div id="printHtml">
        <div class="printHtml">
          <div class="table"
          >
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="left" style="table-layout:fixed;">
                  <tbody>
                      <tr>
                          <td colspan="12" height="50px" style="font-size:25px;" class="center relative">
                              <span>{{invoiceData.project.undertaking}}</span>
                              <div class="absolute t-0 flex" style="left:0px;">
                                  <div style="width:50px;height:49px;">
                                      <img :src="logo2">
                                  </div>
                                  <div style="width:50px;height:49px;">
                                      <img :src="gudemei">
                                  </div>
                              </div>
                          </td>
                      </tr>
                      <tr>
                          <td class="relative" colspan="6" rowspan="3">
                              <div class="absolute t-0" style="left:50px;">客户及地址</div>
                              <div class="center" style="font-size:25px;font-weight:bold;height: 80px;line-height: 80px;" contenteditable="true">{{invoiceData.project.projectName}}</div>
                          </td>
                          <td colspan="6">发货单号<span style="margin-left:30px;font-size:20px;font-weight:bold;">{{invoiceData.deliverGoodsNo}}</span></td>
                      </tr>
                      <tr>
                          <td colspan="6">
                              <div class="flex">
                                  <span>项目名称及地址</span>
                                  <div class="div-input ml-15" style="flex:1;" contenteditable="true">{{invoiceData.project.workPlace}}</div>
                              </div>
                          </td>
                      </tr>
                      <tr>
                          <td colspan="6">
                              <div class="flex">
                                  <span>建筑构件</span>
                                  <div class="div-input ml-15 contenteditable" style="flex:1;" contenteditable="true"></div>
                              </div>
                          </td>
                      </tr>
                      <tr>
                          <td colspan="6">
                              <span>运输商：</span>
                              <span contenteditable="true">{{invoiceData.logCompany.logCompany}}</span>
                          </td>
                          <td colspan="6">
                              <div class="flex">
                                  <div class="flex" style="flex:0.2;">
                                      <span>司机：</span>
                                      <div class="div-input contenteditable" style="flex:1;" contenteditable="true"></div>
                                  </div>
                                  <div class="flex" style="flex:0.4;">
                                      <span>车牌号：</span>
                                      <div class="div-input"  style="flex:1;" contenteditable="true">{{invoiceData.licenseNo}}</div>
                                  </div>
                                  <div class="flex" style="flex:0.4;">
                                      <span>车型：</span>
                                      <div class="div-input contenteditable"  style="flex:1;" contenteditable="true"></div>
                                  </div>
                              </div>
                              
                          </td>
                      </tr>
                      <tr>
                          <td colspan="6" rowspan="14" style="font-size:12px;">
                              <ul class="left-bottom">
                                  <li class="li" v-for="(item,index) in componentData" :key="index">
                                      <div class="left-wrap">
                                          <span>{{item.name}}F</span>
                                      </div>
                                      <div class="right-wrap">
                                          <span 
                                              v-for="(val,valIndex) in item.value" 
                                              :key="valIndex">
                                              {{val.productName}}*{{val.productNum}}
                                          </span>
                                      </div>
                                  </li>
                              </ul>
                          </td>
                          <td colspan="6">
                              <div class="flex">
                                  <div style="width:85px">技术要求：</div>
                                  <div style="flex:1;" contenteditable="true">装车完好无破损!</div>
                              </div>
                          </td>
                      </tr>
                      <tr>
                          <td colspan="6" contenteditable="true">垫木，货架等装车辅助物滞留工地要及时带回,如不带回将按原价计费。</td>
                      </tr>
                      <tr>
                          <td colspan="3">装车辅助物</td>
                          <td colspan="3" class="contenteditable" contenteditable="true"></td>
                      </tr>
                      <tr>
                          <td colspan="3" class="contenteditable" contenteditable="true"></td>
                          <td colspan="3" class="contenteditable" contenteditable="true"></td>
                      </tr>
                      <tr>
                          <td colspan="3" class="contenteditable" contenteditable="true"></td>
                          <td colspan="3" class="contenteditable" contenteditable="true"></td>
                      </tr>
                      <tr class="right-bottom">
                          <td colspan="2" >
                              <div>物件数</div>
                              <div>{{invoiceData.total}}件</div>
                          </td>
                          <td colspan="2" >
                              <div>到达时间</div>
                              <div class="contenteditable" contenteditable="true"></div>
                          </td>
                          <td colspan="2" >
                              <div >发货人签字</div>
                              <div class="contenteditable" contenteditable="true"></div>
                          </td>
                      </tr>
                      <tr class="right-bottom">
                          <td colspan="2" >
                              <div>装车重量</div>
                              <div contenteditable="true">{{invoiceData.weight}}吨</div>
                          </td>
                          <td colspan="2" >
                              <div>卸货开始时间</div>
                              <div class="contenteditable" contenteditable="true"></div>
                          </td>
                          <td colspan="2" >
                              <div>年/月/日</div>
                              <div>{{currentDateTime}}</div>
                          </td>
                      </tr>
                      <tr class="right-bottom">
                          <td colspan="2" >
                              <div >司机签字</div>
                              <div class="contenteditable" contenteditable="true"></div>
                          </td>
                          <td colspan="2" >
                              <div >卸货结束时间</div>
                              <div class="contenteditable" contenteditable="true"></div>
                          </td>
                          <td colspan="2" >
                              <div >收货人签字</div>
                              <div class="contenteditable" contenteditable="true"></div>
                          </td>
                      </tr>
                      <tr class="right-bottom">
                          <td colspan="2" >
                              <div >年/月/日</div>
                              <div class="contenteditable" contenteditable="true"></div>
                          </td>
                          <td colspan="2" >
                              <div >离开工地时间</div>
                              <div class="contenteditable" contenteditable="true"></div>
                          </td>
                          <td colspan="2" >
                              <div >年/月/日</div>
                              <div class="contenteditable" contenteditable="true"></div>
                          </td>
                      </tr>
                  </tbody>
              </table>
          </div>

          <div class="foot">
              <span>第一联白色 存根联</span>
              <span>第二联红色 客户联</span>
              <span>第三联蓝色 记账联</span>
              <span>第四联绿色 运输联</span>
              <span>第五联白色 财务联</span>
              <span>第六联黄色 结算联</span>
          </div>
        </div>
    </div>
    <el-row :gutter="20">
        <el-col :span="2" :offset="22"><div class="grid-content bg-purple"><el-button size="mini" type="primary" @click="printPage">打印</el-button></div></el-col>
    </el-row>    
</div>
</template>
<style lang="scss" scoped>
    #printHtml{
        
    }
    .printHtml{
        color:#000 !important;
    }
    @page{
        size:240mm 140mm;
        margin:4mm;
    }
    ul,li{
        list-style-type:none;
        padding:0;
        margin:0;
    }
    table {
        border-collapse: collapse;
        border-spacing: 1px;
        text-align: center;
        font-size:12px;
    }
    .table tr {
        height: 30px;
    }
    .content, .table, .foot {
        padding: 0 30px;
        margin-top: 10px;
    }
    .content > div {
        margin-top: 5px;
    }
    .foot span{
      font-size:12px;
      margin-left:5px;
    }
    .foot span:first-child{
      margin-left:0;
    }
    .left{
        text-align:left;
    }
    .center{
        text-align:center;
    }
    .right{
        text-align:right;
    }
    .relative{
        position:relative;
    }
    .absolute{
        position:absolute;
    }
    .t-0{
        top:0;
    }
    .flex{
        display:flex;
    }
    .h-40{
        height:40px;
    }
    .mt-15{
        margin-top:15px;
    }
    .ml-15{
        margin-left:15px;
    }
    img{
        width: 100%;
        height:100%;
    }
    textarea,input{
        border:none;
    }
    *{
        outline:none
    }
    tbody{
        border-left:1px solid #000;
        border-top:1px solid #000;
    }
    td{
        border-right:1px solid #000;
        border-bottom:1px solid #000;
    }
    .div-input{
        width: 100px;
        overflow: hidden;
        white-space:nowrap;
        text-overflow:ellipsis;
    }
    .right-bottom{
        font-size:12px;
    }
    .right-bottom td div:last-child{
        text-align:center;
    }
    .left-bottom{
        position:relative;
        display:flex;
        flex-direction:column;
        height:270px;
        li{
            border-bottom:1px solid #000;
            flex:1;
            display:flex;
            align-items: center;
            &:last-child{
                border-bottom:none;
            }
            .left-wrap{
                width: 20%;
                display:flex;
                justify-content: center;
                span{
                }
            }
            .right-wrap{
                flex:1;
                display:flex;
                flex-wrap:wrap;
                margin-left:4%;
                span{
                    width:48%;
                }
            }
        }
        &:after{
            position:absolute;
            top:0;
            left:20%;
            content:'';
            height:100%;
            border-left:1px solid #000;
        }
    }
</style>

<script>
    import model from '../../../model/model'
    import moment from "moment";
    import {Notification} from "element-ui";
    import storage from 'good-storage'
    import { pageStorage } from '@/util/pageStorage.js'
    import logo2 from '@/assets/img/logo2.jpg'
    import gudemei from '@/assets/img/gudemei.jpg'

export default {
    data(){
        return{
            invoiceData:{
                logCompany:{},
                project:{},
                weight:'',
                total:''
            },
            componentData:[],
            loading: true,
            currentDateTime:'',
            logo2,
            gudemei,
            pageStorage:pageStorage('arr'),
        }
    },
    activated() {
        this.init()
    },
    methods:{
        init(){
          this.pageStorage().then(data=>{
            this.showData(data).then(_=>{
              if(location.href.split('?')[1]==='printHtml'){
                this.printHtml(data)
              }
            })
          })
        },
        showData(arr){
            let id = arr[0].id
            this.invoiceData = arr[0]
            
            return model.findByInvoiceIdForZSPrint(id).then(data=>{
                if (data.status == 'failed') {
                    Notification({
                        message: data.msg,
                        type: 'warning'
                    })
                    this.$router.push({name:'invoiceNewList'})
                    return
                }
                this.invoiceData.weight = this.ToFixed(data.entity.productWt,3)
                this.invoiceData.total = data.entity.productCount
                
                Object.keys(data.entity.productByBF).forEach(key=>{
                    this.componentData.push(
                        {
                            name:key,
                            value:data.entity.productByBF[key]
                        }
                    )
                })
                setInterval(() => {
                    this.currentDateTime = this.GetNowFormatDate()
                }, 1000);
                // 清除所有手动填写的格子
                document.querySelectorAll('.contenteditable').forEach(option=>{
                  option.innerHTML = ""
                })
            })
        },
        printPage() {
            let codestr = window.document.getElementById("printHtml").innerHTML;
            storage.session.set(this.$route.name+'codestr',codestr)
            window.open(location.href+'?printHtml')
        },
        printHtml(){
            // 打印缓存里的页面
            window.document.body.innerHTML = storage.session.get(this.$route.name+'codestr');
            window.print();   //打印当前窗口
            // 延时关闭 解决图片加载异常
            setTimeout(_=>{
              window.opener=null;
              window.open('','_self');
              window.close();
            },0)
        }
    }
}
</script>


