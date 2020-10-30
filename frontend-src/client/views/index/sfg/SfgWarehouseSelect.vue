<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true" :model="sfgQuery" ref="sfgQuery">
                            <el-form-item prop="sfgNo">
                                <el-input
                                        placeholder="半成品编码"
                                        v-model="sfgQuery.sfgNo"
                                        style="width:150px"
                                        clearable>
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="sfgName">
                                <el-input
                                        placeholder="半成品名称"
                                        v-model="sfgQuery.sfgName"
                                        style="width:150px"
                                        clearable>
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="buildingId" >
                                <el-select
                                        style="width:110px"
                                        placeholder="楼栋"
                                        v-model="sfgQuery.buildingId"
                                        size="mini"
                                        clearable
                                        @change="changeBuilding"
                                >
                                    <el-option
                                            v-for="item in buildings"
                                            :key="item.id"
                                            :label="item.buildCode"
                                            :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="floorId" >
                                <el-select
                                        style="width:110px"
                                        placeholder="楼层"
                                        :disabled="sfgQuery.buildingId==''"
                                        v-model="sfgQuery.floorId"
                                        clearable
                                        size="mini"
                                >
                                    <el-option
                                            v-for="item in floors"
                                            :key="item.id"
                                            :label="item.floorNum"
                                            :value="item.id"
                                    >
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-form>
                    </el-col>
                    <el-col>
                        <my-search-button-group :model='searchButtonData'></my-search-button-group>
                    </el-col>
                </el-row>
                <el-row>
                    <my-button-group :model="buttonData"></my-button-group>
                </el-row>
            </el-header>
            <el-main
                    ref="mainContent"
                    :style="`height:${this.$store.state.mainContentHeight}px`"
            >
                <el-table
                        :data="sfgData"
                        @row-click="clickRow"
                        ref="moviesTable"
                        @selection-change="handleSelectionChange"
                        size="mini"
                        stripe
                        border
                        height="100%"
                >
                    <el-table-column
                            type="selection"
                            width="80">
                    </el-table-column>
                    <el-table-column
                            prop="sfgNo"
                            label="半成品编码"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="sfgName"
                            label="半成品名称"
                    >
                    </el-table-column>
                    <!--<el-table-column
                            prop="sfgSpecification"
                            label="半成品规格"
                    >
                    </el-table-column>-->
                    <el-table-column
                            prop="projectName"
                            label="项目名称"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="buildCode"
                            label="楼栋"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="floorNum"
                            label="楼层"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="needNum"
                            label="需求量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="haveReceiptNum"
                            label="已入库量"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="unitCNName"
                            label="单位"
                    >
                    </el-table-column>
                </el-table>
            </el-main>
            <el-footer ref="footOperationBar">
                <!--      <div class="classButton">
                          <el-col>
                              <el-button type="success" v-on:click="addClick">添加</el-button>
                              <el-button type="danger" @click="deleteClick">关闭</el-button>
                          </el-col>
                      </div>-->
                <el-row>
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="sfgQuery.pageInfo.currentPage"
                            :page-sizes="sfgQuery.pageInfo.pageSizes"
                            :page-size="sfgQuery.pageInfo.pageSize"
                            :total="sfgQuery.pageInfo.total"
                            layout="total, sizes, prev, pager, next, jumper"
                            background
                    >
                    </el-pagination>
                </el-row>
            </el-footer>
            <el-dialog
                    :visible.sync="showSelectTree"
                    width="340px"
                    append-to-body
            >
                <tree-info
                        :treeArr="treeArr"
                        :baseCheckedId="baseCheckedId"
                        :baseCheckParentId="baseCheckParentId"
                        @tree-edit-submit="checkoutStatusFinish"
                        @tree-edit-cancel="checkoutStatusCancel"
                ></tree-info>
            </el-dialog>

        </el-container>
    </div>
</template>

