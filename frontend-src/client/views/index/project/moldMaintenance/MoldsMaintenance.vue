<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true"
                                 id="moldQuery"
                                 :model="moldQuery"
                                 ref="moldQuery">
                            <el-form-item prop="mold">
                                <el-input
                                        placeholder="模具名称"
                                        v-model="moldQuery.moldName"
                                        style="width:150px"
                                        clearable>
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="projectName">
                                <el-select v-model="moldQuery.projectId" placeholder="项目名称" clearable>
                                    <el-option
                                            v-for="item in this.arrProject"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-form>
                    </el-col>
                    <el-col>
                        <my-search-button-group :model='searchButtonData'></my-search-button-group>
                    </el-col>
                </el-row>
                <el-row>
                    <my-button-group :model="buttonData"></my-button-group>
                </el-row>
            </el-header>
            <el-main
                    ref="mainContent"
                    :style="`height:${this.$store.state.mainContentHeight}px`"
            >
                <el-table
                        :data="molds"
                        size="mini"
                        stripe
                        border
                        @selection-change="handleSelectionChange"
                        height="100%"
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
                            v-if="show"
                            label="ID"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            prop="moldName"
                            label="模具名称"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="projectName"
                            label="项目名称"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="lineName"
                            label="产线"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="moldNum"
                            label="套数"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            :width="rowButtonGroupWidth(rowButtonData)">
                        <template slot-scope="scope">
                            <my-row-button-group
                                    :row="scope.row"
                                    :model="rowButtonData"
                            ></my-row-button-group>
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer ref="footOperationBar">
                <el-row>
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="moldQuery.pageInfo.currentPage"
                            :page-sizes="moldQuery.pageInfo.pageSizes"
                            :page-size="moldQuery.pageInfo.pageSize"
                            :total="moldQuery.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
            <el-dialog :title="mode=='add'?'新建模具':'编辑模具'" v-if="editFormVisible" :visible.sync="editFormVisible"
                       width="24%">
                <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px">
                    <el-form-item label="模具序号" prop="mold_no">
                         <el-input v-model="editForm.mold_no" style="width: 185px" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="模具名称" prop="moldName">
                        <el-input v-model="editForm.moldName" style="width: 185px" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="项目名称" prop="projectId">
                        <el-select v-model="editForm.projectId" placeholder="请选择" style="width: 185px" clearable>
                            <el-option
                                    v-for="item in arrProject"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="产线" prop="lineId">
                        <el-select v-model="editForm.lineId" placeholder="请选择" style="width: 185px" clearable>
                            <el-option
                                    v-for="item in arrLine"
                                    :key="item.id"
                                    :label="item.lineName"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="套数" prop="moldNum">
                        <el-input v-model="editForm.moldNum" style="width: 185px" type="number" clearable></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="editFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
                </div>
            </el-dialog>

            <el-dialog
                    append-to-body
                    title="导入"
                    :visible.sync="dialogUploadVisible"
                    width="50%"
            >
                <upload-mold
                        :upload-edit-form="uploadEditForm"
                        :arrProject="arrProject"
                        @upload-edit-finish="uploadEditFinish"
                        @upload-edit-cancel="uploadEditCancel"
                >
                </upload-mold>
            </el-dialog>

            <el-dialog
                    :visible.sync="editFormUpoloadVisible"
                    width="35%"
                    append-to-body
            >
                <file-upload
                        fileName="模具导入.xls"
                        @download-finish="downloadFinish"
                        @download-cancel="downloadCancel"
                ></file-upload>
            </el-dialog>

        </el-container>
    </div>


</template>

