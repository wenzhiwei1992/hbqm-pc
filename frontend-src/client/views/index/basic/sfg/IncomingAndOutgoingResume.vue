<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                :model="sfgOutPutRecord"
                                ref="sfgOutPutRecord"
                                style="padding-bottom:8px;"
                        >
                            <el-form-item prop="sfgWarehousingNo">
                                <el-input
                                        placeholder="单据号"
                                        v-model="sfgOutPutRecord.sfgWarehousingNo"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="status">
                                <el-select
                                        placeholder="状态"
                                        v-model="sfgOutPutRecord.status"
                                        style="width:150px"
                                        clearable
                                >
                                    <el-option
                                            v-for="item in options"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="projectId">
                                <el-select v-model="sfgOutPutRecord.projectId" style="width: 150px"
                                           @change="changeProject"
                                           placeholder="项目"
                                           clearable>
                                    <el-option
                                            v-for="item in projectList"
                                            :key="item.id"
                                            :label="item.projectName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="buildingId" >
                                <el-select
                                        style="width:110px"
                                        placeholder="楼栋"
                                        v-model="sfgOutPutRecord.buildingId"
                                        :disabled="sfgOutPutRecord.projectId==''"
                                        size="mini"
                                        clearable
                                        @change="changeBuilding"
                                >
                                    <el-option
                                            v-for="item in buildings"
                                            :key="item.id"
                                            :label="item.buildCode"
                                            :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="floorId" >
                                <el-select
                                        style="width:110px"
                                        placeholder="楼层"
                                        :disabled="sfgOutPutRecord.buildingId==''"
                                        v-model="sfgOutPutRecord.floorId"
                                        clearable
                                        size="mini"
                                >
                                    <el-option
                                            v-for="item in floors"
                                            :key="item.id"
                                            :label="item.floorNum"
                                            :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="sfgWarehousingDate" >
                                <el-date-picker
                                        v-model="sfgOutPutRecord.sfgEndDate"
                                        :formatter="formatterDate"
                                        type="date"
                                        style="width: 150px"
                                        placeholder="出入库时间">
                                </el-date-picker>
                            </el-form-item>
                            <el-form-item prop="departmentId">
                                <el-select v-model="sfgOutPutRecord.departmentId" style="width: 150px" placeholder="部门"
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
                            <!--<el-form-item prop="departmentId">
                                <el-select v-model="sfgOutPutRecord.departmentId" style="width: 150px" placeholder="生产班组"
                                           clearable
                                           collapse-tags>
                                    <el-option
                                            v-for="item in teamInfoList"
                                            :key="item.id"
                                            :label="item.teamName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>-->
                            <el-form-item prop="sfgNo">
                                <el-input
                                        placeholder="半成品编码"
                                        v-model="sfgOutPutRecord.sfgNo"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="sfgName">
                                <el-input
                                        placeholder="半成品名称"
                                        v-model="sfgOutPutRecord.sfgName"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>


                        </el-form>
                    </el-col>
                    <el-col>
                        <my-search-button-group :model='searchButtonData'></my-search-button-group>
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
                        :data="sfgList"
                        @selection-change="handleSelectionChange"
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                        size="mini"
                        :default-sort="{prop: 'materialStorageId',}"
                        stripe
                        border
                        height="100%"
                >
                    <el-table-column
                            type="selection"
                            width="39"
                    >
                    </el-table-column>
                    <el-table-column
                            type="index"
                            label="序号"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="codeName"
                            label="状态"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="sfgWarehousingNo"
                            label="单据号"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="projectName"
                            label="项目"
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
                            prop="departmentName"
                            label="部门"
                    >
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
                            prop="stockName"
                            label="仓库"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="inStockNum"
                            label="库存量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="numIn"
                            label="出入库量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="unitCNName"
                            label="单位"
                    >
                    </el-table-column>
                </el-table>
                <el-table
                        :data="exportdatas"
                        @selection-change="handleSelectionChange"
                        size="mini"
                        :default-sort="{prop: 'materialStorageId',}"
                        border
                        id="exportXlsx"
                        hidden
                        height="100%"
                >
                    <el-table-column
                            prop="codeName"
                            label="状态"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="sfgWarehousingNo"
                            label="单据号"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="projectName"
                            label="项目"
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
                            prop="departmentName"
                            label="部门"
                    >
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
                            prop="stockName"
                            label="仓库"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="inStockNum"
                            label="库存量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="numIn"
                            label="出入库量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="unitCNName"
                            label="单位"
                    >
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer ref="footOperationBar">
                <el-row>
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="sfgOutPutRecord.pageInfo.currentPage"
                            :page-sizes="sfgOutPutRecord.pageInfo.pageSizes"
                            :page-size="sfgOutPutRecord.pageInfo.pageSize"
                            :total="sfgOutPutRecord.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
            <el-dialog :visible.sync="editFormVisible" v-if="editFormVisible" width="65%" append-to-body>
                <semi-finished-goods-edit
                        :mode="mode"
                        :sfgInfoEdit="sfgInfoEdit"
                        @sfg-edit-finish="sfgEditFinish"
                        @sfg-edit-cancel="sfgEditCancel"
                ></semi-finished-goods-edit>
            </el-dialog>
        </el-container>
    </div>
