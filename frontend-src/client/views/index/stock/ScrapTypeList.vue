<template>
    <div>
        <el-form :model="editFormScrap" v-if="editFormScrap" :rules="eidtFormRules" ref="editFormScrap"
                 label-width="200px"
                 size="small">
            <el-form-item label="报废类型">
                <!--             <el-radio v-model="editForm.massLevel" label="A">生产报废</el-radio>-->
                <el-radio v-model="editFormScrap.checkResult" label="DC">堆场报废</el-radio>
                <el-radio v-model="editFormScrap.checkResult" label="BG">变更报废</el-radio>
                <!--  <el-radio v-model="editForm.massLevel" label="D">出厂报废</el-radio>-->
            </el-form-item>
        </el-form>
        <div style="text-align:right" class="dialog-footer">
            <el-button @click="editFormCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editFormScrap')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import scrapModel from "../../../model/scrapped/scrapped"
    import {Notification} from "element-ui";

    export default {
        name: "ScrapTypeList",
        props: ['editFormScrap'],
        data() {
            return {
                eidtFormRules: {}
            }
        },
        mounted() {
            console.log('editFormScrap------>', this.editFormScrap)
            this.info()
        },
        methods: {
            info() {
                this.$set(this.editFormScrap,'checkResult','DC')
            },
            editFormCancel() {
                this.$emit('scrap-edit-cancel')
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        console.log(this.editFormScrap.checkResult)
                        if(this.editFormScrap.checkResult!="DC" && this.editFormScrap.checkResult!="BG"){
                            Notification({
                                message: "请选择报废类型",
                                type: 'warning'
                            })
                            return ;
                        }
                        const loading = this.$loading({
                            lock: true,
                            text: 'Loading',
                            spinner: 'el-icon-loading',
                            background: 'rgba(0, 0, 0, 0.7)'
                        })
                        scrapModel.scrap(this.editFormScrap).then((data) => {
                            if (data.status == 'fail') {
                                Notification({
                                    message: data.msg,
                                    type: 'warning'
                                })
                                loading.close()
                                return
                            }
                            Notification({
                                message: '构件已进入审批流程',
                                type: 'success'
                            })
                            loading.close()
                            this.$emit('scrap-edit-finish')
                        })

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
        }
    }
</script>

<style scoped>

</style>
