import clientModel from '../client-model'



export default {
    getBacklogsByProdId(id){
        return clientModel.ax.get('/po/comb/getAsStatuses/'+id) //该controller 已实现，故在此直接使用
    },
    list(pageInfo) {
        return clientModel.ax.post('/basicdata/station/findAll',pageInfo)
    },
    getById(id) {
        return clientModel.ax.get('/basicdata/station/id/'+id)
    },
    delete(id) {
        return clientModel.ax.delete('/basicdata/station/delete/'+id)
    },
    deleteByIds(ids) {
        return clientModel.ax.delete('/basicdata/stations?ids='+ids)
    },
    update(station) {
        return clientModel.ax.put('/basicdata/station/update',station)
    },
    add(station) {
        return clientModel.ax.post('/basicdata/station/add',station)
    },
    getAllProdLines(){
        return clientModel.ax.get('/basicdata/prodLine/list')
    },
    //获取所有工序
    getBackLogs(){
        return clientModel.ax.get('/basicdata/backLogs/listAll')
    }
}
