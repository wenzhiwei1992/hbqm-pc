<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                class="form-inline"
                                :model="sfgPickingQuery"
                                ref="sfgPickingQuery"
                        >
                            <el-form-item>
                                <el-input
                                        v-model="sfgPickingQuery.sfgPickingNo"
                                        placeholder="入库编码"
                                        clearable
                                        style="width: 135px"
                                ></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-select
                                        v-model="sfgPickingQuery.sfgPickingStatus"
                                        placeholder="状态"
                                        clearable
                                        style="width: 135px"
                                        collapse-tags
                                >
                                    <el-option
                                            v-for="item in sysCodes"
                                            :key="item.id"
                                            :label="item.codeName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item>
                                <el-date-picker
                                        v-model="sfgPickingQuery.sfgPickingDate"
                                        align="right"
                                        type="date"
                                        style="width: 135px"
                                        :formatter="formatterDate"
                                        placeholder="收料时间"
                                >
                                </el-date-picker>
                            </el-form-item>
                            <el-form-item prop="projectId">
                                <el-select v-model="sfgPickingQuery.projectId" style="width: 150px" clearable placeholder="项目">
                                    <el-option
                                            v-for="item in projectList"
                                            :key="item.id"
                                            :label="item.projectName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="deptName">
                                <el-select v-model="sfgPickingQuery.departmentId" style="width: 150px" placeholder="部门"
                                           clearable
                                           collapse-tags>
                                    <el-option
                                            v-for="item in deptList"
                                            :key="item.id"
                                            :label="item.deptName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="purpose">
                                <el-select v-model="sfgPickingQuery.purpose" placeholder="用途" style="width: 150px" clearable>
                                    <el-option
                                            v-for="item in options"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
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
                        :data="sfgPickingData"
                        @selection-change="handleSelectionChange"
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                        @expand-change="showSubData"
                        :expand-row-keys="expands"
                        :row-key="getRowKeys"
                        size="mini"
                        stripe
                        border
                        height="100%"
                        style="width:100%"
                >
                    <el-table-column
                            type="selection"
                            width="39"
                    >
                    </el-table-column>
                    <el-table-column type="expand">
                        <template slot-scope="props">
                            <el-table
                                    :data="subDatas"
                                    @selection-change="handleSelectionChangeForPL"
                                    size="mini"
                                    stripe
                                    border
                                    style="width: 100%"
                            >
                                <el-table-column
                                        type="selection"
                                        width="80">
                                </el-table-column>
                                <el-table-column
                                        prop="sfgNo"
                                        label="半成品编码"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="sfgName"
                                        label="半成品名称"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="rebarShape"
                                        label="钢筋形状"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="projectName"
                                        label="项目名称"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="buildCode"
                                        label="楼栋"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="floorNum"
                                        label="楼层"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="unitCNName"
                                        label="单位"
                                >
                                </el-table-column>
                            </el-table>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="sfgPickingNo"
                            label="出库单号"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="sfgPickingStatusStr"
                            label="状态"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="proposer"
                            label="申请人"
                    ></el-table-column>
                    <el-table-column
                            prop="sfgPickingDate"
                            label="领料时间"
                    ></el-table-column>
                    <el-table-column
                            prop="projectName"
                            label="项目"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="deptName"
                            label="部门"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="createdByName"
                            label="创建人"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="dateCreated"
                            label=创建时间
                    >
                    </el-table-column>
                   <!-- <el-table-column
                            prop="purpose"
                            label="用途"
                    ></el-table-column>-->
                    <el-table-column
                            prop="remarks"
                            label=备注
                    >
                    </el-table-column>
                    <el-table-column
                            label="操作"
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
                            :current-page="sfgPickingQuery.pageInfo.currentPage"
                            :page-sizes="sfgPickingQuery.pageInfo.pageSizes"
                            :page-size="sfgPickingQuery.pageInfo.pageSize"
                            :total="sfgPickingQuery.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
            <el-dialog :visible.sync="editFormVisible" v-if="editFormVisible" width="65%" append-to-body>
                <sfg-picking-edit
                        :mode="mode"
                        :sfgPickingInfoEdit="sfgPickingInfoEdit"
                        @sfg-picking-finish="sfgPickingFinish"
                        @sfg-picking-cancel="sfgPickingCancel"
                ></sfg-picking-edit>
            </el-dialog>
        </el-container>
    </div>
</template>

