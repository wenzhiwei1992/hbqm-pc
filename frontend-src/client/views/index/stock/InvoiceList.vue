<template>
    <!--startprint-->
    <div id="invoice-list">
        <el-form :model="editForm" v-if="editForm" ref="editForm" label-width="100px" size="small" :inline="true">
            <el-row>
                <el-form-item label="发货单号" prop="deliverGoodsNo">
                    <el-input v-model="editForm.deliverGoodsNo" size="mini"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmit">查询</el-button>
                </el-form-item>
            </el-row>
        </el-form>
        <el-table
                :data="entities"
                :height="250"
                style="width:100%"
                header-row-class-name="header-row"
                size="mini"
                stripe
                border
                tooltip-effect="light"
                row-key="id"
        >
            <el-table-column type="expand">
                <template slot-scope="props">
                    <el-table
                            :data="props.row.invoiceDetails"
                            size="mini"
                    >
                        <el-table-column
                                type="index"
                                label="序号"
                                width="70">
                        </el-table-column>
                        <el-table-column
                                prop="floorRelevance.building.buildCode"
                                label="楼栋"
                                align="center"
                                width="100"
                        ></el-table-column>
                        <el-table-column
                                prop="floorRelevance.floor.floorNum"
                                label="楼层"
                                align="center"
                                width="100"
                        ></el-table-column>
<!--                        <el-table-column-->
<!--                                prop="productDetail.detailNo"-->
<!--                                label="构件明细编号"-->
<!--                                align="center"-->
<!--                                width="120"-->
<!--                        ></el-table-column>-->
                        <el-table-column
                                prop="productDetail.product.productNo"
                                label="构件编码"
                                align="center"
                                width="100"
                        ></el-table-column>
                        <el-table-column
                                prop="productDetail.product.productName"
                                label="构件名称"
                                align="center"
                                width="100"
                        ></el-table-column>
                        <el-table-column
                                prop="productDetail.product.productWt"
                                label="构件重量"
                                align="center"
                                width="100">
                        </el-table-column>
                        <el-table-column
                                prop="productDetail.stock.stockNo"
                                label="库区号"
                                align="center"
                                width="100">
                        </el-table-column>
                        <el-table-column
                                prop="productDetail.shelf.shelfNo"
                                label="货架号"
                                align="center">
                        </el-table-column>
                    </el-table>
                </template>
            </el-table-column>
            <el-table-column
                    type="index"
                    label="序号"
                    width="50">
            </el-table-column>
            <el-table-column
                    prop="deliverGoodsNo"
                    label="发货单号"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="invoiceNo"
                    label="批次号"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="inspector.inspector"
                    label="质检人"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="logCompany.logCompany"
                    label="物流公司"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="licenseNo"
                    label="车牌号"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="ibeam"
                    label="工字钢"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="squareWood"
                    label="方木"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="createdByName"
                    label="创建人"
                    align="center"
                    width="100"
            ></el-table-column>
            <el-table-column
                    prop="dateCreated"
                    label="创建时间"
                    align="center"
                    width="150"
                    :formatter="momentFormatTime"
            ></el-table-column>
            <el-table-column
                    label="操作"
                    align="center"
                    fixed="right">
                <template slot-scope="scope">
                    <span v-if="scope.row.invoiceStatus=='发货待确认' ">
                        <el-button type="primary" size="mini" @click="printInvoiceList(scope.row)" disabled>发货单打印</el-button>
                         <el-button type="primary" size="mini" @click="printCertiList(scope.row)" disabled>合格证打印</el-button>
                         <el-button type="primary" size="mini" @click="printInvoiceListNew(scope.row)" disabled>发货单针式打印</el-button>
                    </span>
                    <span v-else>
                        <el-button type="primary" size="mini" @click="printInvoiceList(scope.row)" >发货单打印</el-button>
                         <el-button type="primary" size="mini" @click="printCertiList(scope.row)" >合格证打印</el-button>
                         <el-button type="primary" size="mini" @click="printInvoiceListNew(scope.row)" >发货单针式打印</el-button>
                    </span>

                </template>
            </el-table-column>
        </el-table>
    </div>
    <!--endprint-->

</template>
<script>
    import model from '../../../model/model'
    import moment from 'moment'

    export default {
        props: ['planId'],
        data() {
            return {
                //表查询
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: 1000,
                    pageSizes: this.GLOBAL.pageSizes
                },
                entity: {},
                entities: [],
                editForm: {
                    deliverGoodsNo: ''
                }
            }
        },
        computed: {},
        mounted() {
            this.list()
        },
        methods: {
            //调出新版打印单
            printInvoiceListNew(row) {
                this.$emit('print-invoice-list')
                this.$router.push({
                    name: 'invoicePrintNew',
                    params: {
                        id: row.id
                    }
                })
            },
            //打印合格证
            printCertiList(row) {
                this.$emit('print-invoice-list'); //通知主界面关闭当前弹窗
                this.$router.push({
                    name: 'certiPrint',
                    params: {
                        id: row.id
                    }
                })
            },
            //调出打印单
            printInvoiceList(row) {
                this.$emit('print-invoice-list')
                this.$router.push({
                    name: 'invoicePrint',
                    params: {
                        id: row.id
                    }
                })
            },
            //表查询
            list() {
                if (this.planId == null) {
                    return
                }
                this.entity.planId = this.planId
                this.entity.deliverGoodsNo = this.editForm.deliverGoodsNo
                model.list('invoice', this.entity, this.pageInfo).then(data => {
                    let entity = data.entity
                    let list = data.entity.content;
                    this.entities = list
                    console.log('this.entities------>', this.entities)
                    this.pageInfo.total = data.entity.totalElements
                })
            },
            onSubmit() {
                this.list()
            },
            momentFormatTime(row, column, cellValue, index) {
                if (cellValue) {
                    return moment(cellValue).format('YYYY-MM-DD HH:mm:ss')
                } else {
                    return cellValue
                }

            },

        },
        watch: {
            planId: function () {
                this.list()
            }
        }

    }
</script>
<style>
    .el-table th, .el-table tr.header-row {
        background-color: #f5f7fa;
        font-size: 1.1em;
    }

    #entity .el-form-item {
        margin-bottom: 5px;
    }
</style>