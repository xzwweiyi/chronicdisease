<template>
    <div id="app">
        <router-view />
    </div>
</template>

<script>
    export default {
        name: 'App',
        metaInfo() {
            return {
                title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
                titleTemplate: title => {
                    return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
                }
            }
        }
    }
</script>
<style lang='scss'>
    body,
    html {
        margin: 0;
        padding: 0;
        background: #fff;
        -moz-osx-font-smoothing: grayscale;
        -webkit-font-smoothing: antialiased;
        text-rendering: optimizeLegibility;
        font-family: -apple-system, BlinkMacSystemFont, Segoe UI, Helvetica, Arial,
            sans-serif, Apple Color Emoji, Segoe UI Emoji;
    }

    input,
    textarea {
        font-family: -apple-system, BlinkMacSystemFont, Segoe UI, Helvetica, Arial,
            sans-serif, Apple Color Emoji, Segoe UI Emoji;
    }

    * {
        margin: 0;
        padding: 0;
    }
</style>