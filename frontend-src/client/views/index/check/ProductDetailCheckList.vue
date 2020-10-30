<template>
  <div
    id="product-detail-list"
    class="currency-style-box"
  >
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <product-detail-criteria
            :list-type="checkType"
            @change-criteria="changeCriteria"
            @bacth-JZQR="bactchJZQR"
          ></product-detail-criteria>
        </el-row>
        <el-row v-show="checkType==='checkJZQR' || checkType ==='checkDCPJ'|| checkType ==='checkDYJ'">
          <!-- 此处不显示只是用来给表格启用自动高度 -->
          <my-button-group :model="buttonData"></my-button-group>
        </el-row>
      </el-header>
      <el-main
        ref="mainContent"
        :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <el-table
          :data="entities"
          @expand-change="showSubData"
          @selection-change="handleSelectionChange"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
          height="100%"
          style="width:100%"
          header-row-class-name="header-row"
          stripe
          border
          :expand-row-keys="expands"
          tooltip-effect="light"
          row-key="id"
        >
          <el-table-column  type="selection">
          </el-table-column>
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-table
                :data="checkList"
                stripe
                border
              >
                <el-table-column
                  prop="checkType"
                  label="检验类型"
                  align="center"
                  width="100"
                >
                </el-table-column>
                <el-table-column
                  prop="checkResult"
                  label="检验结果"
                  align="center"
                  width="100"
                >
                </el-table-column>
                <el-table-column
                  prop="bhgx"
                  label="不合格项"
                  align="center"
                  width="160"
                  :show-overflow-tooltip="true"
                >
                </el-table-column>
                <el-table-column
                  label="检验明细"
                  align="center"
                  width="120"
                >
                  <template slot-scope="scope">
                    <el-popover
                      trigger="click"
                      placement="top-end"
                      :visible-arrow="false"
                    >
                      <el-table :data="scope.row.items">
                        <el-table-column
                          width="150"
                          property="name"
                          label="检验项"
                        ></el-table-column>
                        <el-table-column
                          width="100"
                          property="result"
                          label="检验结果"
                        ></el-table-column>
                      </el-table>
                      <div
                        slot="reference"
                        class="name-wrapper"
                      >
                        <el-button
                          icon="el-icon-search"
                          round
                          type="primary"
                        >查看</el-button>
                      </div>
                    </el-popover>
                  </template>
                </el-table-column>
                <el-table-column
                  label="检验图片"
                  align="center"
                  width="120"
                >
                  <template slot-scope="scope">
                    <el-button
                      icon="el-icon-search"
                      round
                      type="primary"
                      v-if="scope.row.checkImages.length>0"
                      @click="showCheckImages(scope.row)"
                    >查看检验图片</el-button>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="checkDesc"
                  label="检验说明"
                  align="center"
                  width="120"
                  :show-overflow-tooltip="true"
                >
                </el-table-column>
                <el-table-column
                  prop="checkPersonName"
                  label="检验员"
                  align="center"
                  width="100"
                >
                </el-table-column>
                <el-table-column
                  prop="checkTime"
                  label="检验日期"
                  align="center"
                  width="200"
                  :formatter="momentFormat"
                >
                </el-table-column>
              </el-table>
            </template>
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            width="70"
          >
          </el-table-column>
          <!--            <el-table-column-->
          <!--                    prop="detailNo"-->
          <!--                    label="构件明细编号"-->
          <!--                    align="center"-->
          <!--                    width="120">-->
          <!--            </el-table-column>-->
          <el-table-column
            prop="project.projectName"
            label="项目名称"
            align="center"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="floorRelevance.building.buildCode"
            label="楼栋"
            align="center"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="floorRelevance.floor.floorNum"
            label="楼层"
            align="center"
            width="120"
          >
          </el-table-column>
          <el-table-column
                  prop="detailNo"
                  label="明细编码"
                  align="center"
                  width="120"
          >
          </el-table-column>
          <el-table-column
            prop="product.productNo"
            label="构件编码"
            align="center"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="product.productName"
            label="构件名称"
            align="center"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="product.productWt"
            label="构件重量(t)"
            align="center"
            :formatter="wtFormat"
            width="120"
          >
          </el-table-column>
          <el-table-column
                  v-if="showDCPJReason"
                  prop="planProdTime"
                  label="下发时间"
                  width="120"
                  :formatter="momentFormat"
                  align="center"
          ></el-table-column>
          <el-table-column
            prop="productStatus"
            label="检验状态"
            align="center"
            width="100"
          ></el-table-column>

          <el-table-column
            v-if="showBfReason"
            prop="bfReason"
            label="报废类型"
            align="center"
          >
          </el-table-column>

          <el-table-column
            label="操作"
            align="center"
            fixed="right"
            width="150"
          >
            <template slot-scope="scope">
              <el-tooltip
                    effect="light"
                    :content="checkBtnName.name"
                    placement="top"
              >
                <el-button
                  :type="checkBtnName.type"
                  :icon="checkBtnName.icon"
                  size="mini"
                  v-if="scope.row.productStatus!='报废'&&scope.row.productStatus!='待报废审批'&&scope.row.productStatus!='出厂报废'&&scope.row.productStatus!='浇筑确认'"
                  @click="check(scope.row)"
                ></el-button>
              </el-tooltip>

              <el-tooltip
                      effect="light"
                      content="撤销"
                      placement="top"
              >
                <el-button
                        type="danger"
                        size="mini"
                        icon="el-icon-minus"
                        v-if="scope.row.productStatus=='待成品检' || scope.row.productStatus=='浇筑确认' "
                        @click="returnCheckStatus(scope.row)"
                ></el-button>
              </el-tooltip>

              <el-tooltip
                    effect="light"
                    content="审批"
                    placement="top"
              >
                <el-button
                type="success"
                size="mini"
                icon="el-icon-s-check"
                v-if="scope.row.productStatus=='待报废审批'"
                @click="approve(scope.row)"
              ></el-button>
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
    <el-dialog :title="checkBtnName.name"  v-if="editFormVisibleJzqr" :visible.sync="editFormVisibleJzqr" width="500px" top="200px">
            <check-edit-jzqr
                :product-detail="productDetailJzqr"
                @model-edit-jzqr-finish="modelEditJzqrFinish"
                @model-edit-jzqr-cancel="modelEditJzqrCancel"
            >
            </check-edit-jzqr>
     </el-dialog>

    <el-dialog :title="checkBtnName.name"  v-if="editFormVisibleBatch" :visible.sync="editFormVisibleBatch" width="500px" top="200px">
      <check-edit-batch
              :product-detail="productDetailJzqr"
              @model-edit-jzqr-finish="modelEditBatchFinish"
              @model-edit-jzqr-cancel="modelEditBatchCancel"
      >
      </check-edit-batch>
    </el-dialog>

    <el-dialog
      :title="checkBtnName.name"
      :visible.sync="editFormVisible"
      v-if="editFormVisible"
      width="500px"
      top="10px"
    >
      <model-edit
        :product-detail="productDetail"
        :mode="mode"
        @model-edit-finish="modelEditFinish"
        @model-edit-cancel="modelEditCancel"
      ></model-edit>
    </el-dialog>
    <el-dialog
      title="报废审核"
      :visible.sync="editFormApproveVisible"
      width="500px"
    >
      <approve-edit
        :product-detail="productDetail"
        @approve-edit-finish="approveEditFinish"
        @approve-edit-cancel="approveEditCancel"
      ></approve-edit>
    </el-dialog>
    <el-image-viewer
      v-if="checkImagesVisible"
      :on-close="closeViewer"
      :url-list="checkSelected"
      />
  </div>

