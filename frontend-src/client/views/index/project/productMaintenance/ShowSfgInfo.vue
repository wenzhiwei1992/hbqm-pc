<template>
    <div>
        <el-table
                :data="productSfgData"
                size="mini"
                stripe
                border
                height="250"
                style="width:100%"
        >
            <el-table-column
                    type="index"
                    label="序号"

            >
            </el-table-column>
            <el-table-column
                    prop="sfgNo"
                    label="半成品编码"
            >
            </el-table-column>
            <el-table-column
                    prop="sfgName"
                    label="半成品名称"
            >
            </el-table-column>
            <el-table-column
                    prop="sfgSpecification"
                    label="半成品规格"
            >
            </el-table-column>
            <el-table-column
                    prop="sfgLen"
                    label="半成品长度"
            >
            </el-table-column>
            <el-table-column
                    prop="rebarSpecifications"
                    label="钢筋规格"
            >
            </el-table-column>
            <el-table-column
                    prop="categoryName"
                    label="半成品类型"
            >
            </el-table-column>
            <el-table-column
                    prop="unitName"
                    label="计量单位"
            >
            </el-table-column>
            <el-table-column
                    prop="num"
                    label="数量"
            >
            </el-table-column>

        </el-table>
    </div>
</template>

<script>
    import productSfgModel from '../../../../model/project/productSfg'

    export default {
        name: "ShowSfgInfo",
        props: ["productSfgId"],
        data() {
            return {
                productSfgData: [],
            }
        },
        mounted() {
            this.list()
        },
        methods: {
            list() {
                productSfgModel.findPrSfgByProductId(this.productSfgId).then((data) => {
                    this.productSfgData = data.entity
                })
            }

        }
    }
</script>

<style scoped>

</style>
