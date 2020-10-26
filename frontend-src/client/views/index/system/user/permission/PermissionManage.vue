<template>
  <div>
      <div style="display: inline-block;margin-bottom:10px">
        <el-button
          class="filter-item"
          size="mini"
          type="primary"
          icon="el-icon-plus"
          @click="add">新增</el-button>
      </div>
      <el-table
        :data="datas"
        :loading="loading"
        style="width: 100%"
        row-key="id"
        border
        :tree-props="{children: 'child', hasChildren: 'hasChildren'}">
        <el-table-column
            prop="alias"
            label="名称">
        </el-table-column>
        <el-table-column
            prop="name"
            label="API">
        </el-table-column>
        <el-table-column label="操作" width="130px" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" icon="el-icon-edit" @click="edit(scope.row)"/>
          <el-popover
            :ref="scope.row.id"
            placement="top"
            width="200">
            <p>确定删除吗,如果存在下级节点则一并删除，此操作不能撤销！</p>
            <div style="text-align: right; margin: 0">
              <el-button size="mini" type="text" @click="$refs[scope.row.id].doClose()">取消</el-button>
              <el-button :loading="delLoading" type="primary" size="mini" @click="subDelete(scope.row.id)">确定</el-button>
            </div>
            <el-button slot="reference" type="danger" icon="el-icon-delete" size="mini"/>
          </el-popover>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :title="isAdd?'新增':'编辑'" :visible.sync="editVisiable" v-if="editVisiable" width="40%">
        <permission-edit :permissionObj="permissionObj" @close-pm-edit="closePmEdit()" />
    </el-dialog>
  </div>
</template>

<script>
import permission from '../../../../../model/system/permission'
import PermissionEdit from './PermissionEdit.vue'
import {Notification} from 'element-ui'
export default {
    components:{
        'permission-edit':PermissionEdit
    },
    data() {
        return {
            loading:false,
            delLoading: false,
            datas: [],
            editVisiable:false,
            isAdd:true,
            permissionObj:{}
        }
    },
    mounted(){
        this.list();
    },
    methods: {
        list(){
            this.loading = true;
            permission.loadTree().then(data=>{
                this.datas = data.entity;
                this.loading = false
            })
        },
        add(){
            this.permissionObj = {}
            this.editVisiable=true
        },
        edit(row){
            permission.findByPmId(row.id).then(data=>{
                this.permissionObj = data.entity;
                this.isAdd = false
                this.editVisiable = true;
            })
        },
        subDelete(id){
            //删除该条记录,如果有子信息一并删除
            permission.delete(id).then(data=>{
                Notification({
                    type:'success',
                    message:'删除成功'
                })
                this.list();
            })
        },
        closePmEdit(){
            this.editVisiable = false;
            this.list();
        }
    },
}
</script>

<style scoped>

</style>