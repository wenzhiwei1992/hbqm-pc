<template>
  <div class="register">
    <div class="title">注册新用户</div>
    <div class="content">
      <el-form width="600px" :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="220px" size="small">
        <el-form-item label="基地名称" prop="orgName">
          <el-input v-model="editForm.orgName" style="width:500px;"></el-input>
        </el-form-item>

        <el-form-item label="基地地址" prop="address">
          <el-input v-model="editForm.address" style="width:500px;"></el-input>
        </el-form-item>
        <el-form-item label="基地经纬度" prop="longitude">
          <el-input v-model="editForm.longitude" style="width:500px;"></el-input>
          <a href="http://lbs.amap.com/console/show/picker" target="_blank" style="color: #00CC99;line-height: 30px;">+选择经纬度</a>
        </el-form-item>

        <el-form-item label="申请人" prop="proposer">
          <el-input  v-model="editForm.proposer" style="width:500px;"></el-input>
        </el-form-item>
        <el-form-item label="申请人手机号" prop="proposerPhone">
          <el-input  v-model="editForm.proposerPhone" style="width:500px;"></el-input>
        </el-form-item>


        <el-form-item label="省" prop="provinceId">
          <el-select  v-model="editForm.provinceId" style="width:200px;" placeholder="请选择" clearable  @change="changeProvince">
            <el-option v-for="item in arrProvinces"
                      :key="item.id"
                      :label="item.deptName"
                      :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="市" prop="cityId">
          <el-select  v-model="editForm.cityId" style="width:200px;" placeholder="请选择">
            <el-option v-for="item in arrCitys"
                      :key="item.id"
                      :label="item.deptName"
                      :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="生产类型" prop="productionType">
          <el-select  v-model="editForm.productionType" style="width:200px;" placeholder="请选择">
            <el-option v-for="item in arrProductions"
                      :key="item.id"
                      :label="item.codeName"
                      :value="item.codeName"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="生产线/条" prop="lineNum">
          <el-input-number  v-model="editForm.lineNum" style="width:200px;"></el-input-number>
        </el-form-item>

        <el-form-item label="设计生产能力（万m/万吨）" prop="designProductivity">
          <el-input v-model="editForm.designProductivity" style="width:500px;" @keyup.native="parseValue1()"  placeholder="请输入内容"></el-input>
        </el-form-item>
        <el-form-item
                prop="realProductivity"
                label="实际生产能力（万m/万吨）"
                width="120">
          <el-input  v-model="editForm.realProductivity" style="width:500px;" @keyup.native="parseValue()" placeholder="请输入内容" ></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="state">
          <el-select  v-model="editForm.state" style="width:200px;" placeholder="请选择">
            <el-option v-for="item in arrStates"
                      :key="item.id"
                      :label="item.codeName"
                      :value="item.codeName"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="情况说明" prop="orgDesc">
          <el-input
              type="textarea"
              :rows="3"
              v-model="editForm.orgDesc"
              style="width:500px;" 
              >
          </el-input>
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


    </div>
    <div class="footer">
      <el-button class="btn" type="primary" size="medium" @click="editFormSubmmit('editForm')">立即注册</el-button>
    </div>
  </div>


</template>
<script>
import model from '@/model/model'
import orgModel from '@/model/system/org'
import codeExplainContent from '@/model/basicdata/codeExlpainContent'
import {Notification} from 'element-ui'
import deptModel from '@/model/basicdata/dept'
import {validatePhone} from '@/util/validate.js'

