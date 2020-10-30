<template>
    <el-dialog
            :title="!dataForm.id ? '新增' : '修改'"
            :visible.sync="visible">
        <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
                 label-width="200px">
            <el-form-item label="预制构件名称" prop="perCompName">
                <el-input v-model="dataForm.perCompName" placeholder="预制构件名称"></el-input>
            </el-form-item>
            <el-form-item label="预制构件出厂日期" prop="perCompDate">
                <el-date-picker
                        v-model="dataForm.perCompDate"
                        type="datetime"
                        format="yyyy-MM-dd HH:mm:ss"
                        placeholder="选择日期时间">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="预制混凝土构件生产企业" prop="prodCompany">
                <el-input v-model="dataForm.prodCompany" placeholder="预制混凝土构件生产企业"></el-input>
            </el-form-item>
            <el-form-item label="混凝土设计强度等级" prop="strenthGrade">
                <el-input v-model="dataForm.strenthGrade" placeholder="混凝土设计强度等级"></el-input>
            </el-form-item>
            <el-form-item label="规格尺寸" prop="spec">
                <el-input v-model="dataForm.spec" placeholder="规格尺寸"></el-input>
            </el-form-item>
            <el-form-item label="达到设计强度" prop="strenth">
                <el-input v-model="dataForm.strenth" placeholder="达到设计强度"></el-input>
            </el-form-item>
            <el-form-item label="结论" prop="remark">
                <el-input v-model="dataForm.remark" placeholder="结论"></el-input>
            </el-form-item>
            <el-form-item label="技术负责人" prop="conclus">
                <el-input v-model="dataForm.conclus" placeholder="技术负责人"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()" :diabled="submitting">确定</el-button>
    </span>
    </el-dialog>
</template>

<script>
    import model from '../../../model/model'

    export default {
        data() {
            return {
                visible: false,
                dataForm: {
                    id: 0,
                    perCompDate:null
                },
                dataRule: {
                    perCompName: [
                        {required: true, message: '预制构件名称不能为空', trigger: 'blur'}
                    ],
                },
                submitting:false
            }
        },
        methods: {
            init(id) {
                this.dataForm.id = id || 0
                this.visible = true
                this.$nextTick(() => {
                    this.$refs['dataForm'].resetFields()
                    if (this.dataForm.id) {
                        model.load('certi', id).then(data => {
                            if (data && data.status === 'success') {
                                this.dataForm = data.entity
                            }
                        })
                    }
                })
            },
            // 表单提交
            dataFormSubmit() {
                this.$refs['dataForm'].validate((valid) => {
                    if (valid) {
                        this.submitting=true
                        if (this.dataForm.id) {
                            model.update('certi', this.dataForm)
                                .then((data) => {
                                    this.submitting=false
                                    if (data && data.status === 'success') {
                                        this.$message({
                                            message: '操作成功',
                                            type: 'success',
                                        })
                                        this.visible = false
                                        this.$emit('refreshDataList')
                                    } else {
                                        this.$message.error(data.msg)
                                    }
                                })
                        } else {
                            model.add('certi', this.dataForm)
                                .then((data) => {
                                    this.submitting=false
                                    if (data && data.status === 'success') {
                                        this.$message({
                                            message: '操作成功',
                                            type: 'success',
                                        })
                                        this.visible = false
                                        this.$emit('refreshDataList')
                                    } else {
                                        this.$message.error(data.msg)
                                    }
                                })
                        }

                    }
                })
            }
        }
    }
</script>
