<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form
              :inline="true"
              :model="condition"
              size="mini"
            >
              <el-form-item>
                <el-input
                        v-model="condition.woNo"
                        placeholder="生产单号"
                        style="width: 160px"
                >
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-select
                  v-model="condition.projectId"
                  placeholder="项目"
                  collapse-tags
                  clearable
                  @change="bindBuildData(condition.projectId)"
                >
                  <el-option
                    :key="item.id"
                    :value="item.id"
                    v-for="item in projectListForOrder"
                    :label="item.projectName"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-select
                  v-model="condition.buildId"
                  placeholder="楼栋"
                  collapse-tags
                  @change="bindFloorData(condition.buildId)"
                  clearable
                >
                  <el-option
                    :key="item.id"
                    :value="item.id"
                    v-for="item in buildCodeArr"
                    :label="item.buildCode"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-select
                  v-model="condition.floorId"
                  placeholder="楼层"
                  collapse-tags
                  clearable
                >
                  <el-option
                    :key="item.id"
                    :value="item.id"
                    v-for="item in floorNumArr"
                    :label="item.floorNum"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-select
                  v-model="condition.woStatus"
                  placeholder="构件状态"
                  collapse-tags
                  clearable
                >
                  <el-option
                    label="待执行"
                    value="待执行"
                  ></el-option>
                  <el-option
                    label="执行中"
                    value="执行中"
                  ></el-option>
                  <el-option
                    label="已完成"
                    value="已完成"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-select
                  v-model="condition.typeId"
                  placeholder="构件类型"
                  clearable
                >
                  <el-option
                    :key="item.id"
                    :value="item.id"
                    v-for="item in productTypes"
                    :label="item.name"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-select
                  v-model="condition.selectType"
                  placeholder="类型"
                  style="width: 70px"
                  collapse-tags
                  clearable
                >
                  <el-option
                    label="模糊"
                    value="模糊"
                  >模糊</el-option>
                  <el-option
                    label="精确"
                    value="精确"
                  >精确</el-option>
                </el-select>
                <el-input
                  v-model="condition.productName"
                  placeholder="构件名称"
                  style="width: 60%"
                ></el-input>
              </el-form-item>
              <!--<el-form-item>
                <el-input
                        v-model="condition.teamName"
                        placeholder="班组"
                        style="width: 160px"
                >
                </el-input>
              </el-form-item>-->
              <el-form-item>
                <el-select
                        v-model="condition.teamId"
                        style="width:150px"
                        placeholder="班组"
                        clearable
                >
                  <el-option
                          v-for="item in this.teamList"
                          :key="item.id"
                          :label="item.teamName"
                          :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
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
          @expand-change="showSubData"
          size="mini"
          stripe
          border
          :default-sort="{prop: 'dateCreated', order: 'descending'}"
          :expand-row-keys="expands"
          row-key="id"
          style="width: 100%"
          @selection-change="handleSelectionChangeOrder"
          height="100%"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
        >
          <el-table-column type="selection">
          </el-table-column>
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-table
                :data="subDatas"
                size="mini"
                stripe
                border
                style="width: 100%"
              >
                <el-table-column
                  prop="order.orderNo"
                  label="订单编号"
                >
                </el-table-column>
                <el-table-column
                  prop="project.projectName"
                  label="项目"
                >
                </el-table-column>
                <el-table-column
                  prop="building.buildCode"
                  label="楼栋"
                >
                </el-table-column>
                <el-table-column
                  prop="floor.floorNum"
                  label="楼层"
                >
                </el-table-column>
                 <el-table-column
                  prop="product.productName"
                  label="构件名称"
                >
                </el-table-column>
                <el-table-column
                  prop="productType.name"
                  label="构件类型"
                >
                </el-table-column>
                <el-table-column
                  prop="product.productNo"
                  label="构件编码"
                >
                </el-table-column>
                <el-table-column
                  prop="productDetail.detailNo"
                  label="明细编码"
                >
                </el-table-column>
                <el-table-column
                  prop="productDetail.productStatus"
                  label="构件状态"
                >
                </el-table-column>
                <el-table-column
                  prop="product.productWt"
                  label="重量"
                >
                </el-table-column>
                <el-table-column
                  prop="product.productVol"
                  label="体积"
                >
                </el-table-column>
                <el-table-column
                  prop="product.projectConcrete"
                  label="方量"
                >
                </el-table-column>

                <el-table-column
                  prop="order.planDateFrom"
                  :formatter="formatterDate"
                  label="开始时间"
                >
                </el-table-column>
                <el-table-column
                  prop="order.planDateTo"
                  :formatter="formatterDate"
                  label="结束时间"
                >
                </el-table-column>
                <el-table-column
                align="left"
                  fixed="right"
                        label="是否逾期">
                  <template slot-scope="scope">
                    <el-popover trigger="hover" placement="top">
                      <p>是否逾期</p>
                      <div slot="reference">
                        <el-tag size="medium" :class="scope.row.isOver ?'green' :'red' "> {{
                          scope.row.isOver ?'否' :'是' }}
                        </el-tag>
                      </div>
                    </el-popover>
                  </template>
                </el-table-column>
                <el-table-column
                  label="操作"
                  align="left"
                  fixed="right"
                >
                  <template slot-scope="scope">
                    <el-tooltip
                      class="item"
                      effect="light"
                      content="撤销"
                      placement="top"
                    >
                      <el-button
                        type="danger"
                        icon="el-icon-minus"
                        @click="cancelDPS(scope.row)"
                      ></el-button>
                    </el-tooltip>
                  </template>
                </el-table-column>
              </el-table>
            </template>
          </el-table-column>
          <el-table-column
            type="index"
            label="序号" sortable
          >
          </el-table-column>
          <el-table-column
            prop="woNo"
            label="生产单号"
            sortable
          >
               <template slot-scope="scope">
                <div class="active-link" @click="editClicks(scope.row)">
                  {{scope.row.woNo}}
                </div>
              </template>
          </el-table-column>
          <el-table-column
            prop="woDate"
            label="生产时间"
            sortable
            :formatter="formatterDate"
          >
          </el-table-column>
          <el-table-column
                  prop="projectName"
                  label="项目" sortable
          >
          </el-table-column>
          <el-table-column
            prop="teamName"
            label="班组" sortable
          >
          </el-table-column>
          <el-table-column
            prop="totalQty"
            label="构件数" sortable
          >
          </el-table-column>
          <el-table-column
            prop="woStatus"
            label="状态" sortable
          >
            <template slot-scope="scope">
              <span :class="{status_color1: scope.row.woStatus == '执行中' , status_color2 : scope.row.woStatus == '已完成'}">{{scope.row.woStatus}}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="jhjd"
            label="计划进度"
            width="120" sortable
          >
            <template slot-scope="scope">
              <el-progress
                :text-inside="true"
                :stroke-width="18"
                :percentage="scope.row.jhjd"
                :color="customColorMethod"
              ></el-progress>
            </template>
          </el-table-column>
          <!-- <el-table-column
                    prop="totalConcrete"
                    label="总砼方量(m³)">
            </el-table-column> -->
          <el-table-column
            prop="createdByName"
            label="创建人" sortable
          >
          </el-table-column>
          <!-- <el-table-column
                    prop="dateCreated"
                    :formatter="formatterDate"
                    label="创建时间">
            </el-table-column> -->
          <el-table-column
            label="操作"
            :width="rowButtonGroupWidth(rowButtonData)"
            align="left"
            fixed="right"
          >
            <template slot-scope="scope">
              <my-row-button-group
                :row="scope.row"
                :model="rowButtonData"
              ></my-row-button-group>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer ref="footOperationBar">
        <el-row>
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="condition.pageInfo.currentPage"
            :page-sizes="condition.pageInfo.pageSizes"
            :page-size="condition.pageInfo.pageSize"
            :total="condition.pageInfo.total"
            layout="total, sizes, prev, pager, next, jumper"
            background
          >
          </el-pagination>
        </el-row>
      </el-footer>
    </el-container>
    <el-dialog  title="日计划详情"
                v-if="editFormVisible"
                :visible.sync="editFormVisible"
                width="90%" append-to-body>
        <order-work-detail :detailId="detailId"></order-work-detail>
    </el-dialog>
  </div>
