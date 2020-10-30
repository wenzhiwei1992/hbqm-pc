<template>
    <div style="background: #E4E7ED;width: 100%;height: 100%;">
        <div style="width: 100% ; height:140px;background-color: #409EFF">
            <div class="div_project_name">{{productDetailsInfo.projectName}}</div>
        </div>
        <div style=" ">
            <div id="tableDiv">
                <div class="div_product_type_name">{{productDetailsInfo.productTypeName}}</div>
                <table border="1" cellpadding="0" cellspacing="0" width="90%" height="500px"
                       style="text-align: center;margin-left: 18px">
                    <tbody id="tbody_1">
                    <tr>
                        <td class="td_left">构件名称</td>
                        <td>{{productDetailsInfo.productName}}</td>
                    </tr>
                    <tr>
                        <td class="td_left" >构件编码</td>
                        <td>{{productDetailsInfo.productNo}}</td>
                    </tr>
                    <tr>
                        <td class="td_left" >楼栋</td>
                        <td>{{productDetailsInfo.buildCode}}</td>
                    </tr>
                    <tr>
                        <td class="td_left" >楼层</td>
                        <td>{{productDetailsInfo.floorNum}}</td>
                    </tr>
                    <tr>
                        <td class="td_left" >方量(m³)</td>
                        <td>{{productDetailsInfo.productVol}}</td>
                    </tr>
                    <tr>
                        <td class="td_left" >重量(T)</td>
                        <td>{{productDetailsInfo.productWt}}</td>
                    </tr>
                    <tr>
                        <td class="td_left" >尺寸(长*宽*高)</td>
                        <td>
                            {{productDetailsInfo.productLen}}*{{productDetailsInfo.productThick}}*{{productDetailsInfo.productHeight}}
                        </td>
                    </tr>
                    <!-- <tr>
                        <td class="td_left" >砼等级</td>
                        <td>{{productDetailsInfo.productGrade}}</td>
                    </tr> -->
                    <tr>
                        <td class="td_left" >班组</td>
                        <td>{{productDetailsInfo.teamName}}</td>
                    </tr>
                    <tr>
                        <td class="td_left" >质检人</td>
                        <td>{{productDetailsInfo.inspector}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>
<script>
    import clientModel from "../../../model/client-model";
    import axios from 'axios'

    export default {
        data() {
            return {
                productDetailsInfo: '',
            }
        },
        mounted() {
            this.show()
        },
        methods: {
            show() {
                let url = window.location.href
                let vars = url.split('?')
                let finalVars = vars[1].split('-')
                let model = {
                    id: finalVars[0],
                    tenantId: finalVars[1],
                }
                //用于为后端指定当前对应数据库
                axios.defaults.headers.common['X-TenantID'] = model.tenantId;
                clientModel.ax.post('/productDetailPrintController/getProductDetailInfoById', model).then((data) => {
                    this.productDetailsInfo = data.entity
                })
            },
        }
    }
</script>

<style>
    /*   body {
           background: none;
       }
   */
    #tableDiv {
        text-align: center;
        width: 90%;
        height: 460px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
        margin-top: 10px;
        margin-left: 20px;
        background-color: white;
    }
    table{
        border-collapse: collapse;

        height: 40px;
        margin-top: 10px;
        margin-bottom: 10px;
    }
    table td {
        border: 1px solid #E4E7ED;
        height: 40px;
        background-color: #F2F6FC;
    }

    /* table {
         border-collapse: collapse;
         margin: 0 auto;
         text-align: center;
     }

     table td, table th {
         border: 1px solid #cad9ea;
         color: #666;
         height: 30px;
     }

     table thead th {
         background-color: #CCE8EB;
         width: 100px;
     }

     table tr:nth-child(odd) {
         background: #fff;
     }

     table tr:nth-child(even) {
         background: #F5FAFA;
     }*/
    .div_project_name {
        text-align: left;
        margin-top: 0px;
        padding-top: 70px;
        padding-left: 30px;
        font-weight: bold;
        font-size: 30px;
        color: white;
    }

    .div_product_type_name {
        text-align: left;
        font-weight: bold;
        margin-left: 20px;
        padding-top: 20px;
        font-size: 20px;
    }
    .td_left{

    }
</style>