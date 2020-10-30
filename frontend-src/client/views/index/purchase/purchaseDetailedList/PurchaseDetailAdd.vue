<template>
    <div>
        <div class="demo-input-size">
            <el-form :inline="true" id="projectList" :model="mainInformation" ref="mainInformation">
                <el-row>
                    <el-form-item label="项目" prop="projectId" style="margin-left:2em;">
                        <el-select v-model="mainInformation.projectId" placeholder="请选择" style="width:200px;">
                            <el-option
                                    v-for="item in projects"
                                    :key="item.id"
                                    :label="item.projectName"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="订单时间" prop="purchaseDate">
                        <el-date-picker
                                v-model="mainInformation.purchaseDate"
                                type="date"
                                style="width: 200px"
                                value-format="yyyy-MM-dd"
                                placeholder="选择日期">
                        </el-date-picker>
                    </el-form-item>

                    <el-form-item label="业务员" prop="salesman" style="margin-left:1em;">
                        <el-input
                                placeholder="请输入内容"
                                v-model="mainInformation.salesman"
                                style="width:200px"
                                clearable>
                        </el-input>
                    </el-form-item>

                    <el-form-item label="业务员" prop="salesmanId" style="margin-left:1em;" >
                        <el-select v-model="mainInformation.salesmanId" 
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

                    <el-form-item label="业务类型" prop="businessType">
                        <el-select
                                v-model="mainInformation.businessType"
                                style="width: 200px"
                                placeholder="请选择"
                                clearable >
                            <el-option
                                    v-for="item in optionsStatus"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>

                    
                    <el-form-item label="采购类型" prop="purchaseType">
                        <el-select
                                v-model="mainInformation.purchaseType"
                                style="width: 200px"
                                placeholder="请选择"
                                clearable >
                            <el-option
                                    v-for="item in purchaseTypes"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="汇率" prop="exchangeRrate" style="margin-left:2em;">
                        <el-input
                                placeholder="请输入内容"
                                v-model="mainInformation.exchangeRrate"
                                style="width:200px"
                                clearable>
                        </el-input>
                    </el-form-item>
                    
                    <el-form-item label="付款条件" prop="termPayment" >
                        <el-input
                                placeholder="请输入内容"
                                v-model="mainInformation.termPayment"
                                style="width:200px"
                                clearable>
                        </el-input>
                    </el-form-item>
                    
                    <el-form-item label="币种" prop="currency" style="margin-left:2em;">
                        <el-input
                                placeholder="请输入内容"
                                v-model="mainInformation.currency"
                                style="width:200px"
                                clearable>
                        </el-input>
                    </el-form-item>
                    <el-form-item label="供应商" prop="supplier" style="margin-left:1em;">
                        <el-select v-model="mainInformation.supplier" filterable placeholder="请选择" style="width:200px;">
                            <el-option
                                    v-for="item in suppliers"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="备注" prop="remarks" style="margin-left:2em;" >
                        <el-input
                                placeholder="请输入内容"
                                v-model="mainInformation.remarks"
                                style="width:200px"
                                clearable>
                        </el-input>
                    </el-form-item>
                    <el-form-item label="税前总价" prop="totalPriceAll" align="right">
                        <el-input
                                v-model="mainInformation.totalPriceAll"
                                readonly
                                style="width:200px;"
                                clearable>
                        </el-input>
                    </el-form-item>
                    <el-form-item label="税后总价" prop="afterTaxPrice" align="right">
                        <el-input
                                v-model="mainInformation.afterTaxPrice"
                                readonly
                                style="width:200px;"
                                clearable>
                        </el-input>
                    </el-form-item>
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
                  prop="deliveryTime"
                  label="供货时间"
                  min-width="160px">
                <template slot-scope="scope">
                    <el-date-picker
                            v-model="scope.row.deliveryTime"
                            type="date"
                            style="width:140px"
                            value-format="yyyy-MM-dd"
                            placeholder="选择日期">
                    </el-date-picker>
                </template>
          </el-table-column>
          <el-table-column
                  prop="needNum"
                  label="需求量">
          </el-table-column>
          <el-table-column
                  prop="companyName"
                  label="单位">
          </el-table-column>
          <el-table-column
                  prop="number"
                  label="数量"
                  width="120px">
                  <template slot-scope="scope">
                    <div class="el-input el-input--mini">
                        <input class="el-input__inner" v-model="scope.row.number" 
                        type="number" @change="changTaxRate" :class="scope.row.number > scope.row.needNum ?'inputRed' : ''" /> 
                    </div>
                 </template>
          </el-table-column>

          <el-table-column
                  prop="unitPrice"
                  label="单价"
                  width="80px">
                  <template slot-scope="scope">
                    <div class="el-input el-input--mini">
                        <input class="el-input__inner" v-model="scope.row.unitPrice"  @change="changTaxRate"  /> 
                    </div>
                 </template>
          </el-table-column>
          <el-table-column
                  prop="totalPrice"
                  label="税前价">
          </el-table-column>
          <el-table-column
                prop="taxRate"
                label="税率(%)">
                <template slot-scope="scope">
                    <div class="el-input el-input--mini">
                        <input class="el-input__inner" v-model="scope.row.taxRate" :max="100" type="number"
                                :min="0" onkeyup="value=value.replace(/[^\d]/g,'')"  @change="changTaxRate" /> 
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                  prop="afterTaxRate"
                  label="税后价">
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle" style="margin-top:10px;">
            <el-col >
                <el-button @click="addClick" type="primary">添加</el-button>
                <el-button @click="deletesClicks" type="danger" >删除</el-button>
                <el-button @click="editClick" >编辑</el-button>
                <el-button @click="cancel" >取消</el-button>
                <el-button @click="submitClick" type="primary">提交</el-button>
            </el-col>
        </el-row>
        <el-dialog :title="mode=='add'?'添加':'编辑'" v-if="editFormVisible" :visible.sync="editFormVisible" width="80%" top="10vh">
            <purchase-detail-edit :edit-form="editForm"
                                   :mode="mode"
                                   :projectId="mainInformation.projectId"
                                   @edit-finish="editFinish"
                                   @edit-cancel="editCancel"
            ></purchase-detail-edit>
        </el-dialog>
    </div>
