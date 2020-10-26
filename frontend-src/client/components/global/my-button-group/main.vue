<template>
    <div>
        <ul class="button-group">
            <li
                    class="button-wrap"
                    v-for="(item, index) of buttonData"
                    v-if="authorized(item.authorized)&& !item.display"
                    :key="index"
            >
                <el-button
                        v-if="item.name!=='导入'&&item.name!=='Excel导入'&&item.name!=='半成品导入'"
                        :style="buttonData.length>1?'':'border-radius: 3px;'"
                        :disabled="isDisabled(item.disabled)"
                        :type="item.type"
                        :icon="item.icon"
                        @click="handleClick(item.event)"
                >{{item.name}}
                </el-button>
                <el-upload
                        v-else
                        :action="item.action"
                        :multiple="item.multiple"
                        :on-error="item.methods.onError"
                        :on-success="item.methods.onSuccess"
                        :before-upload="item.methods.beforeUpload"
                >
                    <el-button
                            :type="item.type"
                            :icon="item.icon"
                    >{{item.name}}
                    </el-button>
                </el-upload>
                <!-- <el-upload
                  v-else-if="item.name==='Excel导入'"
                  :action="item.action"
                  :multiple="item.multiple"
                  :on-error="item.methods.onError"
                  :on-success="item.methods.onSuccess"
                  :before-upload="item.methods.beforeUpload"
                >
                  <el-button
                    :type="item.type"
                    :icon="item.icon"
                  >{{item.name}}</el-button>
                </el-upload> -->
            </li>
        </ul>
    </div>
</template>

