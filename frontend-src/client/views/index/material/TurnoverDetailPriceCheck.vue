<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true"
                                 id="projectList"
                                 :model="turnoverQuery"
                                 ref="turnoverQuery"
                                 style="padding-bottom:8px;">

                            <el-form-item prop="materialNo">
                                <el-input
                                        placeholder="物料编码"
                                        v-model="turnoverQuery.materialNo"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="materialName">
                                <el-input
                                        placeholder="物料名称"
                                        v-model="turnoverQuery.materialName"
                                        style="width:150px"
                                        clearable
                                >
                                </el-input>
                            </el-form-item>


                            <el-form-item prop="turnoverDNo">
                                <el-input
                                        placeholder="单据号"
                                        v-model="turnoverQuery.turnoverDNo"
                                        style="width:120px"
                                        clearable>
                                </el-input>
                            </el-form-item>


                            <el-form-item prop="shipmentsId">
                                <el-select
                                        v-model="turnoverQuery.shipmentsId" filterable
                                        style="width:120px"
                                        placeholder="供应商"
                                        clearable>
                                    <el-option
                                            v-for="item in shipmentsSelect"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item prop="type">
                                <el-select
                                        v-model="turnoverQuery.type" filterable
                                        style="width:120px"
                                        placeholder="单据类型"
                                        clearable>
                                    <el-option
                                            v-for="item in options"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item prop="checkPriceStatus">
                                <el-select
                                        v-model="turnoverQuery.checkPriceStatus" filterable
                                        style="width:120px"
                                        placeholder="核价状态"
                                        clearable>
                                    <el-option
                                            v-for="item in optionStatus"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </el-form-item>


                            <el-form-item prop="dateCreated">

                                <el-date-picker
                                        v-model="turnoverQuery.dates"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        type="daterange"
                                        range-separator="至"
                                        start-placeholder="创建时间开始"
                                        end-placeholder="创建时间结束"
                                        :default-time="['00:00:00', '23:59:59']">
                                </el-date-picker>
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
                    :style="`height:${this.$store.state.mainContentHeight}px`">
                <el-table
                        :data="turnovers"
                        @selection-change="handleSelectionChange"
                        size="mini"
                        stripe
                        border
                        height="100%"
                        @sort-change='sortChange'
                        ref="handleClickRowSelection"
                        @row-click="handleClickRowSelection"
                >

                    <el-table-column
                            type="selection"
                            width="80">
                    </el-table-column>
                    <el-table-column
                            type="index"
                            label="序号"
                            width="50">
                    </el-table-column>
                    <el-table-column
                            sortable='custom'
                            prop="materialStorageId"
                            label="原料编码"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            sortable='custom'
                            prop="materialName"
                            label="原料名称"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            sortable='custom'
                            prop="materialSpecification"
                            label="原料规格"
                            width="140">
                    </el-table-column>

                    <el-table-column
                            sortable='custom'
                            prop="amount"
                            label="数量"
                            width="140">
                    </el-table-column>

                    <el-table-column
                            sortable='custom'
                            prop="materialUnit"
                            label="单位"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            sortable='custom'
                            prop="univalence"
                            label="单价"
                            width="140">
                        <template slot-scope="scope">

                            <span v-if="scope.row.checkPriceStatus=='已核价'"
                                  style="font-weight:800;">
                                <el-input v-model="scope.row.univalence" disabled id="number" type="number"></el-input>
                            </span>
                            <span v-else>
                                <el-input v-model="scope.row.univalence" id="number" type="number"></el-input>
                            </span>


                        </template>
                    </el-table-column>
                    <el-table-column
                            sortable='custom'
                            prop="turnoverRecordNo"
                            label="收料单编号"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            sortable='custom'
                            prop="supName"
                            label="供方"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            sortable='custom'
                            prop="checkPriceStatus"
                            label="核价状态"
                            width="140">
                        <template slot-scope="scope">
                                        <span v-if="scope.row.checkPriceStatus=='已核价'"
                                              style="font-weight:800;">{{scope.row.checkPriceStatus}}</span>
                            <span v-else
                                  style="font-weight:800;">未核价</span>
                        </template>
                    </el-table-column>

                    <el-table-column
                            sortable='custom'
                            prop="checkPricePerson"
                            label="核价人"
                            width="140">
                    </el-table-column>

                    <el-table-column
                            sortable='custom'
                            prop="checkPriceTime"
                            :formatter="formatterDate"
                            label="核价时间"
                            width="140">
                    </el-table-column>
                    <el-table-column
                            sortable='custom'
                            prop="dateCreated"
                            :formatter="formatterDate"
                            label="创建时间"
                            width="140">
                    </el-table-column>

                </el-table>
            </el-main>
            <el-footer ref="footOperationBar">
                <el-row>
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="turnoverQuery.pageInfo.currentPage"
                            :page-sizes="turnoverQuery.pageInfo.pageSizes"
                            :page-size="turnoverQuery.pageInfo.pageSize"
                            :total="turnoverQuery.pageInfo.total"
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
    import turnoverRecordModel from '../../../model/materials/turnoverRecord'
    import supplierModel from '../../../model/basicdata/supplier'
    import menuTabService from '../../../service/menuTabService'
    import {Notification} from 'element-ui'
    import moment from "moment";

    export default {
        data() {
            return {
                invoicePrintPreviewSJVisible: false,
                shipmentsSelect: [],
                departmentSelect: [],
                stockSelect: [],
                optionStatus: [
                    {
                        value: '未核价',
                        label: '未核价'
                    },
                    {
                        value: '已核价',
                        label: '已核价'
                    }

                ],
                options: [
                    {
                        value: '1',
                        label: '磅单'
                    },
                    {
                        value: '2',
                        label: '收料单'
                    }

                ],
                turnovers: [],

                buttonData: [
                    {
                        name: '保存修改',
                        event: "updateClick",
                    },
                    {
                        name: '确认',
                        event: "finishClick",
                    }
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "submitForm('turnoverQuery')"
                    },
                    {
                        name: '重置',
                        event: 'resetForm("turnoverQuery")'
                    }
                ],
                rowButtonData: [
                    {
                        name: '编辑',
                        event: "editClick",
                        authorized: 'turnoverRecordList:edit',
                    },
                ],
                turnoverQuery: {
                    turnoverDNo: '',
                    materialNo: '',
                    materialName: '',
                    shipmentsId: '',
                    checkPriceStatus: '',
                    dateCreated: '',
                    dateCreatedOne: '',
                    dateCreatedTwo: '',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                selected: [],
            }
        },
        components: {},
        mounted() {
            this.shipmentsList();
            this.turnOverRList()
        },
        methods: {
            sortChange: function (column, prop, order) {
                let query = this.turnoverQuery;
                if (column.order) {
                    if ("descending" == column.order) {
                        query.sort = column.prop + ' desc';
                    } else {
                        query.sort = column.prop + ' asc';
                    }

                } else {
                    query.sort = 'dateCreated desc';
                }
                turnoverRecordModel.findTurnoverDetailForCheckPrice(query).then((data) => {
                    this.turnovers = data.entity.list
                    this.turnoverQuery.pageInfo.total = data.entity.totalCount
                })
            },
            updateClick() {

                let flag = false;
                if (this.selected.length == 0) {
                    this.$message('请选择核对项');
                    return;
                }

                let message = '是否已确认';
                    this.selected.forEach((row) => {
                        message +='【' + row.materialStorageId + '，' + row.materialName + "】";
                    })
                message+= "核价无误?";
                this.$confirm(message, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.selected.forEach((row) => {
                        if (row.checkPriceStatus == '已核价') {
                            this.$message('【' + row.materialStorageId + '，' + row.materialName + "】已核价，无法再次核价");
                            flag = true;
                            return;
                        }
                    })
                    if (flag) {
                        return;
                    }
                    turnoverRecordModel.updatePrice(this.selected).then((data) => {
                        if (data.status == 'fail') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            return
                        }
                        Notification({
                            message: '编辑成功',
                            type: 'success'
                        })
                        this.turnOverRList()
                    })
                })

            },
            finishClick() {
                let flag = false;
                if (this.selected.length == 0) {
                    this.$message('请选择核对项');
                    return;
                }

                let message = '是否已确认';
                this.selected.forEach((row) => {
                    message +='【' + row.materialStorageId + '，' + row.materialName + "】";
                })
                message+= "核价无误，确认之后将无法修改";
                this.$confirm(message, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                        this.selected.forEach((row) => {
                            if (row.checkPriceStatus == '已核价') {
                                this.$message('【' + row.materialStorageId + '，' + row.materialName + "】已核价，无法再次核价");
                                flag = true;
                                return;
                            }
                        })
                        if (flag) {
                            return;
                        }
                turnoverRecordModel.updateCheckPriceStatus(this.selected).then((data) => {
                    if (data.status == 'fail') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    }
                    Notification({
                        message: '编辑成功',
                        type: 'success'
                    })
                    this.turnOverRList()
                })
                })

            },
            shipmentsList() {
                supplierModel.listByOrgId().then((data) => {
                    this.shipmentsSelect = data.entity;
                })
            },
            turnOverRList() {
                turnoverRecordModel.findTurnoverDetailForCheckPrice(this.turnoverQuery).then((data) => {
                    this.turnovers = data.entity.list
                    this.turnoverQuery.pageInfo.total = data.entity.totalCount
                })
            },
            /**
             *模糊查询
             */
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.turnOverRList()
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            }
            ,
            authorized(index) {
                return menuTabService.authorized(this.$store, index)
            }
            ,
            handleSizeChange(val) {
                this.turnoverQuery.pageInfo.pageSize = val
                this.turnOverRList()
            }
            ,
            handleCurrentChange(val) {
                this.turnoverQuery.pageInfo.currentPage = val
                this.turnOverRList()
            }
            ,
            handleSelectionChange(selected) {
                this.selected = selected
            }
            ,
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            }
            ,
        },


    }
</script>

<style scoped>

</style>