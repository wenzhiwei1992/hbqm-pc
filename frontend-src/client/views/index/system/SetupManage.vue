<template>
    <div>
        <el-form  :model="editForm" :rules="eidtFormRules" ref="editForm" label-width="150px" size="small">
            <el-form-item label="默认菜单" prop="defaultActive">
                <el-select v-model="editForm.defaultActive" placeholder="请选择">
                    <el-option value=""></el-option>
                    <el-option
                            v-for="item in menuList"
                            :key="item.index"
                            :label="item.title"
                            :value="item.index">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="开启多组织" prop="enabledMultiOrg">
                <el-select v-model="editForm.enabledMultiOrg" placeholder="请选择">
                    <el-option :value="false" label="false"></el-option>
                    <el-option :value="true" label="true"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="企业ID" prop="corpid" style="width:420px">
                <el-input type="text" v-model="editForm.corpid" placeholder="登录企业微信获取企业ID"></el-input>
            </el-form-item>
            <el-form-item label="通讯秘钥" prop="corpsecret" style="width:420px">
                <el-input type="text" v-model="editForm.corpsecret"  placeholder="登录企业微信获取通讯秘钥"></el-input>
            </el-form-item>
            <el-form-item label="部门ID" prop="deptId" style="width:420px">
                <el-input type="text" v-model="editForm.deptId"  placeholder="从企业微信查看部门ID获取"></el-input>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
            </el-form-item>
        </el-form>

    </div>



</template>
<script>
    import model from '../../../model/model'
    import clientModel from '../../../model/client-model'
    import {Notification} from 'element-ui'

    export default {
        data() {
            return {
                editForm:{
                    defaultActive:'setupManage',
                    enabledMultiOrg:'false',
                    deptId:0
                },
                menuList:[],
                mode:null,
                eidtFormRules: {
                },
            }
        },
        mounted(){
            clientModel.ax.get('/setup').then((data)=>{
                if(data.entity){
                    this.editForm=data.entity
                    this.mode='update'
                }else{
                    this.mode='add'
                }
            })
            this.$store.state.menu.menu.submenus.forEach((menu)=>{
                menu.menuItems.forEach((item)=>{
                    this.menuList.push(item)
                })
            })
        },
        methods:{
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$store.commit('startLoading')
                        if (this.mode == 'update') {
                            model.update('setup',this.editForm)
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
                                        message: '编辑成功',
                                        type: 'success'

                                    })
                                    this.$store.dispatch('setupRefresh')
                                    this.$store.commit('endLoading')
                                    this.$emit('model-edit-finish')
                                })
                        } else {
                            model.add('setup',this.editForm)
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
                                        message: '保存成功',
                                        type: 'success'

                                    })
                                    this.editForm=data.entity
                                    this.mode='update'
                                    this.$store.dispatch('setupRefresh')
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
</style>