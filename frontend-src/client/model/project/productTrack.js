import clientModel from '../client-model'

export default {
    list(pageInfo) {
        return clientModel.ax.post('/project/getAllProductTrackInfo', pageInfo)
    },
    findAllByFormInline(query) {
        return clientModel.ax.post('/project/findAllProductTrackByFormInline', query)
    },
    findAllProjectList() {
        return clientModel.ax.get('/project/listAllNoPageable')
    },
    getAllProdLines() {
        return clientModel.ax.get('/basicdata/prodLine/list')
    },
    getAllproductTypes() {
        return clientModel.ax.get('/productType/list')
    },
    findAllProductBaseInfo() {
        return clientModel.ax.get('/basicdata/productBase/listAllNoPageable')
    },

    findAllByFormInlineAndFloor(query) {
        return clientModel.ax.post('/chart/productChart/findAllProductChartByFloorRelevance', query)
    },
    findAllByFormInlineAndProject(query) {
        return clientModel.ax.post('/project/findAllByFormInlineAndProject', query)
    },
    getOrderTrack(query) {
        return clientModel.ax.post('/project/track/getOrderTrack', query)
    },
    findProjectByCondition(query) {
        return clientModel.ax.post('/project/track/findProjectByCondition', query)
    },
    findProductScheduleToBFInfo(query) {
        return clientModel.ax.post('/project/track/findProductScheduleToBFInfo', query)
    },
    findProjectActual(query) {
        return clientModel.ax.post('/chart/productChart/findProjectActual', query)
    },
    getCheckErrorEchart(query) {
        return clientModel.ax.post('/check/getCheckErrorEchart', query)
    },
}
