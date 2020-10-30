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
                  v-model="formInline.orderNo"
                  filterable
                  remote
                  reserve-keyword
                  clearable
                  style="width: 160px"
                  placeholder="输入生产计划加载数据"
                  :remote-method="remoteMethod"
                  :loading="loading"
                >
                  <el-option
                    v-for="item in orderDatas"
                    :key="item.id"
                    :label="item.orderNo"
                    :value="item.orderNo"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="formInline.projectName"
                  placeholder="项目名称"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-select
                  v-model="formInline.orderStatus"
                  placeholder="订单状态"
                  multiple
                  collapse-tags
                >
                  <el-option
                    label="新建"
                    value="新建"
                  ></el-option>
                  <el-option
                    label="提交"
                    value="提交"
                  ></el-option>
                  <el-option
                    label="生产中"
                    value="生产中"
                  ></el-option>
                  <el-option
                    label="完成"
                    value="完成"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-date-picker
                  v-model="formInline.planDateFrom"
                  type="date"
                  placeholder="选择开始日期"
                  style="width:84%"
                >
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-date-picker
                  v-model="formInline.planDateTo"
                  type="date"
                  placeholder="选择结束日期"
                  style="width:84%"
                >
                </el-date-picker>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col>
            <my-search-button-group :model="searchButtonData"></my-search-button-group>
          </el-col>
        </el-row>
        <el-row>
          <el-col style="display:none;">
            <my-button-group :model="buttonData"></my-button-group>
          </el-col>
        </el-row>
        <el-row style="display:flex;justify-content: flex-end;">
          <el-col>
            <b>产出量: {{outputTotal}} </b>
            <br>
            <b>订货总数: {{orderQtyTotal}}</b>
          </el-col>
        </el-row>
      </el-header>
      <el-main
        ref="mainContent"
        :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <el-table
          :data="datas"
          @expand-change="showSubData"
          @sort-change="sortChange"
          size="mini"
          stripe
          border
          :default-sort="{prop: 'dateCreated', order: 'descending'}"
          :expand-row-keys="expands"
          row-key="id"
          height="100%"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
          style="width: 100%"
        >
          <!-- 这是点击小箭头出现的信息 -->
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-table
                :data="subDatas"
                @selection-change="handleSelectionChange"
                size="mini"
                stripe
                border
                style="width: 100%"
              >
                <el-table-column
                  type="selection"
                  width="80"
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
                <el-table-column
                  prop="num"
                  label="订货量"
                >
                </el-table-column>
                <el-table-column
                  prop="output"
                  label="产出量"
                >
                </el-table-column>
                <el-table-column
                  prop="underNum"
                  label="生产欠量"
                >
                </el-table-column>
                <el-table-column
                  prop="deliveryNum"
                  label="发货量"
                >
                </el-table-column>
                <el-table-column
                  prop="deliveryUnderNum"
                  label="发货欠量"
                >
                </el-table-column>
                <el-table-column
                  prop="houseTypeName"
                  label="户型"
                  v-if="isShow"
                >
                </el-table-column>
                <el-table-column
                  prop="buildCode"
                  label="楼栋"
                  v-if="isShow"
                >
                </el-table-column>
                <el-table-column
                  prop="floorNum"
                  label="楼层"
                  v-if="isShow"
                >
                </el-table-column>
              </el-table>
            </template>
          </el-table-column>
          <el-table-column
            type="index"
            label="序号" sortable width="90px"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="orderNo"
            label="订单编号"
          >
          </el-table-column>
          <el-table-column
            sortable
            prop="orderStatus"
            label="订单状态"
          >
            <template slot-scope="scope">
              <span :class="{status_color1: scope.row.orderStatus == '生产中' , status_color2 : scope.row.orderStatus == '完成'}">{{scope.row.orderStatus}}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="chooseTime"
            label="提交时间" sortable
            :formatter="formatterDate"
          >
          </el-table-column>
          <el-table-column
            prop="createdByName"
            label="创建人" sortable
          >
          </el-table-column>
          <!-- <el-table-column
                    prop="customer"
                    label="客户名称">
            </el-table-column>

            <el-table-column
                    prop="projectName"
                    label="项目名称">
            </el-table-column>
            <el-table-column
                    prop="workPlace"
                    label="工地名称">
            </el-table-column> -->
          <el-table-column
            prop="output"
            label="产出量" sortable
          >
          </el-table-column>
          <el-table-column
            prop="orderQty"
            label="订货总数" sortable
          >
          </el-table-column>

          <el-table-column
            prop="dateCreated"
            :formatter="formatterDate"
            label="订货时间" sortable
          >
          </el-table-column>
          <el-table-column
            prop="planDateFrom"
            :formatter="formatterDate"
            label="开始时间" sortable
          >
          </el-table-column>
          <el-table-column
            prop="planDateTo"
            :formatter="formatterDate"
            label="结束时间" sortable
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
import orderMove from '../../../model/po/orderMove'
import orderTrack from '../../../model/po/orderTrack'
import { Notification } from 'element-ui'
import moment from "moment";

export default {
  data() {
    return {
      loading: false,
      orderDatas: [], //生产计划远程加载数据
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: 10,
        pageSizes: this.GLOBAL.pageSizes
      },
      datas: [],
      selected: [],
      expands: [],
      subDatas: [],
      formInline: {
        orderNo: '',
        projectName: '',
        orderStatus: '',
        productNo: '',
        productName: ''
      },
      orderQtyTotal: 0,
      outputTotal: 0,
      isShow: false,
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
    this.list()
  },
  methods: {
    sortChange(column) {

      //获取字段名称和排序类型
      let fieldName = column.prop;
      let sortingType = column.order;

      // console.log("fieldName: ",fieldName);
      // console.log("sortingType: ",sortingType);
      this.formInline.sortField = fieldName;
      this.formInline.sortType = sortingType
      this.list();
    },
    remoteMethod(query) {
      if (query !== '') {
        this.loading = true;
        //根据query的值去远程检索数据
        orderMove.searchRemoteByQuery(query).then(data => {
          this.orderDatas = data.entity;
        }).catch(err => {
          console.error("err", err);
        })
        setTimeout(() => {
          this.loading = false;
        }, 200);

      } else {
        this.orderDatas = [];
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
    showSubData(row, expandedRows) {
      let id = row.id;
      //console.log("id :" ,id)
      if (this.expands.some((e) => e == row.id)) {
        this.expands = []

      } else {
        this.expands = []
        this.expands.push(row.id)
      }
      orderTrack.getOrderTrackDetailsById(id).then(data => {
        this.subDatas = data.entity;
        if (data.entity[0].buildCode) {
          this.isShow = true;
        } else {
          this.isShow = false;
        }
      })

    },
    list() {
      this.$store.commit('startLoading')
      this.formInline.pageInfo = this.pageInfo;
      this.orderQtyTotal = 0;
      this.outputTotal = 0;
      orderTrack.findAllByFormInline(this.formInline).then(data => {
        this.datas = data.entity.list;
        this.pageInfo.total = data.entity.total;
        for (let i = 0; i < this.datas.length; i++) {
          this.orderQtyTotal += this.datas[i].orderQty;
          this.outputTotal += this.datas[i].output;
        }
        this.$store.commit('endLoading')
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
.status_color1 {
  font-weight: bolder;
  color: #409eff;
}

.status_color2 {
  font-weight: bolder;
  color: #67c23a;
}
</style>