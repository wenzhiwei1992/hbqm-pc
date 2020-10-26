<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <!-- <el-form :inline="true" id="entity" :model="entity" ref="entity">
                 <el-form-item prop="orgId">
                     <el-select v-model="entity.orgId" placeholder="组织" clearable>
                         <el-option v-for="item in orgs"
                                    :key="item.id"
                                    :label="item.orgName"
                                    :value="item.id"
                         ></el-option>
                     </el-select>
                 </el-form-item>
                 <el-form-item prop="username">
                     <el-input
                             placeholder="用户名"
                             v-model="entity.username"
                             size="mini"
                             clearable>
                     </el-input>
                 </el-form-item>
                 <el-form-item prop="realName">
                     <el-input
                             placeholder="真实姓名"
                             v-model="entity.realName"
                             size="mini"
                             clearable>
                     </el-input>
                 </el-form-item>
             </el-form>-->
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
          <el-table-column
              type="selection"
              align="center">
          </el-table-column>
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-table
                  :data="props.row.menuItems"
                  size="mini"
              >
                <el-table-column type="expand">
                  <template slot-scope="props">
                    <el-table
                        :data="props.row.menuItems"
                        size="mini"
                    >
                      <el-table-column type="expand">
                        <template slot-scope="props">
                          <el-table
                              :data="props.row.menuItems"
                              size="mini"
                          >
                            <el-table-column
                                prop="title"
                                label="菜单名称"
                                align="center"
                                width="100"
                            >
                            </el-table-column>
                            <el-table-column
                                prop="icon"
                                label="图标"
                                align="center"
                                width="100"
                            >
                            </el-table-column>
                            <el-table-column
                                prop="sort"
                                label="排序"
                                align="center"
                                width="100"
                            >
                            </el-table-column>
                            <el-table-column
                                prop="index"
                                label="路由地址"
                                align="center"
                            >
                            </el-table-column>
                            <el-table-column
                                prop="dateCreated"
                                label="创建时间"
                                width="150"
                                align="center"
                                :formatter="momentFormat"
                            ></el-table-column>
                            <el-table-column
                                label="操作"
                                align="center"
                            >
                              <template slot-scope="scope">
                                <my-row-button-group
                                    :row="scope.row"
                                    :model="rowButtonData"
                                ></my-row-button-group>
                              </template>
                            </el-table-column>
                          </el-table>
                        </template>
                      </el-table-column>
                      <el-table-column
                          prop="title"
                          label="菜单名称"
                          align="center"
                          width="100"
                      >
                      </el-table-column>
                      <el-table-column
                          prop="icon"
                          label="图标"
                          align="center"
                          width="100"
                      >
                      </el-table-column>
                      <el-table-column
                          prop="sort"
                          label="排序"
                          align="center"
                          width="100"
                      >
                      </el-table-column>
                      <el-table-column
                          prop="index"
                          label="路由地址"
                          align="center"
                      >
                      </el-table-column>
                      <el-table-column
                          prop="dateCreated"
                          label="创建时间"
                          width="150"
                          align="center"
                          :formatter="momentFormat"
                      ></el-table-column>
                      <el-table-column
                          label="操作"
                          align="center"
                      >
                        <template slot-scope="scope">
                          <my-row-button-group
                              :row="scope.row"
                              :model="rowButtonData"
                          ></my-row-button-group>
                        </template>
                      </el-table-column>
                    </el-table>
                  </template>
                </el-table-column>
                <el-table-column
                    prop="title"
                    label="菜单名称"
                    align="center"
                    width="100"
                >
                </el-table-column>
                <el-table-column
                    prop="icon"
                    label="图标"
                    align="center"
                    width="100"
                >
                </el-table-column>
                <el-table-column
                    prop="sort"
                    label="排序"
                    align="center"
                    width="100"
                >
                </el-table-column>
                <el-table-column
                    prop="index"
                    label="路由地址"
                    align="center"
                >
                </el-table-column>
                <el-table-column
                    prop="dateCreated"
                    label="创建时间"
                    width="150"
                    align="center"
                    :formatter="momentFormat"
                ></el-table-column>
                <el-table-column
                    label="操作"
                    align="center"
                >
                  <template slot-scope="scope">
                    <my-row-button-group
                        :row="scope.row"
                        :model="rowButtonData"
                    ></my-row-button-group>
                  </template>
                </el-table-column>
              </el-table>
            </template>
          </el-table-column>
          <el-table-column
              prop="title"
              label="菜单名称"
              align="center"
              width="100"
          >
          </el-table-column>
          <el-table-column
              prop="icon"
              label="图标"
              align="center"
              width="100"
          >
          </el-table-column>
          <el-table-column
              prop="sort"
              label="排序"
              align="center"
              width="100"
          >
          </el-table-column>
          <el-table-column
              prop="index"
              label="路由地址"
              align="center"
          >
          </el-table-column>
          <el-table-column
              prop="dateCreated"
              label="创建时间"
              width="150"
              align="center"
              :formatter="momentFormat"
          ></el-table-column>
          <el-table-column label="操作" fixed="right" :width="rowButtonGroupWidth(rowButtonData)">
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


    <el-dialog :title="mode=='add'?'新建':'编辑'" v-if="editFormVisible" :visible.sync="editFormVisible" width="500px">
      <menu-edit :edit-form="editForm"
                 :mode="mode"
                 @menu-edit-finish="menuEditFinish"
                 @menu-edit-cancel="menuEditCancel"

      ></menu-edit>
    </el-dialog>


  </div>
