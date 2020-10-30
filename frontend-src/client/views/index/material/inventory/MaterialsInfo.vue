<template>
    <div>
        <el-table
                :data="arr"
                size="mini"
                stripe
                border
                height="400"
                style="width:100%"
                router-link
        >
            <el-table-column
                    sortable
                    prop="materialStorageId"
                    label="物料编码"
                    width="120">
            </el-table-column>
           <el-table-column
                    sortable
                    prop="materialName"
                    label="物料名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="materialSpecification"
                    label="物料规格"
                    width="120">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="materialUnitName"
                    label="计量单位"
                    width="120">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="checkout"
                    label="是否为免检物料"
                    width="140">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="purchasePeriod"
                    label="采购周期"
                    width="120">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="dateCreated"
                    :formatter="formatterDate"
                    label="创建时间"
                    width="140">
            </el-table-column>
            <el-table-column
                    sortable
                    prop="dateModified"
                    :formatter="formatterDate"
                    label="修改时间"
                    width="140">
            </el-table-column>

        </el-table>
    </div>
</template>

<script>
    import moment from "moment";
    export default {
        name: "MaterialsInfo",
        data(){
            return{
                materialStorages:this.materialsChildren,
                selected:[],
            }
        },
        props: ['materialsChildren','arr'],
        mounted(){
        },
        methods:{
            formatterDate(row, column, cellValue, index) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            handleSelectionChange(selected) {
                this.selected = selected
            },

        }    }
</script>

<style scoped>

</style>