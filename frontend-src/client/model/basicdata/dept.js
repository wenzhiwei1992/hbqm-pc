import clientModel from '../client-model'



export default {
    list(pageInfo) {
        return clientModel.ax.post('/basicdata/department/list',pageInfo)
    },
    deptById(id) {
        return clientModel.ax.get('/basicdata/department/id/'+id)
    },
    deptListDelete(id) {
        return clientModel.ax.delete('/basicdata/department/'+id)
    },
    deptListDeletes(ids) {
        return clientModel.ax.delete('/basicdata/department/deletes?ids='+ids)
    },
    deptListUpdate(department) {
        return clientModel.ax.post('/basicdata/department/update',department)
    },
    deptListAdd(department) {
        return clientModel.ax.post('/basicdata/department/add',department)
    },
    listAll(){
        return clientModel.ax.get('/basicdata/department/listAll')
    },
    getProductTypesToTree(id){
        return clientModel.ax.get('/basicdata/department/getProductTypesToTree/'+id)
    },
    findDepartmentsByParentId(model){
        return clientModel.ax.post('/basicdata/department/findDepartmentsByParentId',model)
    },
    findDepartmentById(id){
        return clientModel.ax.get('/basicdata/department/findDepartmentById/'+id)
    },
    updateProductTypeCodeLevelByDrag(model) {
        return clientModel.ax.post('/basicdata/department/updateProductTypeCodeLevelByDrag',model)
    },
    findDepartmentByLevel(level){
        return clientModel.ax.get('/basicdata/department/findDepartmentBylevel/'+level)
    },









}
