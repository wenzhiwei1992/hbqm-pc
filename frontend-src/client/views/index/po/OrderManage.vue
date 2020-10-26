<template>
    <div>
        <el-form :inline="true" :model="formInline" class="demo-form-inline" size="mini">
            <el-row>
                <el-form-item label="订单编号">
                    <el-input v-model="formInline.orderNo" placeholder="订单编号" :disabled="disableOthers"></el-input>
                </el-form-item>
                <el-form-item label="订单状态">
                    <el-select v-model="formInline.orderStatus" placeholder="订单状态" multiple collapse-tags :disabled="disableOthers">
                        <el-option label="新建" value="新建"></el-option>
                        <el-option label="提交" value="提交"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="开始日期">
                <el-date-picker
                        v-model="formInline.planDateFrom"
                        type="date"
                        placeholder="选择日期">
                </el-date-picker>
                </el-form-item>
                <el-form-item label="结束日期">
                    <el-date-picker
                            v-model="formInline.planDateTo"
                            type="date"
                            placeholder="选择日期">
                    </el-date-picker>
                </el-form-item>
            </el-row>
            <el-form-item label="项目名称">
                    <el-input v-model="formInline.projectName" placeholder="项目名称" :disabled="disableOthers"></el-input>
                </el-form-item>
            <el-form-item label="构件编码">
                <el-input v-model="formInline.productNo" placeholder="构件编码" @change="disableOther()" ></el-input>
            </el-form-item>
            <el-form-item label="构件名称">
                <el-input v-model="formInline.productName" placeholder="构件名称" @change="disableOther()"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit">查询</el-button>
            </el-form-item>
        </el-form>
        <el-table
                :data="datas"
                @expand-change="showSubData"
                size="mini"
                stripe
                border
                :default-sort = "{prop: 'dateCreated', order: 'descending'}"
                :expand-row-keys="expands"
                row-key="id"
                height="400"
                style="width: 100%"
                :row-class-name="tableRowClassName"

        >
            <el-table-column type="expand">
                <template slot-scope="props">
                    <el-table
                            :data="subDatas"
                            size="mini"
                            stripe
                            border
                            style="width: 100%"
                            
                    >

                        <el-table-column
                                prop="productNo"
                                label="构件编码"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="productName"
                                label="构件名称"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="prodlineName"
                                label="产线"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="pathName"
                                label="工艺路径"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="num"
                                label="数量"
                        >
                        </el-table-column>
                    </el-table>
                </template>
            </el-table-column>
            <!-- <el-table-column
                    sortable
                    prop="id"
                    label="序号"
                    width="80">
            </el-table-column> -->
            <el-table-column
                    type="index"
                    label="序号"
                    width="70">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="orderNo"
                    label="订单编号"
                    width="160">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="orderStatus"
                    label="订单状态"
                    width="140">
            </el-table-column>
            <el-table-column
                    prop="projectName"
                    label="项目名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="orderQty"
                    label="订货总数"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="planDateFrom"
                    label="开始日期"
                    width="120"
                    :formatter="formatterDate">
            </el-table-column>
            <el-table-column
                    prop="planDateTo"
                    label="结束日期"
                    width="120"
                    :formatter="formatterDate">
            </el-table-column>
            <el-table-column
                    prop="createdByName"
                    label="创建人"
                    width="120">
            </el-table-column>
            <el-table-column
                    :formatter="formatterDateS"
                    prop="dateCreated"
                    label="创建时间"
                    width="160">
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="270"
                    align="left">
                <template slot-scope="scope">
                    <el-button size="mini" @click="editClick(scope.row)">编辑</el-button>
                    <el-button type="success" size="mini" @click="updateStatusToSubmit(scope.row)">提交</el-button>
                    <el-button type="danger" size="mini" @click="updateStatusToCancel(scope.row)">撤销</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle" >
            <el-col style="margin-top:15px">
                <el-button @click="addClick" type="primary">新建</el-button>
            </el-col>
            <el-col>
                <el-row type="flex" justify="end" align="middle">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="pageInfo.currentPage"
                            :page-sizes="pageInfo.pageSizes"
                            :page-size="pageInfo.pageSize"
                            :total="pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-col>
        </el-row>
        <el-dialog :title="mode==='add'?'新建':'编辑'" :visible.sync="editFormVisible" width="70%">
            <el-form inline="inline" :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm"
                     label-width="80px">
                <el-row>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item label="项目名称" prop="projectName">
                                <el-select v-model="editForm.projectName" placeholder="" value=""
                                           @change="proNameChange">
                                    <el-option
                                            v-for="item in projectListForOrder"
                                            :key="item.id"
                                            :label="item.projectName"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="grid-content bg-purple-light">
                            <el-form-item label="客户名称">
                                <el-input v-model="editForm.customer" disabled="disabled"></el-input>
                            </el-form-item>

                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item label="工地名称">
                                <el-input v-model="editForm.workPlace" disabled="disabled"></el-input>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item label="开始日期">
                                <el-date-picker
                                        v-model="editForm.planDateFrom"
                                        type="date"
                                        placeholder="选择日期时间">
                                </el-date-picker>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="grid-content bg-purple-light">
                            <el-form-item label="结束日期">
                                <el-date-picker
                                        v-model="editForm.planDateTo"
                                        type="date"
                                        placeholder="选择日期时间">
                                </el-date-picker>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item label="订单加急">
                                <el-select v-model="editForm.urgOrderFlag" value="">
                                    <el-option label="不加急" value="不加急"></el-option>
                                    <el-option label="加急" value="加急"></el-option>
                                </el-select>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12"><div class="grid-content bg-purple">
                        <el-form-item label="备注">
                            <el-input placeholder="请输入内容"
                                      v-model="editForm.remark">
                            </el-input>
                        </el-form-item>
                    </div></el-col>
                    <el-col :span="12"><div class="grid-content bg-purple-light"></div></el-col>
                </el-row>

                <!--添加构件-->
                <el-col>
                    <el-button @click="initialize">添加</el-button>
                    <el-button @click="deletesProduct">删除</el-button>
                    <span style="margin-left:1%;" >已选择选择生产量(全选查看) : {{selectedOrderNum}}</span>
                    <span style="margin-left:45%;" >已勾选数(全选查看) : {{selected.length}}</span>
                </el-col>
                <br>
                <el-dialog append-to-body :visible.sync="editForm2Visible" width="70%">
                    <el-col>
                        <el-form :inline="true" :model="orderProductCondition" class="demo-form-inline" size="mini">
                            <el-form-item label="户型">
                                <el-select v-model="orderProductCondition.houseTypeId" placeholder="户型"  collapse-tags clearable @change="bindBuildData(orderProductCondition.houseTypeId)">
                                   <el-option 
                                    :key="item.id" 
                                    :value="item.id" 
                                    v-for="item in houseTypeArr"
                                    :label="item.name">
                                   </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="楼栋">
                                <el-select v-model="orderProductCondition.buildCode" placeholder="楼栋"  collapse-tags @change="bindFloorData(orderProductCondition.buildCode)" clearable>
                                   <el-option 
                                    :key="item.id" 
                                    :value="item.id" 
                                    v-for="item in buildCodeArr"
                                    :label="item.buildCode">
                                   </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="楼层">
                                <el-select v-model="orderProductCondition.floorNum" placeholder="楼层"  collapse-tags clearable>
                                    <el-option 
                                    :key="item.id" 
                                    :value="item.floorNum" 
                                    v-for="item in floorNumArr"
                                    :label="item.floorNum">
                                   </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="构件名称">
                                <el-input v-model="orderProductCondition.productName" placeholder="构件名称"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" @click="onOrderProductSubmit">查询</el-button>
                            </el-form-item>
                        </el-form>
                    </el-col>
                    <el-col>
                        <el-button @click="addSelectProduct2ProductDatas" :disabled="addDisabled">添加构件</el-button>
                        <el-button @click="editForm2Visible = false">关闭</el-button>

                    </el-col>
                    <br>
                    <el-table
                            :data="productTempDatas"
                            @selection-change="handleSelectionChangeCheckBox"
                            size="mini"
                            stripe
                            border
                            height="300"
                            ref="table"
                            @row-click="clickRow2"
                    >
                        <el-table-column
                                type="selection"
                                width="80">
                        </el-table-column>
                        <el-table-column
                                prop="productNo"
                                label="构件编码11111"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="productName"
                                label="构件名称"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="projectConcrete"
                                label="砼方量"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="houseType"
                                label="户型"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="buildCode"
                                label="楼栋"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="floorNum"
                                label="楼层"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="surplusCount"
                                label="剩余生产量"
                                width="120">
                        </el-table-column>
                        <el-table-column
                                prop="moldName"
                                label="模具"
                                width="120">
                        </el-table-column>
                    </el-table>
                    <!--分页-->
                    <el-row type="flex" justify="space-around" align="middle" >
                        <el-col>
                            已选择选择生产量 : {{multipleSelectionLength}}
                        </el-col>
                        <el-col>
                            <el-row type="flex" justify="end" align="middle">
                                <el-pagination
                                        @size-change="handleSizeChange2"
                                        @current-change="handleCurrentChange2"
                                        :current-page="pageInfo2.currentPage"
                                        :page-sizes="pageInfo2.pageSizes"
                                        :page-size="pageInfo2.pageSize"
                                        :total="pageInfo2.total"
                                        layout="total, sizes, prev, pager, next, jumper"
                                        background
                                >
                                </el-pagination>
                            </el-row>
                        </el-col>
                    </el-row>
                </el-dialog>
                <el-table
                        :data="editForm.productDatas"
                        @selection-change="handleSelectionChange"
                        v-model="editForm.productDatas"
                        size="mini"
                        stripe
                        border
                        ref="firstPopup"
                        @row-click="clickRow1"
                        height="300"
                >
                    <el-table-column
                            type="selection"
                            width="80">
                    </el-table-column>
                    <el-table-column
                            prop="productNo"
                            label="构件编码"
                            width="160">
                    </el-table-column>
                    <el-table-column
                            prop="productName"
                            label="构件名称"
                            width="160">
                    </el-table-column>
                    <el-table-column
                            prop="num"
                            label="数量"
                            width="120">
                        <template slot-scope="scope">
                            <el-input v-model="scope.row.num" @click.stop.native type="number" :min="1" onkeyup="value=value.replace(/[^\d]/g,'')"
                            @input="handleNumberChange"></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="surplusCount"
                            label="剩余生产量"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="projectConcrete"
                            label="砼方量"
                            width="160">
                    </el-table-column>
                    <el-table-column
                            prop="buildCode"
                            label="楼栋"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            label="楼层"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="moldName"
                            label="模具"
                            width="160">
                    </el-table-column>

                </el-table>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
            </div>
        </el-dialog>

    </div>


