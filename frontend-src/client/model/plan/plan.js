import clientModel from '../client-model'

export default {
    getPlanDetailByPlanId(planId) {
        return clientModel.ax.get('/plan/getPlanDetailByPlanId/' + planId);
    },
    getPlanDetailByPlanIdS(planId) {
        return clientModel.ax.get('/planDetail/getPlanDetailByPlanIdS/' + planId);
    },
    findInvoiceNewList(invoiceList) {
        return clientModel.ax.post('/invoice/findInvoiceNewList', invoiceList)
    },
    addNew(model) {
        return clientModel.ax.post('/invoice/addNew', model)
    },
    addNewS(model) {
        return clientModel.ax.post('/invoice/addInvoiceAndInvoiceDetailS', model)
    },
    updateNewInvoice(model) {
        return clientModel.ax.post('/invoice/updateNew', model)
    },
    findInvoiceDetailsByInvoiceId(invoiceId) {
        return clientModel.ax.get('/invoice/findInvoiceDetailsByInvoiceId/' + invoiceId);
    },
    addNewPlan(model) {
        return clientModel.ax.post('/plan/addNew', model)
    },
    updateNew(model) {
        return clientModel.ax.post('/plan/updateNew', model)
    },
    listPlan(entity, pageInfo) {
        return clientModel.ax.post('/plan/listPlan', {entity: entity, pageInfo: pageInfo})
    },
    findPlanDetailByPlanId(planId) {
        return clientModel.ax.get('/plan/findPlanDetailByPlanId/' + planId);
    },
    deleteNew(id) {
        return clientModel.ax.delete('/plan/deleteNew/' + id);
    },
    sumProductWtAndProductConcrete() {
        return clientModel.ax.get('/invoice/sumProductWtAndProductConcrete');
    },
    sumProductWtAndProductConcreteByPlanId() {
        return clientModel.ax.get('/plan/sumProductWtAndProductConcreteByPlanId');
    },
    loadFloorRelevanceIdAndDecide(model) {
        return clientModel.ax.post('/invoice/loadFloorRelevanceIdAndDecide', model)
    },
    loadFloorRelevanceIdAndDecideByPlan(model) {
        return clientModel.ax.post('/plan/loadFloorRelevanceIdAndDecideByPlan', model)
    },
    sumProductWtNumAndProductConcreteNum(entity) {
        return clientModel.ax.post('/invoice/sumProductWtNumAndProductConcreteNum', entity)
    },
    sumProductWtAndProductConcreteNumToPlan(entity) {
        return clientModel.ax.post('/plan/sumProductWtAndProductConcreteNumToPlan', entity)
    },
    findProductDetailPrintSJ(model) {
        return clientModel.ax.post('/invoice/findProductDetailPrintSJ', model)
    },
    findByIdIn(model) {
        return clientModel.ax.post('/invoiceDetail/findByIdIn', model)
    },
    updateInvoiceStatusByInvoiceId(invoiceId) {
        return clientModel.ax.get('/invoice/updateInvoiceStatusByInvoiceId/' + invoiceId);
    },
    updateInvoiceStatusByInvoiceIdS(invoiceId) {
        return clientModel.ax.get('/invoice/updateInvoiceStatusByInvoiceIdS/' + invoiceId);
    },
    findProductDetailPrintOrg(model) {
        return clientModel.ax.post('/invoice/findProductDetailPrintOrg', model)
    },
    findPlanDetailByPlanIdP(model) {
        return clientModel.ax.post('/planDetail/findPlanDetailByPlanIdPToPc', model);
    },
    findPlanDetailByQueryS(model) {
        return clientModel.ax.post('/invoice/findPlanDetailByQueryS', model)
    },
    findPlanByPlanNo(planNo) {
        return clientModel.ax.get('/plan/findPlanByPlanNo/' + planNo);
    },
    findInvoiceDetailByInfo(model) {
        return clientModel.ax.post('/invoiceDetail/findInvoiceDetailByInfo', model)
    },
    findPlanById(id) {
        return clientModel.ax.get('/plan/' + id);
    },
    updateNewInvoiceS(model) {
        return clientModel.ax.post('/invoice/updateNewS', model)
    },
    findPlanDetailByPlanIdS(planId) {
        return clientModel.ax.get('/planDetail/findPlanDetailByPlanIdP/' + planId);
    },
    findPlanDetailByPlanIdToPowerfulEdit(planId) {
        return clientModel.ax.get('/planDetail/findPlanDetailByPlanIdToPowerfulEdit/' + planId);
    },
    countInvoiceToPlanId(planId) {
        return clientModel.ax.get('/planDetail/countInvoiceToPlanId/' + planId);
    },
    deleteInvoice(id) {
        return clientModel.ax.delete('/invoice/deleteInvoice/' + id);
    },
    findFIdInInvoicesByPlanId(planId) {
        return clientModel.ax.get('/invoice/findFIdInInvoicesByPlanId/' + planId);
    },
    findShipDailyReport(query) {
        return clientModel.ax.post('/invoice/findShipDailyReport', query)
    },
    exportInvoice(query) {
        return clientModel.ax.post('/invoice/exportInvoice', query)
    },
    findReceiptImg(invoiceId) {
        return clientModel.ax.get('/invoice/findReceiptImg/' + invoiceId);
    },


}
