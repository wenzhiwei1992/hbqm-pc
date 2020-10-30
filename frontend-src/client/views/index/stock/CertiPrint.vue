<template>
<div>
    <div id="printHtml">
        <h2 style="text-align: center">{{this.$store.state.user.org.orgName }}合格证</h2>
        <div class="content">
            <div class="top">
                <span>委 托 单 位：</span><span>{{this.$store.state.user.org.orgName }}</span><span style="margin-left: 50px;">合格证编号：</span><span>{{props.certiNo}}</span>
            </div>
            <div class="mid">
                <span>工 程 名 称：</span><span>{{props.projectName}}</span>
            </div>
            <!-- <div class="bot">
                <span>楼   层    号：</span><span>{{props.buildingNum}}{{props.floorNum}}</span>
            </div> -->
        </div>
        <div class="table">
            <table width="100%" border="1" cellpadding="0" cellspacing="0" style="table-layout:fixed">
                <tbody>
                    <tr>
                        <td rowspan="3">构件名称</td>
                        <td rowspan="3">楼栋-楼层</td>
                        <td rowspan="3" style="width: 150px">生产日期</td>
                        <!--<td rowspan="3">构件型号</td>-->
                        <td colspan="3">混凝土</td>
                        <td colspan="2">主筋</td>
                        <td rowspan="3">备注</td>
                    </tr>
                    <tr>
                        <!--<td>构件名称</td>-->
                        <!--<td>生产日期</td>-->
                        <!--<td>构件型号</td>-->
                        <td colspan="2">强度等级</td>
                        <td rowspan="2">出厂强度≥%</td>
                        <td rowspan="2">种类及规格</td>
                        <td rowspan="2">机械性能</td>
                        <!--<td>备注</td>-->
                    </tr>
                    <tr>
                        <td>设计</td>
                        <td>实际</td>
                    </tr>

                    <tr v-for="item in datas" :key="item.id" >
                        <td>{{item.productName}}</td>
                        <td>{{item.buildCode}} {{item.floorNum}} </td>
                        <td>{{item.perCompDate}}</td>
                        <td>{{item.designStrenthGrade}}</td>
                        <td>{{item.strenthGrade}}</td>
                        <td>{{item.strenth}}</td>
                        <td>{{item.typeAndSpecification}}</td>
                        <td>{{item.mechanicalBehavior}}</td>
                        <td>{{item.remark}}</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="foot">
            <div><span>厂检验部门盖章：</span> <span style="margin-left: 150px;">检验员：</span></div>
            <div>本表一式三联，第一联送档案室，第二联技监部门留存，第三联送甲方。</div>
        </div>
    </div>
    <el-row :gutter="20">
        <el-col :span="2" :offset="22"><div class="grid-content bg-purple"><el-button size="mini" type="primary" @click="printPage">打印</el-button></div></el-col>
    </el-row>    
</div>
</template>
<style scoped>

    table {
        border-collapse: collapse;
        border-spacing: 1px;
        text-align: center;
    }

    .table tr {
        height: 30px;
    }

    .content, .table, .foot {
        padding: 0 50px;
        margin-top: 10px;
    }

    .content > div {
        margin-top: 5px;
    }

</style>

<script>
    import model from '../../../model/model'
    import moment from "moment";
    import { pageStorage } from '@/util/pageStorage.js'

export default {
    data(){
        return{
            datas:[],
            props:{
                prodCompany:'蒙城宝业建筑工业化有限公司',//委托单位
                projectName:'XXX',//工程名称
                certiNo:'XXXX',//合格证编号
                // buildingNum:'1#',
                // floorNum:'1'
            },
            pageStorage:pageStorage('id'),
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
        formatterDate(date0) {
            var date = date0;
            if (date == undefined) {
                return "";
            }
            return moment(date).format("YYYY-MM-DD");
        },
        showData(id){
            return model.findByIdForCertiPrint(id).then(data=>{
                this.props = data.entity;
                this.props.prodCompany = '蒙城宝业建筑工业化有限公司';
            }).then(_=>{
                return model.findCertiDetailsByInvoiceId(id).then(data=>{ //获取合格证信息
                    this.datas = data.entity;
                    for(let i = 0;i<this.datas.length;i++){
                        this.datas[i].perCompDate = this.formatterDate(this.datas[i].perCompDate);
                    }
                    console.log("datas: ",data.entity)
                })
            })
            

        },
        printPage() {
            window.open(location.href+'?printHtml')
        },
        printHtml(){
            let codestr = window.document.getElementById("printHtml").innerHTML;
            window.document.body.innerHTML = codestr;
            window.print();   //打印当前窗口

            window.opener=null;
            window.open('','_self');
            window.close();
        }
    }
}
</script>


