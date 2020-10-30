<template>
  <div id="plan-list" class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-form
            :inline="true"
            id="entity"
            :model="entity"
            ref="entity"
          >
            <el-form-item prop="projectId">
              <el-select v-model="entity.projectId" placeholder="源项目名称" clearable filterable @change="changeProject" width="100px">
                <el-option
                        v-for="item in projectList"
                        :key="item.id"
                        :label="item.projectName"
                        :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="buildingId">
              <el-select v-model="entity.buildingId" placeholder="源楼栋" clearable filterable @change="changeBuilding" style="width:110px;" :disabled="entity.projectId==''">
                <el-option
                        v-for="item in buildings"
                        :key="item.id"
                        :label="item.buildCode"
                        :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="floorId">
              <el-select v-model="entity.floorId" placeholder="源楼层" @change="$forceUpdate()" clearable filterable :disabled="entity.buildingId==''" style="width:110px;">
                <el-option
                        v-for="item in floors"
                        :key="item.id"
                        :label="item.floorNum"
                        :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="productName">
              <el-input v-model="entity.productName" placeholder="源构件名称" clearable style="width:110px;"></el-input>
            </el-form-item>
            <el-form-item prop="productNo">
              <el-input v-model="entity.productNo" placeholder="源构件编码" clearable style="width:110px;"></el-input>
            </el-form-item>
            <br>
            <el-form-item prop="sourceStockId">
              <el-select placeholder="源库区" v-model="entity.sourceStockId"  @change="changeSourceStock" width="100">
                <el-option
                  v-for="stock in stocks"
                  :key="stock.id"
                  :label="stock.stockName"
                  :value="stock.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="sourceShelfId">
              <el-select placeholder="源货架" v-model="entity.sourceShelfId"  >
                <el-option
                  v-for="shelf in sourceShelves"
                  :key="shelf.id"
                  :label="shelf.shelfName"
                  :value="shelf.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item style="position:absolute;right:0;top:0;">
              <el-tooltip
                      effect="light"
                      content="查询"
                      placement="top"
              >
                <el-button icon="el-icon-search" type="primary" @click="changeSourceShelf()"></el-button>
              </el-tooltip>
              <el-tooltip
                      effect="light"
                      content="重置"
                      placement="top"
              >
                <el-button icon="el-icon-refresh" @click="resetForm('criteriaForm')"></el-button>
              </el-tooltip>

            </el-form-item>
            <br>
            <el-form-item prop="toStockId">
              <el-select placeholder="目标库区" v-model="entity.toStockId"  @change="changeToStock">
                <el-option
                  v-for="stock in stocks"
                  :key="stock.id"
                  :label="stock.stockName"
                  :value="stock.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="toShelfId">
              <el-select placeholder="目标货架"  v-model="entity.toShelfId">
                <el-option
                  v-for="shelf in toShelves"
                  :key="shelf.id"
                  :label="shelf.shelfName"
                  :value="shelf.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>

        </el-row>
        <el-row style="display:none;">
          <my-button-group :model="buttonData"></my-button-group>
        </el-row>
        <el-row>
          <el-form
            :inline="true"
            :disabled="!canSave"
          >
            <el-form-item>
              <el-button
                size="mini"
                type="primary"
                @click="move('entity')"
              >保存</el-button>
            </el-form-item>
          </el-form>
        </el-row>
      </el-header>
      <el-main
        ref="mainContent"
        :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <el-transfer
          v-model="selectedProductDetailIds"
          :data="productDetails"
          :titles="titles"
          height="100%"
        >
          <div
            class="transfer-footer"
            slot="left-footer"
          >{{sourceShelfInfo}}</div>
          <div
            class="transfer-footer"
            slot="right-footer"
          >{{toShelfInfo}}</div>
        </el-transfer>
      </el-main>
      <el-footer ref="footOperationBar">
        <el-row>

        </el-row>
      </el-footer>
    </el-container>
  </div>
</template>
<script>
import model from '../../../model/model'
import clientModel from '../../../model/client-model'
import stockModel from '../../../model/basicdata/stock'
import shelfModel from '../../../model/basicdata/shelf'
import { Notification } from 'element-ui'
import buildingModel from "../../../model/project/building";
import floorModel from "../../../model/project/floor";
import projectModel from "../../../model/project/project";
var numeral = require('numeral')

