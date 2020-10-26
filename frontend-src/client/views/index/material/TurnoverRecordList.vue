<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true"
                                 id="projectList"
                                 :model="turnoverQuery"
                                 ref="turnoverQuery"
                                 style="padding-bottom:8px;">
                            <el-form-item prop="turnoverDNo">
                                <el-input
                                        placeholder="收料单编号"
                                        v-model="turnoverQuery.turnoverDNo"
                                        style="width:120px"
                                        clearable>
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="status">
                                <el-select
                                        v-model="turnoverQuery.status"
                                        style="width:120px"
                                        placeholder="库存状态"
                                        clearable>
                                    <el-option
                                            v-for="item in optionsStatus"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item prop="shipments">
                                <el-select
                                        v-model="turnoverQuery.shipmentsId" filterable
                                        style="width:120px"
                                        placeholder="供方"
                                        clearable>
                                    <el-option
                                            v-for="item in shipmentsSelect"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item prop="departmentId">
                                <el-select
                                        v-model="turnoverQuery.departmentId" filterable
                                        style="width:120px"
                                        placeholder="部门"
                                        clearable>
                                    <el-option
                                            v-for="item in departmentSelect"
                                            :key="item.id"
                                            :label="item.deptName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item prop="stockId">
                                <el-select
                                        v-model="turnoverQuery.stockId"
                                        style="width:120px"
                                        placeholder="仓库"
                                        clearable>
                                    <el-option
                                            v-for="item in stockSelect"
                                            :key="item.stockName"
                                            :label="item.stockName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="transport">
                                <el-input
                                        placeholder="运输单位"
                                        v-model="turnoverQuery.transport"
                                        style="width:120px"
                                        clearable>
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="dateCreated">

                                <el-date-picker
                                        v-model="turnoverQuery.dates"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        type="daterange"
                                        range-separator="至"
                                        start-placeholder="创建时间开始"
                                        end-placeholder="创建时间结束"
                                        :default-time="['00:00:00', '23:59:59']">
                                </el-date-picker>
                            </el-form-item>
                        </el-form>
                    </el-col>
                    <el-col>
                        <my-search-button-group :model='searchButtonData'></my-search-button-group>
                    </el-col>
                </el-row>
                <el-row>
                    <my-button-group :model="buttonData"></my-button-group>
                </el-row>
            </el-header>
            <el-main
                    ref="mainContent"
                    :style="`height:${this.$store.state.mainContentHeight}px`">
                <el-table
                        :data="turnovers"
                        @selection-change="handleSelectionChange"
                        size="mini"
                        stripe
                        border
                        height="100%"
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                >
                    <el-table-column
                            type="expand"
                            width="50">
                        <template slot-scope="props"><!--:data=" props.row.floorVO_s"turnovers-->
                            <el-table
                                    size="mini"
                                    :data="props.row.turnovers"
                            >
                                <el-table-column
                                        prop="materialStorageId"
                                        label="原料编码"
                                        width="140">
                                </el-table-column>
                                <!-- <el-table-column
                                        prop="materialNo"
                                        label="原原料编码"
                                        width="140">
                                </el-table-column> -->
                                <el-table-column
                                        prop="materialName"
                                        label="原料名称"
                                        width="140">
                                </el-table-column>
                                <el-table-column
                                        prop="materialSpecification"
                                        label="原料规格"
                                        width="140">
                                </el-table-column>
                                <el-table-column
                                        prop="amount"
                                        label="数量"
                                        width="80">
                                </el-table-column>
                                <el-table-column
                                        prop="materialUnit"
                                        label="单位"
                                        width="50">
                                </el-table-column>
                                <el-table-column
                                        prop="checkoutStatus"
                                        label="检验状态"
                                        width="80">
                                    <template slot-scope="scope">
                                        <span v-if="scope.row.checkoutStatus=='退回'"
                                              style="color: #F56C6C;font-weight:800;">{{scope.row.checkoutStatus}}</span>
                                        <span v-else
                                              style="color: #67C23A;font-weight:800;">{{scope.row.checkoutStatus}}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        prop="warehouseStatus"
                                        label="库存状态"
                                        width="80">
                                    <template slot-scope="scope">
                                        <span v-if="scope.row.warehouseStatus=='待入库'||scope.row.warehouseStatus=='退回'"
                                              style="color: #F56C6C;font-weight:800;">{{scope.row.warehouseStatus}}</span>
                                        <span v-else
                                              style="color: #67C23A;font-weight:800;">{{scope.row.warehouseStatus}}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        sortable
                                        prop="warehouseTime"
                                        :formatter="formatterDate"
                                        label="入库时间"
                                        width="140">
                                </el-table-column>
                                <el-table-column
                                        prop="checkout"
                                        label="是否免检"
                                        width="80">
                                </el-table-column>
                                <el-table-column
                                        prop="reason"
                                        label="备注">
                                    <template slot-scope="scope">
                                    <el-tooltip class="item" effect="dark" :content="scope.row.reason" placement="top">
                                        <span>{{scope.row.reason}}</span>
                                    </el-tooltip>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        prop="compromiseReason"
                                        label="让步审批备注">
                                    <template slot-scope="scope">
                                        <el-tooltip class="item" effect="dark" :content="scope.row.compromiseReason" placement="top">
                                            <span>{{scope.row.compromiseReason}}</span>
                                        </el-tooltip>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        label="操作"
                                        align="left"
                                        fixed="left"
                                        width="160">
                                    <template slot-scope="scope">
                                        <!--   <div v-if="scope.row.checkoutStatus=='待检验'"><el-button size="mini" @click="checkoutStatus(scope.row,props.row)">送检</el-button></div>-->
                                        <div v-if="scope.row.warehouseStatus=='待入库' || scope.row.templateUrl">
                                            <el-button type="primary" size="mini" v-if="scope.row.warehouseStatus=='待入库'"
                                                       @click="warehouseStatus(scope.row,props.row)">
                                                入库
                                            </el-button>
                                            <a v-if="scope.row.templateUrl" :href="scope.row.templateUrl" target="_blank">
                                                <el-button type="primary" size="mini" >
                                                    检验模板
                                                </el-button>
                                            </a>
                                        </div>

                                    </template>
                                </el-table-column>
                            </el-table>
                        </template>
                    </el-table-column>
                    <el-table-column
                            type="selection"
                            width="80">
                    </el-table-column>
                    <el-table-column
                            type="index"
                            label="序号"
                            width="50">
                    </el-table-column>
                    <el-table-column
                            prop="turnoverRecordNo"
                            label="收料单编号"
                            width="140">
                    </el-table-column>

                    <el-table-column
                            prop="checkoutStatus"
                            label="检验状态"
                            width="80">
                        <template slot-scope="scope">
                                        <span v-if="scope.row.checkoutStatus=='退回'"
                                              style="color: #F56C6C;font-weight:800;">{{scope.row.checkoutStatus}}</span>
                            <span v-else
                                  style="color: #67C23A;font-weight:800;">{{scope.row.checkoutStatus}}</span>
                        </template>
                    </el-table-column>

                    <el-table-column
                            prop="status"
                            label="库存状态"
                            width="80">
                        <template slot-scope="scope">
                                    <span v-if="scope.row.status=='待入库'"
                                          style="color: #F56C6C">{{scope.row.status}}</span>
                            <span v-else style="color: #67C23A">{{scope.row.status}}</span>
                        </template>
                    </el-table-column>

                    <el-table-column
                            prop="shipments"
                            label="供方"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            prop="deptName"
                            label="部门"
                            width="100">
                    </el-table-column>
                    <el-table-column
                            prop="stockName"
                            label="仓库"
                            width="90">
                    </el-table-column>
                    <el-table-column
                            prop="freight"
                            label="运费"
                            width="80">
                    </el-table-column>
                    <el-table-column
                            prop="transport"
                            label="运输单位"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            prop="licenseNo"
                            label="车牌号"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            prop="dateCreated"
                            :formatter="formatterDate"
                            label="创建时间"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            prop="createdBy"
                            label="创建人"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="modifiedBy"
                            label="修改人"
                            width="90">
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="dateModified"
                            :formatter="formatterDate"
                            label="修改时间"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            fixed="right"
                            :width="rowButtonGroupWidth(rowButtonData)">
                        <template slot-scope="scope">
                            <my-row-button-group
                                    :row="scope.row"
                                    :model="rowButtonData">

                            </my-row-button-group>
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer ref="footOperationBar">
                <el-row>
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="turnoverQuery.pageInfo.currentPage"
                            :page-sizes="turnoverQuery.pageInfo.pageSizes"
                            :page-size="turnoverQuery.pageInfo.pageSize"
                            :total="turnoverQuery.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
        </el-container>
        <el-dialog :title="mode=='add'?'新建':'编辑'"
                   v-if="editFormVisible"
                   :visible.sync="editFormVisible"
                   width="65%" append-to-body>
            <turnover-record-edit :edit-form="editForm"
                                  :mode="mode"
                                  :departmentSelect="departmentSelect"
                                  :stockSelect="stockSelect"
                                  :shipmentsSelect="shipmentsSelect"
                                  @turnoverRecord-edit-finish="materialStorageEditFinish"
                                  @turnoverRecord-edit-cancel="materialStorageEditCancel"
            ></turnover-record-edit>
        </el-dialog>
        <el-dialog :title="'收料单打印'"
                   :visible.sync="invoicePrintPreviewSJVisible"
                   v-if="invoicePrintPreviewSJVisible"
                   width="85%">
            <invoice-print-preview-sj
                    :invoiceIdSJ="invoiceIdSJ"
                    @invoice-print-preview-sj-finish="invoicePrintPreviewSJFinish"
                    @invoice-print-preview-sj-cancel="invoicePrintPreviewSJCancel"
            ></invoice-print-preview-sj>
        </el-dialog>

        <el-dialog
                :visible.sync="editFormUpoloadVisible"
                width="35%"
                append-to-body
        >
            <file-upload
                    fileName="turnoverRecord.xlsx"
                    @download-finish="downloadFinish"
                    @download-cancel="downloadCancel"
            ></file-upload>
        </el-dialog>
    </div>
