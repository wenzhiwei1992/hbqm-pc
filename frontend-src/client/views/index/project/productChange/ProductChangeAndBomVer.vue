<template>
    <div>
        <el-table
                :data="productChanges"
                size="mini"
                stripe
                ref="handleClickRowSelection"
                height="100%"
                style="width: 100%"
        >
            <el-table-column
                    type="index"
                    label="序号"

            >
            </el-table-column>
            <el-table-column
                    prop="changeData"
                    label="变更字段"

                    sortable
            >
            </el-table-column>
            <el-table-column
                    prop="oldValue"
                    label="变更前值"

                    sortable
            >
            </el-table-column>
            <el-table-column
                    prop="newValue"
                    label="变更后值"

                    sortable
            >
            </el-table-column>



        </el-table>
    </div>
</template>

<script>
    import productChangeModel from '../../../../model/project/productChange'

    export default {
        name: "ProductChangeAndBomVer",
        props: ['productId', 'bomVerId'],
        data() {
            return {
                productChanges: [],
            }
        },
        mounted() {
          this.info()
        },
        methods: {
            info(){
                let productChangeQuery = {
                    productId: this.productId,
                    bomVerId: this.bomVerId
                }
                productChangeModel.findAllByQuery(productChangeQuery).then((data) => {
                    this.productChanges = data.entity
                })
            }

        },

    }
</script>

<style scoped>

</style>
