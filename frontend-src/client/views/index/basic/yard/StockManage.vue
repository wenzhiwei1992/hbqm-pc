<template>

    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true" :model="formSelect" class="form-inline" size="mini">
                            <el-form-item>
                                <el-input v-model="formSelect.stockName" placeholder="库区名称"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-select v-model="formSelect.stockTypeCode" placeholder="库区类型" clearable>
                                    <el-option label="材料仓库" value="材料仓库"></el-option>
                                    <el-option label="半成品仓库" value="半成品仓库"></el-option>
                                    <el-option label="配件仓库" value="配件仓库"></el-option>
                                    <el-option label="成品仓库" value="成品仓库"></el-option>
                                    <el-option label="周转材料仓库" value="周转材料仓库"></el-option>
                                    <el-option label="返修仓库" value="返修仓库"></el-option>
                                    <el-option label="废料仓库" value="废料仓库"></el-option>
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
                        :data="datas"
                        @selection-change="handleSelectionChange"
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                        @expand-change="showSubData"
                        size="mini"
                        stripe
                        border
                        :expand-row-keys="expands"
                        row-key="id"
                        height="100%"
                        style="width: 100%"
                >
                    <!--这是点击小箭头出现的信息-->
                    <el-table-column
                            type="expand"
                    >
                        <template>
                            <el-table
                                    :data="subDatas"
                                    size="mini"
                                    stripe
                                    border
                            >
                                <el-table-column
                                        sortable
                                        prop="shelfName"
                                        label="货架名称"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="shelfNum"
                                        label="货架堆放数"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="shelfDesc"
                                        label="货架描述"
                                >
                                </el-table-column>
                                <!-- <el-table-column
                                        prop="dateCreated"
                                        :formatter="formatterDate"
                                        label="创建时间"
                                >
                                </el-table-column> -->
                            </el-table>
                        </template>
                    </el-table-column>

                    <el-table-column
                            type="selection"
                            width="60">
                    </el-table-column>
                    <el-table-column
                            type="index"
                            label="序号"
                            width="50">
                    </el-table-column>
                    <el-table-column
                            prop="stockNo"
                            label="库区号"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="stockName"
                            label="库区名称"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="stockAcreage"
                            label="面积(m²)"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="stockStatus"
                            label="状态"
                            width="80">
                    </el-table-column>
                    <el-table-column
                            prop="stockTypeCode"
                            label="库区类型"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="remark"
                            label="备注"
                            width="120">
                    </el-table-column>
                    <!-- <el-table-column
                            prop="dateCreated"
                            :formatter="formatterDate"
                            label="创建时间"
                            width="140">
                    </el-table-column> -->
                    <!-- <el-table-column
                            prop="dateModified"
                            :formatter="formatterDate"
                            label="修改时间"
                            width="140">
                    </el-table-column> -->
                    <el-table-column label="操作" align="left">
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
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
        </el-container>

        <el-dialog :title="mode=='add'?'新建':'编辑'" :visible.sync="editFormVisible" v-if="editFormVisible">
            <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px">
                <el-form-item label="库区号" prop="stockNo">
                    <el-input v-model="editForm.stockNo"></el-input>
                </el-form-item>
                <el-form-item label="库区名称" prop="stockName">
                    <el-input v-model="editForm.stockName"></el-input>
                </el-form-item>
                <el-form-item label="面积(m²)" prop="stockAcreage">
                    <el-input v-model="editForm.stockAcreage"></el-input>
                </el-form-item>
                <el-form-item label="库区描述" prop="stockDesc">
                    <el-input v-model="editForm.stockDesc"></el-input>
                </el-form-item>
                <el-form-item label="状态" prop="stockStatus">
                    <el-select v-model="editForm.stockStatus" placeholder="请选择" value="">
                        <el-option label="启用" value="启用"></el-option>
                        <el-option label="禁用" value="禁用"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="库分类" prop="stockTypeCode">
                    <el-select v-model="editForm.stockTypeCode" placeholder="请选择" value="">
                        <el-option label="材料仓库" value="材料仓库"></el-option>
                        <el-option label="半成品仓库" value="半成品仓库"></el-option>
                        <el-option label="配件仓库" value="配件仓库"></el-option>
                        <el-option label="成品仓库" value="成品仓库"></el-option>
                        <el-option label="周转材料仓库" value="周转材料仓库"></el-option>
                        <el-option label="返修仓库" value="返修仓库"></el-option>
                        <el-option label="废料仓库" value="废料仓库"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="editForm.remark"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
            </div>
        </el-dialog>

    </div>
