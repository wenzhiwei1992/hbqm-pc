import clientModel from '../client-model'



export default {
    list(pageInfo) {
        return clientModel.ax.post('/basicdata/codeExplain/list',pageInfo)
    },
    getById(id) {
        return clientModel.ax.get('/basicdata/codeExplain/getById/'+id)
    },
    delete(id) {
        return clientModel.ax.delete('/basicdata/codeExplain/delete/'+id)
    },
    deleteByIds(ids) {
        return clientModel.ax.delete('/basicdata/codeExplain/deleteByIds?ids='+ids)
    },
    update(codeExplain) {
        return clientModel.ax.post('/basicdata/codeExplain/update',codeExplain)
    },
    add(codeExplain) {
        return clientModel.ax.post('/basicdata/codeExplain/add',codeExplain)
    },
    findCECSbyCodeId(id){
        return clientModel.ax.get('/basicdata/codeExplainContent/findCECSbyCodeId/'+id)
    },
    updateOrderCode(codeExplain) {
        return clientModel.ax.post('/basicdata/orderCodeRule/update',codeExplain)
    },
    addOrderCode(codeExplain) {
        return clientModel.ax.post('/basicdata/orderCodeRule/add',codeExplain)
    },
    listAllOrderCodeRule() {
        return clientModel.ax.get('/basicdata/orderCodeRule/listAll')
    },
    getCodeNoDetail(ruleId){
        return clientModel.ax.get('/basicdata/orderCodeRule/getDetailByRuleId?ruleId='+ruleId)
    },
    getOrderCodeRuleById(id) {
        return clientModel.ax.get('/basicdata/orderCodeRule/getById/'+id)
    },
    deleteOrderCodeRuleById(id) {
        return clientModel.ax.get('/basicdata/orderCodeRule/deleteOrderCodeRuleById/'+id)
    },
    findCodeByCodeNoAndOrgId(sysCode) {
        return clientModel.ax.post('/basicdata/codeExplainContent/findCodeByCodeNoAndOrgId',sysCode)
    },

}
