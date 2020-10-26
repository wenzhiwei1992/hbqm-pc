import clientModel from '../client-model'



export default {
    list(pageInfo) {
        return clientModel.ax.post('/po/order/orderTackList',pageInfo)
    },
    getOrderTrackDetailsById(id){
        return clientModel.ax.get('/po/order/getOrderTrackDetailsById/'+id)
    },
    findAllByFormInline(obj){//动态查询
        return clientModel.ax.post('/po/order/findAllOrderTrackByFormInline',obj)
    },
    findAllByFormInlineForConcrete(obj){//动态查询
        return clientModel.ax.post('/po/order/findAllByFormInlineForConcrete',obj)
    },
}
