<template>
    <div>
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px">
            <el-form-item label="上级部门名称" prop="parentName" v-if="this.mode == 'add' &&  this.editForm.parentId !=null ">
                <el-input v-model="editForm.parentName"
                          :disabled="this.mode == 'add' &&  this.editForm.parentId !=null"></el-input>
            </el-form-item>

     <!--       <el-form-item label="部门类型" prop="id" v-if="this.mode == 'update' && this.editForm.parentId != null  ">
                <el-select v-model="editForm.parentId" placeholder="请选择" style="width:150px" clearable>
                    <el-option
                            v-for="item in arrDepts"
                            :key="item.id"
                            :label="item.deptName"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>-->
            <el-form-item label="部门名称" prop="deptName">
                <el-input v-model="editForm.deptName"></el-input>
            </el-form-item>
            <el-form-item label="部门代码" prop="deptCode">
                <el-input v-model="editForm.deptCode"></el-input>
            </el-form-item>
        <!--    <el-form-item label="部门编号" prop="deptNo">
                <el-input v-model="editForm.deptNo"></el-input>
            </el-form-item>-->
            <el-form-item label="负责人" prop="deptHod">
                <el-input v-model="editForm.deptHod"></el-input>
            </el-form-item>
            <el-form-item label="部门属性" prop="deptProp">
                <el-input v-model="editForm.deptProp"></el-input>
            </el-form-item>
            <el-form-item label="备注" prop="deptMemo">
                <el-input v-model="editForm.deptMemo"></el-input>
            </el-form-item>
        </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="quit">取 消</el-button>
                <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
            </div>
    </div>
</template>

<script>

    import deptModel from '../../../../model/basicdata/dept'
    import {Notification} from 'element-ui'
    export default {
        name: "DeptTreeEdit",
        props: ['editForm', 'mode'],
        data() {
            return {
                arrDepts:'',
                eidtFormRules: {
                    deptName: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ]
                },
            }
        },
        mounted() {
          /*  if(this.mode ==this.GLOBAL.STATUS.UPDATE){
                this.info()
            }*/
        },
        methods: {
            editFormSubmmit(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.mode==this.GLOBAL.STATUS.UPDATE){
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            deptModel.deptListUpdate(this.editForm)
                                .then((data) => {
                                    console.log(data)
                                    console.log(this.GLOBAL.STATUS.FAIL)
                                    if(data.status==this.GLOBAL.STATUS.FAIL){
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close();
                                        return
                                    }
                                    Notification({
                                        message:this.GLOBAL.MSG.EDIT_SUCCESS,
                                        type: 'success'

                                    })
                                    loading.close();
                                    this.$emit('dept-finish')
                                }).catch(()=>{

                                }
                            )
                        }else{
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            deptModel.deptListAdd(this.editForm)
                                .then((data) => {
                                    if(data.status==this.GLOBAL.STATUS.FAIL){
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close();
                                        return
                                    }
                                    Notification({
                                        message: this.GLOBAL.MSG.ADD_SUCCESS,
                                        type: 'success'

                                    })
                                    loading.close();
                                    this.$emit('dept-finish',null)
                                })
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            info(){

                deptModel.findDepartmentsByParentId(this.editForm.id).then((data)=>{
                    this.arrDepts = data.entity
                })
            },
            quit(){
                this.$emit('dept-cancel')
            },
        }
    }
</script>

<style scoped>

</style>