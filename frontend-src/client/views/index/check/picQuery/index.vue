<template>
  <div
    id="product-detail-list"
    class="currency-style-box"
  >
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
            <el-col>
                <el-form
                    :inline="true"
                    :model="formInline"
                    class="demo-form-inline"
                    size="mini"
                >
                    <el-form-item prop="projectId">
                        <el-select v-model="formInline.projectId" placeholder="项目名称" clearable filterable @change="changeProject"
                                  style="width:160px;">
                            <el-option
                                    v-for="item in projectList"
                                    :key="item.id"
                                    :label="item.projectName"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="buildingId">
                        <el-select v-model="formInline.buildingId" placeholder="楼栋" clearable filterable @change="changeBuilding" style="width:160px;" :disabled="formInline.projectId==''">
                            <el-option
                                    v-for="item in buildings"
                                    :key="item.id"
                                    :label="item.buildCode"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="floorId">
                        <el-select v-model="formInline.floorId" placeholder="楼层" clearable filterable :disabled="formInline.buildingId==''" style="width:160px;">
                            <el-option
                                    v-for="item in floors"
                                    :key="item.id"
                                    :label="item.floorNum"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-input
                                v-model="formInline.productNo"
                                placeholder="构件编码"
                                style="width: 160px"
                                clearable
                        >
                        </el-input>
                    </el-form-item>
                    <!-- <el-form-item prop="productName">
                        <el-select v-model="formInline.productName"
                                    clearable
                                    filterable
                                    placeholder="构件名称"
                                    :disabled="formInline.projectId==''"
                                    style="width:160px">
                            <el-option
                                    v-for="item in arrProduct"
                                    :key="item.id"
                                    :label="item.productName"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item> -->
                    <el-form-item prop="productName">
                        <el-input
                                v-model="formInline.productName"
                                placeholder="构件名称"
                                style="width: 160px"
                                clearable
                        >
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="productTypeId">
                        <el-select v-model="formInline.productTypeId"
                                    clearable
                                    filterable
                                    placeholder="构件类型"
                                    style="width:160px">
                            <el-option
                                    v-for="item in arrProductType"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                                v-model="formInline.checkResult"
                                placeholder="检验结论"
                                style="width: 160px"
                                collapse-tags
                                clearable
                        >
                            <el-option
                                    label="合格"
                                    value="合格"
                            ></el-option>
                            <el-option
                                    label="不合格"
                                    value="不合格"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                                v-model="formInline.checkType"
                                placeholder="图片类型"
                                style="width: 160px"
                                collapse-tags
                                clearable
                        >
                            <el-option
                                    label="隐检"
                                    value="隐检"
                            ></el-option>
                            <el-option
                                    label="成品检"
                                    value="成品检"
                            ></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-date-picker
                          v-model="formInline.timeArr"
                          type="daterange"
                          value-format="yyyy-MM-dd"
                          range-separator="至"
                          style="width: 335px"
                          clearable
                          start-placeholder="检验开始日期"
                          end-placeholder="检验结束日期">
                        </el-date-picker>
                    </el-form-item>
                </el-form>
            </el-col>
            <el-col>
                <my-search-button-group :model="searchButtonData"></my-search-button-group>
            </el-col>
        </el-row>
        <el-row>
          <my-button-group :model="buttonData"></my-button-group>
        </el-row>
      </el-header>
      <el-main ref="mainContent">
        <el-card class="box-card">
          <div class="img-list">
            <div class="demo-image__preview" v-for="(item,index) of imgData" :key="index" >
              <!-- <el-checkbox class="checkbox" v-model="item.checked" @change="imgCheck(index)"></el-checkbox> -->
              <el-image 
                class="image"
                fit="contain"
                :src="item.key" 
                :preview-src-list="srcList"
              >
              </el-image>
              <div class="img-name">
                <div class="text">{{item.value}}</div>
                <div class="text">{{item.lable}}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-main>
      <el-footer ref="footOperationBar">
        <el-row>
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
      </el-footer>
    </el-container>
  </div>
</template>

