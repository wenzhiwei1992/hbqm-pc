<template>
    <div>
        <el-table
                :data="datas"
                @selection-change="handleSelectionChange"
                size="mini"
                stripe
                border
                height="450"
                style="width: 100%"
                row-key="id"
        >
            <el-table-column
                    type="selection">
            </el-table-column>
            <el-table-column
                    type="index"
                    label="序号">
            </el-table-column>
            <el-table-column
                    prop="rawMaterialCode"
                    label="原料编码">
            </el-table-column>
            <el-table-column
                    prop="rawMaterialName"
                    label="原料">
            </el-table-column>
            <el-table-column
                    prop="specification"
                    label="规格">
            </el-table-column>
            <el-table-column
                    prop="companyName"
                    label="单位">
            </el-table-column>
            <el-table-column
                    prop="unitPrice"
                    label="单价">
            </el-table-column>
            <el-table-column
                    prop="taxRate"
                    label="税率">
            </el-table-column>
            <el-table-column
                    prop="number"
                    label="采购总量">
            </el-table-column>
            <el-table-column
                    prop="inputNumAll"
                    label="已入库量">
            </el-table-column>
            <el-table-column
                    prop="lastNumAll"
                    label="待入库量">
            </el-table-column>
        </el-table>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="pushData()">确 定</el-button>
        </div>
    </div>
</template>

<script>
    import serverConfig from '../../../../../server.config'
    import clientModel from '../../../../model/client-model'
    import {Notification} from 'element-ui'
    import moment from "moment";
    export default {
        props:['editForm','mode'],
        mounted(){
           this.loadInfo();
        },
        methods:{
            editFormCancel(){
                this.$emit('edit-cancel')
            },
            loadInfo(){
                clientModel.ax.get('/purchase/getPurchaseDetailsBySupplierId/'+this.editForm.supplier).then(data=>{
                    this.datas = data.entity;
                    console.log("this.datas",this.datas)
                })
            },
            pushData(){
                // add data
                if(this.selected.length == 0 ){
                    this.$message.warning(this.GLOBAL.MSG.SELECTED_ERR)
                    return;
                }
                this.editForm.inputDetails = [];
                for (let i = 0; i < this.selected.length; i++) {
                    const e = this.selected[i];
                    this.editForm.inputDetails.push(e);
                }
                this.$emit('edit-finish',this.editForm,this.mode)
            },
            handleSelectionChange(selected){
                this.selected=selected
            },

        },
        
        data(){
            return{
                headers: {
                    'Content-Type':'multipart/form-data;charset=UTF-8',
                },
                upLoadData:null,
                file:null,
                eidtFormRules: {
                    
                },
                loading:false,
                selected:[],
                datas:[],

            }
        }

    }
</script>

<style scoped>
    .divType{
        display: block;
        border: 1px lavender solid;
        margin-top: -10px;
        padding-top: 10px;
    }
    .divType2{
        border: 1px lavender solid;
        margin-top: 10px;
        padding-top: 10px;
    }
    .divType3{
        border: 1px lavender solid;
        margin-top: 10px;
        padding-top: 10px;
    }
    .divType4{
        border: 1px lavender solid;
        margin-top: 10px;
        padding-top: 10px;
    }
    .dialog-footer{
      margin-top:10px;
    }
</style>