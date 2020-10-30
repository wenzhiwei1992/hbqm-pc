import clientModel from "../client-model";

export default {
    findPrDailyData(time) {
        return clientModel.ax.post('/prDaily/findPrDailyData', time, {showLoading:false})
    },
    findPrDailyProjectDetail(time) {
        return clientModel.ax.post('/prDaily/findPrDailyProjectDetail', time, {showLoading:false})
    },
    findPrDailyTotalData(time) {
        return clientModel.ax.post('/prDaily/findPrDailyTotalData', time, {showLoading:false})
    },
    findCyDsiData(timeAndId) {
        return clientModel.ax.post('/cyDis/findCyDsiData', timeAndId, {showLoading:false})
    },
    findCyDsiDetail(timeAndId) {
        return clientModel.ax.post('/cyDis/findCyDsiDetail', timeAndId, {showLoading:false})
    },
}
