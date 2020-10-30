<template>
  <div>
    <el-form
      :model="invoicesQuery"
      v-if="invoicesQuery"
      ref="invoicesQuery"
      :rules="eidtFormRules"
      label-width="100px"
      size="small"
      :inline="true"
    >
      <el-row>
        <el-row :gutter="24">
          <el-col :span="4">
            <el-form-item
              label="发货单号"
              prop="deliverGoodsNo"
            >
              <el-input
                v-model="invoicesQuery.deliverGoodsNo"
                style="width: 150px"
                size="mini"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item
              label="质检人"
              prop="inspectorId"
            >
              <el-select
                v-model="invoicesQuery.inspectorId"
                style="width: 150px"
                size="mini"
              >
                <el-option
                  v-for="item in inspectorList"
                  :key="item.id"
                  :label="item.inspector"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item
              label="物流公司"
              prop="logCompanyId"
            >
              <el-select
                v-model="invoicesQuery.logCompanyId"
                style="width: 150px"
                size="mini"
              >
                <el-option
                  v-for="item in logCompanyList"
                  :key="item.id"
                  :label="item.logCompany"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item
              label="车牌号"
              prop="licenseNo"
            >
              <el-input
                v-model="invoicesQuery.licenseNo"
                style="width: 150px"
                size="mini"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item
              label="工字钢"
              prop="ibeam"
            >
              <el-input-number
                v-model="invoicesQuery.ibeam"
                style="width: 150px"
                size="mini"
                :precision="2"
              ></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item
              label="方木"
              prop="squareWood"
            >
              <el-input-number
                v-model="invoicesQuery.squareWood"
                style="width: 150px"
                size="mini"
                :precision="2"
              ></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="4">
            <el-form-item
              label=""
              prop="invoiceDate"
            >
              <el-date-picker
                type="date"
                style="width: 150px;"
                size="mini"
                placeholder="发货日期"
                v-model="invoicesQuery.invoiceDate"
              ></el-date-picker>
            </el-form-item>

          </el-col>
          <el-col :span="4">
            <el-form-item>
              <el-select
                      v-model="invoicesQuery.trayIds"
                      @change="functiontest"
                      clearable
                      style="width:150px"
                      size="mini"
                      multiple
                      collapse-tags
                      filterable
                      reserve-keyword
                      placeholder="请输入托盘号"
                      :loading="loading"
              >
                <el-option
                        v-for="item in options"
                        :key="item.id"
                        :label="item.trayNo"
                        :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item
              label=""
              prop="projectId"
            >
              <el-select
                v-model="invoicesQuery.projectId"
                placeholder="项目名称"
                style="width: 150px"
                :disabled="this.newInvoiceDetails!=''"
                clearable
                size="mini"
              >
                <el-option
                  v-for="item in projects"
                  :key="item.id"
                  :label="item.projectName"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="4">
            <el-form-item label="">
              <el-button
                type="success"
                size="mini"
                :disabled="this.invoicesQuery.projectId==''"
                style="width: 150px"
                @click="addProductDetail"
              >
                添加构件明细
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="">
              <el-button
                type="success"
                size="mini"
                :disabled="this.selected.length <= 0"
                style="width: 150px"
                @click="addBuildAndFloor"
              >
                批量添加楼栋楼层
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-row>
    </el-form>
    <el-table
      :data="newInvoiceDetails"
      ref="multipleTable"
      :height="400"
      header-row-class-name="header-row"
      highlight-current-row
      @selection-change="handleSelectionChange"
      size="mini"
      stripe
      border
      tooltip-effect="light"
      row-key="productDetailId"
    >
      <el-table-column
        type="selection"
        width="40"
      >
      </el-table-column>
      <el-table-column
        type="index"
        label="序号"
        width="60"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="productDetailNo"
        label="明细编码"
        align="center"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="projectName"
        label="项目名称"
        align="center"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="buildCode"
        label="楼栋"
        align="center"
        width="100"
      >
        <template slot-scope="scope">
          <el-select
            clearable
            v-model="scope.row.buildCode"
            @focus="focusBuild(scope.row)"
            @change="changeBuildList(scope.row)"
          >
            <el-option
              v-for="item in buildList"
              :key="item.id"
              :label="item.buildCode"
              :value="item.buildCode"
            >
            </el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column
        prop="floorNum"
        label="楼层"
        align="center"
        width="100"
      >
        <template slot-scope="scope">
          <el-select
            clearable
            :disabled="scope.row.buildCode===''"
            @focus="focusFloor(scope.row)"
            @change="changeFloor(scope.row)"
            v-model="scope.row.floorNum"
          >
            <el-option
              v-for="item in floors"
              :key="item.id"
              :label="item.floorNum"
              :value="item.floorNum"
            >
            </el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column
        prop="productNo"
        label="构件编码"
        align="center"
        width="90"
      ></el-table-column>
      <el-table-column
        prop="productName"
        label="构件名称"
        align="center"
        width="90"
      ></el-table-column>
      <el-table-column
        prop="productWt"
        label="构件重量"
        align="center"
        width="90"
      ></el-table-column>
      <el-table-column
        prop="projectConcrete"
        label="构件方量"
        align="center"
        width="90"
      ></el-table-column>
      <el-table-column
        prop="stockName"
        label="库区号"
        align="center"
        width="80"
      >
      </el-table-column>
      <el-table-column
        prop="shelfName"
        label="货架号"
        align="center"
        width="80"
      >
      </el-table-column>
      <el-table-column
              prop="trayNo"
              label="托盘号"
              align="center"
              width="80"
      >
      </el-table-column>
      <!--            <el-table-column
                                      prop="prodTime"
                                      label="入库时间"
                                      align="center"
                                      width="150"
                                      :formatter="momentFormatTime"
                              ></el-table-column>-->
      <el-table-column
        label="操作"
        align="center"
      >
        <template slot-scope="scope">
          <el-button
            type="danger"
            round
            :disabled="scope.row.invoiceDetailStatus=='发货已确认'"
            @click="deleteInvoiceDetail(scope.$index)"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-row
      type="flex"
      justify="space-around"
      align="middle"
    >
      <el-col>
        <el-button
          type="primary"
          @click="editFormSubmmit('invoicesQuery')"
          size="mini"
        >确 定
        </el-button>
        <el-button
          @click="editFormCancel"
          size="mini"
        >取 消
        </el-button>
        <!-- <el-button
          @click="deleteInvoiceDetail"
                    type="danger"
          size="mini"
        >删 除</el-button> -->
      </el-col>
      <el-col>
        <!--  <el-row type="flex" justify="end" align="middle">
                              <el-pagination
                                      @size-change="handleSizeChange"
                                      @current-change="handleCurrentChange"
                                      :current-page="invoicesQuery.pageInfo.currentPage"
                                      :page-sizes="invoicesQuery.pageInfo.pageSizes"
                                      :page-size="invoicesQuery.pageInfo.pageSize"
                                      :total="invoicesQuery.pageInfo.total"
                                      layout="total, sizes, prev, pager, next, jumper"
                                      background
                              >
                              </el-pagination>
                          </el-row>-->

      </el-col>
    </el-row>
    <el-dialog
      append-to-body
      :visible.sync="productDetailChooseVisible"
      width="90%"
      v-if="productDetailChooseVisible"
    >
      <product-detail-choose
        :newInvoiceDetails="newInvoiceDetails"
        :projectId="projectId"
        @product-detail-choose-finish="productDetailChooseFinish"
      ></product-detail-choose>
    </el-dialog>
    <el-dialog
      append-to-body
      :visible.sync="selectBAndFVisible"
      width="30%"
      v-if="selectBAndFVisible"
    >
      <invoice-new-select-b-and-f
        :projectId="projectId"
        :invoicesSelect="invoicesSelect"
        :selectBAndFVisible="selectBAndFVisible"
        @dialogCancel="dialogCancel"
      ></invoice-new-select-b-and-f>
    </el-dialog>

  </div>
