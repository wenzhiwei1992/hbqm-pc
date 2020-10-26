import clientModel from '../client-model'

export default {
    projectList(projectQuery) {//获取自己能管理的组织下的项目

        return clientModel.ax.post('/project/list', projectQuery)
    },
    getAllProjects() {//获取自己能管理的组织下的项目
        return clientModel.ax.get('/project/list')
    },
    findAllProjects() {//获取自己能管理的组织下的项目
        return clientModel.ax.get('/project/findAllProjects')
    },
    projectListS() {//根据组织id获取对应的项目
        return clientModel.ax.get('/project/listS')
    },
    getExternalAllProject() {//根据组织id获取对应的项目+正在做的外协
        return clientModel.ax.get('/project/getExternalAllProject')
    },
    projectLoad(id) {
        return clientModel.ax.get('/project/' + id)
    },
    projectDelete(id) {
        return clientModel.ax.delete('/project/' + id)
    },
    projectDeletes(ids) {
        return clientModel.ax.delete('/project/projects?ids=' + ids)
    },
    projectUpdate(project) {
        return clientModel.ax.post('/project/update', project)
    },
    projectAdd(project) {
        return clientModel.ax.post('/project/add', project)
    },
    upLoadData(id) {
        return clientModel.ax.get('/productExcel/upLoadData/' + id)
    },
    getMaterialsToProject(projectMaterialQuery) {
        return clientModel.ax.post('/project/getMaterialsToProject', projectMaterialQuery)
    },
    getMaterialsInfos(projectMaterialQuery) {
        return clientModel.ax.post('/project/getMaterialsInfos', projectMaterialQuery)
    },
    countProductDetailsByOverTimeMonthAndDay() {
        return clientModel.ax.get('/productDetail/countProductDetailsByOverTimeMonthAndDay')
    },
    getProjectByMoldId(moldId) {
        return clientModel.ax.get('/project/getProjectByMoldId/' + moldId)
    },
    getMaterialsToProductBaseByMaterialNo (arr) {
        return clientModel.ax.post('/project/getMaterialsToProductBaseByMaterialNo', arr)
    },
    getMaterialsToFloorNumByMaterialNo (arr) {
        return clientModel.ax.post('/project/getMaterialsToFloorNumByMaterialNo', arr)
    },
    
    getTeamByOrg() {
        return clientModel.ax.get('/basicdata/teamInfo/listByOrg')
    },
    findProjectProgress(projectId) {
        return clientModel.ax.get('/project/findProjectProgress/' + projectId)
    },


}