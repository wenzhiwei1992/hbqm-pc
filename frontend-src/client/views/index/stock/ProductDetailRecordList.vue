<template>
    <div
        id="product-detail-wait-in-record-list"
        class="currency-style-box"
    >
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <product-detail-criteria
                            ref="productDetailCriteria"
                            list-type="record"
                            v-if="productStatus=='成品入库'"
                            :selected="selected"
                            @change-criteria="changeCriteria"
                            @export-excel="exportExcel"
                            :zkDTO="zkDTO"
                    ></product-detail-criteria>
                    <product-detail-criteria
                            ref="productDetailCriteria"
                            list-type="norecord"
                            v-else
                            @change-criteria="changeCriteria"
                            :selected="selected"
                            @pl-form-show="plFormShow"
                    ></product-detail-criteria>
                </el-row>
                <el-row style="display:none">
                    <my-button-group :model="buttonData"></my-button-group>
                </el-row>
            </el-header>
            <el-main
                    ref="mainContent"
                    :style="`height:${this.$store.state.mainContentHeight}px`"
            >
                <el-table
                        :data="entities"
                        @selection-change="handleSelectionChange"
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                        height="100%"
                        style="width:100%"
                        header-row-class-name="header-row"
                        stripe
                        border
                        :default-sort="{prop: 'inRecord.execDate', order: 'descending'}"
                        tooltip-effect="light"
                        @expand-change="showSubData"
                        :expand-row-keys="expands"
                        row-key="id"
                >
                    <el-table-column type="selection">
                    </el-table-column>
                    <el-table-column type="expand">
                        <template slot-scope="props">
                            <el-table :data="checkLists">
                                <el-table-column
                                        type="index"
                                        label="序号"
                                        width="50"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="checkType"
                                        label="检验类型"
                                        align="center"
                                        width="100"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="checkResult"
                                        label="检验结果"
                                        align="center"
                                        width="100"
                                >
                                </el-table-column>
                                <el-table-column
                                        label="检验明细"
                                        align="center"
                                        width="150"
                                >
                                    <template slot-scope="scope">
                                        <el-popover
                                                trigger="click"
                                                placement="top-end"
                                                :visible-arrow="false"
                                        >
                                            <el-table :data="scope.row.items">
                                                <el-table-column
                                                        width="150"
                                                        property="name"
                                                        label="检验项"
                                                ></el-table-column>
                                                <el-table-column
                                                        width="100"
                                                        property="result"
                                                        label="检验结果"
                                                ></el-table-column>
                                            </el-table>
                                            <div
                                                    slot="reference"
                                                    class="name-wrapper"
                                            >
                                                <el-button
                                                        icon="el-icon-search"
                                                        round
                                                        type="primary"
                                                        v-if="scope.row.checkType!='报废审批'"
                                                >查看
                                                </el-button>
                                            </div>
                                        </el-popover>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        label="检验图片"
                                        align="center"
                                        width="150"
                                >
                                    <template slot-scope="scope">
                                        <el-button
                                                icon="el-icon-search"
                                                round
                                                type="primary"
                                                v-if="scope.row.checkImages && scope.row.checkImages.length>0"
                                                @click="showCheckImages(scope.row)"
                                        >查看检验图片
                                        </el-button>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        prop="checkDesc"
                                        label="说明"
                                        align="center"
                                        width="200"
                                        :show-overflow-tooltip="true"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="checkPersonName"
                                        label="检验员"
                                        align="center"
                                        width="100"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="checkTime"
                                        label="检验日期"
                                        align="center"
                                        width="200"
                                        :formatter="momentFormat"
                                >
                                </el-table-column>
                            </el-table>
                        </template>
                    </el-table-column>
                    <el-table-column
                            type="index"
                            label="序号"
                            width="70"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="order.orderNo"
                            label="订单编号"
                            align="center"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="inRecord.inBoundOrder"
                            label="入库单号"
                            align="center"
                            width="100"
                            v-if="productStatus=='成品入库'"
                    >
                    </el-table-column>
                    <!--            <el-table-column-->
                    <!--                    prop="detailNo"-->
                    <!--                    label="构件明细编号"-->
                    <!--                    align="center"-->
                    <!--                    width="120">-->
                    <!--            </el-table-column>-->
                    <el-table-column
                            prop="project.projectName"
                            label="项目名称"
                            align="center"
                            width="160"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorRelevance.building.buildCode"
                            label="楼栋"
                            align="center"
                            width="80"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorRelevance.floor.floorNum"
                            label="楼层"
                            align="center"
                            width="80"
                    >
                    </el-table-column>

                    <el-table-column
                            prop="detailNo"
                            label="明细编码"
                            align="center"
                            width="80"
                    >
                    </el-table-column>

                    <el-table-column
                            prop="product.productNo"
                            label="构件编码"
                            align="center"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product.productName"
                            label="构件名称"
                            align="center"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="check.checkResult"
                            label="构件属性"
                            align="center"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="check.checkTime"
                            label="成品检日期"
                            :formatter="momentFormat"
                            align="center"
                            width="120"
                    >
                    </el-table-column>
                    <!--     <el-table-column
                              prop="check.checkResult"
                              label="质量状态"
                              align="center">
                      </el-table-column>-->
                    <el-table-column
                            v-if="productStatus=='待入库'"
                            label="已过期限">
                        <template slot-scope="scope">
                            <el-popover trigger="hover" placement="top">
                                <p>是否超出待入库期限</p>
                                <div slot="reference">
                                    <el-tag size="medium" :class="scope.row.noticeDPS=='是' ?'red' :'green' "> {{
                                        scope.row.noticeDPS }}
                                    </el-tag>
                                </div>
                            </el-popover>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="check.attr"
                            label="构件属性"
                            align="center"
                            v-if="productStatus=='成品入库'"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="stock.stockNo"
                            label="库区号"
                            align="center"
                            width="100"
                            v-if="productStatus=='成品入库'"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="shelf.shelfNo"
                            label="货架号"
                            align="center"
                            width="100"
                            v-if="productStatus=='成品入库'"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="inRecord.execDate"
                            label="入库日期"
                            sortable
                            align="center"
                            width="200"
                            v-if="productStatus=='成品入库'"
                            :formatter="momentFormat"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="trayNo"
                            v-if="productStatus=='待入库'"
                            label="托盘号"
                            align="center"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="inRecord.modifiedByName"
                            label="操作人"
                            align="center"
                            width="100"
                            v-if="productStatus=='成品入库'"
                    >
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            fixed="right"
                            align="center"
                            v-if="(productStatus=='成品入库' &&
                                                authorized('productDetailInRecordList:scrapRecord'))
                                                ||productStatus=='待入库'"
                            width="120"
                    >
                        <template slot-scope="scope">
                            <el-tooltip
                                    effect="light"
                                    content="成品入库"
                                    placement="top"
                            >
                                <el-button
                                        v-if="productStatus=='待入库'"
                                        type="success"
                                        icon="el-icon-s-fold"
                                        @click.stop="inRecord(scope.row)"
                                ></el-button>
                            </el-tooltip>

                            <el-tooltip
                                    effect="light"
                                    content="撤销"
                                    placement="top"
                            >
                                <el-button
                                        v-if="productStatus=='待入库'"
                                        type="danger"
                                        icon="el-icon-minus"
                                        @click="returnCheckStatus(scope.row)"
                                ></el-button>
                            </el-tooltip>
                            <el-tooltip
                                    effect="light"
                                    content="报废"
                                    placement="top"
                            >
                                <el-button
                                        v-if="productStatus=='成品入库' && authorized('productDetailInRecordList:scrapRecord')"
                                        type="danger"
                                        icon="el-icon-delete"
                                        @click.stop="inScrap(scope.row)"
                                ></el-button>
                            </el-tooltip>

                        </template>
                    </el-table-column>
                </el-table>


                <el-table
                        :data="exportdatas"
                        id="exportXlsx"
                        hidden
                        height="100%"
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                        style="width: 100%"
                        :summary-method="getSummaries"
                        show-summary
                >
                    <el-table-column
                            type="index"
                            label="序号"
                            width="70"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product.productNo"
                            label="构件编码"
                            align="center"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product.productName"
                            label="构件名称"
                            align="center"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="num"
                            label="数量"
                            align="center"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="shelf.shelfName"
                            label="库位"
                            align="center"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productWt"
                            label="重量(T)"
                            align="center"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productVol"
                            label="体积(m³)"
                            align="center"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product.productGrade"
                            label="混凝土等级"
                            align="center"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="remark"
                            label="备注"
                            align="center"
                            width="120"
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

        <el-dialog title="检验图片" :visible.sync="checkImagesVisible" width="800px" top="10px" v-if="checkSelected!=null">
            <div v-for="checkImage in checkSelected.checkImages" :key="checkImage.id">
                <h3>{{checkImage.id}}</h3>
                <img :src="checkImage.src" :alt="checkImage.id"  style="max-width:100%;">
            </div>
        </el-dialog>
        <el-dialog
                title="入库"
                v-if="editFormVisible"
                :visible.sync="editFormVisible"
                width="30%"
        >
            <model-edit
                    :product-detail="productDetail"
                    :stocks="stocks"
                    :shelves="shelves"
                    :trays="trays"
                    @model-edit-finish="modelEditFinish"
                    @model-edit-cancel="modelEditCancel"
            ></model-edit>
        </el-dialog>

        <el-dialog
                title="批量入库"
                v-if="visiblePL"
                :visible.sync="visiblePL"
                width="30%"
        >
            <el-form
                    :model="editFormPL"
                    v-if="editFormPL"
                    :rules="plRules"
                    ref="editFormPL"
                    label-width="100px"
                    size="small"
            >
                <el-form-item
                        label="库区"
                        prop="toStockId"
                >
                    <el-select
                            v-model="editFormPL.toStockId"
                            @change="changeStock"
                            :disabled="this.visibleTray"
                    >
                        <el-option
                                v-for="stock in stocks"
                                :key="stock.id"
                                :label="stock.stockNo"
                                :value="stock.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item
                        label="货架"
                        prop="toShelfId"
                >
                    <el-select
                            v-model="editFormPL.toShelfId"
                            :disabled="this.visibleTray"
                            filterable
                    >
                        <el-option
                                v-for="shelf in shelves"
                                :key="shelf.id"
                                :label="shelf.shelfName"
                                :value="shelf.id"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>


                <el-form-item label=" 托盘" prop="trayId">
                    <el-select v-model="editFormPL.trayId" filterable clearable @change="changeTray">
                        <el-option
                                v-for="tray in trays"
                                :key="tray.id"
                                :label="tray.trayNo"
                                :value="tray.id">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div
                    style="text-align:right"
                    class="dialog-footer"
            >
                <el-button @click="plCancel">取 消</el-button>
                <el-button
                        type="primary"
                        @click="plSubmmit('editFormPL')"
                >确 定
                </el-button>
            </div>
        </el-dialog>
        <el-dialog
                title="报废"
                v-if="editFormVisibleScrap"
                :visible.sync="editFormVisibleScrap"
                width="40%"
        >
            <scrap-type-list
                    :editFormScrap="editFormScrap"
                    @scrap-edit-finish="scrapEditFinish"
                    @scrap-edit-cancel="scrapEditCancel"
            ></scrap-type-list>
        </el-dialog>
    </div>