<script>
    import projectModel from '../../../../model/project/project'
    import moldModel from '../../../../model/project/mold'
    import PlateModel from '../../../../model/basicdata/plate.js'
    import uploadMold from './UploadMold.vue'
    import FileUpload from '../../../../components/fileUpload/index.vue'
    import {Notification} from 'element-ui'
    import moment from "moment";

    export default {
        components: {
            'upload-mold': uploadMold,
            'file-upload': FileUpload,
        },
         data() {
                     return {
                         editFormUpoloadVisible: false,
                         show: '',//
                         moldQuery: {
                             projectId: '',
                             productNo: '',
                             moldName: '',
                             pageInfo: {
                                 currentPage: 1,
                                 total: 0,
                                 pageSize: this.GLOBAL.pageSize,
                                 pageSizes: this.GLOBAL.pageSizes
                             }
                         },
                         pageInfo: {
                             currentPage: 1,
                             total: 0,
                             pageSize: this.GLOBAL.pageSize,
                             pageSizes: this.GLOBAL.pageSizes
                         },
                         arrNum: [],//moldNum select下拉框
                         uploadEditForm: null,//导入内嵌框表单
                         dialogUploadVisible: false,//上传文件内嵌框visible.sync
                         valueSelectProject: '',//初始化模糊查询下拉框project
                         valueMoldNum: '',//初始化添加构件内嵌套添加模具框moldNum下拉框
                         valueProject: '',//初始化添加构件内嵌套添加模具框project下拉框
                         valueLine: '',//初始化添加构件内嵌套添加模具框line下拉框
                         arrProject: [],//select下拉框（project）
                         arrLine: [],//select下拉框（line）
                         molds: [],
                         selected: [],
                         mode: '',
                         editFormTitle: '',
                         editFormVisible: false,
                         editForm: null,
                         eidtFormRules: {
                             mold_no: [
                                 {required: true, message: '请输入模具序号', trigger: 'blur'},
                             ],
                             moldName: [
                                 {required: true, message: '请输入模具名称', trigger: 'blur'},
                             ],
                             projectId: [
                                 {required: true, message: '请选择项目', trigger: 'change'},
                             ],
                             lineId: [
                                 {required: true, message: '请选择产线', trigger: 'change'},
                             ],
                             moldNum: [
                                 {required: true, message: '请输入套数', trigger: 'blur'},
                             ],
                         },
                         buttonData: [
                             {
                                 name: '新建',
                                 event: 'addClick'
                             },
                             {
                                 name: '选择导入',
                                 event:'upload',
                             },
                             {
                                 name: '删除',
                                 event: 'deletesClick'
                             },
                             {
                                 name: '下载',
                                 event: 'materialExport'
                             }

                         ],
                         searchButtonData: [
                             {
                                 name: '查询',
                                 event: "submitForm('moldQuery')"
                             },
                             {
                                 name: '重置',
                                 event: 'resetForm("moldQuery")'
                             }
                         ],
                         rowButtonData: [
                             {
                                 name: '编辑',
                                 event: "editClick",
                             },
                             {
                                 name: '删除',
                                 event: "deleteClick",
                             },
                         ],
                         editFormSubmmit(formName) {
                             this.$refs[formName].validate((valid) => {
                                 if (valid) {
                                     if (this.mode == 'add') {
                                         const loading = this.$loading({
                                             lock: true,
                                             text: 'Loading',
                                             spinner: 'el-icon-loading',
                                             background: 'rgba(0, 0, 0, 0.7)'
                                         });

                                         console.log(this.editForm)


                                         moldModel.moldAdd(this.editForm).then((data) => {
                                             if (data.status == 'fail') {
                                                 Notification({
                                                     message: data.msg,
                                                     type: 'warning'
                                                 })
                                                 loading.close()
                                                 return
                                             }
                                             Notification({
                                                 message: '新建成功',
                                                 type: 'success'
                                             })
                                             loading.close()
                                             this.editFormVisible = false
                                             this.moldList()/*刷新页面*/
                                         })
                                     } else {

                                         const loading = this.$loading({
                                             lock: true,
                                             text: 'Loading',
                                             spinner: 'el-icon-loading',
                                             background: 'rgba(0, 0, 0, 0.7)'
                                         });
                                         console.log(this.editForm)
                                         moldModel.moldUpdate(this.editForm).then((data) => {
                                             if (data.status == 'fail') {
                                                 Notification({
                                                     message: data.msg,
                                                     type: 'warning'
                                                 })
                                                 loading.close()
                                                 return
                                             }
                                             Notification({
                                                 message: '编辑成功',
                                                 type: 'success'
                                             })
                                             loading.close()
                                             this.editFormVisible = false
                                             this.moldList()
                                         })
                                     }
                                 } else {
                                     console.log('error submit!!');
                                     return false;
                                 }
                             })
                         },

                       }
                      },
        mounted() {
            this.moldList()
            this.getProjects()
            this.getLines()
            this.getMoldNum()
        },
        methods: {
            downloadFinish() {
                this.editFormUpoloadVisible = false
            },
            downloadCancel() {
                this.editFormUpoloadVisible = false
            },
            materialExport() {
                this.editFormUpoloadVisible = true
            },
            uploadEditFinish() {
                this.moldList()
                this.dialogUploadVisible = false
            },
            uploadEditCancel() {
             //   this.dialogVisible = false
            },
            /**
             * 进入导入页面
             */
            upload() {
                this.uploadEditForm = {
                    project: {
                        id: ''
                    }
                }
                this.dialogUploadVisible = true
            },

            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.moldQuery.id = ''
            },

            /**
             *格式化显示时间格式
             */
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            /**
             *模糊查询
             */
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.moldList()
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            /**
             *select moldNum
             */
            getMoldNum() {
                for (var i = 1; i <= 99; i++) {
                    var num = {
                        value: i,
                        label: i
                    }
                    this.arrNum.push(num)
                }
            },
            /**
             * 产生project下拉框赋值
             */
            getProjects() {
                projectModel.projectListS().then((data) => {
                    let projects = data.entity//取到项目[]
                    //把projects中name值取出放入新的数组中
                    for (var j = 0; j < projects.length; j++) {
                        var pName = {
                            value: projects[j].id,
                            label: projects[j].projectName,
                            molds: []
                        }
                        this.arrProject.push(pName)
                    }
                })
            },
            /**
             * 产线line下拉框赋值
             */
            getLines() {
                //2019年8月15日15:05:27 李强 修改为查看当前基地构建信息
                PlateModel.getAllProdLinesByOrgId().then((data) => {
                    this.arrLine = data.entity
                })
            },
            /**
             * 新增模具
             */
            addClick() {
                this.mode = 'add'
                /*          var arrNum = new  Array()
                          for(var i=1;i<=99;i++){
                              var num = {
                                  value:i,
                                  label:i
                              }
                              arrNum.push(num)
                          }*/
                this.editForm = {
                    moldName: '',
                    projectId: '',

                    moldNum: '',
                    lineName: ''
                }
                this.editFormVisible = true
            },
            /**
             * 修改模具
             * @param row
             */
            editClick(row) {
                this.$store.commit('startLoading')
                moldModel.moldLoad(row.id).then(data => {
                    let moldName = data.entity.moldName
                    this.mode = 'update'
                    this.editForm = data.entity;
                    /*                 console.log("this.ed===========================>");//TO DELETE
                                     console.log(this.editForm);//TO DELETE*/
                    this.$store.commit('endLoading')
                    this.editFormVisible = true
                })
            },
            /**
             * 删除模具
             * @param row
             */
            deleteClick(row) {
                this.$confirm('此操作将永久删除该模具, 是否继续?', '提示', {
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
                    moldModel.moldDelete(row.id).then(data => {
                        if (data.status == 'fail') {
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
                        this.moldList()
                    })
                }).catch(() => {
                })
            },
            /**
             * 批量删除模具
             */
            deletesClick() {
                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }
                let ids = []
                this.selected.forEach((row) => {
                    ids.push(row.id)
                })
                this.$confirm('此操作将永久删除勾选模具, 是否继续?', '提示', {
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
                    moldModel.moldDeletes(ids.join(";")).then((data) => {
                        if (data.status == 'fail') {
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
                        this.moldList()
                    })
                }).catch(() => {
                })

            },
            /**
             * 查询模具
             */
            moldList() {
                this.$store.commit('startLoading')
                moldModel.moldList('mold', this.moldQuery, this.moldQuery.pageInfo).then((data) => {
                    this.molds = data.entity.list;
                    this.moldQuery.pageInfo.total = data.entity.totalCount
                })
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.moldQuery.pageInfo.pageSize = val
                this.moldList()
            },
            handleCurrentChange(val) {
                this.moldQuery.pageInfo.currentPage = val
                this.moldList()

            },
            handlebriefCol() {
                this.show = false;
            },
            handleAllCol() {
                this.show = true;
            }
        },
    }

</script>

<style scoped>

</style>