</template>

<script>
import moment from 'moment'
import model from "../../../../model/model";
import menuModel from '../../../../model/system/menu'
import {Notification} from 'element-ui'
import MenuEdit from './MenuEdit.vue'

export default {

  name: "MenuManage",
  components: {
    'menu-edit': MenuEdit,
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
        username: null,
        realName: null,
        orgId: null,
      },
      orgs: [],
      entities: [],
      //行操作
      selected: [],
      user: null,
      expands: [],
      //编辑表单
      mode: '',
      editFormVisible: false,
      editForm: null,
      updatePwdVisible: false,
      qywxFormRules: {
        corpid: [
          {required: true, message: '请输入', trigger: 'blur'},
        ],
        corpsecret: [
          {required: true, message: '请输入', trigger: "blur"}
        ],
        deptId: [
          {required: true, message: '请输入', trigger: "blur"}
        ],

      },
      qywxVisiable: false,
      qywxForm: {},
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        },
      ],
      searchButtonData: [
        {
          name: '查询',
          event: "query"
        }
      ],
      rowButtonData: [
        {
          name: '编辑',
          event: 'editClick',
        },
        {
          name: '删除',
          event: 'deleteClick'
        }
      ]
    }
  },
  computed: {},
  mounted() {
    this.list()
  },
  methods: {
    /**
     * 表查询
     */
    list() {
      let user = this.$store.state.user;
      let appId = ''
      if (user != null && (user.level == 1 || user.level == 2)) {
        appId = 1
      } else if (user.level == 0) {
        appId = 0
      }
      menuModel.getMenuListByAppId(1).then((data) => {
        this.entities = data.entity.content
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
    menuEditFinish() {
      this.editFormVisible = false
      this.list()
    },
    menuEditCancel() {
      this.editFormVisible = false
    },
    //行操作
    addClick() {
      this.mode = 'add'
      this.editForm = {
        type: '0',
        icon: '',
        title: '',
        index: '',
        sort: '',
        parentId: null,
        authMark: 'false',
        isTab: true,
        isRoute: false,
        iFrame: false
      }
      this.editFormVisible = true
    },
    editClick(row) {
      menuModel.getMenuById(row.id).then(data => {
        if (data.entity.appId == 0) {
          data.entity.appId = '管理员菜单'
        } else if (data.entity.appId == 1) {
          data.entity.appId = '用户菜单'
        }
        this.mode = 'update'
        this.editForm = data.entity
        this.editFormVisible = true
      })
    },

    deleteClick(row) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        menuModel.menuDelete(row.id).then(data => {
          if (data.status == 'failed') {
            Notification({
              message: data.msg,
              type: 'warning'
            })
            loading.close()
            return
          }
          Notification({
            message: "删除成功",
            type: "success"
          });
          loading.close();
          this.list()
        })
      }).catch(() => {
      })
    },
    handleRowClick(row, event, column) {
      this.user = row
      this.$refs.handleClickRowSelection.setCurrentRow(row);
    },
    expandChange(row) {
      this.user = row
      this.$refs.handleClickRowSelection.setCurrentRow(row);
      if (this.expands.some((e) => e == row.id)) {
        this.expands = []

      } else {
        this.expands = []
        this.expands.push(row.id)
      }
    },

  },
}
</script>

<style scoped>

</style>
