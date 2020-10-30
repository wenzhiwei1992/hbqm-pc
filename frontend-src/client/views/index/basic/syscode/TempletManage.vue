<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col></el-col>
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
          :data="templetList"
          @selection-change="handleSelectionChange"
          size="mini"
          stripe
          border
          height="100%"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
          style="width:100%"
        >
          <el-table-column
            type="selection"
            width="80"
          >
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            width="50"
          >
          </el-table-column>
          <el-table-column
            prop="templetNo"
            label="模版编号"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="templetName"
            label="模版名称"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="templetJson"
            label="模版内容"
          >
            <template slot-scope="scope">
              <span style="margin-left:5px;" v-for="(item,index) in scope.row.templetJson" :key="index">{{item.name}}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="remark"
            label="备注"
            width="140"
          >
          </el-table-column>
          <el-table-column
            prop="dateCreated"
            :formatter="formatterDate"
            label="创建时间"
            width="140"
          >
          </el-table-column>
          <el-table-column
            label="操作"
            fixed="right"
            :width="rowButtonGroupWidth(rowButtonData)"
          >
            <template slot-scope="scope">
              <my-row-button-group
                :row="scope.row"
                :model="rowButtonData"
              ></my-row-button-group>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
      <el-footer ref="footOperationBar">
        <el-row>
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="templetQuery.pageInfo.currentPage"
            :page-sizes="templetQuery.pageInfo.pageSizes"
            :page-size="templetQuery.pageInfo.pageSize"
            :total="templetQuery.pageInfo.total"
            layout="total, sizes, prev, pager, next, jumper"
            background
          >
          </el-pagination>
        </el-row>
      </el-footer>
    </el-container>

    <el-dialog
      :title="mode=='add'?'新建':'编辑'"
      v-if="editFormVisible"
      :visible.sync="editFormVisible"
      width="800px"
    >
      <templet-edit
        :edit-form-data="editForm"
        :mode="mode"
        @templet-finish="templetFinish"
        @templet-cancel="templetCancel"
      ></templet-edit>
    </el-dialog>
  </div>
</template>

<script>
import templetModel from '../../../../model/basicdata/templet'
import TempletEdit from './TempletEdit.vue'
import { Notification } from 'element-ui'
import moment from "moment"

export default {
  components: {
    'templet-edit': TempletEdit,
  },
  name: "Templet",
  data() {
    return {
      editForm: '',
      editFormVisible: false,
      mode: '',
      templetList: [],
      templetQuery: {
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        },
      },
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        }
      ],
      searchButtonData: [
      ],
      rowButtonData: [
        {
          name: '编辑',
          event: 'editClick',
        },
        {
          name: '删除',
          event: 'deletesClicks',
        },
      ]
    }
  },
  mounted() {
    this.showList()
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.showList()
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    showList() {
      templetModel.templetList(this.templetQuery).then((data) => {
        console.log('data------>', data)
        data.entity.content.map(val=>{
          if(val.templetJson){
            val.templetJson = JSON.parse(val.templetJson)
          }
        })
        this.templetList = data.entity.content
        this.templetQuery.pageInfo.total = data.entity.totalElements
      })
    },
    addClick() {
      this.mode = 'add'
      this.editForm = {
        templetNo: '',
        templetName: '',
        templetJson: '',
        remark: '',
      }
      this.editFormVisible = true
    },
    deletesClicks(row) {
      let ids = []
      // this.selected.forEach((row)=>{
      ids.push(row.id)
      // })
      this.$confirm('此操作将永久删除勾选系统模版, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        templetModel.templetDeletes(ids.join(";")).then((data) => {
          if (data.status == 'fail') {
            Notification({
              message: data.msg,
              type: 'warning'
            })
            loading.close()
            return
          }
          Notification({
            message: '删除成功',
            type: 'success'

          })
          this.showList()
          loading.close()
        })
      }).catch(() => {
      })
    },
    editClick(row) {
      templetModel.templetById(row.id).then((data) => {
        this.mode = 'update'
        if(data.entity.templetJson){
          data.entity.templetJson = JSON.parse(data.entity.templetJson)
        }
        this.editForm = data.entity
        this.editFormVisible = true
      })
    },
    formatterDate(row, column, cellValue, index) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    handleSelectionChange(selected) {
      this.selected = selected
    },
    handleSizeChange(val) {
      this.templetQuery.pageInfo.pageSize = val
      this.showList()
    },
    handleCurrentChange(val) {
      this.templetQuery.pageInfo.currentPage = val
      this.showList()
    },
    templetFinish() {
      this.editFormVisible = false
      this.showList()
    },
    templetCancel() {
      this.editFormVisible = false
      this.showList()
    },
  },
}
</script>

<style scoped>
</style>