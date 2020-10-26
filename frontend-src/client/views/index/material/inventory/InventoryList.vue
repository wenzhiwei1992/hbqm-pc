<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form
              :inline="true"
              id="projectList"
              :model="materialStorageQuery"
              ref="materialStorageQuery"
            >
              <!--                <el-form-item label="物料编码" prop="materialStorageId">
                                    <el-input
                                        placeholder="请输入内容"
                                        v-model="materialStorageQuery.materialStorageIdS"
                                        style="width:150px"
                                        clearable>
                                </el-input>
                                </el-form-item>-->

              <el-form-item prop="status">
                <el-input
                  placeholder="物料编码"
                  v-model="materialStorageQuery.materialStorageIdS"
                  style="width:150px"
                  clearable
                >
                </el-input>
              </el-form-item>
              <el-form-item prop="materialName">
                <el-input
                  placeholder="物料名称"
                  v-model="materialStorageQuery.materialName"
                  style="width:150px"
                  clearable
                >
                </el-input>
              </el-form-item>
              <el-form-item prop="materialSpecification">
                <el-input
                  placeholder="物料规格"
                  v-model="materialStorageQuery.materialSpecification"
                  style="width:150px"
                  clearable
                >
                </el-input>
              </el-form-item>
              <el-form-item prop="categoryId">
                <el-select
                  v-model="materialStorageQuery.categoryIds"
                  filterable
                  multiple
                  placeholder="物料分类"
                  style="width:150px"
                  clearable
                >
                  <el-option
                    v-for="item in arrClassification"
                    :key="item.id"
                    :label="item.classificationName"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <!--<el-form-item label="物料类别" prop="categoryId">-->
              <!--<el-select v-model="materialStorageQuery.categoryId" placeholder="请选择" style="width:150px"-->
              <!--clearable>-->
              <!--<el-option-->
              <!--v-for="item in arrCategory"-->
              <!--:key="item.id"-->
              <!--:label="item.categoryName"-->
              <!--:value="item.id">-->
              <!--</el-option>-->
              <!--</el-select>-->
              <!--</el-form-item>-->
              <el-form-item prop="orgId">
                <el-select
                  v-model="materialStorageQuery.orgId"
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
        <el-row>
          <my-button-group :model="buttonData"></my-button-group>
        </el-row>
      </el-header>
      <el-main
        ref="mainContent"
        class="main-content"
        :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <el-table
          :data="materialStorages"
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
            prop="materialStorageId"
            label="物料编码"
            width="120"
          >
            <template slot-scope="scope">
              <a
                style="cursor:default"
                @click="showM(scope.row.materialStorageId)"
              >{{scope.row.materialStorageId}}</a>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            prop="materialName"
            label="物料名称"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="materialSpecification"
            label="物料规格"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="materialUnit"
            label="计量单位"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="totalAmountNum"
            label="总入库量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="totalSendNum"
            label="总出库量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="repertory"
            label="当前库存"
            width="120"
          >
            <template slot-scope="scope">
              <span
                v-if="scope.row.repertory  < scope.row.inventory"
                style="color: #F56C6C;font-weight: 800;"
              >{{scope.row.repertory}}</span>
              <span
                v-else
                style="color: #67C23A;font-weight:800;"
              >{{scope.row.repertory}}</span>
            </template>
          </el-table-column>

          <el-table-column
            sortable
            prop="numReturn"
            label="可申请量"
            width="120"
          >
          </el-table-column>

          <el-table-column
            sortable
            prop="waitNum"
            label="待发料量"
            width="120"
          >
          </el-table-column>

          <!--<el-table-column-->
          <!--sortable-->
          <!--prop="inventory"-->
          <!--label="最小库存"-->
          <!--width="120">-->
          <!--</el-table-column>-->
          <el-table-column
            sortable
            prop="stockName"
            label="仓库名称"
            width="120"
          >
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
            prop="classificationName"
            label="物料分类"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="categoryName"
            label="物料类型"
            width="120"
          >
          </el-table-column>

          <!--            <el-table-column
                                sortable
                                prop="checkout"
                                label="是否为免检物料"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                sortable
                                prop="decrease"
                                label="是否自动销存库"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                sortable
                                prop="purchasePeriod"
                                label="采购周期"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                sortable
                                prop="dateCreated"
                                :formatter="formatterDate"
                                label="创建时间"
                                width="140">
                        </el-table-column>
                        <el-table-column
                                sortable
                                prop="dateModified"
                                :formatter="formatterDate"
                                label="修改时间"
                                width="140">
                        </el-table-column>-->

        </el-table>
        <el-table
          :data="exportdatas"
          size="mini"
          stripe
          border
          row-key="id"
          height="500"
          id="exportXlsx"
          hidden
          style="width: 100%"
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
            label="物料编码"
            width="120"
          >
            <template slot-scope="scope">
              <a
                style="cursor:default"
                @click="showM(scope.row.materialStorageId)"
              >{{scope.row.materialStorageId}}</a>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            prop="materialName"
            label="物料名称"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="materialSpecification"
            label="物料规格"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="materialUnit"
            label="计量单位"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="repertory"
            label="当前库存"
            width="120"
          >
            <template slot-scope="scope">
              <span
                v-if="scope.row.repertory  < scope.row.inventory"
                style="color: #F56C6C;font-weight: 800;"
              >{{scope.row.repertory}}</span>
              <span
                v-else
                style="color: #67C23A;font-weight:800;"
              >{{scope.row.repertory}}</span>
            </template>
          </el-table-column>
          <!--<el-table-column-->
          <!--sortable-->
          <!--prop="inventory"-->
          <!--label="最小库存"-->
          <!--width="120">-->
          <!--</el-table-column>-->
          <el-table-column
            sortable
            prop="orgName"
            label="基地名称"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="classificationName"
            label="物料分类"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="categoryName"
            label="物料类型"
            width="120"
          >
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer ref="footOperationBar">
        <el-row>
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="materialStorageQuery.pageInfo.currentPage"
              :page-sizes="materialStorageQuery.pageInfo.pageSizes"
              :page-size="materialStorageQuery.pageInfo.pageSize"
              :total="materialStorageQuery.pageInfo.total"
              layout="total, sizes, prev, pager, next, jumper"
              background
            >
            </el-pagination>
        </el-row>
      </el-footer>
    </el-container>

    <el-dialog
      :visible.sync="materialEditFormVisible"
      width="70%"
    >
      <materials-info
        :materialsChildren="materialsChildren"
        :arr="arr"
        @material-status-finish="checkoutStatusFinish"
        @material-status-cancel="checkoutStatusCancel"
      ></materials-info>
    </el-dialog>
  </div>
