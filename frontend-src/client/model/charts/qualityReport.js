import clientModel from "../client-model";


export default {
    dayReport(model) {
        return clientModel.ax.post('/qualityReport/dayReport', model)
    },

}
