<template>
  <div class="currency-style-box">
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
              <el-form-item>
                <el-select
                  v-model="formInline.projectId"
                  placeholder="项目名称"
                  clearable
                  @change="changeProject"
                >
                  <el-option
                    v-for="item in projectSelect"
                    :key="item.id"
                    :label="item.projectName"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>

                <el-form-item prop="buildId">
                    <el-select
                            v-model="formInline.buildId"
                            @change="changeBuilding"
                            placeholder="楼栋"
                            :disabled="formInline.projectId==''"
                            style="width: 150px;"
                            clearable
                            filterable
                    >
                        <el-option
                                :key="item.id"
                                :value="item.id"
                                v-for="item in buildings"
                                :label="item.buildCode"
                        >
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="floorId">
                    <el-select
                            v-model="formInline.floorId"
                            :disabled="formInline.buildId==''"
                            clearable
                            filterable
                            style="width: 150px;"
                            placeholder="楼层"
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


              <el-form-item prop="endTime">
                <el-date-picker
                  v-model="formInline.endTime"
                  type="date"
                  placeholder="完成时间"
                  value-format="yyyy-MM-dd"
                >
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
      <el-main
        ref="mainContent"
        :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <el-table
          :data="datas"
          @expand-change="showSubData"
          size="mini"
          stripe
          border
          ref="handleClickRowSelection"
          height="100%"
          @row-click="handleClickRowSelection"
          style="width: 100%"
        >
          <!-- 这是点击小箭头出现的信息 -->
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-table
                :data="subDatas"
                size="mini"
                stripe
                border
                style="width: 100%"
              >
                <el-table-column
                  type="index"
                  width="80"
                >
                </el-table-column>
                  <el-table-column
                          prop="productTypeName"
                          label="构件类型"
                  >
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
                          prop="devNum"
                          label="需求量"
                  >
                  </el-table-column>
                  <el-table-column
                          prop="quantityNum"
                          label="完成量"
                  >
                  </el-table-column>
                  <el-table-column
                          prop="sendTotalNum"
                          label="发货量"
                  >
                  </el-table-column>

              </el-table>
            </template>
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            width="70"
          >
          </el-table-column>
          <el-table-column
            prop="orgName"
            label="生产基地"
          >
          </el-table-column>
          <el-table-column
            prop="projectName"
            label="项目"
          >
          </el-table-column>

          <el-table-column
            prop="houseTypeName"
            label="户型"
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
            prop="schedule"
            label="完成进度"
            width="180"
          >
            <template slot-scope="scope">
              <el-progress
                :text-inside="true"
                :stroke-width="18"
                :percentage="parseFloat(scope.row.quantityNum/scope.row.devNum*100).toFixed(3)*1"
                :color="customColorMethod"
              ></el-progress>
            </template>
          </el-table-column>

          <el-table-column
            prop="devNum"
            label="需求量"
          >
          </el-table-column>
          <el-table-column
            prop="quantityNum"
            label="完成量"
          >
          </el-table-column>
          <el-table-column
            prop="sendTotalNum"
            label="发货量"
          >
          </el-table-column>
          <el-table-column label="生产欠量">
            <template slot-scope="scope">
              <span>{{scope.row.devNum-scope.row.quantityNum}}</span>
            </template>
          </el-table-column>

        </el-table>
          <el-table
                  :data="exportdatas"
                  size="mini"
                  stripe
                  border
                  height="500"
                  id="exportXlsx"
                  hidden
                  style="width: 100%"
          >
              <el-table-column
                      type="index"
                      label="序号"
                      width="70"
              >
              </el-table-column>
              <el-table-column
                      prop="orgName"
                      label="生产基地"
              >
              </el-table-column>
              <el-table-column
                      prop="projectName"
                      label="项目"
              >
              </el-table-column>

              <el-table-column
                      prop="houseTypeName"
                      label="户型"
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
                      prop="schedule"
                      label="完成进度(%)"
                      width="180"
              >
                  <template slot-scope="scope">
                      <span>{{parseFloat(scope.row.quantityNum/scope.row.devNum*100).toFixed(3)*1}}％</span>
                  </template>
              </el-table-column>

              <el-table-column
                      prop="devNum"
                      label="需求量"
              >
              </el-table-column>
              <el-table-column
                      prop="quantityNum"
                      label="完成量"
              >
              </el-table-column>
              <el-table-column
                      prop="sendTotalNum"
                      label="发货量"
              >
              </el-table-column>
              <el-table-column label="生产欠量">
                  <template slot-scope="scope">
                      <span>{{scope.row.devNum-scope.row.quantityNum}}</span>
                  </template>
              </el-table-column>
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
          >
          </el-pagination>
        </el-row>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
