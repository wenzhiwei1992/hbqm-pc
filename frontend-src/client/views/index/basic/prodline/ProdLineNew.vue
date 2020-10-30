<template>
    <div>
        <el-button type="primary" class="el-icon-edit-outline" v-if="authorized('prodLineNew:newEdit')" @click="$router.push('prodLineManage')">新增/编辑产线</el-button>
        <el-container>
            <el-aside width="200px">
                <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick"
                         node-key="id"
                         :highlight-current="highlightCurrent"
                         :default-expanded-keys="[0]"
                         :default-checked-keys="checkedKeys"
                ></el-tree>
            </el-aside>
            <el-main>
                <el-tabs v-model="activeName" @tab-click="handleClick">
                    <el-tab-pane label="工艺路径" name="first">
                        <el-table
                          :data="pathDatas"
                          size="mini"
                          stripe
                          border
                          height="500"
                          style="width: 100%"
                        >
                            <!-- <el-table-column
                                    type="selection"
                                    width="80">
                            </el-table-column> -->
                            <el-table-column
                                    type="index"
                                    label="序号"
                                    width="50">
                            </el-table-column>
                            <el-table-column
                                    prop="pathCode"
                                    label="路径编码"
                                    width="120">
                            </el-table-column>
                            <el-table-column
                                    prop="pathName"
                                    label="路径名称"
                                    width="120">
                            </el-table-column>

                            
                            <el-table-column
                                    prop="pathDesc"
                                    label="路径描述"
                                    width="120">
                            </el-table-column>
                            <el-table-column
                                    prop="prodLine.lineName"
                                    label="所属产线"
                                    width="120">
                            </el-table-column>
                            <!-- <el-table-column
                                    prop="dateCreated"
                                    :formatter="formatterDate"
                                    label="创建时间"
                                    width="180">
                            </el-table-column>
                            <el-table-column
                                    prop="dateModified"
                                    :formatter="formatterDate"
                                    label="修改时间"
                                    width="180">
                            </el-table-column> -->
                            <el-table-column
                                    label="操作"
                                    width="320"
                                    align="center">
                                <template slot-scope="scope">
                                  <el-tooltip
                                          effect="light"
                                          content="显示工序"
                                          placement="top"
                                  >
                                    <el-button icon="el-icon-view" size="mini" @click="showBacklogs(scope.row)"></el-button>
                                  </el-tooltip>
                                  <el-tooltip
                                          effect="light"
                                          content="新增/编辑"
                                          placement="top"
                                  >
                                    <el-button icon="el-icon-edit-outline" size="mini" type="primary" v-if="authorized('prodLineNew:newEdit')" @click="$router.push({name:'pathManage'})"></el-button>
                                  </el-tooltip>
                                  <el-tooltip
                                          effect="light"
                                          content="删除"
                                          placement="top"
                                  >
                                    <el-button icon="el-icon-delete" size="mini" type="danger" @click="deletePath(scope.row)"></el-button>
                                  </el-tooltip>
                                    
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-tab-pane>
                    <el-tab-pane label="加工工序" name="second">
                        <el-table
                                :data="backlogDatas"
                                @selection-change="handleSelectionChange"
                                size="mini"
                                stripe
                                border
                                style="width: 100%"
                                height="500"
                        >
                            <!-- <el-table-column
                                    type="selection"
                                    width="80">
                            </el-table-column> -->
                            <el-table-column
                                    prop="backlogCode"
                                    label="工序编码"
                            width="120">
                            </el-table-column>
                            <el-table-column
                                    prop="backlogName"
                                    label="工序名称"
                                    width="140"
                            >
                            </el-table-column>
                            <el-table-column
                                    prop="type"
                                    label="工序类型"
                                    width="120">

                            </el-table-column>
                            <el-table-column
                                    prop="makeSpan"
                                    label="工序节拍"
                                    width="120">
                            </el-table-column>
                            <!-- <el-table-column
                                    prop="source"
                                    label="工序资源"
                                    width="120"
                                    show-overflow-tooltip>
                            </el-table-column> -->
                            <el-table-column
                                    prop="prodLine.lineName"
                                    label="所属产线"
                                    width="120">
                            </el-table-column>
                            <el-table-column
                                    label="操作"
                                    width="320"
                                    align="center">
                                <template slot-scope="scope">
                                    <el-button size="mini" @click="showStations(scope.row)">显示工位</el-button>
                                    <el-button size="mini" type="primary" v-if="authorized('prodLineNew:newEdit')"  @click="$router.push({name:'backlogManage'})">新增/编辑</el-button>
                                    <el-button size="mini" type="danger" @click="deleteBacklog(scope.row)">删除</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        
                    </el-tab-pane>
                    <el-tab-pane label="加工工位" name="third">
                        <el-table
                                :data="stationDatas"
                                @selection-change="handleSelectionChange"
                                size="mini"
                                stripe
                                border
                                height="500"
                                style="width: 100%"
                        >
                            <!-- <el-table-column
                                    type="selection"
                                    width="80">
                            </el-table-column> -->

                            <!--<el-table-column-->
                                    <!--prop="backlogId"-->
                                    <!--label="所属工序"-->
                                    <!--width="120">-->
                            <!--</el-table-column>-->

                            <el-table-column
                                    prop="stationName"
                                    label="工位名称"
                                    width="120">
                            </el-table-column>
                            <el-table-column
                                    prop="stationCode"
                                    label="工位编码"
                                    width="120">
                            </el-table-column>
                            <el-table-column
                                    prop="remark"
                                    label="备注"
                                    width="120">
                            </el-table-column>
                            <el-table-column
                                    prop="prodLine.lineName"
                                    label="所属产线"
                                    width="120">
                            </el-table-column>
                            <!-- <el-table-column
                                    prop="dateCreated"
                                    :formatter="formatterDate"
                                    label="创建时间"
                                    width="180">
                            </el-table-column> -->
                            <el-table-column
                                    label="操作"
                                    width="240"
                                    align="center">
                                <template slot-scope="scope">
                                    <el-button size="mini" type="primary" v-if="authorized('prodLineNew:newEdit')" @click="$router.push({name:'stationManage'})">新增/编辑</el-button>
                                    <el-button size="mini" type="danger" v-if="authorized('prodLineNew:delete')" @click="deleteStation(scope.row)">删除</el-button>
                                </template>
                            </el-table-column>
                        </el-table>

                    </el-tab-pane>
                    <!-- <el-row type="flex" justify="space-around" >
                        <el-col style="margin-top:15px ;text-align:left">
                            <el-button size="mini" type="primary" v-if="authorized('prodLineNew:newEdit')" @click="pageNewClick" >新建/编辑</el-button>
                        </el-col>
                    </el-row> -->
                </el-tabs>
            </el-main>

        </el-container>
        <el-dialog :visible.sync="ifStation">
            <el-table
                    align="center"
                    ref="multipleTable"
                    :data="stationsByBacklog"
                    tooltip-effect="dark"
                    size="mini"
                    stripe
                    border
                    style="width: 100%">
                <el-table-column
                        prop="stationCode"
                        label="工位代码"
                        align="center"
                        width="160">
                </el-table-column>
                <el-table-column
                        prop="stationName"
                        label="工位名称"
                        align="center"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="remark"
                        align="center"
                        label="备注"
                        show-overflow-tooltip>
                </el-table-column>
            </el-table>
        </el-dialog>
        <el-dialog :visible.sync="ifBackLog">
            <el-table
                    ref="multipleTable"
                    :data="backLogByPath"
                    stripe
                    border
                    tooltip-effect="dark"
                    style="width: 100%"
                    >
                <el-table-column
                        prop="backlogCode"
                        label="工序编码">
                </el-table-column>
                <el-table-column
                        prop="backlogName"
                        label="工序名称">
                </el-table-column>
                <el-table-column
                        prop="type"
                        label="工序类型">
                </el-table-column>
                <el-table-column
                        prop="makeSpan"
                        label="工序耗时">
                </el-table-column>
                <!-- <el-table-column
                        prop="source"
                        label="工序资源"
                        show-overflow-tooltip>
                </el-table-column> -->
            </el-table>
        </el-dialog>
    </div>

