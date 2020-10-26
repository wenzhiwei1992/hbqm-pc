import clientModel from '../client-model'

export default {
    findUserById(id) {
        return clientModel.ax.get('/user/' + id)
    },

}
