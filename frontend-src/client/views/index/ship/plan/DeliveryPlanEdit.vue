<template>
    <div>
        <el-form :model="editForm" v-if="editForm" ref="editForm" :rules="eidtFormRules" label-width="100px"
                 size="small" :inline="true">

            <el-form-item label="发货日期" prop="deliverDate">
                <el-date-picker type="date" style="width: 150px;" size="mini"
                                v-model="editForm.deliverDate"></el-date-picker>
            </el-form-item>
            <el-form-item label="项目名称" prop="projectId">
                <el-select v-model="editForm.projectId" style="width: 150px;" size="mini" clearable
                           :disabled="this.newInvoiceDetails!=''" placeholder="" @change="changeProject">
                    <el-option
                            v-for="item in projectList"
                            :key="item.id"
                            :label="item.projectName"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button size="mini" type="success" :disabled="this.editForm.projectId==''" @click="chooseProduct">
                    添加构件
                </el-button>

            </el-form-item>
        </el-form>

        <el-table
                :data="newInvoiceDetails"
                size="mini"
                stripe
                border
                style="width:100%"
                :height="GLOBAL.tabelHeight"
        >


            <el-table-column
                    prop="buildCode"
                    label="楼栋"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="floorNum"
                    label="楼层"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="productNo"
                    label="构件编码"
                    align="center"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="productName"
                    label="构件名称"
                    align="center"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="productWt"
                    label="构件重量(t)"
                    align="center"
                    width="110"
                    :formatter="wtFormat"
            ></el-table-column>
            <el-table-column
                    prop="projectConcrete"
                    label="构件方量(m³)"
                    align="center"
                    width="110"
                    :formatter="wtFormat"
            ></el-table-column>

            <el-table-column
                    label="操作"
                    align="center"
                    width="200"
            >
                <template slot-scope="scope">
                    <el-button type="danger" round icon="el-icon-minus"
                             @click="deleteDeliveryPlanDetail(scope.row)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-row type="flex" justify="space-around" align="middle" style="margin-top:15px">
            <el-col>
                <el-button type="primary" size="mini" @click="editFormSubmmit('editForm')">确 定</el-button>
                <el-button @click="editFormCancel" size="mini">取 消</el-button>
            </el-col>
        </el-row>


        <el-dialog append-to-body title="添加构件" :visible.sync="deliveryPlanAddVisible" width="85%"
                   v-if="deliveryPlanAddVisible">
            <delivery-plan-add
                    :newInvoiceDetails="newInvoiceDetails"
                    :projectId="projectId"
                    @delivery-plan-add-finish="deliveryPlanAddFinish"></delivery-plan-add>
        </el-dialog>
    </div>
</template>

