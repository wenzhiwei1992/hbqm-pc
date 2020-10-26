import clientModel from "../client-model";

export default {
    entryExitDetailByMonth(query) {
        return clientModel.ax.post('/materialStorage/entryExitDetailByMonth', query)
    },
    materialStorageList(materialStorageQuery) {
        return clientModel.ax.post('/materialStorage/list', materialStorageQuery)
    },
    materialStorageLoad(id) {
        return clientModel.ax.get('/materialStorage/' + id)
    },
    materialStorageDelete(id) {
        return clientModel.ax.delete('/materialStorage/' + id)
    },
    materialStorageDeletes(ids) {
        return clientModel.ax.delete('/materialStorage/materialStorages?ids=' + ids)
    },
    materialStorageUpdate(materialStorage) {
        return clientModel.ax.post('/materialStorage/update', materialStorage)
    },
    materialStorageAdd(materialStorage) {
        return clientModel.ax.post('/materialStorage/add', materialStorage)
    },
    getMaterialStorageByMaterialStorageId(materialStorageId) {
        return clientModel.ax.get('/materialStorage/getMaterialStorageByMaterialStorageId/' + materialStorageId)
    },
    getMaterialStorageByMaterialStorage() {
        return clientModel.ax.get('/materialStorage/getMaterialStorage')
    },
    getMaterialStorageLimit(turnoverQuery) {
        return clientModel.ax.post('/materialStorage/getMaterialStorageLimit', turnoverQuery)
    },
    getMaterialArrayAdd(array) {
        return clientModel.ax.post('/materialStorage/getMaterialArrayAdd', array)
    },
    getMaterialArrayList() {
        return clientModel.ax.get('/materialStorage/getMaterialArrayList')
    },
    getListPublicClear() {
        return clientModel.ax.get('/materialStorage/getListPublicClear')
    },

    getMaterialArrayAddBelongToDoDetail(array) {
        return clientModel.ax.post('/materialStorage/getMaterialArrayAddBelongToDoDetail', array)
    },
    getMaterialArrayListDoDetailVO() {
        return clientModel.ax.get('/materialStorage/getMaterialArrayListDoDetailVO')
    },
    getListPublicClearDoDetailVO() {
        return clientModel.ax.get('/materialStorage/getListPublicClearDoDetailVO')
    },
    changeSum(arr) {
        return clientModel.ax.post('/materialStorage/changeSum', arr)
    },
    getRepertoryByLimit(materialStorageQuery) {
        return clientModel.ax.post('/materialStorage/getRepertoryByLimit', materialStorageQuery)
    },
    getMateralStorageTurnoverDetail(materialStorageQuery) {
        return clientModel.ax.post('/materialStorage/getMateralStorageTurnoverDetail', materialStorageQuery)
    },
    getmaterialStorageTurnoverReportFormsSum(materialStorageQuery) {
        return clientModel.ax.post('/materialStorage/materialStorageTurnoverReportFormsSum', materialStorageQuery)
    },
    materialExport(materialStorageQuery) {
        return clientModel.ax.post('/materialStorageExcel/exportExcelMaterstorageTurnover', materialStorageQuery)
    },
    getProductMaterialStorages(materialStorageQuery) {
        return clientModel.ax.post('/materialStorage/getProductMaterialStorages', materialStorageQuery)
    },
    findAll() {
        return clientModel.ax.get('/basicdata/units/findAll')
    },
    getOptionUnitsById(id) {
        return clientModel.ax.get('/basicdata/units/getOptionUnitsById/' + id)
    },
    getMaterialsToProductByDate(materialStorageQuery) {
        return clientModel.ax.post('/materialStorage/getMaterialsToProductByDate', materialStorageQuery)
    },
    findMaterialByName(name) {
        return clientModel.ax.get('/materialStorage/findMaterialByName/' + name)
    },
    findAllByOrgIds() {
        return clientModel.ax.get('/basicdata/units/findAllByOrgIds')
    },
    findMaterialStorageByOrgId() {
        return clientModel.ax.get('/materialStorage/findMaterialStorageByOrgId')
    },

}