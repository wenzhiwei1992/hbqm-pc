<template>
  <div id="user-choose">
    <el-form :inline="true" id="entity" :model="entity" ref="entity">
      <el-form-item label="组织" prop="orgId">
        <el-select v-model="entity.orgId" placeholder="请选择" clearable>
          <el-option v-for="item in orgs"
                     :key="item.id"
                     :label="item.orgName"
                     :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="用户名" prop="username">
        <el-input
            v-model="entity.username"
            size="mini"
            clearable>
        </el-input>
      </el-form-item>
      <el-form-item label="真实姓名" prop="realName">
        <el-input
            v-model="entity.realName"
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
          prop="username"
          label="用户名"
          align="center"
          width="100"
      >
      </el-table-column>
      <el-table-column
          prop="realName"
          label="真实姓名"
          align="center"
          width="100"
      >
      </el-table-column>
      <el-table-column
          prop="nickName"
          label="昵称"
          align="center"
          width="100"
      >
      </el-table-column>
      <el-table-column
          prop="mobile"
          label="手机"
          align="center"
          width="100"
      >
      </el-table-column>
      <el-table-column
          prop="email"
          label="邮箱"
          align="center"
          width="200"
      >
      </el-table-column>
      <el-table-column
          prop="telephone"
          label="固话"
          align="center"
          width="100"
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
import orgModel from '../../../../model/system/org'
import {Notification} from 'element-ui'

export default {
  props: ['visible', 'filter', 'groupOrgId'],
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
        username: null,
        realName: null,
        orgId: null,
      },
      orgs: [],
      entities: [],
      //行操作
      selectedRows: []
    }
  },
  computed: {},
  mounted() {
    orgModel.orgAll().then(data => {
      this.orgs = data.entity;
      console.log(this.orgs);//TO DELETE
    }).catch(() => {
      this.$message('加载组织列表失败')
    })
    this.list()
  },
  methods: {
    //列表查询
    list() {
      if (this.filter) {
        for (var attr in this.filter) {
          this.entity[attr] = this.filter[attr]
        }
      }
      console.log('entity-------------->', this.entity);//TO DELETE
      if (this.$store.state.user.level === '0') {
        this.entity.ids.push(this.$store.state.user.id)
      }
      //根据角色对应组织添加相应组织的用户
      this.entity.orgId = this.groupOrgId
      model.list('user', this.entity, this.pageInfo).then(data => {
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

      let ids = []
      this.selectedRows.forEach((row) => {
        ids.push(row.id)
      })
      this.$emit('user-choose-finish', ids)

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

#user-choose .el-table th, .el-table tr.table-row {
  cursor: pointer;
}
</style>
