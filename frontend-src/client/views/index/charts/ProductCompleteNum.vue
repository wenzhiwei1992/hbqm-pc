<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form
              :inline="true"
              :model="productCompleteQuery"
              ref="productCompleteQuery"
              class="demo-form-inline"
              size="mini"
            >
              <el-form-item prop="day">
                <el-date-picker
                  v-model="productCompleteQuery.productCompleteDate"
                  type="date"
                  style="width:150px"
                  placeholder="完成日期(日)"
                >
                </el-date-picker>
              </el-form-item>
              <el-form-item prop="projectId">
                <el-select
                  clearable
                  v-model="productCompleteQuery.projectId"
                  style="width: 150px"
                  placeholder="项目名称"
                >
                  <el-option
                    v-for="item in projects"
                    :key="item.id"
                    :label="item.projectName"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="orgId">
                <el-select
                  v-model="productCompleteQuery.orgId"
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
        :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <el-table
          :data="datas"
          size="mini"
          stripe
          border
          :span-method="objectSpanMethod"
          row-key="id"
          ref="handleClickRowSelection"
          height="100%"
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
            prop="orgName"
            label="基地"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="projectName"
            label="项目"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="plateNo"
            label="模台"
          >
          </el-table-column>
          <el-table-column
            prop="buildCode"
            label="楼栋"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="productName"
            label="构件名称"
            width="100"
          >
          </el-table-column>
          <el-table-column
            label="尺寸"
            align="center"
          >
            <el-table-column
              prop="productLen"
              label="长度"
            >
            </el-table-column>
            <el-table-column
              prop="productHeight"
              label="宽度"
            >
            </el-table-column>
            <el-table-column
              prop="productThick"
              label="厚度"
            >
            </el-table-column>
          </el-table-column>
          <el-table-column
            prop="productAcreage"
            label="面积"
          >
          </el-table-column>
          <el-table-column
            prop="productVol"
            label="体积"
          >
          </el-table-column>
          <el-table-column
            prop="projectConcrete"
            label="砼方量(m³)"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="productCompleteNumDay"
            label="当日完成量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="productCompleteNumAll"
            label="总完成量"
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
            :current-page="productCompleteQuery.pageInfo.currentPage"
            :page-sizes="productCompleteQuery.pageInfo.pageSizes"
            :page-size="productCompleteQuery.pageInfo.pageSize"
            :total="productCompleteQuery.pageInfo.total"
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
import productModel from '../../../model/project/product'
import projectModel from '../../../model/project/project'
import serviceConfig from '../../../../server.config.js'
import orgModel from '../../../model/system/org'
import moment from "moment"
export default {
  name: "ProductCompleteNum",
  data() {
    return {
      datas: [],
      projects: [],
      orgs: [],
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: this.GLOBAL.pageSize,
        pageSizes: this.GLOBAL.pageSizes
      },
      productCompleteQuery: {
        productCompleteDate: new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate()),//默认当前时间
        projectId: '',
        orgId: '',
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        },
      },
      buttonData: [
        {
          name: '导出',
          event: 'exportExcel'
        }
      ],
      searchButtonData: [
        {
          name: '查询',
          event: "submitForm('productCompleteQuery')"
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
    this.loadProjects()
    this.list()
  },
  methods: {
    submitForm(formName) {
      console.log('formName', formName)
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
    list() {
      productModel.getProductCompleteNum(this.productCompleteQuery).then((data) => {
        this.datas = data.entity.list
        this.productCompleteQuery.pageInfo.total = data.entity.total;
      })
    },
    exportExcel() {
      var url = serviceConfig.baseURL + '/materStorageDownloadController/getProductCompleteNumReport'
      var prams = {}
      if (this.productCompleteQuery.productCompleteDate == undefined || this.productCompleteQuery.productCompleteDate == null) {
        this.productCompleteQuery.productCompleteDate = ''
      } else {
        prams = { "productCompleteDate": this.productCompleteQuery.productCompleteDate, "projectId": this.productCompleteQuery.projectId }
      }
      this.exportExcelPost(url, prams)
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
    loadProjects() {
      projectModel.getAllProjects().then((data) => {
        this.projects = data.entity
      })
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (rowIndex === -1) {
        if (columnIndex === 4) {
          return {
            rowspan: 2,
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
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    handleSizeChange(val) {
      this.productCompleteQuery.pageInfo.pageSize = val;
      this.list()
    },
    handleCurrentChange(val) {
      this.productCompleteQuery.pageInfo.currentPage = val;
      this.list()
    },

  }
}
</script>

<style scoped>
</style>