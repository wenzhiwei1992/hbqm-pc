<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form :inline="true" :model="sfgWarehousingDetailQuery" ref="sfgWarehousingDetailQuery">
              <el-form-item prop="sfgNo">
                <el-input
                    placeholder="半成品编码"
                    v-model="sfgWarehousingDetailQuery.sfgNo"
                    style="width:150px"
                    clearable>
                </el-input>
              </el-form-item>
              <el-form-item prop="sfgName">
                <el-input
                    placeholder="半成品名称"
                    v-model="sfgWarehousingDetailQuery.sfgName"
                    style="width:150px"
                    clearable>
                </el-input>
              </el-form-item>
              <el-form-item prop="buildingId">
                <el-select
                    style="width:110px"
                    placeholder="楼栋"
                    v-model="sfgWarehousingDetailQuery.buildingId"
                    size="mini"
                    clearable
                    @change="changeBuilding"
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
                    style="width:110px"
                    placeholder="楼层"
                    :disabled="sfgWarehousingDetailQuery.buildingId==''"
                    v-model="sfgWarehousingDetailQuery.floorId"
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
              <el-form-item prop="stockId">
                <el-select v-model="sfgWarehousingDetailQuery.stockId" style="width: 150px" placeholder="仓库">
                  <el-option
                          v-for="item in stockList"
                          :key="item.id"
                          :label="item.stockNo"
                          :value="item.id">
                  </el-option>
                </el-select>
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
            :data="sfgData"
            @row-click="clickRow"
            ref="moviesTable"
            @selection-change="handleSelectionChange"
            size="mini"
            stripe
            border
            height="100%"
        >
          <el-table-column
              type="selection"
              width="80">
          </el-table-column>
          <el-table-column
                  prop="stockNo"
                  label="仓库"
                  width="100">
          </el-table-column>
          <el-table-column
              prop="sfgNo"
              label="半成品编码"
              width="120">
          </el-table-column>
          <el-table-column
              prop="sfgName"
              label="半成品名称"
          >
          </el-table-column>
          <el-table-column
              prop="rebarShape"
              label="钢筋形状"
          >
          </el-table-column>
          <el-table-column
              prop="projectName"
              label="项目名称"
          >
          </el-table-column>
          <el-table-column
              prop="buildCode"
              label="楼栋"
              width="70"
          >
          </el-table-column>
          <el-table-column
              prop="floorNum"
              label="楼层"
              width="70"
          >
          </el-table-column>
          <el-table-column
              prop="unitCNName"
              label="单位"
          >
          </el-table-column>
          <el-table-column
              prop="needNum"
              label="需求量"
          >
          </el-table-column>
          <el-table-column
              prop="alreadyReceivedNum"
              label="已领用量"
          >
          </el-table-column>
          <el-table-column
              prop="remainingNum"
              label="可领用量"
          >
          </el-table-column>
          <el-table-column
              prop="inStockNum"
              label="库存量"
          >
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer ref="footOperationBar">
        <!--      <div class="classButton">
                  <el-col>
                      <el-button type="success" v-on:click="addClick">添加</el-button>
                      <el-button type="danger" @click="deleteClick">关闭</el-button>
                  </el-col>
              </div>-->
        <el-row>
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="sfgWarehousingDetailQuery.pageInfo.currentPage"
              :page-sizes="sfgWarehousingDetailQuery.pageInfo.pageSizes"
              :page-size="sfgWarehousingDetailQuery.pageInfo.pageSize"
              :total="sfgWarehousingDetailQuery.pageInfo.total"
              layout="total, sizes, prev, pager, next, jumper"
              background
          >
          </el-pagination>
        </el-row>
      </el-footer>
      <el-dialog
          :visible.sync="showSelectTree"
          width="340px"
          append-to-body
      >
        <tree-info
            :treeArr="treeArr"
            :baseCheckedId="baseCheckedId"
            :baseCheckParentId="baseCheckParentId"
            @tree-edit-submit="checkoutStatusFinish"
            @tree-edit-cancel="checkoutStatusCancel"
        ></tree-info>
      </el-dialog>
    </el-container>
  </div>
</template>

<script>
import categoryModel from "../../../model/basicdata/category";
import sfgWarehousingDetailModel from "../../../model/sfg/sfgWarehousingDetail"
import treeInfo from "../basic/materialStorage/TreeInfo.vue";
import floorModel from "../../../model/project/floor";
import buildingModel from "../../../model/project/building";
import stockModel from "../../../model/basicdata/stock";

