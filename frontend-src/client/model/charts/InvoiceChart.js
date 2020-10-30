import clientModel from "../client-model";


export default {
    invoiceChart(query) {
        return clientModel.ax.post('/invoice/invoiceChart', query)
    },
    deliveryDetailsChart(query) {
        return clientModel.ax.post('/invoice/deliveryDetailsChart', query)
    },
    
}
