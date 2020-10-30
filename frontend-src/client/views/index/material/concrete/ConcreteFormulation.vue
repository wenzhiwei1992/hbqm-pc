<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                id="projectList"
                                :model="concreteQuery"
                                ref="concreteQuery"
                        >
                            <el-form-item prop="concreteFormulaNo">
                                <el-input
                                        placeholder="配方编号"
                                        v-model="concreteQuery.concreteFormulaNo"
                                        style="width:120px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="concreteGrade">
                                <el-input
                                        placeholder="砼等级"
                                        v-model="concreteQuery.concreteGrade"
                                        style="width:120px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="putDateRange">
                                <el-date-picker
                                        v-model="concreteQuery.valid"
                                        type="daterange"
                                        align="right"
                                        unlink-panels
                                        range-separator="-"
                                        start-placeholder="开始有效时间"
                                        end-placeholder="结束有效时间"
                                        :default-time="['00:00:00', '23:59:59']"
                                        value-format="yyyy-MM-dd"
                                        style="width:300px;">
                                </el-date-picker>
                            </el-form-item>

                        </el-form>
                    </el-col>

                    <el-col>
                        <my-search-button-group
                                :model="searchButtonData"
                        ></my-search-button-group>
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
                        :data="concretes"
                        @selection-change="handleSelectionChange"
                        size="mini"
                        stripe
                        border
                        height="100%"
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                        @expand-change="showSubData"
                        :expand-row-keys="expands"
                        :row-key="getRowKeys"
                        style="width:100%"
                >
                    <el-table-column
                            type="selection"
                            width="50"
                    >
                    </el-table-column>
                    <el-table-column type="expand">
                        <template slot-scope="props">
                            <el-table
                                    :data="subDatas"
                                    @selection-change="handleSelectionChangeForPL"
                                    size="mini"
                                    stripe
                                    border
                                    style="width: 100%"
                            >
                                <el-table-column
                                        type="selection"
                                        width="80">
                                </el-table-column>
                                <el-table-column
                                        type="index"
                                        label="序号"
                                        width="50"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="materialStorageId"
                                        label="原料编码"
                                        width="120"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="name"
                                        label="原料名称"
                                        width="120"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="specification"
                                        label="原料规格"
                                        width="120"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="unit"
                                        label="单位"
                                        width="120"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="num"
                                        label="用量"
                                        width="120"
                                >
                                </el-table-column>
                            </el-table>
                        </template>
                    </el-table-column>
                    <el-table-column
                            type="index"
                            label="序号"
                            width="50"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="concreteFormulaNo"
                            label="配方编号"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="concreteGrade"
                            label="砼等级"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="concreteSlump"
                            label="塌落度"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="createdByName"
                            label="创建人"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="dateCreated"
                            label="创建时间"
                            width="120"
                            :formatter="formatterDate"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="modifiedByName"
                            label="修改人"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="dateModified"
                            label="修改时间"
                            width="120"
                            :formatter="formatterDate"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="validTime"
                            label="有效时间"
                            width="200"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="remark"
                            label="备注"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            :width="rowButtonGroupWidth(rowButtonData)"
                            align="left"
                            fixed="right"
                    >
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
                            :current-page="concreteQuery.pageInfo.currentPage"
                            :page-sizes="concreteQuery.pageInfo.pageSizes"
                            :page-size="concreteQuery.pageInfo.pageSize"
                            :total="concreteQuery.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
        </el-container>
        <el-dialog :title="getTitle()" v-if="editFormVisible" :visible.sync="editFormVisible">
            <el-form :inline="true" :model="editForm" :rules="eidtFormRules" v-if="editForm" ref="editForm" label-width="80px">
                    <!--<div class="divProductAssociated">
                        <el-form-item label="配方编号" prop="productNo">
                            <el-input v-model="editForm.concreteFormulaNo" style="width: 137px"></el-input>
                        </el-form-item>
                    </div>-->
                    <el-form-item label="砼等级" prop="concreteGrade">
                        <el-input v-model="editForm.concreteGrade" style="width: 180px" placeholder="请输入砼等级"></el-input>
                    </el-form-item>

                    <el-form-item label="塌落度" prop="concreteSlump">
                        <el-input v-model="editForm.concreteSlump" style="width: 180px" placeholder="请输入塌落度"></el-input>
                    </el-form-item>
                    <el-form-item label="开始时间" prop="startTime">
                        <el-date-picker v-model="editForm.startTime" align="right" type="datetime" style="width: 180px"
                                        placeholder="请输入开始时间" :formatter="formatterDate" >
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="结束时间" prop="endTime">
                        <el-date-picker v-model="editForm.endTime" align="right" type="datetime" style="width: 180px"
                                        placeholder="请输入结束时间" :formatter="formatterDate" >
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="备注" prop="remark">
                        <el-input v-model="editForm.remark" style="width: 180px" placeholder="输入备注"></el-input>
                    </el-form-item>
                <div class="divMaterialButtons">
                    <el-row type="flex" justify="space-around" align="middle">
                        <el-col>

                        </el-col>
                    </el-row>
                    <el-table
                            :data="editForm.materials"
                            @selection-change="handleSelectionChange"
                            size="mini"
                            stripe
                            border
                            height="150"
                            style="width:100%"
                    >
                        <el-table-column
                                type="selection"
                                width="40">
                        </el-table-column>
                        <el-table-column
                                prop="materialStorageId"
                                label="原料编码"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="name"
                                label="原料名称"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="specification"
                                label="原料规格"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="unit"
                                label="单位"
                                width="120">
                            <template slot-scope="scope">
                                <span v-if="scope.row.num==0 && scope.row.unit==null">kg</span>
                                <span v-else>{{scope.row.unit}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="num"
                                label="用量"
                        >
                            <template slot-scope="scope">
                                <el-input v-model="scope.row.num" @keyup.native="parseValue(scope.row)" placeholder="请输入内容"></el-input>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="success" @click="addMaterials">添加原料</el-button>
                <el-button type="danger" @click="deleteProductMaterialStorage">删除原料</el-button>
                <el-button type="danger" @click="editFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog :visible.sync="editFormMaterialStorageAddVisible" width="65%" append-to-body>
            <material-storage-add
                    :MaterialStorageAdd="editFormMaterialStorageAdd"
                    :listMaterials="listMaterials"
                    v-on:headCallBack="headCall"
                    @material-storage-add-finish="materialStorageAddFinish"
                    @material-storage-cancel="materialStorageAddCancel"
            ></material-storage-add>
        </el-dialog>
        <!--<el-dialog :visible.sync="editFormVisible" v-if="editFormVisible" width="65%" append-to-body>
            <material-storage-add
                    :mode="mode"
                    :concreteInfoEdit="concreteInfoEdit"
                    @concrete-edit-finish="materialStorageAddFinish"
                    @concrete-edit-cancel="materialStorageAddCancel"
            ></material-storage-add>
        </el-dialog>-->
    </div>
</template>

<script>

    import { Notification } from 'element-ui';
    import moment from 'moment';
    import ConcreteMaterialStorageAdd from "./ConcreteMaterialAdd.vue";
    import concreteFormulaModel from "../../../../model/materials/concreteFormula";

    export default {
        data() {
            return {
                subDatas: [],
                selectedFoPL: [],
                expands: [],
                listMaterials: [],
                editFormMaterialStorageAddVisible: false,
                editFormMaterialStorageAdd: null,
                concretes:[],
                show: '', //控制Table表格显示列数
                editForm:{
                    materials:[{
                        cfmId: '',
                        id: '',
                        materialStorageId: '',
                        matlId: '',
                        name: '',
                        num: '',
                        specification: '',
                        unit: '',
                    }],
                },
                concreteQuery: {
                    concreteFormulaNo:'',
                    concreteGrade:'',
                    validTime:'',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    }
                },
                mold: '',
                selected: [],
                concreteInfoEdit:[],
                //编辑表单
                mode: '',
                editFormTitle: '',
                editFormVisible: false,
                checkEditFormVisible: false,
                eidtFormRules: {
                    concreteGrade: [
                        {required: true, message: '请输入混凝土砼等级', trigger: 'change'},
                    ],
                    concreteSlump: [
                        {required: true, message: '请输入混凝土塌落度', trigger: 'change'},
                    ],
                    startTime: [
                        {required: true, message: '请输入混凝土开始时间', trigger: 'change'},
                    ],
                    endTime: [
                        {required: true, message: '请输入混凝土结束时间', trigger: 'change'},
                    ],
                },
                buttonData: [
                    {
                        name: '新建',
                        event: 'addClick'
                    }
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "submitForm('concreteQuery')"
                    }
                ],
                rowButtonData: [
                    {
                        name: '编辑',
                        event: 'editClick',
                        authorized:'turnoverDetailsList:edit'
                    },
                    {
                        name: '删除',
                        event: 'deleteClick',
                        authorized:'turnoverDetailsList:delete'
                    },
                    {
                        name: '复制',
                        event: 'copy'
                    }
                ]
            };
        },
        components: {
            'material-storage-add': ConcreteMaterialStorageAdd,
        },
        mounted() {
            this.list()
        },
        methods: {
            getTitle(){
                if(this.mode==='add'){
                    return '新建';
                }else if(this.mode==='edit'){
                    return '编辑';
                }else if(this.mode==='copy'){
                    return '复制';
                }
            },
            addClick() {
                this.mode = 'add';
                this.editForm = {
                    concreteGrade:'',
                    concreteSlump:'',
                    startTime:new Date(),
                    endTime:new Date(),
                    remark:'',
                    id:'',
                    materials:[],
                    modifiedByName:this.$store.state.user.realName,
                    dateModified:new Date(),
                    createdByName:this.createdByName,
                    dateCreated:new Date(),
                },
                this.editFormVisible = true;
            },
            addMaterials() {
                this.listMaterials = this.editForm.materials
                this.editFormMaterialStorageAddVisible = true
            },
            parseValue(row) {
                let value = row.num;
                value = value.replace(/[^\d.]/g, ""); // 清除"数字"和"."以外的字符
                value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
                value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                    ".");
                value = value.replace(/^(\-)*(\d+)\.(\d\d\d\d).*$/, '$1$2.$3'); // 只能输入4个小数
                row.num = value;
            },
            //点击小箭头展示半成品相关的原料
            showSubData(row, expandedRows) {
                let id = row.id;
                if (this.expands.some((e) => e == row.id)) {
                    this.expands = []
                } else {
                    this.expands = []
                    this.expands.push(row.id)
                }
                //根据半成品的id查询相关原料
                concreteFormulaModel.findConcreteFormulasById(id).then((data) => {
                    this.subDatas = data.entity.cfmList
                })
            },
            getRowKeys(row) {
                return row.id;
            },
            deleteProductMaterialStorage() {
                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }
               // this.editForm.materials.splice(scope.$index, 1)
                this.selected.forEach((row) => {
                    for (var i = 0; i < this.editForm.materials.length; i++) {
                        if (this.editForm.materials[i].id == row.id) {
                            console.log("row.materialStorageId--------->",row.id)
                            this.editForm.materials.splice(i, 1);
                        }
                    }
                })
            },
            list() {
                concreteFormulaModel.findAllByQuery(this.concreteQuery).then((data) => {
                    this.concretes = data.entity.list
                    this.concreteQuery.pageInfo.total = data.entity.totalCount
                    console.log("data.entity.list",data.entity.list)
                })
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.list()
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            editClick(row) {
                concreteFormulaModel.findConcreteFormulasById(row.id).then((data) => {
                    this.editForm = data.entity
                    this.editForm.materials = data.entity.cfmList
                    this.mode = 'edit'
                    this.editFormVisible = true
                })
            },
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
                            concreteFormulaModel.addConcreteFormulas(this.editForm).then((data) => {
                                if (data.status == 'failed') {
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
                                this.list()
                            })
                        } else if (this.mode == 'edit'){
                            console.log(this.editForm)
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            concreteFormulaModel.updateConcreteFormulas(this.editForm).then((data) =>  {
                                if (data.status == 'failed') {
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
                                this.list()
                            })
                        } else {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            concreteFormulaModel.copyByConcreteFormulaId(this.editForm).then((data) =>  {
                                if (data.status == 'failed') {
                                    Notification({
                                        message: data.msg,
                                        type: 'warning'
                                    })
                                    loading.close()
                                    return
                                }
                                Notification({
                                    message: '复制成功',
                                    type: 'success'
                                })
                                loading.close()
                                this.editFormVisible = false
                                this.list()
                            })
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            deleteClick(row) {
                this.$confirm('确定要删除混凝土配方，是否继续?', '提示', {
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
                    concreteFormulaModel.deleteConcreteFormulaById(row.id).then((data) => {
                        if (data.status == 'failed') {
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
                        this.list()
                    })
                }).catch(() => {
                })

            },
            //复制
            copy(row) {
                concreteFormulaModel.findConcreteFormulasById(row.id).then(data => {
                    this.editForm = data.entity
                    this.editForm.materials = data.entity.cfmList
                    this.mode = 'copy';
                    this.editFormVisible = true
                })
            },
            authorized(index) {
                return menuTabService.authorized(this.$store, index);
            },
            handleSizeChange(val) {
                this.concreteQuery.pageInfo.pageSize = val;
                this.list();
            },
            handleCurrentChange(val) {
                this.concreteQuery.pageInfo.currentPage = val;
                this.list();
            },
            handleSelectionChange(selected) {
                this.selected = selected;
            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return '';
                }
                return moment(date).format('YYYY-MM-DD HH:mm:ss');
            },

            materialStorageEditCancel() {
                this.editFormVisible = false;
                this.list();
            },
            checkoutStatusFinish() {
                this.checkEditFormVisible = false;
                this.list();
            },
            checkoutStatusCancel() {
                this.checkEditFormVisible = false;
                this.list();
            },
            headCall(msg) {
                for (var i = 0; i < msg.length; i++) {
                    this.editForm.materials.push(msg[i])
                }
                this.editFormMaterialStorageAddVisible = false
            },
            materialStorageAddFinish() {
                this.editFormMaterialStorageAddVisible = false
            },
            materialStorageAddCancel() {
                this.editFormMaterialStorageAddVisible = false
            },
            handleSelectionChangeForPL(selected) {
                this.selectedFoPL = selected
            },
        }
    };
</script>

<style scoped>
    .divProductAssociated {
        float: left;
    }
    divMaterialButtons {
        float: left;
    }

</style>
