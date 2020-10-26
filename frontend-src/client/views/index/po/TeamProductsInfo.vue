<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <el-form
              :inline="true"
              id="queryInfo"
              :model="queryInfo"
              ref="queryInfo"
            >
              <el-form-item prop="productNo">
                <el-input
                  placeholder="周计划订单号"
                  v-model="queryInfo.orderNo"
                  style="width:150px"
                  clearable
                ></el-input>
              </el-form-item>

              <el-form-item prop="productNo">
                <el-select
                  v-model="queryInfo.lineId"
                  style="width:150px"
                  placeholder="产线"
                  clearable
                >
                  <el-option
                    v-for="item in prodLines"
                    :key="item.id"
                    :label="item.lineName"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>

              <el-form-item prop="productNo">
                <el-select
                  v-model="queryInfo.teamId"
                  style="width:150px"
                  placeholder="班组"
                  clearable
                >
                  <el-option
                    v-for="item in this.teamList"
                    :key="item.id"
                    :label="item.teamName"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>

              <el-form-item prop="productNo">
                <el-input
                  placeholder="构件编码"
                  v-model="queryInfo.productNo"
                  style="width:150px"
                  clearable
                ></el-input>
              </el-form-item>
              <el-form-item prop="productName">
                <el-input
                  placeholder="构件名称"
                  v-model="queryInfo.productName"
                  style="width:150px"
                  clearable
                ></el-input>
              </el-form-item>
              <el-form-item
                prop="typwpeName"
                @click.native="((type)=>{showSelectTrees(1)})"
              >
                <el-input
                  placeholder="构件类型"
                  v-model="queryInfo.typwpeName"
                  style="width:150px"
                ></el-input>
              </el-form-item>
              <el-form-item
                prop="typwpeId"
                style="display:none"
              >
                <el-input
                  placeholder="构件类型"
                  v-model="queryInfo.typwpeId"
                  style="width:150px"
                  clearable
                ></el-input>
              </el-form-item>

              <el-form-item prop="projectId">
                <el-select
                  v-model="queryInfo.projectId"
                  style="width:150px"
                  placeholder="项目名称"
                  clearable
                  @change="changeProject"
                >
                  <el-option
                    v-for="item in this.arrProject"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>

              <el-form-item>
                <el-select
                  v-model="queryInfo.bulidId"
                  placeholder="楼栋"
                  clearable
                  style="width:150px"
                  :disabled="queryInfo.projectId==''"
                  @change="changeBuilding(queryInfo.bulidId)"
                >
                  <el-option
                    v-for="item in buildings"
                    :key="item.id"
                    :label="item.buildCode"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-select
                  v-model="queryInfo.floorId"
                  placeholder="楼层"
                  clearable
                  style="width:150px"
                  :disabled="queryInfo.bulidId==''"
                >
                  <el-option
                    v-for="item in floors"
                    :key="item.id"
                    :label="item.floorNum"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col>
            <my-search-button-group :model="searchButtonData"></my-search-button-group>
          </el-col>
        </el-row>
        <el-row style="display:none;">
          <my-button-group :model="buttonData"></my-button-group>
        </el-row>
      </el-header>
      <el-main
        ref="mainContent"
        :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <el-table
          :data="datas"
          size="mini"
          stripe
          border
          row-key="id"
          height="100%"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
          style="width: 100%"
        >
          <el-table-column
            type="index"
            label="序号"
            width="70"
          ></el-table-column>
          <el-table-column
            prop="orderNo"
            label="周计划订单号"
            width="120"
          ></el-table-column>
          <el-table-column
            prop="lineName"
            label="产线"
            width="120"
          ></el-table-column>
          <el-table-column
            prop="teamName"
            label="班组"
            width="120"
          ></el-table-column>
          <el-table-column
            prop="projectName"
            label="项目名称"
            width="120"
          ></el-table-column>
          <el-table-column
            prop="planDateFrom"
            label="开始时间"
            width="120"
          ></el-table-column>
          <el-table-column
            prop="planDateTo"
            label="结束时间"
            width="120"
          ></el-table-column>
          <el-table-column
            prop="productNo"
            label="构件编码"
            width="120"
          ></el-table-column>

          <el-table-column
            prop="productName"
            label="构件名称"
            width="120"
          ></el-table-column>

          <el-table-column
            prop="productTypeName"
            label="构件类型"
            width="120"
          ></el-table-column>
          <el-table-column
            prop="bulidCode"
            label="楼栋"
            width="120"
          ></el-table-column>
          <el-table-column
            prop="floorNum"
            label="楼层"
            width="120"
          ></el-table-column>
          <el-table-column
            prop="projectConcrete"
            label="方量"
            width="120"
          ></el-table-column>
          <el-table-column
                  prop="productStatus"
                  label="构件状态"
                  width="120"
          ></el-table-column>
          <el-table-column
            prop="totalNum"
            label="数量"
            width="120"
          ></el-table-column>
          <el-table-column
            prop="completeNum"
            label="产出量"
            width="120"
          ></el-table-column>
        </el-table>
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
          ></el-pagination>
        </el-row>
      </el-footer>
    </el-container>
    <el-dialog
      :visible.sync="showSelectTree"
      width="70%"
      append-to-body
    >
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
import axios from "axios";
import model from "../../../model/model";
import projectModel from "../../../model/project/project";
import productModel from "../../../model/project/product";
import productTypeModel from "../../../model/basicdata/productType";
import plateModel from "../../../model/basicdata/plate";
import serviceConfig from "../../../../server.config.js";
import orgModel from "../../../model/system/org";
import TreeInfo from "../basic/materialStorage/TreeInfo.vue";
import buildingModel from "../../../model/project/building";
import { Notification } from "element-ui";
import xlsx from "xlsx";
import fileSaver from "file-saver";
import moment from "moment";

