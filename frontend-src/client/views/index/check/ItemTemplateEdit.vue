<template>
    <div>
        <el-form  :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px" size="small">
            <el-form-item label="模板名称" prop="templateName">
                <el-input v-model="editForm.templateName"></el-input>
            </el-form-item>
            <el-form-item label="检验类别" prop="checkType">
                <el-input v-model="editForm.checkType"></el-input>
            </el-form-item>
        </el-form>
        <div style="text-align:right" class="dialog-footer">
            <el-button @click="editFormCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>



</template>
<script>
    import model from '../../../model/model'
    import {Notification} from 'element-ui'

    export default {
        props:['editForm','mode'],
        data() {
            return {
                eidtFormRules: {
                    templateName: [
                        {required: true, message: '请输入模板名称', trigger: 'blur'},
                    ],
                },
            }
        },
        methods:{
            editFormCancel(){
                this.$emit('model-edit-cancel')
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.mode == 'update') {
                            model.update('check/template',this.editForm)
                                .then((data) => {
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
                                    this.$emit('model-edit-finish')
                                })
                        } else {
                            model.add('check/template',this.editForm)
                                .then((data) => {
                                    if (data.status == 'fail') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        return
                                    }
                                    Notification({
                                        message: '新建成功',
                                        type: 'success'

                                    })
                                    this.$emit('model-edit-finish')
                                })
                        }

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            }

        }

    }
</script>
<style>
</style>