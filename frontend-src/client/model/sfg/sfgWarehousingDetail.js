import clientModel from "../client-model";


export default {
    findSfgWarehousingDetailByQuery(sfgWarehousingDetailQuery) {
        return clientModel.ax.post('/sfgWarehousingDetail/findSfgWarehousingDetailByQuery', sfgWarehousingDetailQuery)
    },


}
