<template>
    <div>
        <el-table
                :data="productInfo"
                size="mini"
                stripe
                border
                height="250"
                style="width:100%"
        >
            <el-table-column
                    prop="productNo"
                    label="构件编码"
                    width="130">
            </el-table-column>
            <el-table-column
                    prop="productName"
                    label="构件名称"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="productTypeName"
                    label="构件类型"
                    width="140">
            </el-table-column>
            <el-table-column
                    prop="projectName"
                    label="项目名称"
                    width="120"
            >
            </el-table-column>

            <el-table-column
                    prop="moldName"
                    label="模具"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="productVol"
                    label="构件体积(m³)"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="productThick"
                    label="构件厚度m"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="productLen"
                    label="构件长度m"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="productHeight"
                    label="构件宽度m"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="productAcreage"
                    label="构件面积m²"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="productVol"
                    label="构件重量t"
                    width="120">
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
    import productModel from '../../../model/project/product'

    export default {
        name: "CheckProductInfo",
        data() {
            return {
                productInfo: [],
            }
        },
        mounted() {
            this.info()
        },
        methods: {
            info() {
                let productId = this.$route.query.id
                productModel.findProductByProductId(productId).then((data) => {
                    this.productInfo = []
                    this.productInfo.push(data.entity)
                })
            },
        }
    }
</script>

<style scoped>

</style>
