<template>
    <div class="mod-config">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
            <el-form-item>
                <el-input v-model="dataForm.perCompName" placeholder="预制构件名称" clearable></el-input>
            </el-form-item>
            <el-form-item>
                <el-button @click="getDataList()">查询</el-button>
            </el-form-item>
        </el-form>
        <el-table
                :data="dataList"
                border
                stripe
                v-loading="dataListLoading"
                @selection-change="selectionChangeHandle"
                header-row-class-name="header-row"
                style="width: 100%;">
            <el-table-column
                    type="selection"
                    align="center"
                    width="50">
            </el-table-column>
            <el-table-column
                    prop="perCompName"
                    align="center"
                    label="预制构件名称"
                    width="120"
            >
            </el-table-column>
            <el-table-column
                    prop="perCompDate"
                    align="center"
                    label="预制构件出厂日期"
                    width="150"
                    :formatter="timeFormat">
            </el-table-column>
            <el-table-column
                    prop="prodCompany"
                    align="center"
                    width="200"
                    label="预制混凝土构件生产企业">
            </el-table-column>
            <el-table-column
                    prop="strenthGrade"
                    align="center"
                    label="混凝土设计强度等级"
                    width="200"
            >
            </el-table-column>
            <el-table-column
                    prop="spec"
                    align="center"
                    width="100"
                    label="规格尺寸">
            </el-table-column>
            <el-table-column
                    prop="strenth"
                    align="center"
                    width="120"
                    label="达到设计强度">
            </el-table-column>
            <el-table-column
                    align="center"
                    label="操作">
                <template slot-scope="scope">
                    <el-button @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
                    <el-button @click="deleteHandle(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>


        <el-row type="flex" justify="space-around" align="middle">
            <el-col >
                <el-button type="primary" @click="addOrUpdateHandle()" style="margin-top:15px;">新增</el-button>
                <el-button type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0" style="margin-top:15px;">批量删除
                </el-button>
            </el-col>
            <el-col >
                <el-row type="flex" justify="end" align="middle">
                    <el-pagination
                            @size-change="sizeChangeHandle"
                            @current-change="currentChangeHandle"
                            :current-page="pageIndex"
                            :page-sizes="[10, 20, 50, 100]"
                            :page-size="pageSize"
                            :total="totalPage"
                            background
                            layout="total, sizes, prev, pager, next, jumper">
                    </el-pagination>
                </el-row>

            </el-col>
        </el-row>
        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </div>
</template>

<script>
    import AddOrUpdate from './certi-add-or-update.vue'
    import moment from  'moment'
    import model from '../../../model/model'

    export default {
        data() {
            return {
                dataForm: {
                    key: ''
                },
                dataList: [],
                pageIndex: 1,
                pageSize: 10,
                totalPage: 0,
                dataListLoading: false,
                dataListSelections: [],
                addOrUpdateVisible: false
            }
        },
        components: {
            AddOrUpdate
        },
        activated() {
            this.getDataList()
        },
        methods: {
            // 获取数据列表
            getDataList() {
                this.dataListLoading = true
                model.list('certi', this.dataForm, {
                    currentPage: this.pageIndex,
                    total: this.totalPage,
                    pageSize: this.pageSize
                }).then(data => {
                    if (data && data.status === 'success') {
                        this.dataList = data.entity.content
                        this.totalPage = data.entity.totalElements
                    } else {
                        this.dataList = []
                        this.totalPage = 0
                    }
                    this.dataListLoading = false
                })
            },
            // 每页数
            sizeChangeHandle(val) {
                this.pageSize = val
                this.pageIndex = 1
                this.getDataList()
            },
            // 当前页
            currentChangeHandle(val) {
                this.pageIndex = val
                this.getDataList()
            },
            //时间格式
            timeFormat(row, column, cellValue, index){
                if(cellValue){
                    return moment(cellValue).format('YYYY-MM-DD HH:mm:ss')
                }else{
                    return cellValue
                }

            },
            // 多选
            selectionChangeHandle(val) {
                this.dataListSelections = val
            },
            // 新增 / 修改
            addOrUpdateHandle(id) {
                this.addOrUpdateVisible = true
                this.$nextTick(() => {
                    this.$refs.addOrUpdate.init(id)
                })
            },
            // 删除
            deleteHandle(id) {
                var ids = id ? [id] : this.dataListSelections.map(item => {
                    return item.id
                })
                this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    model.deletes('certi', ids.join(";")).then((data) => {
                        if (data && data.status === 'success') {
                            this.$message({
                                message: '操作成功',
                                type: 'success',
                                duration: 1500,
                                onClose: () => {
                                    this.getDataList()
                                }
                            })
                        } else {
                            this.$message.error(data.msg)
                        }
                    })
                }).catch(()=>{})
            }
        }
    }
</script>