</template>

<script>
    import orderModel from '../../../model/po/order'
    import orderChooseModel from '../../../model/po/orderChoose'
    import {Notification} from 'element-ui'
    import moment from "moment";
    import global from '../../../components/global.vue'

    export default {
        data() {
            return {
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize:this.GLOBAL.pageSize,
                    pageSizes:this.GLOBAL.pageSizes
                },
                pageInfo2: {
                    currentPage: 1,
                    total: 0,
                    pageSize:this.GLOBAL.pageSize,
                    pageSizes:this.GLOBAL.pageSizes
                },
                orderProductCondition:{
                    buildCode:'',
                    floorNum:'',
                    productNo:'',
                    productName:'',
                    moldName:''
                },
                houseTypeArr:[],
                selectedOrderNum:0,
                inline: true,
                datas: [],
                subDatas: [],
                mode: '',
                selected: [],
                expands:[],
                editFormTitle: '',
                editFormVisible: false,
                editForm2Visible: false,
                editForm: null,
                eidtFormRules: {},
                projectListForOrder: [],//项目list
                currentProjectID: '',
                productDatas: [],//构件明细，存储在前端
                productTempDatas: [],//二级弹出框存储的列表
                addDisabled: false,
                formInline: {},
                onProductNoChange: false,
                onOrderNoChange: false,
                buildCodeArr:[],//楼栋数据
                floorNumArr:[],
                disableOthers:false,
                multipleSelectionAll:[],// 所有选中的数据包含跨页数据
                multipleSelection:[],//  当前页选中的数据
                multipleSelectionPageAll:{},//所有选中数据长度的集合
                multipleSelectionLength:0, //
                idKey: 'idKey',//  唯一标识
                show:'',//控制table是否被隐藏
                addedId:[],//一层弹窗已添加的ID
            }
        },
        mounted() {
            this.list();
        },
        methods: {
            clickRow1(row){
              this.$refs.firstPopup.toggleRowSelection(row)
            },
            clickRow2(row){
              this.$refs.table.toggleRowSelection(row)
            },
            setSelectRow() {
                if (!this.multipleSelectionAll || this.multipleSelectionAll.length <= 0) {
                    return;
                }
                // 标识当前行的唯一键的名称
                let idKey = this.idKey;
                let selectAllIds = [];
                let that = this;
                this.multipleSelectionAll.forEach(row=>{
                    selectAllIds.push(row[idKey]);
                })
                this.$refs.table.clearSelection();
                for(var i = 0; i < this.productTempDatas.length; i++) {  
                    if (selectAllIds.indexOf(this.productTempDatas[i][idKey]) >= 0) {
                        // 设置选中，记住table组件需要使用ref="table"
                        this.$refs.table.toggleRowSelection(this.productTempDatas[i], true);
                    }
                }
            } ,
            // 记忆选择核心方法
            changePageCoreRecordData () {
                // 标识当前行的唯一键的名称
                let idKey = this.idKey;
                let that = this;
                // 如果总记忆中还没有选择的数据，那么就直接取当前页选中的数据，不需要后面一系列计算
                if (this.multipleSelectionAll.length <= 0) {
                    this.multipleSelectionAll = this.multipleSelection;
                    return;
                }
                // 总选择里面的key集合
                let selectAllIds = [];
                this.multipleSelectionAll.forEach(row=>{
                    selectAllIds.push(row[idKey]);
                })
                let selectIds = []
                // 获取当前页选中的id
                this.multipleSelection.forEach(row=>{
                    selectIds.push(row[idKey]);
                    // 如果总选择里面不包含当前页选中的数据，那么就加入到总选择集合里
                    if (selectAllIds.indexOf(row[idKey]) < 0) {
                        that.multipleSelectionAll.push(row);
                    }
                })
                let noSelectIds = [];
                // 得到当前页没有选中的id
                this.productTempDatas.forEach(row=>{
                    if (selectIds.indexOf(row[idKey]) < 0) {
                        noSelectIds.push(row[idKey]);
                    }
                })
                noSelectIds.forEach(id=>{
                    if (selectAllIds.indexOf(id) >= 0) {
                        for(let i = 0; i< that.multipleSelectionAll.length; i ++) {
                            if (that.multipleSelectionAll[i][idKey] == id) {
                                // 如果总选择中有未被选中的，那么就删除这条
                                that.multipleSelectionAll.splice(i, 1);
                                break;
                            }
                        }
                    }
                })
            },
            // 二层弹窗选中事件  
            handleSelectionChangeCheckBox(val){
                let that = this
                //当前页面选中的数据赋值
                that.multipleSelection = val;
                // 存放当前页面选中的数据长度
                that.multipleSelectionPageAll['pageNum'+that.pageInfo2.currentPage] = that.multipleSelection.length;
                that.multipleSelectionLength = 0;
                let keys = Object.keys(that.multipleSelectionPageAll);
                keys.forEach(key=>{
                  that.multipleSelectionLength+=that.multipleSelectionPageAll[key]
                });
            },
            // 得到选中的所有数据
            // 再执行一次记忆勾选数据匹配，目的是为了在当前页操作勾选后直接获取选中数据
            getAllSelectionData () {
                this.changePageCoreRecordData();
                console.log(this.multipleSelectionAll)
            },
            tableRowClassName({row, rowIndex}) {
                if (row.urgOrderFlag == '加急'){
                    return 'urg-row'
                }
                return '';
            },
            disableOther(){
                if((this.formInline.productNo==undefined && this.formInline.productName==undefined) || 
                (this.formInline.productNo=='' && this.formInline.productName=='')|| 
                (this.formInline.productNo=='' && this.formInline.productName==undefined)|| 
                (this.formInline.productNo==undefined && this.formInline.productName=='')
                ){
                    this.disableOthers=false;
                }else{
                    this.disableOthers=true;
                }
                
            },
            bindBuildData(houseTypeId){
                this.orderProductCondition.buildCode = '';
                if(!houseTypeId){
                    return;
                }
                //初始化楼层和构件的下拉框
                orderModel.getBuildCodeArrByHouseTypeId(this.currentProjectID,houseTypeId).then(data=>{
                    this.buildCodeArr = data.entity;
                })
            },
            bindFloorData(buildCode){
                //console.log("buildCode: ",buildCode);
                this.orderProductCondition.floorNum = '';
                if(!buildCode){
                    return;
                }
                orderModel.getFloorNumArr(this.currentProjectID,buildCode).then(data=>{
                    this.floorNumArr = data.entity;
                }).catch(err=>{
                    //ignore
                })
            },
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD");
            },
            formatterDateS(row, column, cellValue, index){
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },

            onSubmit() {
                this.list();
            },
            showSubData(row, expandedRows) {
                let id = row.id;
                if(this.expands.some((e)=>e==row.id)){
                    this.expands=[]
                }else{
                    this.expands=[]
                    this.expands.push(row.id)
                }
                orderChooseModel.getOrderDetailsByOrderId(id).then(data => {
                    this.subDatas = data.entity;
                })
            },
            //从二级弹出框里选择构件添加到一级弹出框的表单表格中
            addSelectProduct2ProductDatas() {
                let idKeyArr = []
                this.changePageCoreRecordData();
                this.selected = this.multipleSelectionAll;
                if (this.selected.length == 0) {
                    this.$message('请选择添加项');
                    return;
                }
                this.selected.forEach((row) => {
                    let add = true;
                    this.editForm.productDatas.forEach(row0 => {
                        // if (row0.id == row.id && row0.buildingId ==row.buildingId &&   row0.floorId ==row.floorId ) {
                        //     //this.$message("请不要重复添加")
                        //     row.num = row.surplusCount;
                        //     this.editForm.productDatas.push(row);
                        //     add = false;
                        //     return;
                        // }
                        if(row.idKey===row0.idKey){
                          idKeyArr.push(row.idKey)
                        }
                    });
                    if (add) {
                        row.num = row.surplusCount;
                        this.editForm.productDatas.push(row);
                    }
                })
                // 一层弹窗中有二层重复选中的则刷新掉
                let that = this
                let editFormId = []
                that.editForm.productDatas.forEach(val=>{
                  editFormId.push(val.idKey)
                })
                idKeyArr.forEach(idKey=>{
                  that.editForm.productDatas.splice(editFormId.indexOf(idKey),1)
                })
                this.$refs.firstPopup.clearSelection();
                this.editForm2Visible = false;
                this.selected = [];
            },
            // 初始化添加构件的信息
            initialize(){
                this.multipleSelectionLength = 0;
                this.multipleSelectionPageAll = {};
                this.selected = [];
                this.multipleSelectionAll = [];
                this.multipleSelection = [];
                this.productTempDatas = [];
                this.pageInfo2.currentPage = 1;
                this.pageInfo2.pageSize = 50;
                // 获取所有一层弹窗数据的id
                let idArr = []
                this.editForm.productDatas.forEach(function(val){
                  idArr.push(val.idKey)
                })
                this.addedId = idArr;
                this.addProduct();
            },
            // 去重
            removeDuplicate(){
              console.log(this.productTempDatas)
              let that = this;
              for(let i=0;i<that.productTempDatas.length;i++){
                that.productTempDatas.disabled = true;
                that.addedId.forEach(val=>{
                  if(that.productTempDatas[i].idKey===val){
                    that.productTempDatas[i].disabled = false
                  }
                })
              }
              
            },
            //添加构件
            addProduct() {
                if (this.currentProjectID === '' || this.currentProjectID === undefined) {
                    this.$message("请先选择项目！")
                    return
                }
                //初始化户型数据
                orderModel.getHouseTypeArr(this.currentProjectID).then(data=>{
                    this.houseTypeArr = data.entity;
                })
                
                //把二级弹出框的列表赋值，根据当前项目名称
                this.orderProductCondition.pageInfo = this.pageInfo2;
                orderModel.getProductsByProjectId(this.currentProjectID, this.orderProductCondition).then(data => {
                    let en = data.entity.list
                    let tempArr= new Array();
                    console.log(en)
                    en.forEach(e=>{
                        // if(e.surplusCount > 0){
                        e.idKey = ""+e.id+e.floorId+e.buildingId
                        tempArr.push(e);
                        // }
                    })
                    this.productTempDatas = tempArr;
                    //console.log(tempArr)
                    this.pageInfo2.total = data.entity.total
                    if (tempArr.length ===0){
                        this.addDisabled = true;
                    }else{
                        this.addDisabled = false;
                    }
                    this.editForm2Visible = true;
                    setTimeout(()=>{
                        this.setSelectRow();
                    }, 200)
                    this.removeDuplicate()
                    //loading.close();
                }).catch(err => {
                    console.log(err)
                })
            },
            //删除勾选
            deletesProduct() {
                if (this.selected.length === 0) {
                    this.$message('请选择');
                    return;
                }
                this.selected.forEach((row) => {
                    for (let i = 0; i < this.editForm.productDatas.length; i++) {
                        if (this.editForm.productDatas[i].id === row.id) {
                            this.editForm.productDatas.splice(i, 1);
                        }
                    }

                })
            },
            //撤销
            updateStatusToCancel(row) {
                if (row.orderStatus === '新建') {

                    let option = '撤销';
                    this.$confirm('确认撤销?该操作不可回退!','提示',{
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        orderModel.updateStatus(row.id, option).then(data => {
                            if (data.status === 'success') {
                                this.$message("操作成功")
                                this.list()
                            } else {
                                Notification({
                                    message: '更新失败',
                                    type: 'warning'
                                })
                            }
                        })
                    }).catch(()=>{
                            
                    })
                    

                } else {
                    this.$message({
                        type: 'error',
                        message: '该订单不可撤销!'
                    })
                }
            },
            updateStatusToSubmit(row) {
                if (row.orderStatus === '新建' || row.orderStatus === '撤销') {
                    let option = '提交';
                    orderModel.updateStatus(row.id, option).then(data => {
                        if (data.status === 'success') {
                            this.$message("操作成功")
                            this.list()
                        } else {
                            Notification({
                                message: '更新失败',
                                type: 'warning'
                            })
                        }
                    })
                } else {
                    this.$message({
                        type: 'error',
                        message: '该订单状态不可提交'
                    })
                }
            },
            proNameChange(value) {
                //给项目相关的两个文本框赋值
                this.currentProjectID = value;
                for (let i = 0; i < this.projectListForOrder.length; i++) {
                    if (this.currentProjectID === this.projectListForOrder[i].id) {
                        this.editForm.customer = this.projectListForOrder[i].customer;
                        this.editForm.workPlace = this.projectListForOrder[i].workPlace;
                    }
                }

            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(!this.editForm.planDateFrom){
                            this.$message("请选择日期")
                            return
                        }
                        if(!this.editForm.planDateTo){
                            this.$message("请选择日期")
                            return
                        }
                        if(this.editForm.productDatas.length ==0){
                            this.$message("请添加构件")
                            return
                        }
                        let res = false;
                        this.editForm.productDatas.forEach(e=>{
                            if(parseInt(e.num) > parseInt(e.surplusCount)){
                                res = true;
                            }
                        })
                        if(res){
                            this.$message.warning("请检查数量大于剩余生产量的构件信息");
                            return;
                        }
                        if (this.mode === 'edit') {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            })
                            //console.log(" updat submit editForm : ", this.editForm)
                            orderModel.update(this.editForm)
                                .then((data) => {
                                    if (data.status === 'failed') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close()
                                        return
                                    } else {
                                        Notification({
                                            message: '编辑成功',
                                            type: 'success'

                                        })
                                    }
                                    loading.close()
                                    this.editFormVisible = false
                                    this.list();
                                }).catch(() => {

                                }
                            )
                        } else {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            })
                            orderModel.add(this.editForm)
                                .then((data) => {
                                    if (data.status === 'failed') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close()
                                        return
                                    } else {
                                        Notification({
                                            message: '新建成功',
                                            type: 'success'

                                        })
                                    }
                                    loading.close()
                                    this.editFormVisible = false
                                    this.list()
                                })

                        }

                    } else {
                        return false;
                    }
                });
            },
            addClick() {
                this.mode = 'add';
                this.currentProjectID = '';
                this.editForm = {
                    projectName: '',
                    customer: '',
                    workPlace: '',
                    planDateFrom: '',
                    planDateTo: '',
                    urgOrderFlag: '不加急',
                    remark: '',
                    productDatas: []

                }
                this.editFormVisible = true
            },
            editClick(row) {
                if (row.orderStatus === '新建' || row.orderStatus === '订单已撤销') {

                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    orderModel.getById(row.id).then(data => {
                        this.mode = 'edit';
                        this.editForm = data.entity;
                        //let tempData = this.editForm.productDatas;
                        for (let i = 0; i < this.projectListForOrder.length; i++) {
                            if (this.editForm.projectName === this.projectListForOrder[i].projectName) {
                                this.currentProjectID = this.projectListForOrder[i].id;
                            }
                        }
                        loading.close();
                        this.editFormVisible = true
                    }).catch((data) => {
                    })
                } else {
                    this.$message.warning("该订单状态不可编辑！")
                }

            },
            list() {
                this.$store.commit('startLoading')
                orderModel.findAllProjectList().then(data => {
                    this.projectListForOrder = data.entity;
                }).catch(() => {
                    this.$message('加载项目信息失败')
                })
                this.formInline.pageInfo = this.pageInfo;
                this.formInline.orderStatus = ["新建"];
                orderModel.findAllByFormInline(this.formInline).then(data => {
                    this.datas = data.entity.content;
                    //console.log("this.datas",this.datas)
                    this.pageInfo.total = data.entity.totalElements
                    this.$store.commit('endLoading')
                }).catch(err => {
                    console.log("err", err);
                })
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize = val;
                this.list()
            },
            // 分条
            handleSizeChange2(val) {
                this.multipleSelectionAll = [];
                this.pageInfo2.pageSize = val;
                //this.editForm2Visible = false;
                this.addProduct()
                //alert('调整每页的数量')
            },
            // 主页面 分条
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val;
                this.list()
            },
            // 一层弹窗选中事件
            handleSelectionChange(selected) {
                this.selected = selected
                this.selectedNumberCount()
            },
            // 翻页
            handleCurrentChange2(val) {
                this.pageInfo2.currentPage = val;
                // this.editForm2Visible = false;
                this.changePageCoreRecordData();
                this.addProduct()
                // this.getAllSelectionData()
            },
            // 查询
            onOrderProductSubmit(){
                // 所有选中的数据清空
                this.multipleSelectionAll = [];
                this.pageInfo2.currentPage = 1;
                this.addProduct()
                
            },
            // 一层弹窗数量变化的事件
            handleNumberChange(){
                this.selectedNumberCount()
            },
            // 选中的数量
            selectedNumberCount(){
                let that = this;
                that.selectedOrderNum = 0;
                that.selected.forEach((val)=>{
                  that.selectedOrderNum+=val.num*1
                })
            }
        },

    }
</script>
<style>
 .el-table .urg-row {
    background: rgba(103,194,58,.2);
  }

</style>