<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                id="projectList"
                                :model="materialStorageQuery"
                                ref="materialStorageQuery"
                        >
                            <el-form-item prop="materialStorageNo">
                                <el-input
                                        placeholder="物料编码"
                                        v-model="materialStorageQuery.materialStorageNo"
                                        style="width:150px"
                                        clearable>
                                </el-input>
                            </el-form-item>

                            <el-form-item prop="materialName">
                                <el-input
                                        placeholder="物料名称"
                                        v-model="materialStorageQuery.materialName"
                                        style="width:150px"
                                        clearable>
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="categoryName">
                                <el-input
                                        placeholder="物料类别"
                                        v-model="materialStorageQuery.categoryName"
                                        @focus="showSelectTrees"
                                ></el-input>
                            </el-form-item>
                            <!-- <el-form-item label="物料规格" prop="materialSpecification">
                                  <el-input
                                          placeholder="请输入内容"
                                          v-model="materialStorageQuery.materialSpecification"
                                          style="width:150px"
                                          clearable>
                                  </el-input>
                              </el-form-item>
                              <el-form-item label="变动原因" prop="status">
                                  <el-select clearable v-model="materialStorageQuery.status" placeholder="请选择">
                                      <el-option
                                              v-for="item in options"
                                              :key="item.value"
                                              :label="item.label"
                                              :value="item.value">
                                      </el-option>
                                  </el-select>
                              </el-form-item>
                              -->
                            <!-- <el-form-item label="选择月份" prop="month">
                                  <el-date-picker
                                          v-model="materialStorageQuery.month"
                                          type="month"
                                          format="yyyy-MM"
                                          value-format="yyyy-MM"
                                          style="width: 150px"
                                          placeholder="选择日期">
                                  </el-date-picker>
                              </el-form-item> -->
                            <el-form-item>
                                <el-date-picker
                                        v-model="materialStorageQuery.startDay"
                                        type="date"
                                        format="yyyy-MM-dd"
                                        value-format="yyyy-MM-dd"
                                        style="width: 150px"
                                        placeholder="选择开始日期"
                                ></el-date-picker>
                                <el-date-picker
                                        v-model="materialStorageQuery.endDay"
                                        type="date"
                                        format="yyyy-MM-dd"
                                        value-format="yyyy-MM-dd"
                                        style="width: 150px"
                                        placeholder="选择结束日期"
                                ></el-date-picker>
                            </el-form-item>
                            <el-form-item prop="orgId">
                                <el-select
                                        v-model="materialStorageQuery.orgId"
                                        style="width: 150px"
                                        placeholder="基地名称"
                                        clearable
                                >
                                    <el-option
                                            v-for="item in orgs"
                                            :key="item.id"
                                            :label="item.orgName"
                                            :value="item.id"
                                    >
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
                        :data="datas"
                        @selection-change="handleSelectionChange"
                        :default-sort="{prop: 'dateCreated', order: 'descending'}"
                        size="mini"
                        stripe
                        border
                        height="100%"
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                        id="table"
                        :cell-class-name="getCellClass"
                        style="width:100%"
                >
                    <el-table-column
                            type="index"
                            label="序号"
                            width="50"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="orgName"
                            label="基地名称"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="materialType"
                            label="类别"
                            width="80"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="materialStorageNo"
                            label="编号"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="materialName"
                            label="品名"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="materialSpecification"
                            label="规格"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="materialUnit"
                            label="单位"
                            width="80"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="sqjy"
                            label="上期结存数(包含初始库存)"
                            width="200"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="bqrk"
                            label="本期购进数量"
                            width="150"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="bqll"
                            label="生产领用数量"
                            width="150"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="bqjy"
                            label="本期结存数量"
                            width="150"
                    >
                    </el-table-column>
                </el-table>


                <el-table
                        :data="exportdatas"
                        size="mini"
                        stripe
                        border
                        id="exportXlsx"
                        hidden
                >
                    <el-table-column
                            type="index"
                            label="序号"
                            width="50"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="orgName"
                            label="基地名称"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="materialType"
                            label="类别"
                            width="80"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="materialStorageNo"
                            label="编号"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="materialName"
                            label="品名"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="materialSpecification"
                            label="规格"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="materialUnit"
                            label="单位"
                            width="80"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="sqjy"
                            label="上期结存数(包含初始库存)"
                            width="200"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="bqrk"
                            label="本期购进数量"
                            width="150"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="bqll"
                            label="生产领用数量"
                            width="150"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="bqjy"
                            label="本期结存数量"
                            width="150"
                    >
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer ref="footOperationBar">
                <el-row>
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="materialStorageQuery.pageInfo.currentPage"
                            :page-sizes="materialStorageQuery.pageInfo.pageSizes"
                            :page-size="materialStorageQuery.pageInfo.pageSize"
                            :total="materialStorageQuery.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
        </el-container>
        <el-dialog
                :visible.sync="showSelectTree"
                width="340px"
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
    </div>
</template>

