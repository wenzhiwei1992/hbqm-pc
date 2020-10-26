<template>
    <div>
        <el-form :model="deliveryPlanQuery" v-if="deliveryPlanQuery" ref="invoicesQuery" size="small"
                 :inline="true">
            <!--     label-width="100px"-->
            <el-row>
                <el-row>
                    <el-form-item label="库区">
                        <el-select v-model="deliveryPlanQuery.stockId" size="mini" style="width:150px" clearable
                                   @change="changeStock">
                            <el-option
                                    v-for="item in stocks"
                                    :key="item.id"
                                    :label="item.stockName"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="货架">
                        <el-select v-model="deliveryPlanQuery.shelfId" clearable style="width:150px" size="mini">
                            <el-option
                                    v-for="item in shelves"
                                    :key="item.id"
                                    :label="item.shelfName"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="构件名称" prop="productName">
                        <el-input v-model="deliveryPlanQuery.productName" clearable style="width:150px"
                                  size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="构件编号" prop="productNo">
                        <el-input v-model="deliveryPlanQuery.productNo" clearable style="width:150px"
                                  size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="构件类型" prop="productTypeId">
                        <el-select v-model="deliveryPlanQuery.productTypeId" style="width:150px" clearable
                                   placeholder="请选择">
                            <el-option
                                    v-for="item in arrProductType"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="楼栋" prop="buildId">
                        <el-select style="width:150px" v-model="deliveryPlanQuery.buildId" size="mini" clearable
                                   @change="changeBuildingSelect">
                            <el-option
                                    v-for="item in buildings"
                                    :key="item.id"
                                    :label="item.buildCode"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="楼层" prop="floorId">
                        <el-select style="width:150px" v-model="deliveryPlanQuery.floorId" clearable size="mini">
                            <el-option
                                    v-for="item in floors"
                                    :key="item.id"
                                    :label="item.floorNum"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>

                    <!--     <el-form-item label="发货计划号" prop="projectName">
                             <el-input v-model="invoicesQuery.projectName" size="mini"></el-input>
                         </el-form-item>-->
                    <el-form-item>
                        <el-button type="primary" size="mini" @click="onSubmit">查询</el-button>
                    </el-form-item>
                </el-row>
                <el-row>
                    <el-button type="success" size="mini" @click="add">添加</el-button>
                </el-row>
            </el-row>
        </el-form>
        <el-table
                :data="deliveryPlans"
                style="width:100%"
                :height="GLOBAL.tabelHeight"
                header-row-class-name="header-row"
                @row-click="clickRow"
                size="mini"
                stripe
                border
                tooltip-effect="light"
                @selection-change="handleSelectionChange"
                row-key="id"
                ref="moviesTable"

        >
            <el-table-column
                    type="selection"
                    width="37">
            </el-table-column>
            <el-table-column
                    type="index"
                    label="序号"
                    width="70">
            </el-table-column>
            <el-table-column
                    prop="projectName"
                    label="项目名称"
                    align="center"
                    width="100"
            ></el-table-column>
            <el-table-column
                    prop="buildCode"
                    label="楼栋"
                    align="center"
                    width="150"
            >
                <template slot-scope="scope">
                    <el-select clearable v-model="scope.row.buildCode"
                               @focus="focusBuild(scope.row)"
                               @change="changeBuilding(scope.row)">
                        <el-option
                                v-for="item in buildList"
                                :key="item.id"
                                :label="item.buildCode"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column
                    prop="floorNum"
                    label="楼层"
                    align="center"
                    width="150"
            >
                <!--    @blur="blurFloor(scope.row)"
                :disabled="scope.row.floorId!='0'"
                :disabled="scope.row.buildId!='0'"-->
                <template slot-scope="scope">
                    <el-select clearable @focus="focusFloor(scope.row)"
                               v-model="scope.row.floorNum">
                        <el-option
                                v-for="item in floors"
                                :key="item.id"
                                :label="item.floorNum"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column
                    prop="productNo"
                    label="构件编码"
                    align="center"
                    width="100"
            ></el-table-column>
            <el-table-column
                    prop="productName"
                    label="构件名称"
                    align="center"
                    width="100"
            ></el-table-column>
            <el-table-column
                    prop="productTypeName"
                    label="构件类型"
                    align="center"
                    width="100"
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
                    label="构件方量(m³)"
                    align="center"
                    width="110"
                    :formatter="wtFormat"
            ></el-table-column>
            <el-table-column
                    prop="stockName"
                    label="库区号"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="shelfName"
                    label="货架号"
                    align="center"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="prodTime"
                    label="入库时间"
                    align="center"
                    width="150"
                    :formatter="momentFormatTime"
            ></el-table-column>

        </el-table>
        <el-row type="flex" justify="space-around" align="middle">
            <el-col>

            </el-col>
            <el-col>
                <el-row type="flex" justify="end" align="middle">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="deliveryPlanQuery.pageInfo.currentPage"
                            :page-sizes="deliveryPlanQuery.pageInfo.pageSizes"
                            :page-size="deliveryPlanQuery.pageInfo.pageSize"
                            :total="deliveryPlanQuery.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>

            </el-col>
        </el-row>
    </div>
