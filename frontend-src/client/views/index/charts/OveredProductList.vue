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
                <el-date-picker
                  v-model="formInline.day"
                  type="date"
                  placeholder="完成日期(日)"
                >
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-date-picker
                  v-model="formInline.month"
                  type="month"
                  placeholder="完成日期(月)"
                >
                </el-date-picker>
              </el-form-item>

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

              <!-- <el-form-item label="构件编码">
                <el-input v-model="formInline.productNo" placeholder="构件编码"></el-input>
            </el-form-item>
            <el-form-item label="构件名">
                <el-input v-model="formInline.productName" placeholder="构件名"></el-input>
            </el-form-item>
             -->

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
          row-key="id"
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
          <!--            <el-table-column-->
          <!--                    prop="detailNo"-->
          <!--                    label="构件明细编号"-->
          <!--                    width="160">-->
          <!--            </el-table-column>-->
          <el-table-column
            prop="product.productName"
            label="构件名称"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="product.productNo"
            label="构件编码"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="orgName"
            label="基地"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="project.projectName"
            label="项目"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="productStatus"
            label="构件状态"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="floorRelevance.building.buildCode"
            label="楼栋"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="floorRelevance.floor.floorNum"
            label="楼层"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="overTime"
            :formatter="formatterDate"
            label="完成时间"
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
import model from '../../../model/model'
import { Notification } from 'element-ui'
import serviceConfig from '../../../../server.config.js'
import orgModel from '../../../model/system/org'
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
      orgs: [],
      datas: [],
      formInline: {
        day: null,
        month: null,
        orgId: '',
      },
      disabled1: false,
      disabled2: false,
      buttonData: [
        {
          name: '导出',
          event: 'exportExcel'
        },
      ],
      searchButtonData: [
        {
          name: '查询',
          event: 'list'
        },
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
  },
  methods: {
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    list() {
      this.$store.commit('startLoading')
      this.formInline.pageInfo = this.pageInfo;
      model.listForSearch('overProductList', this.formInline).then(data => {
        this.datas = data.entity.content;
        this.pageInfo.total = data.entity.totalElements;
        this.$store.commit('endLoading')
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
    exportExcel() {
      var url = serviceConfig.baseURL + '/overedProductListExport'
      var prams = {

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
  }
}
</script>

<style scoped>
</style>