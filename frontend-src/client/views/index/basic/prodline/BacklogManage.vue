<template>
    <div>
        <el-table
                :data="backlogDatas"
                @selection-change="handleSelectionChange"
                size="mini"
                stripe
                border
                style="width: 100%"
                height="500"
        >
            <el-table-column
                    type="selection"
                    width="80">
            </el-table-column>
            <!--<el-table-column
                    prop="dateCreated"
                    label="创建时间"
                    :formatter="formatterDate"
                    width="120">
            </el-table-column>-->
            <el-table-column
                    type="index"
                    label="序号"
                    width="50">
            </el-table-column>
            <el-table-column
                    prop="backlogCode"
                    label="工序编码"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="backlogName"
                    label="工序名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="type"
                    label="工序类型"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="makeSpan"
                    label="工序节拍"
                    width="120">
            </el-table-column>
            <!-- <el-table-column
                    prop="source"
                    label="工序资源"
                    width="120">
            </el-table-column> -->
            <el-table-column
                    prop="prodLine.lineName"
                    label="所属产线"
                    width="120">
            </el-table-column>
            <el-table-column
                    label="操作"
                    align="left">
                <template slot-scope="scope">
                    <el-button size="mini" @click="beforeAddStations(scope.row)">添加工位</el-button>
                    <el-button size="mini" @click="showStations(scope.row)">显示工位</el-button>
                    <el-button size="mini" @click="editClick(scope.row)">编辑</el-button>
                    <el-button type="danger" size="mini" @click="deleteClick(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle" >
            <el-col style="margin-top:15px">
                <el-button type="primary" @click="addClick">新建</el-button>
                <el-button type="danger" @click="deletesClick">删除选中</el-button>
            </el-col>
            <el-col>
                <el-row type="flex" justify="end" align="middle">
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

            </el-col>
        </el-row>
        <el-dialog :title="mode==='add'?'新建':'编辑'" :visible.sync="editFormVisible" v-if="editFormVisible">
            <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px">
                <el-form-item label="工序编码" prop="backlogCode">
                    <el-input v-model="editForm.backlogCode"></el-input>
                </el-form-item>
                <el-form-item label="工序名称" prop="backlogName">
                    <el-input v-model="editForm.backlogName"></el-input>
                </el-form-item>
                
                <el-form-item label="工序节拍" prop="makeSpan">
                    <el-input v-model="editForm.makeSpan"></el-input>
                </el-form-item>
                <el-form-item label="工序类型" prop="type">
                    <el-select v-model="editForm.type" placeholder="">
                        <el-option value="生产工序" label="生产工序"></el-option>
                        <el-option value="管理工序" label="管理工序"></el-option>
                    </el-select>
                </el-form-item>
                <!-- <el-form-item label="工序资源" prop="source">
                    <el-input v-model="editForm.source"></el-input>
                </el-form-item> -->
                <el-form-item label="所属产线" prop="prodLine">
                    <el-select v-model="editForm.prodLine.id" placeholder="">
                        <el-option
                                v-for="item in prodLines"
                                :key="item.id"
                                :label="item.lineName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog :visible.sync="ifStation">
            <el-table
                    ref="multipleTable"
                    :data="stations"
                    tooltip-effect="dark"
                    size="mini"
                    stripe
                    border
                    style="width: 100%"
                    @selection-change="handleSelectionChange">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        label="序号"
                        width="120">
                    <template slot-scope="scope">{{ scope.row.id }}</template>
                </el-table-column>
                <el-table-column
                        prop="stationCode"
                        label="工位代码"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="stationName"
                        label="工位名称"
                        width="140">
                </el-table-column>
                <el-table-column
                        prop="remark"
                        label="备注"
                        show-overflow-tooltip>
                </el-table-column>

            </el-table>
            <div style="margin-top: 20px">
                <el-button @click="optionBacklog(doa)">{{doa}}选择</el-button>
            </div>
        </el-dialog>
    </div>


