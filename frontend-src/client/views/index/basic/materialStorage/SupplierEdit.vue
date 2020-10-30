<template>
    <div>
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules"  ref="editForm" label-width="80px">

            <el-form-item label="供应商编码" prop="supplierCode"  label-width="110px">
                <el-input v-model="editForm.supplierCode" style="width: 185px"></el-input>
            </el-form-item>
            <el-form-item label="供应商名称" prop="name"  label-width="110px">
                <el-input v-model="editForm.name" style="width: 185px"></el-input>
            </el-form-item>
            <el-form-item label="供应商电话" prop="phone"  label-width="110px">
                <el-input v-model="editForm.phone" style="width: 185px"></el-input>
            </el-form-item>
            <el-form-item label="供应商地址" prop="address"  label-width="110px">
                <el-input v-model="editForm.address" style="width: 185px"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editFormCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import supplier from '../../../../model/basicdata/supplier'
    import {Notification} from 'element-ui'
    export default {
        props:['editForm','mode'],
        data(){
            return{
                eidtFormRules:{
                    name: [
                        {required: true, message: '请输入供应商名称', trigger: 'blur'},
                    ],
                    supplierCode: [
                        {required: true, message: '请输入供应商编码', trigger: 'blur'},
                    ],

                },
                nameTmp:''
            }
        },
        mounted(){
        },
        methods:{
            editFormCancel(){
                this.$emit('edit-cancel')
            },
            editFormSubmmit(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.mode=='edit'){
                            supplier.update(this.editForm)
                                .then((data) => {
                                    if(data.status=='failed'){
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
                                    this.$emit('edit-finish')
                                })
                        }else if(this.mode=='add'){
                            supplier.add(this.editForm)
                                .then((data) => {
                                    if(data.status=='failed'){
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
                                    this.$emit('edit-finish')
                                })
                        }

                    } else {
                        return false;
                    }
                });
            },
        },

    }
</script>

<style scoped>

</style>