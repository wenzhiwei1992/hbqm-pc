<template>
    <div>
            <el-form :inline="true" id="queryInfo" :model="queryInfo" ref="queryInfo">
                <el-form-item label="构件编码" prop="productNo">
                    <el-input
                            placeholder="请输入内容"
                            v-model="queryInfo.productNo"
                            style="width:150px"
                            clearable>
                    </el-input>
                </el-form-item>
                <el-form-item label="构件名称" prop="productName">
                    <el-input
                            placeholder="请输入内容"
                            v-model="queryInfo.productName"
                            style="width:150px"
                            clearable>
                    </el-input>
                </el-form-item>
            <el-form-item label="构件类型" prop="typwpeName" @click.native="((type)=>{showSelectTrees(1)})">
                    <el-input
                            placeholder="请输入内容"
                            v-model="queryInfo.typwpeName"
                            style="width:150px"
                            >
                    </el-input>
                </el-form-item>
                <el-form-item label="构件类型" prop="typwpeId"  style="display:none">
                    <el-input
                            placeholder="请输入内容"
                            v-model="queryInfo.typwpeId"
                            style="width:150px"
                            clearable>
                    </el-input>
                </el-form-item>

                <el-form-item label="项目名称" prop="projectId">
                    <el-select v-model="queryInfo.projectId"  style="width:150px" placeholder="请选择">
                        <el-option
                                v-for="item in this.arrProject"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button  @click="queryList" type="primary">查找</el-button>
                    <el-button @click="exportExcel" type="primary">导出</el-button>
                </el-form-item>
        </el-form>

        <el-table
                :data="datas"
                :span-method="objectSpanMethod"
                size="mini"
                stripe
                border
                row-key="id"
                height="500"
           
                style="width: 100%"

        >
            <el-table-column
                    prop="index"
                    label="序号"
                    width="70">
            </el-table-column>
<el-table-column
                    prop="projectName"
                    label="项目名称"
                    width="120"
            >
            </el-table-column>
             <el-table-column
                    prop="productName"
                    label="构件名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="productNo"
                    label="构件编码"
                    width="120">
            </el-table-column>
           
            <el-table-column
                    prop="productType"
                    label="构件类型"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="materialName"
                    label="原料名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="materialStoageId"
                    label="原料编码"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="oldNum"
                    label="原数量"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="newNum"
                    label="现数量"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="total"
                    label="变更量"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="changeNo"
                    label="变更单号"
                    width="120">
            </el-table-column>
           
        </el-table>

        <el-table
                :data="exportdatas"
                :span-method="objectSpanMethod"
                size="mini"
                stripe
                border
                row-key="id"
                height="500"
                id="exportXlsx"
                hidden
                style="width: 100%"

        >
           
        <el-table-column
                    prop="index"
                    label="序号"
                    width="70">
            </el-table-column>
<el-table-column
                    prop="projectName"
                    label="项目名称"
                    width="120"
            >
            </el-table-column>
             <el-table-column
                    prop="productName"
                    label="构件名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="productNo"
                    label="构件编码"
                    width="120">
            </el-table-column>
           
            <el-table-column
                    prop="productType"
                    label="构件类型"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="materialName"
                    label="原料名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="materialStoageId"
                    label="原料编码"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="oldNum"
                    label="原数量"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="newNum"
                    label="现数量"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="total"
                    label="变更量"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="changeNo"
                    label="变更单号"
                    width="120">
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
        <el-dialog :visible.sync="showSelectTree" width="70%" append-to-body>
            <tree-add-edit1
                    :treeArr="treeArr"
                    :baseCheckedId="baseCheckedId"
                    :baseCheckParentId="baseCheckParentId"
                    @tree-edit-submit="checkoutStatusFinish"
                    @tree-edit-cancel="checkoutStatusCancel"
            ></tree-add-edit1>
        </el-dialog>
    </div>
    
</template>

