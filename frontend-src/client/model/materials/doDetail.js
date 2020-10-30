import clientModel from '../client-model'



export default {

    getDoDetailsByMaDeliveryOrderIdAndLimit(turnoverQuery) {
        return clientModel.ax.post('/doDetail/getDoDetailsByMaDeliveryOrderIdAndLimit', turnoverQuery)
    },
    getMaterialStorageByLimit(turnoverQuery) {
        return clientModel.ax.post('/doDetail/getMaterialStorageByLimit', turnoverQuery)
    },
    getDoDetailsByMaDeliveryOrderId(maDeliveryOrderId) {
        return clientModel.ax.get('/doDetail/getDoDetailsByMaDeliveryOrderId/' + maDeliveryOrderId)
    },
    doDetailDelete(id) {
        return clientModel.ax.delete('/doDetail/' + id)
    },
    deleteDoDetailsByMaDeliveryOrderIds(ids) {
        return clientModel.ax.delete('/doDetail/deleteDoDetailsByMaDeliveryOrderIds?ids=' + ids)
    },
    updateStatusPassThrough(doDetail) {
        return clientModel.ax.post('/doDetail/updateStatusPassThrough', doDetail)
    },
    updateStatusOverrule(doDetail) {
        return clientModel.ax.post('/doDetail/updateStatusOverrule', doDetail)
    },
    changeStatus(arr) {
        return clientModel.ax.post('/doDetail/changeStatus', arr)
    },
    saveReturnNum(row) {
        return clientModel.ax.post('/doDetail/saveReturnNum', row)
    },
    addReturn(arr){
        return clientModel.ax.post('/doDetail/addReturn', arr)
    },
    getReturnDoDetailVO(doDetailVO){
    return clientModel.ax.post('/doDetail/getReturnDoDetailVO', doDetailVO)
    },
    getDoDetailById(id) {
        return clientModel.ax.get('/doDetail/getDoDetailById/' + id)
    },


}