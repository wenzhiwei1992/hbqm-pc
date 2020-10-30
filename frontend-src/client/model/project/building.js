import clientModel from '../client-model'

export default {
    buildingList(pageInfo) {
        return clientModel.ax.post('/building/list', pageInfo)
    },
    buildingLoad(id) {
        return clientModel.ax.get('/building/' + id)
    },
    buildingDelete(id) {
        return clientModel.ax.delete('/building/' + id)
    },
    buildingDeletes(ids) {
        return clientModel.ax.delete('/building/buildings?ids=' + ids)
    },
    buildingUpdate(building) {
        return clientModel.ax.post('/building/update', building)
    },
    buildingAdd(building) {
        return clientModel.ax.post('/building/add', building)
    },
    findByProjectId(projectId) {
        return clientModel.ax.get('/building/findByProjectId?projectId=' + projectId)
    },
    getOldNumAndNewNum(buildingNum) {
        return clientModel.ax.post('/building/getOldNumAndNewNum', buildingNum)
    },
    getBuildCodeAndProjectNameAndHouseTypeNameByBuildingId(buildingId) {
        return clientModel.ax.get('/building/getBuildCodeAndProjectNameAndHouseTypeNameByBuildingId/' + buildingId)
    },

    getBuildingsByByProjectId(projectId) {
        return clientModel.ax.get('/building/getBuildingsByByProjectId/' + projectId)
    },
    getBuildingsByHouseTypeId(houseTypeId) {
        return clientModel.ax.get('/building/getBuildingsByHouseTypeId/' + houseTypeId)
    },
    findBuildingsByProjectIdAndProductId(model) {
        return clientModel.ax.post('/building/findBuildingsByProjectIdAndProductId', model)
    },
    findBuildingsByProjectIdAndProductIds(model) {
        return clientModel.ax.post('/building/findBuildingsByProjectIdAndProductIds', model)
    },
    findProductBelongToFloorNum(model) {
        return clientModel.ax.post('/product/findProductBelongToFloorNum',model)
    },
    findFloorSendPrDetail(model) {
        return clientModel.ax.post('/prReport/findFloorSendPrDetail',model)
    },
    updateFloorProductDetail(model) {
        return clientModel.ax.post('/product/updateFloorProductDetail',model)
    },
    findPTByBuildingId(buildingId) {
        return clientModel.ax.get('/floorRelevance/findPTByBuildingId/' + buildingId)
    },
    findProductBelongToFloorNum1(id) {
        return clientModel.ax.get('/project/findProjectProgress/'+id)
    },
    findBuildCodeAndPVolSumAndPNum(model) {
        return clientModel.ax.post('/findBuildCodeAndPVolSumAndPNum?building', model)
    },
    updateFloorProductDetailNew(model) {
        return clientModel.ax.post('/product/updateFloorProductDetailNew', model)
    },
    copyBuildAllInfo(buildId) {
        return clientModel.ax.get('/floorRelevance/copyBuildAllInfo?buildId='+ buildId)
    },

}