export default {
  components: {
  },
  data() {
    return {
      projectList: [],
      buildings: [],
      floors: [],
      entity: {
        projectId: '',
        buildingId: '',
        floorId: '',
        sourceStockId: null,
        sourceShelfId: null,
        toStockId: null,
        toShelfId: null,
        toProjectId:'',
        toBuildingId: '',
        toFloorId: '',
      },
      stocks: [],
      sourceShelves: [],
      toShelves: [],
      productDetails: [],
      selectedProductDetailIds: [],
      submitting: false,
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        },
      ],
    };
  },
  computed: {
    canSave() {
      return this.entity.sourceStockId !== null
        && this.entity.sourceShelfId !== null
        && this.entity.toStockId !== null
        && this.entity.toShelfId !== null
        && this.selectedProductDetailIds.length > 0
        && !this.submitting
    },
    titles() {
      let titles = []
      let titleSource = '源货架:'
      let titleTo = '目标货架:'
      if (this.entity.sourceShelfId) {
        titleSource += this.stocks.filter(e => e.id == this.entity.sourceStockId)[0].stockName
        titleSource += " " + this.sourceShelves.filter(e => e.id == this.entity.sourceShelfId)[0].shelfName
      } else {
        titleSource += '尚未选择'
      }
      if (this.entity.toShelfId) {
        titleTo += this.stocks.filter(e => e.id == this.entity.toStockId)[0].stockName
        titleTo += " " + this.toShelves.filter(e => e.id == this.entity.toShelfId)[0].shelfName
      } else {
        titleTo += '尚未选择'
      }
      titles.push(titleSource)
      titles.push(titleTo)
      return titles
    },
    sourceShelfInfo() {
      if (this.entity.sourceShelfId) {
        let shelf = this.sourceShelves.filter(e => e.id == this.entity.sourceShelfId)[0]
        return '已堆放数/货架容量:' + shelf.inCount + '/' + shelf.shelfNum
      }
    },
    toShelfInfo() {
      if (this.entity.toShelfId) {
        let shelf = this.toShelves.filter(e => e.id == this.entity.toShelfId)[0]
        return '已堆放数/货架容量:' + shelf.inCount + '/' + shelf.shelfNum
      }
    }
  },
  mounted() {
    this.init()
    this.changeProject()
    this.changeBuilding()
  },
  methods: {
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    resetForm(formName) {
      this.$nextTick(()=>{
        if (this.$refs[formName] !== undefined) {
          this.$refs[formName].resetFields();
        }
        this.entity.projectId = ''
        this.entity.buildingId = ''
        this.entity.floorId = ''
        this.entity.productName = ''
        this.entity.productNo = ''
        this.entity.sourceShelfId = ''
        this.entity.sourceStockId = ''
        this.entity.toShelfId = ''
        this.entity.toStockId = ''
      })
    },
    changeProject() {
      buildingModel.findByProjectId(this.entity.projectId).then(data => {
        this.buildings = data.entity
        this.entity.buildingId = ''
        this.entity.floorId = ''
        this.floors = []
      })
    },
    changeBuilding(id) {
      this.entity.floorId = ''
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
    init() {
      shelfModel.getStocks().then(data => {
        this.stocks = data.entity;
        this.stocks = this.stocks.filter(s => s.stockStatus == '启用' && s.stockTypeCode == '成品仓库')
        console.log("this.stocks--->",data.entity)
      }).catch(err => {
        Notification({
          message: err.msg,
          type: 'warning'
        })
      })
      projectModel.projectListS().then(data => {
          this.projectList = data.entity;
      }).catch(() => {
      })
    },
    /*changeEntity() {
      if (this.entity.sourceStockId) {
        stockModel.getShelfsById(this.entity.sourceStockId).then(data => {
          this.sourceShelves = data.entity
          this.sourceShelves = this.sourceShelves.filter(s => s.shelfStatus == '启用')
        })
      }
      if (this.entity.toStockId) {
        stockModel.getShelfsById(this.entity.toStockId).then(data => {
          this.toShelves = data.entity
          this.toShelves = this.toShelves.filter((s) => {
            return s.shelfStatus == '启用' && s.fullStatus == '未满' && (this.entity.sourceShelfId ? s.id != this.entity.sourceShelfId : true)
          })
        })
      }
      if (this.entity.sourceShelfId) {
        model.moveFinishedProducts(this.entity).then(data => {
          let list = data.entity.list;
          let productDetails = []
          list.forEach((e) => {
            productDetails.push({ key: e.id, label: e.productName + '-' + e.detailNo })
          })
          this.productDetails = productDetails
        })
      }
    },*/
    changeSourceStock(id) {
      this.entity.sourceShelfId = null
      this.entity.toShelfId = null
      this.productDetails = []
      this.selectedProductDetailIds = []
      //this.changeEntity()
      stockModel.getShelfsById(this.entity.sourceStockId).then(data => {
        this.sourceShelves = data.entity
        this.sourceShelves = this.sourceShelves.filter(s => s.shelfStatus == '启用')
      })
    },
    changeSourceShelf() {
      this.entity.toShelfId = null
      this.productDetails = []
      this.selectedProductDetailIds = []
      //this.changeEntity()
      model.moveFinishedProducts(this.entity).then(data => {
        let list = data.entity.list;
        let productDetails = []
        list.forEach((e) => {
          productDetails.push({ key: e.id, label: e.productName + '-' + e.detailNo })
        })
        this.productDetails = productDetails
      })
    },
    changeToStock(id) {
      this.entity.toShelfId = null
      this.selectedProductDetailIds = []
     // this.changeEntity()
      stockModel.getShelfsById(this.entity.toStockId).then(data => {
        this.toShelves = data.entity
        this.toShelves = this.toShelves.filter((s) => {
          return s.shelfStatus == '启用' && s.fullStatus == '未满' && (this.entity.sourceShelfId ? s.id != this.entity.sourceShelfId : true)
        })
      })
    },

    move(formName) {
      this.$confirm('确定要将' + this.selectedProductDetailIds.length + '个构件移到' + this.titles[1] + "?", '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.submitting = true
        clientModel.ax.post('/productDetail/move', {
          toStockId: this.entity.toStockId,
          toShelfId: this.entity.toShelfId,
          selectedProductDetailIds: this.selectedProductDetailIds
        }).then((data) => {
          this.submitting = false
          if (data && data.status === 'success') {
            this.$message({
              message: '移库成功',
              type: 'success',
            })
          } else {
            this.$message.error(data.msg)
          }
          this.selectedProductDetailIds = []
          this.changeEntity()


        })
      }).catch(() => { })
    },
  },
  watch: {
  }

}
</script>
<style>
.el-transfer-panel {
  width: 400px;
}
.transfer-footer {
  padding: 10px 0 0 10px;
}
</style>