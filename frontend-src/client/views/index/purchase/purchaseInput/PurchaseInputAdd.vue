<template>
    <div>
        <div class="demo-input-size">
            <el-form :inline="true" :model="mainInformation" ref="mainInformation">
                <el-row>
                    <el-form-item label="供应商" prop="supplier" filterable  style="margin-left:1em;">
                        <el-select v-model="mainInformation.supplier" filterable
                         style="width:200px;">
                            <el-option
                                    v-for="item in suppliers"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <!-- <el-form-item label="采购单号" prop="purchaseId" >
                       <el-select v-model="mainInformation.purchaseId"
                            filterable
                            remote
                            reserve-keyword
                            placeholder="请输入关键词"
                            :remote-method="remoteMethod"
                            :loading="loading" 
                            style="width:200px;">
                          <el-option
                                  v-for="item in purchases"
                                  :key="item.id"
                                  :label="item.purchaseNo"
                                  :value="item.id">
                          </el-option>
                      </el-select>
                    </el-form-item> -->
                    <el-form-item label="入库时间" prop="inputDate">
                        <el-date-picker
                                v-model="mainInformation.inputDate"
                                type="date"
                                style="width: 200px"
                                value-format="yyyy-MM-dd"
                                placeholder="选择日期">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="业务员" prop="inputSalesmanId" style="margin-left:1em;" >
                        <el-select v-model="mainInformation.inputSalesmanId" 
                        @change="$forceUpdate()"
                         style="width:200px;">
                             <el-option
                                    v-for="item in users"
                                    :key="item.id"
                                    :label="item.username"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    
<!--                     
                    <el-form-item label="业务员" prop="inputSalesman" >
                        <el-input
                                placeholder="请输入内容"
                                v-model="mainInformation.inputSalesman"
                                style="width:200px"
                                clearable>
                        </el-input>
                    </el-form-item> -->
                    
                </el-row>
            </el-form>
        </div>
        <el-table
          :data="editFormAll"
          @selection-change="handleSelectionChange"
          size="mini"
          stripe
          border
          height="400"
          style="width:100%"
        >
          <el-table-column
                  type="selection">
          </el-table-column>
          <el-table-column
                  type="index"
                  label="序号">
          </el-table-column>
          <el-table-column
                  prop="rawMaterialCode"
                  label="原料编码">
          </el-table-column>
          <el-table-column
                  prop="rawMaterialName"
                  label="原料">
          </el-table-column>
           <el-table-column
                  prop="specification"
                  label="规格">
          </el-table-column>
          <el-table-column
                  prop="companyName"
                  label="单位">
          </el-table-column>
            <el-table-column
                    prop="unitPrice"
                    label="单价">
            </el-table-column>
            <el-table-column
                    prop="taxRate"
                    label="税率">
            </el-table-column>
          <el-table-column
                prop="number"
                label="采购总量">
          </el-table-column>
          <el-table-column
                prop="inputNumAll"
                label="已入库量">
            </el-table-column>
            <el-table-column
                prop="lastNumAll"
                label="待入库量">
            </el-table-column>
           <el-table-column
                prop="inputNum"
                label="本次入库量">
            <template slot-scope="scope">
            <div class="el-input el-input--mini">
                <input class="el-input__inner" v-model="scope.row.inputNum" :max="scope.row.number" type="number"
                        :min="0" onkeyup="value=value.replace(/[^\d]/g,'')" /> 
            </div>
            </template>
        </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle" style="margin-top:10px;">
            <el-col >
                <el-button @click="addClick" type="primary">添加</el-button>
                <el-button @click="deletesClicks" type="danger" >删除</el-button>
                <el-button @click="cancel" >取消</el-button>
                <el-button @click="submitClick" type="primary">提交</el-button>
            </el-col>
        </el-row>
        <el-dialog :title="mode=='add'?'添加':'编辑'" v-if="editFormVisible" :visible.sync="editFormVisible" width="80%">
            <purchase-input-edit   :edit-form="editForm"
                                   :mode="mode"
                                   @edit-finish="editFinish"
                                   @edit-cancel="editCancel"
            ></purchase-input-edit>
        </el-dialog>
    </div>
</template>