</template>
<script>
import model from '../../../model/model'
import CheckEdit from './CheckEdit.vue'
import CheckEditJZQR from './CheckEditJZQR.vue'
import CheckEditBatch from './CheckEditBatch.vue'
import ApproveEdit from './ApproveEdit.vue'
import moment from 'moment'
import { Notification } from 'element-ui'
import serverConfig from '../../../../server.config'
import menuTabService from '../../../service/menuTabService'
import ProductDetailCriteria from '../stock/ProductDetailCriteria.vue'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer.vue'
import { showFullScreenLoading, tryHideFullScreenLoading } from '@/common/js/loading.js'
var numeral = require('numeral')
export default {
  components: {
    'model-edit': CheckEdit,
    'check-edit-jzqr':CheckEditJZQR,
    'check-edit-batch':CheckEditBatch,
    'approve-edit': ApproveEdit,
    'product-detail-criteria': ProductDetailCriteria,
    ElImageViewer
  },
  data() {
    return {
      //表查询
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: 20,
        pageSizes: this.GLOBAL.pageSizes
      },
      entity: {
      },
      entities: [],
      //行操作
      //编辑表单
      mode: '',
      editFormVisible: false,
      editFormVisibleJzqr:false,
      editFormVisibleBatch:false,
      editFormApproveVisible: false,
      productDetail: null,
      productDetailJzqr:null,
      checkImagesVisible: false,
      checkSelected: null,
      expands: [],
      checkList: [],
      showBfReason: false,
      showDCPJReason:false,
      checkType:'checkPro',
      buttonData:[
          {
              name:'批量操作',
              event:'bactchJZQR',
              authorized: 'productDetailCheck:batchEdit',
              display: this.getBtn('成品检'),
          },
          {
              name:'批量操作',
              event:'bactchJZQR',
              authorized: 'productDetailCheck:batchEditYJ',
              display: this.getBtn('隐检'),
          },
          {
              name:'批量操作',
              event:'bactchJZQR',
              display: this.getBtn('浇筑确认'),
          },
      ],
      selected:[],
      selectedIds:[],
    }
  },
  computed: {
    checkBtnName() {
      let active = this.$store.state.menuActiveName
      let editBtn = {}
      if (active == 'productDetailHiddenCheckList' || active == 'productDetailHiddenCheckListReturn') {
        editBtn = {
                      type: 'primary',
                      icon: 'el-icon-view',
                      name: '隐检'
                  }

      } else if (active == 'productDetailCheckList' || active == 'productDetailCheckListReturn') {
        editBtn = {
                      type: 'success',
                      icon: 'el-icon-view',
                      name: '成品检'
                  }

      } else if (active == 'productDetailJZQRCheckList') {
        editBtn = {
                      type: 'primary',
                      icon: 'el-icon-finished',
                      name: '浇筑确认'
                  }
      }
      return editBtn
    },
  },
  mounted() {
    this.list()
    // 图片加载完成时关闭loading 修改element-ui组件的方法
    ElImageViewer.methods.handleImgLoad = ()=>{
      tryHideFullScreenLoading()
    }
  },
  methods: {
      getBtn(type){
          let active = this.$store.state.menuActiveName
          let typeName = "";
          if (active == 'productDetailHiddenCheckList' || active == 'productDetailHiddenCheckListReturn') {
              typeName = '隐检'
          } else if (active == 'productDetailCheckList' || active == 'productDetailCheckListReturn') {
              typeName = '隐检'
          } else if (active == 'productDetailJZQRCheckList') {
              typeName = '浇筑确认'
          }

          if(typeName != type){
              return true;
          }
      },
      authorized(index) {
          return (menuTabService.authorized(this.$store, index) || this.checkBtnName.name !="浇筑确认")
      },
      bactchJZQR(){
          let arr = new Array()
          let productDetailIds = "";
          let productNos = "";
          let productNames = "";
          this.productDetailJzqr=this.selected[0];
          this.selected.forEach((row)=>{
              this.selectedIds.push(row.id)
              productDetailIds+=row.id+",";
              productNos+=row.product.productNo+",";
              productNames+=row.product.productName+",";
              arr.push(row)//把选中的构件放到数组中
          })
          if(!productDetailIds){
              return;
          }
          productDetailIds = productDetailIds.substring(0,productDetailIds.length-1);
          productNos = productNos.substring(0,productNos.length-1);
          productNames = productNames.substring(0,productNames.length-1);
          this.productDetailJzqr.productDetailIds = productDetailIds;
          this.productDetailJzqr.product.productNos = productNos;
          this.productDetailJzqr.product.productNames = productNames;


          let active = this.$store.state.menuActiveName
           if (active != 'productDetailJZQRCheckList') {
              this.editFormVisibleBatch=true
          }else{
              this.editFormVisibleJzqr=true
          }
      },
      returnCheckStatus(row){
          console.log(row)
          this.$confirm('此操作将回退构件到上一级质检状态, 是否继续?', '提示', {
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
              let active = this.$store.state.menuActiveName
              let checkType = ''
              if (active == 'productDetailHiddenCheckList' || active == 'productDetailHiddenCheckListReturn') {
                  checkType = '隐检'
              } else if (active == 'productDetailCheckList' || active == 'productDetailCheckListReturn') {
                  checkType = '成品检'
              } else if (active == 'productDetailJZQRCheckList') {
                  checkType = '浇筑确认';
              }
              model.returnCheckStatus(row.id,checkType).then(data => {
                  if (data.status == this.GLOBAL.STATUS.FAILED) {
                      Notification({
                          message: data.msg,
                          type: this.GLOBAL.STATUS.WARNING
                      });
                      loading.close()
                      return;
                  }
                  Notification({
                      message: '回退成功',
                      type: this.GLOBAL.STATUS.SUCCESS
                  });
                  loading.close();
                  this.list()
              })
          }).catch(() => {
          })
      },
    showSubData(row, expandedRows) {
      let id = row.id;
      if (this.expands.some((e) => e == row.id)) {
        this.expands = []
      } else {
        this.expands = []
        this.expands.push(row.id)
      }
      model.showSubData('checkList', id).then(data => {
        let list = data.entity;
        list.forEach((check) => {
          check.items = JSON.parse(check.itemResultJson)
          let s0 = '';
          if(check.items){
              check.items.forEach(e => {
                  if (e.result == '不合格') {
                      s0 += e.name + ",";
                  }
              })
              s0 = s0.substring(0, s0.length - 1)
          }
          check.bhgx = s0;
          let imgs = []
          if (check.checkImages) {
            check.checkImages.split(";").forEach((fileName) => {
              imgs.push({
                id: fileName,
                src: serverConfig.baseURL + '/file?tableName=ck_check&id=' + check.id + "&fileName=" + fileName
              });
            })
          }
          check.checkImages = imgs;
        })
        this.checkList = list
      })
    },
    changeCriteria(entity) {
      this.entity = entity
      this.list()
    },
    //表查询
    list() {
      let active = this.$store.state.menuActiveName
      let productStatus = ''
      if (active == 'productDetailHiddenCheckList') {
        productStatus = '待隐检'
        this.checkType='checkDYJ'
      } else if (active == 'productDetailHiddenCheckListReturn') {
        productStatus = '隐检返修'
      } else if (active == 'productDetailCheckList') {
        productStatus = '待成品检'
        this.checkType ='checkDCPJ'
        this.showDCPJReason = true
      } else if (active == 'productDetailCheckListReturn') {
        productStatus = '返修'
      } else if (active == 'productDetailCheckListUselessApprove') {
        productStatus = '待报废审批'
        //是否显示列
        this.showBfReason = true
      } else if (active == 'productDetailCheckListUseless') {
        productStatus = '报废';
        this.showBfReason = true
      } else if (active == 'productDetailJZQRCheckList') {
        productStatus = '浇筑确认';
        this.checkType='checkJZQR'
      }
      if (productStatus != '') {
        this.entity.productStatus = productStatus
      }
      this.entity.listType = 'check'
      model.list('productDetail', this.entity, this.pageInfo).then(data => {
        let entity = data.entity
        let list = data.entity.content;
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
    momentFormat(row, column, cellValue, index) {
      if (cellValue) {
        return moment(cellValue).format('YYYY-MM-DD')
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
      modelEditBatchFinish() {
          this.editFormVisibleBatch = false
          this.list()
      },
      modelEditBatchCancel() {
          this.editFormVisibleBatch = false
      },
    //编辑表单
    modelEditJzqrFinish(){
        this.editFormVisibleJzqr=false
        this.list()
    },
    modelEditJzqrCancel(){
        this.editFormVisibleJzqr=false
    },
    modelEditFinish() {
      this.editFormVisible = false
      this.list()
    },
    modelEditCancel() {
      this.editFormVisible = false
    },
    approveEditFinish() {
      this.editFormApproveVisible = false
      this.list()
    },
    approveEditCancel() {
      this.editFormApproveVisible = false
    },
    //行操作
    check(row) {
      this.productDetail = row
      this.editFormVisible = true
    },
    approve(row) {
      this.productDetail = row
      this.editFormApproveVisible = true
    },
    showCheckImages(row) {
      showFullScreenLoading()
      this.checkSelected = []
      for(let val of row.checkImages){
        this.checkSelected.push(val.src)
      }
      this.checkImagesVisible = true;
    },
    closeViewer() {
     this.checkImagesVisible = false
    }
  },
  watch: {
    checkImagesVisible: function (n, o) {
      if (!n) {
        this.checkSelected = null;
      }
    }
  }

}
</script>
<style>
#product-detail-check-list .red {
  color: red;
}
#product-detail-check-list .el-tag {
  cursor: pointer;
}
</style>
