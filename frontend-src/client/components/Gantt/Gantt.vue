<template>
  <div ref="gantt"></div>
</template>

<script>
import {gantt} from 'dhtmlx-gantt'
import 'dhtmlx-gantt/codebase/locale/locale_cn'
export default {
  name: 'gantt',
  props: {
    tasks: {
      type: Object,
      default () {
        return {data: [], links: []}
      }
    }
  },
 
  mounted() {
    gantt.config.xml_date = "%Y-%m-%d";
    gantt.config.autosize = "xy";
    // gantt.config.subscales = [
    //   {
    //     unit: 'year',
    //     step: 1,
    //     date: '%Y'
    //   }
    // ]
    gantt.init(this.$refs.gantt);
    gantt.parse(this.$props.tasks);
  }
}
</script>

<style scoped>
  @import "~dhtmlx-gantt/codebase/dhtmlxgantt.css";
</style>
