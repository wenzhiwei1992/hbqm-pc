<template>
    <div>
        <el-table
                :data="datas"
                @selection-change="handleSelectionChange"
                size="mini"
                stripe
                border
                height="400"
                style="width:100%"
        >
            <el-table-column
                    type="selection"
                    width="80">
            </el-table-column>
            <el-table-column
                    type="index"
                    label="序号"
                    width="50">
            </el-table-column>
            <el-table-column
                    prop="companyName"
                    label="公司名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="factoryIdCode"
                    label="工厂识别码"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="logoUrl"
                    label="logo"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="createdBy"
                    label="创建人"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="modifiedBy"
                    label="修改人"
                    width="120">
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
        <el-row type="flex" justify="space-around" align="middle">
            <el-col >
                <el-button @click="addClick" type="primary">新建</el-button>
                <el-button @click="deletesClicks" type="danger">删除</el-button>
                <el-button @click="editClick" >编辑</el-button>
            </el-col>
            <el-col >
                <el-row type="flex" justify="end" align="middle"  style="margin-top:-15px">
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
        <el-dialog :title="mode=='add'?'新建':'编辑'" v-if="editFormVisible" :visible.sync="editFormVisible" width="50%">
            <company-edit :edit-form="editForm"
                       :mode="mode"
                       @edit-finish="editFinish"
                       @edit-cancel="editCancel"
            ></company-edit>
        </el-dialog>

    </div>
</template>

<script>
    import companyModel from '../../../../model/company/company'
    import clientModel from '../../../../model/client-model'
    import companyEdit   from './CompanyEdit.vue'
    import {Notification} from 'element-ui'
    import moment from "moment"

    export default {
        name: "CompanyManage",
        components:{
            'company-edit':companyEdit
        },
        data(){
            return {
                pageInfo:{
                    currentPage:1,
                    total:0,
                    pageSize:this.GLOBAL.pageSize,
                    pageSizes:this.GLOBAL.pageSizes
                },
                editForm:'',
                editFormVisible:false,
                datas:[],
                mode:'',
                selected:[],
                query:{
                    campanyName:'',
                }
            }
        },
        mounted(){
            this.list()
        },
        methods:{

            list(){
                this.query.pageInfo = this.pageInfo;
                companyModel.companyList(this.query).then(data=>{
                    this.datas = data.entity.content;
                    this.pageInfo.total=data.entity.totalElements
                })
            },
            addClick(){
                this.mode = 'add'
                this.editForm={
                    companyName:'',
                    factoryIdCode:'',
                    logoUrl:'',

                }
                this.editFormVisible=true
            },
            editClick(){
                if(this.selected.length===0){
                    this.$message('请选择编辑项');
                    return;
                }
                let ids=[]
                this.selected.forEach((row)=>{
                    ids.push(row.id)
                })
                if(ids.length>1){
                    this.$message('只能勾选一个进行编辑！');
                    return;
                }
                companyModel.companyLoad(ids[0]).then((data)=>{
                    this.mode = 'edit'
                    this.editForm=data.entity
                    this.editFormVisible=true
                })
            },
            deletesClicks(){
                if(this.selected.length==0){
                    this.$message('请选择删除项');
                    return;
                }
                let ids=[]
                this.selected.forEach((row)=>{
                    ids.push(row.id)
                })
                this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    companyModel.companyDeletes(ids).then((data)=>{
                        if(data.status=='failed'){
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        Notification({
                            message: '删除成功',
                            type: 'success'

                        })
                        this.list()
                        loading.close()
                    })
                }).catch(()=>{})
            },
            formatterDate(row, column, cellValue, index){
                let date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },

            handleSelectionChange(selected){
                this.selected=selected
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize=val
                this.list()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage=val
                this.list()
            },
            editFinish(){
                this.editFormVisible=false
                this.list()
            },
            editCancel(){
                this.editFormVisible=false
                this.list()
            },
        }
    }
</script>

<style scoped>

</style>