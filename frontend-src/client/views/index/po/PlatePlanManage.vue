<template>

    <div>
        <el-form :inline="true" :model="formInline" class="demo-form-inline" size="mini">
            <el-form-item label="产线">
                <el-select v-model="formInline.lineName" placeholder="产线" collapse-tags clearable @change="onSubmit">
                    <el-option v-for="pd in prodlines" :value="pd.id" :label="pd.lineName" :key="pd.id" >
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="工序">
                <el-input v-model="formInline.currentProcess" placeholder="工序"></el-input>
            </el-form-item>
            <el-form-item label="模台号" >
                <el-input v-model="formInline.plateNo" placeholder="模台号"></el-input>
            </el-form-item>
           <el-form-item label="模台状态">
                <el-select v-model="formInline.plateStatus" placeholder="模台状态" collapse-tags clearable >
                    <el-option label="空闲" value="空闲"></el-option>
                    <el-option label="占用" value="占用"></el-option>
                </el-select>
            </el-form-item> 
            <el-form-item>
                <el-button type="primary" @click="onSubmit">查询</el-button>
            </el-form-item>
        </el-form>

        <el-table
                :data="datas"
                size="mini"
                stripe
                border
                style="width: 100%"
        >
            <!--这是点击小箭头出现的信息-->

            <el-table-column
                    type="index"
                    label="序号"
                    width="70">
            </el-table-column>

            <el-table-column
                    prop="lineName"
                    label="产线"
                    width="160px">
            </el-table-column>
            <el-table-column
                    prop="plateNo"
                    label="模台号"
                    width="100px">
            </el-table-column>
            <el-table-column
                    label="模台状态"
                    width="100px">
                <template slot-scope="scope">
                    <span style="margin-left: 10px" :class="scope.row.plateStatus == '占用' ? 'plate-green':''">{{ scope.row.plateStatus }}</span>
                </template>
            </el-table-column>
<!--            <el-table-column-->
<!--                    label="组模方案">-->
<!--                <template slot-scope="scope">-->
<!--                    <el-popover trigger="click" placement="top-end" :visible-arrow="false">-->
<!--                        <el-table :data="scope.row.productFoursSplit" >-->
<!--                            <el-table-column width="280px" property="productName" label="待计划排产构件" sortable></el-table-column>-->
<!--                        </el-table>-->
<!--                        <div slot="reference" class="name-wrapper">-->
<!--                            <el-button icon="el-icon-search" round ></el-button>-->
<!--                        </div>-->
<!--                    </el-popover>-->
<!--                </template>-->
<!--            </el-table-column>-->
            <el-table-column
                    prop="productFour"
                    label="组模方案">
            </el-table-column>
            <el-table-column
                    prop="products"
                    label="构件明细"
                    width="120px">
                <template slot-scope="scope">
                    <el-popover trigger="click" placement="top-end" :visible-arrow="false">
                        <el-table :data="scope.row.productsSplit" >
                            <el-table-column width="280px" property="productName" label="已计划排产构件" sortable></el-table-column>
                        </el-table>
                        <div slot="reference" class="name-wrapper">
                            <el-button icon="el-icon-search" round ></el-button>
                        </div>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column
                    label="操作"
                    align="left"
                    width="140px">
                <template slot-scope="scope">
                    <el-button size="mini" icon="el-icon-edit" @click="editClick(scope.row)"></el-button>
                    <el-button type="primary" size="mini"  icon="el-icon-circle-check" @click="planClick(scope.row)"></el-button>
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
        <!--计划排产弹框-->
        <el-dialog :visible.sync="planFormVisible" width="60%">
            <plan-edit
                :plan-form="planForm"
                :team-infos="teamInfos"
                :items="items"
                @planForm-finish="planFormFinish"
                @planForm-cancel="planFormCancel"
            >
            </plan-edit>

        </el-dialog>

        <el-dialog :title="mode==='add'?'新建':'编辑'" :visible.sync="editFormVisible" width="80%" :inline="true">
            <el-form :inline="true" :model="editForm" v-if="editForm" :rules="eidtFormRules" ref="editForm" label-width="80px">
                <el-row>
                    <el-form-item label="模台号" prop="plateNo" >
                        <el-input v-model="editForm.plateNo" disabled="disabled" ></el-input>
                    </el-form-item>
                    <el-form-item label="项目名称" prop="projectId">
                        <el-select v-model="editFormSearch.projectId" placeholder="请选择" clearable  
                            style="width:150px" @change="changeProjectSelect" >
                            <el-option
                                    v-for="item in projects"
                                    :key="item.id"
                                    :label="item.projectName"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="楼栋" prop="buildId">
                        <el-select style="width:150px" v-model="editFormSearch.buildId" size="mini" clearable
                                @change="changeBuildingSelect">
                            <el-option
                                    v-for="item in buildings"
                                    :key="item.id"
                                    :label="item.buildCode"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="楼层" prop="floorId">
                        <el-select style="width:150px" v-model="editFormSearch.floorId" clearable size="mini">
                            <el-option
                                    v-for="item in floors"
                                    :key="item.id"
                                    :label="item.floorNum"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-row>
                <el-row>
                    <el-form-item label="构件类型" >
                        <el-select
                            style="width:160px"
                            v-model="editFormSearch.typeId"
                            filterable
                            clearable
                            remote
                            reserve-keyword
                            placeholder="输入构件类型自动检索"
                            @change="editFormSearchMethodChange"
                            :remote-method="remoteMethod"
                            :loading="loading">
                            <el-option
                                v-for="item in types"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="构件名称" prop="productName">
                        <el-input v-model="editFormSearch.productName" placeholder="构件名称" style="width:150px"></el-input>
                    </el-form-item>
                    <el-form-item label="构件编码" prop="productNo">
                        <el-input v-model="editFormSearch.productNo" placeholder="构件编码" style="width:150px"></el-input>
                        <el-button type="primary" @click="editFormSearchMethod">查询</el-button>
                    </el-form-item>

                </el-row>
                <el-row >
                    <el-col :span="24">
                        <span style="width:80px;margin-left:13px;margin-right: 7px;">组模方案</span>
                        <el-select
                                @remove-tag="removeTag"
                                v-model="editForm.productsLabel" placeholder="请选择" value="" multiple style="width: 80%">
                        </el-select>
                        <el-button type="primary" @click="clearTags" style="margin-buttom:1rem">清空</el-button>
                    </el-col>
                </el-row>
                <el-table
                        :data="productData"
                        @row-click="pushProductData"
                        size="mini"
                        stripe
                        border
                        style="width: 100%"
                    >
                    <el-table-column
                            prop="productNo"
                            label="构件编码"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="productName"
                            label="构件名称"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="productDetailId"
                            label="明细ID"
                            width="80">
                    </el-table-column>
                    <el-table-column
                            prop="buildCode"
                            label="楼栋"
                            width="80">
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            label="楼层"
                            width="80">
                    </el-table-column>
                    <el-table-column
                            prop="typwpeId"
                            label="构件类型">
                    </el-table-column>
                    <el-table-column
                            prop="productLen"
                            label="长">
                    </el-table-column>
                    <el-table-column
                            prop="productHeight"
                            label="高">
                    </el-table-column>
                    <el-table-column
                            prop="productVol"
                            label="面积">
                    </el-table-column>
                </el-table>
                <el-row type="flex" justify="space-around" align="middle" style="margin-top:15px">
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
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editFormSubmmit('editForm')">确 定</el-button>
            </div>
        </el-dialog>

    </div>
