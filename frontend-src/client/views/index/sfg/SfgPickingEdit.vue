<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true" :model="editForm" v-if="editForm" :rules="editFormRules" ref="editForm"
                                 label-width="80px">
                            <!--<el-form-item prop="sfgPickingNo" label="收料单号">
                                <el-input v-model="editForm.sfgPickingNo" placeholder="收料单号"
                                          style="width: 150px"></el-input>
                            </el-form-item>-->
                            <el-form-item prop="proposer" label="申请人">
                                <el-input v-model="editForm.proposer" placeholder="申请人"
                                          style="width: 150px"></el-input>
                            </el-form-item>
                            <el-form-item prop="purpose" label="用途">
                                <!--<el-input v-model="editForm.purpose" placeholder="用途" style="width: 150px"></el-input>-->
                                <el-select v-model="editForm.purpose" placeholder="用途" style="width: 150px" clearable>
                                    <el-option
                                            v-for="item in options"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                           <!-- <el-form-item prop="sfgPickingDate" label="领料时间">
                                <el-date-picker
                                        v-model="editForm.sfgPickingDate"
                                        :formatter="formatterDate"
                                        type="date"
                                        style="width: 150px"
                                        placeholder="领料时间">
                                </el-date-picker>
                            </el-form-item>-->
                            <!--<el-form-item prop="stockId" label="仓库">
                                <el-select v-model="editForm.stockId" style="width: 150px" placeholder="仓库">
                                    <el-option
                                            v-for="item in stockList"
                                            :key="item.id"
                                            :label="item.stockNo"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>-->
                            <el-form-item prop="projectId" label="项目">
                                <el-select v-model="editForm.projectId" style="width: 150px"  @change="changeProject"
                                           placeholder="项目">
                                    <el-option
                                            v-for="item in projectList"
                                            :key="item.id"
                                            :label="item.projectName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="departmentId" label="部门">
                               <!-- <el-input v-model="editForm.departmentId" placeholder="部门" style="width: 150px"></el-input>-->
                                <el-select v-model="editForm.departmentId" style="width: 150px"
                                           placeholder="部门">
                                    <el-option
                                            v-for="item in deptList"
                                            :key="item.id"
                                            :label="item.deptName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="remarks" label="备注">
                                <el-input v-model="editForm.remarks " placeholder="备注"
                                          style="width: 150px"></el-input>
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
                        :data="editForm.sfgPickingDetails"
                        @selection-change="handleSelectionChange"
                        size="mini"
                        stripe
                        border
                        height="100%"
                >
                    <el-table-column
                            type="selection"
                            width="40">
                    </el-table-column>
                    <el-table-column
                            prop="stockNo"
                            label="仓库"
                            width="100">
                    </el-table-column>
                    <el-table-column
                            prop="sfgNo"
                            label="半成品编码"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="sfgName"
                            label="半成品名称"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="rebarShape"
                            label="钢筋形状"
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
                            width="70"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            label="楼层"
                            width="70"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="needNum"
                            label="需求量"
                    ><!--楼层半成品的已领用量=需求量则不显示-->
                    </el-table-column>
                    <el-table-column
                            prop="inStockNum"
                            label="库存量"
                    >
                    </el-table-column>
                   <!-- <el-table-column
                            prop="remainingNum"
                            label="可领用量"
                    >
                    </el-table-column>-->
                    <el-table-column
                            prop="alreadyReceivedNum"
                            label="已出库量"
                    >
                    </el-table-column>
                   <!-- <el-table-column
                            prop="receiveNum"
                            label="领用数量"
                    >
                    </el-table-column>-->
                    <el-table-column
                            prop="num"
                            label="数量"
                    >
                        <template slot-scope="scope">
                            <span v-if="mode=='edit'&&scope.row.remainingNum==0"><el-input disabled
                                                                                             v-model="scope.row.num"
                                                                                             @keyup.native="parseValue(scope.row,scope.$index)"
                                                                                             placeholder="请输入内容"></el-input></span>
                            <span v-else> <el-input v-model="scope.row.num" @keyup.native="parseValue(scope.row)"
                                                     placeholder="请输入内容"></el-input></span>


                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="unitCNName"
                            label="单位"
                            width="70"
                    >
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
            <el-dialog :visible.sync="sfgPickingSelectVisible" v-if="sfgPickingSelectVisible" width="65%"
                       append-to-body>
                <sfg-picking-select
                        :sfgPickingAddList="sfgPickingAddList"
                        :projectId="editForm.projectId"
                        @sfg-picking-select-finish="sfgPickingSelectFinish"
                        @sfg-picking-select-cancel="sfgPickingSelectCancel"
                ></sfg-picking-select>
            </el-dialog>
        </el-container>
    </div>
</template>

