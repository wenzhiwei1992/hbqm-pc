import clientModel from '../client-model'


export default {
    loadByPram(obj){
        return clientModel.ax.post('/basicdata/trayPreserve/listByParam',obj)
    },
    add(tray) {
        return clientModel.ax.post('/basicdata/tray/add',tray)
    },
    update(tray) {
        return clientModel.ax.post('/basicdata/tray/update',tray)
    },
    deleteById(id) {
        return clientModel.ax.delete('/basicdata/tray/delete/'+id)
    },


    getById(id) {
        return clientModel.ax.get('/basicdata/tray/getById/'+id)
    },

    deleteByIds(ids) {
        return clientModel.ax.delete('/basicdata/tray/deleteByIds?ids='+ids)
    },

    findAll(query){
        return clientModel.ax.get('/basicdata/tray/findAllTrayNo?query='+query)
    },

    findAllByOrgId() {
        return clientModel.ax.get('/tray/findAllByOrgId')
    },
    getTraysById(id) {
        return clientModel.ax.get('/basicdata/tray/getTraysById/' + id)
    },


}