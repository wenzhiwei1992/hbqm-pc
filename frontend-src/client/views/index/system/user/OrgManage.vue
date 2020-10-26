<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form
              :inline="true"
              id="entity"
              :model="entity"
              ref="entity"
            >
              <el-form-item prop="id">
                <el-select  v-model="entity.id" placeholder="基地名称" clearable  filterable @change="changeProvince">
                  <el-option v-for="item in orgs"
                             :key="item.id"
                             :label="item.orgName"
                             :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>

              <el-form-item prop="proposer">
                <el-input
                        v-model="entity.proposer"
                        placeholder="申请人"
                        size="mini"
                        clearable
                >
                </el-input>
              </el-form-item>
              <el-form-item prop="proposerPhone">
                <el-input
                        v-model="entity.proposerPhone"
                        placeholder="申请人手机号"
                        size="mini"
                        clearable
                >
                </el-input>
              </el-form-item>

              <el-form-item  prop="provinceId">
                <el-select  v-model="entity.provinceId" placeholder="省" filterable clearable  @change="changeProvince">
                  <el-option v-for="item in arrProvinces"
                             :key="item.id"
                             :label="item.deptName"
                             :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>

              <el-form-item prop="cityId">
                <el-select  v-model="entity.cityId" clearable filterable placeholder="市">
                  <el-option v-for="item in arrCitys"
                             :key="item.id"
                             :label="item.deptName"
                             :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>

              <el-form-item  prop="productionType">
                <el-select  v-model="entity.productionType" clearable placeholder="生产类型">
                  <el-option v-for="item in arrProductions"
                             :key="item.id"
                             :label="item.codeName"
                             :value="item.codeName"
                  ></el-option>
                </el-select>
              </el-form-item>


              <el-form-item prop="state">
                <el-select  v-model="entity.state" clearable placeholder="状态">
                  <el-option v-for="item in arrStates"
                             :key="item.id"
                             :label="item.codeName"
                             :value="item.codeName"
                  ></el-option>
                </el-select>
              </el-form-item>

              <el-form-item prop="approvalStatus">
                <el-select  v-model="entity.approvalStatus" clearable placeholder="审批状态">
                  <el-option v-for="item in arrApprovalStates"
                             :key="item.id"
                             :label="item.codeName"
                             :value="item.codeName"
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
          :data="entities"
          @selection-change="handleSelectionChange"
          ref="handleClickRowSelection"
          height="100%"
          @row-click="handleClickRowSelection"
          style="width:100%"
          header-row-class-name="header-row"
          size="mini"
          stripe
          border
          @expand-change="expandChange"
          row-key="id"
          :expand-row-keys="expands"
        >
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-table
                :data="props.row.users"
                size="mini"
              >

                <el-table-column
                  prop="username"
                  label="用户名"
                  align="center"
                  width="100"
                ></el-table-column>

                <el-table-column
                  prop="realName"
                  label="姓名"
                  align="center"
                  width="100"
                >
                </el-table-column>

              </el-table>
            </template>
          </el-table-column>
          <el-table-column
            prop="users.length"
            label="用户数量"
            align="center"
            width="100"
          >
          </el-table-column>

          <el-table-column
                  prop="orgName"
                  align="center"
                  label="基地名称"
          >
            <template slot-scope="scope">
              <a href="javascript:void(0);" style="color: #409EFF;" @click.stop="editClicks(scope.row)">
                {{scope.row.orgName}}
              </a>
            </template>
          </el-table-column>


          <el-table-column
                  prop="address"
                  label="基地地址"
                  align="center"
          >
          </el-table-column>

          <el-table-column
                  prop="proposer"
                  label="申请人"
                  align="center"
          >
          </el-table-column>

          <el-table-column
                  prop="proposerPhone"
                  label="申请人手机号"
                  align="center"
          >
          </el-table-column>

          <el-table-column
                  prop="longitude"
                  label="基地经纬度"
                  align="center"
          >
          </el-table-column>

          <el-table-column
                  prop="provinceName"
                  label="省"
                  align="center"
          >
          </el-table-column>
          <el-table-column
                  prop="cityName"
                  label="市"
                  align="center"
          >
          </el-table-column>

          <el-table-column
                  prop="productionType"
                  label="生产类型"
                  align="center"
          >
          </el-table-column>

          <el-table-column
                  prop="lineNum"
                  label="生产线/条"
                  align="center"
          >
          </el-table-column>

          <el-table-column
                  prop="designProductivity"
                  label="设计生产能力（万m/万吨）"
                  align="center"
                  width="210"
          >
          </el-table-column>
          <el-table-column
                  prop="realProductivity"
                  label="实际生产能力（万m/万吨）"
                  align="center"
                  width="210"
          >
          </el-table-column>

          <el-table-column
                  prop="state"
                  label="状态"
                  align="center"
          >
          </el-table-column>



          <el-table-column
            prop="orgDesc"
            label="组织描述"
            align="center"
            :show-overflow-tooltip="true"
            width="100"
          >
          </el-table-column>


          <el-table-column
                  fixed="right"
                  prop="approvalStatus"
                  label="审批状态"
                  width="110"
          >
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

    <el-dialog
      :title="mode=='add'?'新建':'编辑'"
      :visible.sync="editFormVisible"
      width="50%"
    >
      <model-edit
        :edit-form="editForm"
        :mode="mode"
        @model-edit-finish="orgEditFinish"
        @model-edit-cancel="orgEditCancel"
      ></model-edit>
    </el-dialog>
    <el-dialog
      :title="userChooseTitle"
      :visible.sync="userChooseVisible"
      v-if="userChooseVisible"
      width="70%"
    >
      <user-choose
        :filter="filter"
        :visible="userChooseVisible"
        @user-choose-finish="userChooseFinish"
      ></user-choose>
    </el-dialog>


    <el-dialog
            title="基地详情"
            :visible.sync="editFormVisible1"
            width="80%"
            top="6vh"
    >

      <el-form
              inline="inline"
              :model="editForm"
              v-if="editForm"
              ref="editForm"
              label-width="80px"
      >

        <span> 基地名称:{{editForm.orgName}}</span>

        <span> 基地地址:{{editForm.address}}</span>

        <span> 基地经纬度:{{editForm.longitude}}</span>

        <span> 申请人:{{editForm.proposer}}</span>

        <span> 申请人手机号:{{editForm.proposerPhone}}</span>
        <el-form-item label="申请人" prop="proposer">
          <el-input disabled v-model="editForm.proposer"></el-input>
        </el-form-item>
        <el-form-item label="申请人手机号" prop="proposerPhone">
          <el-input disabled v-model="editForm.proposerPhone"></el-input>
        </el-form-item>

        <span> 省:{{editForm.provinceName}}</span>

        <span> 市:{{editForm.cityName}}</span>
        <span> 生产类型:{{editForm.productionType}}</span>
        <span> 生产线/条:{{editForm.lineNum}}</span>
        <span> 设计生产能力（万m/万吨）:{{editForm.designProductivity}}</span>
        <span> 实际生产能力（万m/万吨）:{{editForm.realProductivity}}</span>

        <span> 状态:{{editForm.state}}</span>

        <span> 情况说明:{{editForm.orgDesc}}</span>


      </el-form>

    </el-dialog>


  </div>

