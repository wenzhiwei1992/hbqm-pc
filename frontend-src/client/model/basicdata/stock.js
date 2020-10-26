import clientModel from '../client-model'


export default {
    pageableList(pageInfo) {
        return clientModel.ax.post('/basicdata/stock/list', pageInfo)
    },
    getById(id) {
        return clientModel.ax.get('/basicdata/stock/getById/' + id)
    },
    deleteById(id) {
        return clientModel.ax.delete('/basicdata/stock/delete/' + id)
    },
    deleteByIds(ids) {
        return clientModel.ax.delete('/basicdata/stock/delete/deleteStocks?ids=' + ids)
    },
    update(stock) {
        return clientModel.ax.post('/basicdata/stock/update', stock)
    },
    add(stock) {
        return clientModel.ax.post('/basicdata/stock/add', stock)
    },
    getShelfsById(id) {
        return clientModel.ax.get('/basicdata/shelf/getShelfsById/' + id)
    },
    getStockByOrgIdAndType() {
        return clientModel.ax.get('/basicdata/stock/getStockByOrgIdAndType?stockType=' + '材料仓库')
    },
    getDataByParam(formSelect) {
        return clientModel.ax.post('/basicdata/stock/getDataByParam', formSelect, {showLoading:false})
    },
    plInStock(obj) {
        return clientModel.ax.post('/basicdata/stock/plInStock', obj)
    },
    loadZkInfo(obj) {
        return clientModel.ax.post('/basicdata/stock/loadZkInfo', obj)
    },
    getStockByStockType(stockType) {
        return clientModel.ax.get('/basicdata/stock/getStockByOrgIdAndType?stockType=' + stockType)
    },

}
