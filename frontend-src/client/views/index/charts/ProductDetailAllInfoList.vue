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
                <el-input
                  v-model="formSelect.detailNo"
                  placeholder="明细编码"
                  clearable
                  style="width: 135px"
                ></el-input>
              </el-form-item>
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
                  style="width: 170px"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-select
                  v-model="formSelect.productStatus"
                  placeholder="构件状态"
                  clearable
                  style="width: 135px"
                  collapse-tags
                >
                  <el-option
                    label="待隐检"
                    value="待隐检"
                  ></el-option>
                  <el-option
                    label="隐检返修"
                    value="隐检返修"
                  ></el-option>
                  <el-option
                    label="待成品检"
                    value="待成品检"
                  ></el-option>
                  <el-option
                    label="成品返修"
                    value="成品返修"
                  ></el-option>
                  <el-option
                    label="报废"
                    value="报废"
                  ></el-option>
                  <el-option
                    label="待入库"
                    value="待入库"
                  ></el-option>
                  <el-option
                    label="成品入库"
                    value="成品入库"
                  ></el-option>
                  <el-option
                    label="发货待确认"
                    value="发货待确认"
                  ></el-option>
                  <el-option
                    label="已发货"
                    value="已发货"
                  ></el-option>
                </el-select>
                <!--                    <el-input v-model="formSelect.productStatus" placeholder="构件状态" clearable style="width: 135px"></el-input>-->
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="formSelect.plateNo"
                  placeholder="模台号"
                  clearable
                  style="width: 135px"
                ></el-input>
              </el-form-item>

              <el-form-item>
                <el-date-picker
                  v-model="formSelect.predictStartDate"
                  align="right"
                  type="date"
                  style="width: 135px"
                  placeholder="开始日期"
                  :picker-options="pickerOptions1"
                >
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-date-picker
                  v-model="formSelect.predictEndDate"
                  align="right"
                  type="date"
                  style="width: 135px"
                  placeholder="结束日期"
                  :picker-options="pickerOptions1"
                >
                </el-date-picker>
              </el-form-item>
              <el-form-item prop="projectName">
                <el-select
                  style="width: 170px"
                  v-model="formSelect.projectId"
                  placeholder="项目名称"
                  clearable
                  @change="changeProjectSelect"
                >
                  <el-option
                    v-for="item in arrProject"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="buildId">
                <el-select
                  placeholder="楼栋"
                  style="width:135px"
                  v-model="formSelect.buildId"
                  size="mini"
                  clearable
                  @change="changeBuildingSelect"
                >
                  <el-option
                    v-for="item in buildings"
                    :key="item.id"
                    :label="item.buildCode"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="floorId">
                <el-select
                  placeholder="楼层"
                  style="width:135px"
                  v-model="formSelect.floorId"
                  clearable
                  size="mini"
                >
                  <el-option
                    v-for="item in floors"
                    :key="item.id"
                    :label="item.floorNum"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <!--                <el-form-item label="构件明细编码">-->
              <!--                    <el-input v-model="formSelect.detailNo" clearable placeholder="构件明细编码" style="width: 135px"></el-input>-->
              <!--                </el-form-item>-->
              <el-form-item>
                <el-input
                  v-model="formSelect.orderNo"
                  clearable
                  placeholder="周计划单号"
                  style="width: 135px"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-input
                        v-model="formSelect.woNo"
                        clearable
                        placeholder="日计划单号"
                        style="width: 135px"
                ></el-input>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col>
            <my-search-button-group :model='searchButtonData'></my-search-button-group>
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
          @selection-change="handleSelectionChange"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
          height="100%"
          style="width: 100%"
        >
          <el-table-column
            type="selection"
            width="40"
          >
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            width="70"
          >
          </el-table-column>
          <el-table-column
                prop="orderNo"
                label="周计划订单号"
                width="100"
                sortable
        >
        </el-table-column>
          <el-table-column
                  prop="woNo"
                  label="日计划订单号"
                  width="100"
                  sortable
          >
          </el-table-column>
          <el-table-column
            prop="projectName"
            label="项目"
            width="100"
            sortable
          >
          </el-table-column>
          <el-table-column
            prop="typeName"
            label="构件类型"
            width="100"
            sortable
          >
          </el-table-column>
          <el-table-column
            prop="productName"
            label="构件名称"
            width="140"
          >
          </el-table-column>
          <el-table-column
            prop="productNo"
            label="构件编码"
            width="140"
            sortable
          >
          </el-table-column>
          <el-table-column
            prop="detailNo"
            label="明细编码"
            sortable
            width="130"
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
            prop="product_height"
            label="宽度"
            width="60"
          >
          </el-table-column>
          <el-table-column
            prop="product_vol"
            label="体积"
            width="60"
          >
          </el-table-column>
          <el-table-column
            prop="product_wt"
            label="重量"
            width="60"
          >
          </el-table-column>
          <el-table-column
            prop="product_acreage"
            label="面积"
            width="60"
          >
          </el-table-column>
          <el-table-column
            prop="project_concrete"
            label="砼方量"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="product_grade"
            label="砼等级"
            width="80"
          >
          </el-table-column>

          <el-table-column
            prop="buildCode"
            label="楼栋"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="floorNum"
            label="楼层"
            width="80"
          >
          </el-table-column>
          <el-table-column
                  prop="stockno"
                  label="库区"
                  width="80"
          >
          </el-table-column>
          <el-table-column
                  prop="shelfno"
                  label="货架"
                  width="80"
          ></el-table-column>
          <el-table-column
                  prop="datecread"
                  :formatter="formatterDate"
                  label="创建日期"
                  width="140"
          >
          </el-table-column>
          <el-table-column
                  prop="planprodtime"
                  :formatter="formatterDate"
                  label="生产日期"
                  width="140"
          >
          </el-table-column>
          <el-table-column
                  prop="instocktime"
                  :formatter="formatterDate"
                  label="入库日期"
                  width="140"
          >
          </el-table-column>
          <el-table-column
                  prop="sendtime"
                  :formatter="formatterDate"
                  label="发货日期"
                  width="140"
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
          <el-table-column
                  type="index"
                  label="序号"
                  width="70"
          >
          </el-table-column>
          <el-table-column
                  prop="orderNo"
                  label="周计划订单号"
                  width="100"
                  sortable
          >
          </el-table-column>
          <el-table-column
                  prop="woNo"
                  label="日计划订单号"
                  width="100"
                  sortable
          >
          </el-table-column>
          <el-table-column
                  prop="projectName"
                  label="项目"
                  width="100"
                  sortable
          >
          </el-table-column>
          <el-table-column
                  prop="typeName"
                  label="构件类型"
                  width="100"
                  sortable
          >
          </el-table-column>
          <el-table-column
                  prop="productName"
                  label="构件名称"
                  width="140"
          >
          </el-table-column>
          <el-table-column
                  prop="productNo"
                  label="构件编码"
                  width="140"
                  sortable
          >
          </el-table-column>
          <el-table-column
                  prop="detailNo"
                  label="明细编码"
                  sortable
                  width="130"
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
                  prop="product_height"
                  label="宽度"
                  width="60"
          >
          </el-table-column>
          <el-table-column
                  prop="product_vol"
                  label="体积"
                  width="60"
          >
          </el-table-column>
          <el-table-column
                  prop="product_wt"
                  label="重量"
                  width="60"
          >
          </el-table-column>
          <el-table-column
                  prop="product_acreage"
                  label="面积"
                  width="60"
          >
          </el-table-column>
          <el-table-column
                  prop="project_concrete"
                  label="砼方量"
                  width="80"
          >
          </el-table-column>
          <el-table-column
                  prop="product_grade"
                  label="砼等级"
                  width="80"
          >
          </el-table-column>

          <el-table-column
                  prop="buildCode"
                  label="楼栋"
                  width="80"
          >
          </el-table-column>
          <el-table-column
                  prop="floorNum"
                  label="楼层"
                  width="80"
          >
          </el-table-column>
          <el-table-column
                  prop="stockno"
                  label="库区"
                  width="80"
          >
          </el-table-column>
          <el-table-column
                  prop="shelfno"
                  label="货架"
                  width="80"
          >
          </el-table-column>
          <el-table-column
                  prop="datecread"
                  label="创建日期"
                  width="140"
          >
          </el-table-column>
          <el-table-column
                  prop="planprodtime"
                  label="生产日期"
                  width="140"
          >
          </el-table-column>
          <el-table-column
                  prop="instocktime"
                  label="入库日期"
                  width="140"
          >
          </el-table-column>
          <el-table-column
                  prop="sendtime"
                  label="发货日期"
                  width="140"
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
import productDetailModel from '../../../model/project/productDetail'
import buildModel from '../../../model/project/building'
import floorModel from '../../../model/project/floor'
import projectModel from '../../../model/project/project'
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
      expands: [],
      selected: [],
      exportdatas: [],
      formSelect: {
        plateNo: '',
        buildCode: '',
        floorNum: '',
        productStatus: '',
        predictDate: '',
        productName: '',
        detailNo: '',
        floorId: '',
        buildId: '',
        orderNo: '',
        id: '',
      },
      productDetailInfo: null,
      buildings: [],
      floors: [],
      arrProject: [],//select下拉框（project）
      projectId: [],
      buttonData: [
        {
          name: '导出',
          event: 'exportExcel'
        },
      ],
      searchButtonData: [
        {
          name: '查询',
          event: 'onSubmit'
        }
      ],
    }
  },
  mounted() {
    this.pageableList();
    this.info()
  },
  methods: {

    exportExcel() {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      let page = this.pageInfo.currentPage;
      let size = this.pageInfo.pageSize;

      this.formSelect.pageInfo = this.pageInfo;
      this.formSelect.pageInfo.currentPage = 1;
      this.formSelect.pageInfo.pageSize = this.pageInfo.total;

      platePlanModel.getProductDetailInfoListS(this.formSelect).then(data => {
        this.exportdatas = data.entity.content;
        setTimeout(() => {
          var excelName = "export.xlsx";
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

          this.formSelect.pageInfo.pageSize = size;
          this.formSelect.pageInfo.currentPage = page;
          loading.close();
          return wbout;
        }, 500);

      }).catch(err => {

      })


    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (!date || date=="　") {
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
      platePlanModel.getProductDetailAllInfoList(this.formSelect).then(data => {
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

    info() {

      projectModel.findAllProjects().then((data) => {
        let projects = data.entity
        for (var j = 0; j < projects.length; j++) {
          var pName = {
            value: projects[j].id,
            label: projects[j].projectName,
          }
          this.arrProject.push(pName)
        }
      })


    },
    changeProjectSelect(id) {
      if (id == undefined) {
        id = '0'
      }
      this.formSelect.buildId = ''
      this.buildings = []
      this.projectId = id
      console.log('id------>', id)
      buildModel.findByProjectId(id).then((data) => {
        console.log('data------>', data.entity)
        this.buildings = data.entity
      })
    },
    changeBuildingSelect(id) {
      if (id == undefined) {
        id = '0'
      }
      this.formSelect.floorId = ''
      this.floors = []
      // let building = this.buildings.find(b => b.id == id)
      floorModel.findByBuildingIdAndProjectId(id, this.projectId).then((data) => {
        this.floors = data.entity
        //this.formSelect.floorId = ''
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
    }
  }


}

</script>
<style scoped>
.active {
  background: #409eff;
  border: #409eff;
}
</style>