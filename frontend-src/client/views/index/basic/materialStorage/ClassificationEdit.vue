<template>
    <div>
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules"  ref="editForm" label-width="80px">
            <el-form-item label="物料分类名称" prop="classificationName" label-width="110px">
                <el-input v-model="editForm.classificationName" ></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editFormCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import classificationModel from '../../../../model/basicdata/classification'
    import {Notification} from 'element-ui'
    export default {
        name: "ClassificationEdit",
        props:['editForm','mode'],
        mounted(){
            this.classificationNameTmp = this.editForm.classificationName;
        },
        methods:{
            editFormCancel(){
                this.$emit('classification-edit-cancel')
            },
            editFormSubmmit(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.mode=='update'){
                            if (this.classificationNameTmp == this.editForm.classificationName) {
                                this.$message(this.GLOBAL.MSG.VALUE_NOT_CHANGE)
                                return ;
                            }
                            classificationModel.classificationUpdate(this.editForm)
                                .then((data) => {
                                    if(data.status=='fail'){
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
                                    this.$emit('classification-edit-finish')
                                })
                        }else if(this.mode=='add'){
                            classificationModel.classificationAdd(this.editForm)
                                .then((data) => {
                                    if(data.status=='fail'){
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
                                    this.$emit('classification-edit-finish')
                                })
                        }

                    } else {
                        return false;
                    }
                });
            },
        },
        data(){
            return{
                eidtFormRules:{
                    classificationName: [
                        {required: true, message: '请输入物料分类名称', trigger: 'blur'},
                    ],
                },
                classificationNameTmp:'',


            }
        }
    }
</script>

<style scoped>

</style>