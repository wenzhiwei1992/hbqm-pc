<template>
  <div>
    <el-header
      ref="headOperationBar"
      style="height:30px;padding:0;"
    >
      <el-row>
        <my-button-group :model="buttonData"></my-button-group>
      </el-row>
    </el-header>
    <el-main
      style="display:none;"
      ref="mainContent"
      :style="`height:${this.$store.state.mainContentHeight}px`"
    >
    </el-main>
    <!-- <div class="currency-style-box"> -->
    <el-container>
      <el-aside
        width="300px"
        id="tab"
        tyle="overflow-y: auto;overflow-x: visible"
        @tab-click="handleClick"
      >
        <el-tree
          style="width:450px;"
          ref="vuetree"
          :data="data"
          node-key="id"
          :default-expand-all="false"
          :default-expanded-keys="idArr"
          :expand-on-click-node="false"
          :auto-expand-parent="false"
          :highlight-current="true"
          @node-click="handleNodeClick"
          @node-contextmenu="nodeContextmenu"
          @node-drag-end="handleDragEnd"
          @node-drop="handleDrop"
          draggable
          :allow-drop="allowDrop"
          :allow-drag="allowDrag"
        >
          <span
            class="custom-tree-node"
            slot-scope="{ node, data }"
            @mouseenter="mouseenter(node)"
            @mouseleave="mouseleave(node)"
          >
            <span v-if="data.labelNo">({{data.labelNo}})</span><span>{{ node.label }}</span>
            <span>
              <el-button
                class="custom-btn"
                v-show="node.add"
                type="text"
                size="mini"
                icon="el-icon-plus"
                circle
                @click="() => append(data)"
              >
              </el-button>
              <el-button
                class="custom-btn"
                v-show="node.edit"
                type="text"
                size="mini"
                icon="el-icon-edit"
                v-if="node.disabled!=false"
                @click="() => modifiy(node, data)"
              >
              </el-button>
              <!-- v-if="node.disabled=='false'"-->
              <el-button
                class="custom-btn"
                v-show="node.del"
                type="text"
                size="mini"
                icon="el-icon-delete"
                v-if="node.disabled!=false"
                @click="() => remove(node, data)"
              >
              </el-button>
            </span>
          </span>
        </el-tree>
      </el-aside>
      <el-main>
        <el-table
          :data="productTypes"
          :height="GLOBAL.tabelHeight"
          style="width: 100% ;margin-top: 20px"
          header-row-class-name="header-row"
          size="mini"
          @selection-change="handleSelectionChange"
          stripe
          border
          tooltip-effect="light"
          @row-click="clickRow"
          row-key="id"
        >
          <el-table-column
            type="selection"
            width="50"
          >
          </el-table-column>
          <el-table-column
            type="index"
            label="序号"
            width="60"
          >
          </el-table-column>
          <el-table-column
            prop="name"
            label="名称"
          >
          </el-table-column>
          <el-table-column
            prop="typeNo"
            label="编号"
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
              <el-tooltip
                class="item"
                effect="light"
                content="编辑"
                placement="top"
              >
                <el-button
                  size="mini"
                  icon="el-icon-edit"
                  @click="editClick(scope.row)"
                ></el-button>
              </el-tooltip>
              <el-tooltip
                class="item"
                effect="light"
                content="删除"
                placement="top"
              >
                <el-button
                  size="mini"
                  icon="el-icon-circle-close"
                  type="danger"
                  @click="deleteClick(scope.row)"
                >
                </el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
        <!-- <el-button style="float: left" size="mini" icon="el-icon-plus" type="success" @click="addClick">新增
                </el-button>
                <el-button style="float: left" size="mini" icon="el-icon-close" type="danger" @click="deletesClicks">删除
                </el-button> -->
        <el-row
          type="flex"
          justify="space-around"
          align="middle"
        >
          <el-col>

          </el-col>
          <el-col>

          </el-col>
        </el-row>
      </el-main>
    </el-container>

    <el-dialog
      :title="mode==='add'?'新建':'编辑'"
      :close-on-click-modal="false"
      @close='closeDialog'
      :visible.sync="editFormVisible"
      v-if="editFormVisible"
    >
      <product-type-edit
        :editForm="editForm"
        :mode="mode"
        @product-type-finish="productTypeFinish"
        @product-type-cancel="productTypeCancel"
      >
      </product-type-edit>
    </el-dialog>
  </div>
</template>

