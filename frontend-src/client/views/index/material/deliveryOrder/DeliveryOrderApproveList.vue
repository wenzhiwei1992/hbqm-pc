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
              <el-form-item prop="shipments">
                <el-select
                  v-model="deliveryOrderQuery.status"
                  style="width: 150px"
                  placeholder="处理状态"
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
              <el-form-item prop="transport">
                <el-select
                  v-model="deliveryOrderQuery.departmentId"
                  style="width: 150px"
                  placeholder="部门"
                  filterable
                  clearable
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
              <el-form-item prop="proposer">
                <el-input
                  placeholder="申请人"
                  v-model="deliveryOrderQuery.proposer"
                  style="width:150px"
                  clearable
                >
                </el-input>
              </el-form-item>
              <el-form-item prop="status">
                <el-select
                  v-model="deliveryOrderQuery.purpose"
                  style="width: 150px"
                  placeholder="用途"
                  clearable
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
              <el-form-item prop="checkout">
                <el-select
                  v-model="deliveryOrderQuery.projectId"
                  placeholder="项目"
                  style="width: 150px"
                  clearable
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
                  style="width: 150px;"
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
            </el-form>
          </el-col>
          <el-col>
            <my-search-button-group :model="searchButtonData"></my-search-button-group>
          </el-col>
        </el-row>
        <el-row style="display:none;">
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
          @expand-change="showSubData"
          :default-sort="{prop: 'dateCreated', order: 'descending'}"
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
              <el-table
                size="mini"
                :data="props.row.listDoDetail"
              >
                <el-table-column
                  prop="status"
                  label="状态"
                  width="120"
                >
                  <template slot-scope="scope">
                    <span
                      v-if="scope.row.status=='待发料'"
                      style="color: #E6A23C;font-weight: 800;"
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
                  width="120"
                >
                </el-table-column>
                <el-table-column
                  prop="materialName"
                  label="原料名称"
                  width="120"
                >
                </el-table-column>
                <el-table-column
                  prop="materialSpecification"
                  label="原料规格"
                  width="120"
                >
                </el-table-column>
                <el-table-column
                  prop="materialUnit"
                  label="单位"
                  width="120"
                >
                </el-table-column>
                <el-table-column
                  prop="quantity"
                  label="申请量"
                  width="120"
                >
                </el-table-column>
                <!--              <el-table-column
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
                  width="120"
                >
                </el-table-column>
                <el-table-column
                  :formatter="formatterDate"
                  prop="datecreatedBy"
                  label="创建时间"
                  width="140"
                >
                </el-table-column>
                <el-table-column
                  prop="feedingTime"
                  label="发料时间"
                  width="140"
                >
                </el-table-column>
                <!--<el-table-column-->
                <!--label="操作"-->
                <!--align="left"-->
                <!--fixed="left"-->
                <!--width="150">-->
                <!--<template slot-scope="scope">-->
                <!--<span v-if="scope.row.status=='待发料'">-->
                <!--<el-button type="success"  @click="updateStatusPassThrough(scope.row,props.row)" circle>发料</el-button>-->
                <!--<el-button type="danger"   @click="updateStatusOverrule(scope.row,props.row)" circle>驳回</el-button>-->
                <!--</span>-->
                <!--</template>-->
                <!--</el-table-column>-->
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
                style="color: #E6A23C;font-weight: 800;"
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
            label="操作"
            align="left"
            width="150px"
          >
            <template slot-scope="scope">
              <span v-if="scope.row.status=='待发料'">
                <el-button
                  type="success"
                  @click="updateStatusDeliveryOrderPassThroughNew(scope.row,1)"
                  circle
                >发料</el-button>
                <el-button
                  type="danger"
                  size="mini"
                  @click="updateStatusDeliveryOrderPassThroughNew(scope.row,0)"
                  circle
                >驳回</el-button>
              </span>
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
      :visible.sync="checkEditFormVisible"
      width="30%"
    >
      <delivery-order-checkout-status-reason
        :check-edit-form="checkEditForm"
        :doDetailNew="doDetailNew"
        :doDetailOld="doDetailOld"
        :deliveryOrders="deliveryOrders"
        @checkout-status-finish="checkoutStatusFinish"
        @checkout-status-cancel="checkoutStatusCancel"
        @checkout-status-save="checkoutStatusSave"
      ></delivery-order-checkout-status-reason>
    </el-dialog>
  </div>
</template>

