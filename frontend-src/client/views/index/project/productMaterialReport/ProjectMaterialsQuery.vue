<template>
  <el-form
    style="position:relative;"
    :model="productMaterialQuery"
    v-if="productMaterialQuery"
    ref="criteriaForm"
    :inline="true"
  >
    <el-form-item prop="projectId">
      <el-select
        v-model="productMaterialQuery.projectId"
        clearable
        placeholder="项目名称"
        filterable
        @change="changeProject"
        style="width:120px;"
      >
        <el-option
          v-for="item in projectList"
          :key="item.id"
          :label="item.projectName"
          :value="item.id"
        >
        </el-option>
      </el-select>
    </el-form-item>

    <el-form-item
      prop="buildingId"
      v-if="listType=='floorNum' "
    >
      <el-select
        v-model="productMaterialQuery.buildingId"
        placeholder="楼栋"
        clearable
        filterable
        @change="changeBuilding"
        style="width:100px;"
        :disabled="productMaterialQuery.projectId==''"
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
    <el-form-item
      prop="floorId"
      v-if="listType=='floorNum' "
    >
      <el-select
        v-model="productMaterialQuery.floorIds"
        placeholder="楼层"
        clearable
        multiple
        filterable
        :disabled="productMaterialQuery.buildingId==''"
        style="width:80px;"
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

    <el-form-item prop="materialStorageNo">
      <el-input
        v-model="productMaterialQuery.materialStorageNo"
        placeholder="原料编码"
        clearable
        style="width:100px;"
      ></el-input>
    </el-form-item>
    <el-form-item
      prop="materialName"
    >
      <el-input
        placeholder="原料名称"
        v-model="productMaterialQuery.materialName"
        style="width:100px"
        clearable
      >
      </el-input>
    </el-form-item>
    <el-form-item
      prop="materialSpecification"
      v-if="listType=='m' "
    >
      <el-input
        placeholder="原料规格"
        v-model="productMaterialQuery.materialSpecification"
        style="width:100px"
        clearable
      >
      </el-input>
    </el-form-item>
    <el-form-item
      prop="productName"
      v-if="listType=='m' "
    >
      <el-input
        v-model="productMaterialQuery.productName"
        placeholder="构件名称"
        clearable
        style="width:100px;"
      ></el-input>
    </el-form-item>
    <el-form-item
      prop="dateCreated"
      v-if="listType=='m' "
    >
      <el-date-picker
        v-model="productMaterialQuery.date1"
        type="date"
        :formatter="formatterDate"
        style="width: 150px"
        placeholder="开始时间"
      >
      </el-date-picker>
      <el-date-picker
        v-model="productMaterialQuery.date2"
        type="date"
        :formatter="formatterDate"
        style="width: 150px"
        placeholder="结束时间"
      >
      </el-date-picker>
    </el-form-item>
    <el-form-item prop="orgId">
      <el-select
        v-model="productMaterialQuery.orgId"
        style="width: 150px"
        placeholder="基地名称"
        clearable
      >
        <el-option
          v-for="item in orgs"
          :key="item.id"
          :label="item.orgName"
          :value="item.id"
        >
        </el-option>
      </el-select>
    </el-form-item>

    <el-form-item style="position:absolute;right:-100px;top:0;">
      <el-tooltip
        effect="light"
        placement="top"
        content="查询"
      >
        <el-button
          icon="el-icon-search"
          type="primary"
          @click="list()"
        ></el-button>
      </el-tooltip>
      <el-tooltip
        effect="light"
        placement="top"
        content="重置"
      >
        <el-button
          icon="el-icon-refresh"
          @click="resetForm('criteriaForm')"
        ></el-button>
      </el-tooltip>
    </el-form-item>
  </el-form>
</template>

<script>
import projectModel from '../../../../model/project/project'
import buildingModel from '../../../../model/project/building'
import orgModel from '../../../../model/system/org'
import moment from 'moment'
import { Notification } from 'element-ui'
import floorModel from "../../../../model/project/floor";
export default {
  name: "ProjectMaterialsQuery",
  props: ['listType'],
  data() {
    return {
      projectList: [],
      houseTypes: [],
      buildings: [],
      floors: [],
      productBases: [],
      orgs: [],
      productMaterialQuery: {
        date1: '',
        date2: '',
        listType: '',
        projectId: '',
        houseTypeId: '',
        buildingId: '',
        floorId: '',
        materialStorageNo: '',
        materialName: '',
        materialSpecification: '',
        productBaseId: '',
        productName: '',
        orgId: '',
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        },
      }
    }
  },
  mounted() {
    this.info()
    // this.showProductBaseInfos()
  },
  methods: {
    info() {
      projectModel.getAllProjects().then(data => {
        this.projectList = data.entity;
      }).catch(() => {
      })
      orgModel.orgAllByOrgIds().then((data) => {
        this.orgs = data.entity
      })
    },
    changeProject() {
      buildingModel.findByProjectId(this.productMaterialQuery.projectId).then(data => {
        this.buildings = data.entity
        this.productMaterialQuery.buildingId = ''
        this.productMaterialQuery.floorId = ''
        this.floors = []
      })
      /*         houseTypeModel.getHouseTypeByProjectId(this.productMaterialQuery.projectId).then((data)=>{
                   this.houseTypes = data.entity
                   this.productMaterialQuery.houseTypeId=''
                   this.productMaterialQuery.buildingId=''
                   this.productMaterialQuery.floorId=''
                   this.floors=[]
               })*/
    },
    /*            changeHouseType(){
    
                },*/
    changeBuilding(id) {
      this.productMaterialQuery.floorId = ''
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
        //this.floors = building.floors.sort(compare('floorNum'))
      })

    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    /*           showProductBaseInfos(){
                   prodcutTrack.findAllProductBaseInfo().then(data => {
                       this.productBases = data.entity;
                   })
               },*/
    list() {
      this.$emit('change-criteria', this.productMaterialQuery)
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }

}
</script>

<style scoped>
</style>