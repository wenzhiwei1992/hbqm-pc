import clientModel from '../client-model'



export default {
    loadDataForMove(condition){
        return clientModel.ax.post('/order/loadDataForMove',condition)
    },
    searchRemoteByQuery(query){
        return clientModel.ax.get('/po/workOrder/searchMPSOrder?query='+query)
    },
    moveSelectDataToTarget(condition){
        return clientModel.ax.post('/order/moveSelectDataToTarget',condition)
    },
    loadOrderPlanDateById(id){
        return clientModel.ax.get('/po/workOrder/loadOrderPlanDateById/'+id)
    }
}
