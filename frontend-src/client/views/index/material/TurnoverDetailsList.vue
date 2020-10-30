<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                            :inline="true"
                            id="projectList"
                            :model="turnoverQuery"
                            ref="turnoverQuery"
                        >
                            <el-form-item prop="turnoverDNo">
                                <el-input
                                    placeholder="磅单编号"
                                    v-model="turnoverQuery.turnoverDNo"
                                    style="width:120px"
                                    clearable
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="materialStorageId">
                                <el-input
                                    placeholder="原料编码"
                                    v-model="turnoverQuery.materialStorageIdS"
                                    style="width:120px"
                                    clearable
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="materialName">
                                <el-input
                                    placeholder="原料名称"
                                    v-model="turnoverQuery.materialName"
                                    style="width:120px"
                                    clearable
                                >
                                </el-input>
                            </el-form-item>
                            <!--  <el-form-item label="原料规格" prop="materialSpecification">
                        <el-input
                                placeholder="请输入内容"
                                v-model="turnoverQuery.materialSpecification"
                                style="width:120px"
                                clearable>
                        </el-input>
                    </el-form-item>-->
                            <!-- <el-form-item label="供方" prop="shipments">
                        <el-input
                                placeholder="请输入内容"
                                v-model="turnoverQuery.shipments"
                                style="width:120px"
                                clearable>
                        </el-input>
                    </el-form-item> -->
                            <el-form-item prop="shipments">
                                <el-select
                                    v-model="turnoverQuery.shipmentsId"
                                    style="width:120px"
                                    placeholder="供方选择"
                                    filterable
                                    clearable
                                >
                                    <el-option
                                        v-for="item in shipmentsSelect"
                                        :key="item.name"
                                        :label="item.name"
                                        :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item prop="departmentId">
                                <el-select
                                    v-model="turnoverQuery.departmentId"
                                    filterable
                                    style="width:120px"
                                    placeholder="部门选择"
                                    clearable
                                >
                                    <el-option
                                        v-for="item in departmentSelect"
                                        :key="item.id"
                                        :label="item.deptName"
                                        :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item prop="stockId">
                                <el-select
                                    v-model="turnoverQuery.stockId"
                                    style="width:120px"
                                    placeholder="仓库选择"
                                    clearable
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

                            <el-form-item prop="transport">
                                <el-input
                                    placeholder="运输单位"
                                    v-model="turnoverQuery.transport"
                                    style="width:150px"
                                    clearable
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="status">
                                <!--    multiple
                            collapse-tags-->
                                <el-select
                                    v-model="turnoverQuery.status"
                                    style="width:120px"
                                    placeholder="检验状态选择"
                                    clearable
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
                            <el-form-item prop="inventoryStatus">
                                <!--    multiple
                            collapse-tags-->
                                <el-select
                                    v-model="turnoverQuery.inventoryStatus"
                                    style="width:120px"
                                    placeholder="库存状态选择"
                                    clearable
                                >
                                    <el-option
                                        v-for="item in inventoryStatus"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="checkout">
                                <el-select
                                    v-model="turnoverQuery.checkout"
                                    placeholder="是否免检"
                                    clearable
                                    style="width:120px"
                                >
                                    <el-option
                                        v-for="item in options"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="dateCreated">
                                <el-date-picker
                                    v-model="turnoverQuery.dateCreatedOne"
                                    type="date"
                                    value-format="yyyy-MM-dd"
                                    :formatter="formatterDate"
                                    style="width:160px"
                                    placeholder="创建时间开始"
                                >
                                </el-date-picker>
                                <!--  <span style="margin-left: 35px;">至</span>-->
                                <el-date-picker
                                    v-model="turnoverQuery.dateCreatedTwo"
                                    type="date"
                                    value-format="yyyy-MM-dd"
                                    :formatter="formatterDate"
                                    style="width:160px"
                                    placeholder="创建时间结束"
                                >
                                </el-date-picker>
                            </el-form-item>
                        </el-form>
                    </el-col>

                    <el-col>
                        <my-search-button-group
                            :model="searchButtonData"
                        ></my-search-button-group>
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
                    :data="turnovers"
                    @selection-change="handleSelectionChange"
                    size="mini"
                    stripe
                    border
                    height="100%"
                    ref="handleClickRowSelection"
                    @row-click="handleClickRowSelection"
                    style="width:100%"
                >
                    <el-table-column type="selection" width="50">
                    </el-table-column>
                    <el-table-column type="index" label="序号" width="50">
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="turnoverRecordNo"
                        label="磅单编号"
                        width="120"
                    >
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="materialStorageId"
                        label="原料编码"
                        width="120"
                    >
                    </el-table-column>
                    <!--<el-table-column-->
                    <!--sortable-->
                    <!--prop="materialNo"-->
                    <!--label="原原料编码"-->
                    <!--width="130">-->
                    <!--</el-table-column>-->
                    <el-table-column
                        sortable
                        prop="materialName"
                        label="原料名称"
                        width="120"
                    >
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="materialSpecification"
                        label="原料规格"
                        width="120"
                    >
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="materialUnit"
                        label="计量单位"
                        width="120"
                    >
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="status"
                        label="检验状态"
                        width="120"
                    >
                        <template slot-scope="scope">
                            <span
                                v-if="scope.row.status == '退回'"
                                style="color: #F56C6C;font-weight: 800;"
                                >{{ scope.row.status }}</span
                            >
                            <span
                                v-else-if="scope.row.status == '待检验'"
                                style="color: #E6A23C;font-weight:800;"
                                >{{ scope.row.status }}</span
                            >
                            <span
                                v-else
                                style="color: #67C23A;font-weight:800;"
                                >{{ scope.row.status }}</span
                            >
                        </template>
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="warehouseStatus"
                        label="库存状态"
                        width="120"
                    >
                        <template slot-scope="scope">
                            <span
                                v-if="scope.row.warehouseStatus == '待入库'"
                                style="color: #F56C6C;font-weight: 800;"
                                >{{ scope.row.warehouseStatus }}</span
                            >
                            <span
                                v-else
                                style="color: #67C23A;font-weight:800;"
                                >{{ scope.row.warehouseStatus }}</span
                            >
                        </template>
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="warehouseTime"
                        label="入库时间"
                        width="150"
                    >
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="realityWeight"
                        label="实重"
                        width="120"
                    >
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="suttle"
                        label="净重"
                        width="120"
                    >
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="roughWeight"
                        label="毛重"
                        width="120"
                    >
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="tare"
                        label="皮重"
                        width="120"
                    >
                    </el-table-column>
                    <el-table-column prop="sundries" label="杂扣" width="120">
                    </el-table-column>
                    <el-table-column
                        sortable
                        v-if="show"
                        prop="dateCreated"
                        :formatter="formatterDate"
                        label="创建时间"
                        width="140"
                    >
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="createdBy"
                        label="创建人"
                        width="140"
                    >
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="checkout"
                        label="是否免检"
                        width="140"
                    >
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="univalence"
                        label="单价"
                        width="140"
                    >
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="totalUnivalence"
                        label="总价"
                        width="140"
                    >
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="shipments"
                        label="供方"
                        width="140"
                    >
                    </el-table-column>
                    <!--            <el-table-column
                                sortable
                                prop="transport"
                                label="运输单位"
                                width="140">
                        </el-table-column>-->
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
                        prop="freight"
                        label="运费"
                        width="140"
                    >
                    </el-table-column>
                    <el-table-column
                        sortable
                        prop="licenseNo"
                        label="车牌号"
                        width="140"
                    >
                    </el-table-column>
                    <el-table-column
                        sortable
                        v-if="show"
                        prop="transport"
                        label="退货原因"
                        width="140"
                    >
                    </el-table-column>
                    <el-table-column label="操作" fixed="right" :width="rowButtonGroupWidth(rowButtonData)">
                        <template slot-scope="scope">
                            <my-row-button-group
                                :row="scope.row"
                                :model="rowButtonData"
                            ></my-row-button-group>
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

        <!-- <el-button @click="deletesClicks" type="danger" v-if="authorized('turnoverDetailsList:delete')">
                            删除
                        </el-button>
                        <el-button @click="editClick" v-if="authorized('turnoverDetailsList:edit')">编辑</el-button>
                        &lt;!&ndash;     <el-button @click="check" type="primary">送检</el-button>&ndash;&gt;
                        <el-button @click="warehouse" type="primary">入库</el-button>-->
        <!-- </el-form-item>
       -->
        <el-dialog
            :title="mode == 'add' ? '新建' : '编辑'"
            v-if="editFormVisible"
            :visible.sync="editFormVisible"
        >
            <turnover-details-edit
                :edit-form="editForm"
                :mode="mode"
                :arrMaterialStorage="arrMaterialStorage"
                :departmentSelect="departmentSelect"
                :stockSelect="stockSelect"
                :shipmentsSelect="shipmentsSelect"
                @turnoverDetail-edit-finish="materialStorageEditFinish"
                @turnoverDetail-edit-cancel="materialStorageEditCancel"
            ></turnover-details-edit>
        </el-dialog>
        <!--     &lt;!&ndash;送检&ndash;&gt;
             <el-dialog  :visible.sync="checkEditFormVisible" width="30%">
                 <checkout-status :check-edit-form="checkEditForm"
                                  :turnoverDetailId="turnoverDetailId"
                                  :turnoverRecordId="turnoverRecordId"
                                  @checkout-status-finish="checkoutStatusFinish"
                                  @checkout-status-cancel="checkoutStatusCancel"
                 ></checkout-status>
             </el-dialog>-->
    </div>
