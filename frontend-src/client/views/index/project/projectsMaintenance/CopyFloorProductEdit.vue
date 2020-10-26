<template>
    <div>
        <el-form :inline="true" :model="copyEditForm" v-if="copyEditForm" :rules="copyEidtFormRules" ref="copyEditForm"
                 label-width="80px">
            <div>
                <el-form-item label="复制楼层" prop="floorNum1">
                    <el-select v-model="copyEditForm.existId"  style="width: 400px" placeholder="请选择">
                        <el-option
                                v-for="item in existProduct"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
            </div>
            <div>
                <el-form-item label="到" prop="floorNum2">
                    <el-select multiple v-model="copyEditForm.notExistIds"  style="width: 400px" placeholder="请选择">
                        <el-option
                                v-for="item in notExistProduct"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
            </div>


        </el-form>
        <div id="submit">

            <el-button @click="submitForm('copyEditForm')">确定</el-button>
            <el-button @click="close">关闭</el-button>
            <span>复制楼层同时复制该楼层下的所有构件!</span>

        </div>
    </div>
</template>

<script>
    import floorRelevanceModel from '../../../../model/project/floorRelevance'
    import {Notification} from 'element-ui'

    export default {
        mounted() {


        },
        props: ['copyEditForm', 'existProduct', 'notExistProduct', 'buildingId'],
        methods: {
            close() {
                this.$emit('copy-edit-cancel')
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    //  this.copyEditForm
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    var floorRelevanceQuery = {
                        buildId: this.buildingId,
                        existId: this.copyEditForm.existId,
                        notExistIds: this.copyEditForm.notExistIds,
                    }
                    floorRelevanceModel.getFloorRelevancesByBuildingIdAndFloorId(floorRelevanceQuery).then((data) => {
                        if (data.status == 'failed') {
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        Notification({
                            message: '复制成功',
                            type: 'success'
                        })
                        loading.close();
                        this.$emit('copy-edit-finish')
                    })
                })
            }

            // getFloorRelevancesByBuildingIdAndFloorId

        },
        data() {
            return {
                copyEidtFormRules: {}
            }
        }
    }
</script>

<style scoped>
    #submit {
        margin-left: 80px;
    }

</style>