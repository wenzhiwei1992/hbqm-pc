import clientModel from '../client-model'


export default {
    list(pageInfo) {
        return clientModel.ax.post('/basicdata/codeExplainContent/list',pageInfo)
    },
    getById(id) {
        return clientModel.ax.get('/basicdata/codeExplainContent/getById/'+id)
    },
    delete(id) {
        return clientModel.ax.delete('/basicdata/codeExplainContent/delete/'+id)
    },
    update(codeExplainContent) {
        return clientModel.ax.post('/basicdata/codeExplainContent/update',codeExplainContent)
    },
    add(codeExplainContent) {
        return clientModel.ax.post('/basicdata/codeExplainContent/add',codeExplainContent)
    },
    deleteByIds(ids) {
        return clientModel.ax.delete('/basicdata/codeExplainContent/deleteByIds?ids='+ids)
    },
    getAllByCodeNo(codeNo){
        return clientModel.ax.get('/basicdata/codeExplainContent/getByCodeNo/'+codeNo)
    }
}
