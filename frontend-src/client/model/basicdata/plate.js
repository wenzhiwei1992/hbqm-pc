import clientModel from '../client-model'


export default {
    plateList(formInline) {
        return clientModel.ax.post('/basicdata/plate/list', formInline)
    },
    getAllProdLines() {
        return clientModel.ax.get('/basicdata/prodLine/list')
    },
    getAllProdLinesByOrgId() {
        return clientModel.ax.get('/basicdata/prodLine/listByOrgId')
    },
    plateById(id) {
        return clientModel.ax.get('/basicdata/plate/getById/' + id)
    },
    plateListDelete(id) {
        return clientModel.ax.delete('/basicdata/plate/delete/' + id)
    },
    plateListDeletes(ids) {
        return clientModel.ax.delete('/basicdata/plates?ids=' + ids)
    },
    plateUpdate(plate) {
        return clientModel.ax.post('/basicdata/plate/update', plate)
    },
    plateAdd(plate) {
        return clientModel.ax.post('/basicdata/plate/add', plate)
    },
    getAllPlates() {
        return clientModel.ax.get('/basicdata/plate/findAll')
    },
    getIndexChartInfoByDay(day) {
        return clientModel.ax.get('/statistic/getIndexChartInfoByDay?day=' + day)
    },
    findAllByPlateNo(plateNo) {
        return clientModel.ax.get('/basicdata/plate/findAllByPlateNo/' + plateNo)
    },
    getProdlistAndTeam(){
        return clientModel.ax.get('/basicdata/prodLine/listAndTeam')
    },

}