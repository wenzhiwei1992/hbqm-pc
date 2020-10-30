<template>
    <div class="row-button-wrap">
        <div
                v-for="(item,index) of buttonData"
                :key="index"
                class="row-button"
                v-if="authorized(item.authorized) && isDisplay(item.display)"
        >
            <el-tooltip
                    class="item"
                    effect="light"
                    :content="item.name"
                    placement="top"
            >
                <el-button
                        :plain="item.plain"
                        :type="item.type"
                        @click.stop="handleClick(item.event)"
                        :icon="item.icon"
                        :disabled="isDisabled(item.disabled)"
                ></el-button>
            </el-tooltip>
        </div>

    </div>
</template>

<script>
    import menuTabService from '../../../service/menuTabService'

    export default {
        name: 'my-row-button-group',
        props: {
            model: Array,
            row: Object
        },
        mounted() {
            this.receiveButtonData()
        },
        methods: {
            // 获取父组件传过来的值
            receiveButtonData() {
                if (this.model) {
                    this.model.forEach((val, index) => {
                        this.buttonConstant.forEach((item, index) => {
                            if (val.name === item.name) {
                                val.icon = item.icon || ''
                                val.type = item.type || ''
                                this.buttonData.push(val)
                            }
                        })
                    })
                }
            },
            // 按钮点击事件
            handleClick(event) {
                this.seekParent(this.$parent, event)
            },
            // 寻找父组件
            seekParent(parent, event) {
                if (!parent.$refs.mainContent) {
                    this.seekParent(parent.$parent, event)
                } else {
                    if (event) {
                        // 判断event是否有参数
                        if (event.lastIndexOf('(') !== -1) {
                            let parameter
                            let method = event.slice(0, event.lastIndexOf('('))
                            // 判断event参数使用的是''还是""
                            if (event.indexOf("'") !== -1) {
                                parameter = event.slice(event.indexOf("'") + 1, event.lastIndexOf("'"))
                            } else if (event.indexOf('"') !== -1) {
                                parameter = event.slice(event.indexOf('"') + 1, event.lastIndexOf('"'))
                            }
                            parent[method](parameter)
                        } else {
                            parent[event](this.row)
                        }
                    }
                }
            },
            // 按钮权限
            authorized(index) {
                if (index) {
                    return menuTabService.authorized(this.$store, index)
                } else {
                    return true
                }
            },
            // 设置宽度
            // rowButtonGroupWidth(scope,model){
            //   if(model){
            //     let width = model.length * 50
            //     width = width > 80 ? width : 80
            //     scope.store.operationWidth = width  //这里设置operationWidth 给全局方法headerWidth使用
            //   }
            // },
            // 判断按钮是否禁用
            isDisabled(val) {
                if (val && typeof (val) === 'string') {
                    val = val.replace(/scope.row/g, "this.row")
                    return eval(val)
                }
            },
            isDisplay(val) {
                if (val && typeof (val) === 'string') {
                    val = val.replace(/scope.row/g, "this.row")
                    return eval(val)
                }else{
                    return true
                }
            }
        },
        data() {
            let self = this
            return {
                buttonData: [],
                buttonConstant:self.GLOBAL.BUTTON_CONSTANT,
                /*buttonConstant: [
                    {
                        plain: true,
                        icon: 'el-icon-edit',
                        name: '编辑'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-edit-outline',
                        name: '新增/编辑'
                    },
                    {
                        type: 'danger',
                        icon: 'el-icon-delete',
                        name: '删除'
                    },
                    {
                        type: 'danger',
                        icon: 'el-icon-close',
                        name: '撤销'
                    },
                    {
                        type: 'warning',
                        icon: 'el-icon-edit-outline',
                        name: '复制'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-full-screen',
                        name: '二维码'
                    },
                    {
                        icon: 'el-icon-view',
                        name: '显示工序'
                    },
                    {
                        icon: 'el-icon-view',
                        name: '显示工位'
                    },
                    {
                        type: 'success',
                        icon: 'el-icon-s-unfold',
                        name: '入库'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-view',
                        name: '检验'
                    },
                    {
                        type: 'success',
                        icon: 'el-icon-circle-check',
                        name: '发料'
                    },
                    {
                        type: 'danger',
                        icon: 'el-icon-circle-close',
                        name: '驳回'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-office-building',
                        name: '户型楼栋'
                    },
                    {
                        type: 'success',
                        icon: 'el-icon-view',
                        name: '查看构件'
                    },
                    {
                        type: 'success',
                        icon: 'el-icon-view',
                        name: '查看原料'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-circle-check',
                        name: '提交'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-s-operation',
                        name: '产线选择'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-circle-check',
                        name: '排产'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-view',
                        name: '隐检'
                    },
                    {
                        type: 'success',
                        icon: 'el-icon-view',
                        name: '成品检'
                    },
                    {
                        type: 'warning',
                        icon: 'el-icon-refresh-left',
                        name: '返修'
                    },
                    {
                        type: 'danger',
                        icon: 'el-icon-delete-solid',
                        name: '报废'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-s-check',
                        name: '审批'
                    },
                    {
                        type: 'success',
                        icon: 'el-icon-s-fold',
                        name: '成品入库'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-circle-check',
                        name: '确认'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '打印'
                    },
                    {
                        type: 'success',
                        icon: 'el-icon-printer',
                        name: '成品检查记录表打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '隐蔽检验记录表打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '合格证打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-circle-check',
                        name: '创建计划'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-plus',
                        name: '添加用户'
                    },
                    {
                        type: 'success',
                        icon: 'el-icon-user',
                        name: '部门'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-plus',
                        name: '添加角色'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-key',
                        name: '修改密码'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-key',
                        name: '权限'
                    },
                    {
                        type: 'danger',
                        icon: 'el-icon-refresh',
                        name: '报废/返修'
                    },
                    {
                        type: 'success',
                        icon: 'el-icon-view',
                        name: '查看半成品'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-upload',
                        name: '导入图片'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-upload',
                        name: '上传附件'
                    },
                  {
                    type: 'success',
                    icon: 'el-icon-video-pause',
                    name: '恢复'
                  },
                  {
                    type: 'warning',
                    icon: 'el-icon-video-play',
                    name: '暂停'
                  },
                  {
                    type: 'primary',
                    icon: 'el-icon-time',
                    name: '设置时间'
                  },
                ],*/
            }
        }
    }
</script>

<style lang="scss" scoped>
    .row-button-wrap {
        display: flex;

        .row-button {
            margin-left: 10px;

            &:first-child {
                margin-left: 0;
            }
        }
    }
</style>
