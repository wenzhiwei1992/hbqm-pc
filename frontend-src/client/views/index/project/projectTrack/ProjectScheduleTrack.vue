<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form
              :inline="true"
              :model="formInline"
              id="formInline"
              ref="formInline"
              class="demo-form-inline"
              size="mini"
            >
              <el-form-item>
                <el-select
                  v-model="formInline.projectId"
                  placeholder="项目名称"
                  clearable
                >
                  <el-option
                    v-for="item in projectNames"
                    :key="item.id"
                    :label="item.projectName"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="formInline.productNo"
                  placeholder="构件编码"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="formInline.productName"
                  placeholder="构件名称"
                ></el-input>
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
        :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <!-- 项目构件进度表 -->
        <el-table
          :data="datas"
          size="mini"
          stripe
          border
          ref="handleClickRowSelection"
          height="100%"
          @row-click="handleClickRowSelection"
          style="width: 100%"
          @expand-change="showSubData2"
          :expand-row-keys="expands2"
          row-key="productId"
        >
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-table
                :data="subDatas2"
                size="mini"
                stripe
                border
                style="width: 100%"

              >
                <el-table-column
                        prop="buildCode"
                        label="楼栋"
                        :filters="filterBuild"
                        :filter-method="filterHandler"
                        filter-placement="bottom-end"
                >
                </el-table-column>
                <el-table-column
                        prop="floorNum"
                        label="楼层"
                        :filters="filterNum"
                        :filter-method="filterHandler"
                        filter-placement="bottom-end"
                >
                </el-table-column>
                   <el-table-column
                     prop="schedule"
                     label="生产完成进度"
                   >
                     <template slot-scope="scope">
                       <el-progress
                         :text-inside="true"
                         :stroke-width="18"
                         :percentage="scope.row.schedule"
                       ></el-progress>
                     </template>
                   </el-table-column>
                   <el-table-column
                     prop="needTotal"
                     label="需求量"
                   >
                   </el-table-column>
                   <el-table-column
                     prop="overTotal"
                     label="完成量"
                   >
                   </el-table-column>
                  <el-table-column
                    prop="jzCount"
                    label="浇筑量"
                  >
                    <template slot-scope="scope">
                      <span>{{ scope.row.jzCount + scope.row.fhCount }}</span>
                    </template>
                  </el-table-column>
                   <el-table-column
                     prop="fhCount"
                     label="发货量"
                   >
                   </el-table-column>
                 </el-table>
               </template>
             </el-table-column>
          <el-table-column
            prop="projectName"
            label="项目名称"
          >
          </el-table-column>
          <el-table-column
                  prop="productName"
                  label="构件名称"
          >
          </el-table-column>
          <el-table-column
                  prop="productNo"
                  label="构件编码"
          >
          </el-table-column>

          <el-table-column
            prop="schedule"
            label="生产完成进度"
          >
            <template slot-scope="scope">
              <el-progress
                      :text-inside="true"
                      :stroke-width="18"
                      :percentage="scope.row.overTotal>scope.row.needTotal?100:parseFloat(scope.row.overTotal/scope.row.needTotal*100).toFixed(3)*1"
                      :color="customColorMethod"
              ></el-progress>
            </template>
          </el-table-column>
          <el-table-column
            prop="needTotal"
            label="需求量"
          >
          </el-table-column>
          <el-table-column
            prop="overTotal"
            label="完成量"
          >
          </el-table-column>
          <el-table-column
             prop="jzCount"
             label="浇筑量"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.jzCount + scope.row.fhCount }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="fhCount"
            label="发货量"
          >
          </el-table-column>
          <el-table-column
            prop="zkCount"
            label="在库量"
          >
          </el-table-column>
        </el-table>
        <!--导出项目进度预览-->
        <el-table
                :data="exportdatas"
                size="mini"
                stripe
                border
                height="500"
                id="exportXlsx"
                hidden
                style="width: 100%"
        >
          <el-table-column
                  prop="projectName"
                  label="项目名称"
          >
          </el-table-column>
          <el-table-column
                  prop="productName"
                  label="构件名称"
          >
          </el-table-column>
          <el-table-column
                  prop="productNo"
                  label="构件编码"
          >
          </el-table-column>

          <el-table-column
                  prop="schedule"
                  label="生产完成进度(%)"
          >
            <template slot-scope="scope">
              <span>{{parseFloat(scope.row.overTotal/scope.row.needTotal*100).toFixed(3)*1}}</span>％
            </template>
          </el-table-column>
          <el-table-column
                  prop="needTotal"
                  label="需求量"
          >
          </el-table-column>
          <el-table-column
                  prop="overTotal"
                  label="完成量"
          >
          </el-table-column>
          <el-table-column
                  prop="fhCount"
                  label="发货量"
          >
          </el-table-column>
          <el-table-column
                  prop="zkCount"
                  label="在库量"
          >
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
  </div>
</template>

<script>
import prodcutTrack from '../../../../model/project/productTrack'
import orgModel from '../../../../model/system/org'
import buildingModel from '../../../../model/project/building'
import floorModel from '../../../../model/project/floor'
import xlsx from 'xlsx';
import fileSaver from 'file-saver';
import { filterData,filterHandler } from '@/util/filter.js'
import moment from "moment";

