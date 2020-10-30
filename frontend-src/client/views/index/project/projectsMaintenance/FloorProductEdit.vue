<template>
    <div style="height: 500px;">
        <el-form :inline="true" :model="editFormProduct" v-if="editFormProduct" :rules="eidtFormProductRules"
                 ref="editFormProduct" label-width="80px">
            <el-form-item label="项目名称" prop="projectName">
                <el-input :disabled="true" v-model="editFormProduct.projectName"></el-input>
            </el-form-item>
            <el-form-item label="楼栋" prop="buildCode">
                <el-input :disabled="true" v-model="editFormProduct.buildCode"></el-input>
            </el-form-item>
            <el-form-item label="户型" prop="houseType">
                <el-input :disabled="true" v-model="editFormProduct.houseTypeName"></el-input>
            </el-form-item>
            <el-form-item label="楼层" prop="floor"><!--label-width="100px"-->
                <el-select style="width: 162px" v-model="editFormProduct.list.id" placeholder="请先选择楼层"
                           @change="getAllProductByFloorId">
                    <el-option
                            v-for="item in editFormProduct.list"
                            :key="item.id"
                            :label="item.floorNum"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="构件名称">
                <el-input @input="searchComponentName" v-model="componentName"></el-input>
            </el-form-item>
        </el-form>
        <div class="typeOneDiv">
            <div>

            </div>
            <el-table
                    :data="products"
                    size="mini"
                    ref="table"
                    stripe
                    border
                    @selection-change="handleSelectionChange"
                    height="350"
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
                >
                </el-table-column>
                <el-table-column
                        prop="productName"
                        label="构件名称"
                >
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="构件类型"
                        :filters="filterList"
                        :filter-method="filterHandler"
                        filter-placement="bottom-end"
                >
                </el-table-column>
                <el-table-column
                        prop="moldName"
                        label="模具"
                >
                </el-table-column>
                <el-table-column
                        prop="projectConcrete"
                        label="砼方量m³"
                >
                </el-table-column>
                <el-table-column
                        label="需求数"
                >
                    <template slot-scope="scope">
                        <el-input v-model="scope.row.num" onkeyup="value=value.replace(/[^\d]/g,'')"
                                  placeholder="请输入内容"></el-input>
                    </template>
                    <!--                  <template scope="scope" >
                                          <span v-if="scope.row.e!=2" >{{ scope.row.num }}</span>
                                          <span v-if="scope.row.e==2" class="cell-edit-input"><el-input v-model="inputNum"  placeholder="请输入内容"></el-input></span>
                                          <span v-if="scope.row.e!=2" style="margin-left:10px;" class="cell-icon"  @click="handleEdit(scope.row)">  <i class="el-icon-edit"></i> </span>
                                          <span v-if="scope.row.e==2"  style="margin-left:10px;"  class="cell-icon"  @click="handleSave(scope.row)">  <i class="el-icon-document"></i> </span>
                                      </template>-->
                </el-table-column>
                <!--               <el-table-column
                                       label="操作"
                                       width="200">
                                   <template slot-scope="scope">
                                       <el-button size="mini" @click="editClick(scope.row)">编辑</el-button>
                                       <el-button type="danger" size="mini" @click="deleteClick(scope.row)">删除</el-button>
                                   </template>
                               </el-table-column>-->
            </el-table>
            <el-row type="flex" justify="space-around" align="middle" style="margin-top:15px">
                <el-col>
                    <el-button v-if="IschooseFloor" @click="addProductClick" :disabled="true">添加构件</el-button>
                    <el-button v-else @click="addProductClick" :disabled="false" type="success">添加构件</el-button>
                    <el-button @click="deletesClick" type="danger">删除构件</el-button>
                    <el-button @click="editFormCancel" type="danger">关闭</el-button>
                    <el-button type="primary" @click="saveClick">保存</el-button>
                </el-col>
            </el-row>
            <el-dialog :title="productAddTitle" :visible.sync="editFormProductAddVisible" v-if="editFormProductAddVisible" width="65%" append-to-body>
                <product-add-edit :editFormProductAdd="editFormProductAdd"
                                  :editFormProducts="editFormProducts"
                                  :productInfo="productInfo"
                                  :floorId="floorId"
                                  :products="products"
                                  @product-add-edit-finish="productAddEditFinish"
                                  @product-add-edit-cancel="productAddEditCancel"
                ></product-add-edit>
            </el-dialog>
        </div>
    </div>
