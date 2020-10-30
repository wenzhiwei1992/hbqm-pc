<template>
    <div>
        <el-container>
            <el-aside width="300px" id="tab" tyle="overflow-y: auto;overflow-x: auto" @tab-click="handleClick">
                <el-tree
                        style="width:450px;"
                        ref="vuetree"
                        :data="data"
                        node-key="id"
                        :default-expand-all="false"
                        :default-expanded-keys="idArr"
                        :expand-on-click-node="false"
                        :auto-expand-parent="false"
                        :highlight-current="true"
                        @node-click="handleNodeClick"
                        @node-contextmenu="nodeContextmenu"
                        @node-drag-end="handleDragEnd"
                        @node-drop="handleDrop"
                        draggable
                        :allow-drop="allowDrop"
                        :allow-drag="allowDrag"
                >
      <span class="custom-tree-node" slot-scope="{ node, data }" @mouseenter="mouseenter(node)"
            @mouseleave="mouseleave(node)">
        <span>{{ node.label }}</span>
        <span>
          <el-button
                  v-show="node.add"
                  type="text"
                  size="mini"
                  icon="el-icon-plus"
                  circle
                  @click="() => append(data)">
          </el-button>
          <el-button
                  v-show="node.edit"
                  type="text"
                  size="mini"
                  icon="el-icon-edit"
                  v-if="node.disabled!=false"
                  @click="() => modifiy(node, data)">
          </el-button>
            <!-- v-if="node.disabled=='false'"-->
           <el-button
                   v-show="node.del"
                   type="text"
                   size="mini"
                   icon="el-icon-delete"
                   v-if="node.disabled!=false"
                   @click="() => remove(node, data)">
          </el-button>
        </span>
      </span>
                </el-tree>
            </el-aside>
            <el-main>
                <el-table
                        :data="productTypes"
                        :height="GLOBAL.tabelHeight"
                        style="width: 100%"
                        header-row-class-name="header-row"
                        size="mini"
                        stripe
                        border
                        tooltip-effect="light"
                        @row-click="clickRow"
                        row-key="id"
                >
                    <el-table-column
                            type="index"
                            label="序号"
                            width="60">
                    </el-table-column>
                    <el-table-column
                            prop="deptName"
                            label="名称"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="deptCode"
                            label="部门代码"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="deptHod"
                            label="负责人"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="deptProp"
                            label="部门属性"
                    >
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            align="left">
                        <template slot-scope="scope">
                            <span v-if="currentOrgId==currentOrgIdToLogin">
                                  <el-tooltip class="item" effect="light" content="编辑" placement="top">
                                    <el-button size="mini" icon="el-icon-edit"
                                               @click="editClick(scope.row)"></el-button>
                                </el-tooltip>
                            </span>
                            <span v-else>
                                <el-tooltip class="item" effect="light" content="编辑" placement="top">
                                    <el-button size="mini" icon="el-icon-edit"
                                               disabled
                                               @click="editClick(scope.row)"></el-button>
                                </el-tooltip>
                            </span>
                            <span v-if="currentOrgId==currentOrgIdToLogin">
                                <el-tooltip class="item" effect="light" content="删除" placement="top">
                                    <el-button size="mini" icon="el-icon-circle-close" type="danger"
                                               @click="deleteClick(scope.row)">
                                    </el-button>
                                </el-tooltip>
                            </span>
                            <span v-else>
                               <el-tooltip class="item" effect="light" content="删除" placement="top">
                                    <el-button size="mini" icon="el-icon-circle-close" type="danger"
                                               disabled
                                               @click="deleteClick(scope.row)">
                                    </el-button>
                                </el-tooltip>
                            </span>

                        </template>
                    </el-table-column>
                </el-table>
                <el-button style="float: left" size="mini" icon="el-icon-plus" type="success" @click="addClick">新增
                </el-button>
                <el-row type="flex" justify="space-around" align="middle">
                    <el-col>

                    </el-col>
                    <el-col>

                    </el-col>
                </el-row>
            </el-main>
        </el-container>


        <el-dialog :title="mode==='add'?'新建':'编辑'" :close-on-click-modal="false" @close='closeDialog'
                   :visible.sync="editFormVisible" v-if="editFormVisible">
            <dept-tree-edit
                    :editForm="editForm"
                    :mode="mode"
                    @dept-finish="deptFinish"
                    @dept-cancel="deptCancel"
            >
            </dept-tree-edit>
        </el-dialog>
    </div>