</template>
<script>
    import backlogModel from '../../../../model/prodline/backlog'
    import {Notification} from 'element-ui'
    import moment from "moment";

    export default {
        data() {
            const checkProdLine = (rule, value, callback) => {
                if (!value.id) {
                    return callback(new Error('产线不能为空'));
                } else {
                    callback();
                }

            };

            return {
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize:this.GLOBAL.pageSize,
                    pageSizes:this.GLOBAL.pageSizes
                },
                backlogDatas: [],
                selected: [],
                mode: '',
                editFormTitle: '',
                editFormVisible: false,
                editForm: null,
                eidtFormRules: {
                    backlogName: [
                        {required: true, message: '必填字段', trigger: 'blur'},

                    ],
                    backlogCode: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    makeSpan: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    type: [
                        {required: true, message: '必填字段', trigger: 'change'},
                    ],
                    prodLine:[
                        {validator: checkProdLine,trigger:'change'},
                    ]
                },
                doa: '',
                ifStation: false,
                stations: [],
                backLogDTO: {
                    backlogId: '',
                    ids: []
                },
                prodLines: [],
            }
        },
        mounted() {
            this.list();
        },
        methods: {
            formatterDate(row, column, cellValue, index){
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            optionBacklog(doa) {
                if (doa === '删除') {
                    this.deleteStationsByBackLogId();
                } else if (doa === '添加') {
                    this.addStations();
                }

            },
            addStations() {
                //获取空闲的空位
                if (this.selected.length === 0) {
                    this.$message('请选择添加项');
                    return;
                }
                let ids = []
                this.selected.forEach((row) => {
                    ids.push(row.id);
                })
                this.backLogDTO.ids = ids;
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                backlogModel.addStations(this.backLogDTO).then(() => {
                    Notification({
                        message: '新增成功',
                        type: 'success'

                    })
                    this.ifStation = false
                    loading.close();
                    this.list();
                }).catch(err => {
                    Notification({
                        message: err.msg,
                        type: "warning"

                    })
                })
            },

            beforeAddStations(row) {
                this.$store.commit('startLoading')
                this.doa = '添加'
                this.backLogDTO.backlogId = row.id;
                //获取空闲的工位
                backlogModel.getOtherStations().then(data => {
                    this.stations = data.entity;
                    this.ifStation = true;
                    this.$store.commit('endLoading')
                }).catch(err => {
                    Notification({
                        message: err.msg,
                        type: "warning"
                    })
                })
            },
            deleteStationsByBackLogId() {
                if (this.selected.length === 0) {
                    this.$message('请选择添加项');
                    return;
                }
                let ids = []
                this.selected.forEach((row) => {
                    ids.push(row.id);
                })
                this.backLogDTO.ids = ids;
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                backlogModel.deleteStationsByBackLogId(this.backLogDTO).then(() => {
                    Notification({
                        message: '删除成功',
                        type: 'success'

                    });
                    loading.close();
                    this.list();
                }).catch(err => {
                    Notification({
                        message: err.msg,
                        type: "warning"

                    })
                })
                this.ifStation = false
            },
            showStations(row) {
                this.$store.commit('startLoading');
                this.doa = '删除';
                this.backLogDTO.backlogId = row.id;
                //获取与该记录关联的工位
                backlogModel.getStationsByBackLogId(row.id).then(data => {
                    this.stations = data.entity;
                    this.ifStation = true;
                    this.$store.commit('endLoading')
                }).catch(err => {
                    Notification({
                        message: err.msg,
                        type: "warning"
                    })
                })
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.mode == 'edit') {
                            backlogModel.update(this.editForm)
                                .then((data) => {
                                    if (data.status == 'failed') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        return
                                    }
                                    Notification({
                                        message: '编辑成功',
                                        type: 'success'

                                    })
                                    this.editFormVisible = false
                                    this.list();
                                }).catch(() => {

                                }
                            )
                        } else {
                            backlogModel.add(this.editForm)
                                .then((data) => {
                                    if (data.status == 'failed') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        return
                                    }
                                    Notification({
                                        message: '新建成功',
                                        type: 'success'

                                    })
                                    this.editFormVisible = false
                                    this.list()
                                })
                        }

                    } else {
                        return false;
                    }
                });
            },
            addClick() {
                this.mode = 'add';
                this.editForm = {
                    backlogName: '',
                    backlogCode: '',
                    makeSpan: '',
                    type: '',
                    source: '',
                    prodLine: {
                        id: ''
                    }
                }
                this.editFormVisible = true
            },
            editClick(row) {

                backlogModel.getById(row.id).then(data => {
                    this.mode = 'edit';
                    this.editForm = data.entity;
                    if (this.editForm.prodLine == null) {
                        this.editForm.prodLine = {
                            id: ''
                        }
                    }
                    this.editFormVisible = true
                }).catch((data) => {
                    /*Notification({
                        message: data.msg,
                        type: 'warning'
                    })*/
                })

            },
            deleteClick(row) {
                this.$confirm('确认删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading')
                    backlogModel.delete(row.id).then(data => {
                        this.$store.commit('endLoading')
                        this.list();
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
                    backlogModel.deleteByIds(ids).then(data => {
                        if (data.status === 'failed') {
                            Notification({
                                message: data.msg,
                                type: "warning"
                            })
                        }
                        this.$store.commit('endLoading')
                        this.list();
                    })

                }).catch(() => {
                })
            },
            list() {
                this.$store.commit('startLoading')
                backlogModel.getAllProdLines().then(data => {
                    this.prodLines = data.entity;
                }).catch((err) => {
                    console.log("load selectbox data err!", err)
                });

                backlogModel.list(this.pageInfo).then(data => {
                    let entity = data.entity
                    this.backlogDatas = data.entity.content;
                    this.pageInfo.total = data.entity.totalElements
                    this.$store.commit('endLoading')
                }).catch(err => {
                    console.log("err", err);
                })
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize = val
                this.list()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val
                this.list()
            }
        },

    }
</script>
<style>
</style>