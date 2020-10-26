<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form
              :inline="true"
              :model="turnoverQuery"
              ref="turnoverQuery"
            >
              <el-form-item  prop="turnoverDNo">
                <el-input
                        placeholder="收料单编号"
                        v-model="turnoverQuery.turnoverDNo"
                        style="width:120px"
                        clearable>
                </el-input>
              </el-form-item>

              <el-form-item  prop="shipments">
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

              <el-form-item  prop="departmentId">
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
                        type="daterange"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        range-separator="至"
                        start-placeholder="创建时间开始"
                        end-placeholder="创建时间结束"
                        :default-time="['00:00:00', '23:59:59']">
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
          :data="turnovers"
          size="mini"
          :default-sort="{prop: 'dateCreated', order: 'descending'}"
          stripe
          border
          style="width:100%"
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
                        prop="amount"
                        label="数量"
                        width="140">
                </el-table-column>
                <el-table-column
                        prop="univalence"
                        label="单价"
                        width="140">
                </el-table-column>
                <el-table-column
                        prop="totalUnivalence"
                        label="总价"
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
                    <span v-if="scope.row.checkoutStatus=='退回'" style="color: #F56C6C;font-weight:800;">{{scope.row.checkoutStatus}}</span>
                    <span v-else style="color: #67C23A;font-weight:800;">{{scope.row.checkoutStatus}}</span>
                  </template>
                </el-table-column>
                <el-table-column
                        prop="warehouseStatus"
                        label="库存状态"
                        width="140">
                  <template slot-scope="scope">
                    <span v-if="scope.row.warehouseStatus=='待入库'" style="color: #F56C6C;font-weight:800;">{{scope.row.warehouseStatus}}</span>
                    <span v-else
                          style="color: #67C23A;font-weight:800;">{{scope.row.warehouseStatus}}</span>
                  </template>
                </el-table-column>
                <el-table-column
                        
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
                <el-table-column
                        prop="reason"
                        label="备注">
                </el-table-column>
                <el-table-column
                        label="操作"
                        align="left"
                        fixed="left"
                        width="160">
                  <template slot-scope="scope">
                    <div v-if="scope.row.checkoutStatus=='待检验'">
                      <el-button type="primary" size="mini" @click="check(scope.row)">
                        检验
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
                  
                  prop="dateCreated"
                  :formatter="formatterDate1"
                  label="创建时间"
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
                    width="120">
                <template slot-scope="scope">
                    <span v-if="scope.row.status=='待入库'" style="color: #F56C6C">{{scope.row.status}}</span>
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
                  width="140">
          </el-table-column>
          <el-table-column
                  
                  prop="deptCode"
                  label="部门编号"
                  width="140">
          </el-table-column>
          <el-table-column
                  
                  prop="stockName"
                  label="仓库"
                  width="140">
          </el-table-column>
          <el-table-column
                  
                  prop="freight"
                  label="运费"
                  width="140">
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
                  
                  prop="createdBy"
                  label="创建人"
                  width="140">
          </el-table-column>
          <el-table-column
                  
                  prop="dateModified"
                  :formatter="formatterDate1"
                  label="修改时间"
                  width="140">
          </el-table-column>
          <el-table-column
                  label="操作"
                  fixed="right"
                  width="60">
            <template slot-scope="scope">
              <el-tooltip v-if="scope.row.status==''" class="item" effect="light" content="一键检验" placement="top">
                <el-button type="primary"  icon="el-icon-s-promotion"  @click="checkNew(scope.row)"></el-button>
              </el-tooltip>
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

    <!--送检-->
    <el-dialog
      :visible.sync="checkEditFormVisible"
      width="30%"
    >
      <checkout-status
        :check-edit-form="checkEditForm"
        :turnoverDetailId="turnoverDetailId"
        :turnoverRecordId="turnoverRecordId"
        @checkout-status-finish="checkoutStatusFinish"
        @checkout-status-cancel="checkoutStatusCancel"
      ></checkout-status>
    </el-dialog>
  </div>
