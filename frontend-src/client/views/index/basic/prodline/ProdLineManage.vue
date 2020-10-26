<template>
    <div>
        <el-table
                :data="prodLines"
                @selection-change="handleSelectionChange"
                size="mini"
                stripe
                border
                style="width:100%"
                height="500"
        >
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
                    prop="lineNo"
                    label="产线号"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="lineName"
                    label="产线名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="lineDesc"
                    label="产线描述"
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
                    <el-button size="mini" @click="editClick(scope.row)">编辑</el-button>
                    <el-button type="danger" size="mini" @click="deleteClick(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle" >
            <el-col style="margin-top:15px">
                <el-button type="primary" @click="addClick">新建产线</el-button>
                <el-button @click="deletesClick">删除勾选产线</el-button>
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
        <el-dialog :title="mode=='add'?'新建':'编辑'" :visible.sync="editFormVisible" v-if="editFormVisible">
            <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px">
                <el-form-item label="产线编号" prop="lineNo">
                    <el-input v-model="editForm.lineNo"></el-input>
                </el-form-item>
                <el-form-item label="产线名称" prop="lineName">
                    <el-input v-model="editForm.lineName"></el-input>
                </el-form-item>
                <el-form-item label="产线描述" prop="lineDesc">
                    <el-input v-model="editForm.lineDesc"></el-input>
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
    import prodLineModel from '../../../../model/prodline/prodline'
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
                prodLines:[],
                selected:[],
                mode:'',
                editFormTitle:'',
                editFormVisible:false,
                editForm:null,
                eidtFormRules: {
                    lineName: [
                        {required: true, message: '请输入产线名称', trigger: 'blur'},
                    ],
                    lineNo: [
                        {required: true, message: '请输入产线编号', trigger: 'blur'},
                    ]


                }
            }
        },
        mounted() {
            this.prodLineList();
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
                            prodLineModel.prodLineListUpdate(this.editForm)
                                .then((data) => {
                                    if(data.status=='failed'){
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
                                    this.editFormVisible=false
                                    this.prodLineList();
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

                            prodLineModel.prodLineListAdd(this.editForm)
                                .then((data) => {
                                    if(data.status=='failed'){
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
                                    this.editFormVisible=false
                                    loading.close();
                                    this.prodLineList()
                                })
                        }

                    } else {
                        //console.log('error submit!!');
                        return false;
                    }
                });
            },
            addClick() {
                this.mode ='add';
                this.editForm ={
                    lineNo:'',
                    lineName:'',
                    lineDesc:''
                }
                this.editFormVisible=true
            },
            editClick(row) {
                this.$store.commit('startLoading')
                prodLineModel.prodLineById(row.id).then(data=>{
                    this.mode ='edit';
                    this.editForm = data.entity;
                    this.$store.commit('endLoading')
                    this.editFormVisible=true
                }).catch((data)=>{
                    /*Notification({
                        message: data.msg,
                        type: 'warning'
                    })*/
                })

            },
            deleteClick(row) {
                this.$confirm('确认删除?','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading')
                    prodLineModel.prodLineListDelete(row.id).then(data=>{
                        this.$store.commit('endLoading')
                        if(data.status ==='failed'){
                            Notification({
                                message:data.msg,
                                type:"warning"
                            })
                        }
                        this.prodLineList();
                    })
                }).catch(()=>{

                })
            },
            deletesClick() {
                if(this.selected.length ==0){
                    this.$message('请选择删除项');
                    return;
                }
                let ids=[]
                this.selected.forEach((row)=>{
                    ids.push(row.id)
                })
                this.$confirm('确认删除选择项?','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(()=>{
                    this.$store.commit('startLoading')
                    prodLineModel.prodLineListDeletes(ids).then(()=>{
                        this.$store.commit('endLoading')
                        this.prodLineList();
                    })

                }).catch(()=>{

                })
            },
            prodLineList() {
                this.$store.commit('startLoading')
                prodLineModel.prodLineList(this.pageInfo).then(data =>{
                    this.prodLines=data.entity.content;
                    this.pageInfo.total=data.entity.totalElements
                    this.$store.commit('endLoading')
                }).catch(err=>{
                    console.log("err",err);
                })
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize = val
                this.prodLineList()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val
                this.prodLineList()
            }
        },

    }
</script>
<style>
</style>