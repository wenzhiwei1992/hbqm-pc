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
              <el-form-item prop="turnoverDNo">
                <el-input
                  placeholder="单据号"
                  v-model="materialStorageQuery.turnoverDNo"
                  style="width:150px"
                  clearable
                >
                </el-input>
              </el-form-item>
              <el-form-item prop="status">
                <el-input
                  placeholder="物料编码"
                  v-model="materialStorageQuery.materialStorageIdS"
                  style="width:150px"
                  clearable
                >
                </el-input>
              </el-form-item>
              <!--<el-form-item label="原有物料编码" prop="status">-->
              <!--<el-input-->
              <!--placeholder="请输入内容"-->
              <!--v-model="materialStorageQuery.materialNo"-->
              <!--style="width:150px"-->
              <!--clearable>-->
              <!--</el-input>-->
              <!--</el-form-item>-->
              <el-form-item prop="shipments">
                <el-select
                  v-model="materialStorageQuery.shipmentsId"
                  filterable
                  style="width:120px"
                  placeholder="供方"
                  clearable
                >
                  <el-option
                    v-for="item in shipmentsSelect"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="departmentId">
                <el-select
                  v-model="materialStorageQuery.departmentId"
                  filterable
                  style="width:120px"
                  placeholder="部门"
                  clearable
                >
                  <el-option
                    v-for="item in departmentSelect"
                    :key="item.id"
                    :label="item.deptName"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
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
              <el-form-item prop="status">
                <el-select
                  clearable
                  style="width: 150px"
                  v-model="materialStorageQuery.status"
                  placeholder="变动原因"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="projectId">
                <el-select v-model="materialStorageQuery.projectId"
                           placeholder="项目名称"
                           clearable
                           filterable
                           style="width: 150px;">
                  <el-option
                          v-for="item in projectList"
                          :key="item.id"
                          :label="item.projectName"
                          :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="dateCreated">
                <el-date-picker
                  v-model="materialStorageQuery.dateCreatedOne"
                  type="date"
                  style="width: 150px"
                  placeholder="创建开始日期"
                >
                </el-date-picker>
                <el-date-picker
                  v-model="materialStorageQuery.dateCreatedTwo"
                  type="date"
                  style="width: 150px"
                  placeholder="创建结束日期"
                >
                </el-date-picker>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col>
            <my-search-button-group :model="searchButtonData"></my-search-button-group>
          </el-col>
        </el-row>
        <el-row style="display:flex;justify-content: flex-end;">
          <el-col>
          <my-button-group :model="buttonData"></my-button-group>
          </el-col>
          <el-col style="text-align: left;font-weight:bold;">
            <span>入库量 :{{sumArr[0]}}m³ </span>
            <br>
            <span>出库量 :{{sumArr[1]}}m³</span>
            <br>
            <span>退库量 :{{sumArr[2]}}m³</span>
          </el-col>
        </el-row>
      </el-header>
      <el-main
        ref="mainContent"
        :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <el-table
          :data="exportdatas"
          id="exportXlsx"
          hidden
          height="100%"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
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
            prop="status"
            label="状态"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="deliveryOrderNo"
            label="单据号"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="shipments"
            label="供方"
            width="140"
          >
          </el-table-column>
          <el-table-column
                  sortable
                  prop="projectName"
                  label="项目"
                  width="140"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="departmentName"
            label="部门"
            width="140"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="materialStorageId"
            label="物料编码"
            width="120"
          >
          </el-table-column>
          <!--<el-table-column-->
          <!--sortable-->
          <!--prop="materialNo"-->
          <!--label="原有物料编码"-->
          <!--width="130">-->
          <!--</el-table-column>-->
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
            prop="repertory"
            label="库存量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="amount"
            label="数量"
            width="150"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="materialUnit"
            label="单位"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="univalence"
            label="单价"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="univalence"
            label="单价"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="totalUnivalence"
            label="总价"
            width="80"
          >
          </el-table-column>
          <el-table-column
            sortable
            label="创建时间"
            width="140"
          >
            <template slot-scope="scope">
              <span>{{scope.row.dateCreated}}　</span>
            </template>
          </el-table-column>
          <el-table-column
            sortable
            label="修改时间"
            width="140"
          >
            <template slot-scope="scope">
              <span>{{scope.row.dateModified}}　</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="remark"
            label="备注"
            width="120"
          >
          </el-table-column>

        </el-table>

        <el-table
          :data="materialStorageTurnovers"
          @selection-change="handleSelectionChange"
          :default-sort="{prop: 'dateCreated', order: 'descending'}"
          size="mini"
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
            prop="status"
            label="状态"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="deliveryOrderNo"
            label="单据号"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="shipments"
            label="供方"
            width="140"
          >
          </el-table-column>
          <el-table-column
                  sortable
                  prop="projectName"
                  label="项目"
                  width="140"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="departmentName"
            label="部门"
            width="140"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="materialStorageId"
            label="物料编码"
            width="120"
          >
          </el-table-column>
          <!--<el-table-column-->
          <!--sortable-->
          <!--prop="materialNo"-->
          <!--label="原有物料编码"-->
          <!--width="130">-->
          <!--</el-table-column>-->
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
            prop="repertory"
            label="库存量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="amount"
            label="数量"
            width="150"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="materialUnit"
            label="单位"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="univalence"
            label="单价"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="totalUnivalence"
            label="总价"
            width="80"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="dateCreated"
            label="创建时间"
            width="140"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="dateModified"
            label="修改时间"
            width="140"
          >
          </el-table-column>
          <el-table-column
            prop="remark"
            label="备注"
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
  </div>
