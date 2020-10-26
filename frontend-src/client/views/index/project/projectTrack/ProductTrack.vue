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
                <el-select
                  v-model="formInline.projectName"
                  placeholder="项目名称"
                >
                  <el-option
                    value=""
                    label=""
                  > </el-option>
                  <el-option
                    v-for="item in projectNames"
                    :key="item.id"
                    :label="item.projectName"
                    :value="item.projectName"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-select
                  v-model="formInline.lineName"
                  placeholder="产线"
                >
                  <el-option
                    value=""
                    label=""
                  > </el-option>
                  <el-option
                    v-for="item in lineNames"
                    :key="item.id"
                    :label="item.lineName"
                    :value="item.lineName"
                  ></el-option>
                </el-select>

              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="formInline.productNo"
                  placeholder="构件编码"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="formInline.productName"
                  placeholder="构件名称"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-select
                  v-model="formInline.orgId"
                  placeholder="组织名称"
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
          :data="datas"
          size="mini"
          stripe
          border
          ref="handleClickRowSelection"
          height="100%"
          @row-click="handleClickRowSelection"
          style="width: 100%"
        >
          <el-table-column
            type="index"
            label="序号"
            width="50"
          >
          </el-table-column>
          <el-table-column
            prop="orderNo"
            label="订单编号"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="productNo"
            label="构件编码"
          >
          </el-table-column>
          <el-table-column
            prop="productName"
            label="构件名称"
          >
          </el-table-column>
          <!-- <el-table-column
                    prop="typeName"
                    label="构件类别"
                    width="120"
            >
            </el-table-column> -->
          <el-table-column
            prop="schedule"
            label="生产完成进度"
            width="250"
          >
            <template slot-scope="scope">
              <el-progress
                :text-inside="true"
                :stroke-width="18"
                :percentage="scope.row.schedule"
                :color="customColorMethod"
              ></el-progress>
            </template>
          </el-table-column>
          <el-table-column
            prop="needTotal"
            label="需求总数"
          >
          </el-table-column>
          <el-table-column
            prop="overTotal"
            label="完成总量"
          >
          </el-table-column>
          <el-table-column
            prop="scCount"
            label="生产中"
          >
          </el-table-column>
          <el-table-column
            prop="zkCount"
            label="在库量"
          >
          </el-table-column>
          <el-table-column
            prop="fhCount"
            label="发货量"
          >
          </el-table-column>
          <el-table-column
            prop="scqCount"
            label="生产欠量"
          >
          </el-table-column>
          <el-table-column
            prop="fhqCount"
            label="发货欠量"
          >
          </el-table-column>
          <el-table-column
            prop="orgName"
            label="组织名称"
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
import prodcutTrack from '../../../../model/project/productTrack'
import orgModel from '../../../../model/system/org'
import { Notification } from 'element-ui'
import moment from "moment";

export default {
  data() {
    return {
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: 10,
        pageSizes: [2, 5, 10]
      },
      datas: [],
      selected: [],
      expands: [],
      formInline: {
        projectName: '',
        lineName: '',
        productType: '',
        productNo: '',
        productName: ''
      },
      productTypes: [],
      lineNames: [],
      projectNames: [],
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
          event: "onSubmit"
        }
      ],
      rowButtonData: [

      ]

    }

  },
  mounted() {
    this.list();
    orgModel.orgAllByOrgIds().then(data => {
      this.orgs = data.entity;
    }).catch(() => {

    })
  },
  methods: {
    customColorMethod(percentage) {
      if (percentage <= 99) {
        return '#409eff';
      } else {
        return '#67c23a';
      }
    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    onSubmit() {
      this.formInline.pageInfo = this.pageInfo;
      prodcutTrack.findAllByFormInline(this.formInline).then(data => {
        this.datas = data.entity;
        this.pageInfo.total = data.entity.length
      })
    },
    list() {
      this.$store.commit('startLoading')
      prodcutTrack.findAllProjectList().then(data => {
        this.projectNames = data.entity;
      })
      prodcutTrack.getAllProdLines().then(data => {
        this.lineNames = data.entity;
      })
      prodcutTrack.getAllproductTypes().then(data => {
        this.productTypes = data.entity;
      })
      prodcutTrack.list(this.pageInfo).then(data => {
        this.datas = data.entity;
        this.pageInfo.total = data.entity.length;
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
    }, handleSelectionChange(selected) {
      this.selected = selected
    },
  }
}
</script>

<style scoped>
</style>