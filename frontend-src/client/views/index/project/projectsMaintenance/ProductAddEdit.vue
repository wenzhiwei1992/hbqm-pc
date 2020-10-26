<template>
    <div>
        <div class="demo-input-size">
            <el-form :inline="true" id="productQuery" :model="productQuery" ref="productQuery">
                <el-form-item label="构件编码" prop="productNo">
                    <el-input
                            placeholder="请输入内容"
                            v-model="productQuery.productNo"
                            style="width:150px"
                            clearable>
                    </el-input>
                </el-form-item>
                <el-form-item label="构件名称" prop="productNo">
                    <el-select v-model="valueBD" placeholder="类型" style="width: 70px" collapse-tags
                               @change="changeProductNameInput">
                        <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                    <el-input
                            placeholder="请输入内容"
                            :disabled="valueBD==''"
                            v-model="productQuery.productName"
                            style="width:150px"
                            clearable>
                    </el-input>
                </el-form-item>
                <el-form-item label="构件类别" prop="typwpeId">
                    <el-select v-model="productQuery.productTypeId" clearable placeholder="请选择">
                        <el-option
                                v-for="item in arrProductType"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <!--                            <el-form-item label="产线" prop="line">
                                                 <el-select v-model="valueSelectLine" placeholder="请选择">
                                                     <el-option
                                                             v-for="item in this.arrLine"
                                                             :key="item.value"
                                                             :label="item.label"
                                                             :value="item.value">
                                                     </el-option>
                                                 </el-select>
                                              </el-form-item>-->
                <el-form-item>
                    <el-button plain @click="submitForm('productQuery')">查询</el-button>
                </el-form-item>
            </el-form>
        </div>
        <el-table
                :data="productsInfo"
                size="mini"
                stripe
                border
                @selection-change="handleSelectionChange"
                height="377"
                @row-click="clickRow"
                ref="moviesTable"
        >
            <el-table-column
                    type="selection"
                    width="40">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="id"
                    v-if="show"
                    label="ID"
                    width="150">
            </el-table-column>
            <el-table-column
                    prop="productNo"
                    label="构件编码"
                    width="230"
                    style="text-align: center">
            </el-table-column>
            <el-table-column
                    prop="productName"
                    label="构件名称"
                    width="230"
                    style="text-align: center">
            </el-table-column>

            <el-table-column
                    prop="name"
                    label="构件类型"
                    width="230"
                    style="text-align: center">
            </el-table-column>
            <el-table-column
                    prop="moldName"
                    label="模具"
                    width="230">
            </el-table-column>
            <el-table-column
                    prop="projectConcrete"
                    label="砼方量m³"
                    width="225">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="productTypeId"
                    v-if="show"
                    label="ID"
                    width="150">
            </el-table-column>
        </el-table>
        <el-row type="flex" justify="space-around" align="middle" style="margin-top:15px">
            <el-col>
                <el-button @click="addProducts" type="success">添加</el-button>
                <el-button @click="editFormCancel" type="danger">关闭</el-button>
            </el-col>
            <el-col>
                <el-row type="flex" justify="end" align="middle">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="productQuery.pageInfo.currentPage"
                            :page-sizes="productQuery.pageInfo.pageSizes"
                            :page-size="productQuery.pageInfo.pageSize"
                            :total="productQuery.pageInfo.total"
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
    import productTypeModel from '../../../../model/basicdata/productType'
    import productModel from '../../../../model/project/product'
    import floorRelevanceModel from '../../../../model/project/floorRelevance'
    import {Notification} from 'element-ui'

    export default {
        props: ['editFormProductAdd', 'editFormProducts', 'floorId', 'productInfo', 'visible', 'products'],
        data() {
            return {
                arrProductType: [],
                show: '',
                // products:this.productsAdd,//避免子组件修改父组件传入信息
                productsInfo: [],
                ids: [],//存放该楼层已添加的构件
                productNew: [],
                selected: [],
                productQuery: {
                    selectType:'',
                    productNo: '',
                    productTypeId: '',
                    productName: '',
                    projectId: '',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    }
                },
                isShowProductName: false,//判断是否选中构件名称筛选框
                options: [
                    {
                        value: '',
                        label: ''
                    }, {
                        value: '1',
                        label: '模糊'
                    }, {
                        value: '2',
                        label: '精确'
                    },],
                valueBD: '1',
            }
        },

        mounted() {
            this.getProduct()
            this.getProductType()
        },
        methods: {
            // 点击整行都可以进行勾选
            clickRow(row) {
                this.$refs.moviesTable.toggleRowSelection(row)
            },
            /**
             * 分页查询所有构件(当所选楼层已有该构件时，不显示)
             */
            getProduct(){
                this.productQuery.floorId = this.floorId
                this.productQuery.projectId = this.editFormProducts.projectId
                this.productQuery.selectType = this.valueBD
                productModel.getFloorsByBuildingIdAndproductNameAndProductTypeId(this.productQuery).then((data) => {
                    let list = data.entity.list
                    list = list.filter(pd => {
                        return !this.products.some(d => d.id == pd.id)
                    })
                    this.productsInfo = list
                    let  total = 0;
                    if(data.entity.list!=null && list!=null){
                        total = data.entity.list.length - list.length
                    }
                    this.productQuery.pageInfo.total = data.entity.totalCount - total
                })
            },
            /**
             *模糊查询
             */
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.getProduct()
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },
            /**
             * productType下拉框赋值
             */
            getProductType() {
                //2019年8月15日14:16:41 李强 修改为查看单基地的构件类型
                //getAllproductTypes
                productTypeModel.getOrgProductTypes().then((data) => {
                    this.arrProductType = data.entity
                })
            },
            /**
             * 为楼层添加构件
             */
            addProducts() {
                if (this.selected.length == 0) {
                    this.$message('请选择增加项');
                    return;
                }
                var arrF = new Array()
                this.selected.forEach((row) => {
                    this.ids.push(row.id)
                    const trr = {
                        id: row.id,
                        floorId: this.floorId,
                        projectId: this.editFormProducts.projectId,
                        buildingId: this.editFormProducts.id,
                        num: 1,
                        productStatus: 0,//冗余字段,暂时没用
                        productNo: row.productNo,
                        productName: row.productName,
                        projectConcrete: row.projectConcrete,
                        moldName: row.moldName,
                        productTypeId: row.productTypeId,
                        name: row.name
                    }
                    arrF.push(trr)//把选中的构件放到数组中
                })
                //this.productsInfo = []
                this.$emit('product-add-edit-finish', arrF)
            },
            changeProductNameInput(id) {
                if (id != null) {
                    this.isShowProductName = true
                }
            },
            editFormCancel() {
                //关闭后重新打开初始化页面
                //this.products=[];
                this.productsInfo = []
                //  this.productQuery.pageInfo=''
                this.$emit('product-add-edit-cancel')
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },

            handleSizeChange(val) {
                this.productQuery.pageInfo.pageSize = val
                this.getProduct()
            },
            handleCurrentChange(val) {
                this.productQuery.pageInfo.currentPage = val
                this.getProduct()
            },
            handlebriefCol() {
                this.show = false;
            },
            handleAllCol() {
                this.show = true;
            }
        },

    }
</script>

<style scoped>

</style>