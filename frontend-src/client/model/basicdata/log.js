import clientModel from '../client-model'

export default {
    list(condition) {
        return clientModel.ax.post('/api/logs',condition)
    },

}
