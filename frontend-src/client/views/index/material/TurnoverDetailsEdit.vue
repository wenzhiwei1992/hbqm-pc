<template>
    <div>
        <el-form :inline="true" :model="editForm" v-if="editForm"  :rules="eidtFormRules"     ref="editForm" label-width="80px">
            <div class="divType">
                <el-form-item label="原料编码" prop="materialStorageId" >
                    <div v-if="mode=='add'">
                        <el-select v-model="editForm.materialStorageId" filterable placeholder="请选择" @change="getMaterialStorageByMaterialStorageId" style="width: 170px">
                            <el-option
                                    v-for="item in arrMaterialStorage"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </div>
                    <div v-if="mode=='update'">
                        <el-select v-model="editForm.materialStorageId"  disabled  placeholder="请选择" @change="getMaterialStorageByMaterialStorageId"  style="width: 170px">
                            <el-option
                                    v-for="item in arrMaterialStorage"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </div>
                </el-form-item>
                <el-form-item label="原料名称" prop="materialName">
                    <el-input v-model="editForm.materialName"  :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="原料规格" prop="materialSpecification">
                    <el-input v-model="editForm.materialSpecification"  :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="计量单位" prop="materialUnit ">
                    <el-input v-model="editForm.materialUnit" :disabled="true"></el-input>
                </el-form-item>
            </div>
            <div class="divType2">
                <el-form-item label="单价"  prop="univalence">
                    <el-input v-model="editForm.univalence" id="number" type="number" @change="sumTotal()"></el-input>
                </el-form-item>
                <el-form-item label="运费"  prop="freight">
                    <el-input v-model="editForm.freight"></el-input>
                </el-form-item>
                <el-form-item label="车牌号"  prop="licenseNo">
                    <el-input v-model="editForm.licenseNo" ></el-input>
                </el-form-item>
                <el-form-item label="供方"  prop="shipmentsId">
                    <el-select v-model="editForm.shipmentsId" placeholder="请选择" filterable style="width: 170px">
                                <el-option
                                        v-for="item in shipmentsSelect"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                    </el-select>
                </el-form-item>
                <el-button @click="skip('../basicdata/supplier')">添加</el-button>
       <!--         <el-form-item label="运输单位"  prop="transport">
                    <el-input v-model="editForm.transport " ></el-input>
                </el-form-item>-->

                <el-form-item label="部门"  prop="departmentId">
                    <el-select v-model="editForm.departmentId" placeholder="请选择"clearable  style="width: 170px">
                                <el-option
                                        v-for="item in departmentSelect" filterable
                                        :key="item.id"
                                        :label="item.deptName"
                                        :value="item.id">
                                </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="仓库"  prop="stockId">
                    <el-select v-model="editForm.stockId" placeholder="请选择"  style="width: 170px">
                                <el-option
                                        v-for="item in stockSelect"
                                        :key="item.stockName"
                                        :label="item.stockName"
                                        :value="item.id">
                                </el-option>
                    </el-select>
                </el-form-item>
            </div>
            <div class="divType3">
                <el-form-item label="毛重"  prop="roughWeight">
                    <el-input v-model="editForm.roughWeight"  type="number"  @change="getSuttleAndRealityWeightByCount"></el-input>
                </el-form-item>
                <el-form-item label="皮重"  prop="tare">
                    <el-input v-model="editForm.tare"   type="number" @change="getSuttleAndRealityWeightByCount"></el-input>
                </el-form-item>
                <el-form-item label="杂扣"   prop="sundries">
                    <el-input v-model="editForm.sundries"     type="number" @change="getSuttleAndRealityWeightByCount"></el-input>
                </el-form-item>
                <el-form-item label="净重"  prop="suttle">
                    <el-input v-model="editForm.suttle" :disabled="true" ></el-input>
                </el-form-item>
                <el-form-item label="实重"  prop="realityWeight">
                    <el-input v-model="editForm.realityWeight"  :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="总价"  prop="totalUnivalence"  >
                    <el-input v-model="editForm.totalUnivalence"  type="number" @change="sumUnivalence()"></el-input>
                </el-form-item>
            </div>
            <div class="divType4">
                <el-form-item label="备注"  prop="remark">
                    <el-input v-model="editForm.remark"  type="textarea" ></el-input>
                </el-form-item>
