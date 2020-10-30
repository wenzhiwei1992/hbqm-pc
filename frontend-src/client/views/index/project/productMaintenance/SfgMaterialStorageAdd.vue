<template>
    <div>
        <el-form :inline="true"  :model="semiFinishedGoodsQuery" ref="semiFinishedGoodsQuery">
            <el-form-item label="半成品编码" prop="id">
                <el-input
                        placeholder="请输入内容"
                        v-model="semiFinishedGoodsQuery.id"
                        style="width:150px"
                        clearable>
                </el-input>
            </el-form-item>
            <el-form-item label="半成品名称 " prop="sfgName">
                <el-input
                        placeholder="请输入内容"
                        v-model="semiFinishedGoodsQuery.sfgName"
                        style="width:150px"
                        clearable>
                </el-input>
            </el-form-item>
            <el-form-item>
                <el-button plain  @click="submitForm('semiFinishedGoodsQuery')">查找</el-button>
            </el-form-item>
        </el-form>
        <el-table
                :data="showListSfg"
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
                    prop="sfgNo"
                    label="半成品编码"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="sfgName"
                    label="半成品名称"
            >
            </el-table-column>
            <el-table-column
                    prop="categoryName"
                    label="半成品类别"
            >
            </el-table-column>
            <el-table-column
                    prop="unitCNName"
                    label="单位"
            >
            </el-table-column>
            <!--<el-table-column
                    prop="categoryName"
                    label="原料类型"
                  >
            </el-table-column>-->
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
                                :current-page="semiFinishedGoodsQuery.pageInfo.currentPage"
                                :page-sizes="semiFinishedGoodsQuery.pageInfo.pageSizes"
                                :page-size="semiFinishedGoodsQuery.pageInfo.pageSize"
                                :total="semiFinishedGoodsQuery.pageInfo.total"
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
    import sfgModel from "../../../../model/basicdata/sfg";
    export default {
        data(){
            return{
                ids: [],
                selected:[],
                showListSfg:[],
                arrCategory:'',
                semiFinishedGoodsQuery: {
                    sfgNo:'',
                    sfgName:'',
                    categoryName:'',
                    unitCNName:'',
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
        props:['editFormMaterialStorageAdd','listSfgs'],
        mounted(){
            this.showList()
            this.showCategory()
        },
        methods:{
            showList(){
                sfgModel.findAllByQuery(this.semiFinishedGoodsQuery).then((data) => {
                    /*this.showListSfg = data.entity.list
                    this.semiFinishedGoodsQuery.pageInfo.total = data.entity.totalCount*/
                    let lists = data.entity.list
                    lists = lists.filter(m => {
                        return !this.listSfgs.some(s => s.sfgNo == m.sfgNo)
                    })
                    this.showListSfg = lists
                    //filter过滤后数据的总数量
                    let total = 0;
                    if (data.entity.list != null && lists != null) {
                        total = data.entity.list.length - lists.length
                    }
                    this.semiFinishedGoodsQuery.pageInfo.total = data.entity.totalCount - this.listSfgs.length

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
                   /* this.ids.push(row.id)*/
                    const material = {
                        id:row.id,
                        sfgNo: row.sfgNo,
                        sfgName:row.sfgName,
                        categoryName:row.categoryName,
                        unitCNName:row.unitCNName,
                        num:'',
                    }
                    arr.push(material)//把选中的构件放到数组中
                })
                for(var i=0;i<this.listSfgs.length;i++){
                    for(var j=0;j<arr.length;j++) {
                        if (this.listSfgs[i].id == arr[j].id) {
                            this.$message('该半成品已添加！')
                            return
                        }
                    }
                }
                this.showList()
                this.$emit('headCallBackSfg', arr); //第一个参数是父组件中v-on绑定的自定义回调方法，第二个参数为传递的参数
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
                this.semiFinishedGoodsQuery.pageInfo.pageSize=val
                this.showList()
            },
            handleCurrentChange(val) {
                this.semiFinishedGoodsQuery.pageInfo.currentPage=val
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