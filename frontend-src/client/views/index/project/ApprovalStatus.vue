<template>
    <div>
        <el-form :model="editForm" v-if="editForm" :rules="editFormRules" ref="editForm"
                 label-width="80px">
            <el-form-item label="审批结果" prop="checkStatus" label-width="100px">
                <el-radio v-model="editForm.checkStatus" label="合格" >合格
                </el-radio>
                <el-radio v-model="editForm.checkStatus" label="退回">退回
                </el-radio>
            </el-form-item>
            <div>
                <el-form-item label="备注" prop="reason" label-width="100px">
                    <el-input v-model="editForm.reason" type="textarea"></el-input>
                </el-form-item>
            </div>
        </el-form>
        <el-button @click="click">确定</el-button>
        <el-button @click="editFormCancel">关闭</el-button>
    </div>
</template>

<script>
    import approvalModel from "../../../model/system/approval";
    import ApprovalStatus from './ApprovalStatus.vue'
    import {Notification} from 'element-ui'

    export default {
        props: ['editForm'],
        name: "ApprovalStatus",
        mounted() {

        },
        methods: {
            click() {
                if (!this.editForm.checkStatus) {
                    this.$message('请检验');
                    return;
                }

                if (!this.editForm.reason) {
                    this.$message('请填写备注说明');
                    return;
                }

                let approvalFrom = {
                    approvalStatus : this.editForm.checkStatus,
                    associationId : this.editForm.id,
                    cityId : this.editForm.cityId,
                    provinceId : this.editForm.provinceId,
                    codeType : this.editForm.codeName,
                    reason : this.editForm.reason,
                    nowCodeName : this.editForm.approvalStatus,
                }

                approvalModel.add(approvalFrom).then((data) => {
                    if (data.status == 'fail') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    }
                    Notification({
                        message: '审批完成',
                        type: 'success'
                    })
                    this.$emit('checkout-status-finish')
                })
            },
            editFormCancel() {
                this.$emit('checkout-status-cancel')
            },
        },
        data() {
            return {
                editFormRules: {
                    checkStatus: [
                        {required: true, message: '审批结果不能为空', trigger: 'blur'},
                    ],
                    reason: [
                        {required: true, message: '审批意见不能为空', trigger: 'blur'},
                    ],
                },
            }
        }
    }
</script>

<style scoped>

</style>