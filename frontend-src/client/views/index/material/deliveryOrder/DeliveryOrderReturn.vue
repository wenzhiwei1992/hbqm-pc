<template>
    <div>
        <div class="formClass">
            <el-form :inline="true" :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm"
                     label-width="80px">
                <el-form-item label="申 请 人" prop="proposer">
                    <el-input v-model="editForm.proposer" style="width: 178px"></el-input>
                </el-form-item>
                <el-form-item label="部门" prop="departmentId">
                    <el-select
                            v-model="editForm.departmentId" filterable
                            style=""
                            placeholder="请选择">
                        <el-option
                                v-for="item in optionsDept"
                                :key="item.id"
                                :label="item.deptName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="用途" prop="purpose">
                    <el-select
                            v-model="editForm.purpose"
                            style="margin-left: 14px;"
                            placeholder="请选择">
                        <el-option
                                v-for="item in optionsPurpose"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="领料类型" prop="deliveryOrderType">
                    <el-select
                            v-model="editForm.deliveryOrderType"
                            style="width: 178px"
                            placeholder="请选择"
                    >
                        <el-option
                                v-for="item in optionsDeliveryOrderType"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="领料单单号" label-width="120px" prop="deliveryPlanOrderNo">
                    <el-select
                            v-model="editForm.deliveryPlanOrderNo" filterable
                            style="width: 178px"
                            @change="changePlan"
                            placeholder="请选择"
                    >
                        <el-option
                                v-for="item in deliveryPlanOrderSelect"
                                :key="item.id"
                                :label="item.deliveryNum"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="退料日期" prop="deliveryOrderTime" width="300px">
                    <el-date-picker
                            v-model="editForm.deliveryOrderTime"
                            type="datetime"
                            :formatter="formatterDate"
                            placeholder="选择日期时间"
                            default-time="12:00:00"
                            style="width: 186px">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="项目" prop="projectId" width="300px">
                    <el-select v-model="editForm.projectId"
                               placeholder="请选择"
                               disabled readonly
                               style="margin-left: 20px;">
                        <el-option
                                v-for="item in optionsProject"
                                :key="item.id"
                                :label="item.projectName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="仓库" prop="stockId" width="300px">
                    <el-select v-model="editForm.stockId"
                               style=""
                               disabled readonly
                               placeholder="请选择">
                        <el-option
                                v-for="item in stockSelect"
                                :key="item.stockName"
                                :label="item.stockName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="备注" prop="remark" width="300px">
                    <el-input v-model="editForm.remark" type="textarea"></el-input>
                </el-form-item>
            </el-form>
        </div>
        <div>
            <el-row type="flex" justify="space-around" align="middle">
                <el-col>
                    <el-button type="danger" @click="deletesClicks">删除</el-button>
                </el-col>
            </el-row>
            <el-table
                    :data="editForm.listDoDetail"
                    @selection-change="handleSelectionChange"
                    size="mini"
                    stripe
                    border
                    height="300"
                    style="width:100%"
            >
                <el-table-column
                        type="selection"
                        width="35">
                </el-table-column>
                <el-table-column
                        prop="materialStorageId"
                        label="原料编码"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="materialName"
                        label="名称"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="materialSpecification"
                        label="规格"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="materialUnit"
                        label="单位"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="surplus"
                        label="领料量"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="quantity"
                        label="退库量"
                        width="120">
                    <template slot-scope="scope">
                        <span v-if="mode=='update'&&scope.row.surplus==0"><el-input disabled
                                                                                         v-model="scope.row.quantity"
                                                                                         @keyup.native="parseValue(scope.row,scope.$index)"
                                                                                         placeholder="请输入内容"></el-input></span>
                        <span v-else><el-input v-model="scope.row.quantity"
                                               @keyup.native="parseValue(scope.row,scope.$index)"
                                               placeholder="请输入内容"></el-input></span>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editFormCancel">关闭</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import deliveryOrderModel from '../../../../model/materials/deliveryOrder'
    import deptModel from '../../../../model/basicdata/dept'
    import projectModel from '../../../../model/project/project'
    import stockModel from '../../../../model/basicdata/stock'
    import {Notification} from 'element-ui'
    import moment from 'moment'

    export default {
        mounted() {
            this.getAllDept()
            this.getAllProject()
            this.stockList();
            this.getDeliveryOrderNoByTL()

        },
        name: "DeliveryOrderEdit",
        props: ['editForm', 'mode'],
        components: {
        },
        methods: {
            parseValue(row, index) {
                let value = row.quantity;
                value = value.replace(/[^\d.]/g, ""); // 清除"数字"和"."以外的字符
                value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
                value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                    ".");
                value = value.replace(/^(\-)*(\d+)\.(\d\d\d\d).*$/, '$1$2.$3'); // 只能输入3个小数
                row.quantity = value;
                let num = 0;
                for (let i = 0; i < this.editForm.listDoDetail.length; i++) {
                    num = parseFloat(num) + parseFloat(this.editForm.listDoDetail[i].quantity == null ? 0 : this.editForm.listDoDetail[i].quantity);
                }
            },
            //退料
            getDeliveryOrderNoByTL() {
                 deliveryOrderModel.getDeliveryOrderNoByTL().then((data) => {
                    this.deliveryPlanOrderSelect = data.entity;
                     if(!this.editForm.mode && this.editForm.deliveryPlanOrderNo){
                         this.changePlan()
                     }
                })
            },

            changePlan() {
                this.deliveryOrderQuery.returnId = this.editForm.deliveryPlanOrderNo;
                this.deliveryOrderQuery.id = this.editForm.deliveryOrderId;
                this.editForm.listDoDetail = [];
                deliveryOrderModel.getDoDetailsByMaDeliveryOrderIdTL(this.deliveryOrderQuery).then((data) => {
                    if (data.entity.length) {
                        this.editForm.stockId = data.entity[0].stockId;
                        this.editForm.purpose = data.entity[0].purpose;
                        this.editForm.departmentId = data.entity[0].departmentId;
                        this.editForm.projectId = data.entity[0].projectId;
                    }
                    console.log(this.editForm)

                    for (let i = 0; i < data.entity.length; i++) {//遍历为领料单原料集合添加新的原料
                        this.editForm.listDoDetail.push(data.entity[i])
                    }
                })

            },
            stockList() {
                stockModel.getStockByOrgIdAndType().then((data) => {
                    this.stockSelect = data.entity;
                })
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.mode == 'update') {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            deliveryOrderModel.deliveryOrderUpdateTL(this.editForm).then((data) => {
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
                                loading.close();
                                this.$emit('deliveryOrder-edit-finish')
                            })


                        } else if (this.mode == 'add') {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            deliveryOrderModel.deliveryOrderAddTL(this.editForm).then((data) => {
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
                                loading.close();
                                this.$emit('deliveryOrder-edit-finish')
                            })

                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            getAllDept() {
                deptModel.listAll().then((data) => {
                    this.optionsDept = data.entity
                })
            },
            getAllProject() {
                projectModel.projectListS().then((data) => {
                    this.optionsProject = data.entity
                })
            },
            /**
             * 删除操作
             */
            deletesClicks() {
                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }
                this.selected.forEach((row) => {
                    for (var i = 0; i < this.editForm.listDoDetail.length; i++) {
                        // if(this.editForm.listDoDetail.length>1){
                        if (this.editForm.listDoDetail[i].materialStorageId == row.materialStorageId) {
                            this.editForm.listDoDetail.splice(i, 1);
                        }
                    }
                })
            },
            editFormCancel() {
                this.$emit('deliveryOrder-edit-cancel')
            },

            handleSelectionChange(selected) {
                this.selected = selected
            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
        },
        data() {
            return {
                deliveryPlanOrderSelect:[],
                stockSelect: [],
                selected: [],
                quantity: 0,//申请量
                univalence: 0,//单价
                sum: 0,//总价格
                optionsDeliveryOrderType: [
                    {
                        value: '退库',
                        label: '退库'
                    },
                ],
                optionsPurpose: [
                    {
                        value: '生产',
                        label: '生产'
                    },
                    {
                        value: '维修',
                        label: '维修'
                    },
                    {
                        value: '办公',
                        label: '办公'
                    },
                    {
                        value: '固定资产',
                        label: '固定资产'
                    },
                ],
                optionsDept: '',
                optionsProject: '',
                deliveryOrderQuery: {
                    id: '',
                    returnId:''
                },
                eidtFormRules: {
                    proposer: [
                        {required: true, message: '申请人不能为空', trigger: 'blur'},
                    ],
                    departmentId: [
                        {required: true, message: '部门不能为空', trigger: 'change'},
                    ],
                    purpose: [
                        {required: true, message: '用途不能为空', trigger: 'change'},
                    ],
                    deliveryOrderType: [
                        {required: true, message: '领料类型不能为空', trigger: 'change'},
                    ],
                    deliveryPlanOrderNo: [
                        {required: true, message: '请选择退料单号', trigger: 'change'},
                    ],
                    deliveryOrderTime: [
                        {required: true, message: '发料日期不能为空', trigger: 'blur'},
                    ],
                    // stockId: [
                    //     {required: true, message: '仓库不能为空', trigger: 'change'},
                    // ],
                    projectId: [
                        {required: true, message: '项目不能为空', trigger: 'change'},
                    ],
                },
            }

        },
    }
</script>

<style scoped>
    .formClass {
        border: 1px lavender solid;
        margin-bottom: 10px;
        padding-top: 17px;
    }

    .sumClass {
        color: #F56C6C;
    }
</style>
