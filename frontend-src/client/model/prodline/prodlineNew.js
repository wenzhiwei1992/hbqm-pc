import clientModel from '../client-model'


export default {
    list(pageInfo) {
        return clientModel.ax.post('/basicdata/prodline/list',pageInfo)
    },
    getAllProdLinesForTree(){
        return clientModel.ax.get('/basicdata/prodLine/getAllProdLinesForTree')
    },
    getTabDatasByProdLine(filter){
        return clientModel.ax.post('/basicdata/prodline/getTabDatasByProdLine',filter)
    },
    getPathsByProdlineId(lienId){
        return clientModel.ax.get('/basicdata/path/getPathsByProdlineId/'+lienId)
    }
}
