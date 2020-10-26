<template>
    <div id="floor-product-choose">
        <el-form :inline="true" id="entity" :model="entity" ref="entity">
            <el-form-item label="库区">
                <el-select v-model="entity.stockId" size="mini" style="width:110px"  clearable @change="changeStock">
                    <el-option
                            v-for="item in stocks"
                            :key="item.id"
                            :label="item.stockName"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="货架">
                <el-select v-model="entity.shelfId" clearable style="width:110px"  size="mini">
                    <el-option
                            v-for="item in shelves"
                            :key="item.id"
                            :label="item.shelfName"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="楼栋" prop="buildingId">
                <el-select style="width:110px" v-model="entity.buildingId" size="mini" clearable
                           @change="changeBuilding">
                    <el-option
                            v-for="item in buildings"
                            :key="item.id"
                            :label="item.buildCode"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="楼层" prop="floorId">
                <el-select style="width:110px" v-model="entity.floorId" clearable size="mini">
                    <el-option
                            v-for="item in floors"
                            :key="item.id"
                            :label="item.floorNum"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="构件名称" prop="productName">
                <el-input
                        placeholder="请输入内容"
                        v-model="entity.productName"
                        style="width:110px"
                        clearable>
                </el-input>
            </el-form-item>

            <el-form-item>
                <el-button size="mini" icon="el-icon-search" @click="query()">查找</el-button>
            </el-form-item>
        </el-form>
        <el-table
                :data="entities"
                @selection-change="handleSelectionChange"
                size="mini"
        >
            <el-table-column
                    type="selection">
            </el-table-column>
            <el-table-column
                    type="index"
                    label="序号"
                    width="50">
            </el-table-column>
<!--            <el-table-column-->
<!--                    prop="detailNo"-->
<!--                    label="构件明细编号"-->
<!--                    align="center"-->
<!--                    width="120">-->
<!--            </el-table-column>-->
            <el-table-column
                    prop="floorRelevance.building.buildCode"
                    label="楼栋"
                    align="center"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="floorRelevance.floor.floorNum"
                    label="楼层"
                    align="center"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="product.productNo"
                    label="构件编码"
                    align="center"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="product.productName"
                    label="构件名称"
                    align="center"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="stock.stockNo"
                    label="库区号"
                    align="center"
                    width="100"
            >
            </el-table-column>
            <el-table-column
                    prop="shelf.shelfNo"
                    label="货架号"
                    align="center"
                    width="100"
            >
            </el-table-column>
            <el-table-column
                    prop="inRecord.execDate"
                    label="入库日期"
                    align="center"
                    width="200"
                    :formatter="momentFormat"
            >
            </el-table-column>
            <!--        <el-table-column
                            prop="inRecord.operater"
                            label="入库操作人"
                            align="center"
                            width="100"
                    ></el-table-column>-->

        </el-table>
        <el-row type="flex" justify="space-around" align="middle" style="margin-top:15px">
            <el-col>
                <el-button @click="add" :disabled="this.selectedProductDetails.length==0">确定</el-button>
            </el-col>
        </el-row>

    </div>


