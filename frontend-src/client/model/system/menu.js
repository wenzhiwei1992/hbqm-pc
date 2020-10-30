import clientModel from '../client-model'

export default {
    getMenuList() {
        return clientModel.ax.get('/sys/menu/getMenuList')
    },
    menuAdd(menu) {
        return clientModel.ax.post('/sys/menu/add', menu)
    },
    menuUpdate(menu) {
        return clientModel.ax.put('/sys/menu/update', menu)
    },
    menuDelete(id) {
        return clientModel.ax.delete('/sys/menu/delete/' + id)
    },
    getMenuListByAppId(appId) {
        return clientModel.ax.get('/sys/menu/getMenuListByAppId/' + appId)
    },
    getMenuListByQuery(menu) {
        return clientModel.ax.post('/sys/menu/getMenuListByQuery', menu)
    },
    getMenuById(id) {
        return clientModel.ax.get('/sys/menu/getMenuById/' + id)
    },
    findMenuByUserLevelId() {
        return clientModel.ax.get('/sys/menu/findMenuByUserLevelId')
    },


}
