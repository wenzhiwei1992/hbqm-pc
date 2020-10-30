import clientModel from "../client-model";


export default {
    findSfgWarehousingByQuery(sfgWarehousingQuery) {
        return clientModel.ax.post('/sfgWarehousing/findSfgWarehousingByQuery', sfgWarehousingQuery)
    },
    addSfgWarehousing(semiFinishedGoodsDto) {
        return clientModel.ax.post('/sfgWarehousing/addSfgWarehousing', semiFinishedGoodsDto)
    },
    updateSfgWarehousing(semiFinishedGoodsDto) {
        return clientModel.ax.post('/sfgWarehousing/updateSfgWarehousing', semiFinishedGoodsDto)
    },
    deleteSfgWarehousing(ids) {
        return clientModel.ax.delete('/sfgWarehousing/deleteSfgWarehousingWithIds?ids=' + ids)
    },
    findSfgWarehousingById(id) {
        return clientModel.ax.get('/sfgWarehousing/findSemiFinishedGoodsById/' + id)
    },
    findSfgWarehousingAndDetailById(id) {
        return clientModel.ax.get('/sfgWarehousing/findSfgWarehousingAndDetailById/' + id)
    },
    updateSfgToStock(id) {
        return clientModel.ax.get('/sfgWarehousing/updateSfgToStock/' + id)
    },


}
