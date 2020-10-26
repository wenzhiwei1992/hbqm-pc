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

              <el-form-item prop="materialName">
                <el-input
                  placeholder="计划编号"
                  v-model="deliveryOrderQuery.orderNo"
                  style="width:150px"
                  clearable
                >
                </el-input>
              </el-form-item>
              <!-- <el-form-item label="部门" prop="transport">
          <el-select
            clearable
            v-model="deliveryOrderQuery.departmentId"
            style="width: 150px"
            placeholder="请选择"
          >
            <el-option
              v-for="item in optionsDept"
              :key="item.id"
              :label="item.deptName"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item> -->

              <el-form-item prop="dateCreated">
                <el-date-picker
                  v-model="deliveryOrderQuery.dateCreatedOne"
                  type="date"
                  :formatter="formatterDate"
                  style="width: 150px"
                  placeholder="创建开始时间"
                ></el-date-picker>
                <el-date-picker
                  v-model="deliveryOrderQuery.dateCreatedTwo"
                  type="date"
                  :formatter="formatterDate"
                  style="width: 150px"
                  placeholder="创建结束时间"
                ></el-date-picker>
              </el-form-item>
              <!-- <el-form-item label="发料时间" prop="deliveryOrderTime">
          <el-date-picker
            v-model="deliveryOrderQuery.deliveryOrderTimeOne"
            type="date"
            :formatter="formatterDate"
            style="width: 150px"
            placeholder="选择日期"
          ></el-date-picker>至
          <el-date-picker
            v-model="deliveryOrderQuery.deliveryOrderTimeTwo"
            type="date"
            :formatter="formatterDate"
            style="width: 150px"
            placeholder="选择日期"
          ></el-date-picker>
        </el-form-item> -->
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
        >
          <el-table-column
            type="expand"
            width="50"
          >
            <template slot-scope="props">
              <el-table
                size="mini"
                :data="props.row.listDetail"
              >
                <el-table-column
                  prop="materialStorageNo"
                  label="原料编码"
                ></el-table-column>
                <!--<el-table-column prop="materialNo" label="原原料编码" ></el-table-column>-->
                <el-table-column
                  prop="materialName"
                  label="原料名称"
                ></el-table-column>
                <el-table-column
                  prop="materialSpecification"
                  label="原料规格"
                ></el-table-column>
                <el-table-column
                  prop="materialUnit"
                  label="单位"
                ></el-table-column>
                <el-table-column
                  prop="quantity"
                  label="计划申请量"
                ></el-table-column>
                <el-table-column
                  prop="applyTotal"
                  label="实际申请量"
                ></el-table-column>
                <el-table-column
                  prop="claimant"
                  label="申请人"
                ></el-table-column>
                <el-table-column
                  prop="applyStatus"
                  label="领料状态"
                ></el-table-column>
              </el-table>
            </template>
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            width="50"
          ></el-table-column>
          <el-table-column
            sortable
            prop="deliveryNum"
            label="计划领料编号"
            width="180"
          ></el-table-column>
          <el-table-column
            sortable
            prop="status"
            label="状态"
          ></el-table-column>
          <!-- <el-table-column sortable prop="departmentName" label="部门" ></el-table-column> -->
          <el-table-column
            sortable
            prop="createdBy"
            label="创建人"
          ></el-table-column>
          <el-table-column
            sortable
            prop="dateCreated"
            :formatter="formatterDate"
            label="创建时间"
          ></el-table-column>
          <el-table-column
            sortable
            prop="plan_date_from"
            :formatter="formatterDateNoTime"
            label="开始时间"
          ></el-table-column>
          <el-table-column
            sortable
            prop="plan_date_to"
            :formatter="formatterDateNoTime"
            label="结束时间"
          ></el-table-column>
          <!-- <el-table-column
        sortable
        prop="deliveryOrderTime"
        :formatter="formatterDate"
        label="领料时间"
        
      ></el-table-column> -->
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
          ></el-pagination>
        </el-row>
      </el-footer>
    </el-container>

  </div>
</template>

<script>
import doDetailModel from "../../../../model/materials/doDetail";
import deliveryOrderModel from "../../../../model/materials/deliveryOrder";
import materialStorageModel from "../../../../model/basicdata/materialStorage";
import deptModel from "../../../../model/basicdata/dept";
import deliveryPlanModel from "../../../../model/materials/deliveryPlanOrder";
import DeliveryOrderEdit from "../deliveryOrder/DeliveryOrderEdit.vue";
import DeliveryOrderReturn from "../deliveryOrder/DeliveryOrderReturn.vue";
import serviceConfig from '../../../../../server.config.js'
import { Notification } from "element-ui";
import moment from "moment";

