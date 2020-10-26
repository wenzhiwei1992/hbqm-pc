<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form
              :inline="true"
              :model="formInline"
              class="demo-form-inline"
              size="mini"
            >
              <el-form-item>
                <el-select v-model="formInline.projectId" placeholder="项目名称" clearable filterable @change="changeProject"
                           style="width:110px;">
                  <el-option
                          v-for="item in projectList"
                          :key="item.id"
                          :label="item.projectName"
                          :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-select v-model="formInline.buildingId" placeholder="楼栋" clearable filterable @change="changeBuilding"
                           style="width:110px;" :disabled="formInline.projectId==''">
                  <el-option
                          v-for="item in buildings"
                          :key="item.id"
                          :label="item.buildCode"
                          :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-select v-model="formInline.floorId" placeholder="楼层" clearable filterable :disabled="formInline.buildingId==''"
                           @change="$forceUpdate()" style="width:110px;">
                  <el-option
                          v-for="item in floors"
                          :key="item.id"
                          :label="item.floorNum"
                          :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>


              <el-form-item>
                <el-input
                  placeholder="构件名称" clearable
                  v-model="formInline.productName"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-input
                  placeholder="明细编码" clearable
                  v-model="formInline.detailNo"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-select
                  placeholder="出入库类型" clearable
                  v-model="formInline.inOutType"
                  clearable
                >
                  <el-option
                    value="入库"
                    label="入库"
                  ></el-option>
                  <el-option
                    value="出库"
                    label="出库"
                  ></el-option>
                  <el-option
                    value="移库"
                    label="移库"
                  ></el-option>
                  <el-option
                    value="出库待确认"
                    label="出库待确认"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-input
                  placeholder="货架" clearable
                  v-model="formInline.shelfNo"
                ></el-input>
              </el-form-item>
              <el-form-item prop="dateRange">
                <el-date-picker
                        v-model="formInline.recordDateRange"
                        type="daterange"
                        align="right"
                        unlink-panels
                        range-separator="-"
                        start-placeholder="选择开始日期"
                        end-placeholder="选择结束日期"
                        :default-time="['00:00:00', '23:59:59']"
                        value-format="yyyy-MM-dd"
                        style="width:300px;">
                </el-date-picker>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col>
            <my-search-button-group :model="searchButtonData"></my-search-button-group>
          </el-col>
        </el-row>
        <el-row >
          <my-button-group :model="buttonData"></my-button-group>
        </el-row>
      </el-header>
      <el-main
        ref="mainContent"
        :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <el-table
          :data="datas"
          @sort-change="sortChange"
          size="mini"
          stripe
          border
          :default-sort="{prop: 'dateCreated', order: 'descending'}"
          row-key="id"
          style="width: 100%"
          @selection-change="handleSelectionChange"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
          height="100%"
        >
          <!-- 这是点击小箭头出现的信息 -->
          <el-table-column
                  type="selection"
                  width="40"
          >
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="projectName"
            label="项目名称"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="buildCode"
            label="楼栋"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="floorNum"
            label="楼层"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="productNo"
            label="构件编码"
          >
          </el-table-column>
          <el-table-column
            prop="productName"
            label="构件名称"
          >
          </el-table-column>
          <el-table-column
            prop="detailNo"
            label="明细编码"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="inOutType"
            label="出入库类型"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="stockNo"
            label="库区"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="shelfNo"
            label="货架"
          >
          </el-table-column>
          <el-table-column
            prop="createdByName"
            label="创建人"
          >
          </el-table-column>
          <el-table-column
            prop="productStatus"
            label="构件状态"
          >
          </el-table-column>
          <el-table-column
            prop="productLen"
            label="构件长度"
          >
          </el-table-column>
          <el-table-column
            prop="productHeight"
            label="构件宽度"
          >
          </el-table-column>
          <el-table-column
            prop="productThick"
            label="构件高度"
          >
          </el-table-column>

          <el-table-column
            prop="dateCreated"
            :formatter="formatterDate"
            label="操作时间"
          >
          </el-table-column>

        </el-table>
        <el-table
                :data="exportdatas"
                size="mini"
                stripe
                border
                id="exportXlsx"
                hidden
        >
        <!-- 这是点击小箭头出现的信息 -->
        <el-table-column
                sortable
                prop="projectName"
                label="项目名称"
        >
        </el-table-column>
        <el-table-column
                sortable
                prop="buildCode"
                label="楼栋"
        >
        </el-table-column>
        <el-table-column
                sortable
                prop="floorNum"
                label="楼层"
        >
        </el-table-column>
        <el-table-column
                sortable
                prop="productNo"
                label="构件编码"
        >
        </el-table-column>
        <el-table-column
                prop="productName"
                label="构件名称"
        >
        </el-table-column>
        <el-table-column
                prop="detailNo"
                label="明细编码"
        >
        </el-table-column>
        <el-table-column
                sortable
                prop="inOutType"
                label="出入库类型"
        >
        </el-table-column>
        <el-table-column
                sortable
                prop="stockNo"
                label="库区"
        >
        </el-table-column>
        <el-table-column
                sortable
                prop="shelfNo"
                label="货架"
        >
        </el-table-column>
        <el-table-column
                prop="createdByName"
                label="创建人"
        >
        </el-table-column>
        <el-table-column
                prop="productStatus"
                label="构件状态"
        >
        </el-table-column>
        <el-table-column
                prop="productLen"
                label="构件长度"
        >
        </el-table-column>
        <el-table-column
                prop="productHeight"
                label="构件宽度"
        >
        </el-table-column>
        <el-table-column
                prop="productThick"
                label="构件高度"
        >
        </el-table-column>

        <el-table-column
                prop="dateCreated"
                :formatter="formatterDate"
                label="操作时间"
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
import record from '../../../model/basicdata/record'
import { Notification } from 'element-ui'
import moment from "moment";
import fileSaver from 'file-saver';
import xlsx from 'xlsx';
import floorModel from "../../../model/project/floor";
import buildingModel from "../../../model/project/building";