</template>
<script>
    import model from '../../../model/model'
    import moment from 'moment'
    import stockModel from '../../../model/basicdata/stock'
    import shelfModel from '../../../model/basicdata/shelf'
    import {Notification} from 'element-ui'
    import RecordEdit from './RecordEdit.vue'
    import ProductDetailCriteria from './ProductDetailCriteria.vue'
    import ScrapTypeList from "./ScrapTypeList.vue";
    import xlsx from 'xlsx';
    import fileSaver from 'file-saver';
    import menuTabService from "../../../service/menuTabService";
    import trayModel from '../../../model/basicdata/tray.js'
    import serverConfig from "../../../../server.config";

    export default {
        components: {
            'model-edit': RecordEdit,
            'product-detail-criteria': ProductDetailCriteria,
            'scrap-type-list': ScrapTypeList,

        },
        data() {
            return {
                //表查询
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: 20,
                    pageSizes: [20,50,100,200,1000,2000]
                },
                exportdatas: [],
                entity: {
                    recordDateRange: ['', '']
                },
                entities: [],
                //行操作
                selected: [],
                //编辑表单
                productDetail: null,
                editFormVisible: false,
                checkLists: [],//子信息
                expands: [],
                stocks: [],
                shelves: [],
                trays:[],
                editFormPL: {},
                visiblePL: false,
                visibleTray: false,
                editFormScrap: '',
                editFormVisibleScrap: false,
                zkDTO: {
                    zkPs: '',
                    zkM3: '',
                },
                plRules: {
                    toStockId: [
                        {required: true, message: '请选择库区号', trigger: 'blur'},
                    ],
                    toShelfId: [
                        {required: true, message: '请选择货架号', trigger: 'blur'},
                    ],
                },
                buttonData: [
                    {
                        name: '新建'
                    }
                ],
                checkImagesVisible:false,
                checkSelected:null
            }
        },
        computed: {
            productStatus() {
                let active = this.$store.state.menuActiveName
                let productStatus = ''
                if (active == 'productDetailWaitInRecordList') {
                    productStatus = '待入库'
                } else if (active == 'productDetailInRecordList') {
                    productStatus = '成品入库'
                }
                return productStatus
            },

        },
        mounted() {
            this.init()
            this.list()
        },
        methods: {

            getStocks(){
                shelfModel.findAllByOrgId().then(data => {
                    this.stocks = data.entity;
                    this.stocks = this.stocks.filter(s => s.stockStatus == '启用' && (s.stockTypeCode=='成品仓库'||s.stockTypeCode == '返修仓库'))
                }).catch(err => {
                    Notification({
                        message: err.msg,
                        type: 'warning'
                    })
                })
            },
            getTrays(){
                trayModel.findAllByOrgId().then(data => {
                    this.trays = data.entity;
                    this.trays = this.trays.filter(s => s.trayStatus != '发货中')

                })
            },

            authorized(index) {
                return menuTabService.authorized(this.$store, index)
            },
            getSummaries(param) {
                const {columns, data} = param;
                const sums = [];
                let size = 0;
                columns.forEach((column, index) => {
                    if (index === 0) {
                        sums[index] = '合计';
                        return;
                    }
                    sums[index] = '';
                    const values = data.map(item => Number(item[column.property]));
                    if (!values.every(value => isNaN(value))) {
                        sums[index] = values.reduce((prev, curr) => {
                            const value = Number(curr);
                            console.log(value)
                            if (!isNaN(curr)) {
                                if (curr.toString().indexOf('.') != -1) {
                                    let size1 = curr.toString().split('.')[1].length;
                                    if (size1 > size) {
                                        size = size1;
                                    }
                                }
                                return prev + curr;
                            } else {
                                return prev;
                            }
                        }, 0);
                        // console.log(size)
                        // if(Number(sums[index])){
                        //     sums[index] = sums[index].toFixed(size);
                        // }
                    }
                });

                return sums;

            },
            exportExcelAll(rows, entity) {

                let page = this.pageInfo.currentPage;
                let size = this.pageInfo.pageSize;
                this.pageInfo.currentPage = 1;
                this.pageInfo.pageSize = this.pageInfo.total;

                this.entity = entity
                if (this.$route.params.stockId && this.$route.params.stockId) {
                    this.$refs.productDetailCriteria.entity.stockId = this.entity.stockId = this.$route.params.stockId
                    this.$refs.productDetailCriteria.changeStock(this.entity.stockId)
                    this.$refs.productDetailCriteria.entity.shelfId = this.entity.shelfId = this.$route.params.shelfId
                }

                this.entity.listType = 'stock'
                this.entity.productStatus = this.productStatus


                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });

                model.list('productDetail', this.entity, this.pageInfo).then(data => {
                    let entity = data.entity
                    let list = data.entity.content;
                    list.forEach((pd) => {
                        let recordList = pd.recordList
                        if (recordList != null && recordList.length > 0) {
                            let inRecord = recordList.find((record) => {
                                return record.inOutType == '入库'
                            })
                            pd.inRecord = inRecord
                        }

                        let checkList = pd.checkList
                        if (checkList != null && checkList.length > 0) {
                            pd.check = checkList[0]
                        }
                        if (pd.check) {
                            if (pd.check.checkResult == '合格') {
                                pd.check.checkResult = '合格品'
                            } else if (pd.check.checkResult.indexOf('返修') != -1) {
                                pd.check.checkResult = '不良品'
                            } else if (pd.check.checkResult.indexOf('报废') != -1) {
                                pd.check.checkResult = '报废品'
                            } else{

                                pd.check.checkResult = pd.check.checkResult
                                //console.log(pd.check.checkResult+"123")

                            }
                            pd.check.checkResult = '合格品'
                        }
                    })



                    rows = list;
                    this.exportdatas = rows;

                    for (let i = 0; i < rows.length; i++) {
                        rows[i].productWt = rows[i].product.productWt;
                        rows[i].productVol = rows[i].product.productVol;
                        rows[i].num = 1;
                    }

                    //TODO 分页
                    setTimeout(() => {
                        var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
                        excelName = "已入堆场导出" + excelName;
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
                        this.pageInfo.pageSize = size;
                        this.pageInfo.currentPage = page;
                        loading.close();
                        return wbout;
                    }, 500);
                })


            },
            exportExcel(rows,entity) {
                this.exportdatas = rows;
                if(rows){
                    this.exportExcelAll(rows,entity);
                   return;
                }
                for (let i = 0; i < rows.length; i++) {
                    rows[i].productWt = rows[i].product.productWt;
                    rows[i].productVol = rows[i].product.productVol;
                    rows[i].num = 1;
                }
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                //TODO 分页
                setTimeout(() => {
                    var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
                    excelName = "已入堆场导出" + excelName;
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
            returnCheckStatus(row) {
                console.log(row)
                this.$confirm('此操作将回退构件到上一级质检状态, 是否继续?', '提示', {
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
                    let checkType = '待入库'
                    model.returnCheckStatus(row.id, checkType).then(data => {
                        if (data.status == this.GLOBAL.STATUS.FAILED) {
                            Notification({
                                message: data.msg,
                                type: this.GLOBAL.STATUS.WARNING
                            });
                            loading.close()
                            return;
                        }
                        Notification({
                            message: '回退成功',
                            type: this.GLOBAL.STATUS.SUCCESS
                        });
                        loading.close();
                        this.list()
                    })
                }).catch(() => {
                })
            },
            init() {
                this.getStocks();
                this.getTrays();
                /**
                 * 加载在库量的构件数和砼方量(m³)
                 */
                stockModel.loadZkInfo(this.entity).then(data => {
                    this.zkDTO = data.entity
                })
            },
            plSubmmit(formName) {

                if (this.selected.length == 0) {
                    this.$message(this.GLOBAL.MSG.SELECTED_ERR);
                    return;
                }
                let ids = [];
                this.selected.forEach((row) => {
                    ids.push(row.id)
                });
                this.editFormPL.toStockNo = this.stocks.find((e) => e.id == this.editFormPL.toStockId).stockNo
                this.editFormPL.toShelfNo = this.shelves.find((e) => e.id == this.editFormPL.toShelfId).shelfNo

                let cus = {}
                cus.ids = ids;
                cus.inStock = this.editFormPL;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        const loading = this.$loading({
                            lock: true,
                            text: 'Loading',
                            spinner: 'el-icon-loading',
                            background: 'rgba(0, 0, 0, 0.7)'
                        });
                        stockModel.plInStock(cus).then(data => {
                            if (data.status == 'success') {
                                if(data.msg == 'success'){
                                    data.msg = '入库成功'
                                }
                                Notification({
                                    type: 'success',
                                    message: data.msg
                                })
                                loading.close();
                                this.visiblePL = false;
                                this.list();
                            } else {
                                Notification({
                                    type: 'warning',
                                    message: data.msg
                                })
                                loading.close();
                            }
                        }).catch(err => {
                            loading.close();
                        })
                    }
                })
            },
            plFormShow() {
                this.visiblePL = true
                this.getTrays();
            },
            plCancel() {
                this.visiblePL = false;
            },
            changeTray(id){

                if(id){
                    let tary = this.trays.find((e) => e.id == id);
                    if(tary.shelfId){
                        this.editFormPL.toStockId = tary.stockId;
                        this.changeStock(this.editFormPL.toStockId);
                        this.visibleTray = true;
                        this.editFormPL.toShelfId = tary.shelfId;
                    }else{
                        this.visibleTray = false;
                    }
                }else{
                    this.visibleTray = false;
                }

            },
            changeStock(id) {
                stockModel.getShelfsById(id).then(data => {
                    this.shelves = data.entity
                    this.shelves = this.shelves.filter(s => s.shelfStatus == '启用')

                })
            },
            showSubData(row, expandedRows) {
                let id = row.id;
                if (this.expands.some((e) => e == row.id)) {
                    this.expands = []
                } else {
                    this.expands = []
                    this.expands.push(row.id)
                }
                //发送请求-获取信息
                model.getSubDataById(id).then(data => {
                    let list = data.entity.checkList;
                    for (let i = 0; i < list.length; i++) {
                        let checkDetail = list[i];
                        checkDetail.items = []; //循环创建检验明细数组
                    }
                    for (let i = 0; i < list.length; i++) {
                        let checkDetail = list[i];
                        let json = checkDetail.itemResultJson;
                        if (json != null || json != undefined) {
                            let pa = JSON.parse(json);

                            pa.forEach(e => {
                                let item = {}
                                item.name = e.name;
                                item.result = e.result;
                                checkDetail.items.push(item);
                            })

                        }
                    }
                    this.checkLists = list;
                });
                model.showSubData('check/productDetail', id).then(data => {
                    this.checkLists = data.entity.checkList;
                    if (this.checkLists != null) {
                        this.checkLists.forEach((check) => {
                            check.items = JSON.parse(check.itemResultJson)

                            if (check.items) {
                                for (let i = check.items.length - 1; i >= 0; i--) {
                                    if (check.items[i].result == '合格') {
                                        check.items.splice(i, 1);        //2019年11月1日17:08:12 李强 合格项不再显示
                                    }
                                }
                            }
                            let imgs = []
                            if (check.checkImages) {
                                check.checkImages.split(";").forEach((fileName) => {
                                    imgs.push({
                                        id: fileName,
                                        src: serverConfig.baseURL + '/file?tableName=ck_check&id=' + check.id + "&fileName=" + fileName
                                    });
                                })
                            }
                            check.checkImages = imgs;
                        })
                    }
                })

            },
            changeCriteria(entity) {
                this.entity = entity
                this.init()
                this.list()
            },
            //表查询
            list() {
                // 给库区货架input赋值
                if (this.$route.params.stockId && this.$route.params.stockId) {

                    this.$refs.productDetailCriteria.entity.stockId = this.entity.stockId = this.$route.params.stockId
                    this.$refs.productDetailCriteria.changeStock(this.entity.stockId)
                    this.$refs.productDetailCriteria.entity.shelfId = this.entity.shelfId = this.$route.params.shelfId
                }

                this.entity.listType = 'stock'
                this.entity.productStatus = this.productStatus
                model.list('productDetail', this.entity, this.pageInfo).then(data => {
                    let entity = data.entity
                    let list = data.entity.content;
                    list.forEach((pd) => {
                        let recordList = pd.recordList
                        if (recordList != null && recordList.length > 0) {
                            let inRecord = recordList.find((record) => {
                                return record.inOutType == '入库'
                            })
                            pd.inRecord = inRecord
                        }

                        let checkList = pd.checkList
                        // if (checkList != null && checkList.length > 0) {
                        //     pd.check = checkList[0]
                        // }

                        console.log(checkList)
                        if (checkList != null && checkList.length > 0) {
                            let checkInfo = checkList.find((check1) => {
                                if(check1.checkType=='成品检'){
                                    return check1;
                                }
                            })

                            if(!checkInfo || !checkInfo.checkResult){
                           //     console.log(checkList)
                            }


                            pd.check = checkInfo
                        }


                        if (pd.check) {


                            if (pd.check.checkResult == '合格') {
                                pd.check.checkResult = '合格品'
                            } else if (pd.check.checkResult.indexOf('返修') != -1) {
                                pd.check.checkResult = '不良品'
                            } else if (pd.check.checkResult.indexOf('报废') != -1) {
                                pd.check.checkResult = '报废品'
                            }else{

                                pd.check.checkResult = pd.check.checkResult
                                //console.log(pd.check.checkResult+"123")

                            }
                        }
                    })

                    this.entities = list

                    this.pageInfo.total = data.entity.totalElements
                })
            },
            query() {
                this.pageInfo.currentPage = 1
                this.list()
            },
            inScrap(row) {
                this.editFormScrap = row
                this.editFormVisibleScrap = true
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
            momentFormat(row, column, cellValue, index) {
                if (cellValue) {
                    return moment(cellValue).format('YYYY-MM-DD HH:mm:ss')
                } else {
                    return cellValue
                }

            },
            //编辑表单
            modelEditFinish() {
                this.editFormVisible = false
                this.list()
            },
            modelEditCancel() {
                this.editFormVisible = false
            },
            scrapEditFinish() {
                this.editFormVisibleScrap = false
                this.list()
            },
            scrapEditCancel() {
                this.editFormVisibleScrap = false
            },
            //行操作
            inRecord(row) {
                this.productDetail = row
                this.editFormVisible = true
            },
            showCheckImages(row){
                this.checkImagesVisible=true;
                this.checkSelected=row;
            }

        },
        watch:{
            checkImagesVisible:function(n,o){
                if(!n){
                    this.checkSelected=null;
                }
            }
        }

    }
</script>
<style>
    .el-table th,
    .el-table tr.header-row {
        background-color: #f5f7fa;
        font-size: 1.1em;
    }

    #entity .el-form-item {
        margin-bottom: 5px;
    }


    .red {
        color: red;
    }

    .green {
        color: green;
    }
</style>
