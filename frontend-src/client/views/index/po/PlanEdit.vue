<template>
    <div>
        <el-form :inline="true" :model="planForm" v-if="planForm" ref="planForm" label-width="140px" :rules="planFormRulus">
            <el-form-item label="循环周期" prop="period">
                <el-select v-model="planForm.period" placeholder="请选择" :disabled="true">
                    <el-option label="1天" value="1"></el-option>
                    <el-option label="2天" value="2"></el-option>
                    <el-option label="3天" value="3"></el-option>
                    <el-option label="4天" value="4"></el-option>
                    <el-option label="5天" value="5"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="班组" prop="teamId" >
                <el-select v-model="planForm.teamId" placeholder="请选择" clearable>
                    <el-option v-for="item in teamInfos"
                               :key="item.id"
                               :label="item.teamName"
                               :value="item.id"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="预计生产日期" prop="predictDate">
                <el-date-picker
                        v-model="planForm.predictDate"
                        type="date"
                        placeholder="选择日期"
                >
                </el-date-picker>
            </el-form-item>
            <el-table
                    :data="planForm.customData"
            >
                <el-table-column
                        prop="plateNo"
                        label="模台号"
                        width="120">
                </el-table-column>

                <el-table-column
                        label="已选择构件"
                        width="580px">
                    <template slot-scope="scope">
                        <el-checkbox v-model="planForm.checked[item.index]"
                                     v-for="item in scope.row.items2"
                                     :key="item.index"
                                     :label="item.name"
                                     :checked="true"
                                     :disabled="true"
                                     name="type">
                        </el-checkbox>
                    </template>
                </el-table-column>

                <el-table-column
                        prop="num"
                        label="计划数">
                    <template slot-scope="scope">
                        <el-input v-model="scope.row.num" disabled="disabled" style="width: 100%"></el-input>
                    </template>
                </el-table-column>
            </el-table>
        </el-form>
        <el-row>
            <br>
        </el-row>
        <div slot="footer" class="dialog-footer" align="right">
            <el-button @click="cancel">取 消</el-button>
            <el-button type="primary" @click="palnFormSubmmit('palnForm')">创建计划</el-button>
        </div>
    </div>
</template>

<script>
    import combModel from '../../../model/po/comb'
    import {Notification} from 'element-ui'

    export default {
        props: ['planForm', 'items', 'teamInfos'],
        data() {
            return {
                planFormRulus: {
                    teamId: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                },
            }
        },
        methods: {
            cancel() {
                this.$emit("planForm-cancel");
            },

            palnFormSubmmit(palnForm) {
                if(this.planForm.teamId == '' || this.planForm.teamId == undefined){
                    Notification({
                        message: '请选择班组',
                        type: 'warning'
                    })
                    return
                }
                let count = 0;
                for (let i = 0; i < this.planForm.checked.length; i++) {
                    if (this.planForm.checked[i] === false) {
                        count = count + 1;
                    }
                }
                if (count == this.planForm.checked.length) {
                    Notification({
                        message: '请勾选需要创建计划的构件',
                        type: 'warning'
                    })
                    return
                }

                let selected = []; //获取到了选择的ID
                for (let i = 0; i < this.planForm.checked.length; i++) {
                    if (this.planForm.checked[i] === true) {
                        let selectedObj = {
                            productId: '',
                            orderid: '',
                            productDetailId: '',
                            name: '',
                            plateId:'',
                            platePlanId:''
                        }
                        selectedObj.productId = this.items[i].productId;
                        selectedObj.orderid = this.items[i].orderid;
                        selectedObj.productDetailId = this.items[i].productDetailId;
                        selectedObj.name = this.items[i].name;
                        selectedObj.plateId = this.items[i].plateId;
                        selectedObj.platePlanId = this.items[i].platePlanId;
                        selected.push(selectedObj);
                    }
                }
                this.planForm.selected = selected;
                console.log("selected :",selected)
                this.teamInfos.forEach(row => {
                    if (row.id == this.planForm.teamId) {
                        this.planForm.teamName = row.teamName;
                    }
                })

                const loading = this.$loading({
                    text: '创建中',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                })
                combModel.createPlan(this.planForm).then(data => {

                    if (data.status === 'success') {
                        Notification({
                            message: '创建计划成功',
                            type: 'success'
                        });
                        loading.close();
                        this.$emit("planForm-finish");
                    } else {
                        Notification({
                            message: '创建计划失败 :' + data.msg,
                            type: 'success'
                        })
                        loading.close();
                    }

                })


            }
        }
    }

</script>

<style scoped>


</style>