</template>
<script>
    import StockModel from '../../../../model/basicdata/stock.js'
    import menuTabService from '../../../../service/menuTabService'
    import {Notification} from 'element-ui'
    import moment from "moment";

    export default {
        data() {
            return {
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: this.GLOBAL.pageSize,
                    pageSizes: this.GLOBAL.pageSizes
                },
                datas: [],
                selected: [],
                expands: [],
                mode: '',
                editFormTitle: '',
                editFormVisible: false,
                editForm: null,
                eidtFormRules: {
                    stockNo: [
                        {required: true, message: '必填字段', trigger: 'change'},

                    ],
                    stockName: [
                        {required: true, message: '必填字段', trigger: 'change'},
                    ],
                    stockStatus: [
                        {required: true, message: '必填字段', trigger: 'change'},
                    ],
                    stockTypeCode: [
                        {required: true, message: '必填字段', trigger: 'change'},
                    ],
                    stockAcreage: [
                        {required: true, message: '必填字段', trigger: 'change'},
                    ],

                },
                subDatas: [],
                formSelect: {
                    stockName: '',
                    stockTypeCode: '',
                    dateCreated: ''
                },
                buttonData: [
                    {
                        name: '新建',
                        event: 'addClick'
                    },
                    {
                        name: '删除',
                        event: 'deletesClick',
                        authorized: 'stockManage:newDelete'
                    }
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: 'onSubmit'
                    }
                ],
                rowButtonData: [
                    {
                        name: '编辑',
                        event: 'editClick'
                    }
                ]


            }
        },
        mounted() {
            this.pageableList();
        },
        methods: {
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },

            onSubmit() {
                StockModel.getDataByParam(this.formSelect).then(data => {
                    this.datas = data.entity;
                })
            },
            showSubData(row, expandedRows) {
                let id = row.id;
                if (this.expands.some((e) => e == row.id)) {
                    this.expands = []
                } else {
                    this.expands = []
                    this.expands.push(row.id)
                }
                log(id)
                this.subDatas = []
                StockModel.getShelfsById(id).then(data => {
                    this.subDatas.push(...data.entity)
                    log(this.subDatas)
                })
            },
            pageableList() {
                this.$store.commit('startLoading');
                StockModel.pageableList(this.pageInfo).then(data => {
                    this.datas = data.entity.content;
                    this.pageInfo.total = data.entity.totalElements;
                    this.$store.commit('endLoading');
                }).catch(err => {
                    Notification({
                        message: err.msg,
                        type: 'warning'
                    })
                })
            },
            addClick() {
                this.mode = 'add';
                this.editForm = {
                    stockNo: '',
                    stockName: '',
                    stockAcreage: '',
                    stockDesc: '',
                    stockStatus: '',
                    stockTypeCode: '',
                    remark: ''
                }
                this.editFormVisible = true
            },
            editClick(row) {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                StockModel.getById(row.id).then(data => {
                    this.mode = 'edit';
                    this.editForm = data.entity;
                    // if(this.editForm.stockStatus == 1){
                    //     this.editForm.stockStatus = '启用';
                    // }else{
                    //     this.editForm.stockStatus = '禁用';
                    // }
                    loading.close()
                    this.editFormVisible = true
                }).catch((data) => {

                })

            },
            deleteClick(row) {
                this.$confirm('确认删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading')
                    StockModel.deleteById(row.id).then(data => {
                        this.$store.commit('endLoading')
                        this.pageableList();
                    })
                }).catch(() => {

                })
            },
            deletesClick() {
                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }
                let ids = []
                this.selected.forEach((row) => {
                    ids.push(row.id)
                })
                this.$confirm('确认删除选择项?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading')
                    StockModel.deleteByIds(ids).then((data) => {
                        if (data.status === 'failed') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            return
                        }
                        this.$store.commit('endLoading')
                        this.pageableList();
                    })

                }).catch(() => {

                })
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.mode == 'edit') {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            StockModel.update(this.editForm)
                                .then((data) => {
                                    if (data.status == 'failed') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close()
                                        return
                                    }
                                    Notification({
                                        message: '编辑成功',
                                        type: 'success'

                                    })
                                    loading.close()
                                    this.editFormVisible = false
                                    this.pageableList();
                                }).catch(() => {

                                }
                            )
                        } else {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            StockModel.add(this.editForm)
                                .then((data) => {
                                    if (data.status == 'failed') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close()
                                        return
                                    }
                                    Notification({
                                        message: '新建成功',
                                        type: 'success'

                                    })
                                    loading.close()
                                    this.editFormVisible = false
                                    this.pageableList()
                                })
                        }

                    } else {
                        return false;
                    }
                });
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize = val
                this.pageableList()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val
                this.pageableList()
            },
            authorized(index) {
                return menuTabService.authorized(this.$store, index)
            }

        }


    }

</script>
<style>


</style>
