<template>
    <div>
        <table>
            <tr>
                <td style="text-align: center">
                    <el-upload
                        id="upload"
                        style="display: inline"
                        class="upload-demo"
                        action=""
                        :before-upload="beforeAvatarUpload"
                        :multiple="true"
                >
                    <el-button type="primary" class="fs-btn fs-btn-primary fs-btn-md">更新模板</el-button>
                </el-upload></td>
                <td style="text-align: center"><el-button @click="download">下载模板</el-button></td>
                <td><span style="color: red">*第一次下载请先更新模板</span></td>
            </tr>
        </table>
    </div>
</template>

<script>
    import serviceConfig  from  '../../../server.config.js'
    import clientModel from '../../model/client-model'
    import model from "../../model/model";
    import {Notification} from 'element-ui'
    export default {
        name: "fileUpload",
        data(){
            return{

            }
        },
        props: ['fileName'],
        mounted(){
            console.log(this.fileName)
        },
        methods:{
            download(){
                let fileName = this.fileName;
                model.exportFileExists(fileName).then((data) => {
                    if (data.status == 'failed') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    }
                    location.href = serviceConfig.baseURL+'/file/exportTemplateFile?fileName='+fileName;
                    Notification({
                        message: '下载成功',
                        type: 'success'
                    })
                })

            },
            beforeAvatarUpload (file) {
                let Xls = file.name.split('.')
                if (Xls[1] === 'xls' || Xls[1] === 'xlsx') {
                    var formData=new FormData();
                    formData.append('file',file);
                    var file = formData;
                    clientModel.ax.post('/file/uploadTemplateFile',file,{
                        headers:{
                            'Content-Type':'charset=UTF-8',
                        },
                    }).then((data)=>{
                        if (data.status == 'fail') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            return
                        }
                        Notification({
                            message: '更新成功',
                            type: 'success'

                        })
                    })
                    return false
                } else {
                    this.$message.error('上传文件只能是 xls/xlsx 格式!')
                    return false
                }
            },
        }
    }
</script>

<style scoped>

</style>