<script>
    import sfgModel from "../../../model/basicdata/sfg";
    import categoryModel from "../../../model/basicdata/category";
    import treeInfo from "../basic/materialStorage/TreeInfo.vue";
    import buildingModel from "../../../model/project/building";
    import floorModel from "../../../model/project/floor";
    import projectModel from "../../../model/project/project";

    export default {
        name: "SfgWarehouseSelect",
        components: {
            'tree-info': treeInfo,
        },
        props: ['sfgAddList','projectId','planDetails'],
        data() {
            return {
                buildings: [],
                floors: [],
               /* projectList: '',*/
                treeArr: '',
                showSelectTree: false,
                baseCheckedId: [],
                baseCheckParentId: [],
                selected: [],
                sfgData: [],
                arrCategory: '',
                sfgQuery: {
                    sfgNo: '',
                    sfgName: '',
                    sfgSpecification: '',
                    categoryId: '',
                    buildingId: null,
                    floorId: null,
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    },
                },
                buttonData: [
                    {
                        name: '添加',
                        event: 'addClick'
                    },
                ],
                searchButtonData: [
                    {
                        name: '查询',
                        event: "submitForm('sfgQuery')"
                    },
                    {
                        name: '重置',
                        event: 'resetForm("sfgQuery")'
                    }
                ],
                rowButtonData: [
                    {
                        name: '编辑',
                        event: "editClick",
                    },
                ]

            }
        },
        mounted() {
            this.showCategory()
            log(1231231321)
            this.showList()
        },
        methods: {
            showCategory() {
                categoryModel.getCategorysToTreeParentDisabled().then((data) => {
                    this.treeArr = data.entity
                })
                categoryModel.getCategorysByOrgId().then((data) => {
                    this.arrCategory = data.entity
                })
                buildingModel.findByProjectId(this.projectId).then(data => {
                    this.buildings = data.entity
                })
            },
            showList() {
                if (this.sfgQuery.buildingId == null || this.sfgQuery.buildingId == '') {
                    this.sfgQuery.buildingId = ''
                }
                if (this.sfgQuery.floorId == null || this.sfgQuery.floorId == '') {
                    this.sfgQuery.floorId = ''
                }
                this.sfgQuery.projectId = this.projectId
                categoryModel.findAllByQuery(this.sfgQuery).then((data) => {
                    let list = data.entity.list
                    console.log("this.list------->",data.entity.list)

                    list = list.filter(m => {
                        return !this.sfgAddList.some(s => s.sfgId == m.id && s.buildingId == m.buildingId && s.floorId == m.floorId)
                    })
                    this.sfgData = list
                    //filter过滤后数据的总数量
                    let total = 0;
                    if (data.entity.list != null && list != null) {
                        total = data.entity.list.length - list.length
                    }
                    this.sfgQuery.pageInfo.total = data.entity.totalCount - this.sfgAddList.length
                })
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        console.log('submitForm');//TO DELETE
                        this.showList()
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            addClick() {
                if (this.selected.length == 0) {
                    this.$message('请选择增加项');
                    return;
                }
                //存放要添加的原料编码
                let arr = []
                this.selected.forEach((row) => {
                    const sfg = {
                        sfgNo: row.sfgNo,
                        sfgName: row.sfgName,
                        sfgSpecification: row.sfgSpecification,
                        unitCNName: row.unitCNName,
                        sfgId: row.id,
                        num: '',
                        needNum: row.needNum,
                        haveReceiptNum: row.haveReceiptNum,
                        projectName: row.projectName,
                        buildCode: row.buildCode,
                        floorNum: row.floorNum,
                        buildingId: row.buildingId,
                        floorId: row.floorId,


                    }
                    arr.push(sfg)//把选中的构件放到数组中
                })
                //第一个参数是父组件中v-on绑定的自定义回调方法，第二个参数为传递的参数
                this.$emit('sfg-warehouse-select-finish', arr);
                //  this.showList()

            },
            deleteClick() {
                this.$emit('sfg-warehouse-select-cancel')
                this.showList()
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.sfgQuery.pageInfo.pageSize = val
                this.showList()
            },
            handleCurrentChange(val) {
                this.sfgQuery.pageInfo.currentPage = val
                this.showList()
            },
            //点击整行都可以进行勾选
            clickRow(row) {
                this.$refs.moviesTable.toggleRowSelection(row)
            },
            resetForm(formName) {
                this.sfgQuery.categoryId = ''
                this.sfgQuery.categoryName = ''
                this.$refs[formName].resetFields();
            },
            showSelectTrees() {
                this.baseCheckedId.splice(0, this.baseCheckedId.length);
                this.baseCheckedId.push(this.sfgQuery.categoryId);

                this.baseCheckParentId.splice(0, this.baseCheckedId.length);
                this.baseCheckParentId.push(this.sfgQuery.categoryId);

                this.showSelectTree = true
            },
            checkoutStatusFinish(id, name) {
                //子插件回传赋值
                this.sfgQuery.categoryName = name;
                this.sfgQuery.categoryId = id;
                this.showSelectTree = false
            },
            checkoutStatusCancel() {
                this.showSelectTree = false
            },
            changeBuilding(id) {
                if (id == '') {
                    id = 0
                }
                this.sfgQuery.floorId = ''
                this.floors = []
                let building = this.buildings.find(b => b.id == id)
                if (building != null) {
                    floorModel.findByBuildingIdAndProjectId(building.id, building.projectId).then((data) => {
                        this.floors = data.entity
                        this.sfgQuery.floorId = ''
                    })
                }
            },
        },
        watch: {
            projectId: function () {
                this.showCategory()
            }
        },
    }
</script>

<style scoped>

</style>
