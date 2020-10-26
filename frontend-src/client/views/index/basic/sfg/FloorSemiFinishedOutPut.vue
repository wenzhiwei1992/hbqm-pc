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

                            <el-form-item prop="floorStatus">
                                <el-select v-model="sfgOutPutRecord.floorStatus" clearable placeholder="请选择">
                                    <el-option
                                            v-for="item in options"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="outDateRange">
                                <el-date-picker
                                        v-model="sfgOutPutRecord.outDateRange"
                                        type="daterange"
                                        align="right"
                                        unlink-panels
                                        range-separator="-"
                                        start-placeholder="入库开始日期"
                                        end-placeholder="入库结束日期"
                                        :default-time="['00:00:00', '23:59:59']"
                                        value-format="yyyy-MM-dd"
                                        style="width:300px;">
                                </el-date-picker>
                            </el-form-item>
                            <el-form-item prop="putDateRange">
                                <el-date-picker
                                        v-model="sfgOutPutRecord.putDateRange"
                                        type="daterange"
                                        align="right"
                                        unlink-panels
                                        range-separator="-"
                                        start-placeholder="出库开始日期"
                                        end-placeholder="出库结束日期"
                                        :default-time="['00:00:00', '23:59:59']"
                                        value-format="yyyy-MM-dd"
                                        style="width:300px;">
                                </el-date-picker>
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
                            prop="floorStatus"
                            label="楼层状态"
                    >
                        <template slot-scope="scope">
                            <span :class="{status_color1: scope.row.floorStatus == '待生产' ,
                                          status_color2 : scope.row.floorStatus == '已入库',
                                          status_color3 : scope.row.floorStatus == '已领用'}">{{scope.row.floorStatus}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="addTime"
                            label="入库时间"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="addName"
                            label="入库人"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="deliveryTime"
                            label="出库时间"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="deliveryName"
                            label="出库人"
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
            <!-- 批量下载状态为待生产的楼层二维码 -->
            <el-dialog
                    append-to-body
                    :visible.sync="editFormVisibleQR"
                    width="40%"
                    v-if="editFormVisibleQR"
            >
                <shelf-qr-code-edit
                        :shelfNoQRCode="shelfNoQRCode"
                        :shelfIdQRCode="shelfIdQRCode"
                ></shelf-qr-code-edit>
            </el-dialog>
            <el-dialog
                    append-to-body
                    :visible.sync="editFormVisibleQRBatch"
                    width="40%"
                    v-if="editFormVisibleQRBatch"
            >
                <shelf-qr-code-edit-batch
                        :shelfNoQRCodes="floorQRNum"
                        :shelfNameQRCodes="floorQRStatus"
                        :shelfIdQRCodes="floorIdQRCodes"
                ></shelf-qr-code-edit-batch>
            </el-dialog>
        </el-container>
    </div>
</template>

<script>
    import sfgModel from '../../../../model/basicdata/sfg'
    import sfgOutPutModel from '../../../../model/basicdata/sfgOutPut'
    import moment from "moment";
    import floorModel from "../../../../model/project/floor";
    import buildingModel from "../../../../model/project/building";
    import projectModel from "../../../../model/project/project";
    import {Notification} from "element-ui";
    import ShelfQRcodeEdit from "../yard/ShelfQRcodeEdit.vue";
    import ShelfQRcodeEditBatch from "../yard/ShelfQRcodeEditBatch.vue";

    export default {
        components: {
            'shelf-qr-code-edit': ShelfQRcodeEdit,
            'shelf-qr-code-edit-batch': ShelfQRcodeEditBatch,
        },
        data() {
            return {
                editFormVisibleQRBatch:false,
                floorQRStatus: [],
                //floorQRStatus: '',
                floorQRNum:[],
                floorIdQRCodes: [],
                editFormVisibleQR: false,
                shelfNoQRCode:'',
                shelfIdQRCode: '',
                /*项目*/
                projectList: [],
                buildings: [],
                floors: [],
                //编辑操作传值
                sfgInfoEdit: [],
                mode: '',
                editFormVisible: false,
                sfgOutPutRecord: {
                    floorId: '',
                    buildingId:'',
                    floorStatus:'',
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
                        name: '下载二维码',
                        event: 'downloadQrCode'
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
                options: [{
                    value: '待生产',
                    label: '待生产'
                }, {
                    value: '已入库',
                    label: '已入库'
                }, {
                    value: '已领用',
                    label: '已领用'
                }],
            }
        },
        mounted() {
            this.list()
        },
        methods: {
            showQRcode(row) {
                this.shelfIdQRCode = row.floorId
                this.shelfNoQRCode = row.floorNum
                this.editFormVisibleQR = true
            },
            downloadQrCode(){
                if(this.selected.length){
                    this.floorIdQRCodes = []
                    this.floorQRNum = []
                    this.floorQRStatus = []
                        this.selected.map(val=>{
                        this.floorIdQRCodes.push(val.floorId)
                        this.floorQRNum.push(val.floorNum)
                        this.floorQRStatus.push(val.buildCode)
                        })
                    this.editFormVisibleQRBatch = true
                }else{
                    Notification({
                        message: this.GLOBAL.PROJECT_MSG.SELECTED_BATCH_PROJECT,
                        type: 'warning'
                    })
                }
            },
            list() {
                sfgOutPutModel.floorSemiFinishedOutPut(this.sfgOutPutRecord).then((data) => {
                    this.sfgList = data.entity.list
                    this.sfgOutPutRecord.pageInfo.total = data.entity.totalCount
                })
                /*查询项目*/
                projectModel.findAllProjects().then((data) => {
                    this.projectList = data.entity
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
    .status_color1 {
        font-weight: bolder;
        color: red;
    }

    .status_color2 {
        font-weight: bolder;
        color: #67c23a;
    }

    .status_color3 {
        font-weight: bolder;
        color: #409eff;
    }
</style>
