<template></template>
<script>
    import Vue from 'vue'
    import Tinymce from './tinymce/index'
    Vue.component('tinymce', Tinymce)

    export default {
        props: ['previewPageData', 'formData'],
        data() {
            return {
                renderDom: ''
            }
        },
        watch: {
            previewPageData: {
                immediate: true,
                deep: true,
                handler(newval) {
                    const pageData = newval.data
                    if (pageData) {
                        this.newVue('', pageData.js, pageData.html)
                    }
                }
            },
            formData(newVal) {
                // console.log('formData',newVal)
            }
        },
        mounted() {},
        methods: {
            newVue(attrs, main, html) {
                const self = this;
                main = eval(`(${main})`)
                main.template = `<div id="containerForm" class="preview_container_form">${html}</div>`
                new Vue({
                    components: {
                        child: main
                    },
                    data() {
                        return {
                            visible: true
                        }
                    },
                    watch: {},
                    methods: {
                        updatePreviewData(event) {
                            self.$emit('update-preview', event)
                        },
                    },
                    template: `<child ${attrs} />`
                }).$mount('#containerForm')
            },
        },
    }
</script>

<style lang="scss">
    @import '@/utils/generator/com-mixin.scss';
    @import '@/assets/styles/element-ui.scss';
</style>