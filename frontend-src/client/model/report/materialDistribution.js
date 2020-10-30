import clientModel from '../client-model'

export default {

    findMaterialDistribution(model) {
        return clientModel.ax.post('/materialDistribution/findMaterialDistribution', model)
    },
}