import clientModel from '../client-model'

export default{
  listByOrg(){
    return clientModel.ax.get('user/listByOrg')
  },
  listAll(){
    return clientModel.ax.get('/messageTemplate/listAll')
  },
  news(id){
    return clientModel.ax.get('/messageTemplate/id/'+id)
  },
  newsUpdate(news){
    return clientModel.ax.post('/messageTemplate/update',news)
  },
  newsDelete(id){
    return clientModel.ax.get('/messageTemplate/delete/'+id)
  },
  updateJobTime(params){
    return clientModel.ax.post('/quartzJob/updateJobTime',params)
  }
}
