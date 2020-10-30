import clientModel from '../client-model'

export  default {
    floorRelevanceAdd(floorRelevance) {
        return clientModel.ax.post('/floorRelevance/add',floorRelevance)
    },
    floorRelevanceList(pageInfo) {
        return clientModel.ax.post('/floorRelevance/list',pageInfo)
    },
    getAllByBuildId(id) {
        return clientModel.ax.get('/floorRelevance/getFloorRelevanceByBuildId/'+id)
    },
    getAllByFloorId(floorId){
        return clientModel.ax.get('/floorRelevance/getAllByFloorId/'+floorId)
    },
    getAllByBuildIdAndProductIdAndFloorId(floorRelevanceQuery){
        return clientModel.ax.post('/floorRelevance/getAllByBuildIdAndProductIdAndFloorId',floorRelevanceQuery)
    },
    // floorRelevanceUpdate(floorRelevance) {
    //     return clientModel.ax.post('/floorRelevanceUpdate/update',floorRelevance)
    // },
    floorRelevanceLoad(id) {
        return clientModel.ax.get('/floorRelevance/'+id)
    },
    deleteFloorRelevanceByBuildingIdAndFloorIdAndProductId(floorRelevanceQuery) {
    return clientModel.ax.post('/floorRelevance/deleteFloorRelevanceByBuildingIdAndFloorIdAndProductId',floorRelevanceQuery)
    },
    deleteAllByFloorId(floorId) {
        return clientModel.ax.delete('/floorRelevance/deleteAllByFloorId/'+floorId)
    },
    floorRelevanceDelete(id) {
        return clientModel.ax.delete('/floorRelevance/'+id)
    },
    floorRelevanceDeletes(ids) {
        return clientModel.ax.delete('/floorRelevance/floorRelevances?ids='+ids)
    },
    floorRelevanceUpdate(floorRelevance) {
        return clientModel.ax.post('/floorRelevance/update',floorRelevance)
    },
    getAllByBuildingIdAndFloorId(floorRelevanceQuery){
        return clientModel.ax.post('/floorRelevance/getAllByBuildingIdAndFloorId',floorRelevanceQuery)
    },
    getTypwedIdNumByProducttId(productId){
    return clientModel.ax.get('/floorRelevance/getTypwedIdNumByProducttId/'+productId)
    },
    findByBuildingIdAndFloorId(buildingId,floorId){
        return clientModel.ax.get('/floorRelevance/findByBuildingIdAndFloorId?buildingId='+buildingId+"&floorId="+floorId)
    },
    getFloorsBySBuildingId(inter,obj){
        return clientModel.ax.post('/floorRelevance/getFloorsByBuildingId?buildingId='+inter,obj)
    },
    getProductsByBuildIdAndFloorId(floorRelevanceQuery){
        return clientModel.ax.post('/floorRelevance/getProductsByBuildIdAndFloorId',floorRelevanceQuery)
    },
    getFloorRelevanceByPage(inter,obj){
        return clientModel.ax.post('/floorRelevance/getFloorRelevanceByPage?buildingId='+inter,obj)
    },
    getPlanFloors(planFloorVO){
        return clientModel.ax.post('/floorRelevance/planFloor',planFloorVO)
    },
    getFloorNumByBuildingIdAndExistProduct(buildingId){
        return clientModel.ax.get('/floorRelevance/getFloorNumByBuildingIdAndExistProduct/'+buildingId)
    },
    getFloorNumByBuildingIdNotAndExistProduct(buildingId){
        return clientModel.ax.get('/floorRelevance/getFloorNumByBuildingIdNotAndExistProduct/'+buildingId)
    },
    getFloorRelevancesByBuildingIdAndFloorId(floorRelevanceQuery){
        return clientModel.ax.post('/floorRelevance/getFloorRelevancesByBuildingIdAndFloorId',floorRelevanceQuery)
    },
    getBuildCodeByProjectIdAndExistProduct(projectId){
        return clientModel.ax.get('/floorRelevance/getBuildCodeByProjectIdAndExistProduct/'+projectId)
    },
    addProductForFloor(o){
        return clientModel.ax.post('/floorRelevance/addProductForFloor',o)
    },
    showProductFloorByFloorNum(floorId){
        return clientModel.ax.get('/floorRelevance/showProductFloorByFloorNum/'+floorId)
    },
    saveProdcutFloorAndAddProductDetail(productsByFloorNumVO){
        return clientModel.ax.post('/floorRelevance/saveProdcutFloorAndAddProductDetail',productsByFloorNumVO)
    },
    deleteProdetailAndFloorRelevance(arr){
        return clientModel.ax.post('/floorRelevance/deleteProdetailAndFloorRelevance',arr)
    },
    editFormCancelDeleteFloorRelevance(floorId){
        return clientModel.ax.get('/floorRelevance/editFormCancelDeleteFloorRelevance/'+floorId)
    },
    getFloorImages(planFloorVO){
        return clientModel.ax.post('/floorRelevance/getFloorImages',planFloorVO)
    },
    findToPlan(entity){
        return clientModel.ax.post('/floorRelevance/findFloorRelevanceToPlan',entity)
    },
    findFloorRelevanceToPlanByPlanId(planId){
        return clientModel.ax.get('/floorRelevance/findFloorRelevanceToPlanByPlanId/'+planId)
    },
    findToPlanNew(entity){
        return clientModel.ax.post('/floorRelevance/findFloorRelevanceToPlanNew',entity)
    },
    addBatchRelevanceByFloor(entity){
        return clientModel.ax.post('/floorRelevance/addBatchRelevanceByFloor',entity)
    },
}
