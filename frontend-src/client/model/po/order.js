import clientModel from '../client-model'
import project from "../project/project";



export default {
    /**
     * 一键产出新版本
     * @param  form 里面包含ids 和 选择的模式
     */
    oneKeyOrderOutByResult(form){
        return clientModel.ax.post('/po/order/oneKeyOrderOutByResult',form)
    },
    /**
     * 一键产出
     * @param  ids 订单id集合
     */
    oneKeyOrderOut(ids){
        return clientModel.ax.post('/po/order/oneKeyOrderOut?ids='+ids)
    },
    addDetailByRow(obj){
        return clientModel.ax.post('/po/order/addDetailByRow',obj)
    },
    deleteProductDetailByOrderDetailId(id){
        return clientModel.ax.delete('/po/order/deleteProductDetailByOrderDetailId/'+id)
    },
    getBuildCodeArrByHouseTypeId(projectId,houseTypeId){
        return clientModel.ax.get('/po/order/getBuildArrByHouseType/'+projectId+'/'+houseTypeId)
    },
    list(pageInfo) {
        return clientModel.ax.post('/po/order/findAll',pageInfo)
    },
    getById(id) {
        return clientModel.ax.get('/po/order/id/'+id)
    },
    delete(id) {
        return clientModel.ax.delete('/po/order/'+id)
    },
    update(obj) {
        return clientModel.ax.put('/po/order/update',obj)
    },
    add(obj) {
        return clientModel.ax.post('/po/order/add',obj)
    },
    getProductsByProjectId(id,pageinfo){
        return clientModel.ax.post('/project/findProductsByProjectId/'+id,pageinfo)
    },
    getProductsByProjectIdForNoSCPage(id,condition){
        return clientModel.ax.post('/project/findProductsByProjectIdForNoSCPage/'+id,condition)
    },
    getBuildCodeArr(id){
        return clientModel.ax.get('/po/order/listAllBuildInfoNoPageable/'+id)
    },
    getFloorNumArr(projectId,buildCode){
        return clientModel.ax.get('/po/order/listAllFloorInfoNoPageable/'+projectId+'/'+buildCode)
    },
    findAllProjectList(){
        return clientModel.ax.get('/project/listAllNoPageable')
    },
    updateStatus(id,option){
        return clientModel.ax.put('/po/order/updateStaus/'+id+ '/'+option)
    },
    findAllByFormInline(obj,pageInfo){
        return clientModel.ax.post('/po/order/findAllByFormInline',obj,pageInfo)
    },
    getHouseTypeArr(id){
        return clientModel.ax.get('/po/order/listAllHouseType/'+id)
    },

    getProductByNoProjectId(condition){
        return clientModel.ax.post('/project/getProductByNoProjectId',condition)
    },
    findAllProductTypes(){
        return clientModel.ax.get('/po/order/findAllProductTypes')
    }
}
