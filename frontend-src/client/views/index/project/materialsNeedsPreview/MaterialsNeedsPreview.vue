<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <div>
              <project_materials_query @change-criteria="changeCriteria"></project_materials_query>
            </div>
          </el-col>
          <el-col>
             <my-search-button-group :model="searchButtonData"></my-search-button-group>
          </el-col>
        </el-row>
        <el-row >
          <my-button-group :model="buttonData"></my-button-group>
        </el-row>
      </el-header>
      <el-main
        ref="mainContent"
        :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <el-table
              :data="projectMaterials"
              size="mini"
              stripe
              border
              height="100%"
              ref="handleClickRowSelection"
              @row-click="handleClickRowSelection"

            >

          <el-table-column
                  type="index"
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
                prop="quantity"
                label="需求量"
                width="100"
              >
              </el-table-column>
              <el-table-column
                prop="issue"
                label="收料量"
                width="120"
              >
              </el-table-column>
              <el-table-column
                prop="useNum"
                label="领料量"
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


        <el-table
                :data="exportdatas"
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
                  prop="quantity"
                  label="需求量"
                  width="100"
          >
          </el-table-column>
          <el-table-column
                  prop="issue"
                  label="收料量"
                  width="120"
          >
          </el-table-column>
          <el-table-column
                  prop="useNum"
                  label="领料量"
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
import projectModel from '../../../../model/project/project'
import xlsx from 'xlsx';
import fileSaver from 'file-saver';
export default {
  components: {
    'project_materials_query': ProjectMaterialsQuery,
  },
  name: "MaterialsNeedsPreview",
  data() {
    return {
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
      projectMaterials: [],
      productBaseMaterials: [],
        exportdatas:[],
      floorNumMaterials: [],
      expands: [],
      expands2: [],
      subDatas: [],
      subDatas2: [],
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
          const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
          });
          let page = this.productMaterialQuery.pageInfo.currentPage;
          let size = this.productMaterialQuery.pageInfo.pageSize;

          this.productMaterialQuery.pageInfo.currentPage = 1;
          this.productMaterialQuery.pageInfo.pageSize = this.productMaterialQuery.pageInfo.total;

          projectModel.getMaterialsInfos(this.productMaterialQuery).then(data => {

              this.exportdatas = data.entity.list
              setTimeout(() => {
                  var excelName = "export.xlsx";
                  /* generate workbook object from table */
                  let table = document.querySelector('#exportXlsx');
                  var wb = xlsx.utils.table_to_book(table);
                  /* get binary string as output */
                  var wbout = xlsx.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'})
                  try {
                      fileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), excelName)
                  } catch (e) {
                      if (typeof console !== 'undefined') console.log(e, wbout)
                  }

                  this.productMaterialQuery.pageInfo.pageSize = size;
                  this.productMaterialQuery.pageInfo.currentPage = page;
                  loading.close();
                  return wbout;
              }, 500);

          }).catch(err => {

          })

      },
    showSubData(row, expandedRows) {
      this.subDatas = []
      if (this.expands.some((e) => e == row.materialStorageNo)) {
        this.expands = []
      } else {
        this.expands = []
        this.expands.push(row.materialStorageNo)
      }
      var arr = {
        materialStorageNo: row.materialStorageNo,
        projectId: row.projectId,
      }
      projectModel.getMaterialsToProductBaseByMaterialNo(arr).then((data) => {
        this.subDatas = data.entity
      })
    },
    showSubData2(row, expandedRows) {
      this.subDatas2 = []
      if (this.expands2.some((e) => e == row.orderNo)) {
        this.expands2 = []
      } else {
        this.expands2 = []
        this.expands2.push(row.orderNo)
      }
      var arr = {
        materialStorageNo: row.materialStorageNo,
        projectId: row.projectId,
        orderNo: row.orderNo
      }
      projectModel.getMaterialsToFloorNumByMaterialNo(arr).then((data => {
        this.subDatas2 = data.entity
      }))
    },
    changeCriteria(entity) {
      this.productMaterialQuery = entity
      this.showList()
    },
    showList() {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      projectModel.getMaterialsInfos(this.productMaterialQuery).then((data) => {
        console.log(data.entity)
        this.projectMaterials = data.entity.list
        this.productMaterialQuery.pageInfo.total = data.entity.totalCount
        loading.close();
      }).catch(() => {
        loading.close();
      })
    },
    showProductBaseMaterials(row, expandedRows) {
      var arr = {
        materialStorageNo: row.materialStorageNo,
        projectId: row.projectId,
      }
      productModel.getMaterialsToProductBaseByMaterialNo(arr).then((data) => {
        // this.productBaseMaterials = data.entity
        for (var i = 0; i < this.projectMaterials.length; i++) {
          if (this.projectMaterials[i].materialStorageNo == row.materialStorageNo) {
            this.projectMaterials[i].productBaseMaterials = data.entity
            break;
          }
        }
      })
    },
    showfloorNumMaterials(row, expandedRows) {
      var arr = {
        materialStorageNo: row.materialStorageNo,
        projectId: row.projectId,
        baseName: row.baseName,
      }
      productModel.getMaterialsToFloorNumByMaterialNo(arr).then((data) => {
        this.floorNumMaterials = data.entity
      })
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