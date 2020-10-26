import clientModel from "../client-model";

export  default {
    productDetailAdd(productDetail) {
        return clientModel.ax.post('/productDetail/add',productDetail)
    },
    getProductDetailByFloorRelevanceId(id){
    return clientModel.ax.get('/productDetail/getProductDetailByFloorRelevanceId/'+id)
    },
    productDetailDelete(id) {
        return clientModel.ax.delete('/productDetail/'+id)
    },
    deleteAllByFAndFloorRelevanceId(ids) {
        return clientModel.ax.delete('/productDetail/deleteAllByFAndFloorRelevanceId?ids='+ids)
    },
    getProductDetailInfo(row) {
        return clientModel.ax.post('/productDetail/getProductDetailInfo',row)
    },
    getProductDetailInfoById(id){
        return clientModel.ax.get('/productDetail/getProductDetailInfoById/'+id)
    },
    getProductDetailInfoByIdNew(id){
        return clientModel.ax.get('/productDetail/getProductDetailInfoByIdNew/'+id)
    },
    //累计打印次数
    printCountIncrease(ids){
        return clientModel.ax.get('/productDetail/printCountIncrease/'+ids)
    },
    findProductDetailByIdUsedForPrint(ids){
        return clientModel.ax.post('/productDetail/findProductDetailByIdUsedForPrint',ids)
    },
    findFinishedProductList(entity, pageInfo){
        return clientModel.ax.post('/productDetail/findFinishedProductList',{entity: entity, pageInfo: pageInfo})
    },
    printBatchCPJFXD(ids){
        return clientModel.ax.post('/reportChart/printBatchCPJFXD',ids)
    },
    findInStocklist(entity){
        return clientModel.ax.post('/productDetail/inStocklist',entity)
    },

    //核销查询
    findAllByQueryWriteOff(concreteQuery){
        return clientModel.ax.post('/productDetail/concreteFormula/findAllByQueryWriteOff', concreteQuery)
    },
    writeOffConcreteFormulasById(concreteQuery){
        return clientModel.ax.post('/productDetail/concreteFormula/writeOffConcreteFormulasById',concreteQuery)
    },
    writeOffConcreteFormulasByIds(id){
        return clientModel.ax.get('/productDetail/concreteFormula/writeOffConcreteFormulasByIds/'+id)
    },
    findConcreteFormulaNoById(id){
        return clientModel.ax.get('/productDetail/concreteFormula/findConcreteFormulaNoById/'+id)
    },

}
