<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                :model="deliveryOrderQuery"
                                ref="deliveryOrderQuery"
                        >
                            <el-form-item prop="orderNo">
                                <el-input
                                        clearable
                                        placeholder="领料编号"
                                        v-model="deliveryOrderQuery.orderNo"
                                        style="width:150px"
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="shipments">
                                <el-select
                                        clearable
                                        v-model="deliveryOrderQuery.status"
                                        style="width: 150px"
                                        placeholder="处理状态"
                                >
                                    <el-option
                                            v-for="item in optionsStatus"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="transport">
                                <el-select
                                        clearable
                                        v-model="deliveryOrderQuery.departmentId"
                                        filterable
                                        style="width: 150px"
                                        placeholder="部门"
                                >
                                    <el-option
                                            v-for="item in optionsDept"
                                            :key="item.id"
                                            :label="item.deptName"
                                            :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="status">
                                <el-select
                                        clearable
                                        v-model="deliveryOrderQuery.purpose"
                                        style="width: 150px"
                                        placeholder="用途"
                                >
                                    <el-option
                                            v-for="item in optionsPurpose"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="stockId">
                                <el-select
                                        v-model="deliveryOrderQuery.stockId"
                                        clearable
                                        placeholder="仓库"
                                        style="width: 150px"
                                >
                                    <el-option
                                            v-for="item in stockSelect"
                                            :key="item.stockName"
                                            :label="item.stockName"
                                            :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="checkout">
                                <el-select
                                        v-model="deliveryOrderQuery.projectId"
                                        clearable
                                        placeholder="项目"
                                        style="width: 150px"
                                >
                                    <el-option
                                            v-for="item in optionsProject"
                                            :key="item.id"
                                            :label="item.projectName"
                                            :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="proposer">
                                <el-input
                                        clearable
                                        placeholder="申请人"
                                        v-model="deliveryOrderQuery.proposer"
                                        style="width:150px"
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="dateCreated">
                                <el-date-picker
                                        v-model="deliveryOrderQuery.dateCreatedOne"
                                        type="date"
                                        :formatter="formatterDate"
                                        style="width: 150px"
                                        placeholder="创建开始日期"
                                >
                                </el-date-picker>
                                <el-date-picker
                                        v-model="deliveryOrderQuery.dateCreatedTwo"
                                        type="date"
                                        :formatter="formatterDate"
                                        style="width: 150px"
                                        placeholder="创建结束日期"
                                >
                                </el-date-picker>
                            </el-form-item>
                            <el-form-item prop="deliveryOrderTime">
                                <el-date-picker
                                        v-model="deliveryOrderQuery.deliveryOrderTimeOne"
                                        type="date"
                                        :formatter="formatterDate"
                                        style="width: 150px"
                                        placeholder="发料开始日期"
                                >
                                </el-date-picker>
                                <el-date-picker
                                        v-model="deliveryOrderQuery.deliveryOrderTimeTwo"
                                        type="date"
                                        :formatter="formatterDate"
                                        style="width: 150px"
                                        placeholder="发料结束日期"
                                >
                                </el-date-picker>
                            </el-form-item>
                            <el-form-item prop="remark">
                                <el-input
                                        clearable
                                        placeholder="备注"
                                        v-model="deliveryOrderQuery.remark"
                                        style="width:150px"
                                >
                                </el-input>
                            </el-form-item>

                        </el-form>
                    </el-col>
                    <el-col>
                        <my-search-button-group :model="searchButtonData"></my-search-button-group>
                    </el-col>
                </el-row>
                <el-row>
                    <my-button-group :model="buttonData"></my-button-group>
                </el-row>
            </el-header>
            <el-main
                    ref="mainContent"
                    :style="`height:${this.$store.state.mainContentHeight}px`"
            >
                <el-table
                        :data="deliveryOrders"
                        @selection-change="handleSelectionChange"
                        size="mini"
                        stripe
                        border
                        height="100%"
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                        style="width:100%"
                        @expand-change="showSubData"
                        :expand-row-keys="expands"
                        row-key="deliveryOrderId"
                >
                    <el-table-column
                            type="expand"
                            width="50"
                    >
                        <template slot-scope="props">
                            <el-table
                                    size="mini"
                                    :data="props.row.listDoDetail"
                            >
                                <el-table-column
                                        prop="status"
                                        label="状态"
                                        width="140"
                                >
                                    <template slot-scope="scope">
                    <span
                            v-if="scope.row.status=='待发料'"
                            style="color: #E6A23C;font-weight:800;"
                    >{{scope.row.status}}</span>
                                        <span
                                                v-if="scope.row.status=='驳回'"
                                                style="color: #F56C6C;font-weight: 800;"
                                        >{{scope.row.status}}</span>
                                        <span
                                                v-if="scope.row.status=='已发料'"
                                                style="color: #67C23A;font-weight:800;"
                                        >{{scope.row.status}}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        prop="materialStorageId"
                                        label="原料编码"
                                        width="140"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="materialName"
                                        label="原料名称"
                                        width="140"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="materialSpecification"
                                        label="原料规格"
                                        width="140"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="materialUnit"
                                        label="单位"
                                        width="140"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="quantity"
                                        label="申请量"
                                        width="140"
                                >
                                </el-table-column>
                                <!--            <el-table-column
                                                            prop="univalence"
                                                            label="单价"
                                                            width="80">
                                                    </el-table-column>
                                                    <el-table-column
                                                            prop="sum"
                                                            label="总金额"
                                                            width="80">
                                                    </el-table-column>-->
                                <el-table-column
                                        prop="createdBy"
                                        label="创建人"
                                        width="140"
                                >
                                </el-table-column>
                            </el-table>
                        </template>
                    </el-table-column>
                    <el-table-column
                            type="selection"
                            width="80"
                    >
                    </el-table-column>
                    <el-table-column
                            type="index"
                            label="序号"
                            width="50"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="deliveryOrderNo"
                            label="领料编号"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="status"
                            label="状态"
                            width="120"
                    >
                        <template slot-scope="scope">
              <span
                      v-if="scope.row.status=='待发料'"
                      style="color: #E6A23C;font-weight:800;"
              >{{scope.row.status}}</span>
                            <span
                                    v-if="scope.row.status=='驳回'"
                                    style="color: #F56C6C;font-weight: 800;"
                            >{{scope.row.status}}</span>
                            <span
                                    v-if="scope.row.status=='已发料'"
                                    style="color: #67C23A;font-weight:800;"
                            >{{scope.row.status}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="projectName"
                            label="项目"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="deptName"
                            label="部门"
                            width="140"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="stockName"
                            label="仓库"
                            width="140"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="purpose"
                            label="用途"
                            width="140"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="deliveryOrderType"
                            label="类型"
                            width="140"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="proposer"
                            label="申请人"
                            width="140"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="remark"
                            label="备注"
                            width="140"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="dateCreated"
                            :formatter="formatterDate"
                            label="创建时间"
                            width="140"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="deliveryOrderTime"
                            :formatter="formatterDate"
                            label="领料时间"
                            width="140"
                    >
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            fixed="right"
                            :width="rowButtonGroupWidth(rowButtonData)"
                    >
                        <template slot-scope="scope">
                            <my-row-button-group
                                    :row="scope.row"
                                    :model="rowButtonData"
                            ></my-row-button-group>
                            <!-- <el-tooltip class="item" effect="light" content="编辑" placement="top">
                                                  <el-button size="mini" icon="el-icon-edit" @click="editClick(scope.row)" ></el-button>
                                              </el-tooltip>
                                              <el-tooltip class="item" effect="light" content="打印" placement="top">
                                                  <el-button icon="el-icon-printer" size="mini" type="success" @click="printClick(scope.row)"></el-button>
                                              </el-tooltip> -->
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer ref="footOperationBar">
                <el-row>
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="deliveryOrderQuery.pageInfo.currentPage"
                            :page-sizes="deliveryOrderQuery.pageInfo.pageSizes"
                            :page-size="deliveryOrderQuery.pageInfo.pageSize"
                            :total="deliveryOrderQuery.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
        </el-container>

        <el-dialog
                :title="mode=='add'?'新建':'编辑'"
                v-if="editFormVisible"
                :visible.sync="editFormVisible"
                width="75%"
        >
            <delivery-order-edit
                    :edit-form="editForm"
                    :mode="mode"
                    @deliveryOrder-edit-finish="deliveryOrderEditFinish"
                    @deliveryOrder-edit-cancel="deliveryOrderEditCancel"
            ></delivery-order-edit>
        </el-dialog>
        <el-dialog
                :title="'领料单打印'"
                :visible.sync="invoicePrintPreviewSJVisible"
                v-if="invoicePrintPreviewSJVisible"
                width="85%"
        >
            <invoice-print-preview-sj
                    :invoiceIdSJ="invoiceIdSJ"
                    @invoice-print-preview-sj-finish="invoicePrintPreviewSJFinish"
                    @invoice-print-preview-sj-cancel="invoicePrintPreviewSJCancel"
            ></invoice-print-preview-sj>
        </el-dialog>
    </div>
</template>

<script>
    import doDetailModel from '../../../../model/materials/doDetail'
    import deliveryOrderModel from '../../../../model/materials/deliveryOrder'
    import materialStorageModel from '../../../../model/basicdata/materialStorage'
    import deptModel from '../../../../model/basicdata/dept'
    import projectModel from '../../../../model/project/project'
    import stockModel from '../../../../model/basicdata/stock'
    import DeliveryOrderEdit from './DeliveryOrderEdit.vue'
    import DeliveryOrderReturn from './DeliveryOrderReturn.vue'
    import DeliveryOrderPreviewSJ from './DeliveryOrderPreviewSJ.vue'
    import comminModel from '../../../../store/common'

    import {Notification} from 'element-ui'
    import moment from 'moment'

    export default {
        components: {
            'delivery-order-edit': DeliveryOrderEdit,
            'delivery-order-return': DeliveryOrderReturn,
            'invoice-print-preview-sj': DeliveryOrderPreviewSJ,
        },
        name: "DeliveryOrderList",
        mounted() {
            this.getAllDept()
            this.getAllProject()
            this.stockList()
            this.deliveryOrderList()
            console.log(this.$store.state.user.realName+"123")
        },
        methods: {
            invoicePrintPreviewSJFinish() {
                this.deliveryOrderList()
                this.invoicePrintPreviewSJVisible = false
            },
            invoicePrintPreviewSJCancel() {
                this.deliveryOrderList()
                this.invoicePrintPreviewSJVisible = false
            },
            printClick(row) {
                this.invoiceIdSJ = row.deliveryOrderId
                this.invoicePrintPreviewSJVisible = true
            },
            showSubData(row, expandedRows) {
                let id = row.deliveryOrderId;
                if (this.expands.some((e) => e == row.deliveryOrderId)) {
                    this.expands = []
                } else {
                    this.expands = []
                    this.expands.push(row.deliveryOrderId)
                }

                doDetailModel.getDoDetailsByMaDeliveryOrderId(row.deliveryOrderId).then((dataD) => {
                    row.listDoDetail = dataD.entity;
                })

            },
            stockList() {
                stockModel.getStockByOrgIdAndType().then((data) => {
                    this.stockSelect = data.entity;
                })
            },
            deliveryOrderList() {
                deliveryOrderModel.getDeliveryOrderLimit(this.deliveryOrderQuery).then((data) => {
                    this.deliveryOrders = data.entity.list
                    this.deliveryOrderQuery.pageInfo.total = data.entity.totalCount
                })

            },
            /**
             *模糊查询
             */
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.deliveryOrderList()
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            addClickTL(){

                if (this.selected.length == 0 || this.selected.length > 1) {
                    this.$message('请选择一个表单');
                    return;
                }

                if (this.selected[0].status != '已发料' && this.selected[0].status != '部分发料') {
                    this.$message('还未发料');
                    return;
                }

                this.$router.push({
                    name: 'deliveryOrderListTL',
                    params: {
                        deliveryOrderId:this.selected[0].deliveryOrderId
                    }
                })
            },
            addClick() {
                this.mode = 'add'
                this.editForm = {
                    id: '',
                    proposer: this.$store.state.user.realName,
                    departmentId: this.baseDepartMentId,
                    purpose: '生产',
                    deliveryOrderType: '',
                    deliveryOrderTime: new Date(),
                    projectId: '',
                    stockId: this.stockSelect[0].id,
                    baseStockId: this.stockSelect[0].id,
                    remark: '',
                    doDetailId: '',
                    deliveryPlanOrderNo: '',
                    listDoDetail: [],
                }
                materialStorageModel.getListPublicClearDoDetailVO()//每次打开添加页面清空做假添加需要的list集合
                this.editFormVisible = true
            },
            deletesClicks() {
                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }
                let ids = []
                this.selected.forEach((row) => {
                    ids.push(row.deliveryOrderId)
                })
                this.$confirm('此操作将永久删除勾选领料单, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    deliveryOrderModel.deliveryOrderDeletes(ids.join(";")).then((data) => {
                        if (data.status == 'fail') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        doDetailModel.deleteDoDetailsByMaDeliveryOrderIds(ids.join(";")).then((data) => {
                            if (data.status == 'fail') {
                                Notification({
                                    message: data.msg,
                                    type: 'warning'
                                })
                                loading.close()
                                return
                            }
                            Notification({
                                message: '删除成功',
                                type: 'success'

                            })
                            this.deliveryOrderList()
                            loading.close()
                        })

                    })
                }).catch(() => {
                })
            },
            deleteClick(row) {
                this.$confirm('此操作将永久删除该领料单, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    doDetailModel.deleteDoDetailsByMaDeliveryOrderIds(row.deliveryOrderId).then((data) => {
                        if (data.status == 'fail') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            return
                        }
                        deliveryOrderModel.deliveryOrderDelete(row.deliveryOrderId).then(data => {
                            if (data.status == 'fail') {
                                Notification({
                                    message: data.msg,
                                    type: 'warning'
                                })
                                loading.close()
                                return
                            }
                            Notification({
                                message: '删除成功',
                                type: 'success'

                            })
                            this.deliveryOrderList()
                            loading.close()
                        })
                    })

                }).catch(() => {
                })
            },
            editClick(row) {
                if (row.status == '已发料') {
                    this.$message('选择领料单已发料，不能修改！')
                    return
                } else if (row.status == '驳回') {
                    this.$message('选择领料单驳回，不能修改！')
                    return
                }
                deliveryOrderModel.countDeliveryOrderByDetailStatus(row.deliveryOrderId).then((data) => {
                    if (data.status == 'fail') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    }
                    deliveryOrderModel.deliveryOrderLoad(row.deliveryOrderId).then((data) => {
                        doDetailModel.getDoDetailsByMaDeliveryOrderId(row.deliveryOrderId).then((dataD) => {
                            var info = {
                                deliveryOrderId: data.entity.id,
                                proposer: data.entity.proposer,
                                departmentId: data.entity.departmentId,
                                purpose: data.entity.purpose,
                                deliveryOrderType: data.entity.deliveryOrderType,
                                deliveryOrderTime: data.entity.deliveryOrderTime,
                                projectId: data.entity.projectId,
                                remark: data.entity.remark,
                                stockId: data.entity.stockId,
                                lineId: data.entity.lineId,
                                baseStockId: data.entity.stockId,
                                deliveryPlanOrderNo: data.entity.deliveryPlanOrderNo,
                                listDoDetail: dataD.entity
                            }
                            this.mode = 'update'
                            this.editForm = info
                            //   materialStorageModel.getListPublicClearDoDetailVO()//每次打开添加页面清空做假添加需要的list集合
                            this.editFormVisible = true
                        })
                    })

                })
            },
            deliveryOrderEditFinish() {
                this.editFormVisible = false
                this.deliveryOrderList()
            },
            deliveryOrderEditCancel() {
                this.editFormVisible = false
                this.deliveryOrderList()
            },
            getAllDept() {
                deptModel.listAll().then((data) => {
                    this.optionsDept = data.entity
                    this.baseDepartMentId = parseInt(data.msg);
                })
            },
            getAllProject() {
                projectModel.projectListS().then((data) => {
                    this.optionsProject = data.entity
                })
            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.deliveryOrderQuery.pageInfo.pageSize = val
                this.deliveryOrderList()
            },
            handleCurrentChange(val) {
                this.deliveryOrderQuery.pageInfo.currentPage = val
                this.deliveryOrderList()
            },
        },
        data() {
            return {
                invoicePrintPreviewSJVisible: false,
                subDatas: [],
                expands: [],
                dotailStatusNum: 0,
                mold: '',
                selected: [],
                //编辑表单
                mode: '',
                editFormTitle: '',
                stockSelect: [],
                editFormVisible: false,
                editForm: null,
                deliveryOrders: [],
                listDoDetail: [],//退料操作
                optionsProject: '',
                optionsPurpose: [
                    {
                        value: '生产',
                        label: '生产'
                    },
                    {
                        value: '维修',
                        label: '维修'
                    },
                    {
                        value: '办公',
                        label: '办公'
                    },
                    {
                        value: '固定资产',
                        label: '固定资产'
                    },
                ],
                optionsDept: '',
                optionsStatus: [
                    {
                        value: '待发料',
                        label: '待发料'
                    },
                    {
                        value: '驳回',
                        label: '驳回'
                    },
                    {
                        value: '已发料',
                        label: '已发料'
                    },
                ],
                deliveryOrderQuery: {
                    status: '',
                    departmentId: '',
                    purpose: '',
                    projectId: '',
                    proposer: '',
                    dateCreated: '',
                    dateCreatedOne: '',
                    dateCreatedTwo: '',
                    deliveryOrderTime: '',
                    deliveryOrderTimeOne: '',
                    deliveryOrderTimeTwo: '',
                    remark: '',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                scopeRow: '',
                propsRow: '',
                baseDepartMentId: '',
                buttonData: [
                    {
                        name: '新建',
                        event: 'addClick'
                    },
                    {
                        name: '退库',
                        event: 'addClickTL',
                        authorized:'deliveryOrderList:TL'
                    },
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "submitForm('deliveryOrderQuery')"
                    }
                ],
                rowButtonData: [
                    {
                        name: '编辑',
                        event: 'editClick',
                    },
                    {
                        name: '打印',
                        event: 'printClick'
                    }
                ]
            }
        }
    }
</script>

<style scoped>
    .formClass {
        border: 1px lavender solid;
    }
</style>
