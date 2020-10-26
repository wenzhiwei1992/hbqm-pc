import clientModel from '../client-model'

export default {
    scrappedApvList(pageInfo) {
        return clientModel.ax.post('/scrappedApv/list',pageInfo)
    },
    scrappedApvLoad(id) {
        return clientModel.ax.get('/scrappedApv/'+id)
    },
    scrappedApvDelete(id) {
        return clientModel.ax.delete('/scrappedApv/'+id)
    },
    scrappedApvDeletes(ids) {
        return clientModel.ax.delete('/scrappedApv/scrappedApvs?ids='+ids)
    },
    scrappedApvUpdate(scrappedApv) {
        return clientModel.ax.post('/scrappedApv/update',scrappedApv)
    },
    scrappedApvAdd(scrappedApv) {
        return clientModel.ax.post('/scrappedApv/add',scrappedApv)
    },
    scrappedApvLimit(scrapedLimit) {
    return clientModel.ax.post('/scrappedApv/getScrapedApvByProductNoAndProductNameLimit',scrapedLimit)
},
}