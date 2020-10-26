<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true">
                            <el-form-item prop="currentDate">
                                <el-date-picker
                                        v-model="currentDate"
                                        type="month"
                                        value-format="yyyy-MM-DD"
                                        placeholder="选择月"
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
                <div id="data_info">
                    <table id="table">
                        <thead>
                        <tr>
                            <th
                                    colspan="18"
                                    style="text-align: center"
                            >{{currentDate}}质量报表
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td
                                    rowspan="2"
                                    style="vertical-align:middle;text-align: center;"
                            >车间
                            </td>
                            <td
                                    rowspan="2"
                                    style="vertical-align:middle;text-align: center;"
                            >班组
                            </td>
                            <td
                                    rowspan="2"
                                    style="vertical-align:middle;text-align: center;"
                            >类别
                            </td>
                            <td
                                    rowspan="2"
                                    style="vertical-align:middle;text-align: center;"
                            >被检数（块）
                            </td>
                            <td colspan="8">常见缺陷（块）</td>
                            <td
                                    rowspan="2"
                                    style="vertical-align:middle;text-align: center;"
                            >一次合格品
                            </td>
                            <td
                                    rowspan="2"
                                    style="vertical-align:middle;text-align: center;"
                            >检验数（块）
                            </td>
                            <td
                                    rowspan="2"
                                    style="vertical-align:middle;text-align: center;"
                            >一次合格率（块）
                            </td>
                            <td
                                    rowspan="2"
                                    style="vertical-align:middle;text-align: center;"
                            >当月修复量（块）
                            </td>
                            <td
                                    rowspan="2"
                                    style="vertical-align:middle;text-align: center;"
                            >待修复品（块）
                            </td>
                            <td
                                    rowspan="2"
                                    style="vertical-align:middle;text-align: center;"
                            >偶发缺陷
                            </td>
                        </tr>
                        <tr>
                            <td>外形尺寸</td>
                            <td>套筒筋偏位</td>
                            <td>钢筋外露尺寸</td>
                            <td>预埋件</td>
                            <td>破损</td>
                            <td>裂纹</td>
                            <td>蜂窝</td>
                            <td>堵塞</td>
                        </tr>
                        <tr>
                            <td
                                    rowspan="2"
                                    style="vertical-align:middle;text-align: center;"
                            >楼板车间
                            </td>
                            <td></td>
                            <td>隐蔽检验</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>0</td>
                            <td
                                    rowspan="2"
                                    style="vertical-align:middle;text-align: center;"
                            >0
                            </td>
                            <td>0.00%</td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td>成品检验</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0</td>
                            <td>0.00%</td>
                            <td>0</td>
                            <td>0</td>
                            <td></td>
                        </tr>

                        <tr>
                            <td>备注</td>
                            <td colspan="17"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </el-main>
            <el-footer ref="footOperationBar">
                <el-row>
                    <!-- <el-pagination
                                  @size-change="handleSizeChange"
                                  @current-change="handleCurrentChange"
                                  :current-page="pageInfo.currentPage"
                                  :page-sizes="pageInfo.pageSizes"
                                  :page-size="pageInfo.pageSize"
                                  :total="pageInfo.total"
                                  layout="total, sizes, prev, pager, next, jumper"
                                  background
                              >
                              </el-pagination> -->
                </el-row>
            </el-footer>
        </el-container>

    </div>
