<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form :inline="true">
              <el-form-item prop="deliverGoodsNo">
                <el-input v-model="editForm.deliverGoodsNo" style="width: 150px;" placeholder="发货单号"
                          clearable size="mini"></el-input>
              </el-form-item>
              <el-form-item prop="invoiceDate">
                <el-date-picker
                    v-model="editForm.invoiceDate"
                    type="date"
                    style="width:150px"
                    size="mini"
                    value-format="yyyy-MM-dd"
                    placeholder="发货日期">
                </el-date-picker>
              </el-form-item>
              <el-form-item prop="invoiceStatus">
                <el-select v-model="editForm.invoiceStatus" style="width: 150px;" placeholder="发货状态"
                           clearable size="mini">
                  <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="productName">
                <el-input v-model="editForm.productName" style="width: 150px;" placeholder="构件名称"
                          clearable size="mini"></el-input>
              </el-form-item>
              <el-form-item>
                <el-select v-model="editForm.stockId" size="mini" style="width: 150px;" placeholder="库区"
                           clearable @change="changeStock">
                  <el-option
                      v-for="item in stocks"
                      :key="item.id"
                      :label="item.stockName"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-select v-model="editForm.shelfId" clearable style="width: 150px;" placeholder="货架"
                           size="mini">
                  <el-option
                      v-for="item in shelves"
                      :key="item.id"
                      :label="item.shelfName"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="projectId">
                <el-select v-model="editForm.projectId"
                           placeholder="项目名称"
                           clearable
                           filterable
                           @change="changeProject"
                           style="width: 150px;">
                  <el-option
                      v-for="item in projectList"
                      :key="item.id"
                      :label="item.projectName"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="buildingId">
                <el-select v-model="editForm.buildingId"
                           @change="changeBuilding"
                           placeholder="楼栋"
                           :disabled="editForm.projectId==''"
                           style="width: 150px;"
                           clearable filterable>
                  <el-option
                      :key="item.id"
                      :value="item.id"
                      v-for="item in buildings"
                      :label="item.buildCode">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="floorId">
                <el-select v-model="editForm.floorId"
                           :disabled="editForm.buildingId==''"
                           clearable
                           filterable
                           style="width: 150px;"
                           placeholder="楼层">
                  <el-option
                      v-for="item in floors"
                      :key="item.id"
                      :label="item.floorNum"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                  prop="confirmationDate"
              >
                <el-date-picker
                    type="date"
                    style="width: 150px;"
                    size="mini"
                    placeholder="确认日期"
                    v-model="editForm.confirmationDate"
                ></el-date-picker>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col>
            <my-search-button-group :model='searchButtonData'></my-search-button-group>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <my-button-group :model="buttonData" :disabled="this.invoiceStatusDisabled"></my-button-group>
          </el-col>
          <el-col>
            <div>
              <span class="color_font_span_class">总重量(t):</span>
              <span class="color_span_class">{{ productWtsNum }}</span>
            </div>
            <div>
              <span class="color_font_span_class">总方量(m³):</span>
              <span class="color_span_class">{{ productVolsNum }}</span>
            </div>
          </el-col>
        </el-row>
      </el-header>
      <el-main
          ref="mainContent"
          :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <el-table
            :data="entities"
            height="100%"
            style="width: 100%"
            header-row-class-name="header-row"
            size="mini"
            stripe
            border
            tooltip-effect="light"
            @row-click="clickRow"
            row-key="id"
            @expand-change="showSubData"
            @selection-change="handleSelectionChange"
            :expand-row-keys="expands"
            ref="moviesTable"
        >
          <el-table-column
              type="selection"
              width="40">
          </el-table-column>
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
                    prop="productDetailNo"
                    label="明细编号"
                    align="center"
                    width="100"
                ></el-table-column>
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
                <!--                        <el-table-column-->
                <!--                                prop="productDetailNo"-->
                <!--                                label="构件明细编号"-->
                <!--                                align="center"-->
                <!--                                width="120"-->
                <!--                        ></el-table-column>-->
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
                <el-table-column
                    prop="productWt"
                    label="构件重量(t)"
                    align="center"
                    width="110"
                    :formatter="wtFormat"
                ></el-table-column>
                <el-table-column
                    prop="projectConcrete"
                    label="砼方量(m³)"
                    align="center"
                    width="110"
                    :formatter="wtFormat"
                ></el-table-column>
                <el-table-column
                    prop="productVol"
                    label="构件方量(m³)"
                    align="center"
                    width="110"
                    :formatter="wtFormat"
                ></el-table-column>
                <el-table-column
                    prop="stockName"
                    label="库区号"
                    align="center"
                    width="100">
                </el-table-column>
                <el-table-column
                    prop="shelfName"
                    label="货架号"
                    align="center">
                </el-table-column>
              </el-table>
            </template>
          </el-table-column>
          <el-table-column
              type="index"
              label="序号"
              width="70">
          </el-table-column>
          <el-table-column
              prop="plan.planNo"
              label="发货计划号"
              align="center"
              width="100">
          </el-table-column>
          <el-table-column
              prop="deliverGoodsNo"
              label="发货单号"
              align="center"
              width="100">
            <template slot-scope="scope">
              <span v-if="scope.row.mark" style="color:#409EFF ">{{ scope.row.deliverGoodsNo }}</span>
              <span v-else style="color:#303133 ">{{ scope.row.deliverGoodsNo }}</span>
            </template>
          </el-table-column>
          <el-table-column
              prop="invoiceDate"
              label="发货时间"
              align="center"
              width="150"
              :formatter="formatterDate"
          ></el-table-column>
          <el-table-column
              prop="project.projectName"
              label="项目名称"
              align="center"
              width="100">
          </el-table-column>
          <el-table-column
              prop="productNum"
              label="构件数"
              align="center"
              width="100">
          </el-table-column>

          <el-table-column
              prop="productWtsNum"
              label="总重量(t)"
              align="center"
              width="100"
              :formatter="wtFormat"
          ></el-table-column>
          <el-table-column
              prop="productConcretesNum"
              label="砼方量(m³)"
              align="center"
              width="100"
              :formatter="wtFormat"
          ></el-table-column>
          <el-table-column
              prop="productVolsNum"
              label="方量(m³)"
              align="center"
              width="100"
              :formatter="wtFormat"
          ></el-table-column>
          <el-table-column
              prop="inspector.inspector"
              label="质检人"
              align="center"
              width="100">
          </el-table-column>
          <el-table-column
              prop="logCompany.logCompany"
              label="物流公司"
              align="center"
              width="100">
          </el-table-column>
          <el-table-column
              prop="licenseNo"
              label="车牌号"
              align="center"
              width="100">
          </el-table-column>
          <el-table-column
              prop="ibeam"
              label="工字钢"
              align="center"
              width="100">
          </el-table-column>
          <el-table-column
              prop="squareWood"
              label="方木"
              align="center"
              width="100">
          </el-table-column>
          <el-table-column
              prop="dateCreated"
              label="创建时间"
              align="center"
              width="150"
              :formatter="momentFormatTime"
          ></el-table-column>
          <el-table-column
              prop="createdByName"
              label="创建人"
              align="center"
              width="100"
          ></el-table-column>
       <!--   <el-table-column
              prop="confirmationDate"
              label="确认时间"
              align="center"
              width="150"
              :formatter="momentFormatTime"
          ></el-table-column>-->
          <el-table-column
              label="操作"
              fixed="right"
              :width="rowButtonGroupWidth(rowButtonData)">
            <template slot-scope="scope">
              <my-row-button-group
                  :row="scope.row"
                  :model="rowButtonData"
              ></my-row-button-group>
            </template>
          </el-table-column>
        </el-table>
        <div style="display:none">
          <canvas class="qrcode"></canvas>
        </div>
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
    <!-- <el-form :model="editForm" v-if="editForm" ref="editForm" size="small" :inline="true">
        <el-row>

            <el-form-item>
                <el-button type="primary" size="mini" @click="onSubmit">查询</el-button>
                <el-button type="success" size="mini" @click="addInvoice">新建</el-button>
                <el-button type="primary" size="mini" @click="printInvoiceList" style="width: 110px"
                           :disabled="this.invoiceStatusDisabled!='发货已确认'"
                           v-if="authorized('invoiceNewList:print')"
                >发货单打印
                </el-button>
                <el-button type="primary" size="mini" @click="printCertiList" style="width: 110px"
                           :disabled="this.invoiceStatusDisabled!='发货已确认'"
                           v-if="authorized('invoiceNewList:certiPrint')"
                >合格证打印
                </el-button>
                <el-button type="primary" size="mini" @click="printInvoiceListNew" style="width: 110px"
                           :disabled="this.invoiceStatusDisabled!='发货已确认'"
                           v-if="authorized('invoiceNewList:printNew')"
                >发货单针式打印
                </el-button>
                <el-button type="primary"
                           size="mini"
                           v-if="authorized('invoiceNewList:tb')">
                    同步
                </el-button>
                <el-button type="primary" size="mini"
                           @click="printHeifeiList"
                           v-if="authorized('invoiceNewList:hefeiPrint')">
                    合肥发货单打印
                </el-button>
                <el-button type="primary" size="mini"
                           @click="printHongtaiList"
                           v-if="authorized('invoiceNewList:htPrint')">
                    宏泰发货单打印
                </el-button>
                <el-button type="primary" size="mini"
                           @click="printHangzhouList"
                           v-if="authorized('invoiceNewList:hzPrint')">
                    杭州发货单打印
                </el-button>
                <el-button type="primary"
                           size="mini"
                           @click="printInvoiceListNewSJ"
                           style="width: 110px"
                           v-if="authorized('invoiceNewList:sjPrint')"

                >三杰针式打印
                </el-button>
            </el-form-item>
        </el-row>
    </el-form> -->
    <el-dialog :title="mode=='add'?'新建发货单':'编辑发货单'" :visible.sync="invoiceNewEditFormVisible"
               v-if="invoiceNewEditFormVisible" width="85%">
      <invoice-model-edit
          :invoices="invoices"
          :mode="mode"
          @model-edit-finish="invoiceModelEditFinish"
          @model-edit-cancel="invoiceModelEditCancel"
      ></invoice-model-edit>
    </el-dialog>
    <el-dialog :title="'宏泰江夏发货单打印'" :visible.sync="invoicePrintPreviewHTVisible"
               v-if="invoicePrintPreviewHTVisible" width="85%">
      <invoice-print-preview-ht
          :invoicesPrintInfo="invoicesPrintInfo"
          :invoiceIdHT="invoiceIdHT"
          @invoice-print-preview-ht-finish="invoicePrintPreviewHTFinish"
          @invoice-print-preview-ht-cancel="invoicePrintPreviewHTFinish"
      ></invoice-print-preview-ht>
    </el-dialog>
    <el-dialog :title="'杭州宝业发货单打印'" :visible.sync="invoicePrintPreviewHZVisible"
               v-if="invoicePrintPreviewHZVisible" width="85%">
      <invoice-print-preview-hz
          :invoicesPrintInfo="invoicesPrintInfo"
          :invoiceIdHZ="invoiceIdHZ"
          @invoice-print-preview-hz-finish="invoicePrintPreviewHZFinish"
          @invoice-print-preview-hz-cancel="invoicePrintPreviewHZFinish"
      ></invoice-print-preview-hz>
    </el-dialog>
    <el-dialog :title="'发货单打印'" :visible.sync="invoicePrintPreviewSJVisible"
               v-if="invoicePrintPreviewSJVisible" width="85%">
      <invoice-print-preview-sj
          :invoicesPrintInfo="invoicesPrintInfo"
          :invoiceIdSJ="invoiceIdSJ"
          @invoice-print-preview-sj-finish="invoicePrintPreviewSJFinish"
          @invoice-print-preview-sj-cancel="invoicePrintPreviewSJCancel"
      ></invoice-print-preview-sj>
    </el-dialog>
  </div>
