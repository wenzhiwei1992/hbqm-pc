import clientModel from "../client-model";


export default {
    getProductChartInfo(query) {
        return clientModel.ax.post('/chart/productChart/getProductChartInfo', query)
    },

    getProductFloorRelevanceChartDetail(query) {
        return clientModel.ax.post('/chart/productChart/getProductFloorRelevanceChartDetail', query)
    },

    getDailyProductionPlanReport(query) {
        return clientModel.ax.post('/chart/productChart/getDailyProductionPlanReport', query)
    },

    getProductRecordChartDetail(query) {
        return clientModel.ax.post('/chart/productChart/getProductRecordChartDetail', query)
    },
}
