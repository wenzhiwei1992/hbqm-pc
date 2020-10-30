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
        <el-row :gutter="12">
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
          <!--<el-col :span="4">
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
          </el-col>-->
          <el-col :span="4">
            <el-form-item prop="planNo">
              <el-input style="width: 150px" placeholder="发货计划号"
                        disabled
                        v-model="invoicesQuery.planNo"
                        size="mini"></el-input>
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
            <el-form-item label="">
              <!-- <el-button
                       type="success"
                       size="mini"
                       icon="el-icon-plus"

               >
                   新建
               </el-button>-->
            </el-form-item>
          </el-col>
        </el-row>
      </el-row>
    </el-form>
    <el-table
        :data="planDetails"
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
          prop="projectName"
          label="项目名称"
          align="center"
          width="100"
      ></el-table-column>
      <el-table-column
          prop="buildCode"
          label="楼栋"
          align="center"
          width="90"
      >
        <!--  <template slot-scope="scope">
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
          </template>-->
      </el-table-column>
      <el-table-column
          prop="floorNum"
          label="楼层"
          align="center"
          width="90"
      >
        <!--  <template slot-scope="scope">
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
          </template>-->
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
      <!--<el-table-column
              prop="productWt"
              label="构件重量"
              align="center"
              width="90"
      ></el-table-column>
      <el-table-column
              prop="productVol"
              label="构件方量"
              align="center"
              width="90"
      ></el-table-column>-->
      <el-table-column
          prop="num"
          label="计划数"
          align="center"
          width="110"
      ></el-table-column>
      <el-table-column
          prop="notShipNum"
          label="待发货数"
          align="center"
          width="110"
      ></el-table-column>
      <el-table-column
          label="本次发货数"
          prop="currentNum"
          align="center"
          width="100">
        <!--   <template slot-scope="scope">
               <el-input v-model="scope.row.currentNum" size="mini" type="number" min="1"
                         @input="changeNum(scope.row)"></el-input>
               &lt;!&ndash;   <div :class="{'red':!legalAdd(scope.row)}">{{scope.row.newInvoiceDetails.length}}</div>&ndash;&gt;
           </template>-->
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
          width="250"
      >
        <template slot-scope="scope">
          <el-button
              type="primary"
              round
              :disabled="scope.row.notShipNum==0 && scope.row.currentNum==0"
              @click="findInvoiceDetailInfo(scope.row)"
          >查看
          </el-button>
          <el-button
              type="success"
              round
              @click="addProductDetailInfo(scope.row)"
              :disabled="scope.row.notShipNum==0 && scope.row.currentNum==0"
          >添加
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
      </el-col>
      <el-col>

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
          :invoiceDetailQuery="invoiceDetailQuery"
          @product-detail-choose-finish="productDetailChooseFinish"
      ></product-detail-choose>
    </el-dialog>
    <el-dialog
        append-to-body
        :visible.sync="invoiceDetailInfoVisible"
        width="80%"
        v-if="invoiceDetailInfoVisible"
    >
      <invoice-detail-info-s
          :invoiceDetailQuery="invoiceDetailQuery"
          :newInvoiceDetails="newInvoiceDetails"
          :invoiceDetailInfoVisible="invoiceDetailInfoVisible"
          @dialog-finish="dialogFinish"
      ></invoice-detail-info-s>
    </el-dialog>

  </div>
</template>

<script>

var numeral = require('numeral')
import planModel from '../../../../../model/plan/plan'
import projectModel from '../../../../../model/project/project'
import InvoiceNewAdd from './InvoiceAddS.vue'
import invoiceDetailInfoS from './InvoiceDetailInfoS.vue'
import qualityInspectionModel from '../../../../../model/basicdata/qualityInspectionModel'
import TrayModel from "../../../../../model/basicdata/tray";
import {Notification} from "element-ui";
import moment from 'moment'

