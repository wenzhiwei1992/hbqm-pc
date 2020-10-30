<template>
    <el-form :model="entity" v-if="entity" ref="criteriaForm" :inline="true" :style="listType=='checkPro'?'position:relative;':''">
        <el-form-item prop="projectId">
            <el-select v-model="entity.projectId" placeholder="项目名称" clearable filterable @change="changeProject"
                       style="width:110px;">
                <el-option
                        v-for="item in projectList"
                        :key="item.id"
                        :label="item.projectName"
                        :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="buildingId">
            <el-select v-model="entity.buildingId" placeholder="楼栋" clearable filterable @change="changeBuilding"
                       style="width:110px;" :disabled="entity.projectId==''">
                <el-option
                        v-for="item in buildings"
                        :key="item.id"
                        :label="item.buildCode"
                        :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="floorId">
            <el-select v-model="entity.floorId" placeholder="楼层" clearable filterable :disabled="entity.buildingId==''"
                       style="width:110px;">
                <el-option
                        v-for="item in floors"
                        :key="item.id"
                        :label="item.floorNum"
                        :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="stockId" v-if="listType=='checkPro' || listType == 'record' " >
            <el-select v-model="entity.stockId" placeholder="库区" clearable filterable
                       @change="changeStock" style="width:110px;">
                <el-option
                        v-for="item in stocks"
                        :key="item.id"
                        :label="item.stockName"
                        :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="shelfId" v-if="listType=='checkPro' || listType == 'record' " >
            <el-select v-model="entity.shelfId"  placeholder="货架"   :disabled="entity.stockId==''" clearable filterable
                       style="width:110px;">
                <el-option
                        v-for="item in shelves"
                        :key="item.id"
                        :label="item.shelfName"
                        :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>

         <el-form-item prop="detailNo">
             <el-input v-model="entity.detailNo" placeholder="构件明细编号" clearable style="width:120px;"></el-input>
         </el-form-item>
        <el-form-item prop="productName">
            <el-input v-model="entity.productName" placeholder="构件名称" clearable style="width:110px;"></el-input>
        </el-form-item>
        <el-form-item prop="productNo">
            <el-input v-model="entity.productNo" placeholder="构件编码" clearable style="width:110px;"></el-input>
        </el-form-item>
        <el-form-item prop="noticeDPS" v-if="(selected != null && listType != 'record' )">
            <el-select
                    placeholder="是否过期"
                    v-model="entity.noticeDPS"
                    clearable
                    style="width:110px;"
            >
                <el-option
                        value="是"
                        label="是"
                ></el-option>
                <el-option
                        value="否"
                        label="否"
                ></el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="productStatus" v-if="listType=='checkPro' ">
            <!-- <el-input v-model="entity.productStatus" placeholder="检验状态" clearable style="width:110px;"></el-input> -->
            <el-select v-model="entity.productStatus" size="mini" placeholder="当前状态" clearable style="width:110px;">
                <el-option label="待隐检" value="待隐检"></el-option>
                <el-option label="隐检返修" value="隐检返修"></el-option>
                <el-option label="浇筑确认" value="浇筑确认"></el-option>
                <el-option label="待成品检" value="待成品检"></el-option>
                <el-option label="返修" value="返修"></el-option>
                <el-option label="待入库" value="待入库"></el-option>
                <el-option label="成品入库" value="成品入库"></el-option>
                <el-option label="发货待确认" value="发货待确认"></el-option>
                <el-option label="已发货" value="已发货"></el-option>

            </el-select>
        </el-form-item>

        <el-form-item prop="name" @click.native="((type)=>{showSelectTrees(1)})" v-if="listType=='record' ">
            <el-input
                    placeholder="构件类型"
                    v-model="entity.name"
                    style="width:120px"
            >
            </el-input>
        </el-form-item>

        <el-form-item prop="checkResult" v-if="listType=='record' || (selected != null && listType != 'record' ) ">
            <el-select
                    placeholder="构件属性"
                    v-model="entity.checkResult"
                    clearable
                    style="width:110px;"
            >
                <el-option
                        value="合格"
                        label="合格品"
                ></el-option>
                <el-option
                        value="返修"
                        label="不良品"
                ></el-option>
                <!--<el-option -->
                        <!--value="报废"-->
                        <!--label="报废品"-->
                <!--&gt;</el-option>-->
            </el-select>
        </el-form-item>

        <el-form-item prop="trayId" v-if="listType=='record' || listType=='norecord'" >
            <el-select
                    placeholder="托盘"
                    style="width:135px"
                    v-model="entity.trayId"
                    clearable
                    filterable
                    size="mini"
            >
                <el-option
                        v-for="tray in trays"
                        :key="tray.id"
                        :label="tray.trayNo"
                        :value="tray.id">
                </el-option>
            </el-select>
        </el-form-item>

        <el-form-item prop="deliverDateRange" v-if="listType=='checkPro'">
            <el-date-picker
                    v-model="entity.deliverDateRange"
                    type="daterange"
                    align="right"
                    unlink-panels
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    :default-time="['00:00:00', '23:59:59']"
                    value-format="yyyy-MM-dd"
                    style="width:300px;">
            </el-date-picker>
        </el-form-item>
        <el-form-item prop="recordDateRange" v-if="listType=='record' || (selected != null && listType != 'record' )">
            <el-date-picker
                    v-model="entity.recordDateRange"
                    type="daterange"
                    align="right"
                    unlink-panels
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    :default-time="['00:00:00', '23:59:59']"
                    value-format="yyyy-MM-dd"
                    style="width:230px;">
            </el-date-picker>
        </el-form-item>
        <el-form-item prop="planProdTimeDateRange" v-if="listType=='checkDCPJ'">
            <el-date-picker
                    v-model="entity.planProdTimeDateRange"
                    type="daterange"
                    align="right"
                    unlink-panels
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    value-format="yyyy-MM-dd"
                    :default-time="['00:00:00', '23:59:59']"
                    style="width:300px;">
            </el-date-picker>
        </el-form-item>
       <!-- <el-form-item prop="orgId" v-if="listType=='checkPro' ">
            <el-select
                    v-model="entity.orgId"
                    style="width: 150px"
                    placeholder="基地名称"
                    clearable>
                <el-option
                        v-for="item in orgs"
                        :key="item.id"
                        :label="item.orgName"
                        :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>-->
        <el-form-item style="position:absolute;right:0;top:0;">
          <el-tooltip
                    effect="light"
                    content="查询"
                    placement="top"
          >
            <el-button icon="el-icon-search" type="primary" @click="list()"></el-button>
          </el-tooltip>
          <el-tooltip
                    effect="light"
                    content="重置"
                    placement="top"
          >
            <el-button icon="el-icon-refresh" @click="resetForm('criteriaForm')"></el-button>
          </el-tooltip>

        </el-form-item>
        <el-form-item :class="selected != null && listType != 'record' || authorized('productDetailInRecordList:backRecord') && listType == 'record'?'block_class':''">
            <el-button icon="el-icon-s-unfold" v-if="selected != null && listType != 'record'" type="success" @click="oneKeyInStock()">批量入库
            </el-button>
            <el-button icon="el-icon-s-fold" v-if="listType== 'record' && authorized('productDetailInRecordList:backRecord') " type="danger" @click="outStock()">退库</el-button>
            <el-button icon="el-icon-s-unfold" v-if="listType== 'record' && authorized('productDetailInRecordList:printRecord') " type="primary" @click="printRecordInfo()">打印
            </el-button>
            <el-button icon="el-icon-s-fold" v-if="listType== 'record' && authorized('productDetailInRecordList:exportRecord') " type="primary" @click="exportExcel()">导出</el-button>
        </el-form-item>
        <el-form-item class="text_class">
            <div class="text" v-if="listType== 'record'">
              <span class="color_font_span_class"> 在库量 :</span>
              <span class="color_span_class"> {{zkDTO.totalCount}}片 / {{zkDTO.totalProductVol}}m³/ {{zkDTO.totalProjectConcrete}}m³ </span>
            </div>
        </el-form-item>

        <el-dialog :visible.sync="showSelectTree" width="35%" append-to-body>
            <tree-add-edit1
                    :treeArr="treeArr"
                    :baseCheckedId="baseCheckedId"
                    :baseCheckParentId="baseCheckParentId"
                    @tree-edit-submit="checkoutStatusFinish"
                    @tree-edit-cancel="checkoutStatusCancel"
            ></tree-add-edit1>
        </el-dialog>
    </el-form>


