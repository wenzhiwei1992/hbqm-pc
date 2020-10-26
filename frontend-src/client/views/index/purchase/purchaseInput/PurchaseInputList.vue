<template>

    <div>
        <el-table
                :data="datas"
                @selection-change="handleSelectionChange"
                size="mini"
                stripe
                border
                height="450"
                style="width: 100%"
                @expand-change="showSubData"
                :expand-row-keys="expands"
                row-key="id"
        >
            <el-table-column type="expand">
                <template slot-scope="props">
                    <el-table
                            :data="subDatas"
                            size="mini"
                            stripe
                            border
                            style="width: 100%"
                    >
                        <el-table-column
                                prop="purchase.purchaseNo"
                                label="采购单号"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="rawMaterialCode"
                                label="原料编码"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="rawMaterialName"
                                label="原料名称"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="specification"
                                label="规格"
                        >
                         </el-table-column>
                         <el-table-column
                                prop="companyName"
                                label="单位"
                        >
                        </el-table-column>
                         <el-table-column
                                prop="unitPrice"
                                label="单价"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="number"
                                label="采购总量"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="inputNum"
                                label="本次入库数量"
                        >
                         </el-table-column>
                        <el-table-column
                                prop="sum"
                                label="总价"
                        >
                        </el-table-column>
                    </el-table>
                </template>
            </el-table-column>
            <el-table-column
                    type="selection"
                    width="50">
            </el-table-column>
            <el-table-column
                    type="index"
                    label="序号"
                    width="50">
            </el-table-column>
            <el-table-column
                    prop="inputNo"
                    label="入库单号"
                    width="160">
            </el-table-column>
            <el-table-column
                    prop="inputDate"
                    label="入库时间">
            </el-table-column>
            <el-table-column
                    prop="inputSalesman"
                    label="业务员">
            </el-table-column>
            <el-table-column
                    prop="totalInputNum"
                    label="入库总数">
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
        <el-row type="flex" justify="space-around" align="middle" >
            <el-col style="margin-top:15px">
                <el-button @click="addClick" type="primary">新建</el-button>
                <el-button @click="deletesClick" type="danger"> 删除勾选</el-button>
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

    </div>
</template>
<script>
    import purchaseModel from '../../../../model/purchase/purchase'
    import clientModel from '../../../../model/client-model'
    import {Notification} from 'element-ui'
    import  moment from  'moment'
    export default {
        data() {
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
                query:{

                },
                subDatas:[],
                expands:[],
            }
        },
        mounted() {
            this.list();
        },
        methods: {
            showSubData(row, expandedRows) {
                let id = row.id;
                if(this.expands.some((e)=>e==row.id)){
                    this.expands=[]
                }else{
                    this.expands=[]
                    this.expands.push(row.id)
                }
                purchaseModel.getInputDetailById(id).then(data => {
                    this.subDatas = data.entity;
                })
            },
            formatterDate(row, column, cellValue, index){
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            addClick() {
                this.$router.push({ name: 'purchaseInputAdd',mode : 'add'})
            },
            deletesClick() {
                if(this.selected.length ==0){
                    this.$message('请选择删除项');
                    return;
                }
                let ids=[];
                this.selected.forEach((row)=>{
                    ids.push(row.id)
                });
                this.$confirm('确认删除选择项?','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(()=>{
                    this.$store.commit('startLoading');
                    purchaseModel.deleteInputByIds(ids).then((data)=>{
                        if(data.status == 'success'){
                            Notification({
                                type:"success",
                                message:'删除成功'
                            })
                        }else{
                            Notification({
                                type:"error",
                                message:data.msg
                            })
                        }
                        this.$store.commit('endLoading');
                        this.list();
                    })

                }).catch(()=>{

                })
            },
            list() {
                this.$store.commit('startLoading');
                this.query.pageInfo = this.pageInfo;
                purchaseModel.listAll('purchaseInput',this.query).then(data =>{
                    this.datas=data.entity.content;
                    this.pageInfo.total=data.entity.totalElements;
                    this.$store.commit('endLoading')
                }).catch(err=>{
                    console.error(err)
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
            },
            
        },

    }
</script>
<style>

</style>