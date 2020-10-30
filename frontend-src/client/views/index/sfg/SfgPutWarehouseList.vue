<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                class="form-inline"
                                :model="sfgWarehousingQuery"
                                ref="sfgWarehousingQuery"
                        >
                            <el-form-item>
                                <el-input
                                        v-model="sfgWarehousingQuery.sfgWarehousingNo"
                                        placeholder="入库编码"
                                        clearable
                                        style="width: 135px"
                                ></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-select
                                        v-model="sfgWarehousingQuery.status"
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
                                <el-select
                                        v-model="sfgWarehousingQuery.type"
                                        placeholder="来源"
                                        clearable
                                        style="width: 135px"
                                        collapse-tags
                                >
                                    <el-option
                                            label="自产"
                                            value="0"
                                    ></el-option>
                                    <el-option
                                            label="外购"
                                            value="1"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item>
                                <el-date-picker
                                        v-model="sfgWarehousingQuery.sfgWarehousingDate"
                                        align="right"
                                        type="date"
                                        style="width: 135px"
                                        placeholder="入库时间"
                                        :formatter="formatterDate"
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
                    <my-button-group :model="buttonData"></my-button-group>
                </el-row>
            </el-header>
            <el-main
                    ref="mainContent"
                    :style="`height:${this.$store.state.mainContentHeight}px`"
            >
                <el-table
                        :data="sfgWarehousingData"
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
                            prop="sfgWarehousingNo"
                            label="入库编码"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="statusStr"
                            label="状态"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="typeStr"
                            label="来源"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="teamName"
                            label="班组"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="stockNo"
                            label="仓库"
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
                            :current-page="sfgWarehousingQuery.pageInfo.currentPage"
                            :page-sizes="sfgWarehousingQuery.pageInfo.pageSizes"
                            :page-size="sfgWarehousingQuery.pageInfo.pageSize"
                            :total="sfgWarehousingQuery.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
            <el-dialog :visible.sync="editFormVisible" v-if="editFormVisible" width="65%" append-to-body>
                <sfg-put-warehouse-edit
                        :mode="mode"
                        :sfgWarehousingInfoEdit="sfgWarehousingInfoEdit"
                        @sfg-put-warehouse-finish="sfgPutWarehouseEditFinish"
                        @sfg-put-warehouse-cancel="sfgPutWarehouseEditCancel"
                ></sfg-put-warehouse-edit>
            </el-dialog>
        </el-container>
    </div>
</template>

<script>
    import sfgPutWarehouseEdit from './SfgPutWarehouseEdit.vue'
    import sfgWarehousingModel from '../../../model/sfg/sfgWarehousing'
    import {Notification} from "element-ui";
    import codeModel from "../../../model/basicdata/codeExlpain";
    import moment from "moment";
    import sfgModel from "../../../model/basicdata/sfg";

    export default {
        name: 'semiFinishedGoodsPutWarehouse',
        components: {
            'sfg-put-warehouse-edit': sfgPutWarehouseEdit
        },
        data() {
            return {
                subDatas: [],
                selectedFoPL: [],
                expands: [],
                sysCodes: [],
                sfgWarehousingInfoEdit: [],
                selected: [],
                editFormVisible: false,
                mode: 'add',
                sfgWarehousingQuery: {
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                sfgWarehousingData: [],
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
                        event: "submitForm('sfgWarehousingQuery')"
                    },
                    {
                        name: '重置',
                        event: 'resetForm("sfgWarehousingQuery")'
                    }
                ],
                rowButtonData: [
                    {
                        name: '编辑',
                        event: "editClick",
                        disabled: "scope.row.statusFlag=='已入库'"
                    },
                    {
                        name: "入库",
                        event: "updateSfgToStock",
                        disabled: "scope.row.statusFlag=='已入库'"

                    }]
            }
        },
        mounted() {
            this.init()
            this.list()
        },
        methods: {
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
                this.sfgWarehousingQuery.status = ''
                this.sfgWarehousingQuery.type = ''
                this.sfgWarehousingQuery.sfgWarehousingNo = ''
                this.sfgWarehousingQuery.sfgWarehousingDate = ''
            },
            list() {
                sfgWarehousingModel.findSfgWarehousingByQuery(this.sfgWarehousingQuery).then((data) => {
                    this.sfgWarehousingData = data.entity.list
                    this.sfgWarehousingQuery.pageInfo.total = data.entity.totalCount
                })
            },
            init() {
                let sysCode = {
                    codeNo: 'SFG_WAREHOUSING_STATUS',
                    orgId: '',
                }
                codeModel.findCodeByCodeNoAndOrgId(sysCode).then((data) => {
                    this.sysCodes = data.entity
                })
            },
            updateSfgToStock(row) {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                sfgWarehousingModel.updateSfgToStock(row.id).then((data) => {
                    if (data.status == 'failed') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        loading.close()
                        return
                    }
                    Notification({
                        message: '入库成功',
                        type: 'success'

                    })
                    loading.close()
                    this.list()
                })
            },
            addClick() {
                this.mode = 'add'
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
                this.$confirm('此操作将永久删除勾选半成品入库单, 是否继续?', '提示', {
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
                    sfgWarehousingModel.deleteSfgWarehousing(ids.join(";")).then((data) => {
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
                sfgWarehousingModel.findSfgWarehousingAndDetailById(row.id).then((data) => {
                    this.sfgWarehousingInfoEdit = data.entity
                    if (this.sfgWarehousingInfoEdit.type === 0) {
                        this.sfgWarehousingInfoEdit.type = '0'
                    } else {
                        this.sfgWarehousingInfoEdit.type = '1'
                    }
                    this.mode = 'edit'
                    this.editFormVisible = true
                })

            },

            handleSelectionChange(selected) {
                this.selected = selected
            },
            sfgPutWarehouseEditFinish() {
                this.editFormVisible = false
                this.init()
                this.list()
            },
            sfgPutWarehouseEditCancel() {
                this.editFormVisible = false
            },
            handleSizeChange(val) {
                this.sfgWarehousingQuery.pageInfo.pageSize = val
                this.list()
            },
            handleCurrentChange(val) {
                this.sfgWarehousingQuery.pageInfo.currentPage = val
                this.list()
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
                //展示入库相关的半成品
                sfgWarehousingModel.findSfgWarehousingAndDetailById(id).then((data) => {
                    this.subDatas = data.entity.sfgWarehousingDetails;
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
