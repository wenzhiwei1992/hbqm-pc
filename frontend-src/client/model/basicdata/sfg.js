import clientModel from "../client-model";


export default {
    findAllByQuery(semiFinishedGoodsQuery) {
        return clientModel.ax.post('/semiFinishedGoods/findAllByQuery', semiFinishedGoodsQuery)
    },
    addSfg(semiFinishedGoodsDto) {
        return clientModel.ax.post('/semiFinishedGoods/addSemiFinishedGoods', semiFinishedGoodsDto)
    },
    updateSfg(semiFinishedGoodsDto) {
        return clientModel.ax.post('/semiFinishedGoods/updateSemiFinishedGoods', semiFinishedGoodsDto)
    },
    deleteSfg(ids) {
        return clientModel.ax.delete('/semiFinishedGoods/deleteSemiFinishedGoods?ids=' + ids)
    },
    findSfgById(id) {
        return clientModel.ax.get('/semiFinishedGoods/findSemiFinishedGoodsById/' + id)
    },
    findSemiFinishedGoodsVoById(id) {
        return clientModel.ax.get('/semiFinishedGoods/findSemiFinishedGoodsVoById/' + id)
    },
    deleteSfgBomPath(id) {
        return clientModel.ax.get('/semiFinishedGoods/deleteSfgBomPath/' + id)
    },
}
