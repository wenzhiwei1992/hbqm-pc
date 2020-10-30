<template>
    <div>
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules"  ref="editForm" label-width="80px">
            <el-form-item label="构件分类名称" prop="productCategoriesName" label-width="110px">
                <el-input v-model="editForm.productCategoriesName"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editFormCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import productCategoriesModel from '../../../../model/basicdata/productCategories'
    import {Notification} from 'element-ui'
    export default {
        props:['editForm','mode'],
        name: "ProductCategoriesEdit",
        data(){
            return{
                eidtFormRules:{
                    productCategoriesName: [
                        {required: true, message: '请输入构件分类名称', trigger: 'blur'},
                    ],
                }
            }
        },
        mounted(){

        },
        methods:{
            editFormCancel(){
                this.$emit('product-categories-cancel')
            },
            editFormSubmmit(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.mode=='update'){
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            productCategoriesModel.productCategoriesUpdate(this.editForm)
                                .then((data) => {
                                    if(data.status=='fail'){
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close()
                                        return
                                    }
                                    Notification({
                                        message: '编辑成功',
                                        type: 'success'
                                    })
                                    loading.close();
                                    this.$emit('product-categories-finish')
                                })
                        }else if(this.mode=='add'){
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            productCategoriesModel.productCategoriesAdd(this.editForm)
                                .then((data) => {
                                    if(data.status=='fail'){
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close()
                                        return
                                    }
                                    Notification({
                                        message: '新建成功',
                                        type: 'success'
                                    })
                                    loading.close();
                                    this.$emit('product-categories-finish')
                                })
                        }

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
        },
    }
</script>

<style scoped>

</style>