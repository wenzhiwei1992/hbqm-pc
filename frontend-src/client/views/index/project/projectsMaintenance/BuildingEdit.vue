<template>
    <div>
        <el-form :model="editFormBuilding" v-if="editFormBuilding" :rules="eidtFormBuildingRules" ref="editFormBuilding"
                 label-width="80px">
            <el-form-item label="楼栋" prop="buildCode">
                <el-input v-model="editFormBuilding.buildCode"></el-input>
            </el-form-item>
            <el-form-item label="户型" prop="houseTypeId">
                <el-select v-model="editFormBuilding.houseTypeId" :disabled="mode== 'add' " placeholder="请选择">
                    <el-option
                            v-for="item in arrHouseType"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="地上层数" prop="aboveFlrNum">
                <el-input v-model="editFormBuilding.aboveFlrNum" onkeyup="value=value.replace(/[^\d]/g,'')"></el-input>
            </el-form-item>
            <el-form-item label="地下层数" prop="belowFlrNum">
                <el-input v-model="editFormBuilding.belowFlrNum" onkeyup="value=value.replace(/[^\d]/g,'')"></el-input>
            </el-form-item>
            <el-form-item label="最低层数" prop="lowestFlorNum">
                <el-input v-model="editFormBuilding.lowestFlorNum"
                          onkeyup="value=value.replace(/[^\d]/g,'')"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editFormBuildingCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editFormBuilding')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import buildingModel from '../../../../model/project/building'
    import houseTypeModel from '../../../../model/project/houseType'
    import floorModel from '../../../../model/project/floor'
    import {Notification} from 'element-ui'

    export default {
        data() {
            return {
                floorIdNotUpdate: '',//楼层未更新前的楼层
                floorIdDelete: '',//修改楼层时查到的id(删除楼层)
                // arrHouseType:[],
                id: '',
                eidtFormBuildingRules: {
                    buildCode: [
                        {required: true, message: '请输入楼栋信息', trigger: 'blur'},
                    ],
                    houseTypeId: [
                        {required: true, message: '请选择户型信息', trigger: 'change'},
                    ],
                    aboveFlrNum: [
                        {required: true, message: '请输入地上层数', trigger: 'blur'},
                    ],
                    belowFlrNum: [
                        {required: true, message: '请输入地下层数', trigger: 'blur'},
                    ],
                    lowestFlorNum: [
                        {required: true, message: '请输入最低层数', trigger: 'blur'},
                    ],
                }
            }
        },
        props: ['editFormBuilding', 'arrHouseType', 'mode'],
        mounted() {
            // this.getHouseType()'arrHouseType',
        },
        methods: {
            /**
             *循环添加楼层
             * @param build BuildIng
             */
            floorAdd(build) {

                floorModel.addFloors(build)
                /*                let aboveFlrNum = build.aboveFlrNum;
                                let lowestFlorNum = build.lowestFlorNum
                               // let belowFlrNum = build.belowFlrNum;
                                let buildId=build.id;
                                let projectId = build.project.id;
                                for(var i=0;i<aboveFlrNum;i++){
                                    var floors = {
                                        floorNum:i+1,
                                        building:{
                                            id:buildId
                                        },
                                        project:{
                                            id:projectId
                                        }
                                    }
                                    floorModel.floorAdd(floors)
                                }*/
            },
            /*            getHouseType(){
                            houseTypeModel.getHouseTypeByProjectId(this.editFormBuilding.id).then((data)=>{
                                this.arrHouseType= data.entity
                            })
                        },*/
            editFormBuildingCancel() {
                this.editFormBuilding.houseTypeId = ''//点击取消后重新定义houseType select选择框
                this.$emit('building-edit-cancel')
            },
            editFormSubmmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.mode == 'add') {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            let aboveFlrNum = this.editFormBuilding.aboveFlrNum
                            let lowestFlorNum = this.editFormBuilding.lowestFlorNum
                            if (Number(aboveFlrNum) < Number(lowestFlorNum)) {
                                this.$message('输入正确的楼层信息');
                                loading.close();
                                return
                            }
                            const buildObject = {
                                buildCode: this.editFormBuilding.buildCode.trim(),
                                houseTypeId: this.editFormBuilding.houseTypeId,
                                aboveFlrNum: this.editFormBuilding.aboveFlrNum,
                                belowFlrNum: this.editFormBuilding.belowFlrNum,
                                lowestFlorNum: this.editFormBuilding.lowestFlorNum,
                                projectId: this.editFormBuilding.projectId
                            }
                            buildingModel.buildingAdd(buildObject).then((data) => {
                                if (data.status == 'failed') {
                                    Notification({
                                        message: data.msg,
                                        type: 'warning'
                                    })
                                    loading.close();
                                    return
                                }
                                //let name = data.entity.id;
                                let name = data.entity.name//通过节点判断并树状图渲染||this.editForm.dataTree点击添加进入模态框传入值(append(data)方法)
                                const newChild = {id: this.editFormBuilding.treeId++, label: name, children: []};
                                if (!this.editFormBuilding.dataTree.children) {
                                    this.$set(this.editFormBuilding.dataTree, 'children', []);
                                }
                                this.editFormBuilding.dataTree.children.push(newChild);
                                Notification({
                                    message: '新建成功',
                                    type: 'success'

                                })
                                loading.close()
                                this.editFormBuilding.houseTypeId = ''//点击取消后重新定义houseType select选择框
                                this.$emit('building-edit-finish')
                                //this.floorAdd(data.entity);
                            })

                        } else if (this.mode == 'update') {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            var buildingNum = {
                                oldNum: this.floorIdNotUpdate,
                                newNum: this.editFormBuilding.aboveFlrNum,
                                buildingId: this.editFormBuilding.id,
                                projectId: this.editFormBuilding.projectId,
                                lowestFlorNum: this.editFormBuilding.lowestFlorNum
                            }
                            this.editFormBuilding.buildCode = this.editFormBuilding.buildCode.trim()
                            this.editFormBuilding.numVo = buildingNum
                            buildingModel.buildingUpdate(this.editFormBuilding).then((data) => {
                                if (data.status == 'failed') {
                                    Notification({
                                        message: data.msg,
                                        type: 'warning'
                                    })
                                    loading.close()
                                    return
                                }
                                Notification({
                                    message: '编辑成功',
                                    type: 'success'
                                })
                                loading.close()
                                this.$emit('building-edit-finish')
                            })
                            //2020-05-20 16:15 注释  一个月后功能稳定后删除
             /*               buildingModel.getOldNumAndNewNum(buildingNum).then((data) => {
                                if (data.status == 'fail') {
                                    Notification({
                                        message: data.msg,
                                        type: 'warning'
                                    })
                                    loading.close()
                                    return
                                }

                            })*/
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }

                });

            }
        },

    }
</script>

<style scoped>

</style>
