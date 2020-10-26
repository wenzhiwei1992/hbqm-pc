<template>
    <div
      class="currency-style-box"
    >
      <el-container>
        <el-header ref="headOperationBar">
          <el-row style="display:none;">
            <my-button-group :model="buttonData"></my-button-group>
          </el-row>
        </el-header>
        <el-main
          ref="mainContent"
          :style="`height:${this.$store.state.mainContentHeight}px`"
        >
          <gantt class="left-container" style="height:100%;" :tasks="tasks"></gantt>
        </el-main>
        <el-footer ref="footOperationBar"></el-footer>
    </el-container>
    </div>
</template>

<script>
    import Gantt from '@/components/Gantt/Gantt.vue';
    export default {
      name: 'app',
      components: {Gantt},
      data () {
        return {
          tasks: {
            data: [
              {id: 1, text: '任务1', start_date: '2020-01-17', duration: 5, progress: 0.6},
              {id: 2, text: '任务2', start_date: '2020-01-20', duration: 3, progress: 0.9},
              {id: 3, text: '任务3', start_date: '2020-01-22', duration: 2, progress: 0.3},
              {id: 4, text: '任务4', start_date: '2020-01-25', duration: 1, progress: 0.4},
              {id: 5, text: '任务5', start_date: '2020-01-26', duration: 4, progress: 0.5},
              {id: 6, text: '任务6', start_date: '2020-01-22', duration: 6, progress: 0.6},
              {id: 7, text: '任务7', start_date: '2020-01-21', duration: 7, progress: 0.7},
              {id: 8, text: '任务8', start_date: '2020-01-19', duration: 5, progress: 0.8}
            ],
            links: [
              {id: 1, source: 4, target: 1, type: '0'},
              {id: 2, source: 3, target: 2, type: '0'},
              {id: 3, source: 5, target: 6, type: '0'},
              {id: 4, source: 7, target: 8, type: '0'},
              {id: 5, source: 8, target: 1, type: '0'},
              {id: 6, source: 2, target: 6, type: '0'},
              {id: 7, source: 3, target: 4, type: '0'},
              {id: 8, source: 5, target: 3, type: '0'},
            ]
          },
          buttonData:[
            {
              name:'新建'
            }
          ]
        }
      }
    }
</script>