<script>
import projectModel from '@/model/project/project'
import buildingModel from '@/model/project/building'
import floorModel from '@/model/project/floor'
import productModel from '@/model/project/product'
import productTypeModel from '@/model/basicdata/productType'
import productImageModel from '@/model/project/image'
import {Notification} from 'element-ui'
import serviceConfig from "../../../../../server.config";
export default {
  data(){
    return {
      pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
      },
      projectList: [],
      buildings: [],
      floors: [],
      arrProductType:[],
      arrProduct: [],
      formInline:{
          projectId:'',
          buildingId:'',
          floorId:'',
          productNo:'',
          productTypeId:'',
          timeArr:[],
          checkResult:'',
          checkType:'',
          productName:'',
          pageInfo: {
              currentPage: 1,
              total: 0,
              pageSize: this.GLOBAL.pageSize,
              pageSizes: this.GLOBAL.pageSizes
          },
      },
      buttonData:[
          {
              name:'导出',
              event:'downloadimage',
          }
      ],
      searchButtonData: [
          {
              name: '查询',
              event: "search"
          },
          {
              name: '重置',
              event: "reset"
          },
      ],
      imgData:[],
      srcList: []
    }
  },
  mounted(){
      this.init()
      
  },
  methods:{
    init(){
        this.getProjectListS()
        this.getProductType()
        this.search()
    },
    // 获取项目下拉框的值
    getProjectListS(){
        projectModel.projectListS().then(data => {
            this.projectList = data.entity
        })
    },
    // 获取楼栋下拉框的值
    changeProject() {
        buildingModel.findByProjectId(this.formInline.projectId).then(data => {
            this.buildings = data.entity
            this.formInline.buildingId = ''
            this.formInline.floorId = ''
            this.floors = []
        })
        // this.getProductName()
    },
    // 获取楼层下拉框的值
    changeBuilding(id) {
        this.formInline.floorId = ''
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
        })
    },
    // 获取构件类型下拉框的值
    getProductType() {
        productTypeModel.getAllproductTypes().then((data) => {
            this.arrProductType = data.entity
        })
    },
    // 获取构件名称下拉框的值
    getProductName(){
        productModel.findProductsByProjectId(this.formInline.projectId).then((data) => {
            this.arrProduct = data.entity
        })
    },
    search(){
        this.formInline.pageInfo = this.pageInfo
        productImageModel.getCheckImageInfo(this.formInline).then(data=>{
            if(data.status==='success'){
              this.imgData = data.entity.list;
              this.pageInfo.total = data.entity.totalCount
              this.srcList = data.entity.list.map(option=>{
                return option.key
              })
            }else{
              Notification({
                  message: data.msg,
                  type: "error"
              })
            }
        })
    },
    exportExcelPost(URL, PARAMS) {

    },
    downloadimage(){
        var url = serviceConfig.baseURL + '/check/downloadimage'
        var prams = {
            "projectId": this.formInline.projectId,
            "buildingId": this.formInline.buildingId,
            "floorId": this.formInline.floorId,
            "productNo": this.formInline.productNo,
            "productTypeId": this.formInline.productTypeId,
            "timeArr": this.formInline.timeArr,
            "checkType": this.formInline.checkType,
            "productName": this.formInline.productName,
            "checkResult": this.formInline.checkResult,
        }
        var temp = document.createElement("form");
        temp.action = url;
        temp.method = "post";
        temp.style.display = "none";
        for (var x in prams) {
            var opt = document.createElement("textarea");
            opt.name = x;
            opt.value = prams[x];
            temp.appendChild(opt);
        }
        document.body.appendChild(temp);
        temp.submit();
        return temp;
    },
    // 重置输入框
    reset(){
        Object.keys(this.formInline).forEach(option=>{
            this.formInline[option] = ""
        })
        this.formInline.timeArr = []
    },
    handleSizeChange(val) {
        this.pageInfo.pageSize = val;
        this.search()
    },
    handleCurrentChange(val) {
        this.pageInfo.currentPage = val;
        this.search()
    }
  }
}
</script>

<style lang="scss">
  .img-list{
    display:flex;
    flex-wrap:wrap;
    .demo-image__preview{
      position:relative;
      width: 18%;
      box-sizing:border-box;
      margin:1%;
      border:1px solid #EBEEF5;
      box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
      .checkbox{
        position:absolute;
        left:5px;
        top:5px;
      }
      .image{
        width: 100%;
        height: 100px;
      }
      .img-name{
        text-align: center;
        padding:2px 0;
        color:#2b7ad5;
        font-size: 12px;
      }
    }
  }
</style>