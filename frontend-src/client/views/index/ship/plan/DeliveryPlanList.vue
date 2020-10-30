<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true">
                            <el-form-item>
                                <el-input
                                        v-model="entity.planNo"
                                        placeholder="发货计划号"
                                        style="width: 150px"
                                        size="mini"
                                        clearable>
                                </el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-select v-model="entity.projectId" placeholder="项目" style="width: 150px" size="mini" clearable>
                                    <el-option
                                            v-for="item in projectList"
                                            :key="item.id"
                                            :label="item.projectName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item>
                                <el-input
                                        placeholder="构件名称"
                                        v-model="entity.productName"
                                        style="width: 150px"
                                        size="mini"
                                        clearable>
                                </el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-date-picker
                                        style="width: 250px"
                                        v-model="entity.deliverDateRange"
                                        type="daterange"
                                        start-placeholder="计划开始日期"
                                        end-placeholder="计划结束日期"
                                        :default-time="['00:00:00', '23:59:59']">
                                </el-date-picker>
                            </el-form-item>
                        </el-form>
                    </el-col>
                    <el-col>
                        <my-search-button-group :model='searchButtonData'></my-search-button-group>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col>
                        <my-button-group :model="buttonData"></my-button-group>
                    </el-col>
                    <el-col>
                        <div>
                            <span class="color_font_span_class">总重量(t):</span>
                            <span class="color_span_class">{{productWtsNum}}</span>
                        </div>
                        <div>
                            <span class="color_font_span_class">总方量(m³):</span>
                            <span class="color_span_class">{{productVolsNum}}</span>
                        </div>
                    </el-col>
                </el-row>
            </el-header>
            <el-main
                ref="mainContent"
                :style="`height:${this.$store.state.mainContentHeight}px`"
            >
                <el-table
                :data="entities"
                @selection-change="handleSelectionChange"
                ref="handleClickRowSelection planTable"
                height="100%"
                style="width:100%"
                header-row-class-name="header-row"
                highlight-current-row
                row-class-name="table-row"
                stripe
                border
                tooltip-effect="light"
                row-key="id"
                @expand-change="showSubData"
                :expand-row-keys="expands"

                >
                    <el-table-column type="expand">
                        <template slot-scope="props">
                            <el-table
                                    style="width: 100%"
                                    :data="subDatas"
                                    size="mini"

                            >
                                <el-table-column
                                        type="index"
                                        label="序号"
                                        width="70">
                                </el-table-column>
                                <el-table-column
                                    prop="stockNo"
                                    label="库区"
                                    align="c1nter"
                                    width="100"
                            ></el-table-column>
                                <el-table-column
                                        prop="shelfNo"
                                        label="货架"
                                        align="center"
                                        width="110"
                                ></el-table-column>

                                <el-table-column
                                        prop="buildCode"
                                        label="楼栋"
                                        align="c1nter"
                                        width="100"
                                ></el-table-column>
                                <el-table-column
                                        prop="floorNum"
                                        label="楼层"
                                        align="center"
                                        width="110"
                                ></el-table-column>
                                <el-table-column
                                        prop="productNo"
                                        label="构件编码"
                                        align="center"
                                        width="110"
                                ></el-table-column>
                                <el-table-column
                                        prop="productName"
                                        label="构件名称"
                                        align="center"
                                        width="110"
                                ></el-table-column>
                                <el-table-column
                                        prop="productWt"
                                        label="构件重量(t)"
                                        align="center"
                                        width="110"
                                        :formatter="wtFormat"
                                ></el-table-column>
                                <el-table-column
                                        prop="projectConcrete"
                                        label="砼方量(m³)"
                                        align="center"
                                        width="110"
                                        :formatter="wtFormat"
                                ></el-table-column>
                                <el-table-column
                                        prop="productVol"
                                        label="构件方量"
                                        align="center"
                                        width="110"
                                        :formatter="wtFormat"
                                ></el-table-column>
                                <el-table-column
                                        prop="productStatus"
                                        label="构件状态"
                                        align="center"
                                        width="110"
                                ></el-table-column>
                                <el-table-column
                                        prop="invoiceNo"
                                        label="发货单"
                                        align="center"
                                        width="110"
                                ></el-table-column>
                                <!--           <el-table-column
                                                prop="productReqId"
                                                label="是否完成"
                                                align="center"
                                                width="100"
                                        >
                                            <template slot-scope="scope">
                                                <span v-if="scope.row.productReqId=='0'"
                                                        style="color: #F56C6C;font-weight: 800;">未完成</span>
                                                <span v-else style="color: #67C23A;font-weight:800;">已完成</span>
                                            </template>
                                        </el-table-column>-->

                            </el-table>
                        </template>
                    </el-table-column>
                    <el-table-column
                            type="index"
                            label="序号"
                            width="70">
                    </el-table-column>
                    <el-table-column
                            prop="planNo"
                            label="发货计划号"
                            align="center"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            prop="deliverDate"
                            label="发货日期"
                            align="center"
                            :formatter="momentFormatDate"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            prop="planStatus"
                            label="计划状态"
                            align="center"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            prop="project.projectName"
                            label="项目名称"
                            align="center"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            prop="productDetailNum"
                            label="总计划件数"
                            align="center"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            prop="numShipped"
                            label="已发件数"
                            align="center"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            prop="numShippedNot"
                            label="未发件数"
                            align="center"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            prop="productWtsNum"
                            label="总重量(t)"
                            align="center"
                            width="100"
                            :formatter="wtFormat"
                    ></el-table-column>
                    <el-table-column
                            prop="productConcretesNum"
                            label="砼方量"
                            align="center"
                            width="100"
                            :formatter="wtFormat"
                    ></el-table-column>
                    <el-table-column
                            prop="productVolsNum"
                            label="总方量(m³)"
                            align="center"
                            width="100"
                            :formatter="wtFormat"
                    ></el-table-column>
                    <el-table-column
                            prop="createdBy"
                            label="创建人"
                            align="center"
                            width="150">
                    </el-table-column>
                    <el-table-column
                            prop="dateCreated"
                            label="创建时间"
                            align="center"
                            width="150"
                            :formatter="formatterDate">
                    </el-table-column>
                    <!--       <el-table-column
                                    prop="totalWt"
                                    label="总重量(t)"
                                    align="center"
                                    width="100">
                            </el-table-column>-->


                    <el-table-column
                            label="操作"
                            align="center"
                            fixed="right"
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
                        :current-page="pageInfo.currentPage"
                        :page-sizes="pageInfo.pageSizes"
                        :page-size="pageInfo.pageSize"
                        :total="pageInfo.total"
                        layout="total, sizes, prev, pager, next, jumper"
                        background
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
        </el-container>
        <el-dialog :title="mode=='add'?'新建发货计划':'编辑发货计划'" :visible.sync="editFormVisible" v-if="editFormVisible"
                   width="80%">
            <delivery-plan-edit :mode="mode" :plan="plan"
                                @model-edit-finish="deliveryPlanEditFinish"
                                @model-edit-cancel="deliveryPlanEditCancel"

            ></delivery-plan-edit>
        </el-dialog>
    </div>
