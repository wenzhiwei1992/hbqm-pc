<template>
  <div>
    <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="220px" size="small">
      <el-form-item label="基地名称" prop="orgName">
        <el-input v-model="editForm.orgName"></el-input>
      </el-form-item>

      <el-form-item label="基地地址" prop="address">
        <el-input v-model="editForm.address"></el-input>
      </el-form-item>
      <el-form-item label="经纬度" prop="longitude">
        <el-input v-model="editForm.longitude"></el-input>
        <a href="http://lbs.amap.com/console/show/picker" target="_blank" style="color: #00CC99;line-height: 30px;">+选择经纬度</a>
      </el-form-item>

      <el-form-item label="申请人" prop="proposer">
        <el-input disabled v-model="editForm.proposer"></el-input>
      </el-form-item>
      <el-form-item label="申请人手机号" prop="proposerPhone">
        <el-input disabled v-model="editForm.proposerPhone"></el-input>
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

      <el-form-item label="生产类型" prop="productionType">
        <el-select  v-model="editForm.productionType" placeholder="请选择">
          <el-option v-for="item in arrProductions"
                     :key="item.id"
                     :label="item.codeName"
                     :value="item.codeName"
          ></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="生产线/条" prop="lineNum">
        <el-input-number  v-model="editForm.lineNum"></el-input-number>
      </el-form-item>

      <el-form-item label="设计生产能力(万m/万吨)" prop="designProductivity">
        <el-input v-model="editForm.designProductivity"   @keyup.native="parseValue1()"  placeholder="请输入内容"></el-input>
      </el-form-item>


      <el-form-item
              prop="realProductivity"
              label="实际生产能力(万m/万吨)"
              width="120">
         <el-input  v-model="editForm.realProductivity"   @keyup.native="parseValue()" placeholder="请输入内容" ></el-input>
      </el-form-item>



      <el-form-item label="状态" prop="state">
        <el-select  v-model="editForm.state" placeholder="请选择">
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
            v-model="editForm.orgDesc">
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
    <div style="text-align:right" class="dialog-footer">
      <el-button @click="editFormCancel">取 消</el-button>
      <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
    </div>
  </div>


</template>
<script>
import model from '../../../../model/model'
import orgModel from '../../../../model/system/org'
import codeExplainContent from '../../../../model/basicdata/codeExlpainContent'
import {Notification} from 'element-ui'
import deptModel from '../../../../model/basicdata/dept'
import serviceConfig from '../../../../../server.config.js'

export default {
  props: ['editForm', 'mode'],
  data() {
    return {
      orgs: [],
        arrProvinces: [],
        arrCitys: [],
        arrProductions: [],
        arrApprovalStates:[],
        arrStates: [],
      eidtFormRules: {
        orgCode: [
          {required: true, message: '请输入组织编码', trigger: 'blur'},
        ],
        orgName: [
          {required: true, message: '请输入组织名称', trigger: 'blur'},
        ],
        rootOrg: [
          {required: true, message: '请设置根组织', trigger: 'blur'},
        ],
        isWithinOrg: [
          {required: true, message: '请设置是否属于集团内', trigger: 'blur'},
        ],

      },
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
          orgModel.orgAll().then(data => {
              this.orgs = data.entity;
          })
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
          if (this.mode == 'update') {
            model.update('org', this.editForm)
                .then((data) => {
                  if (data.status == 'fail') {
                    Notification({
                      message: data.msg,
                      type: 'warning'
                    })
                    return
                  }
                  Notification({
                    message: '编辑成功',
                    type: 'success'

                  })
                  this.$emit('model-edit-finish')
                })
          } else {
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
                    message: '新建成功',
                    type: 'success'

                  })
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

</style>
