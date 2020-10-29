import clientModel from '../client-model'

export default {
    projectList(projectQuery) {
        return clientModel.ax.post('/reserveproject/list', projectQuery)
    },

    projectLoad(id) {
        return clientModel.ax.get('/reserveproject/' + id)
    },
    projectDelete(id) {
        return clientModel.ax.delete('/reserveproject/' + id)
    },

    projectUpdate(project) {
        return clientModel.ax.post('/reserveproject/update', project)
    },
    projectAdd(project) {
        return clientModel.ax.post('/reserveproject/add', project)
    },


}