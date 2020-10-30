import clientModel from '../client-model'

export default {
    productList(productQuery) {
        return clientModel.ax.post('/product/list', productQuery)
    },
    productListByOrgId(productQuery) {
        return clientModel.ax.post('/product/listByOrgId', productQuery)
    },
    getAllProducts() {
        return clientModel.ax.get('/product/list')
    },
    productLoad(id) {
        return clientModel.ax.get('/product/' + id)
    },
    productDelete(id) {
        return clientModel.ax.delete('/product/' + id)
    },
    productDeletes(ids) {
        return clientModel.ax.delete('/product/products?ids=' + ids)
    },
    productUpdate(product) {
        return clientModel.ax.post('/product/update', product)
    },
    productAdd(product) {
        return clientModel.ax.post('/product/add', product)
    },
    upLoadData(id) {
        return clientModel.ax.get('/productExcel/upLoadData/' + id)
    },
    getFloorsBySBuildingId(inter, obj) {
        return clientModel.ax.post('/product/getFloorsByBuildingId?floorId=' + inter, obj)
    },
    getFloorsByBuildingIdAndproductNameAndProductTypeId(productQuery) {
        return clientModel.ax.post('/product/getFloorsByBuildingIdAndproductNameAndProductTypeId', productQuery)
    },
    findProductsByProjectId(projectId) {
        return clientModel.ax.get('/product/findProductsByProjectId/' + projectId)
    },
    getMaterialsAndProductsS(productMaterialQuery) {
        return clientModel.ax.post('/product/getMaterialsAndProductsS', productMaterialQuery)
    },
    getMaterialsToOrder(productMaterialQuery) {
        return clientModel.ax.post('/product/getMaterialsToOrder', productMaterialQuery)
    },
    getProjectAndMoldAndProTypeByPrroductId(productId) {
        return clientModel.ax.get('/product/getProjectAndMoldAndProTypeByPrroductId/' + productId)
    },
    getProductCompleteNum(productCompleteQuery) {
        return clientModel.ax.post('/product/findProductCompleteNum', productCompleteQuery)
    },
    getProductMaterialChangeInfo(query) {
        return clientModel.ax.post('/product/getProductMaterialChangeInfo', query)
    },
    getTeamProductsInfoByOrder(query) {
        return clientModel.ax.post('/po/order/getTeamProductsInfoByOrder', query)
    },
    getProductDetailList(query) {
        return clientModel.ax.post('/invoice/productDetailList', query)
    },
    changeProductStatus(id, productDetailId, type) {
        return clientModel.ax.get('/invoice/changeProductStatus?id=' + id + "&productDetailId=" + productDetailId + "&type=" + type)
    },
    deleteProductBomPath(id) {
        return clientModel.ax.get('/product/deleteProductBomPath/' + id)
    },
    findProductByProductId(productId) {
        return clientModel.ax.get('/product/findProductByProductId/' + productId)
    },
    exportExcelPrSfg() {
        return clientModel.ax.get('/productExcel/exportExcelPrSfg')
    },


}