</template>

<script>
import workOrder from '../../../model/po/workOrder'
import orderModel from '../../../model/po/order'
import moment from "moment"
import serviceConfig from '../../../../server.config.js'
import orderWorkDetail from './OrderWorkDetail.vue'
import menuTabService from '../../../service/menuTabService'
import { Notification } from 'element-ui'
import projectModel from "../../../model/project/project";
export default {
  components:{
    'order-work-detail':orderWorkDetail
  },
  data() {
    return {
      teamList: [],
      datas: [],
      selected: [],
      selectedOrder: [],
      expands: [],
      subDatas: [],
      editFormVisible:false,
      condition: {
        selectType: '模糊',
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: 20,
          pageSizes: this.GLOBAL.pageSizes
        },
      },
      buildCodeArr: [],//楼栋数据
      floorNumArr: [],
      projectListForOrder: [],//项目list
      currentProjectID: '',
      productTypes: [],
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        },
        {
          name: '导出',
          event: 'exportClick',
          authorized:'workOrder:exportClick'
        }
      ],
      searchButtonData: [
        {
          name: '查询',
          event: "submit"
        }
      ],
      rowButtonData: [
        {
          name: '编辑',
          event: 'editClick',
        },
        {
          name: '撤销',
          event: 'deleteClick',
          authorized: 'workOrder:cancel'
        },
      ]
    }
  },
  mounted() {
    this.list();
    this.getTeamInfoList();
    orderModel.findAllProjectList().then(data => {
      this.projectListForOrder = data.entity;
    }).catch(() => {

    })
    orderModel.findAllProductTypes().then(data => {
      this.productTypes = data.entity;
    }).catch(() => {

    })
  },
  computed: {
  },
  methods: {
    getTeamInfoList() {
      projectModel.getTeamByOrg().then(data => {
        this.teamList = data.entity;
      });
    },
    authorized(index) {
        return menuTabService.authorized(this.$store, index)
    },
    customColorMethod(percentage) {
      if (percentage <= 99) {
        return '#409eff';
      } else {
        return '#67c23a';
      }
    },
    handleSelectionChangeOrder(selected) {
      this.selectedOrder = selected
    },
    exportClick() {
      if (this.selectedOrder.length === 0) {
        this.$message('请选择导出计划项');
        return;
      }
      let ids = []
      this.selectedOrder.forEach((row) => {
        ids.push(row.id)
      })
      this.$store.commit('startLoading')

      let url = serviceConfig.baseURL + '/po/order/exportExitDetailByDay'
      let prams = { "ids": ids }
      let temp = document.createElement("form");
      temp.action = url;
      temp.method = "post";
      temp.style.display = "none";
      for (let x in prams) {
        let opt = document.createElement("textarea");
        opt.name = x;
        opt.value = prams[x];
        temp.appendChild(opt);
      }
      document.body.appendChild(temp);
      temp.submit();
      return temp;
    },
    submit() {
      this.list();
    },
    bindFloorData(buildCode) {

      if (!buildCode) {
        return;
      }
      orderModel.getFloorNumArr(this.currentProjectID, buildCode).then(data => {
        this.floorNumArr = data.entity;
      }).catch(err => {
        //ignore
      })
    },
    bindBuildData(projectId) {
      if (!projectId) {
        return;
      }
      this.currentProjectID = projectId;
      orderModel.getBuildCodeArr(projectId).then(data => {
        this.buildCodeArr = data.entity;
      })
      this.list();

    },
    cancelDPS(row) {
      let id = row.productDetail.id;
      this.$confirm('确认撤销?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        workOrder.cancelDPS(id).then(data => {
          if (data.status == 'success') {
            Notification({
              message: this.GLOBAL.MSG.UPDATE_SUCCESS,
              type: "success"
            })
          } else {
            Notification({
              message: this.GLOBAL.MSG.UPDATE_ERR,
              type: "success"
            })
          }
          this.list();
          this.expands = []
        }).catch(err => {
          console.error(err);
        })
      }).catch(() => {

      })
    },
    deleteClick(row) {
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        workOrder.delete(row.id).then(data => {
          if (data.status == 'success') {
            Notification({
              message: this.GLOBAL.MSG.UPDATE_SUCCESS,
              type: "success"
            })
          } else {
            Notification({
              message: data.msg,
              type: "warning"
            })
          }
          this.list();
        }).catch(err => {
          console.error(err);
        })
      }).catch(() => {

      })
    },
    editClick(row) {
      this.$router.push(
        {
          name: 'workOrderEdit',
          params: {
            id: row.id,
          }
        }
      );

    },
      editClicks(row){
          this.detailId = row.id
          this.editFormVisible = true
      },

    addClick() {
      this.$router.push( {name: 'workOrderEdit' ,} );
    },
    formatterDate(row, column, cellValue, index) {
      var date = cellValue;
      if (date == undefined || date == "") {
        return "";
      }
      return moment(date).format("YYYY-MM-DD");
    },
    showSubData(row, expandedRows) {
      let id = row.id;
      if (this.expands.some((e) => e == row.id)) {
        this.expands = []
      } else {
        this.expands = []
        this.expands.push(row.id)
      }
      workOrder.findSubDataById(id).then(data => {
        data.entity.forEach(val=>{
          val.product.projectConcrete = Number(val.product.projectConcrete).toFixed(3)
        })
        console.log(" data.entity", data.entity)
        this.subDatas = data.entity;
      })

    },
    list() {
      console.log("this.condition", this.condition);
      workOrder.list(this.condition).then(data => {
        this.datas = data.entity.content;
        this.condition.pageInfo.total = data.entity.totalElements;
      })
    },
    handleSizeChange(val) {
      this.condition.pageInfo.pageSize = val;
      this.list()
    },
    handleCurrentChange(val) {
      this.condition.pageInfo.currentPage = val;
      this.list()
    },
    handleSelectionChange(selected) {
      this.selected = selected
    },
  }
}
</script>

<style lang="scss" scoped>
.status_color1 {
  font-weight: bolder;
  color: #409eff;
}

.status_color2 {
  font-weight: bolder;
  color: #67c23a;
}

.active-link{
  color:#409eff;
  text-decoration:underline;
  &:hover{
    color:blue;
    cursor:pointer;
  }
}
.red {
  color: red;
}

.green {
  color: green;
}
</style>
