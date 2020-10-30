<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true" id="projectList" :model="projectQuery" ref="projectQuery">
                            <el-form-item prop="projectName">
                                <el-input
                                        placeholder="项目名称"
                                        v-model="projectQuery.projectName"
                                        style="width:150px"
                                        clearable
                                ></el-input>
                            </el-form-item>
                            <el-form-item prop="customer">
                                <el-input
                                        placeholder="建设单位"
                                        v-model="projectQuery.customer"
                                        style="width:150px"
                                        clearable
                                ></el-input>
                            </el-form-item>
                            <el-form-item prop="workPlace">
                                <el-input
                                        placeholder="项目地址"
                                        v-model="projectQuery.workPlace"
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
                    <el-table-column type="index" label="序号" width="50"></el-table-column>
                    <el-table-column prop="projectNo" label="项目编号" width="150"></el-table-column>
                    <el-table-column prop="projectName" label="项目名称" width="150"></el-table-column>
                    <el-table-column prop="workPlace" label="项目地址" width="150"></el-table-column>
                    <el-table-column prop="customer" label="建设单位" width="150"></el-table-column>
                    <el-table-column prop="productionUnit" label="构件生产单位" width="100"></el-table-column>
                    <el-table-column prop="assemblyRate" label="装配率" width="100"></el-table-column>
                    <el-table-column prop="undertaking" label="施工单位" width="150"></el-table-column>
                    <el-table-column prop="projectScale" label="工程类型" width="100"></el-table-column>

                    <el-table-column prop="structuralStyle" label="结构形式" ></el-table-column>
                    <el-table-column prop="startDate" label="开始时间" width="100"></el-table-column>
                    <el-table-column prop="endDate" label="结束时间" width="100"></el-table-column>
                    <el-table-column prop="projectProgress" label="工程进度" width="100"></el-table-column>
                    <el-table-column prop="isFullDecoration" label="是否为全装修或装配化装修">
                        <template slot-scope="scope">
                            <span v-if="scope.row.isFullDecoration">是</span>
                            <span v-else>否</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="isInvested" label="是否为政府投资工程">
                        <template slot-scope="scope">
                            <span v-if="scope.row.isInvested">是</span>
                            <span v-else>否</span>
                        </template>
                    </el-table-column>

                    <el-table-column prop="isEPC" label="是否采用工程总承包(EPC)">
                        <template slot-scope="scope">
                            <span v-if="scope.row.isEPC">是</span>
                            <span v-else>否</span>
                        </template>
                    </el-table-column>
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
                :title="mode=='add'?'新建项目':'编辑项目'"
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
    import projectModel from "../../../../model/project/project";
    import {Notification} from "element-ui";
    import ProjectsEdit from "./ProjectsEdit.vue";
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
                approvalList:[],
                orgName:"",
                editFormVisible1:false,
                show: "",
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
                    codeType:"PROJECT_APPROVAL_STATE",
                    associationId:row.id
                }
                approvalModel.findApprovalsInfo(query).then(data => {
                    this.approvalList = data.entity
                    this.editFormVisible1 = true
                })
            },
            check(row) {
                this.editForm = row
                this.editForm.codeName = "PROJECT_APPROVAL_STATE";
                this.checkEditFormVisible = true
            },
            changeProvince(){
                let id = this.projectQuery.provinceId;
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

                codeExplainContent.getAllByCodeNo("PROJECT_APPROVAL_STATE").then(data => {
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
            addClick() {
                this.mode = "add";
                let approvalStatus =  "市级审批中"
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
            deletesClick() {
                if (this.selected.length == 0) {
                    this.$message("请选择删除项");
                    return;
                }
                let ids = [];
                this.selected.forEach(row => {
                    ids.push(row.id);
                });
                this.$confirm("此操作将永久删除勾选项目, 是否继续?", "提示", {
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
                        projectModel.projectDeletes(ids.join(";")).then(data => {
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

            // 点击整行都可以进行勾选
            clickRow(row) {
                this.$refs.moviesTable.toggleRowSelection(row)
            },
            checkoutStatusFinish() {
                this.checkEditFormVisible = false
                this.projectList()
            },
            checkoutStatusCancel() {
                this.checkEditFormVisible = false
                this.projectList()
            }
        },

    };
</script>