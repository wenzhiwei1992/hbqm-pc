import clientModel from '../client-model'



export default {

    deliveryOrderLoad(id) {
        return clientModel.ax.get('/deliveryOrder/' + id)
    },
    getDeliveryPlanOrderLimit(deliveryOrderQuery) {
        return clientModel.ax.post('/deliveryPlanOrder/getDeliveryPlanOrderLimit', deliveryOrderQuery)
    },
    getDeliveryPlanOrderByPlanNo(deliveryOrderQuery) {
        return clientModel.ax.post('/deliveryPlanOrder/getDeliveryPlanOrderByNo', deliveryOrderQuery)
    },
    getAllDeliveryPlanDetailByNo(orderId,stockId,projectId) {
        return clientModel.ax.get('/deliveryOrder/getAllDeliveryPlanDetailByNo/' + orderId+'/'+stockId+'/'+projectId)
    },
    
}