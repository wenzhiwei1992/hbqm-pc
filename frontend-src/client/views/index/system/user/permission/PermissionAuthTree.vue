<template>
    <div id="permission-auth-tree">
        <el-form :inline="true" id="opt">
            <el-tree
                    :data="data"
                    ref="tree"
                    :props="defaultProps"
                    show-checkbox
                    node-key="id"

            >
            </el-tree>
        
        </el-form>
        <div slot="footer" class="dialog-footer" style="margin-top:10px">
            <el-button @click="cancel()">取 消</el-button>
            <el-button type="primary" @click="save()" :disabled="permissions == null">确 定</el-button>
        </div>
    </div>

</template>
<script>
    import permissionModel from '../../../../../model/system/permission'
    import {Notification} from 'element-ui'

    export default {
        props:['permissions','group','permissionIds'],
        mounted(){
            if(this.permissionIds){
                this.$refs.tree.setCheckedKeys(this.permissionIds);
            }
        },
        data() {
            return {
                defaultProps:{
                    children: 'child',
                    label: 'alias',
                },
            }
        },
        computed:{
            data(){
                return this.permissions
            },
            
        },
        methods:{
            cancel(){
                this.$emit("close-pm-auth")
            },
            save(){
                //先根据用户组删除所有的权限,再添加前台选择的权限
                let groupPermission ={};
                groupPermission.groupId = this.group.id
                groupPermission.permissionIds=this.$refs.tree.getCheckedKeys()
                this.$store.commit('startLoading')
                permissionModel.update(groupPermission).then((data) => {
                    this.$store.commit('endLoading')
                    if (data.status == 'failed') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    }
                    Notification({
                        message: '保存成功',
                        type: 'success'
                    })
                    this.$emit('close-pm-auth')
                })
                    
            },

        },
    }
</script>
<style>
    #permission-auth-tree  #opt .el-form-item{
        margin-bottom: 5px;
    }
    #permission-auth-tree .auth-title{
        height:36px;
        line-height: 36px;
        text-align: center;
        font-size:14px;
        background-color:#f5f7fa;
        border-bottom: 1px solid #ebeef5;
        border-top: 1px solid #ebeef5;
    }
</style>