<script>
    import axios from 'axios'
    import model from '../../../../model/model'
    import projectModel from '../../../../model/project/project'
    import productModel from '../../../../model/project/product'
    import productTypeModel from '../../../../model/basicdata/productType'
    import serviceConfig from '../../../../../server.config.js'
    import orgModel from '../../../../model/system/org'
    import TreeInfo from '../../basic/materialStorage/TreeInfo.vue'
    import {Notification} from 'element-ui'
    import xlsx from 'xlsx';
    import fileSaver from 'file-saver';
    import moment from "moment";

    export default {
        components: {
            'tree-add-edit1':TreeInfo,
        },
        data() {
            return {
                  showSelectTree:false,
                  baseCheckedId: [],
                  baseCheckParentId:[],
                  treeArr: '',
                  treeType:'',
                arrProductType: [],//select下拉框（productType）
                pageInfo: {
                    currentPage: 1,
                    total: 0,
                    pageSize:this.GLOBAL.pageSize,
                    pageSizes:this.GLOBAL.pageSizes
                },
                datas: [],
                orgs:[],
                exportdatas: [],
                queryInfo: {
                    projectId: '',
                    productNo: '',
                    productName: '',
                    productTypeId: '',
                    typwpeName: '',
                    typwpeId:'',
                    pageInfo: {
                        currentPage: 1,
                        total: 0,
                        pageSize: this.GLOBAL.pageSize,
                        pageSizes: this.GLOBAL.pageSizes
                    }

                  
                },
    
                arrProject: [],//select下拉框（project）
         
            }

        },
        mounted() {
            this.list()
            this.getProjects()
            this.showProductTree()
        },
        methods: {
            showProductTree() {
               // categoryModel.getCategorysToTreeParentDisabled().then((data) => {
                   productTypeModel.getProductTypesToTreeParentDisabled().then((data) => {
                    this.treeArr = data.entity
                })
            },
            checkoutStatusFinish(id,name) {
                if(this.treeType==1){
                    this.queryInfo.typwpeName = name;
                    this.queryInfo.typwpeId = id;
                    this.queryInfo.productTypeId = id;
                }
                    this.showSelectTree = false
            },
            checkoutStatusCancel() {
                this.showSelectTree = false
            },

                showSelectTrees(type){
                //1是列表 2是编辑
                if(type==1){

                    this.baseCheckedId.splice(0,this.baseCheckedId.length);
                    
                    this.baseCheckedId.push(this.queryInfo.typwpeId);
    
                    this.baseCheckParentId.splice(0,this.baseCheckedId.length);
                    this.baseCheckParentId.push(this.queryInfo.typwpeId);
                }
                this.showSelectTree = true
                this.treeType = type
            },
            /**
             * 合并单元格
             */
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      
       let rowCount = row.rowCount;
        let typeIndex = row.typeIndex;
         if (columnIndex === 0 || columnIndex === 1  ||columnIndex===2|columnIndex===3||columnIndex===4||columnIndex===10  ) {//第一列合并
          if (typeIndex  === 1) {
            return {
              rowspan: rowCount,
              colspan: 1
            };
          } else {
            return {
              rowspan: 0,
              colspan: 0
            };
          }
        }


      },
            /**
             * 增加修改product为项目下拉框赋值
             */
            getProjects() {
                projectModel.projectListS().then((data) => {
                    let projects = data.entity
                    for (var j = 0; j < projects.length; j++) {
                        var pName = {
                            value: projects[j].id,
                            label: projects[j].projectName,
                            molds: []
                        }
                        this.arrProject.push(pName)
                    }
                })
            },
            exportExcel() {
                 const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                let page = this.pageInfo.currentPage;
                let size = this.pageInfo.pageSize;
                this.queryInfo.pageInfo.currentPage = 1;
                this.queryInfo.pageInfo.pageSize = this.pageInfo.total;
                //TODO 分页
                productModel.getProductMaterialChangeInfo(this.queryInfo).then(data => {
                    this.exportdatas = data.entity.list;
           setTimeout(() => {
                  var excelName = new Date().toLocaleDateString().replace("/","-").replace("/","-")+".xlsx";
                excelName = "项目构件原料变更"+excelName;
              // console.log(excelName)
                /* generate workbook object from table */
                  let table = document.querySelector('#exportXlsx');
                  var wb = xlsx.utils.table_to_book(table);
                  /* get binary string as output */
                  var wbout = xlsx.write(wb, {bookType: 'xlsx', bookSST: true, type: 'array'})
                  try {
                      fileSaver.saveAs(new Blob([wbout], {type: 'application/octet-stream'}), excelName)
                  } catch (e) {
                      if (typeof console !== 'undefined') console.log(e, wbout)
                  }
                  
               this.queryInfo.pageInfo.pageSize = size;
               this.queryInfo.pageInfo.currentPage = page;
               loading.close();
                  return wbout;
              }, 500);
                    
                }).catch(err => {

                })
              
        
            },
          queryList(){
                this.pageInfo.currentPage = 1;
                this.list();
          },

            list() {
                const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });

                this.queryInfo.pageInfo = this.pageInfo;
                //TODO 分页
                console.log(this.queryInfo)
                productModel.getProductMaterialChangeInfo(this.queryInfo).then(data => {
                    this.datas = data.entity.list;
                    this.pageInfo.total = data.entity.totalCount;
                    loading.close();
                }).catch(err => {

                })
            },
            handleSizeChange(val) {
                this.pageInfo.pageSize = val;
                this.list()
            },
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val;
                this.list()
            },
        }
    }
</script>

<style scoped>

</style>