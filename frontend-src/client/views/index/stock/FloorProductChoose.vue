<template>
    <div id="floor-product-choose">
        <el-form
                :inline="true"
                id="entity"
                :model="entity"
                ref="entity"
        >
            <!--<el-form-item label="户型" prop="houseTypeId">
                      <el-select style="width:110px" v-model="entity.houseTypeId" size="mini" clearable
                                 @change="changeHouseType">
                          <el-option
                                  v-for="item in houseTypes"
                                  :key="item.id"
                                  :label="item.name"
                                  :value="item.id">
                          </el-option>
                      </el-select>
                  </el-form-item>-->
            <el-form-item prop="buildingId">
                <el-select
                        style="width:110px"
                        placeholder="楼栋"
                        v-model="entity.buildingId"
                        size="mini"
                        clearable
                        @change="changeBuilding"
                >
                    <el-option
                            v-for="item in buildings"
                            :key="item.id"
                            :label="item.buildCode"
                            :value="item.id"
                    >
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="floorId">
                <el-select
                        style="width:110px"
                        placeholder="楼层"
                        :disabled="entity.buildingId==''"
                        v-model="entity.floorId"
                        clearable
                        size="mini"
                >
                    <el-option
                            v-for="item in floors"
                            :key="item.id"
                            :label="item.floorNum"
                            :value="item.id"
                    >
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="productTypeIds">
                <el-select v-model="entity.productTypeIds"
                           multiple
                           collapse-tags
                           filterable
                           placeholder="构件类型"
                           style="width:110px">
                    <el-option
                            v-for="item in productTypes"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="productNo">
                <el-input
                        placeholder="构件编码"
                        v-model="entity.productNo"
                        style="width:110px"
                        clearable
                >
                </el-input>
            </el-form-item>
            <el-form-item prop="productName">
                <el-select
                        v-model="valueBD"
                        placeholder="类型"
                        style="width: 70px"
                        collapse-tags
                        @change="changeProductNameInput"
                >
                    <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                    >
                    </el-option>
                </el-select>
                <el-input
                        placeholder="构件名称"
                        :disabled="valueBD==''"
                        v-model="entity.productName"
                        style="width:110px"
                        clearable
                >
                </el-input>
            </el-form-item>
            <el-form-item>
                <el-select
                        v-model="entity.inStockNum"
                        placeholder="在库量"
                        style="width:110px"
                        collapse-tags
                >
                    <el-option
                            v-for="item in optionsNum"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                    >
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button
                        size="mini"
                        icon="el-icon-search"
                        @click="query()"
                ></el-button>
                <el-button
                        plain
                        icon="el-icon-refresh"
                        @click="resetForm('entity')"
                ></el-button>
            </el-form-item>
        </el-form>
        <el-button
                @click="add"
                :disabled="this.selectedFloorProducts.length==0"
        >确定
        </el-button>
        <el-table
                :data="floorProducts"
                @selection-change="handleSelectionChange"
                size="mini"
                height="350"
                @row-click="clickRow"
                border
                ref="moviesTable"
        >
            <el-table-column type="selection" :selectable="checkSelectable">
            </el-table-column>
            <el-table-column
                    type="index"
                    label="序号"
                    width="70"
            >
            </el-table-column>
            <el-table-column
                    prop="productNo"
                    label="构件编码"
                    align="center"
                    width="120"
            >
            </el-table-column>
            <el-table-column
                    prop="productName"
                    label="构件名称"
                    align="center"
                    width="120"
            >
            </el-table-column>
            <el-table-column
                    prop="productTypeName"
                    label="构件类型"
                    align="center"
                    width="120"
            >
            </el-table-column>
            <el-table-column
                    prop="buildCode"
                    label="楼栋"
                    align="center"
                    width="120"
            >
            </el-table-column>
            <el-table-column
                    prop="floorNum"
                    label="楼层"
                    align="center"
                    width="120"
            >
            </el-table-column>
            <!--  <el-table-column
                            prop="productWt"
                            label="构件重量(t)"
                            align="center"
                            width="120">
                    </el-table-column>-->
            <el-table-column
                    prop="reqNum"
                    label="需求量"
                    align="center"
                    width="120"
            >
            </el-table-column>
            <el-table-column
                    prop="sendNum"
                    label="已发量"
                    align="center"
                    width="120"
            >
            </el-table-column>
            <el-table-column
                    prop="inStockNum"
                    label="在库量"
                    align="center"
                    width="120"
            >
            </el-table-column>
        </el-table>

        <el-row
                type="flex"
                justify="space-around"
                align="middle"
                style="margin-top:15px"
        >

            <el-col>
                <el-row
                        type="flex"
                        justify="end"
                        align="middle"
                >
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="entity.pageInfo.currentPage"
                            :page-sizes="entity.pageInfo.pageSizes"
                            :page-size="entity.pageInfo.pageSize"
                            :total="entity.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    ></el-pagination>
                </el-row>
            </el-col>
        </el-row>
    </div>