</template>

<script>
    import purchaseModel  from '../../../../model/purchase/purchase'
    import materialStorageModel  from '../../../../model/basicdata/materialStorage'
    import menuTabService from '../../../../service/menuTabService'
    import PurchaseDetailEdit from './PurchaseDetailEdit.vue'
    import {Notification} from 'element-ui'
    import moment from "moment";
    
    export default {
        data(){
          var checkPlateCount = (rule, value, callback) => {
            if (!value) {
                return callback(new Error('填写合法数字'));
            }
            var r = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/
            if (!r.test(value)) {
                callback(new Error('请输入正数'));
            } else {
                if(value<0){
                    callback(new Error('必须大于0'));
                } else {
                    callback();
                }
            }
          };
            return{
                arrMaterialStorage:[],
                purchaseTypes:[
                    {
                        value: '生产',
                        label: '生产'
                    },
                ],
                optionsStatus:[
                    {
                        value: '业务类型1',
                        label: '业务类型1'
                    },
                    {
                        value: '业务类型2',
                        label: '业务类型2'
                    },
                    {
                        value: '业务类型3',
                        label: '业务类型3'
                    },
                    {
                        value: '业务类型4',
                        label: '业务类型4'
                    },
                ],
                projects:[],
                units:[],
                suppliers:[],
                users:[],
                eidtFormRules: {
                    
                },
                selected:[],
                //编辑表单
                mode:'',
                editFormVisible:false,
                editForm:null,
                editFormAll:[],//表单信息
                mainInformation:{
                  purchaseNo:"",//订单编号
                  businessType:"",//业务类型
                  salesman:"",//业务员
                  termPayment:"",//付款条件
                  remarks:"",//备注
                  purchaseDate:"",//订单时间
                  taxRate:"",//税率
                  totalPriceAll:0,//总价合集
                  afterTaxPrice:0,//税后总价
                },
                datas:[]
            }
        },
        watch:{
            editFormAll(newData){
                let afterTaxPrice = 0.0;
                let totalPriceAll = 0.0;
                let totalNum = 0;
                newData.forEach((val)=>{
                    afterTaxPrice+= (parseFloat(val.totalPrice)+(parseFloat(val.totalPrice)*val.taxRate/100));
                    totalPriceAll += parseFloat(val.totalPrice);
                    val.afterTaxRate = (parseFloat(val.totalPrice)+(parseFloat(val.totalPrice)*val.taxRate/100))
                    totalNum+=parseInt(val.number)
                })

                this.mainInformation.afterTaxPrice =  Math.round( afterTaxPrice* 100) / 100;
                this.mainInformation.totalPriceAll =  Math.round(totalPriceAll*100) / 100;
                this.mainInformation.totalNum = totalNum;
            }
        },
        components:{
            'purchase-detail-edit':PurchaseDetailEdit,
        },
        mounted(){
            //加载项目、供应商、单位下拉选项信息，原料采用远程搜索
            this.initBasicInfo();
            var myDate = new Date();
            this.mainInformation.purchaseDate =  myDate.getFullYear()+"-"+(myDate.getMonth()+1)+"-"+myDate.getDate();
             purchaseModel.getUsersByOrgId().then((data)=>{
                this.users = data.entity;
                for(let i =0;i<data.entity.length;i++){
                    if(data.entity[i].id==data.msg){
                        this.mainInformation.salesmanId =data.entity[i].id;
                    }
                }
            });
        },
        methods:{
            changTaxRate(){
                let afterTaxPrice = 0.0;
                //计算价格
                for (let index = 0; index < this.editFormAll.length; index++) {
                    const e = this.editFormAll[index];
                    //四舍五入 当前行的 总价 =  数量 * 单价
                    this.editFormAll[index].totalPrice = Math.round( (parseFloat(e.unitPrice)*parseFloat(e.number)) * 100) / 100;
                    //手动触发vue 的视图更新
                    this.$set(this.editFormAll,index,this.editFormAll[index] )
                    //当前行的 总价 + 总价*汇率  
                    let rowAfterTaxPrice =  (parseFloat(e.totalPrice)+(parseFloat(e.totalPrice)*e.taxRate/100));
                    //四舍五入
                    afterTaxPrice+=  Math.round( rowAfterTaxPrice* 100) / 100;
                    this.editFormAll[index].afterTaxRate = Math.round(rowAfterTaxPrice * 100) / 100;
                    //手动触发vue 的视图更新
                    this.$set(this.editFormAll,index,this.editFormAll[index] )
                }
                    
                this.mainInformation.afterTaxPrice = Math.round( afterTaxPrice * 100) / 100;
            },
            initBasicInfo(){
                purchaseModel.loadAllProject().then((data)=>{
                    this.projects = data.entity;
                });

                purchaseModel.loadAllSupplier().then((data)=>{
                    this.suppliers = data.entity;
                });

                purchaseModel.loadAllUnits().then((data)=>{
                    this.units = data.entity;
                });
            },
            //添加按钮
            addClick(){
                if(!this.mainInformation.projectId){
                    this.$message.warning(this.GLOBAL.MSG.PROJECT_NOT_SELECT)
                    return;
                }
                this.mode = 'add'
                this.editForm={
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
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    let that = this
                    let uuidAll = []
                    that.editFormAll.forEach((val,index)=>{
                        uuidAll.push(val.uuid)
                    })
                    that.selected.forEach((val,index)=>{
                        that.editFormAll.splice(uuidAll.indexOf(val.uuid),1)
                        uuidAll.splice(uuidAll.indexOf(val.uuid),1)
                    })
                    that.selected = []
                    Notification({
                        message: '删除成功',
                        type: 'success'
                    })
                    loading.close()
                }).catch(()=>{})
            },
            //编辑按钮点击事件
            editClick(){
                if(this.selected.length==0){
                    this.$message('请选择编辑项');
                    return;
                }
                var arr = new Array()
                var that = this
                that.selected.forEach((row,index)=>{
                    arr.push(row)
                })
                if(arr.length>1){
                    this.$message('只能勾选一个进行编辑！');
                    return;
                }
                this.editForm = arr[0]
                this.mode = 'update'
                this.editFormVisible=true
            },
            //编辑表单
            editFinish(editForm,mode){
                this.editFormVisible=false
                for (let i = 0; i < editForm.purchaseDetails.length; i++) {
                    const e = editForm.purchaseDetails[i];
                    this.editFormAll.push(e);
                }
                
            },
            authorized(index){
                return menuTabService.authorized(this.$store,index)
            },
            handleSelectionChange(selected){
                this.selected=selected
            },
            editCancel(){
                this.editFormVisible=false
            },
            submitClick(){
                this.mainInformation.purchaseDetails = this.editFormAll
                if(this.editFormAll.length == 0 ){
                    this.$message.warning(this.GLOBAL.MSG.DATA_IS_EMPTY)
                    return;
                }
                for (let i = 0; i < this.editFormAll.length; i++) {
                    const e = this.editFormAll[i];
                    if(e.number > parseFloat(e.needNum)*1.1 ){
                        this.$message.warning('已大于需求量110%')
                        return;
                    }
                }
                //请求后台，提交表单，关闭当前页面
                purchaseModel.add(this.mainInformation).then(data=>{
                    if(data.status == 'success'){
                         Notification({
                            type:"success",
                            message:'提交成功'
                        })
                        this.$router.push({name:'purchaseDetailedList'})
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
                this.$router.push({name:'purchaseDetailedList'})
            }
        },

    }
</script>

<style scoped>
    .inputRed{
        color: red;
    }
</style>