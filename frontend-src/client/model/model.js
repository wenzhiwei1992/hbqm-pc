import clientModel from './client-model'

export default {
    findCertiDetailsByInvoiceId(id){
        return clientModel.ax.get('/certi/findCertiDetailsByInvoiceId/' + id);
    },
    findByIdForCertiPrint(id){
        return clientModel.ax.get('/certi/findByIdForCertiPrint/' + id);
    },
    findDetailsByInvoiceId(id){
        return clientModel.ax.get('/invoice/findDetailsByInvoiceId/' + id);
    },
    findByInvoiceIdForZSPrint(id){
        return clientModel.ax.get('/invoice/findByInvoiceIdForZSPrint/' + id);
    },
    findByInvoiceIdForHTPrint(model){
        return clientModel.ax.post('/invoice/findByInvoiceIdForHTPrint',model);
    },
    findByIdForPrint(id) {
        return clientModel.ax.get('/invoice/findByIdForPrint/' + id);
    },
    list(path, entity, pageInfo) {
        return clientModel.ax.post('/' + path + '/list', {entity: entity, pageInfo: pageInfo})
    },
    load(path, id) {
        return clientModel.ax.get('/' + path + '/' + id)
    },
    delete(path, id) {
        return clientModel.ax.delete('/' + path + '/' + id)
    },
    deletes(path, ids) {
        return clientModel.ax.delete('/' + path + '/list?ids=' + ids)
    },
    update(path, model) {
        return clientModel.ax.post('/' + path + '/update', model)
    },
    add(path, model) {
        return clientModel.ax.post('/' + path + '/add', model)
    },
    adds(path, list) {
        return clientModel.ax.post('/' + path + '/adds', list)
    },
    getSubDataById(id){
        return clientModel.ax.get('/record/productDetail/showSubData/'+id)
    },
    findProductsByQueryString(query){
        return clientModel.ax.get('/product/findProductsByQueryString?query='+query)
    },
    listForSearch(path, query) {
        return clientModel.ax.post('/' + path + '/list', query)
    },
    userUpdatePwd(user) {
        return clientModel.ax.post('/user/updatePwd',user)
    },
    showSubData(path, id){
        return clientModel.ax.get('/'+path+'/showSubData/'+id)
    },
    getProductListInfoSearch(query) {
        return clientModel.ax.post('/po/getProductsBySearchCondition', query)
    },
    updatePwdForManager(form){
        return clientModel.ax.post('/user/updatePwdForManager', form)
    },
    findInvoiceDetailsByInvoiceId(invoiceId) {
        return clientModel.ax.get('/invoice/findInvoiceDetailsByInvoiceId/' + invoiceId);
    },
    findTempletByTempletNo(templetNo){
        return clientModel.ax.get('/basicdata/templet/findTempletByTempletNo/'+ templetNo)
    },
    /**
     * 退库
     */
    outStock(ids,recordIds){
        return clientModel.ax.put('/stock/outStock?ids=' + ids+'&recordIds='+recordIds);
    },
    getLineChartByProductTypeAndProject(form){
        return clientModel.ax.post('/reportChart/getLineChartByProductTypeAndProject', form)
    },
    returnCheckStatus(id,checkType){
        return clientModel.ax.get('/check/returnProductDetailCheckStatus?id=' + id+'&checkType='+checkType);
    },
    getProductTypeMaterialReport(form){
        return clientModel.ax.post('/reportChart/getProductTypeMaterialReport', form)
    },
    exportFileExists(fileName) {
        return clientModel.ax.get('/file/exportFileExists?fileName='+fileName)
    },

    /**
     * 删除图片
     */
    deleteInvoiceBomPath(id){
        return clientModel.ax.get('/invoice/deleteInvoiceBomPath/'+id)
    },

    /**
     * 成品移库
     */
    moveFinishedProducts(obj) {
        return clientModel.ax.post('/productDetail/moveFinishedProduct', obj)
    },

}

