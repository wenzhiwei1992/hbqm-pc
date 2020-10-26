<template>
    <div>
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules"  ref="editForm" label-width="80px">
            <el-form-item label="物料分类名称" prop="classificationId" label-width="110px">
                <el-select v-model="editForm.classificationId" placeholder="请选择">
                    <el-option
                            v-for="item in arrClassification"
                            :key="item.id"
                            :label="item.classificationName"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="物料类别名称" prop="categoryName"  label-width="110px">
                <el-input v-model="editForm.categoryName" style="width: 185px"></el-input>
            </el-form-item>
            <el-form-item label="编码" prop="categoryNo"  label-width="110px">
                <el-input v-model="editForm.categoryNo" style="width: 185px"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editFormCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import categoryModel from '../../../../model/basicdata/category'
    import {Notification} from 'element-ui'
    export default {
        name: "CategoryEdit",
        props:['editForm','mode','arrClassification'],
        mounted(){
            this.categoryNameTmp = this.editForm.categoryName;
        },
        methods:{
            editFormCancel(){
                this.$emit('category-edit-cancel')
            },
            editFormSubmmit(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.mode=='update'){
                            if (this.categoryNameTmp == this.editForm.categoryName) {
                                this.$message(this.GLOBAL.MSG.VALUE_NOT_CHANGE)
                                return ;
                            }
                            categoryModel.categoryUpdate(this.editForm)
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
                                    this.$emit('category-edit-finish')
                                })
                        }else if(this.mode=='add'){
                            if (this.categoryNameTmp == this.editForm.categoryName) {
                                this.$message(this.GLOBAL.MSG.VALUE_NOT_CHANGE)
                                return ;
                            }
                            categoryModel.categoryAdd(this.editForm)
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
                                    this.$emit('category-edit-finish')
                                })
                        }

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
        },
        data(){
          return{
              eidtFormRules:{
                  classificationId: [
                      {required: true, message: '请选择物料分类', trigger: 'change'},
                  ],
                  categoryName: [
                      {required: true, message: '请输入物料类别名称', trigger: 'blur'},
                  ],
                  categoryNo: [
                      {required: true, message: '请输入编码', trigger: 'blur'},
                  ],
                  categoryNameTmp:''
              }
          }
        },

    }
</script>

<style scoped>

</style>