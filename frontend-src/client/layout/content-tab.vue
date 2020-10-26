<template>
    <el-tabs :value="activeIndex" type="card" closable @tab-remove="removeTab" @tab-click="changeTabClick">
        <el-tab-pane
                v-for="item in openTabs"
                :key="item.index"
                :label="item.title"
                :name="item.index"
        >
            <el-dropdown class="site-tabs__tools" :show-timeout="0">
                <i class="el-icon-arrow-down el-icon--right"></i>
                <el-dropdown-menu slot="dropdown">
                    <!--<el-dropdown-item @click.native="tabsCloseCurrentHandle">关闭当前标签页</el-dropdown-item>-->
                    <!--<el-dropdown-item @click.native="tabsCloseOtherHandle">关闭其它标签页</el-dropdown-item>-->
                    <!--<el-dropdown-item @click.native="tabsCloseAllHandle">关闭全部标签页</el-dropdown-item>-->
                    <el-dropdown-item @click.native="tabsRefreshCurrentHandle">刷新当前标签页</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>

            <router-view :name="item.index"></router-view>
            <!--<component v-bind:is="item.currentView"></component>-->
        </el-tab-pane>
    </el-tabs>
</template>
<script>
    import menuTabService from '../service/menuTabService'
    export default{
        data(){
            return {
            }
        },
        computed:{
            menu(){
                return this.$store.state.menu.menu
            },
            activeIndex: {
                get () {
                    return this.$store.state.tab.activeIndex
                },
                set (value) {
                    this.$store.commit('tab/setActiveIndex', value)
                }
            },
            openTabs(){
                return this.$store.state.tab.openTabs
            }
        },
        methods: {
            removeTab(index) {
                menuTabService.removeTab(this.$store,this.$router,index)
            },
            changeTabClick(tab){
                menuTabService.tabClick(this.$store,this.$router,tab.name)
            },
            tabsRefreshCurrentHandle(){

            }
        }
    }
</script>