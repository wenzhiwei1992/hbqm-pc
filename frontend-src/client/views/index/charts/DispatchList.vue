<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                id="query"
                                :model="query"
                                ref="query"
                        >
                            <el-form-item prop="projectId">
                                <el-select
                                        v-model="query.projectId"
                                        placeholder="项目名称"
                                        clearable
                                        filterable
                                        @change="changeProject"
                                        style="width: 150px;"
                                >
                                    <el-option
                                            v-for="item in projectList"
                                            :key="item.id"
                                            :label="item.projectName"
                                            :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="buildingId">
                                <el-select
                                        v-model="query.buildingId"
                                        @change="changeBuilding"
                                        placeholder="楼栋"
                                        :disabled="query.projectId==''"
                                        style="width: 150px;"
                                        clearable
                                        filterable
                                >
                                    <el-option
                                            :key="item.id"
                                            :value="item.id"
                                            v-for="item in buildings"
                                            :label="item.buildCode"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="floorId">
                                <el-select
                                        v-model="query.floorId"
                                        :disabled="query.buildingId==''"
                                        clearable
                                        filterable
                                        style="width: 150px;"
                                        placeholder="楼层"
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
                            <el-form-item prop="invoiceNo">
                                <el-input
                                        placeholder="发货单号"
                                        v-model="query.deliverGoodsNo"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <!-- <el-form-item prop="orgId">
                                 <el-select
                                         v-model="query.orgId"
                                         style="width: 150px"
                                         placeholder="基地名称"
                                         clearable
                                 >
                                     <el-option
                                             v-for="item in orgs"
                                             :key="item.id"
                                             :label="item.orgName"
                                             :value="item.id"
                                     >
                                     </el-option>
                                 </el-select>
                             </el-form-item>-->
                            <el-form-item prop="productName">
                                <el-input
                                        placeholder="构件名称"
                                        v-model="query.productName"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="invoiceDateRange" >
                                <el-date-picker
                                        v-model="query.invoiceDateRange"
                                        type="daterange"
                                        align="right"
                                        unlink-panels
                                        range-separator="-"
                                        start-placeholder="开始日期"
                                        end-placeholder="结束日期"
                                        value-format="yyyy-MM-dd"
                                        :default-time="['00:00:00', '23:59:59']"
                                        style="width:300px;">
                                </el-date-picker>
                            </el-form-item>
                            <!-- <el-form-item prop="confirmationDate">
                                 <el-date-picker
                                         type="date"
                                         style="width: 150px;"
                                         size="mini"
                                         placeholder="确认日期"
                                         v-model="query.confirmationDate"
                                         value-format="yyyy-MM-dd"
                                 ></el-date-picker>
                             </el-form-item>
                            <el-form-item prop="invoiceDate">
                                <el-date-picker
                                        type="date"
                                        style="width: 150px;"
                                        size="mini"
                                        placeholder="发货日期"
                                        v-model="query.invoiceDate"
                                        value-format="yyyy-MM-dd"
                                ></el-date-picker>
                            </el-form-item>-->
                        </el-form>
                    </el-col>
                    <el-col>
                        <my-search-button-group :model="searchButtonData"></my-search-button-group>
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
                        @selection-change="handleSelectionChange"
                        :default-sort="{prop: 'dateCreated', order: 'descending'}"
                        size="mini"
                        border
                        ref="handleClickRowSelection"
                        height="100%"
                        @row-click="handleClickRowSelection"
                        style="width:100%"
                >
                    <el-table-column
                            type="index"
                            width="50"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="deliverGoodsNo"
                            label="发货单号"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="invoiceNo"
                            label="批次号"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="invoiceDate"
                            label="发货日期"
                            width="120"
                            :formatter="momentFormat"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productType"
                            label="构件类型"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productName"
                            label="构件名称"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="projectName"
                            label="项目名称"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="buildCode"
                            label="楼栋"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            label="楼层"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productVol"
                            label="构件体积(m³)"
                            width="150"
                    >
                    </el-table-column>
                    <!-- <el-table-column
                              prop="thermalInsulationLayer"
                              label="是否含保温层"
                              width="150">
                      </el-table-column> -->
                    <el-table-column
                            prop="productGrade"
                            label="混凝土等级"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productNum"
                            label="构件数"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productWtNum"
                            label="构件总重量"
                            :formatter="wtFormat"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productConcreteNum"
                            label="构件总方量"
                            :formatter="wtFormat"
                            width="120"
                    >
                    </el-table-column>
                    <!--  <el-table-column
                                prop="totalWt"
                                label="构件重量(t)"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="num"
                                label="数量"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                sortable
                                prop="sumNum"
                                label="合计数量"
                                width="120">
                        </el-table-column>-->
                    <el-table-column
                            sortable
                            prop="company"
                            label="物流公司"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="licenseNo"
                            label="车牌号"
                            width="120"
                    >
                    </el-table-column>
                </el-table>
                <!--:span-method="objectSpanMethod"-->
                <el-table
                        :data="exportdatas"
                        @selection-change="handleSelectionChange"
                        :default-sort="{prop: 'dateCreated', order: 'descending'}"
                        size="mini"
                        border
                        height="400"
                        id="exportXlsx"
                        hidden
                        style="width:100%"
                >
                    <el-table-column
                            prop="index"
                            label="序号"
                            width="50"
                    >
                    </el-table-column>
                    <!--             <el-table-column
                                         sortable
                                         prop="orgName"
                                         label="基地名称"
                                         width="120"
                                 >
                                 </el-table-column>-->
                    <el-table-column
                            sortable
                            prop="deliverGoodsNo"
                            label="发货单号"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="invoiceNo"
                            label="批次号"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="invoiceDate"
                            label="发货日期"
                            width="120"
                            :formatter="momentFormat"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productType"
                            label="构件类型"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productName"
                            label="构件名称"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="projectName"
                            label="项目名称"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="buildCode"
                            label="楼栋"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            label="楼层"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productVol"
                            label="构件体积(m³)"
                            width="150"
                    >
                    </el-table-column>
                    <!-- <el-table-column
                              prop="thermalInsulationLayer"
                              label="是否含保温层"
                              width="150">
                      </el-table-column> -->
                    <el-table-column
                            prop="productGrade"
                            label="混凝土等级"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productNum"
                            label="构件数"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productWtNum"
                            label="构件总重量"
                            :formatter="wtFormat"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productConcreteNum"
                            label="构件总方量"
                            :formatter="wtFormat"
                            width="120"
                    >
                    </el-table-column>
                    <!--  <el-table-column
                                prop="totalWt"
                                label="构件重量(t)"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="num"
                                label="数量"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                sortable
                                prop="sumNum"
                                label="合计数量"
                                width="120">
                        </el-table-column>-->
                    <el-table-column
                            sortable
                            prop="company"
                            label="物流公司"
                            width="120"
                    >
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="licenseNo"
                            label="车牌号"
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
                            :current-page="query.pageInfo.currentPage"
                            :page-sizes="query.pageInfo.pageSizes"
                            :page-size="query.pageInfo.pageSize"
                            :total="query.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
        </el-container>
    </div>
