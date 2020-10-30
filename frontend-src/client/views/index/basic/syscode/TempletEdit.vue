<template>
  <div>
    <el-form
      :model="editForm"
      v-if="editForm"
      :rules="editForm"
      ref="editForm"
      label-width="80px"
    >
      <el-form-item
        label="模版编号"
        prop="templetNo"
        label-width="110px"
        :rules="{
                required: true, message: '不能为空', trigger: 'blur'
              }"
      >
        <el-input v-model="editForm.templetNo"></el-input>
      </el-form-item>
      <el-form-item
        label="模版名称"
        prop="templetName"
        label-width="110px"
        :rules="{
                required: true, message: '不能为空', trigger: 'blur'
              }"
      >
        <el-input v-model="editForm.templetName"></el-input>
      </el-form-item>
      <el-form-item
        label-width="110px"
        style="display:inline-block;"
        v-for="(item, index) in editForm.templetJson"
        :prop="'templetJson['+index+'].name'"
        :label="'名称'+(index+1)"
        :key="index"
        :rules="{
                required: true, message: '不能为空', trigger: 'blur'
              }"
      >
        <el-input
          style="width:180px;"
          v-model="item.name"
        ></el-input>
        <el-input
          placeholder="允许偏差值"
          style="width:180px;"
          v-model="item.deviation"
        ></el-input>
        <el-select
          placeholder="是否为必检项"
          style="width:180px;"
          v-model="item.required"
        >
          <el-option label="是" value="true">是</el-option>
          <el-option label="否" value="false">否</el-option>
        </el-select>
        <el-button
          style="position:absolute;top:1px;"
          type="danger"
          icon="el-icon-delete"
          @click.prevent="removeTempletJson(index)"
        ></el-button>
        <el-button
          v-show="index+1==editForm.templetJson.length"
          style="position:absolute;top:1px;right:-68px;"
          icon="el-icon-plus"
          @click="addTempletJson"
        ></el-button>
      </el-form-item>
      <el-form-item
        label="备注"
        prop="remark"
        label-width="110px"
      >
        <el-input v-model="editForm.remark"></el-input>
      </el-form-item>
    </el-form>
    <div
      slot="footer"
      class="dialog-footer"
    >
      <el-button @click="editFormCancel">取 消</el-button>
      <el-button
        type="primary"
        @click="editFormSubmmit('editForm')"
      >确 定</el-button>
    </div>
  </div>
</template>

<script>
import templetModel from '../../../../model/basicdata/templet'
import { Notification } from 'element-ui'

export default {
  props: ['editFormData', 'mode'],
  data() {
    return {
      editForm: {
        templetNo: '',
        templetName: ''
      }
    }
  },
  created() {
    this.editForm = this.editFormData
    if (!this.editForm.templetJson || !this.editForm.templetJson.length) {
      this.editForm.templetJson = [{ name: '', deviation:'', result: '合格', required:false }]
    }
  },
  methods: {
    removeTempletJson(index) {
      this.$confirm('是否确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.editForm.templetJson.splice(index, 1)
      }).catch(() => { })
    },
    addTempletJson() {
      this.editForm.templetJson.push({
        name: '',
        deviation:'',
        result: '合格',
        required:false
      })
    },
    editFormCancel() {
      this.$emit('templet-cancel')
    },
    editFormSubmmit(formName) {
      this.$refs[formName].validate((valid) => {
        console.log(valid)
        if (valid) {
          this.$confirm('是否确定提交?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let editForm = this.editForm
            editForm = JSON.parse(JSON.stringify(editForm))
            editForm.templetJson = JSON.stringify(editForm.templetJson)
            if (this.mode == 'update') {
              const loading = this.$loading({
                lock: true,
                text: 'Loading',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.7)'
              });
              templetModel.templetUpdate(editForm)
                .then((data) => {
                  if (data.status == 'fail') {
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
                  loading.close();
                  this.$emit('templet-finish')
                })
            } else if (this.mode == 'add') {
              const loading = this.$loading({
                lock: true,
                text: 'Loading',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.7)'
              });
              templetModel.templetAdd(editForm)
                .then((data) => {
                  if (data.status == 'fail') {
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
                  this.$emit('templet-finish')
                })
            }
          }).catch(() => { })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
  },
}
</script>

<style scoped>
</style>