<template>
    <div>
        <el-form ref="editForm" :inline="true" :model="editForm" :rules="eidtFormRules" size="small" label-width="80px">
            <el-form-item label="类型" prop="type">
                <el-radio-group v-model="editForm.type" @change="typeChange" size="mini" style="width: 178px">
                    <el-radio-button label="0">目录</el-radio-button>
                    <el-radio-button label="1">菜单</el-radio-button>
                    <el-radio-button label="2">按钮</el-radio-button>
                </el-radio-group>
            </el-form-item>
            <el-form-item v-show="editForm.type.toString() !== '2'" label="标题" prop="title">
                <el-input v-model="editForm.title"
                          style="width: 300px"
                          placeholder="标题"/>
            </el-form-item>
            <el-form-item v-show="editForm.type.toString() === '2'" label="按钮名称" prop="title">
                <el-input v-model="editForm.title" placeholder="按钮名称" style="width: 178px;"/>
            </el-form-item>
            <el-form-item label="图标" prop="icon">
                <el-popover
                        placement="bottom-start"
                        width="450"
                        trigger="click"
                        @show="$refs['iconSelect'].reset()"
                >
                    <IconSelect ref="iconSelect" @selected="selected"/>
                    <el-input slot="reference" v-model="editForm.icon" style="width: 175px;" placeholder="点击选择图标"
                              readonly>
                        <icon-svg v-if="editForm.icon" class="el-input__icon" slot="prefix" :name="editForm.icon" width="1.5em" height="1.5em"/>
                        <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
                    </el-input>
                </el-popover>
            </el-form-item>
            <el-form-item v-show="editForm.type.toString() === '1'" label="按钮权限" prop="authMark">
                <el-radio-group v-model="editForm.authMark" size="mini">
                    <el-radio-button label="true">是</el-radio-button>
                    <el-radio-button label="false">否</el-radio-button>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="唯一标识" prop="index">
                <el-input v-model="editForm.index" placeholder="唯一标识"
                          style="width: 178px;"/>
            </el-form-item>
          <el-form-item label="名称" prop="name">
            <el-input v-model="editForm.name" placeholder="名称"
                      style="width: 178px;"/>
          </el-form-item>
            <el-form-item label="排序" prop="sort">
                <el-input v-model="editForm.sort" style="width: 178px;"></el-input>
            </el-form-item>
            <el-form-item label="路径" prop="path">
                <el-input v-model="editForm.path" placeholder="路径" style="width: 178px;"></el-input>
            </el-form-item>
          <el-form-item label="组件" prop="component">
            <el-input v-model="editForm.component" style="width: 178px;" placeholder="组件"></el-input>
          </el-form-item>
            <!--      <el-form-item label="排序" prop="sort">
                      <el-input-number v-model.number="editForm.sort" :min="0" :max="999" controls-position="right"
                                       style="width: 178px;"/>
                  </el-form-item>-->
            <el-form-item label="pc和手机端标识" prop="flag">
                <el-select v-model="editForm.flag" placeholder="请选择" clearable>
                    <el-option v-for="item in pcAppOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="app标识" prop="appId">
                <el-select v-model="editForm.appId" placeholder="请选择" @change="appChange" clearable>
                    <el-option v-for="item in options"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value"
                    ></el-option>
                </el-select>
            </el-form-item>
            <el-form-item v-show="editForm.type!=0"
                          label="上级类目"
                          prop="parentId">
                <treeselect ref="treeselect"
                            v-model="editForm.parentId"
                            :disabled="!editForm.appId"

                            :options="cloneMenuList"
                            :disable-branch-nodes="editForm.type==='2'?true:false" style="width: 300px;"
                            :max-height="220" openDirection="top" placeholder="选择上级类目"/>
            </el-form-item>
          <el-form-item label="是否为标签页" prop="isTab" label-width="100px">
            <el-switch
                v-model="editForm.isTab"
                active-color="#13ce66"
                inactive-color="#ff4949">
            </el-switch>
          </el-form-item>
          <el-form-item label="路由区分" prop="isRoute">
            <el-switch
                v-model="editForm.isRoute"
                active-color="#13ce66"
                inactive-color="#ff4949">
            </el-switch>
          </el-form-item>
          <el-form-item label="外链菜单" prop="iFrame">
            <el-switch
                v-model="editForm.iFrame"
                active-color="#13ce66"
                inactive-color="#ff4949">
            </el-switch>
          </el-form-item>
          <el-form-item label="是否隐藏(按钮路由)" prop="hidden" label-width="140px">
            <el-switch
                v-model="editForm.hidden"
                active-color="#13ce66"
                inactive-color="#ff4949">
            </el-switch>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button type="text" @click="editFormCancel">取消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editForm')">确认
            </el-button>
        </div>
    </div>
</template>

