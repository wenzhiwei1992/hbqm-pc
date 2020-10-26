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


              <el-form-item prop="projectId">
                <el-select
                  v-model="formInline.projectId"
                  placeholder="项目"
                  clearable
                >
                  <el-option
                    v-for="item in this.arrProject"
                    :key="item.id"
                    :label="item.projectName"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item prop="productTypeId">
                <el-select v-model="formInline.productTypeId"
                           clearable
                           filterable
                           placeholder="构件类别"
                           style="width:150px">
                  <el-option
                          v-for="item in arrProductType"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item  prop="productName">
                <el-input
                  v-model="formInline.productName"
                  placeholder="构件名称"
                ></el-input>
              </el-form-item>

              <el-form-item  prop="productNo">
                <el-input
                        v-model="formInline.productNo"
                        placeholder="构件编码"
                ></el-input>
              </el-form-item>

              <el-form-item  prop="endTime">
                <el-date-picker
                        v-model="formInline.endTime"
                        type="date"
                        placeholder="选择日期时间"
                        value-format="yyyy-MM-dd"
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
          :data="datas"
          size="mini"
          stripe
          border
          row-key="productId"
          ref="handleClickRowSelection"
          height="100%"
          @row-click="handleClickRowSelection"
          style="width: 100%"
        >
          <el-table-column
                  type="index"
                  label="序号"
                  width="70"
          >
          </el-table-column>

          <el-table-column
                  prop="orgName"
                  label="生产基地"
          >
          </el-table-column>
          <el-table-column
                  prop="projectName"
                  label="项目"
          >
          </el-table-column>
          <el-table-column
                  prop="productTypeName"
                  label="构件类型"
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
                  prop="devNum"
                  label="需求量"
                  width="120"
          >
          </el-table-column>
          <el-table-column
                  prop="quantityNum"
                  label="完成量"
                  width="120"
          >
          </el-table-column>
          <el-table-column
                  prop="sendTotalNum"
                  label="发货量"
                  width="120"
          >
          </el-table-column>

          <el-table-column
                  prop="productVol"
                  label="体积(m³)"
                  width="120"
          >
          </el-table-column>

          <el-table-column
                  prop="productGrade"
                  label="砼等级"
                  width="120"
          >
          </el-table-column>

          <el-table-column
                  prop="productConcrete"
                  label="砼方量(m³)"
                  width="120"
          >
          </el-table-column>
        </el-table>

        <el-table
          :data="exportdatas"
          size="mini"
          stripe
          border
          row-key="productId"
          height="500"
          id="exportXlsx"
          hidden
          style="width: 100%"
        >
          <el-table-column
                  type="index"
                  label="序号"
                  width="70"
          >
          </el-table-column>

          <el-table-column
                  prop="orgName"
                  label="生产基地"
          >
          </el-table-column>
          <el-table-column
                  prop="projectName"
                  label="项目"
          >
          </el-table-column>
          <el-table-column
                  prop="productTypeName"
                  label="构件类型"
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
                  prop="devNum"
                  label="需求量"
                  width="120"
          >
          </el-table-column>
          <el-table-column
                  prop="quantityNum"
                  label="完成量"
                  width="120"
          >
          </el-table-column>
          <el-table-column
                  prop="sendTotalNum"
                  label="发货量"
                  width="120"
          >
          </el-table-column>

          <el-table-column
                  prop="productVol"
                  label="体积(m³)"
                  width="120"
          >
          </el-table-column>

          <el-table-column
                  prop="productGrade"
                  label="砼等级"
                  width="120"
          >
          </el-table-column>

          <el-table-column
                  prop="productConcrete"
                  label="砼方量(m³)"
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
import axios from 'axios'
import model from '../../../model/model'
import projectModel from '../../../model/project/project'
import productChartModel from '../../../model/charts/productChart'
import productTypeModel from '../../../model/basicdata/productType'
import serviceConfig from '../../../../server.config.js'
import { Notification } from 'element-ui'
import xlsx from 'xlsx';
import fileSaver from 'file-saver';
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
      datas: [],
      exportdatas: [],
      formInline: {
        projectId: '',
        productName: '',
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        }
      },

      arrProject: [],//select下拉框（project）
      arrProductType: [],
      buttonData: [
        {
          name: '导出',
          event: 'exportExcel'
        }
      ],
      searchButtonData: [
        {
          name: '查询',
          event: "queryList"
        }
      ],
      rowButtonData: [
      ]
    }

  },
  mounted() {
      this.list();
      this.getProjects();
      this.getProductType();
  },
  methods: {
      getProductType() {
          productTypeModel.getAllproductTypes().then((data) => {
              this.arrProductType = data.entity
          })
      },

    /**
     * 增加修改product为项目下拉框赋值
     */
    getProjects() {
      projectModel.projectListS().then((data) => {
          this.arrProject = data.entity
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
      this.formInline.pageInfo.currentPage = 1;
      this.formInline.pageInfo.pageSize = this.pageInfo.total;
      //TODO 分页
        productChartModel.getProductChartInfo(this.formInline).then(data => {
        this.exportdatas = data.entity.list;
        setTimeout(() => {
          var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
          excelName = "项目构件数据报表" + excelName;
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
    queryList() {
      this.pageInfo.currentPage = 1;
      this.list();
    },

    list() {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });

      this.formInline.pageInfo = this.pageInfo;
      //TODO 分页
        productChartModel.getProductChartInfo(this.formInline).then(data => {
                this.datas = data.entity.list;
                this.pageInfo.total = data.entity.totalCount;
        loading.close();
      }).catch(err => {

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
  }
}
</script>

<style scoped>
</style>