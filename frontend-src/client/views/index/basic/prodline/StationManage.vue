<template>
    <div>
        <el-table
                :data="datas"
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
                    prop="stationCode"
                    label="工位编码"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="stationName"
                    label="工位名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="prodLine.lineName"
                    label="所属产线"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="backlogId"
                    label="所属工序"
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
            <el-table-column
                    label="操作"
                    align="left">
                <template slot-scope="scope">
                    <el-button size="mini" @click="editClick(scope.row)">编辑</el-button>
                    <el-button type="danger" size="mini" @click="deleteClick(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle" style="margin-top:15px">
            <el-col>
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
                <el-form-item label="工位编码" prop="stationCode">
                    <el-input v-model="editForm.stationCode"></el-input>
                </el-form-item>
                <el-form-item label="工位名称" prop="stationName">
                    <el-input v-model="editForm.stationName" ></el-input>
                </el-form-item>
                
                <el-form-item label="所属产线" prop="prodLine">
                    <el-select v-model="editForm.prodLine.id" placeholder="" @change="loadBacklogs()">
                        <el-option
                                v-for="item in prodLines"
                                :key="item.id"
                                :label="item.lineName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="所属工序" prop="backlogId">
                    <el-select v-model="editForm.backlogId" placeholder="" :disabled="checkProdId" >
                        <el-option
                                v-for="item in backLogs"
                                :key="item.id"
                                :label="item.backlogName"
                                :value="item.id">
                        </el-option>
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
    import stationModel from '../../../../model/prodline/station'
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
                pageInfo:{
                    currentPage:1,
                    total:0,
                    pageSize:this.GLOBAL.pageSize,
                    pageSizes:this.GLOBAL.pageSizes
                },
                datas:[],
                selected:[],
                mode:'',
                editFormTitle:'',
                editFormVisible:false,
                editForm:null,
                eidtFormRules: {
                    stationName: [
                        {required: true, message: '必填字段', trigger: 'change'},

                    ],
                    stationCode: [
                        {required: true, message: '必填字段', trigger: 'change'},
                    ],
                    backlogId: [
                        {required: true, message: '必填字段', trigger: 'change'},
                    ],
                    prodLine:[
                        {required: true,validator: checkProdLine,trigger:'change'},
                    ]
                },
                prodLines:[],
                backLogs:[],
                checkProdId:true
            }
        },
        mounted() {
            this.list();
        },
        methods: {
            loadBacklogs(){
                this.checkProdId = false;
                this.editForm.backlogId = "";
                stationModel.getBacklogsByProdId(this.editForm.prodLine.id).then(data=>{
                    this.backLogs = data.entity;

                })
            },
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
                            stationModel.update(this.editForm)
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
                            stationModel.add(this.editForm)
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
                                    loading.close();
                                    this.editFormVisible=false
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
                    stationName:'',
                    stationCode:'',
                    backlogId:'',
                    remark:'',
                    prodLine:{
                        id:''
                    }
                }
                this.editFormVisible=true
            },
            editClick(row) {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                stationModel.getById(row.id).then(data=>{
                    this.mode ='edit';
                    this.editForm = data.entity;
                    if(this.editForm.prodLine == null){
                        this.editForm.prodLine={
                            id:''
                        }
                    }
                
                    this.editForm.backlogId = parseInt(this.editForm.backlogId)
                    stationModel.getBacklogsByProdId(this.editForm.prodLine.id).then(data=>{
                        this.backLogs = data.entity;
                    })
                    
                    this.editFormVisible=true
                }).catch((data)=>{
                })
                loading.close();

            },
            deleteClick(row) {
                this.$confirm('确认删除?','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading')
                    stationModel.delete(row.id).then(data=>{
                        this.$store.commit('endLoading')
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
                    stationModel.deleteByIds(ids).then(()=>{
                        this.$store.commit('endLoading')
                        this.list();
                    })

                }).catch(()=>{

                })
            },
            list() {
                this.$store.commit('startLoading')
                stationModel.getAllProdLines().then(data=>{
                    this.prodLines  = data.entity;
                }).catch((err)=>{
                    console.log("load selectbox data err!",err)
                });
                stationModel.list(this.pageInfo).then(data =>{
                    this.datas=data.entity.content;
                    this.pageInfo.total=data.entity.totalElements
                    
                }).catch(err=>{
                    console.log("err",err);
                })
                this.$store.commit('endLoading')
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