<script>
    import model from "../../../../model/model";
    import menuModel from '../../../../model/system/menu'
    import {Notification} from "element-ui";
    import IconSelect from '../../../../components/IconSelect/index.vue'
    import Treeselect from '@riophae/vue-treeselect'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'

    export default {
        data() {
            return {
                menuList: [
                    {
                        id: 0,
                        label: '顶级类目',
                        disableBranchNodes: true,
                        showCount: true,
                        children: []
                    }
                ],
                cloneMenuList: [],
                options: [{
                    value: '0',
                    label: '超级管理员'
                },
                    {
                        value: '1',
                        label: '组织管理员'
                    },
                    {
                        value: '2',
                        label: '普通用户'
                    },],
                pcAppOptions: [
                    {
                        value: true,
                        label: 'pc端'
                    },
                    {
                        value: false,
                        label: 'app端'
                    }
                ],
                eidtFormRules: {
                    title: [
                        {required: true, message: '请输入标题信息', trigger: 'blur'},
                    ],
                    icon: [
                        {required: true, message: '请选择图标', trigger: 'change'},
                    ],
                    index: [
                        {required: true, message: '请输入唯一标识', trigger: 'blur'},
                    ],
                    sort: [
                        {required: true, message: '请输入排序', trigger: 'blur'},
                    ],
                    /*path: [
                        {required: true, message: '请输入路由', trigger: 'blur'},
                    ],*/
                    /*appId: [
                        {required: true, message: '请选择app标识', trigger: 'change'},
                    ],*/
                    /*  parentId: [
                          {required: true, message: '请选择上级类目', trigger: 'change'},
                      ],*/
                }

            }
        },
        components: {Treeselect, IconSelect},
        props: ['editForm', 'mode'],
        name: "MenuEdit",
        mounted() {
            this.loadMenuList()
            this.info()
            console.log('this.mode------->', this.mode)
        },
        methods: {
            info() {
                let level = this.$store.state.user.level
                if (level == 0) {
                    this.options = [
                      {
                          value: '0',
                          label: '管理员菜单'
                      },
                      {
                          value: '1',
                          label: '组织用户菜单'
                      },
                    ]
                } else if (level == 1) {
                    this.options = [
                        {
                            value: '1',
                            label: '组织用户菜单'
                        },]
                }
            },
            appChange(appId) {
                //当选择目录菜单的时候，上级类目只加载目录
                /* let type = null;
                 if (this.editForm.type === '0' || this.editForm.type === '1') {
                     type = 0;
                 }
                 let menu = {
                     type: type,
                     appId: val
                 }*/
                if (this.editForm.type == 0) {
                    this.editForm.parentId = 0
                }
                menuModel.getMenuListByAppId(1).then((data) => {
                    this.menuList[0].children = data.entity.content
                    this.cloneMenuList = this.Clone(this.menuList)
                    this.loopEditAttr(this.cloneMenuList[0].children, this.editForm.type)
                    console.log('cloneMenuList------>', type)
                })
            },
            // 类型改变清空数据
            typeChange(val) {
                if (this.mode == 'add') {
                    this.$refs.treeselect.clear()
                    this.editForm.parentId = null
                    this.editForm.icon = ''
                    this.editForm.title = ''
                    this.editForm.sort = ''
                    this.editForm.authorized = ''
                    this.editForm.authMark = 'false'
                    this.editForm.appId = ''
                    this.cloneMenuList = this.Clone(this.menuList)
                }
                //this.loadMenuList()
                /*      if (this.editForm.type === '1') {
                          this.editForm.type = '0'
                      } else if (this.editForm.type === '2') {
                          this.editForm.type = '1'
                      }*/
                /*     menuModel.getMenuListByQuery(this.editForm).then((data) => {
                         this.menuList[0].children = data.entity.content
                         this.cloneMenuList = this.Clone(this.menuList)
                         this.loopEditAttr(this.cloneMenuList[0].children, this.editForm.type)
                     })*/
                //this.loopEditAttr(this.cloneMenuList[0].children, this.editForm.type)
            },
            // 循环添加label和children(treeselect插件只能识别label和children)
            loopEditAttr(arr, type) {
                // console.log(arr)
                arr.map(val => {
                    if (type === '0') {
                        if (val.menuItems) {
                            val.label = val.title
                            val.children = val.menuItems
                            this.loopEditAttr(val.children, type)
                        }
                    } else if (type === '1') {
                        if (val.index.indexOf(':') === -1) {
                            val.label = val.title
                        }
                        if (val.menuItems) {
                            val.children = val.menuItems
                            this.loopEditAttr(val.children, type)
                        }
                    } else if (type === '2') {
                        if (val.index.indexOf(':') === -1) {
                            val.label = val.title
                        }
                        if (val.menuItems) {
                            val.children = val.menuItems
                            this.loopEditAttr(val.children, type)
                        }
                    }

                })
                // log(arr)
            },
            loadMenuList() {
                //目录 可以新建一级，二级...目录 加载所有目录
                //菜单  加载所有目录
                //按钮  加载所有菜单权限按钮为true的菜单
                //默认加载用户菜单
                menuModel.getMenuListByAppId(1).then((data) => {
                    this.menuList[0].children = data.entity.content
                    this.cloneMenuList = this.Clone(this.menuList)
                    this.loopEditAttr(this.cloneMenuList[0].children, this.editForm.type)
                })
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.mode == 'update') {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            console.log('editForm------>', this.editForm)
                            if (this.editForm.appId == '管理员菜单') {
                                this.editForm.appId = 0
                            } else if (this.editForm.appId == '用户菜单') {
                                this.editForm.appId = 1
                            }
                            menuModel.menuUpdate(this.editForm)
                                .then((data) => {
                                    if (data.status == 'failed') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close()
                                        return
                                    }
                                    Notification({
                                        message: '编辑成功',
                                        type: 'success'

                                    })
                                    loading.close()
                                    this.$emit('menu-edit-finish')
                                })
                        } else {
                            console.log('---------->this.editForm', this.mode)
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            menuModel.menuAdd(this.editForm)
                                .then((data) => {
                                    if (data.status == 'failed') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close()
                                        return
                                    }
                                    Notification({
                                        message: '新建成功',
                                        type: 'success'

                                    })
                                    loading.close()
                                    this.$emit('menu-edit-finish')
                                })
                        }

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            editFormCancel() {
                this.$emit('menu-edit-cancel')
            },
            // 选中图标
            selected(name) {
                this.editForm.icon = name
            }
        }
    }
</script>

<style scoped>

</style>
