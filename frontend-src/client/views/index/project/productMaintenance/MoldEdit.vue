<template>
   <div>
    <el-form :model="innerEditForm" v-if="innerEditForm" :rules="innereidtFormRules" ref="innerEditForm" label-width="80px">
        <el-form-item label="模具名称" prop="moldName">
            <el-input v-model="innerEditForm.moldName"  size="medium"></el-input>
        </el-form-item>
        <el-form-item label="项目名称" prop="projectName">
            <el-select v-model="innerEditForm.project.id" placeholder="请选择">
                <el-option
                        v-for="item in arrProject"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="产线" prop="line">
            <el-select v-model="innerEditForm.lineId" placeholder="请选择">
                <el-option
                        v-for="item in arrLine"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="套数" prop="moldNum">
            <el-input v-model="innerEditForm.moldNum"  size="medium"></el-input>
        </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
        <el-button @click="editFormCancel">取 消</el-button>
        <el-button type="primary" @click="innerFormSubmit('innerEditForm')">确 定</el-button>
    </div>

</div>

</template>

<script>
    import  moldModel from  '../../../../model/project/mold'
    import {Notification} from 'element-ui'
    export default {
        props:['innerEditForm','arrProject','arrLine'],
        mounted(){
        },
        methods:{
            editFormCancel(){
                this.$emit('inner-edit-cancel')
            },
            innerFormSubmit(formName){
                this.$refs[formName].validate((valid) => {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    const inner={
                        moldName:this.innerEditForm.moldName,
                        project:{
                            id:this.innerEditForm.project.id
                        },
                        lineId:this.innerEditForm.lineId,
                        moldNum:this.innerEditForm.moldNum
                    }
                    moldModel.moldAdd(inner).then((data)=>{
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
                        loading.close()
                        this.$emit('inner-edit-finish')

                    })
                })
            },



        },
        data(){
            const checkProjectName = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('项目名称不能为空'));
                }else{
                    callback();
                }
            };

            return{
                innereidtFormRules:{
                    moldName: [
                        {required: true, message: '请输入模具名称', trigger: 'change'},
                    ],
  /*                  projectName: [
                        {validator: checkProjectName,trigger:'change'},
                    ],
                    line: [
                        {required: true, message: '请选择产线', trigger: 'change'},
                    ],*/
                    moldNum: [
                        {required: true, message: '请选择套数', trigger: 'change'},
                    ],
                },
            }
        },
    }





</script>

<style scoped>

</style>