<script>
    import materialStorageModel from '../../../model/basicdata/materialStorage'
    import clientModel from '../../../model/client-model'
    import serviceConfig from '../../../../server.config.js'
    import orgModel from '../../../model/system/org'
    import {Notification} from 'element-ui'
    import moment from "moment"
    import TreeInfo from "../basic/materialStorage/TreeInfo.vue";
    import categoryModel from "../../../model/basicdata/category";
    import fileSaver from 'file-saver';
    import xlsx from 'xlsx';
    import {exportExcelMethod}   from "../../../exportExcelTest.js";

    export default {
        data() {
            return {
                materialStorageQuery: {
                    orgId: '',
                    categoryName: '',
                    categoryId: '',
                    classificationId: '',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                orgs: [],
                datas: [],
                exportdatas: [],
                selected: [],
                buttonData: [
                    {
                        name: '导出',
                        event: 'exportExcel'
                    }
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "submitForm('materialStorageQuery')"
                    }
                ],
                rowButtonData: [],
                showSelectTree: false,
                baseCheckedId: [],
                baseCheckParentId: [],
                treeArr: '',
            }
        },
        name: "EntryExitDetailByMonth",
        components: {
            'tree-add-edit1': TreeInfo,
        },
        mounted() {
           // this.list()
            this.info()
            this.showCategoryTree()
        },
        methods: {
            getCellClass({row, column, rowIndex, columnIndex}){
                // if (columnIndex === 2 ) {
                //     return 'red1'
                // }

            },
            exportExcel1(){
        exportExcelMethod('table', '发货单', 'sheet1');
    },
            exportExcel() {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                let page = this.materialStorageQuery.pageInfo.currentPage;
                let size = this.materialStorageQuery.pageInfo.pageSize;

                this.materialStorageQuery.pageInfo.currentPage = 1;
                this.materialStorageQuery.pageInfo.pageSize = this.materialStorageQuery.pageInfo.total;

                materialStorageModel.entryExitDetailByMonth(this.materialStorageQuery).then((data) => {
                    this.exportdatas = data.entity.content;
                    setTimeout(() => {
                        var excelName = "export.xlsx";
                        /* generate workbook object from table */
                        let table = document.querySelector('#exportXlsx');
                        var wb = xlsx.utils.table_to_book(table);
                        /* get binary string as output */
                        var wbout = xlsx.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' })
                        try {
                            fileSaver.saveAs(new Blob([wbout], { type: 'application/octet-stream' }), excelName)
                        } catch (e) {
                            if (typeof console !== 'undefined') console.log(e, wbout)
                        }

                        this.materialStorageQuery.pageInfo.pageSize = size;
                        this.materialStorageQuery.pageInfo.currentPage = page;
                        loading.close();
                        return wbout;
                    }, 500);

                }).catch(err => {

                })


            },
            exportExcelOld() {
                var url = serviceConfig.baseURL + '/materialStorage/exportEntryExitDetailByMonth'

                // var prams = {"month":this.materialStorageQuery.month}
                let startDay = null;
                let endDay = null;
                if(this.materialStorageQuery.startDay){
                    startDay = this.materialStorageQuery.startDay;
                }
                if(this.materialStorageQuery.endDay){
                    endDay = this.materialStorageQuery.endDay;
                }

                var params = {
                    startDay:startDay,
                    endDay: endDay,
                    categoryId: this.materialStorageQuery.categoryId,
                    categoryName: this.materialStorageQuery.categoryName,
                    classificationId: this.materialStorageQuery.classificationId,
                    materialStorageNo: this.materialStorageQuery.materialStorageNo,
                    orgId: this.materialStorageQuery.orgId
                }
                this.exportExcelPost(url, params)
            },
            list() {
                // if(!this.materialStorageQuery.month){
                //     let date = new Date();
                //     this.materialStorageQuery.month = moment(date).format("YYYY-MM")
                // }
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                materialStorageModel.entryExitDetailByMonth(this.materialStorageQuery).then((data) => {
                    this.datas = data.entity.content
                    this.materialStorageQuery.pageInfo.total = data.entity.totalCount
                    loading.close()
                })
            },
            info() {
                orgModel.orgAllByOrgIds().then((data) => {
                    this.orgs = data.entity
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
            showCategoryTree() {
                // categoryModel.getCategorysToTreeParentDisabled().then((data) => {
                categoryModel.getCategorysToTreeParentDisabled().then((data) => {
                    this.treeArr = data.entity
                })
            },
            showSelectTrees() {
                this.baseCheckedId.splice(0, this.baseCheckedId.length);
                this.baseCheckedId.push(this.materialStorageQuery.categoryId);

                this.baseCheckParentId.splice(0, this.baseCheckedId.length);
                this.baseCheckParentId.push(this.materialStorageQuery.categoryId);

                this.showSelectTree = true
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM");
            },
            handleSizeChange(val) {
                this.materialStorageQuery.pageInfo.pageSize = val
                this.list()
            },
            handleCurrentChange(val) {
                this.materialStorageQuery.pageInfo.currentPage = val
                this.list()
            },
            exportExcelPost(URL, PARAMS) {
                var temp = document.createElement("form");
                console.log(temp)
                temp.action = URL;
                temp.method = "post";
                temp.style.display = "none";
                for (var x in PARAMS) {
                    var opt = document.createElement("textarea");
                    opt.name = x;
                    if(!PARAMS[x]){
                        opt.value = null;
                    }else{
                        opt.value = PARAMS[x];
                    }
                    temp.appendChild(opt);
                }
                document.body.appendChild(temp);
                console.log(temp)
                temp.submit();
                return temp;
            },
            checkoutStatusFinish(id, name) {
                this.materialStorageQuery.categoryName = name;
                this.materialStorageQuery.categoryId = id;
                this.materialStorageQuery.classificationId = id;

                this.showSelectTree = false
            },
            checkoutStatusCancel() {
                this.showSelectTree = false
            },
        },

    }
</script>

<style >
    .red1{
        background-color: cyan;
        color: #3ea44e;
    }
</style>