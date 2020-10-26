<template>
    <div>
        <el-form :inline="true" :model="condition" class="form-inline">
            <el-form-item>
                <el-input v-model="condition.username" placeholder="用户名" clearable />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit">查询</el-button>
            </el-form-item>
        </el-form>
        <el-table v-loading="loading" :data="datas" size="small" style="width: 100%;">
            <el-table-column type="expand">
                <template slot-scope="props">
                    <el-form label-position="left" inline class="demo-table-expand">
                        <el-form-item label="请求方法">
                        <span>{{ props.row.method }}</span>
                        </el-form-item>
                        <el-form-item label="请求参数">
                        <span>{{ props.row.params }}</span>
                        </el-form-item>
                    </el-form>
                </template>
            </el-table-column>
            <el-table-column prop="username" label="用户名"/>
            <el-table-column prop="requestIp" label="IP"/>
            <el-table-column :show-overflow-tooltip="true" prop="address" label="IP来源"/>
            <el-table-column prop="description" label="描述"/>
            <el-table-column prop="browser" label="浏览器"/>
            <el-table-column prop="time" label="请求耗时" align="center">
                <template slot-scope="scope">
                <el-tag v-if="scope.row.time <= 300">{{ scope.row.time }}ms</el-tag>
                <el-tag v-else-if="scope.row.time <= 1000" type="warning">{{ scope.row.time }}ms</el-tag>
                <el-tag v-else type="danger">{{ scope.row.time }}ms</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建日期" width="180px">
                <template slot-scope="scope">
                    <span>{{ formatterDate(scope.row.createTime) }}</span>
                </template>
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle" >
            <el-col style="margin-top:15px">
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
import logModel from '../../../../model/basicdata/log'
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
            loading:false,
            datas:[],
            selected:[],
            condition:{
                
            }
        }
    },
    mounted() {
        this.list();
    },
    methods: {
        onSubmit(){
            this.list();
        },
        formatterDate(column){
            var date = column;
            if (date == undefined) {
                return "";
            }
            return moment(date).format("YYYY-MM-DD HH:mm:ss");
        },
        list() {
            this.loading = true;
            const sort = 'id,desc'
            this.condition.pageInfo= this.pageInfo
            logModel.list(this.condition).then(data =>{
                this.datas = data.data.content
                this.pageInfo.total = data.data.totalElements;
                this.loading = false;
            }).catch(err=>{
                console.error(err)
                this.loading = false;
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
        }
    },

}
</script>
<style>

</style>