<template>
    <div>
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px">
            <el-form-item label="项目编号" prop="projectNo">
                <el-input  onkeyup="value=value.replace(/[^A-Za-z0-9]/g,'')"  v-model="editForm.projectNo"></el-input>
            </el-form-item>
            <el-form-item label="项目名称" prop="projectName">
                <el-input v-model="editForm.projectName"></el-input>
            </el-form-item>
            <el-form-item label="客户名称" prop="customer">
                <el-input v-model="editForm.customer"></el-input>
            </el-form-item>
            <el-form-item label="项目地点" prop="workPlace">
                <el-input v-model="editForm.workPlace"></el-input>
            </el-form-item>
            <el-form-item label="标识单位" prop="undertaking">
                <el-input v-model="editForm.undertaking"></el-input>
            </el-form-item>

            <el-form-item label="开始时间" prop="planStartDate">
                <el-date-picker
                        v-model="editForm.planStartDate"
                        type="date"
                        placeholder="选择开始时间"
                        value-format="yyyy-MM-dd">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="结束时间" prop="planEndDate">
                <el-date-picker
                        v-model="editForm.planEndDate"
                        type="date"
                        placeholder="选择结束时间"
                        value-format="yyyy-MM-dd">
                </el-date-picker>
            </el-form-item>

            <el-form-item label="项目工期" prop="planProjectDuration">
                <el-input v-model="editForm.planProjectDuration"></el-input>
            </el-form-item>
            <el-form-item label="项目状态" prop="projectStatus">
               <!-- <el-input v-model="editForm.projectStatus" placeholder="0：待生产、1：生产中、2：已完成"></el-input>-->
                <el-select v-model="editForm.projectStatus" clearable placeholder="请选择">
                    <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>


            <el-form-item label="logo" prop="logoPath" >
                <el-upload
                        class="avatar-uploader"
                        :action="UploadUrl()"
                        :show-file-list="false"
                        name="fileupload"
                        :on-success="handleAvatarSuccess"
                        :data="paramsData"
                        accept="image/jpeg,image/png"
                        :before-upload="beforeAvatarUpload">
                    <img v-if="imageUrl" :src="imageUrl" class="avatar">
                    <el-button v-else slot="trigger" size="small" type="primary">选取文件</el-button>
                    <!--<i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
                    <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过10M,建议尺寸为正方形</div>
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
    import projectModel from '../../../../model/project/project'
    import {Notification} from 'element-ui'
    import serviceConfig from '../../../../../server.config.js'

    export default {
        props:['editForm','mode'],
        data(){
            return{
                eidtFormRules: {
                    projectNo: [
                        {required: true, message: '请输入项目编号', trigger: 'blur'},
                    ],
                    projectName: [
                        {required: true, message: '请输入项目名称', trigger: 'blur'},
                    ],
                    customer: [
                        {required: true, message: '请输入用户名称', trigger: 'blur'},
                    ],
                    workPlace: [
                        {required: true, message: '请输入工地名称', trigger: 'blur'},
                    ],
                    undertaking: [
                        {required: true, message: '二维码需求单位', trigger: 'blur'},
                    ],
                    planStartDate: [
                        {message: '请选择项目开始时间', trigger: 'blur'},
                    ],
                    planEndDate: [
                        {message: '请输入项目结束时间', trigger: 'blur'},
                    ],
                     projectStatus: [
                         {message: '请输入项目状态', trigger: 'blur'},
                     ],
                    planProjectDuration: [],

                },
                options: [{
                    value: '0',
                    label: '待生产'
                }, {
                    value: '1',
                    label: '生产中'
                }, {
                    value: '2',
                    label: '已完成'
                }],
                value: '',
                fileList:[],
                imageUrl: '',
                paramsData:{
                    folderName:"projectLogo",
                    fileName:""
                }
            }
        },
        watch: {
            editForm: function () {
                this.init()
            }
        },
        mounted() {
            this.init();
        },
        methods:{
            init() {
                if(this.editForm.logoPath){
                    this.imageUrl = this.editForm.baseUrl+this.editForm.logoPath;
                }else{
                    this.imageUrl = '';
                }

            },
            UploadUrl:function(){
                return serviceConfig.baseURL+'/fileUpload/upload';
            },
            handleAvatarSuccess(res, file) {
                this.imageUrl = URL.createObjectURL(file.raw);
                if(res.entity){
                    this.editForm.logoPath = res.entity;
                }
                console.log(res)
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
                // this.paramsData.fileName = file.name
                // let timeStr = Number(new Date());
                // let name = timeStr+"."+file.name.split(".")[file.name.split(".").length-1];
                // this.paramsData.fileName = name;
                return (isJPG || isPNG) && isLt2M;
            },
            editFormCancel(){
                this.$emit('model-edit-cancel')
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
                            projectModel.projectUpdate(this.editForm)
                                .then((data) => {
                                    if(data.status=='fail'){
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close()
                                        return
                                    }
                                    loading.close();
                                    Notification({
                                        message: '编辑成功',
                                        type: 'success'
                                    })
                                    loading.close();
                                    this.editFormVisible=false
                                    this.$emit('model-edit-finish')
                                })
                        }else {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            projectModel.projectAdd(this.editForm)
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
                                    this.editFormVisible=false
                                    this.$emit('model-edit-finish')
                                })
                        }

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            }
        }

    }
</script>

<style scoped>
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
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }
    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }
</style>