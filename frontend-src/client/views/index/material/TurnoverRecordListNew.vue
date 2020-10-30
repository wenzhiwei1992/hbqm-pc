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
                                >
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
                    <my-button-group :model="buttonData" style="display: none"></my-button-group>
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
                        <template slot-scope="props">
                            <el-table
                                    size="mini"
                                    :data="props.row.turnovers"
                            >
                                <el-table-column
                                        prop="materialStorageId"
                                        label="原料编码"
                                        width="140">
                                </el-table-column>
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
                                        v-if="props.row.type==1"
                                        prop="roughWeight"
                                        label="毛重"
                                        width="140">
                                </el-table-column>
                                <el-table-column
                                        v-if="props.row.type==1"
                                        prop="tare"
                                        label="皮重"
                                        width="140">
                                </el-table-column>
                                <el-table-column
                                        v-if="props.row.type==1"
                                        prop="sundries"
                                        label="杂扣"
                                        width="140">
                                </el-table-column>
                                <el-table-column
                                        v-if="props.row.type==1"
                                        prop="suttle"
                                        label="净重"
                                        width="140">
                                </el-table-column>

                                <el-table-column
                                        v-if="props.row.type==1"
                                        prop="amount"
                                        label="实重"
                                        width="140">
                                </el-table-column>




                                <el-table-column
                                        v-if="props.row.type==2"
                                        prop="amount"
                                        label="数量"
                                        width="140">
                                </el-table-column>
                                <el-table-column
                                        prop="univalence"
                                        label="单价(含税)"
                                        width="140">
                                </el-table-column>
                                <el-table-column
                                        prop="totalUnivalence"
                                        label="总价(含税)"
                                        width="140">
                                </el-table-column>
                                <el-table-column
                                        prop="taxRate"
                                        label="税率(%)"
                                        width="140">
                                </el-table-column>
                                <el-table-column
                                        prop="totalUnivalenceNo"
                                        label="总价(不含税)"
                                        width="140">
                                </el-table-column>
                                <el-table-column
                                        prop="materialUnit"
                                        label="单位"
                                        width="140">
                                </el-table-column>
                                <el-table-column
                                        prop="checkoutStatus"
                                        label="检验状态"
                                        width="140">
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
                                        width="140">
                                    <template slot-scope="scope">
                                        <span v-if="scope.row.warehouseStatus=='待入库'"
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
                                        width="140">
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
        <el-dialog :title="mode=='add'?'新建':'编辑'" v-if="editFormVisible" :visible.sync="editFormVisible" width="65%">
            <turnover-record-edit :edit-form="editForm"
                                  :mode="mode"
                                  :departmentSelect="departmentSelect"
                                  :stockSelect="stockSelect"
                                  :shipmentsSelect="shipmentsSelect"
                                  @turnoverRecord-edit-finish="materialStorageEditFinish"
                                  @turnoverRecord-edit-cancel="materialStorageEditCancel"
            ></turnover-record-edit>
        </el-dialog>
        <el-dialog :title="'收料单打印'" :visible.sync="invoicePrintPreviewSJVisible"
                   v-if="invoicePrintPreviewSJVisible" width="85%">
            <invoice-print-preview-sj
                    :invoiceIdSJ="invoiceIdSJ"
                    @invoice-print-preview-sj-finish="invoicePrintPreviewSJFinish"
                    @invoice-print-preview-sj-cancel="invoicePrintPreviewSJCancel"
            ></invoice-print-preview-sj>
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
    import TurnoverRecordEdit from './TurnoverRecordEditNew.vue'
    import DeliveryOrderInPreviewSJ from './DeliveryOrderInPreviewSJ.vue'
    import {Notification} from 'element-ui'
    import moment from "moment";

    export default {
        data() {
            return {
                invoicePrintPreviewSJVisible: false,
                shipmentsSelect: [],
                departmentSelect: [],
                stockSelect: [],
                optionsStatus: [
                    {
                        value: '已入库',
                        label: '已入库'
                    }

                ],
                turnovers: [],
                options: [{
                    value: '是',
                    label: '是'
                }, {
                    value: '否',
                    label: '否'
                }],
                buttonData: [
                    {
                        name: '新建',
                    }
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
                ],
                turnoverQuery: {
                    listType:'updatePrice',
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
            }
        },
        components: {
            'turnover-record-edit': TurnoverRecordEdit,
            'invoice-print-preview-sj': DeliveryOrderInPreviewSJ,
        },
        mounted() {
            this.shipmentsList();
            this.departmentList();
            this.stockList();
            this.turnOverRList()
        },
        methods: {
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
                })
            },
            stockList() {
                stockModel.getStockByOrgIdAndType().then((data) => {
                    this.stockSelect = data.entity;
                })
            },

            turnOverRList() {
                this.turnovers = []
                turnoverRecordModel.getTurnoverRecordLimit(this.turnoverQuery).then((data) => {
                    for (var i = 0; i < data.entity.list.length; i++) {
                        var turnoverRecordId = data.entity.list[i].turnoverRecordId
                        if (turnoverRecordId) {
                            turnoverDetailModel.getTurnoverDetailByTurnoverRecordId(turnoverRecordId).then((data1) => {
                                if (data1.entity) {
                                    for (var j = 0; j < this.turnovers.length; j++) {
                                        if (this.turnovers[j].turnoverRecordId == data1.entity[0].turnoverRecordId) {
                                            this.turnovers[j].turnovers = data1.entity;
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
            editClick(row) {
                turnoverRecordModel.turnoverRecordLoad(row.turnoverRecordId).then((dataR) => {
                turnoverDetailModel.getTurnoverDetailByTurnoverRecordId(row.turnoverRecordId).then((dataD) => {
                    this.editForm = {
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
                        projectId: dataR.entity.projectId,
                        type:dataR.entity.type,
                        dateCreated: new Date(moment(dataR.entity.dateCreated).format("YYYY-MM-DD HH:mm:ss").replace(/-/g, '/'))
                    }
                    this.mode = 'update'
                    this.editFormVisible = true
                })
                })
            },
            /**
             *模糊查询
             */
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.turnOverRList()
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            }
            ,
            authorized(index) {
                return menuTabService.authorized(this.$store, index)
            }
            ,
            handleSizeChange(val) {
                this.turnoverQuery.pageInfo.pageSize = val
                this.turnOverRList()
            }
            ,
            handleCurrentChange(val) {
                this.turnoverQuery.pageInfo.currentPage = val
                this.turnOverRList()
            }
            ,
            handleSelectionChange(selected) {
                this.selected = selected
            }
            ,
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            }
            ,
            //编辑表单
            materialStorageEditFinish() {
                this.editFormVisible = false
                this.turnOverRList()
            }
            ,
            materialStorageEditCancel() {
                this.editFormVisible = false
                this.turnOverRList()
            }
            ,
        },


    }
</script>

<style scoped>

</style>