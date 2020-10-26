import clientModel from '../client-model'

export default {
    scrappedList(pageInfo) {
        return clientModel.ax.post('/scrapped/list',pageInfo)
    },
    scrappedLoad(id) {
        return clientModel.ax.get('/scrapped/'+id)
    },
    scrappedDelete(id) {
        return clientModel.ax.delete('/scrapped/'+id)
    },
    scrappedDeletes(ids) {
        return clientModel.ax.delete('/scrapped/scrappeds?ids='+ids)
    },
    scrappedUpdate(scrapped) {
        return clientModel.ax.post('/scrapped/update',scrapped)
    },
    scrappedUpdateIsCommit(scrapped) {
        return clientModel.ax.post('/scrapped/updateIsCommit',scrapped)
    },
    scrappedAdd(scrapped) {
        return clientModel.ax.post('/scrapped/add',scrapped)
    },
    scrappedLimit(scrapedLimit) {
        return clientModel.ax.post('/scrapped/getScrapedByProductNoAndProductNameLimit',scrapedLimit)
    },
    getScrappedByproductNo(productNo) {
        return clientModel.ax.get('/scrapped/getScrappedByproductNo/'+productNo)
    },
    getBackLogByLineId(productNo) {
        return clientModel.ax.get('/scrapped/getBackLogByLineId/'+productNo)
    },
    getScrappedByScrapedId(id) {
        return clientModel.ax.get('/scrapped/getScrappedByScrapedId/'+id)
    },
    scrap(scrapedList) {
        return clientModel.ax.post('/record/scrap',scrapedList)
    },

}
