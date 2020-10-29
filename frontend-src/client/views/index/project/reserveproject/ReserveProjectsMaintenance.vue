<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true" id="projectList" :model="projectQuery" ref="projectQuery">
                            <el-form-item prop="projectName">
                                <el-input
                                        placeholder="地块编号"
                                        v-model="projectQuery.projectNo"
                                        style="width:150px"
                                        clearable
                                ></el-input>
                            </el-form-item>



                            <el-form-item  prop="provinceId">
                                <el-select  v-model="projectQuery.provinceId" placeholder="省" filterable clearable  @change="changeProvince">
                                    <el-option v-for="item in arrProvinces"
                                               :key="item.id"
                                               :label="item.deptName"
                                               :value="item.id"
                                    ></el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item prop="cityId">
                                <el-select  v-model="projectQuery.cityId" clearable filterable placeholder="市">
                                    <el-option v-for="item in arrCitys"
                                               :key="item.id"
                                               :label="item.deptName"
                                               :value="item.id"
                                    ></el-option>
                                </el-select>
                            </el-form-item>


                            <el-form-item prop="approvalStatus">
                                <el-select  v-model="projectQuery.approvalStatus" clearable placeholder="审批状态">
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
                        :data="projects"
                        size="mini"
                        stripe
                        border
                        @row-click="clickRow"
                        ref="moviesTable"
                        @selection-change="handleSelectionChange"
                        height="100%"
                        :rowIndex:="this.rowIndex"
                        router-link
                >
                    <el-table-column type="expand">
                        <template slot-scope="props">
                            <el-form label-position="left" inline class="demo-table-expand">
                                <el-form-item label="地块编号"><span>{{ props.row.projectNo }}</span></el-form-item>
                                <el-form-item label="地块数量"><span>{{ props.row.landNum }}</span></el-form-item>
                                <el-form-item label="结构类型"><span>{{ props.row.structuralStyle }}</span></el-form-item>
                                <el-form-item label="配建面积"><span>{{ props.row.constructionArea }}</span></el-form-item>
                                <el-form-item label="当年拟落实面积"><span>{{ props.row.implementedArea }}</span></el-form-item>
                                <el-form-item label="全/精装修面积"><span>{{ props.row.decorationArea }}</span></el-form-item>
                                <el-form-item label="EPC项目个数"><span>{{ props.row.epcNum }}</span></el-form-item>
                                <el-form-item label="省"><span>{{ props.row.provinceName }}</span></el-form-item>
                                <el-form-item label="市"><span>{{ props.row.cityName }}</span></el-form-item>
                                <el-form-item label="审批状态"><span>{{ props.row.approvalStatus }}</span></el-form-item>
                            </el-form>
                        </template>
                    </el-table-column>
                    <el-table-column type="index" label="序号" width="50"></el-table-column>
                    <el-table-column prop="projectNo" label="地块编号" width="150"></el-table-column>
                    <el-table-column prop="landNum" label="地块数量" width="150"></el-table-column>
                    <el-table-column prop="structuralStyle" label="结构类型" width="150"></el-table-column>
                    <el-table-column prop="constructionArea" label="配建面积" width="150"></el-table-column>
                    <el-table-column prop="implementedArea" label="当年拟落实面积" width="100"></el-table-column>
                    <el-table-column prop="decorationArea" label="全/精装修面积" width="100"></el-table-column>
                    <el-table-column prop="epcNum" label="EPC项目个数" width="150"></el-table-column>
                    <el-table-column prop="provinceName" label="省" ></el-table-column>
                    <el-table-column prop="cityName" label="市"></el-table-column>
                    <el-table-column prop="approvalStatus" align="center" label="审批状态">
                        <template slot-scope="scope">
                            <a href="javascript:void(0);" style="color: #409EFF;" @click.stop="findApprovals(scope.row)">
                                {{scope.row.approvalStatus}}
                            </a>
                        </template>
                    </el-table-column>

                    <el-table-column label="操作"
                                     fixed="right"
                                     :width="rowButtonGroupWidth(rowButtonData)">
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
                            :current-page="projectQuery.pageInfo.currentPage"
                            :page-sizes="projectQuery.pageInfo.pageSizes"
                            :page-size="projectQuery.pageInfo.pageSize"
                            :total="projectQuery.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    ></el-pagination>
                </el-row>
            </el-footer>
        </el-container>
        <el-dialog
                :title="mode=='add'?'新建储备项目':'编辑储备项目'"
                v-if="editFormVisible"
                :visible.sync="editFormVisible"
        >
            <model-edit
                    :edit-form="editForm"
                    :mode="mode"
                    @model-edit-finish="projectEditFinish"
                    @model-edit-cancel="projectEditCancel"
            ></model-edit>
        </el-dialog>

        <!--送检-->
        <el-dialog
                :visible.sync="checkEditFormVisible"
                width="30%"
        >
            <checkout-status
                    :edit-form="editForm"
                    @checkout-status-finish="checkoutStatusFinish"
                    @checkout-status-cancel="checkoutStatusCancel"
            ></checkout-status>
        </el-dialog>


        <el-dialog
                title="审批详情"
                :visible.sync="editFormVisible1"
                width="80%"
                top="6vh"
        >
            <div class="gjnav">
                <div class="headnav">{{orgName}}</div>
                <div class="bodynav share-box">
                    <el-timeline >
                        <el-timeline-item v-for="(item, index) in approvalList" :key="index" :timestamp="item.dateCreated" placement="top">
                            <el-card>
                                <h4>审批时间:{{item.dateCreated}}</h4>
                                <h4>审批层级:{{item.approvalLevel}}</h4>
                                <h4>审批部门:{{item.deptName}}</h4>
                                <h4>审批结果:{{item.approvalStatus}}</h4>
                                <p> 审批意见:{{item.reason}}</p>
                                <h4>审批人:{{item.approvalerName}}</h4>
                            </el-card>
                        </el-timeline-item>
                    </el-timeline>
                </div>
            </div>
        </el-dialog>
    </div>
