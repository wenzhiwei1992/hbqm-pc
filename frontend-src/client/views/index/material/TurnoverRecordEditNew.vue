<template>
    <div>
        <el-tabs v-model="activeName2" type="card">
            <el-form :inline="true" :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm"
                     label-width="80px">
                <el-form-item label="部门" prop="departmentId">
                    <el-select v-model="editForm.departmentId" disabled readonly placeholder="请选择" clearable
                               style="width: 150px"
                               filterable>
                        <el-option
                                v-for="item in departmentSelect"
                                :key="item.id"
                                :label="item.deptName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="仓库" prop="stockId">
                    <el-select v-model="editForm.stockId" disabled readonly placeholder="请选择" style="width: 150px">
                        <el-option
                                v-for="item in stockSelect"
                                :key="item.stockName"
                                :label="item.stockName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="供方" prop="shipmentsId">
                    <el-select v-model="editForm.shipmentsId" disabled readonly placeholder="请选择" style="width: 150px"
                               filterable>
                        <el-option
                                v-for="item in shipmentsSelect"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="运输单位" prop="transport">
                    <el-input v-model="editForm.transport " disabled readonly style="width: 150px"></el-input>
                </el-form-item>
                <el-form-item label="运费" prop="freight">
                    <el-input disabled readonly v-model="editForm.freight" style="width: 150px"></el-input>
                </el-form-item>
                <el-form-item label="车牌号" prop="licenseNo">
                    <el-input v-model="editForm.licenseNo" disabled readonly style="width: 150px"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="editForm.remark" disabled readonly type="textarea"
                              style="width: 150px"></el-input>
                </el-form-item>
            </el-form>
            <div>
                <el-table
                        :data="editForm.turnovers"
                        size="mini"
                        stripe
                        border
                        height="300"
                        style="width:100%"
                >
                    <el-table-column
                            prop="materialStorageId"
                            label="原料编码"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="materialName"
                            label="原料名称"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="materialSpecification"
                            label="原料规格"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="materialUnitName"
                            label="单位"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            v-if="show"
                            prop="checkout"
                            label="是否免检"
                            width="120">
                    </el-table-column>

                    <el-table-column
                            v-if="editForm.type==1"
                            label="毛重"
                            width="140"
                    >
                        <template slot-scope="scope">
                            <el-input
                                    v-model="scope.row.roughWeight"
                                    disabled readonly
                            ></el-input>
                        </template>
                    </el-table-column>

                    <el-table-column
                            v-if="editForm.type==1"
                            label="皮重"
                            width="140"
                    >
                        <template slot-scope="scope">
                            <el-input
                                    v-model="scope.row.tare"
                                    disabled readonly
                            ></el-input>
                        </template>
                    </el-table-column>

                    <el-table-column
                            v-if="editForm.type==1"
                            label="杂扣"
                            width="140"
                    >
                        <template slot-scope="scope">
                            <el-input
                                    v-model="scope.row.sundries"
                                    disabled readonly
                            ></el-input>
                        </template>
                    </el-table-column>

                    <el-table-column
                            v-if="editForm.type==1"
                            label="净重"
                            width="140"
                    >
                        <template slot-scope="scope">
                            <el-input
                                    v-model="scope.row.suttle"
                                    disabled readonly
                            ></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column
                            v-if="editForm.type==1"
                            label="实重"
                            width="140"
                    >
                        <template slot-scope="scope">
                            <el-input
                                    v-if="scope.row.amount"
                                    v-model="scope.row.amount"
                                    disabled readonly
                            ></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column
                            v-if="editForm.type==2"
                            label="原料数量"
                            width="140"
                    >
                        <template slot-scope="scope">
                            <el-input
                                    v-if="scope.row.amount"
                                    v-model="scope.row.amount"
                                    disabled readonly
                            ></el-input>
                        </template>
                    </el-table-column>

                    <el-table-column
                            label="单价(含税)"
                            width="140"
                    >
                        <template slot-scope="scope">
                            <el-input
                                    v-model="scope.row.univalence"
                                    @input="sunmToal(scope.row,scope.$index)"
                                    @keyup.native="parseValue(scope.row,scope.$index)"
                                    placeholder="请输入内容"
                            ></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="总价(含税)"
                            width="120"
                    >
                        <template slot-scope="scope">
                            <el-input
                                    v-model="scope.row.totalUnivalence"
                                    @input="sunmUnivalence(scope.row,scope.$index)"
                                    @keyup.native="parseTotalValue(scope.row,scope.$index)"
                                    placeholder="请输入内容"
                            ></el-input>
                        </template>
                    </el-table-column>


                    <el-table-column
                            label="税率(%)"
                            width="120"
                    >
                        <template slot-scope="scope">
                            <el-input
                                    v-model="scope.row.taxRate"
                                    @keyup.native="parseTaxRate(scope.row,scope.$index)"
                                    placeholder="请输入内容"
                            ></el-input>
                        </template>
                    </el-table-column>


                    <el-table-column
                            label="总价(不含税)"
                            width="120">
                        <template slot-scope="scope">
                            <el-input
                                    v-model="scope.row.totalUnivalenceNo"
                                    disabled readonly
                            ></el-input>
                        </template>
                    </el-table-column>

                </el-table>
            </div>
        </el-tabs>
        <div slot="footer" class="dialog-footer">
            <el-button type="danger" @click="editFormCancel">关闭</el-button>
            <el-button type="success" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>

    </div>
