<template>
  <div id="group-choose">
    <el-form :inline="true" id="entity" :model="entity" ref="entity">
      <el-form-item label="角色名" prop="groupName">
        <el-input
            v-model="entity.groupName"
            size="mini"
            clearable>
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button size="mini" icon="el-icon-search" @click="query()">查找</el-button>
      </el-form-item>
    </el-form>
    <el-table
        :data="entities"
        style="width:100%"
        header-row-class-name="header-row"
        row-key="id"
        size="mini"
        stripe
        border
        @selection-change="selectionChange"
        row-class-name="table-row"
        ref="table"
    >
      <el-table-column
          type="selection"
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="groupName"
          label="角色名称"
          align="center"
          width="100"
          fixed
      >
      </el-table-column>
      <el-table-column
          prop="groupDesc"
          label="角色描述"
          align="center"
          width="200"
          fixed
      >
      </el-table-column>
    </el-table>
    <el-row type="flex" justify="space-around" align="middle" style="margin-top:15px">
      <el-col>
        <el-button @click="addClick" type="primary">确定</el-button>
      </el-col>
      <el-col>
        <el-row type="flex" justify="end" align="middle">
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

      </el-col>
    </el-row>

  </div>


</template>
<script>
import model from '../../../../model/model'
import {Notification} from 'element-ui'

export default {
  props: ['user', 'visible'],
  components: {},
  data() {
    return {
      //列表查询
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: this.GLOBAL.pageSize,
        pageSizes: this.GLOBAL.pageSizes
      },
      entity: {
        groupName: null,
      },
      entities: [],
      //行操作
      selectedRows: []
    }
  },
  computed: {
    userIds() {
      let ids = []
      this.user.groups.forEach((u) => {
        ids.push(u.id)
      })
      return ids
    }
  },
  mounted() {
    this.list()
  },
  methods: {
    //列表查询
    list() {
      this.entity.ids = this.userIds;
      this.entity.user = this.user
      model.list('group', this.entity, this.pageInfo).then(data => {
        let list = data.entity.content;
        this.entities = list
        this.pageInfo.total = data.entity.totalElements

      })
    },
    query() {
      this.pageInfo.currentPage = 1
      this.list()
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
    //行操作
    selectionChange(selection) {
      this.selectedRows = selection
    },
    addClick() {
      if (this.selectedRows.length == 0) {
        Notification({
          message: '请选择',
          type: 'warning'
        })
        return
      }
      let addRows = []
      this.selectedRows.forEach((group) => {
        addRows.push({userId: this.user.id, groupId: group.id})
      })
      model.adds('userGroup', addRows).then(data => {
        if (data.status == 'fail') {
          Notification({
            message: data.msg,
            type: 'warning'
          })
          return
        }
        Notification({
          message: '保存成功',
          type: 'success'

        })
        this.$store.commit('endLoading')
        this.$emit('group-choose-finish')
      })

    }


  },
  watch: {
    visible: function (n, o) {
      if (n) {
        this.query()
      }
    }
  },


}
</script>
<style>
.el-table th, .el-table tr.header-row {
  background-color: #f5f7fa;
  font-size: 1.1em;
}

#entity .el-form-item {
  margin-bottom: 5px;
}

#group-choose .el-table th, .el-table tr.table-row {
  cursor: pointer;
}
</style>
