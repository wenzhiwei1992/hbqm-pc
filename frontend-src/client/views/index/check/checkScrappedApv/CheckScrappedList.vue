<template>
    <div>
        <div class="demo-input-size">
            <el-form :inline="true" id="projectList" :model="scrapedLimit" ref="scrapedLimit">
                <el-form-item label="构件号" prop="productNo">
                    <el-input
                            placeholder="请输入内容"
                            v-model="scrapedLimit.productNo"
                            style="width:150px"
                            clearable>
                    </el-input>
                </el-form-item>
                <el-form-item label="构件名称" prop="productName">
                    <el-input
                            placeholder="请输入内容"
                            v-model="scrapedLimit.productName"
                            style="width:150px"
                            clearable>
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button plain  @click="submitForm('scrapedLimit')">查找</el-button>
                    <el-button plain  @click="resetForm('scrapedLimit')">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
        <el-table
                :data="scrapeds"
                size="mini"
                stripe
                border
                @selection-change="handleSelectionChange"
                height="500"
                router-link
        >
            <el-table-column
                    type="selection"
                    width="55">
            </el-table-column>
            <el-table-column
                    type="index"
                    label="序号"
                    width="50">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="id"
                    label="报废单号"
                    width="150">
            </el-table-column>
            <el-table-column
                    prop="productNo"
                    label="构件编码"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="productName"
                    label="构件名称"
                    width="120"
            >
            </el-table-column>
            <el-table-column
                    prop="specification"
                    label="规格"
                    width="120"
            >
            </el-table-column>
            <el-table-column
                    prop="backlogName"
                    label="工序"
                    width="120"
            >
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="300">
                <template slot-scope="scope">
                    <div v-if="scope.row.isCommit!='已提交'">
                        <el-button size="mini" @click="editClick(scope.row)">编辑</el-button>
                        <el-button type="danger" size="mini" @click="deleteClick(scope.row)">删除</el-button>
                        <el-button type="primary"   size="mini" @click="commit(scope.row)">提交</el-button>
                    </div>
                    <div v-else="scope.row.isCommit == '已提交' ">
                        <el-button type="primary"    size="mini">已提交</el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle">
            <el-col >
                <el-button type="primary" @click="addClick">新建</el-button>
                <el-button @click="deletesClicks">删除</el-button>
            </el-col>
            <el-col >
                <el-row type="flex" justify="end" align="middle"  style="margin-top:-15px">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="scrapedLimit.pageInfo.currentPage"
                            :page-sizes="scrapedLimit.pageInfo.pageSizes"
                            :page-size="scrapedLimit.pageInfo.pageSize"
                            :total="scrapedLimit.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-col>
        </el-row>
        <el-dialog :title="mode=='add'?'新建':'编辑'" :visible.sync="editFormVisible">
            <check-scrapped-edit :edit-form="editForm"
                        :mode="mode"
                        @scraped-edit-finish="scrapedEditFinish"
                        @scraped-edit-cancel="scrapedEditCancel"
            ></check-scrapped-edit>
        </el-dialog>
    </div>

</template>

<script>
    import scrapedModel  from '../../../../model/scrapped/scrapped'
    import scrapedApvModel  from '../../../../model/scrapped/scrappedApv'
    import CheckScrappedEdit from './CheckScrappedEdit.vue'
    import {Notification} from 'element-ui'
    export default {
        components:{
            'check-scrapped-edit':CheckScrappedEdit,
        },
        mounted(){
           this. scrapedList()
        },
        methods:{
            scrapedList(){
                scrapedModel.scrappedLimit(this.scrapedLimit).then((data)=>{
                    this.scrapeds = data.entity.list
                    this.scrapedLimit.pageInfo.total = data.entity.totalCount
                })
            },
            commit(row){
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                var scrapedApv = {
                    apvType:'',
                    result:'',
                    name:'',
                    time:'',
                    remark:'',
                    ckScrappedId:row.id
                }
                var commit={
                    id:row.id,
                    isCommit :'已提交'
                }
                scrapedModel.scrappedUpdateIsCommit(commit).then((data)=>{
                    if(data.status=='fail'){
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        loading.close()
                        return
                    }
                    scrapedApvModel.scrappedApvAdd(scrapedApv).then((data)=>{
                        if(data.status=='fail'){
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        Notification({
                            message: '提交成功',
                            type: 'success'
                        })
                        this.scrapedList()
                        loading.close()
                    })
                })
               // alert(111)

            },
            addClick(){
                this.mode = 'add'
                this.editForm={
                    product:{
                        productNo:''
                    },
                  //  productNo:'',
                    productName:'',
                    scrapCauseName:'',
                    specification:'',
                    backlogName:'',
                    backlogId:'',
                    productDetailId:'',
                }
                this.editFormVisible=true
            },
            deleteClick(row){
                this.$confirm('此操作将永久删除该报废单, 是否继续?', '提示', {
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
                    scrapedModel.scrappedDelete(row.id).then(data=>{
                        if(data.status=='fail'){
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            return
                        }
                        Notification({
                            message: '删除成功',
                            type: 'success'

                        })
                        this.scrapedList()
                    })
                }).catch(()=>{})
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
                this.$confirm('此操作将永久删除勾选报废单, 是否继续?', '提示', {
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
                    scrapedModel.scrappedDeletes(ids.join(";")).then((data)=>{
                        if(data.status=='fail'){
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
                        loading.close()
                        this.scrapedList()
                    })
                }).catch(()=>{})
            },
            editClick(row){
                this.$store.commit('startLoading')
                scrapedModel.getScrappedByScrapedId(row.id).then((data)=>{
                    this.mode = 'update'
                    this.editForm=data.entity
                    this.$store.commit('endLoading')
                    this.editFormVisible=true
                })
            },
            handleSelectionChange(selected){
                this.selected=selected
            },
            //编辑表单
            scrapedEditFinish(){
                this.editFormVisible=false
                this.scrapedList()
            },
            scrapedEditCancel(){
                this.editFormVisible=false
            },
            handleSizeChange(val) {
                this.scrapedLimit.pageInfo.pageSize=val
                scrapedModel.scrappedLimit(this.scrapedLimit).then((data)=>{
                    this.scrapeds = data.entity.list
                    this.scrapedLimit.pageInfo.total = data.entity.totalCount
                })
            },
            handleCurrentChange(val) {
                this.scrapedLimit.pageInfo.currentPage=val
                scrapedModel.scrappedLimit(this.scrapedLimit).then((data)=>{
                    this.scrapeds = data.entity.list
                    this.scrapedLimit.pageInfo.total = data.entity.totalCount
                })
            },
            submitForm(formName){
                this.$refs[formName].validate((valid) => {
                    if(valid){
                        this.scrapedList()
                    }
                    else{
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
        },
        data(){
            return{
                IsShow:true,
                productNo:'',
                productName:'',
                scrapedLimit:{
                    productNo:'',
                    productName:'',
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
                mold:'',
                scrapeds:[],
                selected:[],
                //编辑表单
                mode:'',
                editFormTitle:'',
                editFormVisible:false,
                editForm:null,
            }
        },
    }
</script>

<style scoped>

</style>