</template>

<script>
    import sfgModel from '../../../../model/basicdata/sfg'
    import sfgOutPutModel from '../../../../model/basicdata/sfgOutPut'
    import xlsx from "xlsx";
    import fileSaver from "file-saver";
    import moment from "moment";
    import floorModel from "../../../../model/project/floor";
    import buildingModel from "../../../../model/project/building";
    import projectModel from "../../../../model/project/project";
    import deptModel from "../../../../model/basicdata/dept";
    import teamInfoModel from "../../../../model/basicdata/teamInfo";

    export default {
        data() {
            return {
                options: [{
                    value: '已出库',
                    label: '已出库'
                }, {
                    value: '已入库',
                    label: '已入库'
                }],
                /*班组*/
                teamInfoList: [],
                /*部门*/
                deptList: [],
                /*项目*/
                projectList: [],
                buildings: [],
                floors: [],
                //编辑操作传值
                sfgInfoEdit: [],
                mode: '',
                editFormVisible: false,
                sfgOutPutRecord: {
                    sfgWarehousingNo:'',
                    status:'',
                    departmentId:'',
                    sfgNo:'',
                    sfgName: '',
                    sfgEndDate:'',
                    floorId: '',
                    buildingId:'',
                    projectId:'',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                sfgList: [],
                exportdatas: [],
                buttonData: [
                    {
                        name: '导出',
                        event: 'sfgExport'
                    },
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "submitForm('sfgOutPutRecord')"
                    },
                    {
                        name: '重置',
                        event: 'resetForm("sfgOutPutRecord")'
                    }
                ],

            }
        },
        mounted() {
            this.list()
        },
        methods: {
            //导出
            sfgExport(){
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                let page = this.sfgOutPutRecord.pageInfo.currentPage;
                let size = this.sfgOutPutRecord.pageInfo.pageSize;
                this.sfgOutPutRecord.pageInfo.currentPage = 1;
                this.sfgOutPutRecord.pageInfo.pageSize = this.sfgOutPutRecord.pageInfo.total;
                //TODO 分页
                sfgOutPutModel.findOutPutRecord(this.sfgOutPutRecord).then((data) => {
                    this.exportdatas = data.entity.list;
                    setTimeout(() => {
                        var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
                        excelName = "半成品出入履历" + excelName;

                        /* generate workbook object from table */
                        let table = document.querySelector('#exportXlsx');
                        var wb = xlsx.utils.table_to_book(table);
                        /* get binary string as sfgOutPutRecord */
                        var wbout = xlsx.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'})
                        try {
                            fileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), excelName)
                        } catch (e) {
                            if (typeof console !== 'undefined') console.log(e, wbout)
                        }
                        this.sfgOutPutRecord.pageInfo.pageSize = size;
                        this.sfgOutPutRecord.pageInfo.currentPage = page;
                        loading.close();
                        return wbout;
                    }, 500);

                }).catch(err => {
                })
            },
            list() {
                sfgOutPutModel.findOutPutRecord(this.sfgOutPutRecord).then((data) => {
                    this.sfgList = data.entity.list
                    this.sfgOutPutRecord.pageInfo.total = data.entity.totalCount
                })
                /*部门*/
                deptModel.listAll().then((data) => {
                    this.deptList = data.entity
                })
                /*查询项目*/
                projectModel.findAllProjects().then((data) => {
                    this.projectList = data.entity
                })
                teamInfoModel.getTeamByOrg().then((data) => {
                    this.teamInfoList = data.entity
                })
                /*查询楼栋*/
                /*buildingModel.findByProjectId(this.projectId).then(data => {
                    this.buildings = data.entity
                    console.log("------->this.buildings",this.buildings)
                })*/

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
            },
            editClick(row) {
                sfgModel.findSemiFinishedGoodsVoById(row.id).then((data) => {
                    this.sfgInfoEdit = data.entity
                    this.mode = 'edit'
                    this.editFormVisible = true
                })

            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.sfgOutPutRecord.pageInfo.pageSize = val
                this.list()
            },
            handleCurrentChange(val) {
                this.sfgOutPutRecord.pageInfo.currentPage = val
                this.list()
            },
            sfgEditFinish() {
                this.editFormVisible = false
                this.list()
            },
            sfgEditCancel() {
                this.editFormVisible = false
            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD");
            },
            changeProject() {
                buildingModel.findByProjectId(this.sfgOutPutRecord.projectId).then(data => {
                    this.buildings = data.entity
                    this.sfgOutPutRecord.buildingId = ''
                    this.sfgOutPutRecord.floorId = ''
                    this.floors = []
                })
            },
            changeBuilding(id) {
                this.sfgOutPutRecord.floorId = ''
                this.floors = []
                if (id == undefined || id == '') {
                    return
                }
                floorModel.findByBuildingIdAndProjectId(id, this.sfgOutPutRecord.projectId).then((data) => {
                    this.floors = data.entity
                    this.sfgOutPutRecord.floorId = ''
                })
            },
        },
    }
</script>

<style scoped>

</style>
