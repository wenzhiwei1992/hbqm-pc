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
              <el-row>

                <el-form-item>
                  <el-input
                    v-model="formSelect.productNo"
                    placeholder="构件编码"
                    clearable
                    style="width: 135px"
                  ></el-input>
                </el-form-item>

                <el-form-item>
                  <el-input
                    v-model="formSelect.productName"
                    placeholder="构件名称"
                    clearable
                    style="width: 135px"
                  ></el-input>
                </el-form-item>
                <!--                <el-form-item label="开始日期">-->
                <!--                    <el-date-picker-->
                <!--                            v-model="formSelect.predictStartDate"-->
                <!--                            align="right"-->
                <!--                            type="date"-->
                <!--                            style="width: 135px"-->
                <!--                            placeholder="选择日期"-->
                <!--                            :picker-options="pickerOptions1">-->
                <!--                    </el-date-picker>-->
                <!--                </el-form-item>-->
                <!--                <el-form-item label="结束日期">-->
                <!--                    <el-date-picker-->
                <!--                            v-model="formSelect.predictEndDate"-->
                <!--                            align="right"-->
                <!--                            type="date"-->
                <!--                            style="width: 135px"-->
                <!--                            placeholder="选择日期"-->
                <!--                            :picker-options="pickerOptions1">-->
                <!--                    </el-date-picker>-->
                <!--                </el-form-item>-->
                <!-- <el-form-item> -->
                <!--                    <el-button type="success" @click="exportExcel()">导出</el-button>-->
                <!-- </el-form-item> -->
              </el-row>

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
          @selection-change="handleSelectionChange"
          height="100%"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
          stripe
          border
          style="width: 100%"
          id="exportXlsx"
        >
          <el-table-column
            type="selection"
            width="60"
          >
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            width="70"
          >
          </el-table-column>
          <el-table-column
            prop="product.productNo"
            label="构件编码"
            width="140"
            sortable
          >
          </el-table-column>
          <el-table-column
            prop="product.productName"
            label="构件名称"
            width="140"
          >
          </el-table-column>
          <el-table-column
            prop="productStatus"
            label="状态"
            width="80"
            sortable
          >
          </el-table-column>
          <el-table-column
            prop="product.productHeight"
            label="宽度"
            width="60"
          >
          </el-table-column>
          <el-table-column
            prop="product.productVol"
            label="体积"
            width="60"
          >
          </el-table-column>
          <el-table-column
            prop="product.productWt"
            label="重量"
            width="60"
          >
          </el-table-column>
          <el-table-column
            prop="product.productAcreage"
            label="面积"
            width="60"
          >
          </el-table-column>
          <el-table-column
            prop="product.projectConcrete"
            label="砼方量"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="product.productGrade"
            label="砼等级"
            width="80"
          >
          </el-table-column>
          <!--            <el-table-column-->
          <!--                    prop="product.predictDate"-->
          <!--                    :formatter="formatterDate"-->
          <!--                    label="预生产日期"-->
          <!--                    width="140">-->
          <!--            </el-table-column>-->
          <el-table-column
            prop="product.moldName"
            label="模具名称"
          >
          </el-table-column>
          <el-table-column
            prop="floorRelevance.buildCode"
            label="楼栋"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="floorRelevance.floorNum"
            label="楼层"
            width="80"
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
import platePlanModel from '../../../model/po/platePlan.js'
import { Notification } from 'element-ui'
import moment from "moment";
import fileSaver from 'file-saver';
import xlsx from 'xlsx';

export default {
  data() {
    return {
      pickerOptions1: {
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }]
      },
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: this.GLOBAL.pageSize,
        pageSizes: this.GLOBAL.pageSizes
      },
      datas: [],
      selected: [],
      formSelect: {
        plateNo: '',
        buildCode: '',
        floorNum: '',
        productStatus: '',
        predictDate: '',
        productName: '',
        detailNo: '',
      },
      checkPrintVisible: false,
      buttonData: [
        {
          name: '入库',
          event: 'updateStatusRK'
        },
        {
          name: '发货',
          event: 'updateStatusFH'
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
    updateStatusRK() {
      let status = '';
      if (this.selected.length == 0) {
        this.$message('请选择');
        return;
      }
      let ids = [];
      this.selected.forEach((row) => {
        ids.push(row.id)
      });
      this.$confirm('确认操作?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.commit('startLoading');
        status = this.GLOBAL.PRODUCT_DETAIL_STATUS.CPRK
        if (status == '') {
          return;
        }
        platePlanModel.updateAllProductDetailInfoStaus(ids, status).then((data) => {
          if (data.status == 'success') {
            Notification({
              type: 'success',
              message: '操作成功'
            })
          }
          this.$store.commit('endLoading');
          this.pageableList();
        })

      }).catch((err) => {
        console.error("err", err)
      })
    },
    updateStatusFH() {
      let status = '';
      if (this.selected.length == 0) {
        this.$message('请选择');
        return;
      }
      let ids = [];
      this.selected.forEach((row) => {
        ids.push(row.id)
      });
      this.$confirm('确认操作?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.commit('startLoading');
        status = this.GLOBAL.PRODUCT_DETAIL_STATUS.YFH;
        if (status == '') {
          return;
        }
        platePlanModel.updateAllProductDetailInfoStaus(ids, status).then((data) => {
          if (data.status == 'success') {
            Notification({
              type: 'success',
              message: '操作成功'
            })
          }
          this.$store.commit('endLoading');
          this.pageableList();
        })

      }).catch((err) => {
        console.error("err", err)
      })
    },
    exportExcel() {
      /* generate workbook object from table */
      var wb = xlsx.utils.table_to_book(document.querySelector('#exportXlsx'))
      /* get binary string as output */
      var wbout = xlsx.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' })
      try {
        fileSaver.saveAs(new Blob([wbout], { type: 'application/octet-stream' }), 'export.xlsx')
      } catch (e) { if (typeof console !== 'undefined') console.log(e, wbout) }
      return wbout
    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    onSubmit() {
      this.pageableList();
    },
    pageableList() {
      this.$store.commit('startLoading');
      this.formSelect.pageInfo = this.pageInfo;
      platePlanModel.getProductDetailInfoForWxRk(this.formSelect).then(data => {
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
    handleSelectionChange(selected) {
      this.selected = selected
    },


  }


}

</script>
<style scoped>
.active {
  background: #409eff;
  border: #409eff;
}
</style>