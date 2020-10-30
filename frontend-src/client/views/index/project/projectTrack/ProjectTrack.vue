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
                  v-model="formInline.projectId"
                  placeholder="项目名称"
                  clearable
                >
                  <el-option
                    v-for="item in projectNames"
                    :key="item.id"
                    :label="item.projectName"
                    :value="item.id"
                  ></el-option>
                </el-select>
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
            prop="projectName"
            label="项目名称"
          >
          </el-table-column>

          <el-table-column
            prop="schedule"
            label="生产完成进度"
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
                  label="需求量"
          >
            <template slot-scope="scope">
              <span>{{scope.row.needTotal}}个/{{scope.row.needTotalVol}}m³</span>
            </template>
          </el-table-column>
          <el-table-column
                  prop="overTotal"
                  label="完成量"
          >
            <template slot-scope="scope">
              <span>{{scope.row.overTotal}}个/{{scope.row.overTotalVol}}m³</span>
            </template>
          </el-table-column>
          <el-table-column
                  prop="jzCount"
                  label="浇筑量"
          >
            <template slot-scope="scope">
              <span>{{scope.row.jzCount+scope.row.overTotal}}个/{{scope.row.jzCountVol+scope.row.overTotalVol}}m³</span>
            </template>
          </el-table-column>
          <el-table-column
                  prop="fhCount"
                  label="发货量"
          >
            <template slot-scope="scope">
              <span>{{scope.row.fhCount}}个/{{scope.row.fhCountVol}}m³</span>
            </template>
          </el-table-column>
          <!--<el-table-column
                  prop="fhqCount"
                  label="发货欠量"
          >
            <template slot-scope="scope">
              <span>{{scope.row.fhqCount}}个/{{scope.row.fhqCountVol}}m³</span>
            </template>
          </el-table-column>-->
          <!-- <el-table-column
                   prop="xfCount"
                   label="下发量"
           >
           </el-table-column>-->
          <el-table-column
                  prop="zkCount"
                  label="在库量"
          >
            <template slot-scope="scope">
              <span>{{scope.row.zkCount}}个/{{scope.row.zkCountVol}}m³</span>
            </template>
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
import orgModel from '../../../../model/system/org'
import prodcutTrack from '../../../../model/project/productTrack'
import { Notification } from 'element-ui'
import moment from "moment";

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
      selected: [],
      expands: [],
      formInline: {
        projectId: '',
        projectName: '',
        lineName: '',
        productType: '',
        productNo: '',
        productName: ''
      },
      productTypes: [],
      lineNames: [],
      projectNames: [],
      productBaseInfos: [],
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
      this.list();
    },
    list() {
      this.$store.commit('startLoading')
      prodcutTrack.findAllProjectList().then(data => {
        this.projectNames = data.entity;
      })
      this.formInline.pageInfo = this.pageInfo;
      prodcutTrack.findAllByFormInlineAndProject(this.formInline).then(data => {
        console.log("data.entity", data.entity);
        this.datas = data.entity.data;
        this.pageInfo.total = data.entity.totalCount;
        this.$store.commit('endLoading')
      }).catch(err => {
        console.error(err)
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