import prodcutTrack from '../../../../model/project/productTrack'
import floorModel from "../../../../model/project/floor";
import prodcutChartModel from '../../../../model/charts/productChart'
import buildingModel from '../../../../model/project/building'
import xlsx from 'xlsx';
import fileSaver from 'file-saver';

export default {
  data() {
    return {
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: this.GLOBAL.pageSize,
        pageSizes: this.GLOBAL.pageSizes
      },
      datas: [],
        exportdatas:[],
      selected: [],
      expands: [],
      subDatas: [],
      formInline: {
        projectId: '',
        buildId: '',
        floorId: '',
        endTime: ''
      },
      projectSelect: [],
      buildings: [],
      floors: [],
        buttonData: [
            {
                name: '导出',
                event: 'exportExcel'
            }
        ],
      searchButtonData: [
        {
          name: '查询',
          event: "onSubmit"
        }
      ],
      rowButtonData: []
    }

  },
  mounted() {
      prodcutTrack.findAllProjectList().then(data => {
          this.projectSelect = data.entity;
      })
    this.list()
  },
  methods: {
      exportExcel() {
          const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
          });
          let page = this.pageInfo.currentPage;
          let size = this.pageInfo.pageSize;
          this.formInline.pageInfo.currentPage = 1;
          this.formInline.pageInfo.pageSize = this.pageInfo.total;
          //TODO 分页
          prodcutTrack.findAllByFormInlineAndFloor(this.formInline).then(data => {
              this.exportdatas = data.entity.list;
              setTimeout(() => {
                  var excelName = new Date().toLocaleDateString().replace("/", "-").replace("/", "-") + ".xlsx";
                  excelName = "楼层生产进度报表" + excelName;
                  /* generate workbook object from table */
                  let table = document.querySelector('#exportXlsx');
                  var wb = xlsx.utils.table_to_book(table);
                  /* get binary string as output */
                  var wbout = xlsx.write(wb, { bookType: 'xlsx', bookSST: true, type: 'array' })
                  try {
                      fileSaver.saveAs(new Blob([wbout], { type: 'application/octet-stream' }), excelName)
                  } catch (e) {
                      if (typeof console !== 'undefined') console.log(e, wbout)
                  }

                  this.formInline.pageInfo.pageSize = size;
                  this.formInline.pageInfo.currentPage = page;
                  loading.close();
                  return wbout;
              }, 500);

          }).catch(err => {

          })


      },
    showSubData(row, expandedRows) {
      let id = row.id;
      if (this.expands.some((e) => e == row)) {
        this.expands = []
          return;
      } else {
        this.expands = []
        this.expands.push(row)
      }
        let queryForm = {
            buildId: row.buildId,
            floorId: row.floorId,
            endTime: this.formInline.endTime
        }
        const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
        });
        prodcutChartModel.getProductFloorRelevanceChartDetail(queryForm).then(data => {
            this.subDatas = data.entity;
            loading.close();
        })


    },
    customColorMethod(percentage) {
      if (percentage <= 99) {
        return '#409eff';
      } else {
        return '#67c23a';
      }
    },
    changeProject() {
      buildingModel.findByProjectId(this.formInline.projectId).then(data => {
        this.buildings = data.entity
        this.formInline.buildId = ''
        this.formInline.floorId = ''
        this.floors = []
      })
    },
      changeBuilding(id) {
          this.formInline.floorId = ''
          this.floors = []
          if (id == undefined || id == '') {
              return
          }
          let building = this.buildings.find(b => b.id == id)
          floorModel.findByBuildingIdAndProjectId(id, building.projectId).then((data) => {
              this.floors = data.entity
              this.formInline.floorId = ''
          })
      },

    onSubmit() {
      this.list();
    },
    list() {
        const loading = this.$loading({
            lock: true,
            text: 'Loading',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
        });
      this.formInline.pageInfo = this.pageInfo;
      prodcutTrack.findAllByFormInlineAndFloor(this.formInline).then(data => {
        this.datas = data.entity.list;
        this.pageInfo.total = data.entity.totalCount
          loading.close();
      })
    },
    handleSizeChange(val) {
      this.pageInfo.currentPage = 1;
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