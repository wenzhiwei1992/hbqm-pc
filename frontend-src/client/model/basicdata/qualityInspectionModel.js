import clientModel from '../client-model'


export default {
    all() {
        return clientModel.ax.post('/basicdata/qualityInspection/all')
    },
    allByOrgId() {
        return clientModel.ax.post('/basicdata/qualityInspection/allByOrgId')
    },
    list(option,pageInfo) {
        return clientModel.ax.post('/basicdata/qualityInspection/findAll?option=' + option,pageInfo)
    },
    getById(id) {
        return clientModel.ax.get('/basicdata/qualityInspection/id/'+id)
    },
    delete(id) {
        return clientModel.ax.delete('/basicdata/qualityInspection/delete/'+id)
    },
    deleteByIds(ids) {
        return clientModel.ax.delete('/basicdata/qualityInspection/deletes?ids='+ids)
    },
    update(qualityInspection) {
        return clientModel.ax.put('/basicdata/qualityInspection/update',qualityInspection)
    },
    add(qualityInspection) {
        return clientModel.ax.post('/basicdata/qualityInspection/add',qualityInspection)
    },
}
