<template>
  <div
    class="currency-style-box"
  >
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <product-detail-criteria
            list-type="check"
            @change-criteria="changeCriteria"
          ></product-detail-criteria>
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
                >
                </el-table-column>
                <el-table-column
                  prop="checkResult"
                  label="检验结果"
                  align="center"
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
                >
                  <template slot-scope="scope">
                    <el-button
                      icon="el-icon-search"
                      round
                      type="primary"
                      v-if="scope.row.checkImages.length>0"
                      @click="showCheckImages(scope.row)"
                    >
                      查看检验图片
                    </el-button>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="checkDesc"
                  label="检验说明"
                  align="center"
                  width="200"
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
                  type="selection"
                  width="39">
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            width="70"
          >
          </el-table-column>
          <el-table-column
            prop="projectName"
            label="项目名称"
            align="center"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="buildCode"
            label="楼栋"
            align="center"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="floorNum"
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
            prop="productNo"
            label="构件编码"
            align="center"
            width="140"
          >
          </el-table-column>
          <el-table-column
            prop="productName"
            label="构件名称"
            align="center"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="productWt"
            label="构件重量(t)"
            align="center"
            :formatter="wtFormat"
            width="120"
          >
          </el-table-column>
          <!--<el-table-column-->
            <!--prop="checkResult"-->
            <!--label="检验状态"-->
            <!--align="center"-->
            <!--width="100"-->
          <!--&gt;</el-table-column>-->
          <el-table-column
                  label="检验状态"
                  align="center"
                  width="100"
          >
            <template slot-scope="scope">
              <span v-if="scope.row.flag" style="color: red">{{scope.row.checkResult}}</span>
              <span v-else>{{scope.row.checkResult}}</span>
            </template>
          </el-table-column>
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
                  v-if="scope.row.productStatus!='报废'&&scope.row.productStatus!='待报废审批'&&scope.row.productStatus!='出厂报废'"
                  @click="check(scope.row)"
                ></el-button>
              </el-tooltip>
              <!-- <el-button type="success" size="mini" v-if="scope.row.productStatus=='待报废审批'"
                               @click="approve(scope.row)">审批
                    </el-button>-->
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
    <el-dialog
      title="检验图片"
      :visible.sync="checkImagesVisible"
      width="800px"
      top="10px"
      v-if="checkSelected!=null"
    >
      <div
        v-for="checkImage in checkSelected.checkImages"
        :key="checkImage.id"
      >
        <h3>{{checkImage.id}}</h3>
        <img
          :src="checkImage.src"
          :alt="checkImage.id"
          style="max-width:100%;"
        >
      </div>
    </el-dialog>
  </div>

</template>
<script>
import model from '../../../model/model'
import CheckEdit from './CheckEdit.vue'
import ApproveEdit from './ApproveEdit.vue'
import moment from 'moment'
import { Notification } from 'element-ui'
import serverConfig from '../../../../server.config'
import ProductDetailCriteria from '../stock/ProductDetailCriteria.vue'
import productDetailModel from '../../../model/project/productDetail'

var numeral = require('numeral')
export default {
  name: "ProductDetailCheckNewList",
  components: {
    'model-edit': CheckEdit,
    'approve-edit': ApproveEdit,
    'product-detail-criteria': ProductDetailCriteria
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
      entity: {},
      entities: [],
      //行操作
      //编辑表单
      mode: '',
      editFormVisible: false,
      editFormApproveVisible: false,
      productDetail: {
      },
      checkImagesVisible: false,
      checkSelected: null,
      expands: [],
      checkList: [],
      showBfReason: false,
      buttonData: [
          {
              name:'返修工单',
              event:'printBatchCPJFXD'
          }
      ]
    }
  },
  computed: {
    checkBtnName() {
      let active = this.$store.state.menuActiveName
      let editBtn = ''
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

  },
  methods: {
      printBatchCPJFXD(){
          let row = this.selected
          if(row&&row.length){
              let arr = new Array()
              this.selected.forEach((row) => {
                  console.log(row)
                  arr.push(row.id)
              })

              var url =  serverConfig.baseURL + '/reportChart/printBatchCPJFXD/'+arr
              var prams = {
                ids:arr
              }
              var temp = document.createElement("form");
              temp.action = url;
              temp.method = "post";
              temp.style.display = "none";
              for (var x in prams) {
                  var opt = document.createElement("textarea");
                  opt.name = x;
                  opt.value = prams[x];
                  temp.appendChild(opt);
              }
              document.body.appendChild(temp);
              temp.submit();
              return temp;

              // productDetailModel.printBatchCPJFXD(arr).then((data) => {
              // })
          }else{
              Notification({
                  message: '请至少选择一个',
                  type: 'warning'
              })
          }

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
          }

          s0 = s0.substring(0, s0.length - 1)
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
      productDetailModel.findFinishedProductList(this.entity, this.pageInfo).then((data) => {
        this.entities = data.entity.list
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
      this.productDetail.active = 'productDetailCheckListReturn'
      this.editFormVisible = true
    },
    approve(row) {
      this.productDetail = row
      this.editFormApproveVisible = true
    },
    showCheckImages(row) {
      this.checkImagesVisible = true;
      this.checkSelected = row;
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