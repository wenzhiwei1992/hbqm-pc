<template>
    <div id="invoice-edit">
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="100px"
                 size="small" :inline="true">
            <el-row :gutter="24">
                <el-col :span="4">
                    <el-form-item label="发货单号" prop="deliverGoodsNo">
                        <el-input v-model="editForm.deliverGoodsNo" size="mini"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="4">
                    <el-form-item label="质检人" prop="inspectorId">
                        <el-select v-model="editForm.inspectorId" size="mini">
                            <el-option
                                    v-for="item in inspectorList"
                                    :key="item.id"
                                    :label="item.inspector"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="4">
                    <el-form-item label="物流公司" prop="logCompanyId">
                        <el-select v-model="editForm.logCompanyId" size="mini">
                            <el-option
                                    v-for="item in logCompanyList"
                                    :key="item.id"
                                    :label="item.logCompany"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="4">
                    <el-form-item label="车牌号" prop="licenseNo">
                        <el-input v-model="editForm.licenseNo" size="mini"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="4">
                    <el-form-item label="工字钢" prop="ibeam">
                        <el-input-number v-model="editForm.ibeam" size="mini" :precision="2"></el-input-number>
                    </el-form-item>
                </el-col>
                <el-col :span="4">
                    <el-form-item label="方木" prop="squareWood">
                        <el-input-number v-model="editForm.squareWood" size="mini" :precision="2"></el-input-number>
                    </el-form-item>
                </el-col>
            </el-row>
            <!--<el-form-item>-->
            <!--<el-button size="mini" @click="chooseProduct" >添加构件</el-button>-->
            <!--<el-button size="mini" @click="deletesProduct">删除构件</el-button>-->
            <!--</el-form-item>-->
        </el-form>

        <el-table
                :data="plan.planDetails"
                ref="planDetails"
                stripe
                border
                :default-sort="{prop: 'productDetail.product.productName', order: 'descending'}"
                height="400"
        >
            <el-table-column type="expand">
                <template slot-scope="props">
                    <el-table
                            :data="props.row.newInvoiceDetails"
                            size="mini"
                    >
                        <el-table-column
                                type="index"
                                label="本次发货明细"
                                align="center"
                                width="110">
                        </el-table-column>
<!--                        <el-table-column-->
<!--                                prop="productDetail.detailNo"-->
<!--                                label="构件明细编号"-->
<!--                                align="center"-->
<!--                                width="120">-->
<!--                        </el-table-column>-->
                        <el-table-column
                                prop="productDetail.product.productNo"
                                label="构件编码"
                                align="center"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="productDetail.product.productName"
                                label="构件名称"
                                align="center"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="productDetail.stock.stockNo"
                                label="入库库区号"
                                align="center"
                                width="100"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="productDetail.shelf.shelfNo"
                                label="入库货架号"
                                align="center"
                                width="100"
                        >
                        </el-table-column>
                        <el-table-column
                                label="操作"
                                align="center"
                        >
                            <template slot-scope="scope">
                                <el-button type="danger" round icon="el-icon-minus"
                                           @click="deleteInvoiceDetail(scope.row)">删除
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </template>
            </el-table-column>
            <el-table-column
                    type="index"
                    label="计划明细"
                    align="center"
                    width="80">
            </el-table-column>
            <el-table-column
                    prop="floorRelevance.building.buildCode"
                    label="楼栋"
                    align="center"
                    width="80"
            ></el-table-column>
            <el-table-column
                    prop="floorRelevance.floor.floorNum"
                    label="楼层"
                    align="center"
                    width="80"
            ></el-table-column>
            <el-table-column
                    prop="product.productNo"
                    label="构件编码"
                    align="center"
                    width="100"
            ></el-table-column>
            <el-table-column
                    prop="product.productName"
                    label="构件名称"
                    align="center"
                    width="100"
            ></el-table-column>
            <el-table-column
                    prop="product.productWt"
                    label="构件重量(t)"
                    align="center"
                    width="100"
                    :formatter="wtFormat"
            ></el-table-column>
            <el-table-column
                    prop="totalWt"
                    label="计划重量"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="num"
                    label="计划数"
                    align="center"
                    width="80">
            </el-table-column>
            <el-table-column
                    prop="invoiceDetails.length"
                    label="已发货数"
                    align="center"
                    width="80">
            </el-table-column>
            <el-table-column
                    label="本次发货数"
                    align="center"
                    width="100">
                <template slot-scope="scope">
                    <div :class="{'red':!legalAdd(scope.row)}">{{scope.row.newInvoiceDetails.length}}</div>
                </template>
            </el-table-column>
            <el-table-column
                    prop="productWarehousingNum"
                    label="库存数"
                    align="center"
                    width="80">
            </el-table-column>
            <el-table-column
                    label="操作"
                    align="center"
            >
                <template slot-scope="scope">
                    <el-button icon="el-icon-plus" type="primary"
                               @click="productDetailChoose(scope.row,plan.planDetails)"
                               :disabled="!permitAdd(scope.row)">添加本次发货构件
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-row type="flex" justify="space-around" align="middle" style="margin-top:15px">
            <el-col>
                <el-button type="primary" @click="editFormSubmmit('editForm')" size="mini" :disabled="submitting">确 定
                </el-button>
                <el-button @click="editFormCancel" size="mini">取 消</el-button>
            </el-col>
        </el-row>


        <el-dialog append-to-body :visible.sync="productDetailChooseVisible" width="70%"
                   v-if="productDetailChooseVisible">
            <product-detail-choose
                    :plan-detail="planDetail"
                    :repeatProductDetailIds="repeatProductDetailIds"
                    @product-detail-choose-finish="productDetailChooseFinish"></product-detail-choose>
        </el-dialog>


    </div>