</template>

<script>
    import turnoverDetailModel from '../../../model/materials/turnoverDetail'
    import turnoverRecordModel from '../../../model/materials/turnoverRecord'
    import materialStorageModel from '../../../model/basicdata/materialStorage'
    import clientModel from '../../../model/client-model'
    import TurnoverAdd from './TurnoverAdd.vue'
    import {Notification} from 'element-ui'

    export default {
        props: ['editForm', 'mode', 'departmentSelect', 'stockSelect', 'shipmentsSelect'],
        methods: {
            sunmToalNew(row, index) {
                let totalUnivalence = row.totalUnivalence;
                let taxRate =  row.taxRate;
                if(totalUnivalence && taxRate){
                    let value = (totalUnivalence*100 /(100+parseInt(taxRate))) .toFixed(3);
                    this.editForm.turnovers[index].totalUnivalenceNo = value == 0 ? "" : value;
                }
            },
            sunmUnivalence(row, index) {
                let value = (row.totalUnivalence / row.amount).toFixed(3);
                this.editForm.turnovers[index].univalence = value == 0 ? "" : value;
                this.sunmToalNew(row, index)
            },
            sunmToal(row, index) {
                let value = row.univalence * row.amount;
                this.editForm.turnovers[index].totalUnivalence = value == 0 ? "" : value;
                this.sunmToalNew(row, index)
            },
            parseTaxRate(row, index){
                let value = row.taxRate;
                value = value.replace(/[^\d.]/g, ""); // 清除"数字"和"."以外的字符
                value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
                value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                    ".");
                value = value.replace(/^(\-)*(\d+)\.(\d\d\d).*$/, '$1$2.$3'); // 只能输入3个小数
                row.taxRate = value;
                this.sunmToalNew(row, index)
            },
            parseTotalValue(row, index) {
                let value = row.totalUnivalence;
                value = value.replace(/[^\d.]/g, ""); // 清除"数字"和"."以外的字符
                value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
                value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                    ".");
                value = value.replace(/^(\-)*(\d+)\.(\d\d\d).*$/, '$1$2.$3'); // 只能输入3个小数
                row.totalUnivalence = value;
                this.sunmUnivalence(row, index);
            },
            parseValue(row, index) {
                let value = row.univalence;
                if (!value) {
                    return;
                }
                value = value.replace(/[^\d.]/g, ""); // 清除"数字"和"."以外的字符
                value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
                value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                    ".");
                value = value.replace(/^(\-)*(\d+)\.(\d\d\d).*$/, '$1$2.$3'); // 只能输入3个小数
                row.univalence = value;
                this.sunmToal(row, index);
            },
            parseAmount(row, index) {
                let value = row.amount;
                value = value.replace(/[^\d.]/g, ""); // 清除"数字"和"."以外的字符
                value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
                value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                    ".");
                value = value.replace(/^(\-)*(\d+)\.(\d\d\d).*$/, '$1$2.$3'); // 只能输入3个小数
                row.amount = value;
                this.sunmToal(row, index);
            },
            getSuttleAndRealityWeightByCount() {
                var turnoverQuery = {
                    roughWeight: this.editForm.roughWeight,
                    tare: this.editForm.tare,
                    sundries: this.editForm.sundries
                };
                turnoverDetailModel.getSuttleAndRealityWeightByCount(turnoverQuery).then((data) => {
                    this.editForm.suttle = data.entity.suttle;
                    this.editForm.realityWeight = data.entity.realityWeight
                })
            },
            editFormCancel() {
                this.$emit('turnoverRecord-edit-cancel')
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.mode == 'update') {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            this.editForm.shipments = '' + '-' + this.editForm.id;
                            turnoverRecordModel.turnoverRecordUpdate(this.editForm)
                                .then((data) => {
                                    if (data.status == 'fail') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        });
                                        loading.close();
                                        return
                                    }
                                    turnoverDetailModel.turnoverDetailUpdates(this.editForm).then((data) => {
                                        if (data.status == 'fail') {
                                            Notification({
                                                message: data.msg,
                                                type: 'warning'
                                            });
                                            loading.close();
                                            return
                                        }
                                        Notification({
                                            message: '编辑成功',
                                            type: 'success'
                                        });
                                        loading.close();
                                        this.$emit('turnoverRecord-edit-finish')
                                    })
                                })
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
        },

        data() {
            return {
                activeName2: 'first',
                show: '',//隐藏tabel
                selected: [],
                turnovers: [],
                eidtFormRules: {
                    shipmentsId: [
                        {required: true, message: '供方不能为空', trigger: 'blur'},
                    ],
                    stockId: [
                        {required: true, message: '仓库不能为空', trigger: 'blur'},
                    ],
                },
            }
        }
    }
</script>

<style scoped>

</style>