export default {
  data() {
    return {
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: this.GLOBAL.pageSize,
        pageSizes: this.GLOBAL.pageSizes
      },
      pageInfo2: {
        currentPage: 1,
        total: 0,
        pageSize: this.GLOBAL.pageSize,
        pageSizes: this.GLOBAL.pageSizes
      },
      datas: [],
      exportdatas: [],
      selected: [],
      expands: [],
      expands2: [],
      subDatas: [],
      subDatas2: [],
      formInline: {
        projectId: '',
        lineName: '',
        productType: '',
        productNo: '',
        productName: '',
        buildCode: '',
        floorNum: '',
        baseName: ''
      },
      query: {
        projectId: '',
        pageInfo: null
      },
      query2: {
        projectId: '',
        productId: '',
        productNo: '',
        productName: '',
        baseName: '',
        pageInfo: null
      },
      productTypes: [],
      lineNames: [],
      projectNames: [],
      buildings: [],
      floors: [],
      orgs: [],
      buttonData: [
        {
          name: '导出',
          event: 'exportExcel'
        }
      ],
      searchButtonData: [
        {
          name: '查询',
          event: "onSubmit"
        },
        {
          name: '重置',
          event: 'reset("formInline")'
        }
      ],
      rowButtonData: [

      ],
      filterBuild:[],
      filterNum:[],
      filterHandler:null
    }

  },
  mounted() {
    orgModel.orgAllByOrgIds().then(data => {
      this.orgs = data.entity;
    }).catch(() => {

    })
    this.list()
    // 全局的filterHandler复制给当前页面的filterHandler  好让html可以直接拿到
    this.filterHandler = filterHandler
  },
  methods: {
    exportExcel() {
      let page = this.pageInfo.currentPage;
      let size = this.pageInfo.pageSize;
      this.formInline.pageInfo.currentPage = 1;
      this.formInline.pageInfo.pageSize = this.pageInfo.total;
      //TODO 分页
      prodcutTrack.findProjectByCondition(this.formInline).then(data => {
        this.exportdatas = data.entity.data;
        setTimeout(() => {
          var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
          excelName = "项目进度预览" + excelName;
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

          this.formInline.pageInfo.pageSize = size;
          this.formInline.pageInfo.currentPage = page;
          return wbout;
        }, 500);

      }).catch(err => {

      })
    },
    customColorMethod(percentage) {
      if (percentage <= 99) {
        return '#409eff';
      } else {
        return '#67c23a';
      }
    },
    showSubData(row, expandedRows) {
      //根据项目显示子信息
      if (!row.projectId) {
        row.projectId = -1;
      }
      let id = parseInt(row.projectId);
      if (this.expands.some((e) => e == row.projectId)) {
        this.expands = []
      } else {
        this.expands = []
        this.expands.push(row.projectId)
      }
      this.query.pageInfo = this.pageInfo2
      this.query.projectId = id;
      prodcutTrack.getOrderTrack(this.query).then(data => {
        this.subDatas = data.entity;
        this.pageInfo2.total = data.entity.length;
      })
    },
    showSubData2(row, expandedRows) {
      let id = row.productId;
      if (this.expands2.some((e) => e == row.productId)) {
        this.expands2 = []
      } else {
        this.expands2 = []
        this.expands2.push(row.productId)
      }
      this.query2.projectId = row.projectId;
      this.query2.productId = row.productId;
      prodcutTrack.findProductScheduleToBFInfo(this.query2).then(data => {
        this.subDatas2 = data.entity;
        this.filterNum = filterData(this.subDatas2,'floorNum')
        this.filterBuild = filterData(this.subDatas2,'buildCode')
      })
    },
    changeProject() {
      buildingModel.findByProjectId(this.formInline.projectId).then(data => {
        this.buildings = data.entity
        this.formInline.buildCode = ''
        this.formInline.floorNum = ''
        this.floors = []
      })
    },
    changeBuilding(id) {
      this.formInline.floorNum = ''
      this.floors = []
      if (!id) {
        return
      }
      let building = this.buildings.find(b => b.id == id)
      function compare(property) {
        return function (a, b) {
          var value1 = a[property];
          var value2 = b[property];
          return value1 - value2;
        }
      }
      floorModel.findByBuildingIdAndProjectId(building.id, building.projectId).then((data) => {
        this.floors = data.entity.sort(compare('floorNum'))
        // this.entity.floorId = null
      })
      this.floors = building.floors.sort(compare('floorNum'))
    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    onSubmit() {
      //更新数据，同时分页
      prodcutTrack.findProjectByCondition(this.formInline).then(data => {
        this.datas = data.entity.data;
        this.pageInfo.total = data.entity.totalCount;
      })
    },
    //重置
    reset(formInline){
      this.$refs[formInline].resetFields();
      this.formInline.productNo = ''
      this.formInline.productName = ''
      this.formInline.projectId = ''
    },
    list() {
      prodcutTrack.findAllProjectList().then(data => {
        this.projectNames = data.entity;
      })
      this.formInline.pageInfo = this.pageInfo;
      return prodcutTrack.findProjectByCondition(this.formInline).then(data => {
        this.datas = data.entity.data;
        this.pageInfo.total = data.entity.totalCount;
      }).catch(err => {
        console.error(err)
      })
    },
    handleSizeChange(val) {
      this.pageInfo.pageSize = val;
      this.list()
    },
    handleCurrentChange(val) {
      this.pageInfo.currentPage = val;
      this.list()
    },
    handleSelectionChange(selected) {
      this.selected = selected
    },
    handleSizeChange2(val) {
      this.pageInfo.pageSize = val;
      this.showSubData()
    },
    handleCurrentChange2(val) {
      this.pageInfo.currentPage = val;
      this.showSubData()
    },
  }
}
</script>

<style scoped>

</style>
