import clientModel from '../client-model'



export default {
    templetList(pageInfo) {
        return clientModel.ax.post('/basicdata/templet/list',pageInfo)
    },
    templetById(id) {
        return clientModel.ax.get('/basicdata/templet/findTemoletById/'+id)
    },
    templetDelete(id) {
        return clientModel.ax.delete('/basicdata/templet/'+id)
    },
    templetDeletes(ids) {
        return clientModel.ax.delete('/basicdata/templet/deletes?ids='+ids)
    },
    templetUpdate(templet) {
        return clientModel.ax.post('/basicdata/templet/update',templet)
    },
    templetAdd(templet) {
        return clientModel.ax.post('/basicdata/templet/add',templet)
    },
    findTempletByTempletNo(templetNo) {
        return clientModel.ax.get('/basicdata/templet/findTempletByTempletNo/'+templetNo)
    },
}
