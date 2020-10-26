import clientModel from "../client-model";


export default {
    findOutPutRecord(sfgOutPutRecord){
        return clientModel.ax.post('/sfgOutPutRecordVO/getMateralStorageTurnoverDetail', sfgOutPutRecord)
    },
    findAllByQueryInventory(sfgOutPutRecord){
        return clientModel.ax.post('/sfgOutPutRecordVO/semiFinishedGoodsInventory', sfgOutPutRecord)
    },
    floorSemiFinishedProduct(sfgOutPutRecord){
        return clientModel.ax.post('/sfgOutPutRecordVO/floorSemiFinishedProduct', sfgOutPutRecord)
    },
    floorSemiFinishedOutPut(sfgOutPutRecord){
        return clientModel.ax.post('/sfgOutPutRecordVO/floorSemiFinishedOutPut', sfgOutPutRecord)
    },
}
