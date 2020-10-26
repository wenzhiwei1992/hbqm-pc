<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <div>
              <project_materials_query
                list-type="base"
                @change-criteria="changeCriteria"
              ></project_materials_query>
            </div>
          </el-col>
          <el-col>
            <!-- <my-search-button-group :model="searchButtonData"></my-search-button-group> -->
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
          :data="productBaseMaterials"
          size="mini"
          stripe
          border
          @selection-change="handleSelectionChange"
          height="100%"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
          router-link
          id="exportXlsx"
        >
          <el-table-column
            prop="materialStorageNo"
            label="原料编码"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="materialName"
            label="原料名称"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="materialSpecification"
            label="原料规格"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="materialUnit"
            label="单位"
            width="80"
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
            prop="orderNo"
            label="订单编号"
            width="170"
          >
          </el-table-column>
          <el-table-column
            prop="quantity"
            label="需求量"
            width="100"
          >
          </el-table-column>
          <el-table-column
            prop="issue"
            label="已发量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="deliveryShortage"
            label="发货欠量"
            width="100"
          >
          </el-table-column>
          <el-table-column
            prop="useNum"
            label="使用量"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="onHandInventory"
            label="在库量"
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
            :current-page="productMaterialQuery.pageInfo.currentPage"
            :page-sizes="productMaterialQuery.pageInfo.pageSizes"
            :page-size="productMaterialQuery.pageInfo.pageSize"
            :total="productMaterialQuery.pageInfo.total"
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
import ProjectMaterialsQuery from '../productMaterialReport/ProjectMaterialsQuery.vue'
import productModel from '../../../../model/project/product'
import fileSaver from 'file-saver';
import xlsx from 'xlsx';

export default {
  name: "OrderMaterials",
  components: {
    'project_materials_query': ProjectMaterialsQuery,
  },
  data() {
    return {
      selected: [],
      productBaseMaterials: [],
      productMaterialQuery: {
        listType: '',
        projectId: '',
        houseTypeId: '',
        buildingId: '',
        floorId: '',
        materialStorageNo: '',
        productBaseId: '',
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
      searchButtonData: [],
      rowButtonData: []
    }
  },

  mounted() {
    this.showList()
  },
  methods: {
    exportExcel() {
      /* generate workbook object from table */
      var wb = xlsx.utils.table_to_book(document.querySelector('#exportXlsx'))
      /* get binary string as output */
      var wbout = xlsx.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' })
      try {
        fileSaver.saveAs(new Blob([wbout], { type: 'application/octet-stream' }), 'export.xlsx')
      } catch (e) {
        if (typeof console !== 'undefined') console.log(e, wbout)
      }
      return wbout
    },
    changeCriteria(entity) {
      this.productMaterialQuery = entity
      this.showList()
    },
    showList() {
      this.productMaterialQuery.listType = 'base'
      productModel.getMaterialsToOrder(this.productMaterialQuery).then((data) => {
        this.productBaseMaterials = data.entity.list
        this.productMaterialQuery.pageInfo.total = data.entity.totalCount
      })
    },
    handleSelectionChange(selected) {
      this.selected = selected
    },
    handleSizeChange(val) {
      this.productMaterialQuery.pageInfo.pageSize = val
      this.showList()
    },
    handleCurrentChange(val) {
      this.productMaterialQuery.pageInfo.currentPage = val
      this.showList()
    },

  }
}
</script>

<style scoped>
</style>