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
                            :selected="selected"
                            @change-criteria="changeCriteria"
                            @export-excel="exportExcel"
                            :zkDTO="zkDTO"
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
                        tooltip-effect="light"
                        @expand-change="showSubData"
                        :expand-row-keys="expands"
                        row-key="productDetailId"
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
                            prop="orderNo"
                            label="订单编号"
                            align="center"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="inBoundOrder"
                            label="入库单号"
                            align="center"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="projectName"
                            label="项目名称"
                            align="center"
                            width="160"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="buildCode"
                            label="楼栋"
                            align="center"
                            width="80"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            label="楼层"
                            align="center"
                            width="80"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="detailNo"
                            label="明细编码"
                            align="center"
                            width="120"
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
                            prop="name"
                            label="构件类型"
                            align="center"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="checkResult"
                            label="构件属性"
                            align="center"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="stockNo"
                            label="库区号"
                            align="center"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="shelfNo"
                            label="货架号"
                            align="center"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="trayNo"
                            label="托盘号"
                            align="center"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="inStockTime"
                            label="入库日期"
                            sortable
                            align="center"
                            width="200"
                            :formatter="momentFormat"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="modifiedByName"
                            label="操作人"
                            align="center"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            :width="rowButtonGroupWidth(rowButtonData)"
                            align="left"
                            fixed="right"
                    >
                        <template slot-scope="scope">
                            <my-row-button-group
                                    :row="scope.row"
                                    :model="rowButtonData"
                            ></my-row-button-group>
                        </template>
                    </el-table-column>
                </el-table>

<!--:summary-method="getSummaries"-->
                <el-table
                        :data="exportdatas"
                        id="exportXlsx"
                        hidden
                        height="100%"
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                        style="width: 100%"


                >
                    <el-table-column
                            type="index"
                            label="序号"
                            width="70"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="orderNo"
                            label="订单编号"
                            align="center"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="inBoundOrder"
                            label="入库单号"
                            align="center"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="projectName"
                            label="项目名称"
                            align="center"
                            width="160"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="buildCode"
                            label="楼栋"
                            align="center"
                            width="80"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            label="楼层"
                            align="center"
                            width="80"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="detailNo"
                            label="明细编码"
                            align="center"
                            width="120"
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
                            prop="name"
                            label="构件类型"
                            align="center"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="checkResult"
                            label="构件属性"
                            align="center"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="stockNo"
                            label="库区号"
                            align="center"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="shelfNo"
                            label="货架号"
                            align="center"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="trayNo"
                            label="托盘号"
                            align="center"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="inStockTime"
                            label="入库日期"
                            sortable
                            align="center"
                            width="200"
                            :formatter="momentFormat"
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
                            prop="productGrade"
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

        <el-dialog
                title="入库"
                v-if="editFormVisible"
                :visible.sync="editFormVisible"
                width="30%"
        >
            <model-edit
                    :product-detail="productDetail"
                    @model-edit-finish="modelEditFinish"
                    @model-edit-cancel="modelEditCancel"
            ></model-edit>
        </el-dialog>


        <el-dialog
                title="堆场报废/返修"
                :visible.sync="editFormVisibleScrap"
                v-if="editFormVisibleScrap"
                width="500px"
                top="10px"
        >
            <scrap-type-list
                    :productDetail="editFormScrap"
                    checkType="inStock"
                    @model-edit-finish="scrapEditFinish"
                    @model-edit-cancel="scrapEditCancel"
            ></scrap-type-list>
        </el-dialog>
    </div>
</template>
<script>
    import model from '../../../model/model'
    import moment from 'moment'
    import productDetailModel from '../../../model/project/productDetail'
    import RecordEdit from './RecordEdit.vue'
    import ProductDetailCriteria from './ProductDetailCriteria.vue'
    import FinishCheckEdit from "../check/FinishCheckEdit.vue";
    import xlsx from 'xlsx';
    import fileSaver from 'file-saver';
    import menuTabService from "../../../service/menuTabService";

    export default {
        components: {
            'model-edit': RecordEdit,
            'product-detail-criteria': ProductDetailCriteria,
            //  'scrap-type-list': ScrapTypeList,
            'scrap-type-list': FinishCheckEdit,
        },
        data() {
            return {
                //表查询
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: 20,
                    pageSizes: this.GLOBAL.pageSizes
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
                editFormScrap: '',
                editFormVisibleScrap: false,
                zkDTO: {
                    totalCount: '',
                    totalProductVol: '',
                    totalProjectConcrete: '',
                },
                buttonData: [
                    {
                        name: '新建'
                    }
                ],
                rowButtonData: [
                    {
                        name: '报废/返修',
                        event: "check",
                        disabled: "scope.row.checkResult=='不良品'",
                        authorized: 'productDetailInRecordList:scrapRecord'
                    },

                ],
            }
        },
        computed: {
            productStatus() {
                let productStatus = '成品入库'
                return productStatus
            },

        },
        mounted() {
            this.list()
        },
        methods: {
            //行操作
            check(row) {
                console.log(row)
                this.editFormScrap = row
                this.editFormVisibleScrap = true
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

                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                this.entity.pageInfo =  this.pageInfo;
                productDetailModel.findInStocklist(this.entity).then(data => {
                    this.zkDTO = data.entity
                    this.exportdatas = data.entity.list
                    this.exportdatas.forEach((pd) => {
                        if(pd.checkResult){
                            if (pd.checkResult == '合格') {
                                pd.checkResult = '合格品'
                            } else if (pd.checkResult.indexOf('返修') != -1) {
                                pd.checkResult = '不良品'
                            } else if (pd.checkResult.indexOf('报废') != -1) {
                                pd.checkResult = '报废品'
                            }
                        }

                    })
                    for (let i = 0; i < this.exportdatas.length; i++) {
                        // rows[i].productWt = rows[i].productWt;
                        // rows[i].productVol = rows[i].productVol;
                        this.exportdatas[i].num = 1;
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
                }).catch(err => {
                    loading.close();
                })


            },
            exportExcel(rows,entity) {
                this.exportdatas = rows;
                if(!rows || !rows.length){
                    this.exportExcelAll(rows,entity);
                   return;
                }
                for (let i = 0; i < rows.length; i++) {
                    // rows[i].productWt = rows[i].product.productWt;
                    // rows[i].productVol = rows[i].product.productVol;
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
            showSubData(row, expandedRows) {
                let id = row.productDetailId;
                if (this.expands.some((e) => e == row.productDetailId)) {
                    this.expands = []
                } else {
                    this.expands = []
                    this.expands.push(row.productDetailId)
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
                    console.log(this.checkLists)
                });

            },
            changeCriteria(entity) {
                this.entity = entity;
                this.pageInfo.currentPage=1;
                this.list();
            },
            //表查询
            list() {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                this.entity.pageInfo =  this.pageInfo;
                productDetailModel.findInStocklist(this.entity).then(data => {
                    this.zkDTO = data.entity
                    this.entities = data.entity.list
                    this.entities.forEach((pd) => {
                        if(pd.checkResult){
                            if (pd.checkResult == '合格') {
                                pd.checkResult = '合格品'
                            } else if (pd.checkResult.indexOf('返修') != -1) {
                                pd.checkResult = '不良品'
                            } else if (pd.checkResult.indexOf('报废') != -1) {
                                pd.checkResult = '报废品'
                                
                            }
                        }

                    })
                    this.pageInfo.total = data.entity.totalCount
                    loading.close();
                }).catch(err => {
                    loading.close();
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

        },

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
