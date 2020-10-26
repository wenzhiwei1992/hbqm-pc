<template>
    <div>
        <el-form :model="selectInfo" v-if="selectInfo" ref="selectInfo" label-width="100px" size="small">
            <el-form-item label="楼栋" prop="buildId">
                <el-select clearable v-model="selectInfo.buildCode"
                            @change="changeBuilding"
                >
                    <el-option
                            v-for="item in builds"
                            :key="item.id"
                            :label="item.buildCode"
                            :value="item.buildCode">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="楼层" prop="floorId">
                <el-select clearable
                           v-model="selectInfo.floorNum"
                           :disabled="selectInfo.buildCode===''"
                           @change="changeFloor"
                           >
                    <el-option
                            v-for="item in floors"
                            :key="item.id"
                            :label="item.floorNum"
                            :value="item.floorNum">
                    </el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <div style="text-align:right" class="dialog-footer">
            <el-button @click="cancel">取 消</el-button>
            <el-button type="primary" @click="editFormSubmmit">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import buildModel from '../../../../model/project/building'
    import floorModel from '../../../../model/project/floor'

    export default {
        name: "InvoiceNewSelectBAndF",
        props: ['projectId','invoicesSelect','selectBAndFVisible'],
        data() {
            return {
                selectInfo: {
                    buildCode: '',
                    buildId:'',
                    floorNum: '',
                    floorId:''
                },
                builds: [],
                floors: [],
                productIds:[]
            }
        },
        mounted() {
            this.info()
        },
        methods: {
            info(){
                console.log('invoicesSelect------>',this.invoicesSelect)
                this.invoicesSelect.forEach(it =>{
                    this.productIds.push(it.productId)
                })
                let model={
                    projectId:this.projectId,
                    productIds:this.productIds,
                }
                buildModel.findBuildingsByProjectIdAndProductIds(model).then((data) => {
                    this.builds = data.entity
                })

            },
            changeBuilding(){
                this.selectInfo.floorNum = ''
                this.selectInfo.floorId = ''
                this.builds.forEach(val=>{
                  if(this.selectInfo.buildCode === val.buildCode){
                    this.selectInfo.buildId = val.id
                  }
                })
                let model ={
                  projectId:this.projectId,
                  productIds:this.productIds,
                  buildId:this.selectInfo.buildId
                }
                console.log(model)
                if(model.buildId!=''){
                  floorModel.findFloorsByProjectIdAndProductIdsAndBuildId(model).then((data) => {
                      this.floors = data.entity
                      if(!this.floors.length){
                        this.selectInfo.buildCode = ''
                        this.$message({
                          message: '所选项目在该楼栋下的楼层数量超出限制',
                          type: 'warning'
                        });
                      }
                  })
                }else{
                  this.floors = []
                }
            },
            changeFloor(){
              let that = this
              that.floors.forEach(val=>{
                if(that.selectInfo.floorNum === val.floorNum){
                  that.selectInfo.floorId = val.id
                }
              })
            },
            editFormSubmmit() {
              if(this.selectInfo.floorId&&this.selectInfo.buildId){
                this.invoicesSelect.forEach(val=>{
                    val.buildId = this.selectInfo.buildId
                    val.buildCode = this.selectInfo.buildCode
                    val.floorId = this.selectInfo.floorId
                    val.floorNum = this.selectInfo.floorNum
                })
                console.log(this.selectInfo.buildId,this.selectInfo.buildCode)
                console.log(this.selectInfo.floorId,this.selectInfo.floorNum)
                this.cancel()
              }else{
                this.$message({
                  message: '楼栋和楼层不能为空',
                  type: 'warning'
                });
              }
            },
            cancel() {
                this.$emit('dialogCancel')
            }
        }
    }
</script>

<style scoped>

</style>