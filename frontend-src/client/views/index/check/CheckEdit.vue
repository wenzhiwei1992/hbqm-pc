<template>
    <div id="check-edit">
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="200px"
                 size="small">
            <el-form-item label="构件编号">

                <el-input v-if=" productDetail.active=='productDetailCheckListReturn'"
                          :value="productDetail.productNo" disabled></el-input>

                <el-input v-else :value="productDetail.product.productNo" disabled></el-input>

            </el-form-item>
            <el-form-item label="构件名称">
                <el-input v-if=" productDetail.active=='productDetailCheckListReturn'"
                          :value="productDetail.productName" disabled></el-input>
                <el-input v-else :value="productDetail.product.productName" disabled></el-input>
            </el-form-item>
        </el-form>
        <el-form class="switch-wrap" v-if="editForm" label-width="200px" size="small">
            <el-form-item :class="item.required?'switch-box':''" :label="item.name+' :'" v-for="item in items" :key="item.name">
                <el-switch
                        :width="50"
                        v-model="item.result"
                        active-value="不合格"
                        active-text="不合格"
                        active-color="#ff4949"
                        inactive-value="合格"
                        inactive-text="合格"
                        inactive-color="#13ce66"
                        @change="itemResultChange"
                >
                </el-switch>
            </el-form-item>
        </el-form>
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="200px"
                 size="small">
            <el-form-item label="质量等级" v-if="massLevelVisible" label-width="100px">
                <el-radio v-model="editForm.massLevel" label="A">A</el-radio>
                <el-radio v-model="editForm.massLevel" label="B">B</el-radio>
                <el-radio v-model="editForm.massLevel" label="C">C</el-radio>
                <el-radio v-model="editForm.massLevel" label="D">D</el-radio>
            </el-form-item>

            <el-form-item label="检验说明">
                <el-input type="textarea" v-model="editForm.checkDesc"></el-input>
            </el-form-item>
            <el-form-item label="检验结果">
                <el-select v-model="editForm.checkResult">
                    <el-option
                            v-for="item in resultList"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
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
    import model from '../../../model/model'
    import clientModel from '../../../model/client-model'
    import templetModel from '../../../model/basicdata/templet'
    import serviceConfig from '../../../../server.config.js'

    import {Notification} from 'element-ui'

    export default {
        props: ['productDetail'],
        data() {
            return {
                massLevelVisible: false,
                editForm: null,
                eidtFormRules: {},
                items: [],
                resultList: [],
                fileList:[],
                formDate:'',
            }
        },
        mounted() {
            this.init()
        },
        computed: {
            checkType() {
                let active = this.$store.state.menuActiveName
                let checkType = ''
                if (active == 'productDetailHiddenCheckList' || active == 'productDetailHiddenCheckListReturn') {
                    checkType = '隐检'
                } else if (active == 'productDetailCheckList' || active == 'productDetailCheckListReturn') {
                    checkType = '成品检'
                } else if (active == 'productDetailJZQRCheckList') {
                    checkType = '浇筑确认';
                }
                return checkType
            },
            reworkFlag() {
                let active = this.$store.state.menuActiveName
                let reworkFlag = null
                if ( active == 'productDetailCheckListReturn') {
                    reworkFlag = true;
                }
                return reworkFlag
            },
        },
        methods: {
            uploadSectionFile(param){
                this.formDate.append('fileupload', param.file);
            },

            submitUpload() {
                this.formDate = new FormData()
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


                var fd = this.formDate;

                fd.append('checkType', this.editForm.checkType);
                if (this.editForm.reworkFlag ) {
                    fd.append('reworkFlag', this.editForm.reworkFlag);
                }
                fd.append('productDetailId', this.editForm.productDetailId);
                fd.append('checkDesc', this.editForm.checkDesc);
                fd.append('checkResult', this.editForm.checkResult);
                fd.append('itemResultJson', this.editForm.itemResultJson);
                if (this.editForm.checkType == '成品检') {
                    fd.append('massLevel', this.editForm.massLevel);
                }

                clientModel.ax.post('/check/add', fd).then((data) => {

                    if(data.status == 'fail'){
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    } else {
                        Notification({
                            message: '构件已完成' + this.checkType,
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
            init() {
                this.editForm = {
                    reworkFlag:this.reworkFlag,
                    checkType: this.checkType,
                    productDetailId: this.productDetail.id,
                    checkDesc: '',
                    checkResult: '合格',
                    massLevel: 'A'
                }
                if (this.checkType == '成品检') {
                    this.massLevelVisible = true;
                }
                if (this.checkType == '成品检') {
                    this.resultList = [{label: '合格品', value: '合格'}]
                    const loading = this.$loading({
                        lock: true,
                        text: "Loading",
                        spinner: "el-icon-loading",
                        background: "rgba(0, 0, 0, 0.7)"
                    });
                    let no = 'CPJ'
                    templetModel.findTempletByTempletNo(no).then((data)=>{
                        if (data.status == "failed") {
                            Notification({
                                message: data.msg,
                                type: "warning"
                            });
                            loading.close();
                            return;
                        }
                        this.items =data.entity
                        loading.close();
                    })
                } else if (this.checkType == '隐检') {
                    this.resultList = [{label: '合格', value: '合格'}]
                    const loading = this.$loading({
                        lock: true,
                        text: "Loading",
                        spinner: "el-icon-loading",
                        background: "rgba(0, 0, 0, 0.7)"
                    });
                    let no = 'YJ'
                    templetModel.findTempletByTempletNo(no).then((data)=>{
                        if (data.status == "failed") {
                            Notification({
                                message: data.msg,
                                type: "warning"
                            });
                            loading.close();
                            return;
                        }
                        this.items =data.entity
                        loading.close();
                    })
                } else if (this.checkType == '浇筑确认') {
                    this.resultList = [{label: '合格', value: '合格'}]
                    const loading = this.$loading({
                        lock: true,
                        text: "Loading",
                        spinner: "el-icon-loading",
                        background: "rgba(0, 0, 0, 0.7)"
                    });
                    let no = 'JZQR'
                    templetModel.findTempletByTempletNo(no).then((data)=>{
                        if (data.status == "failed") {
                            Notification({
                                message: data.msg,
                                type: "warning"
                            });
                            loading.close();
                            return;
                        }
                        this.items =data.entity
                        loading.close();
                    })
                }
            },
            editFormCancel() {
                this.$emit('model-edit-cancel')
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.submitUpload();

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            itemResultChange() {
                if (this.items.some((e) => e.result === '不合格')) {
                    if (this.checkType == '隐检') {
                        this.resultList = [{label: '隐检返修', value: '隐检返修'}]
                        this.editForm.checkResult = '隐检返修'
                    } else if (this.checkType == '成品检') {
                        this.resultList = [{label: '不良品', value: '返修'}, {label: '报废品', value: '待报废审批'}]
                        this.editForm.checkResult = '返修'
                    }
                } else {
                    if (this.checkType == '隐检') {
                        this.resultList = [{label: '合格', value: '合格'}]
                        this.editForm.checkResult = '合格'
                    } else if (this.checkType == '成品检') {
                        this.resultList = [{label: '合格品', value: '合格'}]
                        this.editForm.checkResult = '合格'
                    }

                }
            }

        },
        watch: {
            productDetail: function () {
                this.init()
            }
        }

    }
</script>
<style scoped lang="scss">
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
    /deep/.switch-box label{
      color:red;
    }
</style>