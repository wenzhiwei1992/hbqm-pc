<template>
    <div id="check-edit">
        <el-form  ref="editForm" label-width="80px"size="small">
            <el-form-item label="构件编号">
                <el-input :value="productDetail.productNo" disabled></el-input>
            </el-form-item>
            <el-form-item label="构件名称">
                <el-input :value="productDetail.productName" disabled></el-input>
            </el-form-item>
        </el-form>
        <el-form :model="editForm"  ref="editForm" size="small">
            <el-form-item label="检验说明">
                <el-input type="textarea" v-model="editForm.checkDesc"></el-input>
            </el-form-item>
            <el-form-item label="检验结果">
                <el-select v-model="editForm.checkResult" @change="changeCheckResult">
                    <el-option
                            v-for="item in resultList"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                    >
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="返修类型" v-if="!rectifySgtVisible">
                <el-radio v-model="editForm.rectifySgt" v-if="checkType=='inStock'" label="堆场返修">堆场返修</el-radio>

                <el-radio v-model="editForm.rectifySgt" v-if="checkType=='invoice'" label="出场返修">出场返修</el-radio>
            </el-form-item>

            <el-form-item label="报废类型" v-if="rectifySgtVisible">

                <el-radio v-model="editForm.rectifySgt" v-if="checkType=='inStock'" label="堆场报废">堆场报废</el-radio>
                <el-radio v-model="editForm.rectifySgt" v-if="checkType=='inStock'" label="变更报废">变更报废</el-radio>

                <el-radio v-model="editForm.rectifySgt" v-if="checkType=='invoice'" label="出场报废">出场报废</el-radio>
            </el-form-item>

            <el-form-item label="检验图片" v-model="editForm.fileupload" >

                <el-upload
                        class="avatar-uploader"
                        :action="UploadUrl()"
                        list-type="picture"
                        name="fileupload"
                        ref="upload"
                        :http-request="uploadSectionFile"
                        :auto-upload="false"
                        accept="image/jpeg,image/png">
                    <el-button size="small" type="primary">选取上传图片</el-button>
                </el-upload>
            </el-form-item>

        </el-form>
        <div style="text-align:right" class="dialog-footer">
            <el-button @click="editFormCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>


</template>
<script>
    import clientModel from '../../../model/client-model'
    import serviceConfig from '../../../../server.config.js'

    import {Notification} from 'element-ui'

    export default {
        props: ['productDetail','checkType'],
        data() {
            return {
                massLevelVisible: false,
                rectifySgtVisible:false,
                editForm: {
                    checkType: '成品检',
                    productDetailId: this.productDetail.productDetailId,
                    checkDesc: '',
                    checkResult: '返修',
                    rectifySgt:'',
                    invoiceDetailId:this.productDetail.id
                },
                items: [],
                resultList: [
                    {label: '不良品', value: '返修'},
                    {label: '报废品', value: '待报废审批'}],
                fileList:[],
                formData:'',
                url:'',
            }
        },
        mounted() {
            if(this.checkType=='inStock'){//堆场
                this.editForm.rectifySgt = '堆场返修'
                this.url = '/check/add';
            }else if(this.checkType=='invoice'){//发货单
                this.editForm.rectifySgt = '出场返修'
                this.url = '/check/addReturnOrFinish';
            }

        },
        computed: {
        },
        methods: {
            changeCheckResult(){

                if (this.editForm.checkResult == '待报废审批') {
                    this.rectifySgtVisible = true;
                    this.checkType == 'inStock' ?  this.editForm.rectifySgt = '堆场报废':this.editForm.rectifySgt = '出场报废';
                } else {
                    this.rectifySgtVisible = false;
                    this.checkType == 'inStock' ?  this.editForm.rectifySgt = '堆场返修':this.editForm.rectifySgt = '出场返修';
                }

            },
            uploadSectionFile(param){
                this.formData.append('fileupload', param.file);
            },

            submitUpload() {
                this.formData = new FormData()
                this.$refs.upload.submit();
                if(this.items){
                    let cacheArray = []
                    this.items.map(val=>{
                        if(val.result==='不合格'){
                            cacheArray.push(val)
                        }
                    })
                    this.editForm.itemResultJson = JSON.stringify(cacheArray)
                }

                let fd = this.formData;
                fd.append('checkType', this.editForm.checkType);
                fd.append('productDetailId', this.editForm.productDetailId);
                fd.append('checkDesc', this.editForm.checkDesc);
                fd.append('checkResult', this.editForm.checkResult);
                fd.append('rectifySgt', this.editForm.rectifySgt);
                fd.append('itemResultJson', this.editForm.itemResultJson);
                if(this.checkType=='invoice'){//发货单
                     fd.append('invoiceDetailId', this.editForm.invoiceDetailId);
                }


                clientModel.ax.post(this.url, fd).then((data) => {

                    if(data.status == 'fail'){
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    } else {
                        Notification({
                            message: '构件已完成报废/返修',
                            type: 'success'

                        })
                    }
                    this.fileList =[]
                    this.$emit('model-edit-finish')
                })

            },
            UploadUrl:function(){
                return serviceConfig.baseURL+'/check/add';
            },
            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg';
                const isPNG = file.type === 'image/png';

                const isLt2M = file.size / 1024 / 1024 < 10;

                if (!isJPG && !isPNG) {
                    this.$message.error('上传头像图片只能是 JPG/PNG 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传头像图片大小不能超过 10MB!');
                }
                return (isJPG || isPNG) && isLt2M;
            },
            editFormCancel() {
                this.$emit('model-edit-cancel')
            },
            editFormSubmmit(formName) {
               this.submitUpload();
            },


        },


    }
</script>
<style>
    #check-edit .switch-wrap {
        height: 450px;
        margin: 10px 0;
        border: 1px solid #EBEEF5;
        overflow-y: scroll;
    }

    #check-edit .el-form-item__label {
        font-size: 16px;

    }

    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }
</style>