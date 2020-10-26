import clientModel from '../client-model'



export default {
    list(pageInfo) {
        return clientModel.ax.post('/basicdata/backlog/list',pageInfo)
    },
    getById(id) {
        return clientModel.ax.get('/basicdata/backlog/getById/'+id)
    },
    delete(id) {
        return clientModel.ax.delete('/basicdata/backlog/delete/'+id)
    },
    deleteByIds(ids) {
        return clientModel.ax.delete('/basicdata/backlogs?ids='+ids)
    },
    update(backlog) {
        return clientModel.ax.post('/basicdata/backlog/update',backlog)
    },
    add(backlog) {
        return clientModel.ax.post('/basicdata/backlog/add',backlog)
    },
    getAllProdLines(){
        return clientModel.ax.get('/basicdata/prodLine/list')
    },
    getStationsByBackLogId(id){
        return clientModel.ax.get('/basicdata/station/getStationsByBackLogId/'+id)
    },
    getOtherStations(){
        return clientModel.ax.get("/basicdata/station/getOtherStations")
    },
    addStations(backLogDTO){
        return clientModel.ax.post('/basicdata/station/addStationsForBacklogId',backLogDTO)
    },
    deleteStationsByBackLogId(backLogDTO){
        return clientModel.ax.post('/basicdata/station/deleteStationsByBackLogId',backLogDTO)
    }

}