</template>
<script>
    import model from '../../../model/model'
    import stockModel from '../../../model/basicdata/stock'
    import shelfModel from '../../../model/basicdata/shelf'
    import buildingModel from '../../../model/project/building'
    import floorModel from '../../../model/project/floor'

    import moment from 'moment'

    export default {
        props: ['planDetail', 'repeatProductDetailIds'],
        data() {
            return {
                //表查询
                entity: {
                    stockId: null,
                    shelfId: null,
                    buildingId:'',
                    floorId:'',
                    productName:'',
                },
                entities: [],
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: this.GLOBAL.pageSize,
                    pageSizes: this.GLOBAL.pageSizes
                },
                stocks: [],
                shelves: [],
                selectedProductDetails: [],
                buildings: [],
                floors: [],
                isShowProductName: false,//判断是否选中构件名称筛选框
                options: [
                    {
                        value: '',
                        label: ''
                    }, {
                        value: '1',
                        label: '模糊'
                    }, {
                        value: '2',
                        label: '精确'
                    },],
                valueBD: '1'
            }
        },
        mounted() {
            this.init()
            this.showBuilding()
        },
        computed: {},
        methods: {
            init() {
                shelfModel.getStocks().then(data => {
                    this.stocks = data.entity;
                    this.stocks = this.stocks.filter(s => s.stockStatus == '启用' && s.stockTypeCode == '成品仓库')
                }).catch(err => {
                })
                this.list()

            },
            list() {
                this.entity.productStatus = '成品入库'
                this.entity.productId = this.planDetail.productId
                //发货改为发货当前楼栋楼层（待确认）
                // this.entity.floorRelevanceId = this.planDetail.relevanceId
                this.entity.listType = 'stock'
                model.list('productDetail', this.entity, this.pageInfo).then(data => {
                    let entity = data.entity
                    let list = data.entity.content;

                    list = list.filter(pd => {
                        return !this.planDetail.newInvoiceDetails.some(d => d.productDetail.id == pd.id)
                    })
                    //用于新建发货单未保存是构件明细数据去重
                    if (this.repeatProductDetailIds.length > 0) {
                        list = list.filter(pd => {
                            return !this.repeatProductDetailIds.some(d => d == pd.id)
                        })
                    }
                    list.forEach((pd) => {
                        let recordList = pd.recordList
                        if (recordList != null && recordList.length > 0) {
                            let inRecord = recordList.find((record) => {
                                return record.inOutType == '入库'
                            })
                            pd.inRecord = inRecord
                        }
                    })
                    this.entities = list
                    this.pageInfo.total = data.entity.totalElements
                })
            },
            momentFormat(row, column, cellValue, index) {
                if (cellValue) {
                    return moment(cellValue).format('YYYY-MM-DD HH:mm:ss')
                } else {
                    return cellValue
                }

            },
            changeStock(id) {
                //防止清楚库区时报错
                if (id == '' || id == undefined) {
                    id = 0
                }
                stockModel.getShelfsById(id).then(data => {
                    this.shelves = data.entity
                    this.shelves = this.shelves.filter(s => s.shelfStatus == '启用' && s.fullStatus == '未满')
                    this.entity.shelfId = ''
                })
            },
            query() {
                this.list()
            },
            add() {
                if (this.selectedProductDetails.length == 0) {
                    this.$message('请选择添加项');
                    return;
                }
                console.log('this.selectedProductDetails------>', this.selectedProductDetails)
                this.$emit('product-detail-choose-finish', this.planDetail, this.selectedProductDetails)

            },
            showBuilding() {
                console.log('this.planDetail------>', this.planDetail)
                console.log('repeatProductDetailIds------>', this.repeatProductDetailIds)

                buildingModel.findByProjectId(this.planDetail.floorRelevance.projectId).then(data => {
                    this.buildings = data.entity
                })
            },
            changeBuilding(id) {
                this.entity.floorId = ''
                this.floors = []
                let building = this.buildings.find(b => b.id == id)

                function compare(property) {
                    return function (a, b) {
                        var value1 = a[property];
                        var value2 = b[property];
                        return value1 - value2;
                    }
                }

                floorModel.findByBuildingIdAndProjectId(building.id, building.projectId).then((data) => {
                    this.floors = data.entity
                    this.entity.floorId = ''
                })
                // this.floors=building.floors.sort(compare('floorNum'))
            },
            changeProductNameInput(id) {
                if (id != null) {
                    this.isShowProductName = true
                }
            },
            handleSelectionChange(selected) {
                this.selectedProductDetails = selected
            },

        },
        watch: {
            projectId: function () {
                this.init()
            }
        }

    }
</script>
<style>
    #floor-product-choose .el-form-item__label {
        font-size: 16px;

    }
</style>