import clientModel from '../client-model'

export default {
    addConcreteFormulas(concreteQuery){
        return clientModel.ax.post('/concreteFormula/addConcreteFormulas', concreteQuery)
    },
    findAllByQuery(concreteQuery){
        return clientModel.ax.post('/concreteFormula/findAllByQuery', concreteQuery)
    },
    updateConcreteFormulas(concreteQuery){
        return clientModel.ax.post('/concreteFormula/updateConcreteFormulas', concreteQuery)
    },
    findConcreteFormulasById(id){
        return clientModel.ax.get('/concreteFormula/findConcreteFormulasId/'+id)
    },
    deleteConcreteFormulaById(id){
        return clientModel.ax.delete('/concreteFormula/deleteConcreteFormulaById/'+id)
    },
    copyByConcreteFormulaId(concreteQuery){
        return clientModel.ax.post('/concreteFormula/getCopyByConcreteFormulaId', concreteQuery)
    },
    findAllConcreteFormulas() {//获取混凝土配方编号
        return clientModel.ax.get('/concreteFormula/findAllConcreteFormulas')
    },
}