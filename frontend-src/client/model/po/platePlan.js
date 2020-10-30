import clientModel from '../client-model'


export default {
    pageableList(obj) {
        return clientModel.ax.post('/po/platePlan/findAll', obj)
    },
    getById(id) {
        return clientModel.ax.get('/po/platePlan/id/' + id)
    },
    getProductsById(id) {
        return clientModel.ax.get('/po/platePlan/getProductDetailsById/' + id)
    },
    cancelPlatePlan(id, currentId) {
        return clientModel.ax.put('/po/platePlan/cancelPlatePlan/' + id + '/' + currentId)
    },
    getProductDetailInfo(obj) {
        return clientModel.ax.post('/po/platePlan/productDetails', obj)
    },
    getProductDetailInfoListS(obj) {
        return clientModel.ax.post('/po/platePlan/getProductDetailInfoListS', obj)
    },
    analogSignalSubmit(obj) {
        return clientModel.ax.post('/po/platePlan/analogSignalSubmit', obj)
    },
    /**
     * 构件状态更新
     * @param ids
     * @param status
     * @returns {AxiosPromise<any>}
     */
    updateAllProductDetailInfoStaus(ids,status) {
        return clientModel.ax.post('/po/platePlan/updateAllProductDetailInfoStaus?status='+status, ids)
    },
    getProductDetailInfoForWxRk(formSelect) {
        return clientModel.ax.post('/po/platePlan/getProductDetailInfoForWxRk', formSelect)
    },
    getProductDetailAllInfoList(obj) {
        return clientModel.ax.post('/charts/getProductDetailAllInfoList', obj)
    },

}
