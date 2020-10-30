import clientModel from '../client-model'



export default {
    getDeliveryOrderInfo(id) {
        return clientModel.ax.get('/deliveryOrder/deliveryOrderInfo/' + id)
    },
    deliveryOrderLoad(id) {
        return clientModel.ax.get('/deliveryOrder/' + id)
    },
    deliveryOrderDelete(id) {
        return clientModel.ax.delete('/deliveryOrder/' + id)
    },
    deliveryOrderDeletes(ids) {
        return clientModel.ax.delete('/deliveryOrder/deliveryOrders?ids=' + ids)
    },
    deliveryOrderUpdate(deliveryOrder) {
        return clientModel.ax.post('/deliveryOrder/update', deliveryOrder)
    },
    deliveryOrderAdd(deliveryOrder) {
        return clientModel.ax.post('/deliveryOrder/add',deliveryOrder)
    },
    getDeliveryOrderLimit(deliveryOrderQuery) {
        return clientModel.ax.post('/deliveryOrder/getDeliveryOrderLimit', deliveryOrderQuery)
    },
    updateStatusDeliveryOrderPassThrough(deliveryOrder) {
        return clientModel.ax.post('/deliveryOrder/updateStatusDeliveryOrderPassThrough', deliveryOrder)
    },
    updateStatusDeliveryOrderOverrule(deliveryOrder) {
        return clientModel.ax.post('/deliveryOrder/updateStatusDeliveryOrderOverrule', deliveryOrder)
    },
    countDeliveryOrderByDetailStatus(id) {
        return clientModel.ax.get('/deliveryOrder/countDeliveryOrderByDetailStatus/' + id)
    },
    updateStatusDeliveryOrderPassThroughNew(deliveryOrder) {
        return clientModel.ax.post('/deliveryOrder/updateStatusDeliveryOrderPassThroughNew', deliveryOrder)
    },
    addPKBC(deliveryOrder) {
        return clientModel.ax.post('/turnoverRecord/addPKBC',deliveryOrder)
    },
    getPKBCOrderLimit(deliveryOrderQuery) {
        return clientModel.ax.post('/deliveryOrder/getDeliveryOrderLimit', deliveryOrderQuery)
    },
    updatePKBC(deliveryOrder) {
        return clientModel.ax.post('/PKBC/update', deliveryOrder)
    },


    deliveryOrderUpdateTL(deliveryOrder) {
        return clientModel.ax.post('/deliveryOrder/updateTL', deliveryOrder)
    },
    deliveryOrderAddTL(deliveryOrder) {
        return clientModel.ax.post('/deliveryOrder/addTL',deliveryOrder)
    },

    getDeliveryOrderNoByTL() {
        return clientModel.ax.get('/deliveryOrder/getDeliveryOrderNoByTL')
    },
    getDeliveryOrderDetailById(deliveryOrderQuery) {
        return clientModel.ax.post('/deliveryOrder/getDeliveryOrderDetailById',deliveryOrderQuery)
    },


    getDoDetailsByMaDeliveryOrderIdTL(deliveryOrderQuery) {
        return clientModel.ax.post('/deliveryOrder/getDoDetailsByMaDeliveryOrderIdTL',deliveryOrderQuery)
    },
}