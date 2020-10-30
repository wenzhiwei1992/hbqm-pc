<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                            <el-form :inline="true" :model="formSelect" class="form-inline" size="mini">
                                <el-form-item>
                                    <el-input v-model="formSelect.shelfNo" placeholder="货架号"></el-input>
                                </el-form-item>
                                <el-form-item>
                                    <el-select v-model="formSelect.stockId" placeholder="所属库区" clearable>
                                        <el-option v-for="item in stocks"
                                                :key="item.id"
                                                :label="item.stockName"
                                                :value="item.id"
                                        >
                                        </el-option>
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
                ref="handleClickRowSelection"
                @row-click="handleClickRowSelection"
                height="100%"
                :data="datas"
                @selection-change="handleSelectionChange"
                size="mini"
                stripe
                border
                style="width: 100%"

                >
                    <!--这是点击小箭头出现的信息-->
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
                            prop="shelfNo"
                            label="货架号"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="shelfName"
                            label="货架名称"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="stockId"
                            label="所属库区"
                            width="120">
                    </el-table-column>
                    <!-- <el-table-column
                            prop="shelfDesc"
                            label="货架描述"
                            width="120">
                    </el-table-column> -->
                    <el-table-column
                            prop="shelfStatus"
                            label="货架状态"
                            width="80">
                    </el-table-column>
                    <el-table-column
                            prop="shelfNum"
                            label="最大存放数"
                            width="120">
                    </el-table-column>
                    <!-- <el-table-column
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
                    </el-table-column> -->
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
                <el-form-item label="货架号" prop="shelfNo">
                    <el-input v-model="editForm.shelfNo"></el-input>
                </el-form-item>
                <el-form-item label="货架名称" prop="shelfName">
                    <el-input v-model="editForm.shelfName"></el-input>
                </el-form-item>
                <el-form-item label="货架描述" prop="shelfDesc">
                    <el-input v-model="editForm.shelfDesc"></el-input>
                </el-form-item>
                <el-form-item label="货架状态" prop="shelfStatus">
                    <el-select v-model="editForm.shelfStatus" placeholder="请选择" value="">
                        <el-option label="启用" value="启用"></el-option>
                        <el-option label="禁用" value="禁用"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="货架堆放数" prop="shelfNum">
                    <el-input v-model="editForm.shelfNum" type="number"></el-input>
                </el-form-item>

                <el-form-item label="库区名称" prop="stockId">
                    <el-select v-model="editForm.stockId" placeholder="请选择" value="">
                        <el-option v-for="item in stocks"
                                   :key="item.id"
                                   :label="item.stockName"
                                   :value="item.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="货架数量" prop="shelfCount" v-if="mode=='add'">
                    <el-input v-model="editForm.shelfCount" type="number"></el-input>
                </el-form-item>

                <el-form-item label="起始编号" prop="shelfBeginNum" v-if="mode=='add'">
                    <el-input v-model="editForm.shelfBeginNum" type="number"></el-input>
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
                    :shelfNoQRCodes="shelfNoQRCodes"
                    :shelfNameQRCodes="shelfNameQRCodes"
                    :shelfIdQRCodes="shelfIdQRCodes"
            ></shelf-qr-code-edit-batch>
        </el-dialog>

    </div>
</template>
<script>
    import ShelfQRcodeEdit from './ShelfQRcodeEdit.vue'
    import ShelfQRcodeEditBatch from './ShelfQRcodeEditBatch.vue'
    import ShelfModel from '../../../../model/basicdata/shelf.js'
    import menuTabService from '../../../../service/menuTabService'
    import {Notification} from 'element-ui'

    import moment from "moment";
    import shelf from "../../../../model/basicdata/shelf";

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
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: this.GLOBAL.pageSize,
                    pageSizes: this.GLOBAL.pageSizes
                },
                datas: [],
                selected: [],
                mode: '',
                editFormTitle: '',
                editFormVisible: false,
                editForm: null,
                editFormVisibleQR: false,
                editFormVisibleQRBatch:false,
                shelfNoQRCode: '',
                shelfIdQRCode: '',
                shelfNameQRCodes:'',
                shelfNoQRCodes: [],
                shelfNameQRCodes:[],
                shelfIdQRCodes: [],
                eidtFormRules: {
                    shelfNo: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    shelfStatus: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    shelfNum: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                        {validator: validateNum, trigger: 'change'}
                    ],
                    stockId: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    shelfName: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    shelfCount: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                        {validator: validateNum, trigger: 'change'}
                    ],
                    shelfBeginNum: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                        {validator: validateNum, trigger: 'change'}
                    ],

                },
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
                    },
                    // {
                    //     name:'二维码',
                    //     event:'showQRcode'
                    // },
                    // {
                    //     name:'删除',
                    //     event:'deleteClick'
                    // }
                ]
            }
        },
        mounted() {
            this.pageableList();
        },
        methods: {
            onSubmit() {
                this.formSelect.pageInfo = this.pageInfo;
                ShelfModel.loadByPram(this.formSelect).then(data => {
                    this.datas = data.entity.content;
                    this.pageInfo.total = data.entity.totalElements;
                }).catch(err => {
                    Notification({
                        message: err.msg,
                        type: 'warning'
                    })
                })
            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },

            pageableList() {
                this.$store.commit('startLoading');
                ShelfModel.getStocks().then(data => {
                    this.stocks = data.entity;
                }).catch(err => {
                    Notification({
                        message: err.msg,
                        type: 'warning'
                    })
                })

                ShelfModel.pageableList(this.pageInfo).then(data => {
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
                    shelfNo: '',
                    shelfDesc: '',
                    shelfStatus: '',
                    shelfNum: '',
                    stockId: '',
                    shelfName: '',
                    shelfBeginNum:1
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
                ShelfModel.getById(row.id).then(data => {
                    this.mode = 'edit';
                    this.editForm = data.entity;
                    this.editForm.stockId = parseInt(this.editForm.stockId);
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
                    ShelfModel.deleteById(row.id).then(data => {
                        this.$store.commit('endLoading')
                        this.pageableList();
                    })
                }).catch(() => {

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
                    ShelfModel.deleteByIds(ids).then((data) => {
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
                            ShelfModel.update(this.editForm)
                                .then((data) => {
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
                            ShelfModel.add(this.editForm)
                                .then((data) => {
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
                this.onSubmit()
            },
            authorized(index) {
                return menuTabService.authorized(this.$store, index)
            },
            downloadQrcords(){
              if(this.selected.length){
                this.shelfIdQRCodes = []
                this.shelfNoQRCodes = []
                this.shelfNameQRCodes = '货架号'
                this.selected.map(val=>{
                  this.shelfIdQRCodes.push(val.id)
                  this.shelfNoQRCodes.push(val.shelfNo)
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
