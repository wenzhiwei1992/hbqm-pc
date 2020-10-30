<template>
    <div>
        <el-button type="primary" @click="invoicePrint">发货单打印</el-button>
        <el-table
                :data="invoicesPrintInfoXY"
                :height="GLOBAL.tabelHeight"
                style="width: 100%;margin-top:10px;"
                header-row-class-name="header-row"
                size="mini"
                stripe
                border
                tooltip-effect="light"
                @row-click="clickRow"
                row-key="id"
                @selection-change="handleSelectionChange"
                ref="moviesTable"
        >
            <el-table-column
                    type="selection"
                    width="39">
            </el-table-column>
            <el-table-column
                    type="index"
                    label="序号"
                    width="70">
            </el-table-column>
            <el-table-column
                    prop="buildCode"
                    label="楼栋"
                    align="center"
                    width="100"
            ></el-table-column>
            <el-table-column
                    prop="floorNum"
                    label="楼层"
                    align="center"
                    width="100"
            ></el-table-column>
            <!--                        <el-table-column-->
            <!--                                prop="productDetailNo"-->
            <!--                                label="构件明细编号"-->
            <!--                                align="center"-->
            <!--                                width="120"-->
            <!--                        ></el-table-column>-->
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
                    prop="productWt"
                    label="构件重量(t)"
                    align="center"
                    width="110"
            ></el-table-column>
            <el-table-column
                    prop="projectConcrete"
                    label="砼方量(m³)"
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
                    align="center">
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle">
            <el-col>

            </el-col>
            <el-col>
                <el-row type="flex" justify="end" align="middle">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="model.pageInfo.currentPage"
                            :page-sizes="model.pageInfo.pageSizes"
                            :page-size="model.pageInfo.pageSize"
                            :total="model.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>

            </el-col>
        </el-row>
    </div>
</template>

<script>
    var numeral = require('numeral')
    import planModel from '../../../../model/plan/plan'
    import model from '../../../../model/model'
    export default {
        name: "InvoicePrintPreviewXY",
        props:['invoicesPrintInfo','invoiceIdXY'],
        data(){
            return{
                model:{
                    invoiceId:'',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize:999,
                        pageSizes: []
                    },
                },
                invoicesPrintInfoXY:[],
                selected:[],
            }
        },
        mounted() {
            this.list()
        },
        methods:{
            list(){
                this.model.invoiceId = this.invoiceIdXY.id
                planModel.findProductDetailPrintSJ(this.model).then((data)=>{
                    this.invoicesPrintInfoXY = data.entity.list
                    log(data.entity.list)
                    this.model.pageInfo.total = data.entity.total
                })
            },
            invoicePrint(){
                if (this.selected.length === 0) {
                    this.$message(this.GLOBAL.PLAN_MSG.SELECT_NOT_PRINT);
                    return;
                }
                this.$emit('invoice-print-preview-xy-cancel')
                let arr = []
                
                this.selected.forEach((row) => {
                    arr.push(row.id)
                })
                this.$router.push({
                    name: 'xiangyangPrint',
                    params: {
                        obj:{
                          invoiceIdXY: this.invoiceIdXY,
                          invoiceDetailIds:arr,
                          data:this.invoicesPrintInfoXY,
                          invoicesPrintInfoXY:this.invoicesPrintInfoXY
                        }
                    }
                })
            },
            clickRow(){

            },
            handleSizeChange(val) {
                this.model.pageInfo.currentPage = 1
                this.model.pageInfo.pageSize = val
                this.list()
            },
            handleCurrentChange(val) {
                this.model.pageInfo.currentPage = val
                this.list()
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            momentFormatTime(row, column, cellValue, index) {
                if (cellValue) {
                    return moment(cellValue).format('YYYY-MM-DD HH:mm:ss')
                } else {
                    return cellValue
                }

            },
            // wtFormat(row, column, cellValue, index) {
            //     if (cellValue) {
            //         return numeral(cellValue).format('0.000')
            //     } else {
            //         return cellValue
            //     }
            // },

        }
    }
</script>

<style scoped>

</style>