<template>
    <div id="certi-edit">
        <el-form  :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="150px" size="small" >
            <el-form-item label="构件名称" prop="productIds">
                <el-select
                    v-model="editForm.productIds"
                    multiple
                    filterable
                    remote
                    reserve-keyword
                    placeholder="请输入构件名称关键词"
                    :remote-method="remoteMethod"
                    @change="loadStrenthGrade"
                    :loading="loading">
                        <el-option
                            v-for="item in options4"
                            :key="item.id"
                            :label="item.productName"
                            :value="item.id">
                        </el-option>
                </el-select>
            </el-form-item>
            <!-- <el-form-item label="出厂日期" prop="perCompDate">
                <el-date-picker
                        v-model="editForm.perCompDate"
                        type="datetime"
                        format="yyyy-MM-dd HH:mm:ss"
                        placeholder="选择日期时间">
                </el-date-picker>
            </el-form-item> -->
            <el-form-item label="生产企业" prop="prodCompany">
                <el-input v-model="editForm.prodCompany" ></el-input>
            </el-form-item>
            <el-form-item label="设计强度等级" prop="designStrenthGrade">
                <el-input v-model="editForm.designStrenthGrade"></el-input>
            </el-form-item>
            <el-form-item label="实际强度等级" prop="strenthGrade">
                <el-input v-model="editForm.strenthGrade"></el-input>
            </el-form-item>
            <!-- <el-form-item label="规格尺寸" prop="spec">
                <el-input v-model="editForm.spec" type="number"></el-input>
            </el-form-item> -->
            <el-form-item label="出厂强度" prop="strenth">
                <el-input v-model="editForm.strenth"></el-input>
            </el-form-item>
             <el-form-item label="种类及规格" prop="typeAndSpecification">
                <el-input v-model="editForm.typeAndSpecification"></el-input>
            </el-form-item>
             <el-form-item label="机械性能" prop="mechanicalBehavior">
                <el-input v-model="editForm.mechanicalBehavior"></el-input>
            </el-form-item>
            <el-form-item label="结论" prop="remark">
                <el-input v-model="editForm.remark"></el-input>
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
        props:['id','mode'],
        data() {
            return {
                editForm:{
                    productIds:[],
                    prodCompany:'蒙城宝业建筑工业化有限公司',
                },
                eidtFormRules: {
                    productIds: [
                        {required: true, message: '请输入构件名称', trigger: 'blur'},
                    ],
                },
                options4:[],
                loading: false,

            }
        },
        mounted(){
            this.init()
        },
        computed:{
        },
        methods:{
            loadStrenthGrade(productId){
                let id = productId + "";
                if(id.indexOf(",")){
                   id = id.split(",")[0];
                }
                if(productId.length == 0){
                    return;
                }
                model.load('product',id).then(data=>{
                    let product = data.entity;
                    this.editForm.designStrenthGrade = product.productGrade
                })
            },
            remoteMethod(query){
                if (query !== '') {
                    this.loading = true;
                        setTimeout(() => {
                            this.loading = false;
                            //访问后台进行查询
                            model.findProductsByQueryString(query).then(data=>{
                                this.options4= data.entity;
                            });
                            
                        }, 200);
                } else {
                    this.options4 = [];
                }
            },
            init(){
                if(this.mode=='update'){
                    model.load('certi',this.id).then(data=>{
                        this.editForm=data.entity
                        this.options4 = data.entity.options;
                    })
                }else{
                    this.editForm={
                        productIds:[],
                        prodCompany:'蒙城宝业建筑工业化有限公司',
                    }
                }
            },
            editFormCancel(){
                this.$emit('model-edit-cancel')
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$store.commit('startLoading')
                        if (this.mode == 'update') {
                            model.update('certi',this.editForm)
                                .then((data) => {
                                    if (data.status == 'fail') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        this.$store.commit('endLoading')
                                        return
                                    }
                                    Notification({
                                        message: '编辑成功',
                                        type: 'success'

                                    })
                                    this.$store.commit('endLoading')
                                    this.$emit('model-edit-finish')
                                })
                        } else {
                            model.add('certi',this.editForm)
                                .then((data) => {
                                    if (data.status == 'fail') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        this.$store.commit('endLoading')
                                        return
                                    }
                                    Notification({
                                        message: '新建成功',
                                        type: 'success'

                                    })
                                    this.$store.commit('endLoading')
                                    this.$emit('model-edit-finish')
                                })
                        }

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            }

        },
        watch:{
            id:function(){
                this.init()
            }
        }

    }
</script>
<style>
</style>