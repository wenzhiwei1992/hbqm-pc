<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                id="productChangeQuery"
                                :model="productChangeQuery"
                                ref="productChangeQuery"
                        >
                            <el-form-item prop="productNo">
                                <el-input
                                        placeholder="版本号"
                                        v-model="productChangeQuery.versionNumber"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="productNo">
                                <el-input
                                        placeholder="构件编码"
                                        v-model="productChangeQuery.productNo"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="productName">
                                <el-input
                                        placeholder="构件名称"
                                        v-model="productChangeQuery.productName"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <!-- <el-form-item
                                     prop="typwpeName"
                                     @click.native="((type)=>{showSelectTrees(1)})"
                             >
                                 <el-input
                                         placeholder="构件类型"
                                         v-model="productChangeQuery.typwpeName"
                                         style="width:150px"
                                 >
                                 </el-input>
                             </el-form-item>
                             <el-form-item
                                     prop="typwpeId"
                                     style="display:none"
                             >
                                 <el-input
                                         placeholder="构件类型"
                                         v-model="productChangeQuery.typwpeId"
                                         style="width:150px"
                                         clearable
                                 >
                                 </el-input>
                             </el-form-item>-->
                            <el-form-item prop="materialName">
                                <el-input
                                        placeholder="原料名称"
                                        v-model="productChangeQuery.materialName"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="materialNo">
                                <el-input
                                        placeholder="原料编号"
                                        v-model="productChangeQuery.materialNo"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <!-- <el-form-item prop="projectId">
                                 <el-select
                                         v-model="queryInfo.projectId"
                                         style="width:150px"
                                         placeholder="项目名称"
                                         clearable
                                 >
                                     <el-option
                                             v-for="item in this.arrProject"
                                             :key="item.value"
                                             :label="item.label"
                                             :value="item.value"
                                     >
                                     </el-option>
                                 </el-select>
                             </el-form-item>-->
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
                        :data="datas"
                        size="mini"
                        stripe
                        border
                        row-key="id"
                        height="100%"
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                        style="width: 100%"
                >
                    <!--  <el-table-column
                              prop="projectName"
                              label="项目名称"
                              width="120"
                      >
                      </el-table-column>-->
                    <!-- <el-table-column
                              prop="bulidCode"
                              label="楼栋"
                              width="120"
                      >
                      </el-table-column>
                      <el-table-column
                              prop="floorNum"
                              label="楼层"
                              width="120"
                      >
                      </el-table-column> -->
                    <el-table-column
                            prop="projectName"
                            label="项目名称"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productTypeName"
                            label="构件类型"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productName"
                            label="构件名称"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productNo"
                            label="构件编码"
                    >
                    </el-table-column>


                    <!-- <el-table-column
                             prop="changeData"
                             label="变更字段"
                     >
                     </el-table-column>
                     <el-table-column
                             prop="oldValue"
                             label="变更前的值"
                     >
                     </el-table-column>
                     <el-table-column
                             prop="newValue"
                             label="变更后的值"
                     >
                     </el-table-column>-->
                    <el-table-column
                            prop="bomVersionNum"
                            label="版本号"
                    >
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            fixed="right"
                            :width="rowButtonGroupWidth(rowButtonData)"
                    >
                        <template slot-scope="scope">
                            <my-row-button-group
                                    :row="scope.row"
                                    :model="rowButtonData"
                            ></my-row-button-group>
                            <!-- <el-button size="mini" type="primary" @click="showProduct(scope.row)">查看构件</el-button>
                             <el-button size="mini" type="primary" @click="showMaterial(scope.row)">查看原料</el-button>-->

                            <!-- <el-button size="mini" @click="editClick(scope.row)">编辑</el-button>
                             <el-button type="danger" size="mini" @click="deleteClick(scope.row)">删除</el-button>-->
                        </template>
                    </el-table-column>


                    <!--   <el-table-column
                               prop="productType"
                               label="构件类型"
                               width="120"
                       >
                       </el-table-column>
                       <el-table-column
                               prop="materialName"
                               label="原料名称"
                               width="120"
                       >
                       </el-table-column>
                       <el-table-column
                               prop="materialStoageId"
                               label="原料编码"
                               width="120"
                       >
                       </el-table-column>
                       <el-table-column
                               prop="oldNum"
                               label="原数量"
                               width="120"
                       >
                       </el-table-column>
                       <el-table-column
                               prop="newNum"
                               label="现数量"
                               width="120"
                       >
                       </el-table-column>
                       <el-table-column
                               prop="total"
                               label="变更量"
                               width="120"
                       >
                       </el-table-column>
                       <el-table-column
                               prop="changeNo"
                               label="变更单号"
                               width="120"
                       >
                       </el-table-column>-->

                </el-table>

            </el-main>
            <el-footer ref="footOperationBar">
                <el-row>
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="productChangeQuery.pageInfo.currentPage"
                            :page-sizes="productChangeQuery.pageInfo.pageSizes"
                            :page-size="productChangeQuery.pageInfo.pageSize"
                            :total="productChangeQuery.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
        </el-container>

        <el-dialog
                :visible.sync="showSelectTree"
                width="70%"
                append-to-body
        >
            <tree-add-edit1
                    :treeArr="treeArr"
                    :baseCheckedId="baseCheckedId"
                    :baseCheckParentId="baseCheckParentId"
                    @tree-edit-submit="checkoutStatusFinish"
                    @tree-edit-cancel="checkoutStatusCancel"
            ></tree-add-edit1>
        </el-dialog>

        <el-dialog :visible.sync="editFormMaterialsChangeInfoVisible" v-if="editFormMaterialsChangeInfoVisible" width="55%" append-to-body>
            <materials-change
                    :productId="productId"
                    :bomVerId="bomVerId"
            ></materials-change>
        </el-dialog>

        <el-dialog :visible.sync="editFormProductChangeInfoVisible" v-if="editFormProductChangeInfoVisible" width="55%" append-to-body>
            <product-change-and-bom-ver
                    :productId="productId"
                    :bomVerId="bomVerId"
            ></product-change-and-bom-ver>
        </el-dialog>

    </div>

