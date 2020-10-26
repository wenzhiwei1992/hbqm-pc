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
                                        placeholder="客户名称"
                                        v-model="projectQuery.customer"
                                        style="width:150px"
                                        clearable
                                ></el-input>
                            </el-form-item>
                            <el-form-item prop="workPlace">
                                <el-input
                                        placeholder="项目地点"
                                        v-model="projectQuery.workPlace"
                                        style="width:150px"
                                        clearable
                                ></el-input>
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
                    <el-table-column type="selection" width="55"></el-table-column>
                    <el-table-column type="index" label="序号" width="50"></el-table-column>
                    <el-table-column sortable prop="id" v-if="show" label="ID" width="150"></el-table-column>
                    <el-table-column prop="projectNo" label="项目编号" width="150"></el-table-column>
                    <el-table-column prop="projectName" label="项目名称" width="150"></el-table-column>
                    <el-table-column prop="workPlace" label="项目地点" width="150"></el-table-column>
                    <el-table-column prop="customer" label="客户名称" width="150"></el-table-column>
                    <el-table-column prop="undertaking" label="标识单位" width="150"></el-table-column>
                    <el-table-column prop="buildingList" label="楼栋数" width="100">
                        <!-- <router-link :to="{path:'/Building',query:{id:projects.id}}"  :data-types="projects.types" class="link-des" :id='projects.id'>link</router-link>-->
                    </el-table-column>
                    <el-table-column prop="houseTypeList" label="户型数" width="100"></el-table-column>
                    <el-table-column prop="productNums" label="总构件数" width="100"></el-table-column>
                    <el-table-column prop="productVols" label="总体积（m³）" width="100"></el-table-column>
                    <el-table-column
                            label="logo"
                            align="center"
                            width="100"
                    >
                        <template slot-scope="scope">
                            <el-tooltip :content="scope.row.logoPath" placement="top" effect="light">
                                <a :href="scope.row.baseUrl+scope.row.logoPath" v-if="scope.row.logoPath"  target="_blank">
                                    查看
                                </a>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                    <el-table-column prop="dateCreated" label="创建时间" :formatter="formatterDate" width="150"></el-table-column>
                    <el-table-column label="操作"
                                     fixed="right"
                                     :width="rowButtonGroupWidth(rowButtonData)">
                        <template slot-scope="scope">
                            <my-row-button-group
                                    :row="scope.row"
                                    :model="rowButtonData"
                            ></my-row-button-group>
                           <!-- <el-button size="mini" type="primary" @click.stop="houseTypeAndBuildClick(scope.row)">户型楼栋
                            </el-button>
                            <el-button size="mini" @click.stop="editClick(scope.row)">编辑</el-button>-->
                            <!--            <el-button type="danger" size="mini" @click="deleteClick(scope.row)">删除</el-button>-->
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
        <el-dialog
                :title="buildingAndHouseTitle"
                :visible.sync="buildingAndHouseTypeVisible"
                v-if="buildingAndHouseTypeVisible"
                width="80%"
                append-to-body
        >
            <group-choose
                    :projectInfo="projectInfo"
                    :visible="buildingAndHouseTypeVisible"

                    :dataHouseType="dataHouseType"
                    :dataBuild="dataBuild"
                    @building-and-house-type-click-finish="buildAndHouseTypeClickFinish"
                    @building-and-house-type-click-cancel="buildAndHouseTypeClickCancel"
                    @b-finish="bFinish"
                    @b-cancel="bCancel"
            ></group-choose>
        </el-dialog>

        <el-dialog
                :visible.sync="editFormDownloadVisible"
                width="35%"
                append-to-body
        >
            <file-upload
                    fileName="projectFloorExcel.xlsx"
                    @download-finish="downloadFinish"
                    @download-cancel="downloadCancel"
            ></file-upload>
        </el-dialog>

    </div>
