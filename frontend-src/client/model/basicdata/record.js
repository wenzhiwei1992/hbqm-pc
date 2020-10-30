import clientModel from '../client-model'


export default {
    findAllByFormInline(obj) {
        return clientModel.ax.post('/record/list',obj)
    },
    findAllProjectList(){
        return clientModel.ax.get('/project/listAllNoPageable')
    },
    
}
