import clientModel from '../client-model'

export default {
    getCheckInfoForPrint(checkForPrintDto) {
        return clientModel.ax.post('/check/getCheckInfoForPrint', checkForPrintDto)
    },

}
