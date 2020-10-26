<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form :inline="true" id="entity" :model="entity" ref="entity">
              <el-form-item prop="deptId">
                <el-select v-model="entity.deptId" placeholder="部门"
                           clearable>
                  <el-option
                          v-for="item in arrDepts"
                          :key="item.id"
                          :label="item.deptName"
                          :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>


              <el-form-item prop="orgId">
                <el-select v-model="entity.orgId" placeholder="组织"
                           clearable>
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
              <el-form-item prop="mobile">
                <el-input
                        placeholder="手机号"
                        v-model="entity.mobile"
                        size="mini"
                        clearable>
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
        >

          <el-table-column
                  prop="username"
                  label="用户名"
                  align="center"
                  width="150"
          >
          </el-table-column>

          <el-table-column
                  prop="mobile"
                  label="手机号"
                  align="center"
          >
          </el-table-column>
          <el-table-column
                  prop="realName"
                  label="真实姓名"
                  align="center"
                  width="100"
          >
          </el-table-column>
          <el-table-column
                  prop="groups[0].groupName"
                  label="角色"
                  align="center"
          >
          </el-table-column>
          <el-table-column
                  prop="org.orgName"
                  label="所属组织"
                  align="center"
          >
          </el-table-column>
          <el-table-column
                  prop="deptName"
                  label="部门"
                  align="center"
          >
          </el-table-column>
          <el-table-column
                  prop="dateCreated"
                  label="创建时间"
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

    <el-dialog :title="mode=='add'?'新建':'编辑'" :visible.sync="editFormVisible" width="35%">
      <model-edit :edit-form="editForm"
                  :mode="mode"
                  @model-edit-finish="userEditFinish"
                  @model-edit-cancel="userEditCancel"

      ></model-edit>
    </el-dialog>
    <el-dialog :title="groupChooseTitle" :visible.sync="groupChooseVisible" width="50%">
      <group-choose :user="user" :visible="groupChooseVisible" @group-choose-finish="groupChooseFinish"></group-choose>
    </el-dialog>

    <el-dialog title="修改密码" :visible.sync="updatePwdVisible">
      <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px" size="small">
        <el-form-item label="新密码" prop="newPassword">
          <el-input type="password" v-model="editForm.newPassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input type="password" v-model="editForm.confirmPassword" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div style="text-align:right" class="dialog-footer">
        <el-button type="primary" @click="updatePwdSubmit('editForm')">确 定</el-button>
      </div>
    </el-dialog>

  </div>


</template>
<script>
    import userModel from '../../../../model/system/user'
    import deptModel from '../../../../model/basicdata/dept'
    import model from '../../../../model/model'
    import orgModel from '../../../../model/system/org'
    import UserEdit from './UserEdit.vue'
    import moment from 'moment'
    import GroupChoose from './GroupChoose.vue'
    import modelUserGroup from '../../../../model/system/userGroup'
    import {Notification} from 'element-ui'

    export default {
        components: {
            'model-edit': UserEdit,
            'group-choose': GroupChoose,
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
                arrDepts:[],
                user: null,
                expands: [],
                //编辑表单
                mode: '',
                editFormVisible: false,
                editForm: null,
                //角色选择
                groupChooseVisible: false,
                updatePwdVisible: false,
                eidtFormRules: {
                    newPassword: [
                        {required: true, message: '请输入', trigger: 'blur'},
                    ],
                    confirmPassword: [
                        {required: true, validator: this.confirmValidator, trigger: "blur"}
                    ],
                },
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
                    /*    {
                          name: '添加角色',
                          event: 'groupChooseClick',
                          // disabled:"this.$store.state.user.level==='0'"
                        },*/
                    {
                        name: '编辑',
                        event: 'editClick',
                    },
                    {
                        name: '修改密码',
                        event: 'updatePwdVisibleMethod',
                    },
                    {
                        name: '删除',
                        event: 'deleteClick'
                    }
                ]
            }
        },
        computed: {
            groupChooseTitle() {
                if (this.user != null) {
                    return '请给' + this.user.realName + '添加角色'
                }
            }
        },
        mounted() {
            orgModel.orgAll().then(data => {
                this.orgs = data.entity;
            }).catch(() => {
                this.$message('加载组织列表失败')
            })

            deptModel.listAll().then((data)=>{
                this.arrDepts = data.entity
            })

            this.list()

        },
        methods: {
            confirmValidator(rule, value, callback) {
                if (this.editForm.confirmPassword === "" || this.editForm.confirmPassword == undefined) {
                    callback(new Error("请再次输入密码"));
                } else if (this.editForm.confirmPassword !== this.editForm.newPassword) {
                    callback(new Error("两次输入密码不一致!"));
                } else {
                    callback();
                }
            },
            /**
             * 管理员修改pwd
             */
            updatePwdSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        model.updatePwdForManager(this.editForm)
                            .then((data) => {
                                if (data.status == 'fail') {
                                    Notification({
                                        message: data.msg,
                                        type: 'warning'
                                    })
                                    return
                                }
                                Notification({
                                    message: '编辑成功',
                                    type: 'success'

                                })
                                this.updatePwdVisible = false
                            })
                    } else {
                        return false;
                    }
                });
            },
            //表查询
            list() {
                model.list('user', this.entity, this.pageInfo).then(data => {
                    let entity = data.entity
                    let list = data.entity.content;
                    this.entities = list
                    console.log('this.entities---------->', this.entities)
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
            userEditFinish() {
                this.editFormVisible = false
                this.list()
            },
            userEditCancel() {
                this.editFormVisible = false
            },
            //行操作
            addClick() {
                this.mode = 'add'
                this.editForm = {
                    orgName: this.$store.state.user.org.orgName,
                    orgId: this.$store.state.user.org.id,
                    orgIdsData: [] //可查看组织
                }
                this.editFormVisible = true
            },
            editClick(row) {
                model.load('user', row.id).then(data => {
                    this.mode = 'update'
                    this.editForm = data.entity
                    if (this.editForm.orgIdsData == null) {
                        this.editForm.orgIdsData = []
                    }
                    this.editFormVisible = true
                })
            },
            updatePwdVisibleMethod(row) {
                model.load('user', row.id).then(data => {
                    this.editForm = data.entity;
                    this.editForm.newPassword = '';
                    this.editForm.confirmPassword = '';
                    this.updatePwdVisible = true;
                })
            },
            deleteClick(row) {
                this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading')
                    model.delete('user', row.id).then(data => {
                        this.$store.commit('endLoading')
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
            deleteGroup(row) {
                this.$store.commit('startLoading')
                modelUserGroup.deleteByUserIdAndGroupId(this.user.id, row.id,).then(data => {
                    Notification({
                        message: '删除成功',
                        type: 'success'

                    })
                    this.$store.commit('endLoading')
                    this.list()
                })
            },
            //角色选择
            groupChooseClick(row) {
                userModel.findUserById(row.id).then(data => {
                    this.user = data.entity
                    this.groupChooseVisible = true
                })

            },
            groupChooseFinish() {
                this.list()
                this.groupChooseVisible = false
            }

        },

    }
</script>
<style>
  #user-manage .el-table__expanded-cell[class*=cell] {
    padding: 0px 50px;
  }

  .el-table th, .el-table tr.header-row {
    background-color: #f5f7fa;
    font-size: 1.1em;
  }

  #entity .el-form-item {
    margin-bottom: 5px;
  }
</style>
