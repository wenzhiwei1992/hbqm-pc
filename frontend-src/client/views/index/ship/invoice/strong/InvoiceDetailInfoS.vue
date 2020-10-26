<template>
    <div>
        <el-button
                type="success"
                @click="add"
        >
            确定
        </el-button>
        <el-table
                :data="invoiceDetails"
                style="width:100%"
                :height="GLOBAL.tabelHeight"
                header-row-class-name="header-row"
                size="mini"
                stripe
                border
                tooltip-effect="light"
                row-key="id"
                ref="multipleTable"
        >
            <el-table-column
                    type="index"
                    label="序号"
                    width="70">
            </el-table-column>
                   <el-table-column
                           prop="productDetailNo"
                           label="明细编码"
                           align="center"
                           width="100"
                   ></el-table-column>
            <el-table-column
                    prop="projectName"
                    label="项目名称"
                    align="center"
                    width="100"
            ></el-table-column>
           <!-- <el-table-column
                    prop="buildCode"
                    label="楼栋"
                    align="center"
                    width="150"
            >
            </el-table-column>
            <el-table-column
                    prop="floorNum"
                    label="楼层"
                    align="center"
                    width="70"
            >
            </el-table-column>-->
            <el-table-column
                    prop="productNo"
                    label="构件编码"
                    align="center"
                    width="100"
            ></el-table-column>
            <el-table-column
                    prop="productName"
                    label="构件名称"
                    align="center"
                    width="100"
            ></el-table-column>
            <el-table-column
                    prop="productTypeName"
                    label="构件类型"
                    align="center"
                    width="100"
            ></el-table-column>
            <el-table-column
                    prop="productWt"
                    label="构件重量(t)"
                    align="center"
                    width="110"
            ></el-table-column>
            <el-table-column
                    prop="productVol"
                    label="构件方量(m³)"
                    align="center"
                    width="110"
            ></el-table-column>
            <el-table-column
                    prop="stockName"
                    label="库区号"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="shelfName"
                    label="货架号"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    label="操作"
                    align="center"
                    width="100">
                <template slot-scope="scope">
                    <el-button type="danger"
                               round
                               @click="deleteInvoiceDetail(scope.$index,scope.row)"
                    >删除
                    </el-button>
                </template>
            </el-table-column>

        </el-table>
    </div>
</template>

<script>
    var numeral = require('numeral')
    import planModel from '../../../../../model/plan/plan'
    import {Notification} from "element-ui";
    import moment from 'moment'

    export default {
        name: "InvoiceDetailInfoS",
        props: ['invoiceDetailQuery', 'newInvoiceDetails'],
        data() {
            return {
                invoiceDetails: [],
                productId: '',
                productDetailIds: [],
            }
        },
        mounted() {
            this.list()
        },
        methods: {
            list() {
                this.newInvoiceDetails.forEach(i => {
                    if (this.invoiceDetailQuery.relevanceId == i.relevanceId) {
                        this.invoiceDetails.push(i);
                    }
                })

              /*  planModel.findInvoiceDetailByInfo(this.invoiceDetailQuery).then((data) => {
                    console.log('data--->', data.entity)
                    this.invoiceDetails = data.entity
                    if (this.invoiceDetails != null || this.invoiceDetails != '') {
                        this.productId = this.invoiceDetails[0].productId
                    }
                })*/
            },
            add() {
                this.$emit('dialog-finish', this.invoiceDetails, this.invoiceDetailQuery.relevanceId, this.productDetailIds)
            },
            deleteInvoiceDetail(index, productDetails) {
               this.productDetailIds.push(productDetails.productDetailId)
                this.$refs.multipleTable.clearSelection()
                this.invoiceDetails.splice(index, 1)
            },

        }
    }
</script>

<style scoped>

</style>