</template>

<script>
    var numeral = require('numeral')
    import {Notification} from "element-ui";
    import stockModel from '../../../../model/basicdata/stock'
    import shelfModel from '../../../../model/basicdata/shelf'
    import planModel from '../../../../model/plan/plan'
    import buildModel from '../../../../model/project/building'
    import floorModel from '../../../../model/project/floor'
    import productTypeModel from '../../../../model/basicdata/productType'
    import moment from 'moment'

    export default {
        name: "deliveryPlanAdd",
        props: ['newInvoiceDetails', 'projectId'],
        data() {
            return {
                deliveryPlanQuery: {
                    projectId: '',
                    productName: '',
                    productNo: '',
                    stockId: '',
                    shelfId: '',
                    buildId: '',
                    floorId: '',
                    productTypeId: '',
                    pageName: this.$store.state.menuActiveName,//存放当前页面的名称用于后端api区分调用
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                //查询下拉框赋值
                stocks: [],
                shelves: [],
                projects: [],
                deliveryPlans: [],
                selected: [],
                buildList: [],
                floors: [],
                value1: [],
                arrProductType: [],
                buildings: [],

            }
        },
        mounted() {
            this.info()
            this.list()
        },
        methods: {
            info() {
                shelfModel.getStocks().then(data => {
                    this.stocks = data.entity;
                    this.stocks = this.stocks.filter(s => s.stockStatus == '启用' && s.stockTypeCode == '成品仓库')
                }).catch(err => {
                })

                buildModel.findByProjectId(this.projectId).then(data => {
                    this.buildings = data.entity
                })
                //2019年8月15日16:40:24 李强 修改为单基地
                productTypeModel.getOrgProductTypes().then((data) => {
                    this.arrProductType = data.entity
                })
            },
            // 点击整行都可以进行勾选
            clickRow(row) {
                this.$refs.moviesTable.toggleRowSelection(row)
            },
            focusBuild(row) {
                this.buildList = []
                let model = {
                    projectId: row.projectId,
                    productId: row.productId,
                }
                buildModel.findBuildingsByProjectIdAndProductId(model).then((data) => {
                    this.buildList = data.entity
                })
            },
            focusFloor(row) {

                this.floors = []
                let model = {
                    projectId: row.projectId,
                    productId: row.productId,
                    buildId: row.buildCode
                }
                //只改楼层操作执行
                let valueBuildCode = typeof row.buildCode
                if (valueBuildCode == 'string') {
                    model.buildId = row.buildId
                }
                if (row.buildCode != '' && row.projectId != '' && row.productId != '') {
                    floorModel.findFloorsByProjectIdAndProductIdAndBuildId(model).then((data) => {
                        this.floors = data.entity
                    })
                }
            },
            blurFloor(row) {

            },
            onSubmit() {
                this.list()
            },
            list() {
                this.deliveryPlanQuery.projectId = this.projectId
                planModel.findInvoiceNewList(this.deliveryPlanQuery).then((data) => {
                    let list = data.entity.list
                    list = list.filter(pd => {
                        return !this.newInvoiceDetails.some(d => d.productDetailId == pd.productDetailId)
                    })
                    this.deliveryPlans = list
                    let total = 0;
                    if (data.entity.list != null && list != null) {
                        total = data.entity.list.length - list.length
                    }
                    this.deliveryPlanQuery.pageInfo.total = data.entity.total - total
                })
            },
            changeBuilding(row) {
                this.floors = []
                row.floorNum = ''
                /*    floorModel.findByBuildingIdAndProjectId(row.buildCode, row.projectId).then((data) => {
                        this.floors = data.entity
                    })*/
            },
            changeBuildingSelect(id) {
                if (id == undefined) {
                    id = '0'
                }
                this.deliveryPlanQuery.floorId = ''
                this.floors = []
                // let building = this.buildings.find(b => b.id == id)
                floorModel.findByBuildingIdAndProjectId(id, this.projectId).then((data) => {
                    this.floors = data.entity
                    this.deliveryPlanQuery.floorId = ''
                })
            },
            add() {
                if (this.selected.length == 0) {
                    this.$message(this.GLOBAL.PLAN_MSG.NOT_SELECT);
                    return;
                }
                let num = 0;
                this.selected.forEach(pd => {
                    if ((pd.buildCode !== '' && pd.floorNum === '') || (pd.buildCode === '' && pd.floorNum !== '') || (pd.buildCode === '' && pd.floorNum === '')) {
                        num++;
                    }
                })
                if (num > 0) {
                    this.$message(this.GLOBAL.PLAN_MSG.NOT_BANDF_SELECT);
                    return;
                }
                let deciceInfo = {
                    invoiceDetailList: this.selected,
                    modeDecidePlan: this.GLOBAL.STATUS.ADD,
                }
                console.log('deciceInfo--->', deciceInfo)
                planModel.loadFloorRelevanceIdAndDecideByPlan(deciceInfo).then((data) => {
                    if (data.status == this.GLOBAL.STATUS.FAILED) {
                        Notification({
                            message: data.msg,
                            type: "warning"
                        });
                        return;
                    }
                    this.loadInfo(this.selected)
                    this.$emit('delivery-plan-add-finish', null, this.selected)
                })
            },
            /**
             * 重新加载楼栋楼层数据
             * @param select
             */
            loadInfo(select) {
                select.forEach(pd => {
                    if (pd.buildCode == '' || pd.floorNum == '') {

                    } else if (pd.buildId == 0 && pd.floorId == 0 && pd.buildCode != '' && pd.floorNum != '') {//后台API赋予的0值，为0时是通过构件排产下的数据，楼栋楼层为空
                        buildModel.buildingLoad(pd.buildCode).then((data) => {
                            pd.buildId = data.entity.id
                            pd.buildCode = data.entity.buildCode
                        })
                        floorModel.floorLoad(pd.floorNum).then((dataFloor) => {
                            pd.floorId = dataFloor.entity.id
                            pd.floorNum = dataFloor.entity.floorNum
                        })
                        //楼栋楼层生产下的构件事先会绑定楼栋楼层；此方法为当新建或编辑发货单时重新选择楼栋楼层执行
                    } else {
                        let valueBuildCode = typeof pd.buildCode
                        let valueFloorNum = typeof pd.floorNum
                        if (valueBuildCode == 'number' && valueFloorNum == 'number') {
                            buildModel.buildingLoad(pd.buildCode).then((data) => {
                                pd.buildId = data.entity.id
                                pd.buildCode = data.entity.buildCode
                            })
                            floorModel.floorLoad(pd.floorNum).then((dataFloor) => {
                                pd.floorId = dataFloor.entity.id
                                pd.floorNum = dataFloor.entity.floorNum
                            })
                            //只改楼层操作执行
                        } else if (valueBuildCode == 'string' && valueFloorNum == 'number') {
                            floorModel.floorLoad(pd.floorNum).then((dataFloor) => {
                                pd.floorId = dataFloor.entity.id
                                pd.floorNum = dataFloor.entity.floorNum
                            })
                        }
                    }
                })
            },
            changeStock(id) {
                //防止清楚库区时报错
                if (id == '' || id == undefined) {
                    id = 0
                }
                stockModel.getShelfsById(id).then(data => {
                    this.shelves = data.entity
                    this.shelves = this.shelves.filter(s => s.shelfStatus == '启用' && s.fullStatus == '未满')
                    this.deliveryPlanQuery.shelfId = ''
                })
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
            handleSizeChange(val) {
                this.deliveryPlanQuery.pageInfo.currentPage = 1
                this.deliveryPlanQuery.pageInfo.pageSize = val
                this.list()
            },
            handleCurrentChange(val) {
                this.deliveryPlanQuery.pageInfo.currentPage = val
                this.list()
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },

        }
    }
</script>

<style scoped>

</style>