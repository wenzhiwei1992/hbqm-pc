import clientModel from "../client-model";


export default {
    findSfgMatlBySfgId(id) {
        return clientModel.ax.get('/sfgMatl/findSfgMatlBySfgId/' + id)
    }


}
