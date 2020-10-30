<template>
    <div>
        <el-form :model="checkEditForm" v-if="checkEditForm" :rules="checkEditFormRules" ref="checkEditForm"
                 label-width="80px">
            <el-form-item label="是  否  合   格" prop="checkoutStatus" label-width="100px">
                <el-radio v-model="checkEditForm.checkoutStatus" label="合格" @change="checkoutStatusChangeTrue">合格
                </el-radio>
                <el-radio v-model="checkEditForm.checkoutStatus" label="退回" @change="checkoutStatusChangeFlase">退回
                </el-radio>
                <el-radio v-model="checkEditForm.checkoutStatus" label="让步接收" @change="checkoutStatusChangeFlase">让步接收
                </el-radio>
            </el-form-item>
            <div v-if="isShow">
                <el-form-item label="备注" prop="reason" label-width="100px">
                    <el-input v-model="checkEditForm.reason" type="textarea"></el-input>
                </el-form-item>
            </div>
        </el-form>
        <el-button v-if="turnoverDetailId" @click="click">确定</el-button>
        <el-button v-if="!turnoverDetailId" @click="clickNew">确定</el-button>
        <el-button @click="checkEditFormCancel">关闭</el-button>
    </div>
</template>

<script>
    import turnoverDetailModel from '../../../model/materials/turnoverDetail'
    import CheckoutStatus from './CheckoutStatus.vue'
    import {Notification} from 'element-ui'

    export default {
        props: ['checkEditForm', 'turnoverDetailId', 'turnoverRecordId', 'propsRow', 'turnovers'],
        name: "CheckoutStatus",
        mounted() {
        },
        methods: {
            clickNew() {
                if (this.checkEditForm.checkoutStatus === "") {
                    this.$message('请检验');
                    return;
                }
                if (this.checkEditForm.checkoutStatus != "合格" && !this.checkEditForm.reason) {
                    this.$message('请填写备注说明');
                    return;
                }

                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                })
                this.checkEditForm.id = this.turnoverRecordId;
                turnoverDetailModel.updateStatusByRecordId(this.checkEditForm).then((data) => {
                    loading.close()
                    if (data.status == 'fail') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    }

                    Notification({
                        message: '检验成功',
                        type: 'success'
                    })
                    this.$emit('checkout-status-finish')
                })
            },
            click() {
                if (this.checkEditForm.checkoutStatus === "") {
                    this.$message('请检验');
                    return;
                }
                if (this.checkEditForm.checkoutStatus != "合格" && !this.checkEditForm.reason) {
                    this.$message('请填写备注说明');
                    return;
                }

                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                })
                this.checkEditForm.id = this.turnoverDetailId;
                turnoverDetailModel.updateCheckStatusById(this.checkEditForm).then((data) => {
                    loading.close()
                    if (data.status == 'fail') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    }

                    Notification({
                        message: '检验成功',
                        type: 'success'
                    })
                    this.$emit('checkout-status-finish')
                })
            },
            checkoutStatusChangeFlase() {
                this.isShow = true
            },
            checkoutStatusChangeTrue() {
                this.isShow = false
            },
            checkEditFormCancel() {
                this.$emit('checkout-status-cancel')
            },
        },
        data() {
            return {
                isShow: false,
                checkEditFormRules: {},
            }
        }
    }
</script>

<style scoped>

</style>