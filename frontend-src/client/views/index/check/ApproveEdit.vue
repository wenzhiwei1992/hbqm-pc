<template>
    <div id="check-edit">
        <el-form  :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="100px" size="small" >
            <el-form-item label="审批结果">
                <el-select v-model="editForm.checkResult">
                    <el-option
                            v-for="item in resultList"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="审批说明">
                <el-input type="textarea" v-model="editForm.checkDesc"></el-input>
            </el-form-item>
        </el-form>
        <div style="text-align:right" class="dialog-footer">
            <el-button @click="editFormCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>



</template>
<script>
    import clientModel from '../../../model/client-model'
    import {Notification} from 'element-ui'

    export default {
        props:['productDetail'],
        data() {
            return {
                editForm:null,
                eidtFormRules: {
                },
                items:[],
                resultList:[{label:'审批通过',value:'报废'},{label:'审批不通过',value:'报废审批未通过'}]
            }
        },
        mounted(){
            this.init()
        },
        computed:{
        },
        methods:{
            init(){
                this.editForm={
                    checkType:'报废审批',
                    productDetailId:this.productDetail.id,
                    checkDesc:'',
                    checkResult:'报废'
                }
            },
            editFormCancel(){
                this.$emit('approve-edit-cancel')
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        var fd=new FormData();
                        fd.append('checkType',this.editForm.checkType);
                        fd.append('productDetailId',this.editForm.productDetailId);
                        fd.append('checkDesc',this.editForm.checkDesc);
                        fd.append('checkResult',this.editForm.checkResult);
                        this.$store.commit('startLoading')
                        clientModel.ax.post('/check/add',fd)
                            .then((data) => {
                                if (data.status == 'fail') {
                                    Notification({
                                        message: data.msg,
                                        type: 'warning'
                                    })
                                    this.$store.commit('endLoading')
                                    return
                                }
                                Notification({
                                    message: '构件已完成'+this.editForm.checkType,
                                    type: 'success'

                                })
                                this.$store.commit('endLoading')
                                this.$emit('approve-edit-finish')
                            })

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },

        },
        watch:{
            productDetail:function(){
                this.init()
            }
        }

    }
</script>
<style>
    #check-edit .el-form-item__label{
        font-size:16px;

    }
</style>