<template>
    <div>
        <el-form :inline="true" :model="formInline" class="demo-form-inline" size="mini">
            <el-row>
                <el-form-item label="订单编号">
                    <el-input v-model="formInline.orderNo" placeholder="订单编号" :disabled="disableOthers" style="width: 160px"></el-input>
                </el-form-item>
                <el-form-item label="订单状态">
                    <el-select v-model="formInline.orderStatus" placeholder="订单状态" style="width: 160px" multiple collapse-tags :disabled="disableOthers">
                        <el-option label="新建" value="新建"></el-option>
                        <el-option label="提交" value="提交"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="开始日期">
                <el-date-picker
                        v-model="formInline.planDateFrom"
                        type="date"
                        style="width: 160px"
                        placeholder="选择日期">
                </el-date-picker>
                </el-form-item>
                <el-form-item label="结束日期">
                    <el-date-picker
                            v-model="formInline.planDateTo"
                            type="date"
                            style="width: 160px"
                            placeholder="选择日期">
                    </el-date-picker>
                </el-form-item>
            </el-row>
            <el-form-item label="项目名称">
                    <el-input v-model="formInline.projectName" placeholder="项目名称" :disabled="disableOthers" style="width: 160px"></el-input>
                </el-form-item>
            <el-form-item label="构件编码">
                <el-input v-model="formInline.productNo" placeholder="构件编码" style="width: 160px" @change="disableOther()" ></el-input>
            </el-form-item>
            <el-form-item label="构件名称">
                <el-select v-model="formInline.selectType" placeholder="类型" style="width: 70px" collapse-tags >
                    <el-option label="" value=""></el-option>
                    <el-option label="模糊" value="1"></el-option>
                    <el-option label="精确" value="2"></el-option>
                </el-select>
                <el-input v-model="formInline.productName" style="width: 85px"  :disabled="formInline.selectType=='' || formInline.selectType==null " placeholder="构件名称" @change="disableOther()"></el-input>
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
                height="450"
                style="width: 100%"
                :row-class-name="tableRowClassName"
        >
            <el-table-column type="expand">
                <template slot-scope="props">
                    <el-table
                            :data="subDatas"
                            @selection-change="handleSelectionChangeForPL"
                            size="mini"
                            stripe
                            border
                            style="width: 100%"
                    >
                        <el-table-column
                                type="selection"
                                width="80">
                        </el-table-column>
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
            <el-table-column
                    type="index"
                    label="序号">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="orderNo"
                    label="订单编号">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="orderStatus"
                    label="订单状态">
            </el-table-column>
            <el-table-column
                    prop="projectName"
                    label="项目名称">
            </el-table-column>
            <el-table-column
                    prop="orderQty"
                    label="订货总数">
            </el-table-column>
            <el-table-column
                    prop="planDateFrom"
                    label="开始日期"
                    :formatter="formatterDate">
            </el-table-column>
            <el-table-column
                    prop="planDateTo"
                    label="结束日期"
                    :formatter="formatterDate">
            </el-table-column>
            <el-table-column
                    prop="createdByName"
                    label="创建人">
            </el-table-column>
            <el-table-column
                    :formatter="formatterDateS"
                    prop="dateCreated"
                    label="创建时间">
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="260"
                    align="left"
                    fixed="right">
                <template slot-scope="scope">
                    <el-tooltip class="item" effect="light" content="编辑" placement="top">
                        <el-button icon="el-icon-edit" @click="editClick(scope.row)"></el-button>
                    </el-tooltip>
                    <el-tooltip class="item" effect="light" content="产线选择" placement="top">
                        <el-button type="success" icon="el-icon-position" @click="setProdline(scope.row)"></el-button>
                    </el-tooltip>
                    <el-tooltip class="item" effect="light" content="提交" placement="top">
                        <el-button type="primary" icon="el-icon-circle-check" @click="orderChoose(scope.row)"></el-button>
                    </el-tooltip>
                    <el-tooltip class="item" effect="light" content="撤销" placement="top">
                        <el-button type="danger" icon="el-icon-circle-close" @click="updateStatusToCancel(scope.row)"></el-button>
                    </el-tooltip>
                </template>
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle" >
            <el-col style="margin-top:15px">
                <el-button @click="addClick" type="primary">按构件类型新建</el-button>
                <el-button @click="addClickFB" type="success">按楼栋楼层新建</el-button>
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
        <el-dialog :title="mode==='add'?'新建':'编辑'" :visible.sync="editFormVisible" width="80%">
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
                                <el-input v-model="editForm.customer" disabled="disabled" style="width:175px"></el-input>
                            </el-form-item>

                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item label="工地名称">
                                <el-input v-model="editForm.workPlace" disabled="disabled" style="width:175px"></el-input>
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
                                        placeholder="选择日期" 
                                        style="width:175px">
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
                                        placeholder="选择日期"
                                        style="width:175px">
                                </el-date-picker>
                            </el-form-item>
                        </div>
                    </el-col>
                    <el-col :span="8">
                        <div class="grid-content bg-purple">
                            <el-form-item label="订单加急">
                                <el-select v-model="editForm.urgOrderFlag" value="" style="width:175px">
                                    <el-option label="不加急" value="不加急"></el-option>
                                    <el-option label="加急" value="加急"></el-option>
                                </el-select>
                            </el-form-item>
                        </div>
                    </el-col>
                </el-row>
                <!-- <el-row>
                    <el-col :span="8"><div class="grid-content bg-purple">
                        <el-form-item label="备注">
                            <el-input placeholder="请输入内容"
                                      v-model="editForm.remark">
                            </el-input>
                        </el-form-item>
                    </div></el-col>
                    <el-col :span="8"><div class="grid-content bg-purple-light"></div></el-col>
                </el-row> -->

                <!--添加构件-->
                <el-col style="margin-bottom:10px">
                    <el-button icon="el-icon-plus" @click="initialize" type="primary">添加</el-button>
                    <el-button icon="el-icon-minus" @click="deletesProduct" type="danger">删除</el-button>

                    <span style="margin-left:1%;" >已选择生产量(全选查看) : {{selectedOrderNum}}</span>
                    <span style="margin-left:45%;" >已选条数(全选查看) : {{selected.length}}</span>
                </el-col>
                
                <el-dialog append-to-body :visible.sync="editForm2Visible" width="80%">
                    <el-col>
                        <el-form :inline="true" :model="orderProductCondition" class="demo-form-inline" size="mini">
                            <el-form-item label="户型" v-if="editForm.ifNeedFB">
                                <el-select v-model="orderProductCondition.houseTypeId" placeholder="户型"  collapse-tags clearable @change="bindBuildData(orderProductCondition.houseTypeId)">
                                   <el-option 
                                    :key="item.id" 
                                    :value="item.id" 
                                    v-for="item in houseTypeArr"
                                    :label="item.name">
                                   </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="楼栋" v-if="editForm.ifNeedFB">
                                <el-select v-model="orderProductCondition.buildCode" placeholder="楼栋"  collapse-tags @change="bindFloorData(orderProductCondition.buildCode)" clearable>
                                   <el-option 
                                    :key="item.id" 
                                    :value="item.id" 
                                    v-for="item in buildCodeArr"
                                    :label="item.buildCode">
                                   </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="楼层" v-if="editForm.ifNeedFB">
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
                                <el-select v-model="orderProductCondition.selectType" placeholder="类型" style="width: 70px" collapse-tags  clearable>
                                    <el-option label="模糊" value="模糊"></el-option>
                                    <el-option label="精确" value="精确"></el-option>
                                </el-select>
                                <el-input v-model="orderProductCondition.productName" placeholder="构件名称" style="width: 60%" ></el-input>
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
                            height="400"
                            ref="table"
                            :row-class-name="tableRowClassName2"
                            @row-click="clickRow2"
                    >
                        <el-table-column
                                :selectable='isDisabled'
                                type="selection"
                                width="80">
                        </el-table-column>
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
                                prop="projectConcrete"
                                label="砼方量"
                                >
                        </el-table-column>
                        <el-table-column
                                prop="houseType"
                                v-if="editForm.ifNeedFB"
                                label="户型"
                                >
                        </el-table-column>
                        <el-table-column
                                prop="buildCode"
                                v-if="editForm.ifNeedFB"
                                label="楼栋"
                                >
                        </el-table-column>
                        <el-table-column
                                prop="floorNum"
                                v-if="editForm.ifNeedFB"
                                label="楼层"
                                >
                        </el-table-column>
                        <el-table-column
                                prop="surplusCount"
                                label="剩余生产量"
                                >
                        </el-table-column>
                        <el-table-column
                                prop="moldName"
                                label="模具"
                                >
                        </el-table-column>
                    </el-table>
                    <!--分页-->
                    <el-row type="flex" justify="space-around" align="middle" >
                        <el-col>
                            已选择选择构件数 : {{multipleSelectionLength}}
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
                            type="selection">
                    </el-table-column>
                    <el-table-column
                            prop="productNo"
                            label="构件编码">
                    </el-table-column>
                    <el-table-column
                            prop="productName"
                            label="构件名称">
                    </el-table-column>
                    <el-table-column
                            prop="num"
                            label="数量">
                        <template slot-scope="scope">
                            <!-- <el-input v-model="scope.row.num"  type="number" :min="1"  onkeyup="value=value.replace(/[^\d]/g,'')"
                            @input="handleNumberChange"></el-input> -->
                            
                        <div class="el-input el-input--mini">
                           <input class="el-input__inner" v-model="scope.row.num" :max="scope.row.surplusCount" type="number"
                                   :min="1" onkeyup="value=value.replace(/[^\d]/g,'')"   @input.stop="handleNumberChange"/> 
                        </div>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="surplusCount"
                            label="最大生产量"
                            v-if="editForm.ifNeedFB">
                    </el-table-column>
                    <el-table-column
                            prop="surplusCount"
                            label="剩余生产量"
                            v-if="editForm.ifNeedFB == false">
                    </el-table-column>
                    <el-table-column
                                prop="totalNum"
                                v-if="editForm.ifNeedFB"
                                label="需求量"
                                >
                    </el-table-column>
                    <el-table-column
                            prop="projectConcrete"
                            label="砼方量">
                    </el-table-column>
                    <el-table-column
                            prop="buildCode"
                            v-if="editForm.ifNeedFB"
                            label="楼栋">
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            v-if="editForm.ifNeedFB"
                            label="楼层">
                    </el-table-column>
                    <el-table-column
                            prop="moldName"
                            label="模具">
                    </el-table-column>

                </el-table>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog title="产线选择" :visible.sync="setFromVisible" width="30%">
            <el-form v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px">

                <el-form-item label="产线" prop="lineId">
                    <el-select v-model="lineId" placeholder="" @change="lineChange">
                        <el-option
                                v-for="item in prodLines"
                                :key="item.id"
                                :label="item.lineName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="工艺路径" prop="pathId">
                    <el-select v-model="pathId" placeholder="">
                        <el-option
                                v-for="item in pathsByProdline"
                                :key="item.id"
                                :label="item.pathName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="setFromVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveProdline()">确 定</el-button>
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
                    pageSize: 20 ,
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
                editForm: {
                    ifNeedFB:false,
                },
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
                prodLines: [],//产线集合
                pathsByProdline: [],//根据产线查询工艺路径
                setFromVisible: false,//设置产线页面的显示与隐藏
                lineId:'',
                pathId:'',
                selectedFoPL:[],//设置产线使用的勾选数组
                addedId:[],//一层弹窗已添加的ID
            }
        },
        mounted() {
            this.list();
        },
        methods: {
            addClickFB(){
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
                    productDatas: [],
                    ifNeedFB:true

                }
                this.editFormVisible = true
            },
            // 一层弹窗已添加的构件 禁止勾选
            isDisabled(row,index){
              if(row.existing==='true'){
                return false
              }else{
                return true
              }
            },
            // 一层弹窗已添加的构件 显示已添加的样式
            tableRowClassName2(row,index){
              if(row.row.existing==='true'){
                return 'stopclick'
              }else{
                return ''
              }
            },
            //订单收池
            orderChoose(row){
                if(row.orderStatus === this.GLOBAL.orderStatus.settingLine){
                    let option = this.GLOBAL.orderStatus.confirm;
                    orderChooseModel.updateStatus(row.id,option).then(data=>{
                        if(data.status === 'success'){
                            this.$message.success(this.GLOBAL.MSG.UPDATE_SUCCESS)
                            this.list()
                        }else{
                            Notification({
                                message:this.GLOBAL.MSG.UPDATE_ERR,
                                type:'warning'
                            })
                        }
                    });
                }else{
                    this.$message({
                        type:'warning',
                        message:this.GLOBAL.ORDER_ERR_MSG.NOT_ALLOWED_CONFIRM
                    })
                }
            },
            //设置产线状态
            setProdlineStatus(row){
                if(row.orderStatus === this.GLOBAL.orderStatus.newAdd|| row.orderStatus ===this.GLOBAL.orderStatus.settingLine){
                    //设置收池
                    let option = this.GLOBAL.orderStatus.settingLine;
                    orderChooseModel.updateStatus(row.id,option).then(data=>{
                        if(data.status === 'success'){
                            this.list()
                        }else{
                            Notification({
                                message:this.GLOBAL.MSG.UPDATE_ERR,
                                type:'warning'
                            })
                        }
                    });
                }else{
                    console.log('this.GLOBAL.ORDER_ERR_MSG.NOT_ALLOWED_SETTING_LINE',this.GLOBAL.ORDER_ERR_MSG.NOT_ALLOWED_SETTING_LINE);
                    this.$message({
                        type:'warning',
                        message:this.GLOBAL.ORDER_ERR_MSG.NOT_ALLOWED_SETTING_LINE
                    })
                }
            },
            /**
             * 保存选择的产线
             */
            saveProdline(formName) {
                //把所有选择的构件都设置上
                let prodcuts = []
                this.selectedFoPL.forEach((row) => {
                    prodcuts.push(row);
                })
                let pathName2 = '';
                let lineName2 = '';

                for (let i = 0; i < this.pathsByProdline.length; i++) {
                    if (this.pathsByProdline[i].id == this.pathId) {
                        pathName2 = this.pathsByProdline[i].pathName
                    }
                }

                for (let i = 0; i < this.prodLines.length; i++) {
                    if (this.prodLines[i].id == this.lineId) {
                        lineName2 = this.prodLines[i].lineName
                    }
                }
                for (let i = 0; i < prodcuts.length; i++) {
                    prodcuts[i].lineId = this.lineId; //实际值是id
                    prodcuts[i].pathId = this.pathId; //实际值是id
                    prodcuts[i].prodlineName = lineName2
                    prodcuts[i].pathName = pathName2
                }

                //console.log("prodcuts : ",prodcuts)
                //return;
                //保存产线路径信息
                orderChooseModel.saveProdlines(prodcuts).then(data => {
                    if (data.status === 'success') {
                        Notification({
                            message:this.GLOBAL.MSG.UPDATE_SUCCESS,
                            type:'success'
                        })
                        this.setProdlineStatus(this.orderChooseRow)
                        this.setFromVisible = false;
                    } else {
                        this.$message.error(this.GLOBAL.MSG.UPDATE_ERR)
                    }
                })

            },
            //设置产线弹出框显示
            setProdline(row) {
                if(row.orderStatus != this.GLOBAL.orderStatus.newAdd 
                && row.orderStatus !=this.GLOBAL.orderStatus.settingLine){
                    this.$message.warning(this.GLOBAL.ORDER_ERR_MSG.NOT_ALLOWED_SETTING_LINE);
                    return
                }
                //设置产线
                if (this.selectedFoPL.length === 0) {
                    this.$message(this.GLOBAL.MSG.SELECTED_ERR);
                    return;
                }
                //加载产线数据，显示产线选择框
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                })
                this.editForm = {
                    prodline: {
                        lineName: '',
                    },
                    path: {
                        pathName: '',
                    }
                }
                this.orderChooseRow = row;
                orderChooseModel.getAllProdLines().then(data => {
                    this.prodLines = data.entity;
                    loading.close();
                    this.setFromVisible = true
                })

            },
            //设置产线
            lineChange(value) {
                const loading = this.$loading({
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                })
                this.pathName = '';
                orderChooseModel.getPathsByProdlineId(value).then(data => {
                    this.pathsByProdline = data.entity;
                    //console.log("this.pathsByProdline", this.pathsByProdline)
                    loading.close();
                });
            },
            clickRow1(row){
              this.$refs.firstPopup.toggleRowSelection(row)
            },
            clickRow2(row){
              console.log(row)
              if(row.existing==='false'){
                this.$refs.table.toggleRowSelection(row)
              }
              
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
                    this.$message(this.GLOBAL.ORDER_ERR_MSG.SELECTED_ERR);
                    return;
                }
                this.selected.forEach((row) => {
                    let add = true;
                    this.editForm.productDatas.forEach(row0 => {
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
                this.pageInfo2.pageSize = 20;
                // 获取所有一层弹窗数据的id
                let idArr = []
                this.editForm.productDatas.forEach(val=>{
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
                    that.productTempDatas[i].existing = 'true'
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
                if(this.editForm.ifNeedFB){
                    //初始化户型数据
                    orderModel.getHouseTypeArr(this.currentProjectID).then(data=>{
                        this.houseTypeArr = data.entity;
                    })
                }
                //把二级弹出框的列表赋值，根据当前项目名称
                this.orderProductCondition.pageInfo = this.pageInfo2;
                this.orderProductCondition.ifNeedFB = this.editForm.ifNeedFB;
                //console.log("this.orderProductCondition",this.orderProductCondition)
                orderModel.getProductsByProjectIdForNoSCPage(this.currentProjectID, this.orderProductCondition).then(data => {
                    let en = data.entity.list
                    let tempArr= new Array();
                    console.log(en)
                    en.forEach(e=>{
                        // if(e.surplusCount > 0){
                        e.idKey = ""+e.id+e.floorId+e.buildingId
                        e.existing = 'false'
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
                    this.$message(this.GLOBAL.ORDER_ERR_MSG.SELECTED_ERR);
                    return;
                }
                this.selected.forEach((row) => {
                    for (let i = 0; i < this.editForm.productDatas.length; i++) {
                        if (this.editForm.productDatas[i].idKey === row.idKey) {
                            this.editForm.productDatas.splice(i, 1);
                        }
                    }

                })
            },
            //撤销
            updateStatusToCancel(row) {
                if (row.orderStatus === this.GLOBAL.orderStatus.newAdd || row.orderStatus === this.GLOBAL.orderStatus.settingLine) {

                    let option = this.GLOBAL.orderStatus.cancel;
                    this.$confirm(this.GLOBAL.MSG.CANCEL_NOTICE,'提示',{
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        orderModel.updateStatus(row.id, option).then(data => {
                            if (data.status === 'success') {
                                this.$message(this.GLOBAL.MSG.UPDATE_SUCCESS)
                                this.list()
                            } else {
                                Notification({
                                    message: this.GLOBAL.MSG.UPDATE_ERR,
                                    type: 'warning'
                                })
                            }
                        })
                    }).catch(()=>{
                            
                    })
                    

                } else {
                    this.$message({
                        type: 'error',
                        message: this.GLOBAL.ORDER_ERR_MSG.NOT_ALLOWED_CANCEL
                    })
                }
            },
            updateStatusToSubmit(row) {
                if (row.orderStatus === this.GLOBAL.orderStatus.newAdd || row.orderStatus === '撤销') {
                    let option = '提交';
                    orderModel.updateStatus(row.id, option).then(data => {
                        if (data.status === 'success') {
                            this.$message(this.GLOBAL.MSG.UPDATE_SUCCESS)
                            this.list()
                        } else {
                            Notification({
                                message: this.GLOBAL.MSG.UPDATE_ERR,
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
                        //用户再次确认
                        if(res){
                            this.$confirm('包含数量大于剩余生产量的构件信息, 是否继续?', '提示', {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            }).then(() => {
                                this.$message({
                                    type:'info',
                                    message : this.GLOBAL.MSG.BIZ_NOT_ALLOWED
                                })
                            }).catch(() => {
                                Notification({
                                    type:warning,
                                    message:"取消"
                                })
                            });
                        }else {
                            if (this.mode === 'edit') {
                                const loading = this.$loading({
                                    lock: true,
                                    text: 'Loading',
                                    spinner: 'el-icon-loading',
                                    background: 'rgba(0, 0, 0, 0.7)'
                                })
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
                                                message: this.GLOBAL.MSG.UPDATE_SUCCESS,
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
                    productDatas: [],
                    ifNeedFB:false

                }
                this.editFormVisible = true
            },
            editClick(row) {
                if (row.orderStatus === this.GLOBAL.orderStatus.newAdd ) {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    orderModel.getById(row.id).then(data => {
                        this.mode = 'edit';
                        this.editForm = data.entity;
                        this.editForm.productDatas.forEach(e=>{
                          e.idKey = ""+e.id+e.floorId+e.buildingId
                        })
                        //console.log(this.editForm)
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
                    this.$message.warning(this.GLOBAL.ORDER_ERR_MSG.NOT_ALLOWED_EDIT)
                }

            },
            list() {
                this.$store.commit('startLoading')

                orderModel.findAllProjectList().then(data => {
                    this.projectListForOrder = data.entity;
                }).catch(() => {

                })
                this.formInline.pageInfo = this.pageInfo;
                //获取menu名称，用于 后台api判断，避免api共用导致冲突
                let active=this.$store.state.menuActiveName
                this.formInline.menuActiveName =  active
                orderModel.findAllByFormInline(this.formInline).then(data => {
                    this.datas = data.entity.content;
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
            //设置产线使用的勾选
            handleSelectionChangeForPL(selected){
                this.selectedFoPL = selected
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

  .noti-col{
      background:red;
  }

  tbody tr.stopclick{
    color:rgba(0,0,0,0.2);
    background:#fff;
    /* text-decoration:line-through; */
  }
</style>
