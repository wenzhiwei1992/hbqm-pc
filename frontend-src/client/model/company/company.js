import clientModel from '../client-model'

export default {
    companyList(companyQuery) {
        return clientModel.ax.post('/company/list', companyQuery)
    },
    companyLoad(id) {
        return clientModel.ax.get('/company/getCompanyById/' + id)
    },
    companyDelete(id) {
        return clientModel.ax.delete('/company/' + id)
    },
    companyDeletes(ids) {
        return clientModel.ax.delete('/company/companys?ids=' + ids)
    },
    companyUpdate(company) {
        return clientModel.ax.post('/company/update', company)
    },
   companyAdd(company) {
        return clientModel.ax.post('/company/add', company)
    },


}