</template>

<script>
    import materialsChangeModel from '../../../../model/project/materialsChange'
    import productChangeModel from '../../../../model/project/productChange'
    import projectModel from '../../../../model/project/project'
    import productModel from '../../../../model/project/product'
    import productTypeModel from '../../../../model/basicdata/productType'
    import TreeInfo from '../../basic/materialStorage/TreeInfo.vue'
    import MaterialsChange from './MaterialsChange.vue'
    import ProductChangeAndBomVer from './ProductChangeAndBomVer.vue'
    import {Notification} from 'element-ui'
    import xlsx from 'xlsx';
    import fileSaver from 'file-saver';
    import moment from "moment";

    export default {
        components: {
            'tree-add-edit1': TreeInfo,
            'materials-change': MaterialsChange,
            'product-change-and-bom-ver': ProductChangeAndBomVer
        },
        data() {
            return {
                productId: '',
                bomVerId: '',
                productChangeQuery: {
                    productName: '',
                    materialName: '',
                    materialNo: '',
                    bomVerId: '',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    }
                },
                editFormProductChangeInfoVisible: false,
                editFormMaterialsChangeInfoVisible: false,
                showSelectTree: false,
                baseCheckedId: [],
                baseCheckParentId: [],
                treeArr: '',
                treeType: '',
                arrProductType: [],//select下拉框（productType）
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: this.GLOBAL.pageSize,
                    pageSizes: this.GLOBAL.pageSizes
                },
                datas: [],
                orgs: [],
                exportdatas: [],
                arrProject: [],//select下拉框（project）
                buttonData: [
                    {
                        name: '导出',
                        event: 'exportExcel'
                    }
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "queryList"
                    }
                ],
                rowButtonData: [
                    {
                        name: '查看构件',
                        event: "showProduct"
                    },
                    {
                        name: '查看原料',
                        event: "showMaterial"
                    },
                ]

            }

        },
        mounted() {
            this.list()
            this.getProjects()
            this.showProductTree()
        },
        methods: {
            showProduct(row) {
                this.productId = row.productId
                this.bomVerId = row.bomVerId
                this.editFormProductChangeInfoVisible = true
            },
            showMaterial(row) {
                this.productId = row.productId
                this.bomVerId = row.bomVerId
                this.editFormMaterialsChangeInfoVisible = true
            },
            showProductTree() {
                // categoryModel.getCategorysToTreeParentDisabled().then((data) => {
                productTypeModel.getProductTypesToTreeParentDisabled().then((data) => {
                    this.treeArr = data.entity
                })
            },
            checkoutStatusFinish(id, name) {
                if (this.treeType == 1) {
                    this.queryInfo.typwpeName = name;
                    this.queryInfo.typwpeId = id;
                    this.queryInfo.productTypeId = id;
                }
                this.showSelectTree = false
            },
            checkoutStatusCancel() {
                this.showSelectTree = false
            },

            showSelectTrees(type) {
                //1是列表 2是编辑
                if (type == 1) {

                    this.baseCheckedId.splice(0, this.baseCheckedId.length);

                    this.baseCheckedId.push(this.queryInfo.typwpeId);

                    this.baseCheckParentId.splice(0, this.baseCheckedId.length);
                    this.baseCheckParentId.push(this.queryInfo.typwpeId);
                }
                this.showSelectTree = true
                this.treeType = type
            },
            /**
             * 合并单元格
             */
            objectSpanMethod({row, column, rowIndex, columnIndex}) {

                let rowCount = row.rowCount;
                let typeIndex = row.typeIndex;
                if (columnIndex === 0 || columnIndex === 1 || columnIndex === 2 | columnIndex === 3 || columnIndex === 4 || columnIndex === 10) {//第一列合并
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
                projectModel.projectListS().then((data) => {
                    let projects = data.entity
                    for (var j = 0; j < projects.length; j++) {
                        var pName = {
                            value: projects[j].id,
                            label: projects[j].projectName,
                            molds: []
                        }
                        this.arrProject.push(pName)
                    }
                })
            },
            exportExcel() {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                let page = this.pageInfo.currentPage;
                let size = this.pageInfo.pageSize;
                this.queryInfo.pageInfo.currentPage = 1;
                this.queryInfo.pageInfo.pageSize = this.pageInfo.total;
                //TODO 分页
                productModel.getProductMaterialChangeInfo(this.queryInfo).then(data => {
                    this.exportdatas = data.entity.list;
                    setTimeout(() => {
                        var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
                        excelName = "项目构件原料变更" + excelName;
                        // console.log(excelName)
                        /* generate workbook object from table */
                        let table = document.querySelector('#exportXlsx');
                        var wb = xlsx.utils.table_to_book(table);
                        /* get binary string as output */
                        var wbout = xlsx.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'})
                        try {
                            fileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), excelName)
                        } catch (e) {
                            if (typeof console !== 'undefined') console.log(e, wbout)
                        }

                        this.queryInfo.pageInfo.pageSize = size;
                        this.queryInfo.pageInfo.currentPage = page;
                        loading.close();
                        return wbout;
                    }, 500);

                }).catch(err => {

                })


            },
            queryList() {
                this.productChangeQuery.pageInfo.currentPage = 1;
                this.list();
            },

            list() {
                productChangeModel.findProductChangeAll(this.productChangeQuery).then((data) => {

                    this.datas = data.entity.content;
                    console.log('data.entity------>', this.datas)
                    this.productChangeQuery.pageInfo.total = data.entity.totalElements;
                })
            },
            handleSizeChange(val) {
                this.productChangeQuery.pageInfo.pageSize = val;
                this.list()
            },
            handleCurrentChange(val) {
                this.productChangeQuery.pageInfo.currentPage = val;
                this.list()
            },
        }
    }
</script>

<style scoped>
</style>
