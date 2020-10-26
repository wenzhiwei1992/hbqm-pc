<template>
    <div>
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules"  ref="editForm" label-width="80px">
         <!--   <el-form-item label="构件分类" prop="productCategoriesId">
                <el-input v-model="editForm.productCategoriesId" ></el-input>
            </el-form-item>-->
            <el-form-item label="上级分类名称" prop="parentName"  v-if="this.mode == 'add' &&  this.editForm.parentId !=null ">
                <el-input v-model="editForm.parentName" :disabled="this.mode == 'add' &&  this.editForm.parentId !=null" ></el-input>
            </el-form-item>
           <!-- <el-form-item label="构件分类" prop="id" v-if="this.mode == 'update' && this.editForm.parentId != null  ">
                <el-select v-model="editForm.parentId" placeholder="请选择" style="width:150px" clearable>
                    <el-option
                            v-for="item in arrProductType"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>-->
            <el-form-item label="构件类型名称" prop="name">
                <el-input v-model="editForm.name" ></el-input>
            </el-form-item>
            <el-form-item label="构件类型编号" prop="typeNo">
                <el-input v-model="editForm.typeNo"></el-input>
            </el-form-item>
            <el-form-item label="备注" prop="remarks">
                <el-input v-model="editForm.remarks"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="quit">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import productTypeModel from '../../../../model/basicdata/productType'
    import {Notification} from 'element-ui'
    import moment from "moment";
    export default {
        name: "ProductTypeEdit",
        props:['editForm','mode'],
        data(){
            return{
                eidtFormRules: {
                    productCategoriesId:[
                        {required: true, message: '必填字段', trigger: 'change'},
                    ],
                    name: [
                        {required: true, message: '必填字段', trigger: 'blur'},

                    ],
                    typeNo: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                },
                arrProductType:'',
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
                            productTypeModel.update(this.editForm)
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
                                    this.$emit('product-type-finish')
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
                            productTypeModel.add(this.editForm)
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
                                    this.$emit('product-type-finish',null)
                                })
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            info(){
                productTypeModel.findProductTypesParentById(this.editForm.id).then((data)=>{
                    this.arrProductType = data.entity
                })
            },
            quit(){
                this.$emit('product-type-cancel')
            },

        },

    }
</script>

<style scoped>

</style>