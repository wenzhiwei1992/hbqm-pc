<template>
    <div>
        <el-form :inline="true"  :model="materialStorageQuery" ref="materialStorageQuery">
            <el-form-item label="原料编码" prop="materialStorageId">
                <el-input
                        placeholder="请输入内容"
                        v-model="materialStorageQuery.materialStorageIdS"
                        style="width:150px"
                        clearable>
                </el-input>
            </el-form-item>
            <el-form-item label="原料名称" prop="materialName">
                <el-input
                        placeholder="请输入内容"
                        v-model="materialStorageQuery.materialName"
                        style="width:150px"
                        clearable>
                </el-input>
            </el-form-item>
            <el-form-item label="物料类别" prop="categoryId">
                <el-select clearable v-model="materialStorageQuery.categoryId" placeholder="请选择" style="width:150px">
                    <el-option
                            v-for="item in arrCategory"
                            :key="item.id"
                            :label="item.categoryName"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button plain  @click="submitForm('materialStorageQuery')">查找</el-button>
            </el-form-item>
        </el-form>
        <el-table
                :data="materialStaroges"
                @row-click="clickRow"
                ref="moviesTable"
                @selection-change="handleSelectionChange"
                size="mini"
                stripe
                border
                height="300"
                style="width:100%"
        >
            <el-table-column
                    type="selection"
                    width="80">
            </el-table-column>
            <el-table-column
                    prop="materialStorageId"
                    label="原料编码"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="materialName"
                    label="原料名称"
                   >
            </el-table-column>
            <el-table-column
                    prop="materialSpecification"
                    label="原料规格"
                    >
            </el-table-column>
            <el-table-column
                    prop="materialUnit"
                    label="单位"
                    >
            </el-table-column>
            <el-table-column
                    prop="categoryName"
                    label="原料类型"
                  >
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle">
            <div class="classButton">
                <el-col >
                    <el-button type="success" v-on:click="addClick">添加</el-button>
                    <el-button type="danger"  @click="deleteClick">关闭</el-button>
                </el-col>
            </div>
            <div class="classPage">
                <el-col>
                    <el-row type="flex" justify="end" align="middle"  style="margin-top:-15px">
                        <el-pagination
                                @size-change="handleSizeChange"
                                @current-change="handleCurrentChange"
                                :current-page="materialStorageQuery.pageInfo.currentPage"
                                :page-sizes="materialStorageQuery.pageInfo.pageSizes"
                                :page-size="materialStorageQuery.pageInfo.pageSize"
                                :total="materialStorageQuery.pageInfo.total"
                                layout="total, sizes, prev, pager, next, jumper"
                                background
                        >
                        </el-pagination>
                    </el-row>
                </el-col>
            </div>
        </el-row>
    </div>
</template>

<script>
    import materialStorageModel  from '../../../../model/basicdata/materialStorage'
    import categoryModel from '../../../../model/basicdata/category'
    import {Notification} from 'element-ui'
    import moment from "moment"
    export default {
        data(){
            return{
                selected:[],
                materialStaroges:[],
                arrCategory:'',
                materialStorageQuery: {
                    materialStorageIdS: '',
                    materialName: '',
                    materialSpecification: '',
                    categoryId:'',
                    pageInfo:{
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
            }
        },
        name: "MaterialStorageAdd",
        props:['editFormMaterialStorageAdd','listMaterials'],
        mounted(){
            this.showList()
            this.showCategory()
        },
        methods:{
            showList(){
                materialStorageModel.getProductMaterialStorages(this.materialStorageQuery).then((data)=>{
                    this.materialStaroges = data.entity.list
                    this.materialStorageQuery.pageInfo.total = data.entity.totalCount
                })
            },
            submitForm(formName){
                this.$refs[formName].validate((valid) => {
                    if(valid){
                        console.log('submitForm');//TO DELETE
                       this.showList()
                    }
                    else{
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            addClick(){
                if(this.selected.length==0){
                    this.$message('请选择增加项');
                    return;
                }
                var arr = new Array()//存放要添加的原料编码
                this.selected.forEach((row)=>{
                    const material = {
                        materialStorageId:row.materialStorageId,
                        name:row.materialName,
                        specification:row.materialSpecification,
                        unit:row.materialUnit,
                        productId:'',
                        productNo:'',
                        num:'',
                    }
                    arr.push(material)//把选中的构件放到数组中
                })
                for(var i=0;i<this.listMaterials.length;i++){
                    for(var j=0;j<arr.length;j++) {
                        if (this.listMaterials[i].materialStorageId == arr[j].materialStorageId) {
                            this.$message('该原料已添加！')
                            return
                        }
                    }
                }
                this.showList()
                this.$emit('headCallBack', arr); //第一个参数是父组件中v-on绑定的自定义回调方法，第二个参数为传递的参数
            },
            deleteClick(){
                this.showList()
                this.$emit('material-storage-cancel')
            },
            showCategory(){
                categoryModel.getCategorysByOrgId().then((data)=>{
                    this.arrCategory = data.entity
                })
            },
            handleSelectionChange(selected){
                this.selected = selected
            },
            handleSizeChange(val) {
                this.materialStorageQuery.pageInfo.pageSize=val
                this.showList()
            },
            handleCurrentChange(val) {
                this.materialStorageQuery.pageInfo.currentPage=val
                this.showList()
            },
            //点击整行都可以进行勾选
            clickRow(row){
                this.$refs.moviesTable.toggleRowSelection(row)
            }  
        },


    }
</script>

<style scoped>
    .classPage{
        display: block;
        margin-top: 18px;
    }
</style>