<template>
    <div
            id="product-detail-list"
            class="currency-style-box"
    >
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <product-detail-criteria
                            list-type="checkPro"
                            @change-criteria="changeCriteria"
                    ></product-detail-criteria>
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
                        :data="entities"
                        @selection-change="handleSelectionChange"
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                        height="100%"
                        header-row-class-name="header-row"
                        :expand-row-keys="expands"
                        @expand-change="showSubData"
                        size="mini"
                        stripe
                        border
                        tooltip-effect="light"
                        row-key="id"
                >
                    <el-table-column
                            type="selection"
                            width="40"
                    >
                    </el-table-column>
                    <el-table-column type="expand">
                        <template slot-scope="props">
                            <el-table :data="checkLists">
                                <el-table-column
                                        prop="checkType"
                                        label="检验类型"
                                        align="center"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="checkResult"
                                        label="检验结果"
                                        align="center"
                                >
                                </el-table-column>
                                <el-table-column
                                        label="检验明细"
                                        align="center"
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
                                                >
                                                    <template slot-scope="scope1">
                                                        <span style="color: #F56C6C;font-weight: 800;">{{scope1.row.result}}</span>
                                                    </template>
                                                </el-table-column>
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
                                >
                                    <template slot-scope="scope">
                                        <el-button
                                                icon="el-icon-search"
                                                round
                                                type="primary"
                                                v-if="scope.row.checkImages.length>0"
                                                @click="showCheckImages(scope.row)"
                                        >查看检验图片
                                        </el-button>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        prop="checkDesc"
                                        label="说明"
                                        align="center"
                                        :show-overflow-tooltip="true"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="checkPersonName"
                                        label="检验员"
                                        align="center"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="checkTime"
                                        label="检验日期"
                                        align="center"
                                        :formatter="momentFormat"
                                        width="150px"
                                        show-overflow-tooltip
                                >
                                </el-table-column>
                            </el-table>
                        </template>
                    </el-table-column>
                    <el-table-column
                            type="index"
                            width="60px"
                            align="center"
                            label="序号"
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
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorRelevance.building.buildCode"
                            label="楼栋"
                            width="60px"
                            align="center"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorRelevance.floor.floorNum"
                            label="楼层"
                            width="60px"
                            align="center"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product.productNo"
                            label="构件编码"
                            align="center"
                    >
                        <template slot-scope="scope">
                            <a href="javascript:void(0);" @click="loadProductInfo(scope.row.product.id)"
                               style="color: #409EFF;">
                                {{scope.row.product.productNo}}
                            </a>
                        </template>

                    </el-table-column>
                    <el-table-column
                            prop="detailNo"
                            label="明细编码"
                            align="center"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="product.productName"
                            label="构件名称"
                            align="center"
                    >
                    </el-table-column>


                    <el-table-column
                            prop="product.typwpeName"
                            label="构件类型"
                            align="center"
                    >
                    </el-table-column>

                    <el-table-column
                            prop="product.productVol"
                            label="构件体积"
                            align="center"
                    >
                    </el-table-column>

                    <el-table-column
                            prop="product.projectConcrete"
                            label="构件砼方量"
                            align="center"
                    >
                    </el-table-column>


                    <el-table-column
                            prop="product.productWt"
                            label="构件重量(t)"
                            align="center"
                            :formatter="wtFormat"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productStatus"
                            label="当前状态"
                            align="center"
                    ></el-table-column>
                    <el-table-column
                            prop="massLevel"
                            label="质量等级"
                            align="center"
                    ></el-table-column>
                    <el-table-column
                            prop="orgName"
                            label="基地名称"
                            align="center"
                            show-overflow-tooltip
                    ></el-table-column>
                    <!-- <el-table-column>
                        <template slot-scope="scope">
                            <my-row-button-group
                                    :row="scope.row"
                                    :model="rowButtonData"
                            ></my-row-button-group>
                        </template>
                    </el-table-column> -->
                    <!-- <el-table-column
                                      label="操作"
                                      align="center"
                                      width="200"
                                      >
                                  <template slot-scope="scope">
                                  &lt;!&ndash; <el-button size="medium" type="primary" v-if="scope.row.productStatus==null||scope.row.productStatus==''" @click="updateStatus(scope.row)">更新为待隐检</el-button>&ndash;&gt;
                                      &lt;!&ndash;<el-button size="mini" @click="qrcodeShow(scope.row)">查看二维码</el-button>&ndash;&gt;
                                  &lt;!&ndash; <el-button size="medium" type="success" @click="showProductDetail(scope.row)">查看构件明细</el-button>&ndash;&gt;
                                  </template>
                              </el-table-column>-->
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
                title="二维码"
                :visible.sync="qrcodeVisible"
                v-if="qrcodeVisible"
                width="40%"
        >
            <product-detail-qrcode
                    :id="id"
                    @qrcode-cancel="qrcodeCancel"
            ></product-detail-qrcode>
        </el-dialog>
        <el-dialog
                title="检验图片"
                :visible.sync="checkImagesVisible"
                width="800px"
                top="10px"
                v-if="checkSelected!=null"
        >
            <div
                    v-for="checkImage in checkSelected.checkImages"
                    :key="checkImage.id"
            >
                <h3>{{checkImage.id}}</h3>
                <img
                        :src="checkImage.src"
                        :alt="checkImage.id"
                        style="max-width:100%;"
                >
            </div>
        </el-dialog>
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
    </div>

