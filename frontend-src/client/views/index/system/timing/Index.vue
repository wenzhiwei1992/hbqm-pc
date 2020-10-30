<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>

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
            :data="quartzJobData"
            @selection-change="handleSelectionChange"
            ref="handleClickRowSelection"
            height="100%"
            @row-click="handleClickRowSelection"
            style="width:100%"
            header-row-class-name="header-row"
            size="mini"
            stripe
            border
            row-key="id"

        >
          <el-table-column
              type="selection"
              align="center">
          </el-table-column>
          <el-table-column
              align="center"
              prop="jobName"
              label="任务名称"/>
          <el-table-column
              align="center"
              prop="beanName"
              label="Bean名称"/>
          <el-table-column
              align="center"
              prop="methodName"
              label="执行方法"/>
          <el-table-column
              align="center"
              prop="params"
              label="参数"/>
          <el-table-column
              align="center"
              prop="cron"
              label="cron表达式"/>
          <el-table-column align="center" prop="isPause" width="90px" label="状态">
            <template slot-scope="scope">
              <el-tag :type="scope.row.isPause ? 'success' : 'warning'">{{ scope.row.isPause ? '运行中' : '已暂停' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="remarks" width="150px" label="描述"/>
          <el-table-column
              label="操作" fixed="right"
              :width="rowButtonGroupWidth(rowButtonData)">
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
              :current-page="quartzJobQuery.pageInfo.currentPage"
              :page-sizes="quartzJobQuery.pageInfo.pageSizes"
              :page-size="quartzJobQuery.pageInfo.pageSize"
              :total="quartzJobQuery.pageInfo.total"
              layout="total, sizes, prev, pager, next, jumper"
              background
          >
          </el-pagination>
        </el-row>
      </el-footer>
    </el-container>
    <el-dialog :title="mode=='add'?'新建':'编辑'"
               v-if="editFormVisible"
               :visible.sync="editFormVisible"
               width="730px"
               append-to-body
    >
      <timing-edit :edit-form="editForm"
                   :mode="mode"
                   @quartz-job-edit-finish="quartzJobEditFinish"
                   @quartz-job-edit-cancel="quartzJobEditCancel"

      ></timing-edit>
    </el-dialog>
  </div>
</template>

<script>
import timingModel from "../../../../model/system/timing";
import TimingEdit from "./TimingEdit.vue";
import {Notification} from "element-ui";
import menuModel from "../../../../model/system/menu";

export default {
  name: "index",
  components: {
    'timing-edit': TimingEdit,
  },
  data() {
    return {
      editForm: null,
      mode: '',
      editFormVisible: false,
      quartzJobQuery: {
        pageInfo: {
          currentPage: 1,
          total: 0,
          pageSize: this.GLOBAL.pageSize,
          pageSizes: this.GLOBAL.pageSizes
        },
      },
      quartzJobData: [],
      selected: '',
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        },
        {
          name: '删除',
          event: 'deleteClick'
        },
      ],
      searchButtonData: [
    /*    {
          name: '查询',
          event: "query"
        }*/
      ],
      rowButtonData: [
        {
          name: '编辑',
          event: 'editClick',
        },
        {
          name: '恢复',
          event: 'updateStatus',
          display:'!scope.row.isPause'
        },
        {
          name: '暂停',
          event: 'updateStatus',
          display:'scope.row.isPause'
        }
      ],
    }
  },
  mounted() {
    this.list()
  },
  methods: {
    deleteClick() {
      if (this.selected.length == 0) {
        this.$message('请选择删除项');
        return;
      }
      let ids = []
      this.selected.forEach((row) => {
        ids.push(row.id)
      })
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
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
        timingModel.quartzJobDelete(ids.join(";")).then(data => {
          if (data.status == 'failed') {
            Notification({
              message: data.msg,
              type: 'warning'
            })
            loading.close()
            return
          }
          Notification({
            message: "删除成功",
            type: "success"
          });
          loading.close();
          this.list()
        })
      }).catch(() => {
      })

    },
    editClick(row) {
      this.mode = 'update'
      timingModel.findById(row.id).then(data => {
        this.editForm = data.entity
        this.editFormVisible = true
      })
    },
    // 改变状态
    updateStatus(row) {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      timingModel.updateIsPause(row.id).then(data => {
        if (data.status == 'failed') {
          Notification({
            message: data.msg,
            type: 'warning'
          })
          loading.close()
          return
        }
        Notification({
          message: '编辑成功',
          type: 'success'
        })
        loading.close()
        this.list()
      }).catch(err => {

      })
    },

    list() {
      timingModel.getQuartzJobList(this.quartzJobQuery).then((data) => {
        console.log('data------->', data.entity)
        this.quartzJobData = data.entity.list
        this.quartzJobQuery.pageInfo.total = data.entity.totalCount
      })

    },
    addClick() {
      this.mode = 'add'
      this.editForm = {
        jobName: '',
        remarks: '',
        beanName: '',
        methodName: '',
        cron: '',
        isPause: true,
        params: '',
        pauseAfterFailure: true,
      }
      this.editFormVisible = true
    },
    handleSelectionChange(selected) {
      this.selected = selected
    },
    handleSizeChange(val) {
      this.quartzJobQuery.pageInfo.currentPage = 1
      this.quartzJobQuery.pageInfo.pageSize = val
      this.list()
    },
    handleCurrentChange(val) {
      this.quartzJobQuery.pageInfo.currentPage = val
      this.list()
    },
    //编辑表单
    quartzJobEditFinish() {
      this.editFormVisible = false
      this.list()
    },
    quartzJobEditCancel() {
      this.editFormVisible = false
    },
  },
}
</script>

<style scoped>

</style>
