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
                :data="teamInfos"
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
                    width="60">
            </el-table-column>
            <el-table-column
                    type="index"
                    label="序号">
            </el-table-column>
            <el-table-column
                    prop="teamName"
                    label="班组">
            </el-table-column>
            <el-table-column
                    prop="dept"
                    label="部门">
            </el-table-column>
            <el-table-column
                    prop="workshop"
                    label="车间">
            </el-table-column>
            <el-table-column
                    prop="prodline"
                    label="产线">
            </el-table-column>
            <el-table-column
                    prop="dateModified"
                    :formatter="formatterDate"
                    label="修改时间">
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
                <el-form-item label="班组名称" prop="teamName">
                    <el-input v-model="editForm.teamName"></el-input>
                </el-form-item>
                <el-form-item label="部门" prop="dept">
                    <el-select v-model="editForm.dept" placeholder="" filterable>
                        <el-option
                                v-for="item in depts"
                                :key="item.id"
                                :label="item.deptName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="车间" prop="workshop">
                    <el-input v-model="editForm.workshop"></el-input>
                </el-form-item>
                <el-form-item label="产线" prop="prodline">
                    <el-select v-model="editForm.prodline" placeholder="" >
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

    </div>
</template>
<script>
    import teamInfoModel from '../../../../model/basicdata/teamInfo'
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
                teamInfos:[],
                selected:[],
                mode:'',
                editFormTitle:'',
                editFormVisible:false,
                editForm:null,
                eidtFormRules: {
                    teamName: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    workshop: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    dept: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ]
                },
                prodLines:[],
                depts:[],
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
            this.teamInfoList();
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
                            teamInfoModel.teamInfoListUpdate(this.editForm)
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
                                    this.teamInfoList();
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

                            teamInfoModel.teamInfoListAdd(this.editForm)
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
                                    this.teamInfoList()
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

                };
                this.editFormVisible=true
            },
            editClick(row) {
                this.$store.commit('startLoading');
                teamInfoModel.teamInfoById(row.id).then(data=>{
                    this.mode ='edit';
                    this.editForm = data.entity;
                    this.editForm.dept = parseInt(this.editForm.dept)
                    this.editForm.prodline = parseInt(this.editForm.prodline)
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
                    teamInfoModel.teamInfoListDelete(row.id).then(data=>{
                        this.$store.commit('endLoading');
                        if(data.status ==='failed'){
                            Notification({
                                message:data.msg,
                                type:"warning"
                            })
                        }
                        this.teamInfoList();
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
                    teamInfoModel.teamInfoListDeletes(ids).then(()=>{
                        this.$store.commit('endLoading');
                        this.teamInfoList();
                    })

                }).catch(()=>{

                })
            },
            teamInfoList() {
                this.$store.commit('startLoading');
                teamInfoModel.getAllProdLines().then(data=>{
                    this.prodLines  = data.entity;
                }).catch((err)=>{
                });
                teamInfoModel.getAllDepts().then(data=>{
                    this.depts  = data.entity;
                }).catch((err)=>{

                });
                
                teamInfoModel.teamInfoList(this.pageInfo).then(data =>{
                    this.teamInfos=data.entity.content;
                    this.pageInfo.total=data.entity.totalElements;

                    for(let i =0;i< this.teamInfos.length;i++){
                        for(let j =0;j< this.prodLines.length;j++){
                            if(this.teamInfos[i].prodline == this.prodLines[j].id){
                                this.teamInfos[i].prodline=this.prodLines[j].lineName
                            }
                        }
                    }
                    for(let i =0;i< this.teamInfos.length;i++){
                        for(let j =0;j< this.depts.length;j++){
                            if(this.teamInfos[i].dept == this.depts[j].id){
                                this.teamInfos[i].dept=this.depts[j].deptName
                            }
                        }
                    }
                    this.$store.commit('endLoading')
                }).catch(err=>{
                })
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize = val;
                this.teamInfoList()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val;
                this.teamInfoList()
            }
        },

    }
</script>
<style>

</style>