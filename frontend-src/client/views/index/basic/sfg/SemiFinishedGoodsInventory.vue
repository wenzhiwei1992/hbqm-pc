<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                :model="semiFinishedGoodsQuery"
                                ref="semiFinishedGoodsQuery"
                                style="padding-bottom:8px;"
                        >
                            <el-form-item prop="sfgName">
                                <el-input
                                        placeholder="半成品名称"
                                        v-model="semiFinishedGoodsQuery.sfgName"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="sfgNo">
                                <el-input
                                        placeholder="半成品编码"
                                        v-model="semiFinishedGoodsQuery.sfgNo"
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
                            prop="rebarDiameter"
                            label="钢筋直径"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="rebarShape"
                            label="钢筋形状"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="numIn"
                            label="入库量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="numOut"
                            label="领用量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="inStockNum"
                            label="库存量"
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
                            prop="rebarDiameter"
                            label="钢筋直径"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="rebarShape"
                            label="钢筋形状"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="numIn"
                            label="入库量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="numOut"
                            label=出库量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="inStockNum"
                            label="库存量"
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
                            :current-page="semiFinishedGoodsQuery.pageInfo.currentPage"
                            :page-sizes="semiFinishedGoodsQuery.pageInfo.pageSizes"
                            :page-size="semiFinishedGoodsQuery.pageInfo.pageSize"
                            :total="semiFinishedGoodsQuery.pageInfo.total"
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
    import xlsx from "xlsx";
    import fileSaver from "file-saver";
    import sfgOutPutModel from "../../../../model/basicdata/sfgOutPut";

    export default {
        data() {
            return {
                //编辑操作传值
                sfgInfoEdit: [],
                mode: '',
                editFormVisible: false,
                semiFinishedGoodsQuery: {
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                sfgList: [],
                exportdatas: [],
                editFormVisibleQRBatch:false,
                sfgNoQRCodes: [],
                sfgNameQRCodes: [],
                sfgIdQRCodes:[],
                buttonData: [
                     {
                         name: '导出',
                         event: 'sfgExport'
                     },
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "submitForm('semiFinishedGoodsQuery')"
                    },
                    {
                        name: '重置',
                        event: 'resetForm("semiFinishedGoodsQuery")'
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
                let page = this.semiFinishedGoodsQuery.pageInfo.currentPage;
                let size = this.semiFinishedGoodsQuery.pageInfo.pageSize;
                this.semiFinishedGoodsQuery.pageInfo.currentPage = 1;
                this.semiFinishedGoodsQuery.pageInfo.pageSize = this.semiFinishedGoodsQuery.pageInfo.total;
                //TODO 分页
                sfgOutPutModel.findAllByQueryInventory(this.semiFinishedGoodsQuery).then((data) => {
                    this.exportdatas = data.entity.list;
                    setTimeout(() => {
                        var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
                        excelName = "半成品库存" + excelName;

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
                        this.semiFinishedGoodsQuery.pageInfo.pageSize = size;
                        this.semiFinishedGoodsQuery.pageInfo.currentPage = page;
                        loading.close();
                        return wbout;
                    }, 500);

                }).catch(err => {
                })
            },

            list() {
                sfgOutPutModel.findAllByQueryInventory(this.semiFinishedGoodsQuery).then((data) => {
                    this.sfgList = data.entity.list
                    this.semiFinishedGoodsQuery.pageInfo.total = data.entity.totalCount
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
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.semiFinishedGoodsQuery.pageInfo.pageSize = val
                this.list()
            },
            handleCurrentChange(val) {
                this.semiFinishedGoodsQuery.pageInfo.currentPage = val
                this.list()
            },
            sfgEditFinish() {
                this.editFormVisible = false
                this.list()
            },
            sfgEditCancel() {
                this.editFormVisible = false
            },
        },
    }
</script>

<style scoped>

</style>
