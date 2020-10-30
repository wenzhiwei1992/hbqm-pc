<template>
    <div>
        <el-form :model="editForm" v-if="editForm"  ref="editForm" label-width="80px">
            <el-form-item label="构件编码" prop="productNo">
                <el-input v-model="editForm.productNo"  @change="getScrappedByproductNo"></el-input>
            </el-form-item>
            <el-form-item label="构件名称" prop="productName">
                <el-input v-model="editForm.productName"></el-input>
            </el-form-item>
            <el-form-item label="规格" prop="specification">
                <el-input v-model="editForm.specification"></el-input>
            </el-form-item>
<!--            <el-form-item label="工序" prop="backlogName">
                <el-input v-model="editForm.backlogName"></el-input>
            </el-form-item>-->
           <el-form-item label="工序"  prop="backlogId">
                <el-select v-model="editForm.backlogId" placeholder="请选择">
                    <el-option
                            v-for="item in arrBackLog"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="构件明细编码" prop="productDetailId">
                <el-input v-model="editForm.productDetailId"></el-input>
            </el-form-item>
            <el-form-item label="描述" prop="scrapCauseName">
                <el-input type="textarea"   v-model="editForm.scrapCauseName"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editFormCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import productModel  from '../../../../model/project/product'
    import scrapedModel  from '../../../../model/scrapped/scrapped'
    import {Notification} from 'element-ui'
    export default {
        props:['editForm','mode'],
        mounted(){
        },
        methods:{
            showBackLog(){
                scrapedModel.getBackLogByLineId(this.editForm.productNo).then((data)=>{
                    var arr = new Array()
                    for (var i=0;i<data.entity.length;i++){
                        var parseIntLabel = parseInt(data.entity[i]).id
                        var v ={
                            label:parseIntLabel,
                            value:data.entity[i].backlogName
                        }
                        arr.push(v)
                    }
                    this.arrBackLog = arr
                })
            },
            /**
             * 通过productNo查询出productName   specification  backlogName
             * @param value
             */
            getScrappedByproductNo(value){
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    scrapedModel.getScrappedByproductNo(value).then((data)=>{
                        if(data.status=='fail'){
                            Notification({
                                message: data.msg,
                                type: 'warning'
                            })
                            loading.close()
                            return
                        }
                        scrapedModel.getBackLogByLineId(value).then((data)=>{
                           // this.arrBackLog = data.entity
                            var arr = new Array()
                            for (var i=0;i<data.entity.length;i++){
                                var parseIntLabel = parseInt(data.entity[i]).id
                                console.log("---------------TEST");//TO DELETE
                                console.log(parseIntLabel);//TO DELETE
                                var v ={
                                    label:parseIntLabel,
                                    value:data.entity[i].backlogName
                                }
                                arr.push(v)
                            }
                            this.arrBackLog = arr
                        })
                        var specification = data.entity.productLen+'*'+data.entity.productHeight+'*'+data.entity.productThick
                        this.editForm.productName= data.entity.productName
                        this.editForm.specification = specification
                        loading.close()
                    })
            },
            editFormCancel(){
                this.$emit('scraped-edit-cancel')
            },
            editFormSubmmit(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.mode=='update'){
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            scrapedModel.scrappedUpdate(this.editForm)
                                .then((data) => {
                                    if(data.status=='fail'){
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close()
                                        return
                                    }
                                    Notification({
                                        message: '编辑成功',
                                        type: 'success'
                                    })
                                    loading.close();
                                    this.$emit('scraped-edit-finish')
                                })
                        }else if(this.mode=='add'){
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            scrapedModel.scrappedAdd(this.editForm)
                                .then((data) => {
                                    console.log(data.entity);//TO DELETE
                                    if(data.status=='fail'){
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
                                    loading.close();
                                    this.$emit('scraped-edit-finish')
                                })
                        }

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
             },
     },
     data(){
            return{
                productName:'',
                productNo:'',
                arrBackLog:[],
                eidtFormRules:{

                }
            }
        }
    }
</script>

<style scoped>

</style>