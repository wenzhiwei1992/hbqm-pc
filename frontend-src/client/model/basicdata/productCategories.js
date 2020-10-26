import clientModel from "../client-model";


export default {
    productCategoriesList(productCategoriesQuery) {
        return clientModel.ax.post('/productCategories/list', productCategoriesQuery)
    },
    productCategoriesLoad(id) {
        return clientModel.ax.get('/productCategories/' + id)
    },
    productCategoriesDeletes(ids) {
        return clientModel.ax.delete('/productCategories/productCategories?ids=' + ids)
    },
    productCategoriesUpdate(productCategories) {
        return clientModel.ax.post('/productCategories/update', productCategories)
    },
    productCategoriesAdd(productCategories) {
        return clientModel.ax.post('/productCategories/add',productCategories)
    },
    getProductCategories() {
        return clientModel.ax.get('/productCategories/getProductCategories')
    },
    getProductCategoriesByOrgId() {
        return clientModel.ax.get('/productCategories/getProductCategoriesByOrgId')
    },
}
