<template>
    <div class="currency-style-box">
        <el-container>
            <el-header ref="headOperationBar">
                <el-row>
                    <el-col>
                        <el-form :inline="true" :model="materialStorageQuery" ref="materialStorageQuery">
                            <el-form-item prop="materialStorageIdS">
                                <el-input
                                        placeholder="原料编码"
                                        v-model="materialStorageQuery.materialStorageIdS"
                                        style="width:150px"
                                        clearable>
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="materialName">
                                <el-input
                                        placeholder="原料名称"
                                        v-model="materialStorageQuery.materialName"
                                        style="width:150px"
                                        clearable>
                                </el-input>
                            </el-form-item>
                            <el-form-item prop="categoryId">
                                <el-input
                                        placeholder="类别"
                                        v-model="materialStorageQuery.categoryName"
                                        @focus="showSelectTrees"
                                ></el-input>
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
                        :data="materialStaroges"
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
                            prop="materialStorageId"
                            label="原料编码"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="materialName"
                            label="原料名称"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="materialSpecification"
                            label="原料规格"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="materialUnit"
                            label="单位"
                    >
                    </el-table-column>
                    <el-table-column
                            prop="categoryName"
                            label="原料类型"
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
                            :current-page="materialStorageQuery.pageInfo.currentPage"
                            :page-sizes="materialStorageQuery.pageInfo.pageSizes"
                            :page-size="materialStorageQuery.pageInfo.pageSize"
                            :total="materialStorageQuery.pageInfo.total"
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
    import materialStorageModel from "../../../../model/basicdata/materialStorage";
    import categoryModel from "../../../../model/basicdata/category";
    import treeInfo from '../materialStorage/TreeInfo.vue'
    export default {
        name: "SemiFinishedGoodsSelect",
        components: {
            'tree-info': treeInfo,
        },
        props: ['sfgMatlDtoList'],
        data() {
            return {
                treeArr: '',
                showSelectTree: false,
                baseCheckedId: [],
                baseCheckParentId: [],
                selected: [],
                materialStaroges: [],
                arrCategory: '',
                materialStorageQuery: {
                    materialStorageIdS: '',
                    materialName: '',
                    materialSpecification: '',
                    categoryId: '',
                    categoryName:'',
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
                        event: "submitForm('materialStorageQuery')"
                    },
                    {
                        name: '重置',
                        event: 'resetForm("materialStorageQuery")'
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
            this.showList()
            this.showCategory()
        },
        methods: {
            showList() {
                materialStorageModel.getProductMaterialStorages(this.materialStorageQuery).then((data) => {
                    console.log('list----------->',data.entity.list)
                    let list = data.entity.list
                    list = list.filter(m => {
                        return !this.sfgMatlDtoList.some(sm => sm.matlId == m.id)
                    })
                    this.materialStaroges = list
                    //filter过滤后数据的总数量
                    let total = 0;
                    if (data.entity.list != null && list != null) {
                        total = data.entity.list.length - list.length
                    }
                    this.materialStorageQuery.pageInfo.total = data.entity.totalCount - this.sfgMatlDtoList.length
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
                    const material = {
                        materialStorageId: row.materialStorageId,
                        materialName: row.materialName,
                        materialSpecification: row.materialSpecification,
                        materialUnit: row.materialUnit,
                        matlId: row.id,
                        num: '',
                    }
                    arr.push(material)//把选中的构件放到数组中
                })
                //第一个参数是父组件中v-on绑定的自定义回调方法，第二个参数为传递的参数
                this.$emit('sfg-select-finish', arr);
                //  this.showList()

            },
            deleteClick() {
                this.$emit('sfg-select-cancel')
                this.showList()
            },
            showCategory() {
                categoryModel.getCategorysToTreeParentDisabled().then((data) => {
                    this.treeArr = data.entity
                })
                categoryModel.getCategorysByOrgId().then((data) => {
                    this.arrCategory = data.entity
                })
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },
            handleSizeChange(val) {
                this.materialStorageQuery.pageInfo.pageSize = val
                this.showList()
            },
            handleCurrentChange(val) {
                this.materialStorageQuery.pageInfo.currentPage = val
                this.showList()
            },
            //点击整行都可以进行勾选
            clickRow(row) {
                this.$refs.moviesTable.toggleRowSelection(row)
            },
            checkoutStatusFinish(id, name) {
                //子插件回传赋值
                this.materialStorageQuery.categoryName = name;
                this.materialStorageQuery.categoryId = id;
                this.showSelectTree = false
            },
            checkoutStatusCancel() {
                this.showSelectTree = false
            },
            showSelectTrees() {
                this.baseCheckedId.splice(0, this.baseCheckedId.length);
                this.baseCheckedId.push(this.materialStorageQuery.categoryId);

                this.baseCheckParentId.splice(0, this.baseCheckedId.length);
                this.baseCheckParentId.push(this.materialStorageQuery.categoryId);

                this.showSelectTree = true
            },
        },

    }
</script>

<style scoped>

</style>