</template>

<script>
    import pathModel from '../../../../model/prodline/path'
    import backlogModel from '../../../../model/prodline/backlog'
    import prodlineNew from  '../../../../model/prodline/prodlineNew.js'
    import stationModel from '../../../../model/prodline/station'
    import menuTabService from '../../../../service/menuTabService'
    import {Notification} from 'element-ui'
    import moment from "moment";
    export default {
        data(){
            return{
                highlightCurrent:true,
                activeName: 'first',
                checkedKeys:[],
                data: [
                    {
                        id:'0',
                        label: '产线',
                        children: [{
                            id:'',
                            label: '',
                        }]
                    }
                ],
                pageInfo:{
                    currentPage:1,
                    total:0,
                    pageSize:this.GLOBAL.pageSize,
                    pageSizes:this.GLOBAL.pageSizes
                },

                defaultProps: {
                    children: 'children',
                    label: 'label'
                },
                pathDatas: [],//工艺路径
                backlogDatas:[],//工序
                stationDatas:[],//工位
                selected: [],
                prodlineForCurrent:'',
                backLogByPath:[],//工艺路径点击查看显示工序
                stationsByBacklog:[],//工序点击查看按钮显示的工位
                ifBackLog:false,
                ifStation:false,
                routerName:'',

            }
        },
        mounted() {
            this.list();
        },
        methods:{
            deleteStation(row){
                this.$confirm('确认删除?','提示',{
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading')
                    stationModel.delete(row.id).then(data=>{
                        this.$store.commit('endLoading')
                        if(data.status=='failed'){
                            Notification({
                                type:'warning',
                                message:data.msg
                            })
                        }
                        let tab = {};
                        tab.index = 2;
                        tab.label='加工工位'
                        this.handleClick(tab);
                    })
                }).catch(()=>{

                })
            },
            deleteBacklog(row){
                //删除工序
                this.$confirm('确认删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading')
                    backlogModel.delete(row.id).then(data => {
                        this.$store.commit('endLoading')
                        if(data.status=='failed'){
                            Notification({
                                type:'warning',
                                message:data.msg
                            })
                        }
                        let tab = {};
                        tab.index = 1;
                        tab.label='加工工序'
                        this.handleClick(tab);
                    })
                }).catch((err) => {

                })
            },
            deletePath(row){
                //删除路径
                this.$confirm('确认删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$store.commit('startLoading')
                    pathModel.delete(row.id).then(data => {
                        this.$store.commit('endLoading')
                        if(data.status=='failed'){
                            Notification({
                                type:'warning',
                                message:data.msg
                            })
                        }
                        let tab = {};
                        tab.index = 0;
                        tab.label='工艺路径'
                        this.handleClick(tab);
                    })
                }).catch(() => {

                })
            },
            pageNewClick(){
                this.$router.push(this.routerName);
            },
            showBacklogs(row){
                this.$store.commit('startLoading')
                pathModel.getBackLogByPathId(row.id).then(data => {
                    this.backLogByPath = data.entity;
                    this.ifBackLog = true;
                    this.$store.commit('endLoading')
                }).catch(err => {
                    Notification({
                        message: err.msg,
                        type: "warning"
                    })
                })
            },
            showStations(row){
                this.$store.commit('startLoading');
                //获取与该记录关联的工位
                backlogModel.getStationsByBackLogId(row.id).then(data => {
                    this.stationsByBacklog = data.entity;
                    this.ifStation = true;
                    this.$store.commit('endLoading')
                }).catch(err => {
                    Notification({
                        message: err.msg,
                        type: "warning"
                    })
                })
            },
            formatterDate(row, column, cellValue, index){
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            list(){
                this.routerName = 'pathManage';
                //默认加载第一个标签页,加载树形结构
                prodlineNew.getAllProdLinesForTree().then(data=>{
                    this.data[0].children = data.entity;
                    let lineId = this.data[0].children[0].id;
                    this.prodlineForCurrent = lineId;
                    this.checkedKeys.push(lineId);
                    //根据lineId 获取工艺路径集合
                    prodlineNew.getPathsByProdlineId(lineId).then(data2=>{
                        this.pathDatas = data2.entity;
                        //this.pageInfo.total=data.entity.totalElements
                    })
                });

            },
            handleClick(tab, event) {
                if(this.prodlineForCurrent==undefined || this.prodlineForCurrent==''){
                    Notification({
                        type:'info',
                        message:'请先添加产线'
                    })
                    return;
                }
                if(tab.label == '工艺路径'){
                    this.routerName = 'pathManage';
                }else if(tab.label == '加工工序'){
                    this.routerName = 'backlogManage';
                }else if(tab.label == '加工工位'){
                    this.routerName = 'stationManage';
                }
                let filter ={
                    index:tab.index,
                    label:tab.label,
                    prodline:this.prodlineForCurrent,
                    pageInfo: this.pageInfo
                }
                prodlineNew.getTabDatasByProdLine(filter).then(data => {
                    this.pathDatas = data.entity;
                    this.backlogDatas = data.entity;
                    this.stationDatas = data.entity;
                })

            },
            handleNodeClick(data){
                //根据data 的label ，刷新右侧的页面，更新产线id
                this.prodlineForCurrent = data.id;
                //根据lineId 获取工艺路径集合
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                prodlineNew.getPathsByProdlineId(this.prodlineForCurrent).then(data2=>{
                    this.pathDatas = data2.entity;

                })
                //同时把标签页置为first
                this.activeName = 'first'
                setTimeout(loading.close(),500)

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
            authorized(index){
                return menuTabService.authorized(this.$store,index)
            }
        }
    }

</script>

<style scoped>

    .el-aside {
        color: #333;
        text-align: center;
        line-height: 100%;
        
    }
    .el-tree{
        background-color: transparent;
        border:1px solid #EBEEF5;
        height:555px;
    }

    .el-main {

        margin-top: -20px;
        color: #333;
        text-align: center;

        line-height: 100%;
    }

    body > .el-container {
        margin-bottom: 40px;
    }

    .el-container:nth-child(5) .el-aside,
    .el-container:nth-child(6) .el-aside {
        line-height: 260px;
    }

    .el-container:nth-child(7) .el-aside {
        line-height: 320px;
    }
    .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content{
        background-color: #368df1 !important;
    }

</style>