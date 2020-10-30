import clientModel from "../client-model";


export default {
    findSfgPickingByQuery(sfgPickingQuery) {
        return clientModel.ax.post('/sfgPicking/findSfgPickingByQuery', sfgPickingQuery)
    },
    addSfgPicking(sfgPickingDto) {
        return clientModel.ax.post('/sfgPicking/addSfgPicking', sfgPickingDto)
    },
    updateSfgPicking(sfgPickingDto) {
        return clientModel.ax.post('/sfgPicking/updateSfgPicking', sfgPickingDto)
    },
    deleteSfgPickingWithIds(ids) {
        return clientModel.ax.delete('/sfgPicking/deleteSfgPickingWithIds?ids=' + ids)
    },
    findSfgPickingAndDetailBySfgPickingId(id) {
        return clientModel.ax.get('/sfgPicking/findSfgPickingAndDetailBySfgPickingId/' + id)
    },
    updateSfgToSend(id) {
        return clientModel.ax.get('/sfgPicking/updateSfgToSend/' + id)
    },


}