<script>
    var numeral = require('numeral')
    import planModel from '../../../../model/plan/plan'
    import projectModel from '../../../../model/project/project'
    import DeliveryPlanAdd from './DeliveryPlanAdd.vue'
    import {Notification} from "element-ui";

    export default {
        name: "deliveryPlanEdit",
        props: ['mode', 'plan'],
        components: {
            'delivery-plan-add': DeliveryPlanAdd
        },
        data() {
            return {
                editForm: {
                    modeDecide: '',
                    projectId: '',
                    deliverDate: null,
                    planDetails: []
                },
                projectList: [],
                newInvoiceDetails: [],
                deliveryPlanAddVisible: false,
                projectId: '',
                eidtFormRules: {
                    deliverDate: [
                        {required: true, message: '请选择发货日期', trigger: 'blur'},
                    ],
                    projectId: [
                        {required: true, message: '请选择项目', trigger: 'blur'},
                    ],
                },
            }
        },
        mounted() {
            this.init()
        },
        methods: {
            init() {
                projectModel.getExternalAllProject().then(data => {
                    this.projectList = data.entity;
                }).catch(() => {
                    this.$message('加载项目信息失败')
                })
                if (this.mode == 'update') {
                    console.log('this.plan------>', this.plan)
                    this.editForm = this.plan
                    this.newInvoiceDetails = this.editForm.planDetails
                }

            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.editForm.planDetails = this.newInvoiceDetails
                        if (this.editForm.projectId == null || this.editForm.projectId == '') {
                            this.$message('请选择项目');
                            return;
                        }
                        if (this.editForm.deliverDate == null || this.editForm.deliverDate == '') {
                            this.$message('请选择发货日期');
                            return;
                        }
                        if (this.editForm.planDetails.length == 0) {
                            this.$message('请添加构件明细');
                            return;
                        }
                        //用于后段调用限定API调取相应方法
                        if (this.mode == this.GLOBAL.STATUS.ADD) {
                            this.editForm.modeDecide = this.GLOBAL.STATUS.ADD
                        } else if (this.mode == this.GLOBAL.STATUS.UPDATE) {
                            this.editForm.modeDecide = this.GLOBAL.STATUS.UPDATE
                        }
                        //用于后段调用限定API调取相应方法
                        let deciceInfo = {
                            invoiceDetailList: this.editForm.planDetails,
                            modeDecidePlan: this.mode,
                        }
                        planModel.loadFloorRelevanceIdAndDecideByPlan(deciceInfo).then((data) => {
                            if (data.status == "failed") {
                                Notification({
                                    message: data.msg,
                                    type: "warning"
                                });
                                return;
                            }

                            if (this.mode == 'add') {
                                const loading = this.$loading({
                                    lock: true,
                                    text: 'Loading',
                                    spinner: 'el-icon-loading',
                                    background: 'rgba(0, 0, 0, 0.7)'
                                });

                                planModel.addNewPlan(this.editForm).then((data) => {
                                    if (data.status == "fail") {
                                        Notification({
                                            message: data.msg,
                                            type: "warning"
                                        });
                                        loading.close()
                                        return;
                                    }
                                    //this.editForm.projectId == ''
                                    Notification({
                                        message: this.GLOBAL.MSG.ADD_SUCCESS,
                                        type: "success"
                                    });
                                    loading.close();
                                    this.$emit('model-edit-finish')
                                })
                            } else if (this.mode == 'update') {
                                const loading = this.$loading({
                                    lock: true,
                                    text: 'Loading',
                                    spinner: 'el-icon-loading',
                                    background: 'rgba(0, 0, 0, 0.7)'
                                });

                                planModel.updateNew(this.editForm).then((data) => {
                                    if (data.status == "fail") {
                                        Notification({
                                            message: data.msg,
                                            type: "warning"
                                        });
                                        loading.close()
                                        return;
                                    }
                                    Notification({
                                        message: this.GLOBAL.MSG.EDIT_SUCCESS,
                                        type: "success"
                                    });
                                    loading.close();
                                    this.$emit('model-edit-finish')
                                })
                            }
                        })


                    }
                })
            },
            changeProject() {
                this.editForm.planDetails = []
            },
            //选择构件
            chooseProduct() {
                if (this.editForm.projectId === '' || this.editForm.projectId === undefined || this.editForm.projectId === null) {
                    this.$message("请先选择项目！")
                    return
                }
                this.projectId = this.editForm.projectId
                this.deliveryPlanAddVisible = true

            },
            deleteDeliveryPlanDetail(row) {
                let index = this.newInvoiceDetails.findIndex(d => d.id == row.id)
                if (index >= 0) {
                    this.newInvoiceDetails.splice(index, 1)
                }
            },
            deliveryPlanAddFinish(planDetail, productDetails) {
                console.log('productDetails------>', productDetails)
                productDetails.forEach(pd => {
                    this.newInvoiceDetails.push(pd)
                })
                this.deliveryPlanAddVisible = false

            },
            editFormCancel() {
                this.$emit('model-edit-cancel')
            },
            wtFormat(row, column, cellValue, index) {
                if (cellValue) {
                    return numeral(cellValue).format('0.000')
                } else {
                    return cellValue
                }

            },
        }
    }
</script>

<style scoped>

</style>
