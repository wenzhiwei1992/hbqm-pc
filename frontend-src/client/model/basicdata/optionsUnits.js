import clientModel from "../client-model";


export default {
    list(query) {
        return clientModel.ax.post('/basicdata/units/list', query)
    },
    findById(id) {
        return clientModel.ax.get('/basicdata/units/id/' + id)
    },
    deleteById(id) {
        return clientModel.ax.delete('/basicdata/units/delete/id/'+id)
    },
    deleteByIds(ids) {
        return clientModel.ax.delete('/basicdata/units/deleteByIds?ids=' + ids)
    },
    update(obj) {
        return clientModel.ax.post('/basicdata/units/update', obj)
    },
    add(obj) {
        return clientModel.ax.post('/basicdata/units/add',obj)
    },
}
