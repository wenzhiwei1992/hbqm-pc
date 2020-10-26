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
              <!-- <el-form-item label="产线名称">
                <el-input v-model="formSelect.lineName" placeholder="产线名称"></el-input>
            </el-form-item> -->
            </el-form>
          </el-col>
          <el-col>
            <my-search-button-group :model="searchButtonData"></my-search-button-group>
          </el-col>
        </el-row>
        <el-row style="display:none;">
          <my-button-group :model="buttonData"></my-button-group>
        </el-row>
        <el-row style="display:flex;justify-content: flex-end;">
          <el-col>
            <b>构件总数: {{allPcNumTotal}}</b>
          </el-col>
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
          @expand-change="showSubData"
          row-key="id"
          style="width: 100%"
          id="exportXlsx"
          tooltip-effect="dark"
          height="100%"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
        >

          <!--这是点击小箭头出现的信息-->
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-table :data="subDatas">
                <el-table-column
                  prop="product.productName"
                  label="构件名"
                  width="140"
                >
                </el-table-column>
                <el-table-column
                  sortable
                  prop="id"
                  label="构件明细ID"
                  width="140"
                >
                </el-table-column>
                <el-table-column
                  prop="productStatus"
                  label="状态"
                  width="140"
                >
                </el-table-column>
                <el-table-column
                  prop="floorRelevance.building.buildCode"
                  label="楼栋"
                  width="140"
                >
                </el-table-column>
                <el-table-column
                  prop="floorRelevance.floor.floorNum"
                  label="楼层"
                  width="140"
                >
                </el-table-column>

                <el-table-column
                  label="操作"
                  width="160"
                  align="left"
                >
                  <template slot-scope="scope">
                    <el-tooltip
                      v-if="scope.row.productStatus == '待隐检'"
                      class="item"
                      effect="light"
                      content="仅楼栋楼层排产可撤销"
                      placement="top"
                    >
                      <el-button
                        type="danger"
                        size="mini"
                        icon="el-icon-circle-close"
                        @click="cancelPlatePlan(scope.row)"
                      >撤销</el-button>
                    </el-tooltip>

                  </template>
                </el-table-column>
              </el-table>
            </template>
          </el-table-column>
          <!--           <el-table-column-->
          <!--                    label="操作"-->
          <!--                    align="center"-->
          <!--                    width="120"-->
          <!--                    >-->
          <!--                <template slot-scope="scope">-->
          <!--                    <el-button type="primary" size="mini" @click="sendInfo(scope.row)">模拟信号下发</el-button>-->
          <!--                </template>-->
          <!--            </el-table-column>-->
          <el-table-column
            prop="plateNo"
            label="模台号"
          >
          </el-table-column>
          <el-table-column
            prop="pcNumTotal"
            label="构件数"
            width="100"
          >
          </el-table-column>
          <el-table-column
            prop="productDetailTow"
            label="模台上构件"
            width="300"
            :show-overflow-tooltip="true"
          >
          </el-table-column>
          <el-table-column
            prop="predictDate"
            :formatter="formatterDate"
            label="生产日期"
            width="140"
          >
          </el-table-column>
          <el-table-column
            prop="lineName"
            label="产线"
          >
          </el-table-column>
          <el-table-column
            prop="status"
            label="状态"
          >
          </el-table-column>
          <!-- <el-table-column
                    prop="teamName"
                    label="班组"
                    width="120">
            </el-table-column> -->
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

    <el-dialog
      :visible.sync="analogSignalFormVisible"
      width="60%"
    >
      <el-form
        :model="analogSignal"
        v-if="analogSignal"
      >
        <el-form-item
          label="当前工序"
          labelWidth="160px"
        >
          <el-select
            v-model="analogSignal.asStatus"
            placeholder="当前工序"
            collapse-tags
          >
            <el-option
              v-for="item in asStatuses"
              :key="item.id"
              :label="item.backlogName"
              :value="item.backlogName"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="analogSignalFormVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="analogSignalSubmit"
        >提交</el-button>
      </div>
    </el-dialog>

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
      allPcNumTotal: 0,
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
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
    analogSignalSubmit() {
      platePlanModel.analogSignalSubmit(this.analogSignal).then(data => {
        if (data.status === 'success') {
          Notification({
            message: '模拟下发成功！',
            type: 'success'
          })
          this.analogSignalFormVisible = false
          this.pageableList();
        } else {
          Notification({
            message: '模拟下发失败:' + data.msg,
            type: 'warning'
          })
        }
      })
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
    showSubData(row, expandedRows) {
      console.log("row", row)
      let id = row.id;
      if (this.expands.some((e) => e == row.id)) {
        this.expands = []
      } else {
        this.expands = []
        this.expands.push(row.id)
      }
      this.currentSelectedId = row.id
      platePlanModel.getProductsById(id).then(data => {
        this.subDatas = data.entity;
      })
    },
    pageableList() {
      this.$store.commit('startLoading');
      pathModel.getAllProdLines().then(data => {
        this.prodlines = data.entity;
      }).catch((err) => {
        console.log("load data err!", err)
      });

      this.formSelect.pageInfo = this.pageInfo;
      this.allPcNumTotal = 0;
      platePlanModel.pageableList(this.formSelect).then(data => {
        this.datas = data.entity.content;
        this.pageInfo.total = data.entity.totalElements;

        for (let i = 0; i < this.datas.length; i++) {
          this.allPcNumTotal += this.datas[i].pcNumTotal;
        }

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
    cancelPlatePlan(row) {
      console.log("row", row);
      this.$confirm('此操作将取消该构件明细计划, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        platePlanModel.cancelPlatePlan(row.id, this.currentSelectedId).then(data => {
          let status = data.status;
          if (status == 'success') {
            Notification({
              type: 'success',
              message: '撤销成功'
            })
            this.pageableList();
            this.expands = [];
          } else {
            Notification({
              type: 'error',
              message: '撤销失败：' + data.msg
            })
            this.pageableList();
            this.expands = []
          }
        })
      })

    }

  }


}

</script>
<style>
</style>