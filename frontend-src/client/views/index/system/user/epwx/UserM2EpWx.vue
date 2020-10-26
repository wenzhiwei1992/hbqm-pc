<template>
    <div>
        <el-row>
            <el-col>
                <el-button style="margin-bottom:10px" @click="addClick" type="primary" icon="el-icon-plus">新建</el-button>
            </el-col>
        </el-row>
        <el-table
                :data="datas"
                size="mini"
                stripe
                border
                style="width: 100%"
        >
            <el-table-column
                    type="index"
                    label="序号"
                    width="60">
            </el-table-column>
            <el-table-column
                    prop="userName"
                    label="用户名">
            </el-table-column>
            <!--<el-table-column-->
                    <!--prop="userId"-->
                    <!--label="用户ID">-->
            <!--</el-table-column>-->
            <el-table-column
                    prop="phone"
                    label="手机号">
            </el-table-column>
            <el-table-column
                    prop="epWxUserId"
                    label="三方用户ID">
            </el-table-column>
            <el-table-column prop="type" label="三方平台" align="center">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.type == 2">钉钉</el-tag>
                    <el-tag v-else>企业微信</el-tag>
                </template>
            </el-table-column>
            <el-table-column
                    label="操作"
                    align="left">
                <template slot-scope="scope">
                    <el-button size="mini" @click="editClick(scope.row)" icon="el-icon-edit"></el-button>
                    <el-button size="mini" @click="deletesClick(scope.row)" icon="el-icon-delete"></el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle" >
            <el-col style="margin-top:15px">
                
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
            <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="160px">

                <el-form-item label="用户" prop="userId">
                    <el-select v-model="editForm.userId" filterable  placeholder="请选择">
                        <el-option
                                v-for="item in userSelect"
                                :key="item.id"
                                :label="item.realName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="手机号" prop="phone" v-if="mode!='add' || editFormDDVisible">
                    <el-input v-model="editForm.phone"  ></el-input>
                </el-form-item>

                <el-form-item label="企业微信用户ID" prop="epWxUserId" v-if="mode!='add' || !editFormDDVisible">
                    <el-input v-model="editForm.epWxUserId"></el-input>
                </el-form-item>

                <el-form-item label="三方平台"  prop="type">
                    <el-select  v-model="editForm.type" placeholder="请选择"
                                @change="changePlatform"
                    >
                        <el-option
                                v-for="item in platFormSelect"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
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
    import m2EpWx from '../../../../../model/system/m2EpWx'
    import purchaseModel  from '../../../../../model/purchase/purchase'
    import {Notification} from 'element-ui'
    import  moment from  'moment'
    export default {
        data() {
            return {
                pageInfo:{
                    currentPage: 1,
                    total:0,
                    pageSize: 10,
                    pageSizes:this.GLOBAL.pageSizes
                },
                datas:[],
                userSelect:[],
                mode:'',
                editFormVisible:false,
                editFormDDVisible:false,
                editForm:{

                },
                platFormSelect:[
                    {
                        label:'企业微信',
                        value:1
                    },
                    {
                        label:'钉钉',
                        value:2
                    },
                ],
                eidtFormRules: {
                    userId: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    type:[
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ]
                },
            }
        },
        mounted() {
            this.list();
            this.getUserSelect();
        },
        methods: {
            changePlatform(){
                if(this.editForm.type == 1){//企业微信
                    this.editFormDDVisible = false
                }else if(this.editForm.type == 2){//钉钉
                    this.editFormDDVisible = true
                }
                console.log(this.editFormDDVisible)
            },
            getUserSelect(){
                purchaseModel.getUsersByOrgId().then((data)=>{
                    this.userSelect = data.entity;
                });
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
                        if(this.editForm.type==1){//企业微信
                            if(!this.editForm.epWxUserId){
                                Notification({
                                    message: "绑定企业微信用户必须输入企业微信用户id",
                                    type: 'warning'
                                });
                                return
                            }
                        }else if(this.editForm.type==2){//钉钉
                            if(!this.editForm.phone){
                                Notification({
                                    message: "绑定钉钉用户必须输入用户手机号",
                                    type: 'warning'
                                });
                                return
                            }
                        }
                        if(this.mode=='edit'){
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            m2EpWx.edit(this.editForm)
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

                            m2EpWx.add(this.editForm)
                                .then((data) => {
                                    if(data.status=='failed'){
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        });
                                        loading.close();
                                        return
                                    }
                                    Notification({
                                        message:  data.msg,
                                        type: 'success'

                                    });
                                    this.editFormVisible=false;
                                    loading.close();
                                    this.list()
                                })
                        }

                    } else {
                        return false;
                    }
                });
            },
            addClick() {
                this.mode ='add';
                this.editForm ={
                    userId:'',
                    epWxUserId:'',
                    type:''
                };
                this.editFormVisible=true
            },
            editClick(row) {
                this.$store.commit('startLoading');
                m2EpWx.findbyId(row.id).then(data=>{
                    this.mode ='edit';
                    this.editForm = data.entity;
                    this.editForm.userId = Number(data.entity.userId)
                    this.$store.commit('endLoading');
                    this.editFormVisible=true
                }).catch((data)=>{

                })

            },
            deletesClick(row) {
                this.$confirm('确认删除选择项?','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(()=>{
                    m2EpWx.deleteById(row.id).then((data)=>{
                        if(data.status == 'success'){
                            Notification({
                                type:'success',
                                message: this.GLOBAL.MSG.UPDATE_SUCCESS
                            })
                        }
                        this.list();
                    }).catch(err=>{
                        console.log(err);
                    })

                }).catch((err)=>{
                    console.log(err);
                })
            },
            list() {
                this.$store.commit('startLoading');
                let conditon = {
                    pageInfo : this.pageInfo
                }

                m2EpWx.list(conditon).then(data =>{
                    this.datas=data.entity.content;
                    this.pageInfo.total=data.entity.totalElements;
                    this.$store.commit('endLoading')
                }).catch(err=>{
                })
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