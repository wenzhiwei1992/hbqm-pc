<template>
    <div id="product-detail-list">
        
        <product-detail-criteria list-type="cwbf" @change-criteria="changeCriteria"></product-detail-criteria>
        <el-table
                :data="entities"
                @selection-change="handleSelectionChange"
                style="width:100%"
                header-row-class-name="header-row"
                size="mini"
                stripe
                border
                tooltip-effect="light"
                row-key="id"
        >
            <el-table-column
                    type="index"
                    label="序号">
            </el-table-column>
            <el-table-column
                    prop="project.projectName"
                    label="项目名称"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="floorRelevance.building.buildCode"
                    label="楼栋"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="floorRelevance.floor.floorNum"
                    label="楼层"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="product.productNo"
                    label="构件编码"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="product.productName"
                    label="构件名称"
                    align="center">
            </el-table-column>
            <el-table-column
                    prop="product.productWt"
                    label="构件重量(t)"
                    align="center"
                    :formatter="wtFormat">
            </el-table-column>
            <el-table-column
                    prop="productStatus"
                    label="检验状态"
                    align="center"
            ></el-table-column>
            <el-table-column
                    prop="massLevel"
                    label="质量等级"
                    align="center"
            ></el-table-column>
            <el-table-column
                    prop="orgName"
                    label="基地名称"
                    align="center"
            ></el-table-column>

           <el-table-column
                    label="操作"
                    align="center"
                    width="200"
                    >
                <template slot-scope="scope">
                    <el-button size="medium" type="primary" @click="addBFRecord(scope.row)">报废</el-button>
                </template>
            </el-table-column>
        </el-table>
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

    </div>


</template>
<script>
    import model from '../../../model/model'
    import  moment from  'moment'
    import {Notification} from 'element-ui'
    import serverConfig from '../../../../server.config'
    import productDetailModel from '../../../model/project/productDetail'
    import ProductDetailCriteria from '../stock/ProductDetailCriteria.vue'

    var numeral = require('numeral')
    export default {
        components:{
            'product-detail-criteria':ProductDetailCriteria
        },
        data() {
            return {
                //表查询
                pageInfo:{
                    currentPage:1,
                    total:0,
                    pageSize:10,
                    pageSizes:this.GLOBAL.pageSizes
                },
                entity:{
                    productStatus:'成品入库',
                    deliverDateRange: ['', '']
                },
                entities:[],
            }
        },
        computed:{
        },
        mounted(){
            this.list()
        },
        methods:{
            //后台处理添加一条记录
            addBFRecord(row){
                this.$confirm('确认报废?','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    // row.productStatus=='成品入库' 则 需要更新 bfReason 字段为堆场报废
                    // row.productStatus=='已发货' 则 需要更新 bfReason 字段为场外报废
                    if(row.productStatus=='成品入库'){
                        row.bfReason = '堆场报废'
                    }else if (row.productStatus=='已发货'){
                        row.bfReason = '场外报废'
                    }
                    row.productStatus = '待报废审批'
                    model.update('productDetail',row).then(data=>{
                        this.list()
                    })
                }).catch(()=>{

                })
            },
            changeCriteria(entity){
                this.entity=entity
                this.list()
            },
            //表查询
            list(){
                model.list('productDetail',this.entity,this.pageInfo).then(data=>{
                    this.entities=data.entity.content;
                    this.pageInfo.total=data.entity.totalElements
                })
            },
            query(){
                this.pageInfo.currentPage=1
                this.list()
            },
            handleSelectionChange(selected){
                this.selected=selected
            },
            handleSizeChange(val) {
                this.pageInfo.currentPage=1
                this.pageInfo.pageSize=val
                this.list()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage=val
                this.list()
            },
            momentFormat(row, column, cellValue, index){
                if(cellValue){
                    return moment(cellValue).format('YYYY-MM-DD HH:mm:ss')
                }else{
                    return cellValue
                }

            },
            wtFormat(row, column, cellValue, index){
                if(cellValue){
                    return numeral(cellValue).format('0.000')
                }else{
                    return cellValue
                }

            },


        },
    }
</script>
<style>
    #product-detail-list .red{
        color:red
    }
    #product-detail-list .el-tag{
        cursor:pointer
    }
</style>