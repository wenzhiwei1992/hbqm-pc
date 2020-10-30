import clientModel from '../client-model'

export default {
    floorList(floorQuery) {
        return clientModel.ax.post('/floor/list', floorQuery)
    },
    getAllFloors() {
        return clientModel.ax.get('/floor/list')
    },
    floorLoad(id) {
        return clientModel.ax.get('/floor/' + id)
    },
    findFloorByByBuildingIdAndFloorNumAndProjectId(floorData) {
        return clientModel.ax.post('/floor/findFloorByByBuildingIdAndFloorNumAndProjectId', floorData)
    },
    deleteAllBuildingIdAndFloorNumAndProjectId(floorData) {
        return clientModel.ax.post('/floor/deleteAllBuildingIdAndFloorNumAndProjectId', floorData)
    },
    floorDelete(id) {
        return clientModel.ax.delete('/floor/' + id)
    },
    floorDeletes(ids) {
        return clientModel.ax.delete('/floor/floors?ids=' + ids)
    },
    floorUpdate(floor) {
        return clientModel.ax.post('/floor/update', floor)
    },
    floorAdd(floor) {
        return clientModel.ax.post('/floor/add', floor)
    },
    findByBuildingIdAndProjectId(buildingId, projectId) {
        return clientModel.ax.get('/floor/findByBuildingIdAndProjectId?buildingId=' + buildingId + "&projectId=" + projectId)
    },
    addFloors(build) {
        return clientModel.ax.post('/building/addFloors', build)
    },
    findFloorsGroupByFloorNum() {
        return clientModel.ax.get('/floor/findFloorsGroupByFloorNum')
    },
    findFloorsByProjectIdAndProductIdAndBuildId(model) {
        return clientModel.ax.post('/floor/findFloorsByProjectIdAndProductIdAndBuildId', model)
    },
    findFloorsByProjectIdAndProductIdsAndBuildId(model) {
        return clientModel.ax.post('/floor/findFloorsByProjectIdAndProductIdsAndBuildId', model)
    },
    findFloorNumByBuildingId(buildingId) {
        return clientModel.ax.get('/floor/findFloorNumByBuildingId/' + buildingId)
    },


}