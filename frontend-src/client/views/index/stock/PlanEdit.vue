<template>
    <div id="plan-edit">
        <el-form :model="editForm" v-if="editForm" ref="editForm" label-width="100px" size="small" :inline="true">
            <el-form-item prop="projectName">
                <el-select v-model="editForm.projectId" placeholder="项目名称" style="width: 150px;" clearable size="mini"
                           @change="changeProject">
                    <el-option
                            v-for="item in projectList"
                            :key="item.id"
                            :label="item.projectName"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-date-picker type="date" size="mini" style="width: 150px;" placeholder="发货日期"
                                v-model="editForm.deliverDate"></el-date-picker>
            </el-form-item>
            <el-form-item>
                <!-- :disabled="this.mode=='update'&&this.disabledPlan==true"-->
                <el-button size="mini" @click="chooseProduct">
                    添加构件
                </el-button>
                <!-- <span v-if="this.mode=='update'&&this.disabledPlan== true" style="color: #F56C6C;font-weight: bold">发货计划已生成发货单，不能修改</span>-->

            </el-form-item>
        </el-form>
        <el-table
                :data="planDetailsInfo"
                size="mini"
                height="400px"
                ref="multipleTable"
                border
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
                    prop="productTypeName"
                    label="构件类型"
                    align="center"
                    width="120"
            >
            </el-table-column>
            <!-- <el-table-column
                     prop="wt"
                     label="构件重量(t)"
                     align="center"
                     width="100">
             </el-table-column>
             <el-table-column
                     prop="totalWt"
                     label="构件总重量(t)"
                     align="center"
                     width="100">
             </el-table-column>-->
            <el-table-column
                    prop="reqNum"
                    label="需求量"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="sendNum"
                    label="已发量"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="inStockNum"
                    label="在库量"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column

                    label="发货量"
                    align="center"
                    width="100">
                <template slot-scope="scope">
                    <el-input v-model="scope.row.num"
                              size="mini" type="number" min="1"
                              :disabled="scope.row.num===scope.row.reqNum"
                              @input="changeNum(scope.row)"></el-input>
                </template>
            </el-table-column>
            <el-table-column
                    label="操作"
                    align="center"
            >
                <template slot-scope="scope">
                    <el-button size="mini" type="danger"
                               :disabled="scope.row.isDisabled==true"
                               round
                               @click="deletePlanDetail(scope.$index)"
                    >删除
                    </el-button>
                    <!--             :disabled="scope.row.invoiceDetails.length>0"-->
                </template>
            </el-table-column>
        </el-table>

        <el-row type="flex" justify="space-around" align="middle" style="margin-top:15px">
            <el-col>

                <!--   :disabled="this.mode=='update' && this.disabledPlan==true "-->
                <el-button type="primary" size="mini"
                           @click="editFormSubmmit('editForm')">确 定
                </el-button>
                <el-button @click="editFormCancel" size="mini">取 消</el-button>
            </el-col>
        </el-row>


        <el-dialog append-to-body
                   title="添加构件"
                   :visible.sync="floorProductChooseVisible"
                   width="70%"
                   v-if="floorProductChooseVisible">
            <floor-product-choose :project-id="editForm.projectId"
                                  :plan-details="planDetailsInfo"
                                  @floor-product-choose-finish="floorProductChooseFinish"></floor-product-choose>
        </el-dialog>


    </div>


