<template>
    <aside class="site-sidebar" :class="'site-sidebar--' + sidebarLayoutSkin">
        <div class="site-sidebar__inner">
            <el-menu
                    :default-active="menuActiveName || 'home'"
                    :collapse="sidebarFold"
                    :collapseTransition="false"
                    :unique-opened="true"
                    class="site-sidebar__menu">
                <el-submenu v-for="submenu in menu.submenus"  :index="submenu.index" :key="submenu.index">
                    <template slot="title"><i :class="submenu.icon"></i><span
                            class="menu-title">{{submenu.title}}</span></template>
                    <template v-for="menuItem in submenu.menuItems">
                        <el-menu-item v-if="(!menuItem.hasOwnProperty('menuItems')||menuItem.hasOwnProperty('authMark'))"
                                      :index="menuItem.index"
                                      :key="menuItem.index"
                                      @click="pageJump(menuItem)">
                            {{menuItem.title}}
                        </el-menu-item>
                        <el-submenu v-if="menuItem.hasOwnProperty('menuItems')&&!menuItem.hasOwnProperty('authMark')" :index="menuItem.index" :key="menuItem.index">
                            <template slot="title"><i :class="menuItem.icon"></i><span
                                    class="menu-title">{{menuItem.title}}</span></template>
                            <el-menu-item v-for="menuItem in menuItem.menuItems"
                                          :index="menuItem.index"
                                          :key="menuItem.index"
                                          @click="pageJump(menuItem)">
                                {{menuItem.title}}
                            </el-menu-item>
                        </el-submenu>
                    </template>
                </el-submenu>
             <!--   老版本20年03月18日  稳定可删-->
                <!-- <el-submenu v-for="submenu in menu.submenus" v-if="submenu.authorized"  :index="submenu.index" :key="submenu.index">
                     <template slot="title"><i :class="submenu.icon"></i><span
                             class="menu-title">{{submenu.title}}</span></template>
                     <template v-for="menuItem in submenu.menuItems">
                       <el-menu-item v-if="menuItem.authorized&&(!menuItem.hasOwnProperty('menuItems')||menuItem.hasOwnProperty('authMark'))"
                                   :index="menuItem.index"
                                   :key="menuItem.index"
                                   @click="$router.push({ name: menuItem.index })">
                         {{menuItem.title}}
                       </el-menu-item>
                       <el-submenu v-if="menuItem.authorized&&menuItem.hasOwnProperty('menuItems')&&!menuItem.hasOwnProperty('authMark')" :index="menuItem.index" :key="menuItem.index">
                           <template slot="title"><i :class="menuItem.icon"></i><span
                                   class="menu-title">{{menuItem.title}}</span></template>
                           <el-menu-item v-for="menuItem in menuItem.menuItems" v-if="menuItem.authorized"
                                         :index="menuItem.index"
                                         :key="menuItem.index"
                                         @click="$router.push({ name: menuItem.index })">
                               {{menuItem.title}}
                           </el-menu-item>
                       </el-submenu>
                     </template>
                     &lt;!&ndash; 老版本11月12日  稳定可删 &ndash;&gt;
                     &lt;!&ndash; <el-menu-item v-for="menuItem in submenu.menuItems" v-if="menuItem.authorized&&(!menuItem.hasOwnProperty('menuItems')||menuItem.hasOwnProperty('authMark'))"
                                   :index="menuItem.index"
                                   :key="menuItem.index"
                                   @click="$router.push({ name: menuItem.index })">
                         {{menuItem.title}}
                     </el-menu-item>
                     <el-submenu v-for="menuItem in submenu.menuItems" v-if="menuItem.authorized&&menuItem.hasOwnProperty('menuItems')&&!menuItem.hasOwnProperty('authMark')" :index="menuItem.index" :key="menuItem.index">
                         <template slot="title"><i :class="menuItem.icon"></i><span
                                 class="menu-title">{{menuItem.title}}</span></template>
                         <el-menu-item v-for="menuItem in menuItem.menuItems" v-if="menuItem.authorized"
                                       :index="menuItem.index"
                                       :key="menuItem.index"
                                       @click="$router.push({ name: menuItem.index })">
                             {{menuItem.title}}
                         </el-menu-item>
                     </el-submenu> &ndash;&gt;
                 </el-submenu>-->

            </el-menu>
        </div>
    </aside>
</template>

<script>
    import {isURL} from '@/util/validate'
    import {
        mapState,
        mapActions,
    } from 'vuex'

    export default {
        data() {
            return {}
        },
        components: {},
        computed: {
            sidebarLayoutSkin: {
                get() {
                    return this.$store.state.sidebarLayoutSkin
                }
            },
            sidebarFold: {
                get() {
                    return this.$store.state.sidebarFold
                }
            },
            menuList: {
                get() {
                    return this.$store.state.menuList
                },
                set(val) {
                    this.$store.commit('updateMenuList', val)
                }
            },
            menuActiveName: {
                get() {
                    return this.$store.state.menuActiveName
                },
                set(val) {
                    this.$store.commit('updateMenuActiveName', val)
                }
            },
            mainTabs: {
                get() {
                    return this.$store.state.mainTabs
                },
                set(val) {
                    this.$store.commit('updateMainTabs', val)
                }
            },
            mainTabsActiveName: {
                get() {
                    return this.$store.state.mainTabsActiveName
                },
                set(val) {
                    this.$store.commit('updateMainTabsActiveName', val)
                }
            },
            menu() {
                return this.$store.state.menu.menu
            },
            user(){
                return this.$store.state.user
            }
        },
        watch: {
            $route: 'routeHandle'
        },
        created() {
            this.menuList = JSON.parse(sessionStorage.getItem('menuList') || '[]')
            this.routeHandle(this.$route)
        },
        mounted(){
            if(this.$route.name=='home'){
                this.setupRefresh().then(()=>{
                    let setup=this.$store.state.setup
                    if(setup!=null&&setup.defaultActive){
                        this.$router.push({ name: setup.defaultActive })
                    }
                })
            }
        },
        methods: {
            ...mapActions(['setupRefresh']),
            // 路由操作
            routeHandle(route) {
                if (route.meta.isTab) {
                    // tab选中, 不存在先添加
                    var tab = this.mainTabs.filter(item => item.name === route.name)[0]
                    if (!tab) {
                        tab = {
                            menuId: route.meta.menuId || route.name,
                            name: route.name,
                            title: route.meta.title,
                            type: isURL(route.meta.iframeUrl) ? 'iframe' : 'module',
                            iframeUrl: route.meta.iframeUrl || ''
                        }
                        this.mainTabs = this.mainTabs.concat(tab)
                    }
                    this.menuActiveName = tab.menuId + ''
                    this.mainTabsActiveName = tab.name
                }
            },
            pageJump(menuItem){
              // menuItem.iframe:true表示外链 false表示内部路由
              if(menuItem.iframe){
                window.open(menuItem.path)
              }else{
                this.$router.push({ path: menuItem.path })
              }
            },
        }
    }
</script>