</template>

<script>
import TrayModel from "../../../../model/basicdata/tray";

var numeral = require('numeral')
import planModel from '../../../../model/plan/plan'
import projectModel from '../../../../model/project/project'
import buildModel from '../../../../model/project/building'
import floorModel from '../../../../model/project/floor'
import InvoiceNewAdd from './InvoiceNewAdd.vue'
import invoiceNewSelectBAndF from './InvoiceNewSelectBAndF.vue'
import qualityInspectionModel from '../../../../model/basicdata/qualityInspectionModel'
import { Notification } from "element-ui";
import moment from 'moment'

export default {
  name: "InvoiceNewList",
  //  props: ['invoices', 'mode'],
  components: {
    'product-detail-choose': InvoiceNewAdd,
    'invoice-new-select-b-and-f': invoiceNewSelectBAndF,
  },
  data() {
    return {
      invoicesQuery: {
        deliverGoodsNo: '',
        inspectorId: '',
        logCompanyId: '',
        licenseNo: '',
        squareWood: '',
        projectId: '',
        mode: '',
        trayId:'',
        trayIds:[],
        trays:[],
        invoiceDate: null,
        // invoiceDate:new Date().getFullYear() + '-' + (new Date().getMonth() + 1)+ '-' + new Date().getDate(),
        invoiceDetails: [],
        ibeam: '',
        apiType: 'pc',//用于区分pc端和phone端调用新建发货单Api
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        },
      },
      newInvoiceDetails: [],
      productDetailChooseVisible: false,
      inspectorList: [],
      logCompanyList: [],
      projects: [],
      projectId: '',
      selected: [],
      //批量选择楼栋楼层
      invoicesSelect: [],
      selectBAndFVisible: false,
      eidtFormRules: {
        /*   deliverGoodsNo: [
               {required: true, message: '请填写发货单号', trigger: 'blur'},
           ],*/
        inspectorId: [
          { required: true, message: '请选择质检人', trigger: 'blur' },
        ],
        logCompanyId: [
          { required: true, message: '请选择物流公司', trigger: 'blur' },
        ],
        licenseNo: [
          { required: true, message: '请填写车牌号', trigger: 'blur' },
        ],
        /*  ibeam: [
              {required: true, message: '请填写工字钢', trigger: 'blur'},
          ],
          squareWood: [
              {required: true, message: '请填写方木', trigger: 'blur'},
          ],*/
        trayIds: [
          {required: true, message: '请选择托盘', trigger: 'change'},
        ],
        projectId: [
          { required: true, message: '请选择项目', trigger: 'change' },
        ],
        invoiceDate: [
          { required: true, message: '请选择发货日期', trigger: 'change' },
        ],


      },
      // 下拉框列表
      buildList: [],
      floors: [],
      loading: false,
      trayList:[],
      options:[],
      value:[],
      id:[],
    }
  },
  mounted() {
    this.getTrayOption().then(_=>{
      this.init()
    })
    // this.list()
  },
  methods: {
    /*       list() {
               planModel.findInvoiceNewList(this.invoicesQuery).then((data) => {
                   this.newInvoiceDetails = data.entity.list
                   this.invoicesQuery.pageInfo.total = data.entity.total
               })

                      let id = this.$route.params.id;
        if (id === undefined || id == null) {
            this.$router.push({name: 'invoiceNewList'})
            return

        }
           },*/

    functiontest(){
      log(this.invoicesQuery.trays)
    },
    getTrayOption(){
        this.loading = true
        //根据query的值去远程检索数据
        return TrayModel.findAllByOrgId().then(data => {
          this.options = data.entity;
          this.options = this.options.filter(s => s.trayStatus != '发货中')
          this.loading = false
        }).catch(err => {
          console.error("err", err);
          this.loading = false
        })
    },
    // remoteMethod(query){
    //   if(query != ''){
    //     this.loading = true;
    //     //根据query的值去远程检索数据
    //     trayModel.findAll(query).then(data => {
    //       this.options = data.entity;
    //       this.options = this.options.filter(s => s.trayStatus != '发货中')
    //     }).catch(err => {
    //       console.error("err", err);
    //     })
    //     setTimeout(() => {
    //       this.loading = false;
    //     }, 200);
    //   }else{
    //     this.options = [];
    //   }
    // },
    addProductDetail() {
      if (this.invoicesQuery.projectId == '') {
        this.$message('请选择项目');
        return;
      }
      this.projectId = this.invoicesQuery.projectId
      this.productDetailChooseVisible = true
    },
    productDetailChooseFinish(planDetail, productDetails) {
      productDetails.forEach(pd => {
        this.newInvoiceDetails.push(pd)
      })
      this.productDetailChooseVisible = false
      //  this.$refs.planDetails.toggleRowExpansion(planDetail)
      //  this.$refs.planDetails.toggleRowExpansion(planDetail)//TO SPECIAL:解决本次发货数页面不更新的问题
    },
    addBuildAndFloor() {
      if (this.selected.length == 0) {
        this.$message('请选择添加项');
        return;
      }
      this.invoicesSelect = this.selected
      this.selectBAndFVisible = true
    },
    deleteInvoiceDetail(index) {
      // this.selected.forEach((val,valIndex)=>{
      // 	this.newInvoiceDetails.forEach((item,itemIndex)=>{
      // 		if(val.productDetailId === item.productDetailId){
      // 			this.newInvoiceDetails.splice(itemIndex, 1)
      // 		}
      // 	})
      // })
      this.$refs.multipleTable.clearSelection()
      this.newInvoiceDetails.splice(index, 1)
      // this.selected = []
    },
    init() {
      let mode = this.$route.params.mode;
      let invoices = this.$route.params.invoices;
      if (mode === undefined || mode == null) {
        this.$router.push({ name: 'invoiceNewList' })
        return
      }

      //新增操作清空集合
      if (mode == 'add') {
        this.newInvoiceDetails = []
      }
      if (mode == 'update') {
        this.invoicesQuery = invoices
        if(this.invoicesQuery.trayId){
          let trayId = this.invoicesQuery.trayId.split(',')
          this.invoicesQuery.trayIds = trayId.map(val=>{
            return Number(val)
          })
        }

        console.log('newInvoiceDetails------>', this.invoicesQuery.invoiceDetails)
        console.log('newInvoiceDetails------>', this.invoicesQuery)
        this.newInvoiceDetails = this.invoicesQuery.invoiceDetails
      }

      //2019年8月15日17:25:56 李强 替换为查询该组织下的质检人
      qualityInspectionModel.allByOrgId().then(data => {
        data.entity.forEach(q => {
          if (q.type == '质检人') {
            this.inspectorList.push(q)
          } else if (q.type == '物流公司') {
            this.logCompanyList.push(q)
          }
        })
      }).catch(() => {
      })
      projectModel.getExternalAllProject().then((data) => {
        this.projects = data.entity
      })

    },
    editFormSubmmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let mode = this.$route.params.mode;
          if (mode === undefined || mode == null) {
            this.$router.push({ name: 'invoiceNewList' })
            return
          }
          this.invoicesQuery.invoiceDetails = this.newInvoiceDetails
          this.invoicesQuery.ibeam = numeral(this.invoicesQuery.ibeam).format('0.00')
          this.invoicesQuery.squareWood = numeral(this.invoicesQuery.squareWood).format('0.00')
          this.invoicesQuery.invoice = this.invoice
          this.invoicesQuery.mode = mode
          //用于后段调用限定API调取相应方法
          let deciceInfo = {
            invoiceDetailList: this.invoicesQuery.invoiceDetails,
            modeDecideInvoice: mode,
          }

          //限定
          planModel.loadFloorRelevanceIdAndDecide(deciceInfo).then((data) => {
            if (data.status == "failed") {
              Notification({
                message: data.msg,
                type: "warning"
              });
              return;
            }
            if (mode == 'add') {
              const loading = this.$loading({
                lock: true,
                text: 'Loading',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.7)'
              });
              console.log("invoicesQuery",this.invoicesQuery);
              planModel.addNew(this.invoicesQuery).then((data) => {
                if (data.status == "failed") {
                  Notification({
                    message: data.msg,
                    type: "warning"
                  });
                  loading.close()
                  return;
                }
                this.invoicesQuery.projectId == ''
                Notification({
                  message: "新建成功",
                  type: "success"
                });
                loading.close();
                this.$router.push({ name: 'invoiceNewList' })
                // this.$emit('model-edit-finish')
              })
            } else if (mode == 'update') {
              const loading = this.$loading({
                lock: true,
                text: 'Loading',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.7)'
              });
              this.invoicesQuery.apiType = 'pc'
              planModel.updateNewInvoice(this.invoicesQuery).then((data) => {
                if (data.status == "failed") {
                  Notification({
                    message: data.msg,
                    type: "warning"
                  });
                  loading.close()
                  return;
                }
                Notification({
                  message: "编辑成功",
                  type: "success"
                });
                loading.close();
                // this.$emit('model-edit-finish')
                this.$router.push({ name: 'invoiceNewList' })
              })
            }
          })
        }
      })
    },
    editFormCancel() {
      this.newInvoiceDetails = []
      this.$router.push({ name: 'invoiceNewList' })
      // this.$emit('model-edit-cancel')
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
    handleSelectionChange(selected) {
      if (this.newInvoiceDetails.length) {
        this.selected = selected
      }
    },
    handleSizeChange(val) {
      this.invoicesQuery.pageInfo.currentPage = 1
      this.invoicesQuery.pageInfo.pageSize = val
      this.list()
    },
    handleCurrentChange(val) {
      this.invoicesQuery.pageInfo.currentPage = val
      this.list()
    },
    focusBuild(row) {
      this.buildList = []
      let model = {
        projectId: row.projectId,
        productId: row.productId,
      }
      console.log('productId---->', row.productId)
      buildModel.findBuildingsByProjectIdAndProductId(model).then((data) => {
        this.buildList = data.entity
      })
    },
    focusFloor(row) {
      this.floors = []
      let model = {
        projectId: row.projectId,
        productId: row.productId,
        buildId: row.buildCode
      }
      //只改楼层操作执行
      let valueBuildCode = typeof row.buildCode
      if (valueBuildCode == 'string') {
        model.buildId = row.buildId
      }
      if (row.buildCode != '' && row.projectId != '' && row.productId != '') {
        floorModel.findFloorsByProjectIdAndProductIdAndBuildId(model).then((data) => {
          this.floors = data.entity
        })
      }
    },
    // 关闭批量添加弹窗
    dialogCancel() {
      this.selectBAndFVisible = false
    },
    changeFloor(row) {
      let that = this
      that.floors.forEach((val, index) => {
        if (row.floorNum === val.floorNum) {
          row.floorId = val.id
        }
      })
    },
    changeBuildList(row) {
      let that = this
      that.floors = []
      row.floorNum = ''
      that.buildList.forEach((val, index) => {
        if (row.buildCode === val.buildCode) {
          row.buildId = val.id
        }
      })
    },
  }
}
</script>

<style scoped>
</style>
