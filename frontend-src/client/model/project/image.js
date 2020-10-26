import clientModel from '../client-model.js'

export default{
  getCheckImageInfo(params){
      return clientModel.ax.post('/check/getCheckImageInfo',params)
  },
  downloadimage(params){
      return clientModel.ax.post('/check/downloadimage',params)
  }
} 