</template>

<script>
    import turnoverDetailModel from '../../../model/materials/turnoverDetail'
    import turnoverRecordModel from '../../../model/materials/turnoverRecord'
    import supplierModel from '../../../model/basicdata/supplier'
    import departmentModel from '../../../model/basicdata/dept'
    import stockModel from '../../../model/basicdata/stock'
    import menuTabService from '../../../service/menuTabService'
    import TurnoverRecordEdit from './TurnoverRecordEdit.vue'
    import DeliveryOrderInPreviewSJ from './DeliveryOrderInPreviewSJ.vue'
    import FileUpload from '../../../components/fileUpload/index.vue'
    import CheckoutStatus from './CheckoutStatus.vue'
    import {Notification} from 'element-ui'
    import moment from "moment";

    export default {
        data() {
            return {
                editFormUpoloadVisible: false,
                invoicePrintPreviewSJVisible: false,
                shipmentsSelect: [],
                departmentSelect: [],
                stockSelect: [],
                optionsStatus: [
                    {
                        value: '已入库',
                        label: '已入库'
                    },
                    {
                        value: '待入库',
                        label: '待入库'
                    },
                ],
                turnovers: [],
                options: [{
                    value: '是',
                    label: '是'
                }, {
                    value: '否',
                    label: '否'
                }],
                turnoverQuery: {
                    turnoverDNo: '',
                    materialStorageId: '',
                    materialName: '',
                    materialSpecification: '',
                    shipments: '',
                    shipmentsId: '',
                    stockId: '',
                    departmentId: '',
                    transport: '',
                    checkout: '',
                    status: '',
                    dateCreated: '',
                    dates: ['', ''],
                    dateCreatedOne: '',
                    dateCreatedTwo: '',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                mold: '',
                selected: [],
                //编辑表单
                mode: '',
                editFormTitle: '',
                editFormVisible: false,
                editForm: null,

                checkEditFormTitle: '',
                checkEditFormVisible: false,
                checkEditForm: null,
                propsRow: null,
                scopeRow: null,
                //入检所用ID
                turnoverDetailId: '',
                turnoverRecordId: '',
                baseDepartMentId: '',
                templateCheckArr: [],
                buttonData: [
                    {
                        name: '新建',
                        event: 'addClick'
                    },
                    {
                        name: '下载',
                        event: 'materialExport'
                    },
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "submitForm('turnoverQuery')"
                    },
                    {
                        name: '重置',
                        event: 'resetForm("turnoverQuery")'
                    }
                ],
                rowButtonData: [
                    {
                        name: '编辑',
                        event: "editClick",
                        authorized: 'turnoverRecordList:edit',
                    },
                    {
                        name: '删除',
                        event: "deleteClick",
                        authorized: 'turnoverRecordList:delete',
                    },
                    {
                        name: '入库',
                        event: "checkNew",
                    },
                    {
                        name: '打印',
                        event: "printClick",
                    }
                ]
            }
        },
        components: {
            'turnover-record-edit': TurnoverRecordEdit,
            'checkout-status': CheckoutStatus,
            'invoice-print-preview-sj': DeliveryOrderInPreviewSJ,
            'file-upload': FileUpload,
        },
        mounted() {
            this.shipmentsList();
            this.departmentList();
            this.stockList();
            this.turnOverRList()
            this.checkTemplate()
        },
        methods: {
            checkTemplate() {
                let templateNo = '物料模板'
                turnoverRecordModel.getCheckTemplate(templateNo).then((data) => {
                    this.templateCheckArr = data.entity;
                })
            },
            invoicePrintPreviewSJFinish() {
                this.turnOverRList()
                this.invoicePrintPreviewSJVisible = false
            },
            invoicePrintPreviewSJCancel() {
                this.turnOverRList()
                this.invoicePrintPreviewSJVisible = false
            },
            printClick(row) {
                this.invoiceIdSJ = row.turnoverRecordId
                this.invoicePrintPreviewSJVisible = true
            },
            shipmentsList() {
                supplierModel.listByOrgId().then((data) => {
                    this.shipmentsSelect = data.entity;
                })
            },
            departmentList() {
                departmentModel.listAll().then((data) => {
                    this.departmentSelect = data.entity
                    this.baseDepartMentId = parseInt(data.msg);
                })
            },
            stockList() {
                stockModel.getStockByOrgIdAndType().then((data) => {
                    console.log('stockSelect------>', data.entity)
                    this.stockSelect = data.entity;
                })
            },

            turnOverRList() {
                this.turnovers = []
                turnoverRecordModel.getTurnoverRecordLimit(this.turnoverQuery).then((data) => {
                    for (var i = 0; i < data.entity.list.length; i++) {
                        var turnoverRecordId = data.entity.list[i].turnoverRecordId
                        if (turnoverRecordId == null) {

                        } else {
                            turnoverDetailModel.getTurnoverDetailByTurnoverRecordId(turnoverRecordId).then((data1) => {
                                if (data1.entity == false) {

                                } else {
                                    for (var j = 0; j < this.turnovers.length; j++) {
                                        if (this.turnovers[j].turnoverRecordId == data1.entity[0].turnoverRecordId) {
                                            this.turnovers[j].turnovers = data1.entity;
                                            for (var k = 0; k < this.turnovers[j].turnovers.length; k++) {
                                                for (var v = 0; v < this.templateCheckArr.length; v++) {
                                                    if (this.templateCheckArr[v].templateTypeId == this.turnovers[j].turnovers[k].materialTypeId) {
                                                        console.log(this.turnovers[j].turnovers[k].templateId)
                                                        if (this.turnovers[j].turnovers[k].templateId) {
                                                            this.turnovers[j].turnovers[k].templateUrl = this.templateCheckArr[v].templatePath + "dayin?embed=true&id=" + this.turnovers[j].turnovers[k].templateId;
                                                        }
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            })
                        }
                    }
                    this.turnovers = data.entity.list
                    this.turnoverQuery.pageInfo.total = data.entity.totalCount
                })
            },
            addClick() {
                this.mode = 'add'
                this.editForm = {
                    id: '',
                    materialStorageId: '',
                    materialName: '',
                    materialSpecification: '',
                    materialUnit: '',
                    univalence: '',
                    freight: '',
                    shipments: '',
                    transport: '',
                    licenseNo: '',
                    roughWeight: '',
                    tare: '',
                    sundries: '',
                    suttle: '',
                    realityWeight: '',
                    remark: '',
                    turnoverRecordId: '',
                    turnovers: [],
                    departmentId: this.baseDepartMentId,
                    stockId: this.stockSelect[0].id
                }
                //materialStorageModel.getListPublicClear()//每次打开添加页面清空做假添加需要的list集合
                this.editFormVisible = true
            },
            deletesClicks() {
                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }
                let ids = []//turnoverDetail操作
                let idsR = []//turnoverRecord操作
                this.selected.forEach((row) => {
                    ids.push(row.id)
                    idsR.push(row.turnoverRecordId)
                })
                this.$confirm('此操作将永久删除勾选收料单, 是否继续?', '提示', {
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
                    turnoverRecordModel.turnoverRecordDeletes(idsR.join(";")).then((data) => {
                        if (data.status == 'fail') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        turnoverDetailModel.deleteByTurnoverRecordIds(idsR.join(";")).then((data) => {
                            if (data.status == 'fail') {
                                Notification({
                                    message: data.msg,
                                    type: 'warning'
                                })
                                loading.close()
                                return
                            }
                        })
                        Notification({
                            message: '删除成功',
                            type: 'success'

                        })
                        this.turnOverRList()
                        loading.close()
                    })
                }).catch(() => {
                })
            },
            checkNew(row) {

                if (row.status == '已入库') {
                    this.$message('请查看选中收料单状态！');
                    return;
                }
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                turnoverRecordModel.updateStatusById(row.turnoverRecordId).then((data) => {
                    if (data.status == 'fail') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        loading.close()
                        return
                    }
                    Notification({
                        message: '入库成功',
                        type: 'success'

                    })
                    this.isCheck = false
                    this.turnOverRList()
                    loading.close()
                })
            },
            check() {
                if (this.selected.length == 0) {
                    this.$message('请选择入库项');
                    return;
                }
                var arr = new Array()
                this.selected.forEach((row) => {
                    arr.push(row)
                })
                if (arr.length > 1) {
                    this.$message('只能勾选一个进行编辑！');
                    return;
                }
                if (arr[0].status == '已入库') {
                    this.$message('请查看选中收料单状态！');
                    return;
                }
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                turnoverRecordModel.updateStatusById(arr[0].turnoverRecordId).then((data) => {
                    if (data.status == 'fail') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        loading.close()
                        return
                    }
                    Notification({
                        message: '入库成功',
                        type: 'success'

                    })
                    this.isCheck = false
                    this.turnOverRList()
                    loading.close()
                })
            },
            /**
             * 收料单原料送检
             */
            checkoutStatus(row, row2) {
                this.turnoverDetailId = row.id
                this.turnoverRecordId = row.turnoverRecordId
                this.checkEditForm = {
                    id: row.id,
                    checkoutStatus: '',
                    reason: ''
                }
                // this.scopeRow = row
                this.propsRow = row2
                this.checkEditFormVisible = true
            },
            /**
             * 收料单原料入库操作
             */
            warehouseStatus(row, row2) {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                turnoverDetailModel.updateTurnoverDetailWarehouseStatusByIdNew(row.id).then((data) => {
                    if (data.status == 'fail') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        loading.close()
                        return
                    }
                    Notification({
                        message: '入库成功',
                        type: 'success'
                    })
                    this.turnOverRList()
                    loading.close()
                })


            },
            editClick(row) {
                this.$store.commit('startLoading')
                turnoverRecordModel.turnoverRecordLoad(row.turnoverRecordId).then((dataR) => {
                    if (dataR.entity.status == '已入库' || dataR.entity.status == '部分入库') {
                        this.$message('选择收料单处于入库状态，不能修改！')
                        return
                    } else {
                        turnoverDetailModel.getTurnoverDetailByTurnoverRecordId(row.turnoverRecordId).then((dataD) => {
                            var info = {
                                id: dataR.entity.id,
                                freight: dataR.entity.freight,
                                shipments: dataR.entity.shipments,
                                departmentId: dataR.entity.departmentId,
                                stockId: dataR.entity.stockId,
                                shipmentsId: dataR.entity.shipmentsId,
                                transport: dataR.entity.transport,
                                licenseNo: dataR.entity.licenseNo,
                                remark: dataR.entity.remark,
                                turnovers: dataD.entity,
                            }
                            this.mode = 'update'
                            this.editForm = info
                            this.$store.commit('endLoading')
                            // materialStorageModel.getListPublicClear()
                            this.editFormVisible = true
                        })
                    }
                })
            },
            deleteClick(row) {
                this.$confirm('此操作将永久删除该收料单, 是否继续?', '提示', {
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
                    turnoverRecordModel.turnoverRecordDelete(row.turnoverRecordId).then((data) => {
                        if (data.status == 'fail') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            return
                        }
                        turnoverDetailModel.deleteByTurnoverRecordIds(row.turnoverRecordId).then(data => {
                            if (data.status == 'fail') {
                                Notification({
                                    message: data.msg,
                                    type: 'warning'
                                })
                                loading.close()
                                return
                            }
                        })
                        Notification({
                            message: '删除成功',
                            type: 'success'

                        })
                        this.turnOverRList()
                        loading.close()
                    })

                }).catch(() => {
                })
            },
            /**
             *模糊查询
             */
            submitForm(formName) {
                console.log('formName------>', formName)
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.turnOverRList()
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            authorized(index) {
                return menuTabService.authorized(this.$store, index)
            },
            handleSizeChange(val) {
                this.turnoverQuery.pageInfo.pageSize = val
                this.turnOverRList()
            },
            handleCurrentChange(val) {
                this.turnoverQuery.pageInfo.currentPage = val
                this.turnOverRList()
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            //编辑表单
            materialStorageEditFinish() {
                this.editFormVisible = false
                this.turnOverRList()
            },
            materialStorageEditCancel() {
                this.editFormVisible = false
                this.turnOverRList()
            },
            checkoutStatusFinishs() {
                this.checkEditFormVisible = false
                //this.turnOverRList()
            },
            checkoutStatusCancels() {
                this.checkEditFormVisible = false
                this.turnOverRList()
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.turnoverQuery.dates = ['', '']
                this.turnoverQuery.shipmentsId = ''
                this.turnoverQuery.status = ''
                this.turnoverQuery.departmentId = ''
                this.turnoverQuery.stockId = ''

            },
            materialExport() {
                this.editFormUpoloadVisible = true
            },
            downloadFinish() {
                this.editFormUpoloadVisible = false
            },
            downloadCancel() {
                this.editFormUpoloadVisible = false
            },
        },
    }
</script>

<style scoped>

</style>
