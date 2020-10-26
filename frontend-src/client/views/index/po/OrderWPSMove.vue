<template>
    
    <div>
        <el-form :inline="true" :model="condition"  size="mini">
            <el-row>
                <el-form-item label="原始生产计划" required>
                    <el-select
                        v-model="condition.orderId"
                        filterable
                        remote
                        reserve-keyword
                        placeholder="输入生产计划加载数据"
                        @change="loadDataForMove"
                        :remote-method="remoteMethod"
                        :loading="loading">
                        <el-option
                            v-for="item in orderDatas"
                            :key="item.id"
                            :label="item.orderNo"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="原始开始日期" >
                    <el-input disabled readonly v-model="condition.planDateFrom"></el-input>
                </el-form-item>
                <el-form-item label="原始结束日期" >
                    <el-input disabled readonly v-model="condition.planDateTo"></el-input>
                </el-form-item>
            </el-row>
            <el-row>            
                <el-form-item label="目标生产计划" required>
                    <el-select
                        v-model="condition.targetOrderId"
                        filterable
                        remote
                        reserve-keyword
                        @change="loadDateInfo"
                        placeholder="输入生产计划加载数据"
                        :remote-method="remoteMethod"
                        :loading="loading">
                        <el-option
                            v-for="item in orderDatas"
                            :key="item.id"
                            :label="item.orderNo"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="目标开始日期" >
                    <el-input disabled readonly v-model="condition.targetPlanDateFrom"></el-input>
                </el-form-item>
                <el-form-item label="目标结束日期" >
                    <el-input disabled readonly v-model="condition.targetPlanDateTo"></el-input>
                </el-form-item>
            </el-row>
        </el-form>
        
        <el-table
                :data="datas"
                @selection-change="handleSelectionChange"
                height="400"
                :row-key="getRowKeys"
                stripe
                border>
            <el-table-column
                    type="selection"
                    :reserve-selection="true">
            </el-table-column>
            <el-table-column
                    type="index"
                    label="序号">
            </el-table-column>
            <el-table-column
                    prop="project.projectName"
                    label="项目名称">
            </el-table-column>
            <el-table-column
                    prop="product.productNo"
                    label="构件编码">
            </el-table-column>
            <el-table-column
                    prop="product.productName"
                    label="构件名称">
            </el-table-column>
            <el-table-column
                    prop="floorRelevance.building.buildCode"
                    label="楼栋">
            </el-table-column>
            <el-table-column
                    prop="floorRelevance.floor.floorNum"
                    label="楼层">
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle" >
            <el-col style="margin-top:3%"> </el-col>
            <el-col style="margin-top:15px">
                <el-row type="flex" justify="end" align="middle">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="condition.pageInfo.currentPage"
                            :page-sizes="condition.pageInfo.pageSizes"
                            :page-size="condition.pageInfo.pageSize"
                            :total="condition.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer" align="center">
            <el-button @click="cancel">取 消</el-button>
            <el-button type="primary" @click="submit()">确 定</el-button>
        </div>
    </div>
</template>
<script>

import order from '../../../model/po/orderMove'
import moment from "moment";
import {Notification} from 'element-ui'
export default {
    data(){
        return{
            datas:[], //构件集合 
            selected:[], //选择项集合
            loading:false,
            condition:{ //查询条件
                orderId :'',
                targetOrderId:'',
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: 20 ,
                    pageSizes:this.GLOBAL.pageSizes
                },
            }, 
            orderDatas:[],
        }
    },
    methods:{
        loadDateInfo(){
            order.loadOrderPlanDateById(this.condition.targetOrderId).then(data=>{
                const order = data.entity;
                this.condition.targetPlanDateTo = moment(order.planDateTo).format("YYYY-MM-DD");
                this.condition.targetPlanDateFrom  = moment(order.planDateFrom).format("YYYY-MM-DD");
            })
        },
        loadDataForMove(){
            /**
             * 加载日期信息，返回是 order 数据对象
             */
            order.loadOrderPlanDateById(this.condition.orderId).then(data=>{
                const order = data.entity;
                this.condition.planDateTo = moment(order.planDateTo).format("YYYY-MM-DD");
                this.condition.planDateFrom  = moment(order.planDateFrom).format("YYYY-MM-DD");

            })
            order.loadDataForMove(this.condition).then(data=>{
                this.datas =  data.entity.content;
                this.condition.pageInfo.total = data.entity.totalElements;
            }).catch(err=>{
                console.error("err",err)
            });
        },
        remoteMethod(query) {
            if (query !== '') {
                this.loading = true;
                //根据query的值去远程检索数据
                order.searchRemoteByQuery(query).then(data=>{
                    this.orderDatas = data.entity;
                }).catch(err=>{
                    console.error("err",err);
                })
                setTimeout(() => {
                    this.loading = false;
                }, 200);
                
            } else {
                this.orderDatas = [];
            }
        },
        cancel(){
            this.$router.push({
                name:'orderManageNoProject'
            })
        },
        submit(){

            if(!this.condition.orderId){
                this.$message.warning("请选择原始生产计划")
                return;
            }
            if(!this.condition.targetOrderId){
                this.$message.warning("请选择目标生产计划")
                return;
            }
            if(this.selected.length == 0 ){
                this.$message.warning(this.GLOBAL.MSG.SELECTED_ERR)
                return;
            }
            this.condition.selected = [];
            for (let i = 0; i < this.selected.length; i++) {
                const e = this.selected[i];
                this.condition.selected.push(e.id);
            }
            order.moveSelectDataToTarget(this.condition).then(data=>{
                if (data.status === 'success') {
                        Notification({
                            message: this.GLOBAL.MSG.UPDATE_SUCCESS,
                            type: 'success'
                        });
                        this.$router.push({
                            name:'orderManageNoProject'
                        });
                    } else {
                        Notification({
                            message: this.GLOBAL.MSG.UPDATE_ERR,
                            type: 'success'
                        })
                    }
            }).catch(err=>{
                console.error(err);
            })


        },
        handleSelectionChange(selected){
            this.selected = selected
        },
        handleSizeChange(val) {
            this.condition.pageInfo.pageSize = val;
            this.loadDataForMove()
        },
        handleCurrentChange(val) {
            this.condition.pageInfo.currentPage = val;
            this.loadDataForMove()
        }, 
        getRowKeys(row) {
            return row.id;
        },
    }
}
</script>