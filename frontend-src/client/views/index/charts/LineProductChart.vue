<template>
    <div>
        <el-form :inline="true" :model="formInline" class="demo-form-inline" size="mini">


            <el-form-item label="项目名称" prop="projectId">
                <el-select v-model="formInline.projectId" placeholder="请选择" clearable>
                    <el-option
                            v-for="item in this.arrProject"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>


            <el-form-item label="浇筑日期">
                <el-date-picker
                        v-model="formInline.checkTime"
                        type="daterange"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        style="width: 250px"
                        :default-time="['00:00:00', '23:59:59']"
                >
                </el-date-picker>
            </el-form-item>


            <el-form-item>
                <el-button type="primary" @click="queryList">查询</el-button>
                <el-button type="primary" @click="exportExcel">导出</el-button>
            </el-form-item>
        </el-form>

        <el-table
                :data="datas"
                :span-method="objectSpanMethod"
                size="mini"
                stripe
                border
                row-key="id"
                id="exportXlsx"
                style="width: 100%"

        >
            <el-table-column
                    prop="projectName"
                    label="项目名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="typeName" align="center"
                    label="构件名称"
                    width="180">
            </el-table-column>
            <!--:prop = "'lineData['+index+'].id'"-->

            <template v-for="(item,index) in this.lineSelect">
                <el-table-column
                        :key="item.teamId"
                        :label="item.lineName"
                        align="center">
                    <el-table-column
                            :label="item.teamName"
                            :prop="'lineData.' + index + '.totalVol'"
                            align="center">
                    </el-table-column>
                </el-table-column>
            </template>

            <el-table-column label="合计" align="center">
                <el-table-column
                        prop="totalProductVol" align="center"
                        label="浇筑量（m³）"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="totalProjectVol" align="center"
                        label="总量"
                        width="120">
                </el-table-column>
            </el-table-column>
        </el-table>

    </div>

</template>

<script>
    import axios from 'axios'
    import model from '../../../model/model'
    import projectModel from '../../../model/project/project'
    import plateModel from '../../../model/basicdata/plate'
    import serviceConfig from '../../../../server.config.js'
    import orgModel from '../../../model/system/org'
    import {Notification} from 'element-ui'
    import xlsx from 'xlsx';
    import fileSaver from 'file-saver';
    import moment from "moment";

    export default {
        data() {
            return {
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: this.GLOBAL.pageSize,
                    pageSizes: this.GLOBAL.pageSizes
                },
                datas: [],
                orgs: [],
                exportdatas: [],
                formInline: {
                    checkTime: [],
                    projectId: '',
                    productName: '',
                    houseTypeName: '',
                    houseTypeId: '',
                    orgId: '',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    }
                },

                arrProject: [],//select下拉框（project）
                lineSelect: [],
                timeDefaultShow: ''
            }

        },
        mounted() {
            if (!this.timeDefaultShow) {
                let date = new Date().toLocaleDateString().replace("/", "-").replace("/", "-");
                this.formInline.checkTime = [date + ' 00:00:00', date + ' 23:59:59']
            }
            this.list()
            this.getProjects()
            plateModel.getProdlistAndTeam().then(data => {
                this.lineSelect = data.entity;
            })
        },
        methods: {
            /**
             * 合并单元格
             */
            objectSpanMethod({row, column, rowIndex, columnIndex}) {
                let length = this.lineSelect.length
                let rowCount = row.rowCount;
                let typeIndex = row.typeIndex;

                if (rowIndex >= this.datas.length - 4) {
                    if (columnIndex === length + 2) {
                        return [1, 2];
                    } else if (columnIndex === length + 3) {
                        return [0, 0];
                    }
                }


                if (this.lineSelect[columnIndex - 2]) {
                    let columnCount = this.lineSelect[columnIndex - 2].columnCount;
                    let columnCountIndex = this.lineSelect[columnIndex - 2].columnCountIndex;
                    if (columnCount && (rowIndex == this.datas.length - 3 || rowIndex == this.datas.length - 1) && columnIndex > 1) {
                        if (columnCountIndex === 1) {
                            return [1, columnCount];
                        } else {
                            return [0, 0];
                        }
                    }
                }


                if (rowIndex >= this.datas.length - 4) {
                    if (columnIndex === 0) {
                        return [1, 2];
                    } else if (columnIndex === 1) {
                        return [0, 0];
                    }
                }


                if (columnIndex == 0 || columnIndex >= 3 + length) {//第一列合并
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

                // if ( columnIndex ==2+length && rowIndex  == this.datas.length-4  ) {//第一列合并
                //     return {
                //         rowspan: 2,
                //         colspan: 1
                //     };
                // }
                // if ( columnIndex ==2+length && rowIndex  == this.datas.length-3  ) {//第一列合并
                //     return {
                //         rowspan: 0,
                //         colspan: 0
                //     };
                // }
                //
                // if ( columnIndex ==3+length && rowIndex  == this.datas.length-4  ) {//第一列合并
                //     return {
                //         rowspan: 2,
                //         colspan: 1
                //     };
                // }
                // if ( columnIndex ==3+length && rowIndex  == this.datas.length-3  ) {//第一列合并
                //     return {
                //         rowspan: 0,
                //         colspan: 0
                //     };
                // }
                //
                // if ( columnIndex ==2+length && rowIndex  == this.datas.length-2  ) {//第一列合并
                //     return {
                //         rowspan: 2,
                //         colspan: 1
                //     };
                // }
                // if ( columnIndex ==2+length && rowIndex  == this.datas.length-1  ) {//第一列合并
                //     return {
                //         rowspan: 0,
                //         colspan: 0
                //     };
                // }
                // if ( columnIndex ==3+length && rowIndex  == this.datas.length-2  ) {//第一列合并
                //     return {
                //         rowspan: 2,
                //         colspan: 1
                //     };
                // }
                // if ( columnIndex ==3+length && rowIndex  == this.datas.length-1  ) {//第一列合并
                //     return {
                //         rowspan: 0,
                //         colspan: 0
                //     };
                // }
                //


            },
            /**
             * 增加修改product为项目下拉框赋值
             */
            getProjects() {
                projectModel.findAllProjects().then((data) => {
                    let projects = data.entity
                    for (var j = 0; j < projects.length; j++) {
                        var pName = {
                            value: projects[j].id,
                            label: projects[j].projectName,
                            molds: []
                        }
                        this.arrProject.push(pName)
                    }
                })
            },
            exportExcel() {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                //TODO 分页
                model.getLineChartByProductTypeAndProject(this.formInline).then(data => {
                    this.exportdatas = data.entity.list;
                    setTimeout(() => {
                        let excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
                        excelName = "构件产能报表" + excelName;
                        // console.log(excelName)
                        /* generate workbook object from table */
                        let table = document.querySelector('#exportXlsx');
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
            queryList() {
                this.pageInfo.currentPage = 1;
                this.list();
            },

            list() {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });

                this.formInline.pageInfo = this.pageInfo;
                //TODO 分页
                model.getLineChartByProductTypeAndProject(this.formInline).then(data => {
                    this.datas = data.entity.list;
                    console.log(data)
                    this.pageInfo.total = data.entity.totalCount;
                    loading.close();
                }).catch(err => {

                })
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize = val;
                this.list()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val;
                this.list()
            },
        }
    }
</script>

<style scoped>

</style>