<script>
    import sfgPickingModel from '../../../model/sfg/sfgPicking'
    import {Notification} from "element-ui";
    import moment from "moment";
    import sfgPickingEdit from './SfgPickingEdit.vue'
    import codeModel from "../../../model/basicdata/codeExlpain";
    import projectModel from "../../../model/project/project";
    import deptModel from "../../../model/basicdata/dept";
    import sfgWarehousingModel from "../../../model/sfg/sfgWarehousing";

    export default {
        components: {
            'sfg-picking-edit': sfgPickingEdit
        },
        name: 'SfgPickingList',
        data() {
            return {
                subDatas: [],
                selectedFoPL: [],
                expands: [],
                /*项目*/
                projectList: [],
                /*部门*/
                deptList: [],
                options: [{
                    value: '生产',
                    label: '生产'
                }, {
                    value: '维修',
                    label: '维修'
                }],
                sysCodes: [],
                sfgPickingInfoEdit: [],
                selected: [],
                editFormVisible: false,
                mode: 'add',
                sfgPickingQuery: {
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                sfgPickingData: [],
                buttonData: [
                    {
                        name: '新建',
                        event: 'addClick'
                    },
                    {
                        name: '删除',
                        event: 'deletesClicks'
                    },
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "submitForm('sfgPickingQuery')"
                    },
                    {
                        name: '重置',
                        event: 'resetForm("sfgPickingQuery")'
                    }
                ],
                rowButtonData: [
                    {
                        name: '编辑',
                        event: "editClick",
                        disabled: "scope.row.statusFlag=='已出库'"
                    },
                    {
                        name: "发料",
                        event: "updateSfgToSend",
                        disabled: "scope.row.statusFlag=='已出库'"
                    }]
            }
        },
        mounted() {
            this.init()
            this.list()
        },
        methods: {
            list() {
                sfgPickingModel.findSfgPickingByQuery(this.sfgPickingQuery).then((data) => {
                    this.sfgPickingData = data.entity.list
                    this.sfgPickingQuery.pageInfo.total = data.entity.totalCount
                })
                projectModel.findAllProjects().then((data) => {
                    this.projectList = data.entity
                })
                deptModel.listAll().then((data) => {
                    this.deptList = data.entity
                })
            },
            init() {
                let sysCode = {
                    codeNo: 'SFG_PICKING_STATUS',
                    orgId: '',
                }
                codeModel.findCodeByCodeNoAndOrgId(sysCode).then((data) => {
                    this.sysCodes = data.entity
                })
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.list()
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.sfgPickingQuery.sfgPickingStatus = ''
                this.sfgPickingQuery.projectId = ''
                this.sfgPickingQuery.sfgPickingNo = ''
                this.sfgPickingQuery.departmentId = ''
                this.sfgPickingQuery.purpose = ''
                this.sfgPickingQuery.sfgPickingDate = ''
            },
            updateSfgToStock(row) {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                sfgPickingModel.updateSfgToStock(row.id).then((data) => {
                    if (data.status == 'failed') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        loading.close()
                        return
                    }
                    Notification({
                        message: '发料成功',
                        type: 'success'

                    })
                    loading.close()
                    this.list()
                })
            },
            addClick() {
                this.mode = 'add'
                console.log("this.$store.state.user.deptId",this.$store.state)
                this.editFormVisible = true
            },
            deletesClicks() {
                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }
                let ids = []
                this.selected.forEach((row) => {
                    ids.push(row.id)
                })
                this.$confirm('此操作将永久删除勾选半成品出库单, 是否继续?', '提示', {
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
                    sfgPickingModel.deleteSfgPickingWithIds(ids.join(";")).then((data) => {
                        if (data.status == 'failed') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        Notification({
                            message: '删除成功',
                            type: 'success'

                        })
                        loading.close()
                        this.list()
                    })
                }).catch(() => {
                })

            },
            editClick(row) {
                sfgPickingModel.findSfgPickingAndDetailBySfgPickingId(row.id).then((data) => {
                    this.sfgPickingInfoEdit = data.entity
                    this.mode = 'edit'
                    this.editFormVisible = true
                })

            },
            updateSfgToSend(row) {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                sfgPickingModel.updateSfgToSend(row.id).then((data) => {
                    if (data.status == 'fail') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        loading.close()
                        return
                    }
                    Notification({
                        message: '发料成功',
                        type: 'success'
                    })
                    loading.close()
                    this.list()
                })
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.sfgPickingQuery.pageInfo.pageSize = val
                this.list()
            },
            handleCurrentChange(val) {
                this.sfgPickingQuery.pageInfo.currentPage = val
                this.list()
            },
            sfgPickingFinish() {
                this.editFormVisible = false
                this.init()
                this.list()
            },
            sfgPickingCancel() {
                this.editFormVisible = false
            },
            /*表格整行点击都会勾选
                 * 参数1 2 3 方法自带
                 */
            handleClickRowSelection(row, column, event) {
                this.$refs.handleClickRowSelection.toggleRowSelection(row);
            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD");
            },
            handleSelectionChangeForPL(selected) {
                this.selectedFoPL = selected
            },
            showSubData(row, expandedRows) {
                let id = row.id;
                if (this.expands.some((e) => e == row.id)) {
                    this.expands = []
                } else {
                    this.expands = []
                    this.expands.push(row.id)
                }
                //展示出库相关的半成品
                sfgPickingModel.findSfgPickingAndDetailBySfgPickingId(id).then((data) => {
                    this.subDatas = data.entity.sfgPickingDetails;
                    console.log("this.subDatas", this.subDatas)
                })
            },
            getRowKeys(row) {
                return row.id;
            },
        },
    }
</script>

<style>
</style>
