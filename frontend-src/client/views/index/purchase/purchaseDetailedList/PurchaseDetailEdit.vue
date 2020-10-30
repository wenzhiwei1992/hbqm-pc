<template>
    <div>
        <el-form :inline="true" :model="condition" v-if="condition"  ref="condition">
            <el-form-item label="原料名称" >
                <el-input placeholder="请输入原料名称" v-model="condition.materialName">
                </el-input>
                
            </el-form-item>
            <el-button type="primary" @click="loadMaterialInfoByProjectId()">查 询</el-button>
        </el-form>
        <el-table
                :data="datas"
                @selection-change="handleSelectionChange"
                size="mini"
                stripe
                border
                height="450"
                style="width: 100%"
                :row-key="getRowKeys"
        >
            <el-table-column
                    type="selection"
                    reserve-selection>
            </el-table-column>
            <el-table-column
                    type="index"
                    label="序号">
            </el-table-column>
            <el-table-column
                    prop="materialStorageId"
                    label="原料编码">
            </el-table-column>
            <el-table-column
                    prop="name"
                    label="原料名称">
            </el-table-column>
            <el-table-column
                    prop="specification"
                    label="规格">
            </el-table-column>
            <el-table-column
                    prop="num"
                    label="需求量">
            </el-table-column>
            <el-table-column
                    prop="unit"
                    label="单位">
            </el-table-column>
            <!-- <el-table-column
                    prop="insideNum"
                    label="在库量">
            </el-table-column> -->
        </el-table>
        <div slot="footer" class="dialog-footer">
            <el-row type="flex" justify="space-around" align="middle" >
                <el-col>
                    <el-button type="primary" @click="pushData()">确 定</el-button>
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
    </div>
</template>

<script>
    import materialStorageModel  from '../../../../model/basicdata/materialStorage'
    import serverConfig from '../../../../../server.config'
    import clientModel from '../../../../model/client-model'
    import {Notification} from 'element-ui'
    import moment from "moment";
    export default {
        props:['editForm','mode','projectId'],
        mounted(){
           this.loadMaterialInfoByProjectId();
        },
        methods:{
            loadMaterialInfoByProjectId(){
                this.condition.pageInfo = this.pageInfo;
                clientModel.ax.post('/purchase/projectMaterialList/'+this.projectId,this.condition).then(data=>{
                    this.datas = data.entity.content;
                    this.pageInfo.total = data.entity.totalElements;
                    console.log("this.datas", this.datas)
                })
            },
            editFormCancel(){
                this.$emit('edit-cancel')
            },
            //弹窗的确定按钮
            pushData(){
                if(this.selected.length == 0 ){
                    this.$message.warning(this.GLOBAL.MSG.SELECTED_ERR)
                    return;
                }
                this.editForm.purchaseDetails = [];
                for (let i = 0; i < this.selected.length; i++) {
                    const e = this.selected[i];
                    let purchaseDetail = {}
                    purchaseDetail.materialStorageId = e.materialStorageId;
                    purchaseDetail.rawMaterialName = e.name;
                    purchaseDetail.rawMaterialCode = e.materialStorageId;
                    purchaseDetail.specification =e.specification ;
                    purchaseDetail.companyName = e.unit 
                    purchaseDetail.needNum = e.num
                    purchaseDetail.number = e.num;
                    purchaseDetail.unitPrice = 1;
                    purchaseDetail.totalPrice = 1;
                    purchaseDetail.taxRate = 1;
                    purchaseDetail.deliveryTime = new Date()
                    this.editForm.purchaseDetails.push(purchaseDetail);
                }
                this.$emit('edit-finish',this.editForm,this.mode)
            },
            handleSelectionChange(selected){
                this.selected=selected
            },
            getRowKeys(row) {
                return row.id;
            },
            handleSizeChange(val) {
                this.multipleSelectionAll = [];
                this.pageInfo.pageSize = val;
                this.loadMaterialInfoByProjectId()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val;
                this.loadMaterialInfoByProjectId()
            },

        },
        
        data(){
            var checkPlateCount = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('填写合法数字'));
                }
                var r = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/
                if (!r.test(value)) {
                    callback(new Error('请输入正整数'));
                } else {
                    if(value<0){
                        callback(new Error('必须大于0'));
                    } else {
                        callback();
                    }
                }
            };
            return{
                datas:[],
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize:20,
                    pageSizes:this.GLOBAL.pageSizes
                },
                condition:{

                }
            }
        }

    }
</script>

<style scoped>
    .divType{
        display: block;
        border: 1px lavender solid;
        margin-top: -10px;
        padding-top: 10px;
    }
    .divType2{
        border: 1px lavender solid;
        margin-top: 10px;
        padding-top: 10px;
    }
    .divType3{
        border: 1px lavender solid;
        margin-top: 10px;
        padding-top: 10px;
    }
    .divType4{
        border: 1px lavender solid;
        margin-top: 10px;
        padding-top: 10px;
    }
    .dialog-footer{
      margin-top:10px;
    }
</style>