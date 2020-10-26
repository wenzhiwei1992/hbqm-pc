<template>
  <div>
    <el-form :model="invoicesQuery" v-if="invoicesQuery" ref="invoicesQuery" size="small"
             :inline="true">
      <!--     label-width="100px"-->
      <el-row>
        <el-row>
          <!--                  <el-form-item label="项目名称" prop="projectId">
                                <el-select v-model="invoicesQuery.projectId" clearable style="width:110px" size="mini">
                                    <el-option
                                            v-for="item in projects"
                                            :key="item.id"
                                            :label="item.projectName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>-->
          <el-form-item label="库区">
            <el-select v-model="invoicesQuery.stockId" size="mini" style="width:150px" clearable
                       @change="changeStock">
              <el-option
                  v-for="item in stocks"
                  :key="item.id"
                  :label="item.stockName"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="货架">
            <el-select v-model="invoicesQuery.shelfId" clearable style="width:150px" size="mini">
              <el-option
                  v-for="item in shelves"
                  :key="item.id"
                  :label="item.shelfName"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="构件名称" prop="productName">
            <el-input v-model="invoicesQuery.productName" clearable style="width:150px"
                      size="mini"></el-input>
          </el-form-item>
          <el-form-item label="构件编号" prop="productNo">
            <el-input v-model="invoicesQuery.productNo" clearable style="width:150px"
                      size="mini"></el-input>
          </el-form-item>

          <el-form-item label="构件类型" prop="productTypeId">
            <el-select v-model="invoicesQuery.productTypeId" style="width:150px" clearable
                       placeholder="请选择">
              <el-option
                  v-for="item in arrProductType"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <!-- <el-form-item label="楼栋" prop="buildId">
               <el-select style="width:150px" v-model="invoicesQuery.buildId" size="mini" clearable
                          @change="changeBuildingSelect">
                   <el-option
                           v-for="item in buildings"
                           :key="item.id"
                           :label="item.buildCode"
                           :value="item.id">
                   </el-option>
               </el-select>
           </el-form-item>
           <el-form-item label="楼层" prop="floorId">
               <el-select style="width:150px" v-model="invoicesQuery.floorId" clearable size="mini">
                   <el-option
                           v-for="item in floors"
                           :key="item.id"
                           :label="item.floorNum"
                           :value="item.id">
                   </el-option>
               </el-select>
           </el-form-item>-->

          <!-- <el-form-item label="发货计划号" prop="planNo">
               <el-input v-model="invoicesQuery.planNo" size="mini"></el-input>
           </el-form-item>-->
          <el-form-item>
            <el-button type="primary" size="mini" @click="onSubmit">查询</el-button>
          </el-form-item>
        </el-row>
        <el-row>

          <el-button type="success" size="mini" @click="add">添加</el-button>
        </el-row>
      </el-row>
    </el-form>
    <el-table
        :data="invoices"
        style="width:100%"
        :height="GLOBAL.tabelHeight"
        header-row-class-name="header-row"
        @row-click="clickRow"
        size="mini"
        stripe
        border
        tooltip-effect="light"
        @selection-change="handleSelectionChange"
        row-key="id"
        ref="moviesTable"
    >
      <el-table-column
          type="selection"
          width="40">
      </el-table-column>
      <el-table-column
          type="index"
          label="序号"
          width="70">
      </el-table-column>
      <el-table-column
          prop="productDetailNo"
          label="明细编码"
          align="center"
          width="100"
      ></el-table-column>
      <el-table-column
          prop="projectName"
          label="项目名称"
          align="center"
          width="100"
      ></el-table-column>
      <!--<el-table-column
              prop="buildCode"
              label="楼栋"
              align="center"
              width="150"
      >
      </el-table-column>
      <el-table-column
              prop="floorNum"
              label="楼层"
              align="center"
              width="70"
      >
      </el-table-column>-->
      <el-table-column
          prop="productNo"
          label="构件编码"
          align="center"
          width="100"
      ></el-table-column>
      <el-table-column
          prop="productName"
          label="构件名称"
          align="center"
          width="100"
      ></el-table-column>
      <el-table-column
          prop="productTypeName"
          label="构件类型"
          align="center"
          width="100"
      ></el-table-column>
      <el-table-column
          prop="productWt"
          label="构件重量(t)"
          align="center"
          width="110"
          :formatter="wtFormat"
      ></el-table-column>
      <el-table-column
          prop="productVol"
          label="构件方量(m³)"
          align="center"
          width="110"
          :formatter="wtFormat"
      ></el-table-column>
      <el-table-column
          prop="stockName"
          label="库区号"
          align="center"
          width="100">
      </el-table-column>
      <el-table-column
          prop="shelfName"
          label="货架号"
          align="center"
          width="100">
      </el-table-column>
      <!--      <el-table-column
                    prop="planNo"
                    label="发货计划号"
                    align="center"
                    width="100">
            </el-table-column>-->

    </el-table>
    <el-row type="flex" justify="space-around" align="middle">
      <el-col>

      </el-col>
      <el-col>
        <el-row type="flex" justify="end" align="middle">
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="invoicesQuery.pageInfo.currentPage"
              :page-sizes="invoicesQuery.pageInfo.pageSizes"
              :page-size="invoicesQuery.pageInfo.pageSize"
              :total="invoicesQuery.pageInfo.total"
              layout="total, sizes, prev, pager, next, jumper"
              background
          >
          </el-pagination>
        </el-row>

      </el-col>
    </el-row>
  </div>
</template>

