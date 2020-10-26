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
              <el-form-item prop="orgId">
                <el-select v-model="entity.orgId" placeholder="组织"
                           v-if="this.$store.state.user.level==='0' "
                           clearable>
                  <el-option v-for="item in orgs"
                             :key="item.id"
                             :label="item.orgName"
                             :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item prop="groupName">
                <el-input
                        placeholder="角色名"
                        v-model="entity.groupName"
                        size="mini"
                        clearable
                >
                </el-input>
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
              :style="`height:${this.$store.state.mainContentHeight}px;display:flex;`"
      >
        <el-table
                :data="entities"
                ref="handleClickRowSelection"
                height="100%"
                @row-click="handleRowClick"
                style="width:100%"
                header-row-class-name="header-row"
                size="mini"
                border
                highlight-current-row
                row-class-name="table-row"
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
                <el-table-column
                        prop="nickName"
                        label="昵称"
                        align="center"
                        width="100"
                >
                </el-table-column>
                <el-table-column
                        prop="mobile"
                        label="手机"
                        align="center"
                        width="100"
                >
                </el-table-column>
                <el-table-column
                        prop="email"
                        label="邮箱"
                        align="center"
                        width="200"
                >
                </el-table-column>
                <el-table-column
                        prop="telephone"
                        label="固话"
                        align="center"
                        width="100"
                >
                </el-table-column>
                <el-table-column
                        label="操作"
                        align="center"
                >
                  <template slot-scope="scope">
                    <el-button
                            type="danger"
                            size="mini"
                            @click="deleteUser(scope.row)"
                    >删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </template>
          </el-table-column>
          <el-table-column
                  prop="users.length"
                  label="用户数量"
                  align="center"
          >
          </el-table-column>
          <el-table-column
                  prop="groupName"
                  label="角色名"
                  align="center"
          >
          </el-table-column>
          <el-table-column
                  prop="dateCreated"
                  label="创建时间"
                  align="center"
                  :formatter="momentFormat"
          >

          </el-table-column>
          <!--<el-table-column label="操作" fixed="right">-->
            <!--<template slot-scope="scope">-->
              <!--<my-row-button-group-->
                      <!--:row="scope.row"-->
                      <!--:model="rowButtonData"-->
              <!--&gt;</my-row-button-group>-->
            <!--</template>-->
          <!--</el-table-column>-->
        </el-table>
        <el-aside width="250px">
          <group-auth-tree ref="groupAuthTree" :group="group"
                           :currentMenuList="currentMenuList"></group-auth-tree>
        </el-aside>
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
            width="35%"
    >
      <model-edit
              :edit-form="editForm"
              :mode="mode"
              @model-edit-finish="editFinish"
              @model-edit-cancel="editCancel"
      ></model-edit>
    </el-dialog>

    <el-dialog
            :title="userAPITitle"
            :visible.sync="pmissionVisiable"
            v-if="pmissionVisiable"
            width="70%"
    >
      <pm-auth-tree
              :permissions="permissions"
              :group="group"
              :permissionIds="permissionIds"
              @close-pm-auth="closePmAuth"
      ></pm-auth-tree>
    </el-dialog>
    <el-dialog
            :title="userChooseTitle"
            :visible.sync="userChooseVisible"
            v-if="userChooseVisible"
            width="70%"
    >
      <user-choose
              :filter="filter"
              :groupOrgId="groupOrgId"
              :visible="userChooseVisible"
              @user-choose-finish="userChooseFinish"
      ></user-choose>
    </el-dialog>
  </div>