</template>

<script>
import turnoverDetailModel from '../../../model/materials/turnoverDetail';
import turnoverRecordModel from '../../../model/materials/turnoverRecord';
import materialStorageModel from '../../../model/basicdata/materialStorage';
import supplierModel from '../../../model/basicdata/supplier';
import departmentModel from '../../../model/basicdata/dept';
import stockModel from '../../../model/basicdata/stock';
import menuTabService from '../../../service/menuTabService';
import CheckoutStatus from './CheckoutStatus.vue';
import TurnoverDetailsEdit from './TurnoverDetailsEdit.vue';
import { Notification } from 'element-ui';
import moment from 'moment';

export default {
    data() {
        return {
            show: '', //控制Table表格显示列数
            //入检所用Id
            turnoverRecordId: '',
            turnoverDetailId: '',
            arrMaterialStorage: [],
            isCheck: true,
            shipmentsSelect: [],
            departmentSelect: [],
            stockSelect: [],
            optionsStatus: [
                {
                    value: '待检验',
                    label: '待检验'
                },
                {
                    value: '合格',
                    label: '合格'
                },
                {
                    value: '退回',
                    label: '退回'
                },
                {
                    value: '让步接收',
                    label: '让步接收'
                }
            ],
            inventoryStatus: [
                {
                    value: '待入库',
                    label: '待入库'
                },
                {
                    value: '已入库',
                    label: '已入库'
                }
            ],
            turnovers: [],
            options: [
                {
                    value: '是',
                    label: '是'
                },
                {
                    value: '否',
                    label: '否'
                }
            ],
            turnoverQuery: {
                turnoverDNo: '',
                materialStorageIdS: '',
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
                inventoryStatus: '',
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: this.GLOBAL.pageSize,
                    pageSizes: this.GLOBAL.pageSizes
                }
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
            baseDepartMentId: '',
            buttonData: [
                {
                    name: '新建',
                    event: 'addClick'
                }
            ],
            searchButtonData: [
                {
                    name: '查询',
                    event: "submitForm('turnoverQuery')"
                }
            ],
            rowButtonData: [
                {
                    name: '编辑',
                    event: 'editClick',
                    authorized:'turnoverDetailsList:edit'
                },
                {
                    name: '删除',
                    event: 'deleteClick',
                    authorized:'turnoverDetailsList:delete'
                },
                {
                    name: '入库',
                    event: 'warehouse',
                    disabled:"scope.row.status == '合格' && scope.row.warehouseStatus =='已入库'",
                },
                {
                    name: '打印',
                    event: 'printClick'
                }
            ]
        };
    },
    components: {
        'turnover-details-edit': TurnoverDetailsEdit,
        'checkout-status': CheckoutStatus
    },
    mounted() {
        this.shipmentsList();
        this.departmentList();
        this.stockList();
        this.turnOverList();
    },
    methods: {
        printClick(row) {
            let arr = [];
            this.selected.forEach(row => {
                arr.push(row.id);
            });

            let info1 = row;
            let listDoDetail = [
                {
                    allTotalUnivalence: row.totalUnivalence,
                    amount: row.realityWeight,
                    createdBy: row.createdBy,
                    createdByName: row.createdByName,
                    dateCreated: row.dateCreated,
                    dateCreatedStr: null,
                    dateModified: row.dateModified,
                    inventory: row.inventory,
                    materialId: row.materialId,
                    materialName: row.materialName,
                    materialNo: row.materialNo,
                    materialSpecification: row.materialSpecification,
                    materialStorageId: row.materialStorageId,
                    materialUnit: row.materialUnit,
                    materialUnitName: row.materialUnitName,
                    modifiedBy: row.modifiedBy,
                    modifiedByName: row.modifiedByName,
                    orgId: row.orgId,
                    remark: row.reamrk,
                    totalQuantity: row.realityWeight,
                    totalUnivalence: row.totalUnivalence,
                    turnoverRecordId: row.id,
                    univalence: row.univalence
                }
            ];

            (info1.deliveryOrderNo = row.turnoverRecordNo),
                (info1.deliveryOrderTime = row.warehouseTime),
                (info1.deptName = row.deptName),
                (info1.id = row.id),
                //info1.proposer= (...),
                //info1.quantity=null,
                (info1.reamrk = row.reamrk),
                (info1.remark = row.reamrk),
                //info1.returnNum= null,
                //info1.sum= null
                (info1.supplierName = row.shipments),
                (info1.univalence = row.univalence);

            info1.listDoDetail = listDoDetail;
            this.$router.push({
                name: 'DeliveryOrderInPrintPreviewSJ',
                params: {
                    info: info1,
                    invoiceDetailIds: arr
                }
            });
        },
        shipmentsList() {
            supplierModel.listByOrgId().then(data => {
                this.shipmentsSelect = data.entity;
            });
        },
        departmentList() {
            departmentModel.listAll().then(data => {
                this.departmentSelect = data.entity;
                for (var j = 0; j < data.entity.length; j++) {
                    if (data.entity[j].id == data.msg) {
                        this.baseDepartMentId = data.entity[j].id;
                    }
                }
            });
        },
        stockList() {
            stockModel.getStockByOrgIdAndType().then(data => {
                this.stockSelect = data.entity;
            });
        },

        turnOverList() {
            turnoverDetailModel
                .getTurnoverLimit(this.turnoverQuery)
                .then(data => {
                    this.turnovers = data.entity.list;
                    this.turnoverQuery.pageInfo.total = data.entity.totalCount;
                    this.getMaterialStorage();
                });
        },
        addClick() {
            this.mode = 'add';
            this.editForm = {
                id: '',
                materialStorageId: '',
                materialName: '',
                materialSpecification: '',
                materialUnit: '',
                univalence: '',
                freight: '',
                shipments: '',
                // transport:'',
                licenseNo: '',
                roughWeight: '',
                tare: '',
                sundries: '',
                suttle: '',
                realityWeight: '',
                remark: '',
                turnoverRecordId: '',
                receipt: '',
                departmentId: this.baseDepartMentId,
                stockId: this.stockSelect[0].id
            };
            this.editFormVisible = true;
        },
        deletesClicks() {
            if (this.selected.length == 0) {
                this.$message('请选择删除项');
                return;
            }
            let ids = []; //turnoverDetail操作
            let idsR = []; //turnoverRecord操作
            this.selected.forEach(row => {
                ids.push(row.id);
                idsR.push(row.turnoverRecordId);
            });
            this.$confirm('此操作将永久删除勾选磅单, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
                .then(() => {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    turnoverDetailModel
                        .turnoverDetailDeletes(ids.join(';'))
                        .then(data => {
                            if (data.status == 'fail') {
                                Notification({
                                    message: data.msg,
                                    type: 'warning'
                                });
                                loading.close();
                                return;
                            }
                            turnoverRecordModel
                                .turnoverRecordDeletes(idsR.join(';'))
                                .then(data => {
                                    if (data.status == 'fail') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        });
                                        loading.close();
                                        return;
                                    }
                                    Notification({
                                        message: '删除成功',
                                        type: 'success'
                                    });
                                    this.turnOverList();
                                    loading.close();
                                });
                        });
                })
                .catch(() => {});
        },
        check() {
            if (this.selected.length == 0) {
                this.$message('请选择送检项');
                return;
            }
            var arr = new Array();
            this.selected.forEach(row => {
                arr.push(row);
            });
            if (arr.length > 1) {
                this.$message('只能勾选一个进行编辑！');
                return;
            }
            if (arr[0].status != '待检验') {
                this.$message('请查看选中磅单检验状态！');
                return;
            }
            this.turnoverDetailId = arr[0].id;
            this.turnoverRecordId = arr[0].turnoverRecordId;
            this.checkEditForm = {
                id: arr[0].id,
                checkoutStatus: '',
                reason: ''
            };
            this.checkEditFormVisible = true;
        },
        /**
         * 入库操作
         */
        warehouse(row) {
            /* if (this.selected.length == 0) {
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
       }*/
            if (row.status != '合格' && row.warehouseStatus != '待入库') {
                this.$message('请查看选中磅单检验状态和入库状态！');
                return;
            }
            if (row.warehouseStatus == '已入库') {
                this.$message('该磅单已入库！');
                return;
            }
            const loading = this.$loading({
                lock: true,
                text: 'Loading',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.7)'
            });
            turnoverDetailModel.turnoverDetailLoad(row.id).then(data => {
                if (data.status == 'fail') {
                    Notification({
                        message: data.msg,
                        type: 'warning'
                    });
                    loading.close();
                    return;
                }
                turnoverDetailModel
                    .updateTurnoverDetailWarehouseStatusById(data.entity)
                    .then(data1 => {
                        if (data.status == 'fail') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            });
                            loading.close();
                            return;
                        }
                        Notification({
                            message: '入库成功',
                            type: 'success'
                        });
                        this.turnOverList();
                        loading.close();
                    });
            });
        },
        editClick(row) {
            /*if (this.selected.length == 0) {
          this.$message('请选择编辑项');
          return;
      }
      var arr = new Array()
      this.selected.forEach((row) => {
          arr.push(row)
      })
      if (arr.length > 1) {
          this.$message('只能勾选一个进行编辑！');
          return;
      }*/
            turnoverDetailModel.turnoverDetailLoad(row.id).then(data => {
                turnoverRecordModel
                    .turnoverRecordLoad(row.turnoverRecordId)
                    .then(dataR => {
                        if (dataR.entity.status == '已入库') {
                            this.$message('选择磅单处于入库状态，不能修改！');
                            return;
                        } else {
                            var info = {
                                id: data.entity.id,
                                materialStorageId:
                                    data.entity.materialStorageId,
                                materialName: data.entity.materialName,
                                materialSpecification:
                                    data.entity.materialSpecification,
                                materialUnit: data.entity.materialUnit,
                                univalence: data.entity.univalence,
                                freight: dataR.entity.freight,
                                shipments: dataR.entity.shipments,
                                turnoverRecordId: data.entity.turnoverRecordId,
                                // transport:dataR.entity.transport,
                                departmentId: dataR.entity.departmentId,
                                stockId: dataR.entity.stockId,
                                shipmentsId: dataR.entity.shipmentsId,
                                licenseNo: dataR.entity.licenseNo,
                                roughWeight: data.entity.roughWeight,
                                tare: data.entity.tare,
                                sundries: data.entity.sundries,
                                suttle: data.entity.suttle,
                                realityWeight: data.entity.realityWeight,
                                remark: dataR.entity.remark,
                                totalUnivalence: data.entity.totalUnivalence
                            };
                            this.mode = 'update';
                            this.editForm = info;
                            this.editFormVisible = true;
                        }
                    });
            });
        },
        deleteClick(row) {
            this.$confirm('此操作将永久删除该磅单, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            })
                .then(() => {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    turnoverRecordModel
                        .turnoverRecordDelete(row.turnoverRecordId)
                        .then(data => {
                            if (data.status == 'fail') {
                                Notification({
                                    message: data.msg,
                                    type: 'warning'
                                });
                                return;
                            }
                            turnoverDetailModel
                                .turnoverDetailDelete(row.id)
                                .then(data => {
                                    if (data.status == 'fail') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        });
                                        loading.close();
                                        return;
                                    }
                                });
                            Notification({
                                message: '删除成功',
                                type: 'success'
                            });
                            this.turnOverList();
                            loading.close();
                        });
                })
                .catch(() => {});
        },
        /**
         *模糊查询
         */
        submitForm(formName) {
            this.$refs[formName].validate(valid => {
                if (valid) {
                    this.turnOverList();
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        getMaterialStorage() {
            materialStorageModel
                .getMaterialStorageByMaterialStorage()
                .then(data => {
                    var arr = new Array();
                    for (var i = 0; i < data.entity.length; i++) {
                        var materialStorageId = parseInt(data.entity[i])
                            .materialStorageId;
                        if (data.entity[i].materialSpecification == null) {
                            data.entity[i].materialSpecification = '';
                        }
                        var v = {
                            label: materialStorageId,
                            value:
                                data.entity[i].materialStorageId +
                                '[' +
                                data.entity[i].materialName +
                                ';' +
                                data.entity[i].materialSpecification +
                                ']'
                        };
                        arr.push(v);
                    }
                    this.arrMaterialStorage = arr;
                });
        },
        authorized(index) {
            return menuTabService.authorized(this.$store, index);
        },
        handleSizeChange(val) {
            this.turnoverQuery.pageInfo.pageSize = val;
            this.turnOverList();
        },
        handleCurrentChange(val) {
            this.turnoverQuery.pageInfo.currentPage = val;
            this.turnOverList();
        },
        handleSelectionChange(selected) {
            this.selected = selected;
        },
        formatterDate(row, column, cellValue, index) {
            var date = row[column.property];
            if (date == undefined) {
                return '';
            }
            return moment(date).format('YYYY-MM-DD HH:mm:ss');
        },
        //编辑表单
        materialStorageEditFinish() {
            this.editFormVisible = false;
            turnoverDetailModel
                .getTurnoverLimit(this.turnoverQuery)
                .then(data => {
                    this.turnovers = data.entity.list;
                    this.turnoverQuery.pageInfo.total = data.entity.totalCount;
                });
            this.turnOverList();
        },
        materialStorageEditCancel() {
            this.editFormVisible = false;
            this.turnOverList();
        },
        checkoutStatusFinish() {
            this.checkEditFormVisible = false;
            this.turnOverList();
        },
        checkoutStatusCancel() {
            this.checkEditFormVisible = false;
            this.turnOverList();
        }
    }
};
</script>

<style scoped></style>
