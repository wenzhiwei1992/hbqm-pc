<template>
    <div>
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="100px"
                 size="small">
            <el-form-item label="所属组织" prop="orgName">
                <el-input disabled v-model="editForm.orgName"></el-input>
            </el-form-item>

            <el-form-item label="可查看组织" prop="deptId">
                <el-select v-model="editForm.deptId" disabled placeholder="请选择" @change="assOrgIdsData" clearable>
                    <el-option v-for="item in depts"
                               :key="item.id"
                               :label="item.deptName"
                               :value="item.id"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="用户等级" prop="level"   >
                <el-select v-model="editForm.level" placeholder="请选择" :disabled="editForm.username==this.$store.state.user.username && this.$store.state.user.level==0" clearable>
                    <el-option v-for="item in options"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="用户名" prop="username" :disabled="mode=='update' " >
                <el-input :disabled="mode=='update' " v-model="editForm.username"></el-input>
            </el-form-item>
            <el-form-item label="真实姓名" prop="realName">
                <el-input v-model="editForm.realName"></el-input>
            </el-form-item>
            <el-form-item label="部门" prop="deptId">
                <el-select v-model="editForm.deptId" placeholder="请选择" @change="assOrgIdsData" clearable>
                    <el-option v-for="item in depts"
                               :key="item.id"
                               :label="item.deptName"
                               :value="item.id"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="手机" prop="mobile">
                <el-input v-model="editForm.mobile"></el-input>
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
    import {Notification} from 'element-ui'
    import orgModel from '../../../../model/system/org'
    import deptModel from '../../../../model/basicdata/dept'

    export default {
        props: ['editForm', 'mode'],
        data() {
            return {
                eidtFormRules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'},
                    ],
                    realName: [
                        {required: true, message: '请输入真实姓名', trigger: 'blur'},
                    ],
                    orgId: [
                        {required: true, message: '请选择所属组织', trigger: 'blur'},
                    ],
                    deptId: [
                        {required: true, message: '请选择用户部门', trigger: 'blur'},
                    ],
                    level: [
                        {required: true, message: '请选择用户等级', trigger: 'blur'},
                    ],
                    mobile: [
                        {required: true, message: '请输入手机号', trigger: 'blur'},
                    ],
                },
                orgs: [],
                depts: [],
                options: [
                    {
                        value: '0',
                        label: '超级管理员'
                    },
                    {
                        value: '1',
                        label: '组织管理员'
                    },
                    {
                        value: '2',
                        label: '普通用户'
                    },],
            }
        },
        mounted() {
            console.log(this.mode)
            orgModel.findOrgByUser().then(data => {
                this.orgs = data.entity;
            }).catch(() => {
            })

            deptModel.listAll().then(data => {
                this.depts = data.entity;
            }).catch(() => {
            })
            this.info()

        },
        methods: {
            info() {
                let level = this.$store.state.user.level
                if (level == 0) {
                    this.options = [{
                        value: '0',
                        label: '超级管理员'
                    },
                        {
                            value: '1',
                            label: '组织管理员'
                        },
                        /*  {
                              value: '2',
                              label: '普通用户'
                          },*/]
                }

                // else if (level == 1) {
                //     this.options = [
                //         {
                //             value: '1',
                //             label: '组织管理员'
                //         },
                //         {
                //             value: '2',
                //             label: '普通用户'
                //         },]
                // } else {
                //     this.options = [
                //         {
                //             value: '2',
                //             label: '普通用户'
                //         },]
                // }
            },
            /**
             * 更改部门自动更改可查看组织
             */
            assOrgIdsData() {
                let deptId = this.editForm.deptId;

            },
            editFormCancel() {
                this.$emit('model-edit-cancel')
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let orgIdsVarChar = '';
                        this.editForm.orgIdsData.forEach(e => {
                            orgIdsVarChar += e + ";";
                        })

                        this.editForm.orgIds = orgIdsVarChar;

                        if (this.mode == 'update') {
                            model.update('user', this.editForm)
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
                            model.add('user', this.editForm)
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
