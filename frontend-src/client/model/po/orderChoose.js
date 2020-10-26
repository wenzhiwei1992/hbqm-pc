import clientModel from '../client-model'



export default {
    setTeamInfo(obj){
        return clientModel.ax.get('/po/order/bulkSetTeamInfo',obj)
    },
    getAllTeamInfos(){ //获取所有的班组信息
        return clientModel.ax.get('/basicdata/teamInfo/listAll')
    },
    /**
     * 根据组织ID加载产线信息
     */
    getAllProdLinesByOrgId(orgId){
        return clientModel.ax.get('/po/order/linesByOrgId/'+orgId)
    },
    list(pageInfo) {
        return clientModel.ax.post('/po/orderChoose/findAll',pageInfo)
    },
    getProductsByProjectId(id){
        return clientModel.ax.get('/project/'+id)
    },
    findAllProjectList(){
        return clientModel.ax.get('/project/list')
    },
    updateStatus(id,option){
        return clientModel.ax.put('/po/order/updateStaus/'+id+ '/'+option)
    },
    getOrderDetailsByOrderId(id){
        return clientModel.ax.get('/po/order/getOrderDetailsByOrderId/'+id)
    },

    /**
     * 根据订单ID查询子信息，同时显示信息包含楼栋楼层和项目
     */
    getOrderDetailHaveBFByOrderId(id){
        return clientModel.ax.get('/po/order/getOrderDetailHaveBFByOrderId/'+id)
    },
    saveProdlines(obj){
        return clientModel.ax.post('/po/order/saveProdlines',obj)
    },
    saveProdlinesForWps(obj){
        return clientModel.ax.post('/po/order/saveProdlinesForWps',obj)
    },
    getAllProdLines(){
        return clientModel.ax.get('/basicdata/prodLine/list')
    },
    getPathsByProdlineId(id){
        return clientModel.ax.get('/basicdata/path/getPathsByProdlineId/'+id)
    },
    getPlanDateById(id){
        return clientModel.ax.get('/po/order/getPlanDateById/'+id)
    },
    savePlanDate(obj){
        return clientModel.ax.post('/po/order/savePlanDate',obj)
    },
    findAllByFormInline(obj,pageInfo){//动态查询
        return clientModel.ax.post('/po/order/findAllByFormInline',obj,pageInfo)
    },
    bindProductDetails(id){
        return clientModel.ax.get('/po/order/bindProductDetails/'+id)
    }

}
