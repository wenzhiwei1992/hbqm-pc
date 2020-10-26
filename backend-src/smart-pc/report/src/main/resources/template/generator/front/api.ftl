/**
 * 注意修改为自己的path
 */
import clientModel from '../client-model'


export default {
    list(condition) {
      return clientModel.ax.get('/${changeClassName}/list',condition)
    },
    edit(obj){
      return clientModel.ax.put('/${changeClassName}/update',obj)
    },
    add(){
      return clientModel.ax.post('/${changeClassName}/add',obj)
    },
    delete(id){
      return clientModel.ax.delete('/${changeClassName}/delete/'+id)
    },
}

