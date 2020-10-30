<template>
    <div>
        <div>
            <el-form :inline="true" id="projectList" :model="turnoverQuery" ref="turnoverQuery">
                <el-form-item label="原料编码" prop="materialStorageId">
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
                    <el-button plain  @click="submitForm('turnoverQuery')">查找</el-button>
                </el-form-item>
            </el-form>
        </div>
        <el-table
                :data="turnovers"
                @selection-change="handleSelectionChange"
                style="width:100%"
                height="300"
                header-row-class-name="header-row"
                size="mini"
                stripe
                border
                tooltip-effect="light"
        >
            <el-table-column
                    type="selection"
                    width="80">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="materialStorageId"
                    label="原料编码"
                   >
            </el-table-column>
            <el-table-column
                   sortable
                    prop="materialName"
                    label="原料名称"
                   >
            </el-table-column>
            <el-table-column
                    sortable
                    prop="materialSpecification"
                    label="原料规格"
                   >
            </el-table-column>
            <el-table-column
                   ortable
                    prop="materialUnit"
                    label="计量单位"
                 >
            </el-table-column>
            <!-- <el-table-column
                    sortable
                    prop="univalence"
                    label="单价"
                  >
            </el-table-column>-->
            <el-table-column
                   sortable
                    prop="repertory"
                    label="库存量"
              >
            </el-table-column>
            <el-table-column
                    sortable
                    prop="surplus"
                    label="可申请量"

                    >
                <template slot-scope="scope" >
                    <span v-if="scope.row.surplus==0" style="color: #F56C6C">{{scope.row.surplus}}</span>
                    <span  v-else  style="color: #67C23A" >{{scope.row.surplus}}</span>
                </template>
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle">
            <div class="classButton">
                <el-col >
                    <el-button type="success" @click="addClick">添加</el-button>
                    <el-button type="danger"  @click="deletesClicks">关闭</el-button>
                </el-col>
            </div>
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
    import materialStorageModel  from '../../../../model/basicdata/materialStorage'
    import doDetailModel from '../../../../model/materials/doDetail'
    import {Notification} from 'element-ui'
    export default {
        props:['editFormMaterlStorageAddVisible','editForm'],
        data(){
            return{
                selected:[],
                turnovers:[],
                ids:[],//存放该楼层已添加的构件
                turnoverQuery:{
                    materialStorageIdS:'',
                    materialName:'',
                    stockId:this.editForm.stockId,
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
        },
        mounted(){

             this.materialList()
        },
        methods:{
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
                doDetailModel.getMaterialStorageByLimit(this.turnoverQuery).then((data)=>{
                    let list = data.entity.list
                    list = list.filter(pd => {
                        return !this.editForm.listDoDetail.some(d => d.materialStorageId == pd.materialStorageId)
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
                var arr = new Array()//存放要添加的原料编码
                this.selected.forEach((row)=>{
                    this.ids.push(row.id)
                    const material = {
                        id:row.materialStorageId,
                        univalence: row.univalence,
                        inventory:row.repertory,
                        surplus:row.surplus
                    }
                    arr.push(material)//把选中的构件放到数组中
                })
                for(var i=0;i<this.editForm.listDoDetail.length;i++){
                    for(var j=0;j<arr.length;j++){
                        if(this.editForm.listDoDetail[i].materialStorageId==arr[j].id){
                            this.$message('该原料已添加')
                          //  materialStorageModel.getListPublicClearDoDetailVO()//每次打开添加页面清空做假添加需要的list集合
                            return
                        }
                    }
                }
                this.turnovers=[]
                this.$emit('headCallBack',arr); //第一个参数是父组件中v-on绑定的自定义回调方法，第二个参数为传递的参数
/*                materialStorageModel.getMaterialArrayAddBelongToDoDetail(arr).then((data)=>{
                    if(data.status=='fail'){
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    }
                    this.turnovers=[]
                    this.$emit('materl-storage-add-finish')
                })*/
            },
            deletesClicks(){
                this.turnovers=[]
               // materialStorageModel.getListPublicClearDoDetailVO()//每次打开添加页面清空做假添加需要的list集合
                this.$emit('materl-storage-add-cancel')
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