<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form
                                :inline="true"
                                :model="formInline"
                                class="form-inline"
                        >
                            <el-form-item prop="projectName">
                                <el-select
                                        style="width: 170px"
                                        v-model="formInline.projectId"
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
                                        style="width:90px"
                                        v-model="formInline.buildId"
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
                                        style="width:90px"
                                        v-model="formInline.floorId"
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

                            <el-form-item prop="productTypeId">
                                <el-select v-model="formInline.productTypeId" placeholder="构件类型"
                                           filterable clearable  >
                                    <el-option
                                            v-for="item in this.productTypeSelect"
                                            :key="item.id"
                                            :label="item.name+'('+item.typeNo+')'"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item>
                                <el-input
                                        v-model="formInline.productName"
                                        placeholder="构件名称"
                                        clearable
                                        style="width: 130px"
                                ></el-input>
                            </el-form-item>


                            <el-form-item>
                                <el-input
                                        v-model="formInline.productNo"
                                        placeholder="明细编码"
                                        clearable
                                        style="width: 130px"
                                ></el-input>
                            </el-form-item>

                            <el-form-item prop="checkType">
                                <el-select v-model="formInline.checkType" size="mini" placeholder="生产工序" clearable
                                           @change="changeCheckType"
                                           style="width:110px;">
                                    <el-option label="计划下发" value="计划下发"></el-option>
                                    <el-option label="隐检" value="隐检"></el-option>
                                    <el-option label="浇筑确认" value="浇筑确认"></el-option>
                                    <el-option label="成品检" value="成品检"></el-option>
                                    <el-option label="报废审批" value="报废审批"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="checkResult">
                                <el-select v-model="formInline.checkResult" size="mini" placeholder="质检结果" clearable
                                           :disabled="!this.formInline.checkType"
                                           style="width:110px;">
                                    <el-option v-if="formInline.checkType=='计划下发'" label="计划下发" value="计划下发"></el-option>
                                    <el-option v-if="formInline.checkType=='隐检'" label="合格" value="合格"></el-option>
                                    <el-option v-if="formInline.checkType=='隐检'" label="隐检返修" value="隐检返修"></el-option>
                                    <el-option v-if="formInline.checkType=='浇筑确认'" label="浇筑确认" value="浇筑确认"></el-option>
                                     <el-option v-if="formInline.checkType=='成品检'" label="合格" value="合格"></el-option>
                                    <el-option v-if="formInline.checkType=='成品检'" label="返修" value="返修"></el-option>
                                    <el-option v-if="formInline.checkType=='报废审批'" label="待报废审批" value="待报废审批"></el-option>
                                    <el-option v-if="formInline.checkType=='报废审批'" label="报废" value="报废"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="dateRange">
                                <el-date-picker
                                        v-model="formInline.dateRange"
                                        type="daterange"
                                        align="right"
                                        unlink-panels
                                        range-separator="-"
                                        start-placeholder="开始日期"
                                        end-placeholder="结束日期"
                                        :default-time="['00:00:00', '23:59:59']"
                                        value-format="yyyy-MM-dd"
                                        style="width:300px;">
                                </el-date-picker>
                            </el-form-item>

                            <el-form-item>
                                <el-select
                                        v-model="formInline.teamId"
                                        placeholder="班组"
                                        style="width:130px"
                                        clearable
                                >
                                    <el-option
                                            v-for="item in teamInfos"
                                            :key="item.id"
                                            :label="item.teamName"
                                            :value="item.id"
                                    ></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item>
                                <el-select
                                        v-model="formInline.lineId"
                                        placeholder="产线"
                                        style="width:130px"
                                        clearable
                                >
                                    <el-option
                                            v-for="item in lineSelect"
                                            :key="item.id"
                                            :label="item.lineName"
                                            :value="item.id"
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
                            width="70"
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
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            label="楼层"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productTypeName"
                            label="构件类型"
                            width="150"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productName"
                            label="构件名称"
                            width="200"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="productNo"
                            label="明细编码"
                            width="140"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="checkType"
                            label="生产工序"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="checkResult"
                            label="状态"
                            width="80"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="checkDate"
                            label="日期"
                            :formatter="formatterDate"
                            width="150"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="userName"
                            label="操作人"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="lineName"
                            label="产线"
                            width="130"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="teamName"
                            label="班组"
                            width="130"
                    >
                    </el-table-column>

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
                            prop="projectName"
                            label="项目名称"
                            width="100"
                            sortable
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
                    <el-table-column
                            prop="productTypeName"
                            label="构件类型"
                            width="150"
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
                            label="明细编码"
                            width="140"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="checkType"
                            label="生产工序"
                            sortable
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="checkResult"
                            label="状态"
                            width="80"
                            sortable
                    >
                    </el-table-column>
                    <el-table-column
                            prop="checkDate"
                            label="日期"
                            :formatter="formatterDate"
                            width="140"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="userName"
                            label="操作人"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="lineName"
                            label="产线"
                            width="100"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="teamName"
                            label="班组"
                            width="100"
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


    </div>
