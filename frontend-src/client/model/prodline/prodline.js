import clientModel from '../client-model'



export default {
    prodLineList(pageInfo) {
        return clientModel.ax.post('/basicdata/prodline/list',pageInfo)
    },
    prodLine(lineNo) {
        return clientModel.ax.get('/basicdata/prodline/lineNo/'+lineNo)
    },
    prodLineById(id) {
        return clientModel.ax.get('/basicdata/prodline/id/'+id)
    },
    prodLineListDelete(id) {
        return clientModel.ax.delete('/basicdata/prodline/'+id)
    },
    prodLineListDeletes(ids) {
        return clientModel.ax.delete('/basicdata/prodlines?ids='+ids)
    },
    prodLineListUpdate(prodLine) {
        return clientModel.ax.post('/basicdata/prodline/update',prodLine)
    },
    prodLineListAdd(prodLine) {
        return clientModel.ax.post('/basicdata/prodline/add',prodLine)
    },
}