export default {
  name: "InvoiceNewList",
  //  props: ['invoices', 'mode'],
  components: {
    'product-detail-choose': InvoiceNewAdd,
    'invoice-detail-info-s': invoiceDetailInfoS,
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
        trayId: '',
        trayIds: [],
        trays: [],
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
      planDetails: [],
      productDetailChooseVisible: false,
      inspectorList: [],
      logCompanyList: [],
      projects: [],
      projectId: '',
      selected: [],
      //批量选择楼栋楼层
      invoicesSelect: [],
      selectBAndFVisible: false,
      invoiceDetailInfoVisible: false,
      eidtFormRules: {
        /*   deliverGoodsNo: [
               {required: true, message: '请填写发货单号', trigger: 'blur'},
           ],*/
        inspectorId: [
          {required: true, message: '请选择质检人', trigger: 'blur'},
        ],
        logCompanyId: [
          {required: true, message: '请选择物流公司', trigger: 'blur'},
        ],
        licenseNo: [
          {required: true, message: '请填写车牌号', trigger: 'blur'},
        ],
        ibeam: [
          {required: true, message: '请填写工字钢', trigger: 'blur'},
        ],
        squareWood: [
          {required: true, message: '请填写方木', trigger: 'blur'},
        ],
        invoiceDate: [
          {required: true, message: '请填写发货日期', trigger: 'blur'},
        ],
        planNo: [
          {required: true, message: '请填写发货计划号', trigger: 'blur'},
        ],
        trayIds: [
          {required: true, message: '请选择托盘', trigger: 'change'},
        ],
        /*     projectId: [
                 {required: true, message: '请选择项目', trigger: 'blur'},
             ],*/

      },
      // 下拉框列表
      buildList: [],
      floors: [],
      invoiceDetailQuery: {},
      loading: false,
      options: [],

    }
  },
  mounted() {
    //this.init()
    this.getTrayOption().then(_ => {
      this.init()
    })
  },
  methods: {
    functiontest() {
      log(this.invoicesQuery.trayIds)
    },
    getTrayOption() {
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
    /*remoteMethod(query){
        if(query != ''){
            this.loading = true;
            //根据query的值去远程检索数据
            TrayModel.findAll(query).then(data => {
                this.options = data.entity;
                this.options = this.options.filter(s => s.trayStatus != '发货中')
            }).catch(err => {
                console.error("err", err);
            })
            setTimeout(() => {
                this.loading = false;
            }, 200);
        }else{
            this.options = [];
        }
    },*/
    changeNum(row) {
      if (row.currentNum > row.notSendNum) {
        row.num = 1
        this.$message('超出构件剩余发货量');
        return;
      }
    },
    addProductDetailInfo(row) {
      console.log('projectId--->', row)
      this.invoiceDetailQuery.productId = row.productId
      this.invoiceDetailQuery.projectId = row.projectId
      this.invoiceDetailQuery.relevanceId = row.relevanceId
      this.invoiceDetailQuery.buildId = row.buildId
      this.invoiceDetailQuery.floorId = row.floorId
      this.productDetailChooseVisible = true


      /*      planModel.findPlanByPlanNo(this.invoicesQuery.planNo).then((data) => {
                let planId = data.entity.id
                planModel.findPlanDetailByPlanIdP(planId).then((dataS) => {
                    this.planDetails = dataS.entity

                })
            })*/
    },

    findInvoiceDetailInfo(row) {
      this.invoiceDetailQuery.invoiceId = row.invoiceId
      this.invoiceDetailQuery.productId = row.productId
      this.invoiceDetailQuery.relevanceId = row.relevanceId
      this.invoiceDetailInfoVisible = true
    },

    addProductDetail() {
      if (this.invoicesQuery.projectId == '') {
        this.$message('请选择项目');
        return;
      }
      this.projectId = this.invoicesQuery.projectId
      this.productDetailChooseVisible = true
    },
    productDetailChooseFinish(productDetails) {
      let flag = false;
      //更新当前需求数
      this.planDetails.forEach(p => {
        console.log('p-------->', p)
        console.log('productDetails----------->', productDetails)
        //根据 (productId 和 buildId 和 floorId)或 relevanceId 作为限定条件
        if (p.relevanceId == productDetails[0].relevanceId) {
          if (productDetails.length > p.notShipNum || p.notShipNum == 0) {
            flag = true
            this.$message('超出计划数');
            return;
          }
          p.notShipNum = p.notShipNum - productDetails.length
          p.currentNum = p.currentNum + productDetails.length
        }
      })
      if (flag == false) {
        //添加构件明细
        productDetails.forEach(pd => {
          this.newInvoiceDetails.push(pd)
        })
        this.productDetailChooseVisible = false
      }
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
        this.$router.push({name: 'invoiceListS'})
        return
      }
      //新增操作清空集合
      if (mode == 'add') {
        this.invoicesQuery = []
        this.invoicesQuery = this.$route.params.invoices;
        this.invoicesQuery.mode = mode
        planModel.findPlanDetailByPlanIdP(this.invoicesQuery).then((data) => {
          if (data.status == "failed") {
            Notification({
              message: data.msg,
              type: "warning"
            });
            loading.close()
            return;
          }
          this.planDetails = data.entity
          console.log(' this.planDetails------->', this.planDetails)
        })
        this.newInvoiceDetails = []
      } else if (mode == 'update') {
        this.invoicesQuery = invoices
        if (this.invoicesQuery.trayId) {
          log(this.invoicesQuery.trayId)
          let trayId = this.invoicesQuery.trayId.split(',')
          this.invoicesQuery.trayIds = trayId.map(val => {
            return Number(val)
          })
        }
        this.invoicesQuery.mode = mode
        console.log('invoicesQuery------>', this.invoicesQuery.trayIds)
        this.newInvoiceDetails = this.invoicesQuery.invoiceDetails

        planModel.findPlanDetailByPlanIdP(this.invoicesQuery).then((dataS) => {
          this.planDetails = dataS.entity
        })
        console.log()
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
    legalAdd(row) {
      if (row.newInvoiceDetails.length == 0) {
        return true
      }
      return (row.invoiceDetails.length + row.newInvoiceDetails.length) <= row.num
    },
    editFormSubmmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let mode = this.$route.params.mode;
          if (mode === undefined || mode == null) {
            this.$router.push({name: 'invoiceListS'})
            return
          }
          this.invoicesQuery.invoiceDetails = this.newInvoiceDetails
          this.invoicesQuery.ibeam = numeral(this.invoicesQuery.ibeam).format('0.00')
          this.invoicesQuery.squareWood = numeral(this.invoicesQuery.squareWood).format('0.00')
          this.invoicesQuery.mode = mode
          /*         //用于后段调用限定API调取相应方法
                   let deciceInfo = {
                       invoiceDetailList: this.invoicesQuery.invoiceDetails,
                       modeDecideInvoice: mode,
                   }*/

          /*         //限定
                   planModel.loadFloorRelevanceIdAndDecide(deciceInfo).then((data) => {
                       if (data.status == "failed") {
                           Notification({
                               message: data.msg,
                               type: "warning"
                           });
                           return;
                       }*/
          if (mode == 'add') {
            const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            });
            planModel.addNewS(this.invoicesQuery).then((data) => {
              if (data.status == "failed") {
                Notification({
                  message: data.msg,
                  type: "warning"
                });
                loading.close()
                return;
              }
              //this.invoicesQuery.projectId == ''
              Notification({
                message: "新建成功",
                type: "success"
              });
              loading.close();
              this.$router.push({name: 'invoiceListS'})
              // this.$emit('model-edit-finish')
            })
          } else if (mode == 'update') {
            const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            });
            planModel.updateNewInvoiceS(this.invoicesQuery).then((data) => {
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
              this.$router.push({name: 'invoiceListS'})
            })
          }
          //})
        }
      })
    },
    editFormCancel() {
      this.newInvoiceDetails = []
      this.$router.push({name: 'invoiceListS'})
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
    /*  focusBuild(row) {
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
      },*/
    // 关闭批量添加弹窗
    dialogFinish(invoiceDetails, relevanceId, productDetailIds) {
      console.log('invoiceDetails--->', invoiceDetails)
      console.log('productId--->', relevanceId)
      console.log('productDetailIds--->', productDetailIds)

      if (invoiceDetails.length == 0 && relevanceId == '') {
      } else {
        //找到对应构件更新待发货数和本次发货数
        this.planDetails.forEach(p => {
          //删除完本次发货数
          if (p.relevanceId == relevanceId && invoiceDetails.length == 0) {
            p.notShipNum = p.notShipNum + p.currentNum
            p.currentNum = 0
          } else if (p.relevanceId == relevanceId && invoiceDetails.length > 0) {
            let num = p.currentNum - invoiceDetails.length
            p.notShipNum = p.notShipNum + num
            p.currentNum = invoiceDetails.length
          }


        })
        if (productDetailIds != null) {
          //如果在查看页面做过删除操作，在更新集合内删除对应构件明细
          productDetailIds.forEach(id => {
            let index = this.newInvoiceDetails.findIndex(d => d.productDetailId == id)
            if (index >= 0) {
              this.newInvoiceDetails.splice(index, 1)
            }
          })
        }
      }

      this.invoiceDetailInfoVisible = false
    },

    getArrDifference(arr1, arr2) {

      return arr1.concat(arr2).filter(function (v, i, arr) {

        return arr.indexOf(v) === arr.lastIndexOf(v);

      });

    },
    /*  changeFloor(row) {
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
      },*/
  }
}
</script>

<style scoped>
</style>
