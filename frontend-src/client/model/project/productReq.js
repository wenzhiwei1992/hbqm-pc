import clientModel from "../client-model";

export default {
    deleteAllByFloorRelevanceId(ids) {
        return clientModel.ax.delete('/productReq/deleteAllByFloorRelevanceId?ids=' + ids)
    }

}
