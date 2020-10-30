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
                  placeholder="单号"
                  v-model="turnoverQuery.turnoverDNo"
                  style="width:120px"
                  clearable
                >
                </el-input>
              </el-form-item>

              <el-form-item prop="stockId">
                <el-select
                  v-model="turnoverQuery.stockId"
                  style="width:120px"
                  placeholder="仓库"
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

              <el-form-item prop="dateCreated">
                <el-date-picker
                  v-model="turnoverQuery.dateCreatedOne"
                  type="date"
                  :formatter="formatterDate"
                  style="width: 165px"
                  placeholder="创建开始时间"
                >
                </el-date-picker>
                <el-date-picker
                  v-model="turnoverQuery.dateCreatedTwo"
                  type="date"
                  :formatter="formatterDate"
                  style="width: 165px"
                  placeholder="创建结束时间"
                >
                </el-date-picker>
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
          <el-table-column
            type="expand"
            width="50"
          >
            <template slot-scope="props">
              <!--:data=" props.row.floorVO_s"turnovers-->
              <el-table
                size="mini"
                :data="props.row.turnovers"
              >
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
                  prop="amount"
                  label="数量"
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
                prop="reason"
                label="备注"
                width="140">
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
            prop="turnoverRecordNo"
            label="编号"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="dateCreated"
            :formatter="formatterDate1"
            label="创建时间"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="stockName"
            label="仓库"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="createdBy"
            label="创建人"
          >
          </el-table-column>
          <el-table-column
            prop="reamrk"
            label="备注"
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

    <el-dialog
      :title="mode=='add'?'新建':'编辑'"
      v-if="editFormVisible"
      :visible.sync="editFormVisible"
      width="65%"
    >
      <turnover-record-edit
        :edit-form="editForm"
        :mode="mode"
        :departmentSelect="departmentSelect"
        :stockSelect="stockSelect"
        :shipmentsSelect="shipmentsSelect"
        :optionsProject="optionsProject"
        @turnoverRecord-edit-finish="materialStorageEditFinish"
        @turnoverRecord-edit-cancel="materialStorageEditCancel"
      ></turnover-record-edit>
    </el-dialog>
    <el-dialog
      :title="'收料单打印'"
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
import turnoverDetailModel from '../../../../model/materials/turnoverDetail'
import turnoverRecordModel from '../../../../model/materials/turnoverRecord'
import materialStorageModel from '../../../../model/basicdata/materialStorage'
import supplierModel from '../../../../model/basicdata/supplier'
import departmentModel from '../../../../model/basicdata/dept'
import stockModel from '../../../../model/basicdata/stock'
import menuTabService from '../../../../service/menuTabService'
import TurnoverRecordEdit from './InventorySupplementEdit.vue'
import DeliveryOrderInPreviewSJ from '../DeliveryOrderInPreviewSJ.vue'
import CheckoutStatus from '../CheckoutStatus.vue'
import projectModel from '../../../../model/project/project'
import serviceConfig from '../../../../../server.config.js'
import { Notification } from 'element-ui'
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
        dateCreatedOne: '',
        dateCreatedTwo: '',
        listType: '盘库补差',
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
      optionsProject: '',
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        },
        {
          name: '导出',
          event: 'exportExcel'
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
          authorized: 'turnoverRecordList:edit'
        },
        {
          name: '删除',
          event: 'deleteClick',
          authorized: 'turnoverRecordList:delete'
        },
        {
          name: '打印',
          event: 'printClick'
        }
      ]
    }
  },
  components: {
    'turnover-record-edit': TurnoverRecordEdit,
    'checkout-status': CheckoutStatus,
    'invoice-print-preview-sj': DeliveryOrderInPreviewSJ,
  },
  mounted() {
    this.shipmentsList();
    this.departmentList();
    this.stockList();
    this.turnOverRList()
    this.getAllProject()
  },
  methods: {
    exportExcel() {
      let url = serviceConfig.baseURL + '/pkbcListExport'
      let turnoverDNo = null;
      if (this.turnoverQuery.turnoverDNo) {
        turnoverDNo = this.turnoverQuery.turnoverDNo
      }
      let prams = {
        page: 1,
        pageSize: this.turnoverQuery.pageInfo.total,
        turnoverDNo: turnoverDNo,
        stockId: this.turnoverQuery.stockId,
        dateCreatedOne: this.turnoverQuery.dateCreatedOne,
        dateCreatedTwo: this.turnoverQuery.dateCreatedTwo
      }
      this.deliveryOrderQuery;
      this.exportExcelPost(url, prams)
    },
    exportExcelPost(URL, PARAMS) {
      var temp = document.createElement("form");
      temp.action = URL;
      temp.method = "post";
      temp.style.display = "none";
      for (var x in PARAMS) {
        var opt = document.createElement("textarea");
        opt.name = x;
        opt.value = PARAMS[x];
        temp.appendChild(opt);
      }
      document.body.appendChild(temp);
      temp.submit();
      return temp;
    },
    getAllProject() {
      projectModel.projectListS().then((data) => {
        this.optionsProject = data.entity
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
        for (var j = 0; j < data.entity.length; j++) {
          if (data.entity[j].deptName == '采购部公共') {
            this.baseDepartMentId = data.entity[j].id;
          }

        }
      })
    },
    stockList() {
      stockModel.getStockByOrgIdAndType().then((data) => {
        this.stockSelect = data.entity;
      })
    },

    turnOverRList() {
      this.turnovers = []
      turnoverRecordModel.getPKBCRecordLimit(this.turnoverQuery).then((data) => {
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
        remark: '',
        turnoverRecordId: '',
        listDoDetail: [],
        stockId: this.stockSelect[0].id,
        proposer: this.$store.state.user.username,
        purpose: '盘库补差',
        dateCreated: new Date()
      }
      materialStorageModel.getListPublicClear()//每次打开添加页面清空做假添加需要的list集合
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
    editClick(row) {
      let flag = false;
      for (let index = 0; index < this.$store.state.user.groups.length; index++) {
        if (this.$store.state.user.groups[index].id == '12' || this.$store.state.user.groups[index].groupName == '系统管理员') {
          flag = true;
        }
      };
      this.$store.commit('startLoading')
      turnoverRecordModel.turnoverRecordLoad(row.turnoverRecordId).then((dataR) => {
        if (dataR.entity.status == '已入库' && !flag) {
          this.$message('选择收料单处于入库状态，不能修改！')
          return
        } else {
          turnoverDetailModel.getTurnoverDetailByTurnoverRecordId(row.turnoverRecordId).then((dataD) => {

            var info = {
              id: dataR.entity.id,
              proposer: dataR.entity.createdBy,
              purpose: '盘库补差',
              stockId: dataR.entity.stockId,
              shipmentsId: dataR.entity.shipmentsId,
              transport: dataR.entity.transport,
              licenseNo: dataR.entity.licenseNo,
              remark: dataR.entity.source,
              listDoDetail: dataD.entity,
              dateCreated: new Date(moment(dataR.entity.dateCreated).format("YYYY-MM-DD HH:mm:ss").replace(/-/g, '/'))
            }

            this.mode = 'update'
            this.editForm = info
            this.$store.commit('endLoading')
            materialStorageModel.getListPublicClear()
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
    formatterDate1(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD");
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
    }
  },


}
</script>

<style scoped>
</style>