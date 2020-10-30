<template>
    <div>
        <el-form inline :model="editForm" v-if="editForm" ref="editForm" label-position="left" :disabled="showAll">
            <el-form-item label="产线" prop="lineId">
                <el-select v-model="editForm.lineId" clearable @change="loadTeanInfo">
                    <el-option v-for="item in lines"
                               :key="item.id"
                               :label="item.lineName"
                               :value="item.id"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="班组" prop="teamId">
                <el-select v-model="editForm.teamId" clearable>
                    <el-option v-for="item in teamInfos"
                               :key="item.id"
                               :label="item.teamName"
                               :value="item.id"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="生产时间" prop="woDate">
                <el-date-picker
                        v-model="editForm.woDate"
                        type="date"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="选择日期"
                >
                </el-date-picker>
            </el-form-item>
            <el-form-item label="总砼方量(m³):">
                <span>{{totalConcrete}}</span>
            </el-form-item>
            <div style="margin-bottom:10px">
            </div>
            <el-table
                    :data="editForm.productData"
                    height="500"
                    stripe
                    border>
                <el-table-column
                        prop="project.projectName"
                        label="项目名称">
                </el-table-column>
                <el-table-column
                        prop="product.productNo"
                        label="构件编码">
                </el-table-column>
                <el-table-column
                        prop="product.productName"
                        label="构件名称">
                </el-table-column>
                <el-table-column
                        prop="product.projectConcrete"
                        label="砼方量(m³)">
                </el-table-column>
                <el-table-column
                        prop="floorRelevance.building.buildCode"
                        label="楼栋">
                </el-table-column>
                <el-table-column
                        prop="floorRelevance.floor.floorNum"
                        label="楼层">
                </el-table-column>
                <el-table-column
                        prop="plateId"
                        label="模台号">
                    <template slot-scope="scope">
                        <el-select filterable v-model="scope.row.plateId">
                            <el-option
                                    v-for="item in plates"
                                    :key="item.id"
                                    :label="item.plateNo"
                                    :value="item.id"
                            >
                            </el-option>
                        </el-select>
                    </template>
                </el-table-column>

                <el-table-column
                        align="left"
                        fixed="right"
                        label="是否逾期">
                    <template slot-scope="scope">
                        <el-popover trigger="hover" placement="top">
                            <p>是否逾期</p>
                            <div slot="reference">
                                <el-tag size="medium" :class="scope.row.isOver ?'green' :'red' "> {{
                                    scope.row.isOver ?'否' :'是' }}
                                </el-tag>
                            </div>
                        </el-popover>
                    </template>
                </el-table-column>
            </el-table>
        </el-form>
    </div>
</template>

<script>
    import workOrder from '../../../model/po/workOrder'
    import combModel from '../../../model/po/comb'
    import orderModel from '../../../model/po/order'
    import plateModel from '../../../model/basicdata/plate'
    import {Notification} from 'element-ui'
    import moment from "moment";

    export default {
        props: ['detailId'],
        data() {
            return {

                editForm: {
                    teamId: '',
                    woDate: '',
                    productData: [],
                },
                /*projectConcrete:'0.000',*/
                showAll: true,
                mode: 'add',
                loading: false,
                teamInfos: [],
                addDataVisible: false,
                datasForDialog: [], //弹出框构件集合
                selectedForDialog: [], //弹出框选择项集合
                selected: [], //主表单选择项集合
                projectList: [], // 项目集合
                condition: { //查询条件
                    orderId: '',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: 100,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                orderDatas: [],
                floorNumArr: [],
                buildCodeArr: [],
                plates: [],
                totalConcrete: '0.000',
                lines: [],
                types: [],//构件类型
                addedId: []
            }
        },
        mounted() {
            workOrder.findAllProjectList().then(data => {
                this.projectList = data.entity;
            }).catch(() => {

            })
            workOrder.loadAllLines().then(data => {
                this.lines = data.entity;
            })

            let id = this.detailId;
            //id不为空则是编辑
            this.mode = 'update';
            this.loadWorkById(id);

            plateModel.getAllPlates().then((data) => {
                this.plates = data.entity
            })
        },
        methods: {
            /**
             * 加载班组
             */
            loadTeanInfo() {
                if (!this.editForm.lineId) {
                    return;
                }
                this.editForm.teamId = '';
                workOrder.getTeamInfoById(this.editForm.lineId).then(data => {
                    this.teamInfos = data.entity;
                });
            },
            loadWorkById(id) {
                workOrder.loadWorkById(id).then(data => {
                    data.entity.productData.forEach(val => {
                        val.product.projectConcrete = Number(val.product.projectConcrete).toFixed(3)
                    })
                    console.log(" data.entity", data.entity)
                    this.editForm = data.entity;
                    if (!this.editForm.lineId) {
                        return;
                    }

                    let totalConcrete = "0.00";
                    for (let i = 0; i < data.entity.productData.length; i++) {
                        totalConcrete = Number(totalConcrete) + Number(data.entity.productData[i].product.projectConcrete)
                    }
                    this.totalConcrete = Number(totalConcrete).toFixed(3);

                    workOrder.getTeamInfoById(this.editForm.lineId).then(data => {
                        this.teamInfos = data.entity;
                    });
                }).catch(err => {
                    console.error("err", err)
                })
            },
        }
    }

</script>

<style scoped>

    .red {
        color: red;
    }

    /* .el-tag--medium{
        color: red;
    } */

    tbody tr.stopclick {
        color: rgba(0, 0, 0, 0.2);
        background: #fff;
        /* text-decoration:line-through; */
    }
</style>