<script>
    import treeInfo from '../basic/materialStorage/TreeInfo.vue'
    import stockModel from "../../../model/basicdata/stock"
    import sfgPickingModel from "../../../model/sfg/sfgPicking"
    import sfgPickingSelect from './SfgPickingSelect.vue'
    import teamInfoModel from "../../../model/basicdata/teamInfo";
    import {Notification} from "element-ui";
    import supplierModel from "../../../model/purchase/purchase";
    import deptModel from '../../../model/basicdata/dept';
    import projectModel from '../../../model/project/project'
    import moment from "moment";

    export default {
        name: "SfgPickingEdit",
        props: ['mode', 'sfgPickingInfoEdit'],
        components: {
            'tree-info': treeInfo,
            'sfg-picking-select': sfgPickingSelect
        },
        data() {
            return {
                /*用于过滤已添加半成品*/
                sfgPickingAddList: [],
                editForm: {
                    projectId: null,
                    sfgPickingDetails: [],
                    proposer: this.$store.state.user.realName,
                    //departmentId:this.$store.state.user.deptName,
                },
                editFormRules: {
                    projectId: [
                        {required: true, message: '请输入项目名称', trigger: 'change'},
                    ],
                    departmentId: [
                        {required: true, message: '请选择部门', trigger: 'change'},
                    ],
                    /*proposer: [
                        {required: true, message: '请输入申请人', trigger: 'blur'},
                    ],*/
                    purpose: [
                        {required: true, message: '请输入用途', trigger: 'blur'},
                    ],
                   /* stockId: [
                        {required: true, message: '请选择仓库', trigger: 'blur'},
                    ],*/

                },
                /*项目*/
                projectList: [],
                /*部门*/
                deptList: [],
                /*库区*/
                stockList: [],
                selected: [],
                options: [{
                    value: '生产',
                    label: '生产'
                }, {
                    value: '维修',
                    label: '维修'
                }],
                sfgPickingSelectVisible: false,
                listMaterials: '',
                buttonData: [
                    {
                        name: '添加',
                        event: 'addClick'
                    },
                    {
                        name: '删除',
                        event: "deleteClick",
                    },
                    {
                        name: '确认',
                        event: 'save'
                    },
                ],
                searchButtonData: [],
                /*rowButtonData: [
                  {
                      name: '删除',
                      event: "deleteClick",
                  },
                ],*/
            }
            var checkPlateCount = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('填写合法数字'));
                }
                var r = /^\+?[1-9][0-9]*$/;

                if (value < 0) {
                    callback(new Error('必须大于0'));
                } else {
                    callback();
                }
            };
        },
        mounted() {
            this.sfgPickingInfo()
        },
        methods: {

            addClick() {
                //如果项目名称为空时，不允许添加
                if(this.editForm.projectId === '' || this.editForm.projectId === undefined || this.editForm.projectId === null){
                    this.$message('请先选择增加项目名称');
                    return;
                }
                this.sfgPickingAddList = this.editForm.sfgPickingDetails
                this.sfgPickingSelectVisible = true
            },
            save() {
                console.log('------------>', this.mode)
                if (this.mode == 'add') {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });

                    sfgPickingModel.addSfgPicking(this.editForm).then((data) => {
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
                        this.$emit('sfg-picking-finish')
                    })
                } else if (this.mode == 'edit') {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    sfgPickingModel.updateSfgPicking(this.editForm).then((data) => {
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
                        this.$emit('sfg-picking-finish')
                    })
                }

            },
            sfgPickingInfo() {
                projectModel.findAllProjects().then((data) => {
                    this.projectList = data.entity
                })
                deptModel.listAll().then((data) => {
                    this.deptList = data.entity
                })
                stockModel.getStockByStockType('半成品仓库').then((data) => {
                    this.stockList = data.entity
                })
                //编辑操作执行，加载数据
                if (this.mode == 'edit') {
                    this.editForm = this.sfgPickingInfoEdit
                }
            },
            deleteClick() {
                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }
                this.selected.forEach((row)=>{
                    for(var i=0;i<this.editForm.sfgPickingDetails.length;i++){
                        if(this.editForm.sfgPickingDetails[i].id==row.id && this.editForm.sfgPickingDetails[i].buildingId == row.buildingId &&
                            this.editForm.sfgPickingDetails[i].floorId == row.floorId){
                            this.editForm.sfgPickingDetails.splice(i,1);
                        }
                    }
                })
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            /*deleteClick(scope) {
                this.editForm.sfgPickingDetails.splice(scope.$index, 1)
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
            sfgPickingSelectFinish(list) {
                for (var i = 0; i < list.length; i++) {
                    this.editForm.sfgPickingDetails.push(list[i])
                }
                this.sfgPickingSelectVisible = false
            },
            sfgPickingSelectCancel() {
                this.sfgPickingSelectVisible = false
            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD");
            },
            changeProject() {
                this.editForm.sfgPickingDetails = []
            },
        }
    }
</script>

<style scoped>

</style>
