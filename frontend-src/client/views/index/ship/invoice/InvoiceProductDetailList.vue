<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true" :model="queryInfo" ref="queryInfo" class="form-inline">
                            <el-form-item prop="licenseNo">
                                <el-input v-model="queryInfo.licenseNo" clearable placeholder="发货单号"
                                          style="width: 150px"></el-input>
                            </el-form-item>

                            <el-form-item prop="invoiceNo">
                                <el-input v-model="queryInfo.invoiceNo" clearable placeholder="批次号"
                                          style="width: 150px"></el-input>
                            </el-form-item>

                            <el-form-item prop="productNo">
                                <el-input placeholder="构件编码" v-model="queryInfo.productNo" style="width:150px"
                                          clearable></el-input>
                            </el-form-item>
                            <el-form-item prop="productName">
                                <el-input
                                        placeholder="构件名称"
                                        v-model="queryInfo.productName"
                                        style="width:150px"
                                        clearable
                                ></el-input>
                            </el-form-item>
                            <el-form-item prop="typwpeName" @click.native="((type)=>{showSelectTrees(1)})">
                                <el-input placeholder="构件类型" v-model="queryInfo.typwpeName"
                                          style="width:150px"></el-input>
                            </el-form-item>
                            <el-form-item prop="typwpeId" style="display:none">
                                <el-input placeholder="构件类型" v-model="queryInfo.typwpeId" style="width:150px"
                                          clearable></el-input>
                            </el-form-item>

                            <el-form-item prop="projectId">
                                <el-select
                                        v-model="queryInfo.projectId"
                                        style="width:150px"
                                        placeholder="项目名称"
                                        clearable
                                        @change="changeProject"
                                >
                                    <el-option
                                            v-for="item in this.arrProject"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value"
                                    ></el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item prop="bulidId">
                                <el-select
                                        style="width:150px"
                                        v-model="queryInfo.bulidId"
                                        placeholder="楼栋"
                                        clearable
                                        :disabled="queryInfo.projectId==''"
                                        @change="changeBuilding(queryInfo.bulidId)"
                                >
                                    <el-option
                                            v-for="item in buildings"
                                            :key="item.id"
                                            :label="item.buildCode"
                                            :value="item.id"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="floorId">
                                <el-select
                                        style="width:150px"
                                        v-model="queryInfo.floorId"
                                        placeholder="楼层"
                                        clearable
                                        :disabled="queryInfo.bulidId==''"
                                >
                                    <el-option
                                            v-for="item in floors"
                                            :key="item.id"
                                            :label="item.floorNum"
                                            :value="item.id"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                            <!--
                                  <el-row>
                                    <el-form-item>
                                      <el-button type="primary" @click="queryList">查询</el-button>
                                      &lt;!&ndash; <el-button type="success" @click="exportExcel()">导出</el-button> &ndash;&gt;
                                    </el-form-item>
                                  </el-row>-->
                        </el-form>
                    </el-col>
                    <el-col>
                        <my-search-button-group :model='searchButtonData'></my-search-button-group>
                    </el-col>
                </el-row>
                <el-row>
                    <my-button-group :model="buttonData" style="display: none"></my-button-group>
                </el-row>
            </el-header>
            <el-main
                    ref="mainContent"
                    :style="`height:${this.$store.state.mainContentHeight}px`">
                <el-table
                        :data="datas"
                        size="mini"
                        stripe
                        border
                        height="100%">
                    <el-table-column
                            type="selection"
                            width="40">
                    </el-table-column>
                    <el-table-column type="index" label="序号" width="70"></el-table-column>
                    <el-table-column prop="licenseNo" label="发货单号" width="100" sortable></el-table-column>
                    <el-table-column prop="invoiceNo" label="批次号" width="100" sortable></el-table-column>
                    <el-table-column prop="buildCode" label="楼栋" width="80"></el-table-column>
                    <el-table-column prop="floorNum" label="楼层" width="80"></el-table-column>

                    <el-table-column prop="projectName" label="项目" width="100" sortable></el-table-column>
                    <el-table-column prop="productName" label="构件名称" width="140"></el-table-column>
                    <el-table-column prop="productNo" label="构件编码" width="140" sortable></el-table-column>
                    <el-table-column prop="productTypeName" label="构件类型" width="100" sortable></el-table-column>
                    <el-table-column prop="productVol" label="体积" width="60"></el-table-column>
                    <el-table-column prop="productWt" label="重量" width="60"></el-table-column>
                    <el-table-column prop="projectConcrete" label="砼方量" width="80"></el-table-column>
                    <el-table-column prop="productStatus" label="状态" width="80" sortable></el-table-column>

                    <el-table-column label="操作" align="center" fixed="right"
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
                            :current-page="pageInfo.currentPage"
                            :page-sizes="pageInfo.pageSizes"
                            :page-size="pageInfo.pageSize"
                            :total="pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    ></el-pagination>
                </el-row>
            </el-footer>
        </el-container>
        <el-dialog :visible.sync="showSelectTree" width="70%" append-to-body>
            <tree-add-edit1
                    :treeArr="treeArr"
                    :baseCheckedId="baseCheckedId"
                    :baseCheckParentId="baseCheckParentId"
                    @tree-edit-submit="checkoutStatusFinish"
                    @tree-edit-cancel="checkoutStatusCancel"
            ></tree-add-edit1>
        </el-dialog>



        <el-dialog
                title="发货单报废/返修"
                :visible.sync="editFormVisibleScrap"
                v-if="editFormVisibleScrap"
                width="500px"
                top="10px"
        >
            <scrap-type-list
                    :productDetail="editFormScrap"
                    checkType="invoice"
                    @model-edit-finish="scrapEditFinish"
                    @model-edit-cancel="scrapEditCancel"
            ></scrap-type-list>
        </el-dialog>
    </div>
