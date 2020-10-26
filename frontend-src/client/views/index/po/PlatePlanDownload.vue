<template>

  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form
              :inline="true"
              :model="formSelect"
              class="form-inline"
            >
              <!-- <el-form-item label="产线名称">
                <el-input v-model="formSelect.lineName" placeholder="产线名称"></el-input>
            </el-form-item> -->
              <el-form-item>
                <el-select
                  v-model="formSelect.lineId"
                  placeholder="产线"
                  collapse-tags
                  clearable
                  @change="onSubmit"
                >
                  <el-option
                    v-for="pd in prodlines"
                    :value="pd.id"
                    :label="pd.lineName"
                    :key="pd.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="formSelect.plateNo"
                  placeholder="模台号"
                ></el-input>
              </el-form-item>
              <!-- <el-form-item label="班组名称">
                <el-input v-model="formSelect.teamName" placeholder="班组名称"></el-input>
            </el-form-item> -->
              <el-form-item>
                <el-input
                  v-model="formSelect.productDetailTow"
                  placeholder="构件名称（包含即可）"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-date-picker
                  v-model="formSelect.predictDateStart"
                  type="date"
                  placeholder="选择开始日期"
                >
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-date-picker
                  v-model="formSelect.predictDateEnd"
                  type="date"
                  placeholder="选择结束日期"
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
          :expand-row-keys="expands"
          row-key="id"
          style="width: 100%"
          id="exportXlsx"
          height="100%"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
        >
          <!--          @expand-change="showSubData"-->
          <el-table-column
            prop="plateNo"
            label="模台号"
          >
          </el-table-column>
          <el-table-column
            prop="pcNumTotal"
            label="构件数"
          >
          </el-table-column>
          <el-table-column
            prop="productDetailTow"
            label="模台上构件"
            width="500"
            :show-overflow-tooltip="true"
          >
          </el-table-column>
          <el-table-column
            prop="predictDate"
            :formatter="formatterDate"
            label="生产日期"
          >
          </el-table-column>
          <el-table-column
            prop="lineName"
            label="产线"
          >
          </el-table-column>
          <el-table-column
            prop="createdByName"
            label="编制人"
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
import combModel from '../../../model/po/comb'
import platePlanModel from '../../../model/po/platePlan.js'
import pathModel from '../../../model/prodline/path'
import { Notification } from 'element-ui'
import moment from "moment";
import xlsx from 'xlsx';
import fileSaver from 'file-saver';

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
      expands: [],
      selected: [],
      subDatas: [],
      formSelect: {
        plateNo: '',
        lineName: '',
        lineId: '',
        teamName: '',
        productDetailTow: ''
      },
      prodlines: [],
      currentSelectedId: '',
      analogSignal: {},
      analogSignalFormVisible: false,
      asStatuses: [],
      buttonData: [
        {
          name: '下载',
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
      ]
    }
  },
  mounted() {
    this.pageableList();
  },
  methods: {
    exportExcel() {
      /* generate workbook object from table */
      let table = document.querySelector('#exportXlsx');
      var wb = xlsx.utils.table_to_book(table);
      /* get binary string as output */
      var wbout = xlsx.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' })
      try {
        fileSaver.saveAs(new Blob([wbout], { type: 'application/octet-stream' }), 'export.xlsx')
      } catch (e) {
        if (typeof console !== 'undefined') console.log(e, wbout)
      }
      return wbout;
    },
    sendInfo(row) {
      //修改模台当前工序和状态
      this.analogSignal = {
        id: row.id,
        asStatus: ''
      }
      combModel.getAsStatuses(row.lineId).then(data => {
        this.asStatuses = data.entity;
        this.analogSignalFormVisible = true
      })

    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD");
    },
    onSubmit() {
      this.pageableList();
    },

    pageableList() {
      this.$store.commit('startLoading');
      pathModel.getAllProdLines().then(data => {
        this.prodlines = data.entity;
      }).catch((err) => {
        console.log("load data err!", err)
      });

      this.formSelect.pageInfo = this.pageInfo;
      platePlanModel.pageableList(this.formSelect).then(data => {
        this.datas = data.entity.content;
        this.pageInfo.total = data.entity.totalElements;
        this.$store.commit('endLoading');
      }).catch(err => {
        Notification({
          message: err.msg,
          type: 'warning'
        })
      })
    },
    handleSizeChange(val) {
      this.pageInfo.pageSize = val
      this.pageableList()
    },
    handleCurrentChange(val) {
      this.pageInfo.currentPage = val
      this.pageableList()
    },
  }


}

</script>
<style>
</style>