export default {
  components: {
    "tree-add-edit1": TreeInfo
  },
  data() {
    return {
      showSelectTree: false,
      baseCheckedId: [],
      baseCheckParentId: [],
      treeArr: "",
      treeType: "",
      arrProductType: [], //select下拉框（productType）
      prodLines: [], //产线集合
      teamList: [],
      buildings: [],
      floors: [],
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: this.GLOBAL.pageSize,
        pageSizes: this.GLOBAL.pageSizes
      },
      datas: [],
      orgs: [],
      exportdatas: [],
      queryInfo: {
        projectId: "",
        productNo: "",
        productName: "",
        productTypeId: "",
        materialName: "",
        typwpeName: "",
        typwpeId: "",
        bulidId: "",
        floorId: "",
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        }
      },

      arrProject: [], //select下拉框（project）
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        }
      ],
      searchButtonData: [
        {
          name: '查询',
          event: "queryList"
        }
      ],
      rowButtonData: [
      ]
    };
  },
  mounted() {
    this.list();
    this.getProjects();
    this.getProdLines();
    this.getTeamInfoList();
    this.showProductTree();
  },
  methods: {
    showProductTree() {
      // categoryModel.getCategorysToTreeParentDisabled().then((data) => {
      productTypeModel.getProductTypesToTreeParentDisabled().then(data => {
        this.treeArr = data.entity;
      });
    },
    checkoutStatusFinish(id, name) {
      if (this.treeType == 1) {
        this.queryInfo.typwpeName = name;
        this.queryInfo.typwpeId = id;
        this.queryInfo.productTypeId = id;
      }
      this.showSelectTree = false;
    },
    checkoutStatusCancel() {
      this.showSelectTree = false;
    },

    showSelectTrees(type) {
      //1是列表 2是编辑
      if (type == 1) {
        this.baseCheckedId.splice(0, this.baseCheckedId.length);

        this.baseCheckedId.push(this.queryInfo.typwpeId);

        this.baseCheckParentId.splice(0, this.baseCheckedId.length);
        this.baseCheckParentId.push(this.queryInfo.typwpeId);
      }
      this.showSelectTree = true;
      this.treeType = type;
    },
    /**
     * 合并单元格
     */
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      let rowCount = row.rowCount;
      let typeIndex = row.typeIndex;
      if (
        columnIndex === 0 ||
        columnIndex === 1 ||
        (columnIndex === 2) | (columnIndex === 3) ||
        columnIndex === 4 ||
        columnIndex === 10
      ) {
        //第一列合并
        if (typeIndex === 1) {
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
      projectModel.projectListS().then(data => {
        let projects = data.entity;
        for (var j = 0; j < projects.length; j++) {
          var pName = {
            value: projects[j].id,
            label: projects[j].projectName,
            molds: []
          };
          this.arrProject.push(pName);
        }
      });
    },
    getProdLines() {
      plateModel.getAllProdLinesByOrgId().then(data => {
        this.prodLines = data.entity;
      });
    },
    getTeamInfoList() {
      projectModel.getTeamByOrg().then(data => {
        this.teamList = data.entity;
      });
    },

    exportExcel() {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });
      let page = this.pageInfo.currentPage;
      let size = this.pageInfo.pageSize;
      this.queryInfo.pageInfo.currentPage = 1;
      this.queryInfo.pageInfo.pageSize = this.pageInfo.total;
      //TODO 分页
      productModel
        .getProductMaterialChangeInfo(this.queryInfo)
        .then(data => {
          this.exportdatas = data.entity.list;
          setTimeout(() => {
            var excelName =
              new Date()
                .toLocaleDateString()
                .replace("/", "-")
                .replace("/", "-") + ".xlsx";
            excelName = "项目构件原料变更" + excelName;
            // console.log(excelName)
            /* generate workbook object from table */
            let table = document.querySelector("#exportXlsx");
            var wb = xlsx.utils.table_to_book(table);
            /* get binary string as output */
            var wbout = xlsx.write(wb, {
              bookType: "xlsx",
              bookSST: true,
              type: "array"
            });
            try {
              fileSaver.saveAs(
                new Blob([wbout], { type: "application/octet-stream" }),
                excelName
              );
            } catch (e) {
              if (typeof console !== "undefined") console.log(e, wbout);
            }

            this.queryInfo.pageInfo.pageSize = size;
            this.queryInfo.pageInfo.currentPage = page;
            loading.close();
            return wbout;
          }, 500);
        })
        .catch(err => { });
    },
    queryList() {
      this.pageInfo.currentPage = 1;
      this.list();
    },

    list() {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)"
      });

      this.queryInfo.pageInfo = this.pageInfo;
      //TODO 分页
      productModel
        .getTeamProductsInfoByOrder(this.queryInfo)
        .then(data => {
          this.datas = data.entity.list;
          this.pageInfo.total = data.entity.totalCount;
          loading.close();
        })
        .catch(err => { });
    },
    handleSizeChange(val) {
      this.pageInfo.pageSize = val;
      this.list();
    },
    handleCurrentChange(val) {
      this.pageInfo.currentPage = val;
      this.list();
    },
    changeProject() {
      buildingModel.findByProjectId(this.queryInfo.projectId).then(data => {
        this.buildings = data.entity;
        this.queryInfo.buildId = "";
        this.queryInfo.floorId = "";
        this.floors = [];
      });
    },
    changeBuilding(id) {
      this.queryInfo.floorId = "";
      this.floors = [];
      if (!id) {
        return;
      }

      buildingModel
        .getFloorInfoByBuildId(id, this.queryInfo.projectId)
        .then(data => {
          this.floors = data.entity;
        });
    }
  }
};
</script>

<style scoped>
</style>