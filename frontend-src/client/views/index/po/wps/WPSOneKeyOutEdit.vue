<template>
    <div>
        <el-form :model="form" v-if="form"  ref="form" label-width="140px" :rules="formRules">
            <el-form-item label="成品质检结论" prop="result">
                <el-radio-group v-model="form.result">
                    <!-- 批量到入库 -->
                    <el-radio label="合格" value="合格">合格</el-radio>
                    <!-- 批量到待成品检 -->
                    <el-radio label="待质检" value="待质检">待质检</el-radio>
                    <!-- 批量报废 -->
                    <el-radio label="不合格" value="不合格">不合格</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="检验时间 :" prop="checkDate">
                <el-date-picker
                        v-model="form.checkDate"
                        type="datetime"
                        placeholder="选择日期时间"
                        format="yyyy-MM-dd HH:mm:ss"
                >
                </el-date-picker>
            </el-form-item>
            <el-form-item label="库区" prop="toStockId" v-if="form.result=='合格'">
                <el-select v-model="form.toStockId" @change="changeStock" clearable>
                    <el-option
                            v-for="stock in stocks2"
                            :key="stock.id"
                            :label="stock.stockName"
                            :value="stock.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="货架" prop="toShelfId" v-if="form.result=='合格'">
                <el-select v-model="form.toShelfId" clearable>
                    <el-option
                            v-for="shelf in shelves2"
                            :key="shelf.id"
                            :label="shelf.shelfName"
                            :value="shelf.id">
                    </el-option>
                </el-select>
            </el-form-item>
            
        </el-form>
        <div slot="footer" class="dialog-footer" align="right">
            <el-button @click="cancel">取 消</el-button>
            <el-button type="primary" @click="submit('form')">确定</el-button>
        </div>
    </div>
</template>

<script>
    import order from '../../../../model/po/order'
    import stockModel from '../../../../model/basicdata/stock'
    import shelfModel from '../../../../model/basicdata/shelf'
    import {Notification} from 'element-ui'
    export default {
        props:['selected'],
        data() {
            return {
                form:{
                },
                formRules:{
                    result: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    checkDate: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],

                },
                stocks2:[],
                shelves2:[],
            }
        },
        mounted(){
            shelfModel.findAllByOrgId().then(data => {
                    this.stocks2 = data.entity;
                    this.stocks2=this.stocks2.filter(s=>s.stockStatus=='启用'&&s.stockTypeCode=='成品仓库')
            }).catch(err => {
                Notification({
                    message: err.msg,
                    type: 'warning'
                })
            })
        },
        methods: {
            //v-if="form.result=='合格'"
            changeStock(id){
                if(!id){
                    return
                }
                stockModel.getShelfsById(id).then(data=>{
                    this.shelves2=data.entity
                    this.shelves2=this.shelves2.filter(s=>s.shelfStatus=='启用'&&s.fullStatus=='未满')
                })
            },
            cancel(){
                this.$emit("cancel");
            },
            submit(form){
                this.$refs[form].validate((valid) => {
                    if (valid) {
                        if(!this.form.checkDate){
                            Notification({
                                message: "请选择检验时间",
                                type: 'warning'
                            })
                            return;
                        }
                        const loading = this.$loading({
                            text: '执行中',
                            spinner: 'el-icon-loading',
                            background: 'rgba(0, 0, 0, 0.7)'
                        })
                        let ids = []
                        this.selected.forEach((row) => {
                            ids.push(row.id)
                        })
                        this.form.ids = ids;
                        //后台根据前台选择的ID进行一键产出
                        order.oneKeyOrderOutByResult(this.form).then(data=>{
                            if(data.status == 'success'){
                                Notification({
                                    type:'success',
                                    message: data.entity
                                })
                                loading.close()
                                this.$emit('finish')
                            }else{
                                Notification({
                                    type:'warning',
                                    message:data.msg
                                })
                                loading.close()
                                this.$emit('finish')
                            }
                        }).catch(err=>{
                            loading.close()
                        })
                    } else {
                        return false;
                    }
                });


            }
        }
    }

</script>

<style scoped>


</style>