</template>
<script>
    import groupMenuModel from '../../../../model/system/groupMenu'
    import model from '../../../../model/model'
    import permission from '../../../../model/system/permission'
    import modelUserGroup from '../../../../model/system/userGroup'
    import GroupEdit from './GroupEdit.vue'
    import PmAuthTree from './permission/PermissionAuthTree.vue'
    import GroupAuthTree from './GroupAuthTree.vue'
    import UserChoose from './UserChoose.vue'
    import moment from 'moment'
    import {Notification} from 'element-ui'
    import orgModel from "../../../../model/system/org";

    export default {
        components: {
            'model-edit': GroupEdit,
            'group-auth-tree': GroupAuthTree,
            'user-choose': UserChoose,
            'pm-auth-tree': PmAuthTree,
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
                    groupName: null,
                },
                entities: [],
                //编辑表单
                mode: '',
                editFormVisible: false,
                editForm: null,
                //行操作
                group: null,
                filter: {},
                expandData: null,
                expands: [],
                //用户选择
                userChooseVisible: false,
                permissions: [],//权限集合树结构
                permissionIds: [],//已配置的权限ID集合
                pmissionVisiable: false,
                currentMenuList: [],
                groupOrgId:'',
                orgs:[],
                buttonData: [
                    {
                        name: '新建',
                        event: 'addClick'
                    },
                    // {
                    //   name:'保存',
                    //   event:'saveClick',
                    //   disabled:"this.group==null||!changed"
                    // }
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "query"
                    }
                ],
                rowButtonData: [
                    // {
                    //     name: '编辑',
                    //     event: 'editClick',
                    // },
                    // {
                    //     name: '删除',
                    //     event: 'deleteClick',
                    // },
                    // {
                    //     name: '添加角色',
                    //     event: 'userChooseClick',
                    // },
                    // {
                    //     name: '权限',
                    //     event: 'permissionAdd'
                    // }
                ]

            }
        },
        computed: {
            userAPITitle() {
                if (this.group) {
                    return '请给 {' + this.group.groupName + '} 添加API权限'
                }
            },
            userChooseTitle() {
                if (this.group) {
                    return '请给 {' + this.group.groupName + '} 添加用户'
                }
            }
        },
        mounted() {
            orgModel.orgAll().then(data => {
                this.orgs = data.entity;
            }).catch(() => {
                this.$message('加载组织列表失败')
            })
            this.list()
        },
        methods: {
            saveClick() {
                console.log(this.$refs.groupAuthTree)
            },
            closePmAuth() {
                this.pmissionVisiable = false;
            },
            permissionAdd(row) {
                this.group = row;
                let groupId = row.id;
                permission.loadTree().then(data => {
                    this.permissions = data.entity;
                })
                permission.loadByGroupId(groupId).then(data => {
                    this.permissionIds = data.entity;
                    this.pmissionVisiable = true;
                })

            },
            //表查询
            list() {
                model.list('group', this.entity, this.pageInfo).then(data => {
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
            handleSizeChange(val) {
                this.pageInfo.pageSize = val
                this.pageInfo.currentPage = 1
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
            editFinish() {
                this.editFormVisible = false
                this.list()
            },
            editCancel() {
                this.editFormVisible = false
            },
            addClick() {
                this.mode = 'add'
                this.editForm = {
                    active: '是',
                    level: '2',
                }
                this.editFormVisible = true
            },
            //行操作
            editClick(row) {
                model.load('group', row.id).then(data => {
                    this.mode = 'update'
                    this.editForm = data.entity
                    this.editFormVisible = true
                })
            },
            deleteClick(row) {
                if (row.users && row.users.length > 0) {
                    Notification({
                        message: '该用户组下有未删除的用户',
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
                    model.delete('group', row.id).then(data => {
                        this.$store.commit('endLoading')
                        this.list()
                    })
                }).catch(() => {
                })
            },
            handleRowClick(row, event, column) {
                groupMenuModel.findIndexByGroupId(row.id).then((data) => {
                    if (data.status == 'failed') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    }
                    this.currentMenuList = data.entity
                    this.group = row
                })
                this.$refs.handleClickRowSelection.setCurrentRow(row);
                this.handleClickRowSelection(row)

            },
            expandChange(row) {

                groupMenuModel.findIndexByGroupId(row.id).then((data) => {
                    if (data.status == 'failed') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    }
                    this.currentMenuList = data.entity
                    this.group = row
                })

                this.$refs.handleClickRowSelection.setCurrentRow(row);
                if (this.expands.some((e) => e == row.id)) {
                    this.expands = []

                } else {
                    this.expands = []
                    this.expands.push(row.id)
                }
            },
            deleteUser(row) {
                this.$store.commit('startLoading')
                modelUserGroup.deleteByUserIdAndGroupId(row.id, this.group.id).then(data => {
                    Notification({
                        message: '删除成功',
                        type: 'success'

                    })
                    this.$store.commit('endLoading')
                    this.list()
                })
            },
            //用户选择
            userChooseClick(row) {
                this.group = row
                let ids = []
                this.group.users.forEach((u) => {
                    ids.push(u.id)
                })
                this.filter = {ids: ids}
                this.groupOrgId = this.group.orgId
                this.userChooseVisible = true
            },
            userChooseFinish(ids) {
                var addRows = []
                if (ids.length > 0) {
                    ids.forEach((id) => {
                        addRows.push({userId: id, groupId: this.group.id})
                    })
                    model.adds('userGroup', addRows).then(data => {
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

            }

        },

    }
</script>
<style>
  #group-manage .el-main {
    padding-top: 0;
  }

  #group-manage .el-table th,
  .el-table tr.header-row {
    background-color: #f5f7fa;
    font-size: 1.1em;
  }

  #group-manage .el-table th,
  .el-table tr.table-row {
    cursor: pointer;
  }

  #group-manage .el-table__expanded-cell[class*="cell"] {
    padding: 0px 50px;
  }

  #group-manage #entity .el-form-item {
    margin-bottom: 5px;
  }

  #group-manage .el-aside {
    border: 1px solid #ebeef5;
    padding-left: 5px;
    padding-right: 5px;
  }
</style>
