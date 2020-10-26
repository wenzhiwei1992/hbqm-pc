<template>
    <div>
            <el-table
                    :data="codeExplainContents"
                    @selection-change="handleSelectionChange"
                    size="mini"
                    stripe
                    border
                    height="500"
            >
                <el-table-column
                        type="selection"
                        width="80">
                </el-table-column>
                <el-table-column
                        sortable
                        prop="id"
                        label="序号"
                        width="80">
                </el-table-column>

                <el-table-column
                        prop="codeExplainId"
                        label="代码大类"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="codeName"
                        label="代码"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="content"
                        label="代码内容"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="remark"
                        label="代码备注"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="dateCreated"
                        :formatter="formatterDate"
                        label="创建时间"
                        width="120">
                </el-table-column>
                <el-table-column
                        label="操作"
                        width="400">
                    <template slot-scope="scope">
                        <el-button size="mini" @click="editCECContent(scope.row)">编辑</el-button>
                        <el-button type="danger" size="mini" @click="deleteCECContent(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <el-dialog :title="mode==='add'?'新建':'编辑'"  :visible.sync="contentsInnerVisible"
                       append-to-body width="60%">
                <el-form :model="editContentForm" v-if="editContentForm" :rules="editContentFormRules" ref="editContentForm" label-width="80px">
                    <el-form-item label="代码" prop="codeName">
                        <el-input v-model="editContentForm.codeName" ></el-input>
                    </el-form-item>
                    <el-form-item label="代码内容" prop="content">
                        <el-input v-model="editContentForm.content"></el-input>
                    </el-form-item>
                    <el-form-item label="代码备注" prop="remark">
                        <el-input v-model="editContentForm.remark"></el-input>
                    </el-form-item>
                    <el-form-item label="代码大类" prop="codeExplainId">
                        <el-input v-model="editContentForm.codeExplainId"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="contentsInnerVisible = false">取 消</el-button>
                    <el-button type="primary" @click="editCECContentSubmmit('editContentForm')">确 定</el-button>
                </div>
            </el-dialog>
        <el-row type="flex" justify="space-around" align="middle" style="margin-top:15px">
            <el-col>
                <el-button type="primary" @click="addCECContentClick">新建</el-button>
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

    </div>

</template>
<script>
    import codeExplain from '../../../../model/basicdata/codeExlpain'
    import codeExplainContent from '../../../../model/basicdata/codeExlpainContent'
    import {Notification} from 'element-ui'
    import moment from "moment";
    export default {
        data() {
            return {
                pageInfo:{
                    currentPage:1,
                    total:0,
                    pageSize:10,
                    pageSizes:[2,5,10]
                },
                datas:[],
                selected:[],
                codeBelongs:[],
                mode:'',
                editFormTitle:'',
                contentsInnerVisible:false,
                editContentForm:null,
                editContentFormRules:{

                },
                codeExplainContents:[],
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

            editCECContentSubmmit(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.mode=='edit'){
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            codeExplainContent.update(this.editContentForm)
                                .then((data) => {
                                    if(data.status=='failed'){
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
                                    loading.close()
                                    this.contentsInnerVisible=false;
                                    this.list()
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
                            codeExplainContent.add(this.editContentForm)
                                .then((data) => {
                                    if(data.status=='failed'){
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
                                    loading.close()
                                    this.contentsInnerVisible=false;
                                    this.list()
                                })
                        }

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            deleteCECContent(row){
                this.$confirm('确认删除?','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading')
                    codeExplainContent.delete(row.id).then(data=>{
                        this.$store.commit('endLoading')
                        this.list();
                    })
                }).catch(()=>{

                })
            },
            editCECContent(row){
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                codeExplainContent.getById(row.id).then(data=>{
                    this.mode ='edit';
                    this.editContentForm = data.entity;
                    loading.close()
                    this.contentsInnerVisible=true
                }).catch((data)=>{
                    /*Notification({
                        message: data.msg,
                        type: 'warning'
                    })*/
                })
            },
            addCECContentClick(){
                this.mode ='add';
                this.editContentForm ={
                    codeName:'',
                    content:'',
                    remark:'',
                    codeExplainId:'' //TODO 修改为集合选择框
                },
                this.contentsInnerVisible=true
            },
            deletesClick() {
                if(this.selected.length ===0){
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
                    codeExplainContent.deleteByIds(ids).then(()=>{
                        this.$store.commit('endLoading')
                        this.list();
                    })

                }).catch(()=>{

                })
            },
            list() {
                this.$store.commit('startLoading')
                codeExplainContent.list(this.pageInfo).then(data =>{
                    this.codeExplainContents=data.entity.content;
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