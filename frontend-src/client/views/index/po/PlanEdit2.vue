<template>
    <div>
        <el-form :inline="true" :model="planForm" v-if="planForm" ref="planForm" :rules="planFormRulus">
            <el-row>
                <el-col :span="8">
                    <el-form-item label="模台号" prop="plateNo">
                        <el-input v-model="planForm.plateNo"  :disabled="true" ></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="计划数" prop="num">
                        <el-input v-model="planForm.num"  :disabled="true"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="循环周期" prop="period" >
                        <el-select v-model="planForm.period"  :disabled="true" >
                            <el-option label="1天" value="1"></el-option>
                            <el-option label="2天" value="2"></el-option>
                            <el-option label="3天" value="3"></el-option>
                            <el-option label="4天" value="4"></el-option>
                            <el-option label="5天" value="5"></el-option>
                        </el-select>
                    </el-form-item>
    
                </el-col>
                
           </el-row>
            <el-row>
                
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
            </el-row>
            
            
            <el-table
                    :data="planForm.customData"
            >
                <el-table-column
                        prop="productNo"
                        label="构件编码"
                         >
                </el-table-column>
                <el-table-column
                        prop="productName"
                        label="构件名称"
                         >
                </el-table-column>
                <el-table-column
                        label="排产数量">
                        <template slot-scope="scope">
                            <el-input v-model="scope.row.pcNum" :max="scope.row.allowPcNum" type="number" 
                            :min="1" onkeyup="value=value.replace(/[^\d]/g,'')" > </el-input>
                        </template>
                </el-table-column>
                <el-table-column
                        prop="allowPcNum"
                        label="剩余可排产数量"
                         >
                </el-table-column>
                <!-- <el-table-column
                        prop="buildCode"
                        label="楼栋"
                        width="140">
                </el-table-column>
                <el-table-column
                        prop="floorNum"
                        label="楼层"
                        width="140">
                </el-table-column> -->
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
                    plateNo:[
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ]
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
                let res = false;
                this.planForm.customData.forEach(row=>{
                    if(parseInt(row.pcNum)  > parseInt(row.allowPcNum)){
                        res=true;
                        return;
                    }
                })
                //console.log("aa",this.planForm.customData);
                if(res){
                    Notification({
                        type:'warning',
                        message:'计划排产数量大于剩余可排产数量，请检查'
                    })
                    return;
                }
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
                console.log("this.planForm",this.planForm);
                //创建计划
                if(this.planForm.isMps=='否'){
                    combModel.createPlanM2ForNoMps(this.planForm).then(data => {
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
                        }s
                    })
                    return;
                }
                //fixed bug: 创建计划后应当更新allowPcNum的数值（根据构件ID和楼层关系ID，allowPcNum - pcNum）
                combModel.createPlanM2(this.planForm).then(data => {
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