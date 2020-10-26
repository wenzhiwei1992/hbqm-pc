import clientModel from "../client-model";


export default {
    classificationList(classificationQuery) {
        return clientModel.ax.post('/classification/list', classificationQuery)
    },
    classificationLoad(id) {
        return clientModel.ax.get('/classification/' + id)
    },
    classificationDeletes(ids) {
        return clientModel.ax.delete('/classification/classifications?ids=' + ids)
    },
    classificationUpdate(classification) {
        return clientModel.ax.post('/classification/update', classification)
    },
    classificationAdd(classification) {
        return clientModel.ax.post('/classification/add',classification)
    },
    getClassifications() {
        return clientModel.ax.get('classification/getClassifications')
    },
}
