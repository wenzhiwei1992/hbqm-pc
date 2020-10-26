<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form
              :model="yardDateilQuery"
              v-if="yardDateilQuery"
              ref="yardDateilQuery"
              :inline="true"
            >
              <el-form-item prop="orgId">
                <el-select
                  v-model="yardDateilQuery.orgId"
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
        <el-row style="display:none;">
          <my-button-group :model="buttonData"></my-button-group>
        </el-row>
      </el-header>
      <el-main
        ref="mainContent"
        :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <el-table
          :data="yardDateils"
          @expand-change="showSubData"
          :expand-row-keys="expands"
          row-key="id"
          @selection-change="handleSelectionChange"
          size="mini"
          stripe
          border
          height="100%"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
          style="width:100%"
        >
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-table
                :data="subDatas"
                size="mini"
                stripe
                border
                style="width: 100%"
              >

                <!-- <el-table-column
                                prop="detailNo"
                                label="构件明细编码"
                        >
                        </el-table-column> -->
                <el-table-column
                  prop="product.productNo"
                  label="构件编码"
                >
                </el-table-column>
                <el-table-column
                  prop="product.productName"
                  label="构件名称"
                >
                </el-table-column>
                <el-table-column
                  prop="product.projectConcrete"
                  label="砼方量"
                >
                </el-table-column>
              </el-table>
            </template>
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            width="70"
          >
          </el-table-column>
          <el-table-column
            prop="shelfName"
            label="货架"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="shelfNo"
            label="编号"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="stockName"
            label="库区"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="stockId"
            label="堆场编号"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="stockNum"
            label="已堆放数"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="shelfNum"
            label="货架容量"
            width="100"
          >
          </el-table-column>
          <el-table-column
            prop="stackingCapacity"
            label="剩余容量"
          >
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer ref="footOperationBar">
        <el-row>
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="yardDateilQuery.pageInfo.currentPage"
            :page-sizes="yardDateilQuery.pageInfo.pageSizes"
            :page-size="yardDateilQuery.pageInfo.pageSize"
            :total="yardDateilQuery.pageInfo.total"
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
import yardDateilModel from "../../../model/basicdata/yardDateil";
import orgModel from '../../../model/system/org'
import { Notification } from 'element-ui'

export default {
  name: "YardDetail",
  data() {
    return {
      yardDateilQuery: {
        orgId: '',
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        },
      },
      yardDateils: [],
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: this.GLOBAL.pageSize,
        pageSizes: this.GLOBAL.pageSizes
      },
      expands: [],
      subDatas: [],
      orgs: [],
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        }
      ],
      searchButtonData: [
        {
          name: '查询',
          event: "list"
        }
      ],
      rowButtonData: [
      ]
    }
  },
  mounted() {
    this.info()
    this.list()
  },
  methods: {
    showSubData(row, expandedRows) {
      let id = row.id;
      if (this.expands.some((e) => e == row.id)) {
        this.expands = []
      } else {
        this.expands = []
        this.expands.push(row.id)
      }
      yardDateilModel.getDetailByIdAndStockId(id).then(data => {
        this.subDatas = data.entity;
        console.log("subdata", this.subDatas)
      })
    },
    list() {
      yardDateilModel.getYardDateil(this.yardDateilQuery).then((data) => {
        this.yardDateils = data.entity.list
        this.yardDateilQuery.pageInfo.total = data.entity.totalCount
      })
    },
    info() {
      orgModel.orgAllByOrgIds().then((data) => {
        this.orgs = data.entity
      })
    },
    handleSelectionChange(selected) {
      this.selected = selected
    },
    handleSizeChange(val) {
      this.yardDateilQuery.pageInfo.pageSize = val
      this.list()
    },
    handleCurrentChange(val) {
      this.yardDateilQuery.pageInfo.currentPage = val
      this.list()
    },
  },

}
</script>

<style scoped>
</style>