</template>
<script>
    import model from '../../../model/model'
    import ProdcutDetailChoose from './ProdcutDetailChoose.vue'
    import qualityInspectionModel from '../../../model/basicdata/qualityInspectionModel'
    import moment from 'moment'

    var numeral = require('numeral')
    import {Notification} from 'element-ui'

    export default {
        props: ['plan'],
        components: {
            'product-detail-choose': ProdcutDetailChoose,
        },
        data() {
            return {
                editForm: {
                    deliverGoodsNo: '',
                    inspectorId: null,
                    logCompanyId: null,
                    licenseNo: null,
                    ibeam: 0,
                    squareWood: 0
                },
                eidtFormRules: {
                    deliverGoodsNo: [
                        {required: true, message: '请填写发货单号', trigger: 'blur'},
                    ],
                    inspectorId: [
                        {required: true, message: '请选择质检人', trigger: 'blur'},
                    ],
                    logCompanyId: [
                        {required: true, message: '请选择物流公司', trigger: 'blur'},
                    ],
                    licenseNo: [
                        {required: true, message: '请填写车牌号', trigger: 'blur'},
                    ],
                    ibeam: [
                        {required: true, message: '请填写工字钢', trigger: 'blur'},
                    ],
                    squareWood: [
                        {required: true, message: '请填写方木', trigger: 'blur'},
                    ]

                },
                inspectorList: [],
                logCompanyList: [],
                planDetail: null,
                productDetailChooseVisible: false,
                submitting: false,
                repeatProductDetailIds: [],//用于新建发货单未保存是构件明细数据去重
            }
        },
        mounted() {
            this.init()

        },
        computed: {},
        methods: {
            init() {
                this.plan.planDetails.forEach(pd => pd.newInvoiceDetails = [])
                qualityInspectionModel.all().then(data => {
                    data.entity.forEach(q => {
                        if (q.type == '质检人') {
                            this.inspectorList.push(q)
                        } else if (q.type == '物流公司') {
                            this.logCompanyList.push(q)
                        }
                    })
                }).catch(() => {
                })

            },
            productDetailChoose(row, row2) {

                //该发货计划中新建一条发货单未保存时，数据未做去重处理，导致发货单构件明细重复，该操作是为了避免重复
                let arr = new Array()
                row2.forEach(r => {
                    if (r.newInvoiceDetails.length > 0) {
                        r.newInvoiceDetails.forEach(nd => {
                            arr.push(nd.productDetail.id);
                        })
                    }
                })
                this.repeatProductDetailIds = arr
                arr = []
                this.planDetail = row
                this.productDetailChooseVisible = true
            },
            productDetailChooseFinish(planDetail, productDetails) {
                productDetails.forEach(pd => {
                    planDetail.newInvoiceDetails.push({productDetail: pd})
                })
                this.productDetailChooseVisible = false
                this.$refs.planDetails.toggleRowExpansion(planDetail)
                this.$refs.planDetails.toggleRowExpansion(planDetail)//TO SPECIAL:解决本次发货数页面不更新的问题

                console.log('this.plan.planDetails', this.plan.planDetails)
                console.log()


            },
            deleteInvoiceDetail(row) {
                this.plan.planDetails.forEach(pd => {
                    let index = pd.newInvoiceDetails.findIndex(d => d.productDetail.id == row.productDetail.id)
                    if (index >= 0) {
                        pd.newInvoiceDetails.splice(index, 1)
                        this.$refs.planDetails.toggleRowExpansion(pd)
                        this.$refs.planDetails.toggleRowExpansion(pd)//TO SPECIAL:解决本次发货数页面不更新的问题
                    }
                })
            },
            permitAdd(row) {
                return (row.invoiceDetails.length + row.newInvoiceDetails.length) < row.num
            },
            legalAdd(row) {
                if (row.newInvoiceDetails.length == 0) {
                    return true
                }
                return (row.invoiceDetails.length + row.newInvoiceDetails.length) <= row.num
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let invoiceDetails = []
                        let legalAdd = true
                        this.plan.planDetails.forEach(pd => {
                            legalAdd = this.legalAdd(pd)
                            pd.newInvoiceDetails.forEach(n => {
                                let invoiceDetail = {}
                                invoiceDetail.productDetailId = n.productDetail.id
                                invoiceDetail.relevanceId = pd.relevanceId
                                invoiceDetail.planDetailId = pd.id
                                invoiceDetails.push(invoiceDetail)
                            })
                        })
                        if (!legalAdd) {
                            this.$message('构件数量超过了计划数量')
                            return
                        }
                        if (invoiceDetails.length == 0) {
                            this.$message('请添加构件')
                            return
                        }
                        this.editForm.planId = this.plan.id
                        this.editForm.projectId = this.plan.projectId
                        this.editForm.invoiceDetails = invoiceDetails
                        this.editForm.ibeam = numeral(this.editForm.ibeam).format('0.00')
                        this.editForm.squareWood = numeral(this.editForm.squareWood).format('0.00')
                        this.submitting = true
                        model.add('invoice', this.editForm).then(data => {
                            if (data.status == 'fail') {
                                Notification({
                                    message: data.msg,
                                    type: 'warning'
                                })
                                this.submitting = false
                                return
                            }
                            Notification({
                                message: '保存成功',
                                type: 'success'

                            })
                            this.submitting = false
                            this.$emit('model-edit-finish')
                        })
                    }
                })
            },
            editFormCancel() {
                this.$emit('model-edit-cancel')
            },
            momentFormat(row, column, cellValue, index) {
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

        },
        watch: {}

    }
</script>
<style>
    #invoice-edit .el-form-item__label {
        font-size: 16px;

    }

    #invoice-edit .red {
        color: red;
    }
</style>