</template>
<script>
    import productChart from '@/model/charts/productChart.js'
    import combModel from '@/model/po/comb'
    import plateModel from '@/model/basicdata/plate.js'
    import buildModel from '@/model/project/building'
    import floorModel from '@/model/project/floor'
    import projectModel from '@/model/project/project'
    import productTypeModel from '@/model/basicdata/productType'
    import {Notification} from 'element-ui'
    import moment from "moment";
    import fileSaver from 'file-saver';
    import xlsx from 'xlsx';

    export default {
        data() {
            return {
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: this.GLOBAL.pageSize,
                    pageSizes: [20,50,100,200,500,1000]
                },
                datas: [],
                expands: [],
                selected: [],
                exportdatas: [],
                productTypeSelect:[],
                lineSelect: [],
                teamInfos: [],
                formInline: {
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
                    productTypeId: '',
                    id: '',
                },
                productDetailInfo: null,
                buildings: [],
                floors: [],
                arrProject: [],//select下拉框（project）
                projectId: [],
                buttonData: [
                    {
                        name: '导出',
                        event: 'exportExcel'
                    },
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: 'onSubmit'
                    }
                ],
            }
        },
        mounted() {
            this.pageableList();
            this.info()
        },
        methods: {
            changeCheckType(){
                this.$set(this.formInline,'checkResult','')
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
                //提前加载teamInfos
                combModel.getAllTeamInfos().then(data => {
                    this.teamInfos = data.entity;
                });
                plateModel.getAllProdLinesByOrgId().then(data => {
                    this.lineSelect = data.entity;
                })
                productTypeModel.getOrgProductTypes().then(data => {
                    this.productTypeSelect = data.entity;
                })

            },

            exportExcel(){
                let self = this;
                console.log(self)
                let msg = "是否导出所选数据?";
                if (self.selected.length == 0) {
                    msg = "是否导出全部数据?";;
                }
                this.$confirm(msg, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    if (self.selected.length == 0) {
                        if(this.pageInfo.total>=10000){
                            Notification({
                                message: "导出数据过多",
                                type: 'warning'
                            })
                            return ;
                        }
                        this.exportAllExcel();
                    }else{
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
                    excelName = "构件生产履历" + excelName;
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

                let page = this.pageInfo.currentPage;
                let size = this.pageInfo.pageSize;

                this.formInline.pageInfo = this.pageInfo;
                this.formInline.pageInfo.currentPage = 1;
                this.formInline.pageInfo.pageSize = this.pageInfo.total;

                productChart.getProductRecordChartDetail(this.formInline).then(data => {
                    this.exportdatas = data.entity.list;
                    setTimeout(() => {
                        let excelName = new Date().toLocaleDateString().replace("/","-").replace("/","-")+".xlsx";
                        excelName = "构件生产履历"+excelName;
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

                        this.formInline.pageInfo.pageSize = size;
                        this.formInline.pageInfo.currentPage = page;
                        return wbout;
                    }, 500);

                }).catch(err => {

                })


            },
            formatterDate(row, column, cellValue, index) {
                let date = row[column.property];
                if (!date || date == "　") {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            onSubmit() {
                this.formInline.pageInfo.currentPage = 1;
                this.pageableList();
            },
            pageableList() {
                this.formInline.pageInfo = this.pageInfo;
                productChart.getProductRecordChartDetail(this.formInline).then(data => {
                    this.datas = data.entity.list;
                    this.pageInfo.total = data.entity.totalCount;
                }).catch(err => {
                    Notification({
                        message: err.msg,
                        type: 'warning'
                    })
                })
            },


            changeProjectSelect(id) {
                if (!id ) {
                    id = '0'
                }
                this.formInline.buildId = ''
                this.buildings = []
                this.projectId = id
                buildModel.findByProjectId(id).then((data) => {
                    this.buildings = data.entity
                })
            },
            changeBuildingSelect(id) {
                if (id == undefined) {
                    id = '0'
                }
                this.formInline.floorId = ''
                this.floors = []
                floorModel.findByBuildingIdAndProjectId(id, this.projectId).then((data) => {
                    this.floors = data.entity
                })
            },

            handleSizeChange(val) {
                this.formInline.pageInfo.currentPage = 1;
                this.pageInfo.pageSize = val
                this.pageableList()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val
                this.pageableList()
            },
            handleSelectionChange(selected) {
                this.selected = selected
            }

        }


    }

</script>
<style scoped>
</style>