<script>
    import menuTabService from '../../../service/menuTabService'

    export default {
        name: 'my-button-group',
        props: {
            model: Array,
            disabled: Boolean
        },
        watch: {
            disabled(newVal, oldVal) {
                return newVal
            }
        },
        mounted() {
            this.receiveButtonData()
        },
        activated(){
            this.buttonData = []
            this.receiveButtonData()
        },
        updated() {
            this.seekParent(this.$parent)
        },
        computed: {
            headerOperationBarHeight: {
                get() {
                    return this.$store.state.headerOperationBarHeight
                },
                set(val) {
                    this.$store.commit('updateHeaderOperationBarHeight', val)
                }
            },
            mainContentHeight: {
                get() {
                    return this.$store.state.mainContentHeight
                },
                set(val) {
                    this.$store.commit('updateMainContentHeight', val)
                }
            },
            footerOperationBarHeight: {
                get() {
                    return this.$store.state.footerOperationBarHeight
                },
                set(val) {
                    this.$store.commit('updateFooterOperationBarHeight', val)
                }
            },
            documentClientHeight: {
                get() {
                    return this.$store.state.documentClientHeight
                },
                set(val) {
                    this.$store.commit('updateDocumentClientHeight', val)
                }
            },

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
                            parent[event]()
                        }
                    } else {
                        this.updateOperationBarHeight(parent)
                    }
                }
            },


            // 获取操作栏的高度
            updateOperationBarHeight(parent) {
                this.$nextTick(() => {
                    this.updateHeight(parent)
                })
                window.onresize = () => {
                    if (!parent.$refs.headOperationBar) {
                        window.onresize = null
                        return
                    }
                    this.$nextTick(() => {
                        this.updateHeight(parent)
                    })
                }
            },
            // 获取高度方法
            updateHeight(parent) {
                this.headerOperationBarHeight = parent.$refs.headOperationBar ? parent.$refs.headOperationBar.$el.clientHeight : 0;
                this.footerOperationBarHeight = parent.$refs.footOperationBar ? parent.$refs.footOperationBar.$el.clientHeight : 0;
                this.documentClientHeight = document.documentElement['clientHeight'];
                this.siteContentViewHeight()
            },
            // 计算出中间内容区域的最终高度
            siteContentViewHeight() {
                let height = this.documentClientHeight
                let mainPadding = 40
                let navBarHeight = 51
                let border = 1
                let topTab = 40
                // console.log(this.headerOperationBarHeight,this.footerOperationBarHeight)
                this.mainContentHeight = height
                    - mainPadding
                    - navBarHeight
                    - topTab
                    - border
                    - this.headerOperationBarHeight
                    - this.footerOperationBarHeight
            },
            // 按钮权限
            authorized(index) {
                if (index) {
                    return menuTabService.authorized(this.$store, index)
                } else {
                    return true
                }
            },
            // 判断按钮是否禁用
            isDisabled(val) {
                if (val) {
                    return !this.disabled
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
                        type: 'primary',
                        icon: 'el-icon-search',
                        name: '查询'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-plus',
                        name: '新建'
                    },
                    {
                        type: '',
                        icon: 'el-icon-edit',
                        name: '编辑'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '发货单打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '合格证打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '发货单针式打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '三杰针式打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '合肥发货单打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '叶集发货单打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '宏泰发货单打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '发货单打印A'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '杭州发货单打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '襄阳发货单打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-upload',
                        name: '导入'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-upload',
                        name: 'Excel导入'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-download',
                        name: '导出'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-download',
                        name: '下载发货单'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-download',
                        name: '打印发货单'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-download',
                        name: '下载'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-download',
                        name: '半成品下载'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-download',
                        name: '下载二维码'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-upload2',
                        name: '上传'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-circle-check',
                        name: '提交'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-circle-check',
                        name: '保存'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-circle-check',
                        name: '确认'
                    },
                    {
                        type: 'danger',
                        icon: 'el-icon-delete',
                        name: '删除'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-plus',
                        name: '添加'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-plus',
                        name: '按构件类型新建'
                    },
                    {
                        type: 'success',
                        icon: 'el-icon-plus',
                        name: '按楼栋楼层新建'
                    },
                    {
                        type: 'warning',
                        icon: 'el-icon-document-copy',
                        name: '移单'
                    },
                    {
                        type: 'warning',
                        icon: 'el-icon-connection',
                        name: '同步'
                    },
                    {
                        type: 'warning',
                        icon: 'el-icon-connection',
                        name: '同步企业微信用户'
                    },
                    {
                        type: 'warning',
                        icon: 'el-icon-connection',
                        name: '同步企业钉钉用户'
                    },
                    {
                        type: 'success',
                        icon: 'el-icon-tickets',
                        name: '排产'
                    },
                    {
                        type: 'success',
                        icon: 'el-icon-tickets',
                        name: '批量排产'
                    },
                    {
                        type: 'success',
                        icon: 'el-icon-s-fold',
                        name: '入库'
                    },
                    {
                        type: 'success',
                        icon: 'el-icon-truck',
                        name: '发货'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-upload',
                        name: '选择导入'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '三杰批量打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '批量打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '批量打印A'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '批量打印B'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '批量打印C'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '批量打印D'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '隐检打印'
                    },
                    {
                        type: 'success',
                        icon: 'el-icon-printer',
                        name: '成品检打印'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-printer',
                        name: '返修工单'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-document-copy',
                        name: '复制'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-setting',
                        name: '编辑构件'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-guide',
                        name: '切换版本'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-finished',
                        name: '一键产出'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-upload',
                        name: '图纸导入'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-upload',
                        name: '上传附件'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-takeaway-box',
                        name: '批量操作'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-picture-outline',
                        name: '查看图片'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-upload',
                        name: '半成品导入'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-document-copy',
                        name: '复制楼栋'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-document-copy',
                        name: '复制楼层'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-plus',
                        name: '新建发货单'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-plus',
                        name: '退库'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-plus',
                        name: '新增半成品'
                    },
                    {
                        type: 'primary',
                        icon: 'el-icon-takeaway-box',
                        name: '保存修改'
                    },
                ]*/
            }
        }
    }
</script>

<style lang="scss" scoped>
    ul, li {
        padding: 0;
        margin: 0;
        list-style-type: none;
    }

    .button-group {
        display: inline-block;
        vertical-align: middle;

        .button-wrap {
            display: inline-block;
            box-sizing: border-box;

            .el-button {
                border-radius: 0;
            }

            &:not(:first-child) {
                .el-button {
                    border-left-color: rgba(255, 255, 255, 0.5);
                }
            }

            &:not(:last-child) {
                .el-button {
                    border-radius: rgba(255, 255, 255, 0.5);
                }
            }

            &:first-child {
                .el-button {
                    border-radius: 3px 0 0 3px;
                }
            }

            &:last-child {
                .el-button {
                    border-radius: 0 3px 3px 0;
                }
            }
        }
    }
</style>
