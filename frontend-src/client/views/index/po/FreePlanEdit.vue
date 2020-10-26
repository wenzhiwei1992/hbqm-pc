<template>
    <div>
        <el-form :inline="true" :model="freePlanForm" v-if="freefreePlanForm"  ref="freePlanForm" label-width="140px">
            <el-form-item label="循环周期" prop="period">
                <el-select v-model="freePlanForm.period" placeholder="请选择" >
                    <el-option label="1天" value="1"></el-option>
                    <el-option label="2天" value="2"></el-option>
                    <el-option label="3天" value="3"></el-option>
                    <el-option label="4天" value="4"></el-option>
                    <el-option label="5天" value="5"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="预计生产日期" prop="predictDate">
                <el-date-picker
                        v-model="freePlanForm.predictDate"
                        type="date"
                        placeholder="选择日期"
                >
                </el-date-picker>
            </el-form-item>
            <el-table
                    :data="freePlanForm.customData"
            >
                <el-table-column
                        prop="plateNo"
                        label="模台"
                        width="120">
                </el-table-column>

                <el-table-column
                        v-for="item in freeItems"
                        :key="item.index"
                        :label="item.label"
                        width="120">
                    <template slot-scope="scope" >

                    </template>
                </el-table-column>

                <el-table-column
                        prop="num"
                        label="计划数">
                    <template slot-scope="scope">
                        <el-input v-model="scope.row.num" style="width: 100%"></el-input>
                    </template>
                </el-table-column>

            </el-table>
        </el-form>
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
        props:['freePlanForm','items'],
        data() {
            return {
                freePlanForm:null,
                freePlanFormRules:null,
            }
        },
        methods: {
            cancel(){
                this.$emit("freePlanForm-cancel");
            },

            palnFormSubmmit(palnForm){
                let selected = []; //获取到了选择的ID
                for(let i = 0; i<this.freePlanForm.checked.length;i++){
                    if(this.freePlanForm.checked[i] === true){
                        let productId = this.items[i].productId ;
                        selected.push(productId);
                    }
                }
                this.freePlanForm.selected = selected;
                console.log(this.freePlanForm.selected)
                const loading = this.$loading({
                    text: '创建中',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                })
                combModel.createPlan(this.freePlanForm).then(data=>{
                    if(data.status ==='success'){
                        Notification({
                            message:'创建计划成功',
                            type:'success'
                        })
                    }else{
                        Notification({
                            message:'创建计划失败 :' + data.msg,
                            type:'success'
                        })
                    }
                    loading.close();
                    this.$emit("freePlanForm-finish");
                })

            }
        }
    }

</script>

<style scoped>


</style>