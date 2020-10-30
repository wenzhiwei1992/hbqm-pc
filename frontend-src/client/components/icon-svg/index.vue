<template>
  <svg
    :class="getClassName"
    :width="width"
    :height="height"
    aria-hidden="true">
    <use :xlink:href="getName"></use>
  </svg>
</template>

<script>
  export default {
    name: 'icon-svg',
    props: {
      name: {
        type: String,
        required: true
      },
      className: {
        type: String
      },
      width: {
        type: String
      },
      height: {
        type: String
      }
    },
    computed: {
      getName () {
        return `#iconfont-${this.name}`
      },
      getClassName () {
        return [
          'iconfont-svg',
          `iconfont-svg__${this.name}`,
          this.className && /\S/.test(this.className) ? `${this.className}` : ''
        ]
      }
    }
  }
</script>

<style>
  .icon-svg {
    width: 1em;
    height: 1em;
    fill: currentColor;
    overflow: hidden;
  }
  .remix {
    width: 24px;
    height: 24px;
    fill: #333;
  }
</style>
