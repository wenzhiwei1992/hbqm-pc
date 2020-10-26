<template>
    <div>
        <el-container>
            <el-header ref="headOperationBar" style="height:35px;">
                <el-row v-show="!isNewPage">
                    <my-button-group :model="buttonData"></my-button-group>
                </el-row>
                <el-row v-show="isNewPage">
                    <my-button-group :model="newButtonData"></my-button-group>
                </el-row>
            </el-header>
            <el-container ref="mainContent" :style="`height:${tableHeight}px`">
                <el-aside width="200px" id="tab" style="height:100%;">
                    <el-tree
                            height="100%"
                            :data="data"
                            node-key="id"
                            highlight-current
                            default-expand-all
                            :expand-on-click-node="false"
                            @node-click="nodeClick"
                            class="building-andHouse-type-tree"
                    >
                        <span class="custom-tree-node" slot-scope="{ node, data }" @mouseenter="mouseenter(node)"
                              @mouseleave="mouseleave(node)">
                        <i class="el-icon-s-grid" v-if="node.level===1"></i>
                        <i class="el-icon-menu" v-if="node.level===2"></i>
                        <i class="el-icon-office-building" v-if="node.level===3"></i>
                        <span>{{ node.label }}</span>
                            <span style="margin-left:15px;">
                            <el-button
                                    class="icon-style"
                                    :style="!node.add?'visibility:hidden;':'visibility:visible;'"
                                    type="text"
                                    size="mini"
                                    @click.stop="() =>append(data)"
                                    v-if="!node.disabled"
                            >
                            <i class="el-icon-plus"></i>
                            </el-button>
                            <el-button
                                    class="icon-style"
                                    :style="!node.del?'visibility:hidden;':'visibility:visible;'"
                                    type="text"
                                    size="mini"
                                    @click.stop="() => remove(node,data)"
                                    v-if="node.level!==1||node.disabled">
                                <i class="el-icon-delete"></i>
                            </el-button>
                            <el-button
                                    class="icon-style"
                                    :style="!node.edit?'visibility:hidden;':'visibility:visible;'"
                                    type="text"
                                    size="mini"
                                    @click.stop="() => modifiy(node,data)"
                                    v-if="node.level!==1||node.disabled">
                            <i class="el-icon-edit"></i>
                            </el-button>

                        </span>
                        </span>
                    </el-tree>
                </el-aside>
                <el-container>
                    <el-main id="mainD" style="overflow:hidden;">
                        <div v-show="!isNewPage" :style="`height:${tableHeight-70}px`">
                            <el-table
                                    :data="floors"
                                    @row-click="clickRow"
                                    ref="moviesTable"
                                    size="mini"
                                    stripe
                                    border
                                    @selection-change="handleSelectionChange"
                                    height="100%"
                            >
                                <el-table-column
                                        type="expand"
                                        width="120">
                                    <template slot-scope="props">
                                        <!--  floorsProducts    props.row.products   props.row.floorVO_s  -->
                                        <el-table
                                                size="mini"
                                                :data=" props.row.floorVO_s"
                                        >
                                            <el-table-column
                                                    prop="productNo"
                                                    label="构件编码"
                                                    align="center"
                                                    width="100"
                                            ></el-table-column>
                                            <el-table-column
                                                    prop="name"
                                                    label="构件类别"
                                            >
                                            </el-table-column>
                                            <el-table-column
                                                    prop="productName"
                                                    label="构件名称"
                                            >

                                            </el-table-column>
                                            <el-table-column
                                                    prop="num"
                                                    label="构件数"
                                            >

                                            </el-table-column>
                                            <el-table-column
                                                    :formatter="formatterDate"
                                                    prop="dateCreated"
                                                    label="创建时间"
                                            >
                                            </el-table-column>
                                        </el-table>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        type="selection"
                                        width="55"
                                ></el-table-column>
                                <el-table-column
                                        sortable
                                        prop="id"
                                        v-if="show"
                                        label="ID"
                                        width="150">
                                </el-table-column>
                                <el-table-column
                                        prop="floorNum"
                                        label="楼层"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="productTypeNum"
                                        label="构件类型数"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="productNoNum"
                                        label="构件编码数"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="productDetailNum"
                                        label="构件总数"
                                >
                                </el-table-column>
                            </el-table>
                        </div>
                        <div v-show="isNewPage">
                            <floor-product-detail-edit
                                    ref="childFloorProductDetailEdit"
                                    :projectId="this.projectInfo.id"
                            ></floor-product-detail-edit>
                        </div>
                    </el-main>
                    <el-footer v-show="!isNewPage">
                        <el-row type="flex" justify="end">
                            <el-pagination
                                    @size-change="handleSizeChangeS"
                                    @current-change="handleCurrentChangeS"
                                    :current-page="pageInfoS.currentPage"
                                    :page-sizes="pageInfoS.pageSizes"
                                    :page-size="pageInfoS.pageSize"
                                    :total="pageInfoS.total"
                                    layout="total, sizes, prev, pager, next, jumper"
                                    background
                            >
                            </el-pagination>
                        </el-row>
                    </el-footer>
                </el-container>
            </el-container>
        </el-container>
        <el-dialog :title="mode=='add'?'新建户型':'编辑户型'" v-if="editFormHouseTypeVisible"
                   :visible.sync="editFormHouseTypeVisible" style="z-index: 1000000;" append-to-body>
            <model-edit :edit-form-house-type="editFormHouseType"
                        :mode="mode"
                        @house-edit-finish="houseTypeEditFinish"
                        @house-edit-cancel="houseTypeEditCancel"
            ></model-edit>
        </el-dialog>
        <el-dialog :title="mode=='add'?'新建楼栋':'编辑楼栋'" v-if="editFormBuildingVisible"
                   :visible.sync="editFormBuildingVisible" append-to-body>
            <building-edit :edit-form-building="editFormBuilding"
                           :arr-house-type="arrHouseType"
                           :mode="mode"
                           @building-edit-finish="buildingEditFinish"
                           @building-edit-cancel="buildingEditCancel"
            ></building-edit>
        </el-dialog>
        <el-dialog :title="productTitle" v-if="editFormProductVisible"
                   :visible.sync="editFormProductVisible" width="80%" append-to-body>
            <floor-product-edit :editFormProduct="editFormProduct"
                                :productInfoS="productInfoS"
                                :arrFloorNum="arrFloorNum"
                                v-on:headCallBack="headCall"
                                :visible="editFormProductVisible"
                                @product-edit-finish="productEditFinish"
                                @product-edit-cancel="productEditCancel"
            ></floor-product-edit>
        </el-dialog>
        <el-dialog :before-close="closeDialogCopy" title="复制构件" v-if="editFormCopyVisible"
                   :visible.sync="editFormCopyVisible" width="40%" append-to-body>
            <copy-floor-product-edit
                    :copy-edit-form="copyEditForm"
                    :existProduct="existProduct"
                    :not-exist-product="notExistProduct"
                    :building-id="buildingId"
                    @copy-edit-finish="copyEditFinish"
                    @copy-edit-cancel="copyEditCancel"
            >
            </copy-floor-product-edit>
        </el-dialog>
        <el-dialog title="批量添加构件" v-if="editFormBatchVisible" :visible.sync="editFormBatchVisible" append-to-body>
            <relevance-add-batch

                    :building-id="buildingId"
                    :projectId="projectId"
                    @batch-edit-finish="batchEditFinish"
                    @batch-edit-cancel="batchEditCancel">
            </relevance-add-batch>
        </el-dialog>
    </div>
