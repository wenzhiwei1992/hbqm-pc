import clientModel from "../client-model";


export default {
    categoryList(categoryQuery) {
        return clientModel.ax.post('/category/list', categoryQuery)
    },
    categoryLoad(id) {
        return clientModel.ax.get('/category/' + id)
    },
    delete(id) {
        return clientModel.ax.delete('/category/delete/' + id)
    },
    categoryDeletes(ids) {
        return clientModel.ax.delete('/category/categorys?ids=' + ids)
    },
    categoryUpdate(category) {
        return clientModel.ax.post('/category/update', category)
    },
    categoryAdd(category) {
        return clientModel.ax.post('/category/add',category)
    },
    getCategorys() {
        return clientModel.ax.get('/category/getCategorys')
    },
    getCategorysByOrgId() {
        return clientModel.ax.get('/category/getCategorysByOrgId')
    },
    getCategorysByClassificationId(classificationId) {
        return clientModel.ax.get('/category/getCategorysByClassificationId/' + classificationId)
    },
    getCategories(categoryQuery) {
    return clientModel.ax.post('/category/getCategories',categoryQuery)
    },
    getCategorysToTree() {
        return clientModel.ax.get('/category/getCategorysToTree')
    },
    findCategoryByParentId(id) {
        return clientModel.ax.get('/category/findCategoryByParentId/'+id)
    },
    updateCategoryCodeLevelByDrag(model) {
        return clientModel.ax.post('/category/updateCategoryCodeLevelByDrag', model)
    },
    updateTree(category) {
        return clientModel.ax.post('/category/updateTree', category)
    },
    addTree(category) {
        return clientModel.ax.post('/basicdata/category/add',category)
    },
    getCategorysToTreeParentDisabled() {
        return clientModel.ax.get('/category/getCategorysToTreeParentDisabled')
    },
    findAllByQuery(semiFinishedGoodsQuery) {
        return clientModel.ax.post('/sfgWarehousing/findAllByQuerySfg', semiFinishedGoodsQuery)
    },



}
