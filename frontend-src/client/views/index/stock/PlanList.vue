<template>
  <div class="currency-style-box" id="plan-list">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form :inline="true">
              <el-form-item>
                <el-input
                    placeholder="发货计划号"
                    v-model="entity.planNo"
                    style="width: 150px;"
                    size="mini"
                    clearable>
                </el-input>
              </el-form-item>

              <el-form-item>
                <el-input
                    placeholder="计划状态"
                    v-model="entity.planStatus"
                    style="width: 150px;"
                    size="mini"
                    clearable>
                </el-input>
              </el-form-item>

              <el-form-item>
                <el-select v-model="entity.projectId"
                           placeholder="项目"
                           size="mini"
                           @change="changeProject"
                           style="width: 150px;"
                           clearable>
                  <el-option
                      v-for="item in projectList"
                      :key="item.id"
                      :label="item.projectName"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item>
                <el-select v-model="entity.buildId"
                           placeholder="楼栋"
                           size="mini"
                           style="width: 150px;"
                           :disabled="entity.projectId==''"
                           @change="changeBuilding"
                           clearable>
                  <el-option
                      v-for="item in buildCodeArr"
                      :key="item.id"
                      :label="item.buildCode"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item>
                <el-input
                    v-model="entity.productName"
                    placeholder="构件名称"
                    style="width: 150px"
                    size="mini"
                    clearable>
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-date-picker
                    v-model="entity.deliverDateRange"
                    type="daterange"
                    start-placeholder="计划开始日期"
                    end-placeholder="计划结束日期"
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
          <!-- <el-col> -->
          <my-button-group :model="buttonData"></my-button-group>
          <!-- </el-col> -->
          <!-- <el-col>
              <div>
                  <span class="color_font_span_class">总重量(t):</span>
                  <span class="color_span_class">{{productWtsNum}}</span>
              </div>
              <div>
                  <span class="color_font_span_class">总方量(m³):</span>
                  <span class="color_span_class">{{productVolsNum}}</span>
              </div>
          </el-col> -->
        </el-row>
      </el-header>
      <el-main
          ref="mainContent"
          :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <el-table
            :data="entities"
            ref="handleClickRowSelection planTable"
            height="100%"
            @selection-change="handleSelectionChange"
            style="width:100%"
            @row-click="clickRow"

            header-row-class-name="header-row"
            highlight-current-row
            row-class-name="table-row"
            stripe
            border
            tooltip-effect="light"
            row-key="id"
            @expand-change="showSubData"
            :expand-row-keys="expands"
        >
          <!--    @expand-change="showSubData"
              :expand-row-keys="expands"-->
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-table
                  :data="subDatas"
                  size="mini"
              >
                <el-table-column
                    type="index"
                    label="序号"
                    width="70">
                </el-table-column>
                <el-table-column
                    prop="buildCode"
                    label="楼栋"
                    align="center"
                    width="100"
                ></el-table-column>
                <el-table-column
                    prop="floorNum"
                    label="楼层"
                    align="center"
                    width="100"
                ></el-table-column>
                <el-table-column
                    prop="productNo"
                    label="构件编码"
                    align="center"
                    width="100"
                ></el-table-column>
                <el-table-column
                    prop="productName"
                    label="构件名称"
                    align="center"
                    width="100"
                ></el-table-column>
                <el-table-column
                    prop="productTypeName"
                    label="构件类型"
                    align="center"
                    width="100"
                ></el-table-column>
                <!-- <el-table-column
                            prop="productWt"
                            label="构件重量(t)"
                            align="center"
                            width="100"
                            :formatter="wtFormat"
                    ></el-table-column>
                    <el-table-column
                            prop="totalWt"
                            label="计划重量(t)"
                            align="center"
                            width="100">
                    </el-table-column>-->

                <el-table-column
                    prop="num"
                    label="计划件数"
                    align="center"
                    width="100">
                </el-table-column>
                <el-table-column
                    prop="notShipNum"
                    label="未发件数"
                    align="center"
                    width="100">
                </el-table-column>
              </el-table>
            </template>
          </el-table-column>
          <el-table-column
              type="selection"
              width="40">
          </el-table-column>
          <el-table-column
              type="index"
              label="序号"
              width="70">
          </el-table-column>
          <el-table-column
              prop="planNo"
              label="发货计划号"
              align="center"
              width="100">
          </el-table-column>
          <el-table-column
              prop="deliverDate"
              label="计划发货日期"
              align="center"
              :formatter="momentFormatDate"
              width="120">
          </el-table-column>
          <el-table-column
              prop="planStatus"
              label="计划状态"
              align="center"
              width="150">
          </el-table-column>
          <el-table-column
              prop="project.projectName"
              label="项目名称"
              align="center"
              width="100">
          </el-table-column>
          <el-table-column
              prop="buildIds"
              label="楼栋"
              align="center"
              width="100"
          ></el-table-column>
          <!-- <el-table-column
                  prop="totalNum"
                  label="总计划件数"
                  align="center"
                  width="100">
          </el-table-column>
          <el-table-column
                  prop="totalInvoiceNum"
                  label="发货待确认件数"
                  align="center"
                  width="140">
          </el-table-column>-->
          <el-table-column
              prop="productDetailNum"
              label="总计划件数"
              align="center"
              width="150">
          </el-table-column>
          <el-table-column
              prop="numShipped"
              label="已发件数"
              align="center"
              width="150">
          </el-table-column>
          <el-table-column
              prop="numShippedNot"
              label="未发件数"
              align="center"
              width="150">
          </el-table-column>
          <el-table-column
              prop="productWtsNum"
              label="总重量(t)"
              align="center"
              :formatter="wtFormat"
              width="100">
          </el-table-column>
          <el-table-column
              prop="productVolsNum"
              label="总方量(m³)"
              align="center"
              width="100"
              :formatter="wtFormat"
          ></el-table-column>
          <el-table-column
              prop="createdBy"
              label="创建人"
              align="center"
              width="150">
          </el-table-column>
          <el-table-column
              prop="dateCreated"
              label="创建时间"
              align="center"
              width="150"
              :formatter="formatterDate">
          </el-table-column>
          <el-table-column
              label="操作"
              align="center"
              fixed="right"
              :width="rowButtonGroupWidth(rowButtonData)"
          >
            <template slot-scope="scope">
              <my-row-button-group
                  :row="scope.row"
                  :model="rowButtonData"
              ></my-row-button-group>
              <!--  <el-button size="mini" @click="editClick(scope.row)">编辑</el-button>-->
              <!-- <el-button size="mini" type="danger" @click="deleteClick(scope.row)"
                      :disabled="scope.row.numShipped ==scope.row.productDetailNum ">删除
              </el-button> -->
              <!--<el-button size="medium" type="primary" @click="invoiceEditClick(scope.row)"
                      :disabled="scope.row.totalInvoiceNum>=scope.row.totalNum">新建发货单
              </el-button>
              <el-button size="medium" type="primary" @click="invoiceListClick(scope.row)">查看发货单</el-button>-->
            </template>
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer ref="footOperationBar">
        <el-row>
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="pageInfo.currentPage"
              :page-sizes="pageInfo.pageSizes"
              :page-size="pageInfo.pageSize"
              :total="pageInfo.total"
              layout="total, sizes, prev, pager, next, jumper"
              background
          >
          </el-pagination>
        </el-row>
      </el-footer>
    </el-container>
    <el-dialog :title="mode=='add'?'新建发货计划':'编辑发货计划'" append-to-body :visible.sync="editFormVisible"
               v-if="editFormVisible"
               width="80%">
      <model-edit :mode="mode" :plan="plan"
                  @model-edit-finish="modelEditFinish"
                  @model-edit-cancel="modelEditCancel"

      ></model-edit>
    </el-dialog>

    <el-dialog title="新建发货单" :visible.sync="invoiceEditFormVisible" v-if="invoiceEditFormVisible" width="85%">
      <invoice-model-edit
          :plan="plan"
          @model-edit-finish="invoiceModelEditFinish"
          @model-edit-cancel="invoiceModelEditCancel"

      ></invoice-model-edit>
    </el-dialog>

    <el-dialog title="发货单查看" :visible.sync="invoiceListVisible" v-if="invoiceListVisible" width="98%">
      <invoice-list :plan-id="planId"
                    @print-invoice-list="invoiceListCancel"
      ></invoice-list>
    </el-dialog>

  </div>


