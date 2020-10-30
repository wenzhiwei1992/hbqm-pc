<template>
    <div>
        <el-form :model="uploadEditForm" v-if="uploadEditForm" :rules="uploadeidtFormRules" ref="uploadEditForm" label-width="80px">
            <el-form-item label="项目名称" prop="projectName">
                <el-select v-model="uploadEditForm.project.id" placeholder="请选择">
                    <el-option
                            v-for="item in arrProject"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <el-row>
            <el-upload
                id="upload"
                style="display: inline"
                   class="upload-demo"
                   action=""
                   :before-upload="beforeAvatarUpload"
                   :multiple="true"
            >
                <el-button  type="primary" class="fs-btn fs-btn-primary fs-btn-md">导入数据</el-button><span  style="color: red;font-size: small">*请先选择项目</span>
            </el-upload>
            <el-upload
                id="upload2"
                style="display: inline"
                   class="upload-demo"
                   action=""
                   :before-upload="beforeAvatarUploadForPxml"
                   :multiple="true"
            >
                <el-button type="primary"  v-if="authorized('productsMaintenance:xmlExcel')"  class="fs-btn fs-btn-primary fs-btn-md">导入PXML</el-button>
            </el-upload>
            <el-upload
                    id="upload3"
                    style="display: inline"
                    class="upload-demo"
                    action=""
                    :before-upload="beforeAvatarUploadForBom"
                    :multiple="true"
            >
                <el-button  v-if="authorized('productsMaintenance:bomExcel')" type="primary" class="fs-btn fs-btn-primary fs-btn-md">bom变更导入数据</el-button>
            </el-upload>

            <el-upload
                    id="upload4"
                    style="display: inline"
                    class="upload-demo"
                    action=""
                    :before-upload="beforeAvatarUploadForVertical"
                    :multiple="true"
            >
                <el-button  v-if="authorized('productsMaintenance:verticalExcel')" type="primary" class="fs-btn fs-btn-primary fs-btn-md">竖版导入数据</el-button>
            </el-upload>
        </el-row>



    </div>
</template>

<script>
    import axios from 'axios'
    import clientModel from '../../../../model/client-model'
    import {Notification} from 'element-ui'
    import  projectModel from '../../../../model/project/project'
    import menuTabService from "../../../../service/menuTabService";
    export default {
        props:['uploadEditForm','arrProject'],
        data(){
            return{
                uploadeidtFormRules:{

                }
            }
        },
        methods:{
            authorized(index){
                return menuTabService.authorized(this.$store,index)
            },
            beforeAvatarUpload (file) {
                console.log(file.name)
                let Xls = file.name.split('.')
                if ( Xls[1] === "xls" || Xls[1] === 'xlsx') {
                    //Xls[1] === 'xls' ||
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    if(this.uploadEditForm.project.id ==''){
                        this.$message.error(this.GLOBAL.PROJECT_MSG.SELECT_NOT_PROJECT);
                        loading.close()
                        return false;
                    }
                    var formData=new FormData();
                    formData.append('file',file);
                    var file = formData;
                    clientModel.ax.post('/productExcel/readProductExcels?projectId='+this.uploadEditForm.project.id,file,{
                        headers:{
                            'Content-Type':'charset=UTF-8',
                            //  'Access-Control-Allow-Origin':'*'
                        },
                    }).then((data)=>{
                        if (data.status == 'failed') {
                            Notification({
                                message: data.msg,
                                type: 'warning',
                                duration:0,
                            })
                            loading.close()
                            return
                        }
                        this.$emit('upload-edit-finish')
                        Notification({
                            message: '导入成功',
                            type: 'success'

                        })
                        loading.close()
                    })
                    return false
                } else {
                    this.$message.error('上传文件只能是 xls/xlsx 格式!')
                    return false
                }
            },
            beforeAvatarUploadForPxml (file) {
                console.log(file.name)
                let Xls = file.name.split('.')
                if ( Xls[Xls.length-1] === "xml") {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    var formData=new FormData();
                    formData.append('file',file);
                    var file = formData;
                    clientModel.ax.post('/resolvePxml?projectId='+this.uploadEditForm.project.id,file,{
                        headers:{
                            'Content-Type':'charset=UTF-8',
                        },
                    }).then((data)=>{
                        if (data.status == 'failed') {
                            Notification({
                                message: data.msg,
                                type: 'warning',
                                duration:0,
                            })
                            loading.close()
                            return
                        }
                        this.$emit('upload-edit-finish')
                        Notification({
                            message: '导入成功',
                            type: 'success'

                        })
                        loading.close()
                    })
                    return false
                } else {
                    this.$message.error('上传文件只能是 xml 格式!')
                    return false
                }
            },
            beforeAvatarUploadForBom (file) {
                console.log(file.name)
                let Xls = file.name.split('.')
                if ( Xls[1] === "xls" || Xls[1] === 'xlsx') {
                    //Xls[1] === 'xls' ||
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    if(this.uploadEditForm.project.id ==''){
                        this.$message.error(this.GLOBAL.PROJECT_MSG.SELECT_NOT_PROJECT);
                        loading.close()
                        return false;
                    }
                    var formData=new FormData();
                    formData.append('file',file);
                    var file = formData;
                    clientModel.ax.post('/productExcel/bomProductExcels?projectId='+this.uploadEditForm.project.id,file,{
                        headers:{
                            'Content-Type':'charset=UTF-8',
                            //  'Access-Control-Allow-Origin':'*'
                        },
                    }).then((data)=>{
                        if (data.status == 'failed') {
                            Notification({
                                message: data.msg,
                                type: 'warning',
                                duration:0,
                            })
                            loading.close()
                            return
                        }
                        this.$emit('upload-edit-finish')
                        Notification({
                            message: '导入成功',
                            type: 'success'

                        })
                        loading.close()
                    })
                    return false
                } else {
                    this.$message.error('上传文件只能是 xls/xlsx 格式!')
                    return false
                }
            },

            beforeAvatarUploadForVertical (file) {
                console.log(file.name)
                let Xls = file.name.split('.')
                if ( Xls[1] === "xls" || Xls[1] === 'xlsx') {
                    //Xls[1] === 'xls' ||
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    if(this.uploadEditForm.project.id ==''){
                        this.$message.error(this.GLOBAL.PROJECT_MSG.SELECT_NOT_PROJECT);
                        loading.close()
                        return false;
                    }
                    var formData=new FormData();
                    formData.append('file',file);
                    var file = formData;
                    clientModel.ax.post('/productExcel/readExcel?projectId='+this.uploadEditForm.project.id,file,{
                        headers:{
                            'Content-Type':'charset=UTF-8',
                            //  'Access-Control-Allow-Origin':'*'
                        },
                    }).then((data)=>{
                        if (data.status == 'failed') {
                            Notification({
                                message: data.msg,
                                type: 'warning',
                                duration:0,
                            })
                            loading.close()
                            return
                        }
                        this.$emit('upload-edit-finish')
                        Notification({
                            message: '导入成功',
                            type: 'success'

                        })
                        loading.close()
                    })
                    return false
                } else {
                    this.$message.error('上传文件只能是 xls/xlsx 格式!')
                    return false
                }
            },
        },

    }
</script>

<style scoped>


   .el-dialog__body{
        float: inherit;
        background-color: white;
    }
    #upload{
        float: left;
        margin-right: 2rem;
    }
   #upload2{
       float: left;
       margin-right: 4rem;
   }
   #upload3{
       float: left;
       margin-right: 6rem;
   }
   #upload4{
       float: left;

   }
</style>
