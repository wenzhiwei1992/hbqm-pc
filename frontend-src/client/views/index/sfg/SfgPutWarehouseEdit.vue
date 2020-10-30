<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true" :model="editForm" v-if="editForm" :rules="editFormRules" ref="editForm"
                                 label-width="80px">
                            <el-form-item prop="sfgWarehousingNo" label="入库单号">
                                <el-input v-model="editForm.sfgWarehousingNo" placeholder="入库单号"
                                          style="width: 150px"></el-input>
                            </el-form-item>
                            <el-form-item prop="type" label="入库类型">
                                <el-select
                                        v-model="editForm.type"
                                        placeholder="入库类型"
                                        clearable
                                        style="width: 150px"
                                        collapse-tags
                                >
                                    <el-option
                                            label="生产入库"
                                            value="0"
                                    ></el-option>
                                    <el-option
                                            label="采购入库"
                                            value="1"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item v-if="editForm.type=='1' "  prop="transport" label="运输单位">
                                <el-input v-model="editForm.transport" placeholder="运输单位"
                                          style="width: 150px"></el-input>
                            </el-form-item>
                            <el-form-item  v-if="editForm.type=='1' "  prop="freight" label="运费">
                                <el-input v-model="editForm.freight" placeholder="运费" style="width: 150px"></el-input>
                            </el-form-item>
                            <el-form-item v-if="editForm.type=='1' "  prop="licenseNo" label="车牌号">
                                <el-input v-model="editForm.licenseNo" placeholder="车牌号"
                                          style="width: 150px"></el-input>
                            </el-form-item>


                            <el-form-item v-if="editForm.type=='0' " prop="teamId" label="生产班组">
                                <el-select v-model="editForm.teamId" style="width: 150px"
                                           placeholder="生产班组">
                                    <el-option
                                            v-for="item in teamInfoList"
                                            :key="item.id"
                                            :label="item.teamName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="stockId" label="仓库">
                                <el-select v-model="editForm.stockId" style="width: 150px" placeholder="仓库">
                                    <el-option
                                            v-for="item in stockList"
                                            :key="item.id"
                                            :label="item.stockNo"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item v-if="editForm.type=='1' "  prop="shipmentsId" label="供方">
                                <el-select v-model="editForm.shipmentsId" style="width: 150px"
                                           placeholder="供方">
                                    <el-option
                                            v-for="item in supplierList"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="remarks" label="备注">
                                <el-input v-model="editForm.remarks " placeholder="备注"
                                          style="width: 150px"></el-input>
                            </el-form-item>
                            <el-form-item prop="projectId" label="项目名称">
                                <el-select
                                        v-model="editForm.projectId"
                                        placeholder="项目名称"
                                        @change="changeProject"
                                        clearable
                                        style="width: 150px;"
                                >
                                    <el-option
                                            v-for="item in projectList"
                                            :key="item.id"
                                            :label="item.projectName"
                                            :value="item.id"
                                    >
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
                        :data="editForm.sfgWarehousingDetails"
                        size="mini"
                        @selection-change="handleSelectionChange"
                        stripe
                        border
                        height="100%"
                >
                    <el-table-column
                            type="selection"
                            width="40">
                    </el-table-column>
                    <el-table-column
                            prop="sfgNo"
                            label="半成品编码"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="sfgName"
                            label="半成品名称"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="projectName"
                            label="项目名称"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="buildCode"
                            label="楼栋"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            label="楼层"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="unitCNName"
                            label="单位"
                    >
                        <template slot-scope="scope">
                            <span v-if="scope.row.num==0 && scope.row.unitCNName==null">kg</span>
                            <span v-else>{{scope.row.unitCNName}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="needNum"
                            label="需求量"
                    ><!--楼层半成品的已入库量=需求量则不显示。-->
                        <template slot-scope="scope">
                            <span v-if="scope.row.haveReceiptNum == scope.row.needNum"></span>
                            <span v-else>{{scope.row.needNum}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="haveReceiptNum"
                            label="已入库量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="num"
                            label="入库数量"
                    >
                        <template slot-scope="scope">
                            <el-input v-model="scope.row.num" @keyup.native="parseValue(scope.row)"
                                      placeholder="请输入内容"></el-input>
                        </template>
                    </el-table-column>
                    <!--<el-table-column
                            label="操作"
                            align="center"
                    >
                        <template slot-scope="scope">
                            <my-row-button-group
                                    :row="scope"
                                    :model="rowButtonData"
                            ></my-row-button-group>
                        </template>
                    </el-table-column>-->
                </el-table>
            </el-main>
            <el-footer ref="footOperationBar"></el-footer>
            <el-dialog :visible.sync="sfgWarehouseSelectVisible" v-if="sfgWarehouseSelectVisible" width="65%"
                       append-to-body>
                <sfg-warehouse-select
                        :projectId = "editForm.projectId"
                        :sfgAddList="sfgAddList"
                        @sfg-warehouse-select-finish="sfgWarehouseSelectFinish"
                        @sfg-warehouse-select-cancel="sfgWarehouseSelectCancel"
                ></sfg-warehouse-select>
            </el-dialog>
        </el-container>
    </div>
</template>

<script>
    import treeInfo from '../basic/materialStorage/TreeInfo.vue'
    import stockModel from "../../../model/basicdata/stock"
    import sfgWarehousingModel from "../../../model/sfg/sfgWarehousing"
    import sfgWarehouseSelect from './SfgWarehouseSelect.vue'
    import teamInfoModel from "../../../model/basicdata/teamInfo";
    import {Notification} from "element-ui";
    import supplierModel from "../../../model/purchase/purchase";
    import projectModel from "../../../model/project/project";

    export default {
        name: "SfgWarehouseEdit",
        props: ['mode', 'sfgWarehousingInfoEdit'],
        components: {
            'tree-info': treeInfo,
            'sfg-warehouse-select': sfgWarehouseSelect
        },
        data() {
            return {
                /*projectId:'',*/
                /*用于过滤已添加半成品*/
                sfgAddList: [],
                editForm: {
                    type:'0',
                    projectId: null,
                    planDetails: [],
                    sfgWarehousingDetails: []
                },
                editFormRules: {
                    stockId: [
                        {required: true, message: '请选择仓库', trigger: 'change'},
                    ],
                    type: [
                        {required: true, message: '请选择来源', trigger: 'change'},
                    ],
                    teamId: [
                        {required: true, message: '请选择班组', trigger: 'change'},
                    ],

                },
                selected: [],
                projectList: [],
                /*库区*/
                stockList: [],
                /*班组*/
                teamInfoList: [],
                /*供应商*/
                supplierList: [],
                sfgWarehouseSelectVisible: false,
                listMaterials: '',
                buttonData: [
                    {
                        name: '添加',
                        event: 'addClick'
                    },
                    {
                        name: '删除',
                        event: 'deleteClick'
                    },
                    {
                        name: '确认',
                        event: 'save'
                    },
                ],
                searchButtonData: [],
                rowButtonData: [
                   /* {
                        name: '删除',
                        event: "deleteClick",
                    },*/
                ],
            }
            /*var checkPlateCount = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('填写合法数字'));
                }
                var r = /^\+?[1-9][0-9]*$/;

                if (value < 0) {
                    callback(new Error('必须大于0'));
                } else {
                    callback();
                }
            };*/
        },
        mounted() {
            this.sfgWarehousingInfo()
        },
        methods: {
            deleteClick() {
                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }
                this.selected.forEach((row)=>{
                    for(var i=0;i<this.editForm.sfgWarehousingDetails.length;i++){
                        if(this.editForm.sfgWarehousingDetails[i].id==row.id && this.editForm.sfgWarehousingDetails[i].buildingId == row.buildingId &&
                            this.editForm.sfgWarehousingDetails[i].floorId == row.floorId){
                            this.editForm.sfgWarehousingDetails.splice(i,1);
                        }
                    }
                })

            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            addClick() {
                //如果项目名称为空时，不允许添加
                if(this.editForm.projectId === '' || this.editForm.projectId === undefined || this.editForm.projectId === null){
                    this.$message('请先选择增加项目名称');
                    return;
                }
                this.sfgAddList = this.editForm.sfgWarehousingDetails
                this.sfgWarehouseSelectVisible = true
            },
            save() {
                if (this.mode == 'add') {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    sfgWarehousingModel.addSfgWarehousing(this.editForm).then((data) => {
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
                        this.$emit('sfg-put-warehouse-finish')
                    })
                } else if (this.mode == 'edit') {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    sfgWarehousingModel.updateSfgWarehousing(this.editForm).then((data) => {
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
                        this.$emit('sfg-put-warehouse-finish')
                    })
                }

            },
            sfgWarehousingInfo() {
                stockModel.getStockByStockType('半成品仓库').then((data) => {
                    this.stockList = data.entity
                })
                teamInfoModel.getTeamByOrg().then((data) => {
                    this.teamInfoList = data.entity
                })
                supplierModel.loadAllSupplier().then((data) => {
                    this.supplierList = data.entity
                })
                projectModel.projectListS().then((data) => {
                    this.projectList = data.entity
                })

                //编辑操作执行，加载数据
                if (this.mode == 'edit') {
                    this.editForm = this.sfgWarehousingInfoEdit
                }
            },
            /*deleteClick(scope) {
                this.editForm.sfgWarehousingDetails.splice(scope.$index, 1)
            },*/
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
            sfgWarehouseSelectFinish(list) {
                for (var i = 0; i < list.length; i++) {
                    this.editForm.sfgWarehousingDetails.push(list[i])
                }
                this.sfgWarehouseSelectVisible = false
            },
            sfgWarehouseSelectCancel() {
                this.sfgWarehouseSelectVisible = false
            },
            changeProject() {
                this.editForm.planDetails = []
            },
        }
    }
</script>

<style scoped>

</style>
