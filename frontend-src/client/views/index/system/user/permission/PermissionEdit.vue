<template>
  <div>
        <el-form  :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px" size="small">
            <el-form-item label="名称" prop="alias">
                <el-input v-model="editForm.alias" placeholder="e.g. 组织管理"/>
            </el-form-item>
            <el-form-item label="API" prop="name">
                <el-input v-model="editForm.name"  placeholder="e.g. ORG_SELECT"/>
            </el-form-item>
            <el-form-item label="父级API" prop="pid" >
                <el-select v-model="editForm.pid" clearable placeholder="默认为顶级">
                    <el-option v-for="item in pids"
                               :key="item.id"
                               :label="item.alias"
                               :value="item.id"
                    ></el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <div style="text-align:right" class="dialog-footer">
            <el-button @click="cancel()">取 消</el-button>
            <el-button type="primary" @click="submit('editForm')">确 定</el-button>
        </div>
    </div>
</template>

<script>
import permission from '../../../../../model/system/permission'
import {Notification} from 'element-ui'
export default {
    props:['permissionObj'],
    data(){
        return{
            pids:[],
            editForm:this.permissionObj,
            eidtFormRules:{
                alias: [
                    {required: true, message: '必填项', trigger: 'blur'},
                ],
                name: [
                    {required: true, message: '必填项', trigger: 'blur'},
                ],
            }
        }
    },
    mounted(){
        this.loadPids();
    },
    methods:{
        cancel(){
            this.$emit('close-pm-edit')
        },
        /**
         * 父级API集合
         */
        loadPids(){
            permission.loadPids().then(data=>{
                this.pids = data.entity;
            })
        },
        submit(formName){
            this.$refs[formName].validate((valid) => {
                if(valid){
                    permission.add(this.editForm).then(data=>{
                        if(data.status == 'success'){
                            Notification({
                                type:'success',
                                message:'新增成功'
                            })
                        }else{
                            Notification({
                                message: data.msg,
                                type: 'error'
                            })
                        }
                    })
                    this.$forceUpdate()
                    this.$emit('close-pm-edit')
                }else{
                    return false;
                }
            })
        }
    }
}
</script>

<style>

</style>