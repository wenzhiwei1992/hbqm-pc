import clientModel from '../client-model'

export default {
    deleteByUserIdAndGroupId(userId,groupId) {
        return clientModel.ax.delete('/userGroup/deleteByUserIdAndGroupId?userId='+userId+"&groupId="+groupId)
    },
    deleteByGroupIdAndUserIdIn(groupId,userIds) {
        return clientModel.ax.delete('/userGroup/deleteByGroupIdAndUserIdIn?groupId='+groupId+"&userIds="+userIds)
    },
    syncQywxUser(qywxForm){
        return clientModel.ax.post('/user/syncQywxUser',qywxForm)
    },
    /**
     * 根据组织ID加载
     */
    loadPcSetup(){
        return clientModel.ax.get('/setup/loadPcSetup')
    },
    syncQyddUser(){
        return clientModel.ax.get('/user/m2ep/syncQyddUser')
    },

}