</template>
<script>
    import model from '../../../model/model'
    import planModel from '../../../model/plan/plan'
    import projectModel from '../../../model/project/project'
    import FloorProductChoose from './FloorProductChoose.vue'
    import {Notification} from 'element-ui'

    var numeral = require('numeral');

    export default {
        props: ['mode', 'plan'],
        components: {
            'floor-product-choose': FloorProductChoose,
        },
        data() {
            return {
                editForm: {
                    projectId: null,
                    deliverDate: null,
                    planDetails: []
                },
                input: '',
                projectList: [],
                planDetailsInfo: [],
                floorProductChooseVisible: false,
                disabledPlan: false,
                sendRelevanceIds: [],
            }
        },
        mounted() {
            this.init()
        },
        computed: {},
        methods: {
            init() {
                projectModel.projectListS().then(data => {
                    this.projectList = data.entity;
                }).catch(() => {
                    this.$message('加载项目信息失败')
                })
                if (this.mode == 'update') {
                    this.editForm = this.plan
                    console.log('this.editForm.id------>', this.editForm.id)
                    planModel.findFIdInInvoicesByPlanId(this.editForm.id).then((data) => {
                        this.planDetailsInfo = this.plan.planDetails
                        this.sendRelevanceIds = data.entity
                        console.log('this.sendRelevanceIds------>', data.entity)
                        this.planDetailsInfo.forEach(planDetails => {
                            planDetails.isDisabled = false
                            if (this.sendRelevanceIds.indexOf(planDetails.relevanceId) > -1) {
                                planDetails.isDisabled = true
                            }
                        })
                    })
                    //2020-06-02
             /*       planModel.countInvoiceToPlanId(this.editForm.id).then((data) => {
                        if (data.entity > 0) {
                            this.disabledPlan = true
                        }
                    })*/
                }

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
                this.floorProductChooseVisible = true

            },
            //添加的构件已存在 追加发货数
            //添加的构件不存在 新增构件
            /*   floorProductChooseFinish(selectedFloorProducts){
                   selectedFloorProducts.forEach(fp=>{
                       if(!this.editForm.planDetails.some(pd=>pd.relevanceId==fp.id)){
                           let planDetail={}
                           planDetail.num=fp.num-fp.totalPlanNum
                           planDetail.wt=numeral(fp.product.productWt).format('0.000')
                           planDetail.totalWt=numeral(parseFloat(planDetail.num)*parseFloat(planDetail.wt)).format('0.000')
                           planDetail.relevanceId=fp.id
                           planDetail.productId=fp.productId
                           planDetail.product=fp.product
                           planDetail.floorRelevance=fp
                           planDetail.invoiceDetails=[]
                           this.editForm.planDetails.push(planDetail)
                       }
                   })
                   this.floorProductChooseVisible=false
               },*/
            floorProductChooseFinish(selectedFloorProducts) {
                console.log('selectedFloorProducts------>', selectedFloorProducts)
                JSON.parse(JSON.stringify(selectedFloorProducts)).forEach(fp => {
                    //发货量默认为1
                    fp.num = 1
                    this.planDetailsInfo.push(fp)
                })
                this.floorProductChooseVisible = false
                /*  selectedFloorProducts.forEach(fp => {
                      if (!this.editForm.planDetails.some(pd => pd.relevanceId == fp.id)) {
                          let planDetail = {}
                          planDetail.num = fp.num - fp.totalPlanNum
                          planDetail.wt = numeral(fp.productWt).format('0.000')
                          planDetail.totalWt = numeral(parseFloat(planDetail.num) * parseFloat(planDetail.wt)).format('0.000')
                          planDetail.relevanceId = fp.id
                          planDetail.productId = fp.productId,
                              planDetail.productNo = fp.productNo,
                              planDetail.productName = fp.productName,
                              planDetail.buildingId = fp.buildingId,
                              planDetail.buildCode = fp.buildCode,
                              planDetail.floorId = fp.floorId,
                              planDetail.floorNum = fp.floorNum,
                              planDetail.requirements = fp.num,
                              planDetail.product = fp.product
                          planDetail.floorRelevance = fp
                          planDetail.invoiceDetails = []
                          this.editForm.planDetails.push(planDetail)
                      }
                  })*/
            },
            //删除构件
            deletePlanDetail(index) {
                this.$refs.multipleTable.clearSelection()
                this.planDetailsInfo.splice(index, 1)
                // this.editForm.planDetails = this.editForm.planDetails.filter(pd => pd.relevanceId != row.relevanceId)
            },
            changeNum(row) {
                //this.$forceUpdate()
                //row.totalWt = numeral(parseFloat(row.wt) * parseFloat(row.num)).format('0.000')
                let productNum = row.reqNum - row.sendNum;
                if (row.num > productNum) {
                    row.num = 1
                    this.$message('超出构件剩余发货量');
                    return;
                }
            },
            //确定添加构件
            editFormSubmmit() {
                if (this.editForm.projectId == null || this.editForm.projectId == '') {
                    this.$message('请选择项目');
                    return;
                }
                if (this.editForm.deliverDate == null || this.editForm.deliverDate == '') {
                    this.$message('请选择发货日期');
                    return;
                }
                if (this.planDetailsInfo.length == 0) {
                    this.$message('请添加构件');
                    return;
                }
                /*     let numValid = true
                     this.editForm.planDetails.forEach(pd => {
                         let num = parseFloat(pd.num)
                         if (isNaN(num) || num <= 0 || num < pd.invoiceDetails.length) {
                             numValid = false
                         }
                     })
                     if (!numValid) {
                         this.$message('请输入正确的计划发货数量');
                         return;
                     }*/
                /*       let totalNum = 0
                       let totalWt = 0
                       this.editForm.planDetails.forEach(pd => {
                           totalNum += parseFloat(pd.num)
                           totalWt += parseFloat(pd.totalWt)
                       })
                       this.editForm.totalNum = parseInt(totalNum)
                       this.editForm.totalWt = numeral(totalWt).format('0.000')*/
                this.editForm.planDetails = this.planDetailsInfo
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                if (this.mode == 'add') {
                    model.add('plan', this.editForm).then((data) => {
                        if (data.status == 'fail') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        Notification({
                            message: '保存成功',
                            type: 'success'

                        })
                        loading.close()
                        this.$emit('model-edit-finish')
                    })
                }
                if (this.mode == 'update') {
                    model.update('plan', this.editForm).then((data) => {
                        if (data.status == 'fail') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        Notification({
                            message: '保存成功',
                            type: 'success'

                        })
                        loading.close()
                        this.$emit('model-edit-finish')
                    })
                }

            },
            editFormCancel() {
                this.$emit('model-edit-cancel')
            },

        },
        watch: {}

    }
</script>
<style>
    #plan-edit .el-form-item__label {
        font-size: 16px;

    }
</style>