</template>
<script>
import model from '../../../model/model'
import projectModel from '../../../model/project/project'
import planDetailModel from '../../../model/plan/plan'
import moment from 'moment'
import {Notification} from 'element-ui'
import PlanEdit from './PlanEdit.vue'
import InvoiceEdit from './InvoiceEdit.vue'
import InvoiceList from './InvoiceList.vue'
import floorModel from "../../../model/project/floor";
import buildingModel from "../../../model/project/building";

var numeral = require('numeral')

export default {
  components: {
    'model-edit': PlanEdit,
    'invoice-model-edit': InvoiceEdit,
    'invoice-list': InvoiceList,
  },
  data() {
    return {
      invoices: [],
      //表查询
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: this.GLOBAL.pageSize,
        pageSizes: this.GLOBAL.pageSizes
      },
      entity: {
        planStatus: '',
        deliverDateRange: ['', '']
      },
      buildCodeArr: [],
      floorNumArr: [],
      projectList: [],
      entities: [],
      //行操作
      selected: [],
      planId: null,
      invoiceListVisible: false,
      //编辑表单
      mode: null,
      editFormVisible: false,
      plan: null,
      invoiceEditFormVisible: false,
      expands: [],
      subDatas: [],
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        },
        {
          name: '新建发货单',
          event: 'addInvoiceClick',
          authorized: 'plan:addInvoice',
        }
      ],
      searchButtonData: [
        {
          name: '查询',
          event: 'query'
        }
      ],
      rowButtonData: [
        {
          name: '删除',
          event: 'deleteClick',
          disabled: "scope.row.numShipped ==scope.row.productDetailNum"
        },
        {
          name: '编辑',
          event: 'editClick',
        },
      ]
    }
  },
  computed: {},
  mounted() {
    this.list()
    projectModel.projectListS().then(data => {
      this.projectList = data.entity;
    }).catch(() => {
      this.$message('加载项目信息失败')
    })

  },
  methods: {
    // 点击整行都可以进行勾选
    clickRow(row) {
      this.$refs.moviesTable.toggleRowSelection(row)
    },
    addInvoiceClick() {
      if (this.selected.length == 0) {
        this.$message(this.GLOBAL.PLAN_MSG.NOT_SELECT_ADD);
        return;
      }
      let arr = new Array()
      this.selected.forEach((row) => {
        arr.push(row)
      })
      if (arr.length > 1) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_ONE_ADD);
        return;
      }
      console.log('arr------>', arr[0])
      let invoices = {
        planNo: arr[0].planNo,
        planId: arr[0].id,
        projectId: arr[0].projectId,
      }
      this.mode = 'add'
      this.$router.push({
        name: 'invoiceEditS',
        params: {
          invoices: invoices,
          mode: this.mode
        }
      })
    },
    changeProject() {
      buildingModel.findByProjectId(this.entity.projectId).then(data => {
        this.buildCodeArr = data.entity
        this.entity.buildingId = ''
        this.entity.floorId = ''
        this.floorNumArr = []
      })
    },
    changeBuilding(id) {
      this.entity.floorId = ''
      this.floorNumArr = []
      if (id == undefined || id == '') {
        return
      }
      floorModel.findByBuildingIdAndProjectId(id, this.entity.projectId).then((data) => {
        this.floorNumArr = data.entity
        this.entity.floorId = ''
      })
    },
    showSubData(row) {
      this.subDatas = []
      if (this.expands.some((e) => e == row.id)) {
        this.expands = []
      } else {
        this.expands = []
        this.expands.push(row.id)
      }
      planDetailModel.findPlanDetailByPlanIdS(row.id).then((data) => {
        this.subDatas = data.entity
      })
    },
    //表查询
    list() {
      console.log("entity", this.entity.deliverDateRange);
      this.entity.apiType = 'pc'
      model.list('plan', this.entity, this.pageInfo).then(data => {
        let entity = data.entity
        let list = data.entity.content;
        /* list.forEach(p => {
             let totalInvoiceNum = 0
             p.planDetails.forEach(pd => {
                 totalInvoiceNum += pd.invoiceDetails.length
             })
             p.totalInvoiceNum = totalInvoiceNum
         })*/
        this.entities = list


        this.pageInfo.total = data.entity.totalElements
      })
    },
    query() {
      this.pageInfo.currentPage = 1
      this.list()
    },
    handleSelectionChange(selected) {
      this.selected = selected
    },
    handleSizeChange(val) {
      this.pageInfo.currentPage = 1
      this.pageInfo.pageSize = val
      this.list()
    },
    handleCurrentChange(val) {
      this.pageInfo.currentPage = val
      this.list()
    },
    momentFormatDate(row, column, cellValue, index) {
      if (cellValue) {
        return moment(cellValue).format('YYYY-MM-DD')
      } else {
        return cellValue
      }

    },
    momentFormatTime(row, column, cellValue, index) {
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
    //编辑表单
    modelEditFinish() {
      this.editFormVisible = false
      this.list()
    },
    modelEditCancel() {
      this.editFormVisible = false
    },
    addClick() {
      this.mode = 'add'
      this.editFormVisible = true
    },
    invoiceEditClick(row) {

      this.plan = row
      this.invoiceEditFormVisible = true
      /*      this.$router.push({
                name:'invoiceEdit',
                params:{
                    plan:row
                }
            })*/

    },
    invoiceModelEditFinish() {
      this.list()
      this.invoiceEditFormVisible = false
    },
    invoiceModelEditCancel() {
      this.invoiceEditFormVisible = false
    },

    //行操作
    invoiceListClick(row) {


      this.planId = row.id
      this.invoiceListVisible = true
    },
    invoiceListCancel() {
      this.invoiceListVisible = false
    },
    editClick(row) {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      this.plan = row
      this.mode = this.GLOBAL.STATUS.UPDATE
      planDetailModel.findPlanDetailByPlanIdToPowerfulEdit(row.id).then((data) => {
        if (data.status == 'failed') {
          Notification({
            message: data.msg,
            type: 'warning'
          })
          loading.close()
          return
        }
        this.plan.planDetails = data.entity
        this.editFormVisible = true
        loading.close()
      })
      //let test = new Array()


      // floorRelevanceModel.findFloorRelevanceToPlanByPlanId(row.id).then((data)=>{
      //     console.log("data.entity",data.entity)
      //     this.plan = data.entity
      //     this.mode = 'update'
      //     this.editFormVisible = true
      // })
      // console.log("row.planDetails",row.planDetails)
      // if(row.planDetails[0].product){
      //     row.planDetails.forEach(fp => {
      //         let planDetail = {}
      //         planDetail.num = fp.num
      //         planDetail.wt = fp.productWt
      //         planDetail.totalWt = fp.totalWt
      //         planDetail.relevanceId = fp.id
      //         planDetail.productId = fp.productId,
      //             planDetail.productNo = fp.product.productNo,
      //             planDetail.productName = fp.product.productName,
      //             planDetail.buildingId = fp.buildingId,
      //             planDetail.buildCode = fp.floorRelevance.building.buildCode,
      //             planDetail.floorId = fp.floorId,
      //             planDetail.floorNum = fp.floorRelevance.floor.floorNum,
      //             planDetail.requirements = fp.floorRelevance.num,
      //             planDetail.invoiceDetails = fp.invoiceDetails
      //         test.push(planDetail)
      //     })
      //     this.plan.planDetails = test
      // }


    },
    deleteClick(row) {
      // if(row.groups&&row.groups.length>0){
      //     Notification({
      //         message: '该用户下有未删除的角色',
      //         type: 'warning'
      //
      //     })
      //     return
      // }
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.commit('startLoading')
        model.delete('plan', row.id).then(data => {
          this.$store.commit('endLoading')
          this.list()
        })
      }).catch(() => {
      })
    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },

  },

}
</script>
<style>
.el-table th, .el-table tr.header-row {
  background-color: #f5f7fa;
  font-size: 1.1em;
}

#plan-list .el-table th, .el-table tr.table-row {
  cursor: pointer;
}

#plan-list .el-table__expanded-cell[class*=cell] {
  padding: 0px 50px;
}

#entity .el-form-item {
  margin-bottom: 5px;
}
</style>