</template>
<script>
    import projectModel from '../../../model/project/project'
    import buildingModel from '../../../model/project/building'
    import trayModel from '../../../model/basicdata/tray.js'
    import floorModel from '../../../model/project/floor'
    import stockModel from '../../../model/basicdata/stock'
    import shelfModel from '../../../model/basicdata/shelf'
    import orgModel from '../../../model/system/org'
    import model from '../../../model/model'
    import moment from 'moment'
    import {Notification} from 'element-ui'
    import menuTabService from "../../../service/menuTabService";
    import productTypeModel from "../../../model/basicdata/productType";
    import TreeInfo from "../basic/materialStorage/TreeInfo.vue";


    export default {
        components: {
            'tree-add-edit1': TreeInfo,
        },
        props: ['listType', 'selected', "zkDTO"],
        data() {
            return {
                treeType: '',
                treeArr:'',
                showSelectTree: false,
                baseCheckedId: [],
                baseCheckParentId: [],


                projectList: [],
                buildings: [],
                floors: [],
                stocks: [],
                shelves: [],
                trays:[],
                orgs: [],
                entity: {
                    name:'',
                    typwpeId: '',
                    listType: '',
                    projectId: '',
                    buildingId: '',
                    floorId: '',
                    detailNo: '',
                    productName: '',
                    productNo: '',
                    noticeDPS:'',
                    stockId: '',
                    shelfId: '',
                    orgId: '',
                    recordId:'',
                    deliverDateRange: ['', ''],
                    recordDateRange: ['', ''],
                    planProdTimeDateRange:['',''],
                }
            }
        },
        computed: {},
        mounted() {
            trayModel.findAllByOrgId().then(data => {
                this.trays = data.entity;
            })
            this.entity.listType = this.listType
            console.log(' this.entity.listType---------->', this.entity.listType)
            projectModel.projectListS().then(data => {
                this.projectList = data.entity;
            }).catch(() => {
            })
            shelfModel.getStocks().then(data => {
                this.stocks = data.entity;
                this.stocks = this.stocks.filter(s => s.stockStatus == '启用' && s.stockTypeCode == '成品仓库')
            }).catch(err => {
            })
            orgModel.orgAllByOrgIds().then((data) => {
                this.orgs = data.entity
            }).catch(err => {
            })
            this.showProductTree()
        },
        methods: {
            exportExcel(){
                let self = this;
                let msg = "是否导出所选数据?";
                if (self.selected.length == 0) {
                    msg = "是否导出全部数据?";;
                }
                this.$confirm(msg, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let ids = [];
                    let rows = [];
                    if (self.selected.length == 0) {

                    }else{
                        self.selected.forEach((row) => {
                            ids.push(row.id)
                            rows.push(row)
                        });
                    }

                    this.$emit('export-excel', rows,this.entity)
                }).catch(() => {

                })
            },
            showSelectTrees(type) {
                //1是列表
                if (type == 1) {

                    this.baseCheckedId.splice(0, this.baseCheckedId.length);

                    this.baseCheckedId.push(this.entity.typwpeId);

                    this.baseCheckParentId.splice(0, this.baseCheckedId.length);
                    this.baseCheckParentId.push(this.entity.typwpeId);
                }
                this.showSelectTree = true
                this.treeType = type
            },
            showProductTree() {
                // categoryModel.getCategorysToTreeParentDisabled().then((data) => {
                productTypeModel.getProductTypesToTreeParentDisabled().then((data) => {
                    this.treeArr = data.entity
                })
            },
            changeProject() {
                buildingModel.findByProjectId(this.entity.projectId).then(data => {
                    this.buildings = data.entity
                    this.entity.buildingId = ''
                    this.entity.floorId = ''
                    this.floors = []
                })
            },
            checkoutStatusFinish(id, name) {
                if (this.treeType == 1) {

                    this.entity.name = name;
                    this.entity.typwpeId = id;
                    this.entity.productTypeId = id;
                }
                this.showSelectTree = false
            },
            checkoutStatusCancel() {
                this.showSelectTree = false
            },
            changeBuilding(id) {
                this.entity.floorId = ''
                this.floors = []
                if (!id) {
                    return
                }

                let building = this.buildings.find(b => b.id == id)

                function compare(property) {
                    return function (a, b) {
                        var value1 = a[property];
                        var value2 = b[property];
                        return value1 - value2;
                    }
                }

                floorModel.findByBuildingIdAndProjectId(building.id, building.projectId).then((data) => {
                    this.floors = data.entity.sort(compare('floorNum'))
                    // this.entity.floorId = null
                })

            },
            changeStock(id) {
                this.shelves = []
                this.entity.shelfId = ''
                if (!id) {
                    return
                }
                stockModel.getShelfsById(id).then(data => {
                    this.shelves = data.entity
                    this.shelves = this.shelves.filter(s => s.shelfStatus == '启用' )
                })
            },
            list() {
                this.$emit('change-criteria', this.entity)
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            oneKeyInStock() {
                if (this.selected.length == 0) {
                    this.$message(this.GLOBAL.MSG.SELECTED_ERR);
                    return;
                }
                this.$emit('pl-form-show');

            },
            authorized(index) {
                return menuTabService.authorized(this.$store, index)
            },
            /**
             * 退库
             */
            outStock() {
                let self = this;
                this.$confirm('确认?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    if (self.selected.length == 0) {
                        self.$message(self.GLOBAL.MSG.SELECTED_ERR);
                        return;
                    }
                    let ids = [];
                    let recordIds = [];
                    let flag = true;
                    self.selected.forEach((row) => {
                        ids.push(row.productDetailId)
                        recordIds.push(row.recordId)
                        if(row.checkResult=='不良品'){
                            flag = false;
                        }
                    });
                    if(!flag){
                        Notification({
                            message: '所选构件中包含正在返修的构件，无法退库',
                            type: "error"
                        })
                        return;
                    }

                    const loading = self.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    model.outStock(ids,recordIds).then(data => {
                        if (data.status === 'success') {
                            Notification({
                                message: '退库成功',
                                type: "success"
                            })
                        } else {
                            Notification({
                                message: data.msg,
                                type: "warning"
                            })
                        }
                        this.$emit('change-criteria', this.entity)
                        loading.close()
                    }).catch(err => {
                        loading.close()
                    })
                }).catch(() => {

                })
            },
            printRecordInfo() {
                let self = this;

                this.$confirm('是否打印所选构件?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    if (self.selected.length == 0) {
                        self.$message(self.GLOBAL.MSG.SELECTED_ERR);
                        return;
                    }
                    let projectName = "";
                    let recordDate = "";
                    let info = new Array();　//创建一个数组
                    let projects = new Set(); // 空Set
                    let recordDates = new Set(); // 空Set

                    self.selected.forEach((row) => {
                        info.push(row);
                        projects.add(row.projectName);

                        projectName = row.projectName

                        if( row.inStockTime){
                            recordDate = moment(row.inStockTime).format("YYYY-MM-DD");
                            console.log(recordDate)
                            recordDates.add(recordDate);
                        }else{
                            recordDates.add("");
                        }
                    });
                    if(projects.size!=1){
                        projectName = "";
                    }
                    if(recordDates.size!=1){
                        recordDate = "";
                    }
                    const loading = self.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });

                    this.$router.push({
                        name: 'RecordProductPrinting',
                        params: {
                            info:info,
                            projectName:projectName,
                            recordDate:recordDate
                        }
                    })
                    loading.close();

                }).catch(() => {

                })
            }
        },


    }
</script>
<style scoped lang="scss">
    .color_span_class {
        color: #67C23A;
        font-weight: 800;
    }

    .color_font_span_class {
        font-weight: 800;
    }
    .block_class{
        display:block;
        margin-bottom:5px;
    }
    .text_class{
      position:absolute;
      right:0;
      bottom:0;
      margin-bottom: 5px;
      .text{
        span{
          vertical-align:text-bottom;
          line-height:0;
        }
      }
    }
</style>