</template>
<script>
    import model from '../../../model/model'
    import checkModel from '../../../model/check/check'

    import moment from 'moment'
    import {Notification} from 'element-ui'
    import ProductDetailQrCode from './ProductDetailQrcode.vue'
    import ProductDetailPrint from "./ProductDetailPrint.vue"
    import serverConfig from '../../../../server.config'
    import productDetailModel from '../../../model/project/productDetail'
    import ProductDetailCriteria from '../stock/ProductDetailCriteria.vue'
    import menuTabService from '../../../service/menuTabService'

    var numeral = require('numeral')
    export default {
        components: {
            'product-detail-qrcode': ProductDetailQrCode,
            'product-detail-print': ProductDetailPrint,
            'product-detail-criteria': ProductDetailCriteria
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
                    productStatus: '',
                    deliverDateRange: ['', '']
                },
                entities: [],
                //行操作
                qrcodeVisible: false,
                id: null,
                checkImagesVisible: false,
                checkSelected: null,
                checkPrintVisible: false,
                productDetailInfo: null,
                checkLists: [],
                expands: [],
                templetNo: [],
                buttonData: [
                    {
                        name: '隐检打印',
                        event: 'printBatchYJJLB'
                    },
                    {
                        name: '成品检打印',
                        event: 'printBatchCPJJLB'
                    }
                    ,
                    {
                        name: '成品合格证打印',
                        authorized: 'productDetailCheck:printBatchCheckExcel',
                        event: 'printBatchCheckExcel'
                    }
                ],
                // rowButtonData: [
                //     {
                //         name: '隐蔽检验记录表打印',
                //         event: 'printYJJLB'
                //     },
                //     {
                //         name: '成品检查记录表打印',
                //         event: 'printCPJJLB'
                //     },
                // ]
            }
        },
        computed: {},
        created() {

        },
        mounted() {
            this.list()
            this.getTempletNo()
        },
        methods: {
            authorized(index) {
                return (menuTabService.authorized(this.$store, index) || this.checkBtnName.name !="浇筑确认")
            },
            loadProductInfo(id) {
                this.$router.push({
                    path: '/check/checkProductInfo',
                    query: { id }
                })
            },
            // 获取系统模板
            getTempletNo() {
                model.findTempletByTempletNo('CPJ').then(data => {
                    if (data.entity.length) {
                        this.templetNo = data.entity
                    }
                })
            },
            printBatchCheckExcel() {
                let row = this.selected

                if (row && row.length) {
                    if (row.some(val => val.productStatus === '待隐检')) {
                        Notification({
                            message: '请选择正确的状态',
                            type: 'warning'
                        })
                        return
                    }

                    let productDetailIds = "";
                    this.selected.forEach((row1) => {
                        productDetailIds+=row1.id+",";
                    })
                    var url = serverConfig.baseURL + '/check/downloadCheckExcel'
                    var prams = {
                        "productDetailIds": productDetailIds,
                    }
                    var temp = document.createElement("form");
                    temp.action = url;
                    temp.method = "post";
                    temp.style.display = "none";
                    for (var x in prams) {
                        var opt = document.createElement("textarea");
                        opt.name = x;
                        opt.value = prams[x];
                        temp.appendChild(opt);
                    }
                    document.body.appendChild(temp);
                    temp.submit();
                    return temp;

                } else {
                    Notification({
                        message: '请至少选择一个',
                        type: 'warning'
                    })
                }
            },
            printBatchCPJJLB() {
                let row = this.selected
                if (row.some(val => val.productStatus !== '待入库' && val.productStatus !== '成品入库' && val.productStatus !== '成品入库' && val.productStatus !== '发货待确认' && val.productStatus !== '已发货')) {
                    Notification({
                        message: '请选择正确的状态',
                        type: 'warning'
                    })
                    return
                }
                if (row && row.length) {
                    let checkForPrint = {
                        checkType: 'CPJ',
                        productDetails: row
                    }
                    checkModel.getCheckInfoForPrint(checkForPrint).then((data) => {
                        if (data.status == 'failed') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            return
                        }
                        console.log('data.entity----------test>', data.entity)
                        let array = data.entity
                        this.$router.push({
                            name: 'ProductDetailPrintCPJJLB',
                            params: {array}
                        })
                    })

                } else {
                    Notification({
                        message: '请至少选择一个',
                        type: 'warning'
                    })
                }
            },
            printBatchYJJLB() {
                let row = this.selected
                if (row && row.length) {
                    this.$router.push({
                        name: 'ProductDetailPrintYJJLB',
                        params: {row}
                    })
                } else {
                    Notification({
                        message: '请至少选择一个',
                        type: 'warning'
                    })
                }
            },
            showSubData(row, expandedRows) {
                let id = row.id;
                if (this.expands.some((e) => e == row.id)) {
                    this.expands = []
                } else {
                    this.expands = []
                    this.expands.push(row.id)
                }
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
                this.list()
            },
            //表查询
            list() {
                this.entity.listType = 'check'
                model.list('productDetail', this.entity, this.pageInfo).then(data => {
                    let entity = data.entity
                    let list = data.entity.content;
                    this.entities = list
                    console.log(list)
                    this.pageInfo.total = data.entity.totalElements
                })
            },
            query() {
                this.pageInfo.currentPage = 1
                this.list()
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
            wtFormat(row, column, cellValue, index) {
                if (cellValue) {
                    return numeral(cellValue).format('0.000')
                } else {
                    return cellValue
                }

            },
            //编辑表单
            //行操作
            updateStatus(row) {
                row.productStatus = '待隐检'
                model.update('productDetail', row).then(data => {
                    this.list()
                })

            },
            qrcodeShow(row) {
                this.id = row.id
                this.qrcodeVisible = true
            },
            qrcodeCancel() {
                this.qrcodeVisible = false
            },
            printCancel() {
                this.checkPrintVisible = false
            },
            printFinish() {
                this.checkPrintVisible = true
            },
            showCheckImages(row) {
                this.checkImagesVisible = true;
                this.checkSelected = row;
            },
            showProductDetail(row) {
                productDetailModel.getProductDetailInfo(row).then((data) => {
                    console.log('productDetailInfo----------------->')
                    console.log(data.entity)
                    this.productDetailInfo = data.entity
                    //this.id = row.id
                    this.checkPrintVisible = true
                })
            },
            // printYJJLB(row) {
            //     row = [row]
            //     this.$router.push({
            //         name: 'ProductDetailPrintYJJLB',
            //         params: {row}
            //     })
            // },
            // printCPJJLB(row) {
            //     let templetNo = this.templetNo
            //     row = [row]
            //     this.$router.push({
            //         name: 'ProductDetailPrintCPJJLB',
            //         params: {row, templetNo}
            //     })
            // }


        },
        watch: {
            checkImagesVisible: function (n, o) {
                if (!n) {
                    this.checkSelected = null;
                }
            }
        }
    }
</script>
<style>
    #product-detail-list .red {
        color: red;
    }

    #product-detail-list .el-tag {
        cursor: pointer;
    }
</style>