</template>

<script>
    import projectModel from '../../../../model/project/project'
    import {Notification} from "element-ui";

    var numeral = require('numeral')
    import model from '../../../../model/model'
    import planModel from '../../../../model/plan/plan'
    import DeliveryPlanEdit from './DeliveryPlanEdit.vue'
    import moment from 'moment'

    export default {
        name: "deliveryPlanList",
        components: {
            'delivery-plan-edit': DeliveryPlanEdit,

        },
        data() {
            return {
                //表查询
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: this.GLOBAL.pageSize,
                    pageSizes: this.GLOBAL.pageSizes
                },
                entity: {
                    apiType:'pc',
                    planNo: '',
                    productName: '',
                    projectId: '',
                    deliverDateRange: ['', '']
                },
                projectList: [],
                entities: [],
                //行操作
                selected: [],
                planId: null,
                invoiceListVisible: false,
                //编辑表单
                mode: null,
                editFormVisible: false,
                plan: null,
                expands: [],
                subDatas: [],
                productWtsNum: '',
                productConcretesNum: '',
                productVolsNum:'',
                buttonData:[
                    {
                        name:'新建',
                        event:'addClick'
                    }
                ],
                searchButtonData:[
                    {
                        name:'查询',
                        event:'query'
                    }
                ],
                rowButtonData:[
                    {
                        name:'编辑',
                        event:'editClick'
                    },
                    {
                        name:'删除',
                        event:'deleteClick',
                        disabled:"scope.row.totalInvoiceNum>0"
                    },
                ]
            }
        },
        mounted() {
            this.list()
            this.info()
            this.loadNums()
        },
        methods: {
            list() {
                planModel.listPlan(this.entity, this.pageInfo).then(data => {
                    let entity = data.entity
                    let list = data.entity.content;
                    this.entities = list
                    this.pageInfo.total = data.entity.totalElements
                })
            },
            showSubData(row) {
                this.subDatas = []
                if (this.expands.some((e) => e == row.id)) {
                    this.expands = []
                } else {
                    this.expands = []
                    this.expands.push(row.id)
                }
                planModel.findPlanDetailByPlanId(row.id).then((data) => {
                    this.subDatas = data.entity
                })

            },

            query() {
                this.list()
                this.loadNums()
            },
            loadNums(){
                planModel.sumProductWtAndProductConcreteNumToPlan(this.entity).then((data)=>{
                    this.productWtsNum = this.numeralS(data.entity.productWtsNum)
                    this.productVolsNum = this.numeralS(data.entity.productVolsNum)
                })
            },
            info() {
                projectModel.getExternalAllProject().then(data => {
                    this.projectList = data.entity;
                }).catch(() => {
                    this.$message('加载项目信息失败')
                })

            },
            editClick(row) {
                this.plan = row
                this.mode = this.GLOBAL.STATUS.UPDATE
                planModel.findPlanDetailByPlanId(row.id).then((data) => {
                    this.plan.planDetails = data.entity
                    this.editFormVisible = true
                })
            },
            deleteClick(row) {
                this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
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
                    planModel.deleteNew(row.id).then(data => {
                        if (data.status == this.GLOBAL.STATUS.FAIL) {
                            Notification({
                                message: data.msg,
                                type: this.GLOBAL.STATUS.WARNING
                            });
                            loading.close()
                            return;
                        }
                        Notification({
                            message: this.GLOBAL.MSG.DELETE_SUCCESS,
                            type: this.GLOBAL.STATUS.SUCCESS
                        });
                        loading.close();
                        this.list()
                        this.loadNums()
                    })
                }).catch(() => {
                })
            },
            addClick() {
                this.mode = this.GLOBAL.STATUS.ADD
                this.editFormVisible = true
            },
            //编辑表单
            deliveryPlanEditFinish() {
                this.editFormVisible = false
                this.list()
                this.loadNums()
            },
            deliveryPlanEditCancel() {
                this.editFormVisible = false
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.pageInfo.currentPage = 1
                this.pageInfo.pageSize = val
                this.list()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val
                this.list()
            },
            momentFormatDate(row, column, cellValue, index) {
                if (cellValue) {
                    return moment(cellValue).format('YYYY-MM-DD')
                } else {
                    return cellValue
                }

            },
            momentFormatTime(row, column, cellValue, index) {
                if (cellValue) {
                    return moment(cellValue).format('YYYY-MM-DD HH:mm:ss')
                } else {
                    return cellValue
                }

            },
            wtFormat(row, column, cellValue, index) {
                if (cellValue) {
                    return numeral(cellValue).format('0.000')
                } else {
                    return cellValue
                }

            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            numeralS(num){
                return numeral(num).format('0.000')
            },
        }
    }
</script>

<style scoped>
    .color_span_class {
        color: #67C23A;
        font-weight: 800;
    }
    .color_font_span_class{
        font-weight: 800;
    }
    .float_class{
        display: block;
        float: right;

    }
</style>