export default {
  components: {
    "delivery-order-edit": DeliveryOrderEdit,
    "delivery-order-return": DeliveryOrderReturn
  },
  name: "DeliveryOrderList",
  mounted() {
    this.getAllDept();
    this.deliveryOrderList();
  },
  methods: {
    exportExcel() {
      var url = serviceConfig.baseURL + '/deliveryPlanOrderListExport'
      let pageInfo = this.deliveryOrderQuery.pageInfo;
      pageInfo.pageSize = pageInfo.total;
      let orderNo = null;
      if (this.deliveryOrderQuery.orderNo) {
        orderNo = this.deliveryOrderQuery.orderNo
      }
      var prams = {
        page: 1,
        pageSize: pageInfo.total,
        orderNo: orderNo
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
    deliveryOrderList() {
      deliveryPlanModel
        .getDeliveryPlanOrderLimit(this.deliveryOrderQuery)
        .then(data => {
          console.log(data)
          this.deliveryOrders = data.entity.list;
          console.log(data.entity.list)
          this.deliveryOrderQuery.pageInfo.total = data.entity.totalCount;
        });
    },
    /**
     *模糊查询
     */
    submitForm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.deliveryOrderList();
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },


    returnNum(row, row2) {
      /*                this.returnEditForm = {
                                    materialStorageId:row.materialStorageId,
                                    materialName:row.materialName,
                                    materialSpecification:row.materialSpecification,
                                    quantity:row.quantity,
                                    materialUnit:row.materialUnit,
                                    univalence:row.univalence,
                                    returnNum:'',
                              }*/
      this.returnEditForm = row;
      this.scopeRow = row;
      this.propsRow = row2;
      /*                console.log('TEST------row2');//TO DELETE
                                console.log(row2);//TO DELETE
                                console.log('TEST------this.propsRow');//TO DELETE
                                console.log(this.propsRow);//TO DELETE*/
      this.returnEditFormVisible = true;
    },
    deliveryOrderEditFinish() {
      this.editFormVisible = false;
      this.deliveryOrderList();
    },
    deliveryOrderEditCancel() {
      this.editFormVisible = false;
      this.deliveryOrderList();
    },
    returnFinish() {
      this.returnEditFormVisible = false;
    },
    returnCancel() {
      this.returnEditFormVisible = false;
      this.deliveryOrderList();
    },
    getAllDept() {
      deptModel.listAll().then(data => {
        this.optionsDept = data.entity;
      });
    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    formatterDateNoTime(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD");
    },
    handleSelectionChange(selected) {
      this.selected = selected;
    },
    handleSizeChange(val) {
      this.deliveryOrderQuery.pageInfo.pageSize = val;
      this.deliveryOrderList();
    },
    handleCurrentChange(val) {
      this.deliveryOrderQuery.pageInfo.currentPage = val;
      this.deliveryOrderList();
    }
  },
  data() {
    return {
      dotailStatusNum: 0,
      mold: "",
      selected: [],
      //编辑表单
      mode: "",
      editFormTitle: "",
      stockSelect: [],
      editFormVisible: false,
      editForm: null,
      deliveryOrders: [],
      listDoDetail: [], //退料操作
      optionsProject: "",
      optionsPurpose: [
        {
          value: "生产",
          label: "生产"
        },
        {
          value: "维修",
          label: "维修"
        },
        {
          value: "办公",
          label: "办公"
        },
        {
          value: "固定资产",
          label: "固定资产"
        }
      ],
      optionsDept: "",
      optionsStatus: [
        {
          value: "待发料",
          label: "待发料"
        },
        {
          value: "驳回",
          label: "驳回"
        },
        {
          value: "已发料",
          label: "已发料"
        }
      ],
      deliveryOrderQuery: {
        status: "",
        departmentId: "",
        purpose: "",
        projectId: "",
        proposer: "",
        dateCreated: "",
        dateCreatedOne: "",
        dateCreatedTwo: "",
        deliveryOrderTime: "",
        deliveryOrderTimeOne: "",
        deliveryOrderTimeTwo: "",
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        }
      },
      returnEditFormVisible: false,
      returnEditForm: "",
      scopeRow: "",
      propsRow: "",
      buttonData: [
        {
          name: '导出',
          event: 'exportExcel'
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
    };
  }
};
</script>

<style scoped>
.formClass {
  border: 1px lavender solid;
}
</style>