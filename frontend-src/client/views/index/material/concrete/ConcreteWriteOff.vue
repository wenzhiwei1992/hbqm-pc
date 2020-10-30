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
                            <el-form-item>
                                <el-select v-model="concreteQuery.projectId" placeholder="项目名称"  clearable>
                                    <el-option
                                            v-for="item in projectList"
                                            :key="item.id"
                                            :label="item.projectName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="productGrade">
                                <el-input
                                        placeholder="砼等级"
                                        v-model="concreteQuery.productGrade"
                                        style="width:120px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="completionTimes">
                                <el-date-picker
                                        v-model="concreteQuery.completionTimes"
                                        type="daterange"
                                        align="right"
                                        unlink-panels
                                        range-separator="-"
                                        start-placeholder="完成开时间"
                                        end-placeholder="结束开时间"
                                        :default-time="['00:00:00', '23:59:59']"
                                        value-format="yyyy-MM-dd"
                                        style="width:300px;">
                                </el-date-picker>
                            </el-form-item>
                            <el-form-item prop="status">
                                <el-select v-model="concreteQuery.writeOffStatus" clearable placeholder="请选择">
                                    <el-option
                                            v-for="item in options"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
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
                        style="width:100%"
                >
                    <el-table-column
                            type="selection"
                            width="50"
                    >
                    </el-table-column>
                    <el-table-column
                            type="index"
                            label="序号"
                            width="50"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productGrade"
                            label="砼等级"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productVol"
                            label="方量"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="completionTime"
                            label="完成时间"
                            width="150"
                            :formatter="formatterDate"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="projectName"
                            label="项目名称"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="writeOffStatus"
                            label="状态"
                            width="120"
                            :formatter="formatterDate"
                    >
                        <template slot-scope="scope">
                            <span :class="{status_color1: scope.row.writeOffStatus == '未核销' ,
                                          status_color2 : scope.row.writeOffStatus == '已核销',}">{{scope.row.writeOffStatus}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            :width="rowButtonGroupWidth(rowButtonData)"
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
                <div class="divProductAssociated">
                    <el-form-item label="配方编号">
                        <el-select v-model="editForm.cfmId" placeholder="选择配方编号" prop="cfmId" clearable @change="changes">
                            <el-option
                                    v-for="item in concreteFormulaList"
                                    :key="item.id"
                                    :label="item.concreteFormulaNo"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-input v-model="editForm.productId" style="width: 180px" placeholder="请输入构件" hidden="true"></el-input>
                    <el-input v-model="editForm.projectId" style="width: 180px" placeholder="请输入项目" hidden="true"></el-input>
                </div>
                <div class="divMaterialButtons">
                    <el-row type="flex" justify="space-around" align="middle">
                        <el-col>

                        </el-col>
                    </el-row>
                    <br/><br/><br/>
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
                                prop="nums"
                                label="用量"
                        >

                            <!--<template slot-scope="scope">
                                <span>{{scope.row.num * Number(concreteQuery.productVol)}}</span>
                            </template>-->
                        </el-table-column>
                    </el-table>
                </div>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="danger" @click="editFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>

    import moment from 'moment';
    import {Notification} from "element-ui";
    import productModel from "../../../../model/project/productDetail.js";
    import projectModel from "../../../../model/project/project";
    import concreteFormulaModel from "../../../../model/materials/concreteFormula";

    export default {
        data() {
            return {
                concreteFormulaList: [],
                concretes:[],
                projectList: [],
                show: '', //控制Table表格显示列数
                editForm:{
                    productId:'',
                    cfmId: '',
                    projectId:'',
                    proposer: this.$store.state.user.realName,
                    materials: [{
                        id: '',
                        materialStorageId: '',
                        matlId: '',
                        name: '',
                        num: '',
                        specification: '',
                        unit: '',
                        concreteFormulaNo: '',
                    }],

                },
                concreteQuery: {
                    productVol:'',
                    productGrade:'',
                    completionTime:'',
                    writeOffStatus:'',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    }
                },
                eidtFormRules: {
                    cfmId: [
                        {required: true, message: '请输入混凝土配方编号', trigger: 'change'},
                    ],
                },
                mold: '',
                selected: [],
                concreteInfoEdit:[],
                //编辑表单
                mode: '',
                editFormTitle: '',
                editFormVisible: false,
                checkEditFormVisible: false,
                buttonData: [],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "submitForm('concreteQuery')"
                    }
                ],
                rowButtonData: [
                    {
                        name: '核销',
                        event: 'writeOff',
                        disabled: "scope.row.writeOffStatus=='已核销'"
                    }
                ],
                options: [{
                    value: '已核销',
                    label: '已核销'
                }, {
                    value: '未核销',
                    label: '未核销'
                }],
            };
        },
        mounted() {
            this.list()
            this.getTeamInfoList()
        },
        methods: {
            changes(e){
                /*混凝土配方查询*/ //this.concreteFormulaList.id --->用查询到的混凝土配方id
                productModel.findConcreteFormulaNoById(e).then((data) => {
                    this.editForm.materials = data.entity
                    data.entity.forEach(e=>{ //计算原料用料
                        e.nums  = e.num * Number(this.rows.productVol)
                    })
                })
            },
            getTitle(){
                if(this.mode==='writeOff'){
                    return '核销';
                }
            },
            list() {
                productModel.findAllByQueryWriteOff(this.concreteQuery).then((data) => {
                    this.concretes = data.entity.list
                    this.concreteQuery.pageInfo.total = data.entity.totalCount
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
            getTeamInfoList() {
                /*查询项目*/
                projectModel.findAllProjects().then((data) => {
                    this.projectList = data.entity
                })
            },
            writeOff(row) {
                productModel.writeOffConcreteFormulasByIds(row.id).then((data) => {
                    concreteFormulaModel.findAllConcreteFormulas().then((data) => {
                        this.concreteFormulaList = data.entity;
                    })
                    this.editForm.productId = row.id
                    this.editForm.projectId = row.projectId
                    this.rows = row
                    console.log("this.editForm.productId",this.editForm.productId)
                    this.editFormVisible = true
                })
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        productModel.writeOffConcreteFormulasById(this.editForm).then((data) => {
                            if (data.status == 'failed') {
                                Notification({
                                    message: data.msg,
                                    type: 'warning'
                                })
                                loading.close()
                                return
                            }
                            Notification({
                                message: '核销成功',
                                type: 'success'
                            })
                            this.editFormVisible = false
                            this.list()
                        })
                    }
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

        }
    };
</script>

<style scoped>
    .divProductAssociated {
        float: left;
    }

    .status_color2 {
        font-weight: bolder;
        color: #67c23a;
    }

    .status_color1 {
        font-weight: bolder;
        color: #409eff;
    }
</style>
