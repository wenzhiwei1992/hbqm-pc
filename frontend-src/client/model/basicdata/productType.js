import clientModel from '../client-model'

export default {
    list(pageInfo) {
        return clientModel.ax.post('/basicdata/productType/findAll', pageInfo)
    },
    getById(id) {
        return clientModel.ax.get('/basicdata/productType/id/' + id)
    },
    delete(id) {
        return clientModel.ax.delete('/basicdata/productType/delete/' + id)
    },
    deleteByIds(ids) {
        return clientModel.ax.delete('/basicdata/productType/deletes?ids=' + ids)
    },
    update(productType) {
        return clientModel.ax.put('/basicdata/productType/update', productType)
    },
    add(productType) {
        return clientModel.ax.post('/basicdata/productType/add', productType)
    },
    getAllproductTypes() {
        return clientModel.ax.get('/productType/list')
    },
    getOrgProductTypes() {
        return clientModel.ax.get('/productType/findByOrgId')
    },
    getAllproductTypesByProductCategoriesId(productCategoriesId) {
        return clientModel.ax.get('/productType/getAllproductTypesByProductCategoriesId/' + productCategoriesId)
    },
    getProductTypesToTree() {
        return clientModel.ax.get('/productType/getProductTypesToTree')
    },
    findProductTypesByParentId(id) {
        return clientModel.ax.get('/productType/findProductTypesByParentId/' + id)
    },
    getByIdNew(id) {
        return clientModel.ax.get('/basicdata/productTypeNew/id/' + id)
    },
    findProductTypesParentById(id) {
        return clientModel.ax.get('/productType/findProductTypesParentById/' + id)
    },
    updateProductTypeCodeLevelByDrag(model) {
        return clientModel.ax.post('/productType/updateProductTypeCodeLevelByDrag', model)
    },

    getProductTypesToTreeParentDisabled() {
        return clientModel.ax.get('/productType/getProductTypesToTreeParentDisabled')
    },
    findProductTypeParentById(id) {
        return clientModel.ax.get('/productType/findProductTypeParentById/' + id)
    },






}
