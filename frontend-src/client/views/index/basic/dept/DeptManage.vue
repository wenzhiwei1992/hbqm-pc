<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <my-button-group :model="buttonData"></my-button-group>
                </el-row>
            </el-header>
            <el-main
                ref="mainContent"
                :style="`height:${this.$store.state.mainContentHeight}px`"
            >
                <el-table
                :data="depts"
                @selection-change="handleSelectionChange"
                size="mini"
                stripe
                border
                style="width: 100%"
                ref="handleClickRowSelection"
                @row-click="handleClickRowSelection"
                height="100%"
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
                    prop="deptName"
                    label="部门名称"
                    width="120">
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
                    <el-button size="mini" @click.stop="editClick(scope.row)" icon="el-icon-edit"></el-button>
<!--                    <el-button type="danger" size="mini" @click="deleteClick(scope.row)">删除</el-button>-->
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
                <el-form-item label="部门名称" prop="deptName">
                    <el-input v-model="editForm.deptName"></el-input>
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
    import deptModel from '../../../../model/basicdata/dept'
    import {Notification} from 'element-ui'
    import  moment from  'moment'
    export default {
        data() {
            return {
                pageInfo:{
                    currentPage:1,
                    total:0,
                    pageSize:this.GLOBAL.pageSize,
                    pageSizes:this.GLOBAL.pageSizes
                },
                depts:[],
                selected:[],
                mode:'',
                editFormTitle:'',
                editFormVisible:false,
                editForm:null,
                eidtFormRules: {
                    deptName: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ]
                },
                prodLines:[],
                buttonData:[
                    {
                        name:'新建',
                        event:'addClick'
                    },
                    {
                        name:'删除',
                        event:'deletesClick'
                    }
                ]
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
            editFormSubmmit(formName){

                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.mode=='edit'){
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            deptModel.deptListUpdate(this.editForm)
                                .then((data) => {
                                    if(data.status=='failed'){
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        });
                                        return
                                    }
                                    Notification({
                                        message: '编辑成功',
                                        type: 'success'

                                    });
                                    loading.close();
                                    this.editFormVisible=false;
                                    this.list();
                                }).catch(()=>{

                                }
                            )
                        }else{
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });

                            deptModel.deptListAdd(this.editForm)
                                .then((data) => {
                                    if(data.status=='failed'){
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        });
                                        return
                                    }
                                    Notification({
                                        message: '新建成功',
                                        type: 'success'

                                    });
                                    this.editFormVisible=false;
                                    loading.close();
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
                this.mode ='add';
                this.editForm ={
                    deptName:''
                };
                this.editFormVisible=true
            },
            editClick(row) {
                this.$store.commit('startLoading');
                deptModel.deptById(row.id).then(data=>{
                    this.mode ='edit';
                    this.editForm = data.entity;
                    this.$store.commit('endLoading');
                    this.editFormVisible=true
                }).catch((data)=>{
                })

            },
            deleteClick(row) {
                this.$confirm('确认删除?','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading');
                    deptModel.deptListDelete(row.id).then(data=>{
                        this.$store.commit('endLoading');
                        if(data.status ==='failed'){
                            Notification({
                                message:data.msg,
                                type:"warning"
                            })
                        }
                        this.list();
                    })
                }).catch(()=>{

                })
            },
            deletesClick() {
                if(this.selected.length ==0){
                    this.$message('请选择删除项');
                    return;
                }
                let ids=[];
                this.selected.forEach((row)=>{
                    ids.push(row.id)
                });
                this.$confirm('确认删除选择项?','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(()=>{
                    this.$store.commit('startLoading');
                    deptModel.deptListDeletes(ids).then(()=>{
                        this.$store.commit('endLoading');
                        this.list();
                    })

                }).catch(()=>{

                })
            },
            list() {
                this.$store.commit('startLoading');
                deptModel.list(this.pageInfo).then(data =>{
                    this.depts=data.entity.content;
                    this.pageInfo.total=data.entity.totalElements;
                    this.$store.commit('endLoading')
                }).catch(err=>{
                })
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize = val;
                this.list()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val;
                this.list()
            }
        },

    }
</script>
<style>

</style>