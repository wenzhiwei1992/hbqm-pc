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
                  v-model="formInline.lineName"
                  placeholder="产线"
                  collapse-tags
                  clearable
                  @change="onSubmit"
                >
                  <el-option
                    v-for="pd in prodlines"
                    :value="pd.id"
                    :label="pd.lineName"
                    :key="pd.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="formInline.currentProcess"
                  placeholder="工序"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="formInline.plateNo"
                  placeholder="模台号"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-select
                  v-model="formInline.plateStatus"
                  placeholder="模台状态"
                  collapse-tags
                  clearable
                >
                  <el-option
                    label="空闲"
                    value="空闲"
                  ></el-option>
                  <el-option
                    label="占用"
                    value="占用"
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
          @selection-change="handleSelectionChange"
          @select-all="noticeUserAndCallMps"
          :row-class-name="tableRowClassName2"
          @filter-change="filterAll"
          size="mini"
          stripe
          border
          ref="handleClickRowSelection"
          style="width: 100%"
          height="100%"
          @row-click="handleClickRowSelection"
        >
          <el-table-column
            type="selection"
            :selectable='isDisabled'
            width="40"
          >
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            width="60px"
          >
          </el-table-column>
          <el-table-column
            prop="lineName"
            label="产线"
            width="160px"
          >
          </el-table-column>
          <el-table-column
            prop="plateNo"
            label="模台号"
            width="100px"
          >
          </el-table-column>
          <el-table-column
            label="模台状态"
            width="100px"
          >
            <template slot-scope="scope">
              <span
                style="margin-left: 10px"
                :class="scope.row.plateStatus == '占用' ? 'plate-green':''"
              >{{ scope.row.plateStatus }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="isMps"
            label="是否执行生产计划"
            column-key="isMps"
            :filters="isMpsDatas"
            filter-placement="bottom-end"
            :filter-method="filterMps"
            width="160px"
          >
          </el-table-column>
          <el-table-column
            prop="productsForM2"
            label="组模方案"
            :show-overflow-tooltip="true"
          >
            <template slot-scope="scope">
              <el-tooltip content="系统自动记忆上一次编辑的构件">
                <div slot>{{scope.row.productsForM2}}</div>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column
            label="构件明细"
            width="90px"
          >
            <template slot-scope="scope">
              <el-popover
                trigger="click"
                placement="top-end"
                :visible-arrow="false"
              >
                <el-table :data="scope.row.productsSplit">
                  <el-table-column
                    width="280px"
                    property="productName"
                    label="已计划排产构件"
                    sortable
                  ></el-table-column>
                </el-table>
                <div
                  slot="reference"
                  class="name-wrapper"
                >
                  <el-button
                    icon="el-icon-search"
                    round
                  ></el-button>
                </div>
              </el-popover>
            </template>
          </el-table-column>

          <el-table-column
            label="操作"
            fixed="right"
            :width="rowButtonGroupWidth(rowButtonData)"
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

    <!--计划排产弹框-->
    <el-dialog
      :visible.sync="planFormVisible"
      width="60%"
    >
      <plan-edit2
        :plan-form="planForm"
        :team-infos="teamInfos"
        :items="items"
        @planForm-finish="planFormFinish"
        @planForm-cancel="planFormCancel"
      >

      </plan-edit2>

    </el-dialog>

    <!--批量计划排产弹框-->
    <el-dialog
      :visible.sync="oneKeyPlanFormVisible"
      width="80%"
    >
      <plan-edit-onekey
        :team-infos="teamInfos"
        :allPCProducts="allPCProducts"
        :planForm="planForm"
        @planForm-finish="planFormFinish"
        @planForm-cancel="planFormCancel"
      >

      </plan-edit-onekey>

    </el-dialog>

    <el-dialog
      :title="mode==='add'?'新建':'编辑'"
      :visible.sync="editFormVisible"
      width="70%"
      :inline="true"
    >
      <el-form
        :model="editForm"
        v-if="editForm"
        :rules="eidtFormRules"
        ref="editForm"
        label-position="left"
      >
        <div class="form1_class">
          <el-form-item>
            <el-col :span="6">
              <el-form-item
                label="模台编码"
                prop="plateNo"
              >
                <el-input
                  style="width:140px"
                  v-model="editForm.plateNo"
                  disabled="disabled"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item
                label="是/否执行生产计划"
                prop="isMps"
              >
                <el-select
                  style="width:140px"
                  v-model="editFormSearch.isMps"
                  size="mini"
                  @change="reloadDetailInfo"
                >
                  <el-option
                    label="是"
                    value="是"
                  ></el-option>
                  <el-option
                    label="否"
                    value="否"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="构件类型">
                <el-select
                  style="width:140px;"
                  v-model="editFormSearch.typeId"
                  filterable
                  clearable
                  remote
                  reserve-keyword
                  placeholder="输入构件类型自动检索"
                  @change="editFormSearchMethodChange"
                  :remote-method="remoteMethod"
                  :loading="loading"
                >
                  <el-option
                    v-for="item in types"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>

            </el-col>
          </el-form-item>
        </div>

        <el-form-item class="form2_class">
          <el-col :span="6">
            <el-form-item
              label="构件编码"
              prop="productNo"
            >
              <el-input
                style="width:140px"
                v-model="editFormSearch.productNo"
                placeholder="构件编码"
              ></el-input>

            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="构件名称"
              prop="productName"
            >
              <el-input
                style="width:140px;margin-left:60px"
                v-model="editFormSearch.productName"
                placeholder="构件名称"
              ></el-input>
            </el-form-item>

          </el-col>
          <el-col :span="6">
            <el-form-item
              label="订单编号"
              prop="orderNo"
            >
              <el-input
                style="width:140px"
                v-model="editFormSearch.orderNo"
                placeholder="订单编号"
              ></el-input>
            </el-form-item>

          </el-col>
        </el-form-item>
        <el-form-item class="form3_class">
          <el-col :span="6">
            <el-form-item
              label="项目名称"
              prop="projectName"
            >
              <el-input
                style="width:140px"
                v-model="editFormSearch.projectName"
                placeholder="项目名称"
              ></el-input>

            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="开始时间"
              prop="planDateFrom"
            >
              <el-date-picker
                v-model="editFormSearch.planDateFrom"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
                style="width:140px;margin-left:60px"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item
              label="结束时间"
              prop="planDateTo"
            >
              <el-date-picker
                v-model="editFormSearch.planDateTo"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
                style="width:140px"
              >
              </el-date-picker>
            </el-form-item>
            <!-- <el-form-item label="创建人  " prop="createdByName">
                            <el-input style="width:140px;margin-left:15px" v-model="editFormSearch.createdByName"
                                      placeholder="创建人"></el-input>
                        </el-form-item> -->
          </el-col>
          <el-col :span="2">
            <el-form-item>
              <el-button
                type="primary"
                @click="editFormSearchMethod"
                style="margin-left: -35%;"
              >查询
              </el-button>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item
          prop="productsForM2"
          class="form3_class"
        >
          <el-col :span="24">
            <el-form-item label="组模方案">
              <el-select
                @remove-tag="removeTag"
                v-model="editForm.productsForM2"
                placeholder="双击构件添加"
                multiple
                style="width: 73%"
              >

              </el-select>
              <el-button
                type="primary"
                @click="clearTags"
              >清空</el-button>
            </el-form-item>

          </el-col>

        </el-form-item>
        <el-form-item>
          <el-table
            :data="productData"
            @row-dblclick="pushProductData"
            size="mini"
            stripe
            border
            style="width: 100%"
          >
            <el-table-column
              prop="planDateFrom"
              label="开始日期"
              v-if="dateShow"
              :formatter="formatterDate"
            >
            </el-table-column>
            <el-table-column
              prop="planDateTo"
              label="结束日期"
              v-if="dateShow"
              :formatter="formatterDate"
            >
            </el-table-column>
            <el-table-column
              prop="productNo"
              label="构件编码"
              width="120"
            >
            </el-table-column>
            <el-table-column
              prop="productName"
              label="构件名称"
              width="120"
            >
            </el-table-column>
            <el-table-column
              prop="typwpeId"
              label="构件类型"
            >
            </el-table-column>

            <el-table-column label="排产数量">
              <template slot-scope="scope">
                <div class="el-input el-input--mini">
                  <input
                    class="el-input__inner"
                    v-model="scope.row.pcNum"
                    :max="scope.row.allowPcNum"
                    type="text"
                    :min="1"
                    onkeyup="value=value.replace(/[^\d]/g,'')"
                  > </input>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              prop="allowPcNum"
              label="剩余生产量"
            >
            </el-table-column>

            <el-table-column
              prop="productLen"
              label="长"
            >
            </el-table-column>
            <el-table-column
              prop="productHeight"
              label="高"
            >
            </el-table-column>
            <el-table-column
              prop="productVol"
              label="面积"
            >
            </el-table-column>

          </el-table>
          <el-row
            type="flex"
            justify="space-around"
            align="middle"
            style="margin-top:15px"
          >
            <el-col>
              <el-row
                type="flex"
                justify="end"
                align="middle"
              >
                <el-pagination
                  @size-change="handleSizeChange2"
                  @current-change="handleCurrentChange2"
                  :current-page="pageInfo2.currentPage"
                  :page-sizes="pageInfo2.pageSizes"
                  :page-size="pageInfo2.pageSize"
                  :total="pageInfo2.total"
                  layout="total, sizes, prev, pager, next, jumper"
                  background
                >
                </el-pagination>
              </el-row>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="editFormVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="editFormSubmmit('editForm')"
        >确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import houseTypeModel from '../../../model/project/houseType'
import combModel from '../../../model/po/comb'
import pathModel from '../../../model/prodline/path'
import PlanEdit2 from './PlanEdit2.vue'
import PlanEditForOneKey from './PlanEditForOneKey.vue'
import { Notification } from 'element-ui'
import moment from "moment";

export default {
  components: {
    'plan-edit2': PlanEdit2,
    'plan-edit-onekey': PlanEditForOneKey
  },
  data() {
    return {
      loading: false,
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: 20,
        pageSizes: this.GLOBAL.pageSizes
      },
      pageInfo2: {
        currentPage: 1,
        total: 0,
        pageSize: 20,
        pageSizes: this.GLOBAL.pageSizes
      },
      isMpsDatas: [{ text: '是', value: '是' }, { text: '否', value: '否' }],
      datas: [],
      selected: [],
      mode: '',
      editFormVisible: false,
      editForm: null,
      eidtFormRules: {},
      productData: [],
      planFormVisible: false,
      planForm: null,
      oneKeyPlanFormVisible: false,
      items: [],
      allPCProducts: [],
      formInline: {},
      teamInfos: [],
      prodlines: [],
      currentLineId: '',
      currentPlate: {},
      currPlatePlanId: '',
      houseTypes: [],
      types: [],
      editFormSearch: {
        productNo: '',
        productName: '',
        date: '',
        houseType: '',
        isMps: '是'
      },
      dateShow: true,
      buttonData: [
        {
          name: '批量排产',
          event: 'deviceMpsOrNoMps'
        }
      ],
      searchButtonData: [
        {
          name: '查询',
          event: "onSubmit"
        }
      ],
      rowButtonData: [
        {
          name: '编辑',
          event: 'editClick',
        },
        {
          name: '创建计划',
          event: 'planClick',
        }
      ]
    }
  },
  mounted() {
    this.showHouseType()
    //加载班组信息
    combModel.getAllTeamInfos().then(data => {
      this.teamInfos = data.entity;
    });
    this.pageableList();
  },
  methods: {
    remoteMethod(query) {
      if (query !== '') {
        this.loading = true;
        //根据query的值去远程检索数据
        combModel.findAllProductTypesByQuery(query).then(data => {
          this.types = data.entity;
        }).catch(err => {
        })
        setTimeout(() => {
          this.loading = false;
        }, 200);

      } else {
        this.types = [];
      }
    },
    /**
     * 全部检索，未实现 TODO
     */
    filterAll() {

    },
    /**
     * 是否执行生产计划表格筛选方法
     */
    filterMps(value, row) {

      this.formInline.isMps = value;
      return row.isMps === value;

    },
    /**
     * 全选提示用户
     */
    noticeUserAndCallMps(selection) {
      // if(this.formInline.isMps == '是' || this.formInline.isMps =='否'){
      //     this.deviceMpsOrNoMps(this.formInline.isMps);
      // }else{
      //     Notification({
      //         type:"info",
      //         message:'请先选择并查询 < 是否执行生产计划 > '
      //     })
      //     this.selected = [];
      //     this.$refs.table.clearSelection();
      // }

    },
    /**
     * 自动选择 批量或者非批量排产
     */
    deviceMpsOrNoMps() {
      if (this.selected.length == 0) {
        this.$message('请勾选模台');
        return;
      }
      let ids = [];
      this.selected.forEach((row) => {
        ids.push(row.id)
      });
      if (this.selected[0].isMps == '是') {
        this.oneKeyPlanEdit(ids);
      } else {
        this.oneKeyPlanEditNoMps(ids);
      }
    },
    /**
     * 非MPS的构件批量排产
     */
    oneKeyPlanEditNoMps(ids) {
      const loading = this.$loading({
        lock: true,
        text: '汇总计算模台排产数据，请耐心等待...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      this.planForm = {
        predictDate: new Date(),
        isMps: '否'
      }
      combModel.loadAllForOneKeyPlanNoMps(ids).then(data => {
        if (data.status == 'success') {
          this.allPCProducts = data.entity;
          this.allPCProducts.forEach(e => {
            e.pcNum = e.prevPcNum;
          })
        } else {
          Notification({
            message: data.msg,
            type: 'warning'
          })
        }
        loading.close()
        this.oneKeyPlanFormVisible = true;
      }).catch(err => {
        console.error("err", err)
        loading.close()
      })
    },
    /**
     * 根据选择字段重新加载子信息
     */
    reloadDetailInfo() {
      if (this.editFormSearch.isMps == '否') {
        this.dateShow = false;
      } else {
        this.dateShow = true;
      }
      this.editFormSearchMethodChange();
    },
    /**
     * 批量排产
     *  搜集所有系统记忆构件，计算剩余生产量，显示到列表，点击创建计划全部排产
     */
    oneKeyPlanEdit(ids) {
      const loading = this.$loading({
        lock: true,
        text: '汇总计算模台排产数据，请耐心等待...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      this.planForm = {
        predictDate: new Date(),
        isMps: '是'
      }
      combModel.loadAllForOneKeyPlanByIds(ids).then(data => {
        if (data.status == 'success') {
          this.allPCProducts = data.entity;
          this.allPCProducts.forEach(e => {
            e.pcNum = e.prevPcNum;

          })
        } else {
          Notification({
            message: data.msg,
            type: 'warning'
          })
        }

        loading.close()
        this.oneKeyPlanFormVisible = true;
      }).catch(err => {
        console.error("err", err)
        loading.close()
      })


    },
    editFormSearchMethod() {
      this.editFormSearchMethodChange();
    },
    clearTags() {
      this.editForm.productsForM2 = [];
      this.editForm.productIdsForM2 = [];
      this.editFormSearchMethod();
    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD");
    },
    onSubmit() {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      this.formInline.pageInfo = this.pageInfo;
      combModel.findAllByFormInline(this.formInline).then(data => {
        this.datas = data.entity.content;
        this.pageInfo.total = data.entity.totalElements;
        loading.close()
      })
    },
    removeTag(val) {
      //console.log("val",val);
      let index = -1;
      let length = this.editForm.productIdsForM2.length;
      for (let i = 0; i < length; i++) {
        let item = this.editForm.productIdsForM2[i];
        //console.log("this.editForm.productIdsForM2[i]",item)
        let obj = JSON.parse(item);
        if (obj.productName == val) {
          index = i;
          break;
        }
      }
      //console.log("index",index)
      this.editForm.productIdsForM2.splice(index, 1);
      //console.log("this.editForm.productIdsForM2",this.editForm.productIdsForM2);
    },
    planFormFinish() {
      this.planFormVisible = false;
      this.oneKeyPlanFormVisible = false;
      this.pageableList();
    },
    planFormCancel() {
      this.planFormVisible = false;
      this.oneKeyPlanFormVisible = false;
    },
    pushProductData(row, event, column) {
      if (!row.pcNum) {
        this.$message(this.GLOBAL.PLATE_PLAN_MSG.PC_NUM_IS_NULL);
        return;
      }
      if (parseInt(row.pcNum) > parseInt(row.allowPcNum)) {
        this.$message(this.GLOBAL.PLATE_PLAN_MSG.PC_NUM_GT_ALLOW_NUM);
        return;
      }
      let isRepeat = false;
      let length = this.editForm.productIdsForM2.length;
      for (let i = 0; i < length; i++) {
        let item = this.editForm.productIdsForM2[i];

        let obj = JSON.parse(item);
        if (obj.productName == row.productName) {
          isRepeat = true;
          break;
        }
      }
      if (!isRepeat) {
        // if (this.editForm.productsForM2.indexOf(row.productName) === -1) {
        let obj = {
          productName: '', //构件名称
          productId: '', //构件ID
        }
        obj.productId = row.productId
        obj.productName = row.productName;
        let name = "【" + row.productName + "x" + row.pcNum + "】";
        this.editForm.productsForM2.push(name);
        //从列表中删除一行记录
        let index = this.productData.indexOf(row);
        this.productData.splice(index, 1);
        this.pageInfo2.total -= 1;
      } else {
        this.$message.warning("该构件已添加过")
        return
      }
      //给每一行添加模台号
      row.plateId = this.currentPlate.plateId;
      row.plateNo = this.currentPlate.plateNo;
      row.lineId = this.currentLineId
      row.platePlanId = this.currPlatePlanId;
      var r = JSON.stringify(row);
      //保存构件信息的实际数据
      this.editForm.productIdsForM2.push(r);
    },
    /**
     * 非MPS直接排产。
     *  如果需要记录上一次的排产构件，需要把加载逻辑改为读取 当前模台上的字段 productIdsForM2
     * @param productIds
     * @param row
     */
    planClickByNoMps(productIds, row) {
      let ids = row.productIdsForM2;
      let dd = JSON.parse(ids);
      //当前模台上构件信息
      let productInfos = row.productsForM2;
      let productMap = new Map();
      //循环放入到map中作为记忆
      for (let j = 0; j < dd.length; j++) {
        productMap.set(dd[j].productName, dd[j].pcNum);
      }
      combModel.loadPlanDetailByProductIdsAndNoMps(productIds).then(data => {
        let infos = data.entity;
        for (let i = 0; i < infos.length; i++) {
          let temp = {
            index: 0,
            productId: '',
            productNo: '',
            productName: '',
            plateId: '',
            platePlanId: row.id
          };
          temp.index = i;
          temp.productName = infos[i].productName;
          temp.productNo = infos[i].productNo;
          if (productMap.get(temp.productName)) {
            temp.pcNum = productMap.get(temp.productName);
          } else {
            temp.pcNum = 1;
          }
          temp.allowPcNum = infos[i].allowPcNum;
          temp.productId = infos[i].productId;
          temp.plateId = row.plateId;
          temp.platePlanId = row.id;
          this.planForm.customData.push(temp);
        }
      }).catch(err => {

      })

      this.planForm.lineName = row.lineName;
      this.planForm.lineId = row.lineId;
      this.planForm.plateId = row.plateId;
      this.planForm.plateNo = row.plateNo;
      this.planForm.num = 1;
      this.planForm.isMps = '否'
      this.planFormVisible = true;
    },
    planClick: function (row) {
      // if (row.plateStatus === '占用') {
      //     Notification({
      //         message: '该模台已占用',
      //         type: 'info'
      //     })
      //     return;
      // }

      let productNames = row.productsForM2;
      //组模计划
      let ids = row.productIdsForM2;
      if (ids.length == 0) {
        ids = [];
        this.$message.warning('请先编辑组模方案');
        return
      }


      let dd = JSON.parse(ids);

      //当前模台上构件信息
      let productInfos = row.productsForM2;
      let productMap = new Map();
      //循环放入到map中作为记忆
      for (let j = 0; j < dd.length; j++) {
        productMap.set(dd[j].productName, dd[j].pcNum);
      }


      this.items = [];
      this.planForm = {
        platePlanId: row.id,
        period: '1',//生产周期
        predictDate: new Date(), //计划日期
        lineId: '',
        lineName: '',
        customData: [],
      };

      let productIds = [];
      for (let i = 0; i < dd.length; i++) {
        productIds.push(dd[i].productId);
      }
      if (row.isMps == '否') {
        this.planClickByNoMps(productIds, row);
        return;
      }
      // 如果需要记录上一次的排产构件，需要把加载逻辑改为读取 当前模台上的字段 productIdsForM2
      combModel.updateAllowPCNum(productIds, row.lineId).then(data => {
        let infos = data.entity;
        for (let i = 0; i < infos.length; i++) {
          let temp = {
            index: 0,
            productId: '',
            productNo: '',
            productName: '',
            buildCode: '',
            floorNum: '',
            floorRelevanceId: '',
            plateId: '',
            platePlanId: row.id
          };
          temp.index = i;
          temp.productName = infos[i].productName;
          temp.productNo = infos[i].productNo;
          if (productMap.get(temp.productName)) {
            temp.pcNum = productMap.get(temp.productName);
          } else {
            temp.pcNum = 1;
          }
          temp.allowPcNum = infos[i].allowPcNum;
          temp.productId = infos[i].productId;
          //temp.floorRelevanceId = infos[i].floorRelevanceId;
          temp.plateId = row.plateId;
          temp.platePlanId = row.id;
          this.items.push(temp);

          this.planForm.customData.push(temp);
        }
      })

      this.planForm.lineName = row.lineName;
      this.planForm.lineId = row.lineId;
      this.planForm.plateId = row.plateId;
      this.planForm.plateNo = row.plateNo;
      this.planForm.num = 1;
      this.planFormVisible = true;
    },
    pageableList() {
      this.$store.commit('startLoading');
      //加载查询条件，所有产线集合
      pathModel.getAllProdLines().then(data => {
        this.prodlines = data.entity;
      }).catch((err) => {
        console.log("load selectbox data err!", err)
      });

      this.formInline.pageInfo = this.pageInfo;
      combModel.findAllByFormInline(this.formInline).then(data => {
        this.datas = data.entity.content;
        this.pageInfo.total = data.entity.totalElements;
      })

    },
    editClick(row) {
      // if (row.plateStatus === '占用') {
      //     Notification({
      //         message: '该模台已占用',
      //         type: 'info'
      //     })
      //     return;
      // }
      //console.log("row",row)
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      let orderStatus = '提交';
      this.currentLineId = row.lineId;
      this.currentPlate = {
        plateId: row.plateId,
        plateNo: row.plateNo
      }
      this.currPlatePlanId = row.id;
      this.editFormSearch.isMps = '是'
      //加载待排产构件
      combModel.getEditProductsByProductType(orderStatus, row.lineId, this.pageInfo2).then(data => {
        data.entity.list.some(p => {
          if (p.pcNum == '' || p.pcNum == null || p.pcNum == undefined) {
            p.pcNum = 1
          }
        })
        this.productData = data.entity.list;
        this.pageInfo2.total = data.entity.totalCount;
        loading.close()
        combModel.getById(row.id).then(data => {
          this.mode = 'edit';
          this.editForm = data.entity;
          //console.log("this.editForm.productIdsForM2",this.editForm.productIdsForM2);
          try {
            if (this.editForm.productsForM2.length > 0) {
              this.editForm.productsForM2 = this.editForm.productsForM2.split(";");
              this.editForm.productsForM2.pop();//去除最后一个拆分的空字符串
            } else {
              this.editForm.productsForM2 = [];
            }
            if (this.editForm.productIdsForM2.length > 0) {
              let jsons = JSON.parse(data.entity.productIdsForM2);
              this.editForm.productIdsForM2 = [];
              for (let i = 0; i < jsons.length; i++) {
                let jsonObj = jsons[i];//已在待排产中的构件，如果需要其他模台也不显示，需要在构件明细里面加个"待排产"状态进行过滤
                let index = this.productData.indexOf(row);
                this.productData.splice(index, 1);
                this.pageInfo2.total -= 1;
                this.editForm.productIdsForM2.push(JSON.stringify(jsons[i]));
              }
            } else {
              this.editForm.productIdsForM2 = [];
            }

          } catch (error) {
            this.editForm.productIdsForM2 = [];
            this.editForm.productsForM2 = [];
            loading.close()
          }
          this.editFormVisible = true

        }).catch((data) => {
          console.error(data)
        })
      });


    },
    editFormSubmmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.mode === 'edit') {
            this.editForm.isMps = this.editFormSearch.isMps;
            combModel.updateForM2(this.editForm)
              .then((data) => {
                if (data.status === 'failed') {
                  Notification({
                    message: data.msg,
                    type: 'warning'
                  });
                  return
                }
                Notification({
                  message: '编辑成功',
                  type: 'success'

                });
                this.editFormVisible = false;
                this.pageableList();
              }).catch(() => {

              }
              )
          }
        } else {
          return false;
        }
      });
    },
    showHouseType() {
      houseTypeModel.getAllHouseTypes().then((data) => {
        this.houseTypes = data.entity
      })
    },
    handleSelectionChange(selected) {
      this.selected = selected
      if (this.selected.length == 0) {
        for (let i = 0; i < this.datas.length; i++) {
          this.datas[i].existing = 'false';
        }
      } else {
        let isMps = this.selected[0].isMps;
        for (let i = 0; i < this.datas.length; i++) {
          //true 不能勾选 ，不同类型的不能勾选 ,先全部设置为ture，符合匹配设置为false
          this.datas[i].existing = 'true';
          if (this.datas[i].isMps == isMps) {
            //flase 为可以勾选，相同类型的可以勾选
            this.datas[i].existing = 'false';
          }

        }
      }
    },
    handleSizeChange(val) {
      this.pageInfo.pageSize = val;
      this.pageableList()
    },
    handleCurrentChange(val) {
      this.pageInfo.currentPage = val;
      this.pageableList()
    },
    handleSizeChange2(val) {
      this.pageInfo2.pageSize = val;
      this.editFormSearchMethodChange();
    },
    handleCurrentChange2(val) {
      this.pageInfo2.currentPage = val;
      this.editFormSearchMethodChange();
    },
    editFormSearchMethodChange() {
      let condition = {}
      condition.orderStatus = '提交';
      condition.lineId = this.currentLineId;
      condition.pageInfo = this.pageInfo2;
      condition.productNo = this.editFormSearch.productNo.trim();
      condition.productName = this.editFormSearch.productName.trim();
      condition.planDateFrom = this.editFormSearch.planDateFrom;
      condition.planDateTo = this.editFormSearch.planDateTo;
      condition.houseType = this.editFormSearch.houseType;
      condition.isMps = this.editFormSearch.isMps;

      condition.projectName = this.editFormSearch.projectName;
      condition.orderNo = this.editFormSearch.orderNo;
      condition.createdByName = this.editFormSearch.createdByName;
      condition.typeId = this.editFormSearch.typeId;
      combModel.getEditProductsAndSearchCondition(condition).then(data => {
        data.entity.list.some(p => {
          if (p.pcNum == '' || p.pcNum == null || p.pcNum == undefined) {
            p.pcNum = 1
          }
        })
        this.productData = data.entity.list;
        this.pageInfo2.total = data.entity.totalCount;
      });
    },
    //选择了isMps或者MPS，相反的值会被禁用
    isDisabled(row, index) {
      if (row.existing === 'true') {
        return false
      } else {
        return true
      }
    },
    tableRowClassName2(scope, index) {
      if (scope.row.existing === 'true') {
        return 'stopclick'
      } else {
        return ''
      }
    },

  },


}

</script>
<style>
.plate-green {
  color: #0fe436;
  font-weight: bold;
}

.form1_class {
}

.form2_class {
  margin-top: -18px;
}

.form3_class {
  margin-top: -18px;
}

tbody tr.stopclick {
  color: rgba(0, 0, 0, 0.2);
  background: #fff;
  /* text-decoration:line-through; */
}

.el-dialog__body {
  padding-left: 20px;
  padding-top: 20px;
  padding-bottom: 10px;
  color: #606266;
  font-size: 14px;
  word-break: break-all;
}
</style>