<template>
    <div id="check-list">
        <el-form :model="entity" v-if="entity" ref="criteriaForm" :inline="true">
            <el-form-item label="检验结果">
                <el-select v-model="entity.checkResult" clearable 
                        style="width:110px;">
                    <el-option value="合格" label="合格"> </el-option>
                    <el-option value="不合格" label="不合格"> </el-option>
                    <el-option value="隐检返修" label="隐检返修"> </el-option>
                    <el-option value="返修" label="返修"> </el-option>
                    <el-option value="待报废审批" label="待报废审批"> </el-option>
                    <el-option value="报废审批未通过" label="报废审批未通过"> </el-option>
                    <el-option value="报废" label="报废"> </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button icon="el-icon-search" type="primary" @click="list()">查找</el-button>
                <el-button @click="resetForm('criteriaForm')">重置</el-button>
            </el-form-item>
        </el-form>
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
<!--            <el-table-column-->
<!--                    prop="productDetail.detailNo"-->
<!--                    label="构件明细编号"-->
<!--                    align="center"-->
<!--                    width="120">-->
<!--            </el-table-column>-->
            <el-table-column
                    prop="productDetail.product.productNo"
                    label="构件编码"
                    align="center"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="productDetail.product.productName"
                    label="构件名称"
                    align="center"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="checkType"
                    label="检验类型"
                    align="center"
                    width="100"
            >
            </el-table-column>
            <el-table-column
                    prop="checkResult"
                    label="检验结果"
                    align="center"
                    width="100"
                    >
            </el-table-column>
            <el-table-column
                    label="检验明细"
                    align="center"
            >
                <template slot-scope="scope">
                    <el-popover trigger="click" placement="top-end" :visible-arrow="false">
                        <el-table :data="scope.row.items">
                            <el-table-column width="150" property="name" label="检验项"></el-table-column>
                            <el-table-column width="100" property="result" label="检验结果"></el-table-column>
                        </el-table>
                        <div slot="reference" class="name-wrapper">
                            <el-button type="primary" size="mini">查看</el-button>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column
                    label="检验图片"
                    align="center"
            >
                <template slot-scope="scope">
                    <el-button size="mini" v-if="scope.row.checkImages.length>0" @click="showCheckImages(scope.row)">查看检验图片</el-button>
                </template>
            </el-table-column>
            <el-table-column
                    prop="checkDesc"
                    label="检验说明"
                    align="center"
                    :show-overflow-tooltip="true"
            >
            </el-table-column>
            <el-table-column
                    prop="checkPersonName"
                    label="检验员"
                    align="center"
            >
            </el-table-column>
            <el-table-column
                    prop="checkTime"
                    label="检验日期"
                    align="center"
                    :formatter="momentFormat"
            >
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
        <el-dialog title="检验图片" :visible.sync="checkImagesVisible" width="800px" top="10px" v-if="checkSelected!=null">
            <div v-for="checkImage in checkSelected.checkImages" :key="checkImage.id">
                <h3>{{checkImage.id}}</h3>
                <img :src="checkImage.src" :alt="checkImage.id"  style="max-width:100%;">
            </div>
        </el-dialog>
    </div>


</template>
<script>
    import model from '../../../model/model'
    import  moment from  'moment'
    import {Notification} from 'element-ui'
    import serverConfig from '../../../../server.config'
    export default {
        components:{
        },
        data() {
            return {
                //表查询
                pageInfo:{
                    currentPage:1,
                    total:0,
                    pageSize: 10,
                    pageSizes:this.GLOBAL.pageSizes
                },
                entity:{
                },
                entities:[],
                //行操作
                selected:[],
                //编辑表单
                mode:'',
                editFormVisible:false,
                productDetail:null,
                checkImagesVisible:false,
                checkSelected:null
            }
        },
        computed:{
        },
        mounted(){
            this.list()

        },
        methods:{
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            //表查询
            list(){
                model.list('check',this.entity,this.pageInfo).then(data=>{
                    let entity=data.entity
                    let list=data.entity.content;
                    list.forEach((e)=>{
                        e.items=JSON.parse(e.itemResultJson)
                        let imgs=[]
                        if(e.checkImages){
                            e.checkImages.split(";").forEach((fileName)=>{
                                imgs.push({
                                    id:fileName,
                                    src:serverConfig.baseURL+'/file?tableName=ck_check&id='+e.id+"&fileName="+fileName
                                });
                            })
                        }
                        e.checkImages=imgs;
                    })
                    this.entities=list
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
            //编辑表单
            modelEditFinish(){
                this.editFormVisible=false
                this.list()
            },
            modelEditCancel(){
                this.editFormVisible=false
            },
            //行操作
            addClick(){
                this.mode='add'
                this.id=''
                this.editFormVisible=true
            },
            editClick(row){
                this.mode='update'
                this.id=row.id
                this.editFormVisible=true
            },
            check(row){
                this.productDetail=row
                this.editFormVisible=true
            },
            deleteClick(row){
                this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading')
                    model.delete('check',row.id).then(data=>{
                        this.$store.commit('endLoading')
                        this.list()
                    })
                }).catch(()=>{})
            },
            deletesClick(){
                if(this.selected.length==0){
                    this.$message('请选择删除项');
                    return;
                }
                let ids=[]
                this.selected.forEach((row)=>{
                    ids.push(row.id)
                })
                this.$confirm('此操作将永久删除勾选记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading')
                    model.deletes('check',ids.join(";")).then(()=>{
                        this.$store.commit('endLoading')
                        this.list()
                    })
                }).catch(()=>{})

            },
            showCheckImages(row){
                this.checkImagesVisible=true;
                this.checkSelected=row;
            }

        },
        watch:{
            checkImagesVisible:function(n,o){
                if(!n){
                    this.checkSelected=null;
                }
            }
        }
    }
</script>
<style>
    #check-list .red{
        color:red
    }
    #check-list .el-tag{
        cursor:pointer
    }
</style>