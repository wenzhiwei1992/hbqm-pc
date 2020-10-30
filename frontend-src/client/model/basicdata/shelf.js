import clientModel from '../client-model'


export default {
    pageableList(pageInfo) {
        return clientModel.ax.post('/basicdata/shelf/list',pageInfo)
    },
    getById(id) {
        return clientModel.ax.get('/basicdata/shelf/getById/'+id)
    },
    deleteById(id) {
        return clientModel.ax.delete('/basicdata/shelf/delete/'+id)
    },
    deleteByIds(ids) {
        return clientModel.ax.delete('/basicdata/shelf/deleteByIds?ids='+ids)
    },
    update(shelf) {
        return clientModel.ax.post('/basicdata/shelf/update',shelf)
    },
    add(shelf) {
        return clientModel.ax.post('/basicdata/shelf/add',shelf)
    },
    getStocks(){
        return clientModel.ax.get('/basicdata/stock/findAll')
    },
    loadByPram(obj){
        return clientModel.ax.post('/basicdata/shelf/listByParam',obj)
    },
    findAllByOrgId(){
        return clientModel.ax.get('/basicdata/stock/findAllByOrgId')
    }

}