</template>
<script>
    import serviceConfig from "../../../../../server.config.js";
    import clientModel from "../../../../model/client-model";
    import projectModel from "../../../../model/project/project";
    import houseTypeModel from "../../../../model/project/houseType";
    import {Notification} from "element-ui";
    import ProjectsEdit from "./ProjectsEdit.vue";
    import Building from "./BuildingAndHouseType.vue";
    import buildingModel from "../../../../model/project/building";
    import FileUpload from '../../../../components/fileUpload/index.vue'
    import moment from "moment";

    export default {
        computed: {
            buildingAndHouseTitle() {
                if (this.id != null) {
                    return "id为空";
                }
            }
        },
        components: {
            "model-edit": ProjectsEdit,
            "group-choose": Building,
            'file-upload': FileUpload,
        },
        mounted() {
            this.projectList();
        },
        props: ["rowIndex"],
        data() {
            return {
                arrBuilding: [],
                dataBuild: [],
                dataHouseType: [],
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
                editForm: null,
                //楼栋户型
                buildingAndHouseTypeVisible: false,
                //传值id
                projectInfo: "",
                treeProjects: [],
                treeBuilds: [],
                treeHouseTypes: [],
                editFormDownloadVisible: false,
                buttonData: [
                    {
                        name: '新建',
                        event: 'addClick'
                    },
                    {
                        name: '下载',
                        event: 'floorDownload'
                    },
                    {
                        name: 'Excel导入',
                        action: '',
                        multiple: true,
                        methods: {
                            onError: this.uploadError,
                            onSuccess: this.uploadSuccess,
                            beforeUpload: this.beforeAvatarUpload
                        }
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
                        name: '户型楼栋',
                        event: "houseTypeAndBuildClick",
                    }
                ]
            };
        },
        methods: {
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
            upLoadData() {
                this.selected.forEach(row => {
                    projectModel.upLoadData(row.id).then(data => {
                        if (data.status == "fail") {
                            Notification({
                                message: data.msg,
                                type: "warning"
                            });
                            return;
                        }
                    });
                });
            },
            floorDownload() {
                this.editFormDownloadVisible = true
            },
            uploadSuccess() {
            },
            // 上传错误
            uploadError(response, file, fileList) {
                console.log("上传失败，请重试！");
            },
            /**
             * 自定义上传
             */
            beforeAvatarUpload(file) {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });

                if (this.selected.length == 0) {
                    this.$message.error(this.GLOBAL.PROJECT_MSG.SELECT_NOT_PROJECT);
                    loading.close()
                    return false;
                }
                let arr = new Array()
                this.selected.forEach((row) => {
                    arr.push(row)
                })
                if (arr.length > 1) {
                    this.$message.error(this.GLOBAL.PROJECT_MSG.SELECT_ONE_PROJECT);
                    loading.close()
                    return false;
                }
                this.projectId = arr[0].id
                let Xls = file.name.split(".");
                if (Xls[1] === "xls" || Xls[1] === "xlsx") {
                    var formData = new FormData();
                    formData.append("file", file);
                    var file = formData;
                    clientModel.ax
                        .post('/productExcel/readExcelProductFloor?projectId=' + this.projectId, file, {
                            headers: {
                                "Content-Type": "charset=UTF-8"
                            }
                        })
                        .then(data => {
                            if (data.status == "failed") {
                                Notification({
                                    message: data.msg,
                                    type: "warning"
                                });
                                loading.close()
                                return;
                            }
                            Notification({
                                message: "导入成功",
                                type: "success"
                            });
                            loading.close()
                        });
                    return false;
                } else {
                    this.$message.error("上传文件只能是 xls/xlsx 格式!");
                    return false;
                }
            },
            //户型楼栋选择
            buildAndHouseTypeClick(row) {
                this.user = row;
                this.buildingAndHouseTypeVisible = true;
            },
            buildAndHouseTypeClickFinish() {
                this.projectList();
                this.buildingAndHouseTypeVisible = false;
            },
            buildAndHouseTypeClickCancel() {
                this.projectList();
                this.buildingAndHouseTypeVisible = false;
            },
            /**
             *添加修改删除户型，添加修改删除楼栋重新加载页面
             */
            projectLoad(projectId) {
                projectModel.projectLoad(projectId).then(data => {
                    houseTypeModel.getHouseTypeByProjectIdOpt(data.entity.id).then(houseType => {
                        let content = houseType.entity;
                        let dataTemp = new Array(); //户型下children[]
                        for (let i = 0; i < content.length; i++) {
                            let name = content[i].name;
                            let id = content[i].id;
                            var h1 = {
                                id: id,
                                label: name,
                                disabled: false
                            };
                            dataTemp.push(h1);
                        }
                        var xmH = new Array(); //项目 houseType children[]
                        var xm1 = {
                            label: data.entity.projectName,
                            children: []
                        };
                        xmH.push(xm1);

                        var hx = new Array(); //户型 children[]
                        var hx1 = {
                            label: "户型",
                            children: [],
                            disabled: true
                        };
                        hx.push(hx1);
                        hx[0].children = dataTemp;
                        xmH[0].children = hx;
                        this.dataHouseType = JSON.parse(JSON.stringify(xmH));
                    });

                    buildingModel
                        .getBuildingsByByProjectId(data.entity.id)
                        .then(dataBuilding => {
                            this.arrBuilding = dataBuilding.entity;
                            var dataBuilding = new Array(); //楼顶下children[]

                            //把楼栋放入数组中
                            this.arrBuilding.forEach(building => {
                                let buildCode = building.buildCode; //楼栋号
                                let id = building.id;
                                let aboveFlrNum = building.aboveFlrNum;
                                let belowFlrNum = building.belowFlrNum;
                                var b1 = {
                                    id: id,
                                    label: buildCode,
                                    aboveFlrNum: aboveFlrNum,
                                    belowFlrNum: belowFlrNum,
                                    disabled: false
                                };
                                dataBuilding.push(b1);
                            });

                            var xmB = new Array(); //项目 building children[]
                            var xm2 = {
                                label: data.entity.projectName,
                                children: []
                            };
                            xmB.push(xm2);
                            var buildings = new Array(); //楼栋 children[]
                            var bChild = {
                                label: "楼栋",
                                children: [],
                                disabled: true
                            };
                            buildings.push(bChild);
                            buildings[0].children = dataBuilding;
                            xmB[0].children = buildings;

                            this.dataBuild = JSON.parse(JSON.stringify(xmB));
                        });
                });
            },
            bFinish() {
                this.projectList();
                this.projectLoad(this.projectInfo.id);
            },
            bCancel() {
                this.projectList();
                this.projectLoad(this.projectInfo.id);
            },
            //编辑表单
            projectEditFinish() {
                this.editFormVisible = false;
                this.projectList();
            },
            projectEditCancel() {
                this.editFormVisible = false;
            },
            cshow: function (index) {
                this.rowIndex = index;
            },
            houseTypeAndBuildClick(row) {
                this.projectInfo = row
                this.$router.push({
                    name: 'buildingAndHouseType',
                    params: { projectInfo:row }
                })
            },
            submitForm(formName) {
                this.$refs[formName].validate(valid => {
                    if (valid) {
                        this.projectList();
                        /*        let conditionForm1='';
                                      for(var i=0;i<this.conditionForm.length;i++){
                                          conditionForm1 = this.conditionForm[i];
                                      }*/
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
                this.editForm = {
                    projectName: "",
                    customer: "",
                    workPlace: "",
                    projectNo: '',
                    planStartDate:'',
                    planEndDate:'',
                    projectStatus:''
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
                // const loading = this.$loading({
                //     lock: true,
                //     text: "Loading",
                //     spinner: "el-icon-loading",
                //     background: "rgba(0, 0, 0, 0.7)"
                // });
                projectModel.projectList(this.projectQuery).then(data => {
                    let projects = data.entity.content;
                    projects.map(project => {
                        houseTypeModel.getHouseTypeByProjectIdOpt(project.id).then(houseType => {
                            if (houseType.entity != null) {
                                project.houseTypeList = houseType.entity.length
                            } else {
                                project.houseTypeList = 0
                            }
                        });
                        buildingModel
                            .getBuildingsByByProjectId(project.id)
                            .then(dataBuilding => {
                                if (dataBuilding.entity != null) {
                                    project.buildingList = dataBuilding.entity.length
                                } else {
                                    project.buildingList = 0
                                }
                            })
                    });
                    this.projects = projects;
                    this.projectQuery.pageInfo.total = data.entity.totalElements;
                    // loading.close();
                });
            },
            handleSelectionChange(selected) {
                this.selected = selected;
            },
            handleSizeChange(val) {
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