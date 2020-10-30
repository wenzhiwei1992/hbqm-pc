import clientModel from '../client-model'

export default {
    getQuartzJobList(JobQueryCriteria) {
        return clientModel.ax.post('/quartzJob/findAllQuartzJob', JobQueryCriteria)
    },
    quartzJobAdd(data) {
        return clientModel.ax.post('/quartzJob/addQuartzJob', data)
    },
    updateIsPause(id) {
        return clientModel.ax.get('/quartzJob/updateIsPause/' + id)
    },
    quartzJobUpdate(data) {
        return clientModel.ax.post('/quartzJob/updateQuartzJob', data)
    },
    findById(id) {
        return clientModel.ax.get('/quartzJob/findById/' + id)
    },
    quartzJobDelete(ids) {
        return clientModel.ax.delete('/quartzJob/deleteQuartzJob?ids=' + ids)
    },


}
