<template>
    <div>
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="22%">

            <el-form-item label="项目编号" prop="projectNo">
                <el-input  onkeyup="value=value.replace(/[^A-Za-z0-9]/g,'')"  v-model="editForm.projectNo"></el-input>
            </el-form-item>
            <el-form-item label="项目名称" prop="projectName">
                <el-input v-model="editForm.projectName"></el-input>
            </el-form-item>
            <el-form-item label="项目地址" prop="workPlace">
                <el-input v-model="editForm.workPlace"></el-input>
            </el-form-item>
            <el-form-item label="经纬度" prop="longitude">
                <el-input v-model="editForm.longitude"></el-input>
                <a href="http://lbs.amap.com/console/show/picker" target="_blank" style="color: #00CC99;line-height: 30px;">+选择经纬度</a>
            </el-form-item>
            <el-form-item label="建设单位" prop="customer">
                <el-input v-model="editForm.customer"></el-input>
            </el-form-item>
            <el-form-item label="构件生产单位" prop="productionUnit">
                <el-input v-model="editForm.productionUnit"></el-input>
            </el-form-item>

            <el-form-item label="施工单位" prop="undertaking">
                <el-input v-model="editForm.undertaking"></el-input>
            </el-form-item>

            <el-form-item label="工程类型" prop="projectType">
                <el-select v-model="editForm.projectType" clearable placeholder="请选择">
                    <el-option v-for="item in arrProjectType"
                               :key="item.id"
                               :label="item.codeName"
                               :value="item.codeName"
                    ></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="工程规模(层数、面积)" prop="projectScale" @keyup.native="parseValue1()">
                <el-input v-model="editForm.projectScale"></el-input>
            </el-form-item>


            <el-form-item label="装配面积" prop="assemblyArea"  @keyup.native="parseValue()" >
                <el-input v-model="editForm.assemblyArea"></el-input>
            </el-form-item>
            <el-form-item label="装配率" prop="assemblyRate"   >
                <el-input v-model="editForm.assemblyRate" disabled></el-input>
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


            <el-form-item label="开工时间" prop="startDate">
                <el-date-picker
                        v-model="editForm.startDate"
                        type="date"
                        placeholder="选择开始时间"
                        value-format="yyyy-MM-dd">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="竣工时间" prop="endDate">
                <el-date-picker
                        v-model="editForm.endDate"
                        type="date"
                        placeholder="选择结束时间"
                        value-format="yyyy-MM-dd">
                </el-date-picker>
            </el-form-item>

            <el-form-item label="工程进度" prop="projectProgress">
                <el-input v-model="editForm.projectProgress"></el-input>
            </el-form-item>


            <el-form-item label="是否为全装修或装配化装修" prop="isFullDecoration" >
                <el-radio v-model="editForm.isFullDecoration" :label="true" >是</el-radio>
                <el-radio v-model="editForm.isFullDecoration" :label="false">否</el-radio>
            </el-form-item>


            <el-form-item label="是否为政府投资工程" prop="isInvested">
                <el-radio v-model="editForm.isInvested" :label="true" >是</el-radio>
                <el-radio v-model="editForm.isInvested" :label="false">否</el-radio>
            </el-form-item>

            <el-form-item label="是否采用工程总承包(EPC)" prop="isEPC" >
                <el-radio v-model="editForm.isEPC" :label="true" >是</el-radio>
                <el-radio v-model="editForm.isEPC" :label="false">否</el-radio>
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
    import projectModel from '../../../../model/project/project'
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
                        {required: true, message: '请输入项目编号', trigger: 'blur'},
                    ],
                    projectName: [
                        {required: true, message: '请输入项目名称', trigger: 'blur'},
                    ],
                    workPlace: [
                        {required: true, message: '请输入项目地址', trigger: 'blur'},
                    ],
                    longitude: [
                        {required: true, message: '请输入项目经纬度', trigger: 'blur'},
                    ],
                    customer: [
                        {required: true, message: '请输入建设单位', trigger: 'blur'},
                    ],
                    assemblyArea: [
                        {required: true, message: '请输入装配面积', trigger: 'blur'},
                    ],
                    productionUnit: [
                        {required: true, message: '请输入构件生产单位', trigger: 'blur'},
                    ],
                    undertaking: [
                        {required: true, message: '请输入施工单位', trigger: 'blur'},
                    ],
                    projectScale: [
                        {required: true, message: '请输入工程规模(层数、面积)', trigger: 'blur'},
                    ],

                    projectType: [
                        {required: true,message: '请选择工程类型', trigger: 'blur'},
                    ],

                    structuralStyle: [
                        {required: true,message: '请选择结构形式', trigger: 'blur'},
                    ],

                    startDate: [
                        {required: true,message: '请选择项目开始时间', trigger: 'blur'},
                    ],
                    endDate: [
                        {required: true,message: '请输入项目结束时间', trigger: 'blur'},
                    ],
                    projectProgress: [
                         {required: true,message: '请输入工程进度', trigger: 'blur'},
                     ],
                    isFullDecoration: [
                        {required: true,message: '请选择是否为全装修或装配化装修', trigger: 'blur'},
                    ],
                    isInvested: [
                        {required: true,message: '请选择是否为政府投资工程', trigger: 'blur'},
                    ],
                    isEPC: [
                        {required: true,message: '请选择是否采用工程总承包(EPC)', trigger: 'blur'},
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

            parseValue1() {
                let value = this.editForm.projectScale;
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
                this.editForm.projectScale = value;
                if( this.editForm.assemblyArea){
                    this.editForm.assemblyRate = (parseFloat(this.editForm.assemblyArea)/parseFloat(this.editForm.projectScale))*100
                    this.editForm.assemblyRate =  parseFloat(this.editForm.assemblyRate).toFixed(2);
                }
            },
            parseValue() {
                let value = this.editForm.assemblyArea;
                console.log(value)
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
                this.editForm.assemblyArea = value;
                if( this.editForm.projectScale){
                    this.editForm.assemblyRate = (parseFloat(this.editForm.assemblyArea)/parseFloat(this.editForm.projectScale))*100
                    this.editForm.assemblyRate =  parseFloat(this.editForm.assemblyRate).toFixed(2);
                }
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