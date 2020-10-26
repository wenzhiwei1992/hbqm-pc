<template>
    <el-menu :default-openeds="menu.defaultOpeneds"
             :default-active="menu.defaultActive"
             background-color="#545c64"
             text-color="#fff"
             active-text-color="#ffd04b">
        <el-submenu v-for="submenu in menu.submenus" :index="submenu.index" :key="submenu.index">
            <template slot="title"><i :class="submenu.icon"></i><span class="menu-title">{{submenu.title}}</span></template>
            <el-menu-item v-for="menuItem in submenu.menuItems"
                          :index="menuItem.index"
                          :key="menuItem.index"
                          @click="menuItemClick(menuItem.index)">
                {{menuItem.title}}
            </el-menu-item>
        </el-submenu>
    </el-menu>
</template>
<script>
    import menuTabService from '../service/menuTabService'
    import {
        mapState,
        mapActions,
    } from 'vuex'
    export default{
        data(){
            return {
            }
        },
        computed:{
            menu(){
                return this.$store.state.menu.menu
            },
            openTabs(){
                return this.$store.state.tab.openTabs
            }
        },
        mounted(){
            this.setupRefresh().then(()=>{
                let setup=this.$store.state.setup
                if(setup!=null&&setup.defaultActive){
                    menuTabService.menuItemClick(this.$store,this.$router,setup.defaultActive)
                }else{
                    menuTabService.menuItemClick(this.$store,this.$router,'setupManage')
                }
            })
        },
        methods:{
            menuItemClick(index) {
                menuTabService.menuItemClick(this.$store,this.$router,index)
            },
            ...mapActions(['setupRefresh'])
        }
    }
</script>
<style scoped>
    .el-menu{
        border-right:0;
    }
    .menu-title{
        font-size:1.1em;
    }
</style>