import clientModel from '../client-model'

export default {
    houseTypeList(pageInfo) {
        return clientModel.ax.post('/houseType/list', pageInfo)
    },
    getAllHouseTypes() {
        return clientModel.ax.get('/houseType/list')
    },
    houseTypeLoad(id) {
        return clientModel.ax.get('/houseType/' + id)
    },
    houseTypeDelete(id) {
        return clientModel.ax.delete('/houseType/' + id)
    },
    houseTypeDeletes(ids) {
        return clientModel.ax.delete('/houseType/houseTypes?ids=' + ids)
    },
    houseTypeUpdate(houseType) {
        return clientModel.ax.post('/houseType/update', houseType)
    },
    houseTypeAdd(houseType) {
        return clientModel.ax.post('/houseType/add', houseType)
    },
    getHouseTypeByProjectId(projectId) {
        return clientModel.ax.get('/houseType/getHouseTypeByProjectId/' + projectId)
    },
    getHouseTypeByProjectIdOpt(projectId) {
        return clientModel.ax.get('/houseType/getHouseTypeByProjectIdOpt/' + projectId)
    },
    findHouseTypesToTree(projectId) {
        return clientModel.ax.get('/houseType/findHouseTypesToTree/' + projectId)
    },


}