<script>
var numeral = require('numeral')
import planModel from '../../../../../model/plan/plan'
import buildModel from '../../../../../model/project/building'
import floorModel from '../../../../../model/project/floor'
import stockModel from '../../../../../model/basicdata/stock'
import shelfModel from '../../../../../model/basicdata/shelf'
import projectModel from '../../../../../model/project/project'
import productTypeModel from '../../../../../model/basicdata/productType'
import moment from 'moment'
import {Notification} from "element-ui";

export default {
  name: "InvoiceNewList",
  props: ['newInvoiceDetails', 'invoiceDetailQuery'],
  data() {
    return {
      invoicesQuery: {
        projectId: '',
        productName: '',
        productNo: '',
        stockId: '',
        shelfId: '',
        planNo: '',
        buildId: '',
        floorId: '',
        productTypeId: '',
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        },
      },
      //查询下拉框赋值
      stocks: [],
      shelves: [],
      projects: [],
      invoices: [],
      selected: [],
      buildList: [],
      floors: [],
      buildings: [],
      // projectId: '',
      value1: [],
      arrProductType: [],
    }
  },
  mounted() {
    this.info()
    this.list()
  },
  methods: {
    info() {
      shelfModel.getStocks().then(data => {
        this.stocks = data.entity;
        this.stocks = this.stocks.filter(s => s.stockStatus == '启用' && s.stockTypeCode == '成品仓库')
      }).catch(err => {
      })
      projectModel.getExternalAllProject().then((data) => {
        this.projects = data.entity
      })
      console.log('invoiceDetailQuery--->', this.invoiceDetailQuery)
      buildModel.findByProjectId(this.invoiceDetailQuery.projectId).then(data => {

        this.buildings = data.entity
      })
      productTypeModel.getAllproductTypes().then((data) => {
        this.arrProductType = data.entity
      })
    },


    // 点击整行都可以进行勾选
    clickRow(row) {
      this.$refs.moviesTable.toggleRowSelection(row)
    },
    focusBuild(row) {
      this.buildList = []
      let model = {
        projectId: row.projectId,
        productId: row.productId,
      }
      console.log('productId---->', row.productId)
      buildModel.findBuildingsByProjectIdAndProductId(model).then((data) => {
        this.buildList = data.entity
        console.log(data.entity)
      })
    },
    focusFloor(row) {
      console.log('row------>', row)
      this.floors = []
      let model = {
        projectId: row.projectId,
        productId: row.productId,
        buildId: row.buildCode
      }
      //只改楼层操作执行
      let valueBuildCode = typeof row.buildCode
      if (valueBuildCode == 'string') {
        model.buildId = row.buildId
      }
      if (row.buildCode != '' && row.projectId != '' && row.productId != '') {
        floorModel.findFloorsByProjectIdAndProductIdAndBuildId(model).then((data) => {
          this.floors = data.entity
        })
      }
    },
    onSubmit() {
      this.invoicesQuery.pageInfo.currentPage = 1
      this.list()
    },
    list() {
      this.invoicesQuery.productId = this.invoiceDetailQuery.productId
      planModel.findInvoiceNewList(this.invoicesQuery).then((data) => {
        console.log('data------>', data.entity)
        let list = data.entity.list
        list = list.filter(pd => {
          return !this.newInvoiceDetails.some(d => d.productDetailId == pd.productDetailId)
        })
        this.invoices = list
        //filter过滤后数据的总数量
        let total = 0;
        if (data.entity.list != null && list != null) {
          total = data.entity.list.length - list.length
        }
        this.invoicesQuery.pageInfo.total = data.entity.total - total
      })
    },
    changeBuilding(row) {
      this.floors = []
      row.floorNum = ''
      /*   floorModel.findByBuildingIdAndProjectId(row.buildCode, row.projectId).then((data) => {
             this.floors = data.entity
         })*/
    },
    changeBuildingSelect(id) {
      if (id == undefined) {
        id = '0'
      }
      this.invoicesQuery.floorId = ''
      this.floors = []
      // let building = this.buildings.find(b => b.id == id)
      floorModel.findByBuildingIdAndProjectId(id, this.invoiceDetailQuery.projectId).then((data) => {
        this.floors = data.entity
        this.invoicesQuery.floorId = ''
      })
    },

    add() {
      if (this.selected.length == 0) {
        this.$message('请选择添加项');
        return;
      }
      this.selected.forEach(s => {
        s.relevanceId = this.invoiceDetailQuery.relevanceId
        s.buildId = this.invoiceDetailQuery.buildId
        s.floorId = this.invoiceDetailQuery.floorId
      })
      this.$emit('product-detail-choose-finish', this.selected)
    },

    changeStock(id) {
      //防止清楚库区时报错
      if (id == '' || id == undefined) {
        id = 0
      }
      stockModel.getShelfsById(id).then(data => {
        this.shelves = data.entity
        //this.shelves = this.shelves.filter(s => s.shelfStatus == '启用' && s.fullStatus == '未满')
        this.invoicesQuery.shelfId = ''
      })
    },
    momentFormatTime(row, column, cellValue, index) {
      if (cellValue) {
        return moment(cellValue).format('YYYY-MM-DD HH:mm:ss')
      } else {
        return cellValue
      }

    },
    wtFormat(row, column, cellValue, index) {
      if (cellValue) {
        return numeral(cellValue).format('0.000')
      } else {
        return cellValue
      }

    },
    handleSizeChange(val) {
      this.invoicesQuery.pageInfo.currentPage = 1
      this.invoicesQuery.pageInfo.pageSize = val
      this.list()
    },
    handleCurrentChange(val) {
      this.invoicesQuery.pageInfo.currentPage = val
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
