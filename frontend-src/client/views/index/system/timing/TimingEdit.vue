<template>
  <div>
    <el-form ref="editForm" :inline="true" :model="editForm" :rules="editFormRules" size="small" width="730px"
             label-width="80px">
      <el-form-item label="任务名称" prop="jobName">
        <el-input v-model="editForm.jobName" style="width: 220px;"/>
      </el-form-item>
      <el-form-item label="任务描述" prop="remarks">
        <el-input v-model="editForm.remarks" style="width: 220px;"/>
      </el-form-item>
      <el-form-item label="Bean名称" prop="beanName">
        <el-input v-model="editForm.beanName" style="width: 220px;"/>
      </el-form-item>
      <el-form-item label="执行方法" prop="methodName">
        <el-input v-model="editForm.methodName" style="width: 220px;"/>
      </el-form-item>
      <el-form-item label="Cron表达式" prop="cron">
        <el-input v-model="editForm.cron" style="width: 220px;"/>
      </el-form-item>
      <el-form-item label="任务状态">
        <el-radio-group v-model="editForm.isPause" style="width: 220px">
          <el-radio :label="true">启用</el-radio>
          <el-radio :label="false">暂停</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="失败后暂停">
        <el-radio-group v-model="editForm.pauseAfterFailure" style="width: 220px">
          <el-radio :label="true">是</el-radio>
          <el-radio :label="false">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="参数内容">
        <el-input v-model="editForm.params" style="width: 556px;" rows="4" type="textarea"/>
      </el-form-item>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="editFormCancel">取消</el-button>
      <el-button type="primary" @click="editFormSubmmit('editForm')">确认</el-button>
    </div>
  </div>
</template>

<script>
import timingModel from "../../../../model/system/timing";
import {Notification} from "element-ui";

export default {
  name: "timingEdit",
  props: ['editForm', 'mode'],
  data() {
    return {
      editFormRules: {
        jobName: [
          {required: true, message: '请输入任务名称', trigger: 'blur'}
        ],
        remarks: [
          {required: true, message: '请输入任务描述', trigger: 'blur'}
        ],
        beanName: [
          {required: true, message: '请输入Bean名称', trigger: 'blur'}
        ],
        methodName: [
          {required: true, message: '请输入方法名称', trigger: 'blur'}
        ],
        cron: [
          {required: true, message: '请输入Cron表达式', trigger: 'blur'}
        ],
        personInCharge: [
          {required: true, message: '请输入负责人名称', trigger: 'blur'}
        ]
      },
    }
  },
  mounted() {
  },
  methods: {
    editFormSubmmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.mode == 'update') {
            const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            });
            console.log('editForm------>', this.editForm)
            timingModel.quartzJobUpdate(this.editForm)
                .then((data) => {
                  if (data.status == 'failed') {
                    Notification({
                      message: data.msg,
                      type: 'warning'
                    })
                    loading.close()
                    return
                  }
                  Notification({
                    message: '编辑成功',
                    type: 'success'
                  })
                  loading.close()
                  this.$emit('quartz-job-edit-finish')
                })
          } else {
            console.log('---------->this.editForm', this.mode)
            const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            });
            timingModel.quartzJobAdd(this.editForm)
                .then((data) => {
                  if (data.status == 'failed') {
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
                  loading.close()
                  this.$emit('quartz-job-edit-finish')
                })
          }

        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    editFormCancel() {
      this.$emit('quartz-job-edit-cancel')
    },
  }
}
</script>

<style scoped>

</style>
