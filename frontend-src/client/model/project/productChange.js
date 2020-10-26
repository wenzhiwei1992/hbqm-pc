import clientModel from '../client-model'

export default {
    findProductChangeAll(productChangeQuery) {
        return clientModel.ax.post('/productChange/findAll', productChangeQuery)
    },
    findAllByQuery(productChangeQuery) {
        return clientModel.ax.post('/productChange/findAllByQuery', productChangeQuery)
    },

}
