import clientModel from '../client-model'



export default {
    list(obj) {
        return clientModel.ax.post('/user/m2ep/list',obj)
    },
    findbyId(id) {
        return clientModel.ax.put('/user/m2ep/findById/'+id)
    },
    add(obj){
        return clientModel.ax.post('/user/m2ep/add',obj)
    },
    edit(obj){
        return clientModel.ax.put('/user/m2ep/edit',obj)
    },
    deleteById(id){
        return clientModel.ax.delete('/user/m2ep/delete?id='+id)
    }









}