export default {
  data() {
    return {
      loading: false,
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: 20,
        pageSizes: this.GLOBAL.pageSizes
      },
      datas: [],
      selected: [],
      exportdatas: [],
      formInline: {

      },
      projectList: [], // 项目集合
      buildings: [],
      floors: [],
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
  mounted() {
    record.findAllProjectList().then(data => {
      this.projectList = data.entity;
    }).catch(() => {

    })
    this.list()
  },
  methods: {
    changeProject() {
      buildingModel.findByProjectId(this.formInline.projectId).then(data => {
        this.buildings = data.entity
        this.formInline.buildingId = ''
        this.formInline.floorId = ''
        this.floors = []
      })
    },
    changeBuilding(id) {
      this.formInline.floorId = ''
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
      })
    },
    exportExcel(){
      let self = this;
      console.log("self.selected",self.selected)
      let msg = "是否导出所选数据?";
      if (self.selected.length == 0) {
        msg = "是否导出全部数据?";;
      }
      this.$confirm(msg, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (self.selected.length == 0) {
          if(this.pageInfo.total>=5000){
            Notification({
              message: "导出数据过多",
              type: 'warning'
            })
            return ;
          }
          this.exportAllExcel();
        }else{
          this.exportdatas = self.selected;
          this.exportSelectExcel();
        }

      }).catch(() => {

      })
    },
    exportSelectExcel() {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      //TODO 分页
      setTimeout(() => {
        var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
        excelName = "堆场履历" + excelName;
        /* generate workbook object from table */
        let table = document.querySelector('#exportXlsx');
        var wb = xlsx.utils.table_to_book(table);
        /* get binary string as output */
        var wbout = xlsx.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'})
        try {
          fileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), excelName)
        } catch (e) {
          if (typeof console !== 'undefined') console.log(e, wbout)
        }

        loading.close();
        return wbout;
      }, 500);
    },
    exportAllExcel() {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      let page = this.pageInfo.currentPage;
      let size = this.pageInfo.pageSize;

      this.formInline.pageInfo = this.pageInfo;
      this.formInline.pageInfo.currentPage = 1;
      this.formInline.pageInfo.pageSize = this.pageInfo.total;

      record.findAllByFormInline(this.formInline).then(data => {
        this.exportdatas = data.entity.list;
        setTimeout(() => {
          var excelName = "堆场履历.xlsx";
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
          loading.close();
          return wbout;
        }, 500);

      }).catch(err => {

      })
    },
    sortChange(column) {

      //获取字段名称和排序类型
      let fieldName = column.prop;
      let sortingType = column.order;
      this.formInline.sortField = fieldName;
      this.formInline.sortType = sortingType
      this.list();
    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    onSubmit() {
      this.list();
    },

    list() {
      this.$store.commit('startLoading')
      this.formInline.pageInfo = this.pageInfo;
      record.findAllByFormInline(this.formInline).then(data => {
        this.datas = data.entity.list;
        this.pageInfo.total = data.entity.totalCount;
        this.$store.commit('endLoading')
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
  }
}
</script>

<style scoped>
</style>