<template>
    <div>
        <el-form :model="editForm" v-if="editForm"  :rules="eidtFormRules"  ref="editForm" label-width="80px">
            <el-form-item label="物料名称" prop="materialName">
                <el-input v-model="editForm.materialName" ></el-input>
            </el-form-item>
            <el-form-item label="物料类别" prop="categoryName" >
                <el-input  v-model="editForm.categoryName"  @focus="showSelectTrees"></el-input>
            </el-form-item>
            <el-form-item label="物料分类Id" prop="categoryId" hidden>
                <el-input  v-model="editForm.categoryId"></el-input>
            </el-form-item>
            <el-form-item label="物料编码" prop="materialStorageId" >
                <el-input  v-model="editForm.materialStorageId" :disabled="isUpdate"></el-input>
            </el-form-item>

            <el-form-item label="物料规格" prop="materialSpecification">
                <el-input v-model="editForm.materialSpecification"></el-input>
            </el-form-item>
            <el-form-item label="计量单位"  prop="materialUnit">
                <el-select v-model="editForm.materialUnit"  placeholder="请选择">
                    <el-option
                            v-for="item in optionsUnit"
                            :key="item.id"
                            :label="item.unitCNName"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="采购周期" prop="purchasePeriod">
                <el-input v-model="editForm.purchasePeriod"  type="number"></el-input>
            </el-form-item>
            <el-form-item label="库存预警" prop="inventory">
                <el-input v-model="editForm.inventory"  type="number"></el-input>
            </el-form-item>
            <el-form-item label="损耗率" prop="lossRate">
                <el-input v-model="editForm.lossRate"  @keyup.native="parseValue()"></el-input>
            </el-form-item>
            <el-form-item label="是否为免检物料" prop="checkout">
                <el-radio v-model="editForm.checkout" label="是">是</el-radio>
                <el-radio v-model="editForm.checkout" label="否">否</el-radio>
            </el-form-item>
            <el-form-item label="入库类型" prop="materialType">
                <el-radio v-model="editForm.materialType" label="1">磅单入库</el-radio>
                <el-radio v-model="editForm.materialType" label="2">收料单入库</el-radio>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editFormCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
           <el-dialog :visible.sync="showSelectTree" width="340px" append-to-body>
            <tree-add-edit
                    :treeArr="treeArr"
                    :baseCheckedId="baseCheckedId"
                    :baseCheckParentId="baseCheckParentId"
                    @tree-edit-submit="checkoutStatusFinish"
                    @tree-edit-cancel="checkoutStatusCancel"
            ></tree-add-edit>
        </el-dialog>
    </div> 
    
</template>

<script>
    import materialStorageModel  from '../../../../model/basicdata/materialStorage'
    import classificationModel from '../../../../model/basicdata/classification'
    import categoryModel from '../../../../model/basicdata/category'
    import TreeInfo from  './TreeInfo.vue'
    import {Notification} from 'element-ui'
    export default {
        data(){
            var validates = (rule, value, callback) => {
                if (value !='') {
                    callback();
                } else{
                    callback(new Error('未选中!'));
                }
            };

            return{
                isUpdate:false,
                arrCategory:'',
                optionsUnit:[],
                 showSelectTree:false,
                  baseCheckedId: [],
                  baseCheckParentId:[],
                eidtFormRules:{
                    materialName: [
                        {required: true, message: '请输入物料名称', trigger: 'blur'},
                    ],
                    classificationId: [
                        {required: true, message: '请选择物料分类', trigger: 'change'},
                    ],
                    categoryId: [
                        {required: true, message: '请选择物料类别', trigger: 'change'},
                    ],
                    materialUnit: [
                        {required: true, message: '请选择计量单位', trigger: 'change'},
                    ],
                   // lossRate: [
                   //      { type: 'number', message: '损耗率必须为数字值'},
                   //  ],
                    // inventory: [
                    //     {required: true, message: '请输入库存预警', trigger: 'blur'},
                    // ],
             /*       initial: [
                        {required: true, message: '请输入初始库存', trigger: 'blur'},
                    ],*/
                    materialStorageId: [
                        {required: true, message: '请输入物料编码', trigger: 'blur'},
                    ],
                    categoryName: [
                        {required: true, message: '请选择物料类别', trigger: 'blur'},
                    ],

                    
                }
            }
        },
        components:{
                 'tree-add-edit':TreeInfo
                },
        props:['editForm','mode','arrClassification','treeArr'],
        mounted(){
            this.getUnit()
            this.getCategory()
            if(this.mode=='update'){
              // this.isUpdate=true; 
            }
        },
        methods:{
            parseValue() {
                let value = this.editForm.lossRate;
                console.log(value)
                value = value.replace(/[^\d.]/g, ""); // 清除"数字"和"."以外的字符
                value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
                value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                    ".");
                value = value.replace(/^(\-)*(\d+)\.(\d\d\d\d).*$/, '$1$2.$3'); // 只能输入3个小数
                this.editForm.lossRate = value;
            },
            checkoutStatusFinish(id,name) {
                this.editForm.categoryName = name;
                this.editForm.categoryId = id;
                 this.editForm.classificationId = id;
               
                this.showSelectTree = false
            },
            checkoutStatusCancel() {
                this.showSelectTree = false
            },
            showSelectTrees(){
                this.baseCheckedId.splice(0,this.baseCheckedId.length);
                this.baseCheckedId.push(this.editForm.categoryId);

                this.baseCheckParentId.splice(0,this.baseCheckedId.length);
                this.baseCheckParentId.push(this.editForm.categoryId);
                
                this.showSelectTree = true
            },
            editFormCancel(){
                this.$emit('materialStorage-edit-cancel')
            },
            editFormSubmmit(formName){
                if(this.editForm.lossRate && (this.editForm.lossRate<0 || this.editForm.lossRate>0.5)){
                    Notification({
                        message: "损耗率必须为数字值且在0到0.5之间",
                        type: 'warning'
                    })
                    return
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.mode=='update'){

                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });

                            materialStorageModel.materialStorageUpdate(this.editForm)
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
                                    this.$emit('materialStorage-edit-finish')
                                })
                        }else if(this.mode=='add'){
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            materialStorageModel.materialStorageAdd(this.editForm)
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
                                    this.$emit('materialStorage-edit-finish')
                                })
                        }

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            showCategory(value){
                categoryModel.getCategorysByClassificationId(value).then((data)=>{
                    this.arrCategory = data.entity
                })
            },
            getCategory(){
                categoryModel.getCategorys().then((data)=>{
                    this.arrCategory = data.entity
                })
            },
            getUnit(){
                materialStorageModel.findAllByOrgIds().then((data)=>{
                    this.optionsUnit= data.entity
                })
            },
        },

    }
</script>

<style scoped>

</style>