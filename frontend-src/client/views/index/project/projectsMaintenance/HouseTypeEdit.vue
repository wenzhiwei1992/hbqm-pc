<template>
    <div>
        <el-form  :model="editFormHouseType" v-if="editFormHouseType" :rules="eidtFormHouseTypeRules" ref="editFormHouseType" label-width="80px">
            <el-form-item label="户型" prop="name">
                <el-input v-model="editFormHouseType.name"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="editFormHouseTypeCancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit('editFormHouseType')">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import  houseTypeModel  from '../../../../model/project/houseType'
    import  floorModel from '../../../../model/project/floor'
    import {Notification} from 'element-ui'
    export default {
        methods:{
            editFormHouseTypeCancel(){
                this.$emit('house-edit-cancel')
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
                            const housetypeadd = {
                                name: this.editFormHouseType.name,
                                projectId:this.editFormHouseType.id
                            }
                            houseTypeModel.houseTypeAdd(housetypeadd).then(data => {
                                if (data.status == 'fail') {
                                    Notification({
                                        message: data.msg,
                                        type: 'warning'
                                    })
                                    loading.close();
                                    return
                                }
                                let name = data.entity.name//通过节点判断并树状图渲染||this.editForm.dataTree点击添加进入模态框传入值(append(data)方法)
                                const newChild = {id: this.editFormHouseType.treeId++, label: name, children: []};
                                if (!this.editFormHouseType.dataTree.children) {
                                    this.$set(this.editFormHouseType.dataTree, 'children', []);
                                }
                                this.editFormHouseType.dataTree.children.push(newChild);
                                Notification({
                                    message: '新建成功',
                                    type: 'success'
                                })
                                loading.close();
                                this.$emit('house-edit-finish')
                            })
                        } else if (this.mode == 'update') {
                            const loading = this.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            const housetypeupdate = {
                                name: this.editFormHouseType.name,
                                id: this.editFormHouseType.id
                            }
                            houseTypeModel.houseTypeUpdate(housetypeupdate)
                                .then(data => {
                                    if (data.status == 'fail') {
                                        Notification({
                                            message: data.msg,
                                            type: 'warning'
                                        })
                                        loading.close();
                                        return
                                    }
                                    Notification({
                                        message: '编辑成功',
                                        type: 'success'
                                    })
                                    loading.close();
                                    this.$emit('house-edit-finish')
                                })
                        }
                    }
                })
            }
        },
        props: ['editFormHouseType', 'mode'],
        data() {
            return {
                eidtFormHouseTypeRules:{
                    name: [
                        {required: true, message: '请选择楼栋信息', trigger: 'blur'},
                    ],
                }
            }
        }
    }
</script>

<style scoped>

</style>