</template>

<script>
import materialStorageModel from '../../../../model/basicdata/materialStorage'
import departmentModel from '../../../../model/basicdata/dept'
import supplierModel from '../../../../model/basicdata/supplier'
import xlsx from 'xlsx';
import fileSaver from 'file-saver';
import moment from "moment"
import projectModel from "../../../../model/project/project";

export default {
  data() {
    return {
      options: [{
        value: '已入库',
        label: '入库'
      }, {
        value: '已发料',
        label: '出库'
      },
      {
        value: '已退料',
        label: '退料'
      },
      ],
      materialStorageQuery: {
        turnoverDNo: '',
        materialStorageIdS: '',
        materialName: '',
        materialSpecification: '',
        dateCreatedOne: '',
        dateCreatedTwo: '',
        status: '',
        shipmentsId: '',
        projectId:'',
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        },
      },
      materialStorageTurnovers: [],
      selected: [],
      shipmentsSelect: [],
      departmentSelect: [],
      exportdatas: [],
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
      rowButtonData: [],
      projectList:[],
        sumArr:[]
    }
  },
  name: "TurnoverDetailReportForms",
  mounted() {
    this.info()
    this.list()
    this.shipmentsList()
    this.departmentList()
  },
  methods: {
    info(){
      projectModel.getExternalAllProject().then((data) => {
        this.projectList = data.entity
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
      })
    },
    exportExcel() {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      let page = this.materialStorageQuery.pageInfo.currentPage;
      let size = this.materialStorageQuery.pageInfo.pageSize;
      // console.log(page)
      // console.log(size)
      this.materialStorageQuery.pageInfo.currentPage = 1;
      this.materialStorageQuery.pageInfo.pageSize = this.materialStorageQuery.pageInfo.total;
      //TODO 分页
      materialStorageModel.getMateralStorageTurnoverDetail(this.materialStorageQuery).then(data => {
        this.exportdatas = data.entity.list;
        setTimeout(() => {
          var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
          excelName = "物料进出明细表" + excelName;
          let table = document.querySelector('#exportXlsx');
          var wb = xlsx.utils.table_to_book(table);
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
    list() {
        materialStorageModel.getmaterialStorageTurnoverReportFormsSum(this.materialStorageQuery).then((data) => {
            this.sumArr = data.entity
        })
      materialStorageModel.getMateralStorageTurnoverDetail(this.materialStorageQuery).then((data) => {
        this.materialStorageTurnovers = data.entity.list
        this.materialStorageQuery.pageInfo.total = data.entity.totalCount
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.list()
        }
        else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    handleSelectionChange(selected) {
      this.selected = selected
    },
    handleSizeChange(val) {
      this.materialStorageQuery.pageInfo.pageSize = val
      this.list()
    },
    handleCurrentChange(val) {
      this.materialStorageQuery.pageInfo.currentPage = val
      this.list()
    },
    // exportExcelPost(URL, PARAMS) {
    //     var temp = document.createElement("form");
    //     temp.action = URL;
    //     temp.method = "post";
    //     temp.style.display = "none";
    //     for (var x in PARAMS) {
    //         var opt = document.createElement("textarea");
    //         opt.name = x;
    //         opt.value = PARAMS[x];
    //         temp.appendChild(opt);
    //     }
    //     document.body.appendChild(temp);
    //     temp.submit();
    //     return temp;
    // },
  },

}
</script>

<style>
.el-table .cell.el-tooltip {
  white-space: pre;
}


                         .color_span_class {
                           color: #67C23A;
                           font-weight: 800;
                         }

.color_font_span_class {
  font-weight: 800;
}
.block_class{
  display:block;
  margin-bottom:5px;
}
.text_class{
  position:absolute;
  right:0;
  bottom:0;
  margin-bottom: 5px;
.text{
span{
  vertical-align:text-bottom;
  line-height:0;
}
}
}
</style>