</template>

<script>
import turnoverDetailModel from '../../../model/materials/turnoverDetail'
import turnoverRecordModel from '../../../model/materials/turnoverRecord'
import CheckoutStatus from './CheckoutStatus.vue'
import supplierModel from '../../../model/basicdata/supplier'
import departmentModel from '../../../model/basicdata/dept'
import stockModel from '../../../model/basicdata/stock'
import { Notification } from 'element-ui'
import moment from "moment";

export default {
  components: {
    'checkout-status': CheckoutStatus,
  },
  name: "TurnoverCheckList",
  data() {
    return {
      visible: false,
        shipmentsSelect : [],
        departmentSelect: [],
        stockSelect: [],
      optionsStatus: [
        {
          value: '1',
          label: '磅单'
        },
        {
          value: '2',
          label: '收料单'
        },
      ],
      //入检所用Id
      turnoverRecordId: '',
      turnoverDetailId: '',
      checkEditFormTitle: '',
      checkEditFormVisible: false,
      checkEditForm: null,
      turnovers: [],
      activeName: 'second',
        turnoverQuery: {
            listType : '原料检验',
            turnoverDNo: '',
            materialStorageId: '',
            materialName: '',
            materialSpecification: '',
            shipments: '',
            shipmentsId:'',
            stockId:'',
            departmentId:'',
            transport: '',
            checkout: '',
            templateCheckArr:[],
            status: '',
            dates:['', ''],
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
  },
  mounted() {
      this.shipmentsList();
      this.departmentList();
      this.stockList();
      this.turnOverRList();
      this.checkTemplate()
  },
  methods: {
      checkTemplate() {
          let templateNo = '物料模板'
          turnoverRecordModel.getCheckTemplate(templateNo).then((data) => {
              this.templateCheckArr = data.entity;
          })
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
                  if(data.entity[j].deptName=='采购部公共'){
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
      checkNew(row) {
          this.turnoverDetailId = ''
          this.turnoverRecordId = row.turnoverRecordId
          this.checkEditForm = {
              id: '',
              checkoutStatus: '',
              reason: ''
          }
          this.checkEditFormVisible = true
      },
      formatterDate1(row, column, cellValue, index) {
          var date = row[column.property];
          if (date == undefined) {
              return "";
          }
          return moment(date).format("YYYY-MM-DD");
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
                                      for(var k = 0; k < this.turnovers[j].turnovers.length; k++){
                                          for(var v = 0; v < this.templateCheckArr.length; v ++){
                                              if(this.templateCheckArr[v].templateTypeId == this.turnovers[j].turnovers[k].materialTypeId){
                                                  this.turnovers[j].turnovers[k].templateUrl = this.templateCheckArr[v].templatePath+"?embed=true&turnover_detail_id="+this.turnovers[j].turnovers[k].id;
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
    check(row) {

      if (row.checkoutStatus != '待检验') {
        this.$message('请查看选中原料的检验状态！');
        return;
      }
      this.turnoverDetailId = row.id
      this.turnoverRecordId = row.turnoverRecordId
      this.checkEditForm = {
        id: row.id,
        checkoutStatus: '',
        reason: ''
      }
      this.checkEditFormVisible = true
    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    handleSizeChange(val) {
      this.turnoverQuery.pageInfo.pageSize = val
      this.turnOverRList()
    },
    handleCurrentChange(val) {
      this.turnoverQuery.pageInfo.currentPage = val
      this.turnOverRList()
    },
    checkoutStatusFinish() {
      this.checkEditFormVisible = false
      //this.visible = false
      this.turnOverRList()
    },
    checkoutStatusCancel() {
      this.checkEditFormVisible = false
      //this.visible = false
      this.turnOverRList()
    }
  }
}
</script>

<style scoped>
</style>