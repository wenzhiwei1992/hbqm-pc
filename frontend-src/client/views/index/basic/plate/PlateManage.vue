<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true" :model="formInline">
                            <el-form-item>
                                <el-input v-model="formInline.plateNo" placeholder="模台号"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-select v-model="formInline.prodLine.id" placeholder="产线"  clearable>
                                    <el-option
                                            v-for="item in prodLines"
                                            :key="item.id"
                                            :label="item.lineName"
                                            :value="item.id">
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
                :style="`height:${this.$store.state.mainContentHeight}px`">
                <el-table
                :data="plates"
                @selection-change="handleSelectionChange"
                ref="handleClickRowSelection"
                @row-click="handleClickRowSelection"
                size="mini"
                stripe
                border
                height="100%"
                style="width:100%">
                    <el-table-column
                            type="selection"
                            width="39">
                    </el-table-column>
                    <el-table-column
                            type="index"
                            label="序号"
                            width="50">
                    </el-table-column>

                    <el-table-column
                            sortable
                            prop="plateNo"
                            label="模台号"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="plateWidth"
                            label="宽度(cm)"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="plateLen"
                            label="长度(cm)"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="prodLine.lineName"
                            label="所属产线"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="plateStatus"
                            label="状态"
                            width="120">
                    </el-table-column>
                    <!-- <el-table-column
                            prop="dateCreated"
                            :formatter="formatterDate"
                            label="创建时间"
                            width="140">
                    </el-table-column> -->
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
                            <!-- <el-button size="mini" @click="editClick(scope.row)" icon="el-icon-edit"></el-button> -->
                            <!-- <el-button type="danger" size="mini" @click="deleteClick(scope.row)">删除</el-button> -->
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
        <el-dialog :title="mode=='add'?'新建':'编辑'" :visible.sync="editFormVisible" v-if="editFormVisible">
            <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px">
                <el-form-item label="模台号" prop="plateNo">
                    <el-input v-model="editForm.plateNo"  :disabled="mode=='edit' "></el-input>
                </el-form-item>
      <!--          <el-form-item label="初始值" prop="plateInitial" v-if="mode=='add'">
                    <el-input v-model="editForm.plateInitial"  type="number"></el-input>
                </el-form-item>-->
                <el-form-item label="宽度(cm)" prop="plateWidth">
                    <el-input v-model="editForm.plateWidth" type="number"></el-input>
                </el-form-item>
                <el-form-item label="长度(cm)" prop="plateLen">
                    <el-input v-model="editForm.plateLen" type="number"></el-input>
                </el-form-item>
                <el-form-item label="模台状态" prop="plateStatus">
                    <el-select  v-model="editForm.plateStatus" placeholder="请选择">
                        <el-option value="启用" label="启用"></el-option>
                        <el-option value="禁用" label="禁用"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="模台数量" prop="plateCount" v-if="mode=='add'">
                    <el-input v-model="editForm.plateCount" type="number"></el-input>
                </el-form-item>
                <el-form-item label="产线" prop="prodLine" >
                    <el-select v-model="editForm.prodLine.id" placeholder="请选择" :disabled="mode=='add'?false:true">
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
    import PlateModel from  '../../../../model/basicdata/plate.js'
    import {Notification} from 'element-ui'
    import menuTabService from '../../../../service/menuTabService'
    import moment from "moment";
    export default {
        data(){
            var checkPlateSizeCount = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('填写合法数字'));
                }
                var r = /^\+?[1-9][0-9]*$/;
                if (!r.test(value)) {
                    callback(new Error('请输入正整数'));
                } else {
                    if(value<1){
                        callback(new Error('必须大于0'));
                    } else {
                        callback();
                    }
                }
            };
            var checkPlateCount = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('填写合法数字'));
                }
                var r = /^\+?[1-9][0-9]*$/;
                if (!r.test(value)) {
                    callback(new Error('请输入正整数'));
                } else {
                    if (value > 500) {
                        callback(new Error('必须小于500'));
                    }else if(value<1){
                        callback(new Error('必须大于0'));
                    } else {
                        callback();
                    }
                }
            };
            return {
                pageInfo:{
                    currentPage:1,
                    total:0,
                    pageSize:this.GLOBAL.pageSize,
                    pageSizes:this.GLOBAL.pageSizes
                },
                plates:[],
                selected:[],
                mode:'',
                editFormTitle:'',
                editFormVisible:false,
                editForm:null,
                eidtFormRules: {
                    plateNo: [
                        {required: true, message: '请输入模台号', trigger: 'blur'},
                    ],
                    plateWidth:[
                        {required: true, validator: checkPlateSizeCount, trigger: 'change'},
                    ],
                    plateLen:[
                        {required: true, validator: checkPlateSizeCount, trigger: 'change'},
                    ],
                    plateCount:[
                        {required: true, validator: checkPlateCount, trigger: 'change'},
                    ],
                    prodLine: [
                        {required: true, message: '请选择产线', trigger: 'change'},
                    ],
                    plateInitial: [
                        {required: true, message: '请输入初始值(会从该值开始生成模台编号)', trigger: 'change'},
                    ],

                },
                prodLines:[],
                formInline:{
                    prodLine:{
                        id:''
                    }
                },
                searchButtonData:[
                    {
                        name:'查询',
                        event:'onSubmit'
                    }
                ],
                buttonData:[
                    {
                        name:'新建',
                        event:'addClick'
                    },
                    {
                        name:'删除',
                        event:'deletesClick',
                        authorized:'plateManage:newDelete'
                    },
                ],
                rowButtonData:[
                    {
                        name:'编辑',
                        event:'editClick'
                    }
                ]
            }
        },
        mounted(){
            this.plateList();
        },
        methods:{
            onSubmit(){
                this.plateList();
            },
            formatterDate(row, column, cellValue, index){
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            plateList(){
                this.$store.commit('startLoading');
                PlateModel.getAllProdLines().then(data=>{
                    this.prodLines  = data.entity;
                }).catch((err)=>{
                    console.log("load selectbox data err!",err)
                });
                this.formInline.pageInfo = this.pageInfo;
                PlateModel.plateList(this.formInline).then(data=>{

                    this.plates=data.entity.content;
                    this.pageInfo.total=data.entity.totalElements;
                    this.$store.commit('endLoading')
                }).catch((err)=>{
                    /*Notification({
                        message: err,
                        type: 'success'
                    });*/
                })
            },
            addClick() {
                this.mode ='add';
                this.editForm ={
                    plateNo:'',
                    plateWith:'',
                    plateLen:'',
                    plateStatus:'',
                    plateCount:1,
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
                PlateModel.plateById(row.id).then(data=>{
                    this.mode ='edit';
                    this.editForm = data.entity;
                    this.editForm.plateCount = 1;
                    loading.close();
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
                    this.$store.commit('startLoading')
                    PlateModel.plateListDelete(row.id).then(data=>{
                        this.$store.commit('endLoading')
                        if(data.status == 'success'){
                            Notification({
                                type:'success',
                                message:'删除成功'
                            })
                        }else{
                            Notification({
                                type:'error',
                                message:data.msg
                            })
                        }
                        this.plateList();
                    })
                }).catch(()=>{

                })
            },
            deletesClick(){
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
                    PlateModel.plateListDeletes(ids).then((data)=>{
                        this.$store.commit('endLoading')
                        if(data.status == 'success'){
                            Notification({
                                type:'success',
                                message:'删除成功'
                            })
                        }else{
                            Notification({
                                type:'success',
                                message:data.msg
                            })
                        }
                        this.plateList();
                    })

                }).catch(()=>{

                })
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
                            PlateModel.plateUpdate(this.editForm)
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

                                    this.editFormVisible=false
                                    this.plateList();
                                }).catch(()=>{

                                }

                            )
                            loading.close();
                        }else{
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            PlateModel.plateAdd(this.editForm)
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
                                    this.plateList()
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
                this.plateList()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val
                this.plateList()
            },
            authorized(index){
                return menuTabService.authorized(this.$store,index)
            }

        }


    }

</script>
<style>


</style>