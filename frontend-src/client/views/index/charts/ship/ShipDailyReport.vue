<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                id="invoiceDailyReportQuery"
                                :model="invoiceDailyReportQuery"
                                ref="invoiceDailyReportQuery"
                        >
                            <el-form-item prop="projectId">
                                <el-select
                                        v-model="invoiceDailyReportQuery.projectId"
                                        placeholder="项目名称"
                                        clearable
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
                            <el-form-item prop="invoiceDate">
                                <el-date-picker
                                        style="width: 150px;"
                                        v-model="invoiceDailyReportQuery.invoiceDate"
                                        type="date"
                                        placeholder="发货时间"
                                        value-format="yyyy-MM-dd"
                                >
                                </el-date-picker>
                            </el-form-item>
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
                        :data="shipData"
                        :span-method="objectSpanMethod"
                        size="mini"
                        border
                        ref="handleClickRowSelection"
                        height="100%"
                        @row-click="handleClickRowSelection"
                        style="width:100%"
                        row-key="id"
                        id="exportXlsx"

                >
                    <el-table-column
                            type="index"
                            width="50"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="projectName"
                            label="项目名称"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="buildCode"
                            label="楼栋"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            label="楼层"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="shipNum"
                            label="发货数量(片)"

                    >
                    </el-table-column>
                    <el-table-column
                            prop="shipNumCount"
                            label="数量合计(片)"

                    >
                    </el-table-column>
                    <el-table-column
                            prop="shipVol"
                            label="发货体积(m³)"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="shipVolCount"
                            label="体积合计(m³)"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="busNumCount"
                            label="总车数"
                    >
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer ref="footOperationBar">
                <el-row>
                </el-row>
            </el-footer>
        </el-container>
    </div>
</template>

<script>
    import projectModel from "../../../../model/project/project";
    import planModel from '../../../../model/plan/plan'
    import moment from "moment";
    import xlsx from 'xlsx';
    import fileSaver from 'file-saver';

    export default {
        name: "ShipDailyReport",
        data() {
            return {
                exportdatas: [],
                shipData: [],
                invoiceDailyReportQuery: {
                    projectId: '',
                    invoiceDate: '',
                },
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
                        event: "submitForm('invoiceDailyReportQuery')"
                    }
                ],
            }
        },
        mounted() {
            if (this.invoiceDailyReportQuery.invoiceDate === '') {
                this.invoiceDailyReportQuery.invoiceDate = new Date()
            }
            this.list()
            this.info()
        },
        methods: {
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
            list() {
                planModel.findShipDailyReport(this.invoiceDailyReportQuery).then((data) => {
                    this.shipData = data.entity
                })
            },
            info() {
                projectModel.projectListS().then(data => {
                    this.projectList = data.entity;
                }).catch(() => {
                })
            },
            //当前行row
            //当前列column
            //当前行号rowIndex
            //当前列号columnIndex
            objectSpanMethod({row, column, rowIndex, columnIndex}) {
                let type = row.type
                let num = row.num
                if ((columnIndex === 1 || columnIndex === 5 || columnIndex === 7 || columnIndex === 8) && rowIndex !== this.shipData.length - 1) {
                    if (type) {
                        return {
                            rowspan: num,
                            colspan: 1
                        };
                    } else {
                        return {
                            rowspan: 0,
                            colspan: 0
                        };
                    }
                }
                if (rowIndex === this.shipData.length - 1) {
                    if (columnIndex === 1) {
                        return [1, 4];
                    } else if (columnIndex === 2 || columnIndex === 3 || columnIndex === 4 ) {
                        return [0, 0];
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
                //TODO 分页
                planModel.findShipDailyReport(this.invoiceDailyReportQuery).then((data) => {
                    this.exportdatas = data.entity.list;
                    setTimeout(() => {
                        let excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
                        excelName = "发货日报" + excelName;
                        // console.log(excelName)
                        /* generate workbook object from table */
                        let table = document.querySelector('#exportXlsx');
                        console.log(table)
                        let wb = xlsx.utils.table_to_book(table);
                        /* get binary string as output */
                        let wbout = xlsx.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'})
                        try {
                            fileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), excelName)
                        } catch (e) {
                            if (typeof console !== 'undefined') console.log(e, wbout)
                        }

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
        }
    }
</script>

<style scoped>

</style>
