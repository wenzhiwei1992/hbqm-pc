<template>
    <div>
        <el-form :model="editForm"  :rules="eidtFormRules" ref="editForm" label-width="150px"
                 size="small">
            <el-form-item label="项目" prop="projectId">
                <el-select v-model="editForm.projectId" @change="changeProject" filterable clearable placeholder="请选择">
                    <el-option
                            v-for="item in arrProject"
                            :key="item.id"
                            :label="item.projectName"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="楼栋" prop="buildId">
                <el-select
                        v-model="editForm.buildId"
                        @change="changeBuilding"
                        placeholder="楼栋"
                        :disabled="!editForm.projectId"
                        clearable
                        filterable
                >
                    <el-option
                            :key="item.id"
                            :value="item.id"
                            v-for="item in buildings"
                            :label="item.buildCode"
                    >
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="楼层" prop="floorId">
                <el-select
                        v-model="editForm.floorId"
                        :disabled="!editForm.buildId"
                        clearable
                        filterable
                        placeholder="楼层"
                >
                    <el-option
                            v-for="item in floors"
                            :key="item.id"
                            :label="item.floorNum"
                            :value="item.id"
                    >
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="工程部位" prop="productTypeId">
                <el-select v-model="editForm.productTypeId" filterable clearable placeholder="请选择">
                    <el-option
                            v-for="item in arrProductType"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="强度等级" prop="productGrade">
                <el-input v-model="editForm.productGrade"></el-input>
            </el-form-item>
            <el-form-item label="生产方量" prop="productConcrete">
                <el-input v-model="editForm.productConcrete"></el-input>
            </el-form-item>
            <el-form-item label="报告日期" prop="checkTime">
                <el-date-picker
                        v-model="editForm.checkTime"
                        type="date"
                        format="yyyyMMdd"
                        placeholder="选择日期时间">
                </el-date-picker>
            </el-form-item>

            <el-form-item label="要求" prop="remark">
                <el-input v-model="editForm.remark"></el-input>
            </el-form-item>

            <el-form-item label="检测报告" prop="netCheckImages">
                <el-upload
                        class="avatar-uploader"
                        :action="UploadUrl()"
                        list-type="picture"
                        name="fileupload"
                        :on-success="handleAvatarSuccess"
                        :data="paramsData"
                        :file-list="fileLists"
                        :on-remove="handleRemove"
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
    import model from '../../../../model/model'
    import floorModel from "../../../../model/project/floor";
    import buildingModel from '../../../../model/project/building'
    import serviceConfig from '../../../../../server.config.js'
    import {Notification} from 'element-ui'

    export default {
        props: ['editForm', 'mode','arrProductType','arrProject'],
        data() {
            return {
                imageUrl: '',
                file:'',
                fileLists:[],
                imageUrls:[],
                buildings: [],
                floors: [],
                paramsData:{
                    folderName:"compressiveReport",
                    fileName:""
                },
                eidtFormRules: {
                    projectId: [
                        {required: true, message: '请选择项目', trigger: 'blur'},
                    ],
                    buildId: [
                        {required: true, message: '请选择楼栋', trigger: 'blur'},
                    ],
                    floorId: [
                        {required: true, message: '请选择楼层', trigger: 'blur'},
                    ],
                    productTypeId: [
                        {required: true, message: '请选择工程部位', trigger: 'blur'},
                    ],
                    productGrade: [
                        {required: true, message: '请输入强度等级', trigger: 'blur'},
                    ],
                    checkTime: [
                        {required: true, message: '请选择报告日期', trigger: 'blur'},
                    ],
                },
                loading: false,

            }
        },
        watch: {
            editForm: function () {
                this.init()
            }
        },
        mounted() {
            this.init()
        },
        methods: {
            changeProject() {
                buildingModel.findByProjectId(this.editForm.projectId).then(data => {
                    this.buildings = data.entity
                    this.editForm.buildId = ''
                    this.editForm.floorId = ''
                    this.floors = []
                })
            },
            changeBuilding(id) {
                this.editForm.floorId = ''
                this.floors = []
                if (id == undefined || id == '') {
                    return
                }
                let building = this.buildings.find(b => b.id == id)
                floorModel.findByBuildingIdAndProjectId(id, building.projectId).then((data) => {
                    this.floors = data.entity
                    this.editForm.floorId = ''
                })
            },
            UploadUrl:function(){
                return serviceConfig.baseURL+'/fileUpload/uploadFile';
            },
            handleAvatarSuccess(res, file) {
                // console.log(file)
                this.fileLists.push(file);
                this.imageUrls.push(res.entity)
                this.editForm.checkImages = this.imageUrls.join(",");
            },
            handleRemove(file, fileList) {
                console.log(file)
                console.log(fileList)

                this.imageUrls.forEach((val,index)=>{
                  if(val.indexOf(file.name)!==-1){
                    this.imageUrls.splice(index,1)
                  }
                })
                
                this.editForm.checkImages = this.imageUrls.join(",");
            },
            handlePreview(file) {
                // console.log(file);
            },
            init() {
                this.fileLists = [];
                this.imageUrls = [];
                if(this.editForm.checkImages){
                    var imageArry = this.editForm.checkImages.split(","); //定义一数组
                    for (let i=0;i<imageArry.length ;i++ ){
                        let imageInfo = {
                            name: imageArry[i].split("/")[imageArry[i].split("/").length-1],
                            url : this.editForm.netCheckImages+imageArry[i],
                           // id       : 678,
                        };
                        this.fileLists.push(imageInfo)
                        this.imageUrls.push(imageArry[i])
                    }
                }
                if(this.editForm.projectId){
                    buildingModel.findByProjectId(this.editForm.projectId).then(data => {
                        this.buildings = data.entity
                    })

                    if(this.editForm.buildId){
                        floorModel.findByBuildingIdAndProjectId(this.editForm.buildId, this.editForm.projectId).then((data) => {
                            this.floors = data.entity
                        })
                    }

                }
            },
            editFormCancel() {
                this.$emit('model-edit-cancel')
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$store.commit('startLoading')
                        if (this.mode == 'update') {
                            model.update('compressiveReport', this.editForm)
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
                                        message: '更新成功',
                                        type: 'success'

                                    })
                                    this.$store.commit('endLoading')
                                    this.$emit('model-edit-finish')
                                })

                        } else {
                            model.add('compressiveReport', this.editForm)
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

        }

    }
</script>
<style>
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