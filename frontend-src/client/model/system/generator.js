import clientModel from '../client-model'

export default {
    list(p1,p2,p3) {
        return clientModel.ax.get('/api/generator/tables?name='+p1+'&page='+p2+'&size='+p3)
    },
    getByTableName(param){
        return clientModel.ax.get('/api/generator/columns?tableName='+param)
    },
    getConfig(){
        return clientModel.ax.get('/api/genConfig')
    },
    updateGenConfig(genConfig){
        return clientModel.ax.put('/api/genConfig',genConfig)
    },
    generator(columnsInfos,param){
        return clientModel.ax.post('/api/generator?tableName='+param,columnsInfos)
    }
}
