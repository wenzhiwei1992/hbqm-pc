/**
 * 注意修改为自己的path
 */
import clientModel from '../client-model'


export default {
    list(condition) {
      return clientModel.ax.get('/genConfig/list',condition)
    },
    edit(obj){
      return clientModel.ax.put('/genConfig/update',obj)
    },
    add(){
      return clientModel.ax.post('/genConfig/add',obj)
    },
    delete(id){
      return clientModel.ax.delete('/genConfig/delete/'+id)
    },
}

