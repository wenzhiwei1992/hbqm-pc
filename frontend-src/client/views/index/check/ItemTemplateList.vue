<template>
    <div id="item-template-list">
        <el-table
                :data="entities"
                @selection-change="handleSelectionChange"
                :height="GLOBAL.tabelHeight"
                style="width:100%"
                header-row-class-name="header-row"
                size="mini"
                stripe
                border
                row-key="id"
        >
            <el-table-column
                    type="index"
                    label="序号"
                    width="70">
            </el-table-column>
            <el-table-column
                    prop="templateName"
                    label="模板名称"
                    align="center"
                    width="100"
                    >
            </el-table-column>
            <el-table-column
                    prop="checkType"
                    label="检验类别"
                    align="center"
                    width="100"
                    >
            </el-table-column>
            <el-table-column
                    label="操作"
                    align="center"
                    >
                <template slot-scope="scope">
                    <el-button size="mini" @click="editClick(scope.row)">编辑</el-button>
                    <el-button type="danger" size="mini" @click="deleteClick(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle" style="margin-top:15px">
            <el-col >
                <el-button @click="addClick" type="primary">新建</el-button>
                <!--<el-button @click="deletesClick">删除勾选记录</el-button>-->
            </el-col>
            <el-col >
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
        <el-dialog :title="mode=='add'?'新建':'编辑'" :visible.sync="editFormVisible" width="35%">
            <model-edit :edit-form="editForm"
                       :mode="mode"
                       @model-edit-finish="modelEditFinish"
                       @model-edit-cancel="modelEditCancel"

            ></model-edit>
        </el-dialog>
    </div>


</template>
<script>
    import model from '../../../model/model'
    import ItemTemplateEdit from './ItemTemplateEdit.vue'
    import  moment from  'moment'
    import {Notification} from 'element-ui'
    export default {
        components:{
            'model-edit':ItemTemplateEdit,
        },
        data() {
            return {
                //表查询
                pageInfo:{
                    currentPage:1,
                    total:0,
                    pageSize:this.GLOBAL.pageSize,
                    pageSizes:this.GLOBAL.pageSizes
                },
                entity:{
                    username:null,
                    realName:null
                },
                entities:[],
                //行操作
                //编辑表单
                mode:'',
                editFormVisible:false,
                editForm:null,
            }
        },
        computed:{
        },
        mounted(){
            this.list()

        },
        methods:{
            //表查询
            list(){
                model.list('check/template',this.entity,this.pageInfo).then(data=>{
                    let entity=data.entity
                    let list=data.entity.content;
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
                this.editForm={
                }
                this.editFormVisible=true
            },
            editClick(row){
                model.load('check/template',row.id).then(data=>{
                    this.mode='update'
                    this.editForm=data.entity
                    this.editFormVisible=true
                })
            },
            deleteClick(row){
                this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading')
                    model.delete('check/template',row.id).then(data=>{
                        this.$store.commit('endLoading')
                        this.list()
                    })
                }).catch(()=>{})
            },

        },

    }
</script>
<style>
    .el-table th, .el-table tr.header-row{
        background-color: #f5f7fa;
        font-size:1.1em;
    }
    #entity .el-form-item{
        margin-bottom: 5px;
    }
</style>