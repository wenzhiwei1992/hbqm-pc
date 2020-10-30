<template>
  <div>
    <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px" size="small">
      <el-form-item label="角色名" prop="groupName">
        <el-input v-model="editForm.groupName"></el-input>
      </el-form-item>
      <el-form-item label="角色描述" prop="groupDesc">
        <el-input
                type="textarea"
                :rows="2"
                v-model="editForm.groupDesc">
        </el-input>
      </el-form-item>
      <el-form-item label="是否可用" prop="active">
        <el-radio-group v-model="editForm.active">
          <el-radio label="是">是</el-radio>
          <el-radio label="否">否</el-radio>
        </el-radio-group>
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
    import {Notification} from 'element-ui'

    export default {
        props: ['editForm', 'mode'],
        data() {
            return {
                eidtFormRules: {
                    groupName: [
                        {required: true, message: '请输入角色名', trigger: 'blur'},
                        { min: 1, max: 10, message: '长度请控制在10个字符以内', trigger: 'blur' }
                    ],
                    groupDesc: [
                        { min: 0, max: 100, message: '长度请控制在100个字符以内', trigger: 'blur' }
                    ],
                },
                orgs: []
            }
        },
        mounted() {
            orgModel.findOrgByUser().then(data => {
                this.orgs = data.entity;
            }).catch(() => {
            })
        },
        methods: {
            editFormCancel() {
                this.$emit('model-edit-cancel')
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.mode == 'update') {
                            model.update('group', this.editForm)
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
                            model.add('group', this.editForm)
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
