<template>
    <div>
        <el-button type="primary" @click="invoicePrint">打印</el-button>
        <el-table
                :data="invoicesPrintInfoSJ"
                :height="GLOBAL.tabelHeight"
                style="width: 100%;margin-top:10px;"
                header-row-class-name="header-row"
                size="mini"
                stripe
                border
                tooltip-effect="light"
                @row-click="clickRow"
                row-key="materialStorageId"
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
                    prop="materialStorageId"
                    label="存货编码"
                    align="center"
                    width="100"
            ></el-table-column>
            <el-table-column
                    prop="materialName"
                    label="存货名称"
                    align="center"
                    width="100"
            ></el-table-column>
            <el-table-column
                    prop="materialSpecification"
                    label="规格"
                    align="center"
                    width="100"
            ></el-table-column>
            <el-table-column
                    prop="materialUnit"
                    label="单位"
                    align="center"
                    width="100"
            ></el-table-column>
            <el-table-column
                    prop="quantity"
                    label="数量"
                    align="center"
                    width="100"
            ></el-table-column>
            <el-table-column
                    label="单价"
                    align="center"
                    width="110"
            ></el-table-column>
            <el-table-column
                    label="金额"
                    align="center"
                    width="110"
            ></el-table-column>
            <el-table-column
                    label="备注"
                    align="center"
                    width="110"
            ></el-table-column>
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
    import deliveryOrderModel from '../../../../model/materials/deliveryOrder'
    import doDetailModel from '../../../../model/materials/doDetail'
    export default {
        name: "InvoicePrintPreviewSJ",
        props:['invoiceIdSJ'],
        data(){
            return{
                deliveryOrderQuery: {
                    id: '',
                    returnId:''
                },
                info:'',
                model:{
                    id:'',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize:10,
                        pageSizes: [1,2,3,4,5,6,7,8,9,10,20]
                    },
                },
                invoicesPrintInfoSJ:[],
                selected:[],
            }
        },
        mounted() {
            this.list();
        },
        methods:{
            list(){
                this.model.id = this.invoiceIdSJ

                deliveryOrderModel.getDeliveryOrderInfo(this.invoiceIdSJ).then((data) => {
                    this.deliveryOrderQuery.returnId = data.entity.returnId;
                    this.deliveryOrderQuery.id = this.invoiceIdSJ
                    deliveryOrderModel.getDeliveryOrderDetailById(this.deliveryOrderQuery).then((dataD) => {
                        this.info = data.entity;
                        this.info.listDoDetail = dataD.entity;

                        this.invoicesPrintInfoSJ = dataD.entity
                        this.model.pageInfo.total = dataD.entity.length

                        let pageSize =[];

                        for(let i = 0;i<dataD.entity.length;i++){
                            pageSize.push(parseInt(i)+1);
                        }
                        this.model.pageInfo.pageSizes=pageSize;
                        if(dataD.entity.length<10){
                            this.model.pageInfo.pageSize = dataD.entity.length;
                        }
                    })
                })



            },
            invoicePrint(){
                if (this.selected.length === 0) {
                    this.$message(this.GLOBAL.PLAN_MSG.SELECT_NOT_PRINT);
                    return;
                }
                this.$emit('invoice-print-preview-sj-cancel')
                let arr = []
                this.selected.forEach((row) => {
                    arr.push(row.maDoDetailId)
                })
                let info1 = this.info;
                let list = [];
                for(let i =0;i<this.info.listDoDetail.length;i++){
                    if(arr.indexOf(this.info.listDoDetail[i].maDoDetailId)>-1){
                        list.push(this.info.listDoDetail[i]);
                    }
                }
                info1.listDoDetail = list;
                this.$router.push({
                    name: 'DeliveryOrderPrintPreviewTL',
                    params: {
                        info:info1,
                        invoiceDetailIds:arr
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
            wtFormat(row, column, cellValue, index) {
                if (cellValue) {
                    return numeral(cellValue).format('0.000')
                } else {
                    return cellValue
                }

            },

        }
    }
</script>

<style scoped>

</style>
