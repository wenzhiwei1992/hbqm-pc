<template>
    <div v-loading="loading">
        <el-col style="display:flex;margin-top: -10px;height:200px">
            <div>
                <span class="color_font_span_class">构件编号:</span>
            </div>
            <div style="margin-left:10px;">

            <el-tag v-for="(item,index) in arrProductNos" style="margin-left:4px;"  size="lager" :key="index" :label="item" type="success">{{item}}</el-tag> </div>
        </el-col>

        <el-row>
        <el-upload
                ref="upload"
                name="fileupload"
                class="upload-demo"
                :action='UploadUrl()'
                list-type="picture"
                :http-request="uploadSectionFile"
                accept="image/jpeg,image/png"
                :auto-upload="false">
            <el-button slot="trigger" size="medium" type="primary">选取文件</el-button>
            <el-button style="margin-left: 10px;" size="medium" type="success" @click="submitUpload">上传到服务器</el-button>
        </el-upload>
            </el-row>
    </div>
</template>

<script>
    import axios from 'axios'
    import clientModel from '../../../../model/client-model'
    import {Notification} from 'element-ui'
    import  projectModel from '../../../../model/project/project'
    import serviceConfig from '../../../../../server.config.js'
    export default {
        props:['uploadPDFForm','arrProductIds','arrProductNos'],

        data(){
            return{
                formDate:'',
                loading:false,

            }
        },
        methods:{
            uploadSectionFile(param){
                this.formDate.append('fileupload', param.file);
            },
            UploadUrl:function(){
                return serviceConfig.baseURL+'/webjars/readProductExcels?productIds='+this.arrProductIds;
            },
            submitUpload() {
                this.loading = true;
                this.formDate = new FormData()
                this.$refs.upload.submit();
                var fd = this.formDate;
                fd.append('productIds', this.arrProductIds);
                fd.append('folderName', 'productBom');


                clientModel.ax.post('/webjars/readProductExcels', fd).then((data) => {
                    if (data.status == 'failed') {
                        Notification({
                            message: data.msg,
                            type: 'warning',
                            duration:0,
                        })
                        this.loading = false;
                        return
                    }
                    Notification({
                        message: '导入成功',
                        type: 'success'

                    })
                    this.$refs.upload.clearFiles();
                    this.loading = false;
                    this.$emit('upload-edit-finish')
                })

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
</style>