<template>
    <div>
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="22%">

            <el-form-item label="项目编号" prop="projectNo">
                <el-input  onkeyup="value=value.replace(/[^A-Za-z0-9]/g,'')"  v-model="editForm.projectNo"></el-input>
            </el-form-item>
            <el-form-item label="地块数量" prop="landNum">
                <el-input v-model="editForm.landNum"></el-input>
            </el-form-item>


            <el-form-item label="结构形式" prop="structuralStyle">
                <el-select v-model="editForm.structuralStyle" clearable placeholder="请选择">
                    <el-option v-for="item in arrStructuralStyle"
                               :key="item.id"
                               :label="item.codeName"
                               :value="item.codeName"
                    ></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="配建面积" prop="constructionArea"  @keyup.native="parseValue()" >
                <el-input v-model="editForm.constructionArea"></el-input>
            </el-form-item>

            <el-form-item label="当年拟落实面积" prop="implementedArea"  @keyup.native="parseValue1()" >
                <el-input v-model="editForm.implementedArea"></el-input>
            </el-form-item>
            <el-form-item label="全/精装修面积" prop="decorationArea"  @keyup.native="parseValue2()" >
                <el-input v-model="editForm.decorationArea"></el-input>
            </el-form-item>
            <el-form-item label="EPC项目个数" prop="epcNum">
                <el-input v-model="editForm.epcNum"></el-input>
            </el-form-item>

            <el-form-item label="省" prop="provinceId">
                <el-select  v-model="editForm.provinceId" placeholder="请选择" clearable  @change="changeProvince">
                    <el-option v-for="item in arrProvinces"
                               :key="item.id"
                               :label="item.deptName"
                               :value="item.id"
                    ></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="市" prop="cityId">
                <el-select  v-model="editForm.cityId" placeholder="请选择">
                    <el-option v-for="item in arrCitys"
                               :key="item.id"
                               :label="item.deptName"
                               :value="item.id"
                    ></el-option>
                </el-select>
            </el-form-item>

            <el-form-item  label="审批状态" prop="approvalStatus">
                <el-select  v-model="editForm.approvalStatus" disabled clearable placeholder="审批状态">
                    <el-option v-for="item in arrApprovalStates"
                               :key="item.id"
                               :label="item.codeName"
                               :value="item.codeName"
                    ></el-option>
                </el-select>
            </el-form-item>


        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editFormCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import projectModel from '../../../../model/project/reserveproject'
    import {Notification} from 'element-ui'
    import serviceConfig from '../../../../../server.config.js'
    import codeExplainContent from '../../../../model/basicdata/codeExlpainContent'
    import deptModel from '../../../../model/basicdata/dept'


    export default {
        props:['editForm','mode'],
        data(){
            return{
                eidtFormRules: {
                    projectNo: [
                        {required: true, message: '请输入地块编号', trigger: 'blur'},
                    ],
                    landNum: [
                        {required: true, message: '请输入地块数量', trigger: 'blur'},
                    ],
                    structuralStyle: [
                        {required: true, message: '请输入结构类型', trigger: 'blur'},
                    ],
                    constructionArea: [
                        {required: true, message: '请输入配建面积', trigger: 'blur'},
                    ],
                    implementedArea: [
                        {required: true, message: '请输入当年拟落实面积', trigger: 'blur'},
                    ],
                    decorationArea: [
                        {required: true, message: '请输入全/精装修面积', trigger: 'blur'},
                    ],
                    epcNum: [
                        {required: true, message: 'EPC项目个数', trigger: 'blur'},
                    ],
                    provinceId: [
                        {required: true,message: '请选择省', trigger: 'blur'},
                    ],
                    cityId: [
                        {required: true,message: '请选择市', trigger: 'blur'},
                    ],

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
                arrProvinces: [],
                arrCitys: [],
                arrProjectType: [],
                arrStructuralStyle: [],
                arrApprovalStates:[],
            }
        },
        mounted() {
            this.init();
        },
        methods:{
        parseValue2() {
            let value = this.editForm.decorationArea;
            value = value.replace(/[^(\-)*\d.]/g, ""); // 清除"数字"和"."以外的字符
            value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
            value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
            value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                ".");
            value = value.replace("-", "$#$").replace(/\-/g, "").replace("$#$",
                "-");
            value = value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); // 只能输入3个小数
            this.editForm.decorationArea = value;
        },
            parseValue1() {
                let value = this.editForm.implementedArea;
                value = value.replace(/[^(\-)*\d.]/g, ""); // 清除"数字"和"."以外的字符
                value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
                value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                    ".");
                value = value.replace("-", "$#$").replace(/\-/g, "").replace("$#$",
                    "-");
                value = value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); // 只能输入3个小数
                if(parseFloat(value)>=100000000){
                    value = value.substring(0,8);
                }
                this.editForm.implementedArea = value;
            },
            parseValue() {
                let value = this.editForm.constructionArea;
                value = value.replace(/[^(\-)*\d.]/g, ""); // 清除"数字"和"."以外的字符
                value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
                value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                    ".");
                value = value.replace("-", "$#$").replace(/\-/g, "").replace("$#$",
                    "-");
                value = value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); // 只能输入3个小数
                if(parseFloat(value)>=100000000){
                    value = value.substring(0,8);
                }
                this.editForm.constructionArea = value;
            },
            init(){
                deptModel.findDepartmentByLevel(0).then(data => {
                    this.arrProvinces = data.entity;
                })
                codeExplainContent.getAllByCodeNo("PROJECT_TYPE").then(data => {
                    this.arrProjectType = data.entity;
                })

                codeExplainContent.getAllByCodeNo("STRUCTURAL_STYLE").then(data => {
                    this.arrStructuralStyle = data.entity;
                })
                codeExplainContent.getAllByCodeNo("ORG_APPROVAL_STATE").then(data => {
                    this.arrApprovalStates = data.entity;
                })

                this.getCity();
            },
            getCity(){
                let id = this.editForm.provinceId;
                this.arrCitys = []
                let query = {
                    id:id,
                    orgId:0
                }
                deptModel.findDepartmentsByParentId(query).then(data => {
                    this.arrCitys = data.entity;
                })
            },
            changeProvince(){
                let id = this.editForm.provinceId;
               // this.editForm.cityId = ''
                this.$set(this.editForm,'cityId','')
                this.arrCitys = []
                let query = {
                    id:id,
                    orgId:0
                }
                deptModel.findDepartmentsByParentId(query).then(data => {
                    this.arrCitys = data.entity;
                })
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