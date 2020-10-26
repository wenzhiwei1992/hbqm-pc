<template>
    <div>
            <el-form :inline="true"  :model="productCategoriesQuery" ref="productCategoriesQuery">
                <el-form-item label="构件分类名称" prop="productCategoriesName" >
                    <el-input
                            placeholder="请输入内容"
                            v-model="productCategoriesQuery.productCategoriesName"
                            style="width:150px"
                            clearable>
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button plain  @click="submitForm('productCategoriesQuery')">查找</el-button>
                </el-form-item>
            </el-form>
            <el-table
                    :data="productCategories"
                    @selection-change="handleSelectionChange"
                    size="mini"
                    stripe
                    border
                    height="400"
                    style="width:100%"
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
                        prop="productCategoriesName"
                        label="构件分类名称"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="createdBy"
                        label="创建人"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="modifiedBy"
                        label="修改人"
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
            </el-table>
            <el-row type="flex" justify="space-around" align="middle">
                <el-col >
                    <el-button @click="addClick" type="primary">新建</el-button>
                    <el-button @click="deletesClicks" type="danger">删除</el-button>
                    <el-button @click="editClick" >编辑</el-button>
                </el-col>
                <el-col >
                    <el-row type="flex" justify="end" align="middle"  style="margin-top:-15px">
                        <el-pagination
                                @size-change="handleSizeChange"
                                @current-change="handleCurrentChange"
                                :current-page="productCategoriesQuery.pageInfo.currentPage"
                                :page-sizes="productCategoriesQuery.pageInfo.pageSizes"
                                :page-size="productCategoriesQuery.pageInfo.pageSize"
                                :total="productCategoriesQuery.pageInfo.total"
                                layout="total, sizes, prev, pager, next, jumper"
                                background
                        >
                        </el-pagination>
                    </el-row>
                </el-col>
            </el-row>
           <el-dialog :title="mode=='add'?'新建':'编辑'" v-if="editFormVisible" :visible.sync="editFormVisible">
                <product-categories-edit :edit-form="editForm"
                                     :mode="mode"
                                     @product-categories-finish="productCategoriesFinish"
                                     @product-categories-cancel="productCategoriesCancel"
                ></product-categories-edit>
            </el-dialog>
    </div>
</template>

<script>
    import productCategoriesModel from '../../../../model/basicdata/productCategories'
    import  ProductCategoriesEdit from './ProductCategoriesEdit.vue'
    import {Notification} from 'element-ui'
    import moment from "moment"
    export default {
        components:{
            'product-categories-edit':ProductCategoriesEdit,
        },
        name: "ProductCategoriesList",
        data(){
            return{
                editForm:'',
                editFormVisible:false,
                mode:'',
                productCategories:[],
                productCategoriesQuery:{
                    productCategoriesName:'',
                    pageInfo:{
                        currentPage:1,
                        total:0,
                        pageSize:this.GLOBAL.pageSize,
                        pageSizes:this.GLOBAL.pageSizes
                    },
                }
            }
        },
        mounted(){
            this.showList()
        },
        methods:{
            submitForm(formName){
                this.$refs[formName].validate((valid) => {
                    if(valid){
                        this.showList()
                    }
                    else{
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            showList(){
                productCategoriesModel.productCategoriesList(this.productCategoriesQuery).then((data)=>{
                    this.productCategories =  data.entity[0].content
                    this.productCategoriesQuery.pageInfo.total=data.entity[0].totalElements
                })
            },
            addClick(){
                this.mode = 'add'
                this.editForm={
                    productCategoriesName:'',
                }
                this.editFormVisible=true
            },
            deletesClicks(){
                if(this.selected.length==0){
                    this.$message('请选择删除项');
                    return;
                }
                let ids=[]
                this.selected.forEach((row)=>{
                    ids.push(row.id)
                })
                this.$confirm('此操作将永久删除勾选构件分类, 是否继续?', '提示', {
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
                    productCategoriesModel.productCategoriesDeletes(ids.join(";")).then((data)=>{
                        if(data.status=='fail'){
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        Notification({
                            message: '删除成功',
                            type: 'success'

                        })
                        this.showList()
                        loading.close()
                    })
                }).catch(()=>{})
            },
            editClick(){
                if(this.selected.length==0){
                    this.$message('请选择编辑项');
                    return;
                }
                let ids=[]
                this.selected.forEach((row)=>{
                    ids.push(row.id)
                })
                if(ids.length>1){
                    this.$message('只能勾选一个进行编辑！');
                    return;
                }
                productCategoriesModel.productCategoriesLoad(ids[0]).then((data)=>{
                    this.mode = 'update'
                    this.editForm=data.entity
                    this.editFormVisible=true
                })
            },
            formatterDate(row, column, cellValue, index){
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            handleSelectionChange(selected){
                this.selected=selected
            },
            handleSizeChange(val) {
                this.productCategoriesQuery.pageInfo.pageSize=val
                this.showList()
            },
            handleCurrentChange(val) {
                this.productCategoriesQuery.pageInfo.currentPage=val
                this.showList()
            },
            productCategoriesFinish(){
                this.editFormVisible=false
                this.showList()
            },
            productCategoriesCancel(){
                this.editFormVisible=false
                this.showList()
            },
        },
    }
</script>

<style scoped>

</style>