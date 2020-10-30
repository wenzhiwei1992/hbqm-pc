<template>
    <div>
        <el-form :inline="true" :model="formInline" class="demo-form-inline" size="mini">
            <el-form-item label="订单编号">
                <el-input v-model="formInline.orderNo" placeholder="订单编号"></el-input>
            </el-form-item>
            <el-form-item label="项目名称">
                <el-input v-model="formInline.projectName" placeholder="项目名称"></el-input>
            </el-form-item>
            <el-form-item label="订单状态" >
                <el-select v-model="formInline.orderStatus" placeholder="订单状态" multiple collapse-tags>
                    <el-option label="提交" value="提交"></el-option>
                    <el-option label="设置产线" value="设置产线"></el-option>
                    
                    <el-option label="提交" value="提交"></el-option>
                   <!-- <el-option label="工艺路径已确认" value="工艺路径已确认"></el-option>-->
                </el-select>
            </el-form-item>
            <el-form-item label="构件编码">
                <el-input v-model="formInline.productNo" placeholder="构件编码"></el-input>
            </el-form-item>
            <el-form-item label="构件名称">
                <el-input v-model="formInline.productName" placeholder="构件名称"></el-input>
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
                :default-sort = "{prop: 'dateModified', order: 'descending'}"
                :expand-row-keys="expands"
                row-key="id"
                height="500"
                style="width: 100%"

        >
            <!-- 这是点击小箭头出现的信息 -->
            <el-table-column type="expand">
                <template slot-scope="props">
                    <el-table
                            :data="subDatas"
                            @selection-change="handleSelectionChange"
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
                                prop="moldName"
                                label="模具名称"
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
                    label="序号"
                    width="70">
            </el-table-column>
            <el-table-column
                    prop="orderNo"
                    label="订单编号"
                    width="160"
                    sortable>
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
            <!-- <el-table-column
                    prop="dateCreated"
                    :formatter="formatterDate"
                    label="录入时间"
                    width="180">
            </el-table-column> -->
            <el-table-column
                    prop="orderQty"
                    label="订货总数"
                    width="60">
            </el-table-column>

            <el-table-column
                    prop="chooseTime"
                    :formatter="formatterDate"
                    label="提交时间"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="createdBy"
                    label="创建人"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="dateModified"
                    :formatter="formatterDate"
                    label="创建时间"
                    width="160">
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="320"
                    align="left">
                <template slot-scope="scope">
                    <el-button type="primary" size="mini" @click="setProdline(scope.row)">产线选择</el-button>
                    <el-button type="danger" size="mini" @click="orderReturnToMange(scope.row)">订单退回</el-button>
                    <el-button type="success" size="mini" @click="orderChoose(scope.row)">提交</el-button>
                    <!--<el-button type="success" size="mini" @click="setOrderTime(scope.row)">计划时间</el-button>-->
                    <!--<el-button type="danger" size="mini" @click="orderReturnChoose(scope.row)">退池</el-button>-->
                </template>
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle" >
            <el-col style="margin-top:15px">
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
        <el-dialog title="设置产线" :visible.sync="setFromVisible">
            <el-form v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px">

                <el-form-item label="产线" prop="lineName">
                    <el-select v-model="lineName" placeholder="" @change="lineChange">
                        <el-option
                                v-for="item in prodLines"
                                :key="item.id"
                                :label="item.lineName"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="工艺路径" prop="pathName">
                    <el-select v-model="pathName" placeholder="">
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

        <el-dialog title="安排计划时间" :visible.sync="setOrderDateVisible">
            <el-form v-if="orderDateForm" ref="orderDateForm" label-width="80px">
                <el-form-item label="计划开始时间">
                    <el-date-picker
                            v-model="orderDateForm.planDateFrom"
                            type="date"
                            placeholder="选择日期">
                    </el-date-picker>

                </el-form-item>
                <el-form-item label="计划结束时间">
                    <el-date-picker
                            v-model="orderDateForm.planDateTo"
                            type="date"
                            placeholder="选择日期">
                    </el-date-picker>

                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="setOrderDateVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveOrderDate()">确 定</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    import orderChooseModel from '../../../model/po/orderChoose'
    import {Notification} from 'element-ui'
    import moment from "moment";

    export default {
        data() {
            return {
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                   pageSize:this.GLOBAL.pageSize,
                    pageSizes:this.GLOBAL.pageSizes
                },
                datas: [],
                selected: [],
                expands:[],
                subDatas: [],
                setFromVisible: false,
                setOrderDateVisible:false,
                editForm: null,
                orderDateForm:{
                    id:'',
                    planDateFrom:'',
                    planDateTo:''
                },
                lineName: '',
                pathName: '',
                prodLines: [],//产线集合
                pathsByProdline: [],//根据产线查询工艺路径
                eidtFormRules: {},
                formInline:{
                    orderNo:'',
                    projectName:'',
                    orderStatus:'',
                    productNo:'',
                    productName:''
                },
                show:'',//控制table是否被隐藏
                orderChooseRow:{

                },
            }

        },
        mounted() {
            this.list()
        },
        methods: {
            formatterDate(row, column, cellValue, index){
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            onSubmit(){
                //this.$message.info("敬请期待")
                this.formInline.pageInfo = this.pageInfo;
                orderChooseModel.findAllByFormInline(this.formInline).then(data=>{
                    this.datas = data.entity.content;
                    this.pageInfo.total = data.entity.totalElements
                })
            },
            //设置计划生产时间起止
            saveOrderDate(){
                orderChooseModel.savePlanDate(this.orderDateForm).then(data=>{
                    if(data.status === 'success'){
                        Notification({
                            message:'设置成功',
                            type:'success'
                        })
                        this.setOrderDateVisible=false;
                        //更新订单状态为生产计划提交
                        let option = '提交';
                        orderChooseModel.updateStatus(this.orderDateForm.id,option).then(data=>{
                            if(data.status === 'success'){
                                //this.$message("操作成功")
                                this.list()
                            }else{
                                Notification({
                                    message:'订单状态更新失败',
                                    type:'warning'
                                })
                            }
                        })
                    }else {
                        Notification({
                            message:'设置失败！',
                            type:'warning'
                        })
                    }
                })
            },
            setOrderTime(row){
                console.log("row.id",row.id)
                if(row.orderStatus === '收池' || row.orderStatus === '提交' ){
                    const loading = this.$loading({
                        text: '加载中',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    })
                    //根据ID 获取到order 的两个日期
                    orderChooseModel.getPlanDateById(row.id).then(data=>{
                        this.orderDateForm.id = data.entity.id;
                        this.orderDateForm.planDateFrom = data.entity.planDateFrom;
                        this.orderDateForm.planDateTo = data.entity.planDateTo;
                        loading.close();
                        this.setOrderDateVisible= true;
                    })
                }else{
                    this.$message({
                        type:'warning',
                        message:'该订单状态不可设置生产计划日期!'
                    })
                }

            },
            orderReturnToMange(row){
                if(row.orderStatus === '提交' || row.orderStatus === '设置产线'){
                    let option = '新建';
                    orderChooseModel.updateStatus(row.id,option).then(data=>{
                        if(data.status === 'success'){
                            this.$message("操作成功")
                            this.list()
                        }else{
                            Notification({
                                message:'更新失败',
                                type:'warning'
                            })
                        }
                    })

                }else{
                    this.$message({
                        type:'warning',
                        message:'该订单状态不可退回!'
                    })
                }
            },
            orderReturnChoose(row){
                if(row.orderStatus === '提交'){

                    let option = '确认';
                    orderChooseModel.updateStatus(row.id,option).then(data=>{
                        if(data.status === 'success'){
                            this.$message("操作成功")
                            this.list()
                        }else{
                            Notification({
                                message:'更新失败',
                                type:'warning'
                            })
                        }
                    })

                }else{
                    this.$message({
                        type:'warning',
                        message:'该订单状态不可退池!'
                    })
                }
            },
            //设置产线
            setProdlineStatus(row){
                if(row.orderStatus === '提交'|| row.orderStatus ==='设置产线'){
                    //设置收池
                    let option = '设置产线';
                    orderChooseModel.updateStatus(row.id,option).then(data=>{
                        if(data.status === 'success'){
                            this.list()
                        }else{
                            Notification({
                                message:'更新状态失败,请联系管理员',
                                type:'warning'
                            })
                        }
                    });
                }else{
                    this.$message({
                        type:'warning',
                        message:'该订单状态不可设置产线!'
                    })
                }
            },
            //订单收池
            orderChoose(row){
                if(row.orderStatus === '设置产线'){
                    //通过订单明细的构件id ，数量绑定构件明细指定数量，把这几条记录的订单id 绑定
                    orderChooseModel.bindProductDetails(row.id).then(data=>{
                        if(data.status ==='success'){
                            //this.$message.success('构件明细绑定成功！');
                            //设置收池
                            let option = '提交';
                            orderChooseModel.updateStatus(row.id,option).then(data=>{
                                if(data.status === 'success'){
                                    this.$message.success("操作成功")
                                    this.list()
                                }else{
                                    Notification({
                                        message:'更新状态失败,请联系管理员',
                                        type:'warning'
                                    })
                                }
                            });
                        }else{
                            Notification({
                                type:'error',
                                message:data.msg
                            })
                        }
                    });
                }else{
                    this.$message({
                        type:'warning',
                        message:'该订单状态不可提交!'
                    })
                }
            },
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
            saveProdline(formName) {
                //把所有选择的构件都设置上
                let prodcuts = []
                this.selected.forEach((row) => {
                    prodcuts.push(row);
                })
                let pathName2 = '';
                let lineName2 = '';

                for (let i = 0; i < this.pathsByProdline.length; i++) {
                    if (this.pathsByProdline[i].id == this.pathName) {
                        pathName2 = this.pathsByProdline[i].pathName
                    }
                }

                for (let i = 0; i < this.prodLines.length; i++) {
                    if (this.prodLines[i].id == this.lineName) {
                        lineName2 = this.prodLines[i].lineName
                    }
                }
                for (let i = 0; i < prodcuts.length; i++) {
                    prodcuts[i].lineId = this.lineName; //实际值是id
                    prodcuts[i].pathId = this.pathName; //实际值是id
                    prodcuts[i].prodlineName = lineName2
                    prodcuts[i].pathName = pathName2
                }

                console.log("prodcuts : ",prodcuts)
                //保存产线路径信息
                orderChooseModel.saveProdlines(prodcuts).then(data => {
                    if (data.status === 'success') {
                        Notification({
                            message:'操作成功',
                            type:'success'
                        })
                        this.setProdlineStatus(this.orderChooseRow)
                        this.setFromVisible = false;
                    } else {
                        this.$message.error("操作失败！")
                    }
                })

            },
            setProdline(row) {
                if(row.orderStatus != '提交' && row.orderStatus != '收池' && row.orderStatus !='设置产线'){
                    this.$message.warning('该订单状态不可以设置产线信息');
                    return
                }
                //设置产线
                if (this.selected.length === 0) {
                    this.$message('请点击订单首列的箭头选择构件');
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
            list() {
                this.$store.commit('startLoading')

                // orderChooseModel.list(this.pageInfo).then(data => {
                //     this.datas = data.entity.content;
                //     this.pageInfo.total = data.entity.totalElements
                //     this.$store.commit('endLoading')
                // }).catch(err => {

                // })
                this.formInline.orderStatus=['提交','设置产线'];
                this.onSubmit();
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize = val;
                this.list()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val;
                this.list()
            }, handleSelectionChange(selected) {
                this.selected = selected
            },
        }


    }
</script>

<style scoped>

</style>
