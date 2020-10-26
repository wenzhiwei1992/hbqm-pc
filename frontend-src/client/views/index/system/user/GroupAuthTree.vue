<template>
  <div id="group-auth-tree" style="position:relative;">
    <div class="auth-title">
      <el-form :inline="true" id="opt">
        <el-form-item>
          <el-button style="padding:11px;" size="mini" @click="save()" :disabled="group==null||!changed">保存
          </el-button>
        </el-form-item>
      </el-form>
      <span v-if="group">{{ group.groupName }}</span>角色权限
    </div>
    <el-tree
        v-show="group!=null"
        :data="allMenus"
        ref="tree"
        :props="defaultProps"
        show-checkbox
        node-key="index"
        default-expand-all
    >
    </el-tree>
  </div>


</template>
<script>
import groupMenuModel from '../../../../model/system/groupMenu'
import model from '../../../../model/model'
import menuModel from '../../../../model/system/menu'
import {Notification} from 'element-ui'

export default {
  props: ['group', 'currentMenuList'],

  data() {
    return {
      defaultProps: {
        children: 'menuItems',
        label: 'title',
      },
      authList: [],
      allMenus: [],
    }
  },
  computed: {
    data() {
      return this.$store.state.menu.menu.submenus
    },
    changed() {
      return this.$refs.tree.getCheckedKeys().join(";") != this.group.authorities
    }
  },
  mounted() {

    this.info()
  },
  methods: {
    info() {

      menuModel.findMenuByUserLevelId().then((data) => {
        this.allMenus = data.entity.content
      })

    },
    save() {
      let authorities = this.$refs.tree.getCheckedKeys();

      //子节点未全选中时父节点半选中，获取不到父节点id
      let hafCheckedKeys = this.$refs.tree.getHalfCheckedKeys();
      this.group.authorities = authorities.concat(hafCheckedKeys).join(";")
      this.$store.commit('startLoading')

      groupMenuModel.updateGroupPermission(this.group).then((data) => {
        if (data.status == 'failed') {
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
        window.location.reload()
      })

      /* model.update('group', this.group)
           .then((data) => {
               this.$store.commit('endLoading')
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
               window.location.reload()
           })*/
    },
    checked(index, data, newArr) {
      data.forEach(item => {
        if (item.index == index) {
          if (item.menuItems == undefined) {
            newArr.push(item.index)
          }
        } else {
          if (item.menuItems != undefined) {
            if (item.menuItems.length != 0) {
              this.checked(index, item.menuItems, newArr)
            }
          }
        }
      });
    },

  },

  watch: {
    group: function (n, o) {
      if (n) {
        let authorities = this.currentMenuList
        //let authorities = this.group.authorities
        if (authorities) {
          let newArr = []
          authorities.forEach(item => {
            this.checked(item, this.allMenus, newArr)
          })
          this.authList = newArr
          //this.authList = authorities.split(";")
          this.$refs.tree.setCheckedKeys(this.authList);
        } else {
          this.$refs.tree.setCheckedKeys([]);
        }
      } else {
        this.$refs.tree.setCheckedKeys([]);
      }
    }
  }
}
</script>
<style lang="scss" scoped>
#group-auth-tree {

}

#group-auth-tree #opt .el-form-item {
  margin-bottom: 5px;
}

#group-auth-tree .auth-title {
  position: sticky;
  top: 0;
  left: 0;
  z-index: 998;
  height: 36px;
  line-height: 36px;
  text-align: center;
  font-size: 14px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
  border-top: 1px solid #ebeef5;
}

#opt {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 999;
}
</style>
