import clientModel from '../client-model'



export default {
    teamInfoList(pageInfo) {
        return clientModel.ax.post('/basicdata/teamInfo/list',pageInfo)
    },
    teamInfoById(id) {
        return clientModel.ax.get('/basicdata/teamInfo/id/'+id)
    },
    teamInfoListDelete(id) {
        return clientModel.ax.delete('/basicdata/teamInfo/'+id)
    },
    teamInfoListDeletes(ids) {
        return clientModel.ax.delete('/basicdata/teamInfo/deletes?ids='+ids)
    },
    teamInfoListUpdate(teamInfo) {
        return clientModel.ax.post('/basicdata/teamInfo/update',teamInfo)
    },
    teamInfoListAdd(teamInfo) {
        return clientModel.ax.post('/basicdata/teamInfo/add',teamInfo)
    },
    getAllProdLines(){
        return clientModel.ax.get('/basicdata/prodLine/list')
    },
    getAllDepts(){
        return clientModel.ax.get('/basicdata/department/listAll')
    },
    getTeamByOrg() {
        return clientModel.ax.get('/basicdata/teamInfo/listByOrg')
    },
    getAllInspectors(){
        return clientModel.ax.get('/basicdata/qualityInspection/allInspector')
    }
}
