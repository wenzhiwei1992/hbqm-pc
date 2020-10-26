import clientModel from '../client-model'

export default {
    findMaterialsByProductId(productId) {
        return clientModel.ax.get('/materialChange/findMaterialsByProductId/' + productId)
    },
    findAllByQuery(productChangeQuery) {
        return clientModel.ax.post('/materialChange/findAllByQuery', productChangeQuery)
    },


}