</template>

<script>
    import floorModel from "../../../model/project/floor";

    var numeral = require('numeral')
    import model from '../../../model/charts/InvoiceChart'
    import orgModel from '../../../model/system/org'
    import buildingModel from '../../../model/project/building'
    import clientModel from '../../../model/client-model'
    import xlsx from 'xlsx';
    import fileSaver from 'file-saver';
    import serviceConfig from '../../../../server.config.js'
    import {Notification} from 'element-ui'
    import moment from "moment"
    import projectModel from "../../../model/project/project";

    export default {
        data() {
            return {
                rowList: [],
                spanArr: [],
                position: 0,
                datas: [],
                exportdatas: [],
                orgs: [],
                query: {
                    deliverGoodsNo: '',
                    orgId: '',
                    projectId: '',
                    buildingId: '',
                    floorId: '',
                    invoiceDateRange: ['', ''],
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                selected: [],
                buildings: [],
                floors: [],
                projectList: [],
                buttonData: [
                    {
                        name: '导出',
                        event: 'exportExcel'
                    }
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "submitForm('query')"
                    }
                ],
                rowButtonData: []

            }
        },
        mounted() {
            this.list()
            this.info()
        },
        methods: {
            list() {
                model.invoiceChart(this.query).then((data) => {
                    this.datas = data.entity.content
                    this.query.pageInfo.total = data.entity.total
                    console.log('row--->', data.entity.content)
                    //    this.rowspan()
                })
            },
            info() {
                orgModel.orgAllByOrgIds().then((data) => {
                    this.orgs = data.entity
                })
                projectModel.projectListS().then(data => {
                    this.projectList = data.entity;
                }).catch(() => {
                })

            },
            changeProject() {
                buildingModel.findByProjectId(this.query.projectId).then(data => {
                    this.buildings = data.entity
                    this.query.buildingId = ''
                    this.query.floorId = ''
                    this.floors = []
                })
            },
            changeBuilding(id) {
                this.query.floorId = ''
                this.floors = []
                if (id == undefined || id == '') {
                    return
                }
                let building = this.buildings.find(b => b.id == id)
                floorModel.findByBuildingIdAndProjectId(id, building.projectId).then((data) => {
                    this.floors = data.entity
                    this.query.floorId = ''
                })
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.list()
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            handleSizeChange(val) {
                this.query.pageInfo.pageSize = val
                this.list()
            },
            handleCurrentChange(val) {
                this.query.pageInfo.currentPage = val
                this.list()
            },
            rowspan() {
                //   this.datas.forEach((val,index)=>{
                //     if(index===0){
                //       this.spanArr.push(1);
                //       this.position=0;
                //     }else{
                //       if(this.datas[index].deliverGoodsNo===this.datas[index-1].deliverGoodsNo){
                //         this.spanArr[this.position]+=1;
                //         this.spanArr.push(0);
                //       }else{
                //         this.spanArr.push(1);
                //         this.position=index;
                //       }
                //     }
                //   })
            },
            // objectSpanMethod({row,column,rowIndex,columnIndex}){//行合并
            //   if(columnIndex===0 || columnIndex===1||columnIndex===2 ||columnIndex===4 ||columnIndex===13||columnIndex===14){//序号为1的合并   4 12 13 14
            //     const _row = this.spanArr[rowIndex];
            //     const _col = _row>0 ? 1 : 0;
            //     return{
            //       rowspan:_row,
            //       colspan:_col
            //     }
            //   }
            // },
            objectSpanMethod({row, column, rowIndex, columnIndex}) {

                let rowCount = row.rowCount;
                let typeIndex = row.typeIndex;
                if (columnIndex === 0 || columnIndex === 1 || columnIndex === 4 || columnIndex === 12 || columnIndex === 13 || columnIndex === 14) {//第一列合并
                    if (typeIndex === 1) {
                        return {
                            rowspan: rowCount,
                            colspan: 1
                        };
                    } else {
                        return {
                            rowspan: 0,
                            colspan: 0
                        };
                    }
                }

            },
            exportExcel() {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                let page = this.query.pageInfo.currentPage;
                let size = this.query.pageInfo.pageSize;
                // console.log(page)
                // console.log(size)
                this.query.pageInfo.currentPage = 1;
                this.query.pageInfo.pageSize = this.query.pageInfo.total;
                //TODO 分页


                model.invoiceChart(this.query).then((data) => {
                    this.exportdatas = data.entity.content;
                    setTimeout(() => {
                        var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
                        excelName = "发货单数据报表" + excelName;

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

                        this.query.pageInfo.pageSize = size;
                        this.query.pageInfo.currentPage = page;
                        loading.close();
                        return wbout;
                    }, 500);

                }).catch(err => {

                })


            },
            wtFormat(row, column, cellValue, index) {
                if (cellValue) {
                    return numeral(cellValue).format('0.000')
                } else {
                    return cellValue
                }

            },
            momentFormat(row, column, cellValue, index) {
                if (cellValue) {
                    return moment(cellValue).format('YYYY-MM-DD')
                } else {
                    return cellValue
                }

            },
        },

    }
</script>

<style scoped>
</style>
