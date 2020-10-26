<template>
    <div>
        <el-form inline :model="editForm"  v-if="editForm" ref="editForm"  label-position="left" :rules="editFormRulus" :disabled="showAll">
            <el-form-item label="产线" prop="lineId" >
                <el-select v-model="editForm.lineId" clearable @change="loadTeanInfo" >
                    <el-option v-for="item in lines"
                               :key="item.id"
                               :label="item.lineName"
                               :value="item.id"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="班组" prop="teamId" >
                <el-select v-model="editForm.teamId" clearable >
                    <el-option v-for="item in teamInfos"
                               :key="item.id"
                               :label="item.teamName"
                               :value="item.id"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="生产时间" prop="woDate">
                <el-date-picker
                        v-model="editForm.woDate"
                        type="date"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        placeholder="选择日期"
                >
                </el-date-picker>
            </el-form-item>
            <el-form-item label="总砼方量(m³):">
                 <span >{{totalConcrete}}</span>
            </el-form-item>
            <div style="margin-bottom:10px">
                <el-button icon="el-icon-plus" type="primary" @click="addClick"  >添加</el-button>
                <el-button icon="el-icon-minus" type="danger" @click="deleteProductData" >删除</el-button>
                <!-- <el-button icon="el-icon-position" type="success" onclick="alert('敬请期待')">批量设置模台</el-button> -->
            </div>
            <el-table
                    :data="editForm.productData"
                    @selection-change="handleMainFormSelectionChange"
                    height="450"
                    stripe
                    border>
                <el-table-column
                        type="selection">
                </el-table-column>
                <el-table-column
                        prop="project.projectName"
                        label="项目名称">
                </el-table-column>
                <el-table-column
                        prop="product.productNo"
                        label="构件编码">
                </el-table-column>
                <el-table-column
                        prop="product.productName"
                        label="构件名称">
                </el-table-column>
                <el-table-column
                        prop="product.projectConcrete"
                        label="砼方量(m³)">
                </el-table-column>
                <el-table-column
                        prop="floorRelevance.building.buildCode"
                        label="楼栋">
                </el-table-column>
                <el-table-column
                        prop="floorRelevance.floor.floorNum"
                        label="楼层">
                </el-table-column>
                <el-table-column
                        prop="plateId"
                        label="模台号">
                        <template slot-scope="scope">
                            <el-select filterable v-model="scope.row.plateId" :disabled="showAll">
                                <el-option
                                        v-for="item in plates"
                                        :key="item.id"
                                        :label="item.plateNo"
                                        :value="item.id"
                                >
                                </el-option>
                            </el-select>
                           <!-- <el-input v-model="scope.row.plateId" ></el-input>-->
                        </template>
                </el-table-column>
            </el-table>
        </el-form>
        <div slot="footer" class="dialog-footer" align="center" style="margin-top:10px">
            <el-button @click="cancel">取 消</el-button>
            <el-button type="primary" @click="submit('editForm')" icon="el-icon-circle-check" :disabled="showAll">提交</el-button>
        </div>

        <el-dialog title="构件" :visible.sync="addDataVisible" width="90%" :inline="true" top="6vh">
             <el-form :inline="true" :model="condition"  size="mini">
                 <el-form-item label="项目名称" >
                    <el-select v-model="condition.projectId" @change="loadAddDataForDialog" style="width: 140px">
                        <el-option
                                v-for="item in projectList"
                                :key="item.id"
                                :label="item.projectName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="楼栋" >
                    <el-select v-model="condition.buildId" placeholder="楼栋"  
                    style="width: 140px"
                    collapse-tags @change="bindFloorData(condition.buildId)" clearable>
                        <el-option 
                        :key="item.id" 
                        :value="item.id" 
                        v-for="item in buildCodeArr"
                        :label="item.buildCode">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="楼层">
                    <el-select v-model="condition.floorId" placeholder="楼层" 
                    style="width: 140px" collapse-tags clearable>
                        <el-option 
                        :key="item.id" 
                        :value="item.id" 
                        v-for="item in floorNumArr"
                        :label="item.floorNum">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="构件类型">
                    <el-select
                        v-model="condition.typeId"
                        filterable
                        clearable
                        remote
                        reserve-keyword
                        style="width: 140px"
                        placeholder="输入构件类型"
                        :remote-method="remoteMethodForType"
                        :loading="loading">
                        <el-option
                            v-for="item in types"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="构件名称">
                    <!--<el-select v-model="condition.selectType" placeholder="类型" style="width: 70px" collapse-tags  clearable>-->
                        <!--<el-option label="模糊" value="模糊"></el-option>-->
                        <!--<el-option label="精确" value="精确"></el-option>-->
                    <!--</el-select>-->
                    <!--<el-input v-model="condition.productName" placeholder="构件名称" style="width: 60%" ></el-input>-->
                    <el-input v-model="condition.productName" placeholder="构件名称"  ></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" style="margin-right:30px" @click="dialogSubmit()">查询</el-button>
                </el-form-item>
                <el-row>
                    <el-form-item label="生产计划" >
                        <el-select
                            v-model="condition.orderId"
                            filterable
                            remote
                            reserve-keyword
                            placeholder="输入生产计划加载数据"
                            @change="loadAddDataForDialog"
                            :remote-method="remoteMethod"
                            :loading="loading">
                            <el-option
                                v-for="item in orderDatas"
                                :key="item.id"
                                :label="item.orderNo"
                                :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="开始日期" >
                        <el-input disabled readonly v-model="condition.planDateFrom"></el-input>
                    </el-form-item>
                    <el-form-item label="结束日期" >
                        <el-input disabled readonly v-model="condition.planDateTo"></el-input>
                    </el-form-item>
                </el-row>
             </el-form>
            <el-table
                    :data="datasForDialog"
                    :cell-class-name="cell"
                    @selection-change="handleDialogSelectionChange"
                    :row-class-name="tableRowClassName2"
                    ref="table"
                    @row-click="clickRow2"
                    height="400"
                    :row-key="getRowKeys"
                    stripe
                    border>
                <el-table-column
                        type="selection"
                        :selectable='isDisabled'
                        :reserve-selection="true">
                </el-table-column>
                <el-table-column
                        type="index"
                        label="序号"
                        width="70">
                </el-table-column>
                <el-table-column
                        prop="project.projectName"
                        label="项目名称">
                </el-table-column>
                <el-table-column
                        prop="productType.name"
                        label="构件类型">
                </el-table-column>
                <el-table-column
                        prop="product.productNo"
                        label="构件编码">
                </el-table-column>
                <el-table-column
                        prop="product.productName"
                        label="构件名称">
                </el-table-column>
                <el-table-column
                        prop="floorRelevance.building.buildCode"
                        label="楼栋">
                </el-table-column>
                <el-table-column
                        prop="floorRelevance.floor.floorNum"
                        label="楼层">
                </el-table-column>
                <el-table-column
                        label="已过期限">
                        <template slot-scope="scope">
                            <el-popover trigger="hover" placement="top">
                            <p>是否超出周计划期限日期</p>
                            <div slot="reference" >
                                <el-tag  size="medium"  :class="scope.row.noticeDPS == '是'?'red' :'' "> {{ scope.row.noticeDPS }}</el-tag>
                            </div>
                            </el-popover>
                        </template>
                </el-table-column>
                
            </el-table>
            <el-row type="flex" justify="space-around" align="middle" >
                <el-col style="margin-top:3%"> </el-col>
                <el-col style="margin-top:15px">
                    <el-row type="flex" justify="end" align="middle">
                        <el-pagination
                                @size-change="handleSizeChange"
                                @current-change="handleCurrentChange"
                                :current-page="condition.pageInfo.currentPage"
                                :page-sizes="condition.pageInfo.pageSizes"
                                :page-size="condition.pageInfo.pageSize"
                                :total="condition.pageInfo.total"
                                layout="total, sizes, prev, pager, next, jumper"
                                background
                        >
                        </el-pagination>
                    </el-row>
                </el-col>
            </el-row>
            <div slot="footer" class="dialog-footer">
                <el-button @click="addDataVisible = false">取 消</el-button>
                <el-button type="primary" @click="dataAdd()">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import workOrder from '../../../model/po/workOrder'
    import combModel from '../../../model/po/comb'
    import orderModel from '../../../model/po/order'
    import plateModel from '../../../model/basicdata/plate'
    import {Notification} from 'element-ui'
    import moment from "moment";
    export default {
        data() {
            return {
                editFormRulus: {
                    lineId:[
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    teamId:[
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    woDate: [
                        {required: true, message: '必填字段', trigger: 'blur'},
                    ],
                    
                },
                editForm:{
                    teamId:'',
                    woDate:'',
                    productData:[],
                },
                /*projectConcrete:'0.000',*/
                showAll:'',
                mode:'add',
                loading: false,
                teamInfos:[],
                addDataVisible:false,
                datasForDialog:[], //弹出框构件集合 
                selectedForDialog:[], //弹出框选择项集合
                selected:[], //主表单选择项集合
                projectList:[], // 项目集合
                condition:{ //查询条件
                    orderId :'',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: 100 ,
                        pageSizes:this.GLOBAL.pageSizes
                    },
                }, 
                orderDatas:[],
                floorNumArr:[],
                buildCodeArr:[],
                plates:[],
                totalConcrete:'0.000',
                lines:[],
                types:[],//构件类型
                addedId:[]
            }
        },
        mounted(){
            
            workOrder.findAllProjectList().then(data => {
                this.projectList = data.entity;
            }).catch(() => {

            })
            workOrder.loadAllLines().then(data=>{
                this.lines = data.entity;
            })

            let id = this.$route.params.id;
            this.showAll = this.$route.params.showAll;//点击生产单号不能编辑
            //id不为空则是编辑
            if(id != undefined || id != null  ){
                this.mode = 'update';
                this.loadWorkById(id);
            }

            plateModel.getAllPlates().then((data)=>{
                this.plates = data.entity
            })
        },
        methods: {
            remoteMethodForType(query) {
                if (query !== '') {
                    this.loading = true;
                    //根据query的值去远程检索数据
                    combModel.findAllProductTypesByQuery(query).then(data=>{
                        this.types = data.entity;
                    }).catch(err=>{
                    })
                    setTimeout(() => {
                        this.loading = false;
                    }, 200);
                    
                } else {
                    this.types = [];
                }
            
            },
            /**
             * 加载班组
             */
            loadTeanInfo(){
                if(!this.editForm.lineId){
                    return;
                }
                this.editForm.teamId = '';
                workOrder.getTeamInfoById(this.editForm.lineId).then(data=>{
                    this.teamInfos = data.entity;
                });
            },
            // addClick(){
            //     // 新加产线，去除班组筛选
            //     if(!this.editForm.lineId){
            //         this.$message.warning('请选择产线');
            //         return;
            //     }
            //     this.loadAddDataForDialog();
            //     this.addDataVisible = true;
            // },
            cell({row, column, rowIndex, columnIndex}){
                if(parseFloat(row.noticeDPS)=='是' && columnIndex == 7){
                    return 'red'
                }
            },
            dialogSubmit(){
                this.loadAddDataForDialog();
            },
            bindFloorData(buildCode){
            
                if(!buildCode){
                    return;
                }
                orderModel.getFloorNumArr(this.currentProjectID,buildCode).then(data=>{
                    this.floorNumArr = data.entity;
                }).catch(err=>{
                    //ignore
                })
            },
            loadWorkById(id){
                workOrder.loadWorkById(id).then(data=>{
                    data.entity.productData.forEach(val=>{
                        val.product.projectConcrete = Number(val.product.projectConcrete).toFixed(3)
                    })
                    console.log(" data.entity", data.entity)
                    this.editForm = data.entity;
                    if(!this.editForm.lineId){
                        return;
                    }
                    workOrder.getTeamInfoById(this.editForm.lineId).then(data=>{
                        this.teamInfos = data.entity;
                    });
                }).catch(err=>{
                    console.error("err",err)
                })
            },
            remoteMethod(query) {
                if (query !== '') {
                    this.loading = true;
                    //根据query的值去远程检索数据
                    workOrder.searchByQuery(query).then(data=>{
                        this.orderDatas = data.entity;
                    }).catch(err=>{
                        console.error("err",err);
                    })
                    setTimeout(() => {
                        this.loading = false;
                    }, 200);
                    
                } else {
                    this.orderDatas = [];
                }
            },
            /**
             * 选择订单（周计划、主生产计划）加载弹出框构件信息
             */
            loadAddDataForDialog(){
                if(this.condition.orderId){
                    /**
                     * 加载日期信息，返回是 order 数据对象
                     */
                    workOrder.loadOrderPlanDateById(this.condition.orderId).then(data=>{
                        const order = data.entity;
                        this.condition.planDateTo = moment(order.planDateTo).format("YYYY-MM-DD");
                        this.condition.planDateFrom  = moment(order.planDateFrom).format("YYYY-MM-DD");

                    }).catch(err=>{

                    })
                }
                this.currentProjectID = this.condition.projectId;
                if(this.condition.projectId){
                    orderModel.getBuildCodeArr(this.condition.projectId).then(data=>{
                        this.buildCodeArr = data.entity;
                    })
                }
                //this.condition.teamId = this.editForm.teamId;
                this.condition.lineId = this.editForm.lineId;
                workOrder.loadAddDataForDialog(this.condition).then(data=>{
                    console.log("datasForDialog",data.entity.content)
                    // this.datasForDialog =  data.entity.content;
                    let tempArr= new Array();
                    data.entity.content.forEach(e=>{
                        e.idKey = e.id
                        e.existing = 'false'
                        tempArr.push(e);
                    })
                    this.datasForDialog = tempArr
                    console.log('this.datasForDialog---------------',this.datasForDialog)
                    this.condition.pageInfo.total = data.entity.totalElements;
                    let totalConcrete = "0.00";
                    for(let i =0;i<data.entity.content.length;i++){
                        totalConcrete=Number(totalConcrete) + Number(data.entity.content[i].product.projectConcrete) 
                    }
                    this.totalConcrete = Number(totalConcrete).toFixed(3); ;
                    this.removeDuplicate()
                }).catch(err=>{
                    console.error("err",err)
                });
            },
            /**
             * 把选择的构件添加到productData 里面
             */
            dataAdd(){
                if(this.selectedForDialog.length == 0 ){
                    this.$message.info(this.GLOBAL.MSG.SELECTED_ERR)
                    return;
                }
                for (let index = 0; index < this.selectedForDialog.length; index++) {
                    const e = this.selectedForDialog[index];
                    if(this.editForm.productData.indexOf(e) < 0 ){
                        console.log('productData------>',e)
                        this.editForm.productData.push(e);
                    }
                    
                }
                this.addDataVisible = false;
            },
            /**
             * 删除前端选择的数据
             */
            deleteProductData(){
                if(this.selected.length ==0){
                    this.$message('请选择删除项');
                    return;
                }
                this.selected.forEach((row) => {
                  for (let i = this.editForm.productData.length-1;i>=0;i--) {
                    if (this.editForm.productData[i].id === row.id) {
                        this.editForm.productData.splice(i, 1);
                    }
                  }
                })
                // let dataInfos = this.editForm.productData;
                // let idsIndex = [];
                // //获取所有选择的ID在表格中的索引
                // this.selected.forEach((row)=>{
                //     for (let index = 0; index < dataInfos.length; index++) {
                //         if(row == dataInfos[index]){
                //             this.editForm.productData.splice(index,1);
                //         }
                //     }
                // });
            },
            cancel() {
                this.$router.push({
                    name:'workOrder'
                });
            },
            submit(editForm) {
                 this.$refs[editForm].validate((valid) => {
                    if (valid) {
                        if(this.editForm.productData.length == 0 ){
                            this.$message.warning(this.GLOBAL.MSG.DATA_IS_EMPTY)
                            return;
                        }
                        if(this.mode == 'add'){
                            const loading = this.$loading({
                                text: '创建中',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            workOrder.add(this.editForm).then(data=>{
                                if (data.status === 'success') {
                                    Notification({
                                        message: this.GLOBAL.MSG.ADD_SUCCESS,
                                        type: 'success'
                                    });
                                    loading.close();
                                    this.$router.push({
                                        name:'workOrder'
                                    });
                                } else {
                                    Notification({
                                        message: data.msg,
                                        type: 'error'
                                    })
                                    loading.close();
                                }
                            }).catch(err=>{
                                loading.close();
                                console.error("err",err)
                            });
                        }else if(this.mode == 'update'){
                            const loading = this.$loading({
                                text: '加载中',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            })
                            workOrder.update(this.editForm).then(data=>{
                                if (data.status === 'success') {
                                    Notification({
                                        message: this.GLOBAL.MSG.EDIT_SUCCESS,
                                        type: 'success'
                                    });
                                    loading.close();
                                    this.$router.push({
                                        name:'workOrder'
                                    });
                                } else {
                                    Notification({
                                        message: this.GLOBAL.MSG.UPDATE_ERR,
                                        type: 'success'
                                    })
                                    loading.close();
                                }
                            }).catch(err=>{
                                loading.close();
                                console.error("err",err)
                            });
                        }
                        
                    }else{
                         return false;
                    }
                })
                

            },
            handleMainFormSelectionChange(selected){
                this.selected = selected
            },
            handleDialogSelectionChange(selectedForDialog){
                this.selectedForDialog = selectedForDialog
            },
            handleSizeChange(val) {
                this.condition.pageInfo.pageSize = val;
                this.loadAddDataForDialog()
            },
            handleCurrentChange(val) {
                this.condition.pageInfo.currentPage = val;
                this.loadAddDataForDialog()
            }, 
            getRowKeys(row) {
                return row.id;
            },
            // 一层弹窗已添加的构件 显示已添加的样式
            tableRowClassName2(row,index){
                if(row.row.existing==='true'){
                    return 'stopclick'
                }else{
                    return ''
                }
            },
            clickRow2(row){
                console.log(row)
                if(row.existing==='false'){
                    this.$refs.table.toggleRowSelection(row)
                }
            },
            // 初始化添加构件的信息
            addClick(){
                // 新加产线，去除班组筛选
                if(!this.editForm.lineId){
                    this.$message.warning('请选择产线');
                    return;
                }
                this.selected = [];
                this.productTempDatas = [];
                // 获取所有一层弹窗数据的id
                let idArr = []
                this.editForm.productData.forEach(val=>{
                    idArr.push(val.idKey)
                })
                this.addedId = idArr;
                this.loadAddDataForDialog();
                this.addDataVisible = true;
                this.$refs.table.clearSelection();
            },
            // 去重
            removeDuplicate(){
                let that = this;
                for(let i=0;i<that.datasForDialog.length;i++){
                    // that.datasForDialog.disabled = true;
                    console.log('that.addedId',that.addedId)
                    that.addedId.forEach(val=>{
                        if(that.datasForDialog[i].idKey===val){
                            that.datasForDialog[i].existing = 'true'
                        }
                    })
                }
            },
            isDisabled(row){
                if(row.existing==='true'){
                    return false
                }else{
                    return true
                }
            },
        }
    }

</script>

<style scoped>

.red {
    color: red;
}

/* .el-tag--medium{
    color: red;
} */

tbody tr.stopclick{
    color:rgba(0,0,0,0.2);
    background:#fff;
    /* text-decoration:line-through; */
}
</style>