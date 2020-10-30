<template>
    <div id="check-edit">
        <el-form :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="100px" size="small">
            <el-form-item label="构件编号">
                <el-input :value="productDetail.product.productNos" disabled></el-input>
            </el-form-item>
            <el-form-item label="构件名称">
                <el-input :value="productDetail.product.productNames" disabled></el-input>
            </el-form-item>
            <el-form class="switch-wrap" v-if="editForm" label-width="200px" size="small">
            <el-form-item :label="item.name+' :'" v-for="item in items" :key="item.name">
                <el-switch
                        :disabled="true"
                        :width="50"
                        v-model="item.result"
                        active-value="不合格"
                        active-text="不合格"
                        active-color="#ff4949"
                        inactive-value="合格"
                        inactive-text="合格"
                        inactive-color="#13ce66"
                        @change="itemResultChange"
                >
                </el-switch>
            </el-form-item>
            </el-form>
            <el-form-item label="检验时间 :" prop="checkDate">
                <el-date-picker
                  v-model="editForm.checkDate"
                  type="datetime"
                  placeholder="选择日期时间"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  @change="changeDate"
                >
                </el-date-picker>
            </el-form-item>
            <el-form-item label="检验说明 :">
                <el-input type="textarea" v-model="editForm.checkDesc"></el-input>
            </el-form-item>
            <el-form-item label="检验结果 :">
                <el-select v-model="editForm.checkResult">
                    <el-option
                            v-for="item in resultList"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <div style="text-align:right" class="dialog-footer">
            <el-button @click="editFormCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>



</template>
<script>
    import model from '../../../model/model'
    import clientModel from '../../../model/client-model'
    import templetModel from '../../../model/basicdata/templet'
    import moment from 'moment'
    import {Notification} from 'element-ui'

    export default {
        props:['productDetail'],
        data() {
            return {
                massLevelVisible : false,
                editForm:null,
                eidtFormRules: {
                  checkDate:{required: true, message: '请选择日期',trigger: 'change'}
                },
                items:[],
                resultList:[],

            }
        },
        mounted(){
            this.init()
        },
        computed:{
            checkType(){
                let active=this.$store.state.menuActiveName
                let checkType=''
                if(active=='productDetailHiddenCheckList'||active=='productDetailHiddenCheckListReturn'){
                    checkType='隐检'
                }else if(active=='productDetailCheckList'||active=='productDetailCheckListReturn'){
                    checkType='成品检'
                }else if(active == 'productDetailJZQRCheckList'){
                    checkType='浇筑确认';
                }
                return checkType
            },
        },
        methods:{
            changeDate(){

            },
            init(){
                this.editForm={
                    checkType:this.checkType,
                    productDetailId:this.productDetail.id,
                    checkDesc:'',
                    checkResult:'合格',
                    massLevel:'A',
                    productDetailIds:this.productDetail.productDetailIds,
                    checkDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
                }
                if(this.checkType=='成品检'){
                    this.massLevelVisible = true;
                }
                this.resultList=[{label:'合格',value:'合格'}]

                if (this.checkType == '成品检') {
                    let no = 'CPJ'
                    templetModel.findTempletByTempletNo(no).then((data)=>{
                        if (data.status == "failed") {
                            Notification({
                                message: data.msg,
                                type: "warning"
                            });
                            return;
                        }
                        this.items =data.entity
                    })
                } else if (this.checkType == '隐检') {
                    let no = 'YJ'
                    templetModel.findTempletByTempletNo(no).then((data)=>{
                        if (data.status == "failed") {
                            Notification({
                                message: data.msg,
                                type: "warning"
                            });
                            return;
                        }
                        this.items =data.entity
                    })
                } else if (this.checkType == '浇筑确认') {
                    let no = 'JZQR'
                    templetModel.findTempletByTempletNo(no).then((data)=>{
                        if (data.status == "failed") {
                            Notification({
                                message: data.msg,
                                type: "warning"
                            });
                            return;
                        }
                        this.items =data.entity
                    })
                }
            },
            editFormCancel(){
                this.$emit('model-edit-jzqr-cancel')
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        const loading = this.$loading({
                            lock: true,
                            text: 'Loading',
                            spinner: 'el-icon-loading',
                            background: 'rgba(0, 0, 0, 0.7)'
                        })
                        this.editForm.itemResultJson=JSON.stringify(this.items)
                        var fd=new FormData();
                        fd.append('checkType',this.editForm.checkType);
                        fd.append('checkDate',this.editForm.checkDate);
                        fd.append('productDetailId',this.editForm.productDetailId);
                        fd.append('checkDesc',this.editForm.checkDesc);
                        fd.append('checkResult',this.editForm.checkResult);
                        fd.append('itemResultJson',this.editForm.itemResultJson);
                        fd.append('productDetailIds',this.editForm.productDetailIds);
                        if(this.editForm.checkType=='成品检'){
                          fd.append('massLevel',this.editForm.massLevel);
                        }
                        this.$store.commit('startLoading')
                        clientModel.ax.post('/check/batchAddCheckInfo',fd)
                            .then((data) => {
                                if (data.status == 'fail') {
                                    Notification({
                                        message: data.msg,
                                        type: 'warning'
                                    })
                                    this.$store.commit('endLoading')
                                    loading.close()
                                    return
                                }
                                if(data.msg){
                                  Notification({
                                      message: data.msg,
                                      type: 'success'

                                  })
                                }else{
                                  Notification({
                                      message: '构件已完成'+this.checkType,
                                      type: 'success'

                                  })
                                }

                                loading.close()
                                this.$store.commit('endLoading')
                                this.$emit('model-edit-jzqr-finish')
                            })

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            itemResultChange(){
                if(this.items.some((e)=>e.result==='不合格')){
                    if(this.checkType=='隐检'){
                        this.resultList=[{label:'隐检返修',value:'隐检返修'}]
                        this.editForm.checkResult='隐检返修'
                    }else if(this.checkType=='成品检'){
                        this.resultList=[{label:'返修',value:'返修'},{label:'报废',value:'待报废审批'}]
                        this.editForm.checkResult='返修'
                    }
                }else{
                    this.resultList=[{label:'合格',value:'合格'}]
                    this.editForm.checkResult='合格'
                }
            }

        },
        watch:{
            productDetail:function(){
                this.init()
            }
        }

    }
</script>
<style>
    #check-edit .switch-wrap{
      height:450px;
      margin: 10px 0;
      border: 1px solid #EBEEF5;
      overflow-y:scroll;
    }
    #check-edit .el-form-item__label{
        font-size:16px;

    }
</style>
