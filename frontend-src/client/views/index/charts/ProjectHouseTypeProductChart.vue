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

              <el-form-item prop="orgId">
                <el-select
                  v-model="formInline.orgId"
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

              <el-form-item prop="projectName">
                <el-select
                  v-model="formInline.projectId"
                  placeholder="项目名称"
                  clearable
                  @change="changeHouseType"
                >
                  <el-option
                    v-for="item in this.arrProject"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </el-form-item>

              <el-form-item>
                <el-select
                  v-model="formInline.houseTypeId"
                  clearable
                  placeholder="户型名称"
                >
                  <el-option
                    v-for="item in houseTypeList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>

              <!-- <el-form-item label="户型名称">
                <el-input v-model="formInline.houseTypeName" clearable placeholder="户型名称"></el-input>
            </el-form-item> -->

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
        <el-table
          :data="datas"
          :span-method="objectSpanMethod"
          size="mini"
          stripe
          border
          row-key="id"
          ref="handleClickRowSelection"
          height="100%"
          @row-click="handleClickRowSelection"
          style="width: 100%"
        >
          <el-table-column
            prop="index"
            label="序号"
            width="70"
          >
          </el-table-column>

          <el-table-column
            prop="orgName"
            label="基地名称"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="projectName"
            label="项目名称"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="productName"
            label="构件名称"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="houseTypeName"
            label="户型"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="houseTypeDevNum"
            label="需求量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="houseTypeSendNum"
            label="发货量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="devNum"
            label="需求总量"
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
            prop="underproductionNum"
            label="生产欠量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="sendTotalNum"
            label="发货总量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="stockNum"
            label="库存量"
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
            prop="productVol"
            label="构件体积"
            width="120"
          >
          </el-table-column>

          <el-table-column
            prop="totalProductVol"
            label="合计发货体积"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="productConcrete"
            label="砼方量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="totalProductConcrete"
            label="合计完成砼方量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="keepWarm"
            label="保温方量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="totalKeepWarm"
            label="合计完成保温方量"
            width="120"
          >
          </el-table-column>
        </el-table>

        <el-table
          :data="exportdatas"
          :span-method="objectSpanMethod"
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
            prop="index"
            label="序号"
            width="70"
          >
          </el-table-column>
          <el-table-column
            prop="orgName"
            label="基地名称"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="projectName"
            label="项目名称"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="productName"
            label="构件名称"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="houseTypeName"
            label="户型"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="houseTypeDevNum"
            label="需求量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="houseTypeSendNum"
            label="发货量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="devNum"
            label="需求总量"
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
            prop="underproductionNum"
            label="生产欠量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="sendTotalNum"
            label="发货总量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="stockNum"
            label="库存量"
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
            prop="productVol"
            label="构件体积"
            width="120"
          >
          </el-table-column>

          <el-table-column
            prop="totalProductVol"
            label="合计发货体积"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="productConcrete"
            label="砼方量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="totalProductConcrete"
            label="合计完成砼方量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="keepWarm"
            label="保温方量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="totalKeepWarm"
            label="合计完成保温方量"
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
import houseTypeModel from '../../../model/project/houseType'
import serviceConfig from '../../../../server.config.js'
import orgModel from '../../../model/system/org'
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
      orgs: [],
      exportdatas: [],
      formInline: {
        projectId: '',
        productName: '',
        houseTypeName: '',
        houseTypeId: '',
        orgId: '',
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        }
      },

      arrProject: [],//select下拉框（project）
      houseTypeList: [],
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
    orgModel.orgAllByOrgIds().then((data) => {
      this.orgs = data.entity
    })
    this.list()
    this.getProjects()
  },
  methods: {
    changeHouseType(id) {
      //防止清楚库区时报错
      if (id == '' || id == undefined) {
        id = 0
      }
      houseTypeModel.getHouseTypeByProjectIdOpt(id).then(data => {
        this.houseTypeList = data.entity
        this.formInline.houseTypeId = ''
      })
    },
    /**
     * 合并单元格
     */
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {

      let houseTypeCount = row.houseTypeCountNum;
      let houseTypeIndex = row.houseTypeIndex;
      if (columnIndex <= 3 || columnIndex >= 7) {//第一列合并
        if (houseTypeIndex === 1) {
          return {
            rowspan: houseTypeCount,
            colspan: 1
          };
        } else {
          return {
            rowspan: 0,
            colspan: 0
          };
        }
      }

    },
    /**
     * 增加修改product为项目下拉框赋值
     */
    getProjects() {
      projectModel.findAllProjects().then((data) => {
        let projects = data.entity
        for (var j = 0; j < projects.length; j++) {
          var pName = {
            value: projects[j].id,
            label: projects[j].projectName,
            molds: []
          }
          this.arrProject.push(pName)
        }
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
      // console.log(page)
      // console.log(size)
      this.formInline.pageInfo.currentPage = 1;
      this.formInline.pageInfo.pageSize = this.pageInfo.total;
      //TODO 分页
      model.getProductListInfoSearch(this.formInline).then(data => {
        this.exportdatas = data.entity.list;
        setTimeout(() => {
          var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
          excelName = "项目构件数据报表" + excelName;
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
      model.getProductListInfoSearch(this.formInline).then(data => {
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