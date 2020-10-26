<template>
    <div>
        <el-form :inline="true" :model="batchEditForm" v-if="batchEditForm" :rules="batchEditFormRules" ref="batchEditForm">
          <el-form-item label="楼层范围" prop="floorIdBefore">
              <el-select v-model="batchEditForm.floorIdBefore" placeholder="请选择">
                  <el-option
                          v-for="item in floorBefore"
                          :key="item.id"
                          :label="item.floorNum"
                          :value="item.id">
                  </el-option>
              </el-select>
          </el-form-item>
          <el-form-item label="至" prop="floorIdAfter">
              <el-select v-model="batchEditForm.floorIdAfter" placeholder="请选择">
                  <el-option
                          v-for="item in floorAfter"
                          :key="item.id"
                          :label="item.floorNum"
                          :value="item.id">
                  </el-option>
              </el-select>
          </el-form-item>
        </el-form>
        <div>
            <el-button type="primary" @click="addClick">添加</el-button>
            <el-button type="primary" @click="submitForm('relevance')">保存</el-button>
        </div>
        <el-form :rules="model.rules" :model="model" ref="relevance">
          <el-table
                  :data="model.relevanceData"
                  style="width:100%"
                  :height="GLOBAL.tabelHeight"
                  header-row-class-name="header-row"
                  size="mini"
                  stripe
                  border
                  tooltip-effect="light"
                  row-key="id"
                  ref="moviesTable"
          >
              <el-table-column
                      label="构件名称"
                      align="center"
                      width="200px"
              >
                  <template slot-scope="scope">
                    <el-form-item :prop="'relevanceData.' + scope.$index + '.productId'" :rules="model.rules.productId">
                        <el-select clearable filterable v-model="scope.row.productId"
                          @focus="focusProduct()"
                        >
                          <el-option
                            v-for="item in productList"
                            :key="item.id"
                            :label="item.productName"
                            :value="item.id">
                          </el-option>
                        </el-select>
                    </el-form-item>
                      
                  </template>
              </el-table-column>
              <el-table-column
                      label="件数"
                      align="center"
              >
                  <template slot-scope="scope">
                    <el-form-item :prop="'relevanceData.' + scope.$index + '.num'" :rules="model.rules.num">
                      <el-input v-model="scope.row.num"></el-input>
                    </el-form-item>
                  </template>
              </el-table-column>
              <el-table-column
                      label="操作"
                      width="60px"
                      align="center"
              >
                  <template slot-scope="scope">
                      <el-button class="el-icon-delete" type="danger" @click="deleteClick(scope.$index)"></el-button>
                  </template>
              </el-table-column>
          </el-table>
        </el-form>
    </div>
</template>

<script>
    import floorModel from "../../../../model/project/floor";
    import floorRelevanceModel from "../../../../model/project/floorRelevance";
    import productModel from "../../../../model/project/product";

    import {Notification} from "element-ui";

    export default {
        name: "RelevanceAddBatch",
        props: ['buildingId', 'projectId'],
        data() {
            return {
                productList: [],
                floorBefore: [],
                floorAfter: [],
                batchEditForm: {
                    projectId: '',
                    buildingId: '',
                    floorIdBefore: '',
                    floorIdAfter: '',
                    floorRelevances: '',
                },
                batchEditFormRules: {},
                model:{
                  rules:{
                    productId:{ required: true, message: '请输入构件名称',trigger:"change"},
                    num:{ 
                      validator(rule,value,callback){               
                        if(Number.isInteger(Number(value)) && Number(value) > 0){                
                          callback();
                        }else{                 
                          callback(new Error("请输入有效数字"));               
                        }             
                      },             
                      trigger: 'blur',      
                    }
                  },
                  relevanceData: [],
                },
                
            }
        },
        mounted() {
            this.info()
        },
        methods: {
            deleteClick(index) {
                this.model.relevanceData.splice(index,1)
            },
            addClick() {
                var relevance = {
                    productName: '',
                    productId: '',
                    num: '1',
                }
                this.model.relevanceData.push(relevance);
            },
            info() {
                floorModel.findByBuildingIdAndProjectId(this.buildingId, this.projectId).then((data) => {
                    this.floorBefore = data.entity
                    this.floorAfter = data.entity
                })

            },
            focusProduct() {
                productModel.findProductsByProjectId(this.projectId).then((data) => {
                    this.productList = data.entity
                })
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    //  this.copyEditForm
                    if(valid){
                      const loading = this.$loading({
                          lock: true,
                          text: 'Loading',
                          spinner: 'el-icon-loading',
                          background: 'rgba(0, 0, 0, 0.7)'
                      });
                      this.batchEditForm.projectId = this.projectId
                      this.batchEditForm.buildingId = this.buildingId
                      this.batchEditForm.floorRelevances = this.model.relevanceData
                      floorRelevanceModel.addBatchRelevanceByFloor(this.batchEditForm).then((data) => {
                          if (data.status == 'failed') {
                              Notification({
                                  message: data.msg,
                                  type: 'warning'
                              })
                              loading.close()
                              return
                          }
                          Notification({
                              message: '保存成功',
                              type: 'success'
                          })
                          loading.close();
                          this.$emit('batch-edit-finish')
                      })
                    }
                })
            },
            close() {
                this.$emit('batch-edit-cancel')
            },
        }
    }
</script>

<style scoped>

</style>
