import clientModel from '../client-model'

export default {
    /**
     * 撤销日生产计划构件
     * 1.更新构件状态=null
     * 2.移除关联关系
     */
    cancelDPS(id){
        return clientModel.ax.put('/po/workOrder/cancelDPS/'+id)
    },
    loadWorkById(id){
        return clientModel.ax.get('/po/workOrder/findById/'+id)
    },
    list(obj){
        return clientModel.ax.post('/po/workOrder/list', obj)
    },
    add(form){
        return clientModel.ax.post('/po/workOrder/add', form)
    },
    update(form){
        return clientModel.ax.post('/po/workOrder/update', form)
    },
    delete(id){
        return clientModel.ax.delete('/po/workOrder/delete/'+id)
    },
    getAllTeamInfos(){
        return clientModel.ax.get('/basicdata/teamInfo/listAll')
    },
    findAllProjectList(){
        return clientModel.ax.get('/project/listAllNoPageable')
    },
    loadAddDataForDialog(condition){
        return clientModel.ax.post('/po/workOrder/loadAddDataForDialog',condition)
    },
    searchByQuery(query){
        return clientModel.ax.get('/po/workOrder/searchMPSOrder?query='+query)
    },
    findSubDataById(id){
        return clientModel.ax.get('/po/workOrder/findSubDataById/'+id)
    },
    loadOrderPlanDateById(id){
        return clientModel.ax.get('/po/workOrder/loadOrderPlanDateById/'+id)
    },
    loadAllLines(){
        return clientModel.ax.get('/basicdata/prodLine/listByOrgId')
    },
    getTeamInfoById(lineId){
        return clientModel.ax.get('/basicdata/teamInfo/getTeamInfoById?lineId='+ lineId)
    }
}
