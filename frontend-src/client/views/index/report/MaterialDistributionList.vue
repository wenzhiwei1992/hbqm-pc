<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form :inline="true">
              <el-form-item>
                <el-date-picker
                  style="width: 250px"
                  v-model="mdQuery.date"
                  type="daterange"
                  start-placeholder="计划开始日期"
                  end-placeholder="计划结束日期"
                >
                </el-date-picker>
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
        <el-table
          :data="entities"
          ref="handleClickRowSelection"
          height="100%"
          @row-click="handleClickRowSelection"
          style="width:100%"
          header-row-class-name="header-row"
          highlight-current-row
          row-class-name="table-row"
          stripe
          border
          tooltip-effect="light"
          row-key="id"
        >
          <el-table-column
            type="index"
            label="序号"
            width="70"
          >
          </el-table-column>
          <el-table-column
            prop="materialStorageId"
            label="原料编号"
            align="center"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="materialName"
            label="原料名称"
            align="center"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="materialSpecification"
            label="原料规格"
            align="center"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="categoryName"
            label="单位"
            align="center"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="standardNum"
            label="标准领料分配合计"
            align="center"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="realNum"
            label="实际领料数量(仓库数据)"
            align="center"
            width="200"
          >
          </el-table-column>
          <el-table-column
            prop="differencesNum"
            label="差异 "
            align="center"
            width="150"
          >
          </el-table-column>
        </el-table>
        <el-table
          :data="entities"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
          height="500px"
          style="width:100%"
          id="exportXlsx"
          hidden
          stripe
          border
        >
          <el-table-column
            type="index"
            label="序号"
            width="70"
          >
          </el-table-column>
          <el-table-column
            prop="materialStorageId"
            label="原料编号"
            align="center"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="materialName"
            label="原料名称"
            align="center"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="materialSpecification"
            label="原料规格"
            align="center"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="categoryName"
            label="单位"
            align="center"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="standardNum"
            label="标准领料分配合计"
            align="center"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="realNum"
            label="实际领料数量(仓库数据)"
            align="center"
            width="200"
          >
          </el-table-column>
          <el-table-column
            prop="differencesNum"
            label="差异 "
            align="center"
            width="150"
          >
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer ref="footOperationBar">
        <el-row>
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="mdQuery.pageInfo.currentPage"
            :page-sizes="mdQuery.pageInfo.pageSizes"
            :page-size="mdQuery.pageInfo.pageSize"
            :total="mdQuery.pageInfo.total"
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
import mdModel from "../../../model/report/materialDistribution";
import productModel from "../../../model/project/product";
import xlsx from "xlsx";
import fileSaver from "file-saver";
import moment from "moment";

export default {
  name: "MaterialDistributionList",
  data() {
    return {
      mdQuery: {
        date: [new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate() - 7),
        new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate())],
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
      entities: [],
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
          event: "onclick"
        }
      ],
      rowButtonData: [
      ]
    }
  },
  mounted() {
    this.list()
  },
  methods: {
    onclick() {
      this.list()
    },
    list() {
      mdModel.findMaterialDistribution(this.mdQuery).then((data) => {
        this.entities = data.entity.list
        this.mdQuery.pageInfo.total = data.entity.totalCount
      })
    },
    exportExcel() {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      let page = this.pageInfo.currentPage;
      let size = this.pageInfo.pageSize;

      this.mdQuery.pageInfo.currentPage = 1;
      this.mdQuery.pageInfo.pageSize = this.mdQuery.pageInfo.total;

      mdModel.findMaterialDistribution(this.mdQuery).then(data => {
        this.exportdatas = data.entity.list;
        setTimeout(() => {
          var excelName = moment(this.mdQuery.date[0]).format("YYYY-MM-DD") + '至' + moment(this.mdQuery.date[1]).format("YYYY-MM-DD") + "原材料领料分配表.xlsx";
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

          this.mdQuery.pageInfo.pageSize = size;
          this.mdQuery.pageInfo.currentPage = page;
          loading.close();
          return wbout;
        }, 500);

      }).catch(err => {

      })
    },
    handleSizeChange(val) {
      this.mdQuery.pageInfo.currentPage = 1
      this.mdQuery.pageInfo.pageSize = val
      this.list()
    },
    handleCurrentChange(val) {
      this.mdQuery.pageInfo.currentPage = val
      this.list()
    },

  }

}
</script>

<style scoped>
</style>