</template>

<script>
    import projectModel from '../../../../model/project/project'
    import buildingModel from '../../../../model/project/building'
    import houseTypeModel from '../../../../model/project/houseType'
    import floorRelevanceModel from '../../../../model/project/floorRelevance'
    import HouseTypeEdit from './HouseTypeEdit.vue'
    import BuildingEdit from './BuildingEdit.vue'
    import FloorProductEdit from './FloorProductEdit.vue'
    import CopyFloorProductEdit from './CopyFloorProductEdit.vue'
    import productReqModel from '../../../../model/project/productReq'
    import FloorProductDetailEdit from './FloorProductDetailEdit.vue'
    import RelevanceAddBatch from './RelevanceAddBatch.vue'
    import moment from "moment";
    import storage from 'good-storage'
    import {Notification} from 'element-ui'

    let id = 1000;

    export default {
        data() {
            const data = [{
                id: 0,
                label: '',
                disabled: false,
                children: [{
                    id: '',
                    label: '',
                    children: [],
                }]
            }];
            return {
                arrBuilding: [],
                arrFloorNum: [],
                existProduct: [],//有构件楼层
                notExistProduct: [],//没有构件楼层
                buildingId: '',//楼栋的id
                show: '',//控制table是否被隐藏
                floorId: '',//删除楼层下构件所用的Id
                fs: '',//显示在页面的对象
                typwedIdNum: 0,
                floorQuery: {
                    buildId: '',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: this.GLOBAL.pageSize,
                    pageSizes: this.GLOBAL.pageSizes
                },
                pageInfoS: {
                    currentPage: 1,
                    total: 0,
                    pageSize: this.GLOBAL.pageSize,
                    pageSizes: this.GLOBAL.pageSizes
                },
                selected: [],
                data2: [],
                dataB: [],
                floors: [],
                floorsProducts: [],
                arrHouseType: [],//selectHouseType(户型)
                updateHouseType: '',//编辑楼栋时传值
                //  list:[],:props="defaultProps"
                // dataTree: JSON.parse(JSON.stringify(data)),
                data: JSON.parse(JSON.stringify(data)),
                defaultProps: {
                    children: 'children',
                    label: 'label',
                    disabled: 'disabled'
                },
                mode: '',
                modeProduct: '',
                value: '',
                project: null,
                editFormTitle: '',
                //编辑楼栋表单
                editFormBuilding: null,//楼栋弹出框
                editFormBuildingVisible: false,//楼栋

                //编辑户型表单
                editFormHouseType: null,//户型弹出框
                editFormHouseTypeVisible: false,//户型
                //编辑构造表单
                editFormProduct: null,
                editFormProductVisible: false,
                productInfoS: null,
                //复制
                copyEditForm: null,
                editFormCopyVisible: false,
                //批量添加构件
                projectId: '',
                editFormBatchVisible: false,
                floorIdHeadCall: 0,
                isNewPage: true,
                levelNum: 0,
                buttonData: [
                    {
                        name: '编辑构件',
                        event: 'addClickProduct'
                    },
                    {
                        name: '复制',
                        event: 'copyClick'
                    },
                    {
                        name: '切换版本',
                        event: 'editFloorProduct'
                    }
                ],
                newButtonData: [
                    {
                        name: '编辑构件',
                        event: 'addClickProduct'
                    },
                    {
                        name: '复制楼栋',
                        event: 'copyClickBuild'
                    },
                    {
                        name: '复制楼层',
                        event: 'copyClick'
                    },
                    {
                        name: '切换版本',
                        event: 'editFloorProduct'
                    },
                    {
                        name: '保存',
                        event: 'saveProduct'
                    },
                    {
                        name: '导出',
                        event: 'exportExcel'
                    },
                    {
                        name: '添加',
                        event: 'clickOpenRelevance',
                    }
                ],
                projectInfo: '',
                isPageOpen: false
            }
        },
        computed: {
            productTitle() {
                if (this.building != null) {
                    return 'id为空'
                }
            },
            tableHeight() {
                return this.$store.state.mainContentHeight
            }
        },
        props: ['visible', 'dataBuild', 'dataHouseType'],
        components: {
            'model-edit': HouseTypeEdit,
            'building-edit': BuildingEdit,
            'floor-product-edit': FloorProductEdit,
            'copy-floor-product-edit': CopyFloorProductEdit,
            'floor-product-detail-edit': FloorProductDetailEdit,
            'relevance-add-batch': RelevanceAddBatch,
        },
        mounted() {
            this.init()
        },
        // 因页面切换时要求不刷新 故而使用keep-alive来缓存组件 缓存后的组件再次进入不执行mounted钩子 故而使用activated钩子
        activated() {
            this.init()
        },
        methods: {
            init() {
                // 缓存数据
                let storageData = storage.get('BuildingAndHouseType_projectInfo')
                // 上一个页面传递的参数
                let projectInfo = this.$route.params.projectInfo
                // 错误进入(无缓存无参数)
                let isError = !storageData && !projectInfo
                // 第一次进该页面 或者清除了缓存(无缓存有参数)
                let isFirst = !storageData && projectInfo
                // 顶部tabar进入或者刷新页面(有缓存无参数)
                let onlyStorage = storageData && !projectInfo
                // 非第一次进入,也是频率最多的进入(有缓存有参数)
                let normal = storageData && projectInfo
                // 页面是否已经打开过
                let isPageOpen = this.isPageOpen
                if (isError) {
                    this.$router.go(-1)
                    return
                }
                if (isFirst) {
                    // 第一次进入页面 给参数存到缓存
                    this.$refs.childFloorProductDetailEdit.tableShow = false
                    this.$refs.childFloorProductDetailEdit.productNameOptions = []
                    storage.set('BuildingAndHouseType_projectInfo', projectInfo)
                }
                if (onlyStorage) {
                    if (isPageOpen) {
                        // tabbar切换 不做任何操作
                        return
                    }
                }
                if (normal) {
                    if (isPageOpen) {
                        if (JSON.stringify(projectInfo) === JSON.stringify(storageData)) {
                            // 带了重复的参数
                            return
                        }
                    }
                    // 缓存和参数不相等时取参数的值赋给缓存
                    this.$refs.childFloorProductDetailEdit.tableShow = false
                    this.$refs.childFloorProductDetailEdit.productNameOptions = []
                    storage.set('BuildingAndHouseType_projectInfo', projectInfo)

                }
                // 页面赋值
                this.isPageOpen = true
                this.projectInfo = storage.get('BuildingAndHouseType_projectInfo')
                this.listTree()
                this.getprojects()
                this.getHouseType()
                this.getBuilding()
            },
            clickOpenRelevance() {
                if (this.buildingId === '') {
                    this.$message.warning("请先选择楼栋")
                    return
                }
                let projectInfo = this.projectInfo
                this.projectId = projectInfo.id
                console.log('project_id', this.projectId)
                this.editFormBatchVisible = true
            },
            exportExcel() {
                this.$refs.childFloorProductDetailEdit.exportExcel()
            },
            saveProduct() {
                this.$refs.childFloorProductDetailEdit.click()
                this.getprojects()
            },
            listTree() {
                let projectInfo = this.projectInfo;
                this.data[0].label = projectInfo.projectName
                houseTypeModel.findHouseTypesToTree(projectInfo.id).then((data) => {
                    this.data[0].children = data.entity
                    this.setLevel(this.data)
                })
            },
            setLevel(data) {
                this.levelNum += 1
                for (let i = 0; i < data.length; i++) {
                    data[i].level = this.levelNum
                    if (data[i].children && data[i].children.length) {
                        this.setLevel(data[i].children)
                    }
                }
            },
            // 点击整行都可以进行勾选
            clickRow(row) {
                this.$refs.moviesTable.toggleRowSelection(row)
            },
            headCall(msg) {
                this.floorIdHeadCall = msg
            },
            copyEditFinish() {
                this.editFormCopyVisible = false
                this.listTree()
                //初始化所选楼栋下楼层
                let a = {
                    id: this.buildingId,
                    disabled: true
                }
                this.nodeClick(a)
            },
            copyEditCancel() {
                this.editFormCopyVisible = false
                this.listTree()
                //初始化所选楼栋下楼层
                let a = {
                    id: this.buildingId,
                    disabled: true
                }
                this.nodeClick(a)
            },
            /**
             *格式化显示时间格式
             */
            formatterDate(row, column, cellValue, index) {
                let date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            //TODO 模块拆分，后续删除
            /*    closeDialog(done) {
                    this.$confirm('确认关闭？')
                        .then(_ => {

                            floorRelevanceModel.editFormCancelDeleteFloorRelevance(this.floorIdHeadCall).then((data) => {

                            })
                            done();

                        })
                        .catch(_ => {
                        });
                    this.editFormProductVisible = false
                },*/
            closeDialogCopy(done) {
                this.editFormCopyVisible = false
            },
            /**
             * 点击关闭按钮时触发
             */
            closeClick() {
                this.getprojects()
                this.floors = []
                this.$emit('building-and-house-type-click-cancel')
            },
//复制楼栋
            copyClickBuild() {
                if (this.buildingId) {
                    this.$confirm('此操作将会复制该楼栋和楼栋下的构件信息, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        buildingModel.copyBuildAllInfo(this.buildingId).then(data => {
                            if (data.status == 'fail') {
                                Notification({
                                    message: data.msg,
                                    type: 'warning'
                                })
                                return
                            }
                            Notification({
                                message: '复制成功',
                                type: 'success'
                            })
                            this.listTree()
                        })
                    }).catch(() => {
                    })
                } else {
                    this.$message('请选择楼栋');
                    return;
                }

            },
            /**
             * 复制楼层信息
             */
            copyClick() {
                //及时刷新
                this.existProduct = []
                this.notExistProduct = []
                if (this.buildingId > 0) {
                    floorRelevanceModel.getFloorNumByBuildingIdAndExistProduct(this.buildingId).then((data) => {
                        for (var i = 0; i < data.entity.length; i++) {
                            var arr = {
                                label: (data.entity[i])[0] + 'F',
                                value: (data.entity[i])[1]
                            }
                            this.existProduct.push(arr)
                        }
                    })
                    floorRelevanceModel.getFloorNumByBuildingIdNotAndExistProduct(this.buildingId).then((data) => {
                        for (var i = 0; i < data.entity.length; i++) {
                            var arr = {
                                label: (data.entity[i])[1] + 'F',
                                value: (data.entity[i])[0]
                            }
                            this.notExistProduct.push(arr)
                        }
                    })
                    this.copyEditForm = {
                        existId: '',
                        notExistId: '',
                        notExistIds: [],
                    }
                    this.editFormCopyVisible = true
                } else {
                    this.$message('请选择楼栋');
                    return;
                }

            },
            /**
             *楼栋所选楼层删除构件
             */
            deletesClick() {
                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }
                this.$confirm('此操作将永久删除勾选楼层,并同时删除楼层下构件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.selected.forEach((row) => {
                        let floorRelevanceQuery = {
                            buildId: this.buildingId,
                            floorId: row.id
                        }
                        floorRelevanceModel.getAllByBuildingIdAndFloorId(floorRelevanceQuery).then((data) => {//删除构件明细
                            var frId = []
                            for (var k = 0; k < data.entity.length; k++) {
                                frId.push(data.entity[k].id)
                            }
                            productReqModel.deleteAllByFloorRelevanceId(frId.join(";")).then((data) => {
                                if (data.status == 'fail') {
                                    Notification({
                                        message: data.msg,
                                        type: 'warning'
                                    })
                                    return
                                }
                                //删除楼层下所有构件
                                floorRelevanceModel.deleteAllByFloorId(row.id).then((data) => {
                                    if (data.status == 'fail') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        return
                                    }
                                    this.listTree()
                                })
                                /*              Notification({
                                                  message: '删除成功',
                                                  type: 'success'
                                              })*/
                            })

                        })

                    })
                }).catch(() => {
                })
            },
            editFloorProduct() {
                this.isNewPage = !this.isNewPage
            },
            //编辑户型表单
            houseTypeEditFinish() {
                this.editFormHouseTypeVisible = false
                this.listTree()
                //   this.getprojects()
                this.getHouseType()
            },
            houseTypeEditCancel() {
                this.getHouseType()
                this.editFormHouseTypeVisible = false
            },
            //编辑楼栋表单
            buildingEditFinish() {
                this.editFormBuildingVisible = false

                // this.getprojects()
                this.listTree()
            },
            buildingEditCancel() {
                this.editFormBuildingVisible = false
            },
            floorFinish() {
                this.floorAdd(data.entity);
            },
            //编辑构件表单
            productEditFinish() {
                this.editFormProductVisible = false
                // this.listTree().then(data=>{
                let val = {
                    id: this.buildingId,
                    disabled: true,
                }
                this.nodeClick(val)
                // })
            },
            productEditCancel() {
                this.editFormProductVisible = false
                // this.listTree().then(data=>{
                let val = {
                    id: this.buildingId,
                    disabled: true,
                }
                this.nodeClick(val)
                // })
                // this.listTree()
                // let val = {
                //     id: this.buildingId,
                //     disabled: true,
                // }
                // this.nodeClick(val)
            },
            /**
             * 进入添加构件页面
             */
            addClickProduct() {
                if (this.buildingId > 0) {
                    buildingModel.getBuildCodeAndProjectNameAndHouseTypeNameByBuildingId(this.buildingId).then((data) => {
                        this.editFormProduct = data.entity
                        buildingModel.buildingLoad(this.buildingId).then((data) => {
                            this.productInfoS = data.entity
                        })
                        this.editFormProductVisible = true
                    })
                } else {
                    this.$message('请选择楼栋');
                }
            },
            /**
             * 点击楼栋树状图tree节点查看楼层
             * @param node
             */
            nodeClick(node) {
                console.log('node', node)
                this.floors = []
                this.buildingId = 0
                if (node.disabled == true) {
                    this.buildingId = node.id
                    // 可编辑表格获取接口数据
                    this.$refs.childFloorProductDetailEdit.startMounted(node)

                    // 老版本不可编辑表格获取数据
                    floorRelevanceModel.getFloorRelevanceByPage(node.id, this.pageInfoS).then((data) => {
                        for (let i = 0; i < data.entity.list.length; i++) {
                            let floorRelevanceQuery = {
                                buildId: node.id,
                                floorId: data.entity.list[i].id
                            }
                            floorRelevanceModel.getProductsByBuildIdAndFloorId(floorRelevanceQuery).then((data1) => {
                                if (data1.entity == false) {

                                } else {
                                    for (let i = 0; i < this.floors.length; i++) {
                                        if (this.floors[i].id == data1.entity[0].id) {
                                            this.floors[i].floorVO_s = data1.entity;
                                            break;
                                        }
                                    }
                                }
                            })
                        }
                        this.floors = data.entity.list;
                        this.pageInfoS.total = data.entity.totalCount;
                    })
                }
            },
            getBuilding() {
                let projectInfo = this.projectInfo;
                buildingModel.getBuildingsByByProjectId(projectInfo.id).then((data) => {
                    this.arrBuilding = data.entity
                })
            },

            /**
             * 跳转Building页面渲染页面
             */
            getprojects() {
                let projectInfo = this.projectInfo;
                projectModel.projectLoad(projectInfo.id).then(data => {
                    let projects = data.entity
                    var dataTemp = new Array();//户型下children[]this.$route.params.id
                    var dataBuilding = new Array();//楼栋下children[]
                    /*             projects.buildingList.forEach((building)=>{
                                     let buildCode = building.buildCode;
                                     var b1={
                                         label:buildCode
                                     };
                                     dataTemp.push(b1);
                                 })*/
                    houseTypeModel.getHouseTypeByProjectIdOpt(data.entity.id).then(houseType => {

                        let content = houseType.entity;
                        let dataTemp = new Array(); //户型下children[]
                        for (let i = 0; i < content.length; i++) {
                            let name = content[i].name;
                            let id = content[i].id;
                            var h1 = {
                                id: id,
                                label: name,
                                disabled: false
                            };
                            dataTemp.push(h1);
                        }
                        var xmH = new Array(); //项目 houseType children[]
                        var xm1 = {
                            label: data.entity.projectName,
                            children: []
                        };
                        xmH.push(xm1);

                        var hx = new Array(); //户型 children[]
                        var hx1 = {
                            label: "户型",
                            children: [],
                            disabled: true
                        };
                        hx.push(hx1);
                        hx[0].children = dataTemp;
                        xmH[0].children = hx;
                        this.data2 = JSON.parse(JSON.stringify(xmH));
                    });

                    buildingModel
                        .getBuildingsByByProjectId(data.entity.id)
                        .then(dataBuilding => {
                            this.arrBuilding = dataBuilding.entity;
                            var dataBuilding = new Array(); //楼顶下children[]

                            //把楼栋放入数组中
                            this.arrBuilding.forEach(building => {
                                let buildCode = building.buildCode; //楼栋号
                                let id = building.id;
                                let aboveFlrNum = building.aboveFlrNum;
                                let belowFlrNum = building.belowFlrNum;
                                var b1 = {
                                    id: id,
                                    label: buildCode,
                                    aboveFlrNum: aboveFlrNum,
                                    belowFlrNum: belowFlrNum,
                                    disabled: false
                                };
                                dataBuilding.push(b1);
                            });

                            var xmB = new Array(); //项目 building children[]
                            var xm2 = {
                                label: data.entity.projectName,
                                children: []
                            };
                            xmB.push(xm2);
                            var buildings = new Array(); //楼栋 children[]
                            var bChild = {
                                label: "楼栋",
                                children: [],
                                disabled: true
                            };
                            buildings.push(bChild);
                            buildings[0].children = dataBuilding;
                            xmB[0].children = buildings;

                            this.dataB = JSON.parse(JSON.stringify(xmB));
                        });
                })
            },
            /**
             * 添加户型 楼栋
             * @param data
             */
            append(data) {
                let projectInfo = this.projectInfo;
                this.mode = 'add';
                if (data.id != 0) {
                    this.editFormBuilding = {
                        treeId: id,
                        projectId: projectInfo.id,
                        dataTree: data,
                        buildCode: '',
                        aboveFlrNum: '',
                        belowFlrNum: '',
                        lowestFlorNum: '',
                        houseType: this.arrHouseType,
                        houseTypeId: data.id,
                    }
                    this.editFormBuildingVisible = true
                } else {
                    this.editFormHouseType = {
                        treeId: id,
                        id: projectInfo.id,
                        dataTree: data,
                        name: '',
                    }
                    this.editFormHouseTypeVisible = true
                }

            },
            /* /!**
              * 添加楼栋
              * @param data
              *!/
             appendBuild(data) {
                 this.mode = 'add';
                 //this.getHouseType()
                 this.editFormBuilding = {
                     treeId: id,
                     projectId: this.projectInfo.id,
                     dataTree: data,
                     buildCode: '',
                     aboveFlrNum: '',
                     belowFlrNum: '',
                     lowestFlorNum: '',
                     houseType: this.arrHouseType,
                 }
                 this.editFormBuildingVisible = true
             },*/
            /**
             * 删除户型
             * @param node
             */
            remove(node, data) {
                //楼栋
                if (data.disabled) {
                    this.$confirm('此操作将永久删除该楼栋, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        // const loading = this.$loading({
                        //     lock: true,
                        //     text: 'Loading',
                        //     spinner: 'el-icon-loading',
                        //     background: 'rgba(0, 0, 0, 0.7)'
                        // });
                        buildingModel.buildingDelete(node.data.id).then(data => {
                            if (data.status == 'fail') {
                                Notification({
                                    message: data.msg,
                                    type: 'warning'
                                })
                                // loading.close()
                                return
                            }
                            Notification({
                                message: '删除成功',
                                type: 'success'
                            })
                            // loading.close()
                            this.listTree()
                        })
                    }).catch(() => {
                    })
                } else {
                    if (data.children && data.children.length) {
                        Notification({
                            message: '请先删除该户型下的所有楼栋',
                            type: 'warning'
                        })
                    } else {
                        this.$confirm('此操作将永久删除该户型, 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            houseTypeModel.houseTypeDelete(node.data.id).then(data => {
                                if (data.status == 'fail') {
                                    Notification({
                                        message: data.msg,
                                        type: 'warning'
                                    })
                                    // loading.close()
                                    return
                                }
                                Notification({
                                    message: '删除成功',
                                    type: 'success'
                                })
                                // loading.close()
                                this.getHouseType()
                                this.listTree()
                            })
                        }).catch(() => {
                        })
                    }
                }

            },
            /* /!**
              * 删除楼栋
              * @param node
              *!/
             removeBuild(node) {
                 this.$confirm('此操作将永久删除该楼栋, 是否继续?', '提示', {
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
                     buildingModel.buildingDelete(node.data.id).then(data => {
                         if (data.status == 'fail') {
                             Notification({
                                 message: data.msg,
                                 type: 'warning'
                             })
                             loading.close()
                             return
                         }
                         Notification({
                             message: '删除成功',
                             type: 'success'
                         })
                         loading.close()
                         this.$emit('b-finish')
                     })
                 }).catch(() => {
                 })
             },*/
            /**
             * 修改户型
             * @param node
             * @param data
             */
            modifiy(node, data) {
                if (data.disabled == true) {
                    this.getHouseType()
                    buildingModel.buildingLoad(node.data.id).then(data => {
                        this.mode = 'update'
                        this.editFormBuilding = data.entity
                        this.$store.commit('endLoading')
                    })
                    this.editFormBuildingVisible = true
                } else {
                    houseTypeModel.houseTypeLoad(node.data.id).then(data => {
                        this.mode = 'update'
                        this.editFormHouseType = data.entity
                        this.editFormHouseTypeVisible = true
                    })
                }
            },
            /**
             * select show  houseType(户型)
             */
            getHouseType() {
                let projectInfo = this.projectInfo;
                houseTypeModel.getHouseTypeByProjectId(projectInfo.id).then((data) => {
                    this.arrHouseType = data.entity
                })
            },
            /* /!**
              * 修改楼栋
              * @param node
              * @param data
              *!/
             modifiyBuilding(node, data) {
                 this.$store.commit('startLoading')
                 this.getHouseType()
                 buildingModel.buildingLoad(node.data.id).then(data => {
                     this.mode = 'update'
                     this.editFormBuilding = data.entity
                     this.$store.commit('endLoading')
                 })
                 this.editFormBuildingVisible = true
             },*/
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.floorQuery.pageInfo.pageSize = val
                this.projectList()
            },
            handleCurrentChange(val) {
                this.floorQuery.pageInfo.currentPage = val
                this.projectList()
            },
            handleSizeChangeS(val) {
                this.pageInfoS.pageSize = val
                this.floors = []
                floorRelevanceModel.getFloorRelevanceByPage(this.buildingId, this.pageInfoS).then((data) => {
                    for (var i = 0; i < data.entity.list.length; i++) {
                        //  data.entity.list[i].floorNum= data.entity.list[i].floorNum+
                        var floorRelevanceQuery = {
                            buildId: this.buildingId,
                            floorId: data.entity.list[i].id
                        }
                        floorRelevanceModel.getProductsByBuildIdAndFloorId(floorRelevanceQuery).then((data1) => {
                            for (var i = 0; i < this.floors.length; i++) {
                                if (this.floors[i].id == data1.entity[0].id) {
                                    this.floors[i].floorVO_s = data1.entity;
                                    break;
                                }
                            }
                        })
                    }
                    this.floors = data.entity.list;
                })
            },
            handleCurrentChangeS(val) {
                this.pageInfoS.currentPage = val
                this.floors = []
                floorRelevanceModel.getFloorRelevanceByPage(this.buildingId, this.pageInfoS).then((data) => {
                    for (var i = 0; i < data.entity.list.length; i++) {
                        //  data.entity.list[i].floorNum= data.entity.list[i].floorNum+'层'
                        var floorRelevanceQuery = {
                            buildId: this.buildingId,
                            floorId: data.entity.list[i].id
                        }
                        floorRelevanceModel.getProductsByBuildIdAndFloorId(floorRelevanceQuery).then((data1) => {
                            for (var i = 0; i < this.floors.length; i++) {
                                if (this.floors[i].id == data1.entity[0].id) {
                                    this.floors[i].floorVO_s = data1.entity;
                                    break;
                                }
                            }
                        })
                    }
                    this.floors = data.entity.list;
                })
            },
            batchEditFinish() {
                this.editFormBatchVisible = false
            },
            batchEditCancel() {
                this.editFormBatchVisible = false
            },
            handlebriefCol() {
                this.show = false;
            },
            handleAllCol() {
                this.show = true;
            },
            mouseenter(node) {
                this.$set(node, 'del', true)
                this.$set(node, 'add', true)
                this.$set(node, 'edit', true)
            },
            mouseleave(node) {
                this.$set(node, 'del', false)
                this.$set(node, 'add', false)
                this.$set(node, 'edit', false)
            },
        },

    }
</script>


<style scoped>
    #tab {
        border: 1px lavender solid;
    }

    #tab button.icon-style {
        padding: 5px;
        margin-left: 0;
    }

    el-aside {
        background-color: #D3DCE6;
        color: #333;
        text-align: center;
        line-height: 200px;
    }

    el-main {
        background-color: #E9EEF3;
        color: #333;
        text-align: center;
        /* line-height: 160px; */
    }
</style>
<style>
    .building-andHouse-type-tree .el-tree-node > .el-tree-node__children {
        overflow: initial;
    }

    .custom-table thead.is-group tr:nth-child(1) th {
        background-color: #ed7d31 !important;
        color: #ffffff !important;
    }

    .custom-table thead.is-group tr:nth-child(2) th {
        background-color: #4472c4 !important;
        color: #ffffff !important;
    }

    .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
        background: #409eff;
    }

    .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content .custom-tree-node {
        color: #fff;
    }

    .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content .custom-tree-node span i {
        color: #fff;
    }
</style>
