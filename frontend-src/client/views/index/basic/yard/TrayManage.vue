<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                            <el-form :inline="true" :model="formSelect" class="form-inline" size="mini">
                                <el-form-item>
                                    <el-input v-model="formSelect.trayNo" placeholder="托盘号"></el-input>
                                </el-form-item>
                                <el-form-item>
                                    <el-select v-model="formSelect.trayStatus" placeholder="托盘状态" clearable>
                                        <el-option
                                                v-for="item in options"
                                                :key="item.value"
                                                :label="item.label"
                                                :value="item.value">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item>
                                    <el-date-picker
                                        v-model="formSelect.dateCreated"
                                        type="date"
                                        placeholder="选择创建日期">
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
                ref="handleClickRowSelection"
                @row-click="handleClickRowSelection"
                height="100%"
                :data="datas"
                @selection-change="handleSelectionChange"
                :expand-row-keys="expands"
                @expand-change="showSubData"
                :row-key="getRowKeys"
                size="mini"
                stripe
                border
                style="width: 100%"

                >
                    <!--这是点击小箭头出现的信息-->
                    <el-table-column  type="expand">
                        <template>
                            <el-table
                                    :data="subDatas"
                                    size="mini"
                                    stripe
                                    border
                            >
                                <el-table-column
                                        sortable
                                        prop="detailNo"
                                        label="构件明细码"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="productNo"
                                        label="构件编码"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="productName"
                                        label="构件名称"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="productVol"
                                        label="构件体积"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="productStatus"
                                        label="构件状态"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="createdByName"
                                        label="操作人"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="dateCreated"
                                        label="操作时间"
                                >
                                </el-table-column>
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
                            prop="trayNo"
                            label="托盘号"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="trayStatus"
                            label="托盘状态"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="maxStackNum"
                            label="最大堆放数"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="practicalStackNum"
                            label="实际堆放数"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="createdByName"
                            label="创建人"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            prop="dateCreated"
                            :formatter="formatterDate"
                            label="创建时间"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            prop="dateModified"
                            :formatter="formatterDate"
                            label="修改时间"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            align="left">
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

        <el-dialog :title="mode==='add'?'新建':'编辑'" :visible.sync="editFormVisible" v-if="editFormVisible">
            <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="120px">
                <el-form-item label="托盘号" prop="trayNo">
                    <el-input v-model="editForm.trayNo" :disabled="showNow"></el-input>
                </el-form-item>
                <el-form-item label="数量" prop="trayCount" v-if="mode=='add'">
                    <el-input v-model="editForm.trayCount" type="number"></el-input>
                </el-form-item>
                <el-form-item label="最大堆放数" prop="maxStackNum">
                    <el-input v-model="editForm.maxStackNum" type="number"></el-input>
                </el-form-item>
                <el-form-item label="起始编号" prop="trayBeginNum" v-if="mode=='add'">
                    <el-input v-model="editForm.trayBeginNum" type="number"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
            </div>
        </el-dialog>
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
        <!-- 批量下载二维码 -->
        <el-dialog
                append-to-body
                :visible.sync="editFormVisibleQRBatch"
                width="40%"
                v-if="editFormVisibleQRBatch"
        >
            <shelf-qr-code-edit-batch
                    :shelfNoQRCodes="trayNoQRCodes"
                    :shelfNameQRCodes="trayNameQRCodes"
                    :shelfIdQRCodes="trayIdQRCodes"
            ></shelf-qr-code-edit-batch>
        </el-dialog>

    </div>