</template>

<script>
    import deptTreeEdit from './DeptTreeEdit.vue'
    import deptModel from '../../../../model/basicdata/dept'
    import orgModel from '../../../../model/system/org'
    import {Notification} from 'element-ui'
    import moment from "moment";

    export default {
        name: "DeptTreeManage",
        components: {
            'dept-tree-edit': deptTreeEdit,
        },
        data() {
            const data = [{
                id: 0,
                label: '',
                disabled: false,
                children: [{
                    id: '',
                    label: '',
                    children: [],
                }]
            }];
            return {
                data: JSON.parse(JSON.stringify(data)),
                productTypes: [],
                editForm: {
                    deptCode: '',
                    deptName: '',
                    id: '',
                    deptType: '',
                    deptFax: '',
                    deptPostCode: '',
                    deptMemo: '',
                    parentId: null,
                    codeLevel: '',
                    blnvCEnd: '',
                    state: '',
                    ifSync: '',
                    parentName: '',
                },
                editFormVisible: false,
                mode: '',
                addDeptId: '',
                currentNodeKey: '',
                idArr: [],
                currentOrgId: '',
                currentOrgIdToLogin: this.$store.state.user.orgId,
            }

        },
        mounted() {
            this.list()
            let info = {
                id: 0
            }
            this.currentNodeKeyClick(info);
            this.handleNodeClick(info)
            this.idArr = [0, 1]
        },
        methods: {
            list() {
                let id = this.$route.params.id;
                if (id == undefined || id == null) {
                    this.$router.push({name: 'orgManage'})
                    return
                }
                this.currentOrgId = id
                orgModel.getOrgById(id).then((data) => {
                    this.data[0].label = data.entity.orgName
                })
                deptModel.getProductTypesToTree(id).then((data) => {
                    this.data[0].children = data.entity
                })
            },
            /**
             * 关闭前重新渲染页面
             */
            closeDialog() {
                this.editForm = {
                    deptCode: '',
                    deptName: '',
                    id: '',
                    deptType: '',
                    deptFax: '',
                    deptPostCode: '',
                    deptMemo: '',
                    parentId: null,
                    codeLevel: '',
                    blnvCEnd: '',
                    state: '',
                    ifSync: '',
                    parentName: '',
                }
            },
            /**
             * 默认展开节点高亮
             */
            currentNodeKeyClick(data) {
                this.$nextTick(function () {
                    this.$refs.vuetree.setCurrentKey(data.id)
                })
            },
            handleClick(tab, event) {


            },
            handleNodeClick(data, node) {
                let model = {
                    id: data.id,
                    orgId: this.$route.params.id
                }
                // console.log('addProductTypeId------>',this.addProductTypeId)
                deptModel.findDepartmentsByParentId(model).then((data1) => {
                    this.productTypes = data1.entity
                    console.log('data------>', data)
                    this.addDeptId = data.id
                })
            },
            mouseenter(node) {
                //不是当前登陆的组织没有权CUD
                if (this.$route.params.id == this.$store.state.user.orgId) {
                    this.$set(node, 'del', true)
                    this.$set(node, 'add', true)
                    this.$set(node, 'edit', true)
                }
            },
            mouseleave(node) {
                //不是当前登陆的组织没有权CUD
                if (this.$route.params.id == this.$store.state.user.orgId) {
                    this.$set(node, 'del', false)
                    this.$set(node, 'add', false)
                    this.$set(node, 'edit', false)
                }
            },
            nodeContextmenu(data, node) {
            },
            append(data) {
                this.mode = this.GLOBAL.STATUS.ADD
                //0为前端页面显示第一级'构件类型'
                if (data.id != 0) {
                    deptModel.findDepartmentById(data.id).then((data1) => {
                        this.editForm.parentId = data1.entity.id
                        this.editForm.parentName = data1.entity.deptName
                        this.editFormVisible = true
                    })
                } else {
                    this.editFormVisible = true
                }
            },

            remove(node, data) {
                this.$confirm('此操作将永久删除该构件类型, 是否继续?', '提示', {
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
                    deptModel.deptListDelete(node.data.id).then(data => {
                        if (data.status == this.GLOBAL.STATUS.FAILED) {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        Notification({
                            message: this.GLOBAL.MSG.DELETE_SUCCESS,
                            type: 'success'
                        })
                        loading.close()
                        this.list()

                    })
                }).catch(() => {
                })
            },
            modifiy(node, data) {
                this.mode = this.GLOBAL.STATUS.UPDATE
                deptModel.findDepartmentById(data.id).then((data1) => {
                    this.editForm = data1.entity
                    this.editFormVisible = true
                })
            },
            clickRow() {

            },
            addClick() {

                this.mode = this.GLOBAL.STATUS.ADD
                /*  if(this.addProductTypeId == ''){
                      this.$message('请选择构件类型');
                      return;
                  }*/

                if (this.addDeptId != 0) {
                    deptModel.findDepartmentById(this.addDeptId).then((data1) => {
                        this.editForm.parentId = data1.entity.id
                        this.editForm.parentName = data1.entity.deptName
                        this.editFormVisible = true
                    })
                } else {
                    this.editFormVisible = true
                }
            },
            editClick(row) {
                this.mode = this.GLOBAL.STATUS.UPDATE
                deptModel.findDepartmentById(row.id).then((data1) => {
                    this.editForm = data1.entity
                    this.editFormVisible = true
                })
            },
            deleteClick(row) {
                this.$confirm('此操作将永久删除该构件类型, 是否继续?', '提示', {
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
                    deptModel.deptListDelete(row.id).then(data => {
                        if (data.status == this.GLOBAL.STATUS.FAILED) {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        Notification({
                            message: this.GLOBAL.MSG.DELETE_SUCCESS,
                            type: 'success'
                        })
                        loading.close()
                        this.list()
                        console.log('row------>', row)
                        //为null时为第一级，赋予0值方便查询
                        if (row.parentId == null) {
                            row.parentId = 0
                        }
                        let info = {
                            id: row.parentId
                        }
                        this.handleNodeClick(info)

                    })
                }).catch(() => {
                })
            },
            /**
             * 拖拽事件
             * @param node
             * @param ev
             */
            handleDragEnd(draggingNode, dropNode, dropType, ev) {
                /*   if(dropNode.label=='构件类型'){
                       return type !== 'inner';
                   }*/
                console.log('tree drag end: ', dropNode && dropNode.label, dropType);
            },
            handleDrop(draggingNode, dropNode, dropType, ev) {
                let model = {
                    draggingNodeId: draggingNode.key,
                    dropNodeId: dropNode.key,
                    dropType: dropType,
                }
                deptModel.updateProductTypeCodeLevelByDrag(model).then((data) => {
                    this.list()
                    let info = {
                        id: data.entity
                    }
                    this.handleNodeClick(info)
                    // this.currentNodeKeyClick(this.addProductTypeId)
                })
            },
            allowDrop(draggingNode, dropNode, type) {
                if (type == 'prev' && dropNode.data.label == this.data[0].label) {
                    return type !== 'prev';
                } else if (type == 'next' && dropNode.data.label == this.data[0].label) {
                    return type !== 'next';
                } else {
                    return true;
                }
            },
            allowDrag(draggingNode) {
                //不是当前登陆的组织没有权限拖拽树
                if (this.currentOrgId != this.currentOrgIdToLogin) {
                    return draggingNode.data === -1;
                }
                return draggingNode.data.label.indexOf(this.data[0].label) === -1;
            },
            deptFinish() {
                //取消前重新渲染页面
                this.editForm = {
                    deptCode: '',
                    deptName: '',
                    id: '',
                    deptType: '',
                    deptFax: '',
                    deptPostCode: '',
                    deptMemo: '',
                    parentId: null,
                    codeLevel: '',
                    blnvCEnd: '',
                    state: '',
                    ifSync: '',
                    parentName: '',
                }

                this.editFormVisible = false

                this.list()
                let info = {
                    id: 0
                }
                this.handleNodeClick(info)

            },
            deptCancel() {
                //取消前重新渲染页面
                this.editForm = {
                    deptCode: '',
                    deptName: '',
                    id: '',
                    deptType: '',
                    deptFax: '',
                    deptPostCode: '',
                    deptMemo: '',
                    parentId: null,
                    codeLevel: '',
                    blnvCEnd: '',
                    state: '',
                    ifSync: '',
                    parentName: '',
                }
                this.editFormVisible = false
                this.list()
                let info = {
                    id: 0
                }
                this.handleNodeClick(info)
            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
        }

    }
</script>

<style scoped>
    .el-aside {

        color: #333;
        text-align: center;
    }

    .el-main {

        color: #333;
        text-align: center;
        margin-top: -20px;

    }

    #tab {
        border: 1px lavender solid;
        height: 500px;
    }

</style>