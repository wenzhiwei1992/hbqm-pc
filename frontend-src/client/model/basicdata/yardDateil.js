import clientModel from '../client-model'


export default {
    getYardDateil(pageInfo) {
        return clientModel.ax.post('/yardDateil/getYardDateil',pageInfo)
    },
    getDetailByIdAndStockId(id){
        return clientModel.ax.get('/yardDateil/getYardDateils/'+id)
    }
}
