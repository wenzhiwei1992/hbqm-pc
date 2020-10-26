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
                    prop="name"
                    label="名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="typeNo"
                    label="编号"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="remarks"
                    label="备注"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="dateCreated"
                    :formatter="formatterDate"
                    label="创建时间"
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
        <el-dialog :title="mode==='add'?'新建':'编辑'" :visible.sync="editFormVisible">
            <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px">
                <el-form-item label="构件分类" prop="productCategoriesId">
                    <el-select v-model="editForm.productCategoriesId" placeholder="请选择" style="width:150px" clearable>
                        <el-option
                                v-for="item in arrProductCategories"
                                :key="item.id"
                                :label="item.productCategoriesName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="构件类型名称" prop="name">
                    <el-input v-model="editForm.name" ></el-input>
                </el-form-item>
                <el-form-item label="构件类型编号" prop="typeNo">
                    <el-input v-model="editForm.typeNo"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="remarks">
                    <el-input v-model="editForm.remarks"></el-input>
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
    import productCategoriesModel from '../../../../model/basicdata/productCategories'
    import productTypeModel from '../../../../model/basicdata/productType'
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
                arrProductCategories:[],
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
                    productCategoriesId:[
                        {required: true, message: '必填字段', trigger: 'change'},
                    ],
                    name: [
                        {required: true, message: '必填字段', trigger: 'blur'},

                    ],
                    typeNo: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                },
            }
        },
        mounted() {
            this.list();
            this.showProductCategoriesList()
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
                            productTypeModel.update(this.editForm)
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
                            productTypeModel.add(this.editForm)
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
                    name:'',
                    type:'',
                    productCategoriesId:'',
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
                productTypeModel.getById(row.id).then(data=>{
                    this.mode ='edit';
                    this.editForm = data.entity;
                    loading.close()
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
                    productTypeModel.delete(row.id).then(data=>{
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
                    productTypeModel.deleteByIds(ids).then(()=>{
                        this.$store.commit('endLoading')
                        this.list();
                    })

                }).catch(()=>{

                })
            },
            list() {
                this.$store.commit('startLoading')
                productTypeModel.list(this.pageInfo).then(data =>{
                    this.datas=data.entity.content;

                    this.pageInfo.total=data.entity.totalElements

                    this.$store.commit('endLoading')
                }).catch(err=>{
                    console.log("err",err);
                })
            },
            showProductCategoriesList(){
              productCategoriesModel.getProductCategories().then((data)=>{
                  this.arrProductCategories = data.entity
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