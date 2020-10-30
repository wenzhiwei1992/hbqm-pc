<template>
    <div>
        <div class="demo-input-size">
            <el-form :inline="true" id="projectList" :model="categoryQuery" ref="categoryQuery">
                <el-form-item label="物料分类名称" prop="materialName">
                    <el-select v-model="categoryQuery.classificationId" placeholder="请选择" style="width:150px" clearable>
                        <el-option
                                v-for="item in arrClassification"
                                :key="item.id"
                                :label="item.classificationName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="物料类别名称" prop="categoryName">
                    <el-input
                            placeholder="请输入内容"
                            v-model="categoryQuery.categoryName"
                            style="width:150px"
                            clearable>
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button plain  @click="submitForm('categoryQuery')">查找</el-button>
                </el-form-item>
            </el-form>
            <el-table
                    :data="categorys"
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
                        prop="categoryName"
                        label="物料类别名称"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="categoryNo"
                        label="编号"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="classificationName"
                        label="物料分类名称"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="createdBy"
                        label="创建人"
                        width="120">
                </el-table-column>
                <el-table-column
                        v-if="show"
                        prop="modifiedBy"
                        label="修改人"
                        width="120">
                </el-table-column>
                <el-table-column
                        v-if="show"
                        prop="dateCreated"
                        :formatter="formatterDate"
                        label="创建时间"
                        width="140">
                </el-table-column>
                <el-table-column
                        v-if="show"
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
                                :current-page="categoryQuery.pageInfo.currentPage"
                                :page-sizes="categoryQuery.pageInfo.pageSizes"
                                :page-size="categoryQuery.pageInfo.pageSize"
                                :total="categoryQuery.pageInfo.total"
                                layout="total, sizes, prev, pager, next, jumper"
                                background
                        >
                        </el-pagination>
                    </el-row>
                </el-col>
            </el-row>
            <el-dialog :title="mode=='add'?'新建':'编辑'" v-if="editFormVisible" :visible.sync="editFormVisible" width="30%">
                <category-edit :edit-form="editForm"
                               :mode="mode"
                               :arrClassification="arrClassification"
                               @category-edit-finish="categoryEditFinish"
                               @category-edit-cancel="categoryEditCancel"
                ></category-edit>
            </el-dialog>
        </div>
    </div>
</template>

<script>
    import classificationModel from '../../../../model/basicdata/classification'
    import categoryModel from '../../../../model/basicdata/category'
    import CategoryEdit   from './CategoryEdit.vue'
    import {Notification} from 'element-ui'
    import moment from "moment"
    export default {
        components:{
            'category-edit':CategoryEdit,
        },
        name: "CategoryList",
        mounted(){
            this.categoryList()
            this.showClassification()
        },
        methods:{
            submitForm(formName){
                this.$refs[formName].validate((valid) => {
                    if(valid){
                        this.categoryList()
                    }
                    else{
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            categoryList(){
                categoryModel.getCategories(this.categoryQuery).then((data)=>{
                    this.categorys =  data.entity.list
                    this.categoryQuery.pageInfo.total=data.entity.totalCount
                })
            },
            addClick(){
                this.mode = 'add'
                this.editForm={
                    classificationId:'',
                    categoryName:'',
                    categoryNo:'',
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
                this.$confirm('此操作将永久删除勾选物料类别, 是否继续?', '提示', {
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
                    categoryModel.categoryDeletes(ids.join(";")).then((data)=>{
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
                        this.categoryList()
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
                categoryModel.categoryLoad(ids[0]).then((data)=>{
                    this.mode = 'update'
                    this.editForm=data.entity
                    this.editFormVisible=true
                })
            },
            showClassification(){
                classificationModel.getClassifications().then((data)=>{
                    this.arrClassification = data.entity
                })
            },
            handleSelectionChange(selected){
                this.selected=selected
            },
            handleSizeChange(val) {
                this.categoryQuery.pageInfo.pageSize=val
                this.categoryList()
            },
            handleCurrentChange(val) {
                this.categoryQuery.pageInfo.currentPage=val
                this.categoryList()
            },
            formatterDate(row, column, cellValue, index){
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            //编辑表单
            categoryEditFinish(){
                this.editFormVisible=false
                this.categoryList()
            },
            categoryEditCancel(){
                this.editFormVisible=false
                this.categoryList()
            },
        },
        data(){
            return{
                show:'',
                arrClassification:'',
                editForm:'',
                editFormVisible:false,
                mode:'',
                categorys:[],
                categoryQuery:{
                    classificationId:'',
                    categoryName:'',
                    pageInfo:{
                        currentPage:1,
                        total:0,
                        pageSize:this.GLOBAL.pageSize,
                        pageSizes:this.GLOBAL.pageSizes
                    },
                },
                selected:'',
            }
        }
    }
</script>

<style scoped>

</style>