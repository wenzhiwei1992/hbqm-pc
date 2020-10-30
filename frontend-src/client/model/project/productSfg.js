import clientModel from "../client-model";

export default {
    findPrSfgByProductId(productId) {
        return clientModel.ax.get('/productSfg/findPrSfgByProductId/' + productId)
    },
    addSfg(productSfg){
        return clientModel.ax.post('/productSfg/addProductSfg/add' , productSfg)
    },

}
