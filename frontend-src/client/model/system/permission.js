import clientModel from '../client-model'

export default {
    loadTree(){
        return clientModel.ax.get('/permission/loadTree')
    },
    loadByGroupId(groupId) {
        return clientModel.ax.get('/permission/findById/'+groupId)
    },
    update(obj){
        return clientModel.ax.post('/permission/update',obj)
    },
    findByPmId(id){
        return clientModel.ax.get('/permission/findByPmId/'+id)
    },
    loadPids(){
        return clientModel.ax.get('/permission/loadPids')
    },
    add(obj){
        return clientModel.ax.post('/permission/add',obj)
    },
    delete(id){
        return clientModel.ax.delete('/permission/delete/'+id)
    }
    
}
