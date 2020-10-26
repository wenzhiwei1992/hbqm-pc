<template>
    <div>
        <div class="formClass">
            <el-form :inline="true" :model="editForm" v-if="editForm" :rules="eidtFormRules"   ref="editForm" label-width="80px">
                <el-form-item label="操 作 人"  prop="proposer">
                    <el-input v-model="editForm.proposer" style="width: 178px" ></el-input>
                </el-form-item>

                <el-form-item label="用途"  prop="purpose">
                    <el-select
                            v-model="editForm.purpose"
                            style="margin-left: 14px;"
                            placeholder="请选择">
                        <el-option
                                v-for="item in optionsPurpose"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="仓库"  prop="stockId" width="300px" >
                    <el-select v-model="editForm.stockId"  @change="changeStock"
                               style=""
                               placeholder="请选择">
                        <el-option
                                v-for="item in stockSelect"
                                :key="item.stockName"
                                :label="item.stockName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="备注"  prop="remark" width="300px" >
                    <el-input v-model="editForm.remark"  type="textarea" ></el-input>
                </el-form-item>
            </el-form>
        </div>
        <div>
            <el-row type="flex" justify="space-around" align="middle">
                <el-col >
                    <el-button @click="addClick">添加</el-button>
                    <el-button @click="deletesClicks">删除</el-button>
                </el-col>
            </el-row>
            <!--       @cell-click="changeSum"-->
            <el-table
                    :data="editForm.listDoDetail"
                    @selection-change="handleSelectionChange"
                    size="mini"
                    stripe
                    border
                    height="300"
                    style="width:100%"
            >
                <el-table-column
                        type="selection"
                        width="35">
                </el-table-column>
                <el-table-column
                        prop="materialStorageId"
                        label="原料编码"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="materialName"
                        label="名称"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="materialSpecification"
                        label="规格"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="materialUnit"
                        label="单位"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="inventory"
                        label="库存量"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="surplus"
                        label="可补差量"
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="amount"
                        label="补差量"
                        width="120">
                    <template slot-scope="scope">
                        <span v-if="mode=='update'&&scope.row.surplus==0"><el-input  v-model="scope.row.amount"   @keyup.native="parseValue(scope.row,scope.$index)" placeholder="请输入内容" ></el-input></span>
                        <span v-else-if="scope.row.materialStorageId.indexOf('GC')!=-1"> <el-input v-model="scope.row.amount"  @keyup.native="parseValue(scope.row,scope.$index)" placeholder="请输入内容"></el-input></span>
                        <span v-else><el-input  v-model="scope.row.amount"   @keyup.native="parseValue(scope.row,scope.$index)" placeholder="请输入内容" ></el-input></span>
                    </template>
                </el-table-column>
                <el-table-column
                        label="备注"
                        width="120"
                >
                    <!--<el-input v-model="editForm.reason" type="textarea"  ></el-input>-->

                    <template slot-scope="scope">
                        <el-input
                                type="textarea"
                                v-model="scope.row.reason"
                                placeholder="请输入内容"
                        ></el-input>
                    </template>
                </el-table-column>
            </el-table>
            <el-dialog   :visible.sync="editFormMaterlStorageAddVisible" width="70%" v-if="editFormMaterlStorageAddVisible" append-to-body>
                <material-storage-add :editFormMaterlStorageAdd="editFormMaterlStorageAdd"
                                      :editForm="editForm"
                                      v-on:headCallBack="headCall"
                                      @materl-storage-add-finish="materlStorageAddFinish"
                                      @materl-storage-add-cancel="materlStorageAddCancel"
                ></material-storage-add>
            </el-dialog>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editFormCancel">关闭</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import deliveryOrderModel from '../../../../model/materials/deliveryOrder'
    import materialStorageModel  from '../../../../model/basicdata/materialStorage'
    import stockModel from '../../../../model/basicdata/stock'
    import MaterialStorageAdd from  '../deliveryOrder/MaterialStorageAdd.vue'
    import {Notification} from 'element-ui'
    import  moment from  'moment'
    export default {
        mounted(){
            this.stockList();
        },
        name: "DeliveryOrderEdit",
        props:['editForm','mode'],
        components:{
            'material-storage-add': MaterialStorageAdd,
        },
        computed:{
            materlStorageAddTitle(){

            }
        },
        methods:{
            parseValue(row, index) {
                let value = row.amount;
                value = value.replace(/[^(\-)*\d.]/g, ""); // 清除"数字"和"."以外的字符
                value = value.replace(/^\./g, ""); // 验证第一个字符是数字而不是
                value = value.replace(/\.{2,}/g, "."); // 只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
                    ".");
                value = value.replace("-", "$#$").replace(/\-/g, "").replace("$#$",
                    "-");
                value = value.replace(/^(\-)*(\d+)\.(\d\d\d\d).*$/, '$1$2.$3'); // 只能输入3个小数
                row.amount = value;
                let num = 0;
                for(let i =0;i<this.editForm.listDoDetail.length;i++){
                    num=parseFloat(num)+parseFloat(this.editForm.listDoDetail[i].amount==null?0:this.editForm.listDoDetail[i].quantity);
                }
                this.totalQuantity = num.toFixed(4);
            },
            changeStock(){
                let stockId = this.editForm.stockId;
                this.editForm.listDoDetail =  [];
            } ,
            stockList() {
                stockModel.getStockByOrgIdAndType().then((data) => {
                    this.stockSelect = data.entity;
                })
            },
            editFormSubmmit(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        console.log(this.editForm)
                        if(this.mode=='update'){
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            deliveryOrderModel.updatePKBC(this.editForm).then((data)=>{
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
                                this.$emit('turnoverRecord-edit-finish')
                            })


                        }else if(this.mode=='add'){
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            deliveryOrderModel.addPKBC(this.editForm).then((data)=>{
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
                                this.$emit('turnoverRecord-edit-finish')
                            })

                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            changeSum(row){
                var arr = new Array()
                arr.push(row)//改变申请量后对象
                arr.push(this.editForm.listDoDetail)//存放对象的集合
                materialStorageModel.changeSum(arr).then((data)=>{
                    this.editForm.listDoDetail = data.entity
                })
            },
            addClick(){
                if(''==this.editForm.stockId){
                    this.$message('请先选择仓库！')
                    return
                }
                this.editFormMaterlStorageAddVisible= true
            },
            /**
             * 删除操作
             */
            deletesClicks(){
                if(this.selected.length==0){
                    this.$message('请选择删除项');
                    return;
                }
                this.selected.forEach((row)=>{
                    for(var i=0;i<this.editForm.listDoDetail.length;i++){
                        // if(this.editForm.listDoDetail.length>1){
                        if(this.editForm.listDoDetail[i].materialStorageId==row.materialStorageId){
                            this.editForm.listDoDetail.splice(i,1);
                        }
                        //}else{
                        // this.$message('该数据不能删除！')
                        //}
                    }
                })
            },
            editFormCancel(){
                this.$emit('turnoverRecord-edit-cancel')
            },
            materlStorageAddFinish(){
                this.editFormMaterlStorageAddVisible = false
                this.showMaterialStorageList()
            },
            materlStorageAddCancel(){
                this.editFormMaterlStorageAddVisible = false
                // this.showMaterialStorageList()
            },
            handleSelectionChange(selected){
                this.selected=selected
            },
            formatterDate(row, column, cellValue, index){
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            headCall(msg){
                this.arrMaterialsAddDelivery = msg
                this.showMaterialStorageList()
                this.editFormMaterlStorageAddVisible = false
            },
            showMaterialStorageList(){
                materialStorageModel.getMaterialArrayAddBelongToDoDetail(this.arrMaterialsAddDelivery).then((data)=>{
                    if(data.status=='fail'){
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        return
                    }
                    for(var i=0;i<data.entity.length;i++){//遍历为领料单原料集合添加新的原料
                        this.editForm.listDoDetail.push(data.entity[i])
                    }
                })
            },
        },
        data(){
            return{
                arrMaterialsAddDelivery:[],
                stockSelect:[],
                selected:[],
                quantity:0,//申请量
                univalence:0,//单价
                sum:0,//总价格
                optionsPurpose:[
                    {
                        value: '盘库补差',
                        label: '盘库补差'
                    },
                ],
                deliveryOrderQuery:{
                    orderNo:''
                },
                eidtFormRules:{
                    proposer: [
                        {required: true, message: '操作人不能为空', trigger: 'blur'},
                    ],
                    purpose: [
                        {required: true, message: '用途不能为空', trigger: 'change'},
                    ],
                    stockId: [
                        {required: true, message: '仓库不能为空', trigger: 'change'},
                    ],
                },
                editFormMaterlStorageAdd:'',
                editFormMaterlStorageAddVisible:false,
            }

        },
    }
</script>

<style scoped>
    .formClass{
        border: 1px lavender solid;
        margin-bottom: 10px;
        padding-top: 17px;
    }
    .sumClass{
        color: #F56C6C;
    }
</style>