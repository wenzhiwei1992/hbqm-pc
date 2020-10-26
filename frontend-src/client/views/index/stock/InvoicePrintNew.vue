<template>
    <div>
        <div id="printHtml">
            <div id="back_gauge_class">
                <!--          <div id="img" style="position: absolute">
                             &lt;!&ndash; <img src="../img/logo/baoye.jpg" alt="图片" width="70" height="70">&ndash;&gt;
                          </div>-->
                <div id="allTitle">
                    <div id="title">
                        {{this.$store.state.user.org.orgName }}
                    </div>
                    <div id="invoice">
                        <span style="font-size: 20px;border-bottom: 2px solid black">发货单</span>
                    </div>
                    <div style="width: 100%">
                        <div style="float: right">
                    <span>
                        发货日期:
                    </span>
                            <span><input type="text" name="invoice_date" :value="this.props.invoiceDate | formatterDate"
                                         style="text-align: center;table-layout:fixed"/></span>
                            <!-- <span>{{ this.props.invoiceDate | formatterDate}}</span>-->
                        </div>
                    </div>
                </div>
                <table border="1" cellpadding="4" cellspacing="0" width="100%"
                       style="text-align: center;width: 100%;table-layout:fixed">
                    <tbody id="tbody_1">
                    <tr>
                        <th>项目名称</th>
                        <td colspan="3">
                            <input type="text" name="project_name" v-model="props.project.projectName"
                                   style="text-align: center;width: 100%;table-layout:fixed"/>
                        </td>
                        <th>楼栋</th>
                        <td><input type="text" name="otherInfo_build" v-model="otherInfo.build" style="width:100%;height:100%;"/></td>
                        <th>发货单号</th>
                        <td style="word-break:break-all;"
                        ><span>{{ this.props.deliverGoodsNo }}</span></td>
                    </tr>
                    <tr>
                        <th>收货单位</th>
                        <td colspan="3"><span>{{ this.props.project&&this.props.project.customer }}</span></td>
                        <th>物流公司</th>
                        <td colspan="3"><span>{{ this.props.logCompany.logCompany }}</span></td>
                    </tr>
                    <tr>
                        <th>供方</th>
                        <td colspan="3"><span> {{this.$store.state.user.org.orgName }}</span></td>
                        <th>方木</th>
                        <td><span>{{ this.props.squareWood }}</span></td>
                        <th>工字钢</th>
                        <td><span>{{ this.props.ibeam }}</span></td>
                    </tr>
                    <tr>
                        <th>技术要求</th>
                        <td colspan="3"><span><input type="text" style="width:100%;height:100%;"
                                                     value="装车完好,施工现场请做好成品保护"></span></td>
                        <th>备注</th>
                        <td colspan="3"><input name="otherInfo_remarks"  type="text" v-model="otherInfo.remarks" style="width:100%;height:100%;"></td>
                    </tr>
                    <tr>
                        <th colspan="4">装车构件信息</th>
                        <th colspan="4"><input type="text" style="width:100%;height:100%;"
                                               value="垫木,货架等装车辅助物要及时带回,如不带回将按原价计费。"></th>
                    </tr>
                    <tr>
                        <td rowspan="5" colspan="4">{{ this.zsprintObj.products }}</td>
                        <th>物件数(块)</th>
                        <td><span>{{ this.zsprintObj.productCount }}</span></td>
                        <th>装车方量</th>
                        <td><span>{{ this.zsprintObj.productVol  }}</span></td>
                    </tr>
                    <tr>
                        <th>制单人</th>
                        <td><span>{{this.props.createdByName}}</span></td>
                        <th>质检人</th>
                        <td><span>{{this.props.inspector.inspector}}</span></td>
                    </tr>
                    <tr>
                        <th>司机签字</th>
                        <td><input name="otherInfo_driver" type="text" v-model="otherInfo.driver" style="width:100%;height:100%;"></td>
                        <th>车牌号</th>
                        <td><span>{{this.props.licenseNo}}</span></td>
                    </tr>
                    <tr>
                        <th>收货人</th>
                        <td><input name="otherInfo_consignee" type="text" v-model="otherInfo.consignee" style="width:100%;height:100%;"></td>
                        <th>收货时间</th>
                        <td><input name="otherInfo_time" type="text" v-model="otherInfo.time" style="width:100%;height:100%;"></td>
                    </tr>
                    <tr>
                        <th>联系方式</th>
                        <th><input name="otherInfo_tel" type="text" v-model="otherInfo.tel" style="width:100%;height:100%;"></th>
                        <th>技术验收人</th>
                        <th><input name="otherInfo_acceptor" type="text" v-model="otherInfo.acceptor" style="width:100%;height:100%;"></th>
                    </tr>
                    <tr>
                        <td colspan="8">
                            <div style="display:flex;justify-content:space-between;padding-right: 0px 100px;">
                                <span>第一联白色 存根联</span>
                                <span>第二联红色 财务联</span>
                                <span>第三联蓝色 客户联</span>
                                <span>第四联绿色 运输联</span>
                                <span>第五联黄色 记账联</span>
                            </div>

                        </td>
                    </tr>
                    </tbody>
                </table>

            </div>
        </div>
        <el-row :gutter="20" style="margin-top:50px;">
            <el-col :span="2" :offset="22">
                <div class="grid-content bg-purple">
                    <el-button size="mini" type="primary" @click="printPage">打印</el-button>
                </div>
            </el-col>
        </el-row>
    </div>