</template>
<script>
    import buildingModel from '../../../model/project/building'
    import floorRelevanceModel from '../../../model/project/floorRelevance'
    import floorModel from '../../../model/project/floor'
    import houseTypeModel from '../../../model/project/houseType'
    import productModel from '../../../model/project/product'
    import productTypeModel from "../../../model/basicdata/productType";

    export default {
        props: ['projectId', 'planDetails'],
        data() {
            return {
                entity: {
                    buildingId: null,
                    floorId: null,
                    houseTypeId: '',
                    productName: '',
                    selectType: '',
                    inStockNum: '',
                    productTypeIds: [],
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizesMax500
                    }
                },
                // houseTypes: [],

                buildings: [],
                floors: [],
                productTypes: [],
                floorProducts: [],
                selectedFloorProducts: [],
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
                valueBD: '1',
                optionsNum: [
                    {
                        value: '',
                        label: ''
                    }, {
                        value: '1',
                        label: '1'
                    }, {
                        value: '2',
                        label: '2'
                    }, {
                        value: '3',
                        label: '≥3'
                    },],
            }
        },
        mounted() {
            //this.showHouseType()
            this.init()
            this.list()
        },
        computed: {},
        methods: {
            // 点击整行都可以进行勾选
            clickRow(row) {
                this.$refs.moviesTable.toggleRowSelection(row)
            },
            init() {
                buildingModel.findByProjectId(this.projectId).then(data => {
                    this.buildings = data.entity
                })
                productTypeModel.getAllproductTypes().then((data) => {
                    this.productTypes = data.entity
                })
                /* floorModel.findFloorsGroupByFloorNum().then((data) => {
                     function compare(property) {
                         return function (a, b) {
                             var value1 = a[property];
                             var value2 = b[property];
                             return value1 - value2;
                         }
                     }

                     this.floorsselectedFloorProducts = data.entity
                 })*/

            },
            query() {
                this.entity.pageInfo.currentPage = 1
                this.list()
            },
            list() {
                if (this.entity.buildingId == null || this.entity.buildingId == '') {
                    // this.$message('请选择楼栋');
                    // return;
                    this.entity.buildingId = ''
                }
                if (this.entity.floorId == null || this.entity.floorId == '') {
                    // this.$message('请选择楼层');
                    // return;
                    this.entity.floorId = ''
                }
                this.entity.selectType = this.valueBD
                this.entity.projectId = this.projectId
                floorRelevanceModel.findToPlanNew(this.entity).then((data => {
                    let list = data.entity.list

                    if (list) {
                        list.forEach(fp => {
                            fp.optional = !this.planDetails.some(pd => pd.relevanceId === fp.relevanceId)
                        })
                        list.forEach(fp => {
                            if(fp.optional){
                              fp.optional = fp.reqNum !== fp.sendNum
                            }
                        })
                    }
                    this.floorProducts = list
                    //filter过滤后数据的总数量
                    // let total = 0;
                    // if (data.entity.list != null && list != null) {
                    //     total = data.entity.list.length - list.length
                    // }
                    // this.entity.pageInfo.total = data.entity.totalCount - total
                    this.entity.pageInfo.total = data.entity.totalCount
                }))

                /*        floorRelevanceModel.findToPlan(this.entity).then((data => {
                            let result = data.entity.list
                            if (result != null) {
                                this.floorProducts = result.filter(fp => {
                                    return !this.planDetails.some(pd => pd.relevanceId == fp.id)
                                })
                            }
                            this.entity.pageInfo.total = data.entity.totalCount
                            // console.log(result);//TO DELETE
                        }))*/
                /*         floorRelevanceModel.findByBuildingIdAndFloorId(this.entity.buildingId,this.entity.floorId).then(data=>{
                             let result=data.entity
                             if(result!=null){
                                 this.floorProducts=result.filter(fp=>{
                                     return !this.planDetails.some(pd=>pd.relevanceId==fp.id)
                                 })
                             }
                             console.log(result);//TO DELETE
                         })*/
            },
            // 表格row是否可以勾选
            checkSelectable(row){
              return row.optional
            },
            /*  changeHouseType(id) {
                  if (id == undefined || id == null || id == '') {
                      id = '0'
                  }
                  buildingModel.getBuildingsByHouseTypeId(id).then((data) => {
                      this.buildings = data.entity
                      this.entity.buildingId = ''
                      this.entity.floorId = ''
                      this.floors = []
                  })
              },*/
            changeBuilding(id) {
                if (id == '') {
                    id = 0
                }
                this.entity.floorId = ''
                this.floors = []
                let building = this.buildings.find(b => b.id == id)
                if (building != null) {
                    floorModel.findByBuildingIdAndProjectId(building.id, building.projectId).then((data) => {
                        this.floors = data.entity
                        this.entity.floorId = ''
                    })
                }
                /* function compare(property) {
                     return function (a, b) {
                         var value1 = a[property];
                         var value2 = b[property];
                         return value1 - value2;
                     }
                 }
                 this.floors=building.floors.sort(compare('floorNum'))*/
            },
            changeProductNameInput(id) {
                if (id != null) {
                    this.isShowProductName = true
                }
            },

            add() {
                if (this.selectedFloorProducts.length == 0) {
                    this.$message('请选择添加项');
                    return;
                }
                /*let legalAdd = true
                this.selectedFloorProducts.forEach(fp => {
                    // fp.num = 1
                    if (fp.inStockNum == 0) {
                        legalAdd = false
                    }
                })
                if (!legalAdd) {
                    this.$message('已选构件中在库量为0');
                    return;
                }*/

                /*        let legalAdd = true
                        this.selectedFloorProducts.forEach(fp => {
                            if (fp.totalPlanNum >= fp.num) {
                                legalAdd = false
                            }
                        })
                        if (!legalAdd) {
                            this.$message('已计划数超过需求数，无法添加');
                            return;
                        }*/
                this.$emit('floor-product-choose-finish', this.selectedFloorProducts)

            },
            /*   showHouseType() {
                   houseTypeModel.getAllHouseTypes().then((data) => {
                       this.houseTypes = data.entity
                   })
               },*/
            handleSelectionChange(selected) {
                this.selectedFloorProducts = selected
            },
            handleSizeChange(val) {
                this.entity.pageInfo.pageSize = val;
                this.list();
            },
            handleCurrentChange(val) {
                this.entity.pageInfo.currentPage = val;
                this.list();
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.entity.buildingId = ''
                this.entity.floorId = ''
            }

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
