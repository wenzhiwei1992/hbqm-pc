import clientModel from "../client-model";

export default {
    getIndexMapNum1(orgIds) {
        return clientModel.ax.post('/indexMap/getIndexMapNum1', {orgIds},{showLoading:false})
    },
    getIndexMapNum2(orgIds) {
        return clientModel.ax.post('/indexMap/getIndexMapNum2', {orgIds},{showLoading:false})
    },
    getIndexMapNum3(orgIds) {
        return clientModel.ax.post('/indexMap/getIndexMapNum3', {orgIds},{showLoading:false})
    },
    getIndexMapEcharts01(orgIds) {
        return clientModel.ax.post('/indexMap/getIndexMapEcharts01', {orgIds},{showLoading:false})
    },
    getIndexMapEcharts02(orgIds) {
        return clientModel.ax.post('/indexMap/getIndexMapEcharts02', {orgIds},{showLoading:false})
    },
    getIndexMapEcharts03(orgIds) {
        return clientModel.ax.post('/indexMap/getIndexMapEcharts03', {orgIds},{showLoading:false})
    },
    getIndexMapEcharts04(orgIds) {
        return clientModel.ax.post('/indexMap/getIndexMapEcharts04', {orgIds},{showLoading:false})
    }
}