</template>
<script>
    import combModel from '../../../model/po/comb'
    import pathModel from '../../../model/prodline/path'
    import projectModel from '../../../model/project/project'
    import buildModel from '../../../model/project/building'
    import floorModel from '../../../model/project/floor'
    import PlanEdit from  './PlanEdit.vue'
    import {Notification} from 'element-ui'
    import moment from "moment";

    export default {
        components:{
            'plan-edit':PlanEdit,
        },
        data() {
            return {
                loading: false,
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: 10 ,
                    pageSizes:this.GLOBAL.pageSizes
                },
                pageInfo2:{
                    currentPage: 1,
                    total: 0,
                    pageSize: 10,
                    pageSizes:this.GLOBAL.pageSizes
                },
                datas: [],
                selected: [],
                mode: '',
                editFormTitle: '',
                editFormVisible: false,
                editForm: null,
                eidtFormRules: {},
                productData:[],
                planFormVisible:false,
                planForm:null,
                items:[],
                formInline:{},
                isChecked:'',
                teamInfos:[],
                prodlines:[],
                currentLineId:'',
                popDatas:[],
                editFormSearch:{
                    productNo:'',
                    productName:''
                },
                projects:[],
                buildings:[],
                floors:[],
                types:[]

            }
        },
        mounted() {
            this.pageableList();
            projectModel.findAllProjects().then((data) => {
                this.projects = data.entity
            })
        },
        methods: {
            remoteMethod(query) {
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
            changeProjectSelect(){
                if (this.editFormSearch.projectId == undefined) {
                    this.editFormSearch.projectId = 0
                }
                this.editFormSearch.buildId = ''
                this.buildings = []
                buildModel.findByProjectId(this.editFormSearch.projectId).then((data)=>{
                    this.buildings = data.entity
                })
            },
            changeBuildingSelect() {
                if (this.editFormSearch.buildId == undefined) {
                    this.editFormSearch.buildId = 0
                }
                this.floors = []
                floorModel.findByBuildingIdAndProjectId(this.editFormSearch.buildId, this.editFormSearch.projectId).then((data) => {
                    this.floors = data.entity
                })
            },
            editFormSearchMethod(){
                this.editFormSearchMethodChange();
            },
            clearTags(){
                this.editForm.productsLabel=[];
                this.editForm.productIds = [];
                this.editFormSearchMethodChange();
            },
            formatterDate(row, column, cellValue, index){
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            onSubmit(){
                this.formInline.pageInfo = this.pageInfo;
                combModel.findAllByFormInline(this.formInline).then(data=>{
                    this.datas = data.entity.content;
                    this.pageInfo.total = data.entity.totalElements;
                    for(let i =0 ;i< this.datas.length;i++){
                        //console.log("this.datas i ", this.datas[i]);
                        if(this.datas[i].productsForM2){
                            if(this.datas[i].productsForM2.length>0){
                                this.datas[i].plateStatus = '定模占用';
                            }
                        }
                        
                    }
                })
            },
            removeTag(val){
                //console.log("val",val);
                let index = -1;
                let length = this.editForm.productIds.length;
                for(let i=0; i < length ; i++ ){
                    let item = this.editForm.productIds[i];
                    //console.log("item",item)
                    let obj = JSON.parse(item);
                    let cpValue =obj.productName + "-"+ obj.productDetailId
                    if(cpValue == val){
                        index = i;
                        break;
                    }
                }
                this.editForm.productIds.splice(index,1);
                //console.log("this.editForm.productIds",this.editForm.productIds);
                //let index = this.editForm.productsLabel.indexOf(val);
            },
            planFormFinish(){
                this.planFormVisible=false;
                this.pageableList();
            },
            planFormCancel(){
                this.planFormVisible=false;
            },
            pushProductData(row, event, column){
                let productName = "【"+ row.productName+'-'+row.productDetailId+ "】";
                if(this.editForm.productsLabel.indexOf(productName) === -1){
                    let obj = {
                        productName:'', //构件名称
                        productDetailId:'', //构件明细ID

                    }
                    obj.productName = productName;
                    obj.productDetailId = row.productDetailId;
                    this.editForm.productsLabel.push(productName);

                    //从列表中删除一行记录
                    let index = this.productData.indexOf(row);
                    this.productData.splice(index,1); 
                    this.pageInfo2.total-=1;
                }else{
                    this.$message.warning("该构件已添加过")
                    return
                }
                var r = JSON.stringify(row);
                //console.log(r)
                //保存构件信息的实际数据
                this.editForm.productIds.push(r);
            },
            //自由计划
            planFreeClick(row){},
            planClick: function (row) { //组模计划
                //提前加载teamInfos
                combModel.getAllTeamInfos().then(data=>{
                    this.teamInfos = data.entity;
                });
                this.items = [];
                this.planForm = {
                    combId:row.id,
                    period: '1',
                    predictDate: new Date(),
                    lineId:'',
                    lineName : '',
                    customData: [],
                    checked: [],
                };
                this.planForm.lineName  = row.lineName;
                this.planForm.lineId = row.lineId;
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                let orderStatus = '提交';
                combModel.getProductsByStatus(orderStatus,row.lineId, this.pageInfo2).then(data => {
                    this.pageInfo2.total = data.entity.totalCount;
                    if (this.pageInfo2.total == 0) {
                        Notification({
                            message: '请检查项目构件需求量',
                            type: 'warning'
                        });
                    }
                });
                loading.close();

                //检查
                let ids = row.productIds;
                //console.log("ids",ids)
                if(ids!=null){
                    if(ids.length==0 ){
                        ids = [];
                        this.$message.warning('请先编辑组模方案');
                        return
                    }
                }else{
                    this.$message.warning('请先编辑组模方案');
                    return
                }
                
                let dd = JSON.parse(ids);
                let pds = "";
                for (let i = 0; i < dd.length; i++) {
                    pds = pds + dd[i].productDetailId + ";"; //获取所有的构件明细ID，检查是否已经排过计划
                }
                //检查模台上构件是否已在生产计划中
                combModel.checkProductOnPlate(pds).then(data => {
                    this.isChecked = data.status;
                    if (this.isChecked === 'failed') {
                        Notification({
                            message: '请检查模台上构件是否已排计划',
                            type: 'warning'
                        });
                    }else {

                        row.rowNames = [];
                        row.items2=[];
                        for (let i = 0; i < dd.length; i++) {
                            let temp = {
                                index: 0,
                                productId: '',
                                orderid: '',
                                productDetailId: '',
                                plateId:'',
                                platePlanId:row.id,
                                label: '',
                                name: ''
                            };
                            temp.index = i;
                            temp.name = dd[i].productName + '-' + dd[i].productDetailId;
                            temp.label = '构件' + (i + 1) + ':明细ID' + dd[i].productDetailId;
                            temp.productId = dd[i].productId;
                            temp.orderid = dd[i].orderid;
                            temp.productDetailId = dd[i].productDetailId;
                            temp.plateId= row.plateId;
                            temp.platePlanId=row.id;
                            this.items.push(temp);
                            let nameIndex = name + i;
                            row.rowNames.push(nameIndex);
                            row.rowNames[i] = temp.name;
                            row.num = 1;

                            row.items2.push(temp);
                        }
                        this.planForm.customData.push(row);//给模台号复制
                        this.planFormVisible = true;
                    }
                });

            },
            pageableList() {
                this.$store.commit('startLoading');
                //加载查询条件，所有产线集合
                pathModel.getAllProdLines().then(data=>{
                    this.prodlines  = data.entity;
                }).catch((err)=>{
                    console.log("load selectbox data err!",err)
                });
                this.formInline.pageInfo = this.pageInfo;
                combModel.findAllByFormInline(this.formInline).then(data=>{
                    this.datas = data.entity.content;
                    this.pageInfo.total = data.entity.totalElements;
                    for(let i =0 ;i< this.datas.length;i++){
                        if(this.datas[i].productsForM2){
                            if(this.datas[i].productsForM2.length>0){
                                this.datas[i].plateStatus = '定模占用';
                            }
                        }
                    }
                })
            },
            editClick(row) {
                if(row.productsForM2==null){
                    //ingore
                }else if(row.productsForM2.length>0){
                    this.$message("该模台已被定模计划使用！");
                    return;
                }
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                let orderStatus='提交';
                this.currentLineId = row.lineId;
                combModel.getProductsByStatus(orderStatus,row.lineId,this.pageInfo2).then(data=>{
                    this.productData= data.entity.list;
                    this.pageInfo2.total = data.entity.totalCount;
                    loading.close();
                    combModel.getById(row.id).then(data => {
                        this.mode = 'edit';
                        this.editForm = data.entity;
                        try {
                            if(this.editForm.products.length>0){
                                this.editForm.productsLabel=this.editForm.products.split(";");
                                this.editForm.productsLabel.pop();//去除最后一个拆分的空字符串
                            }else{
                                this.editForm.productsLabel =[];
                            }
                            if(this.editForm.productIds.length >0){
                                let jsons =  JSON.parse(data.entity.productIds);
                                this.editForm.productIds=[];
                                for(let i =0;i<jsons.length;i++){
                                    let jsonObj = jsons[i];//已在待排产中的构件，如果需要其他模台也不显示，需要在构件明细里面加个"待排产"状态进行过滤
                                    let index = this.productData.indexOf(row);
                                    this.productData.splice(index,1); 
                                    this.pageInfo2.total-=1;
                                    this.editForm.productIds.push(JSON.stringify(jsons[i]));
                                }
                            }else{
                                this.editForm.productIds = [];
                            }
                            
                        } catch (error) {
                            this.editForm.productsLabel =[];
                            this.editForm.productIds = [];
                        }
                        this.editFormVisible = true
                    }).catch((data) => {
                        console.error(data)
                    })
                });

            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.mode === 'edit') {
                            combModel.update(this.editForm)
                                .then((data) => {
                                    if (data.status === 'failed') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        });
                                        return
                                    }
                                    Notification({
                                        message: '编辑成功',
                                        type: 'success'

                                    });
                                    this.editFormVisible = false;
                                    this.pageableList();
                                }).catch(() => {

                                }
                            )
                        }
                    } else {
                        return false;
                    }
                });
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize = val;
                this.pageableList()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val;
                this.pageableList()
            },
            handleSizeChange2(val) {
                this.pageInfo2.pageSize = val;
                this.editFormSearchMethodChange();
            },
            handleCurrentChange2(val) {
                this.pageInfo2.currentPage = val;
                this.editFormSearchMethodChange();
            },
            editFormSearchMethodChange(){
                let condition = {}
                condition.orderStatus ='提交';
                condition.lineId = this.currentLineId;
                condition.pageInfo = this.pageInfo2;
                condition.productNo = this.editFormSearch.productNo.trim();
                condition.productName = this.editFormSearch.productName.trim();
                condition.projectId = this.editFormSearch.projectId;
                condition.buildId = this.editFormSearch.buildId
                condition.floorId = this.editFormSearch.floorId
                condition.typeId = this.editFormSearch.typeId
                combModel.getProductsByStatusAndSeachCondition(condition).then(data=>{
                    this.productData= data.entity.list;
                    this.pageInfo2.total = data.entity.totalCount;
                });
            }

        }


    }

</script>
<style>

.plate-green{
    color: #0FE436;
    font-weight: bold
}
</style>