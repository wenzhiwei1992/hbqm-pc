import clientModel from '../client-model'



export default {
    list(pageInfo) {
        return clientModel.ax.post('/basicdata/path/list',pageInfo)
    },
    getById(id) {
        return clientModel.ax.get('/basicdata/path/getById/'+id)
    },
    delete(id) {
        return clientModel.ax.delete('/basicdata/path/delete/'+id)
    },
    deleteByIds(ids) {
        return clientModel.ax.delete('/basicdata/paths?ids='+ids)
    },
    update(path) {
        return clientModel.ax.post('/basicdata/path/update',path)
    },
    add(path) {
        return clientModel.ax.post('/basicdata/path/add',path)
    },
    getAllProdLines(){
        return clientModel.ax.get('/basicdata/prodLine/list')
    },
    getBackLogByPathId(id){
        return clientModel.ax.get('/basicdata/path/getBacklogs/'+id)
    },
    deleteBacklogFromPathByBackLogIds(pathDTO){
        return clientModel.ax.post('/basicdata/path/deleteBacklogs',pathDTO);
    },
    getOtherBackLogByPathId(id){
        return clientModel.ax.get('/basicdata/path/getOtherBacklogs/'+id)
    },
    addBacklogFromPathByBackLogIds(pathDTO){
        return clientModel.ax.post('/basicdata/path/addBacklogs',pathDTO);
    }
}
