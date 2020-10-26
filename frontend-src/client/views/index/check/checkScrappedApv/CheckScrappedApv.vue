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
                    sortable
                    prop="id"
                    label="ID"
                    width="150">
            </el-table-column>
<!--            <el-table-column
                    prop="productStatus"
                    label="状态"
                    width="120">
            </el-table-column>-->
            <el-table-column
                    prop="productNo"
                    label="构件号"
                    width="120">
            </el-table-column>>
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
                    prop="name"
                    label="审核人"
                    width="120"
            >
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="300">
                <template slot-scope="scope">
                    <div v-if="scope.row.result==''">
                            <el-button size="mini" @click="editClick(scope.row)">审核通过</el-button>
                            <el-button type="danger" size="mini" @click="deleteClick(scope.row)">审核驳回</el-button>
                    </div>
                    <div v-else-if="scope.row.result=='审批通过'">
                            <el-button type="primary">审核已通过</el-button>
                    </div>
                    <div v-else="scope.row.result=='审批驳回'">
                        <el-button type="danger">审核已驳回</el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle">
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
    </div>
</template>

<script>
    import scrappedApvModel  from '../../../../model/scrapped/scrappedApv'
    import {Notification} from 'element-ui'
    export default {
        mounted(){
            this.scrapApvList()
        },
        methods:{
            scrapApvList(){
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                scrappedApvModel.scrappedApvLimit(this.scrapedLimit).then((data)=>{
                    this.scrapeds = data.entity.list
                    this.scrapedLimit.pageInfo.total = data.entity.totalCount
                    loading.close()
                })
            },
            submitForm(formName){
                this.$refs[formName].validate((valid) => {
                    if(valid){
                        this.scrapApvList()
                    }
                    else{
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            editClick(row){
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                var nowDate = new Date();
                var scrapedApv = {
                    id:row.id,
                    apvType:'领导审批',
                    result:'审批通过',
                    name:this.$store.state.user.username,
                    time:nowDate
                }
                scrappedApvModel.scrappedApvUpdate(scrapedApv).then((data)=>{
                    if(data.status=='fail'){
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        loading.close()
                        return
                    }
                    Notification({
                        message: '审批通过',
                        type: 'success'
                    })

                    loading.close()
                    this.scrapApvList()
                })

            },
            deleteClick(row){
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                var nowDate = new Date();
                var scrapedApv = {
                    id:row.id,
                    apvType:'领导审批',
                    result:'审批驳回',
                    name:this.$store.state.user.username,
                    time:nowDate
                }

                scrappedApvModel.scrappedApvUpdate(scrapedApv).then((data)=>{
                    if(data.status=='fail'){
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        loading.close()
                        return
                    }
                    Notification({
                        message: '审批驳回',
                        type: 'success'
                    })

                    loading.close()
                    this.scrapApvList()
                })
            },
            handleSelectionChange(selected){
                this.selected=selected
            },
            handleSizeChange(val) {
                this.scrapedLimit.pageInfo.pageSize=val
                scrappedApvModel.scrappedApvLimit(this.scrapedLimit).then((data)=>{
                    this.scrapeds = data.entity.list
                    this.scrapedLimit.pageInfo.total = data.entity.totalCount
                })
            },
            handleCurrentChange(val) {
                this.scrapedLimit.pageInfo.currentPage=val
                scrappedApvModel.scrappedApvLimit(this.scrapedLimit).then((data)=>{
                    this.scrapeds = data.entity.list
                    this.scrapedLimit.pageInfo.total = data.entity.totalCount
                })
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
        },
        data(){
            return{
                scrapeds:[],
                selected:[],
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

            }
        }

    }
</script>

<style scoped>

</style>