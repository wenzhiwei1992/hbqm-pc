import clientModel from '../client-model'


export default {
    getTurnoverLimit(turnoverQuery) {
        return clientModel.ax.post('/turnoverDetail/getTurnoverLimit', turnoverQuery)
    },
    turnoverDetailLoad(id) {
        return clientModel.ax.get('/turnoverDetail/' + id)
    },
    turnoverDetailDelete(id) {
        return clientModel.ax.delete('/turnoverDetail/' + id)
    },
    turnoverDetailDeletes(ids) {
        return clientModel.ax.delete('/turnoverDetail/turnoverDetails?ids=' + ids)
    },
    deleteByTurnoverRecordIds(ids) {
        return clientModel.ax.delete('/turnoverDetail/deleteByTurnoverRecordIds?ids=' + ids)
    },
    turnoverDetailUpdate(turnoverDetail) {
        return clientModel.ax.post('/turnoverDetail/update', turnoverDetail)
    },
    turnoverDetailAdd(turnoverDetail) {
        return clientModel.ax.post('/turnoverDetail/add', turnoverDetail)
    },
    getSuttleAndRealityWeightByCount(turnoverQuery) {
        return clientModel.ax.post('/turnoverDetail/getSuttleAndRealityWeightByCount', turnoverQuery)
    },
    turnoverDetailAdds(obj) {
        return clientModel.ax.post('/turnoverDetail/adds', obj)
    },
    turnoverDetailUpdates(turnoverDetail) {
        return clientModel.ax.post('/turnoverDetail/updates', turnoverDetail)
    },
    getTurnoverDetailByTurnoverRecordId(turnoverRecordId) {
        return clientModel.ax.get('/turnoverDetail/getTurnoverDetailByTurnoverRecordId/' + turnoverRecordId)
    },
    updateCheckStatusById(turnoverDetail) {
        return clientModel.ax.post('/turnoverDetail/updateCheckStatusById', turnoverDetail, {
            headers: {
                'Content-Type': 'application/json;charset=UTF-8'
            }
        })
    },
    updateTurnoverDetailWarehouseStatusById(turnoverDetail) {
        return clientModel.ax.post('/turnoverDetail/updateTurnoverDetailWarehouseStatusById', turnoverDetail)
    },
    getTurnoverDetailsById(id) {
        return clientModel.ax.get('/turnoverDetail/getTurnoverDetailsById/' + id)
    },
    getPoundListCheck(turnoverQuery) {
        return clientModel.ax.post('/turnoverDetail/getPoundListCheck', turnoverQuery)
    },
    updateTurnoverDetailWarehouseStatusByIdNew(id) {
        return clientModel.ax.get('/turnoverDetail/updateTurnoverDetailWarehouseStatusByIdNew/' + id)
    },
    updateStatusByRecordId(turnoverQuery) {
        return clientModel.ax.post('/turnoverDetail/updateStatusByRecordId',turnoverQuery)
    },
    updateConcessionStatusById(turnoverQuery) {
        return clientModel.ax.post('/turnoverDetail/updateConcessionStatusById',turnoverQuery)
    },
    updateConcessionStatusByRecordId(turnoverQuery) {
        return clientModel.ax.post('/turnoverDetail/updateConcessionStatusByRecordId',turnoverQuery)
    },
    getTurnoverDetailByTurnoverRecordIdAndLimit(turnoverQuery) {
        return clientModel.ax.post('/turnoverDetail/getTurnoverDetailByTurnoverRecordIdAndLimit', turnoverQuery)
    },
}