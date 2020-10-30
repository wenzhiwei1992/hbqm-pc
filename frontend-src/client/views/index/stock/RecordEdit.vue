<template>
    <div id="record-edit">
        <el-form  :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="100px" size="small" >
            <el-form-item label="构件编号">
                <el-input :value="productDetail.product.productNo" disabled></el-input>
            </el-form-item>
            <el-form-item label="构件名称">
                <el-input :value="productDetail.product.productName" disabled></el-input>
            </el-form-item>
            <el-form-item label="库区" prop="toStockId">
                <el-select v-model="editForm.toStockId" @change="changeStock">
                    <el-option
                            v-for="stock in stocks"
                            :key="stock.id"
                            :label="stock.stockName"
                            :value="stock.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="货架" prop="toShelfId">
                <el-select v-model="editForm.toShelfId" filterable>
                    <el-option
                            v-for="shelf in shelves"
                            :key="shelf.id"
                            :label="shelf.shelfName"
                            :value="shelf.id">
                    </el-option>
                </el-select>
            </el-form-item>

            <!--<el-form-item label=" 托盘" prop="taryId">-->
                <!--<el-select v-model="editForm.taryId" filterable>-->
                    <!--<el-option-->
                            <!--v-for="tary in trays"-->
                            <!--:key="tary.id"-->
                            <!--:label="tary.trayNo"-->
                            <!--:value="tary.id">-->
                    <!--</el-option>-->
                <!--</el-select>-->
            <!--</el-form-item>-->
        </el-form>
        <div style="text-align:right" class="dialog-footer">
            <el-button @click="editFormCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>



</template>
<script>
    import model from '../../../model/model'
    import stockModel from '../../../model/basicdata/stock'
    import shelfModel from '../../../model/basicdata/shelf'
    import trayModel from '../../../model/basicdata/tray.js'
    import {Notification} from 'element-ui'

    export default {
        props:['productDetail','stocks','shelves','trays'],
        data() {
            return {
                editForm:{
                    toStockId:null,
                    toShelfId:null
                },
                eidtFormRules: {
                    toStockId: [
                        {required: true, message: '请选择库区号', trigger: 'blur'},
                    ],
                    toShelfId: [
                        {required: true, message: '请选择货架号', trigger: 'blur'},
                    ],
                },
            }
        },
        mounted(){
            this.init()
        },
        computed:{
        },
        methods:{
            init(){
                this.editForm.productDetailId=this.productDetail.id
            },
            changeStock(id){
                stockModel.getShelfsById(id).then(data=>{
                    this.shelves=data.entity
                    this.shelves=this.shelves.filter(s=>s.shelfStatus=='启用'&&s.fullStatus=='未满')
                })
            },
            editFormCancel(){
                this.$emit('model-edit-cancel')
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let productStatus=this.productDetail.productStatus
                        if(productStatus=='待入库'){
                            this.editForm.toStockNo=this.stocks.find((e)=>e.id==this.editForm.toStockId).stockNo
                            this.editForm.toShelfNo=this.shelves.find((e)=>e.id==this.editForm.toShelfId).shelfNo
                            this.editForm.inOutType='入库'

                            this.$store.commit('startLoading')
                            model.add('record',this.editForm)
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
                                        message: '构件已完成入库',
                                        type: 'success'

                                    })
                                    this.$store.commit('endLoading')
                                    this.$emit('model-edit-finish')
                                })
                        }


                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },

        },
        watch:{
            productDetail:function(){
                this.init()
            }
        }

    }
</script>
<style>
    #check-edit .el-form-item__label{
        font-size:16px;

    }
</style>