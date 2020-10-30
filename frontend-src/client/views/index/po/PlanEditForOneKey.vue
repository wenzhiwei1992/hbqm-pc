<template>
    <div>
        <el-form :inline="true" :model="planForm" v-if="planForm" ref="planForm" :rules="planFormRulus">
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
                    :data="allPCProducts" 
                    
                    >
                <!-- @selection-change="handleSelectionChange"
                <el-table-column
                    type="selection"
                    width="80">
                </el-table-column> -->
                <el-table-column
                        prop="plateNo"
                        label="模台号"
                         >
                </el-table-column>
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
                        <div class="el-input el-input--mini">
                            <input class="el-input__inner" v-model="scope.row.pcNum" :max="scope.row.allowPcNum" type="number"
                                   :min="1" onkeyup="value=value.replace(/[^\d]/g,'')" 
                                   />
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="allowPcNum"
                        label="剩余可排产数量"
                         >
                </el-table-column>
                <el-table-column
                        prop="planDateTo"
                        label="开始时间"
                         >
                </el-table-column>
                <el-table-column
                        prop="planDateFrom"
                        label="结束时间"
                         >
                </el-table-column>
            </el-table>
        </el-form>
        <el-row>
            <br>
        </el-row>
        <div slot="footer" class="dialog-footer" align="right">
            <el-row>
                <el-col :span="12" align="left">
                </el-col>
                <el-col :span="12">
                    <el-button @click="cancel">取 消</el-button>
                    <el-button type="primary" @click="palnFormSubmmit('palnForm')">创建计划1</el-button>
                </el-col>
            </el-row>
            
        </div>
    </div>
</template>

<script>
    import combModel from '../../../model/po/comb'
    import {Notification} from 'element-ui'

    export default {
        props: ['planForm','allPCProducts','teamInfos'],
        data() {
            return {
                planFormRulus: {
                    teamId: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    predictDate:[
                        {
                            required: true, message: '必填字段', trigger: 'change'
                        }
                    ]
                },
                selected:[],
                savedCurrentPcNum:[],
            }
        },
        mounted(){
            
        },
        methods: {
            /**
             * 删除不想进行排产的构件
             */
            // deleteSelected(){
            //     if(this.selected.length ==0){
            //         this.$message('请选择删除项');
            //         return;
            //     }
            //     let ids=[];
            //     this.selected.forEach((row)=>{
            //         ids.push(row.id)
            //     });
            // },
            cancel() {
                this.$emit("planForm-cancel");
            },
            palnFormSubmmit(palnForm) {
                this.savedCurrentPcNum = this.allPCProducts
                if(this.planForm.teamId == '' || this.planForm.teamId == undefined){
                    Notification({
                        message: '请选择班组',
                        type: 'warning'
                    })
                    return
                }
                let res = false;
                this.allPCProducts.forEach(row=>{
                    let count = 0; 
                    for (let index = 0; index < this.savedCurrentPcNum.length; index++) {
                        const e = this.savedCurrentPcNum[index];
                        if(e.productId == row.productId){
                            count += parseInt(e.pcNum);
                        }
                    }
                    if(count > row.allowPcNum){
                        res=true;
                        return;
                    }
                    if(parseInt(row.pcNum)  > parseInt(row.allowPcNum)){
                        res=true;
                        return;
                    }
                })
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
                this.planForm.customData = this.allPCProducts;
                console.log("this.planForm",this.planForm);
                if(this.planForm.isMps == '否'){
                    //根据非MPS的构件创建计划
                    combModel.createPlanM2ForOneKeyNoMps(this.planForm).then(data => {
                        if (data.status === 'success') {
                            Notification({
                                message: '创建计划成功',
                                type: 'success'
                            });
                            loading.close();
                            this.$emit("planForm-finish");
                        } else {
                            Notification({
                                message: '创建失败: \n' + data.msg,
                                type: 'warning'
                            })
                            loading.close();
                        }
                    })
                    return;
                }

                //创建计划
                combModel.createPlanM2ForOneKey(this.planForm).then(data => {
                    if (data.status === 'success') {
                        Notification({
                            message: '创建计划成功',
                            type: 'success'
                        });
                        loading.close();
                        this.$emit("planForm-finish");
                    } else {
                        Notification({
                            message: '创建失败: \n' + data.msg,
                            type: 'warning'
                        })
                        loading.close();
                    }
                })


            },
            handleSelectionChange(val){
                let that = this
                //当前页面选中的数据赋值
                that.selected = val;
                
            },
        }
    }

</script>

<style scoped>


</style>