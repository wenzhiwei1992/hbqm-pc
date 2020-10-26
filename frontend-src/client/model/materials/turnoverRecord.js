import clientModel from '../client-model'



export default {

    getTurnoverRecordInfo(id) {
        return clientModel.ax.get('/turnoverRecord/getInfo/' + id)
    },
    turnoverRecordLoad(id) {
        return clientModel.ax.get('/turnoverRecord/' + id)
    },
    turnoverRecordDelete(id) {
        return clientModel.ax.delete('/turnoverRecord/' + id)
    },
    turnoverRecordDeletes(ids) {
        return clientModel.ax.delete('/turnoverRecord/turnoverRecords?ids=' + ids)
    },
    turnoverRecordUpdate(turnoverRecord) {
        return clientModel.ax.post('/turnoverRecord/update', turnoverRecord)
    },
    turnoverRecordAdd(turnoverRecord) {
        return clientModel.ax.post('/turnoverRecord/add',turnoverRecord)
    },
    updateStatusById(turnoverRecordId){
        return clientModel.ax.get('/turnoverRecord/updateStatusById/'+turnoverRecordId)
    },
    getTurnoverRecordLimit(turnoverQuery) {
        return clientModel.ax.post('/turnoverRecord/getTurnoverRecordLimit',turnoverQuery)
    },
    clearUploadPhoto(){
        return clientModel.ax.get('/turnoverRecord/clearUploadPhoto')
    },
    changeStatusTurnoverRecord(arr) {
        return clientModel.ax.post('/turnoverRecord/changeStatusTurnoverRecord', arr)
    },
    getPKBCRecordLimit(turnoverQuery) {
        return clientModel.ax.post('/turnoverRecord/getPKBCRecordLimit',turnoverQuery)
    },
    getCheckTemplate(templateNo) {
        return clientModel.ax.get('/thridTemplate/listAllByNo?templateNo='+templateNo)
    },
    findTurnoverDetailForCheckPrice(turnoverQuery) {
        return clientModel.ax.post('/turnoverRecord/findTurnoverDetailForCheckPrice',turnoverQuery)
    },
    updatePrice(turnoverRecord) {
        return clientModel.ax.post('/turnoverRecord/updateCheckPrice', turnoverRecord)
    },
    updateCheckPriceStatus(turnoverRecord) {
        return clientModel.ax.post('/turnoverRecord/updateCheckPriceStatus', turnoverRecord)
    },
}