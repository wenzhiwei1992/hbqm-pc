<template>
    <div>
        <div>
            <el-form :inline="true" id="projectList" :model="turnoverQuery" ref="turnoverQuery">
            <el-form-item label="原料类型" prop="categoryName" >
                <el-input  v-model="turnoverQuery.categoryName"   @click.native="((type)=>{showSelectTrees(1)})"></el-input>
            </el-form-item>
            <el-form-item label="物料分类Id" prop="categoryId"   style="display:none">
                <el-input  v-model="turnoverQuery.categoryId"></el-input>
            </el-form-item>
                <el-form-item label="原料编码" prop="materialStorageIdS">
                    <el-input
                            placeholder="请输入内容"
                            v-model="turnoverQuery.materialStorageIdS"
                            style="width:150px"
                            clearable>
                    </el-input>
                </el-form-item>
                <el-form-item label="原料名称" prop="materialName">
                    <el-input
                            placeholder="请输入内容"
                            v-model="turnoverQuery.materialName"
                            style="width:150px"
                            clearable>
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button plain type="primary"  @click="submitForm('turnoverQuery')">查找</el-button>
                </el-form-item>
            </el-form>
        </div>
        <el-table
                :data="turnovers"
                @selection-change="handleSelectionChange"
                size="mini"
                stripe
                border
                height="300"
                style="width:100%"
        >
            <el-table-column
                    sortable
                    type="selection"
                    width="80">
            </el-table-column>
            <el-table-column
                   sortable
                    prop="materialId"
                    label="原料编码"
                    width="120">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="materialName"
                    label="原料名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="categoryName"
                    label="原料类型"
                    width="120">
            </el-table-column>
            <el-table-column
                   sortable
                    prop="materialSpecification"
                    label="原料规格"
                    width="120">
            </el-table-column>
            <el-table-column
                   sortable
                    prop="materialUnit"
                    label="单位"
                    width="120">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="checkout"
                    label="是否免检"
                    width="120">
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle">
            <div class="classButton">
                <el-col >
                    <el-button type="success"  @click="addClick">添加</el-button>
                    <el-button type="danger"  @click="deletesClicks">关闭</el-button>
                </el-col>
            </div>
            <el-dialog :visible.sync="showSelectTree" width="340px" append-to-body>
            <tree-add-edit
                    :treeArr="treeArr"
                    :baseCheckedId="baseCheckedId"
                    :baseCheckParentId="baseCheckParentId"
                    @tree-edit-submit="checkoutStatusFinish"
                    @tree-edit-cancel="checkoutStatusCancel"
            ></tree-add-edit>
        </el-dialog>
            <div class="classPage">
                <el-col>
                    <el-row type="flex" justify="end" align="middle"  style="margin-top:-15px">
                        <el-pagination
                                @size-change="handleSizeChange"
                                @current-change="handleCurrentChange"
                                :current-page="turnoverQuery.pageInfo.currentPage"
                                :page-sizes="turnoverQuery.pageInfo.pageSizes"
                                :page-size="turnoverQuery.pageInfo.pageSize"
                                :total="turnoverQuery.pageInfo.total"
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
    import materialStorageModel  from '../../../model/basicdata/materialStorage'
    import categoryModel from '../../../model/basicdata/category'
    import TreeInfo from  '../basic/materialStorage/TreeInfo.vue'
    import {Notification} from 'element-ui'
    export default {
        props:['editFormTurnoverAddVisible','editForm'],
        name: "TurnoverAdd",
        mounted(){
            this.materialList()
            this.showCategoryTree()
        },
        components:{
                 'tree-add-edit':TreeInfo
                },
        methods:{
             checkoutStatusFinish(id,name) {
                this.turnoverQuery.categoryName = name;
                this.turnoverQuery.categoryId = id;
                 this.turnoverQuery.classificationId = id;
               
                this.showSelectTree = false
            },
            checkoutStatusCancel() {
                this.showSelectTree = false
            },
            showSelectTrees(){
                this.baseCheckedId.splice(0,this.baseCheckedId.length);
                this.baseCheckedId.push(this.turnoverQuery.categoryId);

                this.baseCheckParentId.splice(0,this.baseCheckedId.length);
                this.baseCheckParentId.push(this.turnoverQuery.categoryId);
                
                this.showSelectTree = true
            },
            showCategoryTree() {
                   categoryModel.getCategorysToTreeParentDisabled().then((data) => {
                    this.treeArr = data.entity
                })
            },
            /**
             *模糊查询
             */
            submitForm(formName){
                this.$refs[formName].validate((valid) => {
                    if(valid){
                        this.materialList()
                    }
                    else{
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            materialList(){
                var turnoverQuery = {
                    materialStorageIdS:'',
                    materialName:'',
                }
                materialStorageModel.getMaterialStorageLimit(this.turnoverQuery).then((data)=>{
                    let list = data.entity.list
                    list = list.filter(pd => {
                        return !this.editForm.turnovers.some(d => d.materialStorageId == pd.materialId)
                    })
                    this.turnovers = list
                    let  total = 0;
                    if(data.entity.list!=null && list!=null){
                        total = data.entity.list.length - list.length
                    }
                     this.turnoverQuery.pageInfo.total = data.entity.totalCount - total
                })
            },
            addClick(){
                if(this.selected.length==0){
                    this.$message('请选择增加项');
                    return;
                }
                var arr = new Array()
                this.selected.forEach((row)=>{
                    this.ids.push(row.id)
                    const material = {
                        id:row.materialId,
                    }
                    arr.push(material)//把选中的构件放到数组中
                })
                for(var i=0;i<this.editForm.turnovers.length;i++){
                    for(var j=0;j<arr.length;j++){
                       if(this.editForm.turnovers[i].materialStorageId==arr[j].id){
                           this.$message('该原料已添加！')
                         //  materialStorageModel.getListPublicClear()
                           return
                       }
                    }
                }
                this.turnovers=[]
                this.$emit('headCallBack',arr); //第一个参数是父组件中v-on绑定的自定义回调方法，第二个参数为传递的参数
/*                materialStorageModel.getMaterialArrayAdd(arr).then((data)=>{
                    if(data.status=='fail'){
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    }
                    this.turnovers=[]
                    this.$emit('turnover-add-finish')
                })*/
            },
            deletesClicks(){
                this.turnovers=[]
               // materialStorageModel.getListPublicClear()
                this.$emit('turnover-add-cancel')
            },
            handleSelectionChange(selected){
                this.selected = selected
            },
            handleSizeChange(val) {
                this.turnoverQuery.pageInfo.pageSize=val
                this.materialList()
            },
            handleCurrentChange(val) {
                this.turnoverQuery.pageInfo.currentPage=val
                this.materialList()
            },
        },
        data(){
            return{
                selected:[],
                turnovers:[],
                treeArr:[],
                showSelectTree:false,
                  baseCheckedId: [],
                  baseCheckParentId:[],
                ids:[],//存放该楼层已添加的构件
                turnoverQuery:{
                    materialStorageIdS:'',
                    materialName:'',
                    pageInfo:{
                        currentPage:1,
                        total:0,
                        pageSize:this.GLOBAL.pageSize,
                        pageSizes:this.GLOBAL.pageSizes
                    },
                },
                pageInfo:{
                    currentPage:1,
                    total:0,
                    pageSize:this.GLOBAL.pageSize,
                    pageSizes:this.GLOBAL.pageSizes
                },
            }
        }
    }
</script>

<style scoped>
    .classButton{
        display: block;
        margin-top: 6px;
    }
    .classPage{
        display: block;
        margin-top: 18px;
    }
</style>