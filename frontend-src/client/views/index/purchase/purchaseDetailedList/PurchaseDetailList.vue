<template>

    <div>
        <el-table
                :data="datas"
                @selection-change="handleSelectionChange"
                size="mini"
                stripe
                border
                height="450"
                style="width: 100%"
                @expand-change="showSubData"
                :expand-row-keys="expands"
                row-key="id"
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
                                prop="rawMaterialName"
                                label="原料名称"
                        >
                        </el-table-column>
                         <el-table-column
                                prop="specification"
                                label="原料规格"
                        >
                        </el-table-column>
                         <el-table-column
                                prop="unitName"
                                label="单位"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="deliveryTime"
                                label="供货时间"
                        >
                        </el-table-column>
                        
                        <el-table-column
                                prop="number"
                                label="采购总量"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="inputNumAll"
                                label="入库量"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="unitPrice"
                                label="单价"
                        >
                        </el-table-column>
                        <el-table-column
                                prop="totalPrice"
                                label="税前价"
                        >
                        </el-table-column>

                        <el-table-column
                                prop="taxRate"
                                label="税率(%)">
                        </el-table-column>
                        <el-table-column
                            prop="afterTaxRate"
                            label="税后价">
                        </el-table-column>
                    </el-table>
                </template>
            </el-table-column>
            <el-table-column
                    type="selection"
                    width="50">
            </el-table-column>
            <el-table-column
                    type="index"
                    label="序号"
                    width="50">
            </el-table-column>
            <el-table-column
                    prop="purchaseNo"
                    label="采购单号"
                    width="160">
            </el-table-column>
            <el-table-column
                    prop="projectName"
                    label="项目"
                    :filters="projects"
                    :filter-method="filterProject"
                    >
            </el-table-column>
            <el-table-column
                    prop="purchaseDate"
                    label="采购时间">
            </el-table-column>
            <el-table-column
                    prop="salesman"
                    label="业务员">
            </el-table-column>
            <el-table-column
                    prop="totalPriceAll"
                    label="税前总价">
            </el-table-column>
            <el-table-column
                    prop="afterTaxPrice"
                    label="税后总价">
            </el-table-column>
            <el-table-column
                    prop="dateCreated"
                    :formatter="formatterDate"
                    label="创建时间"
                    width="140">
            </el-table-column>
            <el-table-column
                    prop="dateModified"
                    :formatter="formatterDate"
                    label="修改时间"
                    width="140">
            </el-table-column>
            <el-table-column
                    label="操作"
                    align="left">
                <template slot-scope="scope">
                    <!-- <el-button size="mini" @click="editClick(scope.row)">编辑</el-button> -->
                </template>
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle" >
            <el-col style="margin-top:15px">
                <el-button @click="addClick" type="primary">新建</el-button>
                <!-- <el-upload
                        id="upload"
                        style="display:inline-flex"
                        class="upload-demo"
                        action=""
                        :before-upload="beforeAvatarUpload"
                >
                    <el-button @click="importClick" type="primary">导入</el-button>
                </el-upload> -->
                <el-button @click="deletesClick" type="danger"> 删除勾选</el-button>
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

    </div>
</template>
<script>
    import purchaseModel from '../../../../model/purchase/purchase'
    import clientModel from '../../../../model/client-model'
    import {Notification} from 'element-ui'
    import  moment from  'moment'
    export default {
        data() {
            return {
                pageInfo:{
                    currentPage:1,
                    total:0,
                    pageSize:this.GLOBAL.pageSize,
                    pageSizes:this.GLOBAL.pageSizes
                },
                datas:[],
                selected:[],
                mode:'',
                query:{

                },
                subDatas:[],
                expands:[],
                projects:[],
            }
        },
        mounted() {
            purchaseModel.loadAllProject().then((data)=>{
                let projectList = data.entity;
                for (let i = 0; i < projectList.length; i++) {
                    const e = projectList[i];
                    let e_temp ={
                        text:'',
                        value:'',
                    }
                    e_temp.text = e.projectName;
                    e_temp.value = e.projectName;
                    this.projects.push(e_temp);
                }
            });
            this.list();
        },
        methods: {
            filterProject(value, row, column){
                const property = column['property'];
                return row[property] === value;
            },
            showSubData(row, expandedRows) {
                let id = row.id;
                if(this.expands.some((e)=>e==row.id)){
                    this.expands=[]
                }else{
                    this.expands=[]
                    this.expands.push(row.id)
                }
                purchaseModel.getPurchaseDetailsById(id).then(data => {
                    this.subDatas = data.entity;
                })
            },
            formatterDate(row, column, cellValue, index){
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            addClick() {
                this.$router.push({ name: 'purchaseDetailAdd',mode : 'add'})
            },
            editClick(row) {
                this.$router.push({ 
                    name: 'purchaseDetailAdd',
                    param:{
                        mode : 'update',
                        id : row.id
                    }
                })
            },
            deletesClick() {
                if(this.selected.length ==0){
                    this.$message('请选择删除项');
                    return;
                }
                let ids=[];
                this.selected.forEach((row)=>{
                    ids.push(row.id)
                });
                this.$confirm('确认删除选择项?','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(()=>{
                    this.$store.commit('startLoading');
                    purchaseModel.deleteByIds(ids).then((data)=>{
                        if(data.status == 'success'){
                            Notification({
                                type:"success",
                                message:'删除成功'
                            })
                        }else{
                            Notification({
                                type:"error",
                                message:data.msg
                            })
                        }
                        this.$store.commit('endLoading');
                        this.list();
                    })

                }).catch(()=>{

                })
            },
            list() {
                this.$store.commit('startLoading');
                this.query.pageInfo = this.pageInfo;
                purchaseModel.list(this.query).then(data =>{
                    this.datas=data.entity.content;
                    this.pageInfo.total=data.entity.totalElements;
                    this.$store.commit('endLoading')
                }).catch(err=>{
                    console.error(err)
                })
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize = val;
                this.list()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val;
                this.list()
            },
            /**
             * 待定用处
             */
            importClick(){
                
            },
            /**
             * 文件上传，导入采购单和明细
             */
            beforeAvatarUpload (file) {
                console.log(file.name)
                let xls = file.name.split('.')
                if (xls[xls.length-1] === 'xlsx') {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    let formData=new FormData();
                    formData.append('file',file);
                    let file0 = formData;
                    clientModel.ax.post('/purchase/excelImport',file0,{
                        headers:{
                            'Content-Type':'charset=UTF-8',
                            //  'Access-Control-Allow-Origin':'*'
                        },
                    }).then((data)=>{
                        if (data.status == 'failed') {
                            Notification({
                                message: data.msg,
                                type: 'warning',
                            })
                            loading.close()
                            return
                        }
                        this.$emit('upload-edit-finish')
                        Notification({
                            message: '导入成功',
                            type: 'success'

                        })
                        this.list();
                        loading.close()
                    }).catch((err)=>{
                        console.error("err",err)
                        loading.close()
                    })
                    return false
                } else {
                    this.$message.error('上传文件只能是 xlsx 格式!')
                    return false
                }
            },
        },

    }
</script>
<style>

</style>