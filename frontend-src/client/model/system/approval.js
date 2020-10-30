import clientModel from '../client-model'

export default {
    add(list) {
        return clientModel.ax.post('/approval/add', list)
    },

    findApprovalsInfo(list) {
        return clientModel.ax.post('/approval/findByTypeAndAssociationId', list)
    },
}