</template>

<script>
import materialStorageModel from '../../../../model/basicdata/materialStorage'
import classificationModel from '../../../../model/basicdata/classification'
import categoryModel from '../../../../model/basicdata/category'
import orgModel from '../../../../model/system/org'
import MaterialsInfo from './MaterialsInfo.vue'
import { Notification } from 'element-ui'
import xlsx from 'xlsx';
import fileSaver from 'file-saver';
import moment from "moment"

export default {
  data() {
    return {
      exportdatas: [],
      materialsChildren: '',
      materialEditFormVisible: false,
      materialEditForm: null,
      arrClassification: [],
      arrCategory: [],
      orgs: [],
      materialStorageQuery: {
        classificationId: '',
        categoryId: '',
        categoryIds: [],
        materialStorageIdS: '',
        materialName: '',
        materialSpecification: '',
        orgId: '',
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        },
      },
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: this.GLOBAL.pageSize,
        pageSizes: this.GLOBAL.pageSizes
      },
      materialStorages: [],
      selected: [],
      arr: [],

      buttonData: [
        {
          name: '导出',
          event: 'exportExcel'
        }
      ],
      searchButtonData: [
        {
          name: '查询',
          event: "submitForm('materialStorageQuery')"
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
  name: "InventoryList",
  components: {
    'materials-info': MaterialsInfo,
  },
  mounted() {
    this.materialList()
    this.info()
  },
  methods: {
    exportExcel() {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      let page = this.pageInfo.currentPage;
      let size = this.pageInfo.pageSize;
      this.materialStorageQuery.pageInfo.currentPage = 1;
      this.materialStorageQuery.pageInfo.pageSize = this.materialStorageQuery.pageInfo.total;
      //TODO 分页
      materialStorageModel.getRepertoryByLimit(this.materialStorageQuery).then(data => {
        this.exportdatas = data.entity.list;
        setTimeout(() => {
          var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
          excelName = "原料库存" + excelName;
          // console.log(excelName)
          /* generate workbook object from table */
          let table = document.querySelector('#exportXlsx');
          var wb = xlsx.utils.table_to_book(table);
          /* get binary string as output */
          var wbout = xlsx.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' })
          try {
            fileSaver.saveAs(new Blob([wbout], { type: 'application/octet-stream' }), excelName)
          } catch (e) {
            if (typeof console !== 'undefined') console.log(e, wbout)
          }

          this.materialStorageQuery.pageInfo.pageSize = size;
          this.materialStorageQuery.pageInfo.currentPage = page;
          loading.close();
          return wbout;
        }, 500);

      }).catch(err => {

      })


    },
    showM(id) {
      materialStorageModel.getMaterialStorageByMaterialStorageId(id).then((data) => {
        this.materialsChildren = data.entity
        this.arr = []
        this.arr.push(this.materialsChildren)
        this.materialEditFormVisible = true
      })
    },
    materialList() {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      materialStorageModel.getRepertoryByLimit(this.materialStorageQuery).then((data) => {
        this.materialStorages = data.entity.list
        this.materialStorageQuery.pageInfo.total = data.entity.totalCount
        loading.close()
      })
    },
    /**
     *模糊查询
     */
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.materialList()
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    info() {
      orgModel.orgAllByOrgIds().then((data) => {
        this.orgs = data.entity
      })
      classificationModel.getClassifications().then((data) => {
        this.arrClassification = data.entity
      })
    },
    showCategorys(id) {
      this.materialStorageQuery.categoryId = ''
      this.arrCategory = []
      if (!id) {
        return
      }
      categoryModel.getCategorysByClassificationId(id).then((data) => {
        this.arrCategory = data.entity
      })
    },
    handleSelectionChange(selected) {
      this.selected = selected
    },
    handleSizeChange(val) {
      this.materialStorageQuery.pageInfo.pageSize = val
      this.materialList()
    },
    handleCurrentChange(val) {
      this.materialStorageQuery.pageInfo.currentPage = val
      this.materialList()
    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    //编辑表单
    materialStorageEditFinish() {
      this.editFormVisible = false
      this.materialList()
    },
    materialStorageEditCancel() {
      this.editFormVisible = false
      this.materialList()
    },
    checkoutStatusFinish() {
      this.materialEditFormVisible = false
      this.materialList()
    },
    checkoutStatusCancel() {
      this.materialEditFormVisible = false
      this.materialList()
    }
  },

}
</script>

<style lang="scss" scoped>
  .main-content{
    position:relative;
    &:before{
      content:'*当前库存:红色表示库存不足';
      color:red;
      line-height:20px;
      font-size:12px;
      position:absolute;
      z-index:999;
      bottom:0px;
      left:20px;
    }
    &:after{
      content:'绿色表示库存充裕';
      color:rgb(103, 194, 58);
      line-height:20px;
      font-size:12px;
      position:absolute;
      z-index:999;
      bottom:0px;
      left:14.5em;
    }
  }
</style>