<script>
import productTypeModel from '../../../../model/basicdata/productType'
import productTypeEdit from './ProductTypeEdit.vue'
import { Notification } from 'element-ui'
import moment from "moment";

export default {
  name: "ProductTypeList",
  components: {
    'product-type-edit': productTypeEdit,
  },
  data() {
    const data = [{
      id: 0,
      label: '构件类型',
      disabled: false,
      children: [{
        id: '',
        label: '',
        children: [],
      }]
    }];
    return {
      data: JSON.parse(JSON.stringify(data)),
      productTypes: [],
      editForm: {
        productCategoriesName: '',
        name: '',
        id: '',
        typeNo: '',
        remarks: '',
        parentId: null,
        codeLevel: '',
        blnvCEnd: '',
        state: '',
        ifSync: '',
        parentName: '',
      },
      editFormVisible: false,
      mode: '',
      addProductTypeId: '',
      currentNodeKey: '',
      idArr: [],
      selected: [],
      buttonData: [
        {
          name: '新建',
          event: 'addClick'
        },
        {
          name: '删除',
          event: 'deletesClicks'
        }
      ],
    }

  },
  mounted() {
    this.list()
    //默认显示第一级信息
    let info = {
      id: 0
    }
    this.currentNodeKeyClick(info);
    this.handleNodeClick(info)
  },
  methods: {
    // 获取free的所有id 下拉全部展开
    setTreeArrId(treeArr) {
      let that = this
      treeArr.forEach(val => {
        that.idArr.push(val.id)
        val.children.forEach(item => {
          that.idArr.push(item.id)
          item.children.forEach(page => {
            that.idArr.push(page.id)
          })
        })
      })
    },
    /**
     * 关闭前重新渲染页面
     */
    closeDialog() {
      this.editForm = {
        productCategoriesName: '',
        name: '',
        id: '',
        typeNo: '',
        remarks: '',
        parentId: null,
        codeLevel: '',
        blnvCEnd: '',
        parentName: '',
      }
    },

    list() {
      productTypeModel.getProductTypesToTree().then((data) => {
        this.data[0].children = data.entity
        this.setTreeArrId(this.data)
      })

    },
    /**
     * 默认展开节点高亮
     */
    currentNodeKeyClick(data) {
      this.$nextTick(function () {
        this.$refs.vuetree.setCurrentKey(data.id)
      })
    },
    handleClick(tab, event) {


    },
    handleNodeClick(data, node) {
      // console.log('addProductTypeId------>',this.addProductTypeId)
      productTypeModel.findProductTypesByParentId(data.id).then((data1) => {
        this.productTypes = data1.entity
        console.log('data------>', data)
        this.addProductTypeId = data.id
      })
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
    nodeContextmenu(data, node) {
        // log('data',data)
        // log('node',node)
    },
    append(data) {
      this.mode = this.GLOBAL.STATUS.ADD
      //0为前端页面显示第一级'构件类型'
      if (data.id != 0) {
        productTypeModel.getById(data.id).then((data1) => {
          this.editForm.parentId = data1.entity.id
          this.editForm.parentName = data1.entity.name
          this.editFormVisible = true
        })
      } else {
        this.editFormVisible = true
      }
    },

    remove(node, data) {
      this.$confirm('此操作将永久删除该构件类型, 是否继续?', '提示', {
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
        productTypeModel.delete(node.data.id).then(data => {
          if (data.status == this.GLOBAL.STATUS.FAIL) {
            Notification({
              message: data.msg,
              type: 'warning'
            })
            loading.close()
            return
          }
          Notification({
            message: this.GLOBAL.MSG.DELETE_SUCCESS,
            type: 'success'
          })
          loading.close()
          this.list()

        })
      }).catch(() => {
      })
    },
    modifiy(node, data) {
      this.mode = this.GLOBAL.STATUS.UPDATE
      productTypeModel.getById(data.id).then((data1) => {
        this.editForm = data1.entity
        this.editFormVisible = true
      })
    },
    clickRow() {

    },
    addClick() {
      console.log('this.addProductTypeId ------->', this.addProductTypeId)
      this.mode = this.GLOBAL.STATUS.ADD
      /*  if(this.addProductTypeId == ''){
            this.$message('请选择构件类型');
            return;
        }*/

      if (this.addProductTypeId != 0) {
        productTypeModel.getById(this.addProductTypeId).then((data1) => {
          this.editForm.parentId = data1.entity.id
          this.editForm.parentName = data1.entity.name
          this.editFormVisible = true
        })
      } else {
        this.editFormVisible = true
      }
    },
    editClick(row) {
      this.mode = this.GLOBAL.STATUS.UPDATE
      productTypeModel.getById(row.id).then((data1) => {
        this.editForm = data1.entity
        this.editFormVisible = true
      })
    },
    deleteClick(row) {
      this.$confirm('此操作将永久删除该构件类型, 是否继续?', '提示', {
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
        productTypeModel.delete(row.id).then(data => {
          if (data.status == this.GLOBAL.STATUS.FAIL) {
            Notification({
              message: data.msg,
              type: 'warning'
            })
            loading.close()
            return
          }
          Notification({
            message: this.GLOBAL.MSG.DELETE_SUCCESS,
            type: 'success'
          })
          loading.close()
          this.list()
          console.log('row------>', row)
          //为null时为第一级，赋予0值方便查询
          if (row.parentId == null) {
            row.parentId = 0
          }
          let info = {
            id: row.parentId
          }
          this.handleNodeClick(info)

        })
      }).catch(() => {
      })
    },
    deletesClicks() {
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
        const loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        productTypeModel.deleteByIds(ids).then((data) => {
          if (data.status == this.GLOBAL.STATUS.FAIL) {
            Notification({
              message: data.msg,
              type: 'warning'
            })
            loading.close()
            return
          }
          Notification({
            message: this.GLOBAL.MSG.DELETE_SUCCESS,
            type: 'success'
          })
          loading.close()
          this.list()
          let info = {
            id: 0
          }
          this.handleNodeClick(info)
        })

      }).catch(() => {

      })
    },
    /**
     * 拖拽事件
     * @param node
     * @param ev
     */
    handleDragEnd(draggingNode, dropNode, dropType, ev) {
      /*   if(dropNode.label=='构件类型'){
             return type !== 'inner';
         }*/
      console.log('tree drag end: ', dropNode && dropNode.label, dropType);
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      let model = {
        draggingNodeId: draggingNode.key,
        dropNodeId: dropNode.key,
        dropType: dropType,
      }
      productTypeModel.updateProductTypeCodeLevelByDrag(model).then((data) => {
        this.list()
        let info = {
          id: data.entity
        }
        this.handleNodeClick(info)
        // this.currentNodeKeyClick(this.addProductTypeId)
      })
    },
    allowDrop(draggingNode, dropNode, type) {
      if (type == 'prev' && dropNode.data.label == '构件类型') {
        return type !== 'prev';
      } else if (type == 'next' && dropNode.data.label == '构件类型') {
        return type !== 'next';
      } else {
        return true;
      }
    },
    allowDrag(draggingNode) {
      return draggingNode.data.label.indexOf('构件类型') === -1;
    },
    productTypeFinish() {
      //取消前重新渲染页面
      this.editForm = {
        productCategoriesName: '',
        name: '',
        id: '',
        typeNo: '',
        remarks: '',
        parentId: null,
        codeLevel: '',
        blnvCEnd: '',
        parentName: '',
      }

      this.editFormVisible = false

      this.list()
      let info = {
        id: 0
      }
      /*     if(productType.parentId == null){
               productType.parentId = 0
           }
           console.log('productType------>',productType)
           this.idArr = [productType.parentId,productType.id]*/
      this.currentNodeKeyClick(info)
      this.handleNodeClick(info)

    },
    productTypeCancel() {
      //取消前重新渲染页面
      this.editForm = {
        productCategoriesName: '',
        name: '',
        id: '',
        typeNo: '',
        remarks: '',
        parentId: null,
        codeLevel: '',
        blnvCEnd: '',
        parentName: '',
      }
      this.editFormVisible = false
      //this.list()
      let info = {
        id: 0
      }
      this.handleNodeClick(info)
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
  }

}
</script>

<style lang="scss" scoped>
.el-aside {
  color: #333;
  text-align: center;
}

.el-main {
  color: #333;
  text-align: center;
  margin-top: -20px;
}

#tab {
  border: 1px lavender solid;
  height: 500px;
}
/deep/span.custom-tree-node{
  display: flex;
  span{
    height: 24px;
    line-height: 24px;
    &:nth-child(2){
      max-width: 150px;
      overflow:hidden;
      word-wrap:normal;
      white-space:nowrap;
      text-overflow:ellipsis;
    }
    .custom-btn{
      margin-left:0;
      padding:7px;
    }
  }
} 
</style>