<!--                <el-form-item label="图片上传"  prop="receipt">
                    <el-upload
                            id="upload"
                            class="upload-demo"
                             action=""
                            :before-upload="beforeAvatarUpload"
                            :multiple="true"
                    >
                        <el-button  class="fs-btn fs-btn-primary fs-btn-md"   type="primary">上传图片</el-button>
                    </el-upload>
                </el-form-item>-->
            </div>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editFormCancel">关 闭</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import turnoverDetailModel  from '../../../model/materials/turnoverDetail'
    import turnoverRecordModel  from '../../../model/materials/turnoverRecord'
    import materialStorageModel  from '../../../model/basicdata/materialStorage'
    import serverConfig from '../../../../server.config'
    import clientModel from '../../../model/client-model'
    import {Notification} from 'element-ui'
    export default {
        props:['editForm','mode','arrMaterialStorage','departmentSelect','stockSelect','shipmentsSelect'],
        mounted(){
           // this.getMaterialStorage()
        },
        methods:{
            sumTotal(){
                if(this.editForm.univalence && this.editForm.realityWeight){
                    this.editForm.totalUnivalence = this.editForm.realityWeight*this.editForm.univalence
                    this.editForm.totalQuantity = this.editForm.totalUnivalence.toFixed(4);
                }
            },
            sumUnivalence(){
                if(this.editForm.totalUnivalence && this.editForm.realityWeight){
                    this.editForm.univalence = this.editForm.totalUnivalence/this.editForm.realityWeight
                    this.editForm.univalence = this.editForm.univalence.toFixed(4);
                }
            },
            skip(a){
                this.$router.push(a)
            },
            handlePreview(file) {
                console.log(file);
            },
            submitUpload() {
                this.$refs.upload.submit();
            },
            uploadPhoto(){

            },
            // 上传成功后的回调
            uploadSuccess (response, file, fileList) {
                console.log('上传文件', response)
            },
            // 上传错误
            uploadError (response, file, fileList) {
                console.log('上传失败，请重试！')
            },
            beforeAvatarUpload (file) {
                console.log(file.name)
                let reg = /\.(jpg|jpeg|png)$/i;
                if (!reg.test(file.name)) {
                    this.$message({
                        showClose: true,
                        message: '上传支持格式（.jpg/.jpeg/.png）',
                        type: 'error'
                    });
                    return false;
                }
                else if (file.size > 500 * 1024) {
                    this.$message({
                        showClose: true,
                        message: '文件大小上限为500K',
                        type: 'error'
                    });
                    return false;
                }else{
                    var formData=new FormData();
                    formData.append('file',file);
                    var file = formData;
                    clientModel.ax.post('/turnoverRecord/getUploadPhoto',file,{
                        headers:{
                            'Content-Type':'charset=UTF-8',
                        },
                    }).then((data)=>{
                        if(data.status=='fail'){
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            return
                        }
                        this.editForm.receipt = data.entity
                        Notification({
                            message: '上传成功',
                            type: 'success'
                        })
                    })
                    return false;
                }

            },
            importFileUrl(){
              return   serverConfig.baseURL+'/turnoverRecord/getUploadPhoto'
            },
            getSuttleAndRealityWeightByCount(){
               var turnoverQuery = {
                   roughWeight:  this.editForm.roughWeight,
                   tare: this.editForm.tare,
                   sundries:this.editForm.sundries
               }
                turnoverDetailModel.getSuttleAndRealityWeightByCount(turnoverQuery).then((data)=>{
                    this.editForm.suttle = data.entity.suttle
                    this.editForm.realityWeight = data.entity.realityWeight
                    this.sumTotal();
                })

            },
            getMaterialStorage(){
                materialStorageModel.getMaterialStorageByMaterialStorage().then((data)=>{
                    var arr = new Array()
                    for (var i=0;i<data.entity.length;i++){
                        var materialStorageId =parseInt( data.entity[i]).materialStorageId
                        var v ={
                            label:materialStorageId,
                            value:data.entity[i].materialStorageId+'['+data.entity[i].materialName+';'+data.entity[i].materialSpecification+']'
                        }
                        arr.push(v)
                    }
                   // this.arrMaterialStorage = arr
                })
            },
            getMaterialStorageByMaterialStorageId(){
                 var arr = this.editForm.materialStorageId.split('[')
                materialStorageModel.getMaterialStorageByMaterialStorageId(arr[0]).then((data)=>{
                    console.log(data.entity)
                    this.editForm.materialName = data.entity.materialName
                    this.editForm.materialSpecification = data.entity.materialSpecification
                    data.entity.materialUnit = parseInt(data.entity.materialUnit)
                    materialStorageModel.getOptionUnitsById(data.entity.materialUnit).then((data)=>{
                        this.editForm.materialUnit = data.entity.unitCNName
                    })
                })
            },
            editFormCancel(){
                this.$emit('turnoverDetail-edit-cancel')
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
                            this.editForm.shipments = ''+'-'+this.editForm.turnoverRecordId
                            turnoverRecordModel.turnoverRecordUpdate(this.editForm)
                                .then((data) => {
                                    if(data.status=='fail'){
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close()
                                        return
                                    }
                                    turnoverDetailModel.turnoverDetailUpdate(this.editForm).then((data)=>{
                                        if(data.status=='fail'){
                                            Notification({
                                                message: data.msg,
                                                type: 'warning'
                                            })
                                            loading.close()
                                            return
                                        }
                                    })
                                    Notification({
                                        message: '编辑成功',
                                        type: 'success'
                                    })
                                    loading.close();
                                    this.$emit('turnoverDetail-edit-finish')
                                })
                        }else if(this.mode=='add'){
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            if(this.editForm.univalence==''){
                                this.editForm.univalence=0
                            }
                            turnoverRecordModel.turnoverRecordAdd(this.editForm)
                                .then((data) => {
                                    if(data.status=='fail'){
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close()
                                        return
                                    }
                                    turnoverDetailModel.turnoverDetailAdd(this.editForm).then((data)=>{
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
                                    this.$emit('turnoverDetail-edit-finish')
                                })
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
            var checkPlateCount = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('填写合法数字'));
                }
              //  var r = /^\+?[0-9][0-9]*$/
                var r = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/
                if (!r.test(value)) {
                    callback(new Error('请输入正整数'));
                } else {
                    if(value<0){
                        callback(new Error('必须大于0'));
                    } else {
                        callback();
                    }
                }
            };
            return{
                headers: {
                    'Content-Type':'multipart/form-data;charset=UTF-8',
                },
                upLoadData:null,
                file:null,
                eidtFormRules: {
                    materialStorageId: [
                        {required: true, message: '原料编码不能为空', trigger: 'change'},
                    ],
                    shipmentsId: [
                        {required: true, message: '供方不能为空', trigger: 'change'},
                    ],
                    // departmentId: [
                    //     {required: true, message: '部门不能为空', trigger: 'change'},
                    // ],
                    stockId: [
                        {required: true, message: '仓库不能为空', trigger: 'change'},
                    ],
             /*       univalence: [
                         {required: true,  validator: checkPlateCount, trigger: 'blur'},
                   ],*/
         /*           licenseNo: [
                        {required: true, message: '车牌号不能为空', trigger: 'blur'},
                    ],*/
                    roughWeight: [
                        {required: true, validator: checkPlateCount, trigger: 'blur'},
                    ],
                    tare: [
                        {required: true, validator: checkPlateCount, trigger: 'blur'},
                    ],
                    sundries: [
                        {required: true, validator: checkPlateCount, trigger: 'blur'},
                    ],
                },
                options: [{
                    value: '吨',
                    label: '吨'
                },]
            }
        }
    }
</script>

<style scoped>
    .divType{
        display: block;
        border: 1px lavender solid;
        margin-top: -10px;
        padding-top: 10px;
    }
    .divType2{
        border: 1px lavender solid;
        margin-top: 10px;
        padding-top: 10px;
    }
    .divType3{
        border: 1px lavender solid;
        margin-top: 10px;
        padding-top: 10px;
    }
    .divType4{
        border: 1px lavender solid;
        margin-top: 10px;
        padding-top: 10px;
    }
</style>