</template>
<script>
    import ShelfQRcodeEdit from './ShelfQRcodeEdit.vue'
    import ShelfQRcodeEditBatch from './ShelfQRcodeEditBatch.vue'
    import TrayModel from '../../../../model/basicdata/tray.js'
    import menuTabService from '../../../../service/menuTabService'
    import {Notification} from 'element-ui'

    import moment from "moment";
    import StockModel from "../../../../model/basicdata/stock";

    export default {
        components: {
            'shelf-qr-code-edit': ShelfQRcodeEdit,
            'shelf-qr-code-edit-batch': ShelfQRcodeEditBatch,
        },
        data() {
            var validateNum = (rule, value, callback) => {
                var r = /^\+?[1-9][0-9]*$/;
                if (!r.test(value)) {
                    callback(new Error('请输入正整数'));
                } else {
                    if (value < 1) {
                        callback(new Error('必须大于0'));
                    } else {
                        callback();
                    }
                }

            };
            return {
                subDatas: [],
                expands: [],
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: 500,
                    pageSizes: this.GLOBAL.pageSizes
                },
                formSelect: {
                    trayNo: '',
                    trayStatus: '',
                    dateCreated:'',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                datas: [],
                selected: [],
                showNow:'',
                mode: '',
                editFormTitle: '',
                editFormVisible: false,
                editForm: null,
                editFormVisibleQR: false,
                editFormVisibleQRBatch:false,
                shelfNoQRCode: '',
                shelfIdQRCode: '',
                trayNameQRCodes:'',
                trayNoQRCodes: [],
                trayNameQRCodes:[],
                trayIdQRCodes: [],
                eidtFormRules: {
                    trayNo: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],

                    trayCount: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                        {validator: validateNum, trigger: 'change'}
                    ],

                    maxStackNum: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    trayBeginNum: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                        {validator: validateNum, trigger: 'change'}
                    ],

                },
                options: [{
                    value: '空闲',
                    label: '空闲'
                }, {
                    value: '使用中',
                    label: '使用中'
                }, {
                    value: '发货中',
                    label: '发货中'
                }],
                stocks: [],
                formSelect: {},
                buttonData:[
                    {
                        name:'新建',
                        event:'addClick'
                    },
                    {
                        name:'删除',
                        event:'deletesClick',
                        authorized:'shelfManage:newDelete'
                    },
                    {
                        name:'下载二维码',
                        event:'downloadQrcords',
                    }
                ],
                searchButtonData:[
                    {
                        name:'查询',
                        event:'onSubmit'
                    }
                ],
                rowButtonData:[
                    {
                        name:'编辑',
                        event:'editClick'
                    }
                ]
            }
        },
        mounted() {
            this.pageableList();
            this.list()
        },
        methods: {
            onSubmit(){
                this.pageInfo.currentPage = 1;
                this.list();
            },
            list() {
                log(this.pageInfo)
                this.formSelect.pageInfo = this.pageInfo;
                TrayModel.loadByPram(this.formSelect).then(data => {
                    this.datas = data.entity.list;
                    this.pageInfo.total = data.entity.totalCount;
                }).catch(err => {
                    Notification({
                        message: err.msg,
                        type: 'warning'
                    })
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
                this.subDatas = []
                TrayModel.getTraysById(id).then(data => {
                    this.subDatas.push(...data.entity)
                })
            },
            getRowKeys(row) {
                return row.id;
            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },

            pageableList() {
                this.formSelect.pageInfo = this.pageInfo;

                TrayModel.loadByPram(this.formSelect).then(data => {
                    this.datas = data.entity.list;
                    this.pageInfo.total = data.entity.totalCount;
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
                this.showNow = false
                this.editForm = {
                    trayNo: '',
                    status:0,
                    trayStatus: '空闲',
                    maxStackNum: '',
                    trayCount:'',
                    trayBeginNum:1,

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
                this.showNow = true
                TrayModel.getById(row.id).then(data => {
                    this.mode = 'edit';
                    this.editForm = data.entity;
                    loading.close()
                    this.editFormVisible = true
                }).catch((data) => {
                })

            },
            deletesClick() {
                if (this.selected.length === 0) {
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
                    TrayModel.deleteByIds(ids).then((data) => {
                        if (data.status === 'failed') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            return
                        }
                        Notification({
                            message: '删除成功',
                            type: 'success'

                        })
                        this.$store.commit('endLoading')
                        this.pageableList();
                    })

                }).catch(() => {

                })
            },
            showQRcode(row) {
                this.shelfIdQRCode = row.id
                this.shelfNoQRCode = row.shelfNo
                this.editFormVisibleQR = true
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.mode === 'edit') {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            TrayModel.update(this.editForm).then((data) => {
                                    if (data.status === 'failed') {
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
                            TrayModel.add(this.editForm).then((data) => {
                                    const loading = this.$loading({
                                        lock: true,
                                        text: 'Loading',
                                        spinner: 'el-icon-loading',
                                        background: 'rgba(0, 0, 0, 0.7)'
                                    });
                                    if (data.status === 'failed') {
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
                this.onSubmit()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val
                this.list()
            },
            authorized(index) {
                return menuTabService.authorized(this.$store, index)
            },
            downloadQrcords(){
              if(this.selected.length){
                this.trayIdQRCodes = []
                this.trayNoQRCodes = []
                this.trayNameQRCodes = '托盘号'
                this.selected.map(val=>{
                    this.trayIdQRCodes.push(val.id)
                    this.trayNoQRCodes.push(val.trayNo)
                })
                this.editFormVisibleQRBatch = true
              }else{
                Notification({
                    message: this.GLOBAL.PROJECT_MSG.SELECTED_BATCH_PROJECT,
                    type: 'warning'
                })
              }
            }

        }


    }

</script>
<style>


</style>