export default {
  // props: ['editForm', 'mode'],
  data() {
    return {
      orgs: [],
        arrProvinces: [],
        arrCitys: [],
        arrProductions: [],
        arrStates: [],
        arrApprovalStates:[],
      eidtFormRules: {
        orgName: [
          {required: true, message: '请输入组织名称', trigger: 'blur'},
            { min: 1, max: 10, message: '长度请控制在10个字符以内', trigger: 'blur' }
        ],
        address: [
          {required: true, message: '请输入组织地址', trigger: 'blur'},
            { min: 1, max: 50, message: '长度请控制在50个字符以内', trigger: 'blur' }
        ],
        longitude: [
          {required: true, message: '请输入经纬度', trigger: 'blur'},
            { min: 1, max: 50, message: '长度请控制在50个字符以内', trigger: 'blur' }
        ],
        proposer: [
          {required: true, message: '请输入申请人', trigger: 'blur'},
            { min: 1, max: 10, message: '长度请控制在10个字符以内', trigger: 'blur' }
        ],
        proposerPhone: [
          {required: true, message: '请输入申请人手机号', trigger: 'blur'},
            { validator: validatePhone, trigger: 'blur' }
        ],
        provinceId: [
          {required: true, message: '请输入省', trigger: 'blur'},
        ],
        cityId: [
          {required: true, message: '请输入市', trigger: 'blur'},
        ],
        productionType: [
          {required: true, message: '请输入生产类型', trigger: 'blur'},
        ],
        lineNum: [
          {required: true, message: '请输入生产线/条', trigger: 'blur'},
            { min: 1, max: 10, message: '长度请控制在10个字符以内', trigger: 'blur' }
        ],
        designProductivity: [
          {required: true, message: '请输入设计生产能力(万m/万吨)', trigger: 'blur'},
        ],
        realProductivity: [
          {required: true, message: '请输入实际生产能力(万m/万吨)', trigger: 'blur'},
        ],
        state: [
          {required: true, message: '请输入状态', trigger: 'blur'},
        ],
        orgDesc: [
          {required: true, message: '请输入组织描述', trigger: 'blur'},
            { min: 1, max: 100, message: '长度请控制在100个字符以内', trigger: 'blur' }
        ]
      },
      editForm:{
        orgName:'',
        address:'',
        longitude:'',
        proposer:'',
        proposerPhone:'',
        provinceId:'',
        cityId:'',
        productionType:'',
        lineNum:'',
        designProductivity:'',
        realProductivity:'',
        state:'',
        orgDesc:'',
      }
    }
  },
    watch:{
        editForm:function(){
            this.getCity()
        }
    },
  mounted() {
   this.init();
  },
  methods: {
      init(){
          deptModel.findDepartmentByLevel(0).then(data => {
              this.arrProvinces = data.entity;
          })
          codeExplainContent.getAllByCodeNo("ORG_PRODUCTION_TYPE").then(data => {
              this.arrProductions = data.entity;
          })

          codeExplainContent.getAllByCodeNo("ORG_STATE").then(data => {
              this.arrStates = data.entity;
          })
          codeExplainContent.getAllByCodeNo("ORG_APPROVAL_STATE").then(data => {
              this.arrApprovalStates = data.entity;
              let approvalStatus =  "市级审批中"
              if(this.arrApprovalStates.length>0){
                  approvalStatus = this.arrApprovalStates[0].codeName;
              }
              this.editForm.approvalStatus = approvalStatus;
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
      parseValue1() {
          let value = this.editForm.designProductivity;
          value = value.replace(/[^(\-)*\d.]/g, ""); // 清除"数字"和"."以外的字符
          value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
          value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
          value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
              ".");
          value = value.replace("-", "$#$").replace(/\-/g, "").replace("$#$",
              "-");
          value = value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); // 只能输入3个小数
          this.editForm.designProductivity = value;
      },
      parseValue() {
          let value = this.editForm.realProductivity;
          value = value.replace(/[^(\-)*\d.]/g, ""); // 清除"数字"和"."以外的字符
          value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
          value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
          value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
              ".");
          value = value.replace("-", "$#$").replace(/\-/g, "").replace("$#$",
              "-");
          value = value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); // 只能输入3个小数
          this.editForm.realProductivity = value;
      },

    editFormCancel() {
      this.$emit('model-edit-cancel')
    },
    editFormSubmmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
            model.add('org', this.editForm)
                .then((data) => {
                  if (data.status == 'fail') {
                    Notification({
                      message: data.msg,
                      type: 'warning'
                    })
                    return
                  }
                  Notification({
                    message: '提交审批成功,请等候通知',
                    type: 'success'

                  })
                  this.$emit('model-edit-finish')
                })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    }

  }

}
</script>
<style lang="scss">
  .register{
    background:#fff;
    overflow: hidden;
    height:100%;
    .title{
      position:fixed;
      top:0;
      left:0;
      width:100%;
      background:#d53d44;
      height:30px;
      color:#fff;
      line-height:30px;
      text-indent:2em;
      z-index:10;
    }
    .content{
      width:80%;
      margin:50px auto 20px;
    }
    .footer{
      display:flex;
      justify-content: center;
      margin-bottom:20px;
    }
  }
</style>
<style>
  body{
    height:100%
  }
</style>