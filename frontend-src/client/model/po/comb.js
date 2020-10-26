import clientModel from '../client-model'



export default {

    /**
     * 根据非MPS的构件，生成排产单
     */
    createPlanM2ForOneKeyNoMps(obj){
        return clientModel.ax.post('/po/comb/createPlanM2ForOneKeyNoMps',obj)
    },
    /**
     * 非MPS的构件进行一键排产（根据选择的模台）
     */
    loadAllForOneKeyPlanNoMps(ids){
        return clientModel.ax.post('/po/comb/loadAllForOneKeyPlanNoMps',ids)
    },
    /**
     * 非MPS的构件进行排产
     */
    createPlanM2ForNoMps(obj){
        return clientModel.ax.post('/po/comb/createPlanM2ForNoMps',obj)
    },
    /**
     * 根据构件ID集合查询剩余生产量（此处的剩余生产量计算逻辑为 不排MPS的构件 ）
     */
    loadPlanDetailByProductIdsAndNoMps(ids){
        return clientModel.ax.post('/po/comb/loadPlanDetailByProductIdsAndNoMps',ids)
    },
    /**
     * 根据所有的构件和计划排产数，生成排产单
     */
    createPlanM2ForOneKey(obj){
        return clientModel.ax.post('/po/comb/createPlanM2ForOneKey',obj)
    },
    /**
     * 加载所有已经编辑过待排产构件的信息
     */
    loadAllForOneKeyPlan(){
        return clientModel.ax.get('/po/comb/loadAllForOneKeyPlan')
    },
    /**
     * 加载已经编辑过待排产构件的信息(根据勾选的ID集合)
     */
    loadAllForOneKeyPlanByIds(ids){
        return clientModel.ax.post('/po/comb/loadAllForOneKeyPlanByIds',ids)
    },
    /**
     * 模台计划排产按钮加载前置信息
     */
    updateAllowPCNum(obj,lineId){
        return clientModel.ax.post('/po/comb/updateAllowPCNum?lineId='+lineId,obj)
    },
    createPlanM2(planForm){
        return clientModel.ax.post('/po/comb/createPlanM2',planForm)
    },
    updateForM2(editForm){
        return clientModel.ax.post('/po/comb/updateForM2',editForm)
    },
    pageableList(pageInfo) {
        return clientModel.ax.post('/po/comb/findAll',pageInfo)
    },
    getById(id) {
        return clientModel.ax.get('/po/comb/id/'+id)
    },
    update(obj) {
        return clientModel.ax.post('/po/comb/update',obj)
    },
    getProductsByStatusAndSeachCondition(condition){
        return clientModel.ax.post('/po/comb/getProductsBySearchCondition',condition)
    },
    getProductsByStatus(str,lineId,obj){
        return clientModel.ax.post('/po/comb/getProductsByStatus?status='+str+'&lineId='+lineId,obj)
    },
    createPlan(obj){
        return clientModel.ax.post('/po/comb/createPlan',obj)
    },
    findAllByFormInline(obj){
        return clientModel.ax.post('/po/comb/findAllByFormInline',obj)
    },
    checkProductOnPlate(products){
        return clientModel.ax.post('/po/comb/checkProductOnPlate?productDetailIds='+products)
    },
    analogSignalSubmit(obj){
        return clientModel.ax.post('/po/comb/analogSignalSubmit',obj)
    },
    getAsStatuses(lineId){
        return clientModel.ax.get('/po/comb/getAsStatuses/'+lineId)
    },
    getAllTeamInfos(){ //获取所有的班组信息
        return clientModel.ax.get('/basicdata/teamInfo/listAll')
    },
    checkProductDetailStatusNotEqNull(obj){
        return clientModel.ax.post('/po/comb/checkProductDetailStatusEqNull',obj)
    },
    getEditProductsByProductType(str,lineId,obj){//根据产线、收池状态查询构件类型，不是明细
        return clientModel.ax.post('/po/comb/getEditProductsByProductType?status='+str+'&lineId='+lineId,obj)
    },
    getEditProductsAndSearchCondition(condition){
        return clientModel.ax.post('/po/comb/getEditProductsAndSearchCondition',condition);
    },
    findAllProductTypesByQuery(query){
        return clientModel.ax.get('/po/order/findAllProductTypesByQuery?query='+query)
    }
}
