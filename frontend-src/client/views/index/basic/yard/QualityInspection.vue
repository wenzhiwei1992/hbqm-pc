<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <my-button-group :model="buttonData"></my-button-group>
        </el-row>
      </el-header>
      <el-main
        ref="mainContent"
        :style="`height:${this.$store.state.mainContentHeight}px`"
      >
        <el-row v-show="!changeSwitch" style="height:100%;overflow-y:auto;">
          <el-card v-for="(item,index) of datas" :key="index" class="main-card">
            <div style="border-right:1px solid #ccc;flex:.4;">
              <span>质检人 : {{item.inspector}}</span>
            </div>
            <div style="margin-left:10px;flex:.4;">
              <span>创建时间 : {{item.dateCreated}}</span>
            </div>
            <div class="main-card-buttons" style="margin-left:10px;flex:.2;text-align: right;">
              <span @click="editClick(item)">编辑</span>
              <span @click="deletesClickRow(item)">删除</span>
            </div>
          </el-card>
        </el-row>
        
        <el-table
          v-show="changeSwitch"
          :data="datas"
          @selection-change="handleSelectionChange"
          size="mini"
          stripe
          border
          style="width:100%;"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
          height="100%"
        >
          <el-table-column type="selection">
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
          >
          </el-table-column>
          <el-table-column
            prop="inspector"
            label="质检人"
          >
          </el-table-column>
          <el-table-column
            prop="remarks"
            label="备注"
          >
          </el-table-column>
          <el-table-column
            prop="dateCreated"
            :formatter="formatterDate"
            label="创建时间"
          >
          </el-table-column>
          <el-table-column
            label="操作"
            align="left"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                @click="editClick(scope.row)"
                icon="el-icon-edit"
              ></el-button>
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

    <el-dialog
      :title="mode==='add'?'新建':'编辑'"
      :visible.sync="editFormVisible"
      v-if="editFormVisible"
    >
      <el-form
        :model="editForm"
        v-if="editForm"
        :rules="eidtFormRules"
        ref="editForm"
        label-width="80px"
      >
        <el-form-item
          label="类型"
          prop="type"
        >
          <el-input
            v-model="editForm.type"
            value="质检人"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item
          label="质检人"
          prop="inspector"
          id="ispEnabled"
        >
          <el-input v-model="editForm.inspector"></el-input>
        </el-form-item>
        <el-form-item
          label="备注"
          prop="remarks"
        >
          <el-input v-model="editForm.remarks"></el-input>
        </el-form-item>
      </el-form>
      <div
        slot="footer"
        class="dialog-footer"
      >
        <el-button @click="editFormVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="editFormSubmmit('editForm')"
        >确 定</el-button>
      </div>
    </el-dialog>

  </div>

</template>
<script>
import qualityInspectionModel from '../../../../model/basicdata/qualityInspectionModel'
import { Notification } from 'element-ui'
import moment from "moment";
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
      selected: [],
      mode: '',
      editFormTitle: '',
      editFormVisible: false,
      editForm: null,
      eidtFormRules: {
        inspector: [
          { required: true, message: '必填字段', trigger: 'blur' },
        ],
        type: [
          { required: true, message: '必填字段', trigger: 'blur' },
        ],
      },
      ispEnabled: false,
      logEnabled: false,
      changeSwitch:false,
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        },
        {
          name: '删除',
          event: 'deletesClick'
        },
        {
          name: '切换版本',
          event: 'switchClick'
        }
      ]
    }
  },
  mounted() {
    this.list();
  },
  methods: {
    switchClick(){
      this.changeSwitch = !this.changeSwitch
    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    editFormSubmmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.mode == 'edit') {
            const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            });
            qualityInspectionModel.update(this.editForm)
              .then((data) => {
                if (data.status == 'failed') {
                  Notification({
                    message: data.msg,
                    type: 'warning'
                  })
                  return
                }
                Notification({
                  message: this.GLOBAL.MSG.EDIT_SUCCESS,
                  type: 'success'

                })
                loading.close();
                this.editFormVisible = false
                this.list();
              }).catch(() => {

              }
              )
          } else {
            const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            });
            qualityInspectionModel.add(this.editForm)
              .then((data) => {
                if (data.status == 'failed') {
                  Notification({
                    message: data.msg,
                    type: 'warning'
                  })
                  return
                }
                Notification({
                  message: this.GLOBAL.MSG.ADD_SUCCESS,
                  type: 'success'

                })
                loading.close();
                this.editFormVisible = false
                this.list()
              })
          }

        } else {
          return false;
        }
      });
    },
    addClick() {
      this.mode = 'add';
      this.editForm = {
        inspector: '',
        type: '质检人',
        remarks: '',
      }
      this.editFormVisible = true
    },
    editClick(row) {
      qualityInspectionModel.getById(row.id).then(data => {
        this.mode = 'edit';
        this.editForm = data.entity;
        this.editFormVisible = true
      }).catch((data) => {
      })

    },
    deletesClick() {
      if (this.selected.length == 0) {
        this.$message(this.GLOBAL.MSG.SELECTED_ERR);
        return;
      }
      let ids = []
      this.selected.forEach((row) => {
        ids.push(row.id)
      })
      this.$confirm('确认删除选择项?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.commit('startLoading')
        qualityInspectionModel.deleteByIds(ids).then(() => {
          this.$store.commit('endLoading')
          this.list();
        })

      }).catch(() => {

      })
    },
    deletesClickRow(row) {
      let ids = []
      ids.push(row.id)
      this.$confirm('确认删除选择项?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.commit('startLoading')
        qualityInspectionModel.deleteByIds(ids).then(() => {
          this.$store.commit('endLoading')
          this.list();
        })

      }).catch(() => {

      })
    },
    list() {
      this.$store.commit('startLoading')
      qualityInspectionModel.list("质检人", this.pageInfo).then(data => {
        let entity = data.entity
        this.datas = data.entity.content;
        console.log(this.datas)
        this.datas.map(val=>{
          val.dateCreated = this.FormatDateTimeSecond(val.dateCreated)
        })
        this.pageInfo.total = data.entity.totalElements

        this.$store.commit('endLoading')
      }).catch(err => {
        console.log("err", err);
      })
    },
    handleSelectionChange(selected) {
      this.selected = selected
    },
    handleSizeChange(val) {
      this.pageInfo.pageSize = val;
      this.list()
    },
    handleCurrentChange(val) {
      this.pageInfo.currentPage = val;
      this.list()
    }
  },

}
</script>
<style scoped lang="scss">
  .main-card{
    margin-top:10px;
    .el-card__body{
      display: flex;
    }
    &:first{
      margin-top:0;
    }
    .main-card-buttons{
      span{
        cursor: pointer;
        color:#4391f4;
        border-bottom: 1px solid rgba(0,0,0,0);
        &:hover{
          border-bottom: 1px solid #4391f4;
        }
      }
    }
  }

</style>
<style>
  .main-card .el-card__body{
      display: flex;
      position: relative;
      width: 100%;
  }
  .main-card .el-card__body:after{
      content: "";
      position: absolute;
      left:0;
      top:0;
      height: 100%;
      width: 5px;
      background:#4391f4;
  }
</style>