<script>
    import purchaseModel  from '../../../../model/purchase/purchase'
    import materialStorageModel  from '../../../../model/basicdata/materialStorage'
    import menuTabService from '../../../../service/menuTabService'
    import PurchaseInputEdit from './PurchaseInputEdit.vue'
    import {Notification} from 'element-ui'
    import moment from "moment";
    
    export default {
        data(){
            return{
                arrMaterialStorage:[],
                units:[],
                eidtFormRules: {
                },
                selected:[],
                suppliers:[],
                users:[],
                //编辑表单
                mode:'',
                editFormVisible:false,
                editForm:null,
                editFormAll:[],//表单信息
                mainInformation:{
                },
                datas:[],
                purchases:[],//采购订单集合
                loading:false,

            }
        },
        components:{
            'purchase-input-edit':PurchaseInputEdit,
        },
        mounted(){
            purchaseModel.loadAllSupplier().then((data)=>{
                this.suppliers = data.entity;
            });
            purchaseModel.getUsersByOrgId().then((data)=>{
                this.users = data.entity;
                for(let i =0;i<data.entity.length;i++){
                    if(data.entity[i].id==data.msg){
                        this.mainInformation.inputSalesmanId =data.entity[i].id;
                    }
                }
            });
        },
        methods:{
            loadInfo(){

            },
            remoteMethod(query){
                if (query !== '') {
                    this.loading = true;
                    setTimeout(() => {
                        this.loading = false;
                        purchaseModel.findPurchaseByNo(query).then(data=>{
                            this.purchases = data.entity;
                        })
                    },200)
                    
                } else {
                    this.purchases = [];
                }
            },
            //添加按钮
            addClick(){
                this.mode = 'add'
                this.editForm={
                    purchaseId :this.mainInformation.purchaseId,
                    supplier: this.mainInformation.supplier,
                    inputDetails:[],
                }
                if(!this.mainInformation.supplier){
                    this.$message.info(this.GLOBAL.PURCHASE_MSG.SELECTED_SUPPLIER_BEFORE)
                    return;
                }
                this.editFormVisible=true
            },
            //删除表单
            deletesClicks(){
                if(this.selected.length==0){
                    this.$message('请选择删除项');
                    return;
                }
                this.$confirm('确认删除勾选的信息?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let that = this
                    let idAll = []
                    that.editFormAll.forEach((val,index)=>{
                        idAll.push(val.id)
                    })
                    that.selected.forEach((val,index)=>{
                        that.editFormAll.splice(idAll.indexOf(val.id),1)
                        idAll.splice(idAll.indexOf(val.id),1)
                    })
                    that.selected = []
                    Notification({
                        message: '删除成功',
                        type: 'success'
                    })
                }).catch(()=>{})
            },
            authorized(index){
                return menuTabService.authorized(this.$store,index)
            },
            handleSelectionChange(selected){
                this.selected=selected
            },
            editFinish(editForm,mode){
                this.editFormVisible=false
                editForm.inputDetails.forEach(e=>{
                     this.editFormAll.push(e)
                })
               
            },
            editCancel(){
                this.editFormVisible=false
            },
            submitClick(){
                for (let index = 0; index < this.editFormAll.length; index++) {
                    const e = this.editFormAll[index];
                    if(e.inputNum > e.lastNumAll ){
                        this.$message.warning('本次入库量不能大于待入库量');
                        return;
                    }
                    
                }

                this.mainInformation.inputDetails = this.editFormAll
                if(this.editFormAll.length == 0){
                    this.$message.warning(this.GLOBAL.MSG.DATA_IS_EMPTY)
                    return;
                }
                //请求后台，提交表单，关闭当前页面
                purchaseModel.addMethod('purchaseInput',this.mainInformation).then(data=>{
                    if(data.status == 'success'){
                         Notification({
                            type:"success",
                            message:'提交成功'
                        })
                        this.$router.push({name:'purchaseInputList'})
                    }else{
                        Notification({
                            type:"error",
                            message:data.msg
                        })
                    }
                });
              
            },
            cancel(){
                //取消返回list页面
                this.$router.push({name:'purchaseInputList'})
            }
        },

    }
</script>

<style scoped>

</style>