</template>

<script>
import menuTabService from "../../../../../service/menuTabService";
import serviceConfig from '../../../../../../server.config.js'

var numeral = require('numeral')
import model from '../../../../../model/model'
import planModel from '../../../../../model/plan/plan'
import InvoiceNewEdit from './InvoiceEditS.vue'
import invoicePrintPreviewSJ from '../InvoicePrintPreviewSJ.vue'
import invoicePrintPreviewHT from '../InvoicePrintPreviewHT.vue'
import invoicePrintPreviewHZ from '../InvoicePrintPreviewHZ.vue'
import stockModel from '../../../../../model/basicdata/stock'
import shelfModel from '../../../../../model/basicdata/shelf'
import projectModel from '../../../../../model/project/project'
import moment from 'moment'
import {Notification} from "element-ui";
import buildingModel from "../../../../../model/project/building";
import floorModel from "../../../../../model/project/floor";
import QRCode from 'qrcode'
import $ from "jquery";


export default {
  name: "InvoiceListS",
  components: {
    'invoice-model-edit': InvoiceNewEdit,
    'invoice-print-preview-sj': invoicePrintPreviewSJ,
    'invoice-print-preview-ht': invoicePrintPreviewHT,
    'invoice-print-preview-hz': invoicePrintPreviewHZ,
  },
  data() {
    return {
      //表查询
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: this.GLOBAL.pageSize,
        pageSizes: this.GLOBAL.pageSizes
      },
      entity: {},
      entities: [],
      editForm: {
        deliverGoodsNo: '',
        invoiceStatus: '',
        invoiceNo: '',
        stockId: '',
        shelfId: '',
        productName: '',
        buildingId: '',
        floorId: '',
        projectId: '',
        confirmationDate: ''
      },
      invoiceNewEditFormVisible: false,
      //选择条件赋值
      invoices: [],
      stocks: [],
      shelves: [],
      expands: [],
      subDatas: [],
      invoicesEdit: '',
      mode: '',
      options: [{
        value: '发货待确认',
        label: '发货待确认'
      }, {
        value: '发货已确认',
        label: '发货已确认'
      }],
      selected: [],
      invoiceStatusDisabled: false,
      productWtsNum: '',
      productConcretesNum: '',
      productVolsNum: '',
      //三杰打印
      invoiceIdSJ: '',
      invoicesPrintInfo: [],
      invoicePrintPreviewSJVisible: false,
      //宏泰打印
      invoiceIdHT: '',
      invoicePrintPreviewHTVisible: false,
      //杭州打印
      //宏泰打印
      invoiceIdHZ: '',
      invoicePrintPreviewHZVisible: false,
      floors: [],
      projectList: [],
      buildings: [],
      buttonData: [
        /*     {
                 name: '新建',
                 event: 'addInvoice'
             },*/
        {
          name: '发货单打印',
          event: 'printInvoiceList',
          authorized: 'invoiceNewList:print',
          //disabled: true
        },
        {
          name: '合格证打印',
          event: 'printCertiList',
          authorized: 'invoiceNewList:certiPrint',
          //disabled: true
        },
        {
          name: '发货单针式打印',
          event: 'printInvoiceListNew',
          authorized: 'invoiceNewList:printNew',
          //disabled: true
        },
        {
          name: '合肥发货单打印',
          event: 'printHeifeiList',
          authorized: 'invoiceNewList:hefeiPrint',
          //disabled: true
        },
        {
          name: '叶集发货单打印',
          event: 'printYeJiList',
          authorized: 'invoiceNewList:yjPrint',
        },
        {
          name: '宏泰发货单打印',
          event: 'printHongtaiList',
          authorized: 'invoiceNewList:htPrint',
          //disabled: true
        },
        {
          name: '杭州发货单打印',
          event: 'printHangzhouList',
          authorized: 'invoiceNewList:hzPrint',
          //disabled: true
        },
        {
          name: '三杰针式打印',
          event: 'printInvoiceListNewSJ',
          authorized: 'invoiceNewList:sjPrint',
          //disabled: true
        },
        {
          name: '下载发货单',
          event: 'addQRCode',
          authorized: 'invoiceNewList:exportQRCode',
        },
      ],
      searchButtonData: [
        {
          name: '查询',
          event: 'onSubmit'
        }
      ],
      rowButtonData: [
        {
          name: '编辑',
          event: 'editClick',
          authorized: 'invoiceListS:edit',
          disabled: "scope.row.invoiceStatus=='发货已确认'"
        },
        {
          name: '确认',
          event: 'updateInvoiceStatusByInvoiceId',
          authorized: 'invoiceListS:confirm',
          disabled: "scope.row.invoiceStatus=='发货已确认'"
        },
        {
          name: '合格证打印',
          event: 'exportInvoiceDetailsCertificate',
        },
        {
          name: '删除',
          event: 'deleteInvoice',
          disabled: "scope.row.invoiceStatus=='发货已确认'"
        },
      ]
    }
  },
  mounted() {
    this.info()
    this.list()
    this.loadNums()

  },
  methods: {
    // 点击整行都可以进行勾选
    clickRow(row) {
      this.$refs.moviesTable.toggleRowSelection(row)
    },

    //调出新版打印单
    printInvoiceListNew(row) {
      if (this.selected.length == 0) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_NOT_PRINT);
        return;
      }
      let arr = new Array()
      this.selected.forEach((row) => {
        arr.push(row)
      })
      if (arr.length > 1) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_ONE_PRINT);
        return;
      }

      /*   if (arr[0].invoiceStatus == '发货待确认') {
             this.$message(this.GLOBAL.PLAN_MSG.NOT_CONFIRM);
             return;
         }*/
      // this.$emit('print-invoice-list')
      this.$router.push({
        name: 'invoicePrintNew',
        params: {
          id: arr[0].id
        }
      })
    },
    printInvoiceListNewSJ(row) {
      if (this.selected.length == 0) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_NOT_PRINT);
        return;
      }
      let arr = new Array()
      this.selected.forEach((row) => {
        arr.push(row)
      })
      if (arr.length > 1) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_ONE_PRINT);
        return;
      }
      planModel.findInvoiceDetailsByInvoiceId(arr[0].id).then((data) => {
        this.invoiceIdSJ = arr[0].id
        this.invoicesPrintInfo = data.entity
        this.invoicePrintPreviewSJVisible = true
      })


      /* this.$router.push({
           name: 'invoicePrintingSj',
           params: {
               id: arr[0].id
           }
       })*/

    },
    deleteInvoice(row) {
      this.$confirm('确定要删除发货单, 是否继续?', '提示', {
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
        planModel.deleteInvoice(row.id).then((data) => {
          if (data.status == "failed") {
            Notification({
              message: data.msg,
              type: "warning"
            });
            loading.close()
            return;
          }
          Notification({
            message: "删除成功",
            type: "success"
          });
          loading.close();
          this.list()
        })
      }).catch(() => {
      })
    },
    exportInvoiceDetailsCertificate(row) {
      let fileName = "产品合格证.xlsx";
      model.exportFileExists(fileName).then((data) => {
        if (data.status == 'failed') {
          Notification({
            message: data.msg,
            type: 'warning'
          })
          return
        }
        var url = serviceConfig.baseURL + '/export/exportInvoiceDetailsCertificate/' + row.id
        var prams = {}
        this.exportExcelPost(url, prams)
      })
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
    //下载发货单
    addQRCode() {
      if (this.selected.length == 0) {
        this.$message(this.GLOBAL.PLAN_MSG.NOT_SELECT_EXPORT);
        return;
      }
      let arr = new Array()
      this.selected.forEach((row) => {
        arr.push(row)
      })
      if (arr.length > 1) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_ONE_EXPORT);
        return;
      }
      // if (arr[0].invoiceStatus === "发货待确认") {
      //     this.$message(this.GLOBAL.PLAN_MSG.NOT_CONFIRM);
      //     return;
      // }
      let that = this
      let id = arr[0].id.toString()
      //获取当前链接
      let url = location.href.split("#");
      let finalUrl = url[0] + "cym/#/common/receipt/ReceiptConfirm/ReceiptDetail";
      //let finalUrl = "http://192.168.1.98:8001/#/common/receipt/ReceiptConfirm/ReceiptDetail";
      QRCode.toCanvas($('.qrcode')[0], finalUrl + '?' + id + "-" + this.$store.state.user.tenantId + "-", {
        width: '300',
        height: '300',
        margin: '1',
        errorCorrectionLevel: 'H',
      }, function (error, url) {
        // if (error) console.error(error)
        console.log('success!');
        let srcAndBase = that.convertCanvasToImage($('.qrcode')[0]).src
        that.exportInvoice(srcAndBase, id)
      })
    },
    exportInvoice(image, id) {
      var url = serviceConfig.baseURL + '/invoice/exportInvoice'
      var prams = {
        base: image,
        invoiceId: id
      }
      this.exportExcelPost(url, prams)
    },
    convertCanvasToImage(canvas) {
      var image = new Image();
      image.src = canvas.toDataURL("image/jpeg");
      return image;

    },
    //打印合格证
    printCertiList(row) {

      // this.$emit('print-invoice-list'); //通知主界面关闭当前弹窗
      if (this.selected.length == 0) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_NOT_PRINT);
        return;
      }
      let arr = new Array()
      this.selected.forEach((row) => {
        arr.push(row)
      })
      if (arr.length > 1) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_ONE_PRINT);
        return;
      }

      /*  if(arr[0].invoiceStatus=='发货待确认'){
            this.$message('该发货单未扫码确认发货');
            return;
        }*/
      this.$router.push({
        name: 'certiPrint',
        params: {
          id: arr[0].id
        }
      })
    },
    //合肥发货单打印
    printHeifeiList(row) {
      // this.$emit('print-invoice-list'); //通知主界面关闭当前弹窗
      if (this.selected.length == 0) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_NOT_PRINT);
        return;
      }
      let arr = new Array()
      this.selected.forEach((row) => {
        arr.push(row)
      })
      if (arr.length > 1) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_ONE_PRINT);
        return;
      }

      /*  if(arr[0].invoiceStatus=='发货待确认'){
            this.$message('该发货单未扫码确认发货');
            return;
        }*/
      this.$router.push({
        name: 'hefeiPrint',
        params: {
          arr: arr
        },
      })
    },
    //叶集发货单打印
    printYeJiList(row) {
      // this.$emit('print-invoice-list'); //通知主界面关闭当前弹窗
      if (this.selected.length == 0) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_NOT_PRINT);
        return;
      }
      let arr = new Array()
      this.selected.forEach((row) => {
        arr.push(row)
      })
      if (arr.length > 1) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_ONE_PRINT);
        return;
      }

      /*  if(arr[0].invoiceStatus=='发货待确认'){
            this.$message('该发货单未扫码确认发货');
            return;
        }*/
      this.$router.push({
        name: 'yejiPrint',
        params: {
          arr: arr
        },
      })
    },
    // 宏泰发货单打印
    printHongtaiList(row) {
      if (this.selected.length == 0) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_NOT_PRINT);
        return;
      }
      let arr = new Array()
      this.selected.forEach((row) => {
        arr.push(row)
      })
      if (arr.length > 1) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_ONE_PRINT);
        return;
      }
      planModel.findInvoiceDetailsByInvoiceId(arr[0].id).then((data) => {
        this.invoiceIdHT = arr[0]
        this.invoicesPrintInfo = data.entity
        this.invoiceIdHT.logoPath = data.entity[0].logoPath
        this.invoicePrintPreviewHTVisible = true
      })
    },
    // 杭州发货单打印
    printHangzhouList(row) {
      if (this.selected.length == 0) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_NOT_PRINT);
        return;
      }
      let arr = new Array()
      this.selected.forEach((row) => {
        arr.push(row)
      })
      if (arr.length > 1) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_ONE_PRINT);
        return;
      }
      planModel.findInvoiceDetailsByInvoiceId(arr[0].id).then((data) => {
        this.invoiceIdHZ = arr[0]
        this.invoicesPrintInfo = data.entity
        this.invoicePrintPreviewHZVisible = true
      })
    },
    //调出打印单
    printInvoiceList(row) {
      if (this.selected.length == 0) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_NOT_PRINT);
        return;
      }
      let arr = new Array()
      this.selected.forEach((row) => {
        arr.push(row)
      })
      if (arr.length > 1) {
        this.$message(this.GLOBAL.PLAN_MSG.SELECT_ONE_PRINT);
        return;
      }
      //2020-06-03  发货待确认状态也可以打印
      /*      if (arr[0].invoiceStatus == '发货待确认') {
                this.$message(this.GLOBAL.PLAN_MSG.NOT_CONFIRM);
                return;
            }*/
      this.$router.push({
        name: 'invoicePrint',
        params: {
          id: arr[0].id
        }
      })
    },
    //表查询
    list() {
      // this.entity.planId = this.planId
      /*       this.entity.deliverGoodsNo = this.editForm.deliverGoodsNo
             this.entity.invoiceNo = this.editForm.invoiceNo
             this.entity.invoiceStatus = this.editForm.invoiceStatus
             this.entity.productName = this.editForm.productName
             this.entity.stockId = this.editForm.stockId
             this.entity.shelfId = this.editForm.shelfId
             this.entity.projectId = this.editForm.projectId,
                 this.entity.buildingId = this.editForm.buildingId,
                 this.entity.floorId = this.editForm.floorId,*/
      model.list('invoice', this.editForm, this.pageInfo).then(data => {
        let entity = data.entity
        let list = data.entity.content;
        this.entities = list
        this.pageInfo.total = data.entity.totalElements
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
      let model = {
        planId: row.id
      }
      planModel.findInvoiceDetailsByInvoiceId(row.id).then((data) => {
        this.subDatas = data.entity
      })
    },
    editClick(row) {
      this.mode = 'update'
      this.invoices = row
      planModel.findInvoiceDetailsByInvoiceId(row.id).then((data) => {
        this.invoices.invoiceDetails = data.entity
        planModel.findPlanById(this.invoices.planId).then((data) => {
          console.log('data.entity--->', data.entity)
          this.invoices.planNo = data.entity.planNo
          this.$router.push({
            name: 'invoiceEditS',
            params: {
              invoices: this.invoices,
              mode: this.mode
            }
          })
        })

        //this.invoiceNewEditFormVisible = true
      })
    },
    changeStock(id) {
      //防止清楚库区时报错
      if (id == '' || id == undefined) {
        id = 0
      }
      stockModel.getShelfsById(id).then(data => {
        this.shelves = data.entity
        this.shelves = this.shelves.filter(s => s.shelfStatus == '启用' && s.fullStatus == '未满')
        this.editForm.shelfId = ''
      })
    },
    addInvoice() {
      this.mode = 'add'
      this.$router.push({
        name: 'invoiceEditS',
        params: {
          mode: this.mode
        }
      })

      //this.invoiceNewEditFormVisible = true
    },
    updateInvoiceStatusByInvoiceId(row) {
      this.$confirm('此操作为确认该发货单下构件数据，操作后该发货单下构件数据将不能更改, 是否继续?', '提示', {
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
        planModel.updateInvoiceStatusByInvoiceIdS(row.id).then((data) => {
          if (data.status == "failed") {
            Notification({
              message: data.msg,
              type: "warning"
            });
            loading.close()
            return;
          }
          Notification({
            message: "已确认",
            type: "success"
          });
          loading.close();
          this.list()
        })
      }).catch(() => {
      })
    },
    onSubmit() {
      this.list()
      this.loadNums()
    },
    loadNums() {
      /*       planModel.sumProductWtAndProductConcrete().then((data)=>{
                 this.productWtsNum = this.numeralS(data.entity.productWtsNum)

                 this.productConcretesNum = this.numeralS(data.entity.productConcretesNum)
             })*/
      this.entity.deliverGoodsNo = this.editForm.deliverGoodsNo
      this.entity.invoiceNo = this.editForm.invoiceNo
      this.entity.invoiceStatus = this.editForm.invoiceStatus
      this.entity.productName = this.editForm.productName
      this.entity.stockId = this.editForm.stockId
      this.entity.shelfId = this.editForm.shelfId
      this.entity.projectId = this.editForm.projectId,
          this.entity.buildingId = this.editForm.buildingId,
          this.entity.floorId = this.editForm.floorId,
          planModel.sumProductWtNumAndProductConcreteNum(this.entity).then((data) => {
            this.productWtsNum = this.numeralS(data.entity.productWtsNum)

            this.productVolsNum = this.numeralS(data.entity.productVolsNum)
          })
    },
    info() {
      shelfModel.getStocks().then(data => {
        this.stocks = data.entity;
        this.stocks = this.stocks.filter(s => s.stockStatus == '启用' && s.stockTypeCode == '成品仓库')
      }).catch(err => {
      })
      projectModel.getExternalAllProject().then((data) => {
        this.projectList = data.entity
      })


    },
    numeralS(num) {
      return numeral(num).format('0.000')
    },
    getSelection() {
      if (this.selected.length == 0) {
        this.$message('请选择打印项');
        return;
      }
      let arr = new Array()
      this.selected.forEach((row) => {
        arr.push(row)
      })
      if (arr.length > 1) {
        this.$message('只能勾选一个进行打印');
        return;
      }
      if (arr[0].invoiceStatus == '发货待确认') {
        this.$message('该发货单未扫码确认发货');
        return;
      }
    },
    changeProject() {
      buildingModel.findByProjectId(this.editForm.projectId).then(data => {
        this.buildings = data.entity
        this.editForm.buildingId = ''
        this.editForm.floorId = ''
        this.floors = []
      })
    },
    changeBuilding(id) {
      this.editForm.floorId = ''
      this.floors = []
      if (id == undefined || id == '') {
        return
      }
      let building = this.buildings.find(b => b.id == id)
      floorModel.findByBuildingIdAndProjectId(id, building.projectId).then((data) => {
        this.floors = data.entity
        this.editForm.floorId = ''
      })
    },
    authorized(index) {
      return menuTabService.authorized(this.$store, index)
    },
    momentFormatTime(row, column, cellValue, index) {
      if (cellValue) {
        return moment(cellValue).format('YYYY-MM-DD HH:mm:ss')
      } else {
        return cellValue
      }

    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (!date || date == "　") {
        return "";
      }
      return moment(date).format("YYYY-MM-DD");
    },
    wtFormat(row, column, cellValue, index) {
      if (cellValue) {
        return numeral(cellValue).format('0.000')
      } else {
        return cellValue
      }
    },
    invoiceModelEditFinish() {
      this.list()
      this.loadNums()
      this.invoiceNewEditFormVisible = false
    },
    invoiceModelEditCancel() {
      this.invoiceNewEditFormVisible = false
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
    handleSelectionChange(selected) {
      this.selected = selected
      let flag = this.invoiceStatusDisabled
      selected.length !== 1 ? flag = false : selected && selected[0].invoiceStatus == '发货已确认' ? flag = true : flag = false
      this.invoiceStatusDisabled = flag
    },
    invoicePrintPreviewSJFinish() {
      this.list()
      this.invoicePrintPreviewSJVisible = false
    },
    invoicePrintPreviewSJCancel() {
      this.list()
      this.invoicePrintPreviewSJVisible = false
    },
    invoicePrintPreviewHTFinish() {
      this.list()
      this.invoicePrintPreviewHTVisible = false
    },
    invoicePrintPreviewHTCancel() {
      this.list()
      this.invoicePrintPreviewHTVisible = false
    },
    invoicePrintPreviewHZFinish() {
      this.list()
      this.invoicePrintPreviewHZVisible = false
    },
    invoicePrintPreviewHZCancel() {
      this.list()
      this.invoicePrintPreviewHZVisible = false
    },

  }
}
</script>

<style scoped>
.num_span_class {
  display: block;
  padding-right: 20px;

}

.color_span_class {
  color: #67C23A;
  font-weight: 800;
}

.color_font_span_class {
  font-weight: 800;
}

.float_class {
  display: block;
  float: right;

}

</style>
