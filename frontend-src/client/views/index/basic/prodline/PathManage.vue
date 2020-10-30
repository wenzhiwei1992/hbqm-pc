<template>
    <div>
        <el-table
                :data="pathDatas"
                @selection-change="handleSelectionChange"
                size="mini"
                stripe
                border
                height="500"
                style="width: 100%"
        >
            <el-table-column
                    type="selection"
                    width="80">
            </el-table-column>
            <el-table-column
                    type="index"
                    label="序号"
                    width="50">
            </el-table-column>
            <el-table-column
                    prop="pathCode"
                    label="路径编码"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="pathName"
                    label="路径名称"
                    width="120">
            </el-table-column>
            
            <el-table-column
                    prop="pathDesc"
                    label="路径描述"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="prodLine.lineName"
                    label="所属产线"
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
                    <el-button size="mini" @click="beforeAddBackLogs(scope.row)">添加工序</el-button>
                    <el-button size="mini" @click="showBackLog(scope.row)">显示工序</el-button>
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
                <el-form-item label="工艺编码" prop="pathCode">
                    <el-input v-model="editForm.pathCode"></el-input>
                </el-form-item>
                <el-form-item label="工艺名称" prop="pathName">
                    <el-input v-model="editForm.pathName"></el-input>
                </el-form-item>
                <el-form-item label="工艺描述" prop="pathDesc">
                    <el-input v-model="editForm.pathDesc"></el-input>
                </el-form-item>
                <el-form-item label="所属产线" prop="prodLine">
                    <el-select v-model="editForm.prodLine.id" placeholder="请选择" >
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

        <el-dialog :visible.sync="ifBackLog">
            <el-table
                    ref="multipleTable"
                    :data="backLogByPath"
                    tooltip-effect="dark"
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
                        prop="backlogCode"
                        label="工序代码"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="backlogName"
                        label="工序名称"
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
    import pathModel from '../../../../model/prodline/path'
    import {Notification} from 'element-ui'
    import moment from "moment";

    export default {
        data() {
            const checkProdLine = (rule, value, callback) => {
                if (!value.id) {
                    return callback(new Error('产线不能为空'));
                }else{
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
                pathDatas: [],
                selected: [],
                backLogByPath: [],
                ifBackLog: false,
                mode: '',
                editFormTitle: '',
                editFormVisible: false,
                editForm: null,
                eidtFormRules: {
                    pathName:[
                        {required: true, message: '必填字段', trigger:'change'},
                    ],
                    pathCode:[
                        {required: true, message: '必填字段', trigger:'change'},
                    ],
                    prodLine:[
                        {validator: checkProdLine,trigger:'change'},
                    ]

                },
                //数据传输模型，前后端多对多交互的中间模型
                pathDTO: {
                    id: '',
                    backLogSet: []
                },
                //delete or add
                doa: '',
                prodLines:[]

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
                    this.deleteBacklog();
                } else if (doa === '添加') {
                    this.addBackLogs();
                }

            },
            addBackLogs() {
                if (this.selected.length == 0) {
                    this.$message('请选择添加项');
                    return;
                }
                let backLogSet = []
                this.selected.forEach((row) => {
                    let backlog = {}
                    backlog.id = row.id
                    backlog.backlogName = row.backlogName
                    backlog.backlogCode = row.backlogCode
                    backLogSet.push(backlog)
                })
                this.pathDTO.backLogSet = backLogSet;

                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                pathModel.addBacklogFromPathByBackLogIds(this.pathDTO).then(() => {
                    Notification({
                        message: '新增成功',
                        type: 'success'

                    })
                    loading.close();
                    this.ifBackLog = false
                    this.list();
                }).catch(err => {
                    Notification({
                        message: err.msg,
                        type: "warning"

                    })
                })

            },
            beforeAddBackLogs(row) {
                //get data by row.id
                this.$store.commit('startLoading')
                this.doa = '添加'

                pathModel.getOtherBackLogByPathId(row.id).then(data => {
                    this.pathDTO.id = row.id;
                    this.backLogByPath = data.entity;
                    // console.log(this.backLogByPath);
                    this.ifBackLog = true;
                    this.$store.commit('endLoading')
                }).catch(err => {
                    Notification({
                        message: err.msg,
                        type: "warning"
                    })
                })
            },
            deleteBacklog() {

                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }
                let backLogSet = []
                this.selected.forEach((row) => {
                    let backlog = {}
                    backlog.id = row.id
                    backlog.backlogName = row.backlogName
                    backlog.backlogCode = row.backlogCode
                    backLogSet.push(backlog)
                })
                this.pathDTO.backLogSet = backLogSet;
                this.$confirm('确认删除选择项?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    pathModel.deleteBacklogFromPathByBackLogIds(this.pathDTO).then(() => {
                        Notification({
                            message: '删除成功',
                            type: 'success'

                        })
                        loading.close();
                        this.ifBackLog = false
                        this.list();
                    })

                }).catch(err => {

                })
            },
            showBackLog(row) {
                //get data by row.id
                this.$store.commit('startLoading')
                this.doa = '删除'
                pathModel.getBackLogByPathId(row.id).then(data => {
                    this.pathDTO.id = row.id;
                    this.backLogByPath = data.entity;
                    // console.log(this.backLogByPath);
                    this.ifBackLog = true;
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
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            pathModel.update(this.editForm)
                                .then((data) => {
                                    if (data.status == 'failed') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close();
                                        return
                                    }
                                    Notification({
                                        message: '编辑成功',
                                        type: 'success'

                                    })
                                    loading.close();
                                    this.editFormVisible = false
                                    this.list();
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
                            pathModel.add(this.editForm)
                                .then((data) => {
                                    if (data.status == 'failed') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close();
                                        return
                                    }
                                    Notification({
                                        message: '新建成功',
                                        type: 'success'

                                    })
                                    loading.close();
                                    this.editFormVisible = false
                                    this.list()
                                })
                        }

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            addClick() {
                this.mode = 'add';
                this.editForm = {
                    pathName: '',
                    pathCode: '',
                    pathDesc: '',
                    prodLine:{
                        id:''
                    }
                }
                this.editFormVisible = true
            },
            editClick(row) {

                pathModel.getById(row.id).then(data => {
                    this.mode = 'edit';

                    this.editForm = data.entity;
                    if(this.editForm.prodLine == null){
                        this.editForm.prodLine={
                            id:''
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
                    pathModel.delete(row.id).then(data => {
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
                    pathModel.deleteByIds(ids).then(() => {
                        this.$store.commit('endLoading')
                        this.list();
                    })

                }).catch(() => {

                })
            },
            list() {
                this.$store.commit('startLoading')
                pathModel.getAllProdLines().then(data=>{
                    this.prodLines  = data.entity;
                }).catch((err)=>{
                    console.log("load selectbox data err!",err)
                });

                pathModel.list(this.pageInfo).then(data => {
                    let entity = data.entity
                    console.log("entity", entity);//TODO DELETE
                    this.pathDatas = data.entity.content;
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