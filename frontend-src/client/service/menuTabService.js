export default {
    getMenuItem(store, index) {
        let item
        store.state.menu.menu.submenus.forEach((submenu) => {
            submenu.menuItems.forEach((menuItem) => {
                if (menuItem.index == index) {
                    item = menuItem
                }
            })
        })
        return item
    },
    menuItemClick(store, router, index, query) {
        let menuItem = this.getMenuItem(store, index)
        if (menuItem) {
            let openTabs = store.state.tab.openTabs
            if (openTabs.some((t) => t.index == index)) {
                this.setActive(store, router, index, query)
                return;
            }
            store.commit('tab/addOpenTab', {
                title: menuItem.title,
                index: index,
                query: query
            });

            this.setActive(store, router, index, query)
        }

    },
    removeTab(store, router, index) {
        store.commit('tab/removeOpenTab', index)
        let activeIndex = store.state.tab.activeIndex
        if (activeIndex != index) {
            return;
        }
        let lastOpenTab;
        store.state.tab.openTabs.forEach((t) => {
            lastOpenTab = t
        })
        if (lastOpenTab) {
            this.setActive(store, router, lastOpenTab.index, lastOpenTab.query)
        } else {
            this.setActive(store, router, '', '')
        }

    },
    tabClick(store, router, index) {
        let openTabs = store.state.tab.openTabs
        let tab = openTabs.filter((t) => {
            t.index == index
        })
        this.setActive(store, router, index, tab.query)
    },
    setActive(store, router, index, query) {
        store.commit('tab/setActiveIndex', index)
        store.commit('menu/setDefaultActive', index)
        router.push({name: 'tabs', query: query})
    },
    setCurrentMenus(store, menus) {
        store.commit('menu/setSubmenus', menus)
        store.commit('menu/setMenuAuthorized', true)
    },
    setCurrentSystem(store, system) {
        store.commit('menu/setCurrentSystem', system)
        store.commit('menu/setMenuAuthorized', true)
    },
    authMenu(store) {
        var self = this
        var level1Menus = store.state.menu.menu.submenus
        log('level1Menus---------------------->', level1Menus)
        level1Menus.forEach(level1Menu => {
            level1Menu.authorized = false
            level1Menu.menuItems.forEach(level2Menu => {
                if (self.authorized(store, level2Menu.index)) {
                    level2Menu.authorized = true
                    level1Menu.authorized = true
                } else {
                    level2Menu.authorized = false
                }
                if (level2Menu.menuItems) {
                    level2Menu.menuItems.forEach(level3Menu => {
                        if (self.authorized(store, level3Menu.index)) {
                            level3Menu.authorized = true
                            level2Menu.authorized = true
                            level1Menu.authorized = true
                        } else {
                            level3Menu.authorized = false
                        }
                        if (level3Menu.menuItems) {
                            level3Menu.menuItems.forEach(level4Menu => {
                                if (self.authorized(store, level4Menu.index)) {
                                    level4Menu.authorized = true
                                    level3Menu.authorized = true
                                    level2Menu.authorized = true
                                    level1Menu.authorized = true
                                } else {
                                    level4Menu.authorized = false
                                }
                            })
                        }
                    })
                }
            })
        })
        store.commit('menu/setSubmenus', level1Menus)
        store.commit('menu/setMenuAuthorized', true)
        console.log('level1Menus------>', level1Menus)

    },
    checked(data, newArr) {
        data.forEach(item => {
            if (item.menuItems != undefined) {
                if (item.menuItems.length != 0) {
                    newArr.push(item.index)
                    this.checked(item.menuItems, newArr)
                }
            } else if (item.menuItems == undefined) {
                newArr.push(item.index)
            }
        })
    },
    authorized(store, index) {
        if (store.state.user.username == 'admin' && (index == 'orgManage' || index == 'userManage' || index == 'groupManage' || index == 'setupManage')) {
            return true
        }
        var newArr = []
        var menus = store.state.menu.menu.submenus
        this.checked(menus, newArr)
        var authorized = false
        newArr.forEach(g=>{
            var authorities = g
            if (authorities) {
                var arr = authorities.split(";");
                if (arr.some(d => d == index)) {
                    authorized = true
                }
            }
        })


       /* var groups = store.state.user.groups
        if (groups == null || groups.length == 0) {
            return false
        }
        var authorized = false
        groups.forEach(g => {
            //
            var authorities = g.authorities
            if (authorities) {
                var arr = authorities.split(";");
                if (arr.some(d => d == index)) {
                    authorized = true
                }
            }
        })*/
        return authorized
    }
}
