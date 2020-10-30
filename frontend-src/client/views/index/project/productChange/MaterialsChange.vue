<template>
    <div>
        <el-table
                :data="materialsChanges"
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
    import materialsChangeModel from '../../../../model/project/materialsChange'

    export default {
        data() {
            return {
                eidtFormRules: '',
                materialsChanges: [],
            }
        },
        props: ['productId', 'bomVerId'],
        mounted() {
            this.info()
        },
        methods: {
            info() {
                let productChangeQuery = {
                    productId: this.productId,
                    bomVerId: this.bomVerId
                }
                console.log('productChangeQuery------>',productChangeQuery)
                materialsChangeModel.findAllByQuery(productChangeQuery).then((data) => {
                    this.materialsChanges = data.entity
                })
            },
        },

    }
</script>

<style scoped>

</style>