</template>
<script>
    import qualityReportModel from "../../../model/charts/qualityReport";
    import moment from "moment";
    import {Notification} from "element-ui";
    import xlsx from "xlsx";
    import fileSaver from "file-saver";

    export default {
        data() {
            return {
                currentDate: moment(new Date()).format("YYYY-MM-DD"),
                datas: [],
                table: "",
                buttonData: [
                    {
                        name: '导出',
                        event: 'exportCheck'
                    }
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "onclick"
                    }
                ],
                rowButtonData: []
            };
        },
        mounted() {
            this.list();
        },
        methods: {
            onclick() {
                this.list()
            },
            list() {
                let model = {
                    month: this.currentDate,
                    flag: false,
                }
                qualityReportModel.dayReport(model).then(data => {
                    this.datas = data.entity;
                    var html = "";
                    html += '<thead>';
                    html += '<tr>';
                    html += '<th colspan="10" style="text-align: center;border :solid 0.1px #696969;">' + moment(this.currentDate).format("YYYY-MM") + '质量报表</th>';
                    html += '</tr>';
                    html += '</thead>';
                    html += '<tbody>';
                    html += '<tr>';
                    html += '<td rowspan="1" style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">车间</td>';
                    html += '<td rowspan="1" style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">班组</td>';
                    html += '<td rowspan="1" style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">类别</td>';
                    html += '<td rowspan="1" style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">被检数（块）</td>';
                    html += '<td rowspan="1" style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">一次合格品</td>';
                    html += '<td rowspan="1" style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">检验数（块）</td>';
                    html += '<td rowspan="1" style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">一次合格率（块）</td>';
                    html += '<td rowspan="1" style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">当月修复量（块）</td>';
                    html += '<td rowspan="1" style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">待修复品（块）</td>';
                    html += '<td rowspan="1" style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">偶发缺陷</td>';
                    html += '</tr>';
                    for (var index = 0; index < this.datas.length; index++) {
                        var dataUnit = this.datas[index];
                        let rcl = dataUnit.checkNum;
                        let next_index = this.datas[index + 1];
                        if (next_index) {
                            rcl += this.datas[index + 1].checkNum
                        }

                        let ychgl = ((parseFloat(dataUnit.ychg) / parseFloat(dataUnit.checkNum) * 100).toFixed(2));
                        if (isNaN(ychgl)) {
                            ychgl = "0%";
                        } else {
                            ychgl = ychgl + "%";
                        }
                        html += '<tr>';
                        html += '<td rowspan="2" style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + dataUnit.lineName + '</td>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;"></td>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + dataUnit.checkType + '</td>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + dataUnit.checkNum + '</td>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + dataUnit.ychg + '</td>';
                        html += '<td rowspan="2" style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + rcl + '</td>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + ychgl + '</td>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + dataUnit.xfl + '</td>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + dataUnit.waitXF + '</td>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + 0 + '</td>';
                        html += '</tr>';
                        index++;
                        dataUnit = this.datas[index];
                        let ychgl2 = ((parseFloat(dataUnit.ychg) / parseFloat(dataUnit.checkNum) * 100).toFixed(2));
                        if (isNaN(ychgl2)) {
                            ychgl2 = "0%";
                        } else {
                            ychgl2 = ychgl2 + "%";
                        }
                        html += '<tr>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;"></td>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + dataUnit.checkType + '</td>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + dataUnit.checkNum + '</td>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + dataUnit.ychg + '</td>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + ychgl2 + '</td>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + dataUnit.xfl + '</td>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + dataUnit.waitXF + '</td>';
                        html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">' + 0 + '</td>';
                        html += '</tr>';
                    }
                    html += '<tr>';
                    html += '<td style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;">备注</td>';
                    html += '<td colspan="17" style="vertical-align:middle;text-align: center;border :solid 0.1px #696969;"></td>';
                    html += '</tr>';
                    html += '</tbody>';
                    let tableDiv = document.getElementById("table");
                    tableDiv.innerHTML = html;
                });
            },
            exportCheck() {
                /* generate workbook object from table */
                var wb = xlsx.utils.table_to_book(document.querySelector('#data_info'))

                /* get binary string as output */
                var wbout = xlsx.write(wb, {bookType: 'xlsx', bookSST: false, type: 'array'})
                try {
                    fileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), '日质量报表.xlsx')
                } catch (e) {
                    if (typeof console !== 'undefined') console.log(e, wbout)
                }
                return wbout
            },
        }
    };
</script>
<style scoped>
    #tab-select span {
        background: white;
        color: #444444;
    }

    #tab-select span.select {
        background: #a3a3a5;
        color: #444444;
    }

    table tr th {
        border: solid 0.1px #696969;
        text-align: center;
    }

    table tr td {
        border: solid 0.1px #696969;
        text-align: center;
    }

    table {
        border-collapse: collapse;
        width: 100%;
        line-height: 25px;
    }
</style>