<#--noinspection ALL-->
<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
    <#if hasQuery>
      <!-- 搜索 -->
      <el-input v-model="query.value" clearable placeholder="输入搜索内容" style="width: 200px;" class="filterDto-item" @keyup.enter.native="list()"/>
      <el-select v-model="query.type" clearable placeholder="类型" class="filterDto-item" style="width: 130px">
        <el-option v-for="item in queryTypeOptions" :key="item.key" :label="item.display_name" :value="item.key"/>
      </el-select>
      <el-button class="filterDto-item" size="mini" type="success" icon="el-icon-search" @click="list()">搜索</el-button>
    </#if>
      <!-- 新增 -->
      <div style="display: inline-block;margin: 0px 2px;">
        <el-button
          class="filterDto-item"
          size="mini"
          type="primary"
          icon="el-icon-plus"
          @click="add">新增</el-button>
      </div>
    </div>
    <!--表单组件-->
    <eForm ref="form" :is-add="isAdd"/>
    <!--表格渲染-->
    <el-table v-loading="loading" :data="data" size="small" style="width: 100%;">
      <#if columns??>
          <#list columns as column>
          <#if column.columnShow = 'true'>
              <#if column.columnType != 'Timestamp'>
      <el-table-column prop="${column.changeColumnName}" label="<#if column.columnComment != ''>${column.columnComment}<#else>${column.changeColumnName}</#if>"/>
              <#else>
      <el-table-column prop="${column.changeColumnName}" label="<#if column.columnComment != ''>${column.columnComment}<#else>${column.changeColumnName}</#if>">
        <template slot-scope="scope">
          <span>{{ formatterDate(scope.row.${column.changeColumnName}) }}</span>
        </template>
      </el-table-column>
              </#if>
          </#if>
          </#list>
      </#if>
      <el-table-column label="操作" width="150px" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" icon="el-icon-edit" @click="edit(scope.row)"/>
          <el-popover
            :ref="scope.row.${pkChangeColName}"
            placement="top"
            width="180">
            <p>确定删除本条数据吗？</p>
            <div style="text-align: right; margin: 0">
              <el-button size="mini" type="text" @click="$refs[scope.row.${pkChangeColName}].doClose()">取消</el-button>
              <el-button :loading="delLoading" type="primary" size="mini" @click="subDelete(scope.row.${pkChangeColName})">确定</el-button>
            </div>
            <el-button slot="reference" type="danger" icon="el-icon-delete" size="mini"/>
          </el-popover>
        </template>
      </el-table-column>
    </el-table>
    <!--分页组件-->
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
  </div>
</template>

<script>
import model from 'path'
<#if hasTimestamp>
import moment from "moment";
</#if>
import eForm from './form'
import {Notification} from 'element-ui'
export default {
  components: { eForm },
  data() {
    return {
      delLoading: false,
      pageInfo:{
        currentPage:1,
        total:0,
        pageSize: this.GLOBAL.pageSize,
        pageSizes:this.GLOBAL.pageSizes
      },
      isAdd:true,
      <#if hasQuery>
      queryTypeOptions: [
        <#if queryColumns??>
        <#list queryColumns as column>
        { key: '${column.changeColumnName}', display_name: '<#if column.columnComment != ''>${column.columnComment}<#else>${column.changeColumnName}</#if>' }<#if column_has_next>,</#if>
        </#list>
        </#if>
      ]
      </#if>
      query:{}, data:[], loading:false, condition:{}
    }
  },
  created() {
  },
  mounted() {
      this.list()
  },
  methods: {
  <#if hasTimestamp>
    formatterDate(cellValue){
      var date = cellValue;
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
  </#if>
    list() {
      <#if hasQuery>
      const query = this.query
      const type = query.type
      const value = query.value
      if (type && value) { this.condition[type] = value }
      </#if>
      this.condition.pageInfo = this.pageInfo
      model.list(condition).then(data=>{
        this.data = data.entity
      }).catch(err=>{
        console.error(err)
      })
    },

    subDelete(${pkChangeColName}) {
      this.delLoading = true
      model.delete(${pkChangeColName}).then(res => {
        this.delLoading = false
        this.$refs[${pkChangeColName}].doClose()
        this.list()
        Notification({
          message: '删除成功',
          type: 'success'
        })

      }).catch(err => {
        this.delLoading = false
        this.$refs[${pkChangeColName}].doClose()
        console.log(err.response.data.message)
      })
    },
    add() {
      this.isAdd = true
      this.$refs.form.dialog = true
    },
    edit(data) {
      this.isAdd = false
      const _this = this.$refs.form
      _this.form = {
        <#if columns??>
        <#list columns as column>
        ${column.changeColumnName}: data.${column.changeColumnName}<#if column_has_next>,</#if>
        </#list>
        </#if>
      }
      _this.dialog = true
    },
    handleSelectionChange(selected){
      this.selected=selected
    },
    handleSizeChange(val) {
      this.pageInfo.currentPage=1
      this.pageInfo.pageSize=val
      this.list()
    },
    handleCurrentChange(val) {
      this.pageInfo.currentPage=val
      this.list()
    },
  }
}
</script>

<style scoped>

</style>
