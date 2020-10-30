<template>
  <div class="currency-style-box">
    <el-container>
      <el-header ref="headOperationBar">
        <el-row>
          <el-col>
            <!--工具栏-->
            <div class="head-container">
              <el-input
                v-model="query.name"
                clearable
                placeholder="请输入表名"
                style="width: 200px;"
                class="filter-item"
                @keyup.enter.native="list()"
              />
            </div>
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
        <!--表格渲染-->
        <el-table
          v-loading="loading"
          :data="data"
          size="small"
          style="width: 100%;"
          ref="handleClickRowSelection"
          height="100%"
          @row-click="handleClickRowSelection"
        >
          <el-table-column
            label="序号"
            width="85"
            align="center"
          >
            <template slot-scope="scope">
              <div>{{ scope.$index + 1 }}</div>
            </template>
          </el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            prop="tableName"
            label="表名"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="engine"
            label="数据库引擎"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="coding"
            label="字符编码集"
          />
          <el-table-column
            :show-overflow-tooltip="true"
            prop="remark"
            label="备注"
          />
          <el-table-column
            prop="createTime"
            label="创建日期"
          >
            <template slot-scope="scope">
              <span>{{ formatterDate(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            width="140px"
            align="center"
            fixed="right"
          >
            <template slot-scope="scope">
              <Generator :name="scope.row.tableName" />
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
import Generator from './generator.vue'
import generator from '../../../../model/system/generator'
import moment from "moment";
export default {
  name: 'GeneratorIndex',
  components: { Generator },
  data() {
    return {
      loading: false, dialog: false,
      form: { author: '', pack: '', path: '', moduleName: '', cover: 'false', apiPath: '', prefix: '' },
      rules: {
        author: [
          { required: true, message: '作者不能为空', trigger: 'blur' }
        ],
        pack: [
          { required: true, message: '包路径不能为空', trigger: 'blur' }
        ],
        moduleName: [
          { required: true, message: '包路径不能为空', trigger: 'blur' }
        ],
        path: [
          { required: true, message: '前端代码生成路径不能为空', trigger: 'blur' }
        ],
        apiPath: [
          { required: true, message: '前端Api文件生成路径不能为空', trigger: 'blur' }
        ],
        cover: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      },
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: 10,
        pageSizes: this.GLOBAL.pageSizes
      },
      query: {
        name: ''
      },
      data: [],
      loading: false,
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        },
      ],
      searchButtonData: [
        {
          name: '查询',
          event: 'list'
        }
      ],
      rowButtonData: [
        {
          name: '编辑',
          event: 'editClick',
        },
        {
          name: '删除',
          event: 'deleteClick',
        },
      ]
    }
  },
  mounted() {
    this.list();
  },
  methods: {
    formatterDate(cellValue) {
      var date = cellValue;
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
    list() {
      generator.list(this.query.name, this.pageInfo.currentPage - 1, this.pageInfo.pageSize).then(data => {
        this.data = data.content
        this.pageInfo.total = data.totalElements
      }).catch(err => {
        console.log(err)
      })
    },
    cancel() {
      this.resetForm()
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
  }
}
</script>

<style scoped>
</style>
