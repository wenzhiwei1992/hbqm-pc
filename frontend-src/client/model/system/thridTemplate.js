import clientModel from '../client-model'



export default {
    templetList(pageInfo) {
        return clientModel.ax.post('/thridTemplate/list',pageInfo)
    },
    templetById(id) {
        return clientModel.ax.get('/thridTemplate/findThridTemplateById/'+id)
    },
    templetDelete(id) {
        return clientModel.ax.delete('/thridTemplate/'+id)
    },
    templetDeletes(ids) {
        return clientModel.ax.delete('/thridTemplate/deletes?ids='+ids)
    },
    templetUpdate(templet) {
        return clientModel.ax.post('/thridTemplate/update',templet)
    },
    templetAdd(templet) {
        return clientModel.ax.post('/thridTemplate/add',templet)
    },
}