</template>
<script>
    var numeral = require('numeral')
    import model from '../../../model/model'
    import moment from "moment";
    import storage from 'good-storage'
    import { pageStorage } from '@/util/pageStorage.js'

    export default {
        data() {
            return {
                invoiceId: null,
                props: {
                    inspector: {
                        inspector: ''
                    },
                    logCompany: {
                        logCompany: ''
                    },
                    project: {
                        projectName: '',
                        workPlace: '',
                        customer: '',
                    }
                },
                otherInfo:{
                  // 楼栋
                  build:'',
                  // 备注
                  remarks:'',
                  // 司机
                  driver:'',
                  // 收货人
                  consignee:'',
                  // 收货时间
                  time:'',
                  // 联系方式
                  tel:'',
                  // 技术验收人
                  acceptor:'',
                },
                //针式打印需要的信息
                zsprintObj: {
                    products: '',
                    productCount: 0,
                    productConcrete: 0
                },
                datas: [],
                pageStorage:pageStorage('id'),
            }
        },
        filters: {
            formatterDate(row) {
                var date = row;
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD");
            },
        },
        mounted() {
            // this.showData();
            // this.foobar('printHtml');
            //displate:none other nav

        },
        activated() {
            this.init()
            this.foobar('printHtml');
        },
        methods: {
            init(){
              this.pageStorage().then(data=>{
                this.showData(data).then(_=>{
                  if(location.href.split('?')[1]==='printHtml'){
                    this.otherInfo = storage.session.get(this.$route.name+'otherInfo')
                    this.props = storage.session.get(this.$route.name+'props')
                    this.$nextTick(_=>{
                      this.printHtml()
                    })
                  }else{
                    this.otherInfo={
                      // 楼栋
                      build:'',
                      // 备注
                      remarks:'',
                      // 司机
                      driver:'',
                      // 收货人
                      consignee:'',
                      // 收货时间
                      time:'',
                      // 联系方式
                      tel:'',
                      // 技术验收人
                      acceptor:'',
                    }
                  }
                })
              })
            },
            changeInput() {
                let ipText = document.getElementsByName("project_name");
                console.log("ipText", ipText)
                document.getElementsByName("project_name").item(0).onchange();
                document.getElementsByName("invoice_date").item(0).onchange();

                document.getElementsByName("otherInfo_build").item(0).onchange();
                document.getElementsByName("otherInfo_remarks").item(0).onchange();
                document.getElementsByName("otherInfo_driver").item(0).onchange();
                document.getElementsByName("otherInfo_time").item(0).onchange();
                document.getElementsByName("otherInfo_consignee").item(0).onchange();
                document.getElementsByName("otherInfo_tel").item(0).onchange();
                document.getElementsByName("otherInfo_acceptor").item(0).onchange();
            },
            showData(id) {
                return model.findByIdForPrint(id).then(data => {
                    this.props = data.entity;
                    this.props.project = this.props.project||{}
                    console.log("this.props", this.props);
                }).catch(err=>{}).then(_=>{
                  //根据invoiceId统计打印单需要的物件数、装车方量、构件信息
                  return model.findByInvoiceIdForZSPrint(id).then(data => {
                      this.zsprintObj = data.entity;
                      this.zsprintObj.productVol = this.numeralS(this.zsprintObj.productVol)
                  })
                }).catch(err=>{})
            },
            numeralS(num) {
                return numeral(num).format('0.000')
            },
            printPage() {
                storage.session.set(this.$route.name+'otherInfo',this.otherInfo)
                storage.session.set(this.$route.name+'props',this.props)
                window.open(location.href+'?printHtml')
            },
            printHtml(){
                this.changeInput()
                let codestr = window.document.getElementById("printHtml").innerHTML;
                log(codestr)
                window.document.body.innerHTML = codestr;
                window.print();

                window.opener=null;
                window.open('','_self');
                window.close();
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

    #bg {
        width: 93%;
        margin-left: 10px;
        float: left;
    }

    .foot {
        float: left;
        width: 21%;
    }

    input {
        border: none;
        text-align: center;
    }

    input:focus {
        outline: none;
    }

    #back_gauge_class {
        margin-top:50px ;
    }


</style>