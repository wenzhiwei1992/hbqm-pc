import clientModel from '../client-model'

export default {
    updateGroupPermission(group) {
        return clientModel.ax.post('/groupMenu/updateGroupPermission', group)
    },
    findGroupMenusByGroupId(groupId) {
        return clientModel.ax.get('/groupMenu/findGroupMenusByGroupId/' + groupId);
    },
    findIndexByGroupId(groupId) {
        return clientModel.ax.get('/groupMenu/findIndexByGroupId/' + groupId);
    },

}
