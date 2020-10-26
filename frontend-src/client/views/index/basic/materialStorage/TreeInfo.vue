<template>
  <div id="app">
    <el-aside>
      <el-tree
        class="tree"
        :data="treeArr"
        :props="defaultProps"
        show-checkbox
        node-key="id"
        size="mini"
        stripe
        border
        router-link
        ref="treeForm"
        check-strictly
        @check="nodeClick"
        @check-change="handleNodeClick"
        :default-checked-keys="defaultCheckedKeys"
        :default-expanded-keys="defaultExpandedKeys"
      ></el-tree>
    </el-aside>
    <div slot="footer" class="dialog-footer" style="width:100%;margin-top:20px;" size="mini">
      <el-button @click="editTreeCancel">取 消</el-button>
      <el-button type="primary" @click="editTreeSubmmit">确 定</el-button>
    </div>
  </div>
</template>

<script>
import moment from "moment";
export default {
  name: "TreeInfo",
  data() {
    return {
      checkedId: '',
      checkedName: "",
      defaultCheckedKeys:this.baseCheckedId,
      defaultExpandedKeys:[],
      defaultProps: {
        children: "children",
        label: "label"
      }
    };
  },
  props: ["treeArr","baseCheckedId","baseCheckParentId"],
  mounted() {
    console.log(
    this.setTreeArrId(this.treeArr))
  },
  methods: {
    // 获取free的所有id 下拉全部展开
    setTreeArrId(treeArr){
      let that = this
      let idKey = []
      treeArr.forEach((val,index)=>{
        that.defaultExpandedKeys.push(val.id)
        // val.children.forEach(val=>{
        //   that.defaultExpandedKeys.push(val.id)
        // })
      })
    },
    setCheckedKeys(){
      this.$refs.treeForm.setCheckedKeys(this.defaultCheckedKeys);
    }, editTreeSubmmit() {
      this.$emit("tree-edit-submit", this.checkedId, this.checkedName);
    },
    editTreeCancel() {
      this.$emit("tree-edit-cancel");
    },
    nodeClick(data, checked, node) {
      this.checkedName = data.label;
   //   console.log(this.$refs.treeForm.getNode(data.id))
   //this.$refs.treeForm.getNode(id).parent.data
    },
    handleNodeClick(data, checked, node) {
      if (checked === true) {
        this.checkedId = data.id;
        this.$refs.treeForm.setCheckedKeys([data.id]);
      } else {
        if (this.checkedId == data.id) {
          this.$refs.treeForm.setCheckedKeys([data.id]);
        }
      }
    }
  }
};
</script>

<style lang="scss" scoped>
  #app{
    .el-aside{
      height:300px;
      width: 100%;
      border:1px solid #eee;
      .el-tree{
        height:100%;
      }
    }
  }
</style>