</template>
<script>
    import approvalModel from "../../../../model/system/approval";
    import serviceConfig from "../../../../../server.config.js";
    import clientModel from "../../../../model/client-model";
    import codeExplainContent from '../../../../model/basicdata/codeExlpainContent'
    import deptModel from '../../../../model/basicdata/dept'
    import projectModel from "../../../../model/project/reserveproject";
    import {Notification} from "element-ui";
    import ProjectsEdit from "./ReserveProjectsEdit.vue";
    import CheckoutStatus from '../ApprovalStatus.vue';
    import moment from "moment";

    export default {
        components: {
            "model-edit": ProjectsEdit,
            'checkout-status': CheckoutStatus
        },
        mounted() {
            this.projectList();
            this.init();
        },
        props: ["rowIndex"],
        data() {
            return {
                arrBuilding: [],
                dataBuild: [],
                dataHouseType: [],
                show: "",
                approvalList:[],
                orgName:"",
                editFormVisible1:false,
                projectQuery: {
                    projectName: "",
                    customer: "",
                    workPlace: "",
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    }
                },
                projectId: "", //文件上传时传入的id
                projects: [],
                selected: [],
                //编辑表单
                mode: "",
                editFormTitle: "",
                editFormVisible: false,
                checkEditFormVisible: false,
                editForm: null,
                //传值id
                projectInfo: "",
                arrProvinces: [],
                arrCitys: [],
                arrApprovalStates:[],
                buttonData: [
                    {
                        name: '新建',
                        event: 'addClick'
                    },
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "submitForm('projectQuery')"
                    },
                    {
                        name: '重置',
                        event: 'resetForm("projectQuery")'
                    }
                ],
                rowButtonData: [
                    {
                        name: '编辑',
                        event: "editClick",
                    },
                    {
                        name: '删除',
                        event: "deleteClick",
                    },
                    {
                        name: '审批',
                        event: "check",
                        display:'scope.row.isApproval',
                    },
                ]
            };
        },
        methods: {
            findApprovals(row) {
                this.orgName = row.orgName;
                let query = {
                    codeType:"RESERVE_PROJECT_APPROVAL_STATE",
                    associationId:row.id
                }
                approvalModel.findApprovalsInfo(query).then(data => {
                    this.approvalList = data.entity
                    this.editFormVisible1 = true
                })
            },
            check(row) {
                this.editForm = row
                this.editForm.codeName = "RESERVE_PROJECT_APPROVAL_STATE";
                this.checkEditFormVisible = true
            },
            changeProvince(){
                let id = this.projectQuery.provinceId;
                //   this.entity.cityId = ""
                this.$set(this.projectQuery,'cityId','')
                this.arrCitys = []
                let query = {
                    id:id,
                    orgId:0
                }
                deptModel.findDepartmentsByParentId(query).then(data => {
                    this.arrCitys = data.entity;
                })
            },
            init(){

                deptModel.findDepartmentByLevel(0).then(data => {
                    this.arrProvinces = data.entity;
                })

                codeExplainContent.getAllByCodeNo("RESERVE_PROJECT_APPROVAL_STATE").then(data => {
                    this.arrApprovalStates = data.entity;
                })

            },

            /**
             *格式化显示时间格式
             */
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            //编辑表单
            projectEditFinish() {
                this.editFormVisible = false;
                this.projectList();
            },
            projectEditCancel() {
                this.editFormVisible = false;
            },
            submitForm(formName) {
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.projectList();
                    } else {
                        console.log("error submit!!");
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },

            checkoutStatusFinish() {
                this.checkEditFormVisible = false
                this.projectList()
            },
            checkoutStatusCancel() {
                this.checkEditFormVisible = false
                this.projectList()
            },
            addClick() {
                this.mode = "add";
                let approvalStatus =  "省级审批中"
                if(this.arrApprovalStates.length>0){
                    approvalStatus = this.arrApprovalStates[0].codeName;
                }
                this.editForm = {
                    approvalStatus: approvalStatus
                };
                this.editFormVisible = true;
            },
            editClick(row) {
                this.$store.commit("startLoading");
                projectModel.projectLoad(row.id).then(data => {
                    this.mode = "update";
                    this.editForm = data.entity;
                    this.$store.commit("endLoading");
                    this.editFormVisible = true;
                });
            },
            deleteClick(row) {
                this.$confirm("此操作将永久删除该项目, 是否继续?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                })
                    .then(() => {
                        const loading = this.$loading({
                            lock: true,
                            text: "Loading",
                            spinner: "el-icon-loading",
                            background: "rgba(0, 0, 0, 0.7)"
                        });
                        projectModel.projectDelete(row.id).then(data => {
                            if (data.status == "fail") {
                                Notification({
                                    message: data.msg,
                                    type: "warning"
                                });
                                loading.close();
                                return;
                            }
                            Notification({
                                message: "删除成功",
                                type: "success"
                            });
                            loading.close();
                            this.projectList();
                        });
                    })
                    .catch(() => {
                    });
            },
            projectList() {
                projectModel.projectList(this.projectQuery).then(data => {
                    let projects = data.entity.content;
                    this.projects = projects;
                    this.projectQuery.pageInfo.total = data.entity.totalElements;
                });
            },
            handleSelectionChange(selected) {
                this.selected = selected;
            },
            handleSizeChange(val) {
                this.pageInfo.currentPage = 1
                this.projectQuery.pageInfo.pageSize = val;
                this.projectList();
            },
            handleCurrentChange(val) {
                this.projectQuery.pageInfo.currentPage = val;
                this.projectList();
            },
            handlebriefCol() {
                this.show = false;
            },
            handleAllCol() {
                this.show = true;
            },
            // 点击整行都可以进行勾选
            clickRow(row) {
                this.$refs.moviesTable.toggleRowSelection(row)
            },
            downloadFinish() {
                this.editFormDownloadVisible = false
            },
            downloadCancel() {
                this.editFormDownloadVisible = false
            },
        },

    };
</script>

<style>
    .demo-table-expand {
        font-size: 0;
    }
    .demo-table-expand label {
        width: 90px;
        color: #99a9bf;
    }
    .demo-table-expand .el-form-item {
        margin-right: 0;
        margin-bottom: 0;
        width: 50%;
    }
</style>