export default {
  name: "SfgPickingSelect",
  props: ['sfgPickingAddList', 'projectId','sfgPickingDetails'],
  components: {
    'tree-info': treeInfo,
  },
  data() {
    return {
      buildings: [],
      floors: [],
      stockList: [],
      treeArr: '',
      showSelectTree: false,
      baseCheckedId: [],
      baseCheckParentId: [],
      selected: [],
      sfgData: [],
      arrCategory: '',
      sfgWarehousingDetailQuery: {
        sfgNo: '',
        sfgName: '',
        sfgSpecification: '',
        categoryId: '',
        floorId: null,
        buildingId: null,
        stockId: null,
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        },
      },
      buttonData: [
        {
          name: '添加',
          event: 'addClick'
        },
      ],
      searchButtonData: [
        {
          name: '查询',
          event: "submitForm('sfgWarehousingDetailQuery')"
        },
        {
          name: '重置',
          event: 'resetForm("sfgWarehousingDetailQuery")'
        }
      ],
      rowButtonData: [
        {
          name: '编辑',
          event: "editClick",
        },
      ]

    }
  },
  mounted() {
    this.showList()
    this.showCategory()
  },
  methods: {
    showList() {
      if (this.sfgWarehousingDetailQuery.buildingId == null || this.sfgWarehousingDetailQuery.buildingId == '') {
        this.sfgWarehousingDetailQuery.buildingId = ''
      }
      if (this.sfgWarehousingDetailQuery.floorId == null || this.sfgWarehousingDetailQuery.floorId == '') {
        this.sfgWarehousingDetailQuery.floorId = ''
      }
      if (this.sfgWarehousingDetailQuery.stockId == null || this.sfgWarehousingDetailQuery.stockId == '') {
        this.sfgWarehousingDetailQuery.stockId = ''
      }
      this.sfgWarehousingDetailQuery.projectId = this.projectId
      sfgWarehousingDetailModel.findSfgWarehousingDetailByQuery(this.sfgWarehousingDetailQuery).then((data) => {
        let list = data.entity.list
        console.log("list----------->",data.entity.list)
        console.log("stockId----->",this.sfgWarehousingDetailQuery)
        list = list.filter(m => {
          return !this.sfgPickingAddList.some(s => s.sfgId == m.sfgId && s.buildingId == m.buildingId && s.floorId == m.floorId )
        })
        this.sfgData = list
        //filter过滤后数据的总数量
        let total = 0;
        if (data.entity.list != null && list != null) {
          total = data.entity.list.length - list.length
        }
        this.sfgWarehousingDetailQuery.pageInfo.total = data.entity.totalCount - this.sfgPickingAddList.length
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log('submitForm');//TO DELETE
          this.showList()
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    addClick() {
      if (this.selected.length == 0) {
        this.$message('请选择增加项');
        return;
      }
      //存放要添加的原料编码
      let arr = []
      let legalAdd = true
      this.selected.forEach((row) => {
        const sfg = {
          sfgNo: row.sfgNo,
          sfgName: row.sfgName,
          sfgSpecification: row.sfgSpecification,
          unitCNName: row.unitCNName,
          sfgId: row.sfgId,
          inStockNum: row.inStockNum,
          remainingNum: row.remainingNum,
          num: '1',
          needNum: row.needNum,
          rebarShape: row.rebarShape,
          projectName: row.projectName,
          buildCode: row.buildCode,
          floorNum: row.floorNum,
          alreadyReceivedNum: row.alreadyReceivedNum,
          receiveNum: row.receiveNum,
          buildingId: row.buildingId,
          floorId: row.floorId,
          stockNo: row.stockNo,
          stockId: row.stockId,
        }
        arr.push(sfg)//把选中的构件放到数组中
        if (row.remainingNum == 0) {
          legalAdd = false
        }
      })
      if (!legalAdd) {
        this.$message('已选半成品中可申请量为0');
        return;
      }
      //第一个参数是父组件中v-on绑定的自定义回调方法，第二个参数为传递的参数
      this.$emit('sfg-picking-select-finish', arr);
      //  this.showList()

    },
    deleteClick() {
      this.$emit('sfg-picking-select-cancel')
      this.showList()
    },
    showCategory() {
      categoryModel.getCategorysToTreeParentDisabled().then((data) => {
        this.treeArr = data.entity
      })
      categoryModel.getCategorysByOrgId().then((data) => {
        this.arrCategory = data.entity
      })
      buildingModel.findByProjectId(this.projectId).then(data => {
        this.buildings = data.entity
      })
      stockModel.getStockByStockType('半成品仓库').then((data) => {
        this.stockList = data.entity
      })
    },
    handleSelectionChange(selected) {
      this.selected = selected
    },
    handleSizeChange(val) {
      this.sfgWarehousingDetailQuery.pageInfo.pageSize = val
      this.showList()
    },
    handleCurrentChange(val) {
      this.sfgWarehousingDetailQuery.pageInfo.currentPage = val
      this.showList()
    },
    //点击整行都可以进行勾选
    clickRow(row) {
      this.$refs.moviesTable.toggleRowSelection(row)
    },
    checkoutStatusFinish(id, name) {
      //子插件回传赋值
      this.sfgWarehousingDetailQuery.categoryName = name;
      this.sfgWarehousingDetailQuery.categoryId = id;
      this.showSelectTree = false
    },
    checkoutStatusCancel() {
      this.showSelectTree = false
    },
    showSelectTrees() {
      this.baseCheckedId.splice(0, this.baseCheckedId.length);
      this.baseCheckedId.push(this.sfgWarehousingDetailQuery.categoryId);

      this.baseCheckParentId.splice(0, this.baseCheckedId.length);
      this.baseCheckParentId.push(this.sfgWarehousingDetailQuery.categoryId);

      this.showSelectTree = true
    },
    resetForm(formName) {
      this.sfgWarehousingDetailQuery.categoryId = ''
      this.sfgWarehousingDetailQuery.categoryName = ''
      this.$refs[formName].resetFields();
    },
    changeBuilding(id) {
      if (id == '') {
        id = 0
      }
      this.sfgWarehousingDetailQuery.floorId = ''
      this.floors = []
      let building = this.buildings.find(b => b.id == id)
      if (building != null) {
        floorModel.findByBuildingIdAndProjectId(building.id, building.projectId).then((data) => {
          this.floors = data.entity
          this.sfgWarehousingDetailQuery.floorId = ''
        })
      }
    },
  },
  watch: {
    projectId: function () {
      this.showCategory()
    }
  },

}
</script>

<style scoped>

</style>
