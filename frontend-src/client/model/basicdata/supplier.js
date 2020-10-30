import clientModel from "../client-model";


export default {
    listByOrgId() {
        return clientModel.ax.get('basicdata/supplier/findAll')
    },
    list(query) {
        return clientModel.ax.post('/basicdata/supplier/list', query)
    },
    findById(id) {
        return clientModel.ax.get('/basicdata/supplier/id/' + id)
    },
    deleteById(id) {
        return clientModel.ax.delete('/basicdata/supplier/delete/id/'+id)
    },
    deleteByIds(ids) {
        return clientModel.ax.delete('/basicdata/supplier/deleteByIds?ids=' + ids)
    },
    update(obj) {
        return clientModel.ax.post('/basicdata/supplier/update', obj)
    },
    add(obj) {
        return clientModel.ax.post('/basicdata/supplier/add',obj)
    },
}
