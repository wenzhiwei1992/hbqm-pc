import clientModel from '../client-model'

export  default {
    moldList(path, entity, pageInfo) {
        return clientModel.ax.post('/' + path + '/list', {entity: entity, pageInfo: pageInfo})
    },
    getAllMolds(){
        return clientModel.ax.get('/mold/list')
    },
    moldLoad(id) {
        return clientModel.ax.get('/mold/'+id)
    },
    moldDelete(id) {
        return clientModel.ax.delete('/mold/'+id)
    },
    moldDeletes(ids) {
        return clientModel.ax.delete('/mold/molds?ids='+ids)
    },
    moldUpdate(mold) {
        return clientModel.ax.post('/mold/update',mold)
    },
    moldAdd(mold) {
        return clientModel.ax.post('/mold/add',mold)
    },
    getMoldsByProjectId(projectId){
        return clientModel.ax.get('/mold/getMoldsByProjectId/'+projectId)
    }
}