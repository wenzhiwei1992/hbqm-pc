<template>
    <div>
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules"  ref="editForm" label-width="80px">

            <el-form-item label="中文名称" prop="unitCNName"  label-width="110px">
                <el-input v-model="editForm.unitCNName" style="width: 185px"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editFormCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import unitsModel from '../../../../model/basicdata/optionsUnits'
    import {Notification} from 'element-ui'
    export default {
        name: "OptionUnitsEdit",
        props:['editForm','mode'],
        mounted(){
            this.unitCNNameTmp = this.editForm.unitCNName
        },
        data(){
            return{
                eidtFormRules:{
                    // unitI18NName: [
                    //     {required: true, message: '请输入标准名称(KG/G/CM/M)', trigger: 'blur'},
                    // ],
                    unitCNName: [
                        {required: true, message: '请输入中文名称(千克/克/厘米/米)', trigger: 'blur'},
                    ],

                },
                unitCNNameTmp:''
            }
        },
        methods:{
            editFormCancel(){
                this.$emit('edit-cancel')
            },
            editFormSubmmit(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.mode=='edit'){
                            if (this.unitCNNameTmp == this.editForm.unitCNName) {
                                this.$message(this.GLOBAL.MSG.VALUE_NOT_CHANGE)
                                return ;
                            }
                            unitsModel.update(this.editForm)
                                .then((data) => {
                                    if(data.status=='failed'){
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
                                    this.$emit('edit-finish')
                                })
                        }else if(this.mode=='add'){
                            if (this.unitCNNameTmp == this.editForm.unitCNName) {
                                this.$message(this.GLOBAL.MSG.VALUE_NOT_CHANGE)
                                return ;
                            }
                            unitsModel.add(this.editForm)
                                .then((data) => {
                                    if(data.status=='failed'){
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
                                    this.$emit('edit-finish')
                                })
                        }

                    } else {
                        return false;
                    }
                });
            },
        },

    }
</script>

<style scoped>

</style>