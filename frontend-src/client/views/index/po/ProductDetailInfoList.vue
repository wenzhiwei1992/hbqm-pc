<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                :model="formSelect"
                                class="form-inline"
                        >
                            <el-form-item>
                                <el-input
                                        v-model="formSelect.detailNo"
                                        placeholder="明细编码"
                                        clearable
                                        style="width: 135px"
                                ></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-input
                                        v-model="formSelect.productNo"
                                        placeholder="构件编码"
                                        clearable
                                        style="width: 135px"
                                ></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-input
                                        v-model="formSelect.productName"
                                        placeholder="构件名称"
                                        clearable
                                        style="width: 170px"
                                ></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-select
                                        v-model="formSelect.productStatus"
                                        placeholder="构件状态"
                                        clearable
                                        style="width: 135px"
                                        collapse-tags
                                >
                                    <el-option
                                            label="待隐检"
                                            value="待隐检"
                                    ></el-option>
                                    <el-option
                                            label="隐检返修"
                                            value="隐检返修"
                                    ></el-option>
                                    <el-option
                                            label="浇筑确认"
                                            value="浇筑确认"
                                    ></el-option>
                                    <el-option
                                            label="待成品检"
                                            value="待成品检"
                                    ></el-option>
                                    <el-option
                                            label="成品返修"
                                            value="返修"
                                    ></el-option>
                                    <el-option
                                            label="报废"
                                            value="报废"
                                    ></el-option>
                                    <el-option
                                            label="待入库"
                                            value="待入库"
                                    ></el-option>
                                    <el-option
                                            label="成品入库"
                                            value="成品入库"
                                    ></el-option>
                                    <el-option
                                            label="报废入库"
                                            value="报废入库"
                                    ></el-option>
                                    <el-option
                                            label="发货待确认"
                                            value="发货待确认"
                                    ></el-option>
                                    <el-option
                                            label="已发货"
                                            value="已发货"
                                    ></el-option>
                                </el-select>
                                <!--                    <el-input v-model="formSelect.productStatus" placeholder="构件状态" clearable style="width: 135px"></el-input>-->
                            </el-form-item>
                            <el-form-item>
                                <el-input
                                        v-model="formSelect.plateNo"
                                        placeholder="模台号"
                                        clearable
                                        style="width: 135px"
                                ></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-select
                                        v-model="formSelect.printCount"
                                        placeholder="打印次数"
                                        clearable
                                        style="width: 135px"
                                        collapse-tags
                                >
                                    <el-option
                                            label="0"
                                            value="0"
                                    ></el-option>
                                    <el-option
                                            label="1"
                                            value="1"
                                    ></el-option>
                                    <el-option
                                            label="2"
                                            value="2"
                                    ></el-option>
                                    <el-option
                                            label="3"
                                            value="3"
                                    ></el-option>
                                    <el-option
                                            label="4"
                                            value="4"
                                    ></el-option>
                                    <el-option
                                            label="5"
                                            value="5"
                                    ></el-option>
                                    <el-option
                                            label=">5"
                                            value="6"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item>
                                <el-date-picker
                                        v-model="formSelect.predictStartDate"
                                        align="right"
                                        type="date"
                                        style="width: 135px"
                                        placeholder="开始日期"
                                        :picker-options="pickerOptions1"
                                >
                                </el-date-picker>
                            </el-form-item>
                            <el-form-item>
                                <el-date-picker
                                        v-model="formSelect.predictEndDate"
                                        align="right"
                                        type="date"
                                        style="width: 135px"
                                        placeholder="结束日期"
                                        :picker-options="pickerOptions1"
                                >
                                </el-date-picker>
                            </el-form-item>
                            <el-form-item prop="projectName">
                                <el-select
                                        style="width: 170px"
                                        v-model="formSelect.projectId"
                                        placeholder="项目名称"
                                        clearable
                                        @change="changeProjectSelect"
                                >
                                    <el-option
                                            v-for="item in arrProject"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="buildId">
                                <el-select
                                        placeholder="楼栋"
                                        style="width:135px"
                                        v-model="formSelect.buildId"
                                        size="mini"
                                        clearable
                                        @change="changeBuildingSelect"
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
                                        placeholder="楼层"
                                        style="width:135px"
                                        v-model="formSelect.floorId"
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
                            <el-form-item>
                                <el-input
                                        v-model="formSelect.orderNo"
                                        clearable
                                        placeholder="周计划单号"
                                        style="width: 135px"
                                ></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-input
                                        v-model="formSelect.woNo"
                                        clearable
                                        placeholder="日计划单号"
                                        style="width: 135px"
                                ></el-input>
                            </el-form-item>
                            <el-form-item prop="trayId">
                                <el-select
                                        placeholder="托盘"
                                        style="width:135px"
                                        v-model="formSelect.trayId"
                                        clearable
                                        filterable
                                        size="mini"
                                >
                                    <el-option
                                            v-for="tray in trays"
                                            :key="tray.id"
                                            :label="tray.trayNo"
                                            :value="tray.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item>
                                <el-select
                                        v-model="formSelect.isPrint"
                                        placeholder="是否已打印二维码"
                                        clearable
                                        style="width: 155px"
                                        collapse-tags
                                >
                                    <el-option
                                            label="是"
                                            value="1"
                                    ></el-option>
                                    <el-option
                                            label="否"
                                            value="0"
                                    ></el-option>

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
                        :data="datas"
                        size="mini"
                        stripe
                        border
                        @selection-change="handleSelectionChange"
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                        height="100%"
                        style="width: 100%"
                >
                    <el-table-column
                            type="selection"
                            width="40"
                    >
                    </el-table-column>
                    <el-table-column
                            type="index"
                            label="序号"
                            width="70" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="projectName"
                            label="项目"
                            width="100" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="buildCode"
                            label="楼栋"
                            width="80" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            label="楼层"
                            width="80" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="typeName"
                            label="构件类型"
                            width="100" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productName"
                            label="构件名称"
                            width="140" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productNo"
                            label="构件编码"
                            width="140" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="detailNo"
                            label="明细编码"
                            width="130" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productStatus"
                            label="状态"
                            width="80" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product_height"
                            label="宽度"
                            width="60" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product_vol"
                            label="体积"
                            width="60" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product_wt"
                            label="重量"
                            width="60" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product_acreage"
                            label="面积"
                            width="60" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="project_concrete"
                            label="砼方量"
                            width="80" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product_grade"
                            label="砼等级"
                            width="80" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="predictDate"
                            :formatter="formatterDate"
                            label="预生产日期"
                            width="140" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="moldName"
                            label="模具名称"
                            width="120" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="plateNo"
                            label="模台号"
                            width="100" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="orderNo"
                            label="周计划订单号"
                            width="100" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="woNo"
                            label="日计划订单号"
                            width="100" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="trayNo"
                            label="托盘号"
                            width="100" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="printCount"
                            label="打印次数"
                            width="120" sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="printCount"
                            label="是否已打印二维码"
                            width="140">
                        <template slot-scope="scope">
                                        <span v-if="scope.row.printCount>0"
                                              style="color: #67C23A;font-weight:800;">是</span>
                            <span v-else
                                  style="color: #F56C6C;font-weight:800;">否</span>
                        </template>
                    </el-table-column>
                    <!-- <el-table-column
                            label="操作"
                            align="center"
                            fixed="right"
                            width="120"
                    >
                        <template slot-scope="scope">
                            <el-tooltip
                                    class="item"
                                    effect="light"
                                    content="打印旧版"
                                    placement="top"
                            >
                                <el-button
                                        size="mini"
                                        icon="el-icon-printer"
                                        :class="scope.row.printCount!=='0'?'active':''"
                                        type="warning"
                                        @click.stop="showProductDetail(scope.row.id)"
                                ></el-button>
                            </el-tooltip> -->
                    <!-- <el-tooltip
                            class="item"
                            effect="light"
                            content="打印新版"
                            placement="top"
                    >
                        <el-button
                                icon="el-icon-printer"
                                :class="scope.row.printCount!=='0'?'active':''"
                                size="mini"
                                type="success"
                                @click.stop="showProductDetailNews(scope.row.id)"
                        ></el-button>
                    </el-tooltip> -->
                    <!-- <el-tooltip class="item" effect="light" content="宏泰江夏打印" placement="top">
                              <el-button  icon="el-icon-printer" :class="scope.row.printCount!=='0'?'active':''" size="mini"
                                         type="success" @click.stop="showProductDetailPrintHT(scope.row.id)"></el-button>
                          </el-tooltip> -->
                    <!-- <el-button @click="showProductDetailSJ(scope.row.id)">三杰</el-button>-->
                    <!--    <el-button size="medium" :class="scope.row.printCount!=='0'?'active':''" type="success" @click="showProductDetail(scope.row.id)">二维码打印</el-button>
                             &lt;!&ndash; <el-button size="medium" type="success" @click="showProductDetailNew(scope.row.id)">打印</el-button>&ndash;&gt;
                              <el-button icon="el-icon-printer" size="medium" type="success" @click="showProductDetailNews(scope.row.id)">打印</el-button>-->
                    <!-- </template>
                </el-table-column> -->
                </el-table>

                <el-table
                        :data="exportdatas"
                        size="mini"
                        stripe
                        border
                        id="exportXlsx"
                        hidden
                >
                    <el-table-column
                            type="index"
                            label="序号"
                            width="70"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="plateNo"
                            label="模台号"
                            width="100"
                            sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="orderNo"
                            label="订单号"
                            width="100"
                            sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="projectName"
                            label="项目"
                            width="100"
                            sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="typeName"
                            label="构件类型"
                            width="100"
                            sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productName"
                            label="构件名称"
                            width="140"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productNo"
                            label="构件编码"
                            width="140"
                            sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="detailNo"
                            label="明细编码"
                            sortable
                            width="130"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productStatus"
                            label="状态"
                            width="80"
                            sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product_height"
                            label="宽度"
                            width="60"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product_vol"
                            label="体积"
                            width="60"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product_wt"
                            label="重量"
                            width="60"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product_acreage"
                            label="面积"
                            width="60"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="project_concrete"
                            label="砼方量"
                            width="80"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product_grade"
                            label="砼等级"
                            width="80"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="predictDate"
                            :formatter="formatterDate"
                            label="预生产日期"
                            width="140"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="printCount"
                            label="打印次数"
                            sortable
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="moldName"
                            label="模具名称"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="buildCode"
                            label="楼栋"
                            width="80"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            label="楼层"
                            width="80"
                    >
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
        <el-dialog
                title=""
                :visible.sync="checkPrintVisible"
                v-if="checkPrintVisible"
                width="50%"
                top="10px"
        >
            <product-detail-print
                    :productDetailInfo="productDetailInfo"
                    @print-cancel="printCancel"
                    @print-finish="printFinish"
            >
            </product-detail-print>
        </el-dialog>
        <el-dialog
                title=""
                :visible.sync="productPrintVisible"
                v-if="productPrintVisible"
                width="50%"
                top="10px"
        >
            <product-detail-print-new
                    :productDetailInfoNew="productDetailInfoNew"
                    @product-print-cancel="productPrintCancel"
                    @product-print-finish="productPrintFinish"
            >
            </product-detail-print-new>
        </el-dialog>
        <el-dialog
                title=""
                :visible.sync="productPrintNewVisible"
                v-if="productPrintNewVisible"
                width="50%"
                top="10px"
        >
            <product-detail-print-news
                    :productDetailInfoNew="productDetailInfoNew"
                    @product-print-cancel="productPrintNewCancel"
                    @product-print-finish="productPrintNewFinish"
            >
            </product-detail-print-news>
        </el-dialog>
        <el-dialog
                title=""
                :visible.sync="productPrintSJVisible"
                v-if="productPrintSJVisible"
                width="50%"
                top="10px"
        >
            <product-detail-print-sj
                    :productDetailInfoSJ="productDetailInfoSJ"
                    @product-print-sj-cancel="productPrintSJCancel"
                    @product-print-sj-finish="productPrintSJFinish"
            >
            </product-detail-print-sj>
        </el-dialog>
        <el-dialog
                title=""
                :visible.sync="productPrintHTVisible"
                v-if="productPrintHTVisible"
                width="50%"
                top="10px"
        >
            <product-detail-print-ht
                    :productDetailPrintHTInfo="productDetailPrintHTInfo"
                    @product-print-ht-cancel="productPrintHTCancel"
                    @product-print-ht-finish="productPrintHTFinish"
            >
            </product-detail-print-ht>
        </el-dialog>
    </div>
