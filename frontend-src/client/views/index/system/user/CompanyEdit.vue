<template>
    <div>
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px">

            <el-form-item label="公司名称" prop="companyName" label-width="110px">
                <el-input v-model="editForm.companyName" style="width: 185px"></el-input>
            </el-form-item>
            <el-form-item label="工厂识别码" prop="factoryIdCode" label-width="110px">
                <el-input v-model="editForm.factoryIdCode" style="width: 185px"></el-input>
            </el-form-item>
            <el-form-item label="logo" prop="logoUrl" label-width="110px">
                <el-upload
                        id="upload"
                        class="upload-demo"
                        action=""
                        :before-upload="beforeAvatarUpload"
                        :multiple="true"
                >
                    <el-button  class="fs-btn fs-btn-primary fs-btn-md"   type="primary">上传图片</el-button>
                </el-upload>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editFormCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import companyModel from '../../../../model/company/company'
    import clientModel from '../../../../model/client-model'
    import {Notification} from 'element-ui'

    export default {
        name: "CompanyManage",
        props: ['editForm', 'mode'],
        data() {
            return {
                eidtFormRules: {
                    companyName: [
                        {required: true, message: '请输入公司名称', trigger: 'blur'},
                    ],
                    factoryIdCode: [
                        {required: true, message: '请输入工厂识别码', trigger: 'blur'},
                    ],

                }
            }
        },
        methods: {
            editFormCancel() {
                this.$emit('edit-cancel')
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.mode == 'edit') {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            companyModel.companyUpdate(this.editForm)
                                .then((data) => {
                                    if (data.status == 'failed') {
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
                                    this.$emit('edit-finish')
                                })
                        } else if (this.mode == 'add') {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            companyModel.companyAdd(this.editForm)
                                .then((data) => {
                                    if (data.status == 'failed') {
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
                                    this.$emit('edit-finish')
                                })
                        }

                    } else {
                        return false;
                    }
                });
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
                    clientModel.ax.post('/company/getUploadPhoto',file,{
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
                        this.editForm.logoUrl = data.entity
                        Notification({
                            message: '上传成功',
                            type: 'success'
                        })
                    })
                    return false;
                }

            },
        },

    }
</script>

<style scoped>

</style>