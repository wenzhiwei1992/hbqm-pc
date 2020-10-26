<template>
    <div>
        <el-row>
            <el-col>
                <el-button style="margin-bottom:10px" @click="addClick" type="primary" icon="el-icon-plus">新建
                </el-button>
            </el-col>
        </el-row>
        <el-table
                :data="datas"
                size="mini"
                stripe
                border
                style="width: 100%"
        >
            <el-table-column
                    type="index"
                    label="序号"
                    width="60">
            </el-table-column>
            <el-table-column
                    prop="templateTypeName"
                    label="物料类型">
            </el-table-column>
            <el-table-column
                    prop="templatePath"
                    label="模板地址">
            </el-table-column>
            <el-table-column
                    label="操作"
                    align="left">
                <template slot-scope="scope">
                    <el-button size="mini" @click="editClick(scope.row)" icon="el-icon-edit"></el-button>
                    <el-button size="mini" @click="deletesClick(scope.row)" icon="el-icon-delete"></el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle">
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
        <el-dialog :title="mode=='add'?'新建':'编辑'" :visible.sync="editFormVisible" v-if="editFormVisible">
            <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="160px">

                <el-form-item label="类型" prop="templateTypeId">
                    <el-select v-model="editForm.templateTypeId" filterable placeholder="请选择">
                        <el-option
                                v-for="item in arrCategory"
                                :key="item.id"
                                :label="item.categoryName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="模板地址" prop="templatePath">
                    <el-input v-model="editForm.templatePath"></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
            </div>
        </el-dialog>

    </div>
</template>
<script>
    import thridTemplate from '../../../../model/system/thridTemplate'
    import categoryModel from '../../../../model/basicdata/category'
    import {Notification} from 'element-ui'
    import moment from 'moment'

    export default {
        data() {
            return {
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: 10,
                    pageSizes: this.GLOBAL.pageSizes
                },
                datas: [],
                arrCategory: [],
                mode: '',
                editFormVisible: false,
                editFormDDVisible: false,
                editForm: {
                    templateTypeId: '',
                    templatePath: '',
                    templateNo: '物料模板'
                },
                eidtFormRules: {
                    templateTypeId: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    templatePath: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ]
                },
            }
        },
        mounted() {
            this.list();
            this.showCategory();
        },
        methods: {
            showCategory() {
                categoryModel.getCategorysByOrgId().then((data) => {
                    this.arrCategory = data.entity
                })
            },

            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.mode == 'edit') {
                            thridTemplate.templetUpdate(this.editForm)
                                .then((data) => {
                                    if (data.status == 'failed') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        });
                                        return
                                    }
                                    Notification({
                                        message: '编辑成功',
                                        type: 'success'

                                    });
                                    this.editFormVisible = false;
                                    this.list();
                                }).catch(() => {

                                }
                            )
                        } else {

                            thridTemplate.templetAdd(this.editForm)
                                .then((data) => {
                                    if (data.status == 'failed') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        });
                                        return
                                    }
                                    Notification({
                                        message: data.msg,
                                        type: 'success'

                                    });
                                    this.editFormVisible = false;
                                    this.list()
                                })
                        }

                    } else {
                        return false;
                    }
                });
            },
            addClick() {
                this.mode = 'add';
                this.editForm = {
                    templateTypeId: '',
                    templatePath: '',
                    templateNo: '物料模板'
                };
                this.editFormVisible = true
            },
            editClick(row) {
                this.$store.commit('startLoading');
                thridTemplate.templetById(row.id).then(data => {
                    this.mode = 'edit';
                    this.editForm = data.entity;
                    this.editForm.userId = Number(data.entity.userId)
                    this.$store.commit('endLoading');
                    this.editFormVisible = true
                }).catch((data) => {

                })

            },
            deletesClick(row) {
                this.$confirm('确认删除选择项?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    thridTemplate.templetDeletes(row.id).then((data) => {
                        if (data.status == 'success') {
                            Notification({
                                type: 'success',
                                message: this.GLOBAL.MSG.UPDATE_SUCCESS
                            })
                        }
                        this.list();
                    }).catch(err => {
                        console.log(err);
                    })

                }).catch((err) => {
                    console.log(err);
                })
            },
            list() {
                this.$store.commit('startLoading');
                let conditon = {
                    pageInfo: this.pageInfo
                }

                thridTemplate.templetList(conditon).then(data => {
                    this.datas = data.entity.content;
                    this.datas.forEach((pd) => {
                        this.arrCategory.find((type) => {
                            if(pd.templateTypeId == type.id){
                                pd.templateTypeName = type.categoryName
                            }
                        })
                    });

                    this.pageInfo.total = data.entity.totalElements;
                    this.$store.commit('endLoading')
                }).catch(err => {
                })
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