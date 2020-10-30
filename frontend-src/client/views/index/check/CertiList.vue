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
          :data="entities"
          @selection-change="handleSelectionChange"
          ref="handleClickRowSelection"
          @row-click="handleClickRowSelection"
          height="100%"
          style="width:100%"
          header-row-class-name="header-row"
          size="mini"
          stripe
          border
          tooltip-effect="light"
          row-key="id"
        >
          <el-table-column
            type="selection"
            align="center"
          >
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            width="70"
          >
          </el-table-column>
          <el-table-column
            prop="productNamesString"
            label="构件名称"
            align="center"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="perCompDate"
            label="出厂日期"
            align="center"
            width="120"
            :formatter="momentFormat"
          >
          </el-table-column>
          <el-table-column
            prop="prodCompany"
            label="生产企业"
            align="center"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="designStrenthGrade"
            label="设计强度等级"
            align="center"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="strenthGrade"
            label="实际强度等级"
            align="center"
            width="100"
          >
          </el-table-column>
          <el-table-column
            prop="strenth"
            label="出厂强度"
            align="center"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="typeAndSpecification"
            label="种类及规格"
            align="center"
            width="120"
          >
          </el-table-column>
          <el-table-column
            prop="mechanicalBehavior"
            label="机械性能"
            align="center"
            width="120"
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
      :title="mode=='add'?'新建':'编辑'"
      :visible.sync="editFormVisible"
      width="50%"
      top="10px"
    >
      <model-edit
        :id="id"
        :mode="mode"
        @model-edit-finish="modelEditFinish"
        @model-edit-cancel="modelEditCancel"
      ></model-edit>
    </el-dialog>
  </div>

</template>
<script>
import model from '../../../model/model'
import CertiEdit from './CertiEdit.vue'
import moment from 'moment'
import { Notification } from 'element-ui'
export default {
  components: {
    'model-edit': CertiEdit,
  },
  data() {
    return {
      //表查询
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: this.GLOBAL.pageSize,
        pageSizes: this.GLOBAL.pageSizes
      },
      entity: {
        username: null,
        realName: null
      },
      entities: [],
      //行操作
      selected: [],
      //编辑表单
      mode: '',
      editFormVisible: false,
      id: null,
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        },
        {
          name: '删除',
          event: 'deletesClick'
        }
      ],
      searchButtonData: [
      ],
      rowButtonData: [

        {
          name: '编辑',
          event: 'editClick'
        },
        {
          name: '删除',
          event: 'deleteClick'
        },
      ]
    }
  },
  computed: {
  },
  mounted() {
    this.list()

  },
  methods: {
    //表查询
    list() {
      model.list('certi', this.entity, this.pageInfo).then(data => {
        let entity = data.entity
        let list = data.entity.content;
        this.entities = list
        this.pageInfo.total = data.entity.totalElements
      })
    },
    query() {
      this.pageInfo.currentPage = 1
      this.list()
    },
    handleSelectionChange(selected) {
      this.selected = selected
    },
    handleSizeChange(val) {
      this.pageInfo.currentPage = 1
      this.pageInfo.pageSize = val
      this.list()
    },
    handleCurrentChange(val) {
      this.pageInfo.currentPage = val
      this.list()
    },
    momentFormat(row, column, cellValue, index) {
      if (cellValue) {
        return moment(cellValue).format('YYYY-MM-DD HH:mm:ss')
      } else {
        return cellValue
      }

    },
    //编辑表单
    modelEditFinish() {
      this.editFormVisible = false
      this.list()
    },
    modelEditCancel() {
      this.editFormVisible = false
    },
    //行操作
    addClick() {
      this.mode = 'add'
      this.id = ''
      this.editFormVisible = true
    },
    editClick(row) {
      this.mode = 'update'
      this.id = row.id
      this.editFormVisible = true
    },
    deleteClick(row) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.commit('startLoading')
        model.delete('certi', row.id).then(data => {
          this.$store.commit('endLoading')
          this.list()
        })
      }).catch(() => { })
    },
    deletesClick() {
      if (this.selected.length == 0) {
        this.$message('请选择删除项');
        return;
      }
      let ids = []
      this.selected.forEach((row) => {
        ids.push(row.id)
      })
      this.$confirm('此操作将永久删除勾选记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.commit('startLoading')
        model.deletes('certi', ids.join(";")).then(() => {
          this.$store.commit('endLoading')
          this.list()
        })
      }).catch(() => { })

    },

  },

}
</script>
<style>
.el-table th,
.el-table tr.header-row {
  background-color: #f5f7fa;
  font-size: 1.1em;
}
#entity .el-form-item {
  margin-bottom: 5px;
}
</style>