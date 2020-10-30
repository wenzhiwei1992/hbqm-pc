<template>
    <div>
        <el-form :model="checkEditForm" v-if="checkEditForm"  ref="checkEditForm" label-width="80px">
            <el-form-item label="驳回理由" prop="projectName" label-width="100px">
                <el-input v-model="checkEditForm.reason"  type="textarea" ></el-input>
            </el-form-item>
        </el-form>
        <el-button @click="click">确定</el-button>
        <el-button @click="checkEditFormCancel">关闭</el-button>
    </div>
</template>

<script>
    import doDetailModel  from '../../../../model/materials/doDetail'
    import {Notification} from 'element-ui'
    import  moment from  'moment'
    export default {
        name: "DeliveryOrderCheckoutStatusReason",
        props:['checkEditForm','doDetailNew','doDetailOld','deliveryOrders'],
        data(){
            return{
                checkEditFormRules:'',
                doDetails:this.doDetailNew,
            }
        },
        mounted(){

        },
        methods:{
            click(){
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                })
                doDetailModel.getDoDetailById(this.doDetailNew.maDoDetailId).then((data)=>{
                    if(data.status=='fail'){
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        loading.close()
                        return
                    }
                    this.doDetails = this.doDetailNew
                   this.doDetails.reason = this.checkEditForm.reason
                doDetailModel.updateStatusOverrule(this.doDetails).then((data1)=>{
                    if(data1.status=='fail'){
                        Notification({
                            message: data1.msg,
                            type: 'warning'
                        })
                        loading.close()
                        return
                    }
                    var arr = new Array()
                    arr.push(data1.entity)
                    arr.push(this.doDetailOld.listDoDetail)
                    doDetailModel.changeStatus(arr).then((data)=>{
                        for(var j=0;j<this.deliveryOrders.length;j++){
                            if(this.deliveryOrders[j].deliveryOrderId==data.entity[0].maDeliveryOrderId){
                                this.deliveryOrders[j].listDoDetail = data.entity;
                                break;
                            }
                        }
                        if(data1.entity.num==1){
                            this.$emit('checkout-status-finish')
                        }
                        Notification({
                            message: '驳回成功',
                            type: 'success'

                        })
                        this.$emit('checkout-status-save')
                        loading.close()
                    })
                })
                })
            },
            checkEditFormCancel(){
                this.$emit('checkout-status-cancel')
            },

        },

    }
</script>

<style scoped>

</style>