</template>
<script>

    import codeExplainContent from '../../../../model/basicdata/codeExlpainContent'
    import deptModel from '../../../../model/basicdata/dept'
import menuTabService from '../../../../service/menuTabService'
import model from '../../../../model/model'
import orgModel from '../../../../model/system/org'
import OrgEdit from './OrgEdit.vue'
import moment from 'moment'
import UserChoose from './UserChoose.vue'
import deptTreeManage from './DeptTreeManage.vue'
import { Notification } from 'element-ui'
export default {
  components: {
    'model-edit': OrgEdit,
    'user-choose': UserChoose,
  },
  data() {
    return {
      //表查询
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: this.GLOBAL.pageSize,
        pageSizes: this.GLOBAL.pageSizes
      },
      entity: {
        orgCode: null,
        orgName: null,
      },
      entities: [],
      //行操作
      selected: [],
      org: null,
      filter: {},
      expands: [],
        orgs:[],
        arrProvinces: [],
        arrCitys: [],
        arrProductions: [],
        arrStates: [],
        arrApprovalStates:[],
      mode: '',
      editFormVisible: false,
        editFormVisible1:false,
      editForm: null,
      userChooseVisible: false,
      buttonData: [
        {
          name: '新建',
          event: 'addClick',
          authorized: 'orgManage:add'
        }
      ],
      searchButtonData: [
        {
          name: '查询',
          event: "query"
        }
      ],
      rowButtonData: [
     /*   {
          name: '添加用户',
          event: 'userChooseClick',
        },*/
        {
          name: '编辑',
          event: 'editClick',
          authorized: 'orgManage:edit'
        },
        {
          name: '删除',
          event: 'deleteClick',
          authorized: 'orgManage:del'
        },
        {
          name: '部门',
          event: 'deptClick'
        }
      ]
    }
  },
  computed: {
    userChooseTitle() {
      if (this.org != null) {
        return '请给' + this.org.orgName + '添加用户'
      }
    }
  },
  mounted() {
    this.list()
      this.init();
  },
  methods: {

      init(){
          orgModel.orgAll().then(data => {
              this.orgs = data.entity;
          })
          deptModel.findDepartmentByLevel(0).then(data => {
              this.arrProvinces = data.entity;
          })
          codeExplainContent.getAllByCodeNo("ORG_PRODUCTION_TYPE").then(data => {
              this.arrProductions = data.entity;
          })

          codeExplainContent.getAllByCodeNo("ORG_STATE").then(data => {
              this.arrStates = data.entity;
          })
          codeExplainContent.getAllByCodeNo("ORG_APPROVAL_STATE").then(data => {
              this.arrApplyStates = data.entity;
          })

      },

      changeProvince(){
          let id = this.entity.provinceId;
       //   this.entity.cityId = ""
          this.$set(this.entity,'cityId','')
          this.arrCitys = []
          let query = {
              id:id,
              orgId:0
          }
          deptModel.findDepartmentsByParentId(query).then(data => {
              this.arrCitys = data.entity;
          })
      },
    //表查询
    list() {
      model.list('org', this.entity, this.pageInfo).then(data => {
        let entity = data.entity
        let list = data.entity.content;
        this.entities = list
        this.pageInfo.total = data.entity.totalElements
      })
    },
    query() {
      this.pageInfo.currentPage = 1
      this.list()
    },
    handleSelectionChange(selected) {
      this.selected = selected
    },
    handleSizeChange(val) {
      this.pageInfo.currentPage = 1
      this.pageInfo.pageSize = val
      this.list()
    },
    handleCurrentChange(val) {
      this.pageInfo.currentPage = val
      this.list()
    },
    momentFormat(row, column, cellValue, index) {
      if (cellValue) {
        return moment(cellValue).format('YYYY-MM-DD HH:mm:ss')
      } else {
        return cellValue
      }

    },
    //编辑表单
    orgEditFinish() {
      this.editFormVisible = false
      this.list()
    },
    orgEditCancel() {
      this.editFormVisible = false
    },
    //行操作
    addClick() {
      this.mode = 'add'
      this.editForm = {
          orgDesc:"",
          address:"",
          productionType:"",
          lineNum:null,
          designProductivity:"",
          realProductivity:"",
          state:"",
          provinceId:"",
          cityId:"",
      }
      this.editFormVisible = true
    },
    editClick(row) {
      model.load('org', row.id).then(data => {
        this.mode = 'update'
        this.editForm = data.entity
        this.editFormVisible = true
      })
    },


      editClicks(row) {

          model.load('org', row.id).then(data => {
              this.mode = 'update'
              this.editForm = data.entity
              this.editFormVisible1 = true
          })

      },

    deleteClick(row) {
      if (row.users && row.users.length > 0) {
        Notification({
          message: '该组织下有未删除的用户',
          type: 'warning'

        })
        return
      }
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.commit('startLoading')
        model.delete('org', row.id).then(data => {
          this.$store.commit('endLoading')
          this.list()
        })
      }).catch(() => { })
    },
    expandChange(row) {
      this.org = row
      this.$refs.handleClickRowSelection.setCurrentRow(row);
      if (this.expands.some((e) => e == row.id)) {
        this.expands = []

      } else {
        this.expands = []
        this.expands.push(row.id)
      }
    },
    removeUser(row) {
      this.$store.commit('startLoading')
      orgModel.removeUser(row.id, this.org.id).then(data => {
        Notification({
          message: '删除成功',
          type: 'success'

        })
        this.$store.commit('endLoading')
        this.list()
      })
    },
    //进入部门页面
    deptClick(row) {
      this.$router.push({
        name: 'deptTreeManage',
        params: {
          id: row.id
        }
      })

    },
    //用户选择
    userChooseClick(row) {
      this.org = row
      let ids = []
      this.org.users.forEach((u) => {
        ids.push(u.id)
      })
      this.filter = { ids: ids, orgIdNull: true }
      this.userChooseVisible = true
    },
    userChooseFinish(ids) {
      var addRows = []
      if (ids.length > 0) {
        ids.forEach((id) => {
          addRows.push({ id: id, orgId: this.org.id })
        })
        orgModel.addUsers(addRows).then(data => {
          if (data.status == 'fail') {
            Notification({
              message: data.msg,
              type: 'warning'
            })
            return
          }
          Notification({
            message: '保存成功',
            type: 'success'

          })

          this.list()
          this.userChooseVisible = false
        })
      }
    },
    authorized(index) {
      return menuTabService.authorized(this.$store, index)
    }

  },

}
</script>
<style>
#user-manage .el-table__expanded-cell[class*="cell"] {
  padding: 0px 50px;
}
.el-table th,
.el-table tr.header-row {
  background-color: #f5f7fa;
  font-size: 1.1em;
}
#entity .el-form-item {
  margin-bottom: 5px;
}
</style>
