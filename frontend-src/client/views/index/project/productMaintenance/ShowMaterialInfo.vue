<template>
    <div>
        <el-table
                :data="editFormShowMaterialInfo"
                size="mini"
                stripe
                border
                height="250"
                style="width:100%"
        >
            <el-table-column
                    prop="materialStorageId"
                    label="原料编码"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="name"
                    label="原料名称"
                    >
            </el-table-column>
            <el-table-column
                    prop="specification"
                    label="原料规格"
                    >
            </el-table-column>
            <el-table-column
                    prop="unit"
                    label="单位"
                    >
                <template slot-scope="scope">
                    <span v-if="scope.row.num==0 && scope.row.unit==null">kg</span>
                    <span v-else>{{scope.row.unit}}</span>
                </template>
            </el-table-column>
            <el-table-column
                    prop="num"
                    label="数量"
                   >
                <template slot-scope="scope">
                    <span v-if="scope.row.num==0 && scope.row.materialStorageId.indexOf('GC')!=-1"
                          placeholder="请输入内容">{{scope.row.length}}</span>
                    <span v-else
                          placeholder="请输入内容">{{scope.row.num}}</span>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
    export default {
        name: "ShowMaterialInfo",
        data() {
            return {
                eidtFormRules: '',
            }
        },
        props: ['editFormShowMaterialInfo'],
        mounted() {

        },
        methods: {},

    }
</script>

<style scoped>

</style>