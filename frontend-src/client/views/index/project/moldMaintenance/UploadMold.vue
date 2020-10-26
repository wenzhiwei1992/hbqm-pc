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
                style="display: inline;margin-left: 50px"
                   class="upload-demo"
                   action=""
                   :before-upload="beforeAvatarUpload"
                   :multiple="true"
            >
                <el-button  type="primary" class="fs-btn fs-btn-primary fs-btn-md">导入数据</el-button>
                <span  style="color: red;font-size: small;margin-left:20px;">*请先选择项目</span>
            </el-upload>
        </el-row>



    </div>
</template>

<script>
    import clientModel from '../../../../model/client-model'
    import {Notification} from 'element-ui'
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
            beforeAvatarUpload(file){
                console.log(file.name)
                /*const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });*/
                if(this.uploadEditForm.project.id ==''){
                    this.$message.error(this.GLOBAL.PROJECT_MSG.SELECT_NOT_PROJECT);
                   // loading.close()
                    return false;
                }
                let Xls = file.name.split('.')
                if (Xls[1] === 'xls' || Xls[1] === 'xlsx') {
                    var formData = new FormData();
                    formData.append('file', file);
                    var file = formData;
                    clientModel.ax.post('/mold/readExcelProductFloor?id=' +this.uploadEditForm.project.id, file, {
                        headers: {
                            'Content-Type': 'charset=UTF-8',
                        },
                    })
                        .then(data => {
                            if (data.status == "failed") {
                                Notification({
                                    message: data.msg,
                                    type: "warning"
                                });
                                loading.close()
                                return;
                            }
                            this.$emit('upload-edit-finish')
                            Notification({
                                message: "导入成功",
                                type: "success"
                            });
                           // loading.close()
                        });
                    console.log("duojishan")
                    //loading.close()
                    return false;
                } else {
                    loading.close()
                    this.$message.error('上传文件只能是 xls/xlsx 格式!')
                    return false;
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
</style>
