import clientModel from '../client-model'

export default {
    orgAll() {
        return clientModel.ax.get('/org/all')
    },
    removeUser(userId, orgId) {
        return clientModel.ax.delete('/org/removeUser?userId=' + userId + "&orgId=" + orgId)
    },
    addUsers(list) {
        return clientModel.ax.post('/org/addUsers', list)
    },
    orgAllByOrgIds() {
        return clientModel.ax.get('/org/allByOrgIds')
    },
    getOrgById(id) {
        return clientModel.ax.get('/org/' + id)
    },
    findOrgByUser() {
        return clientModel.ax.get('/org/findOrgByUser')
    },


}
