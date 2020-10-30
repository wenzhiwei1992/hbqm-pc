import clientModel from '../client-model'

export default {
    
    add(model) {
        return clientModel.ax.post('/purchase/add', model)
    },
    list(query){
        return clientModel.ax.post('/purchase/list', query)
    },
    deleteByIds(ids){
        return clientModel.ax.delete('/purchase/deletes?ids='+ ids)
    },
    deleteInputByIds(ids){
        return clientModel.ax.delete('/purchaseInput/deletes?ids='+ ids)
    },
    loadAllProject(){
        return clientModel.ax.get('/project/listAllNoPageable');
    },
    loadAllSupplier(){
        return clientModel.ax.get('/basicdata/supplier/findAll');
    },
    loadAllUnits(){
        return clientModel.ax.get('/basicdata/units/findAll');
    },
    getPurchaseDetailsById(id){
        return clientModel.ax.get('/purchase/getPurchaseDetailsById/'+id);
    },

    listAll(path,query){
        return clientModel.ax.post('/'+path+'/list', query)
    },

    addMethod(path,model){
        return clientModel.ax.post('/'+path+'/add', model)
    },
    findPurchaseByNo(query){
        return clientModel.ax.get('/purchase/listByPurchaseNo?query='+query)
    },
    getInputDetailById(id){
        return clientModel.ax.get('/purchaseInput/getInputDetailById/'+id);
    },

    getUsersByOrgId(){
        return clientModel.ax.get('/user/listByOrg');
    },
    
}
