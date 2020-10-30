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
      <el-container ref="mainContent" style="padding-left: 20px;" :style="`height:${this.$store.state.mainContentHeight}px`">
        <div style="width:300px;height:100%;overflow:auto;border: 1px lavender solid;">
          <el-aside width="100%" id="tab" style="height:100%;">
              <el-tree
                  height="100%"
                  :data="colList"
                  node-key="id"
                  highlight-current
                  default-expand-all
                  :expand-on-click-node="false"
                  @node-click="nodeClick"
                  class="building-andHouse-type-tree"
              >
                  <div class="custom-tree-node" slot-scope="{ node, data }" @mouseenter="mouseenter(node)" @mouseleave="mouseleave(node)">
                      <span>{{ node.label }}</span>
                      <el-button
                          class="icon-style"
                          :style="!node.del?'visibility:hidden;':'visibility:visible;'"
                          type="text"
                          size="mini"
                          @click.stop="() => deleteClick(node,data)">
                          <i class="el-icon-delete"></i>
                      </el-button>
                      <el-button
                          class="icon-style"
                          style="margin-left:0;"
                          :style="!node.edit?'visibility:hidden;':'visibility:visible;'"
                          type="text"
                          size="mini"
                          @click.stop="() => editClick(node,data)">
                          <i class="el-icon-edit"></i>
                      </el-button>
                  </div>
              </el-tree>
          </el-aside>
        </div>
        <el-main
          ref="mainContent"
          :style="`height:${this.$store.state.mainContentHeight}px`"
        >
          <el-table
            :data="codeExplainContents"
            size="mini"
            stripe
            border
            style="width: 100%"
            height="100%"
          >
            <el-table-column
              prop="codeNo"
              label="代码大类"
              :show-overflow-tooltip="true"
            >
            </el-table-column>
            <el-table-column
              prop="codeName"
              label="代码名称"
              :show-overflow-tooltip="true"
            >
            </el-table-column>
            <el-table-column
              prop="isEnabled"
              :formatter="formatBoolean"
              label="启用"
              width="50"
            >
            </el-table-column>
            <el-table-column
              prop="sort"
              label="排序"
              width="50"
            >
            </el-table-column>
            <!-- <el-table-column
              prop="homeTabName"
              label="菜单名称"
              width="100"
            >
            </el-table-column>
            <el-table-column
              prop="homeTabIcon"
              label="图标"
              align="center"
              width="50"
            >
              <template slot-scope="scope">
                <i style="font-size:16px;" :class="scope.row.homeTabIcon"></i>
              </template>
            </el-table-column> -->
            <el-table-column
              prop="remark"
              label="代码备注"
              :show-overflow-tooltip="true"
            >
            </el-table-column>
            <el-table-column
              prop="dateCreated"
              label="创建时间"
              :formatter="formatterDate"
              :show-overflow-tooltip="true"
            >
            </el-table-column>
          </el-table>
        </el-main>
      </el-container>
    </el-container>

    <el-dialog
      :title="mode==='add'?'新建':'编辑'"
      :visible.sync="editFormVisible"
      width="80%"
    >
      <el-form
        :model="editForm"
        v-if="editForm"
        :rules="eidtFormRules"
        ref="editForm"
        label-width="120px"
      >
        <el-form-item
          label="所属模块"
          prop="codeBelong"
        >
          <el-select
            v-model="editForm.codeBelong"
            placeholder=""
          >
            <el-option
              v-for="item in codeBelongs"
              :key="item.id"
              :label="item.name"
              :value="item.name"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="系统代码"
          prop="codeNo"
        >
          <el-input
            v-model="editForm.codeNo"
            :disabled="mode==='add'?disabled:''"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="代码名称"
          prop="codeName"
        >
          <el-input v-model="editForm.codeName"></el-input>
        </el-form-item>
        <el-form-item
          label="代码备注"
          prop="remark"
        >
          <el-input v-model="editForm.remark"></el-input>
        </el-form-item>
        <!--子项动态添加-->
        <el-form-item>
          <el-table
            :data="editForm.codeContents"
            size="mini"
            stripe
            border
            height="320"
          >
            <el-table-column
              prop="codeNo"
              label="代码大类"
              width="150"
              :show-overflow-tooltip="true"
            >
            </el-table-column>
            <el-table-column
              prop="codeName"
              label="代码名称"
              width="200"
            >
              <template slot-scope="scope">
                <el-input v-model="scope.row.codeName"></el-input>
              </template>
            </el-table-column>
            <!-- <el-table-column
              prop="homeTabName"
              label="菜单名称"
              align="center"
              width="100"
            >
              <template slot-scope="scope">
                <el-input v-model="scope.row.homeTabName"></el-input>
              </template>
            </el-table-column>
            <el-table-column
                prop="homeTabIcon"
                label="图标"
                align="center"
                width="100"
            >
              <template slot-scope="scope">
                <el-input v-model="scope.row.homeTabIcon"></el-input>
              </template>
            </el-table-column>
            <el-table-column
                prop="homeTabTemplateName"
                label="组件名"
                width="80"
                align="center"
            >
              <template slot-scope="scope">
                <el-input v-model="scope.row.homeTabTemplateName"></el-input>
              </template>
            </el-table-column>
            <el-table-column
                prop="homeTabHtml"
                label="模板"
                width="150"
                align="center"
            >
              <template slot-scope="scope">
                <el-input v-model="scope.row.homeTabHtml"></el-input>
              </template>
            </el-table-column>
            <el-table-column
                prop="homeTabRef"
                label="ref"
                width="80"
                align="center"
            >
              <template slot-scope="scope">
                <el-input v-model="scope.row.homeTabRef"></el-input>
              </template>
            </el-table-column> -->
            <el-table-column
              prop="isEnabled"
              label="启用"
            >
              <template slot-scope="scope">
                <el-switch
                  :width="50"
                  v-model="scope.row.isEnabled"
                  :active-value="true"
                  :inactive-value="false"
                >
                </el-switch>
              </template>
            </el-table-column>
            <el-table-column
              prop="sort"
              label="排序"
            >
              <template slot-scope="scope">
                <el-input v-model="scope.row.sort"></el-input>
              </template>
            </el-table-column>
            <el-table-column
              prop="remark"
              label="代码备注"
            >
              <template slot-scope="scope">
                <el-input v-model="scope.row.remark"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right">
              <template slot-scope="scope">
                <el-button
                  type="danger"
                  size="mini"
                  @click="deletCodeContent(scope)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-col>
            <el-button @click="addCodeContent">新增一行</el-button>
          </el-col>
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
import codeExplain from '../../../../model/basicdata/codeExlpain'
import codeExplainContent from '../../../../model/basicdata/codeExlpainContent'
import { Notification } from 'element-ui'
import moment from "moment";
export default {
  data() {
    const check = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('必填项'));
      } else {
        callback();
      }
    };
    return {
      pageInfo: {
        currentPage: 1,
        total: 0,
        pageSize: 9999,
        pageSizes: [9999]
      },
      datas: [],
      selected: [],
      expands: [],
      codeBelongs: [
        {
          id: 1,
          name: '基础数据模块'
        },
        {
          id: 2,
          name: '生产模块'
        },
        {
          id: 3,
          name: '项目模块'
        },
        {
          id: 4,
          name: '移动App模块'
        },
        {
          id: 5,
          name: '发货模块'
        },
        {
          id: 6,
          name: '订单模块'
        },
        {
          id:7,
          name:'首页图表模块'
        }
      ],
      mode: '',
      editFormTitle: '',
      editFormVisible: false,
      contentsVisible: false,
      editForm: null,
      editContentForm: null,
      eidtFormRules: {
        codeNo: [
          { required: true, message: '必填项', trigger: 'blur' },
            { min: 1, max: 10, message: '长度请控制在10个字符以内', trigger: 'blur' }
        ],
        codeBelong: [
          { required: true, validator: check, trigger: 'blur' },
            { min: 1, max: 10, message: '长度请控制在20个字符以内', trigger: 'blur' }
        ],
        codeName: [
          { required: true, message: '必填项', trigger: 'blur' },
            { min: 1, max: 10, message: '长度请控制在10个字符以内', trigger: 'blur' }
        ]
      },
      codeExplainContents: [],
      codeExplainId: '',
      disabled: false,
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        },
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
          event: 'deleteClick',
        },
      ],
      colList:[]
    }
  },
  mounted() {
    this.list();
  },
  methods: {
    formatBoolean: function (row, column, cellValue) {
      var ret = ''
      if (cellValue) {
        ret = "是"
      } else {
        ret = "否"
      }
      return ret;
    },
    showSubData(row, expandedRows) {
      if (this.expands.some((e) => e == row.id)) {
        this.expands = []
      } else {
        this.expands = []
        this.expands.push(row.id)
      }

      codeExplainContent.getAllByCodeNo(row.codeNo).then(data => {
        this.codeExplainContents = data.entity;
        console.log("this.codeExplainContents", this.codeExplainContents)
      })
    },
    deletCodeContent(scope) {
      this.editForm.codeContents.splice(scope.$index, 1)
    },
    addCodeContent() {
      if (this.editForm.codeNo == '' || this.editForm.codeNo == undefined) {
        this.$message.warning("请先输入系统代码")
        return
      }
      let codeNo = this.editForm.codeNo;
      let tmp = {
        codeNo: codeNo,
        codeName: '',
        remark: '',
        isEnabled: true,
        homeTabIcon:'',
        homeTabTemplateName:'',
        homeTabHtml:'',
        homeTabRef:'',
        sort: 999
      }
      this.editForm.codeContents.push(tmp);
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
            codeExplain.update(this.editForm)
              .then((data) => {
                if (data.status == 'failed') {
                  Notification({
                    message: data.msg,
                    type: 'warning'
                  })
                  return
                }
                Notification({
                  message: '编辑成功',
                  type: 'success'

                })
                loading.close()
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
            codeExplain.add(this.editForm)
              .then((data) => {
                if (data.status == 'failed') {
                  Notification({
                    message: data.msg,
                    type: 'warning'
                  })
                  return
                }
                Notification({
                  message: '新建成功',
                  type: 'success'

                })
                loading.close()
                this.editFormVisible = false
                this.list()
              })
          }

        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    addClick() {
      this.mode = 'add';
      this.editForm = {
        codeNo: '',
        codeBelong: '',
        codeName: '',
        codeExplain: '',
        codeContents: []
      }
      this.editFormVisible = true
    },
    editClick(node) {
      codeExplain.getById(node.data.id).then(data => {
        this.mode = 'edit';
        this.editForm = data.entity;
        this.editFormVisible = true
      })

    },
    deleteClick(node) {
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.commit('startLoading')
        codeExplain.delete(node.data.id).then(data => {
          this.$store.commit('endLoading')
          this.list();
        })
      })
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
      this.$confirm('确认删除选择项?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.commit('startLoading')
        codeExplain.deleteByIds(ids).then(() => {
          this.$store.commit('endLoading')
          this.list();
        })
      })
    },
    list() {
      this.$store.commit('startLoading')
      codeExplain.list(this.pageInfo).then(data => {
        this.datas = data.entity.content;
        this.colList = data.entity.content;
        data.entity.content.forEach(val=>{
          val.label = val.codeName
        })
        this.pageInfo.total = data.entity.totalElements
        this.$store.commit('endLoading')
      }).catch(err => {
        console.log("err", err);
      })
    },
    /**
     * 点击树状图tree 获取当前节点数据(node)
     * @param node
     */
    nodeClick(node) {
        console.log('node', node)
        this.showSubData(node)
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
    },
    mouseenter(node) {
        this.$set(node, 'del', true)
        this.$set(node, 'add', true)
        this.$set(node, 'edit', true)
    },
    mouseleave(node) {
        this.$set(node, 'del', false)
        this.$set(node, 'add', false)
        this.$set(node, 'edit', false)
    },
  },

}
</script>
<style lang="scss" scoped>
  .building-andHouse-type-tree{
    .custom-tree-node{
      width:100%;
      line-height: 28px;
      .icon-style{
        float:right;
        &:hover{
          background:#4fa4fc;
          color:#fff;
        }
      }
    }
  }

</style>
