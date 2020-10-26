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
              <el-form-item prop="materialStorageId">
                <el-input
                  placeholder="原料编码"
                  v-model="turnoverQuery.materialStorageIdS"
                  style="width:150px"
                  clearable
                >
                </el-input>
              </el-form-item>
              <el-form-item prop="materialName">
                <el-input
                  placeholder="原料名称"
                  v-model="turnoverQuery.materialName"
                  style="width:150px"
                  clearable
                >
                </el-input>
              </el-form-item>
              <el-form-item prop="dateCreated">
                <el-date-picker
                  v-model="turnoverQuery.dateCreatedOne"
                  type="date"
                  :formatter="formatterDate"
                  style="width: 150px"
                  placeholder="选择开始日期"
                >
                </el-date-picker>
                <el-date-picker
                  v-model="turnoverQuery.dateCreatedTwo"
                  type="date"
                  :formatter="formatterDate"
                  style="width: 150px"
                  placeholder="选择结束日期"
                >
                </el-date-picker>
              </el-form-item>
              <el-form-item prop="materialType">
                <el-select
                  v-model="turnoverQuery.materialType"
                  style="width: 150px"
                  placeholder="磅单/收料单"
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
              <el-form-item prop="orgId">
                <el-select
                  v-model="turnoverQuery.orgId"
                  style="width: 150px"
                  placeholder="基地名称"
                  clearable
                >
                  <el-option
                    v-for="item in orgs"
                    :key="item.id"
                    :label="item.orgName"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
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
          height="100%"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
          style="width:100%"
        >
          <el-table-column
            type="index"
            label="序号"
            width="50"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="materialStorageId"
            label="原料编码"
            width="120"
          >
          </el-table-column>
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
            prop="categoryName"
            label="原料类型"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="materialType"
            label="收料方式"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="checkoutStatus"
            label="检验状态"
            width="120"
          >
            <template slot-scope="scope">
              <span
                v-if="scope.row.checkoutStatus=='不合格'"
                style="color: #F56C6C;font-weight:800;"
              >{{scope.row.checkoutStatus}}</span>
              <span
                v-else-if="scope.row.checkoutStatus=='待检验'"
                style="color: #E6A23C;font-weight:800;"
              >{{scope.row.checkoutStatus}}</span>
              <span
                v-else
                style="color: #67C23A;font-weight:800;"
              >{{scope.row.checkoutStatus}}</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            prop="orgName"
            label="基地名称"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="dateCreated"
            :formatter="formatterDate"
            label="创建时间"
            width="150"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="checkoutTime"
            label="检验时间"
            width="150"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="modifiedBy"
            label="检验人员"
            width="120"
          >
          </el-table-column>
          <!--   <el-table-column
                       sortable
                       fixed="left"
                       label="操作"
                       align="left"
                       width="120">
                   <template slot-scope="scope">
                       &lt;!&ndash;        <el-popover
                                       placement="right"
                                       title="原料检验"
                                       width="400"
                                       trigger="click"
                                       content="这是一段内容,这是一段内容,这是一段内容,这是一段内容。">
                                   <checkout-status :check-edit-form="checkEditForm"
                                                    :turnoverDetailId="turnoverDetailId"
                                                    :turnoverRecordId="turnoverRecordId"
                                                    @checkout-status-finish="checkoutStatusFinish"
                                                    @checkout-status-cancel="checkoutStatusCancel"
                                   ></checkout-status>

                               </el-popover>&ndash;&gt;
                       <el-button  slot="reference"  v-if="scope.row.checkoutStatus=='待检验'" size="mini" @click="check(scope.row)" type="primary">检验</el-button>
                   </template>
               </el-table-column>-->
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
import orgModel from '../../../model/system/org'
import CheckoutStatus from './CheckoutStatus.vue'
import { Notification } from 'element-ui'
import moment from "moment";

export default {
  components: {
    'checkout-status': CheckoutStatus,
  },
  name: "ShowMaterialCheckStatus",
  data() {
    return {
      visible: false,
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
      orgs: [],
      activeName: 'second',
      turnoverQuery: {
        isCheck: 2,
        materialStorageIdS: '',
        materialName: '',
        materialSpecification: '',
        shipments: '',
        transport: '',
        checkout: '',
        status: '',
        dateCreated: '',
        dateCreatedOne: '',
        dateCreatedTwo: '',
        materialType: '',
        orgId: '',
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

      ]
    }
  },
  mounted() {
    this.info()
    this.showList()
  },
  methods: {
    showList() {
      turnoverDetailModel.getPoundListCheck(this.turnoverQuery).then((data) => {
        this.turnovers = data.entity.list
        this.turnoverQuery.pageInfo.total = data.entity.totalCount
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.showList()
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
      //this.visible = true
      this.checkEditFormVisible = true
    },
    info() {
      orgModel.orgAllByOrgIds().then((data) => {
        this.orgs = data.entity
      })
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
      this.showList()
    },
    handleCurrentChange(val) {
      this.turnoverQuery.pageInfo.currentPage = val
      this.showList()
    },
    checkoutStatusFinish() {
      this.checkEditFormVisible = false
      //this.visible = false
      this.showList()
    },
    checkoutStatusCancel() {
      this.checkEditFormVisible = false
      //this.visible = false
      this.showList()
    }
  }
}
</script>

<style scoped>
</style>