</template>

<script>
    import projectModel from "../../../../model/project/project";
    import productModel from "../../../../model/project/product";
    import productTypeModel from "../../../../model/basicdata/productType";
    import plateModel from "../../../../model/basicdata/plate";
    import TreeInfo from "../../basic/materialStorage/TreeInfo.vue";
    import buildingModel from "../../../../model/project/building";
    import {Notification} from "element-ui";
    import xlsx from "xlsx";
    import fileSaver from "file-saver";
    import FinishCheckEdit from "../../check/FinishCheckEdit.vue";

    export default {
        components: {
            "tree-add-edit1": TreeInfo,
            'scrap-type-list': FinishCheckEdit,
        },
        data() {
            return {
                showSelectTree: false,
                baseCheckedId: [],
                baseCheckParentId: [],
                treeArr: "",
                treeType: "",
                arrProductType: [], //select下拉框（productType）
                prodLines: [], //产线集合
                teamList: [],
                buildings: [],
                floors: [],
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: this.GLOBAL.pageSize,
                    pageSizes: this.GLOBAL.pageSizes
                },
                datas: [],
                orgs: [],
                exportdatas: [],
                editFormScrap: '',
                editFormVisibleScrap: false,
                queryInfo: {
                    projectId: "",
                    productNo: "",
                    productName: "",
                    productTypeId: "",
                    materialName: "",
                    typwpeName: "",
                    typwpeId: "",
                    bulidId: "",
                    floorId: "",
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    }
                },

                arrProject: [], //select下拉框（project）
                searchButtonData: [
                    {
                        name: '查询',
                        event: "submitForm"
                    }
                ],
                rowButtonData: [

                    {
                        name: '报废/返修',
                        event: "check",
                        authorized: 'productDetailInRecordList:scrapRecord',
                        disabled: "scope.row.productStatus=='报废'|| scope.row.productStatus=='返修'|| scope.row.productStatus=='待报废审批'"
                    },

                ],
                buttonData: [
                    {
                        name: '编辑',
                        event: "editClick",
                    },
                ],
            };
        },
        mounted() {
            this.list();
            this.getProjects();
            this.getProdLines();
            this.getTeamInfoList();
            this.showProductTree();
        },
        methods: {
            submitForm() {
                this.pageInfo.currentPage = 1;
                this.list();
            },
            showProductTree() {
                // categoryModel.getCategorysToTreeParentDisabled().then((data) => {
                productTypeModel.getProductTypesToTreeParentDisabled().then(data => {
                    this.treeArr = data.entity;
                });
            },
            checkoutStatusFinish(id, name) {
                if (this.treeType == 1) {
                    this.queryInfo.typwpeName = name;
                    this.queryInfo.typwpeId = id;
                    this.queryInfo.productTypeId = id;
                }
                this.showSelectTree = false;
            },
            checkoutStatusCancel() {
                this.showSelectTree = false;
            },

            showSelectTrees(type) {
                //1是列表 2是编辑
                if (type == 1) {
                    this.baseCheckedId.splice(0, this.baseCheckedId.length);

                    this.baseCheckedId.push(this.queryInfo.typwpeId);

                    this.baseCheckParentId.splice(0, this.baseCheckedId.length);
                    this.baseCheckParentId.push(this.queryInfo.typwpeId);
                }
                this.showSelectTree = true;
                this.treeType = type;
            },
            /**
             * 合并单元格
             */
            objectSpanMethod({row, column, rowIndex, columnIndex}) {
                let rowCount = row.rowCount;
                let typeIndex = row.typeIndex;
                if (
                    columnIndex === 0 ||
                    columnIndex === 1 ||
                    (columnIndex === 2) | (columnIndex === 3) ||
                    columnIndex === 4 ||
                    columnIndex === 10
                ) {
                    //第一列合并
                    if (typeIndex === 1) {
                        return {
                            rowspan: rowCount,
                            colspan: 1
                        };
                    } else {
                        return {
                            rowspan: 0,
                            colspan: 0
                        };
                    }
                }
            },
            /**
             * 增加修改product为项目下拉框赋值
             */
            getProjects() {
                projectModel.projectListS().then(data => {
                    let projects = data.entity;
                    for (var j = 0; j < projects.length; j++) {
                        var pName = {
                            value: projects[j].id,
                            label: projects[j].projectName,
                            molds: []
                        };
                        this.arrProject.push(pName);
                    }
                });
            },
            getProdLines() {
                plateModel.getAllProdLinesByOrgId().then(data => {
                    this.prodLines = data.entity;
                });
            },
            getTeamInfoList() {
                projectModel.getTeamByOrg().then(data => {
                    this.teamList = data.entity;
                });
            },

            exportExcel() {
                const loading = this.$loading({
                    lock: true,
                    text: "Loading",
                    spinner: "el-icon-loading",
                    background: "rgba(0, 0, 0, 0.7)"
                });
                let page = this.pageInfo.currentPage;
                let size = this.pageInfo.pageSize;
                this.queryInfo.pageInfo.currentPage = 1;
                this.queryInfo.pageInfo.pageSize = this.pageInfo.total;
                //TODO 分页
                productModel
                    .getProductDetailList(this.queryInfo)
                    .then(data => {
                        this.exportdatas = data.entity.list;
                        setTimeout(() => {
                            var excelName =
                                new Date()
                                    .toLocaleDateString()
                                    .replace("/", "-")
                                    .replace("/", "-") + ".xlsx";
                            excelName = "项目构件原料变更" + excelName;
                            // console.log(excelName)
                            /* generate workbook object from table */
                            let table = document.querySelector("#exportXlsx");
                            var wb = xlsx.utils.table_to_book(table);
                            /* get binary string as output */
                            var wbout = xlsx.write(wb, {
                                bookType: "xlsx",
                                bookSST: true,
                                type: "array"
                            });
                            try {
                                fileSaver.saveAs(
                                    new Blob([wbout], {type: "application/octet-stream"}),
                                    excelName
                                );
                            } catch (e) {
                                if (typeof console !== "undefined") console.log(e, wbout);
                            }

                            this.queryInfo.pageInfo.pageSize = size;
                            this.queryInfo.pageInfo.currentPage = page;
                            loading.close();
                            return wbout;
                        }, 500);
                    })
                    .catch(err => {
                    });
            },
            list() {
                const loading = this.$loading({
                    lock: true,
                    text: "Loading",
                    spinner: "el-icon-loading",
                    background: "rgba(0, 0, 0, 0.7)"
                });

                this.queryInfo.pageInfo = this.pageInfo;
                //TODO 分页
                productModel
                    .getProductDetailList(this.queryInfo)
                    .then(data => {
                        this.datas = data.entity.list;
                        this.pageInfo.total = data.entity.totalCount;
                        loading.close();
                    })
                    .catch(err => {
                    });
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize = val;
                this.list();
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val;
                this.list();
            },
            changeProject() {
                buildingModel.findByProjectId(this.queryInfo.projectId).then(data => {
                    this.buildings = data.entity;
                    this.queryInfo.buildId = "";
                    this.queryInfo.floorId = "";
                    this.floors = [];
                });
            },
            changeBuilding(id) {
                this.queryInfo.floorId = "";
                this.floors = [];
                if (!id) {
                    return;
                }

                buildingModel
                    .getFloorInfoByBuildId(id, this.queryInfo.projectId)
                    .then(data => {
                        this.floors = data.entity;
                    });
            },
            /**
             * 报废
             * @param row
             * @param type
             */
            changeProductStatusScrapped(row) {
                const loading = this.$loading({
                    lock: true,
                    text: "Loading",
                    spinner: "el-icon-loading",
                    background: "rgba(0, 0, 0, 0.7)"
                });
                let productDetailId = row.productDetailId;
                let invoiceDetailId = row.id;
                let type = 1;
                productModel
                    .changeProductStatus(invoiceDetailId, productDetailId, type)
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
                            message: "已报废",
                            type: "success"
                        });
                        this.list();
                        loading.close();
                    })
                    .catch(err => {
                    });
            },
            /**
             * 返修
             * @param row
             * @param type
             */
            changeProductStatusRework(row) {
                const loading = this.$loading({
                    lock: true,
                    text: "Loading",
                    spinner: "el-icon-loading",
                    background: "rgba(0, 0, 0, 0.7)"
                });
                let productDetailId = row.productDetailId;
                let invoiceDetailId = row.id;
                let type = 2;
                productModel
                    .changeProductStatus(invoiceDetailId, productDetailId, type)
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
                            message: "已返修",
                            type: "success"
                        });
                        this.list();
                        loading.close();
                    })
                    .catch(err => {
                    });
            },
            //行操作
            check(row) {
                this.editFormScrap = row
                this.editFormVisibleScrap = true
            },
            scrapEditFinish() {
                this.editFormVisibleScrap = false
                this.list()
            },
            scrapEditCancel() {
                this.editFormVisibleScrap = false
            },
        }
    };
</script>
<style scoped>
    .active {
        background: #409eff;
        border: #409eff;
    }
</style>