</template>

<script>
    import productModel from '../../../../model/project/product'
    import floorModel from '../../../../model/project/floor'
    import floorRelevanceModel from '../../../../model/project/floorRelevance'
    import productDetailModel from '../../../../model/project/productDetail'
    import ProductAddEdit from './ProductAddEdit.vue'
    import {Notification} from 'element-ui'

    export default {
        data() {
            return {
                num: 0,
                failNum: 0,
                pName: '',//构件名称
                productsAdd: [],//传入添加构件页面数据
                show: '',//控制table是否被隐藏
                test: [],
                frNum: '',//楼层下所包含构件更改前的数量
                pdNum: '',//构件生成构件明细的数量
                typwpeIdBig: '',//构件大类id
                frId: '',//构件关联表Id
                IschooseFloor: true,//判断是否选择楼层（控制页面添加按钮的显示）
                fId: '',//楼层添加完构件刷新页面
                florId1: '',//点击保存时保存修改数据（其中的floorId）对应saveClick方法
                productId1: '',//点击保存时保存修改数据（productId）
                buildId1: '',//点击保存时保存修改数据（buildId）
                inputNum: '',
                products: [],
                selected: [],
                floorId: '',
                projectId: '',//传入添加构件页面的值
                editFormProducts: '',
                productInfo: '',
                productQuery: {
                    productNo: '',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    }
                },
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize: this.GLOBAL.pageSize,
                    pageSizes: this.GLOBAL.pageSizes
                },
                //编辑添加构件
                editFormProductAdd: null,
                editFormProductAddVisible: false,
                eidtFormProductRules: {},
                componentName:'',
                backupsProducts:'',
                filterList:[]
            }
        },
        mounted() {


        },
        props: ['arrFloorNum', 'editFormProduct', 'productInfoS', 'visible'],
        components: {
            'product-add-edit': ProductAddEdit
        },
        computed: {
            productAddTitle() {

            }
        },
        methods: {
            // input模糊查询
            searchComponentName(){
                let self = this
                let componentName = self.componentName
                let products = self.products
                // 筛选需要备份数据
                let backupsProducts = self.backupsProducts
                products = self.Clone(backupsProducts)
                if(componentName){
                  self.products = products.filter(product=>{
                    return product.productName.toUpperCase().indexOf(componentName.toUpperCase())!==-1
                  })
                }else{
                  self.products = self.Clone(backupsProducts)
                }
                self.filterList = self.filterData(self.products,'name')
            },
            // 获取filters需要的数据
            filterData(data,key){
              let arr = []
              data.forEach(val=>{
                let str = JSON.stringify(arr)
                if(str.indexOf(val[key])===-1){
                  arr.push({
                    text:val[key],
                    value:val[key]
                  })
                }
              })
              return arr
            },
            // 自动获取筛选的表头
            filterHandler(value, row, column) {
              const property = column['property'];
              return row[property] === value;
            },
            checked() {
                //首先el-table添加ref="table"引用标识
                this.$refs.table.toggleAllSelection();
            },
            /**
             *楼栋所选楼层删除构件
             */
            deletesClick() {
                if (this.selected.length == 0) {
                    this.$message('请选择删除项');
                    return;
                }

                for(let i=this.selected.length;i--;){
                  for(let j=this.backupsProducts.length;j--;){
                    if(this.selected[i].id===this.backupsProducts[j].id){
                      this.backupsProducts.splice(j,1);
                    }
                  }
                }
                // 因为筛选需要备份数据  删除数据的时候给备份一起删除
                this.products = this.backupsProducts
            },
            /**
             *楼栋所选楼层保存构件
             */
            saveClick() {
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                let productsByFloorNumVO = {
                    building: '',
                    floorId: '',
                    floorVO_list:[],
                }
                productsByFloorNumVO.floorVO_list = this.products
               // productsByFloorNumVO.floorVO_ = row
                productsByFloorNumVO.building = this.productInfoS
                productsByFloorNumVO.floorId = this.fId
                floorRelevanceModel.saveProdcutFloorAndAddProductDetail(productsByFloorNumVO).then((data) => {
                    if (data.status == 'failed') {
                        Notification({
                            message: data.msg,
                            type: 'warning'
                        })
                        loading.close()
                        return
                    }
                    this.num++
                    if (this.num == 1) {
                        Notification({
                            message: '保存成功',
                            type: 'success'
                        })
                    }
                    loading.close()
                    this.getAllProductByFloorId(0);
                    this.$emit('product-edit-finish')
                })
            },
            /**
             *显示对应楼层构件
             */
            getAllProductByFloorId(value) {

                this.fId = value
                if (value == 0) {
                    this.IschooseFloor = true;
                } else {
                    this.IschooseFloor = false;
                }
                this.products = []
                floorRelevanceModel.showProductFloorByFloorNum(value).then((data) => {
                    // 备份products
                    this.backupsProducts = data.entity
                    this.products = data.entity
                    this.searchComponentName()
                    this.filterList = this.filterData(this.products,'name')
                })
                this.products.forEach(item => {
                    this.$refs.table.toggleRowSelection(item, true)
                });
                this.$emit('headCallBack', value); //第一个参数是父组件中v-on绑定的自定义回调方法，第二个参数为传递的参数
            },
            /**
             * 跳转到添加构件页面，为所选楼层添加构件
             */
            addProductClick() {
                productModel.productList(this.productQuery).then((data) => {
             /*       let entity = data.entity
                    let entity1 = ''
                    for (var i = 0; i < entity.length; i++) {
                        entity1 = entity[i]
                    }
                    this.productInfo = entity1.content*/
                    this.productInfo = data.entity.content
                })
                this.editFormProducts = this.productInfoS
                this.floorId = this.editFormProduct.list.id
                this.editFormProductAddVisible = true
             /*   productModel.getFloorsBySBuildingId(this.floorId, this.pageInfo).then((data) => {
                    this.productsAdd = data.entity.list
                    this.pageInfo.total = data.entity.totalCount;

                })*/
            },
            editFormCancel() {
                if (this.fId == '') {//如果不选择楼层点击关闭，默认楼层号为0
                    this.fId = 0
                }
                this.getAllProductByFloorId(0);
                this.$emit('product-edit-cancel');
               /* floorRelevanceModel.editFormCancelDeleteFloorRelevance(this.fId).then((data) => {
                    this.getAllProductByFloorId(0);
                    this.$emit('product-edit-cancel');
                })*/
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {

                    }
                })
            },
            //编辑添加构件表单
            productAddEditFinish(floorRelevancesList) {
                floorRelevancesList.forEach(pd => {
                  // 因为筛选需要备份数据  添加数据的时候给备份一起添加
                    this.backupsProducts.push(pd)
                    this.products = this.backupsProducts
                })
                this.editFormProductAddVisible = false
               // this.getAllProductByFloorId(this.fId)
            },
            productAddEditCancel() {
                this.editFormProductAddVisible = false
                //this.getAllProductByFloorId(this.fId)
            },
            handleSelectionChange(selected) {
                this.selected = selected
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

<style scoped lang="scss">
    .typeOneDiv {
        height: 500px;
        /deep/ .el-table__header-wrapper .el-table__column-filter-trigger i{
          color: red;
          font-size: 16px;
          font-weight: bold;
        }
    }

</style>