<template>
    <div>
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules"  ref="editForm" label-width="80px">
            <!--   <el-form-item label="构件分类" prop="productCategoriesId">
                   <el-input v-model="editForm.productCategoriesId" ></el-input>
               </el-form-item>-->
            <el-form-item label="上级类别名称" prop="parentName"  v-if="this.mode == 'add' &&  this.editForm.parentId !=null ">
                <el-input v-model="editForm.parentName" :disabled="this.mode == 'add' &&  this.editForm.parentId !=null" ></el-input>
            </el-form-item>
          <!--  <el-form-item label="原料分类" prop="id" v-if="this.mode == 'update' && this.editForm.parentId != null  ">
                <el-select v-model="editForm.parentId" placeholder="请选择" style="width:150px" clearable>
                    <el-option
                            v-for="item in arrCategory"
                            :key="item.id"
                            :label="item.categoryName"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>-->
            <el-form-item label="原料类别名称" prop="categoryName">
                <el-input v-model="editForm.categoryName" ></el-input>
            </el-form-item>
            <el-form-item label="编码" prop="categoryNo">
                <el-input v-model="editForm.categoryNo"></el-input>
            </el-form-item>
      <!--      <el-form-item label="备注" prop="remarks">
                <el-input v-model="editForm.remarks"></el-input>
            </el-form-item>-->
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="quit">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import categoryModel from '../../../../model/basicdata/category'
    import {Notification} from 'element-ui'
    export default {
        name: "CategoryEditTree",
        props:['editForm','mode'],
        data(){
            return{
                eidtFormRules: {
                    categoryName: [
                        {required: true, message: '请输入物料类别名称', trigger: 'blur'},
                    ],
                    categoryNo: [
                        {required: true, message: '请输入编码', trigger: 'blur'},
                    ],
                },
                arrCategory:'',
            }
        },
        mounted() {
            if(this.mode ==this.GLOBAL.STATUS.UPDATE){
                this.info()
            }
        },
        methods:{
            editFormSubmmit(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.mode==this.GLOBAL.STATUS.UPDATE){
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            categoryModel.updateTree(this.editForm)
                                .then((data) => {
                                    if(data.status==this.GLOBAL.STATUS.FAIL){
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close();
                                        return
                                    }
                                    Notification({
                                        message:this.GLOBAL.MSG.EDIT_SUCCESS,
                                        type: 'success'

                                    })
                                    loading.close();
                                    this.$emit('category-finish')
                                }).catch(()=>{

                                }
                            )
                        }else{
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            categoryModel.addTree(this.editForm)
                                .then((data) => {
                                    if(data.status==this.GLOBAL.STATUS.FAIL){
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close();
                                        return
                                    }
                                    Notification({
                                        message: this.GLOBAL.MSG.ADD_SUCCESS,
                                        type: 'success'

                                    })
                                    loading.close();
                                    this.$emit('category-finish',data.entity)
                                })
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            info(){
                categoryModel.findCategoryByParentId(this.editForm.id).then((data)=>{
                    this.arrCategory = data.entity
                })
            },
            quit(){
                this.$emit('category-cancel')
            },

        },
    }
</script>

<style scoped>

</style>