<script>
import doDetailModel from '../../../../model/materials/doDetail'
import deliveryOrderModel from '../../../../model/materials/deliveryOrder'
import deptModel from '../../../../model/basicdata/dept'
import projectModel from '../../../../model/project/project'
import DeliveryOrderEdit from './DeliveryOrderEdit.vue'
import DeliveryOrderCheckoutStatusReason from './DeliveryOrderCheckoutStatusReason.vue'
import { Notification } from 'element-ui'
import moment from 'moment'
export default {
  components: {
    'delivery-order-edit': DeliveryOrderEdit,
    'delivery-order-checkout-status-reason': DeliveryOrderCheckoutStatusReason
  },
  name: "DeliveryOrderList",
  mounted() {
    this.getAllDept()
    this.getAllProject()
    this.deliveryOrderList()
  },
  methods: {
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
    updateStatusDeliveryOrderPassThroughNew(row, type) {
      if (row.status == '已发料') {
        this.$message('该领料单已发料！');
        return;
      }
      if (row.status == '驳回') {
        this.$message('该领料单已驳回！');
        return;
      }
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      if (type == 1) {
        row.status = '发料';
      } else {
        row.status = '驳回';
      }

      deliveryOrderModel.updateStatusDeliveryOrderPassThroughNew(row).then((data) => {
        if (data.status == 'fail') {
          Notification({
            message: data.msg,
            type: 'warning'
          })
          loading.close()
          return
        }
        if (type == 1) {
          Notification({
            message: '发料成功',
            type: 'success'

          })
        } else {
          Notification({
            message: '驳回成功',
            type: 'success'

          })
        }

        this.deliveryOrderList()
        loading.close()
      })
    },
    updateStatusDeliveryOrderPassThrough() {
      if (this.selected.length == 0) {
        this.$message('请选择发料项');
        return;
      }
      var arr = new Array()
      this.selected.forEach((row) => {
        arr.push(row)
      })
      if (arr.length > 1) {
        this.$message('只能勾选一个进行发料！');
        return;
      }
      if (arr[0].status == '已发料') {
        this.$message('该领料单已发料！');
        return;
      }
      if (arr[0].status == '驳回') {
        this.$message('该领料单已驳回！');
        return;
      }
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      //  var deliveryOrder = row
      deliveryOrderModel.updateStatusDeliveryOrderPassThrough(arr[0]).then((data) => {
        if (data.status == 'fail') {
          Notification({
            message: data.msg,
            type: 'warning'
          })
          loading.close()
          return
        }
        Notification({
          message: '发料成功',
          type: 'success'

        })
        this.deliveryOrderList()
        loading.close()
      })
    },
    updateStatusDeliveryOrderOverrule() {
      if (this.selected.length == 0) {
        this.$message('请选择驳回项');
        return;
      }
      var arr = new Array()
      this.selected.forEach((row) => {
        arr.push(row)
      })
      if (arr.length > 1) {
        this.$message('只能勾选一个进行驳回！');
        return;
      }
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      //var deliveryOrder = row
      deliveryOrderModel.updateStatusDeliveryOrderOverrule(arr[0]).then((data) => {
        if (data.status == 'fail') {
          Notification({
            message: data.msg,
            type: 'warning'
          })
          loading.close()
          return
        }
        Notification({
          message: '驳回成功',
          type: 'success'

        })
        this.deliveryOrderList()
        loading.close()
      })
    },
    updateStatusPassThrough(row, row2) {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      var doDetail = row
      doDetailModel.updateStatusPassThrough(doDetail).then((data1) => {
        if (data1.status == 'fail') {
          Notification({
            message: data1.msg,
            type: 'warning'
          })
          loading.close()
          return
        }
        var arr = new Array()
        arr.push(data1.entity)
        arr.push(row2.listDoDetail)
        doDetailModel.changeStatus(arr).then((data) => {
          for (var j = 0; j < this.deliveryOrders.length; j++) {
            if (this.deliveryOrders[j].deliveryOrderId == data.entity[0].maDeliveryOrderId) {
              this.deliveryOrders[j].listDoDetail = data.entity;
              break;
            }
          }
          if (data1.entity.num == 1) {
            this.deliveryOrderList()
          }
          Notification({
            message: '出料成功',
            type: 'success'

          })
          loading.close()
        })
      })
    },
    updateStatusOverrule(row, row2) {
      this.doDetailNew = row
      this.doDetailOld = row2
      this.checkEditForm = {
        reason: ''
      }
      this.checkEditFormVisible = true

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
        }
        else {
          console.log('error submit!!');
          return false;
        }
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
    checkoutStatusFinish() {
      this.checkEditFormVisible = false
      this.deliveryOrderList()
    },
    checkoutStatusCancel() {
      this.checkEditFormVisible = false
      this.deliveryOrderList()
    },
    checkoutStatusSave() {
      this.checkEditFormVisible = false
    },
  },
  data() {
    return {
      doDetailNew: '', //驳回操作
      expands: [],
      doDetailOld: '', //驳回操作
      checkEditFormTitle: '',
      checkEditFormVisible: false,
      checkEditForm: null,
      value4: true,
      mold: '',
      selected: [],
      //编辑表单
      mode: '',
      editFormTitle: '',
      editFormVisible: false,
      editForm: null,
      deliveryOrders: [],
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
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        },
      },
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        }
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
          authorized: 'turnoverDetailsList:edit'
        },
        {
          name: '删除',
          event: 'deleteClick',
          authorized: 'turnoverDetailsList:delete'
        },
        {
          name: '入库',
          event: 'warehouse',
          disabled: "scope.row.status == '合格' && scope.row.warehouseStatus =='已入库'",
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
</style>