</template>
<script>
    import platePlanModel from '../../../model/po/platePlan.js'
    import productDetailModel from '../../../model/project/productDetail'
    import trayModel from '../../../model/basicdata/tray.js'
    import buildModel from '../../../model/project/building'
    import floorModel from '../../../model/project/floor'
    import projectModel from '../../../model/project/project'
    import {Notification} from 'element-ui'
    import moment from "moment";
    import fileSaver from 'file-saver';
    import xlsx from 'xlsx';
    import ProductDetailPrintNew from "../check/ProductDetailPrintNew.vue"
    import ProductDetailPrintNews from "../check/print/ProductDetailPrintNews.vue"
    import ProductDetailPrint from "../check/ProductDetailPrint.vue"
    import productDetailPrintSJ from './ProductDetailPrintSJ.vue'
    import productDetailPrintHT from './ProductDetailPrintHT.vue'
    import menuTabService from "../../../service/menuTabService";

    export default {
        data() {
            return {
                pickerOptions1: {
                    // disabledDate(time) {
                    //     return time.getTime() > Date.now();
                    // },
                    shortcuts: [{
                        text: '今天',
                        onClick(picker) {
                            picker.$emit('pick', new Date());
                        }
                    }, {
                        text: '昨天',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() - 3600 * 1000 * 24);
                            picker.$emit('pick', date);
                        }
                    }, {
                        text: '一周前',
                        onClick(picker) {
                            const date = new Date();
                            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', date);
                        }
                    }]
                },
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: this.GLOBAL.pageSize,
                    pageSizes: this.GLOBAL.pageSizes
                },
                datas: [],
                expands: [],
                selected: [],
                exportdatas: [],
                formSelect: {
                    plateNo: '',
                    buildCode: '',
                    floorNum: '',
                    productStatus: '',
                    predictDate: '',
                    productName: '',
                    detailNo: '',
                    floorId: '',
                    buildId: '',
                    orderNo: '',
                    id: '',
                    isPrint: '0',
                },
                checkPrintVisible: false,
                productDetailInfo: null,
                productDetailInfoNew: '',
                productPrintVisible: false,
                productDetailInfoNews: '',
                productPrintNewVisible: false,
                productPrintSJVisible: false,
                productPrintHTVisible: false,
                productDetailPrintHTInfo: '',
                productDetailInfoSJ: [],
                buildings: [],
                trays:[],
                floors: [],
                arrProject: [],//select下拉框（project）
                projectId: [],
                buttonData: [
                    {
                        name: '导出',
                        event: 'exportExcel',
                        authorized: 'platePlanProductDetails:export'
                    },
                    {
                        name: '批量打印A',
                        event: 'printsHT',
                        authorized: 'platePlanProductDetails:orgPrint'
                    },
                    {
                        name: '批量打印B',
                        event: 'prints',
                        authorized: 'platePlanProductDetails:sjPrint'
                    },
                    {
                        name: '批量打印C',
                        event: 'printsXY',
                        authorized: 'platePlanProductDetails:xyPrint'
                    },
                    {
                        name: '批量打印D',
                        event: 'printsHBHT',
                        authorized: 'platePlanProductDetails:htPrint'
                    }
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: 'onSubmit'
                    }
                ],
                // rowButtonData:[
                //     {
                //         name:'编辑',
                //         event:'editClick'
                //     }
                // ]
            }
        },
        components: {
            'product-detail-print': ProductDetailPrint,
            'product-detail-print-new': ProductDetailPrintNew,
            'product-detail-print-news': ProductDetailPrintNews,
            'product-detail-print-sj': productDetailPrintSJ,
            'product-detail-print-ht': productDetailPrintHT,
        },
        mounted() {
            trayModel.findAllByOrgId().then(data => {
                this.trays = data.entity;
            })
            this.pageableList();
            this.info()
            console.log('this.$store.state.user------>', this.$store.state.user)

        },
        methods: {
            /**
             * 三杰批量打印
             */
            prints() {
                if (this.selected.length == 0) {
                    this.$message(this.GLOBAL.PLAN_MSG.SELECT_NOT_PRINT);
                    return;
                }
                let arr = []
                this.selected.forEach((row) => {
                    arr.push(row.id)
                })

                this.$router.push({
                    name: 'productDetailPrintSJ',
                    params: {
                        ids: arr
                    }
                })

                //this.productPrintSJVisible=true

            },
            printsHT() {
                if (this.selected.length == 0) {
                    this.$message(this.GLOBAL.PLAN_MSG.SELECT_NOT_PRINT);
                    return;
                }
                let arr = []
                this.selected.forEach((row) => {
                    arr.push(row.id)
                })
                this.$router.push({
                    name: 'productDetailPrintsHT',
                    params: {
                        ids: arr
                    }
                })
            },
            printsXY() {
                if (this.selected.length == 0) {
                    this.$message(this.GLOBAL.PLAN_MSG.SELECT_NOT_PRINT);
                    return;
                }
                let arr = []
                this.selected.forEach((row) => {
                    arr.push(row.id)
                })
                this.$router.push({
                    name: 'productDetailPrintsXY',
                    params: {
                        ids: arr
                    }
                })
            },
            printsHBHT() {
                if (this.selected.length == 0) {
                    this.$message(this.GLOBAL.PLAN_MSG.SELECT_NOT_PRINT);
                    return;
                }
                let arr = []
                this.selected.forEach((row) => {
                    arr.push(row.id)
                })
                this.$router.push({
                    name: 'productDetailPrintsHBHT',
                    params: {
                        ids: arr
                    }
                })
            },
            exportExcel() {
                let self = this;
                let msg = "是否导出所选数据?";
                if (self.selected.length == 0) {
                    msg = "是否导出全部数据?";
                    ;
                }
                this.$confirm(msg, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    if (self.selected.length == 0) {
                        if (this.pageInfo.total >= 5000) {
                            Notification({
                                message: "导出数据过多",
                                type: 'warning'
                            })
                            return;
                        }
                        this.exportAllExcel();
                    } else {
                        this.exportdatas = self.selected;
                        this.exportSelectExcel();
                    }

                }).catch(() => {

                })
            },
            exportSelectExcel() {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                //TODO 分页
                setTimeout(() => {
                    var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
                    excelName = "构件二维码" + excelName;
                    /* generate workbook object from table */
                    let table = document.querySelector('#exportXlsx');
                    var wb = xlsx.utils.table_to_book(table);
                    /* get binary string as output */
                    var wbout = xlsx.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'})
                    try {
                        fileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), excelName)
                    } catch (e) {
                        if (typeof console !== 'undefined') console.log(e, wbout)
                    }

                    loading.close();
                    return wbout;
                }, 500);
            },
            exportAllExcel() {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                let page = this.pageInfo.currentPage;
                let size = this.pageInfo.pageSize;

                this.formSelect.pageInfo = this.pageInfo;
                this.formSelect.pageInfo.currentPage = 1;
                this.formSelect.pageInfo.pageSize = this.pageInfo.total;
                //获取menu名称，用于 后台api判断，避免api共用导致冲突
                let active = this.$store.state.menuActiveName
                this.formSelect.menuActiveName = active
                platePlanModel.getProductDetailInfoListS(this.formSelect).then(data => {
                    this.exportdatas = data.entity.content;
                    setTimeout(() => {
                        var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
                        excelName = "构件二维码" + excelName;
                        /* generate workbook object from table */
                        let table = document.querySelector('#exportXlsx');
                        var wb = xlsx.utils.table_to_book(table);
                        /* get binary string as output */
                        var wbout = xlsx.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'})
                        try {
                            fileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), excelName)
                        } catch (e) {
                            if (typeof console !== 'undefined') console.log(e, wbout)
                        }

                        this.formSelect.pageInfo.pageSize = size;
                        this.formSelect.pageInfo.currentPage = page;
                        loading.close();
                        return wbout;
                    }, 500);

                }).catch(err => {

                })


            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            onSubmit() {
                this.pageableList();
            },
            pageableList() {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                this.formSelect.pageInfo = this.pageInfo;
                //获取menu名称，用于 后台api判断，避免api共用导致冲突
                let active = this.$store.state.menuActiveName
                this.formSelect.menuActiveName = active
                platePlanModel.getProductDetailInfoListS(this.formSelect).then(data => {
                    this.datas = data.entity.content;
                    this.pageInfo.total = data.entity.totalElements;
                    loading.close();
                }).catch(err => {
                    Notification({
                        message: err.msg,
                        type: 'warning'
                    })
                })
            },
            showProductDetail(row) {
                productDetailModel.getProductDetailInfoById(row).then((data) => {
                    this.productDetailInfo = data.entity
                    this.checkPrintVisible = true
                })

            },
            showProductDetailNew(row) {
                this.$emit('product-print-finish')
                this.$router.push({
                    name: 'productDetailPrintNew',
                    params: {
                        id: row
                    }
                })
            },
            showProductDetailNews(row) {
                productDetailModel.getProductDetailInfoById(row).then((data) => {
                    this.productDetailInfoNew = data.entity
                    this.productPrintNewVisible = true
                })


                /*     this.$emit('product-print-finish')
                     this.$router.push({
                         name:'productDetailPrintNews',
                         params:{
                             id:row
                         }
                     })*/

            },
            /**
             * 宏泰打印页面
             * @param row
             */
            showProductDetailPrintHT(row) {
                //api需要productDetailId集合，
                let productDetailIds = [];
                productDetailIds.push(row)
                console.log('productDetailIds------>', productDetailIds)
                productDetailModel.findProductDetailByIdUsedForPrint(productDetailIds).then((data) => {
                    console.log('data------>', data.entity)
                    this.productDetailPrintHTInfo = data.entity[0]

                    this.productPrintHTVisible = true
                })
            },
            info() {

                projectModel.findAllProjects().then((data) => {
                    let projects = data.entity
                    for (var j = 0; j < projects.length; j++) {
                        var pName = {
                            value: projects[j].id,
                            label: projects[j].projectName,
                        }
                        this.arrProject.push(pName)
                    }
                })


            },
            changeProjectSelect(id) {
                if (id == undefined) {
                    id = '0'
                }
                this.formSelect.buildId = ''
                this.buildings = []
                this.projectId = id
                console.log('id------>', id)
                buildModel.findByProjectId(id).then((data) => {
                    console.log('data------>', data.entity)
                    this.buildings = data.entity
                })
            },
            changeBuildingSelect(id) {
                if (id == undefined) {
                    id = '0'
                }
                this.formSelect.floorId = ''
                this.floors = []
                // let building = this.buildings.find(b => b.id == id)
                floorModel.findByBuildingIdAndProjectId(id, this.projectId).then((data) => {
                    this.floors = data.entity
                    //this.formSelect.floorId = ''
                })
            },
            showProductDetailSJ(id) {
                productDetailModel.findProductDetailByIdUsedForPrint(id).then((data) => {
                    this.productDetailInfoSJ = data.entity
                })
                this.productPrintSJVisible = true
            },
            authorized(index) {
                return menuTabService.authorized(this.$store, index)
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize = val
                this.pageableList()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val
                this.pageableList()
            },
            printCancel() {
                this.checkPrintVisible = false
            },
            printFinish() {
                this.checkPrintVisible = true
            },
            productPrintCancel() {
                this.productPrintVisible = false
            },
            productPrintFinish() {
                this.productPrintVisible = true
            },

            productPrintNewCancel() {
                this.productPrintNewVisible = false
            },
            productPrintNewFinish() {
                this.productPrintNewVisible = true
            },
            /**
             * 三杰
             */
            productPrintSJCancel() {
                this.productPrintSJVisible = false
            },
            productPrintSJFinish() {
                this.productPrintSJVisible = true
            },
            /**
             * 宏泰
             */
            productPrintHTCancel() {
                this.productPrintHTVisible = false
            },
            productPrintHTFinish() {
                this.productPrintHTVisible = true
            },
            handleSelectionChange(selected) {
                this.selected = selected
            }
        }


    }

</script>
<style scoped>
    .active {
        background: #409eff;
        border: #409eff;
    }
</style>
